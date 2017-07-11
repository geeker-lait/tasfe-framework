package com.tasfe.framework.logagent.logback.monitor;

import com.tasfe.framework.logagent.logback.utils.ParseLogbackXml;
import com.tasfe.framework.logagent.logback.utils.PropertyUtils;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.tasfe.framework.logagent.logback.utils.ParseLogbackXml.getFilePathRegex;

/**
 * Created by lait on 2017/6/2.
 */
@Component
public class KafkaMonitor {
    private Logger logger = LoggerFactory.getLogger(KafkaMonitor.class);

    private static boolean use_flag = false;
    private static boolean old_use_flag = false;
    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String topicName = "Tasfe.Syslog.Monitor";
    private static String rewriteTopicName = "tasfe.log4j";
    private static String servers = "logcenter-kafka.01.service.prod.uc:9092,logcenter-kafka.02.service.prod.uc:9092,logcenter-kafka.03.service.prod.uc:9092";
    private static Producer<String, String> producer = null;
    private static String logFileDirPath = "";


    @PostConstruct
    public void init() {
        System.out.println(">>>>>>>>>>初始化监控类<<<<<<<<<<<<");
        Properties properties = PropertyUtils.getProperties();
        if (properties != null && !properties.isEmpty()) {
            servers = properties.getProperty("logcenter.bootstrap.servers", servers);
            rewriteTopicName = properties.getProperty("consumer.topicName", rewriteTopicName);
        }
        System.out.println("[logcenter.bootstrap.servers -> " + servers + "]  [consumer.topicName -> " + rewriteTopicName + "]");
        //读取配置文件
        ParseLogbackXml.getAttribute();
        /*for (String key : ParseLogbackXml.resourceMap.keySet()) {
            System.out.println(key + "\t" + ParseLogbackXml.resourceMap.get(key));
        }*/
        logFileDirPath = getFilePathRegex();
        System.out.println("log dir >>> " + logFileDirPath);
        intervalTimer();
    }

    public void intervalTimer() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        //每5秒进行检查 检测kafka状态是否由false变为true,启动复写机制
        service.scheduleAtFixedRate(kafkaTestProducer(), 0, 10, TimeUnit.SECONDS);
    }


    public Runnable kafkaTestProducer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Properties props = new Properties();
                props.put("bootstrap.servers", servers);
                props.put("acks", "all");
                props.put("metadata.fetch.timeout.ms", "3000");
                props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                try {
                    if (producer == null) {
                        producer = new KafkaProducer<>(props);
                    }
                    producer.send(new ProducerRecord<>(topicName, "validate message--" + sf.format(new Date())), new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception exception) {
                            if (exception != null) {
                                checkFalse();
                                exception.printStackTrace();
                            } else {
                                checkTrue();
                                if (use_flag && !old_use_flag) {
                                    //说明useflag由false变为true，启动复写程序
                                    System.out.println("复写程序启动.....");
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            rewriteFileToKafka(logFileDirPath);
                                        }
                                    }).start();
                                }
                            }
                            old_use_flag = use_flag;
//                            System.out.println("use_flag = " + use_flag + "\t" + "old_use_flag = " + old_use_flag);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        };
        return runnable;
    }

    public static void checkFalse() {
        use_flag = false;
    }

    public static void checkTrue() {
        use_flag = true;
    }

    public void rewriteFileToKafka(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                String s = filelist[i];
                //需要处理的文件以logback.xml中的suffix属性结尾
                if (!s.endsWith(ParseLogbackXml.resourceMap.get("SUFFIX"))) {
                    continue;
                }
                String fileName = filePath + "/" + s;
                File file1 = new File(fileName);
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file1));
                    String tempString = null;
                    System.out.println("复写文件：" + fileName);
                    while ((tempString = reader.readLine()) != null) {
                        //重写文件中的数据到kafka中
                        producer.send(new ProducerRecord<>(rewriteTopicName, tempString));
                        producer.flush();
//                        System.out.println(tempString);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
                //清空文件中的内容
                ParseLogbackXml.delFileContent(fileName);
                System.out.println("清空文件：" + fileName);
                /*//重命名日志文件
                if (!ParseLogbackXml.renameFile(fileName, fileName + ".finish")) {
                    //重命名日志文件失败，清空其中的内容
                    ParseLogbackXml.delFileContent(fileName);
                }else {
                    //删除已经补偿写入的文件
                    delFinishFile(logFileDirPath);
                }*/
            }
            System.out.println("复写程序完成.....");
        }
    }

    public void delFinishFile(String dirPath) {
        File file = new File(dirPath);
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                String s = filelist[i];
                //需要处理的文件以logback.xml中的suffix属性结尾
                if (!s.endsWith("finish")) {
                    continue;
                }
                String fileName = dirPath + "/" + s;
                if (ParseLogbackXml.deleteFile(fileName)) {
                    System.out.println(fileName + " deleted success.");
                }
            }
        }
    }
}

package com.tasfe.framework.logagent.logback.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuxueyou on 2017/6/5.
 */
public class ParseLogbackXml {
    public static Map<String, String> resourceMap = new HashMap<>();
    private static Map<String, String> appenderPatternMap = null;

    public static Map getAppenderPatternMap() {
        if (appenderPatternMap == null) {
            appenderPatternMap = new HashMap();
            InputStream in = null;
                in = ParseLogbackXml.class.getClassLoader().getResourceAsStream("tasfe-logback.xml");
                Document doc = null;
                try {
                    doc = Jsoup.parse(in, "UTF-8", "");
                    Elements properties = doc.select("appender");

                    for (Element property : properties) {
                        appenderPatternMap.put(property.attr("name"), property.select("pattern").text());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null)
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        return appenderPatternMap;
    }

    public static void getAttribute() {
        InputStream in = null;
        if (resourceMap.isEmpty()) {
            in = ParseLogbackXml.class.getClassLoader().getResourceAsStream("tasfe-logback.xml");
            Document doc = null;
            try {
                doc = Jsoup.parse(in, "UTF-8", "");
                Elements properties = doc.select("property");

                for (Element property : properties) {
                    resourceMap.put(property.attr("name"), property.attr("value"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null)
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    /**
     * 把文件中的${}中的内容，用System.getProperty()替换
     * 例如：${user.dir}/cc/${user.home}/aa
     * 转换后：E:/Workspaces/eagle2.0/Project/jar-fx-logagent/cc/C:/Users/wuxueyou/aa
     *
     * @param filePath
     * @return
     */
    public static String parseFilePath(String filePath) {

        String tmpfilePath = filePath;

        String filePattern = "";
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(tmpfilePath);
        while (matcher.find()) {
            filePattern = matcher.group(1);
            tmpfilePath = Pattern.compile("\\$\\{(.*?)\\}").matcher(tmpfilePath).replaceFirst(System.getProperty(filePattern).replaceAll("\\\\", "/"));
//            System.out.println(tmpfilePath);
            matcher = pattern.matcher(tmpfilePath);
        }
        return tmpfilePath;
    }

    /**
     * 通过读取logbackxml，得到文件夹名称
     *
     * @return
     */
    public static String getFilePathRegex() {
        String filePath = ParseLogbackXml.resourceMap.get("LOG_HOME");
        String realPathRegex = ParseLogbackXml.parseFilePath(filePath);
        return realPathRegex;
    }

    /**
     * 重命名文件
     *
     * @param file
     * @param toFile
     */
    public static boolean renameFile(String file, String toFile) {

        boolean result = false;
        File toBeRenamed = new File(file);
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            System.out.println("File does not exist: " + file);
            return false;
        }

        File newFile = new File(toFile);

        //修改文件名
        if (toBeRenamed.renameTo(newFile)) {
            result = true;
            System.out.println(file + " has been renamed to " + toFile);
        } else {
            System.out.println("Error renmaing file");
        }
        return result;
    }

    public static void delFileContent(String filename) {
        File file = new File(filename);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }

    }

    /**
     * 删除单个文件
     *
     * @param sPath
     * @return
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

}

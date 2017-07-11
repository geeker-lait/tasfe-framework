package com.tasfe.framework.logagent.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import com.tasfe.framework.logagent.logback.delivery.FailedDeliveryCallback;
import com.tasfe.framework.logagent.logback.utils.PropertyUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @since 0.0.1
 */
public class KafkaAppender<E> extends KafkaAppenderConfig<E> {

    /**
     * Kafka clients uses this prefix for its slf4j logging.
     * This appender defers appends of any Kafka logs since it could cause harmful infinite recursion/self feeding effects.
     */
    private static final String KAFKA_LOGGER_PREFIX = "org.apache.kafka.clients";

    private LazyProducer lazyProducer = null;
    private final AppenderAttachableImpl<E> aai = new AppenderAttachableImpl<E>();
    private final ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<E>();

    private static Lock commitLock = new ReentrantLock();
    private static ConcurrentLinkedQueue logQueue = new ConcurrentLinkedQueue();
    private static String bootstrapServers = "logcenter-kafka.01.service.prod.uc:9092,logcenter-kafka.02.service.prod.uc:9092,logcenter-kafka.03.service.prod.uc:9092";
    private static int batchNum = 100;
    private static int maxCommitInterval = 10000;

    private final FailedDeliveryCallback<E> failedDeliveryCallback = new FailedDeliveryCallback<E>() {
        @Override
        public void onFailedDelivery(E evt, Throwable throwable) {
            aai.appendLoopOnAppenders(evt);
        }
    };

    public KafkaAppender() {
        // setting these as config values sidesteps an unnecessary warning (minor bug in KafkaProducer)
        addProducerConfigValue(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        addProducerConfigValue(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        Properties properties = PropertyUtils.getProperties();
        if (properties != null && !properties.isEmpty()) {
            addProducerConfigValue(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getProperty("logcenter.bootstrap.servers", bootstrapServers));
            addProducerConfigValue(ProducerConfig.ACKS_CONFIG, properties.getProperty("logcenter.acks", "0"));
            addProducerConfigValue(ProducerConfig.METADATA_FETCH_TIMEOUT_CONFIG, properties.getProperty("logcenter.metadata.fetch.timeout.ms", "1000"));

            batchNum = Integer.parseInt(properties.getProperty("logcenter.batchNum", "100"));
            maxCommitInterval = Integer.parseInt(properties.getProperty("logcenter.maxCommitInterval", "10000"));
        }
    }


    @Override
    public void doAppend(E e) {
        ensureDeferredAppends();
        if (e instanceof ILoggingEvent && ((ILoggingEvent) e).getLoggerName().startsWith(KAFKA_LOGGER_PREFIX)) {
            deferAppend(e);
        } else {
            super.doAppend(e);
        }
    }

    @Override
    public void start() {
        // only error free appenders should be activated
        if (!checkPrerequisites()) return;

        lazyProducer = new LazyProducer();

        super.start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                commitLock.lock();
                try {
                    batchAppend(0);
                } finally {
                    commitLock.unlock();
                }
            }
        }, 1000, maxCommitInterval);
    }

    @Override
    public void stop() {
        super.stop();
        if (lazyProducer != null && lazyProducer.isInitialized()) {
            try {
                lazyProducer.get().close();
            } catch (KafkaException e) {
                this.addWarn("Failed to shut down kafka producer: " + e.getMessage(), e);
            }
            lazyProducer = null;
        }
    }

    @Override
    public void addAppender(Appender<E> newAppender) {
        aai.addAppender(newAppender);
    }

    @Override
    public Iterator<Appender<E>> iteratorForAppenders() {
        return aai.iteratorForAppenders();
    }

    @Override
    public Appender<E> getAppender(String name) {
        return aai.getAppender(name);
    }

    @Override
    public boolean isAttached(Appender<E> appender) {
        return aai.isAttached(appender);
    }

    @Override
    public void detachAndStopAllAppenders() {
        aai.detachAndStopAllAppenders();
    }

    @Override
    public boolean detachAppender(Appender<E> appender) {
        return aai.detachAppender(appender);
    }

    @Override
    public boolean detachAppender(String name) {
        return aai.detachAppender(name);
    }

    @Override
    protected void append(E e) {
        logQueue.add(e);
        commitLock.lock();
        try {
            batchAppend(batchNum);
        } finally {
            commitLock.unlock();
        }
    }

    private void batchAppend(int threshold) {
        if (logQueue.size() > threshold) {
            while (!logQueue.isEmpty()) {
                E event = (E) logQueue.poll();
                final byte[] payload = encoder.doEncode(event);
                final byte[] key = keyingStrategy.createKey(event);
                final ProducerRecord<byte[], byte[]> record = new ProducerRecord<byte[], byte[]>(topic, key, payload);
                deliveryStrategy.send(lazyProducer.get(), record, event, failedDeliveryCallback);
            }
            logQueue.clear();
        }
    }

    protected Producer<byte[], byte[]> createProducer() {
        return new KafkaProducer<byte[], byte[]>(new HashMap<String, Object>(producerConfig));
    }

    private void deferAppend(E event) {
        queue.add(event);
    }

    // drains queue events to super
    private void ensureDeferredAppends() {
        E event;

        while ((event = queue.poll()) != null) {
            super.doAppend(event);
        }
    }

    /**
     * Lazy initializer for producer, patterned after commons-lang.
     *
     * @see <a href="https://commons.apache.org/proper/commons-lang/javadocs/api-3.4/org/apache/commons/lang3/concurrent/LazyInitializer.html">LazyInitializer</a>
     */
    private class LazyProducer {

        private volatile Producer<byte[], byte[]> producer;

        public Producer<byte[], byte[]> get() {
            Producer<byte[], byte[]> result = this.producer;
            if (result == null) {
                synchronized (this) {
                    result = this.producer;
                    if (result == null) {
                        this.producer = result = this.initialize();
                    }
                }
            }

            return result;
        }

        protected Producer<byte[], byte[]> initialize() {
            Producer<byte[], byte[]> producer = null;
            try {
                producer = createProducer();
            } catch (Exception e) {
                addError("error creating producer", e);
            }
            return producer;
        }

        public boolean isInitialized() {
            return producer != null;
        }
    }

}

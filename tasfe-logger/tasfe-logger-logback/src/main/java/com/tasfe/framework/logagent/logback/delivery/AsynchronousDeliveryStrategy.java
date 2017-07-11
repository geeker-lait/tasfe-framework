package com.tasfe.framework.logagent.logback.delivery;

import org.apache.kafka.clients.producer.*;

/**
 * @since 0.0.1
 */
public class AsynchronousDeliveryStrategy implements DeliveryStrategy {

    @Override
    public <K, V, E> boolean send(Producer<K, V> producer, ProducerRecord<K, V> record, final E event,
                                  final FailedDeliveryCallback<E> failedDeliveryCallback) {
        try {
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        failedDeliveryCallback.onFailedDelivery(event, exception);
                    }
                }
            });
            return true;
        } catch (BufferExhaustedException e) {
            failedDeliveryCallback.onFailedDelivery(event, e);
            return false;
        }
    }

}

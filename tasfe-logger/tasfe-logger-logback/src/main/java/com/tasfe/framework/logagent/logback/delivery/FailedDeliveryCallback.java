package com.tasfe.framework.logagent.logback.delivery;

/**
 * @since 0.0.1
 */
public interface FailedDeliveryCallback<E> {
    void onFailedDelivery(E evt, Throwable throwable);
}

package com.tasfe.framework.netty.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;

import com.tasfe.framework.netty.ObjectLocal;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ThreadLocalAsyncExecutor implements AsyncTaskExecutor {

    private static final ThreadLocal<Object> responses = new ThreadLocal<>();
    private static final ObjectLocal<Runnable> futures = new ObjectLocal<>();
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Runnable task) {
        logger.info("execute " + task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        logger.info("execute " + task + ", " + startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        logger.info("submit runnable {}", task);
        FutureTask<Object> future = new FutureTask<>(task, null);
        Object response = responses.get();
        responses.remove();
        futures.set(response, future);
        return future;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        logger.info("submit callable " + task);
        return null;
    }

    public static void setResponse(Object response) {
        responses.set(response);
    }

    public static Runnable getAndRemoveAsyncRunnable(Object object) {
        Runnable runnable = futures.get(object);
        futures.remove(runnable);
        return runnable;
    }
}

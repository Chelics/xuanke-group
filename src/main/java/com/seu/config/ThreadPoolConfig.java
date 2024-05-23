package com.seu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 线程池配置类
 */
@Configuration
public class ThreadPoolConfig {

    private final int corePoolSize = 10; // 核心线程数
    private final int maximumPoolSize = 20; // 最大线程数，适当增大以适应高峰需求
    private final long keepAliveTime = 60L; // 空闲线程存活时间，单位为秒
    private final TimeUnit timeUnit = TimeUnit.SECONDS; // 时间单位
    private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(100); // 工作队列

    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                workQueue,
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略：直接拒绝新任务并抛出异常
        );
    }
}



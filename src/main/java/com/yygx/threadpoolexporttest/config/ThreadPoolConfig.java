package com.yygx.threadpoolexporttest.config;

import java.util.concurrent.*;


/**
 * <p>
 * 注释：
 * </p>
 * Since: 2024/9/20 <p>
 * Author: <a href="https://github.com/YFBY-Java" target="_blank">吟风抱月</a>
 */
public class ThreadPoolConfig {

    // 核心线程数
    private static final int CORE_POOL_SIZE = 5;

    // 最大线程数
    private static final int MAX_POOL_SIZE = 10;

    // 空闲线程的存活时间  60秒
    private static final long KEEP_ALIVE_TIME = 60L;

    // 队列容量
    private static final int WORK_QUEUE_SIZE = 100;

    public static ExecutorService createThreadPool() {
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, // 秒
                new ArrayBlockingQueue<>(WORK_QUEUE_SIZE),
                Executors.defaultThreadFactory(),   // 默认线程工厂，线程工厂里可以设置线程名之类的
                new ThreadPoolExecutor.CallerRunsPolicy()  // 拒绝策略
        );
    }
}
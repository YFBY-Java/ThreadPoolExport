package com.yygx.threadpoolexporttest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

/**
 * <p>
 * 注释：
 * </p>
 * Since: 2024/9/20 <p>
 * Author: <a href="https://github.com/YFBY-Java" target="_blank">吟风抱月</a>
 */
// 配置类
@Configuration
public class AppConfig {

    @Bean
    public ExecutorService executorService(){
        return ThreadPoolConfig.createThreadPool();  // 使用自定义的线程池
    }
}

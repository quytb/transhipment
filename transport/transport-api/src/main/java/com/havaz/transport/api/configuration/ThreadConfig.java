package com.havaz.transport.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ThreadConfig {
    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }

    @Bean
    @Primary
    TaskScheduler poolScheduleTask() {
        ThreadPoolTaskScheduler config = new ThreadPoolTaskScheduler();
        config.setPoolSize(1);
        config.setThreadNamePrefix("Auto Create Lenh: ");
        return config;
    }
}

package com.ZhongHou.Ecommerce.Payment.configWS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//@Configuration
//@EnableAsync
public class AsyncConfig {

    /**
     * Đây là 'bộ xử lý' (TaskExecutor) mà @Async sẽ tìm kiếm.
     * @Primary nói với Spring: "Đây là lựa chọn mặc định, ưu tiên 1!"
     */
    @Bean
    @Primary // test
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}

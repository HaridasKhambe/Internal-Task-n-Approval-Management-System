package com.taskflowx.config;

import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/*
 Enables @Async in AuditService
 Audit logs run in background (non-blocking)
 Thread pool prevents resource exhaustion
*/

@Configuration
@EnableAsync  // Enables @Async annotation support
public class AsyncConfig {

  @Value("${spring.task.execution.pool.core-size}")
  private int corePoolSize;

  @Value("${spring.task.execution.pool.max-size}")
  private int maxPoolSize;

  @Value("${spring.task.execution.pool.queue-capacity}")
  private int queueCapacity;

  @Bean(name = "taskExecutor")
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    // Core pool: 5 threads always active
    executor.setCorePoolSize(corePoolSize);

    // Max pool: up to 10 threads when needed
    executor.setMaxPoolSize(maxPoolSize);

    // Queue capacity: 100 tasks can wait if all threads busy
    executor.setQueueCapacity(queueCapacity);

    // Thread name prefix for debugging
    executor.setThreadNamePrefix("Async-");

    executor.initialize();
    return executor;
  }

}

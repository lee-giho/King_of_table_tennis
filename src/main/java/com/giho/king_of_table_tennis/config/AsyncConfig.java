package com.giho.king_of_table_tennis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean(name = "expireExecutor")
  public Executor expireExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2); // 코어 스레드 수: 상시 유지되는 스레드 수(작없이 없을 때도 유지)
    executor.setMaxPoolSize(4); // 최대 스레드 수: 큐가 가득 차거나 바쁠 때 늘어날 수 있는 상한
    executor.setQueueCapacity(100); // 큐 용량: 코어 스레드가 바쁠 때 대기시킬 작업 수
    executor.setThreadNamePrefix("expire-"); // 스레드 이름 prefix
    executor.initialize();
    return executor;
  }
}

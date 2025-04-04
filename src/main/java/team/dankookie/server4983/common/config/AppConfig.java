package team.dankookie.server4983.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig {

  @Bean(name = "defaultTaskExecutor", destroyMethod = "shutdown")
  public ThreadPoolTaskExecutor defaultTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(200);
    executor.setMaxPoolSize(300);
    return executor;
  }

  @Bean(name = "messagingTaskExecutor", destroyMethod = "shutdown")
  public ThreadPoolTaskExecutor messagingTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(50);
    executor.setMaxPoolSize(100);
    return executor;
  }
}

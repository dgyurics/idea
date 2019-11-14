package idea.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class TaskPoolConfig implements AsyncConfigurer {

  private static final int MAX_POOL_SIZE = 20;
  private static final int CORE_POOL_SIZE = 10;
  private static final int QUEUE_CAPACITY = 50;

  @Override
  public Executor getAsyncExecutor() {
    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setMaxPoolSize(MAX_POOL_SIZE); // can be modified at runtime
    executor.setCorePoolSize(CORE_POOL_SIZE);
    executor.setQueueCapacity(QUEUE_CAPACITY);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.setThreadNamePrefix("custom-executor");
    executor.initialize();
    return executor;
  }
}
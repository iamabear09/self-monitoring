package jhp.monitoring.api.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPool() {

        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setQueueCapacity(200);
        threadPool.setMaxPoolSize(50);
        threadPool.setCorePoolSize(10);
        threadPool.setThreadNamePrefix("jhp");

        return threadPool;
    }
}

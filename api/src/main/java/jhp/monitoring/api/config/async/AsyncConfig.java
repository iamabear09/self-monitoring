package jhp.monitoring.api.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean(name = "threadPool")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setQueueCapacity(200);
        threadPool.setMaxPoolSize(50);
        threadPool.setCorePoolSize(10);
        threadPool.setThreadNamePrefix("jhp-");
        threadPool.initialize(); //명시적으로 안적어도 Bean 등록 시 Init

        return threadPool;
    }
}

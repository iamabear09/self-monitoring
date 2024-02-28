package jhp.monitoring.api.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    public static final String TOPIC_CREATE_RECORD = "create-record";
    public static final String TOPIC_PATCH_UPDATE_RECORD = "patch-record";

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicCreateRecord() {
        return TopicBuilder.name(TOPIC_CREATE_RECORD)
                .partitions(4)
                .replicas(1)
                .compact()
                .build();
    }


    @Bean
    public NewTopic topicPatchRecord() {
        return TopicBuilder.name(TOPIC_PATCH_UPDATE_RECORD)
                .partitions(4)
                .replicas(1)
                .compact()
                .build();
    }
}

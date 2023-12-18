package com.vancefm.ticketstack.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.vancefm.ticketstack.utils.Constants.KAFKA_TOPIC_NAME;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ticketStackTopic(){
        return TopicBuilder.name(KAFKA_TOPIC_NAME).build();
    }
}

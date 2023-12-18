package com.vancefm.ticketstack.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.vancefm.ticketstack.utils.Constants.KAFKA_TOPIC_NAME;

@Slf4j
@Service
public class KafkaConsumer {

    //Add listeners here

    @KafkaListener(topics = KAFKA_TOPIC_NAME, groupId = "${spring.kafka.consumer.group-id}", clientIdPrefix = "consumer1")
    public void consume(String message){
        //We can do other business logic here too
        log.info(String.format("Message received from Kafka -> %s", message));
    }

}

package com.vancefm.ticketstack.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.vancefm.ticketstack.utils.Constants.KAFKA_TOPIC_NAME;

@Slf4j
@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message){
        log.info(String.format("Sending message to Kafka -> %s", message));
        kafkaTemplate.send(KAFKA_TOPIC_NAME, message);
    }

}

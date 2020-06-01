package io.vizit.vpoc.mq;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaClient {
    private final KafkaTemplate kafkaTemplate;

    public KafkaClient(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, String data) {
        this.kafkaTemplate.send(topic, data);
    }
}

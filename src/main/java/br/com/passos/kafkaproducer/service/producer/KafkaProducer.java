package br.com.passos.kafkaproducer.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic}")
    private String topic;

    public void sendToTopic(Object object) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error on processing object to json", e);
        }

        kafkaTemplate.send(topic, json).whenComplete((recordMetadata, exception) -> {
            log.debug(String.valueOf(recordMetadata));
            if (exception != null) {
                log.error("Error on sending message to topic: {}", topic, exception);
            }
        });
    }

    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void sendToTopic() {
        sendToTopic("Hello World!");
    }
}

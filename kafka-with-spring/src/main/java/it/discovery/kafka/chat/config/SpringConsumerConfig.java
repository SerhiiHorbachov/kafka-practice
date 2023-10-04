package it.discovery.kafka.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@EnableKafka
@Slf4j
public class SpringConsumerConfig {

    @KafkaListener(groupId = "spring-client", topics = "chat_messages")
    public void readChatMessages(ConsumerRecord<String, String> record) {
        log.info("New message {} in the chat from {}", record.value(), record.key());

    }
}

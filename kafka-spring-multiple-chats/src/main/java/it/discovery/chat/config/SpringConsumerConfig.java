package it.discovery.chat.config;

import it.discovery.chat.model.ChatMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@EnableKafka
@Slf4j
public class SpringConsumerConfig {

    @KafkaListener(groupId = "spring-client", topics = "${chat.name}")
    public void readChatMessages(ConsumerRecord<String, ChatMessageVO> record) {
        log.info("New message {} with type {} in the chat {} from {}",
            record.value().text(),
            record.value().messageType(),
            record.value().chat(),
            record.key());

    }
}

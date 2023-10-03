package it.discovery;

import it.discovery.kafka.chat.producer.ChatProducer;
import it.discovery.kafka.chat.producer.SpringChatProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaWithSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaWithSpringApplication.class, args);
    }

    @Bean
    ChatProducer chatProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return new SpringChatProducer(kafkaTemplate);
    }

}

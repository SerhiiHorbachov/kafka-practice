package it.discovery;

import it.discovery.chat.model.ChatMessageVO;
import it.discovery.chat.producer.v2.ChatProducer;
import it.discovery.chat.producer.v2.SpringChatProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaSpringMultipleChatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringMultipleChatsApplication.class, args);
    }

    @Bean
    ChatProducer chatProducer(KafkaTemplate<String, ChatMessageVO> kafkaTemplate, Environment env) {
        return new SpringChatProducer(kafkaTemplate, env);
    }

}

package it.discovery.chat.producer.v2;

import it.discovery.chat.model.ChatMessageVO;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@RequiredArgsConstructor
@Component
public class SpringChatProducer implements ChatProducer<SendResult<String, ChatMessageVO>> {

    private final KafkaTemplate<String, ChatMessageVO> kafkaTemplate;
    private final Environment env;

    @Override
    public Future<SendResult<String, ChatMessageVO>> send(ChatMessage message) {
        ProducerRecord<String, ChatMessageVO> record = new ProducerRecord<>(env.getRequiredProperty("chat.name"),
            message.sender(), new ChatMessageVO(message.text(), message.chat(), message.messageType()));

        return kafkaTemplate.send(record);
    }
}





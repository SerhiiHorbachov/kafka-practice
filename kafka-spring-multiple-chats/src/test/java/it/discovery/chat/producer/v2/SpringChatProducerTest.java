package it.discovery.chat.producer.v2;


import it.discovery.KafkaSpringMultipleChatsApplication;
import it.discovery.chat.model.ChatMessageVO;
import it.discovery.chat.model.MessageType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = KafkaSpringMultipleChatsApplication.class)
public class SpringChatProducerTest extends BaseKafkaTest {

    @Autowired
    private ChatProducer<SendResult<String, ChatMessageVO>> chatProducer;

    @Test
    void send() {
    }


    @Test
    void send_messageValid_success() throws InterruptedException, ExecutionException {

        ChatMessage message = new ChatMessage("Kevin", "Hello from Kevin!", "IT-events", MessageType.TEXT);
        Future<SendResult<String, ChatMessageVO>> future = chatProducer.send(message);
        future.get();
        Thread.sleep(100);
        ConsumerRecords<String, ChatMessageVO> records = readMessages("chat_messages.v2", JsonDeserializer.class.getName());

        Iterator<ConsumerRecord<String, ChatMessageVO>> iterator = records.iterator();
        assertTrue(iterator.hasNext());
        ConsumerRecord<String, ChatMessageVO> record = iterator.next();
        assertEquals(message.sender(), record.key());
        ChatMessageVO value = record.value();
        assertEquals(message.text(), value.text());
        assertEquals(message.chat(), value.chat());


    }

}

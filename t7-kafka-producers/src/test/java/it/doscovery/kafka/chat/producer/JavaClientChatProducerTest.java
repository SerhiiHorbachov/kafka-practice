package it.doscovery.kafka.chat.producer;


import it.discovery.kafka.chat.model.Chat;
import it.discovery.kafka.chat.model.ChatMessage;
import it.discovery.kafka.chat.producer.ChatProducer;
import it.discovery.kafka.chat.producer.JavaClientChatProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class JavaClientChatProducerTest {

    @Container
    KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.1"));
    ChatProducer chatProducer;

    @BeforeEach
    void setup() {
        chatProducer = new JavaClientChatProducer(kafka.getBootstrapServers());
    }

    @Test
    void send_messageValid_success() throws ExecutionException, InterruptedException {
        Chat chat = new Chat("chat-messages");
        ChatMessage message = new ChatMessage("Peter", "Hello from Peter!", chat);
        Future<RecordMetadata> future = chatProducer.send(message);
        future.get();

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, getClass().getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(List.of(chat.name()));
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
            assertTrue(iterator.hasNext());
            ConsumerRecord<String, String> record = iterator.next();
            assertEquals(message.sender(), record.key());
            assertEquals(message.text(), record.value());
            assertEquals(message.chat().name(), record.topic());
        }
    }
}

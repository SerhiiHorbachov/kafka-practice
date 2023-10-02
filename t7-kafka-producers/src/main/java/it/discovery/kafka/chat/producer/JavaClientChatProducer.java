package it.discovery.kafka.chat.producer;

import it.discovery.kafka.chat.config.CustomPartitioner;
import it.discovery.kafka.chat.model.Chat;
import it.discovery.kafka.chat.model.ChatMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class JavaClientChatProducer implements ChatProducer {

    private final Properties properties;

    public JavaClientChatProducer(String bootstrapServers) {
        this.properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
    }


    @Override
    public Future<RecordMetadata> send(ChatMessage message) {
        //Make it singleton
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {

            ProducerRecord<String, String> record = new ProducerRecord<>(message.chat().name(),//topic name
                message.sender(), //sender
                message.text()); //value
            return producer.send(record);
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Chat chat = new Chat("chat-messages");
        ChatMessage message;
        String sender;
        ChatProducer producer = new JavaClientChatProducer("localhost:9092");
        for (int i = 0; i < 10; i++) {
            sender = (i % 2) == 0 ? "Peter" : "John";
            message = new ChatMessage(sender, "Hello from " + sender + "!", chat);

            Future<RecordMetadata> future = producer.send(message);
            RecordMetadata recordMetadata = future.get();
            System.out.println("Message offset: " + recordMetadata.offset());
            System.out.println("Message sender: " + sender);
            System.out.println("Message opartition: " + recordMetadata.partition());
            System.out.println("Message timestamp: " + recordMetadata.timestamp());
            System.out.println("=====================");
            Thread.sleep(1000);
        }
    }
}

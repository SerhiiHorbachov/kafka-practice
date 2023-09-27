package it.discovery.kafka.chat.producer;

import it.discovery.kafka.chat.model.ChatMessage;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.Future;

public interface ChatProducer {
    Future<RecordMetadata> send(ChatMessage message);
}

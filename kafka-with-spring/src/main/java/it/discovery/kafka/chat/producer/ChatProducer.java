package it.discovery.kafka.chat.producer;

import it.discovery.kafka.chat.model.ChatMessage;

import java.util.concurrent.Future;

public interface ChatProducer<T> {
    Future<T> send(ChatMessage message);
}

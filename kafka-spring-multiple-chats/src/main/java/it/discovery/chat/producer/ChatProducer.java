package it.discovery.chat.producer;


import it.discovery.chat.model.ChatMessage;

import java.util.concurrent.Future;

public interface ChatProducer<T> {
    Future<T> send(ChatMessage message);
}

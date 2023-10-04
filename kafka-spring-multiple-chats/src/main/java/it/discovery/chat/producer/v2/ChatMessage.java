package it.discovery.chat.producer.v2;

import it.discovery.chat.model.MessageType;

public record ChatMessage(String sender, String text, String chat, MessageType messageType) {
}

package it.discovery.chat.web;

import it.discovery.chat.model.MessageType;

public record ChatMessageDto(String sender, String text, String chat, MessageType messageType) {
}

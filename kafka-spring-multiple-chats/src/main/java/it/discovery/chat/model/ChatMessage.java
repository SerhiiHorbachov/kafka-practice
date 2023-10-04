package it.discovery.chat.model;

public record ChatMessage(String sender, String text, Chat chat) {
}

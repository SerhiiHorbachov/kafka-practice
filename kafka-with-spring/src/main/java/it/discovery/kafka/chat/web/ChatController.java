package it.discovery.kafka.chat.web;

import it.discovery.kafka.chat.model.Chat;
import it.discovery.kafka.chat.model.ChatMessage;
import it.discovery.kafka.chat.producer.ChatProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@RestController
@RequestMapping("messages")
public class ChatController {

    private final ChatProducer chatProducer;
    private final Environment env;

    @PostMapping
    public void sendMessage(@RequestBody ChatMessageDto dto) throws ExecutionException, InterruptedException {
        Future future = chatProducer.send(new ChatMessage(dto.sender(), dto.text(), new Chat(env.getRequiredProperty("chat.name"))));
        SendResult<String, String> result1 = (SendResult<String, String>) future.get();
        System.out.println("Topic: " + result1.getProducerRecord().topic());
        System.out.println("Timestamp: " + result1.getProducerRecord().timestamp()); //TODO: null, investigate
        System.out.println("Partition: " + result1.getProducerRecord().partition()); //TODO: null investigate
        System.out.println("==========");

    }


}

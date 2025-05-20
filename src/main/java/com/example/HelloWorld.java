package com.example;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    private final ChatClient chatClient;

    HelloWorld(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/")
    String home() {
        return chatClient
                .prompt()
                .user("who are you")
                .call()
                .content();
    }

}


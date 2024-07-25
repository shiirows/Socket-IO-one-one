package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.example.demo.model.Greeting;
import com.example.demo.model.HelloMessage;
import com.example.demo.model.PrivateMessage;

@Controller
public class GreetingController {


    private final SimpMessagingTemplate messagingTemplate;

    public GreetingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload PrivateMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String recipient = message.getRecipient();
        System.out.println(recipient);
        messagingTemplate.convertAndSendToUser(recipient, "/queue/reply", new Greeting("Private: " + HtmlUtils.htmlEscape(message.getName())));
    }
}

package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.example.demo.model.Greeting;
import com.example.demo.model.PrivateMessage;
import com.example.demo.service.UserService;

@Controller
public class GreetingController {

	private final SimpMessagingTemplate messagingTemplate;
	private final UserService userService;

	public GreetingController(SimpMessagingTemplate messagingTemplate, UserService userService) {
		this.messagingTemplate = messagingTemplate;
		this.userService = userService;
	}

	@MessageMapping("/private-message")
	public void sendPrivateMessage(@Payload PrivateMessage message, SimpMessageHeaderAccessor headerAccessor) {
		String recipient = message.getRecipient();
		String sender = message.getSender();

		// Send the message to the recipient
		messagingTemplate.convertAndSendToUser(recipient, "/queue/reply",
				new Greeting(HtmlUtils.htmlEscape(sender) + " : " + HtmlUtils.htmlEscape(message.getName())));

		// Send the message to the sender
		messagingTemplate.convertAndSendToUser(sender, "/queue/reply",
				new Greeting("toi : " + HtmlUtils.htmlEscape(message.getName())));
	}

	@MessageMapping("/connect")
	public void connectUser(@Payload String username) {
		userService.addUser(username);

		// Récupère la liste des utilisateurs comme un Set
		Set<String> usersSet = userService.getAllUsers();

		// Convertit le Set en List
		List<String> usersList = new ArrayList<>(usersSet);

		// Envoie la liste des utilisateurs au frontend
		messagingTemplate.convertAndSend("/topic/users", usersList);
	}

	@MessageMapping("/disconnect")
	public void disconnectUser(@Payload String username) {

		userService.removeUser(username);

		// Envoie la liste mise à jour des utilisateurs
		messagingTemplate.convertAndSend("/topic/users", userService.getAllUsers());
	}

}

package com.spotify.api.controller;

import com.spotify.api.model.Message;
import com.spotify.api.repository.UserRepository;
import com.spotify.api.service.external.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.*;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;
import java.util.UUID;

@RestController
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired private SimpUserRegistry registry;
    @Autowired private SpotifyService spotifyService;
    @Autowired private UserRepository repository;
    @MessageMapping("/message/{destination}")
    public void onMessage(@Payload Message message, Principal user, @DestinationVariable String destination) {

        message.setId(UUID.randomUUID().toString());
        message.setDate(new Date());
        message.setFrom(user.getName());
        message.setType(Message.MessageType.MESSAGE);
        messagingTemplate.convertAndSend("/topic/" + destination + "/message", message);

    }
}

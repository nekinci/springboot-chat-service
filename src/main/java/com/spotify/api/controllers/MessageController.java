package com.spotify.api.controllers;

import com.spotify.api.models.Message;
import com.spotify.api.repositories.UserRepository;
import com.spotify.api.service.SpotifyService;
import com.spotify.api.util.ServletUtil;
import com.wrapper.spotify.model_objects.specification.Episode;
import com.wrapper.spotify.model_objects.specification.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.user.*;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
    @MessageMapping("/message")
    public void onMessage(@Payload Message message, Principal user) {

        message.setId(UUID.randomUUID().toString());
        message.setDate(new Date());
        message.setFrom(user.getName());
        message.setType(Message.MessageType.MESSAGE);
        messagingTemplate.convertAndSend("/topic/general", message);

    }
}

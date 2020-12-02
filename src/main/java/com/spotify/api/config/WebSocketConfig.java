package com.spotify.api.config;

import com.spotify.api.core.RoomUsers;
import com.spotify.api.models.Message;
import com.spotify.api.models.User;
import com.spotify.api.models.UserToken;
import com.spotify.api.repositories.UserRepository;
import com.spotify.api.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired private UserRepository userRepository;
    @Autowired private RoomService roomService;
    @Autowired private SimpMessagingTemplate template;
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry){
        registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableStompBrokerRelay("/topic")
                .setRelayPort(61613)
                .setRelayHost("localhost")
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }

    @EventListener(SessionConnectEvent.class)
    public void handleWebsocketConnectListner(SessionConnectEvent event) {
        System.out.println("Received a new web socket connection : " + new Date() + " " + event.getUser());
        System.out.println(event.getMessage().getHeaders().get("simpDestination"));
        String destination = event.getMessage().getHeaders().get("simpDestination").toString();
        com.spotify.api.models.Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setMessage(event.getUser().getName());
        message.setType(Message.MessageType.JOIN);
        message.setFrom("server@connection::1");
        message.setDate(new Date());
        template.convertAndSend(destination, message);
    }

    @EventListener(SessionSubscribeEvent.class)
    public void handleSubscribeListener(SessionSubscribeEvent event){
        System.out.println(event.getMessage().getHeaders().get("simpDestination"));
        String destination = event.getMessage().getHeaders().get("simpDestination").toString();
        com.spotify.api.models.Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setMessage(event.getUser().getName());
        message.setType(Message.MessageType.JOIN);
        message.setFrom("server@connection::1");
        message.setDate(new Date());
        template.convertAndSend(destination, message);
    }

    @EventListener(SessionUnsubscribeEvent.class)
    public void handleUnsubscribeListener(SessionUnsubscribeEvent event){
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListner(SessionDisconnectEvent event) {
        System.out.println("Session closed"+  new Date());
    }


}

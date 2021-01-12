package com.spotify.api.config;

import com.spotify.api.repository.UserRepository;
import com.spotify.api.service.RoomService;
import com.spotify.api.service.redis.RedisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Date;
import java.util.HashMap;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired private RoomService roomService;
    @Autowired private RedisUserService redisUserService;
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry){
        registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayPort(61613)
                .setRelayHost("localhost")
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }

    @EventListener(SessionConnectEvent.class)
    public void handleWebsocketConnectListner(SessionConnectEvent event) {
        System.out.println("Received a new web socket connection : " + new Date() + " " + event.getUser());
        redisUserService.add(event.getUser().getName());
        roomService.getRoomName(event.getUser().getName());
    }

    @EventListener(SessionSubscribeEvent.class)
    public void handleSubscribeListener(SessionSubscribeEvent event){
        redisUserService.addDestination(event.getUser().getName(), event.getMessage().getHeaders().get("simpDestination").toString());
    }

    @EventListener(SessionUnsubscribeEvent.class)
    public void handleUnsubscribeListener(SessionUnsubscribeEvent event){
        redisUserService.removeDestination(event.getUser().getName());
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListner(SessionDisconnectEvent event) {
        System.out.println("Session closed at: "+  new Date());
        redisUserService.remove(event.getUser().getName());
        roomService.unsubscribe(event.getUser().getName());

    }


}

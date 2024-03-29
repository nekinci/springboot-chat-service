package com.spotify.api.controller;

import com.spotify.api.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @MessageMapping("/my_room")
    public void whatIsMyRoom(Principal user)
    {
        roomService.getRoomName(user.getName());
    }

    @MessageMapping("/subscribe/{destination}")
    public void addUserAndBroadcast(Principal user, @DestinationVariable String destination, @Payload String command){
        if (!command.equalsIgnoreCase("subscribe")) return;
        roomService.subscribe(user.getName(), destination);
    }

    @MessageMapping("/typing/{destination}")
    public void onTyping(@Payload boolean value, Principal user, @DestinationVariable String destination){
        roomService.onTyping(user.getName(), destination, value);
    }


}

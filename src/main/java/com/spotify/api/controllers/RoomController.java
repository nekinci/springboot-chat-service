package com.spotify.api.controllers;

import com.spotify.api.service.RoomService;
import com.spotify.api.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class RoomController {


    @Autowired
    private RoomService roomService;
    public void getOnlineUsers(String room){

    }

    @MessageMapping("/info/myRoom")
    public void whatIsMyRoom(Principal user){
        roomService.getRoomName(user.getName());
    }

    public void getOnlineUserCount(String room){

    }
}

package com.spotify.api.controller;

import com.spotify.api.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
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


}

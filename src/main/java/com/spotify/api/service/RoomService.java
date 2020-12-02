package com.spotify.api.service;

import com.spotify.api.models.Room;
import com.spotify.api.models.User;
import com.wrapper.spotify.model_objects.specification.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoomService {

    @Autowired
    private SpotifyService spotifyService;
    @Autowired UserService service;
    @Autowired private SimpMessagingTemplate messagingTemplate;
    public Room getRoomName(String email){
        User user = service.getUserByEmail(email);
        Room room = null;
        if (Objects.nonNull(user) && Objects.nonNull(user.getUserToken())){
            Track track = spotifyService.currentPlaying(user.getUserToken());
            room = Room.builder().roomName(track.getName()).songId(track.getId()).build();
            messagingTemplate.convertAndSend("/info/myRoom", room);
            return room;
        }
        room = Room.builder().songId("###").roomName("Herkese Açık").build();
        messagingTemplate.convertAndSendToUser(email, "/info/myRoom", room);
        return room;
    }
}

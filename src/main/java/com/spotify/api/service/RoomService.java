package com.spotify.api.service;

import com.spotify.api.model.Room;
import com.spotify.api.model.User;
import com.spotify.api.repository.redis.RedisRoomRepository;
import com.spotify.api.service.external.SpotifyService;
import com.spotify.api.service.redis.RedisUserService;
import com.wrapper.spotify.model_objects.specification.Track;
import org.apache.hc.client5.http.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Service
public class RoomService {

    @Value("${room.public.id}") private String songId;
    @Value("${room.public.name}}") private String roomName;
    @Autowired private SpotifyService spotifyService;
    @Autowired private RedisUserService redisUserService;
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private UserService userService;
    @Autowired private RedisRoomRepository redisRoomRepository;
    public Room getRoomName(String email){
        Room room = Room.builder().id(songId).name(roomName).build();
        room.setConnectionTime(new Date());
        room.setCalculatedEndTime(10_000);
        User user = redisUserService.getByEmail(email);
        if(Objects.isNull(user)) user = userService.getByEmail(email);
        if (Objects.nonNull(user) && Objects.nonNull(user.getUserToken())) {
            try{
                Track track = spotifyService.currentPlaying(user.getUserToken());
                room.setName(track.getName());
                room.setId(track.getId());
            }catch (Exception e){
                System.out.println(e.getMessage());
            }finally {
                messagingTemplate.convertAndSendToUser(email, "/queue/my_room", room);
                return room;
            }
        }
        messagingTemplate.convertAndSendToUser(email, "/queue/my_room", room);
        return room;
    }

    public void addToRoom(String user, String destination){
        if(Objects.isNull(user) || Objects.isNull(destination)) return;
        com.spotify.api.model.redis.Room room = redisRoomRepository.findById(destination).orElse(null);
        if(Objects.isNull(room)) {
            room = new com.spotify.api.model.redis.Room();
            room.setDestination(destination);
            room.setId(destination);
            room.setUserList(new ArrayList<com.spotify.api.model.redis.valueObject.User>());
        }
    }


}

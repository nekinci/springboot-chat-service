package com.spotify.api.service;

import com.spotify.api.dto.OnlineUserDto;
import com.spotify.api.dto.TypingDto;
import com.spotify.api.model.Room;
import com.spotify.api.model.User;
import com.spotify.api.repository.redis.RedisRoomRepository;
import com.spotify.api.service.external.SpotifyService;
import com.spotify.api.service.redis.RedisUserService;
import com.wrapper.spotify.model_objects.specification.Track;
import org.apache.hc.client5.http.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Value("${room.public.id}") private String songId;
    @Value("${room.public.name}") private String roomName;
    @Autowired private SpotifyService spotifyService;
    @Autowired private RedisUserService redisUserService;
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private UserService userService;
    @Autowired private RedisRoomRepository redisRoomRepository;

    public Room getRoomName(String email){
        Room room = Room.builder().id(songId).name(roomName).build();
        room.setConnectionTime(new Date());
        room.setCalculatedEndTime(40_000);
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

    public void onTyping(String user, String destination, boolean value){
        if(Objects.isNull(user) || !validateDestination(destination)) return;
        com.spotify.api.model.redis.Room room  = redisRoomRepository.findById(destination).orElse(null);
        User __user = userService.getByEmail(user);
        if(Objects.isNull(room)) return;
        if(Objects.isNull(room.getTypingList())){
            room.setTypingList(new ArrayList<>());
        }
        TypingDto typingDto = new TypingDto();
        typingDto.setEmail(user);
        typingDto.setDestination(destination);
        typingDto.setFullName(__user.getName() + " " + __user.getSurname());
        if(value && room.getTypingList().stream().filter(x -> x.equalsIgnoreCase(user)).count() <= 0){
            room.getTypingList().add(user);
            typingDto.setTyping(true);
        }
        else {
            typingDto.setTyping(false);
            room.setTypingList(room.getTypingList().stream().filter(x -> !x.equalsIgnoreCase(user)).collect(Collectors.toList()));
        }
        redisRoomRepository.save(room);
        messagingTemplate.convertAndSend("/topic/typing#"+ destination, typingDto);
    }

    public void unsubscribe(String user){
        if(Objects.isNull(user)) return;
        List<com.spotify.api.model.redis.Room> rooms = getByUser(user);
        if(Objects.isNull(rooms)) return;
        rooms.forEach(y -> System.out.println(y.getDestination()));
        rooms.forEach(x->{
            if(Objects.isNull(x.getUserList()))
                return;

            x.getUserList().forEach(y -> {
                if(y.getEmail().equalsIgnoreCase(user)){
                    messagingTemplate.convertAndSend("/topic/userList#"+x.getDestination(), forEveryone(y, x, "LEAVE"));
                    messagingTemplate.convertAndSend("/topic/typing#"+x.getDestination(), toTypingDto(user, "", false));
                }
            });
            x.setUserList(x.getUserList().stream().filter(y->!y.getEmail().equalsIgnoreCase(user)).collect(Collectors.toList()));
            x.setTypingList(x.getTypingList() == null ? new ArrayList<>(): x.getTypingList().stream().filter(y -> !y.equalsIgnoreCase(user)).collect(Collectors.toList()));
        });
        redisRoomRepository.saveAll(rooms);
    }

    public void subscribe(String user, String destination){
        if(Objects.isNull(user) || !validateDestination(destination)) return;
        com.spotify.api.model.redis.Room room = redisRoomRepository.findById(destination).orElse(null);
        if(Objects.isNull(room)) {
            room = new com.spotify.api.model.redis.Room();
            room.setDestination(destination);
            room.setId(destination);
        }
        if(Objects.isNull(room.getUserList())) room.setUserList(new ArrayList<com.spotify.api.model.redis.valueObject.User>());
        if(room.getUserList().stream().anyMatch(x->x.getEmail().equalsIgnoreCase(user))) return;
        User source = userService.getByEmail(user);
        if(Objects.isNull(source)) return;
        if(Objects.isNull(room.getTypingList())){ room.setTypingList(new ArrayList<>());}
        com.spotify.api.model.redis.valueObject.User target = new com.spotify.api.model.redis.valueObject.User();
        map(source, target);
        room.getUserList().add(target);
        redisRoomRepository.save(room);
        messagingTemplate.convertAndSend( "/topic/userList#"+destination, forEveryone(target, room, "JOIN"));
        messagingTemplate.convertAndSendToUser(user, "/queue/userList", forUser(target.getId(), room));
        messagingTemplate.convertAndSendToUser(user, "/queue/typing", room.getTypingList().stream().map(x -> toTypingDto(x, source.getName()+" "+source.getSurname())).toArray());
    }



    // -------------------HELPER METHODS----------------------------- //


    private TypingDto toTypingDto(String email, String name, Boolean typing){
        if (Objects.isNull(typing)) typing = true;
        TypingDto dto = new TypingDto();
        dto.setTyping(typing.booleanValue());
        dto.setEmail(email);
        dto.setFullName(name);
        return dto;
    }
    private TypingDto toTypingDto(String email, String name){
        return toTypingDto(email, name, null);
    }
    private void map(User source, com.spotify.api.model.redis.valueObject.User target){
        target.setName(source.getName());
        target.setSurname(source.getSurname());
        target.setFullName(source.getName() + " " + source.getSurname());
        target.setEmail(source.getEmail());
//        target.setAvatar(source.getAvatar().getUrl());
        target.setId(source.getId());
    }

    private OnlineUserDto forUser(String id, com.spotify.api.model.redis.Room room){
        OnlineUserDto dto = new OnlineUserDto();
        dto.setUserList(room.getUserList().stream().filter(x -> x.getId() != id).collect(Collectors.toList()));
        dto.setCount(room.getUserCount());
        dto.setDestination(room.getDestination());
        return dto;
    }

    private OnlineUserDto forEveryone(com.spotify.api.model.redis.valueObject.User user, com.spotify.api.model.redis.Room room, String type){
        OnlineUserDto dto = new OnlineUserDto();
        dto.setDestination(room.getDestination());
        dto.setType(type);
        List<com.spotify.api.model.redis.valueObject.User> userList = new ArrayList<>();
        userList.add(user);
        dto.setUserList(userList);
        dto.setCount(room.getUserCount());
        return dto;
    }

    private boolean validateDestination(String destination){
        if(Objects.isNull(destination)) return false;
        if(destination.contains("undefined")) return false;
        return true;
    }

    private List<com.spotify.api.model.redis.Room> getByUser(String user){
        List<com.spotify.api.model.redis.valueObject.User> userList = new ArrayList<>();
        com.spotify.api.model.redis.Room room = new com.spotify.api.model.redis.Room();
        com.spotify.api.model.redis.valueObject.User _user = new com.spotify.api.model.redis.valueObject.User();
        _user.setEmail(user);
        userList.add(_user);
        Iterable<com.spotify.api.model.redis.Room> rooms = redisRoomRepository.findAll(Example.of(room));
        if(Objects.isNull(rooms)) return null;
        List<com.spotify.api.model.redis.Room> roomList = new ArrayList<>();
        rooms.forEach(x->{ if(Objects.nonNull(x)) roomList.add(x); });
        return roomList;
    }
}

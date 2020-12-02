package com.spotify.api.core;

import com.spotify.api.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class RoomUsers {

    private final HashMap<String, ArrayList<User>> users = new HashMap<>();
    private static RoomUsers instance = null;
    private RoomUsers(){
        System.out.println("Created At: " + new Date());
    }

    private synchronized static void createInstance(){
        if(Objects.isNull(instance)){
            instance = new RoomUsers();
        }
    }
    public static RoomUsers getInstance(){
        if(Objects.isNull(instance))
            createInstance();
        return instance;
    }

    public void addUserToRoom(String room, User user){
        ArrayList<User> _users = users.get(room);
        if(_users == null){
            _users = new ArrayList<>();
            users.put(room, _users);
        }
        _users.add(user);
    }

    public void removeUserFromRoom(String room, String email){
        ArrayList<User> _users = users.get(room);
        if(Objects.nonNull(_users)){
            _users.stream().filter(x->!x.getEmail().equalsIgnoreCase(email));
        }
    }
}

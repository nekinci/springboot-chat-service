package com.spotify.api.service;

import com.spotify.api.dto.Paginator;
import com.spotify.api.model.Message;
import com.spotify.api.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class MessageService {

    private @Autowired SimpMessagingTemplate messagingTemplate;
    private @Autowired MessageRepository messagesRepository;

    public void onMessage(Message message, String destination){
        message.setId(UUID.randomUUID().toString());
        message.setDate(new Date());
        message.setType(Message.MessageType.MESSAGE);
        messagesRepository.save(message);
        messagingTemplate.convertAndSend("/topic/message#" + destination, message);
    }

    public Paginator<Message> get(String destination, Pageable pageable){
        if (Objects.isNull(destination)) return null;
        return map(messagesRepository.findByRoomIdOrderByDateDesc(destination, pageable));
    }

    private Paginator<Message> map(Page<Message> message){
        if(Objects.isNull(message)) return null;
        Paginator<Message> messagePaginator = new Paginator<>();
        messagePaginator.setData(new ArrayList<>(message.getContent()));
        messagePaginator.getData().sort(Comparator.comparing(Message::getDate));
        messagePaginator.setTotalSize(message.getTotalElements());
        messagePaginator.setPage(message.getNumber());
        messagePaginator.setTotalPage(message.getTotalPages());
        messagePaginator.setSize(message.getSize());
        return messagePaginator;
    }

            

}

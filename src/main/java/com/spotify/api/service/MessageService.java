package com.spotify.api.service;

import com.spotify.api.model.Message;
import com.spotify.api.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class MessageService {

    private SimpMessagingTemplate messagingTemplate;
    private @Autowired MessageRepository messagesRepository;

    public void onMessage(Message message, String destination){
        message.setId(UUID.randomUUID().toString());
        message.setDate(new Date());
        message.setType(Message.MessageType.MESSAGE);

        messagesRepository.save(message);
        messagingTemplate.convertAndSend("/topic/message#" + destination, message);
    }

    public Page<Message> get(String destination, Pageable pageable){
        if (Objects.isNull(destination)) return null;
        return messagesRepository.findByRoomIdOrderByDateDesc(destination, pageable);
    }

}

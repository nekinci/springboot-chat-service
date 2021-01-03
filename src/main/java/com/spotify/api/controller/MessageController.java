package com.spotify.api.controller;
import com.spotify.api.model.Message;
import com.spotify.api.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.List;

@RestController
public class MessageController {

    @Autowired private MessageService messageService;

    @MessageMapping("/message/{destination}")
    public void onMessage(@Payload Message message, Principal user, @DestinationVariable String destination) {
        message.setFrom(user.getName());
        message.setRoomId(destination);
        messageService.onMessage(message, destination);
    }

    @GetMapping("/messages/{destination}")
    public Page<Message> getWithPagination(@PathVariable("destination") String destination,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "3") int size){
        Pageable pageable = PageRequest.of(page, size);
        return messageService.get(destination, pageable);
    }
}

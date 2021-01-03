package com.spotify.api.repository;
import com.spotify.api.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Document("messages")
public interface MessageRepository extends MongoRepository<Message, String> {
    Page<Message> findByRoomIdOrderByDateDesc(String roomId, Pageable pageable);
}

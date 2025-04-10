package com.talhaatif.chatapp.repository;

import com.talhaatif.chatapp.entity.ChatMessage;
import com.talhaatif.chatapp.entity.UserPresence;
import org.mapstruct.control.MappingControl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPresenceRepository extends MongoRepository<UserPresence, String> {
    Optional<UserPresence> findBySessionId(String sessionId);
    Optional<UserPresence> findByUsername(String username);
    void deleteBySessionId(String sessionId);
}

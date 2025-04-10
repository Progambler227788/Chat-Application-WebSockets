package com.talhaatif.chatapp.service.impl;


import com.talhaatif.chatapp.dto.request.ChatMessageRequest;
import com.talhaatif.chatapp.dto.request.TypingNotificationRequest;
import com.talhaatif.chatapp.dto.response.ChatMessageResponse;
import com.talhaatif.chatapp.dto.response.UserActivityResponse;
import com.talhaatif.chatapp.entity.ChatMessage;
import com.talhaatif.chatapp.entity.UserPresence;
import com.talhaatif.chatapp.mapper.ChatMapper;
import com.talhaatif.chatapp.repository.ChatRepository;
import com.talhaatif.chatapp.repository.UserPresenceRepository;
import com.talhaatif.chatapp.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.talhaatif.chatapp.constant.WebSocketConstants.*;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserPresenceRepository userPresenceRepository;
    private final SimpMessagingTemplate messagingTemplate;


    ChatServiceImpl(ChatRepository chatRepository, SimpMessagingTemplate messagingTemplate, UserPresenceRepository userPresenceRepository, ChatMapper chatMapper) {

        this.chatRepository = chatRepository;
        this.messagingTemplate = messagingTemplate;
        this.userPresenceRepository = userPresenceRepository;
        this.chatMapper = chatMapper;
    }

    @Override
    public ChatMessageResponse saveMessage(ChatMessageRequest request) {
        ChatMessage message = chatMapper.toEntity(request);
        ChatMessage savedMessage = chatRepository.save(message);

        // Broadcast the message to all subscribers
        messagingTemplate.convertAndSend(
                PUBLIC_MESSAGES_TOPIC,
                chatMapper.toResponse(savedMessage)
        );

        return chatMapper.toResponse(savedMessage);
    }

    @Override
    public List<ChatMessageResponse> getRoomMessages(String roomId) {
        return chatRepository.findByRoomIdOrderByTimestampAsc(roomId)
                .stream()
                .map(chatMapper::toResponse)
                .toList();
    }

    @Override
    public List<ChatMessageResponse> getRecentMessages(String roomId, LocalDateTime after) {
        return chatRepository.findRecentMessages(roomId, after)
                .stream()
                .map(chatMapper::toResponse)
                .toList();
    }

    @Override
    public long getUnreadMessageCount(String roomId, String sender) {
        return chatRepository.countUnreadMessages(roomId, sender);
    }

    @Override
    public void sendTypingNotification(TypingNotificationRequest request) {
        UserActivityResponse activity = new UserActivityResponse(
                request.username(),
                "TYPING",
                LocalDateTime.now()
        );
        messagingTemplate.convertAndSend(USER_ACTIVITY_TOPIC, activity);
    }

    @Override
    public void joinChat(String username, String sessionId) {
        UserPresence presence = new UserPresence();
        presence.setUsername(username);
        presence.setSessionId(sessionId);
        presence.setStatus("ONLINE");
        presence.setLastSeen(LocalDateTime.now());

        userPresenceRepository.save(presence);

        // Broadcast user joined event
        messagingTemplate.convertAndSend(
                USER_PRESENCE_TOPIC,
                new UserActivityResponse(username, "JOINED", LocalDateTime.now())
        );
    }

    @Override
    public void leaveChat(String sessionId) {
        userPresenceRepository.findBySessionId(sessionId)
                .ifPresent(presence -> {
                    userPresenceRepository.delete(presence);

                    // Broadcast user left event
                    messagingTemplate.convertAndSend(
                            USER_PRESENCE_TOPIC,
                            new UserActivityResponse(presence.getUsername(), "LEFT", LocalDateTime.now())
                    );
                });
    }
}

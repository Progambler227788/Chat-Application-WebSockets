package com.talhaatif.chatapp.service;


import com.talhaatif.chatapp.dto.request.ChatMessageRequest;
import com.talhaatif.chatapp.dto.request.TypingNotificationRequest;
import com.talhaatif.chatapp.dto.response.ChatMessageResponse;
import com.talhaatif.chatapp.dto.response.UserActivityResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatService {
    ChatMessageResponse saveMessage(ChatMessageRequest request);

    List<ChatMessageResponse> getRoomMessages(String roomId);

    List<ChatMessageResponse> getRecentMessages(String roomId, LocalDateTime after);

    long getUnreadMessageCount(String roomId, String sender);

    void sendTypingNotification(TypingNotificationRequest request);

    void joinChat(String username, String sessionId);

    void leaveChat(String sessionId);
}
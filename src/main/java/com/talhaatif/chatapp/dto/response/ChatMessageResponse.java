package com.talhaatif.chatapp.dto.response;


import com.talhaatif.chatapp.entity.ChatMessage.MessageType;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        String id,
        String roomId,
        String sender,
        String content,
        MessageType type,
        LocalDateTime timestamp
) {}

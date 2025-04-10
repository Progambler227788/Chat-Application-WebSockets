package com.talhaatif.chatapp.dto.request;

import com.talhaatif.chatapp.entity.ChatMessage.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageRequest(
        @NotBlank String roomId,
        @NotBlank String sender,
        @NotBlank String content,
        @NotNull MessageType type
) {}
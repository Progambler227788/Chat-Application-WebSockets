package com.talhaatif.chatapp.dto.request;


import jakarta.validation.constraints.NotBlank;

public record TypingNotificationRequest(
        @NotBlank String username,
        @NotBlank String conversationId
) {}

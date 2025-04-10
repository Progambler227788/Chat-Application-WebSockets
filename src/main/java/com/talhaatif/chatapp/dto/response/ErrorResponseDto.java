package com.talhaatif.chatapp.dto.response;

public record ErrorResponseDto (
    int status,
    String error,
    String message
) {}

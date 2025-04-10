package com.talhaatif.chatapp.dto.response;


import java.time.LocalDateTime;

public record UserActivityResponse(
        String username,
        String activityType,
        LocalDateTime timestamp
) {}

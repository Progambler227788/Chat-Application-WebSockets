package com.talhaatif.chatapp.entity;


import com.talhaatif.chatapp.constant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = AppConstants.TYPING_STATUS_COLLECTION)
public class TypingStatus {
    @Id
    private String id;

    private String conversationId;
    private String username;

    @Indexed(expireAfterSeconds = AppConstants.TYPING_INDICATOR_TTL_SECONDS)
    private LocalDateTime expiresAt;
}

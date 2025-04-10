package com.talhaatif.chatapp.entity;

import com.talhaatif.chatapp.constant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = AppConstants.MESSAGES_COLLECTION)
public class ChatMessage {
    @Id
    private String id;

    @Indexed
    private String roomId;

    private String sender;
    private String content;
    private MessageType type;

    @Indexed
    private LocalDateTime timestamp;

    private Map<String, Boolean> readReceipts;

    public enum MessageType {
        CHAT, JOIN, LEAVE, TYPING, IMAGE, FILE
    }
}
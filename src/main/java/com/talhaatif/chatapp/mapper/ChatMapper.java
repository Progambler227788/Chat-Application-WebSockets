package com.talhaatif.chatapp.mapper;

import com.talhaatif.chatapp.dto.request.ChatMessageRequest;
import com.talhaatif.chatapp.dto.response.ChatMessageResponse;
import com.talhaatif.chatapp.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

/**
 * Mapper for converting between ChatMessage entities and DTOs.
 * Uses MapStruct for efficient mapping without reflection.
 */
@Mapper(componentModel = "spring")
public interface ChatMapper {

    /**
     * Converts ChatMessageRequest DTO to ChatMessage entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", expression = "java(getCurrentTimestamp())")
    @Mapping(target = "readReceipts", ignore = true)
    ChatMessage toEntity(ChatMessageRequest request);

    /**
     * Converts ChatMessage entity to ChatMessageResponse DTO
     */
    ChatMessageResponse toResponse(ChatMessage entity);

    /**
     * Provides current timestamp for mapping
     */
    @Named("getCurrentTimestamp")
    default LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }
}
package com.talhaatif.chatapp.exception;

public class ResourceNotFoundException extends ChatException {
    public ResourceNotFoundException(String resource, String id) {
        super(String.format("%s not found with id %s", resource, id));
    }
}

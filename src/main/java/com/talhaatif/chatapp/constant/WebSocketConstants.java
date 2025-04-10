package com.talhaatif.chatapp.constant;

public class WebSocketConstants {
    public static final String PUBLIC_MESSAGES_TOPIC = "/topic/public";
    public static final String PRIVATE_MESSAGES_QUEUE = "/queue/private";
    public static final String USER_ACTIVITY_TOPIC = "/topic/activity";
    public static final String USER_PRESENCE_TOPIC = "/topic/presence";

    public static final String APP_DESTINATION_PREFIX = "/app";
    public static final String SEND_MESSAGE_ENDPOINT = "/chat.send";
    public static final String TYPING_ENDPOINT = "/chat.typing";
    public static final String JOIN_ENDPOINT = "/chat.join";
    public static final String LEAVE_ENDPOINT = "/chat.leave";
}
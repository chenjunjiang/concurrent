package com.chenjj.concurrent.eventDriven.chat;

/**
 * 用户聊天Event
 */
public class UserChatEvent extends UserOnlineEvent {
    private final String message;

    public UserChatEvent(User user, String message) {
        super(user);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

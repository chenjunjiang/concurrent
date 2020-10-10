package com.chenjj.concurrent.eventDriven.chat;

import com.chenjj.concurrent.eventDriven.Event;

/**
 * 用户上线Event
 */
public class UserOnlineEvent extends Event {
    private final User user;

    public UserOnlineEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

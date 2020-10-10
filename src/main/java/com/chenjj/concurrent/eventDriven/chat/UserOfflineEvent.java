package com.chenjj.concurrent.eventDriven.chat;

/**
 * 用户下线Event
 */
public class UserOfflineEvent extends UserOnlineEvent {
    public UserOfflineEvent(User user) {
        super(user);
    }
}

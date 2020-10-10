package com.chenjj.concurrent.eventDriven.chat;

/**
 * 聊天室的参与者
 */
public class User {
    private final String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

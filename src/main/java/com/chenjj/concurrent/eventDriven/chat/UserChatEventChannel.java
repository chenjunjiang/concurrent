package com.chenjj.concurrent.eventDriven.chat;

import com.chenjj.concurrent.eventDriven.AsyncChannel;
import com.chenjj.concurrent.eventDriven.Event;

public class UserChatEventChannel extends AsyncChannel {
    @Override
    protected void handle(Event message) {
        UserChatEvent event = (UserChatEvent) message;
        System.out.println("The User[" + event.getUser().getName() + "] say: " + event.getMessage());
    }
}

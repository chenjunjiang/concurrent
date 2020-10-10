package com.chenjj.concurrent.eventDriven.chat;

import com.chenjj.concurrent.eventDriven.AsyncChannel;
import com.chenjj.concurrent.eventDriven.Event;

public class UserOnlineEventChannel extends AsyncChannel {
    @Override
    protected void handle(Event message) {
        UserOnlineEvent event = (UserOnlineEvent) message;
        System.out.println("The User[" + event.getUser().getName() + "] is online.");
    }
}

package com.chenjj.concurrent.eventDriven.chat;

import com.chenjj.concurrent.eventDriven.AsyncChannel;
import com.chenjj.concurrent.eventDriven.Event;

public class UserOfflineEventChannel extends AsyncChannel {
    @Override
    protected void handle(Event message) {
        UserOfflineEvent event = (UserOfflineEvent) message;
        System.out.println("The User[" + event.getUser().getName() + "] is offline.");
    }
}

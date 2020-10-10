package com.chenjj.concurrent.eventDriven.chat;

import com.chenjj.concurrent.eventDriven.AsyncEventDispatcher;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 聊天参与者线程
 */
public class UserChatThread extends Thread {
    private final User user;
    private final AsyncEventDispatcher dispatcher;

    public UserChatThread(User user, AsyncEventDispatcher dispatcher) {
        super(user.getName());
        this.user = user;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try {
            // User上线，发送Online Event
            dispatcher.dispatch(new UserOnlineEvent(user));
            for (int i = 0; i < 5; i++) {
                // 发送聊天信息
                dispatcher.dispatch(new UserChatEvent(user, getName() + "-Hello-" + i));
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // User下线
            dispatcher.dispatch(new UserOfflineEvent(user));
        }
    }
}

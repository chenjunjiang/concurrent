package com.chenjj.concurrent.eventBus;

import java.util.concurrent.TimeUnit;

public class SimpleSubscriber1 {
    @Subscribe
    public void method1(String message) {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==SimpleSubscriber1==method1==" + message);
    }

    @Subscribe(topic = "test")
    public void method2(String message) {
        System.out.println("==SimpleSubscriber1==method2==" + message);
    }
}

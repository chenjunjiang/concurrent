package com.chenjj.concurrent.eventBus;

/**
 * 当目录发生变化时用来接收事件，它就是一个Subscriber
 */
public class FileChangeListener {
    @Subscribe
    public void onChange(FileChangeEvent event) {
        System.out.printf("%s-%s\n", event.getPath(), event.getKind());
    }
}

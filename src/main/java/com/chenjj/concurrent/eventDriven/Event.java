package com.chenjj.concurrent.eventDriven;

/**
 * Event是对Message最简单的实现，在以后的使用中，将Event直接作为其它Message的基类即可
 */
public class Event implements Message {
    @Override
    public Class<? extends Message> getType() {
        return this.getClass();
    }
}

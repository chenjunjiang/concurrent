package com.chenjj.concurrent.eventDriven;

/**
 * 每个Event可以被称为Message，Message是对Event更高一级的抽象，每一个Message都有一个特定的Type用于与对应的Handler做关联
 */
public interface Message {
    Class<? extends Message> getType();
}

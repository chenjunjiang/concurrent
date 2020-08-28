package com.chenjj.concurrent.threadPool;

public class RunnableDenyException extends RuntimeException {
    public RunnableDenyException(String message) {
        super(message);
    }
}

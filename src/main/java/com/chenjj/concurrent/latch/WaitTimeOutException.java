package com.chenjj.concurrent.latch;

public class WaitTimeOutException extends Exception {
    public WaitTimeOutException(String message) {
        super(message);
    }
}

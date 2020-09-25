package com.chenjj.concurrent.reference;

import java.io.IOException;

/**
 * 模拟网络编程中的Socket，主要是close方法
 */
public class Socket {
    private final byte[] data = new byte[1024 * 1024 * 1];

    public void close() throws IOException {
        System.out.println("Socket closed......");
        throw new IOException();
    }
}

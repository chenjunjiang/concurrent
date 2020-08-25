package com.chenjj.concurrent.threadSync;

import java.util.HashMap;

/**
 * HashMap在多线程同时写的情况下，如果不对其进行同步封装，则很容易出现死循环引起的死锁，程序运行一段时间后
 * CPU等资源居高不下。
 */
public class HashMapDeadLock {
    private final HashMap<String, String> map = new HashMap<>();

    public void add(String key, String value) {
        this.map.put(key, value);
    }

    public static void main(String[] args) {
        final HashMapDeadLock hashMapDeadLock = new HashMapDeadLock();
        for (int x = 0; x < 2; x++) {
            new Thread(() -> {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    hashMapDeadLock.add(String.valueOf(i), String.valueOf(i));
                }
            }).start();
        }
    }
}

package com.chenjj.concurrent.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TryLockTest {
    private final static Object VAL_OBJ = new Object();

    public static void main(String[] args) {
        final TryLock lock = new TryLock();
        final List<Object> validation = new ArrayList<>();
        IntStream.range(1, 10).forEach(i -> {
            new Thread(() -> {
                while (true) {
                    try {
                        if (lock.tryLock()) {
                            System.out.println(Thread.currentThread() + ": get the lock.");
                            // 确保validation中存在一个元素
                            if (validation.size() > 1) {
                                throw new IllegalStateException("validation failed");
                            }
                            validation.add(VAL_OBJ);
                        } else {
                            // 未获得锁，简单做个休眠，防止出现CPU过高电脑死机的情况
                        }
                        TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (lock.release()) {
                            System.out.println(Thread.currentThread() + ":release the lock.");
                            validation.remove(VAL_OBJ);
                        }
                    }
                }
            }).start();
        });
    }
}

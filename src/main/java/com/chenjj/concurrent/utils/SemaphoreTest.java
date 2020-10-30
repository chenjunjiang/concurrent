package com.chenjj.concurrent.utils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 模拟某个登录系统，最多限制给定数量的人员同时在线，如果所能申请的许可证不足，那么将告诉用户无法登录，稍后重试。
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        final int MAX_PERMIT_LOGIN_ACCOUNT = 10;
        final LoginService loginService = new LoginService(MAX_PERMIT_LOGIN_ACCOUNT);
        IntStream.range(0, 20).forEach(i -> {
            new Thread(() -> {
                boolean login = loginService.login();
                if (!login) {
                    System.out.println(Thread.currentThread() + " is refused due to exceed max online account.");
                    return;
                }
                try {
                    // 简单模拟登录成功后的系统操作
                    simulateWork();
                } finally {
                    loginService.logout();
                }
            }, "User-" + i).start();
        });
    }

    private static void simulateWork() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class LoginService {
        private final Semaphore semaphore;

        public LoginService(int maxPermitLoginAccount) {
            this.semaphore = new Semaphore(maxPermitLoginAccount);
        }

        /*public boolean login() {
            // tryAcquire不会阻塞
            boolean login = semaphore.tryAcquire();
            if (login) {
                System.out.println(Thread.currentThread() + " login success.");
            }
            return login;
        }*/

        public boolean login() {
            // tryAcquire不会阻塞
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread() + " login success.");
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        /**
         * 释放许可证
         */
        public void logout() {
            semaphore.release();
            System.out.println(Thread.currentThread() + " logout success.");
        }
    }
}

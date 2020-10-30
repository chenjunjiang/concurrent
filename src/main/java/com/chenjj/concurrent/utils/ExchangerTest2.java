package com.chenjj.concurrent.utils;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 一个线程生成数据，另外一个线程处理数据。也就是说其中A线程会用到B线程交换过来的数据，
 * 而B线程压根不会用到（忽略）A线程交换过来的数据。
 */
public class ExchangerTest2 {
    public static void main(String[] args) throws InterruptedException {
        final Exchanger<String> exchanger = new Exchanger<>();
        StringGenerator generator = new StringGenerator(exchanger, "Generator");
        StringConsumer consumer = new StringConsumer(exchanger, "Consumer");
        consumer.start();
        generator.start();
        TimeUnit.MINUTES.sleep(1);
        consumer.close();
        generator.close();
    }

    private interface Closable {
        // 关闭当前线程
        void close();

        // 判断当前线程是否关闭
        boolean closed();
    }

    private abstract static class ClosableThread extends Thread implements Closable {
        protected final Exchanger<String> exchanger;
        private volatile boolean closed = false;

        protected ClosableThread(Exchanger<String> exchanger, final String name) {
            super(name);
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            // 当前线程未关闭时不断地执行doExchange()方法
            while (!closed) {
                this.doExchange();
            }
        }

        /**
         * 关闭当前线程
         */
        @Override
        public void close() {
            System.out.println(this.getName() + " will be closed.");
            this.closed = true;
            this.interrupt();
        }

        @Override
        public boolean closed() {
            return this.closed || this.isInterrupted();
        }

        protected abstract void doExchange();
    }

    private static class StringGenerator extends ClosableThread {
        private char initialChar = 'A';

        protected StringGenerator(Exchanger<String> exchanger, String name) {
            super(exchanger, name);
        }

        @Override
        protected void doExchange() {
            // 模拟复杂的数据生成过程
            String str = "";
            for (int i = 0; i < 3; i++) {
                randomSleep();
                str += (initialChar++);
            }
            try {
                // 如果当前线程未关闭，则执行exchange方法
                if (!this.closed()) {
                    // 不关心另外一个线程交换过来的数据
                    exchanger.exchange(str);
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread() + " received the close signal.");
            }
        }
    }

    private static class StringConsumer extends ClosableThread {

        protected StringConsumer(Exchanger<String> exchanger, String name) {
            super(exchanger, name);
        }

        @Override
        protected void doExchange() {
            try {
                // 如果当前线程未关闭，则执行exchange方法
                if (!this.closed()) {
                    String data = exchanger.exchange(null);
                    System.out.println("received the data:" + data);
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread() + " received the close signal.");
            }
        }
    }

    private static void randomSleep() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/19 0019.
 */
public class Producer implements Runnable {

    private Queue queue;

    public Producer(Queue queue) {
        this.queue = queue;
        new Thread(this).start();
    }

    @Override
    public void run() {
        int number = 0;
        while (number<10) {
            queue.put(number++);
        }
    }
}

package com.chenjj.concurrent.threadCommunication;

import java.util.LinkedList;

public class EventQueue {
    private final int max;

    static class Event {
    }

    private final LinkedList<Event> eventQueue = new LinkedList<>();

    private final static int DEFAULT_MAX_EVENT = 10;

    public EventQueue() {
        this(DEFAULT_MAX_EVENT);
    }

    public EventQueue(int max) {
        this.max = max;
    }

    public void offer(Event event) {
        synchronized (eventQueue) {
            while (eventQueue.size() >= max) {
                try {
                    console("the queue is full.");
                    /**
                     * wait()方法必须在同步方法中使用
                     * 当前线程执行了该对象的wait方法之后，将会放弃对该monitor的所有权并且进入与该对象关联的
                     * wait set中，其它线程也有机会继续争抢该monitor的所有权。
                     *
                     * 如果这里有多个线程在等待，当其它线程执行notifyAll之后，在这里等待的所有线程都会被唤醒，
                     * 但并不是所有被唤醒的线程都能立即执行，只有其中一个重新获取到对象的monitor的所有权的线程
                     * 才能继续执行，其它线程仍需要获取到对象的monitor的所有权后才能执行。
                     * The thread then waits until it can re-obtain ownership of the monitor and resumes execution.
                     */
                    eventQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            console("the new event is submitted.");
            eventQueue.addLast(event);
            /**
             * notify()的作用是唤醒那些曾经执行monitor的wait方法而进入阻塞的线程
             */
            eventQueue.notifyAll();
        }
    }

    public Event take() {
        synchronized (eventQueue) {
            while (eventQueue.isEmpty()) {
                try {
                    console("the queue is empty.");
                    eventQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Event event = eventQueue.removeFirst();
            this.eventQueue.notifyAll();
            console("the event" + event + " is handled.");
            return event;
        }
    }

    private void console(String message) {
        System.out.printf("%s:%s\n", Thread.currentThread().getName(), message);
    }
}

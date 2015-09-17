package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/17 0017.
 */
public class Caller implements Runnable {
    private String msg;
    private Callme callme;
    private Thread thread;

    public Caller(Callme callme, String msg, String threadName) {
        this.callme = callme;
        this.msg = msg;
        thread = new Thread(this, threadName);
        thread.start();
    }

    @Override
    public void run() {
        callme.call(this.msg);
        callme.callNoSynch(this.thread);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Callme getCallme() {
        return callme;
    }

    public void setCallme(Callme callme) {
        this.callme = callme;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}

package com.chenjj.concurrent.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class Tracker extends PhantomReference<Socket> {

    public Tracker(Socket socket, ReferenceQueue<Socket> queue) {
        super(socket, queue);
        // 这里只要执行这句话socket就会变成强引用
        //this.socket = socket;
    }

    public void close() {
        System.out.println("再次释放资源......");
    }
}

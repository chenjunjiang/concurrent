package com.chenjj.concurrent;

/**
 * Created by Administrator on 2015/9/19 0019.
 * IDEA快捷键 如 System.out.println（sout Tab键）main方法（psvm Tab键）
 */
public class TestProducerAndConsumer {
    public static void main(String[] args) {
        Queue queue = new Queue();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
    }
}

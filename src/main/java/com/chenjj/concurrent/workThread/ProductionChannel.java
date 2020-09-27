package com.chenjj.concurrent.workThread;

import com.chenjj.concurrent.forkjoin.Product;

/**
 * 流水线传送带
 * 在传送带上除了负责产品加工的工人，还有待加工的产品
 */
public class ProductionChannel {
    // 传送带上最多可以有多少待加工产品
    private final static int MAX_PROD = 100;
    // 待加工产品
    private final Production[] productions;
    private int tail;
    private int head;
    // 当前在流水线上有多少个待加工的产品
    private int total;
    // 流水线上的工人
    private final Worker[] workers;

    public ProductionChannel(int workerSize) {
        this.workers = new Worker[workerSize];
        this.productions = new Production[MAX_PROD];
        for (int i = 0; i < workerSize; i++) {
            workers[i] = new Worker("Worker-" + i, this);
            workers[i].start();
        }
    }

    /**
     * 往传送带上放半成品(待加工的产品)
     *
     * @param production
     */
    public void offerProduction(Production production) {
        synchronized (this) {
            // 当传送带上待加工的产品超过了最大值时则不允许往传送带上放产品
            while (total >= productions.length) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 将半成品放到传送带上
            productions[tail] = production;
            tail = (tail + 1) % productions.length;
            total++;
            this.notifyAll();
        }
    }

    /**
     * 工人从传送带上获取半成品加工
     *
     * @return
     */
    public Production takeProduction() {
        synchronized (this) {
            // 当传送带上没有半成品时，工人就等待
            while (total <= 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取半成品
            Production production = productions[head];
            head = (head + 1) % productions.length;
            total--;
            this.notifyAll();
            return production;
        }
    }
}

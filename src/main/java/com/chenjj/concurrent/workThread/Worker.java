package com.chenjj.concurrent.workThread;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Worker extends Thread {
    private final ProductionChannel productionChannel;
    // 模拟每个工人加工产品的耗时
    private final static Random random = new Random(System.currentTimeMillis());

    public Worker(String workerName, ProductionChannel productionChannel) {
        super(workerName);
        this.productionChannel = productionChannel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 从传送带上获取半成品
                Production production = productionChannel.takeProduction();
                System.out.println(getName() + " process the " + production);
                // 加工
                production.create();
                TimeUnit.SECONDS.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.chenjj.concurrent.workThread;

/**
 * 产品除了说明书以外还需要有产品自身的属性
 */
public class Production extends InstructionBook {
    // 产品编号
    private final int proID;

    public Production(int proID) {
        this.proID = proID;
    }

    @Override
    public void firstProcess() {
        System.out.println("execute the " + proID + " first process");
    }

    @Override
    public void secondProcess() {
        System.out.println("execute the " + proID + " second process");
    }
}

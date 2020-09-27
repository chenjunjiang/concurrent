package com.chenjj.concurrent.workThread;

/**
 * 产品组装说明书
 * 经过流水线的产品将通过create方法进行加工
 */
public abstract class InstructionBook {
    public final void create() {
        this.firstProcess();
        this.secondProcess();
    }

    public abstract void firstProcess();

    public abstract void secondProcess();
}

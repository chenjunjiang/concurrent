package com.chenjj.concurrent.singleThreadExecution;

/**
 * 为了解决交叉锁的情况，将需要的资源统一封装在一个类中
 */
public class TablewarePair {
    private final Tableware leftTool;
    private final Tableware rightTool;

    public TablewarePair(Tableware leftTool, Tableware rightTool) {
        this.leftTool = leftTool;
        this.rightTool = rightTool;
    }

    public Tableware getLeftTool() {
        return leftTool;
    }

    public Tableware getRightTool() {
        return rightTool;
    }
}

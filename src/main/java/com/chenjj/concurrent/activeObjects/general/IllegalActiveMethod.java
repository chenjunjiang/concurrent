package com.chenjj.concurrent.activeObjects.general;

/**
 * 若方法不能被转换为Active方法时抛出该异常
 */
public class IllegalActiveMethod extends Exception {
    public IllegalActiveMethod(String message) {
        super(message);
    }
}

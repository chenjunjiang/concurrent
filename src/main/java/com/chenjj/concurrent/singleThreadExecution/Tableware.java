package com.chenjj.concurrent.singleThreadExecution;

/**
 * 虽然使用synchronized关键字可以保证single thread execution，但是如果使用不当则会导致死锁的情况发生，
 * 比如A手持刀等待B放下叉，而B手持叉等待A放下刀
 */
public class Tableware {
    // 餐具名称
    private final String tooName;

    public Tableware(String toolName) {
        this.tooName = toolName;
    }

    @Override
    public String toString() {
        return "Tableware{" +
                "tooName='" + tooName + '\'' +
                '}';
    }
}

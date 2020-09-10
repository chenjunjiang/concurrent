package com.chenjj.concurrent.singleThreadExecution;

/**
 * 吃面线程
 * 创建两个吃面线程执行之后会出现死锁，其主要原因是交叉锁导致两个线程之间相互等待彼此释放持有锁
 */
public class EatNoodleThread extends Thread {
    private final String name;
    // 左手边的餐具
    private final Tableware leftTool;
    // 右手边的餐具
    private final Tableware rightTool;

    public EatNoodleThread(String name, Tableware leftTool, Tableware rightTool) {
        this.name = name;
        this.leftTool = leftTool;
        this.rightTool = rightTool;
    }

    @Override
    public void run() {
        while (true) {
            this.eat();
        }
    }

    /**
     * 吃面过程
     */
    private void eat() {
        synchronized (leftTool) {
            System.out.println(name + " take up " + leftTool + "(left)");
            synchronized (rightTool) {
                System.out.println(name + " take up " + rightTool + "(right)");
                System.out.println(name + " is eating now.");
                System.out.println(name + " put down " + rightTool + "(right)");
            }
            System.out.println(name + " put down " + leftTool + "(left)");
        }
    }

    public static void main(String[] args) {
        Tableware fork = new Tableware("fork");
        Tableware knife = new Tableware("knife");
        new EatNoodleThread("A", fork, knife).start();
        new EatNoodleThread("B", knife, fork).start();
    }
}

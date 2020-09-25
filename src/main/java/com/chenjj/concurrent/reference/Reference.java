package com.chenjj.concurrent.reference;

public class Reference {
    // 1M的数据
    private final byte[] data = new byte[2 << 19];

    /**
     * finalize方法会在垃圾回收的标记阶段被调用(垃圾回收器在回收一个对象前，首先会进行标记，标记过程会调用finalize)，
     * 千万不要认为该方法被调用之后，就代表对象被回收了，对象可以在finalize中"自我救赎"。
     * 该方法只会被调用一次。
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        System.out.println("the reference will be gc.");
    }
}

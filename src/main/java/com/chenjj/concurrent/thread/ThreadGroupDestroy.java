package com.chenjj.concurrent.thread;

/**
 * 原理可以看README.md
 *
 * group.isDestroyed=false
 * java.lang.ThreadGroup[name=main,maxpri=10]
 *     Thread[main,5,main]
 *     Thread[Monitor Ctrl-Break,5,main]
 *     java.lang.ThreadGroup[name=TestGroup,maxpri=10]
 *
 * group.isDestroyed=true
 * java.lang.ThreadGroup[name=main,maxpri=10]
 *     Thread[main,5,main]
 *     Thread[Monitor Ctrl-Break,5,main]
 */
public class ThreadGroupDestroy {
    public static void main(String[] args) {
        ThreadGroup group = new ThreadGroup("TestGroup");
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
        System.out.println("group.isDestroyed=" + group.isDestroyed());
        mainGroup.list();

        group.destroy();
        System.out.println("group.isDestroyed=" + group.isDestroyed());
        mainGroup.list();
    }
}

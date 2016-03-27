package com.chenjj.concurrent.threadSyncUtils;

import java.util.concurrent.Phaser;

/**
 * 使用Phaser来实现CountDownLatch相同的功能
 * 
 * @author Administrator
 *
 */
public class PhaserThread1 implements Runnable {
	private Phaser phaser;

	public PhaserThread1(int number) {
		this.phaser = new Phaser(number){// 此处可使用CountDownLatch(number)

			@Override
			protected boolean onAdvance(int phase, int registeredParties) {
				System.out.println("所有的参与线程执行完之后意味着当前阶段任务已完成，就会调用该方法");
				return super.onAdvance(phase, registeredParties);
			}
		};
	}

	public void arrive(String name) {
		System.out.printf("%s has arrived.", name);
		// 调用此方法表明当前线程的任务已经执行完毕了,不需要等待其他线程
		// 调用phaser.arriveAndAwaitAdvance();就需要等到其他线程
		phaser.arrive();// 此处可使用latch.countDown()
		System.out.printf("PhaserThread1: Waiting for %d participants.\n", phaser.getUnarrivedParties());
		// phase就是阶段，初值为0，当所有的线程执行完当前阶段任务，意味着要开始下一阶段任务了，此时phase的值自动加1。
		// 所以这里最后输出的值是1
		System.out.println(phaser.getPhase() + "---");
	}

	@Override
	public void run() {
		System.out.printf("PhaserThread1: Initialization: %d participants.\n", phaser.getUnarrivedParties());
		phaser.awaitAdvance(phaser.getPhase());// 等到所有的线程执行完当前阶段任务后(即phaser.getPhase()的值已经改变了)才继续往下执行
		System.out.println("等到所有的participants arrive之后(意味着要开始下一阶段任务了，此时phase的值自动加1)才开始执行");
	}

}

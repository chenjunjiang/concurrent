package com.chenjj.concurrent.threadSyncUtils;

public class PhaserTest1 {

	public static void main(String[] args) {
		PhaserThread1 phaserThread1 = new PhaserThread1(10);
		Thread thread = new Thread(phaserThread1);
		thread.start();
		// 这里的10个线程只执行了一个阶段就完成任务了
		for (int i = 0; i < 10; i++) {
			Phasers phasers = new Phasers(phaserThread1, "Phasers" + i);
			Thread thread1 = new Thread(phasers);
			thread1.start();
		}
	}

}

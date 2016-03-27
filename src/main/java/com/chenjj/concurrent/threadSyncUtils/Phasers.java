package com.chenjj.concurrent.threadSyncUtils;

import java.util.concurrent.TimeUnit;

public class Phasers implements Runnable {
	private PhaserThread1 phaserThread1;
	private String name;
	
	public Phasers(PhaserThread1 phaserThread1, String name) {
		this.phaserThread1 = phaserThread1;
		this.name = name;
	}

	@Override
	public void run() {
		// 让线程随机休眠一段时间,模拟实际的业务操作
		long duration = (long) (Math.random() * 10);
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		phaserThread1.arrive(name);
	}

}

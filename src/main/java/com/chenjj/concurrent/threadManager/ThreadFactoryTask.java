package com.chenjj.concurrent.threadManager;

import java.util.concurrent.TimeUnit;

public class ThreadFactoryTask implements Runnable {

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

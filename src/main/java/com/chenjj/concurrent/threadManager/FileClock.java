package com.chenjj.concurrent.threadManager;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FileClock implements Runnable {

	@Override
	public void run() {
		for (int i = 0; i <= 10; i++) {
			System.out.printf("%s\n", new Date());
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {// 5s后执行线程被打断,输出异常信息,但是线程并未中断
				// 会继续执行剩下的循环代码
				System.out.printf("The FileClock has been interrupted");
			}
		}
	}

}

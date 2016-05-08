package com.chenjj.concurrent.executor;

import java.util.concurrent.Callable;

public class Task3 implements Callable<String> {

	@Override
	public String call() throws Exception {
		while (true) {// 该方法中存在死循环,不需要返回值
			System.out.printf("Task: Test\n");
			Thread.sleep(100);
		}
	}

}

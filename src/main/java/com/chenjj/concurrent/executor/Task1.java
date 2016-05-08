package com.chenjj.concurrent.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Task1 implements Callable<Result> {

	private String name;

	public Task1(String name) {
		this.name = name;
	}

	@Override
	public Result call() {
		System.out.printf("%s: Staring\n", this.name);
		long duration = (long) (Math.random() * 10);
		System.out.printf("%s: Waiting %d seconds for results.\n", this.name, duration);
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int value = 0;
		for (int i = 0; i < 5; i++) {
			value += (int) (Math.random() * 100);
		}
		Result result = new Result();
		result.setName(this.name);
		result.setValue(value);
		System.out.println(this.name + ": Ends");

		return result;
	}

}

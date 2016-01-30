package com.chenjj.concurrent.threadManager;

public class ExceptionTask implements Runnable {

	@Override
	public void run() {
		Integer.parseInt("TTT");
		System.out.println("抛出异常了还会继续执行吗?");// 抛出异常后不继续向下执行
	}

}

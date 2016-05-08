package com.chenjj.concurrent.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Task3Main {

	public static void main(String[] args) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		Task3 task3 = new Task3();
		System.out.printf("Main: Executing the Task\n");
		// 执行submit后不会阻塞当前主线程,主线程会继续往下执行
		Future<String> result = executor.submit(task3);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Main: Done: %s\n", result.isDone());
		System.out.printf("Main: Canceling the Task\n");
		result.cancel(true);
		// 将isCancelled()方法和isDone()的调用结果写入控制台，验证任务已取消，因此，已完成。
		System.out.printf("Main: Canceled: %s\n", result.isCancelled());
		System.out.printf("Main: Done: %s\n", result.isDone());
		executor.shutdown();
		System.out.printf("Main: The executor has finished\n");
	}

}

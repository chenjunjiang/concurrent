package com.chenjj.concurrent.executor;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Task2Main {

	public static void main(String[] args) {
		ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
		System.out.printf("Main: Starting at: %s\n", new Date());
		for (int i = 0; i < 5; i++) {
			Task2 task2 = new Task2("Task" + i);
			executor.schedule(task2, i + 1, TimeUnit.SECONDS);
		}
		// This method does not wait for previously submitted tasks to complete execution.
		// Use awaitTermination to do that. 
		executor.shutdown();
		executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		try {
			// 阻塞当前线程直到所有的任务完成,该方法必须在shutdown()方法之后执行
			//Blocks until all tasks have completed execution after a shutdown request, 
			// or the timeout occurs, or the current thread is interrupted, whichever happens first.
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("执行完了:"+executor.getCompletedTaskCount());
		System.out.printf("Main: Ends at: %s\n", new Date());
	}

}

package com.chenjj.concurrent.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class SearchNumberTaskMain {

	public static void main(String[] args) {
		ArrayGenerator generator = new ArrayGenerator();
		int array[] = generator.generateArray(1000);
		TaskManager manager = new TaskManager();
		// 默认的线程大小java.lang.Runtime.availableProcessors
		ForkJoinPool pool = new ForkJoinPool();
		SearchNumberTask task = new SearchNumberTask(array, 0, 1000, 5, manager);
		pool.execute(task);
		pool.shutdown();
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/*try {
			int result = task.get();// 由于task已经被cancel了,所以通过get()方法获取返回值时会报
			// java.util.concurrent.CancellationException
			System.out.println(result);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}*/
		System.out.printf("Main: The program has finished\n");
	}

}

package com.chenjj.concurrent.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class DocumentTaskMain {

	public static void main(String[] args) {
		// 创建一个带有100行，每行1000个单词的Document
		Document documentObj = new Document();
		String[][] document = documentObj.generateDocument(100, 1000, "the");
		DocumentTask documentTask = new DocumentTask(document, 0, 100, "the");
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(documentTask);
		do {
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
			System.out.printf("******************************************\n");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!documentTask.isDone());
		pool.shutdown();
		// 使用awaitTermination()方法等待任务的结束。
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			System.out.printf("Main: The word appears %d in the document", documentTask.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}

package com.chenjj.concurrent.executor;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletionServiceMain {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<String> service = new ExecutorCompletionService<String>(executor);
		ReportRequest faceRequest = new ReportRequest("Face", service);
		ReportRequest onlineRequest = new ReportRequest("online", service);
		Thread faceThread = new Thread(faceRequest);
		Thread onlineThread = new Thread(onlineRequest);
		ReportProcessor processor = new ReportProcessor(service);
		Thread senderThread = new Thread(processor);

		System.out.printf("Main: Starting the Threads\n");
		faceThread.start();
		onlineThread.start();
		senderThread.start();

		try {
			Thread.currentThread().sleep(200);
			// 使用shutdown()方法关闭执行者，使用awaitTermination()方法等待任务的结果。
			System.out.printf("Main: Waiting for the report generators.\n");
			executor.shutdown();

			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		processor.setEnd(true);
		System.out.println("Main: Ends");
	}

}

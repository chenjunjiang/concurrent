package com.chenjj.concurrent.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Task1Main {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Task1> task1List = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Task1 task1 = new Task1(i + "任务");
			task1List.add(task1);
		}
		List<Future<Result>> resultList = null;
		try {
			resultList = executor.invokeAll(task1List);// 等待所有任务完成
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
		System.out.println("Main: Printing the results");
		for (int i = 0, len = resultList.size(); i < len; i++) {
			Future<Result> future = resultList.get(i);
			try {
				Result result = future.get();
				System.out.println(result.getName() + ": " + result.getValue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}

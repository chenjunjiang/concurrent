package com.chenjj.concurrent.forkjoin;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class SearchNumberTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 8566951641695537256L;

	private final static int NOT_FOUND = -1;

	private int numbers[];
	private int start;
	private int end;
	private int number;
	private TaskManager manager;

	public SearchNumberTask(int[] numbers, int start, int end, int number, TaskManager manager) {
		this.numbers = numbers;
		this.start = start;
		this.end = end;
		this.number = number;
		this.manager = manager;
	}

	@Override
	protected Integer compute() {
		System.out.println("Task: " + start + ":" + end);
		// 如果start和end之差大于10（这个任务将处理超过10个元素的数组），调用launchTasks()方法，将这个任务的工作拆分成两个任务。
		int ret;
		if (end - start > 10) {
			ret = launchTasks();
		} else {
			ret = lookForNumber();
		}
		return ret;
	}

	private int lookForNumber() {
		for (int i = start; i < end; i++) {
			if (numbers[i] == number) {
				System.out.printf("Task: Number %d found in position %d\n", number, i);
				manager.cancelTasks(this);
				return i;
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return NOT_FOUND;
	}

	private int launchTasks() {
		int mid = (start + end) / 2;
		SearchNumberTask task1 = new SearchNumberTask(numbers, start, mid, number, manager);
		SearchNumberTask task2 = new SearchNumberTask(numbers, mid, end, number, manager);
		manager.addTask(task1);
		manager.addTask(task2);
		task1.fork();
		task2.fork();
		int returnValue;
		returnValue = task1.join();
		if (returnValue != NOT_FOUND) {
			return returnValue;
		}
		returnValue = task2.join();

		return returnValue;
	}

	public void writeCancelMessage() {
		System.out.printf("Task: Canceled task from %d to %d", start, end);

	}

}

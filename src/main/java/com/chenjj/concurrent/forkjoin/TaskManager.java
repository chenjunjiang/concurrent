package com.chenjj.concurrent.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

// 创建一个TaskManager类。我们将使用这个类来存储在ForkJoinPool中执行的所有任务。
// 由于ForkJoinPool和ForkJoinTask类的局限性，你将使用这个类来取消ForkJoinPool类的所有任务。
public class TaskManager {

	private List<ForkJoinTask<Integer>> tasks;

	public TaskManager() {
		tasks = new ArrayList<>();
	}

	public void addTask(ForkJoinTask<Integer> task) {
		tasks.add(task);
	}

	public void cancelTasks(ForkJoinTask<Integer> cancelTask) {
		for (ForkJoinTask<Integer> task : tasks) {
			if (task != cancelTask) {
				boolean result = task.cancel(true);
				System.out.println("任务" + task.toString() + "正常结束了吗?" + task.isCompletedNormally());
				System.out.println("任务" + task.toString() + "被取消成功了吗?" + result);
				((SearchNumberTask)task).writeCancelMessage();
			}
		}
	}
}

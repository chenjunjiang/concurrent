package com.chenjj.concurrent.forkjoin;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ExceptionTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 8425612440965497805L;

	private int[] array;
	private int start;
	private int end;

	public ExceptionTask(int[] array, int start, int end) {
		super();
		this.array = array;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		System.out.printf("Task: Start from %d to %d\n", start, end);
		if (end - start < 10) {
			if (3 > start && 3 < end) {
				// throw new RuntimeException("This task throws an" + "Exception: Task from " + start + " to " + end);
				Exception e = new Exception("This task throws an Exception: " + "Task from " + start + " to " + end);
				completeExceptionally(e);
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			int mid = (end + start) / 2;
			ExceptionTask task1 = new ExceptionTask(array, start, mid);
			ExceptionTask task2 = new ExceptionTask(array, mid, end);
			invokeAll(task1, task2);
		}
		System.out.printf("Task: End form %d to %d\n", start, end);

		return 0;
	}

}

package com.chenjj.concurrent.forkjoin;

import java.util.concurrent.RecursiveTask;

// 创建LineTask类，指定它继承RecursiveTask类，并参数化为Integer类型。这个类将实现统计单词在一行中出现的次数的任务。
public class LineTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 6097103813654248092L;

	private String line[];
	private int start;
	private int end;
	private String word;

	public LineTask(String[] line, int start, int end, String word) {
		this.line = line;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	@Override
	protected Integer compute() {
		int result = 0;
		if (end - start < 100) {
			result = count(line, start, end, word);
		} else {
			int mid = (start + end) / 2;
			LineTask task1 = new LineTask(line, start, mid, word);
			LineTask task2 = new LineTask(line, mid, end, word);
			invokeAll(task1, task2);
			try {
				// groupResults()方法,它合计两个数的值，并返回这个结果。
				result = groupResults(task1.get(), task2.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private int count(String[] line, int start, int end, String word) {
		int counter = 0;
		for (int i = start; i < end; i++) {
			if (line[i].equals(word)) {
				counter++;
			}
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return counter;
	}

	private int groupResults(Integer number1, Integer number2) {
		if (number1 != null && number2 != null) {
			return number1 + number2;
		}
		return 0;
	}
}

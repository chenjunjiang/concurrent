package com.chenjj.concurrent.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

// 创建一个DocumentTask类，指定它继承RecursiveTask类，并参数化为Integer类型。该类将实现统计单词在一组行中出现的次数的任务。
public class DocumentTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 5538061461521185376L;

	private String[][] document;
	private int start;
	private int end;
	private String word;

	public DocumentTask(String document[][], int start, int end, String word) {
		this.document = document;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	@Override
	protected Integer compute() {
		int result = 0;
		// 如果属性end和start的差小于10，那么这个任务统计该单词位于行在调用processLines()方法的这些位置中出现的次数。
		if (end - start < 10) {
			result = processLines(document, start, end, word);
		} else {
			// 否则，用两个对象分解行组，创建两个新的DocumentTask对象用来处理这两个组，并且在池中使用invokeAll()方法来执行它们。
			int mid = (start + end) / 2;
			DocumentTask task1 = new DocumentTask(document, start, mid, word);
			DocumentTask task2 = new DocumentTask(document, mid, end, word);
			invokeAll(task1, task2);
			try {
				result = groupResults(task1.get(), task2.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private int processLines(String[][] document, int start, int end, String word) {
		List<LineTask> tasks = new ArrayList<LineTask>();
		for (int i = start; i < end; i++) {
			LineTask task = new LineTask(document[i], 0, document[i].length, word);
			tasks.add(task);
		}
		invokeAll(tasks);
		// 合计所有这些任务返回的值，并返回这个结果。
		int result = 0;
		for (int i = 0, len = tasks.size(); i < len; i++) {
			LineTask task = tasks.get(i);
			try {
				result = result + task.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private int groupResults(Integer number1, Integer number2) {
		if (number1 != null && number2 != null) {
			return number1 + number2;
		}
		return 0;
	}
}

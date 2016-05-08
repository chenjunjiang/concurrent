package com.chenjj.concurrent.forkjoin;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public class Task extends RecursiveAction {

	private static final long serialVersionUID = -7919359295439949186L;

	private List<Product> products;
	// 声明两个私有的、int类型的属性first和last。这些属性将决定这个任务产品的阻塞过程。
	private int first;
	private int last;
	private double increment;

	public Task(List<Product> products, int first, int last, double increment) {
		this.products = products;
		this.first = first;
		this.last = last;
		this.increment = increment;
	}

	@Override
	protected void compute() {
		// 如果last和first的差小于10（任务只能更新价格小于10的产品），使用updatePrices()方法递增地设置产品的价格。
		if (last - first < 10) {
			updatePrices();
		} else {
			// 如果last和first的差大于或等于10，则创建两个新的Task对象，一个处理产品的前半部分，另一个处理产品的后半部分，
			// 然后在ForkJoinPool中，使用invokeAll()方法执行它们。
			int middle = (first + last) / 2;
			System.out.printf("Task: Pending tasks:%s\n", getQueuedTaskCount());
			Task task1 = new Task(products, first, middle + 1, increment);
			Task task2 = new Task(products, middle + 1, last, increment);
			invokeAll(task1, task2);
		}
	}

	private void updatePrices() {
		for (int i = first; i < last; i++) {
			Product product = products.get(i);
			product.setPrice(product.getPrice() * (1 + increment));
		}
	}

}

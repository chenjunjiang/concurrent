package com.chenjj.concurrent.forkjoin;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class TaskMain {

	public static void main(String[] args) {
		ProductListGenerator generator = new ProductListGenerator();
		List<Product> products = generator.generate(10000);
		Task task = new Task(products, 0, products.size(), 0.20);
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);
		do {
			System.out.printf("Main: Thread Count: %d\n", pool.getActiveThreadCount());
			System.out.printf("Main: Thread Steal: %d\n", pool.getStealCount());
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());
		pool.shutdown();
		// 使用isCompletedNormally()方法检查假设任务完成时没有出错，在这种情况下，写入一条信息到控制台。
		if (task.isCompletedNormally()) {
			System.out.printf("Main: The process has completed normally.\n");
		}
		for (int i = 0, len = products.size(); i < len; i++) {
			Product product = products.get(i);
			// 在增长之后，所有产品的价格应该是12。将价格不是12的所有产品的名称和价格写入到控制台，用来检查它们错误地增长它们的价格。
			if (product.getPrice() != 12) {
				System.out.printf("Product %s: %f\n", product.getName(), product.getPrice()+" 价格不等于12");
			}
		}
		System.out.println("Main: End of the program.\n");
	}

}

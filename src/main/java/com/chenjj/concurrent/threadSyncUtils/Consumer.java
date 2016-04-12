package com.chenjj.concurrent.threadSyncUtils;

import java.util.List;
import java.util.concurrent.Exchanger;

//8. 现在, 来实现consumer。创建一个类名为Consumer并一定实现 Runnable 接口。
public class Consumer implements Runnable {
	// 9. 声明名为buffer的 List<String>对象。这个对象类型是用来相互交换的。
	private List<String> buffer;
	// 10. 声明一个名为exchanger的 Exchanger<List<String>> 对象。用来同步 producer和consumer。
	private final Exchanger<List<String>> exchanger;

	// 11. 实现类的构造函数，并初始化2个属性。
	public Consumer(List<String> buffer, Exchanger<List<String>> exchanger) {
		this.buffer = buffer;
		this.exchanger = exchanger;
	}

	// 12. 实现 run() 方法。在方法内，实现10次交换。
	@Override
	public void run() {
		int cycle = 1;
		for (int i = 0; i < 10; i++) {
			System.out.printf("Consumer: Cycle %d\n", cycle);
			// 13.
			// 在每次循环，首先调用exchange()方法来与producer同步。Consumer需要消耗数据。此方法可能会抛出InterruptedException异常,
			// 加上处理代码。
			try {
				// 第一个调用 exchange()方法的线程会进入休眠直到其他线程的达到，线程都到达之后开始交换数据
				buffer = exchanger.exchange(buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 14.把producer发来的在buffer里的10字符串写到操控台并从buffer内删除，留空。System.out.println("Consumer:" + buffer.size());
			for (int j = 0; j < 10; j++) {
				String message = buffer.get(0);
				System.out.println("Consumer: " + message);
				System.out.println("Consumer: " + buffer.size());
				buffer.remove(0);
			}
			cycle++;
		}
	}

}

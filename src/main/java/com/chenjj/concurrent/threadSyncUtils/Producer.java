package com.chenjj.concurrent.threadSyncUtils;

import java.util.List;
import java.util.concurrent.Exchanger;

public class Producer implements Runnable {
	// 1. 首先，从实现producer开始吧。创建一个类名为Producer并一定实现 Runnable 接口。
	private List<String> buffer;
	// 2. 声明 List<String>对象，名为 buffer。这是等等要被相互交换的数据类型。
	private final Exchanger<List<String>> exchanger;

	// 3. 声明 Exchanger<List<String>>; 对象，名为exchanger。这个 exchanger
	// 对象是用来同步producer和consumer的。
	public Producer(List<String> buffer, Exchanger<List<String>> exchanger) {
		this.buffer = buffer;
		this.exchanger = exchanger;
	}

	// 5. 实现 run() 方法. 在方法内，实现10次交换。
	@Override
	public void run() {
		int cycle = 1;
		for (int i = 0; i < 10; i++) {
			System.out.printf("Producer: Cycle %d\n", cycle);
			// 6. 在每次循环中，加10个字符串到buffer。
			for (int j = 0; j < 10; j++) {
				String message = "Event " + ((i * 10) + j);
				System.out.printf("Producer: %s\n", message);
				buffer.add(message);
			}
			try {
				// 7. 调用 exchange() 方法来与consumer交换数据。此方法可能会抛出InterruptedException 异常, 加上处理代码。
				// 第一个调用 exchange()方法的线程会进入休眠直到其他线程的达到，线程都到达之后开始交换数据
				buffer = exchanger.exchange(buffer);// 返回其他线程(在这里就是consumer)提供的数据
				System.out.println("Producer: " + buffer.size());
				cycle++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

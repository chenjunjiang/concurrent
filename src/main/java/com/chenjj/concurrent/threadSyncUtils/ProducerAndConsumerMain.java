package com.chenjj.concurrent.threadSyncUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

//15.现在，实现例子的主类通过创建一个类，名为Core并加入 main() 方法。
public class ProducerAndConsumerMain {

	public static void main(String[] args) {
		// 16. 创建2个buffers。分别给producer和consumer使用.
		List<String> buffer1 = new ArrayList<String>();
		List<String> buffer2 = new ArrayList<String>();
		// 17. 创建Exchanger对象，用来同步producer和consumer。
		Exchanger<List<String>> exchanger = new Exchanger<>();
		// 18. 创建Producer对象和Consumer对象。
		Producer producer = new Producer(buffer1, exchanger);
		Consumer consumer = new Consumer(buffer2, exchanger);
		// 19. 创建线程来执行producer和consumer并开始线程。
		Thread threadProducer = new Thread(producer);
		Thread threadConsumer = new Thread(consumer);
		threadProducer.start();
		threadConsumer.start();
	}

}

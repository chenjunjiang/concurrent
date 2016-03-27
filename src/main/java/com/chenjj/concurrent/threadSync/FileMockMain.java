package com.chenjj.concurrent.threadSync;

public class FileMockMain {

	public static void main(String[] args) {
		FileMock mock = new FileMock(100, 10);
		Buffer buffer = new Buffer(20);
		Producer2 producer2 = new Producer2(mock, buffer);
		Thread threadProducer = new Thread(producer2, "Producer");
		Consumer2[] consumers = new Consumer2[3];
		Thread[] threadConsumers = new Thread[3];
		for (int i = 0; i < 3; i++) {
			consumers[i] = new Consumer2(buffer);
			threadConsumers[i] = new Thread(consumers[i], "Consumer " + i);
		}
		threadProducer.start();
		for (int i = 0; i < 3; i++) {
			threadConsumers[i].start();
		}
	}

}

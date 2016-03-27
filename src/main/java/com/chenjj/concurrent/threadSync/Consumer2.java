package com.chenjj.concurrent.threadSync;

import java.util.Random;

public class Consumer2 implements Runnable{
	private Buffer buffer;
	
	public Consumer2(Buffer buffer){
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		while(buffer.hasPendingLines()){
			String line = buffer.get();
			processLine(line);
		}
	}

	private void processLine(String line) {
		Random random = new Random();
		try {
			Thread.sleep(random.nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

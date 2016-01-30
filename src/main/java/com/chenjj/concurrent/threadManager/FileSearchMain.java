package com.chenjj.concurrent.threadManager;

import java.util.concurrent.TimeUnit;

public class FileSearchMain {

	public static void main(String[] args) {
		FileSearch searcher = new FileSearch("C:\\", "amdkmpfd.cbz");
		Thread thread = new Thread(searcher);
		thread.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread.interrupt();
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("线程是否被中断:"+thread.isInterrupted());
	}

}

package com.chenjj.concurrent.threadSync;

import java.util.Date;
import java.util.LinkedList;

public class EventStorage {
	private int maxSize;
	private LinkedList<Date> storage;

	public EventStorage() {
		maxSize = 10;
		storage = new LinkedList<Date>();
	}

	public synchronized void set() {
		while (storage.size() == maxSize) {// 使用while防止假唤醒
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		storage.offer(new Date());
		System.out.printf("Set: %d", storage.size());
		System.out.println("+++++++++++++++++");
		notifyAll();
	}

	public synchronized void get() {
		while (storage.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Get: %d: %s", storage.size(), storage.poll());
		System.out.println("----------------");
		notifyAll();
	}
}

package com.chenjj.concurrent.threadSync;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PricesInfo {
	private double price1;
	private double price2;
	private ReadWriteLock lock;// 读写锁

	public PricesInfo() {
		price1 = 0;
		price2 = 0;
		lock = new ReentrantReadWriteLock();
	}

	/**
	 * 读锁控制price1的访问
	 * 
	 * @return
	 */
	public double getPrice1() {
		lock.readLock().lock();
		double value = price1;
		lock.readLock().unlock();
		return value;
	}

	/**
	 * 读锁控制price2的访问
	 * 
	 * @return
	 */
	public double getPrice2() {
		lock.readLock().lock();
		double value = price2;
		lock.readLock().unlock();
		return value;
	}

	public void setPrices(double price1, double price2) {
		lock.writeLock().lock();
		this.price1 = price1;
		this.price2 = price2;
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lock.writeLock().unlock();
	}
}

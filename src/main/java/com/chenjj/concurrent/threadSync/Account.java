package com.chenjj.concurrent.threadSync;

import java.util.concurrent.TimeUnit;

public class Account {

	private double balance;

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public synchronized void addAmount(double amount) {
		double tmp = balance;
		try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tmp += amount;
		balance = tmp;
		System.out.println("账户添加了:"+amount);
		System.out.println("添加后,账户余额:"+balance);
	}

	public synchronized void subtractAmount(double amount) {
		double tmp = balance;
		try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tmp -= amount;
		balance = tmp;
		System.out.println("账户取走了:"+amount);
		System.out.println("取走后,账户余额:"+balance);
	}
}

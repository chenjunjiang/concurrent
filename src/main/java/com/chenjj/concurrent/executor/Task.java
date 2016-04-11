package com.chenjj.concurrent.executor;

import java.util.Date;
import java.util.concurrent.TimeUnit;

// 1.首先，实现能被服务器执行的任务。创建实现Runnable接口的Task类。
public class Task implements Runnable {
	// 2.声明一个类型为Date，名为initDate的属性，来存储任务创建日期，和一个类型为String，名为name的属性，来存储任务的名称。
	private Date initDate;
	private String name;
	
	// 3.实现Task构造器，初始化这两个属性。
	public Task(String name){
		this.initDate = new Date();
		this.name = name;
	}
	
	// 4.实现run()方法。
	@Override
	public void run() {
		// 5.首先，将initDate属性和实际日期（这是任务的开始日期）写入到控制台。
		System.out.printf("%s: Task %s: Created on: %s\n",Thread.currentThread().getName(),name,initDate);
		System.out.printf("%s: Task %s: Started on: %s\n",Thread.currentThread().getName(),name,new Date());
		// 6.然后，使任务睡眠一个随机时间。
		long duration = (long) (Math.random()*10);
		System.out.printf("%s: Task %s: Doing a task during %dseconds\n",Thread.currentThread().getName(),name,duration);
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 7.最后，将任务完成时间写入控制台。
		System.out.printf("%s: Task %s: Finished on: %s\n",Thread.currentThread().getName(),name,new Date());
	}

}

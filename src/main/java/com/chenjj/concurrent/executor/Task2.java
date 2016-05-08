package com.chenjj.concurrent.executor;

import java.util.Date;
import java.util.concurrent.Callable;

public class Task2 implements Callable<String> {

	private String name;
	
	public Task2(String name){
		this.name = name;
	}
	
	@Override
	public String call() throws Exception {
		System.out.printf("%s: Starting at : %s\n",name,new Date());
		return "Hello, world";
	}

}

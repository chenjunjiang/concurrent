package com.chenjj.concurrent.executor;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UserValidator {
	private String name;
	
	public UserValidator(String name){
		this.name = name;
	}
	
	public boolean validate(String name, String password) {
		Random random = new Random();
		long duration = (long) (Math.random() * 10);
		System.out.printf("Validator %s: Validating a user during %d seconds\n", this.name, duration);
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		// 返回一个随机Boolean值。如果用户验证通过，这个方法将返回true，否则，返回false。
		return random.nextBoolean();
	}
	
	public String getName(){
		return name;
	}
}

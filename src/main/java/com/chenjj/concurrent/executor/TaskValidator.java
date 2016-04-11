package com.chenjj.concurrent.executor;

import java.util.concurrent.Callable;

public class TaskValidator implements Callable<String> {
	private UserValidator userValidator;
	private String user;
	private String password;

	public TaskValidator(UserValidator userValidator, String user, String password) {
		this.userValidator = userValidator;
		this.user = user;
		this.password = password;
	}

	@Override
	public String call() throws Exception {
		if (!userValidator.validate(user, password)) {
			System.out.printf("%s: The user has not been found\n", userValidator.getName());
			throw new Exception("Error validating user");
		}
		System.out.printf("%s: The user has been found\n", userValidator.getName());
		return userValidator.getName();
	}

}

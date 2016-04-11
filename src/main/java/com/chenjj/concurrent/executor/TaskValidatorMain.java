package com.chenjj.concurrent.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskValidatorMain {

	public static void main(String[] args) {
		String userName = "test";
		String password = "test";
		UserValidator ldapValidator = new UserValidator("LDAP");
		UserValidator dbValidator = new UserValidator("DateBase");
		TaskValidator ladpTask = new TaskValidator(ldapValidator, userName, password);
		TaskValidator dbTask = new TaskValidator(dbValidator, userName, password);

		List<TaskValidator> taskList = new ArrayList<>();
		taskList.add(ladpTask);
		taskList.add(dbTask);

		ExecutorService executorService = Executors.newCachedThreadPool();
		String result;
		try {
			result = executorService.invokeAny(taskList);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		executorService.shutdown();
		System.out.printf("Main: End of the Execution\n");
	}

}

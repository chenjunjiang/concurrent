package com.chenjj.concurrent.executor;

// 15.最后，实现这个示例的主类，创建Main类，并实现main()方法。
public class TaskMain {

	public static void main(String[] args) {
		Server server = new Server();
		for(int i=0;i<100;i++){
			Task task = new Task("Task"+i);
			server.executeTask(task);
		}
		server.endServer();
	}

}

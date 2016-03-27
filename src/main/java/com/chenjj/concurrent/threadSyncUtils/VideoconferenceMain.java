package com.chenjj.concurrent.threadSyncUtils;

//19. 最后，实现例子的 main 类通过创建一个名为 Main 的类并为其添加 main() 方法。
public class VideoconferenceMain {

	public static void main(String[] args) {
		// 20. 创建 Videoconference 对象名为 conference，将等待10个参与者。
		Videoconference conference = new Videoconference(10);
		// 21. 创建 Thread 来运行这个 Videoconference 对象并开始运行。
		Thread threadConference = new Thread(conference);
		threadConference.start();
		// 22. 创建 10个 Participant 对象，为每个对象各创建一个 Thread 对象来运行他们，开始运行全部的线程。
		for (int i = 0; i < 10; i++) {
			Participant participant = new Participant(conference, "Participant" + i);
			Thread thread = new Thread(participant);
			thread.start();
		}
	}

}

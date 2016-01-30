package com.chenjj.concurrent.threadManager;

import java.io.File;

public class FileSearch implements Runnable {
	private String initPath;
	private String fileName;

	public FileSearch(String initPath, String fileName) {
		super();
		this.initPath = initPath;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		File file = new File(initPath);
		if (file.isDirectory()) {
			try {
				directoryProcess(file);
			} catch (InterruptedException e) {
				System.out.printf("%s: The search has been interrupted", Thread.currentThread().getName());
			}
			System.out.println();
			System.out.println("线程:"+Thread.currentThread().getName()+"中断了还会执行吗?");
			System.out.println("线程状态为:"+Thread.currentThread().getState());
			System.out.println("是否被中断:"+Thread.currentThread().isInterrupted());
		}
	}

	private void directoryProcess(File file) throws InterruptedException {
		File[] list = file.listFiles();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				if (list[i].isDirectory()) {
					directoryProcess(list[i]);
				} else {
					fileProcess(list[i]);
				}
			}
		}
		if (Thread.interrupted()) {// 线程如果被中断就抛出异常
			throw new InterruptedException();
		}
	}

	private void fileProcess(File file) throws InterruptedException {
		if (fileName.equals(file.getName())) {
			System.out.printf("%s : %s\n", Thread.currentThread().getName(), file.getAbsolutePath());
		}
		if (Thread.interrupted()) {
			System.out.println("---------------"+Thread.interrupted());
			throw new InterruptedException();
		}
	}

}

package com.chenjj.concurrent.forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderProcessor extends RecursiveTask<List<String>> {

	// 声明这个类的序列号版本UID。这个元素是必需的，因为RecursiveTask类的父类，ForkJoinTask类实现了Serializable接口。
	private static final long serialVersionUID = -4469040365490170951L;

	private String path;// 这个属性将存储任务将要处理的文件夹的全路径。
	private String extension;// 这个属性将存储任务将要查找的文件的扩展名。

	public FolderProcessor(String path, String extension) {
		this.path = path;
		this.extension = extension;
	}

	@Override
	protected List<String> compute() {
		// 用来保存存储在文件夹中的文件。
		List<String> list = new ArrayList<>();
		// 用来保存将要处理存储在文件夹内的子文件夹的子任务。
		List<FolderProcessor> tasks = new ArrayList<>();
		File file = new File(path);
		File[] content = file.listFiles();
		if (content != null) {
			for (int i = 0, len = content.length; i < len; i++) {
				// 如果是子文件夹，则创建一个新的FolderProcessor对象，并使用fork()方法异步地执行它。
				if (content[i].isDirectory()) {
					FolderProcessor task = new FolderProcessor(content[i].getAbsolutePath(), extension);
					task.fork();
					tasks.add(task);
				} else {
					if (checkFile(content[i].getName())) {
						list.add(content[i].getAbsolutePath());
					}
				}
			}
			if (tasks.size() > 50) {
				System.out.printf("%s: %d tasks ran.\n", file.getAbsolutePath(), tasks.size());
			}
			addResultsFromTasks(list, tasks);
		}
		return list;
	}

	/**
	 * 对于保存在tasks中的每个任务，调用join()方法，这将等待任务执行的完成，并且返回任务的结果。使用addAll()
	 * 方法将这个结果添加到list中。
	 * 
	 * @param list
	 * @param tasks
	 */
	private void addResultsFromTasks(List<String> list, List<FolderProcessor> tasks) {
		for (FolderProcessor task : tasks) {
			list.addAll(task.join());
		}
	}

	private boolean checkFile(String name) {
		return name.startsWith(extension);
	}

}

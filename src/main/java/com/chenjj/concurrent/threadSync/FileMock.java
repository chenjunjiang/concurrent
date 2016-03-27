package com.chenjj.concurrent.threadSync;

public class FileMock {
	private String[] content;// 存储文件内容
	private int index;// 文件行数

	/**
	 * 初始化文件内容
	 * 
	 * @param size
	 * @param length
	 */
	public FileMock(int size, int length) {
		content = new String[size];
		for (int i = 0; i < size; i++) {
			StringBuilder builder = new StringBuilder(length);
			for (int j = 0; j < length; j++) {
				int indice = (int) (Math.random() * 255);
				builder.append((char) indice);
			}
			content[i] = builder.toString();
		}
		index = 0;
	}

	public boolean hasMoreLines() {
		return index < content.length;
	}

	public String getLine() {
		if (this.hasMoreLines()) {
			System.out.println("Mock: " + (content.length - index));
			return content[index++];
		}
		return null;
	}
}

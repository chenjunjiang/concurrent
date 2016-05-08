package com.chenjj.concurrent.forkjoin;

import java.util.Random;

public class Document {

	// 创建一个带有一些单词的字符串数组。这个数组将被用来生成字符串二维数组。
	private String words[] = { "the", "hello", "goodbye", "packt", "java", "thread", "pool", "random", "class", "main" };

	// 实现generateDocument()方法。它接收以下参数：行数、每行的单词数。这个例子返回一个字符串二维数组，来表示将要查找的单词。
	public String[][] generateDocument(int numLines, int numWords, String word) {
		int counter = 0;
		String[][] document = new String[numLines][numWords];
		Random random = new Random();
		for (int i = 0; i < numLines; i++) {
			for (int j = 0; j < numWords; j++) {
				int index = random.nextInt(words.length);
				document[i][j] = words[index];
				if (document[i][j].equals(word)) {
					counter++;
				}
			}
		}
		System.out.println("DocumentMock: The word appears " + counter + " times in the document");

		return document;
	}
}

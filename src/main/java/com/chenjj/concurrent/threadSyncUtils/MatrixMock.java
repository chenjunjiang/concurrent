package com.chenjj.concurrent.threadSyncUtils;

import java.util.Random;

//1.  我们从实现2个辅助类开始。首先，创建一个类名为 MatrixMock。此类随机生成一个在1-10之间的 数字矩阵，我们将从中查找数字。
public class MatrixMock {
	// 2. 声明私有 int matrix，名为 data。
	private int data[][];

	// 3. 实现类的构造函数。此构造函数将接收矩阵的行数，行的长度，和我们将要查找的数字作为参数。3个参数全部int 类型。
	public MatrixMock(int size, int length, int number) {
		// 4. 初始化构造函数将使用的变量和对象。
		int counter = 0;
		data = new int[size][length];
		Random random = new Random();
		// 5. 用随机数字填充矩阵。每生成一个数字就与要查找的数字对比，如果相等，就增加counter值。
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < length; j++) {
				data[i][j] = random.nextInt(10);
				if (data[i][j] == number) {
					counter++;
				}
			}
		}
		// 6. 最后，在操控台打印一条信息，表示查找的数字在生成的矩阵里的出现次数。此信息是用来检查线程们获得的正确结果的。
		System.out.printf("Mock: There are %d ocurrences of number in generated data.\n", counter, number); // 译者注：把字符串里的number改为%d.
	}

	// 7. 实现 getRow() 方法。此方法接收一个 int为参数，是矩阵的行数。返回行数如果存在，否则返回null。
	public int[] getRow(int row) {
		if (row >= 0 && row < data.length) {
			return data[row];
		}
		return null;
	}
}

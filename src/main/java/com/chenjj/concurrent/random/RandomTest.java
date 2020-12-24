package com.chenjj.concurrent.random;

import java.util.Random;

public class RandomTest {
    public static void main(String[] args) {
        // 创建一个默认种子的随机数生成器
        Random random = new Random();
        // 输出随机数
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt(5));
        }
    }
}

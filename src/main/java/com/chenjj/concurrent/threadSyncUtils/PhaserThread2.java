package com.chenjj.concurrent.threadSyncUtils;

import java.util.concurrent.Phaser;

public class PhaserThread2 implements Runnable {

	private char c;
	private Phaser phaser;
	
	public PhaserThread2(char c, Phaser phaser){
		this.c = c;
		this.phaser = phaser;
	}
	
	@Override
	public void run() {
		while(!phaser.isTerminated()){//只要phaser不终结就继续执行
			for(int i=0; i<10; i++) { //将当前字母打印10次
				System.out.print(c + " ");
			}
			//打印完当前字母后，将其更新为其后第三个字母，例如b更新为e，用于下一阶段打印
			c = (char) (c+3);
			if(c>'z'){//如果超出了字母z，则在phaser中动态减少一个线程，并退出循环结束本线程
				//当3个工作线程都执行此语句后，phaser中就只剩一个主线程了
				phaser.arriveAndDeregister();
			}else{
				// 所有线程执行 了这个方法之后表示它们都已经到达了阶段终点,可以一起进入下一个阶段了(如果有的话)
				phaser.arriveAndAwaitAdvance();
			}
		}
	}

}

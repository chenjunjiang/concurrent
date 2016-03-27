package com.chenjj.concurrent.threadSyncUtils;

import java.util.concurrent.Phaser;

public class PhaserTest2 {

	public static void main(String[] args) {
		
		Phaser phaser = new Phaser(3){//共有3个工作线程，因此在构造函数中赋值为3
			// 当每一个阶段执行完毕，此方法会被自动调用
			@Override
			protected boolean onAdvance(int phase, int registeredParties) {
				System.out.println("\n=========华丽的分割线=============");
				 //本例中，当只剩一个线程时，这个线程必定是主线程，返回true表示phaser终结 
				return registeredParties == 1;
			}
		};
		
		System.out.println("程序开始执行");
		for(int i=0;i<3;i++){//创建并启动3个线程 
			PhaserThread2 phaserThread2 = new PhaserThread2((char)(97+i), phaser);
			new Thread(phaserThread2).start();
		}
		
		phaser.register();//将主线程动态增加到phaser中，此句执行后phaser共管理4个线程
		while(!phaser.isTerminated()){//只要phaser不终结，主线程就循环等待
			phaser.arriveAndAwaitAdvance();// Equivalent in effect to awaitAdvance(arrive())
		}
		//跳出上面循环后，意味着phaser终结，即3个工作线程已经结束
		System.out.println("程序结束");
	}
}

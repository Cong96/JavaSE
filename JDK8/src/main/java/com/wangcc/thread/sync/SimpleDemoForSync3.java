package com.wangcc.thread.sync;

class Demo {
	public synchronized void syncmethod() {
		for (int i = 0; i < 5; i++) {
			System.out.println(Thread.currentThread().getName() + ":" + i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void nosync() {
		for (int i = 0; i < 5; i++) {
			System.out.println(Thread.currentThread().getName() + ":" + i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

public class SimpleDemoForSync3 {
	public static void main(String[] args) {
		Demo demo = new Demo();
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				demo.syncmethod();
			}
		}, "t1");
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				demo.nosync();
			}
		}, "t2");
		t1.start();
		t2.start();

	}
}

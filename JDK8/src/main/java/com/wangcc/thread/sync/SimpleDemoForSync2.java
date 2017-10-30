package com.wangcc.thread.sync;

class MyThread extends Thread {
	public MyThread(String name) {
		super(name);
	}

	public void run() {
		synchronized (this) {
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
}

public class SimpleDemoForSync2 {
	public static void main(String[] args) {
		Runnable runnable = new MyRunnable();
		Thread t1 = new MyThread("t1");
		Thread t2 = new MyThread("t2");
		t1.start();
		t2.start();

	}
}

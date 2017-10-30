package com.wangcc.thread.waitnotify;

class MyThread extends Thread {
	public MyThread(String name) {
		super(name);
	}

	public void run() {
		synchronized (this) {
			System.out.println(Thread.currentThread().getName() + " call notify()");
			// 唤醒当前的wait线程
			notify();
		}
	}
}

class MyRunnable implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized (this) {
			System.out.println(Thread.currentThread().getName() + " call notify()");
			// 唤醒当前的wait线程
			notify();
		}
	}
}

public class WaitTest {
	public static void main(String[] args) {
		Runnable runnable = new MyRunnable();
		Thread t1 = new Thread(runnable, "t1");
		// Thread t1 = new MyThread("t1");
		synchronized (t1) {
			try {
				// 启动“线程t1”
				System.out.println(Thread.currentThread().getName() + " start t1");
				t1.start();
				Thread.sleep(100);
				// 主线程等待t1通过notify()唤醒。
				System.out.println(Thread.currentThread().getName() + " wait()");
				t1.wait();

				System.out.println(Thread.currentThread().getName() + " continue");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

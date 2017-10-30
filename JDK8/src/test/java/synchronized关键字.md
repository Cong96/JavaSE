title:synchronized关键字

date:2017年10月30日22:18:19

---

### 一.可内置重入锁

 每个Java对象都可以用做一个实现同步的锁，这些锁被称为内置锁或监视器锁。线程在进入同步代码块之前会自动获取锁，并且在退出同步代码块时会自动释放锁。获得内置锁的唯一途径就是进入由这个锁保护的同步代码块或方法。（当我们调用某对象的synchronized方法时，就获取了该对象的同步锁。）

​    当某个线程请求一个由其他线程持有的锁时，发出请求的线程就会阻塞。**不同线程对同步锁的访问是互斥的。**也就是说，某时间点，对象的同步锁只能被一个线程获取到！然而，由于内置锁是可重入的，因此如果摸个线程试图获得一个已经由它自己持有的锁，那么这个请求就会成功。“重入”意味着获取锁的操作的粒度是“线程”，而不是调用。重入的一种实现方法是，为每个锁关联一个获取计数值和一个所有者线程。当计数值为0时，这个锁就被认为是没有被任何线程所持有，当线程请求一个未被持有的锁时，JVM将记下锁的持有者，并且将获取计数值置为1，如果同一个线程再次获取这个锁，计数值将递增，而当线程退出同步代码块时，计数器会相应地递减。当计数值为0时，这个锁将被释放。



### 二.synchronized关键字

   采用synchronized修饰符实现的同步机制叫做互斥锁机制，它所获得的锁叫做互斥锁。每个对象都有一个monitor(锁标记)，当线程拥有这个锁标记时才能访问这个资源，没有锁标记便进入锁池。任何一个对象系统都会为其创建一个互斥锁，这个锁是为了分配给线程的，防止打断原子操作。每个对象的锁只能分配给一个线程，因此叫做互斥锁。



- 当一个线程访问“某对象”的“synchronized方法”或者“synchronized代码块”时，其他线程对“该对象”的该“synchronized方法”或者“synchronized代码块”的访问将被阻塞。



```
package com.wangcc.thread.sync;

class MyRunnable implements Runnable {

	@Override
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

public class SimpleDemoForSync {
	public static void main(String[] args) {
		Runnable runnable = new MyRunnable();
		Thread t1 = new Thread(runnable, "t1");
		Thread t2 = new Thread(runnable, "t2");
		t1.start();
		t2.start();

	}
}

```

```
t1:0
t1:1
t1:2
t1:3
t1:4
t2:0
t2:1
t2:2
t2:3
t2:4

```

```
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

```

```
t1:0
t2:0
t2:1
t1:1
t2:2
t1:2
t2:3
t1:3
t2:4
t1:4
```



- 当一个线程访问“某对象”的“synchronized方法”或者“synchronized代码块”时，其他线程仍然可以访问“该对象”的非同步代码块。

  ```
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
  ```

  ```
  t2:0
  t1:0
  t1:1
  t2:1
  t1:2
  t2:2
  t1:3
  t2:3
  t2:4
  t1:4

  ```

  ​

   1、如果同一个方法内同时有两个或更多线程，则每个线程有自己的局部变量拷贝。

​      2、类的每个实例都有自己的对象级别锁。当一个线程访问实例对象中的synchronized同步代码块或同步方法时，该线程便获取了该实例的对象级别锁，其他线程这时如果要访问synchronized同步代码块或同步方法，便需要阻塞等待，直到前面的线程从同步代码块或方法中退出，释放掉了该对象级别锁。

​      3、访问同一个类的不同实例对象中的同步代码块，不存在阻塞等待获取对象锁的问题，因为它们获取的是各自实例的对象级别锁，相互之间没有影响。

​      4、持有一个对象级别锁不会阻止该线程被交换出来，也不会阻塞其他线程访问同一示例对象中的非synchronized代码。当一个线程A持有一个对象级别锁（即进入了synchronized修饰的代码块或方法中）时，线程也有可能被交换出去，此时线程B有可能获取执行该对象中代码的时间，但它只能执行非同步代码（没有用synchronized修饰），当执行到同步代码时，便会被阻塞，此时可能线程规划器又让A线程运行，A线程继续持有对象级别锁，当A线程退出同步代码时（即释放了对象级别锁），如果B线程此时再运行，便会获得该对象级别锁，从而执行synchronized中的代码。

​     5、持有对象级别锁的线程会让其他线程阻塞在所有的synchronized代码外。例如，在一个类中有三个synchronized方法a，b，c，当线程A正在执行一个实例对象M中的方法a时，它便获得了该对象级别锁，那么其他的线程在执行同一实例对象（即对象M）中的代码时，便会在所有的synchronized方法处阻塞，即在方法a，b，c处都要被阻塞，等线程A释放掉对象级别锁时，其他的线程才可以去执行方法a，b或者c中的代码，从而获得该对象级别锁。

​     6、使用synchronized（obj）同步语句块，可以获取指定对象上的对象级别锁。obj为对象的引用，如果获取了obj对象上的对象级别锁，在并发访问obj对象时时，便会在其synchronized代码处阻塞等待，直到获取到该obj对象的对象级别锁。当obj为this时，便是获取当前对象的对象级别锁。

​     7、类级别锁被特定类的所有示例共享，它用于控制对static成员变量以及static方法的并发访问。具体用法与对象级别锁相似。

​    8、互斥是实现同步的一种手段，临界区、互斥量和信号量都是主要的互斥实现方式。synchronized关键字经过编译后，会在同步块的前后分别形成monitorenter和monitorexit这两个字节码指令。根据虚拟机规范的要求，在执行monitorenter指令时，首先要尝试获取对象的锁，如果获得了锁，把锁的计数器加1，相应地，在执行monitorexit指令时会将锁计数器减1，当计数器为0时，锁便被释放了。由于synchronized同步块对同一个线程是可重入的，因此一个线程可以多次获得同一个对象的互斥锁，同样，要释放相应次数的该互斥锁，才能最终释放掉该锁。






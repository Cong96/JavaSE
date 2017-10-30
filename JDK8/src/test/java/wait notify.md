title:wait notify

date:2017年10月30日23:15:29

---

### 一.wait notify等方法简单介绍

在Object.java中，定义了wait(), notify()和notifyAll()等接口。wait()的作用是让当前线程进入等待状态，同时，wait()也会让当前线程释放它所持有的锁。而notify()和notifyAll()的作用，则是唤醒当前对象上的等待线程；notify()是唤醒单个线程，而notifyAll()是唤醒所有的线程。

Object类中关于等待/唤醒的API详细信息如下：
**notify()        **-- 唤醒在此对象监视器上等待的单个线程。  该方法也要在同步方法或同步块中调用，即在调用前，线程也必须要获得该对象的对象级别锁，的如果调用notify（）时没有持有适当的锁，也会抛出IllegalMonitorStateException。

​     该方法用来通知那些可能等待该对象的对象锁的其他线程。如果有多个线程等待，则线程规划器任意挑选出其中一个wait（）状态的线程来发出通知，并使它等待获取该对象的对象锁（notify后，当前线程不会马上释放该对象锁，wait所在的线程并不能马上获取该对象锁，要等到程序退出synchronized代码块后，当前线程才会释放锁，wait所在的线程也才可以获取该对象锁），但不惊动其他同样在等待被该对象notify的线程们。当第一个获得了该对象锁的wait线程运行完毕以后，它会释放掉该对象锁，此时如果该对象没有再次使用notify语句，则即便该对象已经空闲，其他wait状态等待的线程由于没有得到该对象的通知，会继续阻塞在wait状态，直到这个对象发出一个notify或notifyAll。这里需要注意：**它们等待的是被notify或notifyAll，而不是锁。**这与下面的notifyAll（）方法执行后的情况不同。 
**notifyAll() **  -- 唤醒在此对象监视器上等待的所有线程。该方法与notify（）方法的工作方式相同，重要的一点差异是：

​      notifyAll使所有原来在该对象上wait的线程统统退出wait的状态（即全部被唤醒，不再等待notify或notifyAll，但由于此时还没有获取到该对象锁，因此还不能继续往下执行），**变成等待获取该对象上的锁，一旦该对象锁被释放（notifyAll线程退出调用了notifyAll的synchronized代码块的时候），他们就会去竞争。如果其中一个线程获得了该对象锁，它就会继续往下执行，在它退出synchronized代码块，释放锁后，其他的已经被唤醒的线程将会继续竞争获取该锁，一直进行下去，直到所有被唤醒的线程都执行完毕。**
**wait()          **                               -- 让当前线程处于“等待(阻塞)状态”，“直到其他线程调用此对象的 notify() 方法或 notifyAll() 方法”，当前线程被唤醒(进入“就绪状态”)。  public final void wait()  throws InterruptedException,IllegalMonitorStateException

​     该方法用来将当前线程置入休眠状态，直到接到通知或被中断为止。在调用wait（）之前，线程必须要获得该对象的对象级别锁，即只能在同步方法或同步块中调用wait（）方法。进入wait（）方法后，当前线程释放锁。在从wait（）返回前，线程与其他线程竞争重新获得锁。如果调用wait（）时，没有持有适当的锁，则抛出IllegalMonitorStateException，它是RuntimeException的一个子类，因此，不需要try-catch结构。

 
**wait(long timeout)    **                -- 让当前线程处于“等待(阻塞)状态”，“直到其他线程调用此对象的 notify() 方法或 notifyAll() 方法，或者超过指定的时间量”，当前线程被唤醒(进入“就绪状态”)。
**wait(long timeout, int nanos)  **-- 让当前线程处于“等待(阻塞)状态”，“直到其他线程调用此对象的 notify() 方法或 notifyAll() 方法，或者其他某个线程中断当前线程，或者已超过某个实际时间量”，当前线程被唤醒(进入“就绪状态”)。显然，这两个方法是设置等待超时时间的，后者在超值时间上加上ns，精度也难以达到，因此，该方法很少使用。对于前者，如果在等待线程接到通知或被中断之前，已经超过了指定的毫秒数，则它通过竞争重新获得锁，并从wait（long）返回。另外，需要知道，如果设置了超时时间，当wait（）返回时，我们不能确定它是因为接到了通知还是因为超时而返回的，因为wait（）方法不会返回任何相关的信息。但一般可以通过设置标志位来判断，在notify之前改变标志位的值，在wait（）方法后读取该标志位的值来判断，当然为了保证notify不被遗漏，我们还需要另外一个标志位来循环判断是否调用wait（）方法。



 **深入理解**

 如果线程调用了对象的wait（）方法，那么线程便会处于该对象的等待池中，等待池中的线程不会去竞争该对象的锁。

当有线程调用了对象的notifyAll（）方法（唤醒所有wait线程）或notify（）方法（只随机唤醒一个wait线程），被唤醒的的线程便会进入该对象的锁池中，锁池中的线程会去竞争该对象锁。

 优先级高的线程竞争到对象锁的概率大，假若某线程没有竞争到该对象锁，它还会留在锁池中，唯有线程再次调用wait（）方法，它才会重新回到等待池中。而竞争到对象锁的线程则继续往下执行，直到执行完了synchronized代码块，它会释放掉该对象锁，这时锁池中的线程会继续竞争该对象锁。

### 二.简单例子分析wait ，notify

```java
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

public class WaitTest {
	public static void main(String[] args) {
      //新建线程t1
		Thread t1 = new MyThread("t1");
		//目前只有主线程在运行，他理所应当获得t1对象锁
      synchronized (t1) {
			try {
				System.out.println(Thread.currentThread().getName() + " start t1");
			//这里开启线程，t1线程就绪，但是当执行run方法的时候，发现this对象，这里即为t1对象锁已经归主线程所有，而且此时还是在主线程以t1为对象锁的代码块中，并没有释放t1对象锁，所以这时候t1线程没有执行的权利	
              t1.start();
				Thread.sleep(100);
				System.out.println(Thread.currentThread().getName() + " wait()");
			//这里调用了t1.wait()，我们需要明白wait()方法他的真正含义
              // 在调用wait的时候，线程自动释放其占有的对象锁，同时不会去申请对象锁。当线程被唤醒的时候，它才再次获得了去获得对象锁的权利。
              //执行了t1.wait()即是释放了该对象的所，也让此时拥有该对象的线程线程阻塞，这里拥有该对象的线程就是主线程，这时候主线程被阻塞，t1获得this对象锁开始执行
              t1.wait();
//当执行到notify的时候，唤醒在等待该对象同步锁的线程(只唤醒一个,如果有多个在等待),注意的是在调用此方法的时候，并不能确切的唤醒某一个等待状态的线程，而是由JVM确定唤醒哪个线程，而且不是按优先级。
//调用任意对象的notify()方法则导致因调用该对象的 wait()方法而阻塞的线程中随机选择的一个解除阻塞（但要等到获得锁后才真正可执行）。
				System.out.println(Thread.currentThread().getName() + " continue");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

```

```
main start t1
main wait()
t1 call notify()
main continue
```





- 调用obj的wait(), notify()方法前，必须获得obj锁，也就是必须写在synchronized(obj){...} 代码段内。
- 调用obj.wait()后，线程A就释放了obj的锁，否则线程B无法获得obj锁，也就无法在synchronized(obj){...} 代码段内唤醒A。
- 当obj.wait()方法返回后，线程A需要再次获得obj锁，才能继续执行。
- 如果A1,A2,A3都在obj.wait()，则B调用obj.notify()只能唤醒A1,A2,A3中的一个（具体哪一个由JVM决定）。
- obj.notifyAll()则能全部唤醒A1,A2,A3，但是要继续执行obj.wait()的下一条语句，必须获得obj锁，因此，A1,A2,A3只有一个有机会获得锁继续执行，例如A1，其余的需要等待A1释放obj锁之后才能继续执行。
-  当B调用obj.notify/notifyAll的时候，B正持有obj锁，因此，A1,A2,A3虽被唤醒，但是仍无法获得obj锁。直到B退出synchronized块，释放obj锁后，A1,A2,A3中的一个才有机会获得锁继续执行。

谈一下synchronized和wait()、notify()等的关系:

1.有synchronized的地方不一定有wait,notify

2.有wait,notify的地方必有synchronized.这是因为wait和notify不是属于线程类，而是每一个对象都具有的方法，而且，这两个方法都和对象锁有关，有锁的地方，必有synchronized。

另外，注意一点：如果要把notify和wait方法放在一起用的话，必须先调用notify后调用wait，因为如果调用完wait，该线程就已经不是currentthread了。

### 三.通知遗漏

```
package com.wangcc.thread.waitnotify;

public class MissedNotify extends Object {
	private Object proceedLock;

	public MissedNotify() {
		print("in MissedNotify()");
		proceedLock = new Object();
	}

	public void waitToProceed() throws InterruptedException {
		print("in waitToProceed() - entered");

		synchronized (proceedLock) {
			print("in waitToProceed() - about to wait()");
			proceedLock.wait();
			print("in waitToProceed() - back from wait()");
		}

		print("in waitToProceed() - leaving");
	}

	public void proceed() {
		print("in proceed() - entered");

		synchronized (proceedLock) {
			print("in proceed() - about to notifyAll()");
			proceedLock.notifyAll();
			print("in proceed() - back from notifyAll()");
		}

		print("in proceed() - leaving");
	}

	private static void print(String msg) {
		String name = Thread.currentThread().getName();
		System.out.println(name + ": " + msg);
	}

	public static void main(String[] args) {
		final MissedNotify mn = new MissedNotify();

		Runnable runA = new Runnable() {
			public void run() {
				try {
					// 休眠1000ms，大于runB中的500ms，
					// 是为了后调用waitToProceed，从而先notifyAll，后wait，
					// 从而造成通知的遗漏
					Thread.sleep(1000);
					mn.waitToProceed();
				} catch (InterruptedException x) {
					x.printStackTrace();
				}
			}
		};

		Thread threadA = new Thread(runA, "threadA");
		threadA.start();

		Runnable runB = new Runnable() {
			public void run() {
				try {
					// 休眠500ms，小于runA中的1000ms，
					// 是为了先调用proceed，从而先notifyAll，后wait，
					// 从而造成通知的遗漏
					Thread.sleep(500);
					mn.proceed();
				} catch (InterruptedException x) {
					x.printStackTrace();
				}
			}
		};

		Thread threadB = new Thread(runB, "threadB");
		threadB.start();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException x) {
		}

		// 试图打断wait阻塞
		print("about to invoke interrupt() on threadA");
		threadA.interrupt();
	}
}
```



```
main: in MissedNotify()
threadB: in proceed() - entered
threadB: in proceed() - about to notifyAll()
threadB: in proceed() - back from notifyAll()
threadB: in proceed() - leaving
threadA: in waitToProceed() - entered
threadA: in waitToProceed() - about to wait()
main: about to invoke interrupt() on threadA
java.lang.InterruptedException
	at java.lang.Object.wait(Native Method)
	at java.lang.Object.wait(Object.java:502)
	at com.wangcc.thread.waitnotify.MissedNotify.waitToProceed(MissedNotify.java:16)
	at com.wangcc.thread.waitnotify.MissedNotify$1.run(MissedNotify.java:50)
	at java.lang.Thread.run(Thread.java:748)

```


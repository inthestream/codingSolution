package com.yun.publiclibrary.java.e0;

import java.security.AccessControlContext;

/**
 * A thread is a thread of execution in a program. The Java virtual Machine allows an application to have multiple
 * threads of execution running concurrently.
 *
 * Every thread has a priority. Threads with higher priority are executed in preference to threads with lower priority.
 * Each thread may or may not also be marked as a daemon. When code running in some thread creates a new Thread object,
 * the new thread has its priority initially set equal to the priority of the creating thread, and is a daemon thread
 * if and only if the creating thread is a daemon.
 *
 * When a Java Virtual machine starts up, there is usually a single non-daemon thread(which typically calls the method
 * named main of some designated class). The Java Virtual Machine continues to execute threads until either of the following occurs:
 *
 * The exit method of class Runtime has been called and the security manager has permitted the exit operation to take place.
 * All threads that are not daemon threads have died, either by returning from the call to the run method or by
 * throwing an exception that propagates beyond the run method.
 *
 * There are two ways to create a new thread of execution. One is to declare class to be a subclass of Thread.
 * This subclass should override the run method of class Thread. An instance of the subclass can then be allocated and started.
 * For example, a thread that computes primes larger that a stated value could be written as follows:
 *
 *                  class PrimeThread extends Thread {
 *                      long minPrime;
 *                      PrimeThread(long minPrime) {
 *                          this.minPrime = minPrime;
 *                      }
 *
 *                      public void run() {
 *                          // compute primes larger that minPrime
 *                           . . .
 *                      }
 *                  }
 *
 * The following code would then create a thread and start it running:
 *
 *                  PrimeThread p = new PrimeThread(143);
 *                  p.start();
 *
 * The other way to create a thread is to declare a class that implements the Runnable interface. That class then
 * implements the run method. An instance of the class can then be allocated, passed as an argument when creating Thread,
 * and started. The same example in this other style looks like the following:
 *
 *                  class PrimeRun implements Runnable {
 *                      long minPrime;
 *                      PrimeRun(long minPrime) {
 *                          this.minPrime = minPrime;
 *                      }
 *
 *                      public void run() {
 *                          // compute primes larger that minPrime
 *                           . . .
 *                      }
 *                  }
 *
 * The following code would then create a thread and start it running:
 *
 *                  PrimeRun p = new PrimeRun(143);
 *                  new Thread(p).start();
 *
 * Every thread has a name for identification purposes. More than one thread may have the same name. If a name is not
 * specified when a thread is created, a new name is generated for it.
 *
 * Unless otherwise noted, passing a null argument to a constructor or method in this class will cause a NullPointerException to be thrown.
 */
public class Thread implements Runnable {
    /* Make sure registerNatives is the first thing <client> does. */
    private static native void registerNatives();
    static {
        registerNatives();
    }

    private volatile String name;
    private int priority;

    /* Whether or not the thread is a daemon thread. */
    private boolean daemon = false;

    /* Fields reserved for exclusive use by the JVM */
    private boolean stillborn = false;
    private long eetop;

    /* What will be run. */
    private Runnable target;

    /* The group of this thread */
    private ThreadGroup group;

    /* The context ClassLoader for this thread */
    private ClassLoader contextClassLoader;

    /* The inherited AccessControlContext of this thread */
    private AccessControlContext inheritedAccessControlContext;

    /* For autonumbering anonymous threads. */
    private static int threadInitNumber;
    private static synchronized int nextThreadNum() { return threadInitNumber++; }

    /* ThreadLocal values pertaining to this thread. This map is maintained
       by the ThreadLocal class
     */
    //ThreadLocal.ThreadLocalMap threadLocals = null;


    @Override
    public void run() {

    }
}

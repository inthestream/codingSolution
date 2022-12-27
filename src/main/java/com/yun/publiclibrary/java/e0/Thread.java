package com.yun.publiclibrary.java.e0;

import jdk.internal.HotSpotIntrinsicCandidate;
import sun.security.util.SecurityConstants;

import java.security.AccessControlContext;
import java.security.AccessController;

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
    ThreadLocal.ThreadLocalMap threadLocals = null;

    /**
     * InteritableThreadLocal values pertaining to this thread. This map is
     * maintained by the InteritableThreadLocal class.
     */
    ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;

    /**
     * The requested stack size for this thread, or 0 if the creator did not specify a stack size.
     * It is up to the VM to do whatever it likes with this number; some VMs will ignore it.
     */
    private final long stackSize;

    // JVM-private state that persists after native thread termination.
    private long nativeParkEventPointer;

    // Thread ID
    private final long tid;

    // For generating thread ID
    private static long threadSeqNumber;

    private static synchronized long nextThreadID() { return ++threadSeqNumber; }

    // Java thread status for tools, default indicates thread 'not yet started'
    private volatile int threadStatus;

    /**
     * The argument supplied to the current call to java.util.concurrent.locks.LockSupport.park: Set by (private)
     * java.util.concurrent.locks.LockSupport.setBlocker Accessed using java.util.concurrent.locks.LockSupport.getBlocker
     */
    volatile Object parkBlocker;

    /**
     * The object in which this thread is blocked in an interruptible I/O operation, if any.
     * The blocker's interrupt method should  be invoked after setting this thread's interrupt status.
     */
    private volatile Interruptible blocker;
    private final Object blockerLock = new Object();

    /**
     * Set the blocker field; invoked via jdk.internal.access.SharedSecrets from java.nio code
     */
    static void blockedOn(Interruptible b) {
        Thread me = Thread.currentThread();
        synchronized (me.blockerLock) {
            me.blocker = b;
        }
    }

    // The minimum priority that a thread can have
    public static final int MIN_PRIORITY = 1;

    // The default priority that is assigned to a thread
    public static final int NORM_PRIORITY = 5;

    // The maximum priority that a thread can have
    public static final int MAX_PRIORITY = 10;

    /**
     * Returns a reference to the currently executing thread object.
     *
     * @return the currently executing thread
     */
    @HotSpotIntrinsicCandidate
    public static native Thread currentThread();


    /**
     * A hint to the scheduler that the current thread is willing to yield its current use of a processor. The scheduler
     * is free to ignore this hint.
     *
     * Yield is a heuristic attempt to improve relative progression between threads that would otherwise overutilise a CPU.
     * Its use should be combined with detailed profiling and benchmarking to ensure that it actually has the desire effect.
     *
     * It is rarely appropriate to use this method. It may be useful for debugging or testing purposes, where it may help
     * to reproduce bugs due to race conditions. It may also be useful when designing concurrency control constructs such as
     * the ones in the java.util.concurrent.locks package.
     */
    public static native void yield();

    /**
     * Causes the currently executing thread to sleep (temporarily cease execution) for the specified number of millisecond,
     * subject to the precision and accuracy of system timers and schedulers. The thread does not lose ownership of
     * any monitors.
     *
     * @param millis the length of time to sleep in milliseconds
     * @throws IllegalArgumentException  if the value of millis is negative
     * @throws InterruptedException if any thread has interrupted the current thread.
     *         The interrupted status of the current thread is cleared when this exception is thrown.
     */
    public static native void sleep(long millis) throws InterruptedException;

    /**
     * Causes the currently executing thread to sleep (temporarily cease execution) for the specified number of milliseconds
     * plus the specified number of nanoseconds, subject to the precision and accuracy of system timers and schedulers.
     * The thread does not lose ownership of any monitors.
     *
     * @param millis the length of time to sleep in milliseconds
     * @param nanos 0-99999 additional nanoseconds to sleep
     * @throws  IllegalArgumentException if the value of millis is negative, or the value of nanos is not in the range
     * @throws InterruptedException if any thread has interrupted the current thread. The interrupted status of the current
     *                              thread is cleared when this exception is thrown
     */
    public static void sleep(long millis, int nanos) throws InterruptedException {
        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException("nanosecond timeout value out of range");
        }

        if (nanos >= 5000000 || (nanos != 0 && millis == 0)) {
            millis++;
        }
        sleep(millis);
    }

    /**
     *  Indicates that the caller is momentarily unable to progress, until the occurrence of one or more actions on the
     *  part of other activities. By invoking this method within each iteration of a spin-wait loop construct, the calling
     *  thread indicates to the runtime that it is busy-waiting. The runtime may take action to improve the performance of
     *  invoking spin-wait loop constructions.
     *
     * @implNote As an example consider a method in a class that spins in a loop until some flag is set outside of that method.
     *           A call to the onSpinWait method should be placed innside the spin loop.
     *
     *                  class EventHandler {
     *                      volatile boolean eventNotificationNotReceived;
     *                      void waitForEventAndHandleIt() {
     *                          while (eventNotificationNotReceived) {
     *                              java.lang.Thread.onSpinWait();
     *                          }
     *                          readAndProcessEvent();
     *                      }
     *                      void readAndProcessEvent() {
     *                          // Read event from some source and process it
     *                      }
     *                  }
     *
     *           This code above would remain correct even if the onSpinWait method was not called at all.
     *           However on some architectures the Java Virtual Machine may issue the processor instructions to address
     *           such code patterns in a more beneficial way.
     */
    @HotSpotIntrinsicCandidate
    public static void onSpinWait() {}

    /**
     * Initializes a Thread.
     *
     * @param g the Thread group
     * @param target the object whose run() method gets called
     * @param name the name of the new Thread
     * @param stackSize the desired stack size for the new thread, or zero to indicate that this parameter is to be ignored
     * @param acc the AccessControlContext to inherit, or AccessController.getContext() if null
     * @param inheritThreadLocal if true, inherit initial values for inheritable thread-locals from the constructing thread
     */
    private Thread(ThreadGroup g, Runnable target, String name, long stackSize, AccessControlContext acc, boolean inheritThreadLocal) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }

        this.name = name;

        Thread parent = currentThread();
        SecurityManager security = System.getSecurityManager();
        if (g == null) {
            // Determine if it's an applet or not

            // If there is a security manager, ask the security manager what to do
            if (security != null)
                g = security.getThreadGroup();

            // If the security manager doesn't have a strong opinion on the matter, use the parent thread group
            if (g == null)
                g = parent.getThreadGroup();
        }

        // checkAccess regardless of whether threadgroup is explicitly passed in
        g.checkAccess();

        // Do we have the required permissions?
        if (security != null) {
            if (isCCLOverridden(getClass())) {
                security.checkPermission(SecurityConstants.SUBCLASS_IMPLEMENTATION_PERMISSION);
            }
        }

        //g.addUnstarted();

        this.group = g;
        this.daemon = parent.isDaemon();
        this.priority = parent.getPriority();
        if (security == null || isCCLOverridden(parent.getClass()))
            this.contextClassLoader = parent.contextClassLoader;
        else
            this.contextClassLoader = parent.contextClassLoader;
        this.inheritedAccessControlContext = acc != null ? acc : AccessController.getContext();
        this.target = target;
        setPriority(priority);
        if (inheritThreadLocals && parent.inheritableThreadLocals != null)
            this.inheritableThreadLocals = ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);

        // Stash the specified stack size in case the VM cares
        this.stackSize = stackSize;

        // Set thread ID
        this.tid = nextThreadID();
    }

    /**
     * Throws CloneNotSupportedException as a Thread can not be meaningfully cloned. Construct a new Thread instead.
     *
     * @throws CloneNotSupportedException always
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Allocates a new Thread object. This constructor has the same effect as Thread(null, null, gname),
     * where gname is a newly generated name. Automatically generated names are of the form "Thread-"+n, where n is an integer
     */
    public Thread() { this (null, null, "Thread-" + nextThreadNum(), 0); }

    /**
     * Allocates a new Thread object. This constructor  has the same effect as Thread(null, target, gname), where gname is
     * a newly generated name. Automatically generated names are of the form "Thread-"+n, where n is an integer.
     *
     * @param target the object whose run method is invoked when this thread is started. If null, this classes run method does nothing
     */
    public Thread(Runnable target) { this(null, target, "Thread-"+nextThreadNum(), 0); }

    /**
     * Creates a new Thread that inherits the given AccessControlContext but thread-local variable are not inherited.
     * This is not a public constructor.
     */
    Thread(Runnable target, AccessControlContext acc) {
        this(null, target, "Thread-" + nextThreadNum(), 0, acc, false);
    }

    /**
     * Allocates a new Thread object. This constructor has the same effect as Thread(group, target, gname), where
     * gname is a newly generated name. Automatically generated names are of the form "Thread-" + n, where n is an integer.
     *
     * @param group the thread group. If null and there is a security manager, the group is determined by SecurityManager.
     *         getThreadGroup() returns null, the group is set to the current thread's thread group.
     * @param target the object whose run method is invoked when this thread is started. If null, this thread's run method is invoked.
     * @throws SecurityException if the current thread cannot create a thread in the specified thread group
     */
    public Thread(ThreadGroup group, Runnable target) { this(group, target, "Thread-" + nextThreadNum(), 0); }

    /**
     * Allocates a new Thread object. This constructor has the same effect as Thread(null, null, name).
     *
     * @param name the name of the new thread
     */
    public Thread(String name) { this(null, null, name, 0); }

    /**
     * Allocates a new Thread object. This constructor has the same effect as Thread(group, null, name).
     *
     * @param group the thread group. If null and there is a security manager, the group is determined by SecurityManager.
     *              getThreadGroup(). If there is not a security manager or SecurityManager.getThreadGroup() returns null,
     *              the group is set to the current thread's thread group.
     * @param name the name of the new thread
     * @throws SecurityException if the current thread cannot create a thread  in the specified thread group
     */
    public Thread(ThreadGroup group, String name) { this(group, null, name, 0); }

    /**
     * Allocates a new Thread object. This constructor has the same effect as Thread(null, target, name).
     *
     * @param target the object whose run method is invoked when this thread is started. If null, this thread's run method is invoked.
     * @param name the name of the new thread
     */
    public Thread(Runnable target, String name) { this(null, target, name, 0); }

    /**
     * Allocates a new Thread object so that it has target as its run object, has the specified name as its name,
     * and belongs to the thread group referred to by group.
     *
     * If there is a security manager, its checkAccess method is invoked with the ThreadGroup as its argument.
     *
     * In addition, its checkPermission method is invoked with the RuntimePermission("enableContextClassLoaderOverride")
     * permission when invoked directly or indirectly by the constructor of a subclass which overrides the getContextClassLoader or
     * setContextClassLoader methods.
     *
     * The priority of the newly created thread is set equal to the priority of the thread creating it, that is, the currently
     * running thread. The method setPriority may be used to change the priority to a new value.
     *
     * The newly created thread is initially marked as being a daemon thread if and only if the thread creating it is currently
     * marked as a daemon thread. The method setDaemon may be used to change whether a thread is a daemon.
     *
     * @param group the thread group. If null and there is a security manager, the group is determined by SecurityManager.
     *              getThreadGroup(). If there is not a security manager or SecurityManager.getThreadGroup() returns null,
     *              the group is set to the current thread's thread group.
     * @param target the object whose run method is invoked when this thread is started. If null, this thread's run method is invoked.
     * @param name the name of the new thread
     * @throws SecurityException if the current thread cannot create a thread in the specified thread group or
     *                           cannot override the context class loader methods.
     */
    public Thread(ThreadGroup group, Runnable target, String name) { this(group, target, name, 0); }

    /**
     * Allocates a new Thread object so that it has target as its run object, has the specified name as its name,
     * and belongs to the thread group referred to by group, and has the specified stack size.
     *
     * This constructor is identical to Thread(ThreadGroup, Runnable, String) with the exception of the fact that it allows the
     * thread stack size to be specified. The stack size is the approximate number of bytes of address space that the virtual machine
     * is to allocate for this thread's stack. The effect of the stackSize parameter, if any, is highly platform dependent.
     *
     * On some platform, specifying a higher value for the stackSize parameter may allow a thread to achieve greater recursion
     * depth before throwing a StackOverflowError. Similarly, specifying a lower value may allow a greater number of threads to
     * exist concurrently without throwing an OutOfMemoryError (or other internal error). The details of the relationship between
     * the value of the stackSize parameter and the maximum recursion depth and concurrency level are platform dependent. On some
     * platforms, the value of the stackSize parameter may have no effect whatsoever.
     *
     * The virtual machine is free to treat the stackSize parameter as a suggestion. If the specified value is unreasonable low for
     * the platform, the virtual machine may instead use some platform-specific minimum value; if the specified value is
     * unreasonably high, the virtual machine may instead use some platform-specific maximum. Likewise, the virtual machine is
     * free to round the specified value up or down as it sees fit(or to ignore it completely).
     *
     * Specifying a value of zero for the stackSize parameter will cause this constructor to behave exactly like
     * the Thread(ThreadGroup, Runnable, String) constructor.
     *
     * Due to the platform-dependent nature of the behavior of this constructor, extreme care should be exercised in its use.
     * The thread stack Size necessary to perform a given computation will likely vary from one JRE implementation to another.
     * In light of this variation, careful tuning of the stack size parameter may be required, and the tuning may need to be repeated
     * for each JRE implementation on which an application is to run.
     *
     * @implSpec Java platform implementers are encouraged to document their implementation's behavior with respect to the stackSize parameter.
     * @param group the thread group. If null and there is a security manager, the group is determined by SecurityManager
     *              getThreadGroup(). If there is not a security manager or SecurityManager.getThreadGroup() returns null,
     *              the group is set to the current thread's thread group
     * @param target the object whose run method is invoked when this thread is started. If null, this thread's run method is invoked
     * @param name the name of the new thread
     * @param stackSize the desired stack size for the new thread, or zero to indicate that this parameter is to be ignored
     * @throws SecurityException if the current thread cannot create a thread in the specified thread group
     */
    public Thread(ThreadGroup group, Runnable target, String name, long stackSize) {
        this(group, target, name, stackSize, null, true);
    }

    /**
     * Allocates a new Thread object so that it has target as its run object, has the specified name as its name,
     * belongs to the thread group referred to by group, has the specified stackSize, and inherits initial values for inheritable
     * thread-local variables if inheritThreadLocals is true.
     *
     * This constructor is identical to Thread(ThreadGroup, Runnable, String, long) with the added ability to suppress, or not,
     * the inheriting of initial values for inheritable thread-local variables f rom the constructing thread. This allows for finer
     * grain control over inheritable thread-locals. Care must be taken when passing a value of false for inheritThreadLocals, as it
     * may lead to unexpected behavior if the new thread executes code that expects a specific  thread-local value to be inherited.
     *
     * Specifying a value of true for the inheritThreadLocals parameter will cause this constructor to behave exactly like the
     * Thread(ThreadGroup, Runnable, String, long) constructor.
     *
     * @param group the thread group. If null and there is a security manager, the group is determined by SecurityManager.
     *              getThreadGroup(). If there is not a security manager or SecurityManager.getThreadGroup() returns null,
     *              the group is set to the current thread's thread group.
     * @param target the object whose run method is invoked when this thread is started. If null, this thread's run method is invoked
     * @param name the name of the new thread
     * @param stackSize the desired stack size for the new thread, or zero to indicate that this parameter is to be ignored
     * @param inheritThreadLocals if true, inherit initial values for inheritable thread-locals from the constructing thread,
     *                            otherwise no initial values are inherited.
     * @throws SecurityException if the current thread cannot create a thread in the specified thread group
     */
    public Thread(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {
        this(group, target, name, stackSize, null, inheritThreadLocals);
    }

    /**
     * Causes this thread to begin execution; the Java Virtual Machine calls the run method of this thread.
     *
     * The result is that two threads are running concurrently: the current thread (which returns from the call to the
     * start method) and the other thread (which executes its run method).
     *
     * It is never legal to start a thread more than once. In particular, a thread may not be restarted once it has completed execution.
     *
     * @throws IllegalThreadStateException if the thread  was already started.
     */
    public synchronized void start() {
        /**
         * This method is not invoked for the main method thread or "system" group threads created/set up by the VM.
         * Any new functionality added to this method in the future may have to also be added to the VM.
         *
         * A zero status value corresponds to state "NEW".
         */
        if (threadStatus != 0)
            throw new IllegalThreadStateException();

        /**
         * Notify the group that this thread is about to be started so that it can be added to the group's list
         * of threads and the group's unstarted count can be decremented.
         */
        group.add(this);

        boolean started = false;
        try {
            start0();
            started = true;
        } finally {
            try {
                if (!started) {
                    group.threadStartFailed(this);
                }
            } catch (Throwable ignore) {
                // do nothing. If start0 threw a Throwable then it will be passed up the call stack.
            }
        }
    }

    private native void start0();

    /**
     * If this thread was constructed using a separate Runnable run object, then that Runnable object's run method is called;
     * otherwise, this method does nothing and returns.
     *
     * Subclasses of Thread should override this method.
     */
    @Override
    public void run() {
        if (target != null)
            target.run();
    }

    // This method is called by the system to give a Thread a chance to clean up before it actually exits.
    private void exit() {
        if (threadLocals != null && TerminatingThreadLocal.REGISTRY.isPresent()) {
            TerminatingThreadLocal.threadTerminated();
        }
        if (group != null) {
            group.threadTerminated(this);
            group = null;
        }

        // Aggressively null out all reference fields:
        target = null;
        // Speed the release of some of these resources
        threadLocals = null;
        inheritableThreadLocals = null;
        inheritedAccessControlContext = null;
        blocker = null;
        uncaughtExceptionHandler = null;
    }

    /**
     * Interrupts this thread.
     *
     * Unless the current thread is interrupting itself, which is always permitted, the checkAccess method of this thread is invoked,
     * which may cause a SecurityException to be thrown.
     *
     * If this thread is blocked in an invocation of the wait(), wait(long), or wait(long, int) methods of the Object class, or of the
     * join(), join(long), join(long, int), sleep(long), or sleep(long, int), methods of this class, then its interrupt status will be
     * cleared and it will receive and InterruptedException.
     *
     * If this thread is blocked in an I/O operation upon an InterruptibleChannel then the channel will be closed, the thread's interrupt
     * status will be set, and the thread will receive a java.nio.channels.ClosedByInterruptException.
     *
     * If this threadis blocked in a java.nio.channels.Selector then the thread's interrupt status will be set and it will return immediately
     * from the selection operation, possibly with a non-zero value, just as if the selector's wakeup method were invoked.
     *
     * If none of the previous conditions hold then this thread's interrupt status will be set.
     *
     * Interrupting a thread that is not alive need not have any effect.
     *
     * @throws SecurityException if the current thread cannot modify this thread
     */
    public void interrupt() {
        if (this != Thread.currentThread()) {
            checkAccess();

            // thread may be blocked in an I/O operation
            synchronized(blockerLock) {
                Interruptible b = blocker;
                if (b != null) {
                    interrupt0();   // set interrupt status
                    b.interrupt(this);
                    return;
                }
            }
        }

        // set interrupt status
        interrupt0();
    }

    /**
     * Tests whether the current thread has been interrupted. The interrupted status of the thread is cleared by this method.
     * In other words, if this method were to be called twice in succession, the second call would return false (unless the current
     * thread were interrupted again, after the first call had cleared its interrupted status and before the second call had examined it.)
     *
     * A thread interruption ignored because a thread was not alive at the time of the interrupt will be reflected by this method
     * returning false.
     *
     * @return true if the current thread has been interrupted; false otherwise
     */
    public static boolean interrupted() { return currentThread().isInterrupted(true); }

    /**
     * Tests whether this thread has been interrupted. The interrupted status of the thread is unaffected by this method.
     *
     * A thread interruption ignored because a thread was not alive at the time of the interrupt will be reflected by this method
     * returning false.
     *
     * @rerturn true if this thread has been interrupted; false otherwise.
     */
    public boolean isInterrupted() { return isInterrupted(false); }

    /**
     * Tests if some thread has been interrupted. The interrupted state is reset or not based on the value of ClearInterrupted that is passed
     */
    @HotSpotIntrinsicCandidate
    private native boolean isInterrupted(boolean ClearInterrupted);

    /**
     * Tests if this thread is alive. A thread is alive if it has been started and has not yet died.
     *
     * @return true if this thread is alive; false otherwise
     */
    public final native boolean isAlive();

    /**
     * Change the priority of this thread.
     *
     * First the checkAccess method of this thread is called with no arguments. This may result in throwing  a SecurityException.
     *
     * Otherwise, the priority of this thread is set to the smaller of the specified newPriority and the maximum permitted priority of
     * the thread's thread group.
     *
     * @param newPriority priority to set this thread to
     * @throws IllegalArgumentException If the priority is not in the range MIN_PRIORITY to MAX_PRIORITY
     * @throws SecurityException if the current thread cannot modify this thread.
     */
    public final void setPriority(int newPriority) {
        ThreadGroup g;
        checkAccess();
        if (newPriority > MAX_PRIORITY || newPriority < MIN_PRIORITY) {
            throw new IllegalArgumentException();
        }
        if ((g = getThreadGroup()) != null) {
            if (newPriority > g.getMaxPriority()) {
                newPriority = g.getMaxPriority();
            }
            setPriority(priority = newPriority);
        }
    }

    /**
     * Returns this thread's priority
     * @return this thread's priority
     */
    public final int getPriority() { return priority; }

    /**
     * Changes the name of this thread to be equal to the argument name.
     *
     * First the checkAccess method of this thread is called with no arguments. This may result in throwing a SecurityException.
     *
     * @param name the new name for this thread
     * @throws SecurityException if the current thread cannot modify this thread
     */
    public final synchronized void setName(String name) {
        checkAccess();
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }
        this.name = name;
        if (threadStatus != 0) {
            setNativeName(name);
        }
    }

    /**
     * Returns this thread's name.
     * @return this thread's name
     */
    public final String getName() { return name; }

    /**
     * Returns the thread group to which this thread belongs. This method returns null if this thread has died (been stopped).
     *
     * @return this thread's thread group
     */
    public final ThreadGroup getThreadGroup() { return group; }

    /**
     * Returns an estimate of the number of active threads in the current thread's thread group and its subgroups. Recursively
     * iterates over all subgroups in the current thread's thread group.
     *
     * The value returned  is only an estimate because the number of threads may change dynamically while this method traverses
     * internal data structures, and might be affected by the presence of certain system threads. This method is intended primarily
     * for debugging and monitoring purposes.
     *
     * @return an estimate of the number of active threads in the current thread's thread group and in any other thread group that
     *         has the current thread's thread group as an ancestor
     */
    public static int activeCount() { return currentThread().getThreadGroup().activeCount(); }

    /**
     * Copies into the specified array every active thread in the current thread's thread group and its subgroups. This method simply
     * invokes the ThreadGroup.enumerate(Thread[]) method of the current thread's thread group.
     *
     * An application might use the activeCount method to get an estimate of how big the array should be, however if the array is too short
     * to hold all the threads, the extra threads are silently ignored. If it is critical to obtain every active thread in the current
     * thread's thread group and its subgroups, the invoker should verify that the returned int value is strictly less than the length
     * of tarray.
     *
     * Due to the inherent race condition in this method, it is recommended that the method only be used for debugging and monitoring
     * purposes.
     *
     * @param tarray an array into which to put list of threads
     * @return the number of threads put into the array
     */
    public static int enumerate(Thread tarray[]) { return currentThread().getThreadGroup().enumerate(tarray); }


}

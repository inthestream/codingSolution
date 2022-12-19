package com.yun.publiclibrary.java.e0;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * This class provides thread-local variables. These variables differ from their normal counterparts in that each
 * thread that accesses one (via its get or set method) has its own, independently initialized copy of the variable.
 * ThreadLocal instances are typically private static fields in classes that wish to associate state with a thread.
 *
 * For example, the class below generates unique identifiers local to each thread. A thread's id is assigned the  first
 * time it invokes ThreadId.get() and remains unchanged on subsequent calls.
 *
 *      import java.util.concurrent.atomic.AtomicInteger;
 *
 *      public class ThreadId {
 *          // Atomic integer  containing the next thread ID to be assigned
 *          private static final AtomicInteger nextId = new AtomicInteger(0);
 *
 *          // Thread local variable containing each thread's ID
 *          private static final ThreadLocal<Integer> threadId =
 *              new ThreadLocal<Integer>() {
 *                  @Override protected Integer initialValue() {
 *                      return nextId.getAndIncrement();
 *                  }
 *          };
 *
 *          // Returns the concurrent thread's unique ID, assigning it if necessary
 *          public static int get() {
 *              return threadId.get();
 *          }
 *      }
 *
 * Each thread holds an implicit reference to its copy of a thread-local variable as long as the thread is alive and
 * the ThreadLocal instance is accessible; after a thread goes away, all of its copies of thread-local instances are
 * subject to garbage collection (unless other references to these copies exist).
 *
 * @author Josh Bloch and Doug Lea
 */
public class ThreadLocal<T> {
    /**
     * ThreadLocals rely on per-thread linear-probe hash maps attached to each thread (Thread.threadLocals and inheritableThreadLocals).
     * The ThreadLocal objects act as keys, searched via threadLocalHashCode. This is a custom hash code (useful only within
     * ThreadLocalMaps) that eliminates collisions in the common case where consecutively constructed ThreadLocals are
     * used by the same threads, while remaining well-behaved in less common cases.
     */
    private final int threadLocalHashCode = nextHashCode();

    /**
     * The next hash code to be given out. Updated atomically. Starts at zero.
     */
    private static AtomicInteger nextHashCode = new AtomicInteger();

    /**
     * The difference between successively generated hash codes- turns implicit sequential thread-local IDs into
     * near-optimally spread multiplicative hash values for power-of-two-sized tables.
     */
    private static final int HASH_INCREMENT = 0x61c88647;

    /**
     * Returns the next hash code.
     */
    private static int nextHashCode() { return nextHashCode.getAndAdd(HASH_INCREMENT); }

    /**
     * Returns the current thread's 'initial value' for this thread-local variable. This method will be invoked the
     * first time a thread accesses the variable with the get method, unless the thread previously invoked the set
     * method, in which case the initialValue method will not be invoked for the thread. Normally this method is
     * invoked at most once per thread, but it may be invoked again in case of subsequent invocations of remove
     * followed by get.
     *
     * This implementation simply returns null; if the programmer desires thread-local variables to have an initial
     * value other than null, ThreadLocal must be subclassed, and this method overridden. Typically an anonymous
     * inner class will be used.
     *
     * @return the initial value for this thread-local
     */
    protected T initialValue() { return null; }

    /**
     * Creates a thread local variable. The initial value of the variable is determined by invoking the get method
     * on the Supplier.
     *
     * @param supplier the supplier to be used to determine the initial value
     * @return a new thread local variable
     * @throws NullPointerException if the specified supplier is null
     */
    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal<>(supplier);
    }

    /**
     * Creates a thread local variable.
     */
    public ThreadLocal() {}

    /**
     * Returns the value in the current thread's copy of this thread-local variable. If the variable has no value for
     * the current thread, it is first initialized to the value returned by an invocation of the initiialValue method.
     *
     * @return the current thread's value of this thread-local
     */
    public T get() {
        java.lang.Thread t = java.lang.Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
}

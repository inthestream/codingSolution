package com.yun.publiclibrary.java.e1;

/**
 * The Runnable interface should be implemented by any class whose instances are intended to be excuted by a thread.
 * The class must define a method of no arguments called run.
 *
 * This interface is designed to provide a common protocol for objects that wish to execute code while they are
 * active. For example, Runnable is implemented by class Thread. Being active simply means that a thread has been
 * started and has not yet been stopped.
 *
 * In addition, Runnable provides the means for a class to be active while not subclassing Thread. A class that
 * implements Runnable can run without subclassing Thread by instantiating a Thread instance and passing itself
 * in as the target. In most cases, the Runnable interface should be used if you are only planning to override
 * the run() method and no other Thread methods. This is important because classes should not be subclassed unless
 * the programmer intends on modifying or enhancing the fundamental behavior of the class.
 *
 * Author: Arthur van Hoff
 */
@FunctionalInterface
public interface Runnable1 {
    /**
     * When an object implementing interface Runnable is used to create a thread, starting the thread causes
     * the Object's run method to be called in that separately executing thread.
     *
     * The general contract of the method run is that it may take any action whatsoever.
     */
    public abstract void run();
}

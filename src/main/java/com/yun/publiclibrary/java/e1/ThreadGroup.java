package com.yun.publiclibrary.java.e1;

import java.io.PrintStream;
import java.util.Arrays;

/**
 * A thread group represents a set of threads. In addition, a thread group can also include other thread groups.
 * The thread groups form a tree in which every thread group except the initial thread group has a parent.
 *
 * A thread is allowed to access information about its own thread group, but not to access information about its thread group's
 * parent thread group or any other thread groups.
 *
 * The locking strategy for this code is to try to lock only one level of the tree whenever possible, but otherwise to lock from
 * the bottom up.
 * That is, from child  thread groups to parents.
 * This has the advantage o f limiting the number of locks that need to be held and in particular avoids having to grab the lock for
 * the root thread group, (or a global lock) which would be a source of contention on a multi-processor system with many thread groups.
 * This policy often leads to taking a snapshot of the state of a thread group and working off of that snapshot, rather than holding
 * the thread group locked while we work on the children.
 */
public class ThreadGroup implements Thread.UncaughtExceptionHandler {
    private final ThreadGroup parent;
    String name;
    int maxPriority;
    boolean destroyed;
    boolean daemon;

    int nUnstartedThread = 0;
    int nthreads;
    Thread threads[];

    int ngroups;
    ThreadGroup groups[];

    /**
     * Creates an empty Thread group that is not in any Thread group. This method is used to create the system Thread group.
     */
    private ThreadGroup() {
        this.name = "system";
        this.maxPriority = Thread.MAX_PRIORITY;
        this.parent = null;
    }

    /**
     * Construcs a new thread group. The parent of this new group is the thread group of the currently running thread.
     * The checkAccess method of the parent thread group is called with no arguments; this may result in a security exception.
     *
     * @param name the name of the new thread group.
     * @throws SecurityException if the current thread cannot create a thread in the specified thread group.
     */
    public ThreadGroup(String name) {  this(Thread.currentThread().getThreadGroup(), name); }

    /**
     * Creates a new thread group. The parent of this group is the specified thread group.
     * The checkAccess method of the parent thread group is called with no arguments; this may result in a security exception.
     *
     * @param parent the parent thread group.
     * @param name the name of the new thread group.
     * @throws NullPointerException if the thread group argument is null.
     * @throws SecurityException if the current thread cannot create a thread in the specified thread group.
     */
    public ThreadGroup(ThreadGroup parent, String name) { this(checkParentAccess(parent), parent, name); }

    private ThreadGroup(Void unused, ThreadGroup parent, String name) {
        this.name = name;
        this.maxPriority = parent.maxPriority;
        this.daemon = parent.daemon;
        this.parent = parent;
        parent.add(this);
    }

    /**
     * @throws NullPointerException if the parent argument is {@code null}
     * @throws SecurityException if the current thread cannot create a thread in the specified thread group.
     */
    private static Void checkParentAccess(ThreadGroup parent) {
        parent.checkAccess();
        return null;
    }

    /**
     * Returns the name of this thread group.
     * @return the name of this thread group
     */
    public final String getName() { return name; }

    /**
     * Returns the parent of this thread group.
     * First, if the parent is not null, the checkAccess method of the parent thread group is called with no arguments;
     * this may result in a security exception.
     *
     * @return the parent of this thread group. The top-level thread group is the only thread group whose parent is null.
     * @throws SecurityException if the current thread cannot modify this thread group
     */
    public final ThreadGroup getParent() {
        if (parent != null)
            parent.checkAccess();
        return parent;
    }

    /**
     * Returns the maximum priority of this thread group. Threads that are part of this group cannot have a higher priority
     * than the maximum priority.
     *
     * @return the maximum priority that a thread in this thread group can have.
     */
    public final int getMaxPriority() { return maxPriority; }

    /**
     * Tests if this thread group is a daemon thread group. A daemon thread group is automatically destroyed when its last thread
     * is stopped o r its last thread group is destroyed.
     *
     * @return true if this thread group is a daemon thread group, false otherwise.
     */
    public final boolean isDaemon() { return daemon; }

    /**
     * Tests if this thread group has been destroyed.
     *
     * @return true if this object is destroyed
     */
    public synchronized boolean isDestroyed() { return destroyed; }

    /**
     * Changes the daemon status of this thread group.
     * First, the checkAccess method o f this thread group is called with no arguments; this may result in a security exception.
     * A daemon thread group is automatically destroyed when its last thread is stopped or its last thread group is destroyed.
     *
     * @param daemon if true, marks this thread group as a daemon thread group; otherwise, marks this thread group as normal.
     * @throws SecurityException if the current thread cannot modify this thread group.
     */
    public final void setDaemon(boolean daemon) {
        checkAccess();
        this.daemon = daemon;
    }

    /**
     * Sets the maximum priority of the group. Threads in the thread group that already have a higher priority are not affected.
     * First, the checkAccess method of this thread group is called with no arguments; this may result in a security exception.
     * If the pri argument is less that Thread.MIN_PRIORITY or greater than Thread.MAX_PRIORITY, the maximum priority of the group
     * remains unchanged.
     * Otherwise, the priority of this ThreadGroup object is set to the smaller of the specified pri and the maximum permitted
     * priority of the parent of this thread group. (If this thread group is the system thread group, which has no parent, then
     * its maximum priority is simply set to pri.) Then this method is called recursively, with pri as its argument, for every
     * thread group that belongs to this thread group.
     *
     * @param pri the new priority of the thread group
     * @throws SecurityException if the current thread cannot modify this thread group.
     */
    public final void setMaxPriority(int pri) {
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized(this) {
            checkAccess();
            if (pri < Thread.MIN_PRIORITY || pri > Thread.MAX_PRIORITY) {
                return;
            }
            maxPriority = (parent != null) ? Math.min(pri, parent.maxPriority) : pri;
            ngroupsSnapshot = ngroups;

            if (groups != null) {
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            } else {
                groupsSnapshot = null;
            }
        }
        for (int i = 0; i < ngroupsSnapshot; i++) {
            groupsSnapshot[i].setMaxPriority(pri);
        }
    }

    /**
     * Tests if this thread group is either the thread group argument or one of its ancestor thread group.
     * @param g a thread group
     * @return true if this thread group is the thread group argument or one of its ancestor thread group; false otherwise.
     */
    public final boolean parentOf(ThreadGroup g) {
        for (; g != null; g = g.parent) {
            if (g == this)
                return true;
        }
        return false;
    }

    /**
     * Determines if the currently running thread has permission to modify this thread group.
     * If there is a security manager, its checkAccess method  is called with this thread group as its argument.
     * This may result in throwing a SecurityException
     *
     * @throws SecurityException if the current thread is not allowed to access this thread group
     */
    public final void checkAccess() {
        com.yun.publiclibrary.java.e0.SecurityManager security = (com.yun.publiclibrary.java.e0.SecurityManager) System.getSecurityManager();
        if (security != null) {
            security.checkAccess(this);
        }
    }

    /**
     * Returns an estimate of the number of active threads in this thread group and its subgroups. Recursively iterates over
     * all subgroups in this thread group.
     * The value returned is only an estimate because the number of threads may change dynamically while this method traverses
     * internal data structures, and might be affected by the presence of certain system threads. This method is intended primarily
     * for debugging and monitoring purposes.
     *
     * @return an estimate of the number of active threads in this thread group and in any other thread group that has this thread
     *         group as an ancestor
     */
    public int activeCount() {
        int result;
        // Snapshot sub-group data so we don't hold this lock
        // while our children are computing.
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            if (destroyed) {
                return 0;
            }
            result = nthreads;
            ngroupsSnapshot = ngroups;
            if (groups != null)
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            else
                groupsSnapshot = null;
        }
        for (int i = 0; i < ngroupsSnapshot; i++)
            result += groupsSnapshot[i].activeCount();
        return result;
    }

    /**
     * Copies into the specified array every active thread in this thread group and its subgroups.
     * An invocation of this method behaves in exactly the same way as the invocation
     *
     *          enumerate(list, true)
     *
     * @param list an array into which to put the list of threads
     * @return the number of threads put into the array
     * @throws SecurityException if checkAccess determines that the current thread cannot access this thread group
     */
    public int enumerate(Thread list[]) {
        checkAccess();
        return enumerate(list, 0, true);
    }

    /**
     * Copies into the specified array every active thread in this thread group. If recurse is true, this method recursively
     * enumerates all subgroups of this thread group and references to every active thread in these subgroups are also included.
     * If the array is too short to hold all the threads, the extra threads are silently ignored.
     *
     * An application might use the activeCount method to get an estimate of how big the array should be,
     * however if the array is too short to hold all the threads, the extra threads are silently ignored. If it is critical to obtain
     * every active thread in this thread group, the caller should verify that the returned int value is strictly less than
     * the length of list.
     *
     * Due to the inherent race condition in this method, it is recommended that the method only be used for debugging and
     * monitoring purpose.
     *
     * @param list an array into which to put the list of threads
     * @param recurse if true, recursively enumerate all subgroups of this thread group
     * @return the number of threads put into the array
     * @throws SecurityException if checkAccess determines that the current thread cannot access this thread group.
     */
    public int enumerate(Thread list[], boolean recurse) {
        checkAccess();
        return enumerate(list, 0, recurse);
    }

    private int enumerate(Thread list[], int n, boolean recurse) {
        int ngroupsSnapshot = 0;
        ThreadGroup[] groupsSnapshot = null;
        synchronized (this) {
            if (destroyed)
                return 0;
            int nt = nthreads;
            if (nt > list.length - n)
                nt = list.length - n;

            for (int i = 0; i < nt; i++)
                if (threads[i].isAlive())
                    list[n++] = threads[i];

            if (recurse) {
                ngroupsSnapshot = ngroups;
                if (groups != null)
                    groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
                else
                    groupsSnapshot = null;
            }
        }

        if (recurse)
            for (int i = 0; i < ngroupsSnapshot; i++)
                n = groupsSnapshot[i].enumerate(list, n, true);

        return n;
    }

    /**
     * Returns an estimate of the number of active groups in this thread group and its subgroups. Recursively
     * iterates over all subgroups in this thread group.
     *
     * The value returned is only an estimate because the number of thread groups may change dynamically
     * while this method traverses internal data structures. This method is intended primarily for debugging and
     * monitoring purpose.
     *
     * @return the number of active thread groups with this thread group as an ancestor
     */
    public int activeGroupCount() {
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            if (destroyed)
                return 0;
            ngroupsSnapshot = ngroups;
            if (groups != null)
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            else
                groupsSnapshot = null;
        }
        int n = ngroupsSnapshot;
        for (int i = 0; i < ngroupsSnapshot; i++)
            n += groupsSnapshot[i].activeGroupCount();

        return n;
    }

    public int enumerate(ThreadGroup list[]) {
        checkAccess();
        return enumerate(list, 0, true);
    }

    public int enumerate(ThreadGroup list[], boolean recurse) {
        checkAccess();
        return enumerate(list, 0, recurse);
    }

    private int enumerate(ThreadGroup list[], int n, boolean recurse) {
        int ngroupsSnapshot = 0;
        ThreadGroup[] groupsSnapshot = null;
        synchronized (this) {
            if (destroyed) {
                return 0;
            }
            int ng = ngroups;
            if (ng > list.length - n) {
                ng = list.length - n;
            }
            if (ng > 0) {
                System.arraycopy(groups, 0, list, n, ng);
                n += ng;
            }
            if (recurse) {
                ngroupsSnapshot = ngroups;
                if (groups != null) {
                    groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
                } else {
                    groupsSnapshot = null;
                }
            }
        }
        if (recurse) {
            for (int i = 0 ; i < ngroupsSnapshot ; i++) {
                n = groupsSnapshot[i].enumerate(list, n, true);
            }
        }
        return n;
    }

    /**
     * Destroys this thread group and all of its subgroups. This thread group must be empty, indicating that all threads
     * that had been in this thread group have since stopped.
     *
     * First, the checkAccess method of this thread group is called with no arguments; this may result in a security exception.
     *
     * @throws IllegalThreadStateException if the thread group is not empty or if the thread group has already been destroyed.
     * @throws SecurityException if the current thread cannot modify this thread group.
     */
    public final void destroy() {
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            checkAccess();
            if (destroyed || (nthreads > 0))
                throw new IllegalThreadStateException();
            ngroupsSnapshot = ngroups;
            if (groups != null)
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            else
                groupsSnapshot = null;

            if (parent != null) {
                destroyed = true;
                ngroups = 0;
                groups = null;
                nthreads = 0;
                threads = null;
            }
        }
        for (int i = 0; i < ngroupsSnapshot; i += 1)
            groupsSnapshot[i].destroy();
        if (parent != null)
            parent.remove(this);
    }

    /**
     * Adds the specified Thread group to this group.
     *
     * @param g the specified Thread group to be added
     * @throws IllegalThreadStateException If the Thread group has been destroyed.
     */
    private final void add(ThreadGroup g) {
        synchronized (this) {
            if (destroyed)
                throw new IllegalThreadStateException();
            if (groups == null)
                groups = new ThreadGroup[4];
            else if (ngroups == groups.length)
                groups = Arrays.copyOf(groups, ngroups * 2);

            groups[ngroups] = g;

            ngroups++;
        }
    }

    /**
     * Removes the specified Thread group from this group.
     *
     * @param g the Thread group to be removed
     * @return if this Thread has already been destroyed.
     */
    private void remove(ThreadGroup g) {
        synchronized (this) {
            if (destroyed)
                return;

            for (int i = 0; i < ngroups; i++) {
                if (groups[i] == g) {
                    ngroups -= 1;
                    System.arraycopy(groups, i + 1, groups, i, ngroups - i);
                    // Zap dangling refrence to the dead group so that
                    // the garbaga  collector will collect it.
                    groups[ngroups] = null;
                    break;
                }
            }

            if (nthreads == 0)
                notifyAll();

            if (daemon && (nthreads == 0) && (nUnstartedThread == 0) && (ngroups == 0))
                destroy();
        }
    }

    /**
     * Increments the count of unstarted threads in the thread group. Unstarted threads are not added to the thread group
     * so that they can be collected if they are never started, but they must be counted so that daemon thread groups with
     * unstarted threads in them are not destroyed.
     */
    void addUnstarted() {
        synchronized (this) {
            if (destroyed)
                throw new IllegalThreadStateException();
            nUnstartedThread++;
        }
    }

    /**
     * Adds the specified thread to this thread group.
     * This method is called from both library code and the Virtual Machine. It is called from VM to add certain system threads
     * to the system thread group.
     *
     * @param t the Thread to be added
     * @throws IllegalThreadStateException if the Thread group has been destroyed
     */
    void add(Thread t) {
        synchronized (this) {
            if (destroyed)
                throw new IllegalThreadStateException();
            if (threads == null)
                threads = new Thread[4];
            else if (nthreads == threads.length)
                threads = Arrays.copyOf(threads, nthreads * 2);
            threads[nthreads] = t;

            // This is done last so it doesn't mattor in case the thread is killed
            nthreads++;

            // The thread is now a fully fledged member of the group, even though it may, or may not, have been started yet.
            // It will prevent the group from being destroyed so the unstarted Threads count is decremented.
            nUnstartedThread--;
        }
    }

    /**
     * Notifies the group that the thread t has failed an attempt to start.
     * The state of this thread group is rolled back as if the attempt to start the thread has never occurred. The thread is again
     * considered an unstarted member of the thread group, and a subsequent attempt to start the thread is permitted.
     *
     * @param t the Thread whose start method was invoked
     */
    void threadStartFailed(Thread t) {
        synchronized (this) {
            remove(t);
            nUnstartedThread++;
        }
    }

    /**
     * Notifies the group that the thread t has terminated.
     * Destroy the group if all of the following conditions are true; this is a daemon thread group; there are no more alive or
     * unstarted threads in the group; there are no subgroups in this thread group.
     *
     * @param t the Thread that has terminated
     */
    void threadTerminated(Thread t) {
        synchronized (this) {
            remove(t);

            if (nthreads == 0)
                notifyAll();
            if (daemon && (nthreads == 0) && (nUnstartedThread == 0) && (ngroups == 0))
                destroy();
        }
    }

    /**
     * Removes the specified Thread from this group. Invoking this method on a thread group that has been destroyed has no effect.
     *
     * @param t the Thread to be removed
     */
    private void remove(Thread t) {
        synchronized (this) {
            if (destroyed)
                return ;
            for (int i = 0; i < nthreads; i++) {
                if (threads[i] == t) {
                    System.arraycopy(threads, i + 1, threads, i, --nthreads - i);
                    // Zap dangling reference to the dead thread so that the garbage collector will collect it.
                    threads[nthreads] = null;
                    break;
                }
            }
        }
    }

    /**
     * Prints information about this thread group to the standard output. This method is useful only for debugging.
     */
    public void list() { list(System.out, 0); }
    void list(PrintStream out, int indent) {
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            for (int j = 0; j < indent; j++)
                out.print(" ");
            out.println(this);
            indent += 4;
            for (int i = 0; i < nthreads; i++) {
                for (int j = 0; j < indent; j++) {
                    out.print(" ");
                }
                out.println(threads[i]);
            }
            ngroupsSnapshot = ngroups;
            if (groups != null)
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            else
                groupsSnapshot = null;
        }
        for (int i = 0; i < ngroupsSnapshot; i++)
            groupsSnapshot[i].list(out, indent);
    }

    /**
     * Called by the Java Virtual Machine when a thread in this thread group stops because of an uncaught exception,
     * and the thread does not have a specific Thread.UncaughtExceptionHandler installed.
     * The uncaughtException method of ThreadGroup does the following:
     * - If this thread group has a parent thread group, the uncaughtException method of that parent is called with
     * the same two arguments.
     * - Otherwise, this method checks to see if there is a default uncaught exception handler installed, and if so,
     * its uncaughtException method is called with the same two arguments.
     * - Otherwise, this method determines if the Throwable argument is an instance of ThreadDeath. If so, nothing
     * special is done. Otherwise, a message containing the thread's name, as returned from the thread's getName method,
     * and a stack backtrace, using the Throwable's printStackTrace method, is printed to the standard error stream.
     *
     * Applications can override this method in subclasses of ThreadGroup to provide alternative handling of uncaught exceptions.
     *
     * @param t the thread that is about to exit.
     * @param e the uncaught exception.
     */
    @Override
    public void uncaughtException(java.lang.Thread t, Throwable e) {
        if (parent != null) {
            parent.uncaughtException(t, e);
        } else {

        }
    }
}

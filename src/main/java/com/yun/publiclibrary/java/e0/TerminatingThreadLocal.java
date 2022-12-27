package com.yun.publiclibrary.java.e0;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;

public class TerminatingThreadLocal<T> extends ThreadLocal<T> {
    public static final ThreadLocal<Collection<jdk.internal.misc.TerminatingThreadLocal<?>>> REGISTRY =
            new ThreadLocal<>() {
                @Override
                protected Collection<jdk.internal.misc.TerminatingThreadLocal<?>> initialValue() {
                    return Collections.newSetFromMap(new IdentityHashMap<>(4));
                }
            };

    public static void threadTerminated() {
    }

    public static void register(TerminatingThreadLocal<?> terminatingThreadLocal) {
    }
}

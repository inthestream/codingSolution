package com.yun.publiclibrary.java.e8;

import java.util.Objects;

@FunctionalInterface
public interface Consumer<T> {
    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this operation followed by the after operation.
     * If performing either throws an exception
     * @param after
     * @return
     */
    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };

    }
}

/*

    Consumer<String> first = x -> System.out.println(x.toLowerCase());
    Consumer<String> second = y -> System.out.println("aaa " + y);

    Consumer<String> result = first.andThen(second);*/

package com.yun.publiclibrary.java.e8;
// java.util.function.*

import com.yun.publiclibrary.java.e0.Comparator;

import java.util.Objects;

/**
 * Represents an operation upon two operands of the same type, producing a result of the same type as the operands.
 * This is a specialization of BiFunction for the case where the operands and the result are all of the same type.
 * This is a functional interface whose functional method is apply(Object, Object).
 */
@FunctionalInterface
public interface BinaryOperator<T> extends BiFunction<T,T,T> {
    /**
     * Returns a BinaryOperator which returns the lesser of two elements according to the specified Comparator.
     * @param comparator a Comparator for comparing the two values
     * @TypeParameter<T> the type of the input arguments of the comparator
     * @return a BinaryOperator which returns the lesser of its operands, according to the supplied Comparator
     * @throws NullPointerException if the argument is null
     */
    static <T> BinaryOperator<T> minBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> comparator.compare(a, b) <= 0 ? a : b;
    }

    /**
     * Returns a BinaryOperator which returns the greater of two elements according to the specified Comparator.
     * @param comparator a Comparator for comparing the two values
     * @typeparameter<T> the type  of the input arguments of the comparator
     * @return a BinaryOperator which returns the greater of its operands, according to the supplied Comparator
     * @throws NullPointerException if the argument is null
     */
    static <T> BinaryOperator<T> maxBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
    }
}

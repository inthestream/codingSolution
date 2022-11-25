package com.yun.publiclibrary.java.e2;


import com.yun.publiclibrary.java.e8.Consumer;

import java.util.Objects;

/**
 * An iterator over a collection. Iterator takes the place of Enumeration in the Java collections Framework.
 * Iterators differ f rom enumerations in two ways:
 *
 * -Iterators allow the caller to remove elements from the underlying collection during the iteration with well-defined semantics.
 * -Method names have been improved.
 *
 * This interface is a member of the Java Collections Framework.
 *
 * @param <E> the type of elements returned by this iterator
 */
public interface Iterator<E> {
    /**
     * Returns true if the iteration has more elements.
     *              (In other words, returns true if next would return an element rather than throwing an exception)
     *
     * @return true if the iteration has more elements
     */
    boolean hasNext();

    /**
     * Returns the next element in the iteration
     *
     * @return the next element in the iteration.
     * @throws java.util.NoSuchElementException if the iteration has no more elements
     */
    E next();

    /**
     * Removes from the underlying collection the last element returned by this iterator (optional operation).
     * This method can be called only once per cal to {@code next}. The behavior of an iterator is unspecified
     * if the underlying collection is modified while the iteration is in progress in any way other than by calling this method.
     *
     * @throws UnsupportedOperationException if the remove operation is not supported by this iterator
     * @throws IllegalStateException if the {@code next} method has not yet been called, or the remove method has
     *                               already been called after the last call to the {@code next} method.
     */
    default void remove() { throw new UnsupportedOperationException("remove"); }

    /**
     * Performs the given action for each remaining element until all elements have been processed or the action
     * throws an exception. Actions are performed in the order of iteration, if that order is specified.
     * Exceptions thrown by the action are relayed to the caller
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     */

    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(next());
        }
    }
}

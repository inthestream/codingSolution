package com.yun.publiclibrary.java.e5;

import com.yun.publiclibrary.java.e2.Iterator;

import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @param <T> the type of elements returned by the iterator
 */
public interface Iterable<T> {
    /**
     * Returns an iterator over elements of type T
     *
     * @return an iterator
     */
    Iterator<T> iterator();

    /**
     * Performs the given action for each element of the Iterable until all elements have been processed or
     * the action throwns an exception. Unless otherwise specified by the implementing class, actions are performed
     * in the order of iteration (if an iteration order is specified).
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     */

    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (Iterator<? extends T> t = iterator(); t.hasNext(); ) {
            action.accept(t.next());
        }
    }

    /**
     * Create a {@code Spliterator} over the elements described by this Iterable.
     *
     * @return a {@code Spliterator} over the elements described by this Iterable.
     *
     * Impelementatio Node: The default implementation should usually be overriden. The spliterator
     * returned by the default implementation has poor splitting capabilities, is unsized, and does not
     * report any spliterator characteristics. Implementing classes can nearly always provides a better implementation.
     */
    //default Spliterator<T> spliterator() { return Spliterators.spliteratorUnknownSize(iterator(), 0); }


}

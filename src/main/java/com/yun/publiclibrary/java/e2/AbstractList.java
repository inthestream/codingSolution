package com.yun.publiclibrary.java.e2;

import java.util.*;
import java.util.function.Consumer;

/**
 * This class provides a skeletal implementation of the List interface to minimize the effort required to
 * implement this interface backed by a "random access" data store (such as an array). For sequential access data
 * (such as linked lit), AbstractSequentialList should be used in preference to this class.
 *
 * To implement an unmodifiable list, the programmer needs only to extend this class and provide
 * implementations for the get(int) and size() methods.
 *
 * To implement a modifiable list, the programmer must additionally override the set(int, E) method (which
 * otherwise throws an UnsupportedOperationException). If the list is variable-size the programmer must additionally
 * override the add(int, E) and remove(int) methods.
 *
 * The programmer should generally provide a void (no argument) and collection constructor, as per the
 * recommendation in the Collection interface specification.
 *
 * Unlike the other abstract collection implementations, the programmer does not have to provide an iterator
 * implementation; the iterator and list iterator are implemented by this class, on top of the "random access" methods:
 * get(int), set(int, E), add(int, E) and remove(int).
 *
 * The documentation for each non-abstract method in this class describes its implementation in detail. Each of
 * these methods may be overriden if the collection being implemented admits a more efficient implementation.
 */
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    protected AbstractList() {

    }

    /**
     * Appends the specified element to the end of this list(optional operation).
     *
     * Lists that support this operation may place limitations on what elements may be added to this list.
     * In particular, some lists will refuse to add null elements, and others will impose restrictions on the type of
     * elements that may be added. List classes should clearly specify in their documentation any restrictions on
     * what elements may be added.
     *
     * @param e element to be appended to this list
     * @return true (as specified by Collection.add)
     * @throws UnsupportedOperationException if the add operation is not supported by this list
     * @throws ClassCastException if the class of the specified element prevents it from being added to this list
     * @throws NullPointerException if the specified element is null and this list does n ot permit null elements
     * @throws IllegalArgumentException if some property of this element prevents it from being added to this list
     * @implNote This implementation calls add(size(), e).
     *           Note that this implementation throws an UnsupportedOperationException unless add(int, E) is overridden.
     */
    public boolean add(E e) {
        add(size(), e);
        return true;
    }

    /**
     * Returns the element at the specified position in this list.
     * @throws IndexOutOfBoundsException
     */
    public abstract E get(int index);

    /**
     * Replaces the element at the specified position in this list with the specified element(optional operation).
     *
     * @throws UnsupportedOperationException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     * @implNote This implementation always throws an UnsupportedOperationException.
     */
    public E set(int index, E element) { throw new UnsupportedOperationException(); }

    /**
     * Inserts the specified element at the specified position in this list(optional operation). Shifts the element
     * currently at that position(if any) and any subsequent elements to the right(adds one to their indices).
     *
     * @throws UnsupportedOperationException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     * @implNote This implementation always throws an UnsupportedOperation.
     */
    public void add(int index, E element) { throw new UnsupportedOperationException(); }

    /**
     * Removes the element at the specified position in this list(optional operation). Shifts any subsequent
     * elements to the left(subtracts one from their indices). Returns the element that was removed from the list.
     *
     * @throws UnsupportedOperationException
     * @throws IndexOutOfBoundsException
     * @implNote This implementation always throws an UnsupportedOperationException.
     */
    public E remove(int index) { throw new UnsupportedOperationException(); }

    // Search Operations

    /**
     * Returns the index of the first occurrence of the specified element in this list, or -1 if this list doest not
     * contain the element. More formally, returns the lowest index i such that Objects.equals(o, get(i)), or
     * -1 if there is no such index.
     *
     * @throws ClassCastException
     * @throws NullPointerException
     * @implNote This implementation first gets a list iterator(with listIterator()). Then,
     *           it iterates over the list until the specified element is found or the end of the list is reached.
     */
    public int IndexOf(Object o) {
        ListIterator<E> it = listIterator();
        if (o == null) {
            while (it.hasNext())
                if (it.next() == null)
                    return it.previousIndex();
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return it.previousIndex();
        }
        return -1;
    }

    /**
     * Returns the index of the last occurrence of the specified element in this list, or -1 if this list does not
     * contain the element. More formally, returns the highest index i such that Objects.equals(o, get(i)), or -1
     * if there is no such index.
     *
     * @throws ClassCastException
     * @throws NullPointerException
     * @implNote This implementation first gets a list iterator that points to the end of the list
     *           (with listIterator(size()). Then, it iterates backwards over the list until the specified
     *           element is found, or the beginning of the list is reached.
     */
    public int lastIndexOf(Object o) {
        ListIterator<E> it = listIterator(size());
        if (o == null) {
            while (it.hasPrevious())
                if (it.previous() == null)
                    return it.nextIndex();
        } else {
            while (it.hasPrevious())
                if (o.equals(it.previous()))
                    return it.nextIndex();
        }
        return -1;
    }

    // Bulk Operation
    /**
     * Removes all of the elements from this list(optional operation). This list will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the clear operation is not supported by this list
     * @implNote This implementation calls removeRange(0, size()).
     *           Note that this implementation throws an UnsupportedOperationException unless remove(int index) or
     *           removeRange(int fromIndex, int toIndex) is overridden.
     */
    public void clear() { removeRange(0, size()); }

    /**
     * Inserts all of the elements in the specified collection into this list at the specified position(optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right(increases their indices).
     * The new elements will appear in this list in the order that they are returned by the specified collection's iterator.
     * The behavior of this operation is undefined if the specified collection is modified while the operation is in progress.
     * (Note that this will occur if the specified collection is this list, and it's nonempty.)
     *
     * @throws UnsupportedOperationException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     * @implNote This implementation gets an iterator over the specified collection and iterates over it, inserting the elements
     *           obtained from the iterator into this list at the appropriate position, one at a time, using add(int, E).
     *           Many implementations will override this method for efficiency.
     *           Note that this implementation throws an UnsupportedOperationException unless add(int, E) is overriden.
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);
        boolean modified = false;
        for (E e : c) {
            add(index++, e);
            modified = true;
        }
        return modified;
    }

    // Iterators

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     * @implNote This implementation returns a straightforward  implementation of the iterator interface,
     *           relying on the backing list's size(), get(int), and remove(int) methods.
     *           Note that the iterator returned by this method will throw an UnsupportedOperationException
     *           in response to its remove method unless the list's remove(int) method is overridden.
     *           This implementation can be made to throw runtime exceptions in the face of concurrent
     *           modification, as described in the specification for the (protected)modCount field.
     */
    public java.util.Iterator<E> iterator() {
        return (java.util.Iterator<E>) new Itr();
    }

    /**
     * Returns a list iterator over the elements in this list(in proper sequence).
     *
     * @implNote This implementation returns listIterator(0).
     */
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper sequence), starting at the specified position
     * in the list. The specified index indicates the first element that would be returned by an initial call to next.
     * An initial call to previous would return the element with the specified index minus one.
     *
     * @throws IndexOutOfBoundsException
     * @implNote This implementation returns a straightforward implementation of the ListIterator interface that
     *           extends the implementation of the Iterator interface returned by the iterator() method. The
     *           ListIterator implementation relies on the backing list's get(int), set(int, E), add(int, E) and
     *           remove(int) methods.
     *           Note that the list iterator returned by this implementation will throw an UnsupportedOperationException
     *           in response to its remove, set and add methods unless the list's remove(int), set(int, E), and add(int, E)
     *           methods are overridden.
     *           This implementation can be made to throw runtime exceptions in the face of concurrent modification,
     *           as described in the specification for the (protected)modCount field.
     */
    public ListIterator<E> listIterator(final int index) {
        rangeCheckForAdd(index);

        return new ListItr(index);
    }

    private class Itr implements Iterator<E> {
        // Index of element to be returned by subsequent call to next.
        int cursor = 0;

        // Index of element returned by most recent call to next or previous. Reset to -1 if this element is
        // deleted by a call to remove.
        int lastRet = -1;

        // The modCount value that the iterator believes that the backing List should have. If this expectation is
        // violated, the iterator has detected concurrent modification.
        int expectedModCount = modCount;

        public boolean hasNext() { return cursor != size(); }

        public E next() {
            checkForComodification();
            try {
                int i = cursor;
                E next = get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractList.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) { cursor = index; }

        public boolean hasPrevious() { return cursor != 0; }

        public E previous() {
            checkForComodification();
            try {
                int i = cursor - 1;
                E previous = get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public int nextIndex() { return cursor; }

        public int previousIndex() { return cursor - 1; }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractList.this.set(lastRet, e);
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                AbstractList.this.add(i, e);
                lastRet = -1;
                cursor = i + 1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive.
     * (If fromIndex and toIndex are equal, the returned list is empty.) The returned list is backed by this list,
     * so non-structural changes in the returned list are reflected in this list, and vice-versa.
     * The returned list supports all of the optional list operations  supported by this list.
     *
     * This method eliminates the need for explicit range operations (of the sort that commonly exist for arrays).
     * Any operation that expects a list can be used as a range operation by passing a subList view instead of a
     * whole list. For example, the following idiom removes a range of elements from a list:
     *
     *          list.subList(from, to).clear();
     *
     * Similar idioms may be constructed for indexOf and lastIndexOf, and all of the algorithms in the Collections class
     * can be applied to a subList.
     *
     * The semantics of the list returned by this method become undefined if the backing list (i.e., this list) is structurally
     * modified in any way other than via the returned list. (Structural modifications are those that change the size of
     * this list, or otherwise perturb it in such a fashion that iterations in progress may yield incorrect results.)
     *
     * @throws IndexOutOfBoundsException if an endpoint index value is out of range (fromIndex < 0 || toIndex > size)
     * @throws IllegalArgumentException if the endpoint indices are out of order (fromIndex > toIndex)
     * @implNote This implementation returns a list that subclasses AbstractList. The subclass stores, in private fields,
     *           the size of the subList (which can change over its  lifetime), and the expected modCount value of the
     *           backing list. There are two variants of the subclass, one of which implements RandomAccess.
     *           If this list implements RandomAccess the returned list will be an instance of the subclass that
     *           implements RandomAccess.
     *           The subclass's set(int, E), get(int), add(int, E), remove(int), addAll(int, Collection) and
     *           removeRange(int, int) methods all delegate to the corresponding methods on the backing abstract list,
     *           after bounds-checking the index and adjusting for the offset. The addAll(Collection c) method merely
     *           returns addAll(size, c).
     *           The listIterator(int) method returns a "wrapper object" over a list iterator on the backing list,
     *           which is created with the corresponding method on the backing list. The iterator method merely returns
     *           listIterator(), and the size method merely returns the subclass's size field.
     *           All methods first check to see if the actual modCount of the backing list is equal to its expected value,
     *           and throw a ConcurrentModificationException if it is not.
     */
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size());
        return (this instanceof RandomAccess ? new RandomAccessSubList<>(this, fromIndex, toIndex) :
                new SubList<>(this, fromIndex, toIndex));
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
    }

    // Comparison and hashing

    /**
     * Compares the specified object with this list for equality. Returns true if and only if the specified object is
     * also a list, both lists have the same size, and all corresponding pairs of elements in the two lists are equal.
     * (Two elements e1 and e2 are equal if (e1 == null ? e2 == null : e1.equals(e2)).) In other words, two lists are
     * defined to be equal if they contain the same elements in the same order.
     *
     * @param       o the object to be compared for equality with this list
     * @return      true if the specified object is equal to this list
     * @implNote    This implementation first checks if the specified object is this list. If so, it returns true; if not,
     *              it checks if the specified object is a list, if not, it returns false; if so, it iterates over both lists,
     *              comparing corresponding pairs of elements. If any comparison returns false, this method returns false.
     *              If either iterator runs out  of elements before the other it returns false (as the lists are of unequal
     *              length); otherwise it returns true when the iterations complete.
     */
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;

        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    /**
     * Returns      the hash code value for this list.
     * @return      the hash code value for this list.
     * @implNote    This implementation uses exactly the code that is used to define the list
     *              hash function  in the documentation for the List.hashCode method.
     */
    public int hashCode() {
        int hashCode = 1;
        for (E e : this)
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        return hashCode;
    }

    /**
     * Removes from this list all of the elements whose index is between fromIndex, inclusive, and
     * toIndex, exclusive. Shifts any succeeding elements to the left(reduces their index). This call
     * shortens the list by (toIndex - fromIndex) elements. (If toIndex == fromIndex, this operation
     * has no effect.)
     *
     * This method is called by the clear operation on this list and its subLists. Overriding this method
     * to take advantage of the internals of the list implementation can substantially improve the performance
     * of the clear operation on this list and its subLists.
     *
     * @param       fromIndex index of first element to be removed
     * @param       toIndex index after last element to be removed
     * @implNote    This implementation gets a list iterator positioned before fromIndex, and repeatedly calls
     *              ListIterator.next followed by ListIterator.remove until the entire range has been removed.
     *              Note: if ListIterator.remove requires linear time, this implementation requires quadratic time.
     */
    protected void removeRange(int fromIndex, int toIndex) {
        ListIterator<E> it = listIterator(fromIndex);
        for (int  i = 0, n = toIndex-fromIndex; i < n; i++) {
            it.next();
            it.remove();
        }
    }

    /**
     * The number of times this list has been structurally modified. Structural modifications are those that
     * change the size of the list, or otherwise perturb it in such a fashion that iterations in progress may yield incorrect
     * results.
     *
     * This field is used by the iterator and list iterator implementation returned by the iterator and listIterator methods.
     * If the value of this field changes unexpectedly, the iterator (or list iterator) will throw a
     * ConcurrentModificationException in response to the next, remove, previous, set or add operations. This provides fail-fast
     * behavior, rather than non-deterministic behavior in the face of concurrent modification during iteration.
     *
     * Use of this field by subclasses is optional. If a subclass wishes to provide fail-fast iterators (and list iterators),
     * then it merely has to increment this field in its add(int, E) and remove(int) methods (and any other methods that it
     * overrides that result in structural modifications to the list). A single call to add(int, E) or remove(int) must add
     * no more than one to this field, or the iterators (and list iterators) will throw bogus ConcurrentModificationException.
     * If an implementation does not wish to provide fail-fast iterators, this field may be ignored.
     */
    protected transient int modCount = 0;

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) { return "Index: " + index + ", Size: " + size(); }

    /**
     * An index-based split-by-two, lazily initialized Spliterator covering a List that access elements via List.get.
     * If access results in an IndexOutOfBoundsException then a ConcurrentModificationException is thrown instead(since
     * the list has been structurally modified while traversing). If the List is an instance of AbstractList then concurrent
     * modification checking is performed using the AbstractList's modCount field.
     */
    static final class RandomAccessSpliterator<E> implements Spliterator<E> {
        private final List<E> list;
        private int index;  // current index, modified on advance/split
        private int fence;  // -1 until used; then one past last index

        // The following fields are valid if covering an AbstractList
        private final AbstractList<E> alist;
        private int expectedModCount;   // initialized when fence set

        RandomAccessSpliterator(List<E> list) {
            assert list instanceof RandomAccess;

            this.list = list;
            this.index = 0;
            this.fence = -1;

            this.alist = list instanceof AbstractList ? (AbstractList<E>) list : null;
            this.expectedModCount = alist != null ? alist.modCount : 0;
        }

        /**
         * Create new spliterator covering the given range.
         */
        private RandomAccessSpliterator(RandomAccessSpliterator<E> parent, int origin, int fence) {
            this.list = parent.list;
            this.index = origin;
            this.fence = fence;

            this.alist = parent.alist;
            this.expectedModCount = parent.expectedModCount;
        }

        // initialize fence to size on first use
        private int getFence() {
            int hi;
            List<E> lst = list;
            if ((hi = fence) < 0) {
                if (alist != null) {
                    expectedModCount = alist.modCount;
                }
                hi = fence = lst.size();
            }
            return hi;
        }

        public Spliterator<E> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null : // divide range in half unless too small
                    new RandomAccessSpliterator<>(this, lo, index = mid);
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null)
                throw new NullPointerException();
            int hi = getFence(), i = index;
            if (i < hi) {
                index = i + 1;
                action.accept(get(list, i));
                checkAbstractListModCount(alist, expectedModCount);
                return true;
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            List<E> lst = list;
            int hi = getFence();
            int i  = index;
            index = hi;
            for (; i < hi; i++) {
                action.accept(get(lst, i));
            }
            checkAbstractListModCount(alist, expectedModCount);
        }

        public long estimateSize() { return (long) (getFence() - index); }
        public int characteristics() { return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED; }

        private static <E> E get(List<E> list, int i) {
            try {
                return list.get(i);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        static void checkAbstractListModCount(AbstractList<?> alist, int expectedModCount) {
            if (alist != null && alist.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private static class SubList<E> extends java.util.AbstractList<E> {
        private final AbstractList<E> root;
        private final SubList<E> parent;
        private final int offset;
        protected int size;

        /**
         * Constructs a sublist of an arbitrary AbstractList, which is not a SubList itself.
         */
        public SubList(AbstractList<E> root, int fromIndex, int toIndex) {
            this.root = root;
            this.parent = null;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = root.modCount;
        }

        /**
         * Constructs a sublist of another SubList.
         */
        protected SubList(SubList<E> parent, int fromIndex, int toIndex) {
            this.root = parent.root;
            this.parent = parent;
            this.offset = parent.offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = root.modCount;
        }

        public E set(int index, E element) {
            Objects.checkIndex(index, size);
            checkForComodification();
            return root.set(offset + index, element);
        }

        public E get(int index) {
            Objects.checkIndex(index, size);
            checkForComodification();
            return root.get(offset + index);
        }

        public int size() {
            checkForComodification();
            return size;
        }

        public void add(int index, E element) {
            rangeCheckForAdd(index);
            checkForComodification();
            root.add(offset + index, element);
            updateSizeAndModCount(1);
        }

        public E remove(int index) {
            Objects.checkIndex(index, size);
            checkForComodification();
            E result = root.remove(offset + index);
            updateSizeAndModCount(-1);
            return result;
        }

        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            root.removeRange(offset + fromIndex, offset + toIndex);
            updateSizeAndModCount(fromIndex - toIndex);
        }

        public boolean addAll(Collection<? extends E> c) { return addAll(size, c); }

        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize == 0)
                return false;
            checkForComodification();
            root.addAll(offset + index, c);
            updateSizeAndModCount(cSize);
            return true;
        }

        public java.util.Iterator<E> iterator() { return listIterator(); }

        public ListIterator<E> listIterator(int index) {
            checkForComodification();
            rangeCheckForAdd(index);

            return new ListIterator<E>() {
                private final ListIterator<E> i = root.listIterator(offset + index);

                public boolean hasNext() { return nextIndex() < size; }

                public E next() {
                    if (hasNext())
                        return i.next();
                    else
                        throw new NoSuchElementException();
                }

                public boolean hasPrevious() { return previousIndex() >= 0; }

                public E previous() {
                    if (hasPrevious())
                        return i.previous();
                    else
                        throw new NoSuchElementException();
                }

                public int nextIndex() { return i.nextIndex() - offset; }
                public int previousIndex() { return i.previousIndex() - offset; }
                public void remove() {
                    i.remove();
                    updateSizeAndModCount(-1);
                }

                public void set(E e) { i.set(e); }

                public void add(E e) {
                    i.add(e);
                    updateSizeAndModCount(1);
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList<>(this, fromIndex, toIndex);
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) { return "Index: " + index + ", Size: " + size; }

        private void checkForComodification() {
            if (root.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }

        private void updateSizeAndModCount(int sizeChange) {
            SubList<E> slist = this;
            do {
                slist.size += sizeChange;
                slist.modCount = root.modCount;
                slist = slist.parent;
            } while (slist != null);
        }
    }

    private static class RandomAccessSubList<E>
            extends SubList<E> implements RandomAccess {
        /**
         * Constructs a sublist of an arbitrary AbstractList, which is not a RandomAccessSubList itself.
         */
        RandomAccessSubList(AbstractList<E> root, int fromIndex, int toIndex) {
            super(root, fromIndex, toIndex);
        }

        /**
         * Constructs a sublist of another RandomAccessSublist.
         */
        RandomAccessSubList(RandomAccessSubList<E> parent, int fromIndex, int toIndex) {
            super(parent, fromIndex, toIndex);
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new RandomAccessSubList<>(this, fromIndex, toIndex);
        }
    }
}

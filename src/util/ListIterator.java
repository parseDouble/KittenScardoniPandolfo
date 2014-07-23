package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator over the elements of a list.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ListIterator<E> implements Iterator<E> {

    /**
     * The next node from which the element must be returned.
     */

    private ListNode<E> next;

    /**
     * Builds an iterator from the given node of a list.
     *
     * @param next the next element from which the iteration starts
     */

    ListIterator(ListNode<E> next) {
	this.next = next;
    }

    /**
     * Determines if there is still some element to be scanned.
     *
     * @return true if and only if there is still some element to be
     *         scanned with this iterator
     */

    public boolean hasNext() {
	return next != null;
    }

    /**
     * Yields the next element from this iterator and advances to the next
     * element.
     *
     * @return the next element from this iterator
     * @throws NoSuchElementException if there is no next element anymore
     */

    public E next() {
	E result;

	try {
	    result = next.head;
	}
	catch (NullPointerException e) {
	    throw new NoSuchElementException("util.ListIterator: next()");
	}

	next = next.tail;

	return result;
    }

    /**
     * Yields the next element from this iterator.
     *
     * @return the next element from this iterator
     * @throws NoSuchElementException if there is no next element anymore
     */

    public E peep() {
	try {
	    return next.head;
	}
	catch (NullPointerException e) {
	    throw new NoSuchElementException("util.ListIterator: peep()");
	}
    }

    /**
     * Removes from the underlying collection the last element returned by
     * this iterator. This is operation is optional and has not been
     * implemented.
     *
     * @throws UnsupportedOperationException
     */

    public void remove() {
	throw new UnsupportedOperationException("util.List.remove");
    }
}
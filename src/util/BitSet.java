package util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A set of elements, implemented through a bitset of <tt>long</tt>s.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class BitSet<E> implements Iterable<E> {

    /**
     * The bits representing the set. A bit is 1 if and only if the
     * corresponding element is in the set.
     */

    private long[] bits;

    /**
     * The factory that generated this bitset.
     */

    private BitSetFactory<E> factory;

    /**
     * Builds an empty set belonging to the given factory.
     *
     * @param factory the factory
     */

    BitSet(BitSetFactory<E> factory) {
	bits = new long[1];
	this.factory = factory;
    }

    /**
     * Builds a clone of another bitset. The clone will belong to the same
     * factory.
     *
     * @param other the other set, which must be cloned
     */

    public BitSet(BitSet<E> other) {
	bits = new long[other.bits.length];
	System.arraycopy(other.bits,0,bits,0,bits.length);
	factory = other.factory;
    }

    /**
     * Builds a bitset whose set of possible elements
     * is shared with that of the argument. Its elements are those of the
     * given collection.
     *
     * @param other the other bitset
     * @param elements the elements put inside the bitset
     */

    BitSet(BitSetFactory<E> factory, Collection<E> elements) {
	this(factory);

	// we add all the <tt>elements</tt>
	for (E e: elements) add(e);
    }

    /**
     * Removes final 0's from the array <tt>bits</tt>.
     */

    private void normalise() {
	if (bits.length > 1) {
	    int remove = 0, pos = bits.length - remove - 1;
	    while (pos >= 1 && bits[pos] == 0L) {
		remove++;
		pos--;
	    }

	    if (remove > 0) {
		long[] newBits = new long[++pos];
		System.arraycopy(bits,0,newBits,0,pos);
		bits = newBits;
	    }
	}
    }

    /**
     * Removes the given element from this set.
     *
     * @param e the element to remove
     */

    public void remove(E e) {
	int pos = factory.indexOf(e);

	// if the element has not been seen before,
	// we don't have anything to remove
	if (pos == -1) return;

	// we check if the element is in this set
	if ((pos >> 6) >= bits.length) return;

	// we remove the element
	bits[pos >> 6] &= ~(1L << (pos & 0x3f));

	normalise();
    }

    /**
     * Removes the given elements from this set.
     *
     * @param other the elements to remove
     * @return true if and only if something has been removed
     * @throws DifferentFactoryException if this bitset and <tt>other</tt>
     *                                   are generated by different factories
     */

    public boolean removeAll(BitSet<E> other) {
	if (factory != other.factory)
	    throw new DifferentFactoryException("util.BitSet.removeAll");

	boolean removed = false;
	int pos = bits.length;
	if (other.bits.length < pos) pos = other.bits.length;

	for (--pos; pos >= 0; pos--) {
	    if ((bits[pos] & other.bits[pos]) != 0) removed = true;

	    bits[pos] &= ~other.bits[pos];
	}

	normalise();

	return removed;
    }

    /**
     * Removes all elements from this set.
     */

    public void clear() {
	bits = new long[1];
    }

    /**
     * Yields the number of elements in this set.
     *
     * @return the number of elements in this set
     */

    public int size() {
	int count = 0;

	for (int pos = bits.length - 1; pos >= 0; pos--)
	    for (int i = 63; i >= 0; i--)
		if (((1L << i) & bits[pos]) != 0) count++;

	return count;
    }

    /**
     * Adds the given element to this bitset.
     *
     * @param e the element to add
     */

    public void add(E e) {
	int pos = factory.indexOf(e);

	if (pos == -1) {
	    // the element has never been seen before: we add it to the
	    // set of all elements
	    factory.add(e);
	    pos = factory.indexOf(e);
	}

	if ((pos >> 6) >= bits.length) {
	    // the array <tt>bits</tt> is not long enough for the position
	    // of the element that we are adding
	    long[] newBits = new long[1 + (pos >> 6)];
	    System.arraycopy(bits,0,newBits,0,bits.length);
	    bits = newBits;
	}

	bits[pos >> 6] |= (1L << (pos & 0x3f));
    }

    /**
     * Determines if an element belongs to this set.
     *
     * @param e the element
     * @return true if and only if <tt>e</tt> is in this set
     */

    public boolean contains(E e) {
	int pos = factory.indexOf(e);

	// if the element has never been seen before, it is not inside this set
	if (pos == -1) return false;

	// otherwise we check if the bit is on
	return (pos >> 6) < bits.length &&
	    (bits[pos >> 6] & (1L << (pos & 0x3f))) != 0;	    
    }

    /**
     * Adds to this bitset all the elements of <tt>other</tt>. Both
     * bitsets must be defined on the same elements set.
     *
     * @param other the other bitset, whose elements must be added to this
     * @throws DifferentFactoryException if this bitset and <tt>other</tt>
     *                                   are generated by different factories
     */

    public void addAll(BitSet<E> other) throws DifferentFactoryException {
	if (factory != other.factory)
	    throw new DifferentFactoryException("util.BitSet.addAll");

	long[] newBits;

	if (other.bits.length > bits.length)
	    newBits = new long[other.bits.length];
	else
	    newBits = bits;

	for (int pos = newBits.length - 1; pos >= 0; pos--)
	    newBits[pos] =
		((pos < bits.length) ? bits[pos] : 0L)
		|
		((pos < other.bits.length) ? other.bits[pos] : 0L);

	bits = newBits;
    }

    /**
     * Leaves in this bitset only those elements which are also in the
     * <tt>other</tt> bitset. Both
     * bitsets must be defined on the same elements set.
     *
     * @param other the other bitset, whose elements must be retained in this
     *              bitset
     * @throws DifferentFactoryException if this bitset and <tt>other</tt>
     *                                   are generated by different factories
     */

    public void retainAll(BitSet<E> other) throws DifferentFactoryException {
	if (factory != other.factory)
	    throw new DifferentFactoryException("util.BitSet.retainAll");

	for (int pos = bits.length - 1; pos >= 0; pos--)
	    bits[pos] &= ((pos < other.bits.length) ? other.bits[pos] : 0L);

	normalise();
    }

    /**
     * Yields the intersection of this bitset and another. Neither of
     * them gets modified.
     *
     * @param other the other bitset
     * @return the intersection of this bitset and <tt>other</tt>
     * @throws DifferentFactoryException if this bitset and <tt>other</tt>
     *                                   are generated by different factories
     */

    public BitSet<E> intersection(BitSet<E> other)
	throws DifferentFactoryException {

	if (factory != other.factory)
	    throw new DifferentFactoryException("util.BitSet.intersection");

	BitSet<E> result;

	if (bits.length > other.bits.length) result = new BitSet<E>(other);
	else result = new BitSet<E>(this);

	for (int pos = result.bits.length - 1; pos >= 0; pos--)
	    result.bits[pos] = bits[pos] & other.bits[pos];

	result.normalise();

	return result;
    }

    /**
     * Yields a new set obtained from this by removing the given elements.
     *
     * @param other the elements to remove
     * @return a bitset obtained from <tt>this</tt> by removing the elements in
     *         <tt>other</tt>
     * @throws DifferentFactoryException if this bitset and <tt>other</tt>
     *                                   are generated by different factories
     */

    public BitSet<E> minus(BitSet<E> other) {
	if (factory != other.factory)
	    throw new DifferentFactoryException("util.BitSet.minus");

	BitSet<E> result = new BitSet<E>(this);

	int pos = bits.length;
	if (other.bits.length < pos) pos = other.bits.length;

	for (--pos; pos >= 0; pos--) result.bits[pos] &= ~other.bits[pos];

	result.normalise();

	return result;
    }

    /**
     * Yields the union of this bitset and another. Neither of them gets
     * modified.
     *
     * @param other the other bitset
     * @return the union of this bitset and <tt>other</tt>
     * @throws DifferentFactoryException if this bitset and <tt>other</tt>
     *                                   are generated by different factories
     */

    public BitSet<E> union(BitSet<E> other) throws DifferentFactoryException {
	if (factory != other.factory)
	    throw new DifferentFactoryException("util.BitSet.union");

	BitSet<E> result;
	int pos;

	if (bits.length < other.bits.length) {
	    result = new BitSet<E>(other);
	    pos = bits.length;
	} else {
	    result = new BitSet<E>(this);
	    pos = other.bits.length;
	}

	for (--pos; pos >= 0; pos--)
	    result.bits[pos] = bits[pos] | other.bits[pos];

	// there is no need to normalise since the two bitsets were already
	// normalised

	return result;
    }

    /**
     * Adds the <tt>other</tt> bitset to this bitset and yields the
     * bitset of the elements which have been actually added, since they
     * were not already in this bitset.
     *
     * @param other the bitset to add to this bitset
     * @return the bitset of the elements which were in <tt>other</tt> but
     *         not in this bitset
     * @throws DifferentFactoryException if this bitset and <tt>other</tt>
     *                                   are generated by different factories
     */

    public BitSet<E> addAllAndDifference(BitSet<E> other)
	throws DifferentFactoryException {

	if (factory != other.factory)
	    throw new DifferentFactoryException
		("util.BitSet.addAllAndDifference");

	if (this.includes(other)) return factory.getEmptySet();

	BitSet<E> added = new BitSet<E>(other);

	long[] newBits, ob = other.bits, ab = added.bits, tb = this.bits;

	if (ob.length > tb.length) newBits = new long[ob.length];
	else newBits = tb;

	int l1 = ab.length, l2 = tb.length;
	for (int pos = newBits.length - 1; pos >= 0; pos--) {
	    if (pos < l1 && pos < l2) {
		ab[pos] ^= (ab[pos] & tb[pos]);
		newBits[pos] = tb[pos] | ob[pos];
	    }
	    else if (pos < l1) newBits[pos] = ob[pos];
	    else if (pos < l2) newBits[pos] = tb[pos];
	}

	bits = newBits;

	added.normalise();

	return added;
    }

    /**
     * Determines if this bitset is empty.
     *
     * @return true if and only if this bitset is empty
     */

    public boolean isEmpty() {
	return bits.length == 1 && bits[0] == 0L;
    }

    /**
     * Yields an iterator over the elements of this list, from the first
     * towards the last one.
     *
     * @return the iterator
     */

    public Iterator<E> iterator() {
	return new BitSetIterator<E>(bits,factory);
    }

    /**
     * Yields the has code of this bitset.
     *
     * @return the hash code
     */

    public int hashCode() {
	long code = 0;
	for (int i = bits.length - 1; i >= 0; i--) code += bits[i];

	return (int)code + (int)(code >> 32);
    }

    /**
     * Determines if this bitset is equal to <tt>other</tt>, that is, if it
     * is defined over the same elements set and contains the same elements.
     *
     * @param other the other bitset
     * @return true if and only if <tt>other</tt> is a bitset defined on the
     *         same elements set and which has the same elements as this bitset
     * @throws DifferentFactoryException if this bitset and <tt>other</tt>
     *                                   are generated by different factories
     */
 
    public boolean equals(Object other) throws DifferentFactoryException {
	if (!(other instanceof BitSet)) return false;

	BitSet otherBS = (BitSet)other;

	// if the factories are the same, we compare the bitsets
	if (factory == otherBS.factory) {
	    long[] tb = this.bits, ob = otherBS.bits;

	    // we first try an inexpensive test
	    if (ob.length != tb.length) return false;

	    int l = tb.length, pos;

	    for (pos = 0; pos < l; pos++) if (tb[pos] != ob[pos]) return false;

	    return true;
	} else
	    throw new DifferentFactoryException("util.BitSet.equal");
    }

    /**
     * Determines if this bitset contains all elements of <tt>other</tt>, that
     * is, if it is defined over the same elements set and contains the same
     * elements.
     *
     * @param other the other bitset
     * @return true if and only if <tt>other</tt> is a bitset defined on the
     *         same elements set and which has no more elements than this bitset
     * @throws DifferentFactoryException if this bitset and <tt>other</tt>
     *                                   are generated by different factories
     */

    public boolean includes(BitSet<E> other) throws DifferentFactoryException {
	if (factory != other.factory)
	    throw new DifferentFactoryException("util.BitSet.includes");

	// if the factories are the same, we compare the bitsets
	if (other.bits.length > bits.length) return false;

	for (int pos = other.bits.length - 1; pos >= 0; pos--)
	    if ((bits[pos] | other.bits[pos]) != bits[pos]) return false;

	return true;
    }

    /**
     * Yields a string describing this bitset.
     *
     * @return a comma-separated description of the elements of this bitset
     */

    public String toString() {
	String result = "[";

	for (E e: this)
	    if (result.length() == 1) result += e.toString();
	    else result += "," + e.toString();

	return result + "]";
    }

    /**
     * An iterator over the elements of a bitset.
     *
     * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
     */

    private class BitSetIterator<E> implements Iterator<E> {

	/**
	 * The bits representing the set under iteration.
	 * A bit is 1 if and only if the
	 * corresponding element is in the set.
	 */

	private long[] bits;

	/**
	 * The factory that generated the set under iteration.
	 */

	private BitSetFactory<E> factory;

	/**
	 * The next bit which is to be considered.
	 */

	private int nextBit;

	/**
	 * Builds an iterator from the given bits and elements.
	 *
	 * @param bits the bits representing the set under iteration.
	 *             A bit is 1 if and only if the
	 *             corresponding element is in the set
	 * @param factory the factory that generated the set under iteration
	 */

	BitSetIterator(long[] bits, BitSetFactory<E> factory) {
	    this.bits = bits;
	    this.factory = factory;
	    this.nextBit = findNextBit(-1);
	}

	/**
	 * Yields the next non-zero bit in the bitset, starting from
	 * <tt>start</tt>.
	 *
	 * @param start the position (non-inclusive) from which the search
	 *              will proceed upwards
	 * @return the position of the next non-zero bit in the bitset,
	 *         starting from <tt>start</tt>, non-inclusive. -1 if no such
	 *         position exists
	 */

	private int findNextBit(int start) {
	    int l = bits.length << 6;
	    for (int next = start + 1; next < l; next++) {
		if (bits[next >> 6] == 0L) {
		    // if an entire long is empty, we go straight to the
		    // next long
		    next += 63;
		    continue;
		}
		if ((bits[next >> 6] & (1L << (next & 0x3f))) != 0)
		    return next;
	    }

	    return -1;
	}

	/**
	 * Determines if there is still some element to be scanned.
	 *
	 * @return true if and only if there is still some element to be
	 *         scanned with this iterator
	 */

	public boolean hasNext() {
	    return nextBit != -1;
	}

	/**
	 * Yields the next element from this iterator.
	 *
	 * @return the next element from this iterator
	 * @throws NoSuchElementException if there is no next element anymore
	 */

	public E next() {
	    int oldNextBit;

	    if ((oldNextBit = nextBit) == -1)
		throw new NoSuchElementException("util.BitSetIterator");

	    nextBit = findNextBit(nextBit);
	    return factory.get(oldNextBit);
	}

	/**
	 * Removes from the underlying collection the last element returned by
	 * this iterator. This is operation is optional and has not been
	 * implemented.
	 *
	 * @throws UnsupportedOperationException
	 */

	public void remove() {
	    throw new UnsupportedOperationException
		("util.BitSetIterator.remove");
	}
    }
}
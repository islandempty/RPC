package com.ie.protocol.collection.model;

/**
 * @author islandempty
 * @since 2021/6/1
 **/
import java.util.Comparator;

public class NaturalComparator<E extends Comparable<? super E>> implements Comparator<E> {

    //单例
    private static final NaturalComparator<?> INSTANCE = new NaturalComparator<>();

    /**
     * Constructor whose use should be avoided.
     * <p>
     * Please use the {@link #getInstance()} method whenever possible.
     */
    public NaturalComparator() {
        super();
    }

    /**
     * Gets the singleton instance of a ComparableComparator.
     * <p>
     * Developers are encouraged to use the comparator returned from this method
     * instead of constructing a new instance to reduce allocation and GC overhead
     * when multiple comparable comparators may be used in the same VM.
     *
     * @param <E> the element type
     * @return the singleton ComparableComparator
     */

    public static <E extends Comparable<? super E>> NaturalComparator<E> getInstance() {
        return (NaturalComparator<E>) INSTANCE;
    }

    /**
     * Compare the two {@link Comparable Comparable} arguments.
     * This method is equivalent to:
     * <pre>((Comparable)obj1).compareTo(obj2)</pre>
     *
     * @param a the first object to compare
     * @param b the second object to compare
     * @return negative if obj1 is less, positive if greater, zero if equal
     * @throws NullPointerException if <i>obj1</i> is <code>null</code>,
     *                              or when <code>((Comparable)obj1).compareTo(obj2)</code> does
     * @throws ClassCastException   if <i>obj1</i> is not a <code>Comparable</code>,
     *                              or when <code>((Comparable)obj1).compareTo(obj2)</code> does
     */

    @Override
    public int compare(final E a, final E b) {
        return a.compareTo(b);
    }
}


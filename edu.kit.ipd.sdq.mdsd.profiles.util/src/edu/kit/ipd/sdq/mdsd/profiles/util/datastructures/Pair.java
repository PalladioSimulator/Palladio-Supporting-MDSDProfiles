package edu.kit.ipd.sdq.mdsd.profiles.util.datastructures;

/**
 * A 2-tuple (also called double).
 * 
 * @param <A>
 *            the type of the first element
 * @param <B>
 *            the type of the second element
 * @author Max Kramer
 */
public class Pair<A, B> {
    /** The first element. */
    private final A first;
    /** The second element. */
    private final B second;

    /**
     * Constructs a new Pair using the given first and second element.
     * 
     * @param first
     *            the first element
     * @param second
     *            the second element
     */
    public Pair(final A first, final B second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair [first=" + getFirst() + ", second=" + getSecond() + "]";
    }

    /**
     * @return the first element
     */
    public A getFirst() {
        return first;
    }

    /**
     * @return the second element
     */
    public B getSecond() {
        return second;
    }
}

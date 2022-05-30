package puzzles.water;

import java.util.Objects;

/**
 * The bucket class
 *
 * @author Jack Schneider
 */
public class Bucket {
    private int size;
    private int fill;

    /**
     * Creates a new bucket
     * @param size the size of the bucket
     * @param fill the amount the bucket is filled
     */
    public Bucket(int size, int fill){
        this.size = size;
        this.fill = fill;
    }

    /**
     * Gets the size of the bucket
     * @return the size of the bucket
     */
    public int getSize() { return size;}

    /**
     * Gets the amount the bucket is filled
     * @return the amount the bucket is filled
     */
    public int getFill() { return fill;}

    /**
     * Compares two objects to see if they are equal
     * @param o the other object
     * @return the state of whether the objects are equivalent
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bucket bucket = (Bucket) o;
        return size == bucket.size && fill == bucket.fill;
    }

    /**
     * Hashes the bucket object
     * @return the hash of the bucket object
     */
    @Override
    public int hashCode() {
        return Objects.hash(size, fill);
    }

    /**
     * Returns the bucket object in string format
     * @return the bucket in string format
     */
    @Override
    public String toString() {
        return Integer.toString(fill);
    }
}

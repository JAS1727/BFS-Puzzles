package puzzles.water;

import solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * The water configuration class
 *
 * @author Jack Schneider
 */
public class WaterConfiguration implements Configuration {
    private int targetAmount;
    private ArrayList<Bucket> bucketsList;

    /**
     * Creates new water configuration
     * @param targetAmount the target amount of water
     * @param bucketsList a list of the buckets
     */
    public WaterConfiguration(int targetAmount, ArrayList<Bucket> bucketsList) {
        this.targetAmount = targetAmount;
        this.bucketsList = bucketsList;
    }

    /**
     * Returns the state of whether the configuration is the solution
     * @return the state of whether the configuration is the solution
     */
    @Override
    public boolean isSolution() {
        for (Bucket bucket : bucketsList) {
            if (bucket.getFill() == targetAmount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the neighbor configurations of the current configuration
     * @return the neighbor configurations of the current configuration
     */
    @Override
    public ArrayList<Configuration> getNeighbors() {
        ArrayList<Configuration> neighborsList = new ArrayList<>();
        for (int i = 0; i < bucketsList.size(); i++) {
            /* For filling buckets */
            if (bucketsList.get(i).getFill() != bucketsList.get(i).getSize()) {
                ArrayList<Bucket> newBucketList = new ArrayList<>();
                for (Bucket bucket : bucketsList) {
                    newBucketList.add(new Bucket(bucket.getSize(), bucket.getFill()));
                }
                newBucketList.set(i, new Bucket(bucketsList.get(i).getSize(), bucketsList.get(i).getSize()));
                neighborsList.add(new WaterConfiguration(targetAmount, newBucketList));
            }
            /* For dumping buckets */
            if (bucketsList.get(i).getFill() != 0) {
                ArrayList<Bucket> newBucketList = new ArrayList<>();
                for (Bucket bucket : bucketsList) {
                    newBucketList.add(new Bucket(bucket.getSize(), bucket.getFill()));
                }
                newBucketList.set(i, new Bucket(bucketsList.get(i).getSize(), 0));
                neighborsList.add(new WaterConfiguration(targetAmount, newBucketList));
                /* For emptying a bucket into another */
                for (int j = 0; j < bucketsList.size(); j++) {
                    if(i != j) {
                        ArrayList<Bucket> emptyingBucketList = new ArrayList<>();
                        for (Bucket bucket : bucketsList) {
                            emptyingBucketList.add(new Bucket(bucket.getSize(), bucket.getFill()));
                        }
                        int firstBucketFill;
                        int secondBucketFill;
                        if (bucketsList.get(i).getFill() + bucketsList.get(j).getFill() > bucketsList.get(j).getSize()) {
                            firstBucketFill = bucketsList.get(i).getFill() -
                                    (bucketsList.get(j).getSize() - bucketsList.get(j).getFill());
                            secondBucketFill = bucketsList.get(j).getSize();
                        } else {
                            firstBucketFill = 0;
                            secondBucketFill = bucketsList.get(j).getFill() + bucketsList.get(i).getFill();
                        }
                        emptyingBucketList.set(i, new Bucket(bucketsList.get(i).getSize(), firstBucketFill));
                        emptyingBucketList.set(j, new Bucket(bucketsList.get(j).getSize(), secondBucketFill));
                        neighborsList.add(new WaterConfiguration(targetAmount, emptyingBucketList));
                    }

                }
            }
        }
        return neighborsList;
    }

    /**
     * Compares two configurations to see if they are equal
     * @param anotherConfig the other configuration
     * @return the state of whether the configurations being compared are equal
     */
    @Override
    public boolean equals(Object anotherConfig) {
        boolean match = true;
        if (anotherConfig instanceof WaterConfiguration) {
            if (this.targetAmount == ((WaterConfiguration) anotherConfig).targetAmount &&
                    this.bucketsList.size() == ((WaterConfiguration) anotherConfig).bucketsList.size()) {
                for (int i = 0; i < this.bucketsList.size(); i++) {
                    if(this.bucketsList.get(i).getFill() !=
                            ((WaterConfiguration) anotherConfig).bucketsList.get(i).getFill()) {
                        match = false;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return match;
    }

    /**
     * Hashes the water configuration
     * @return the hash of the water configuration
     */
    @Override
    public int hashCode() {
        return Objects.hash(targetAmount, bucketsList);
    }

    /**
     * Returns the water configuration in string form
     * @return the water configuration in string form
     */
    @Override
    public String toString() {
        return Arrays.toString(bucketsList.toArray());
    }
}

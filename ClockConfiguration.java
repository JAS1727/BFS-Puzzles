package puzzles.clock;

import solver.Configuration;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The clock configuration class
 *
 * @author Jack Schneider
 */
public class ClockConfiguration implements Configuration {
    private int hours;
    private int current;
    private int solution;

    /**
     * Creates a new clock configuration
     * @param hours the total number of hours on the clock
     * @param current the current hour of the clock
     * @param solution the solution hour of the clock
     */
    public ClockConfiguration(int hours, int current, int solution) {
        this.hours = hours;
        this.current = current;
        this.solution = solution;
    }

    /**
     * Returns the state of whether the configuration is the solution
     * @return the state of whether the configuration is the solution
     */
    @Override
    public boolean isSolution() {
        if (current == solution) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the neighbor configurations of the current configuration
     * @return the neighbor configurations of the current configuration
     */
    @Override
    public ArrayList<Configuration> getNeighbors() {
        ClockConfiguration leftNeighbor;
        ClockConfiguration rightNeighbor;
        if(current + 1 > hours) {
            rightNeighbor = new ClockConfiguration(hours, 1, solution);
        } else {
            rightNeighbor = new ClockConfiguration(hours, current + 1, solution);;
        }
        if(current - 1 < 1) {
            leftNeighbor = new ClockConfiguration(hours, hours, solution);;
        } else {
            leftNeighbor = new ClockConfiguration(hours, current - 1, solution);;
        }
        ArrayList<Configuration> list = new ArrayList<>();
        list.add(leftNeighbor);
        list.add(rightNeighbor);
        return list;
    }

    /**
     * Compares two configurations to see if they are equal
     * @param anotherConfig the other configuration
     * @return the state of whether the configurations being compared are equal
     */
    @Override
    public boolean equals(Object anotherConfig) {
        if (anotherConfig instanceof ClockConfiguration) {
            if (this.hours == ((ClockConfiguration) anotherConfig).hours &&
            this.current == ((ClockConfiguration) anotherConfig).current &&
            this.solution == ((ClockConfiguration) anotherConfig).solution) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Hashes the clock configuration
     * @return the hash of the clock configuration
     */
    @Override
    public int hashCode() {
        return Objects.hash(hours, current, solution);
    }

    /**
     * Returns the clock configuration in string form
     * @return the clock configuration in string form
     */
    @Override
    public String toString() {
        return Integer.toString(current);
    }
}

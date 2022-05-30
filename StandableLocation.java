package puzzles.tipover.model;

/**
 * A stand-able location interface stubbing out necessary functions
 * @author Jack Schneider
 * November 2021
 */
public interface StandableLocation {

    /**
     * Returns the state of whether the stand-able location is also the goal location
     * @return the state of whether the stand-able location is also the goal location
     */
    boolean isGoalLocation();

    /**
     * Returns the state of whether the stand-able location is also the tipper location
     * @return the state of whether the stand-able location is also the tipper location
     */
    boolean isTipperLocation();
}

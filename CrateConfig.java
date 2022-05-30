package puzzles.tipover.model;

import util.Coordinates;

/**
 * A crate configuration used to create a create
 * @author Jack Schneider
 * November 2021
 */
public class CrateConfig implements StandableLocation{

    Coordinates selfCoordinates;
    Coordinates tipperCoordinates;
    Coordinates goalCoordinates;

    /**
     * Creates a new crate
     * @param selfCoordinates the crate's own coordinates
     * @param tipperCoordinates the coordinates of the tipper
     * @param goalCoordinates the coordinates of the goal
     */
    public CrateConfig(Coordinates selfCoordinates, Coordinates tipperCoordinates, Coordinates goalCoordinates) {
        this.selfCoordinates = selfCoordinates;
        this.tipperCoordinates = tipperCoordinates;
        this.goalCoordinates = goalCoordinates;
    }

    /**
     * Returns the state of whether the crate location is also the goal location
     * @return the state of whether the crate location is also the goal location
     */
    @Override
    public boolean isGoalLocation() {
        if(selfCoordinates.equals(goalCoordinates)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the state of whether the crate location is also the tipper location
     * @return the state of whether the crate location is also the tipper location
     */
    @Override
    public boolean isTipperLocation() {
        if(selfCoordinates.equals(tipperCoordinates)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the crate in string format
     * @return the crate in string format
     */
    public String toString() {
        if (isTipperLocation()) {
            return "*" + 1;
        } else if (isGoalLocation()) {
            return "!" + 1;
        } else {
            return " " + 1;
        }
    }
}

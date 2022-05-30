package puzzles.tipover.model;

import util.Coordinates;

/**
 * A tower configuration class used to create towers
 * @author Jack Schneider
 * November 2021
 */
public class TowerConfig implements StandableLocation{

    private Coordinates selfCoordinates;
    private Coordinates tipperCoordinates;
    private Coordinates goalCoordinates;
    private int towerHeight;

    /**
     * Creates a new tower
     * @param selfCoordinates the tower's own coordinates
     * @param tipperCoordinates the coordinates of the tipper
     * @param goalCoordinates the coordinates of the goal
     * @param towerHeight the height of the tower
     */
    public TowerConfig(Coordinates selfCoordinates, Coordinates tipperCoordinates, Coordinates goalCoordinates, int towerHeight) {
        this.selfCoordinates = selfCoordinates;
        this.tipperCoordinates = tipperCoordinates;
        this.goalCoordinates = goalCoordinates;
        this.towerHeight = towerHeight;
    }

    /**
     * Returns the state of whether the tower location is also the goal location
     * @return the state of whether the tower location is also the goal location
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
     * Returns the state of whether the tower location is also the tipper location
     * @return the state of whether the tower location is also the tipper location
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
     * Gets the height of the tower
     * @return the height of the tower
     */
    public int getTowerHeight() {return towerHeight;}

    /**
     * Returns the tower in string format
     * @return the tower in string format
     */
    public String toString() {
        if (isTipperLocation()) {
            return "*" + towerHeight;
        } else if (isGoalLocation()) {
            return "!" + towerHeight;
        } else {
            return " " + towerHeight;
        }
    }
}

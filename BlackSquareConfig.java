package puzzles.tipover.model;

import util.Coordinates;

/**
 * A black square class used to create a blank square without a tower or crate
 * @author Jack Schneider
 * November 2021
 */
public class BlackSquareConfig {

    private Coordinates selfCoordinates;

    /**
     * Creates a new black square configuration
     * @param selfCoordinates the black square's own coordinates
     */
    public BlackSquareConfig(Coordinates selfCoordinates) {
        this.selfCoordinates = selfCoordinates;
    }

    /**
     * Gets the coordinates of the black square
     * @return the coordinates of the black square
     */
    public Coordinates getSelfCoordinates() {
        return selfCoordinates;
    }

    /**
     * Returns the black square in string format
     * @return the black square in string format
     */
    public String toString() {return " _";}
}

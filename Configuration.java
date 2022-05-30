package solver;

import java.util.ArrayList;

/**
 * Configuration abstraction for the solver algorithm
 *
 * @author Jack Schneider
 * November 2021
 */
public interface Configuration {

    /**
     * Returns the state of whether the configuration is the solution
     * @return the state of whether the configuration is the solution
     */
    boolean isSolution();

    /**
     * Returns the neighbor configurations of the current configuration
     * @return the neighbor configurations of the current configuration
     */
    ArrayList<Configuration> getNeighbors();
}

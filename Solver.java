package solver;

import puzzles.tipover.model.TipOverConfig;
import util.Coordinates;
import util.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class contains a universal algorithm to find a path from a starting
 * configuration to a solution, if one exists
 *
 * @author Jack Schneider
 */
public class Solver {
    LinkedList<Configuration> queue = new LinkedList<>();
    private HashMap<Configuration, Configuration> predecessor = new HashMap<>();
    private Configuration startConfig;
    private Configuration endConfig;

    /**
     * Creates a new solver
     * @param startConfig the configuration the search starts from
     */
    public Solver(Configuration startConfig) {
        this.startConfig = startConfig;
    }

    /**
     * Performs Breadth first search and prints the following throughout the process:
     * the total number configurations generated, the number of unique configurations generated,
     * and a list of steps towards the end configuration if it is found.
     */
    public void performBFS(boolean debug) {
        queue.add(startConfig);
        predecessor.put(startConfig, startConfig);
        int totalConfigs = 1;
        int uniqueConfigs = 1;
        boolean solutionFound = false;
        while (!queue.isEmpty()) {
            Configuration current = queue.remove(0);
            if (current.isSolution()) {
                solutionFound = true;
                endConfig = current;
                break;
            }
            for (Configuration nbr : current.getNeighbors()) {
                if(!predecessor.containsKey(nbr)) {
                    predecessor.put(nbr, current);
                    queue.add(nbr);
                    uniqueConfigs++;
                }
                totalConfigs++;
            }
        }
        if (debug){
            displayConfigs(totalConfigs, uniqueConfigs, solutionFound);
        }
    }

    /**
     * Returns the path of configurations from the start configuration to the end configuration
     * @return the path of configurations
     */
    public LinkedList<Configuration> getPath() {
        LinkedList<Configuration> path = new LinkedList<>();
        if (predecessor.containsKey(endConfig)) {
            Configuration currentConfig = endConfig;
            while (!currentConfig.equals(startConfig)) {
                path.add(0, currentConfig);
                currentConfig = predecessor.get(currentConfig);
            }
            path.add(0, startConfig);
        }
        return path;
    }

    /**
     * Performs the BFS search without displaying configurations and returns the configuration representing
     * the next step towards the solution.
     * @return the configuration representing the next step towards the solution
     */
    public Configuration hint() {
        performBFS(false);
        LinkedList<Configuration> path = getPath();
        if(path.size() > 0) {
            return getPath().get(1);
        } else {
            Object placeholder = new Object();
            Coordinates placeholderCoordinates = new Coordinates(0,0);
            return new TipOverConfig(new Grid(placeholder,0,0),
                    0,0,placeholderCoordinates,placeholderCoordinates);
        }
    }

    /**
     * Displays the total configurations, unique configurations, and steps of the puzzle.
     * @param totalConfigs the total amount of configurations generated
     * @param uniqueConfigs the amount of unique configurations generated
     * @param solutionFound the state of whether the solution has been found
     */
    public void displayConfigs(int totalConfigs, int uniqueConfigs, boolean solutionFound) {
        System.out.println("Total configs: " + totalConfigs);
        System.out.println("Unique configs: " + uniqueConfigs);
        if (queue.isEmpty() && !solutionFound) {
            System.out.println("No solution");
        } else {
            int i = 0;
            for(Configuration config: getPath()) {
                System.out.println("Step " + i + ": " + config);
                i++;
            }
        }
    }
}

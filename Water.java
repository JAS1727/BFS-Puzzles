package puzzles.water;

import solver.Solver;

import java.util.ArrayList;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Jack Schneider
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main( String[] args ) {
        if ( args.length < 2 ) {
            System.out.println(
                    ( "Usage: java Water amount bucket1 bucket2 ..." )
            );
        }
        else {
            ArrayList<Bucket> bucketsList = new ArrayList();
            ArrayList<String> bucketsSizeList = new ArrayList();
            for (int i =1; i < args.length; i++) {
                bucketsList.add(new Bucket(Integer.parseInt(args[i]), 0));
                bucketsSizeList.add(args[i]);
            }
            System.out.println("Amount: " + args[0] + ", Buckets: " + bucketsSizeList);
            WaterConfiguration startConfig = new WaterConfiguration(Integer.parseInt(args[0]), bucketsList);
            Solver waterSolver = new Solver(startConfig);
            waterSolver.performBFS(true);
        }
    }
}

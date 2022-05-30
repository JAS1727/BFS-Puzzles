package puzzles.tipover;

import puzzles.tipover.model.BlackSquareConfig;
import puzzles.tipover.model.CrateConfig;
import puzzles.tipover.model.TipOverConfig;
import puzzles.tipover.model.TowerConfig;
import solver.Solver;
import util.Coordinates;
import util.Grid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Main class of the Tip Over puzzle.
 * @author Jack Schneider
 * November 2021
 */
public class TipOver {

    /**
     * Runs an instance of the Tip Over puzzle
     * @param args the data file for the puzzle
     */
    public static void main( String[] args ) {
        if ( args.length < 1 ) {
            System.err.println("No file found");
        } else {
            try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
                String[] newLineParsed = in.readLine().split(" ");
                int tableHeight = Integer.parseInt(newLineParsed[0]);
                int tableWidth = Integer.parseInt(newLineParsed[1]);
                Coordinates tipperCoordinates = new Coordinates(Integer.parseInt(newLineParsed[2]), Integer.parseInt(newLineParsed[3]));
                Coordinates goalCoordinates = new Coordinates(Integer.parseInt(newLineParsed[4]), Integer.parseInt(newLineParsed[5]));
                Object placeholder = new Object();
                Grid table = new Grid(placeholder,tableHeight, tableWidth);
                String newLine;
                int heightCounter = 0;
                while ((newLine = in.readLine()) != null && newLine.length() > (2*tableWidth)-2) {
                    newLineParsed = newLine.split(" ");
                    for (int x=0; x < tableWidth; x++) {
                        Coordinates selfCoordinates = new Coordinates(heightCounter,x);
                        // If object is a black square
                        if (Integer.parseInt(newLineParsed[x]) == 0) {
                            table.set(new BlackSquareConfig(selfCoordinates), heightCounter, x);
                        }
                        // If object is a crate
                        else if (Integer.parseInt(newLineParsed[x]) == 1) {
                            table.set(new CrateConfig(selfCoordinates, tipperCoordinates,goalCoordinates), heightCounter, x);
                        }
                        // If object is a tower
                        else if (Integer.parseInt(newLineParsed[x]) > 1) {
                            int towerHeight = Integer.parseInt(newLineParsed[x]);
                            table.set(new TowerConfig(selfCoordinates, tipperCoordinates, goalCoordinates, towerHeight), heightCounter, x);
                        }
                    }
                    heightCounter++;
                }
                TipOverConfig startConfig = new TipOverConfig(table, tableWidth, tableHeight, tipperCoordinates, goalCoordinates);
                Solver tipOverSolver = new Solver(startConfig);
                tipOverSolver.performBFS(true);
            } catch (IOException i){
                System.err.println("IOException error");
            }
        }
    }
}

package puzzles.tipover.model;

import solver.Configuration;
import solver.Solver;
import util.Coordinates;
import util.Grid;
import util.Observer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A Tip Over model class used to create a new Tip Over model
 * @author Jack Schneider
 * November 2021
 */
public class TipOverModel {

    private TipOverConfig currentConfig;
    private List<Observer<TipOverModel, Object>> observers;
    private String initFile;
    private Solver solver;
    private boolean tipMovePerformed;
    private boolean reloadState;
    private boolean nonMoveState;

    /**
     * Creates a new Tip Over model using a file name
     * @param filename the name of the file used to create the model
     * @throws FileNotFoundException
     */
    public TipOverModel(String filename) throws FileNotFoundException {
        this.observers = new LinkedList<>();
        initFile = filename;
        currentConfig = new TipOverConfig(filename);
        tipMovePerformed = false;
        reloadState = false;
        nonMoveState = false;
    }

    /**
     * Creates a new empty Tip Over model with a table without rows nor columns
     */
    public TipOverModel(){
        currentConfig = new TipOverConfig(new Grid(new Object(),0,0),
                0,0,new Coordinates(0,0), new Coordinates(0,0));
    }

    /**
     * Loads a new puzzle from the provided file
     * @param filename the name of the provided file
     */
    public void load(String filename) {
        try{
            this.currentConfig = new TipOverConfig(filename);
            initFile = filename;
            reloadState = true;
            announce(null);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    /**
     * Reloads the most recently loaded file back to its initial state
     * @throws FileNotFoundException
     */
    public void reload() throws FileNotFoundException{
        this.currentConfig = new TipOverConfig(initFile);
        reloadState = true;
        announce(null);
    }

    /**
     * Moves the tipper in a legal direction of either north, east, south, or west
     * @param direction the direction the tipper is instructed to move
     */
    public void move(String direction) {
        ArrayList<Configuration> neighbors = currentConfig.getNeighbors();
        Configuration comparableConfig = currentConfig;
        for (Configuration config : neighbors) {
            TipOverConfig tipConfig = (TipOverConfig) config;
            if (direction.equals("N") && tipConfig.getTipperCoordinates().row() ==
                    currentConfig.getTipperCoordinates().row() -1) {
                currentConfig = tipConfig;
                if (currentConfig.getTable().get(currentConfig.getTipperCoordinates().row()+1,
                        currentConfig.getTipperCoordinates().col()) instanceof BlackSquareConfig) {
                    System.out.println("A tower has been tipped over.");
                    tipMovePerformed = true;
                }
                announce(null);
            } else if (direction.equals("E") && tipConfig.getTipperCoordinates().col() ==
                    currentConfig.getTipperCoordinates().col() +1) {
                currentConfig = tipConfig;
                if (currentConfig.getTable().get(currentConfig.getTipperCoordinates().row(),
                        currentConfig.getTipperCoordinates().col() -1) instanceof BlackSquareConfig) {
                    System.out.println("A tower has been tipped over.");
                    tipMovePerformed = true;
                }
                announce(null);
            } else if (direction.equals("S") && tipConfig.getTipperCoordinates().row() ==
                    currentConfig.getTipperCoordinates().row() +1) {
                currentConfig = tipConfig;
                if (currentConfig.getTable().get(currentConfig.getTipperCoordinates().row()-1,
                        currentConfig.getTipperCoordinates().col()) instanceof BlackSquareConfig) {
                    System.out.println("A tower has been tipped over.");
                    tipMovePerformed = true;
                }
                announce(null);
            } else if (direction.equals("W") && tipConfig.getTipperCoordinates().col() ==
                    currentConfig.getTipperCoordinates().col() -1) {
                currentConfig = tipConfig;
                if (currentConfig.getTable().get(currentConfig.getTipperCoordinates().row(),
                        currentConfig.getTipperCoordinates().col()+1) instanceof BlackSquareConfig) {
                    System.out.println("A tower has been tipped over.");
                    tipMovePerformed = true;
                }
                announce(null);
            }
        }
        if (comparableConfig.equals(currentConfig)) {
            System.out.println("No crate or tower there.");
            nonMoveState = true;
            announce(null);
        }
    }

    /**
     * Provides a hint for the player by moving the tipper one step on a correct path
     * towards the goal
     */
    public void hint() {
        this.solver = new Solver(currentConfig);
        TipOverConfig hintConfig = (TipOverConfig) solver.hint();
        if (hintConfig.getTableHeight() == 0) {
            System.out.println("Unsolvable board");
        } else {
            if(currentConfig.getTable().get(currentConfig.getTipperCoordinates().row(),
                    currentConfig.getTipperCoordinates().col()) instanceof TowerConfig &&
                    hintConfig.getTable().get(currentConfig.getTipperCoordinates().row(),
                            currentConfig.getTipperCoordinates().col()) instanceof BlackSquareConfig) {
                System.out.println("A tower has been tipped over.");
                tipMovePerformed = true;
            }
            currentConfig = new TipOverConfig(hintConfig.getTable(), hintConfig.getTableWidth(),
                    hintConfig.getTableHeight(), hintConfig.getTipperCoordinates(),
                    hintConfig.getGoalCoordinates());
        }
        announce(null);
    }

    /**
     * Displays the current board
     */
    public void show() {
        System.out.println(currentConfig);
    }

    /**
     * Helps the player by displaying all legal commands
     */
    public void help() {
        System.out.println("Legal commands are...");
        System.out.println("\t> help : Show all commands.");
        System.out.println("\t> move {north|south|east|west}: Go in given direction, possible tipper a tower. (1 argument)");
        System.out.println("\t> reload filename: Load the most recent file again.");
        System.out.println("\t> load {board-file-name}: Load a new game board file. (1 argument)");
        System.out.println("\t> hint Make the next move for me.");
        System.out.println("\t> show Display the board.");
        System.out.println("\t> quit\n");
    }

    /**
     * Terminates the program
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * Adds an observer to the observers list
     * @param obs the observer object added to the list
     */
    public void addObserver( Observer< TipOverModel, Object > obs ) {
        this.observers.add( obs );
    }

    /**
     * Updates all observers in the observers list
     * @param arg argument used in the update function
     */
    private void announce( String arg ) {
        for ( var obs : this.observers ) {
            obs.update( this, arg );
        }
    }

    /**
     * Gets the current configuration
     * @return the current configuration
     */
    public Configuration getCurrentConfig() {return currentConfig;}

    /**
     * Gets the state of a tip move being performed
     * @return the state of a tip move being performed
     */
    public boolean getTipMovePerformed() {return tipMovePerformed;}

    /**
     * Sets the state of the tipMovePerformed boolean
     * @param state the given state
     */
    public void setTipMovePerformed(boolean state) {tipMovePerformed = state;}

    /**
     * Gets the state of reload
     * @return the state of reload
     */
    public boolean getReloadState() {return reloadState;}

    /**
     * Sets the state of reload
     * @param state the state
     */
    public void setReloadState(boolean state) {reloadState = state;}

    /**
     * Gets the state of if a non move has been made
     * @return the state of if a non move has been made
     */
    public boolean getNonMoveState() {return nonMoveState;}

    /**
     * Sets the state of if a non move has been made
     * @param state the chosen state
     */
    public void setNonMoveState(boolean state) {nonMoveState = state;}
}

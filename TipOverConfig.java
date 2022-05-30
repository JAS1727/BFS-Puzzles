package puzzles.tipover.model;

import solver.Configuration;
import util.Coordinates;
import util.Grid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A Tip Over game configuration class used to create a Tip Over game configuration
 * @author Jack Schneider
 * November 2021
 */
public class TipOverConfig implements Configuration {

    private Grid table;
    private int tableWidth;
    private int tableHeight;
    private Coordinates tipperCoordinates;
    private Coordinates goalCoordinates;
    private int tipperRow;
    private int tipperColumn;

    /**
     * Creates a new Tip Over game configuration
     * @param table the table of the game
     * @param tableWidth the width of the table
     * @param tableHeight the height of the table
     * @param tipperCoordinates the coordinates of the tipper
     * @param goalCoordinates the coordinates of the goal
     */
    public TipOverConfig(Grid table, int tableWidth, int tableHeight, Coordinates tipperCoordinates, Coordinates goalCoordinates) {
        Object placeHolder = new Object();
        Grid newTable = new Grid(placeHolder, tableHeight, tableWidth);
        for(int y = 0; y < tableHeight;y++) {
            for (int x = 0; x < tableWidth;x++) {
                if (table.get(y,x) instanceof CrateConfig) {
                    newTable.set(new CrateConfig(new Coordinates(y,x),tipperCoordinates,goalCoordinates),y,x);
                } else if (table.get(y,x) instanceof TowerConfig) {
                    newTable.set(new TowerConfig(new Coordinates(y,x),tipperCoordinates,goalCoordinates, ((TowerConfig) table.get(y,x)).getTowerHeight()),y,x);
                } else {
                    newTable.set(table.get(y,x),y,x);
                }
            }
        }
        this.table = newTable;
        this.tableWidth = tableWidth;
        this.tableHeight = tableHeight;
        this.tipperCoordinates = tipperCoordinates;
        this.goalCoordinates = goalCoordinates;
        this.tipperRow = tipperCoordinates.row();
        this.tipperColumn = tipperCoordinates.col();
    }

    /**
     * Creates a new Tip Over game configuration based on a file
     * @param filename the name of the file used to create the Tip Over game configuration
     * @throws FileNotFoundException
     */
    public TipOverConfig(String filename) throws FileNotFoundException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] newLineParsed = in.readLine().split(" ");
            this.tableHeight = Integer.parseInt(newLineParsed[0]);
            this.tableWidth = Integer.parseInt(newLineParsed[1]);
            this.tipperCoordinates = new Coordinates(Integer.parseInt(newLineParsed[2]), Integer.parseInt(newLineParsed[3]));
            this.tipperRow = tipperCoordinates.row();
            this.tipperColumn = tipperCoordinates.col();
            this.goalCoordinates = new Coordinates(Integer.parseInt(newLineParsed[4]), Integer.parseInt(newLineParsed[5]));
            Object placeholder = new Object();
            this.table = new Grid(placeholder,tableHeight, tableWidth);
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
        } catch(IOException i) {}
    }

    /**
     * Returns the state of whether the Tip Over game configuration is the solution configuration
     * @return the state of whether the Tip Over game configuration is the solution configuration
     */
    @Override
    public boolean isSolution() {
        if (tipperCoordinates.equals(goalCoordinates)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the neighbor configurations of the Tip Over game configuration
     * @return the neighbor configurations of the Tip Over game configuration
     */
    @Override
    public ArrayList<Configuration> getNeighbors() {
        ArrayList<Configuration> neighborsList = new ArrayList<>();
        // A placeholder value for when creating parameter object for checkSurroundingCoordinates
        int placeHolderInt = 0;
        // A placeholder coordinate for when creating parameter object for checkSurroundingCoordinates
        Coordinates placeHolderCoordinates = new Coordinates(placeHolderInt, placeHolderInt);
        // Looks if surrounding coordinates are associated with crates and adds a
        // new TipOverConfig with new tipper location to neighborsList
        for (Coordinates crateCoordinate : checkSurroundingCoordinates(new CrateConfig
                (placeHolderCoordinates, placeHolderCoordinates, placeHolderCoordinates))) {
            neighborsList.add(new TipOverConfig(table, tableWidth, tableHeight, crateCoordinate,goalCoordinates));
        }
        // Looks if surrounding coordinates are associated with towers and adds a
        // new TipOverConfig with new tipper location to neighborsList
        for (Coordinates towerCoordinate : checkSurroundingCoordinates(new TowerConfig
                (placeHolderCoordinates, placeHolderCoordinates, placeHolderCoordinates, placeHolderInt))) {
            neighborsList.add(new TipOverConfig(table, tableWidth, tableHeight, towerCoordinate,goalCoordinates));
        }
        // Adds necessary functionality when tipper is on tower
        if (table.get(tipperRow, tipperColumn) instanceof TowerConfig) {
            Object placeholder = new Object();
            Grid northTable = new Grid(placeholder, tableHeight, tableWidth);
            Grid eastTable = new Grid(placeholder, tableHeight, tableWidth);
            Grid southTable = new Grid(placeholder, tableHeight, tableWidth);
            Grid westTable = new Grid(placeholder, tableHeight, tableWidth);
            for(int y = 0; y < tableHeight;y++) {
                for (int x = 0; x < tableWidth;x++) {
                    if (table.get(y,x) instanceof CrateConfig) {
                        northTable.set(new CrateConfig(new Coordinates(y,x),tipperCoordinates,goalCoordinates),y,x);
                        eastTable.set(new CrateConfig(new Coordinates(y,x),tipperCoordinates,goalCoordinates),y,x);
                        southTable.set(new CrateConfig(new Coordinates(y,x),tipperCoordinates,goalCoordinates),y,x);
                        westTable.set(new CrateConfig(new Coordinates(y,x),tipperCoordinates,goalCoordinates),y,x);
                    } else if (table.get(y,x) instanceof TowerConfig) {
                        northTable.set(new TowerConfig(new Coordinates(y,x),tipperCoordinates,goalCoordinates, ((TowerConfig) table.get(y,x)).getTowerHeight()),y,x);
                        eastTable.set(new TowerConfig(new Coordinates(y,x),tipperCoordinates,goalCoordinates, ((TowerConfig) table.get(y,x)).getTowerHeight()),y,x);
                        southTable.set(new TowerConfig(new Coordinates(y,x),tipperCoordinates,goalCoordinates, ((TowerConfig) table.get(y,x)).getTowerHeight()),y,x);
                        westTable.set(new TowerConfig(new Coordinates(y,x),tipperCoordinates,goalCoordinates, ((TowerConfig) table.get(y,x)).getTowerHeight()),y,x);
                    } else {
                        northTable.set(table.get(y,x),y,x);
                        eastTable.set(table.get(y,x),y,x);
                        southTable.set(table.get(y,x),y,x);
                        westTable.set(table.get(y,x),y,x);
                    }
                }
            }
            northTable.set(new BlackSquareConfig(tipperCoordinates), tipperRow, tipperColumn);
            eastTable.set(new BlackSquareConfig(tipperCoordinates), tipperRow, tipperColumn);
            southTable.set(new BlackSquareConfig(tipperCoordinates), tipperRow, tipperColumn);
            westTable.set(new BlackSquareConfig(tipperCoordinates), tipperRow, tipperColumn);
            boolean northTableUsed = false;
            boolean eastTableUsed = false;
            boolean southTableUsed = false;
            boolean westTableUsed = false;
            Coordinates northTableTipperCoordinates = new Coordinates(tipperRow-1,tipperColumn);
            Coordinates eastTableTipperCoordinates = new Coordinates(tipperRow,tipperColumn+1);
            Coordinates southTableTipperCoordinates = new Coordinates(tipperRow+1,tipperColumn);
            Coordinates westTableTipperCoordinates = new Coordinates(tipperRow,tipperColumn-1);
            for(Coordinates blackSquareCoordinate : checkSurroundingCoordinates(new BlackSquareConfig(placeHolderCoordinates))) {
                // Checks for north table coordinates and sets them to north table
                if (blackSquareCoordinate.row() < tipperRow) {
                    northTableUsed = true;
                    northTable.set(new CrateConfig(blackSquareCoordinate, northTableTipperCoordinates,
                            goalCoordinates),blackSquareCoordinate.row(), blackSquareCoordinate.col());
                }
                // Checks for east table coordinates and sets them to east table
                if (blackSquareCoordinate.col() > tipperColumn) {
                    eastTableUsed = true;
                    eastTable.set(new CrateConfig(blackSquareCoordinate, eastTableTipperCoordinates,
                            goalCoordinates),blackSquareCoordinate.row(), blackSquareCoordinate.col());
                }
                // Checks for south table coordinates and sets them to south table
                if (blackSquareCoordinate.row() > tipperRow) {
                    southTableUsed = true;
                    southTable.set(new CrateConfig(blackSquareCoordinate, southTableTipperCoordinates,
                            goalCoordinates),blackSquareCoordinate.row(), blackSquareCoordinate.col());
                }
                // Checks for west table coordinates and sets them to west table
                if (blackSquareCoordinate.col() < tipperColumn) {
                    westTableUsed = true;
                    westTable.set(new CrateConfig(blackSquareCoordinate, westTableTipperCoordinates,
                            goalCoordinates),blackSquareCoordinate.row(), blackSquareCoordinate.col());
                }
            }
            if (northTableUsed) {
                neighborsList.add(new TipOverConfig(northTable, tableWidth, tableHeight,
                        northTableTipperCoordinates, goalCoordinates));
            }
            if (eastTableUsed) {
                neighborsList.add(new TipOverConfig(eastTable, tableWidth, tableHeight,
                        eastTableTipperCoordinates, goalCoordinates));
            }
            if (southTableUsed) {
                neighborsList.add(new TipOverConfig(southTable, tableWidth, tableHeight,
                        southTableTipperCoordinates, goalCoordinates));
            }
            if (westTableUsed) {
                neighborsList.add(new TipOverConfig(westTable, tableWidth, tableHeight,
                        westTableTipperCoordinates, goalCoordinates));
            }
        }
        return neighborsList;
    }

    /**
     * Checks the surrounding coordinates of the tipper based on an object type.
     * @param objectReference object used to search for objects of the same type
     * @return a list of coordinates associated with objects with same type as objectReference
     */
    public ArrayList<Coordinates> checkSurroundingCoordinates(Object objectReference) {
        ArrayList<Coordinates> returnableCoordinates = new ArrayList<>();
        // Checks north of tipper
        if (tipperRow > 0 && table.get(tipperRow -1, tipperColumn).getClass().equals(objectReference.getClass())) {
            if(objectReference instanceof BlackSquareConfig) {
                if (tipperRow-((TowerConfig) table.get(tipperRow,tipperColumn)).getTowerHeight() > -1) {
                    ArrayList<Coordinates> possibleCoordinates = new ArrayList<>();
                    boolean dropsOnStandableLocation = false;
                    for (int northDrop = 1; northDrop <= ((TowerConfig) table.get(tipperRow,tipperColumn)).getTowerHeight(); northDrop++) {
                        if (table.get(tipperRow -northDrop, tipperColumn) instanceof CrateConfig ||
                                table.get(tipperRow -northDrop, tipperColumn) instanceof TowerConfig) {
                            dropsOnStandableLocation = true;
                        }
                        possibleCoordinates.add(new Coordinates(tipperRow -northDrop, tipperColumn));
                    }
                    if (dropsOnStandableLocation) {
                        possibleCoordinates = new ArrayList<>();
                    }
                    returnableCoordinates.addAll(possibleCoordinates);
                }
            } else {
                returnableCoordinates.add(new Coordinates(tipperRow -1, tipperColumn));
            }
        }
        // Checks east of tipper
        if (tipperColumn < tableWidth-1 && table.get(tipperRow, tipperColumn +1).getClass().equals(objectReference.getClass())) {
            if(objectReference instanceof BlackSquareConfig) {
                if (tipperColumn+((TowerConfig) table.get(tipperRow,tipperColumn)).getTowerHeight() < tableWidth) {
                    ArrayList<Coordinates> possibleCoordinates = new ArrayList<>();
                    boolean dropsOnStandableLocation = false;
                    for (int eastDrop = 1; eastDrop <= ((TowerConfig) table.get(tipperRow,tipperColumn)).getTowerHeight(); eastDrop++) {
                        if (table.get(tipperRow, tipperColumn +eastDrop) instanceof CrateConfig ||
                                table.get(tipperRow, tipperColumn +eastDrop) instanceof TowerConfig) {
                            dropsOnStandableLocation = true;
                        }
                        possibleCoordinates.add(new Coordinates(tipperRow, tipperColumn +eastDrop));
                    }
                    if (dropsOnStandableLocation) {
                        possibleCoordinates = new ArrayList<>();
                    }
                    returnableCoordinates.addAll(possibleCoordinates);
                }
            } else {
                returnableCoordinates.add(new Coordinates(tipperRow, tipperColumn+1));
            }
        }
        // Checks south of tipper
        if (tipperRow < tableHeight-1 && table.get(tipperRow +1, tipperColumn).getClass().equals(objectReference.getClass())) {
            if(objectReference instanceof BlackSquareConfig) {
                if (tipperRow+((TowerConfig) table.get(tipperRow,tipperColumn)).getTowerHeight() < tableHeight) {
                    ArrayList<Coordinates> possibleCoordinates = new ArrayList<>();
                    boolean dropsOnStandableLocation = false;
                    for (int southDrop = 1; southDrop <= ((TowerConfig) table.get(tipperRow,tipperColumn)).getTowerHeight(); southDrop++) {
                        if (table.get(tipperRow +southDrop, tipperColumn) instanceof CrateConfig ||
                                table.get(tipperRow +southDrop, tipperColumn) instanceof TowerConfig) {
                            dropsOnStandableLocation = true;
                        }
                        possibleCoordinates.add(new Coordinates(tipperRow +southDrop, tipperColumn));
                    }
                    if (dropsOnStandableLocation) {
                        possibleCoordinates = new ArrayList<>();
                    }
                    returnableCoordinates.addAll(possibleCoordinates);
                }
            } else {
                returnableCoordinates.add(new Coordinates(tipperRow +1, tipperColumn));
            }
        }
        // Checks west of tipper
        if (tipperColumn > 0 && table.get(tipperRow, tipperColumn -1).getClass().equals(objectReference.getClass())) {
            if(objectReference instanceof BlackSquareConfig) {
                if (tipperColumn-((TowerConfig) table.get(tipperRow,tipperColumn)).getTowerHeight() > -1) {
                    ArrayList<Coordinates> possibleCoordinates = new ArrayList<>();
                    boolean dropsOnStandableLocation = false;
                    for (int westDrop = 1; westDrop <= ((TowerConfig) table.get(tipperRow,tipperColumn)).getTowerHeight(); westDrop++) {
                        if (table.get(tipperRow, tipperColumn -westDrop) instanceof CrateConfig ||
                                table.get(tipperRow, tipperColumn -westDrop) instanceof TowerConfig) {
                            dropsOnStandableLocation = true;
                        }
                        possibleCoordinates.add(new Coordinates(tipperRow, tipperColumn -westDrop));
                    }
                    if (dropsOnStandableLocation) {
                        possibleCoordinates = new ArrayList<>();
                    }
                    returnableCoordinates.addAll(possibleCoordinates);
                }
            } else {
                returnableCoordinates.add(new Coordinates(tipperRow, tipperColumn -1));
            }
        }
        return returnableCoordinates;
    }

    /**
     * Gets the table of the Tip Over configuration
     * @return the table of the Tip Over configuration
     */
    public Grid getTable() {return table;}

    /**
     * Gets the width of the table
     * @return the width of the table
     */
    public int getTableWidth() {return tableWidth;}

    /**
     * Gets the height of the table
     * @return the height of the table
     */
    public int getTableHeight() {return tableHeight;}

    /**
     * Gets the coordinates of the tipper
     * @return the coordinates of the tipper
     */
    public Coordinates getTipperCoordinates() {return tipperCoordinates;}

    /**
     * Gets the coordinates of the goal
     * @return the coordinates of the goal
     */
    public Coordinates getGoalCoordinates() {return goalCoordinates;}

    /**
     * Compares the Tip Over game configuration with another object to see if they're equal
     * @param o the other object to be compared to
     * @return the state of whether the Tip Over game configuration equals the other object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipOverConfig that = (TipOverConfig) o;
        boolean tableEqualTester = true;
        for(int y = 0; y < tableHeight;y++) {
            for (int x = 0; x < tableWidth;x++) {
                if(!that.table.get(y,x).getClass().equals(table.get(y,x).getClass())) {
                    tableEqualTester = false;
                }
            }
        }
        return tableWidth == that.tableWidth && tableHeight == that.tableHeight
                && tipperCoordinates.equals(that.tipperCoordinates) &&
                goalCoordinates.equals(that.goalCoordinates) && tableEqualTester;
    }

    /**
     * Hashes the Tip Over game configuration
     * @return the hash of the Tip Over game configuration
     */
    @Override
    public int hashCode() {
        return Objects.hash(tableWidth, tableHeight, tipperCoordinates, goalCoordinates);
    }

    /**
     * Returns the Tip Over game configuration in string format
     * @return the Tip Over game configuration in string format
     */
    public String toString() {
        String tableString = "\n      ";
        for (int x = 0;x < tableWidth;x++) {
            tableString += x + "  ";
        }
        tableString += "\n    ";
        for (int x = 0;x < tableWidth*3;x++) {
            tableString += "_";
        }
        for (int y = 0;y < tableHeight;y++) {
            tableString += "\n " + y + " |";
            for (int x = 0;x < tableWidth;x++) {
                tableString += " " + table.get(y,x);
            }
        }
        tableString += "\n";
        return tableString;
    }
}

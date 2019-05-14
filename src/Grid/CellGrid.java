package Grid;

import CellPackage.Cell;
import XMLPackage.SimulationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The CellGrid class holds references on all the CellSpaces that live within it. It is responsible for creating
 * those CellSpaces as well as filling them with cells. A convenient approach to modularity of the grid.
 * 
 * @author Feroze Mohideen
 */
public class CellGrid {
    private int myRows;
    private int myCols;

    private double myCellWidth;
    private double myCellHeight;

    private CellSpace[][] myGrid;
//    private CellSpace[][] myGrid;

    private SimulationData mySimulationData;
    private static final int EMPTY = 0;

    private ArrayList<String> myStates;

     /**
     * Creates a 2D array of CellSpaces given information on the simulation size, etc. given by SimulationData
     * @param data SimulationData object holding parsed information
     */
    public CellGrid(SimulationData data) {
        mySimulationData = data;

        myRows = (int) data.getRows();
        myCols = (int) data.getCols();

        myGrid = new CellSpace[myRows][myCols];

        myCellWidth = data.getCellWidth();
        myCellHeight = data.getCellHeight();

        myStates = data.getStateNames();


    }
    /**
     * Allows the Simulation to access the global Grid
     * @return 2D array of CellSpaces
     */
    public CellSpace[][] getGrid() {
        return myGrid;
    }
    /**
     * Creates the CellSpace, adds the cell to it, and moves it according to its position in the array and its shape
     * @param cells 2D array of finished cells
     */
    public void fillCells(Cell[][] cells) {
        for (int i = 0; i < myRows; i++) {
            for (int j = 0; j < myCols; j++) {
                var c = new CellSpace(this);
                c.populateCell(cells[i][j]);
                Position pos = getOffset(i, j);
                c.moveSpace(pos.getX(), pos.getY());
                myGrid[i][j] = c;
            }
        }
    }

    private Position getOffset(int i, int j) {
        if (mySimulationData.getGridShape().equals("squareGrid")) {
            return new Position(j * mySimulationData.getCellWidth(), i * mySimulationData.getCellHeight());
        }
        else if (mySimulationData.getGridShape().equals("triangleGrid")) {
            return new Position(j * (mySimulationData.getGameWidth() / mySimulationData.getCols()),
                    i * mySimulationData.getCellHeight());
        }
        else if (mySimulationData.getGridShape().equals("hexagonGrid")) {
            return new Position(j * mySimulationData.getCellWidth() * 0.75,
                    i * mySimulationData.getCellHeight() + ((j % 2) * mySimulationData.getCellHeight() / 2.0) );
        }
        else return null;
    }
    /**
     * Scans the grid and returns its empty cells
     * @return List of all cells whose state is empty
     */
    public List<? extends Cell> getEmptyCells() {
//    public List<? extends Cell> getEmptyCells() {
        List<Cell> empty = new ArrayList();
        for (CellSpace[] c1: myGrid) {
            for (CellSpace c2: c1) {
                if (c2.getCell().getState() == EMPTY) {
                    empty.add(c2.getCell());
                    //System.out.println(c2.getCell().getClass());
                }
            }
        }
        return empty;
    }
    /**
     * Updates all cells in the grid
     */
    public void update() {
        // update all cells
        for (CellSpace[] cs1 : myGrid) {
            for (CellSpace cs2 : cs1) {
                cs2.updateCell();
            }
        }

        // convert cell state
        for (CellSpace[] cs1 : myGrid) {
            for (CellSpace cs2 : cs1) {
                cs2.updateState();
            }
        }

        // change display
        for (CellSpace[] cs1 : myGrid) {
            for (CellSpace cs2 : cs1) {
                cs2.updateDisplay();
            }
        }
    }

    /**
     * Gets the coordinates of a cell within the grid
     * @param cell
     * @return Position object representing cell coordinates, returns {-1,-1} if not found
     */
    public Position getCoordinates(Cell cell) {
 //   public  Position getCoordinates(Cell cell) {
        for (int i = 0; i < myGrid.length; i++) {
            for (int j  = 0; j < myGrid[i].length; j++) {
                if (myGrid[i][j].getCell() == cell) {
                    return new Position(i, j);
                }
            }
        }
        // will do this if it can't find the cell, maybe change it to an exception?
        return new Position(-1, -1);
    }
    /**
     * Diagnostic method providing state details on all the cells within the grid
     * @return Map of all the states and their associated cell counts
     */
    public HashMap<String, Integer> getStateMap() {
        var ret = new HashMap<String, Integer>();
        for (String state : myStates) {
            ret.put(state, getCount(state));
        }
        return ret;
    }

    private int getCount(String state) {
        int s = myStates.indexOf(state);
        int count = 0;
        for (int i = 0; i < myGrid.length; i++) {
            for (int j  = 0; j < myGrid[i].length; j++) {
                if (myGrid[i][j].getCell().getState() == s) {
                    count++;
                }
            }
        }
        return count;
    }
}

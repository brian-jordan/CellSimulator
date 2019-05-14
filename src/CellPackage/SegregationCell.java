package CellPackage;

import XMLPackage.SimulationData;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is used for the Segregation Simulation. It depends on the Cell abstract class and assumes it is
 * implemented correctly.  This class is used when we populate a grid space with new SegregationCell objects,
 * subsequently iterating through these cells and updating their states based on the rules described below.
 *
 * @author Bryant Huang
 */
public class SegregationCell extends Cell {

    private double myProbSatisfied;
    private List<Cell> myFriends = new ArrayList<>();
    private List<Cell> myActiveNeighbors = new ArrayList<>();
    private List<Cell> currEmptyCells= new ArrayList<>();
    private List<? extends Cell> allEmptyCells;
    private double percentSame;

    private static final int EMPTY = 0;

    public SegregationCell(SimulationData simData, Integer initialState) {
        super(simData, initialState);
        myProbSatisfied = Double.parseDouble(simData.getParameter("probSatisfied").get(0));
    }

    /**
     * This method is used to update the states of the cells. It assumes that the moveCell method works properly.
     */
    public void updateCell() {
        myFriends.clear();
        myActiveNeighbors.clear();
        if (myCurrState != EMPTY) {
            for (int i = 0; i < myNeighbors.size(); i++) {
                if (myNeighbors.get(i).myCurrState == this.myCurrState) {
                    myFriends.add(myNeighbors.get(i));
                }
                if (myNeighbors.get(i).myCurrState != EMPTY) {
                    myActiveNeighbors.add(myNeighbors.get(i));
                }
            }
            percentSame = (double) myFriends.size() / (double) myActiveNeighbors.size();
            if (percentSame < myProbSatisfied)
                moveCell();
        }
    }

    /**
     * This method moves the cell to a new location if the percentage of same cells is not satisfied, and
     * is only called when this condition is satisfied.  It assumes the getEmptyCells() method in the Grid
     * classes works properly, and then chooses a random location to set the cell's location to be.
     */
    public void moveCell() {
        allEmptyCells = mySpace.getGrid().getEmptyCells();
        currEmptyCells.clear();
        for (int c = 0; c < allEmptyCells.size(); c++) {
            if (!allEmptyCells.get(c).isChanged)
                currEmptyCells.add(allEmptyCells.get(c));
        }
        Random r = new Random();
        if (currEmptyCells.size() > 0) {
            int num = r.nextInt(currEmptyCells.size());
            currEmptyCells.get(num).setNextState(myCurrState);
            setNextState(EMPTY);
        }
    }

}



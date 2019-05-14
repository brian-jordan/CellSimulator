package CellPackage;

import XMLPackage.SimulationData;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class simulates the Rock Paper Scissors Bacteria Simulation. It depends on methods from the Cell abstract
 * class and assumes that all the method in that class work properly.  One would create the simulation by iterating
 * through a grid and placing a new RPSCell object in each cell.
 *
 * @author Bryant Huang
 */

public class RPSCell extends Cell{


    public static final int EMPTY = 0;
    public static final int RED = 1;
    public static final int BLUE = 2;
    public static final int GREEN = 3;

    private int level;
    private int nextLevel;
    Random random = new Random();
    private ArrayList <RPSCell> potentialCells = new ArrayList<>();


    public RPSCell(SimulationData simData, Integer initialState){
        super(simData, initialState);
        level = 0;
        nextLevel = level;
    }

    /**
     * This method is the implementation of the abstract method in the main class and is used to update the Cell
     * based on the rules of the RPS Simulation. We assume the various method called serve their functionality.
     */
    public void updateCell() {
        potentialCells = getUnchangedNeighbors();
        if (potentialCells.size()!=0) {
            int index = random.nextInt(potentialCells.size());
            RPSCell neighbor = potentialCells.get(index);
            if (myCurrState == EMPTY) {
                if (neighbor.level < 9)
                    setNextState(neighbor.getState(), neighbor.level + 1);
            } else if ((myCurrState == RED && neighbor.getState() == BLUE) ||
                            (myCurrState == BLUE && neighbor.getState() == GREEN) ||
                                    (myCurrState == GREEN && neighbor.getState() == RED)) {
                neighbor.setNextState(myCurrState, 0);
                setNextState(EMPTY, 0);
            }

        }
    }

    /**
     * This method returns the neighbors that have not been changed.  This is used to determine which cells are
     * eligible to have their states changed to simulate movement.
     * @return list of cells that are eligible to have the current cell be moved to, i.e swap states.
     */
    public ArrayList<RPSCell> getUnchangedNeighbors(){
        potentialCells.clear();
        for (int i = 0; i < myNeighbors.size(); i++){
            if (!myNeighbors.get(i).isChanged) {
                potentialCells.add((RPSCell)(myNeighbors.get(i)));
            }
        }
        return potentialCells;
    }

    /**
     * This method sets the next state of the cell, as well as the level, which is used to determined whether
     * a cell can continue reproducing or not.
     * @param state the new state to set the state to
     * @param level the new level to set the level to
     */
    public void setNextState(int state, int level) {
        super.setNextState(state);
        nextLevel =  level;
    }

    /**
     * This method updates the state and level of the Cell following the change.  It assumes the method in the
     * super works properly to update the states, and then updates the level.
     */
    @Override
    public void updateState() {
        super.updateState();
        level = nextLevel;
    }
}

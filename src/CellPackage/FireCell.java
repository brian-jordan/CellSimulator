package CellPackage;

import XMLPackage.SimulationData;

/**
 * This class is utilized for the Fire simulations.  This class assumes that the the cell abstract class
 * works properly. This class is used when we populate a grid space with new FireCells, subsequently iterating
 * through these cells and updating their states based on the rules described below.
 *
 * @author Bryant Huang
 */
public class FireCell extends Cell{

    private double probCatchFire;
    private static final int EMPTY  = 0;
    private static final int FIRE = 1;
    private static final int TREE  = 2;

    public FireCell(SimulationData simData, Integer initialState){
        super(simData, initialState);
        probCatchFire = Double.parseDouble(simData.getParameter("probCatch").get(0));
    }

    /**
     * This method is used to update the cells and their states.  We assume that the getState() and
     * setNextState() methods from the superclass work properly to check the current state of the cell as well
     * as set the next state of the cell. We need this method to properly update the states of the simulation
     * on the screen.
     */
    public void updateCell(){
        if (myCurrState == TREE){
            for (Cell neighbor : myNeighbors){
                if (neighbor.getState()==FIRE) {
                    if (Math.random() < probCatchFire)
                        setNextState(FIRE);
                }
            }
        }
        else if (myCurrState ==FIRE)
            setNextState(EMPTY);
    }
}

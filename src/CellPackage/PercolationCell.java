package CellPackage;

import XMLPackage.SimulationData;

/**
 * This class is used for the Percolation simulation.  It depends on the Cell abstract class and assumes it is
 * implemented correctly.  This class is used when we populate a grid space with new PercolationCells, subsequently
 * iterating through these cells and updating their states based on the rules described below.
 *
 * @author Bryant Huang
 */
public class PercolationCell extends Cell{

    private static final int OPEN  = 0;
    private static final int PERCOLATED = 1;

    public PercolationCell(SimulationData simData, Integer initialState){
        super(simData, initialState);
    }

    /**
     * This method is used to update the states of the method.  We assume that these variables are initialized
     * correctly and that the methods getState() and setNextState() work properly.
     */
    public void updateCell(){
       if (myCurrState == OPEN){
           for (int i = 0; i< myNeighbors.size(); i++){
               if (myNeighbors.get(i).getState() == PERCOLATED) {
                   setNextState(PERCOLATED);
                   break;
               }
           }
       }
    }
}

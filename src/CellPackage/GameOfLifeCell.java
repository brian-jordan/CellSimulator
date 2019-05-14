package CellPackage;
import XMLPackage.SimulationData;

/**
 * This class is utilized for the Game of Life simulations.  This class assumes that the the cell abstract class
 * works properly. This class is used when we populate a grid space with new GameOfLifeCells, subsequently iterating
 * through these cells and updating their states based on the rules described below.
 *
 * @author Bryant Huang
 */
public class GameOfLifeCell extends Cell{

    private int totalAlive = 0;
    private static final int DEAD  = 0;
    private static final int ALIVE = 1;


    public GameOfLifeCell(SimulationData simData, Integer initialState){
        super(simData, initialState);
    }

    /**
     * This method is used to update the cells and their states.  We assume that the getState() and
     * setNextState() methods from the superclass work properly to check the current state of the cell as well
     * as set the next state of the cell. We need this method to properly update the states of the simulation
     * on the screen.
     */
    public void updateCell(){
        totalAlive = 0;
        for (int i = 0; i< myNeighbors.size();i++){
            if (myNeighbors.get(i).getState() == ALIVE) {
                totalAlive++;
            }
        }
        if (this.myCurrState == ALIVE){
           if (totalAlive < 2 || totalAlive > 3)
               this.setNextState(DEAD);
        }
        else {
            if (totalAlive == 3)
                this.setNextState(ALIVE);
        }
    }
}

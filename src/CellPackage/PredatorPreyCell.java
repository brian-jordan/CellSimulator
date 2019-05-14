package CellPackage;

import XMLPackage.SimulationData;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  This class is used to represent the PredatorPrey Simulation.  It depends on the Cell abstract class and assumes it is
 *  implemented correctly.  This class is used when we populate a grid space with new PredatorPreyCells, subsequently
 *  iterating through these cells and updating their states based on the rules described below.
 *
 * @author Bryant Huang
 */
public class PredatorPreyCell extends Cell {

    private int myEnergy;
    private int energy;
    private int nextEnergy;
    private List<PredatorPreyCell> waterCells = new ArrayList<>();
    private List<PredatorPreyCell> preyCells = new ArrayList<>();


    private static final int PREDATOR = 0;
    private static final int PREY = 1;
    private static final int WATER = 2;
    private static final int REPRODUCTION_TIME = 4;
    private static final int ENERGY_FROM_PREY = 2;
    private static final int PREY_START_ENERGY = 0;
    private static final int PREDATOR_START_ENERGY = 3;

    public PredatorPreyCell(SimulationData simData, Integer initialState) {
        super(simData, initialState);
        energy = Integer.parseInt(simData.getParameter("energy").get(0));
        nextEnergy = energy;
    }

    /**
     * The method below is used to update the cell's state.  It assumes the various methods used are valid and
     * that they work.
     */
    public void updateCell() {
        if (!isChanged) {
            if (myCurrState == PREY) {
                if (getUnchangedEmptyNeighbors().size() != 0){
                    movePrey();
                }
            } else if (myCurrState == PREDATOR) {
                if (energy == 0)
                    killPredator();
                else if (getUnchangedPreyNeighbors().size() != 0)
                    eatPrey();
                else if (getUnchangedEmptyNeighbors().size() != 0)
                    movePredator();
                else
                    setNextStateAndEnergy(PREDATOR, this.energy - 1);
            }
        }
    }

    /**
     * This method is in charge of the movement of the Prey cells. It assumes that the waterCells list containing
     * all potential water cells that it is possible to move to, is filled correctly.  The prey either reproduces
     * or simply moves to a next cell.
     */
    public void movePrey() {
        Random r = new Random();
        int num = r.nextInt(waterCells.size());
        myEnergy = this.energy;
        waterCells.get(num).setNextStateAndEnergy(PREY, myEnergy + 1);
        if (this.energy != REPRODUCTION_TIME) {
            this.setNextStateAndEnergy(WATER, 0);
        } else {
            this.setNextStateAndEnergy(PREY, PREY_START_ENERGY);
        }
    }

    /**
     * This method is used to move a Predator Cell. It is only used when the Predator has no viable Prey cells
     * to go to, and depends on the waterCells being filled with the correct potential water cells to move to.
     */
    public void movePredator() {
        Random r = new Random();
        myEnergy = this.energy;
        int num = r.nextInt(waterCells.size());
        waterCells.get(num).setNextStateAndEnergy(PREDATOR, myEnergy - 1);
        setNextStateAndEnergy(WATER, 0);
    }

    /**
     * This method is used to have a Predator cell consume a prey cell.  It assumes that the preyCells list
     * is filled correctly with valid cells.
     */
    public void eatPrey() {
        Random r = new Random();
        int num = r.nextInt(preyCells.size());
        myEnergy = this.energy;
        preyCells.get(num).setNextStateAndEnergy(PREDATOR, myEnergy + ENERGY_FROM_PREY);
        if (this.energy != REPRODUCTION_TIME) {
            setNextStateAndEnergy(WATER, 0);
        } else {
            setNextStateAndEnergy(PREDATOR, PREDATOR_START_ENERGY);
        }
    }

    /**
     * This method is used to kill a Predator.  It is used in the case when the Predator cell's energy reaches
     * 0, and the cell state is simply set to WATER after execution.
     */
    public void killPredator() {
        setNextStateAndEnergy(WATER, 0);
    }

    /**
     * This method returns a list of unchanged empty neighbors, which in this simulations means the water cells
     * that have not have their state changed yet.
     * @return list of cells that are empty and eligible to have their state changed to Prey or Predator.
     */
    public List<PredatorPreyCell> getUnchangedEmptyNeighbors() {
        waterCells.clear();
        for (int i = 0; i < myNeighbors.size(); i++) {
            if ((myNeighbors.get(i)).getState() == WATER && !(myNeighbors.get(i)).isChanged) {
                waterCells.add((PredatorPreyCell) myNeighbors.get(i));
            }
        }
        return waterCells;
    }

    /**
     * This method returns a list of unchanged prey neighbors, which in this simulation means the prey cells
     * that have not been iterated through or changed.
     * @return list of Prey cells that are eligible to have their states changed.
     */
    public List<PredatorPreyCell> getUnchangedPreyNeighbors() {
        preyCells.clear();
        for (int i = 0; i < myNeighbors.size(); i++) {
            if ((myNeighbors.get(i)).getState() == PREY && !(myNeighbors.get(i)).isChanged) {
                preyCells.add((PredatorPreyCell)myNeighbors.get(i));
            }
        }
        return preyCells;
    }

    /**
     * Method that sets the next state and energy levels of cells.  This is used after the rules of the
     * simulation have been applied, and is different from the one in the abstract class in that it sets a new
     * energy level as well, a variable we have to account for in this simulation.
     * @param state the new state to set the cell to
     * @param energy the new energy level to set the energy to
     */
    public void setNextStateAndEnergy(int state, int energy) {
        myNextState = state;
        nextEnergy = energy;
        isChanged = true;
    }

    /**
     * This method updates the state of the cells after the iteration that makes all the changes. It is also
     * different from the method in the abstract because of the energy update and is necessary for the simulation.
     */
    @Override
    public void updateState() {
        myCurrState = this.myNextState;
        isChanged = false;
        energy = nextEnergy;
    }
}



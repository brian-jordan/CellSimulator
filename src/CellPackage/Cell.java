package CellPackage;

import Exceptions.ConfigurationException;
import Grid.CellSpace;
import Grid.Position;
import XMLPackage.SimulationData;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import java.util.List;

/**
 * This abstract class provides all the basic implementation of a Cell that is necessary given any set of rules.
 * This class is extended by all the other Cell subclasses that run simulations.  Users should be clear that
 * this is an abstract class and meant to be extended as a means of easily running more simulations.
 *
 *  @author Bryant Huang
 *  @author Feroze Mohideen
 */
public abstract class Cell {
    protected int myCurrState;
    protected Color[] myColors;
    protected Shape myShape;
    protected int myNextState;
    protected List<? extends Cell> myNeighbors;
    protected CellSpace mySpace;
    protected boolean attached = false;
    protected boolean isChanged;
    protected Position myPosition;

    public Cell(SimulationData simData, Integer initialState){
        myCurrState = initialState;
        myColors = simData.getColors();
        isChanged = false;
        myNextState = myCurrState;
        if (myColors.length - simData.getNumColors() != 0)
            throw new ConfigurationException("Invalid colors.");
        myPosition = null;

    }

    /**
     * This updateCell method is abstract so each individual Cell subclass can update the cell based on a unique
     * set of rules.
     */
    public abstract void updateCell();

    /**
     * This method is used to set the shape of the cell. The parameter takes in a Shape object, which makes the
     * method very flexible to accommodate all the different shapes that the cells can be.  It depends on the
     * makeShape() method working correctly to pass in the Shape object.
     *
     * @param shape a shape that is created an passed in
     */
    public void setShape(Shape shape) {
        myShape = shape;
    }

    /**
     * This method is used to update the state of any cell.  It sets the current state to the next state.
     */
    public void updateState(){
        myCurrState = this.myNextState;
        isChanged = false;
    }

    /**
     * This method is used to get the state of any cell.
     * @return current state of the cell.
     */
    public int getState(){
        return this.myCurrState;
    }

    /**
     * This method is used to set the next state of a cell.  It assumes that the state passed in is valid, which
     * is an error that should already be checked by the values passed in through the subclasses.
     * @param state the state to set the cell to
     */
    public void setNextState(int state){
        this.myNextState = state;
        isChanged = true;
    }

    /**
     * This method updates the display of the cells, changing the colors depending on what state the cell is in.
     */
    public void updateDisplay(){
        mySpace.getChildren().clear();
        mySpace.getChildren().add(myShape);
        myShape.setFill(myColors[myCurrState]);
    }

    /**
     * This method adds all the cell neighbors to a list of neighbors.
     * @param neighbors list of all the neighbors of a cell
     */
    public void addNeighbors(List<? extends Cell> neighbors) {
        myNeighbors = neighbors;
    }

    /**
     * This method gets the list of neighbors of a cell.
     * @return list of neighbors of cell
     */
    public List<? extends Cell> getNeighbors() {
        return myNeighbors;
    }

    /**
     * THis method creates a CellSpace for the cell, which tells it what space its occupying.
     * @param cellSpace CellSpace that each Cell occupies
     */
    public void createSpace(CellSpace cellSpace) {
        mySpace = cellSpace;
    }

    /**
     * This method is used to attach the shape of any cell to the visualization root.
     */
    public void attach() {
        myShape.setFill(myColors[myCurrState]);
        myShape.setStroke(Color.BLACK);
        mySpace.getChildren().add(myShape);
        attached = true;
    }

    /**
     * This method returns the width of the shape of the cell.  It assumes the shape is valid.
     * @return width of shape of cell
     */
    public double getCellWidth(){
        return myShape.getBoundsInParent().getWidth();
    }

    /**
     * This method returns a Position object of the cell, which is where on the grid the cell is.  This is used
     * by the Ant class to determine location of the Ant objects.
     * @return
     */
    public Position getMyPosition(){
        return myPosition;
    }

    /**
     * This method returns the length of colors and is used to implement the feature of clicking on the cells
     * to change their states.
     * @return the length of the colors array, also is the number of colors.
     */
    public int getLenColors() {
        return myColors.length;
    }
}

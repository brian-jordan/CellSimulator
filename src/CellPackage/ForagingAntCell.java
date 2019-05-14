package CellPackage;

import XMLPackage.SimulationData;
import javafx.scene.shape.Shape;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class ForagingAntCell extends Cell{

    /**
     * This method is used to create Cells for the ForagingAnt Simulation.  It has specific methods used for the
     * rules of this simulation and properties particular to this simulation.  This class depends on the Ant Class
     * and Cell abstract class, and will fail or fail to run properly without these classes.  To utilize this class,
     * one would simply call "ForagingAnt example = new ForagingAnt(x,y)", and a grid shape specific to this simulation
     * would be created.
     *
     * @author Bryant Huang
     * @author Feroze Mohideen
     *
     */

    public static final int EMPTY = 0;
    public static final int FOOD = 1;
    public static final int NEST = 2;
    public static final double FAINT = 0.99;
    public static final int MAX_ANT = 200;
    public static final int START_ANT = 10;
    public static final double MAX_PHEROMONE_LEVEL = 100;
    public static final int INITIAL_FOOD_LEVEL = 40;
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;


    private boolean isFood;
    private boolean isNest;
    private int antCount;
    private int foodLevel;
    private double foodPheromoneLevel;
    private double homePheromoneLevel;
    private double nextFoodPheromoneLevel;
    private double nextHomePheromoneLevel;
    private Shape myAntShape;
    private ArrayList<Ant> ants;
    private ArrayList<Ant>  nextAnts;
    private boolean initialized = false;

    private ArrayList<ForagingAntCell> myForwardNeighbors = new ArrayList<>();
    private ArrayList<ForagingAntCell> myBackNeighbors = new ArrayList<>();

    public ForagingAntCell(SimulationData simData, Integer initialState){
        super(simData, initialState);
        isFood = false;
        if (myCurrState == NEST)
            isNest = true;
        else
            isNest = false;
        if (myCurrState == FOOD){
            foodLevel = INITIAL_FOOD_LEVEL;
        }
        else
            foodLevel = 0;
        foodPheromoneLevel = 0;
        homePheromoneLevel = 0;
        nextFoodPheromoneLevel = foodPheromoneLevel;
        nextHomePheromoneLevel = homePheromoneLevel;
        ants = new ArrayList<>();
        nextAnts = new ArrayList<>(ants);
    }

    /**
     * This method implements the abstract updateCell method to update the Ant position based on a method implemented
     * in the ant class. We assume that the initializeAnts(), moveAnt(), and fadePheromones() methods work properly
     * to set initial ant positions of the nest, move the ants based on simulation rules, and decrement the pheromone
     * amounts in each cell with every iteration, respectively.
     *
     */
    public void updateCell(){
        if (!initialized && isNest) {
            initialized = true;
            initializeAnts();
        }
        for (Ant a : ants){
            a.moveAnt();
        }
        fadePheromones();
    }

    /**
     * This method initializes the Ants objects on a particular cell.  It assumes that the cell given has
     * not been initialized yet and is a valid nest cell when the ants originate.  If the cell does not
     * match these conditions the program does not fail but runs incorrectly.  We assume the createAntShape
     * method works properly to create a Shape for the Ant object.
     */
    public void initializeAnts(){
        for (int i  = 0; i < START_ANT; i++){
            Ant ant = new Ant(this);
            ant.createAntShape(this.getCellWidth()/5);
            ants.add(ant);
        }
    }

    /**
     * This method gets the "forward neighbors" of the cell.  Based on simulation rules, the ants are supposed
     * to first check their forward 3 neighbors first, meaning that if an ant is facing N, it checks the N, NE, and
     * NW neighbors first to see if there are any valid cells to move to.  We assume that the getCoordinates method
     * works correctly to get us X and Y values.  Based on the cell direction, we then compare X and Y coordinates to
     * determine what would be considered a "forward neighbor"
     *
     * @param direction the initial direction of the ant, i.e. which way the ant is facing.
     * @param cell The cell that we are finding forward neighbors for
     * @return An arraylist of forward neighbors, should be max length of 3.
     */
    public ArrayList<ForagingAntCell> getForwardNeighbors(int direction, ForagingAntCell cell){
        if (direction == RIGHT) {
            for (int i = 0; i < cell.myNeighbors.size(); i++) {
                if (mySpace.getGrid().getCoordinates(cell.myNeighbors.get(i)).getX() > mySpace.getGrid().getCoordinates(cell).getX()){
                    myForwardNeighbors.add((ForagingAntCell)cell.myNeighbors.get(i));
                }
            }
        }
        if (direction == LEFT) {
            for (int i = 0; i < cell.myNeighbors.size(); i++) {
                if (mySpace.getGrid().getCoordinates(cell.myNeighbors.get(i)).getX() < mySpace.getGrid().getCoordinates(cell).getX()){
                    myForwardNeighbors.add((ForagingAntCell)cell.myNeighbors.get(i));
                }
            }
        }
        if (direction == UP) {
            for (int i = 0; i < cell.myNeighbors.size(); i++) {
                if (mySpace.getGrid().getCoordinates(cell.myNeighbors.get(i)).getY() > mySpace.getGrid().getCoordinates(cell).getY()){
                    myForwardNeighbors.add((ForagingAntCell)cell.myNeighbors.get(i));
                }
            }
        }
        if (direction == DOWN) {
            for (int i = 0; i < cell.myNeighbors.size(); i++) {
                if (mySpace.getGrid().getCoordinates(cell.myNeighbors.get(i)).getY() < mySpace.getGrid().getCoordinates(cell).getY()){
                    myForwardNeighbors.add((ForagingAntCell)cell.myNeighbors.get(i));
                }
            }
        }
        return myForwardNeighbors;
    }

    /**
     * This method checks the neighbors behind any given cell to find the "back" neighbors.  For example, if an
     * Ant is facing N, then the back neighbors of the cell the Ant occupies would be the cells in the directions
     * W, E, S, SW, and SE.  This method is contingent on the myForwardNeighbors method working, because it simply
     * reuses the algorithm and subtracts the forward cells from the total cells.  Based on the direction parameter,
     * the getForwardNeighbors algorithm will appropriate calculate the list of back cells.
     *
     * @param cell the cell that the ant occupies
     * @param direction the direction that the ant is facing
     * @return a list of the back neighbors, should be of length 5.
     */
    public ArrayList<ForagingAntCell> getBackNeighbors(ForagingAntCell cell, int direction){
        for (Cell c : myNeighbors){
            myBackNeighbors.add((ForagingAntCell)(c));
        }
        myBackNeighbors.removeAll(cell.getForwardNeighbors(direction,cell));
        return myBackNeighbors;
    }

    /**
     * This method calculates the maximum level of food pheromones that the 8 neighboring cells of any given
     * cell has. It iterates through all the neighbors and returns the max food pheromone level.  This method
     * is necessary for to set the pheromone level of the cell according to the simulation rules and succesfully
     * set up a path to the food source.
     *
     * @return integer that represents the maximum food pheromone level in all 8 neighbor cells.
     */
    public double getMaxFoodPheromones(){
        double max = -1;
        for (int i = 0; i < myNeighbors.size(); i++){
            if (((ForagingAntCell) myNeighbors.get(i)).foodPheromoneLevel > max)
                max = ((ForagingAntCell) myNeighbors.get(i)).foodPheromoneLevel;
        }
        return max;
    }

    /**
     * This method calculates the maximum level of home pheromones that the 8 neighboring cells of any given
     * cell has. It iterates through all the neighbors and returns the max home pheromone level.  This method
     * is necessary for to set the pheromone level of the cell according to the simulation rules and successfully
     * set up a path back to the nest.
     *
     * @return integer that represents the maximum food pheromone level in all 8 neighbor cells.
     */
    public double getMaxHomePheromones(){
        double max = -1;
        for (int i = 0; i < myNeighbors.size(); i++){
            if (((ForagingAntCell) myNeighbors.get(i)).homePheromoneLevel > max)
                max = ((ForagingAntCell) myNeighbors.get(i)).homePheromoneLevel;
        }
        return max;
    }

    /**
     * This method updates the display of the Cell once changes have been made to its properties.   It updates the
     * position of the ants in the cell using a random number in between the x and y bounds of the cell to simulate
     * movement.  It iterates through the list of Ant object that every ForagingAntCell has and sets the Ant positions.
     * This method is contingent on the ants list being correctly updated, or else it will add duplicate objects
     * to the screen and lead to an error.  Each ant in the list must also have a Shape initialized.  This method
     * overrides the method in the Cell abstract because it needs to update the shapes of the ants on the screen.
     */
    @Override
    public void updateDisplay(){
        super.updateDisplay();
        for (Ant a : ants) {
            myAntShape = a.getMyAntShape();
            mySpace.getChildren().add(myAntShape);
            double random = ThreadLocalRandom.current().nextDouble(myShape.getLayoutBounds().getMinX(), myShape.getLayoutBounds().getMaxX());
            myAntShape.setLayoutX(random);
            random = ThreadLocalRandom.current().nextDouble(myShape.getLayoutBounds().getMinY(), myShape.getLayoutBounds().getMaxY());
            myAntShape.setLayoutY(random);
            myAntShape.setFill(myColors[myColors.length - 1]);
        }
    }

    /**
     * This method updates the states of the cells in this simulation.  Because the ForagingAntCells now have
     * properties of pheromone levels and ants, this method overrides the method in the Cell abstract class to
     * account for these changes.  It works properly depending on if the method in the abstract class works
     * properly
     */
    @Override
    public void updateState() {
        super.updateState();
        foodPheromoneLevel = nextFoodPheromoneLevel;
        homePheromoneLevel = nextHomePheromoneLevel;
        ants = new ArrayList<>(nextAnts);
    }

    /**
     * This method is used to evaporate the level of pheromones in each cell as time passes.  It is called
     * in the updateCell() method, so every time that the cell is iterated through, the pheromones decrement
     * to simulate real life ant pheromones fading.
     */
    public void fadePheromones(){
        foodPheromoneLevel *= FAINT;
        homePheromoneLevel *= FAINT;
    }

    /**
     * This method decreases the food level of the Food cell and is called whenever an Ant object is added to
     * the Cell which has the current state of food.
     */
    public void depleteFood(){
        foodLevel--;
    }

    /**
     * This method below increments the food level of a cell and is used when an Ant deposits food onto the Nest
     * cell.
     */
    public void addFood(){
        foodLevel++;
    }

    /**
     * This method is used to set the next food pheromone level of the cell.  A "next" variable is used because
     * the current pheromone level cannot be altered or else it would make the calculations of the correct path
     * incorrect.
     *
     * @param newFoodPheromoneLevel the new level to set the cell's food pheromone level to.
     */
    public void setNextFoodPheromoneLevel(double newFoodPheromoneLevel){
        nextFoodPheromoneLevel = newFoodPheromoneLevel;
    }

    /**
     * This method is used to set the next home pheromone level of the cell.  A "next" variable is used because
     * the current pheromone level cannot be altered or else it would make the calculations of the correct path
     * incorrect.
     *
     * @param newHomePheromoneLevel the new level to set the cell's home pheromone level to.
     */
    public void setNextHomePheromoneLevel(double newHomePheromoneLevel){
        nextHomePheromoneLevel = newHomePheromoneLevel;
    }

    /**
     * This method is used to retrieve the home pheromone level of any given ForangingAntCell.  We assume that
     * there is a valid level to return.  This is necessary in calculated the correct cell an Ant moves to.
     *
     * @return the home pheromone level of any Foraging Ant Cell
     */
    public double getHomePheromoneLevel(){
        return homePheromoneLevel;
    }

    /**
     * This method is used to retrieve the food pheromone level of any given ForangingAntCell.  We assume that
     * there is a valid level to return. This is necessary in calculated the correct cell an Ant moves to.
     *
     * @return the food pheromone level of any Foraging Ant Cell
     */
    public double getFoodPheromoneLevel(){
        return foodPheromoneLevel;
    }

    /**
     * This method returns the count of ants in a given cell.  It's used to check if the number of ants has
     * exceed the number of total ants allowed per cell.
     *
     * @return the count of ants in the cell
     */
    public double getAntCount(){
        return antCount;
    }

    /**
     * This method returns the list of next ants, or total ants supposed to remain in the cell following
     * an update iteration.
     *
     * @return a list of ants
     */
    public ArrayList<Ant> getNextAntList(){
        return nextAnts;
    }

    /**
     * This method removes a certain Ant from the next ant list.  This is used when Ants have to move around.
     *
     * @param ant the Ant object to be removed
     */
    public void removeFromNextAntList(Ant ant){
        nextAnts.remove(ant);
    }

    /**
     * This method adds a certain Ant to the next ant list.  This is used when Ants have to move around.
     *
     * @param ant the Ant object to be added to the next list.
     */
    public void addToNextAntList(Ant ant){
        nextAnts.add(ant);
    }

    /**
     * This method returns the current state of the cell. This is used to check if a cell is a food or nest cell.
     * @return current state of the cell
     */
    public int getCurrState(){
        return myCurrState;
    }

    /**
     * Gets the width of the cell object on the screen.  This is used to create a proportionally sized Ant object
     * given a set ratio.
     *
     * @return width of the cell shape.
     */
    public double getCellWidth(){
        return myShape.getBoundsInParent().getWidth();
    }

}

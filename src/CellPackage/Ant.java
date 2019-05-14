package CellPackage;

import Grid.Position;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import java.util.ArrayList;

import static CellPackage.ForagingAntCell.*;

/**
 * This class is for representing ants in the ForagingAnts simulation.  It creates an Ant object that has properties
 * such as food, position, shape, and direction.
 * @author Bryant Huang
 * @author Feroze Mohideen
 */
public class Ant {

    private ForagingAntCell myCell;
    private Position position;
    private int direction;
    private int bestChoice;
    private boolean hasFood;
    private Position nextPosition;
    private ArrayList<ForagingAntCell> myForwardNeighbors;
    private ArrayList<ForagingAntCell> myBackNeighbors;
    private Shape myAntShape;

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private static final int ANT_RATIO = 10;


    public Ant(ForagingAntCell cell){
        myCell = cell;
        position = cell.getMyPosition();
        nextPosition = position;
        direction = 0;
        hasFood = false;
        myAntShape = createAntShape(myCell.getCellWidth()/ANT_RATIO);
        myCell.getNextAntList().add(this);
        myForwardNeighbors = null;
        myBackNeighbors = null;

    }

    /**
     * This method is used to move the ant to the new space based on the rules of the game, and this choice
     * is determined by the helper methods written below.  This would be called on an Ant object, and assumes
     * that the Ant object is valid.  We iterate through a list of ants in this application, and this method
     * is called on each ant in a given cell.
     */
    public void moveAnt(){
        this.myForwardNeighbors = myCell.getForwardNeighbors(direction,myCell);
        bestChoice = checkBestCell(myForwardNeighbors);
        if (this.myForwardNeighbors.size()!=0 && bestChoice != -1){
            myCell.removeFromNextAntList(this);
            this.myForwardNeighbors.get(bestChoice).addToNextAntList(this);
            setDirection(this.myForwardNeighbors.get(bestChoice));
            checkFoodOrNest(this.myForwardNeighbors);
            leavePheromones();
        } else {
            this.myBackNeighbors = myCell.getBackNeighbors(myCell, direction);
            bestChoice = checkBestCell(myBackNeighbors);
            if (this.myBackNeighbors.size()!=0 && bestChoice!= -1) {
                myCell.removeFromNextAntList(this);
                this.myBackNeighbors.get(bestChoice).removeFromNextAntList(this);
                setDirection(this.myBackNeighbors.get(bestChoice));
                checkFoodOrNest(this.myBackNeighbors);
                leavePheromones();
            }
        }
    }

    /**
     * This method sets the new direction of the Ant object based on the cell it moves to.  It uses the position
     * variable of the object and compares the X and Y coordinates to determine if it was a movement up, down, left
     * or to the right.  This direction is then stored in the Ant object.  This method assumes that there is a valid
     * cell parameter passed in and that each ant has a position, nextPosition, and direction that is valid.
     * @param newCell this is the new cell that the program has determined the Ant will move to.
     */
    public void setDirection(ForagingAntCell newCell){
        this.nextPosition = newCell.getMyPosition();
        if (this.position == null){
            direction = RIGHT;
        }
        else {
            if (this.nextPosition.getX() - this.position.getX() > 0) {
                this.direction = RIGHT;
            } else if (nextPosition.getX() - this.position.getX() < 0) {
                this.direction = LEFT;
            } else if (nextPosition.getY() - this.position.getY() > 0) {
                this.direction = UP;
            } else if (nextPosition.getY() - this.position.getY() < 0) {
                this.direction = DOWN;
            }
        }
    }

    /**
     * Checks the ArrayList of neighbors given to either find the best way home or to the food.  If the ant has food, then it checks
     * for the max home pheromone way home.  If it doesn't have food, it looks for the max food pheromone way home.  This is
     * done with the checkHome boolean.  We need this method to determine the appropriate path for the ants.  If
     * there is no valid entry, then the method returns -1, which executes a different method.
     *
     * @param neighbors the list of neighboring cells of the current cells
     * @return bestChoice, which is the index of the max home/food pheromone level out of the neighbor cells
     */
    public int checkBestCell(ArrayList<ForagingAntCell> neighbors){
        double currMaxPheromoneLevel = -1;
        double newPheromoneLevel;
        bestChoice = -1;
        boolean checkHome = hasFood;
            for (int i = 0; i < neighbors.size(); i ++) {
                bestChoice = -1;
                if (checkHome)
                    newPheromoneLevel = neighbors.get(i).getHomePheromoneLevel();
                else
                    newPheromoneLevel = neighbors.get(i).getFoodPheromoneLevel();
                if (newPheromoneLevel > currMaxPheromoneLevel && myCell.getAntCount() <= MAX_ANT) {
                    bestChoice = i;
                }
            }
            return bestChoice;
    }

    /**
     * This method checks if a given cell is a food or nest cell.  If it is a food cell, then the Ant has the
     * boolean hasFood changed to true, and likewise for if the cell is a nest cell.  This method is necessary
     * to reverse the ants path once it reaches one of the destinations.  We assume myCell is a valid cell
     * that was initialized when creating the Ant object.
     *
     * @param neighbors the list of neighbors passed in, with the index best choice being the cell that is evaluated.
     */

    public void checkFoodOrNest(ArrayList<ForagingAntCell> neighbors){
        if (neighbors.get(bestChoice).getCurrState() == FOOD){
            hasFood = true;
            myCell.setNextFoodPheromoneLevel(MAX_PHEROMONE_LEVEL);
            myCell.depleteFood();
        }
        if (neighbors.get(bestChoice).getCurrState() == NEST){
            myCell.setNextHomePheromoneLevel(MAX_PHEROMONE_LEVEL);
            hasFood = false;
            myCell.addFood();
        }
    }

    /**
     * This method leaves pheromones along the path that an ant takes in order to leave a trace for other
     * ants to follow when calculating their next movements.  If the ant has food, then it leaves a trace
     * of food pheromones, but if it doesn't, it leaves a trace of home pheromones, which leads to home. The
     * number of pheromones left is determined by the max pheromone level of all the cells neighbors.  We assume
     * that the getFoodPheromoneLevel and getMaxFoodPheromones methods work properly so we can compare pheromones
     * of the current cell with that of the max neighbor.
     */

    public void leavePheromones(){
        if (hasFood && myCell.getFoodPheromoneLevel() < myCell.getMaxFoodPheromones()) {
            myCell.setNextFoodPheromoneLevel(myCell.getMaxFoodPheromones());
        }
        else if (myCell.getHomePheromoneLevel() < myCell.getMaxHomePheromones()){
            myCell.setNextHomePheromoneLevel(myCell.getMaxHomePheromones());
        }

    }

    /**
     * This method creates the shape of the ant so it can be represented on the Grid.  It creates a circle
     * shape that shows up as a dot on the screen.  We assume a valid parameter will be entered when this
     * method is called.
     *
     * @param ant_width The width of the Ant object visualized on the screen
     * @return a new circle that we can add to the visualization root
     */
    public Shape createAntShape(double ant_width){
        return new Circle(ant_width);
    }

    /**
     * This method returns the Shape object created of the ant.  This is used to access the Ant shape in
     * other classes so that it can be added to the visualization root. The variable is private so this
     * method is necessary to return the correct shape.  It assumes we have a valid shape in the cell.
     *
     * @return The Shape object of any given Ant object.
     */
    public Shape getMyAntShape(){
        return myAntShape;
    }

    /**
     * This method is necessary when calling "remove" on any list of Ants.  The equals method utilized in the
     * Java implementation of "remove" does not properly compare Ant objects.  Using our own equals method we
     * determine ants as equal if their direction and position are equal.  Given that positions are randomly
     * generated, we believe this removes the possibility of mistakenly removing the incorrect ant.  We assume
     * valid values of position and direction of any given Ant object.  With this override of equals, we can
     * correctly remove ants from an arraylist.
     *
     * @param ant The object that we are comparing the Ant object to
     * @return true if the Ant objects are equal, false if they are not
     */
    @Override
    public boolean equals (Object ant){
        return (this.position == ((Ant)ant).position && this.direction ==((Ant)ant).direction);
    }
}

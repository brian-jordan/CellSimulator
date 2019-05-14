package Grid;
/**
 * This is a packaging class to transfer information about a Cell's position within a 2D array. Essentially a tuple
 *
 * @author Feroze Mohideen
 */
public class Position {
    double myX;
    double myY;
    /**
     * Tuple constructor
     * @param x x-value within grid
     * @param y y-value within grid
     */
    public Position(double x, double y) {
        myX = x;
        myY = y;
    }
    /**
     * Returns positional data
     * @return x-coordinate
     */
    public double getX() {
        return myX;
    }
    /**
     * Returns positional data
     * @return y-coordinate
     */
    public double getY() {
        return myY;
    }
}

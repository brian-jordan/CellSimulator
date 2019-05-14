package Grid;

import CellPackage.Cell;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Container class which extends to fit whatever shape lives within it. Holds one cell and possibly other objects.
 *
 * @author Feroze Mohideen
 */
public class CellSpace extends Pane {

    private Cell myCell;
    private CellGrid myGrid;
    private Position position;

    public CellSpace(CellGrid grid) {
        myGrid = grid;
        setOnMouseClicked(e -> redraw());

    }

    public void moveSpace(double x, double y) {
        this.setTranslateX(x);
        this.setTranslateY(y);

    }

    public void redraw() {
        myCell.setNextState((myCell.getState() + 1) % myCell.getLenColors());
        myCell.updateState();
        myCell.updateDisplay();
    }


    public void populateCell(Cell cell){
        myCell = cell;
        cell.createSpace(this);
    }
    /**
     * Provides interface for the grid and other superclasses to access what lives within the CellSpace
     * @return Cell object that inhabits the space
     */
    public Cell getCell() {
        return myCell;
    }


    public void updateState() {
        myCell.updateState();
    }
    /**
     * Calls update on the internal cell
     */
    public void updateCell() {
        myCell.updateCell();
    }

    public void updateDisplay() {
        myCell.updateDisplay();
    }
    /**
     * Allows cells to access the grid, usually shouldn't be used unless a cell needs global information such as the
     * number of empty states within the grid
     * @return CellGrid object
     */
    public CellGrid getGrid() {
        return myGrid;
    }
}

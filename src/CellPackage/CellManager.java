package CellPackage;

import Exceptions.ConfigurationException;
import XMLPackage.SimulationData;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
/**
 * The CellManager class is the generator class that takes in all the information about a simulation and populates
 * and returns a grid of cells based on it. This class uses Java reflection to generate different cell, cell
 * neighbor, and shape types, avoiding the use of pesky if statements and demonstrating full use of polymorphism.
 *
 *
 *  @author Feroze Mohideen
 */
public class CellManager {
    /**
     * This method uses information from the simulation generated from the parser to create a grid of cells whose
     * initial state depends on whether the user is loading from a saved configuration or a random one.
     * @param data condensed data from the XML parser
     * @param fromSaved boolean describing the choice of XML file
     * @return a 2D cell array with cells filled in dependent on the simulation type
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ConfigurationException
     */
    public Cell[][] createCells(String simulationName, SimulationData data, boolean fromSaved) throws
            ClassNotFoundException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            InvocationTargetException,
            ConfigurationException {

        Cell[][] cells = new Cell[(int) data.getRows()][(int) data.getCols()];
        createCells(simulationName, data, cells, fromSaved);

        for (int i = 0; i < data.getRows(); i++) {
            for (int j = 0; j < data.getCols(); j++) {
                Cell cell = cells[i][j];
                List<Cell> cellList = new ArrayList();

                int[][] pos = getNeighbors(data);

                for (int[] neighbor : pos) {
                    int r = neighbor[0];
                    int c = neighbor[1];

                    if (i + r < data.getRows() && i + r >= 0 && j + c < data.getCols() && j + c >= 0) {
                        cellList.add(cells[i+r][j+c]);
                    }
                }
                cell.addNeighbors(cellList);
            }
        }

        for (int i = 0; i < data.getRows(); i++) {
            for (int j = 0; j < data.getCols(); j++) {
                Cell cell = cells[i][j];
                cell.setShape(makeShape(data, i, j));
            }
        }
        return cells;
    }

    private int[][] getNeighbors(SimulationData data) {
        if (data.getNeighborType().equals("neighbors8")) {
            return new int[][]{
                            {-1,-1}, {-1, 0}, {-1, 1},
                             {0, -1},         {0, 1},
                             {1, -1}, {1, 0}, {1, 1}};
        }
        else if(data.getNeighborType().equals("neighbors4")) {
            return new int[][]{
                                    {-1, 0},
                            {0, -1},         {0, 1},
                                     {1, 0}         };
        }
        else if(data.getNeighborType().equals("neighbors12")) {
            return new int[][]{
                                    {-1, -1}, {-1, 0}, {-1, 1},
                            {0, -2}, {0, -1},       {0, 1}, {0, 2},
                            {1, -2}, {1, -2},       {1, 1}, {1, 2}

            };
        }
        else if(data.getNeighborType().equals("neighbors6")) {
            return new int[][]{
                                {-1, 0},
                    {0, -1},            {0, 1},
                    {1, -1},            {1,1},
                                {1, 0}

            };
        }
        return null;
    }

    private Shape makeShape(SimulationData data, int i, int j) {
        // TODO change these strings to constants
        if (data.getGridShape().equals("squareGrid")) {
            return rect(data);
        }
        else if (data.getGridShape().equals("triangleGrid")) {
            return triangle(data, i, j);
        }
        else if (data.getGridShape().equals("hexagonGrid")) {
            return hexagon(data);
        }
        else {
            return null;
        }
    }

    private Shape hexagon(SimulationData data) {
        double cw = data.getCellWidth();
        double ch = data.getCellHeight();

        // TODO fix this magic number
        double x2 = cw * 0.75;
        double x1 = cw - x2;
        return new Polygon(x1, 0, x2, 0, cw, ch/2, x2, ch, x1, ch, 0, ch/2);
    }

    private Shape triangle(SimulationData data, int i, int j) {
        if (i%2 == 0 ^ j%2 == 0) {
            return new Polygon(data.getCellWidth()/2,0,  data.getCellWidth(),data.getCellHeight(),  0,data.getCellHeight());
        }
        else {
            return new Polygon(0,0,  data.getCellWidth(),0,  data.getCellWidth()/2,data.getCellHeight());

        }
    }

    private Shape rect(SimulationData data) {
        return new Rectangle(0, 0, data.getCellWidth(), data.getCellHeight());
    }

    private void createCells(String simulationName, SimulationData data, Cell[][] cells, boolean fromSaved)
            throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException, ConfigurationException {

        if (fromSaved) {
            ArrayList<String> states = data.getStates();
            if(states.size() != data.getCols()*data.getRows()) {
                throw new ConfigurationException("XML dimension mismatch!");
            }
            else {
                for (int i = 0; i < data.getRows(); i++) {
                    for (int j = 0; j < data.getCols(); j++) {
                        cells[i][j] = chooseCell(simulationName, data,
                                Integer.parseInt(states.get((int) (i*data.getCols() + j))));
                    }
                }
            }

        }
        else {
            for (int i = 0; i < data.getRows(); i++) {
                for (int j = 0; j < data.getCols(); j++) {
                    cells[i][j] = chooseCell(simulationName, data, data.getInitialState());
                }
            }
        }

    }

    private Cell chooseCell(String simulationName, SimulationData data, int initialState) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return newInstance("CellPackage." +simulationName+"Cell", data, initialState);
    }

    private static <T> T newInstance(final String className, final Object... args)
            throws ClassNotFoundException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        Class[] types = new Class[args.length];
        for ( int i = 0; i < types.length; i++ ) {
            types[i] = args[i].getClass();
        }
        return (T) Class.forName(className).getConstructor(types).newInstance(args);
    }
}

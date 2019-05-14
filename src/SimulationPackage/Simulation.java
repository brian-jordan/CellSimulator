package SimulationPackage;

import CellPackage.Cell;
import CellPackage.CellManager;
import Exceptions.ConfigurationException;
import Grid.CellGrid;
import Grid.CellSpace;
import XMLPackage.SimulationData;
import XMLPackage.XMLWriter;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Main class that holds everything going on about a Simulation. Created to extend Group so that the front-end can
 * just attach it to its design and call update on it without worrying about what is going on within it - an
 * excellent example of modularity.
 *
 * @author Feroze Mohideen
 */
public class Simulation extends Group {
    private CellGrid myCellGrid;
    private XMLWriter myWriter;
    SimulationData myData;
    String simulationName;
    /**
     * Initializes a simulation given only a filename. The parser takes the name and constructs a SimulationData
     * object which is used by CellFactory to create and fill the Cell Grid. Exceptions are thrown if the filename
     * doesn't match the type of Cell used.
     * @param filename name of the XML file
     * @param fromSaved boolean describing whether the user is loading from a previous state configuration or a new one
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws ConfigurationException
     */
    public Simulation(String filename, boolean fromSaved) throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            IOException, SAXException,
            ParserConfigurationException,
            ConfigurationException {

        myData = new SimulationData(filename);
        simulationName = myData.getName();

        myWriter = new XMLWriter();

        myCellGrid = new CellGrid(myData);

        var cm = new CellManager();
        Cell[][] cells = cm.createCells(simulationName, myData, fromSaved);

        myCellGrid.fillCells(cells);

        // attach cell panes to society
        for (CellSpace[] cs1: myCellGrid.getGrid()) {
            for (CellSpace cs2: cs1) {
                cs2.getCell().attach();
                getChildren().add(cs2);
            }
        }
    }
    /**
     * Used by the Front-end to save to XML
     * @return XMLWriter
     */
    public XMLWriter getWriter() {
        return myWriter;
    }
    /**
     * Returns parsed information on the simulation
     * @return SimulationData object holding information on the sim
     */
    public SimulationData getSimData(){
        return myData;
    }
    /**
     * Gives name of the simulation.
     * @return simulation name
     */
    public String getSimName(){
        return simulationName;
    }
    /**
     * Gives the constructed Cell grid
     * @return a CellGrid object which holds a 2D array of cellspaces
     */
    public CellGrid getMyCellGrid(){
        return myCellGrid;
    }
    /**
     * Method which the front-end calls on each iteration to change all cells
     */
    public void update() {
        myCellGrid.update();
    }
    /**
     * These next 3 methods are used by the StateMap on the front-end; the generate diagnostic information about the
     * simulation initially and as it progresses.
     * @return a hashmap in the form of {'state': count} to display counts of each state on the graph
     */
    public HashMap<String, Integer> getMap() {
        return myCellGrid.getStateMap();
    }

    public ArrayList<String> getStateNames() {
        return myData.getStateNames();
    }

    public Color[] getStateColors() {
        return myData.getColors();
    }
}

package XMLPackage;

import Exceptions.ConfigurationException;
import javafx.scene.paint.Color;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
/**
 * This class grabs data from the Parser and has many public methods to extract data from it instead of accessing the
 * parsed data directly. In this way, it first of all offers a cleaner API for classes from the Configuration and
 * Visualization packages to access, and secondly abstracts the specific data structure that we decided to implement
 * (in this case we use a Map, but the objects that call these methods have no idea)
 *
 * @author Feroze Mohideen
 */
public class SimulationData {
    private Map<String, ArrayList<String>> myGameConfig;
    private Map<String, ArrayList<Double>> myCellConfig;

    private Map<String, ArrayList<String>> myParameters;
    /**
     * A simulationdata object can be called to return information about the current simulation running
     * @param filename The XML file from which we would like to construct our Maps
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public SimulationData(String filename) throws ParserConfigurationException, SAXException, IOException {
        var parser = new XMLParser();
            myGameConfig = parser.parseGame(filename);
            myCellConfig = parser.parseCell(filename);
            myParameters = parser.parseParameters(filename);
    }
    /**
     * Generates a state to assign a cell given the state probabilities
     * @return integer indexing the state of a cell
     */
    public int getInitialState() {
        ArrayList<Double> input = myCellConfig.get("stateProb");
        int count = 0;
        double prob = new Random().nextDouble();
        for (Double d: input) {
            if (prob < d) {
                return count;
            }
            else {
                prob -= d;
            }
            count++;
        }
        return 0;
    }
    /**
     * Constructs an array of JavaFX Color objects to be used by the CellFactory
     * @return an array of Colors
     */
    public Color[] getColors() {
        var colors = new Color[myGameConfig.get("colors").size()];
        for (int i=0; i < colors.length; i++) {
            String color = myGameConfig.get("colors").get(i);
            try {
                colors[i] = Color.web(color);
            }
            catch (Exception e) {
                colors[i] = Color.web("#dce22b");
            }

        }
        return colors;
    }
    /**
     * Returns the shape of the grid
     * @return String written under the GridShape tag
     */
    public String getGridShape() {
        return myGameConfig.get("gridShape").get(0);
    }
    /**
     * Calculates the width of a cell based on the cell shape
     * @return double value of the cell width
     */
    public double getCellWidth() {
        if (getGridShape().equals("squareGrid")) {
            return myCellConfig.get("width").get(0)/myCellConfig.get("columns").get(0);
        }
        else if (getGridShape().equals("triangleGrid")){
            return myCellConfig.get("width").get(0)/myCellConfig.get("columns").get(0) * 2;
        }
        else if (getGridShape().equals("hexagonGrid")){
            //TODO fix this magic number
            return myCellConfig.get("width").get(0)/myCellConfig.get("columns").get(0) * 4 / 3;
        }
        else return 0;
    }
    /**
     * Calculates the height of the cell based on the cell shape
     * @return double value of the cell height
     */
    public double getCellHeight() {
        if (getGridShape().equals("squareGrid")) {
            return myCellConfig.get("height").get(0)/myCellConfig.get("rows").get(0);
        }
        else if (getGridShape().equals("triangleGrid")){
            return myCellConfig.get("height").get(0)/myCellConfig.get("rows").get(0);
        }
        else if (getGridShape().equals("hexagonGrid")){
            return myCellConfig.get("height").get(0)/myCellConfig.get("rows").get(0);
        }
        else return 0;
    }

    /**
     * Returns rows of the grid
     * @return number of rows
     */
    public double getRows() {
        return myCellConfig.get("rows").get(0);
    }
    /**
     * Returns columns of the grid
     * @return number of columns
     */
    public double getCols() {
        return myCellConfig.get("columns").get(0);
    }
    /**
     * Gives the simulation name
     * @return name of specific society
     */
    public String getName() {
        return myGameConfig.get("title").get(0);
    }

    public String getNeighborType() {
        return myGameConfig.get("neighbors").get(0);
    }

    public ArrayList<String> getParameter(String param) {
        return myParameters.get(param);
    }

    public Map getGameConfig(){
        return myGameConfig;
    }
    public Map getCellConfig(){
        return myCellConfig;
    }
    public Map getParameters(){
        return myParameters;
    }

    public ArrayList<String> getStates() {
        return myGameConfig.get("initialState");
    }

    public double getGameWidth() {
        return myCellConfig.get("width").get(0);
    }

    public int getNumColors() {
        return Integer.parseInt(myGameConfig.get("numColors").get(0));
    }

    public ArrayList<String> getStateNames() {
        return myParameters.get("name");
    }
}

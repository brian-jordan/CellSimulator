package XMLPackage;

import Grid.CellSpace;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class is utilized for the XML writer, which writes the current state of any simulation along with its
 * properties to a XML file that when loaded, can reload all the exact same properties and state of the simulation.
 * It depends and assumes that the simulation and its maps of configuration data are all valid and contain the
 * necessary information necessary to pass along to a saved state of the configuration.
 *
 * @author Bryant Huang
 */
public class XMLWriter {

    String pathStart = "data/";

    /**
     * This method saves the XML file, calling multiple helper method to write various different parts
     * @param fileName name of file
     * @param myCellSpace the CellSpace that contains all states of cells
     * @param gameConfig map of game configuration
     * @param cellConfig map of cell configuration
     * @param parameterConfig map of parameter configuration
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public void saveAsXML (String fileName, CellSpace[][] myCellSpace, Map gameConfig, Map cellConfig, Map parameterConfig) throws ParserConfigurationException, TransformerException {
        Map <String, ArrayList<String>> gameInfo = gameConfig;
        Map <String, ArrayList<Double>> cellInfo = cellConfig;
        Map <String, ArrayList<String>> params = parameterConfig;

        DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("config");
        doc.appendChild(root);

        writeGameConfig(root,doc,gameInfo,fileName);
        writeCellConfig(root, doc, cellInfo, myCellSpace, params);
        writeParameters(root,doc,params);

        convertToFile(doc,fileName);
        System.out.println("File Saved");
    }

    /**
     * This method writes the game configuration of the simulation, assuming all helper methods wrote properly
     * in gathering the necessary information.
     * @param root the root of the XML where information is saved to
     * @param doc the Document object that is the structure of the XML
     * @param gameInfo map containing information of the game
     * @param fileName name of file
     */
    public void writeGameConfig(Element root, Document doc, Map <String, ArrayList<String>> gameInfo, String fileName){
        writeTitleAndAuthor(root,doc,fileName,gameInfo);
        writeShapes(root,doc,gameInfo);
        writeColors(root,doc,gameInfo);
        writeNeighbors(root,doc,gameInfo);
    }

    /**
     * This method writes the cell configuration of the simulation, assuming all helper methods wrote properly
     * in gathering the necessary information.
     * @param root the root of the XML file where information is saved to
     * @param doc the Document object that is the structure of the XML
     * @param cellInfo map containing information of the game
     * @param myCellSpace Cell Space that contains all the shapes of cells
     * @param params map containing other relevant information pertaining to cells
     */
    public void writeCellConfig(Element root, Document doc, Map <String, ArrayList<Double>> cellInfo, CellSpace[][] myCellSpace, Map <String, ArrayList<String>> params){
        writeProbabilities(root, cellInfo, doc);
        writeStates(root,cellInfo,doc,params);
        writeInitialStates(root,doc,myCellSpace);
        writeCellParameters(root, doc, cellInfo);
        writeGridParameters(root, doc, cellInfo);
    }

    /**
     * This method writes any miscellaneous parameters that are simulation specific and unique.
     * @param root the root of the XML file where information is saved to
     * @param doc Document object that is structure of the XML
     * @param params map containing parameters of relevant simulation information
     */
    public void writeParameters(Element root, Document doc, Map <String, ArrayList<String>> params){
        Element parameters= doc.createElement("parameters");
        root.appendChild(parameters);
        for (String key : params.keySet()){
            for (int k =0; k < params.get(key).size(); k++) {
                Element param = doc.createElement(key);
                param.appendChild(doc.createTextNode(String.valueOf(params.get(key).get(k))));
                parameters.appendChild(param);
            }

        }
    }

    /**
     * This method converts the written Document into an actual XML file. It depends on the entire creation
     * of the file being successful and information being accurately written.
     * @param doc Document object that is structure of the XML
     * @param fileName name of the file
     * @throws TransformerException
     */
    public void convertToFile(Document doc, String fileName) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 4);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(pathStart + "s_" + fileName + ".xml"));
        transformer.transform(source, result);
    }

    /**
     * This method writes the given state probabilities per simulation, writing probabilities for each state.
     * @param root root of the XML file where information is saved to
     * @param cellInfo map containing information about cell configuration
     * @param doc Document object that is structure of the XML
     */
    public void writeProbabilities(Element root, Map<String, ArrayList<Double>> cellInfo, Document doc){
        Element stateProb = doc.createElement("stateProb");
        root.appendChild(stateProb);
        for (int i = 0; i < cellInfo.get("stateProb").size(); i++) {
            Element prob = doc.createElement("prob");
            prob.appendChild(doc.createTextNode(String.valueOf((cellInfo.get("stateProb")).get(i))));
            stateProb.appendChild(prob);
        }
    }

    /**
     * This method saves all the option of states to the XML.
     * @param root root of the XML file where information is saved to
     * @param cellInfo map containing information about cell configuration
     * @param doc Document object that is structure of the XML
     * @param params map containing information about relevant parameters to the simulation
     */
    public void writeStates(Element root, Map<String, ArrayList<Double>> cellInfo, Document doc,Map<String, ArrayList<String>> params ){
        Element states = doc.createElement("states");
        root.appendChild(states);
        for (int i = 0; i < (cellInfo.get("states")).size(); i++) {
            Element option = doc.createElement("option");
            option.appendChild(doc.createTextNode(String.valueOf(cellInfo.get("states").get(i))));
            option.setAttribute("name", params.get("name").get(i));
            states.appendChild(option);
        }
    }

    /**
     * This method writes all the saved states of the cells into the XML file.  It writes each individual state
     * of each cell.
     * @param root root of XML file where information is saved to
     * @param doc Document object that is structure of the XML
     * @param myCellSpace Cell Space that contains all the shapes of cells
     */
    public void writeInitialStates(Element root, Document doc, CellSpace[][] myCellSpace){
        Element initialState = doc.createElement("initialState");
        root.appendChild(initialState);
        for (int i = 0; i < myCellSpace.length; i++) {
            for (int j = 0; j < myCellSpace[i].length; j++) {
                Element state = doc.createElement("state");
                state.appendChild(doc.createTextNode(String.valueOf(myCellSpace[i][j].getCell().getState())));
                state.setAttribute("i", String.valueOf(i));
                state.setAttribute("j", String.valueOf(j));
                initialState.appendChild(state);
            }
        }
    }

    /**
     * THie method writes the parameters of the cell, such as the width and the height.
     * @param root root of XML file where information is saved to
     * @param doc Document object that is structure of the XML
     * @param cellInfo map containing information about cell configuration
     */
    public void writeCellParameters(Element root, Document doc, Map<String, ArrayList<Double>> cellInfo){
        Element width = doc.createElement("width");
        width.appendChild(doc.createTextNode(String.valueOf((cellInfo.get("width")).get(0))));
        root.appendChild(width);

        Element height = doc.createElement("height");
        height.appendChild(doc.createTextNode(String.valueOf((cellInfo.get("height")).get(0))));
        root.appendChild(height);
    }

    /**
     * This method writes the parameters of the grid, such as its columns and rows
     * @param root root of XML file where information is saved to
     * @param doc Document object that is structure of the XML
     * @param cellInfo map containing information about cell configuration
     */
    public void writeGridParameters(Element root, Document doc, Map <String, ArrayList<Double>> cellInfo){
        Element columns = doc.createElement("columns");
        columns.appendChild(doc.createTextNode(String.valueOf((cellInfo.get("columns")).get(0))));
        root.appendChild(columns);

        Element rows = doc.createElement("rows");
        rows.appendChild(doc.createTextNode(String.valueOf((cellInfo.get("rows")).get(0))));
        root.appendChild(rows);
    }

    /**
     * Writes the title and author of the XML file.
     * @param root root of XML file where information is saved to
     * @param doc Document object that is structure of the XML
     * @param fileName name of the file
     * @param gameInfo map containing information about game configuration
     */
    public void writeTitleAndAuthor(Element root, Document doc, String fileName, Map<String, ArrayList<String>> gameInfo) {
        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode(fileName));
        root.appendChild(title);
        Element author = doc.createElement("author");
        author.appendChild(doc.createTextNode((gameInfo.get("author")).get(0)));
        root.appendChild(author);
    }

    /**
     * This method writes what type of shape the shape in any given simulation is.  This is important because
     * it determines how the simulation runs and visualization of the grid.
     *
     * @param root root of XML file where information is saved to
     * @param doc Document object that is structure of the XML
     * @param gameInfo map containing information about game configuration
     */
    public void writeShapes(Element root, Document doc, Map<String, ArrayList<String>> gameInfo){
        Element cellShape = doc.createElement("cellShape");
        cellShape.appendChild(doc.createTextNode((gameInfo.get("cellShape")).get(0)));
        root.appendChild(cellShape);

        Element gridShape = doc.createElement("gridShape");
        gridShape.appendChild(doc.createTextNode((gameInfo.get("gridShape")).get(0)));
        root.appendChild(gridShape);
    }

    /**
     * This method writes all the possible colors of the simulation, along with a total number of colors
     * that the simulation contains.  Accurate visualization depends on this information getting passed through.
     * @param root
     * @param doc
     * @param gameInfo
     */
    public void writeColors(Element root, Document doc, Map<String, ArrayList<String>> gameInfo){
        Element colors = doc.createElement("colors");
        root.appendChild(colors);
        for (int i = 0; i < gameInfo.get("colors").size(); i++) {
            Element color = doc.createElement("color");
            colors.appendChild(color);
            color.appendChild(doc.createTextNode(String.valueOf(gameInfo.get("colors").get(i))));
        }
        Element numColors = doc.createElement("numColors");
        root.appendChild(numColors);
        numColors.appendChild(doc.createTextNode(String.valueOf(gameInfo.get("numColors").get(0))));
    }

    /**
     * This method writes how many neighbors the simulation considers when running its algorithms.
     * @param root root of XML file where information is saved to
     * @param doc Document object that is structure of the XML
     * @param gameInfo map containing information about game configuration
     */
    public void writeNeighbors(Element root, Document doc, Map<String, ArrayList<String>> gameInfo){
        Element neighbors = doc.createElement("neighbors");
        neighbors.appendChild(doc.createTextNode(String.valueOf((gameInfo.get("neighbors")).get(0))));
        root.appendChild(neighbors);
    }
}
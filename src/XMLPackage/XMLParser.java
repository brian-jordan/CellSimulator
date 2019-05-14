package XMLPackage;

import java.util.*;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;

/**
 * This class is used to parse XML files for any simulation.  It works by calling the various specific parse
 * methods below, which stores the data into Maps.  Just simply call parseGame, parseCell, or parseParameters,
 * and the appropriate data will be stored in the maps described above.
 *
 * @author Bryant Huang
 */

public class XMLParser{

    String pathStart = "data/";

    private NodeList rootList;
    private NodeList subList;
    private Map<String, ArrayList<String>> gameConfigMap = new HashMap<>();
    private Map<String, ArrayList<Double>> cellConfigMap = new HashMap<>();
    private Map<String, ArrayList<String>> parameterMap = new HashMap<>();

    private ArrayList<String> cellProperties = new ArrayList<>(List.of("stateProb", "states", "rows", "columns", "width", "height"));
    private ArrayList<String> gameProperties = new ArrayList<>(List.of("title", "author", "cellShape", "gridShape", "colors", "numColors",
            "neighbors", "initialState"));
    private ArrayList<String> parameterProperties = new ArrayList<>(List.of("parameters", "states"));

    public XMLParser(){

    }

    /**
     * This method parses the game properties, all of which specified by the gameProperties ArrayList above
     * @param fileName name of file being parsed
     * @return Map of the game properties, with Strings for keys
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public Map <String, ArrayList<String>> parseGame(String fileName) throws IOException, SAXException, ParserConfigurationException {
            initializeParser(fileName);
            for (int i = 0; i < rootList.getLength(); i++) {
                Node node = rootList.item(i);
                if (gameProperties.contains(node.getNodeName())) {
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        gameConfigMap.put(node.getNodeName(), new ArrayList<>());
                        subList = node.getChildNodes();
                        if (subList.getLength() > 1) {
                            parseGameChildren(node, subList, gameConfigMap);
                        }
                        else
                            gameConfigMap.get(node.getNodeName()).add(node.getTextContent());
                    }
                }
            }
            return gameConfigMap;
    }

    /**
     * This method parses the game properties, all of which specified by the cellProperties ArrayList above.
     * @param fileName name of the file being parsed.
     * @return Map of cell properties, with Strings for keys.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public Map <String, ArrayList<Double>> parseCell(String fileName) throws IOException, SAXException, ParserConfigurationException {
        initializeParser(fileName);
        for (int i = 0; i < rootList.getLength(); i++) {
            Node node = rootList.item(i);
            if (cellProperties.contains(node.getNodeName())) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    cellConfigMap.put(node.getNodeName(), new ArrayList<>());
                    subList = node.getChildNodes();
                    if (subList.getLength() > 1) {
                        parseCellChildren(node, subList, cellConfigMap);
                    }
                    else {
                        cellConfigMap.get(node.getNodeName()).add(Double.parseDouble(node.getTextContent()));
                    }
                }
            }
        }
        return cellConfigMap;
    }

    /**
     * This method parses the parameter properties, all of which specified by the paramProperties ArrayList above.
     * @param fileName name of file being parsed
     * @return Map of cell properties, with Strings for keys.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public Map<String, ArrayList<String>> parseParameters(String fileName) throws IOException, SAXException, ParserConfigurationException {
        initializeParser(fileName);
        for (int i = 0; i < rootList.getLength(); i++) {
            Node node = rootList.item(i);
            if (parameterProperties.contains(node.getNodeName())) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    subList = node.getChildNodes();
                    for (int k = 0; k < subList.getLength(); k++){
                        Node subNode  = subList.item(k);
                        if (subNode.getNodeType() == Node.ELEMENT_NODE && !((subNode.getNodeName()).equals("option"))){
                            if (!parameterMap.containsKey(subNode.getNodeName())) {
                                parameterMap.put(subNode.getNodeName(), new ArrayList<>());
                            }
                            (parameterMap.get(subNode.getNodeName())).add(subNode.getTextContent());
                        }
                    }

                }
            }
        }
        return parameterMap;

    }

    /**
     * Parses the children of any children nodes of the Game. This is necessary to fully parsed all the information
     * in the XML and prevent missing out on nodes with multiple children nodes.
     *
     * @param node the node who's children nodes are being parsed.
     * @param subList The list of nodes that have children nodes needed to be parsed as well.
     * @param map the map of the game configuration
     */
    public void parseGameChildren(Node node, NodeList subList, Map<String, ArrayList<String>> map){
        for (int k = 0; k < subList.getLength(); k++) {
            Node subNode = subList.item(k);
                if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                    map.get(node.getNodeName()).add(subNode.getTextContent());               }
        }
    }
    /**
     * Parses the children of any children nodes of the cell properites. This is necessary to fully parsed all the information
     * in the XML and prevent missing out on nodes with multiple children nodes.
     *
     * @param node the node who's children nodes are being parsed.
     * @param subList The list of nodes that have children nodes needed to be parsed as well.
     * @param map the map of the cell configuration
     */
    public void parseCellChildren(Node node, NodeList subList, Map<String, ArrayList<Double>> map){
        for (int k = 0; k < subList.getLength(); k++) {
            Node subNode = subList.item(k);
            if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("initialState") && !((map.get("states")).contains(Double.parseDouble(subNode.getTextContent())))) {
                    throw new IndexOutOfBoundsException("Invalid states.");
                }
                map.get(node.getNodeName()).add(Double.parseDouble(subNode.getTextContent()));
            }
        }
    }

    /**
     * This method initializes the parser root and parses the document into a tree type structure with nodes
     * containing all the information, which we then parse through with the methods written.
     * @param fileName name of file to be parsed.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void initializeParser(String fileName) throws ParserConfigurationException, IOException, SAXException {
        File inputFile = new File(pathStart + fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputFile);
        rootList = doc.getDocumentElement().getChildNodes();
    }
}


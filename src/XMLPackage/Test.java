package XMLPackage;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class Test {
    public static void main(String [] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        XMLParser tester = new XMLParser();
//        tester.parseGame("GameOfLife.saved.xml");
//        tester.parseParameters("PredatorPrey.xml");
////
        XMLWriter writer = new XMLWriter();
//        writer.saveAsXML("GameOfLife");

    }
}

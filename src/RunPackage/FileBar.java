package RunPackage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ResourceBundle;

/**
 * Project 2: Cell Society
 * Duke CompSci 308 Spring 2019 - Duvall
 * Date Created: 1/30/2019
 * Date Last Modified: 2/12/2019
 * @author Bryant Huang
 * @author Brian Jordan
 * @author Feroze Mohideen
 */

/**
 * The FileBar class creates an HBox to be added to the User Interface that controls file selection by the user
 * The main assumption for the class is that xml files corresponding to simulations will be located on the machine
 * running to program.
 * This class extends the HBox class to return a node that is easily addable to the User Interface.
 * This class creates an interactive portion of the User Interface involving a file selector button, file display box,
 * and a display simulation button.
 */

/**
 * This is an example of good code, because it exemplifies the design principle of modularity.  Originally, this part of
 * the code was implemented in the UserInterface class.  Toward's the end of the last sprint, the UserInterface class
 * had many lines of code and included basically all of the functionality of the front-end of the program.  Breaking this
 * part off into a separate class encapsulated the front-end elements and the functions regarding the selection of an
 * XML file to run.  This also represents a completely closed off portion of code given that all of the methods and
 * non-final variables being private to the class.
 */

public class FileBar extends HBox {

    public static final String BUTTON_BOX_STYLE = "simButtonBox";
    public static final String FILE_BAR_SP_STYLE = "fileBarStackPane";
    public static final String FILE_ADDER_IMAGE = "fileAdder.gif";
    public static final String RUN_BUTTON_IMAGE = "runButton.gif";
    public static final int FILE_IMAGE_SIZE = 20;
    public static final int FILE_BOX_WIDTH = 300;
    public static final int FILE_BOX_HEIGHT = 30;
    public static final String FILE_NAME_BOX_STYLE = "fileBar";
    public static final String FILE_TEXT_STYLE = "fileBarText";
    public static final String CHOOSE_FILE_STRING = "ChooseFile";
    public static final String SAVED_INDICATOR = "s_";

    private Text myLeftFileText;
    private Text myRightFileText;

    /**
     * Creates instance of a file bar that creates and displays the file selector button, file display box, and
     * display simulation
     * @param run - RunCellSociety instance that runs the program
     * @param ui - UserInterface instance that calls the constructor
     * @param stage - window the program is displayed in
     * @param duel - indicator if window is currently running two simulations
     * @param displayedStrings - resource bundle containing strings displayed in the interface
     */
    
    public FileBar(RunCellSociety run, UserInterface ui, Stage stage, boolean duel, ResourceBundle displayedStrings){
        super();
        this.getStyleClass().add(BUTTON_BOX_STYLE);
        createFileSelectorButton(ui, stage, duel);
        var fileBar = new StackPane();
        fileBar.getStyleClass().add(FILE_BAR_SP_STYLE);
        createFileBar(fileBar, duel, displayedStrings);
        this.getChildren().add(fileBar);
        createSimulateButton(run, ui, duel);
    }

    private void createFileSelectorButton(UserInterface ui, Stage stage, boolean duel){
        var fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        boolean currentDuel = duel;
        Button fileButton = ui.makeButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File selectedFile = fc.showOpenDialog(stage);
                if (selectedFile != null){
                    getFile(selectedFile, currentDuel);
                }
            }
        });

        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(FILE_ADDER_IMAGE));
        fileButton.setGraphic(ui.makeCorrectSize(image, FILE_IMAGE_SIZE));
        this.getChildren().add(fileButton);
    }

    private void createSimulateButton(RunCellSociety run, UserInterface ui, Boolean duel){
        boolean currentDuel = duel;
        Button runSimulationButton = ui.makeButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateSimulation(run, currentDuel, duel);
            }
        });
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(RUN_BUTTON_IMAGE));
        runSimulationButton.setGraphic(ui.makeCorrectSize(image, FILE_IMAGE_SIZE));
        this.getChildren().add(runSimulationButton);
    }

    private void createFileBar(StackPane boxGroup, boolean duel, ResourceBundle displayedStrings){
        Rectangle fileNameSpace = new Rectangle(FILE_BOX_WIDTH, FILE_BOX_HEIGHT);
        fileNameSpace.getStyleClass().add(FILE_NAME_BOX_STYLE);
        Text newText = getChooseFileText(displayedStrings);
        newText.getStyleClass().add(FILE_TEXT_STYLE);
        boxGroup.getChildren().add(fileNameSpace);
        boxGroup.getChildren().add(newText);
        if (duel){
            myRightFileText = newText;
        }
        else{
            myLeftFileText = newText;
        }
    }

    private Text getChooseFileText(ResourceBundle displayedStrings){
        return new Text(displayedStrings.getString(CHOOSE_FILE_STRING));
    }

    private void getFile(File chosenFile, boolean isDuel){
        String fileName;
        if (chosenFile.toString().indexOf('/') != -1){
            fileName = chosenFile.toString().substring(chosenFile.toString().lastIndexOf('/') + 1);
        }
        else {
            fileName = chosenFile.toString().substring(chosenFile.toString().lastIndexOf('\\') + 1);
        }

        if (isDuel) {
            myRightFileText.setText(fileName);
        }
        else {
            myLeftFileText.setText(fileName);
        }
    }

    private void updateSimulation(RunCellSociety run, boolean isDuel, boolean duel){
        String simName;
        if (isDuel){
            simName = myRightFileText.getText();
        }
        else {
            simName = myLeftFileText.getText();
        }
        System.out.printf("Creating new simulation: %s%n", simName);
        boolean fromSaved = false;
        if (simName.substring(0, 2).equals(SAVED_INDICATOR)){
            fromSaved = true;
        }
        run.createSimulation(simName, isDuel, fromSaved, duel);
    }


}

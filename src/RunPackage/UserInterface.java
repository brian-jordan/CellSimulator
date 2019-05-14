package RunPackage;

import Exceptions.SimulationException;
import SimulationPackage.Simulation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import java.util.ResourceBundle;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

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
 * The UserInterface class creates and displays the user interface window and receives input from the user.
 * Few assumptions were made in the development of the class to ensure flexibility, but one example is that the size of
 * the display window would not be adjusted.
 * This class relies heavily on the JavaFX node, button and text classes.
 * Calling the constructor creates the home display of the program and clicking the buttons calls the on Action methods.
 */


public class UserInterface{

    public static final String DEFAULT_RESOURCE_PACKAGE = "/Resources/";
    public static final String STYLESHEET = "default.css";
    public static final String PANE_STYLE = "pane";
    public static final String MAIN_BOX_STYLE = "mainBoxes";
    public static final String CENTERED_BOX_STYLE = "centeredBox";
    public static final String CONTROL_BOX_STYLE = "controlBox";
    public static final String SIMULATION_BUTTON_STYLE = "simButton";
    public static final String TITLE_BOX_STYLE = "titleBox";
    public static final String TITLE_TEXT_STYLE = "titleText";
    public static final String INFO_TEXT_STYLE = "infoText";
    public static final String SIMULATION_BOX_STYLE = "simulationSpace";
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int SIMULATION_WIDTH = 400;
    public static final int SIMULATION_HEIGHT = 400;
    public static final String PAUSE_BUTTON_IMAGE = "pauseButton.gif";
    public static final String PLAY_BUTTON_IMAGE = "playButton.gif";
    public static final String STEP_BUTTON_IMAGE = "forwardButton.gif";
    public static final String INFO_TEXT_String = "InfoString";
    public static final String TITLE_STRING = "TitleText";
    public static final String SAVE_BUTTON_STRING = "SaveButton";
    public static final String MULTISIMULATION_STRING = "DuelSimulationButton";
    public static final int CONTROL_IMAGE_SIZE = 40;
    public static final double INITIAL_SLIDER_VALUE = 0.5;


    private Stage myStage;
    private BorderPane myRoot;
    private RunCellSociety myRun;
    private Scene myScene;
    private Slider mySlider;
    private Button myPlayPauseButton;
    private ResourceBundle myStrings;
    private boolean paused;
    private StackPane myLeftSimulationSpace;
    private StackPane myRightSimulationSpace;
    private boolean duel;

    /**
     * Creates instance of the User Interface and calls method to populate window
     * @param stage - window the program is displayed in
     * @param runner - RunCellSociety instance that calls the constructor
     * @param displayedStrings - resource bundle containing strings displayed in the interface
     */

    public UserInterface(Stage stage, RunCellSociety runner, ResourceBundle displayedStrings){
        myStage = stage;
        myRun = runner;
        myStrings = displayedStrings;
        setStartingScreen();
    }

    /**
     * Creates the initial screen of the programs's User Interface
     */

    public void setStartingScreen(){
        paused = true;
        duel = false;
        myRoot = new BorderPane();
        myRoot.getStyleClass().add(PANE_STYLE);
        myRoot.setTop(makeTitleBar());
        myRoot.setCenter(makeSimulationSpace());
        myRoot.setRight(makeSimulationSpace());
        makeSimulation((VBox)(myRoot.getCenter()));
        myScene = new Scene(myRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        myScene.getStylesheets().add(UserInterface.class.getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
        setMyStage();
    }

    private void setMyStage(){
        myStage.setScene(myScene);
        myStage.setTitle(myStrings.getString(TITLE_STRING));
        myStage.show();
    }

    private HBox makeTitleBar(){
        var titleBar = new HBox();
        titleBar.getStyleClass().add(TITLE_BOX_STYLE);
        Text title = createTitle();
        titleBar.getChildren().add(title);
        createDuelSimulationButton(titleBar);
        return titleBar;
    }

    private VBox makeSimulationSpace(){
        var simulationSpace = new VBox();
        simulationSpace.getStyleClass().add(MAIN_BOX_STYLE);
        return simulationSpace;
    }

    private void makeSimulation(VBox boxGroup){
        makeSimulationBox(boxGroup);
        if (!duel){
            VBox controls = makeControlBox();
            boxGroup.getChildren().add(controls);
        }
        HBox fileBarBox = new FileBar(myRun, this, myStage, duel, myStrings);
        boxGroup.getChildren().add(fileBarBox);
    }

    private void makeSimulationBox(VBox boxGroup){
        StackPane sp = new StackPane();
        sp.getStyleClass().add(CENTERED_BOX_STYLE);
        createCellLayout(sp);
        if (duel){
            myRightSimulationSpace = sp;
        }
        else {
            myLeftSimulationSpace = sp;
        }
        boxGroup.getChildren().add(sp);
    }

    private VBox makeControlBox(){
        var controlSpace = new VBox();
        createSimulationControls(controlSpace);
        return controlSpace;
    }

    private Text createTitle(){
        Text t = new Text();
        t.setText(myStrings.getString(TITLE_STRING));
        t.getStyleClass().add(TITLE_TEXT_STYLE);
        return t;
    }

    private void createDuelSimulationButton(HBox boxGroup){
        var duelSimulationButton = makeButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                createOrRemoveDuelSimulation();
            }
        });
        duelSimulationButton.setText(myStrings.getString(MULTISIMULATION_STRING));
        duelSimulationButton.getStyleClass().add(SIMULATION_BUTTON_STYLE);
        boxGroup.getChildren().add(duelSimulationButton);
    }

    private void createCellLayout(StackPane boxGroup){
        Rectangle spaceHolder = new Rectangle(SIMULATION_WIDTH, SIMULATION_HEIGHT);
        spaceHolder.getStyleClass().add(SIMULATION_BOX_STYLE);
        Text infoText = getInfoText();
        infoText.setWrappingWidth(SIMULATION_WIDTH);
        infoText.getStyleClass().add(INFO_TEXT_STYLE);
        boxGroup.getChildren().add(spaceHolder);
        boxGroup.getChildren().add(infoText);
    }

    private void createSimulationControls(VBox boxGroup) {
        var controlButtonBox = new HBox();
        controlButtonBox.getStyleClass().add(CONTROL_BOX_STYLE);
        makeControlButtons(controlButtonBox);
        var controlSliderBox = new HBox();
        controlSliderBox.getStyleClass().add(CONTROL_BOX_STYLE);
        mySlider = makeSlider();
        controlSliderBox.getChildren().add(mySlider);
        boxGroup.getChildren().add(controlButtonBox);
        boxGroup.getChildren().add(controlSliderBox);
    }

    private void makeControlButtons(HBox box){
        myPlayPauseButton = makeButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                playPauseSimulation();
            }
        });
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(PLAY_BUTTON_IMAGE));
        myPlayPauseButton.setGraphic(makeCorrectSize(image, CONTROL_IMAGE_SIZE));
        box.getChildren().add(myPlayPauseButton);
        var stepButton = makeButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stepSimulation();
            }
        });
        image = new Image(this.getClass().getClassLoader().getResourceAsStream(STEP_BUTTON_IMAGE));
        stepButton.setGraphic(makeCorrectSize(image, CONTROL_IMAGE_SIZE));
        box.getChildren().add(stepButton);
        var saveButton = makeButton(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    saveState();
                } catch (Exception e) {
                    myRun.showErrorMessage(new SimulationException("No Simulation Running"));
                }
            }
        });
        saveButton.setText(myStrings.getString(SAVE_BUTTON_STRING));
        saveButton.getStyleClass().add(SIMULATION_BUTTON_STYLE);
        box.getChildren().add(saveButton);
    }

    /**
     * Basic create button method modified for specific functionality when called
     * @param handler - event handler for user input
     * @return - returns button object
     */

    public Button makeButton(EventHandler<ActionEvent> handler){
        var newButton = new Button();
        newButton.setOnAction(handler);
        return newButton;
    }

    private Slider makeSlider(){
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(1);
        slider.setValue(0);
        return slider;
    }

    private Text getInfoText(){
        return new Text(myStrings.getString(INFO_TEXT_String));
    }

    private void createOrRemoveDuelSimulation(){
        if (!duel){
            duel = true;
            if (!((VBox)myRoot.getRight()).getChildren().isEmpty()){
                ((VBox) myRoot.getRight()).getChildren().clear();
            }
            makeSimulation((VBox)(myRoot.getRight()));
        }
        else {
            ((VBox) myRoot.getRight()).getChildren().clear();
            duel = false;
        }
    }

    private void playPauseSimulation(){
        if (paused){
            var image = new Image(this.getClass().getClassLoader().getResourceAsStream(PAUSE_BUTTON_IMAGE));
            myPlayPauseButton.setGraphic(makeCorrectSize(image, CONTROL_IMAGE_SIZE));
            paused = false;
            setSliderValue(INITIAL_SLIDER_VALUE);
            myRun.play();
        }
        else {
            stopSimulation();
            myRun.stop();
        }
    }

    private void stepSimulation(){
        stopSimulation();
        myRun.step();
    }

    private void stopSimulation(){
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(PLAY_BUTTON_IMAGE));
        myPlayPauseButton.setGraphic(makeCorrectSize(image, CONTROL_IMAGE_SIZE));
        paused = true;
        setSliderValue(0);
    }

    private void saveState() throws TransformerException, ParserConfigurationException {
        stopSimulation();
        myRun.stop();
        saveCurrentGrid();
    }

    private void saveCurrentGrid() throws TransformerException, ParserConfigurationException {
        Simulation[] currSims = this.myRun.getSimulations();
        for (Simulation sim : currSims) {
            sim.getWriter().saveAsXML(sim.getSimName(), sim.getMyCellGrid().getGrid(),sim.getSimData().getGameConfig(), sim.getSimData().getCellConfig(), sim.getSimData().getParameters());
        }
    }

    /**
     * Adds simulation cells with initial states to the User Interface
     * @param newSim - new simulation to be run in simulation space of window
     * @param isDuel - indicator if simulation should be in left or right simulation window
     */

    public void placeCells(Simulation newSim, boolean isDuel){
        if (isDuel) {
            myRightSimulationSpace.getChildren().clear();
            myRightSimulationSpace.getChildren().add(newSim);
        }
        else {
            myLeftSimulationSpace.getChildren().clear();
            myLeftSimulationSpace.getChildren().add(newSim);
        }
    }

    /**
     * Adds state plot to the User Interface
     * @param plot - Line graph used to track state quantities
     */

    public void placePlot(StatePlot plot){
        ((VBox)myRoot.getRight()).getChildren().clear();
        ((VBox)myRoot.getRight()).getChildren().add(plot);
    }

    /**
     * Get method for the slider's value
     * @return - current numerical position of the slider
     */

    public double getSliderValue(){
        return mySlider.getValue();
    }

    /**
     * Set method for the slider's value
     * @param val - desired numerical position of the slider
     */

    public void setSliderValue(double val){
        mySlider.setValue(val);
    }

    /**
     * Adjusts an image to the desired size
     * @param image - image file to be depicted in the window
     * @param size - desired height and width of image
     * @return - image modified to the desired size
     */

    public ImageView makeCorrectSize(Image image, int size){
        var iv = new ImageView(image);
        iv.setFitWidth(size);
        iv.setFitHeight(size);
        return iv;
    }
}

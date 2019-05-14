package RunPackage;

import Exceptions.SimulationException;
import SimulationPackage.Simulation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;
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
 * The RunCellSociety class is the main class in this implementation of the Cell Society program.  Its purposes is to
 * integrate front and back end functionality of the program.
 * No assumptions were made in the development of this class in order to keep the code as flexible as possible.
 * This class extends the application class and depends on the frontend and backend classes for functionality
 * This class creates the User Interface and animation of the program and translates information from the frontend into
 * calls to backend methods.
 */

public class RunCellSociety extends Application {

    public static final int FRAMES_PER_SECOND = 10;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final double INITIAL_SIMULATION_SPEED = 1;
    public static final String DEFAULT_RESOURCE_PACKAGE = "/Resources/";
    public static final String STRINGS_FILE = "Strings";
    public static final String ALERT_STRING = "AlertTitle";
    public static final String RESET_MESSAGE_STRING = "ResetMessage";

    private Stage myStage;
    private Timeline myAnimation;
    private UserInterface myUI;
    private Simulation[] mySimulations;
    private ResourceBundle myStrings;
    private int myStepCount;
    private StatePlot myPlot;

    /**
     * Initializes display, creates an instance of the User Interface class and calls to create the animation
     * @param stage
     */

    @Override
    public void start (Stage stage){
        mySimulations = new Simulation[2];
        myStage = stage;
        myStrings = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + STRINGS_FILE);
        myUI = new UserInterface(myStage, this, myStrings);
        createAnimation();
    }

    private void createAnimation(){
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);
    }

    private void step (double elapsedTime) {

        if (myAnimation.getRate() !=  INITIAL_SIMULATION_SPEED * myUI.getSliderValue()){
            myAnimation.setRate(INITIAL_SIMULATION_SPEED * myUI.getSliderValue());
        }
        step();
    }

    /**
     * Creates an instance of the Simulation class based on a specific xml file
     * @param simFile - XML file of specific simulation selected by user
     * @param isDuel - indicator if simulation should be in left or right simulation window
     * @param fromSaved - indicator if XML file selected is from saved simulation
     * @param currentlyDuel - indicator if window is currently running two simulations
     */

    public void createSimulation(String simFile, boolean isDuel, boolean fromSaved, boolean currentlyDuel) {
        try {
            Simulation newSim = new Simulation(simFile, fromSaved);
            myUI.placeCells(newSim, isDuel);
            if (isDuel) {
                mySimulations[1] = newSim;
            }
            else {
                mySimulations[0] = newSim;
            }
        } catch (Exception e){
            showErrorMessage(new SimulationException("Error with Simulation"));
            myUI.setStartingScreen();
        }
        if (!currentlyDuel){
            myPlot = new StatePlot(mySimulations[0], myStrings);
            myStepCount = 0;
            myPlot.addData(myStepCount, mySimulations[0]);
            myUI.placePlot(myPlot);
        }

    }

    /**
     * Runs current simulations
     */

    public void play(){
        myAnimation.setRate(INITIAL_SIMULATION_SPEED);
        myAnimation.play();
    }

    /**
     * Pauses current simulations
     */

    public void stop(){
        myAnimation.stop();
    }

    /**
     * Creates popup box in the window describing exception
     * @param e - Specific exception
     */

    public void showErrorMessage(Exception e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(myStrings.getString(ALERT_STRING));
        alert.setHeaderText(e.getMessage());
        alert.setContentText(myStrings.getString(RESET_MESSAGE_STRING));
        alert.showAndWait();
    }

    /**
     * Makes one step in the simulation calling to update the states of the cells
     */

    public void step(){
        for (int i = 0; i < mySimulations.length; i++){
            if (mySimulations[i] == null){
                myStepCount++;
                myPlot.addData(myStepCount, mySimulations[0]);
            }
            else{
                mySimulations[i].update();
            }
        }
    }

    /**
     * Get method for array of current simulations
     * @return - array of current simulations
     */

        public Simulation[] getSimulations () {
            return mySimulations;
        }

        /**
         * Start the program.
         */
        public static void main (String[]args){
            launch(args);
        }

}


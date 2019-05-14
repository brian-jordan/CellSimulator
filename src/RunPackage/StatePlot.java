package RunPackage;

import SimulationPackage.Simulation;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
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
 * The StatePlot class creates a node that contains the plot of the quantities of cells at a specific state for the
 * running simulation.
 * The main assumption for the class is that all of the information about a simulation would be provided.
 * This class extends the Pane class to return a node that is easily addable to the User Interface.
 * This class creates a plot of cell state count corresponding to a specific simulation and adds it to a Pane object.
 */

/**
 * This is an example of well designed code, because it exemplifies modularity in the front-end of the program.  This
 * class encapsulates the plotting of cell state counts in the program window.  It is closed off from other parts of the
 * program except for an addData method that allows for points to be dynamically plotted after each step.  This also
 * exemplifies flexible code, because it works for any type of simulation as long as the names of the states, colors of
 * the states' cells, and data are passed in.
 */

public class StatePlot extends Pane {


    public static final String PLOT_TITLE_STRING = "PlotTitle";
    public static final String XAXIS_STRING = "XAxisLabel";
    public static final String YAXIS_STRING = "YAxisLabel";
    public static final String AXIS_DESIGN = "axis";
    public static final String CHART_STYLE = "chart";

    private XYChart.Series[] mySeriesArray;

    /**
     * Creates an instance of the StatePlot class and adds a formatted plot to it corresponding to a specific simulation
     * @param mySim - specific simulation to plot the states of
     * @param displayedStrings - resource bundle containing strings displayed in the interface
     */

    public StatePlot(Simulation mySim, ResourceBundle displayedStrings){
        super();
        NumberAxis[] axis = makeAxis(displayedStrings);
        LineChart<Number, Number> plotOfStates = new LineChart<>(axis[0], axis[1]);
        plotOfStates.setTitle(displayedStrings.getString(PLOT_TITLE_STRING));
        String myColors = getColors(mySim);
        plotOfStates.getStyleClass().add(CHART_STYLE);
        plotOfStates.setStyle(myColors);
        mySeriesArray = createStatesPlots(mySim, plotOfStates);
        this.getChildren().add(plotOfStates);
    }

    private NumberAxis[] makeAxis(ResourceBundle displayedStrings){
        NumberAxis[] axis = new NumberAxis[2];
        var xAxis = new NumberAxis();
        var yAxis = new NumberAxis();
        xAxis.getStyleClass().add(AXIS_DESIGN);
        yAxis.getStyleClass().add(AXIS_DESIGN);
        xAxis.setLabel(displayedStrings.getString(XAXIS_STRING));
        yAxis.setLabel(displayedStrings.getString(YAXIS_STRING));
        axis[0] = xAxis;
        axis[1] = yAxis;
        return axis;
    }

    private XYChart.Series[] createStatesPlots(Simulation mySim, LineChart<Number, Number> chart){
        XYChart.Series[] sa = new XYChart.Series[mySim.getStateNames().size()];
        int iter = 0;
        for (String state: mySim.getStateNames()){
            sa[iter] = new XYChart.Series();
            sa[iter].setName(state);
            chart.getData().add(sa[iter]);
            iter++;
        }
        return sa;
    }

    /**
     * Adds cell state counts to the plot in relation to the step number
     * @param step - number of times cell states have been updated
     * @param mySim - specific simulation to plot the states of
     */

    public void addData(int step, Simulation mySim){
        int iter = 0;
        for (String s: mySim.getStateNames()){
            mySeriesArray[iter].getData().add(new XYChart.Data(step, mySim.getMap().get(s)));
            iter++;
        }
    }

    private String getColors(Simulation mySim){
        String colorString = "";
        for (int i = 0; i < mySim.getStateColors().length; i++){
            colorString = colorString + "CHART_COLOR_" + (i + 1) + ": #" + mySim.getStateColors()[i].toString().substring(2, mySim.getStateColors()[i].toString().length() - 2) + " ;";
        }
        return colorString;
    }
}

cell society
====
 
This project implements a cellular automata simulator.

Names: Bryant Huang, Brian Jordan, Feroze Mohideen

### Timeline

Start Date: 01/30/2019

Finish Date: 02/04/2019

Hours Spent: 90

### Primary Roles

Brian: worked on the front-end visualizations including the GUI layout, State Plots and button functionalities. 
Bryant: worked on the back-end simulation rule implementation as well as the XML parser.
Feroze: worked on the front-end/back-end integration and data flow between parser and simulation.

### Resources Used

The `images` directory must be marked as resources root. The `data` directory contains all XML files used for testing.

Image Sources
Play Button: https://pixabay.com/en/play-button-round-blue-glossy-151523/
Pause Button: https://openclipart.org/detail/169038/windows-media-player-pause-button
Arrow Button: //https://pixabay.com/en/back-return-arrow-left-button-158491/
File Adder Button: https://www.flaticon.com/free-icon/add-file-button_9233
Run Button: http://www.freepngclipart.com/free-png/54933-play-computer-youtube-button-icons-free-hq-image

CSS Styles
Title Text: https://docs.oracle.com/javafx/2/get_started/css.htm*/
Button Style: http://fxexperience.com/2011/12/styling-fx-buttons-with-css/*/



### Running the Program

Main class: `RunPackage.RunCellSociety.java`

Data files needed: An XML file such as one found in the `data` directory and all images placed in the `images` 
directory.

Interesting data files: `s_Fire.xml` and `s_RPS.xml` are two example XML files one can use to load from a saved state
. In addition, one can use `percolation_bad.xml` to display error-checking.

Features implemented:

- Allowed a variety of grid location shapes:
  - square: with 8 neighbors max
  - triangular: with 12 neighbors max
  - hexagonal: with 6 neighbors 
 - Implemented additional simulations (any simulations should work on any kinds of grid or neighborhood types):
  - Rock, Paper, Scissors Game
  - Foraging Ants
 - Implemented error checking for incorrect file data, such as (but not necessarily limited to)
   - invalid or no simulation type given
   - checks for valid .xml file ending
   - checks if the number of colors doesn't match the total different colors given
   - Checks if the .xml file contains valid formatting and content
   - checks if valid cell state values given (i.e. if a cell matches "DEAD" or "ALIVE")
   - cell locations given that are outside the bounds of the grid's size
   - If error thrown, these errors all display a dialog box that handles the error, prevents the game from crashing, and returns the user back to the GUI.
 - Allowed simulations initial configuration to be set by
   - list of specific locations and states
   - randomly based on probability/concentration distributions
   - set initial configuration from saved file
 - Allowed users to save the current state of the simulation as an XML configuration file
    - files are saved with an "s_" beginning attached and are successfully reloaded every single time
 - Displayed a graph of the populations of all of the "kinds" of cells over the time of the simulation  
 - Allowed users to interact with the simulation dynamically to create or change a state at a grid location
 - Allowed users to run multiple simulations at the same time so they can compare the results side by side.
 

Assumptions or Simplifications: We assume a few rules of some simulations.  One of the assumptions made universally was the fact that one a cell was altered, it could not be changed again.  Instead of using more complex algorithms to first collect all possible moves and maximize the amount of moves possible by all cells, we wanted to make sure that our simple update implementation works and was of good design.  This was fundamental to our update algorithm  - technically speaking, the cells at the end of the grid would have less chances of moving if the cells prior to that had already occupied that spot.  We made assumptions about the energy and reproduction levels in the Predator Prey game, the probability of catching fire in the Fire game, the minimum ratio for cells to be satisfied in Segregation, and the number/distribution of ants in the Foraging ants game. Though initial states and variables are assumed in some cases, for the most part the rules align directly with what was given.  We made the assumption that these games were of Rectangular shape, although our complete implementation now supports different shapes.  The Foraging Ants and RPS games may have been simplified both in design and scale - our game could not support such a large grid based on current implementation or it would either be very slow or crash.  Such simplifications enabled us to continue improving as we made greater steps.

Known Bugs: The Foraging Ants simulation doesn't work properly. Predator Prey works for the most part, but there is a bug that does not spawn additional prey cells when the simulation progresses beyond a certain point.  An error message mistakenly pops up when save is clicked, but the file are still saved successfully and the program continues on following dismissal.

Extra credit: Added a way to extend the simulation once it's over by changing the states of some grid locations 
through clicking.


### Notes

We learned a lot during this project about working together as a team as well as how to make good design decisions 
when working on a collaborative project. We managed to split up work effectively while not getting in the way of each
 other too much, and handled blocks in our coding processes efficiently as well. This was all of our first times 
 using git collaboratively, so we all learned a lot about branching and merging which we know will serve us well in 
 later projects as well as future careers. We learned a lot about time management, building on foundational implementations, and the importance of modularity in making continual developement easier.  Overall, we did our best to utilize the lessons we have been shown in class - 'DRY' programming and 'Tell the Other Guy' in particular.  

### Impressions
Feroze: I enjoyed most of this project - it was really cool to see the pieces come together from front-end to 
back-end and how we were able to easily adapt our design in sprint 2 when we added more simulations. I appreciated 
the rest of my group's shared dedication to implementing features and working hard.

Brian: This was my first time working on a group project developing an application.  I lucked out in terms of groupmates
with Bryant and Feroze being experienced and hardworking teammates.  We worked well together throughout the entire
lifecycle of the project in a communicative and collaborative way.  I enjoyed working on the front-end of the assignment
and really getting to understand the in's and out's of javafx.  I look forward to trying out working on the backend in
future projects.

Bryant: This was a really fun and great project for me.  Feeling that I underachieved my abilities in the Breakout game, I took the lessons on good design from class and really focused on them this time, making that a priority before I began coding.  Luckily, I had two great teammates in Feroze and Brian, and our constant communication about what we were working on made the integration of back-end and front-end implementation mostly seamless.  This was a great experience for me working with a team on an application for the first time and I feel like I learned a lot.  Couldn't have been more happy with my teammates for this project and I look forward to future challenges!

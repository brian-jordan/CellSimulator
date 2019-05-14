Inheritance Review Questions
CompSci 308 Spring 2019
01/31/2019
Brian Jordan Michael Zhang

Part 1
1. What is an implementation decision that your design is encapsulating (i.e., hiding) for other areas of the program?
Michael:
    The other parts of the program don't know the root node.  Kept within the main class and not visible to the other
components of the program.  The cells themselves don't know how they are being depicted.  This information is being
stored in the visualization class.
Brian:
    The visualization part of the program does not know about the cells and how they will be depicted.  It only receives
a group of cells which it adds to the root and renders in the display.

2. What inheritance hierarchies are you intending to build within your area and what behavior are they based around?
Michael:
    Visualization doesn't have sub-classes.  There are abstract classes for simulations and cells.  There are child
classes that are extended off of these and specific to the certain simulation.  The cells behavior is knowing where they
are located and the simulations behavior is having ownership of the grid.
Brian:
    In a similar way, we are using inheritance with abstract simulation and cell classes.  The simulation class is
responsible for administering the rules of the simulation in updating cell state.  The cell class is responsible for
knowing and changing its state and how it is depicted on the screen.

3. What parts within your area are you trying to make closed and what parts open to take advantage of this polymorphism 
you are creating?
Michael:
    There is no polymorphism in his visualization part of the program.  The simulation and cell classes use polymorphism
and the grid is closed off by the simulation class and the location of the cell is closed off by the cell class.
Brian:
    There is polymorphism in the simulation and cell classes in my groups program as well.  What's closed off to the
simulation class is the rules of the game.  What's closed off to the cell class is the visualization and location of the
cells.

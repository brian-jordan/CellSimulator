The main aspect on the backend that we worked on was changing the Cell abstract so it did not contain all of the 
variables that were only specific to subclass simulations.  For example, we deleted the energy variable from the 
abstract class and made it specific to the PredatorPrey class, and we also cleaned up some methods that were somewhat 
duplicated in both the abstract and the subclasses.  We deleted some global variables that were repeated in the 
subclasses and cleared up some comments.  Overall, the Cell class and abstract classes became a lot more flexible.

We accomplished this by learning more about wildcards and generics, two useful tools I think we will definitely work 
with during the rest of the project. The main issue was that Cell objects were being collected and casted to their 
superclass, therefore losing all their subclass methods. Wildcards allowed us to create a list of unknown objects 
that extend Cell whose subclass specific methods we could use.

For refactoring the frontend, the issues that the design site captured revolved around storing values and objects.  One
example of this was the issue with using magic numbers.  The values used in the UserInterface class that previously were
not stored in final variables were changed to be except for the general values of 0 and 1.  Another issue that
the design site pointed out was the case of objects being made global in the UserInterface class even though they were
not instantiated and only taken as an input to the constructor.  We decided to remove these global objects and rather just pass the
argument objects into the methods that needed to use or modify them.  The most amount of frontend refactoring time in
lab was spent moving the css and properties files from a separate resources directory into the src directory in order to
prevent modifications from outside the program.  Setting up the file path so that the existing methods recognized the
files in the new location took a while to figure out but ended up functioning.  These were the general refactoring
changes made to the frontend of the code.  In terms of refactoring long methods, all of the methods in the front end 
classes are pretty short and single functioning so we didnt focus on this during lab.  The one change we still need to 
make in these classes is fixing exception handling which we are focusing on in this sprint.
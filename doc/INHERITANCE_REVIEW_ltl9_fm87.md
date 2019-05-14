# Part 1

### Encapsulation
Encapsulate cell methods locally to make sure that cells can be treated as independent entities. 
### Inheritance
We are using the cell class as an abstract class. Its behavior common to all cell types relies on updating the cell 
state and changing its visualization state to match that change in state. Each type of cell with its different rules 
will implement these basic methods.
###Polymorphism
I'm making adding cell types (and possibly cell shapes) open, along with implementing the rules that go with their 
specific simulations. Also, I am thinking of possibly adding the ability to add entirely new simulation visualization
 options to the main GUI.
###Exceptions
There will be exceptions involving trying to upload files that don't match the XML template we are using. We will 
have to account for that using `catch` statements.
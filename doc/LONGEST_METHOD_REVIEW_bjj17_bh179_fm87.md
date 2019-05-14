# Longest Method Refactoring
### Longest method: `makeControlButtons()` within `UserInterface.java`
- In this method, we make 3 separate buttons. Instead, we could have three methods that make each separate button, or one polymorphic method that makes each method. 
- We could use lambda expressions instead of overriding the Event Handler in each button instantiation, reducing the number of lines of code.
- We could implement another method that generally sets the image of a button, `setControlButtonImage`, and use it for all the buttons.

### Second longest method: `getForwardNeighbors(int direction, ForagingAntCell cell)` within `ForagingAntCell.java`
- We can try to condense all four of the `if` statements to one `if` statement, or a `switch` tree.
- Additionally, we could use an `enum` to encompass all types of Neighbors and use that within the Ant cells.

### Third longest method: `getNeighbors(SimulationData data)` within `CellManager.java`
- We changed this method within the masterpiece, but we noticed that we could take advantage of polymorphism to solve the repetition in this piece of code.
- We implemented a `Neighbor` superclass with different neighbor classes extending it, e.g. `Neighbors4` and `Neighbors8`.
- Each `Neighbor` subclass updates the private `int[][]` variable that we need, providing extensibility. To add a new neighbor configuration, we would just need to subclass from `Neighbor` with the new configuration. 

## Key Issues
- The most pressing issue is the duplicated structures and use of a lot of if statements rather than using enums and switch statements to execute different commands.  Changing this would most drastically improve the design and enable greater flexibility.  The next issue would be more greatly utilizing polymorphism to further enhance flexibility. Least important would be separating the longer methods into smaller helper methods. This is important organization but doesn't greatly change the design of the program to enhance functionality.

## Specific Overall Design Changes
- For the `makeControlButtons()` method, it would be effective to use lamda expressions instead of fully writing out the overrides of the buttons'`handle()`methods.  The other design change would be to create a method that sets the image of the button by taking in the arguments of the specific button object and the image identifier string reducing the repeated code.
- For the `getNeighbors(SimulationData data)` method, we could implement a direction enum to remove the if statements and need for the nested if statements.
- For the `getNeighbors(SimulationData data)` method, we could implement polymorphism to create different options for selecting cell neighbors.  This would also allow for better ease of extension when adding new neighbor configurations.

## Three resolutions
1. Take advantage of data structures like enums to reduce the number of branching `if` statements.
2. Use inheritance and subclassing to provide greater extensibility.
3. Pay closer attention to methods and extract specific functionalities into helper classes to make methods more clear. 
# Color Twist Animation

Consider a screen with a single row of pixels. Pixels can be either Red or Yellow. Animate the pixels using the 
following rules:

1. Red pixels can only move to the RIGHT.
2. Yellow pixels can only move to the LEFT.
3. Both color pixels will move at the same speed.
4. The pixels can pass through each other. So if the Red and Yellow colors occupy the SAME pixel, then Orange should be 
displayed.
5. The initial state at each position will be given; a 'Y' for a leftward moving Yellow pixel, an 'R' for a rightward 
moving Red pixel or a '.' (dot) for an empty pixel. All positions on the screen will be shown. Initially, 
no location will contain two color pixels passing through each other.

Create an animation of the process. At each unit of time, show occupied locations with either an 'R', 'Y' or 'O' (if 
two colors occupy the SAME pixel) and unoccupied locations with a '.' (dot). Start with a class called Animation that 
contains an animate method that is given an initial speed and initial state. The speed is the number of positions each 
particle moves in one unit of time. 

The method will return an array of strings in which each successive element shows the occupied locations at the next 
unit of time. The FIRST element should show the occupied locations at the initial instant (at time = 0) in 
the 'R', 'Y', '.' format. The LAST element should show the empty screen the first time that it becomes empty.

CLASS: Animation

METHOD: animate(speed, init), where speed is an integer and init is a String.

ASSUMPTIONS:
- speed will be between 1 and 10 (inclusive)
- init (initial state) will contain between 1 and 50 characters (inclusive)
- Each character in the initial state will be 'R', 'Y' or '.' (dot).

Pete Sattler  
November 2018  
_peter@sattler22.net_

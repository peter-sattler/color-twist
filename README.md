# Crowd Twist Animation Challenge 2018

Consider a screen with single row of pixels. Pixels can be either Red or Yellow colors. We have to animate the pixels using the following rules :
Red pixels can only move to the right.


1. Yellow pixels can only move to the left.
2. Both color pixels will move at the same speed.
3. The pixels can pass through each other. So if the Red and Yellow colors occupy the SAME pixel then we have to display Orange color.
4. You will be given the initial conditions by a String init containing at each position a 'Y' for a leftward moving yellow pixel, an 'R' for a rightward
moving Red pixel, or a '.' for an empty pixel. init shows all the positions on the screen. Initially, no location on the screen contains the two color
pixels passing through each other.

We would like an animation of the process. At each unit of time, we want a string showing occupied locations with either a 'R' , 'Y' or 'O' (if the two
colors occupy same pixel) and unoccupied locations with a '.'. Create a class Animation that contains a method animate that is given an initial
speed and a String init giving the initial conditions. The speed is the number of positions each particle moves in one time unit.

The method will return an array of strings in which each successive element shows the occupied locations at the next time unit. The first element
of the return should show the occupied locations at the initial instant (at time = 0) in the 'Y','R', '.' format. The last element in the return should
show the empty screen at the first time that it becomes empty.

Class: Animation

Method: animate(speed, init), where speed is an integer and init is a String.

You may assume the following constraints:
- speed will be between 1 and 10 inclusive
- init will contain between 1 and 50 characters inclusive
- each character in init will be '.' or 'Y', 'R' or 'O'

package breakout;

import java.awt.Color;
import acm.graphics.*;
import acm.util.RandomGenerator;

// This class represents a ball in the breakout game
public class Ball extends GOval {
	// INSTANCE VARIABLES
	// Random generator
	private RandomGenerator rand = RandomGenerator.getInstance();
	
	// Ball moving speed in x axis
	private double xSpeed;
	// Ball moving speed in y axis
	private double ySpeed;
	
	// Constructor
	public Ball(double x, double y, double width, double height) {
		super(x, y, width, height);
		
		this.setFilled(true);
		this.setColor(Color.black);
	}
	
	// Moves ball on screen
	public void move() {
		super.move(xSpeed, ySpeed);
	}
	
	// Bounces ball, inverting the x or y axis speed
	public void bounce(char axis) {
		switch(axis) {
			case 'x': xSpeed = -xSpeed;	break;
			case 'y': ySpeed = -ySpeed;	break;
		}
	}
	
	// Start moving the ball in a random direction
	public void start(double xs, double ys) {
		xSpeed = xs;
		ySpeed = ys;
		
		if (rand.nextBoolean()) this.bounce('x');
	}
}


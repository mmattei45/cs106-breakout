package breakout;

import acm.graphics.*;

import java.awt.Color;

//This class represents the paddle in the breakout game
public class Paddle extends GRect {
	//Class instance variables
	//Sets the x limits of moving the paddle
	private double minXLimit, maxXLimit;
	
	//Class constructor
	public Paddle(double paddleX, double paddleY, double paddleWidth, double paddleHeight) {
		super(paddleX, paddleY, paddleWidth, paddleHeight);
		setFilled(true);
		setColor(Color.BLUE);
	}
	
	//MinXLimit
	public void setMinXLimit(double min) {
		minXLimit = min;
	}
	
	//MaxXLimit
	public void setMaxXLimit(double max) {
		maxXLimit = max;
	}
	
	//Move the paddle left
	public void moveLeft(double speed) {
		if (getX() > minXLimit) {
			move(-speed, 0);
		}
	}
	
	//Move the paddle right
	public void moveRight(double speed) {
		if(getX() + getWidth() < maxXLimit) {
			move(speed, 0);
		}
	}
}

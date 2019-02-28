package breakout;

/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;


public class Breakout extends GraphicsProgram {

	// Dimensions of the canvas, in pixels
	// These should be used when setting up the initial size of the game,
	// but in later calculations you should use getWidth() and getHeight()
	// rather than these constants for accurate size information.
	public static final int CANVAS_WIDTH = 420;
	public static final int CANVAS_HEIGHT = 600;

	// Number of bricks in each row
	public static final int NBRICK_COLUMNS = 10;

	// Number of rows of bricks
	public static final int NBRICK_ROWS = 10;

	// Separation between neighboring bricks, in pixels
	public static final double BRICK_SEP = 4;

	// Width of each brick, in pixels
	public static final double BRICK_WIDTH = Math.floor(
			(CANVAS_WIDTH - (NBRICK_COLUMNS + 1.0) * BRICK_SEP) / NBRICK_COLUMNS);

	// Height of each brick, in pixels
	public static final double BRICK_HEIGHT = 8;

	// Offset of the top brick row from the top, in pixels
	public static final double BRICK_Y_OFFSET = 70;

	// Dimensions of the paddle
	public static final double PADDLE_WIDTH = 60;
	public static final double PADDLE_HEIGHT = 10;
	
	// Offset of the paddle up from the bottom 
	public static final double PADDLE_Y_OFFSET = 30;
	
	// Paddle moving speed
	public static final double PADDLE_SPEED = 6;

	// Radius of the ball in pixels
	public static final double BALL_RADIUS = 4;

	// The ball's vertical velocity.
	public static final double VELOCITY_Y = -8.0;

	// The ball's minimum and maximum horizontal velocity; the bounds of the
	// initial random velocity that you should choose (randomly +/-).
	public static final double VELOCITY_X_MIN = 1.0;
	public static final double VELOCITY_X_MAX = 4.0;

	// Animation delay or pause time between ball moves (ms)
	public static final double DELAY = 1000.0 / 60.0;

	// Number of turns 
	public static final int NTURNS = 3;
	
	public void run() {
		// Set the window's title bar text
		setTitle("CS 106A Breakout");
		
		/* You fill this in, along with any subsidiary methods */
		setup();
		addMouseListeners();
		
		// Starts the game
		this.pauseGame();
		while (this.lives > 0 && !this.gameWon) {
			ball.move();
			checkForColision();
			pause(DELAY);
		}
		
		// Ends the game
		this.gameOver();
	}
	
	// Sets the window size
	public void init() {
		this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
	}

	// Mouse moved event, moves the paddle	
	public void mouseMoved(MouseEvent e){
		this.paddleLocation = new GPoint(this.paddle.getLocation());
		if((e.getX() + PADDLE_WIDTH/2) < getWidth() && (e.getX() > PADDLE_WIDTH/2))
		{
			this.paddle.move((e.getX() - this.paddle.getWidth()/2) - paddleLocation.getX(), 0);
		}
	}
	
	// Mouse clicked, unpause the game
	public void mouseClicked(MouseEvent e) {
		if (this.gamePaused) {
			this.gamePaused = false;
		}
	}
	
	// Set up the game
	private void setup() {
		// Set game variables
		this.lives = NTURNS;
		this.gamePaused = true;
		this.gameWon = false;
		this.bricksLeft = NBRICK_COLUMNS * NBRICK_ROWS;
		
		// Set life display
		this.livesDisplay = new LivesDisplay(lives);
		add(this.livesDisplay);
		
		// Set window variables
		windowWidth = getWidth();
		windowHeight = getHeight();
		
		
		// Add paddle
		this.paddle = new Paddle((windowWidth - PADDLE_WIDTH) / 2, windowHeight - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
		// Paddle moving limits
		this.paddle.setMinXLimit(0);
		this.paddle.setMaxXLimit(windowWidth);
		add(this.paddle);
		
		
		// Add ball
		this.ball = new Ball(windowWidth / 2, windowHeight / 2, ballDiameter, ballDiameter);
		this.ball.setColor(Color.RED);
		add(this.ball);
		// Starts ball;
		this.ball.start(rand.nextDouble(VELOCITY_X_MIN, VELOCITY_X_MAX), VELOCITY_Y);
		
		// Add bricks
		addWall(NBRICK_ROWS, NBRICK_COLUMNS, BRICK_SEP);
	}
	
	// Pauses the game
	private void pauseGame() {
		// Checks if game is over
		if (this.gameWon || this.lives <= 0) {
			return;
		}
		
		// Sets variables
		GLabel pausedText = new GLabel("Click to start the game!");
		pausedText.setLocation((getWidth() / 2) - (pausedText.getWidth() / 2), getHeight()/2);
		add(pausedText);
		this.ball.setVisible(false);
		this.gamePaused = true;
		
		
		// Wait for enter keypress
		while(true) {
			pause(DELAY);
			
			// Countdown to unpause game
			if (!this.gamePaused) {
				// Move number to center of screen
				int i = 3;
				pausedText.setVisible(false);
				pausedText.setLabel(""+i);
				pausedText.setLocation((getWidth() / 2) - (pausedText.getWidth() / 2), getHeight()/2);
				
				for (;i > 0; i--) {
					pausedText.setLabel(""+i);					
					pause(200);
					pausedText.setVisible(true);
					pause(500);
					pausedText.setVisible(false);
				}
				
				break;
			}
		}
		
		// Unpause the game
		remove(pausedText);
		ball.start(rand.nextDouble(VELOCITY_X_MIN, VELOCITY_X_MAX), VELOCITY_Y);
		this.ball.setVisible(true);
	}
	
	private void gameOver() {
		ball.setVisible(false);
		String message = this.gameWon ? "You win!" : "You lose!";
		GLabel gameOver = new GLabel(message);
		gameOver.setLocation((this.windowWidth / 2) - (gameOver.getWidth() / 2), this.windowHeight / 2);
		add(gameOver);
	}

	// Checks if ball hit something
	private void checkForColision() {
		double ballX = ball.getX();
		double ballY = ball.getY();
		
		// element ball is hitting
		hit = checkHitPoints(ball);
		
		if (hit == null) {
			// Checks if ball did hit the walls
			if (ballX < 0 || ballX + ball.getWidth() > windowWidth) ball.bounce('x');
			if (ballY < 0) ball.bounce('y');
			if (ballY > windowHeight) this.removeLife();
		}
		else {
			// Checks if ball did hit the paddle
			if (hit == paddle)
				ball.bounce('y');
			else
				onBrickHit(hit);
		}
	}
	
	// Remove a brick from game
	private void onBrickHit(GObject brick) {
		if (brick.getClass() == GRect.class) {
			this.bricksLeft--;
			
			if(this.bricksLeft <= 0) {
				this.gameWon = true;
			}
			
			this.ball.bounce('y');
			remove(brick);
		}
	}
	
	// Remove a life
	private void removeLife() {
		this.lives--;
		this.livesDisplay.removeLife();
		this.ball.setLocation(windowWidth / 2, windowHeight / 2);
		this.pauseGame();
	}
	
	// Check 4 hitpoints on the ball
	private GObject checkHitPoints(Ball ball) {
		// Checks 1st hit point
		GObject hit = getElementAt(ball.getX(), ball.getY());
		if (hit != null) return hit;
		
		// Checks 2nd hit point
		hit = getElementAt(ball.getX() + ballDiameter, ball.getY());
		if (hit != null) return hit;
		
		// Checks 3rd hit point
		hit = getElementAt(ball.getX(), ball.getY() + ballDiameter);
		if (hit != null) return hit;
		
		// Checks 4th hit point
		hit = getElementAt(ball.getX() + ballDiameter, ball.getY() + ballDiameter);
		if (hit != null) return hit;
		
		// default
		return hit;
	}
	
	// Add the brick wall on canvas
	private void addWall(int rows, int cols, double step) {
		// Cursor that represent the position of each brick
		double cursorX;
		double cursorY;
		GRect brick;
		double wallWidth = (BRICK_WIDTH * NBRICK_COLUMNS) + (BRICK_SEP * (NBRICK_COLUMNS - 1));
		double xPadding = (getWidth() - wallWidth) / 2.0;
		Color brickColor;
		
		// Add brick wall
		for (int i = 0; i < rows; i++) {
			// Moves cursor to line start
			cursorY = BRICK_Y_OFFSET + (i * (BRICK_HEIGHT + BRICK_SEP)) + BRICK_SEP;
			
			// Select color for line
			brickColor = setBrickColor(i);
			
			// Add brick column
			for (int j = 0; j < cols; j++) {
				// Adjusts cursor
				cursorX = (j * (BRICK_WIDTH + BRICK_SEP)) + xPadding;
				
				// Create brick
				brick = new GRect(cursorX, cursorY, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				brick.setColor(brickColor);
				add(brick);
			}
		}
	}
	
	// Selects the color of the brick depending on the line
	private Color setBrickColor(int line) {
		int linesWithThisColor = NBRICK_ROWS / 5;
		
		if(line < linesWithThisColor) {
			return Color.RED;
		}
		else if (line < linesWithThisColor * 2) {
			return Color.ORANGE;
		}
		else if (line < linesWithThisColor * 3) {
			return Color.YELLOW;
		}
		else if(line < linesWithThisColor * 4) {
			return Color.GREEN;
		}
		else {
			return Color.CYAN;
		}
	}
	
	//INSTANCE VARIABLES
	private GPoint paddleLocation;
	private LivesDisplay livesDisplay;
	private Paddle paddle;
	private RandomGenerator rand = RandomGenerator.getInstance();
	private Ball ball;
	private double ballDiameter = BALL_RADIUS * 2;
	
	// Window size
	private double windowWidth;
	private double windowHeight;
	
	// Game variables
	private boolean gameWon;
	private boolean gamePaused;
	private int lives;
	private GObject hit;
	private int bricksLeft;
}


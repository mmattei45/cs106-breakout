package breakout;

import acm.graphics.*;

public class LivesDisplay extends GLabel {
	public LivesDisplay(int lifes) {
		super("Lives: " + lifes, 5, 20);
		this.lifes = lifes;
	}
	
	public void removeLife() {
		this.lifes--;
		this.setLabel("Lives: " + this.lifes);
	}
	
	private int lifes;
}

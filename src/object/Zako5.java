package object;

import java.awt.Image;

public class Zako5 extends Enemy {
	private final int INTERVAL = 25;
	private int count = INTERVAL;
	
	public Zako5(Image image,Image ic, char c) {
		super(image, ic, c);
		x = (int)(Math.random() * (app.getGUIManager().getWidth() - width));
		y = -height;
	}
	
	@Override
	public void move() {
		if (count < 0) {
			y += image.getHeight(app.getGUIManager());
			count = INTERVAL;
			super.move();
		}
		count--;
	}
	
}
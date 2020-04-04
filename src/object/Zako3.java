package object;

import java.awt.Image;

public class Zako3 extends Enemy {
	private double addy = 0;
	
	public Zako3(Image image,Image ic, char c) {
		super(image, ic, c);
		x = (int)(Math.random() * (app.getGUIManager().getWidth() - width));
		y = -height;
	}
	
	@Override
	public void move() {
		addy += 0.2;
		y += addy;
		super.move();
	}
	
}
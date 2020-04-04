package object;

import java.awt.Image;

public class Zako1 extends Enemy {

	public Zako1(Image image,Image ic, char c) {
		super(image, ic, c);
		x = (int)(Math.random() * (app.getGUIManager().getWidth() - width));
		y = -height;
	}
	
	@Override
	public void move() {
		y += 3;
		super.move();
	}
	
}
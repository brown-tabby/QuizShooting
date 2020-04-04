package object;

import java.awt.Image;

public class Zako6 extends Enemy {
	private final double SPEED = 0.2;
	private double addx = 3, addy = 3;

	public Zako6(Image image,Image ic, char c) {
		super(image, ic, c);
		x = (int)(Math.random() * (app.getGUIManager().getWidth() - width));
		y = -height;
	}
	
	@Override
	public void move() {
		if (x < app.getPlayer().getX()) {
			addx += SPEED;
		}
		if (x > app.getPlayer().getX()) {
			addx -= SPEED;
		}
		if (y < app.getPlayer().getY()) {
			addy += SPEED;
		}
		if (y > app.getPlayer().getY()) {
			addy -= SPEED + 0.3;
		}
		x += addx;
		y += addy;
		super.move();
	}
	
}
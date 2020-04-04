package object;

import java.awt.Image;

public class Zako4 extends Enemy {
	private final double SPEED = 0.2;
	private final int moveArea = 10;
	private double addx = 5;
	private int X;

	public Zako4(Image image,Image ic, char c) {
		super(image, ic, c);
		x = (int)(Math.random() * (app.getGUIManager().getWidth() - width));
		y = -height;
		X = x;
	}
	
	@Override
	public void move() {
		y += 4;
		if (x - X < -moveArea) {
			addx += SPEED;
		}
		if (x - X > moveArea) {
			addx -= SPEED;
		}
		x += addx;
		super.move();
	}
	
}
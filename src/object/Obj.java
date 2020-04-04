package object;
import java.awt.Graphics;
import java.awt.Image;

import pub.QuizShooting;

public abstract class Obj {
	protected static QuizShooting app;
	protected Image image;
	protected int x, y;
	protected int width, height;
	protected boolean dead;
	
	protected Obj(Image image) {
		this.image = image;
		width = image.getWidth(app.getGUIManager());
		height = image.getHeight(app.getGUIManager());
		dead = false;
	}
	
	public abstract void move();
	
	public boolean checkHit(Obj c) {
		if (c.x - width < x && x < c.x + c.width &&
				c.y - height < y && y < c.y + c.height) {
			return true;
		}
		return false;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void dead() {
		dead = true;
	}
	
	public static void setApp(QuizShooting appTemp) {
		app = appTemp;
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, x, y, app.getGUIManager());
	}
	
}

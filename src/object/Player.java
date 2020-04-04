package object;
import java.awt.Image;

import pub.HiddenFunction;
import pub.Key;
import pub.Materials;

public class Player extends Obj {
	private final byte SPEED = 6;
	private final byte B_TIME = 9;
	private Image iBullet1;
	private Image iBullet2;
	private byte bulletTime;
	private short lives;

	public Player(Image image) {
		super(image);
		iBullet1 = app.getMaterials().iArray[Materials.BULLET1];
		iBullet2 = app.getMaterials().iArray[Materials.BULLET2];
		x = (app.getGUIManager().getWidth() - width) / 2;
		y = app.getGUIManager().getHeight() - height - 38;
		lives = 5;
	}

	@Override
	public void move() {
		if (Key.left) {
			x -= SPEED;
			if (x < 0) x = 0;
		}
		if (Key.right) {
			x += SPEED;
			if (app.getGUIManager().getWidth() - width < x) x = app.getGUIManager().getWidth() - width;
		}
		if (Key.up) {
			y -= SPEED;
			if (y < 0) y = 0;
		}
		if (Key.down) {
			y += SPEED;
			if (app.getGUIManager().getHeight() - height < y) y = app.getGUIManager().getHeight() - height;
		}
		if(bulletTime > 0) bulletTime--; 
	}
	
	@Override
	public boolean checkHit(Obj c) {
		if (c instanceof Enemy) {
			if (super.checkHit(c)) {
				c.dead();
				dead();
				return true;
			}
		}
		return false;
	}
	
	public void shoot() {
		if (bulletTime <= 0 && !HiddenFunction.getShikaku()) {
			app.getMaterials().playSE(app.getMaterials().snd_Shoot);
			if (Key.z) {
				app.getCList().add(new Bullet1(iBullet1, this));
			} else if (Key.x) {
				app.getCList().add(new Bullet2(iBullet2, this));
			}
			bulletTime = B_TIME;
		}
		if (bulletTime <= 0 && HiddenFunction.getShikaku()) {
			app.getMaterials().playSE(app.getMaterials().snd_Shoot);
			if (Key.z) {
				for (int i = -5; i < 5; i++) {
					app.getCList().add(new Bullet1(iBullet1, this, i));
				}
			} else if (Key.x) {
				for (int i = -5; i < 5; i++) {
					app.getCList().add(new Bullet2(iBullet2, this, i));
				}
			}
			bulletTime = B_TIME;
		}
	}
	
	@Override
	public void dead() {
		dead = true;
		app.getMaterials().playSE(app.getMaterials().snd_PBang);
		lives--;
		if (lives >= 0) {
			dead = false;
		}
	}
	
	public short getLives() {
		return lives;
	}
	
	public void upLives() {
		lives++;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
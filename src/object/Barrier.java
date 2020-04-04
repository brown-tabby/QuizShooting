package object;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class Barrier extends Obj {
	private final int INTERVAL = (int)(3000 * 0.2);
	private int cnt = 0;
	private Player player;

	public Barrier(Image image, Player player) {
		super(image);
		this.player = player;
		x = player.getX();
		y = player.getY();
		width = width * 2;
		height = height * 2;
	}

	@Override
	public void move() {
		x = player.x - (player.width / 2);
		y = player.y - (player.height / 2);
	}
	
	@Override
	public boolean checkHit(Obj c) {
		if (cnt <= 0) {
			if (c instanceof Enemy) {
				if (super.checkHit(c)) {
					app.getMaterials().playSE(app.getMaterials().snd_Bang);
//					dead();
					c.dead();
					cnt = INTERVAL;
					return true;
				}
			}
		}
		cnt--;
		return false;
	}
	
	@Override
	public void draw(Graphics g) {
		if (cnt <= 0) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.YELLOW);
			AlphaComposite ac;
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
			g2.setComposite(ac);
			g2.fillOval(x, y, width, height);
			g2.setColor(Color.BLACK);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
	}
	
	@Override
	public boolean isDead() {
		if (player.isDead()) dead();
		return dead;
	}
	
}

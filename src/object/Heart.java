package object;

import java.awt.Image;

public class Heart extends Obj {

	public Heart(Image image) {
		super(image);
		x = (int)(Math.random() * (app.getGUIManager().getWidth() - width));
		y = -height;
	}

	@Override
	public void move() {
		y += 6;
		if (x < -width) dead();
		else if (app.getGUIManager().getWidth() < x) dead();
		else if (app.getGUIManager().getHeight() < y) dead();
	}
	
	@Override
	public boolean checkHit(Obj c) {
		if (c instanceof Player) {
			if (super.checkHit(c)) {
				app.getMaterials().playSE(app.getMaterials().snd_Bang);
				app.getPlayer().upLives();
				dead();
				return true;
			}
		}
		return false;
	}
	
}

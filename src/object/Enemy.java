package object;
import java.awt.Image;

import pub.Materials;

class Enemy extends Obj {

	private char chr;
	private Image iChr;
	
	protected Enemy(Image image, Image ic, char c) {
		super(image);
		chr = c;
		iChr = ic;
		
	}

	@Override
	public void move() {
		y += 1;
		if (x < -width) dead();
		else if (app.getGUIManager().getWidth() < x) dead();
		else if (app.getGUIManager().getHeight() < y) dead();
	}
	
	@Override
	public boolean checkHit(Obj c) {
		if (c instanceof Bullet) {
			if (super.checkHit(c)) {
				app.getMaterials().playSE(app.getMaterials().snd_Bang);
				dead();
				dropChar(c);
				c.dead();
				return true;
			}
		}
		return false;
	}
	
	private void dropChar(Obj c) {
		if (c.image.equals(app.getMaterials().iArray[Materials.BULLET2])) {
			for(int i = 0; i < app.getWList().size(); i++) {
				if (!app.getWList().get(i).isSet()) {
					app.getWList().get(i).setChar(new Char(iChr, x, y, chr));
					break;
				}
			}
		}
	}
	
	public void dropHint() {
		for(int i = 0; i < app.getWList().size(); i++) {
			if (!app.getWList().get(i).isSet()) {
				app.getWList().get(i).setChar(new Char(iChr, x, y, chr));
				break;
			}
		}
		dead();
	}
	
}
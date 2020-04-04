package object;
import java.awt.Image;

public class Char extends Obj {
	
	private char chr;
	private Waku waku;

	protected Char(Image image, int x, int y, char c) {
		super(image);
		this.x = x;
		this.y = y;
		chr = c;
		for(int i = 0; i < app.getWList().size(); i++) {
			if(!app.getWList().get(i).isSet()) {
				waku = app.getWList().get(i);
				waku.setChar(this);
				break;
			}
		}
		
	}

	@Override
	public void move() {
		if (waku != null) {
			int xx, yy;
			xx = waku.getX();
			yy = waku.getY();
			if (x != xx) {
				x += x < xx ? 3 : -3;
				x += x < xx ? 2 : -2;
				x += x < xx ? 1 : -1;
				if (x != xx) {
					x += x < xx ? 1 : -1;
				}
			}
			if (y != yy) {
				y += y < yy ? 7 : -7;
				y += y < yy ? 4 : -4;
				y += y < yy ? 2 : -2;
				if (y != yy) {
					y += y < yy ? 1 : -1;
				}
			}
		} else {
			dead();
		}
	}
	
	@Override
	public boolean checkHit(Obj c) {
		return false;
	}
	
	char getChar() {
		return chr;
	}

}
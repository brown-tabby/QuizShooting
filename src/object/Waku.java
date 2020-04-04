package object;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class Waku extends Obj {
	
	private boolean isSet;
	private Char character;
	
	public Waku(Image image, int xx) {
		super(image);
		x = width * xx;
		y = app.getGUIManager().getHeight() - height;
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		AlphaComposite ac;
		ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
		g2.setComposite(ac);
		g2.drawImage(image, x, y, app.getGUIManager());
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		if (character != null) {
			if (character.x != x || character.y != y) {
				character.move();
			}
			character.draw(g);
		}
	}
	
	public boolean isSet() {
		return isSet;
	}
	
	public void setChar(Char c) {
		if (c == null) {
			character = null;
			isSet = false;
		} else {
			character = c;
			isSet = true;
		}
	}
	
	public char getChar() {
		if(character != null)
			return character.getChar();
		return 0;
	}
	
	public int getX() {return x;}
	public int getY() {return y;}

	@Override
	public void move() {}
	
	@Override
	public boolean isDead() {
		return false;
	}
}

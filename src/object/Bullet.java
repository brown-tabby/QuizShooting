package object;
import java.awt.Image;

class Bullet extends Obj {
	private final int SPEED = 12;
	
	protected Bullet(Image image, Player player) {
		super(image);
		x = (player.x + player.width / 2) - (width / 2);
		y = (player.y + player.height / 2) - (height / 2);
	}

	@Override
	public void move() {
		y -= SPEED;
		if (x < -width || app.getGUIManager().getWidth() < x ||
				y < -height || app.getGUIManager().getHeight() < y)
			dead();
	}

}

class Bullet1 extends Bullet {

	protected Bullet1(Image image, Player player) {
		super(image, player);
	}
	
	protected Bullet1(Image image, Player player, int xx) {
		super(image, player);
		x = (player.x + player.width / 2) - (width / 2) + (player.width * xx);
		y = (player.y + player.height / 2) - (height / 2);
	}
	
}

class Bullet2 extends Bullet {

	protected Bullet2(Image image, Player player) {
		super(image, player);
	}
	
	protected Bullet2(Image image, Player player, int xx) {
		super(image, player);
		x = (player.x + player.width / 2) - (width / 2) + (player.width * xx);
		y = (player.y + player.height / 2) - (height / 2);
	}
	
	
}
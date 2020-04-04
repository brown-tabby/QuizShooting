package pub;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Key extends KeyAdapter {
	public static boolean left, right, up, down;
	public static boolean z,  x;
	static boolean space, enter; 
	static boolean c, p, h, q;
	private boolean isPressed, isPressed2; 
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_Z: z = true; break;
		case KeyEvent.VK_X: x = true; break;
		case KeyEvent.VK_C: c = true; break;
		case KeyEvent.VK_P: p = true; break;
		case KeyEvent.VK_H: h = true; break;
		case KeyEvent.VK_Q: q = true; break;
		case KeyEvent.VK_LEFT: left = true; break;
		case KeyEvent.VK_RIGHT: right = true; break;
		case KeyEvent.VK_UP: up = true; break;
		case KeyEvent.VK_DOWN: down = true; break;
		case KeyEvent.VK_SPACE: space = true; break;
		case KeyEvent.VK_ENTER: enter = true; break;
		
		case KeyEvent.VK_SHIFT: HiddenFunction.resetCommand(); break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if (c != 0) {
			HiddenFunction.command(c);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_Z: z = false; break;
		case KeyEvent.VK_X: x = false; break;
		case KeyEvent.VK_C: c = false; break;
		case KeyEvent.VK_P: p = false; break;
		case KeyEvent.VK_H: h = false; break;
		case KeyEvent.VK_Q: q = false; break;
		case KeyEvent.VK_LEFT: left = false; break;
		case KeyEvent.VK_RIGHT: right = false; break;
		case KeyEvent.VK_UP: up = false; break;
		case KeyEvent.VK_DOWN: down = false; break;
		case KeyEvent.VK_SPACE: space = false; break;
		case KeyEvent.VK_ENTER: enter = false; break;
		}
	}
	
	/**
	 * キーの押しっぱなしによる連続入力を防止する.
	 * 2つのキー入力を制御しようとすると競合するので
	 * 1ループにつき1つのキーしか制御できない
	 * @param key 制御するキー
	 * @return 入力時はtrue
	 */
	public boolean protectPressed(boolean key) {
		if (!key || isPressed) {			// キーが離された状態
			isPressed = true;
			if (key || isPressed2) {		// キーが押された状態
				isPressed2 = true;
				if (!key && isPressed2) {	// キーが離された状態 
					isPressed = false;
					isPressed2 = false;
					return true;
				}
			}
		}
		return false;
	}
	
}

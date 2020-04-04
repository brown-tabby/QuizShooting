package pub;

public class HiddenFunction {
	
	private static boolean high;
	private static boolean sikakunashi;
	private static boolean tamagura;
	private static boolean yesyes;
	private static String inputCommand = "";
	private static final String CLEAR_C = "allno";
	private static final String HIGH_C = "dio";
	private static final String SIKAKU_C = "sikaku";
	private static final String TAMAGURA_C = "tmgr";
	private static final String YES_C = "yesyes";
	
	public static void command(char chr) {
		if (!high) high = isCommand(HIGH_C);
		if (!sikakunashi) sikakunashi = isCommand(SIKAKU_C);
		if (!tamagura) tamagura = isCommand(TAMAGURA_C);
		if (!yesyes) yesyes = isCommand(YES_C);
		if (isCommand(CLEAR_C)) {
			resetCommands();
		}
		if(chr != 'z' && chr != 'x' &&
			chr != 'c' && chr != 'p' &&
			chr != 'h')
			inputCommand += chr;
	}
	
	public static void resetCommand() {
		inputCommand = "";
	}
	
	public static void resetCommands() {
		high = false;
		sikakunashi = false;
		tamagura = false;
		yesyes = false;
	}
	
	private static boolean isCommand(String command) {
		if(inputCommand.equals(command)) {
			return true;
		}
		return false;
	}
	
	public static boolean getHigh() {return high;}
	public static boolean getShikaku() {return sikakunashi;}
	public static boolean getTamagura() {return tamagura;}
	public static boolean getYes() {return yesyes;}
	
	/**
	 * shiftキーを押し、h,i,g,h入力でhighがtrueになる
	 * 静的変数[high]がtrueの時、発動可能
	 * enterキーで発動
	 * [stopSec]秒の間、プレイヤーのみが動ける.
	 * チェックメイトにはまったのだッ！
	 */
	public void the_world(QuizShooting app) {
		Thread t = new Thread() {
			@Override
			public void run() {
				final int stopMs = (int)(((10000 * (0.02 * 100)) / 2));
				int tCount;
				boolean stopFlag = false;
				
				int j = app.getGUIManager().getWidth();
				int k = j;
				for(tCount = 0; !stopFlag; tCount += 25) {
					app.getGUIManager().clearPaint();
					app.getPlayer().move();
					app.getGUIManager().drawAll();
					if (Key.q || tCount > stopMs) {
						stopFlag = true;
					}
					if (Key.z || Key.x) {
						app.getPlayer().shoot();
					}
					if (tCount < 4000) {
						app.getGUIManager().drawString("ザ・ワールドッ！時よ止まれ！", j, 300);
						j -= 7;
					}
					if (stopMs - tCount < 4000) {
						app.getGUIManager().drawString("そして時は動き出す・・・", k, 300);
						k -= 7;
					}
					app.getGUIManager().repaint();
					try {
						sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

package pub;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import object.*;

public class QuizShooting implements Runnable {
	private volatile Thread gameThread;
	private GUIManager guiManager;		// 描画を担当するクラス
	private Materials materials;		// 素材を持つクラス
	private HiddenFunction hidden;		// 隠し機能を持つクラス
	private Key key;					// キーリスナー
	private Player player;				// 操作キャラクター
	private Quiz quiz;					// クイズが記述されているファイルを持つクラス
	private ArrayList<Obj> cList;
	private WList wList;
	private PlayTime pTime;				// プレイタイムを持つクラス
	
	// 各場面において1度だけ実行したい処理がある場合、
	// isProcessedを使って処理済みかどうかを判定する
	private boolean isProcessed;
	private String playerName;
	private String stringScore;

	public static void main(String[] args) {
		new QuizShooting();
	}
	
	QuizShooting() {
		init();
		start();
	}
	
	/**
	 * メソッドテスト用コンストラクタ.
	 * @param foo 意味無し
	 */
	QuizShooting(int foo) {
		Quiz.runTest();
	}
	
	/**
	 * 初期処理.
	 */
	public void init() {
		materials = new Materials();
		materials.loadImage();
		materials.loadSound();
		
		guiManager = new GUIManager(this);
		
		key = new Key();
		guiManager.addKeyListener(key);
		
		Obj.setApp(this);
		cList = new ArrayList<Obj>();
		
		guiManager.setScene(GUIManager.Scene.INIT);
		
		hidden = new HiddenFunction();
	}
	
	/**
	 * メインスレッド.
	 */
	@Override
	public void run() {
		while(true) {
			guiManager.clearPaint();
			
			switch(guiManager.getScene()) {
			case INIT: gameInit(); break;
			case TITLE: gameTitle(); break;
			case CHOICE: gameChoice(); break;
			case LOAD: gameLoad(); break;
			case START: gameStart(); break;
			case QUIZ: gameQuiz(); break;
			case MAIN: gameMain(); break;
			case OVER: gameOver(); break;
			case CLEAR: gameClear(); break;
			case RANK: gameRank(); break;
			}
			
			guiManager.repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ゲームを初期化する.
	 */
	private void gameInit() {
		System.gc();
		
		cList = new ArrayList<Obj>(); 
		
		HiddenFunction.resetCommands();
		
		isPlayerChoice = false;
		
		guiManager.setScene(GUIManager.Scene.TITLE);
	}
	
	/**
	 * タイトル画面の処理.
	 */
	private void gameTitle() {
		guiManager.paintGameTitle();
		if (!isProcessed) {
			materials.stopMusic();
			materials.playMusic(materials.music_title, 0);
			isProcessed = true;
		}
		
		if (key.protectPressed(Key.space)) {
			guiManager.setScene(GUIManager.Scene.CHOICE);
			isProcessed = false;
		}
	}
	
	private boolean isPlayerChoice;
	/**
	 * ジャンル選択時の処理.
	 */
	private void gameChoice() {
		if  (!isPlayerChoice) {
			gamePlayerChoice();
		} else {
			gameChoiceGenre();
		}
	}
	
	private Key tmpKey;
	public int choiceIdx;
	private void gamePlayerChoice() {
		if (!isProcessed) {
			choiceIdx = 1;
			guiManager.setBackground(GUIManager.GAME_MAIN_C);
			guiManager.createChoiceP();
			tmpKey = new Key();
			isProcessed = true;
		}
		guiManager.paintPlayerChoice();
		
		if (key.protectPressed(Key.right)) {
			if (choiceIdx+1 < guiManager.choiceP.length) {
				choiceIdx++;
				guiManager.cursorP = guiManager.choiceP[choiceIdx];
			}
		}
		if (tmpKey.protectPressed(Key.left)) {
			if (choiceIdx-1 >= 0) {
				choiceIdx--;
				guiManager.cursorP = guiManager.choiceP[choiceIdx];
			}
		}
		
		if (Key.space) {
			isPlayerChoice = true;
		}
		
		if (isPlayerChoice) {
			isProcessed = false;
			tmpKey = null;
		}
	}
	
	private void gameChoiceGenre() {
		final int VALUE = 8;  // 一行に表示できるジャンルアイコン数
		if (!isProcessed) {
			char[] c = {'い', 'ろ', 'は', 'に', 'ほ', 'へ', 'と', 'ち'};  // ジャンルアイコン
			Quiz.QUIZ[] qAddress = {
					Quiz.QUIZ.ANIMAL,
					Quiz.QUIZ.COMICANIME,
					Quiz.QUIZ.ENTERTAINMENT,
					Quiz.QUIZ.GAME,
					Quiz.QUIZ.IT,
					Quiz.QUIZ.KANZI,
					Quiz.QUIZ.SPORTS,
					Quiz.QUIZ.TIPS,
//					Quiz.QUIZ.JOJO1,
//					Quiz.QUIZ.GINTAMA
			};
			int[] hints;
			int xx = 0, yy = 0;
			for (int i = 0; i < c.length;) {
				hints = materials.searchIndex(c[i]);
				cList.add(new ChoiceChar(materials.chars[hints[0]][hints[1]], qAddress[i], xx, yy));
				xx++;
				i++;
				if(i % VALUE == 0) {  // 一行に表示できる数を表示したら
					xx = 0;  // 左に戻り、
					yy++;    // 行を１段下げる
				}
			}
			
			player = new Player(materials.iPlayers[choiceIdx]);
			cList.add(player);
			
			isProcessed = true;
		}
		if (Key.z) {
			player.shoot();
		}
		moveAll();
		checkHitAll();
		guiManager.paintGameChoice();
		Obj c;
		for(int i = 0; i < cList.size(); i++) {
			c = cList.get(i);
			if (c instanceof ChoiceChar && c.isDead()) {
				quiz = new Quiz(((ChoiceChar)cList.get(i)).getQuiz());
				cList.clear();
				guiManager.setScene(GUIManager.Scene.LOAD);
				isProcessed = false;
				break;
			}
		}
	}
	
	/**
	 * ゲームのロード時の処理.
	 */
	private void gameLoad() {
		player = new Player(materials.iPlayers[choiceIdx]);
		cList.add(player);
		if (choiceIdx == 1) {
			cList.add(new Barrier(materials.iPlayers[choiceIdx], player));
		}
		
		guiManager.setScene(GUIManager.Scene.START);
	}
	
	/**
	 * ゲームのスタート時の処理.
	 */
	private void gameStart() {
		wList = new WList();
		pTime = new PlayTime();
		pTime.start();
		materials.stopMusic();
		materials.playMusic(materials.music_play, 0);
		guiManager.setScene(GUIManager.Scene.QUIZ);
	}
	
	
	private int eCount = 0;
	private int[] kanaHints;
	/**
	 * クイズ1問ごとの初期処理.
	 */
	private void gameQuiz() {
		if (quiz.getQuizNumSum() == 7) {
			materials.fadeoutMusic(materials.music_play);
			materials.playMusic(materials.music_play2, 2750);
		} else if (quiz.getQuizNumSum() == 10) {
			materials.fadeoutMusic(materials.music_play2);
			materials.playMusic(materials.music_last, 2750);
		}
		
		if (quiz.getQuizNumSum() != 11 &&
				quiz.getAnswer() != null) {
			kanaHints = new int[quiz.getAnswer().length()];
			for (int i = 0; i < kanaHints.length; i++) {
				kanaHints[i] = materials.searchIndex(quiz.getAnswer().charAt(i))[0];
			}
			
			wList.createWList();
			for(int j = 0; j < quiz.getAnswer().length(); j++) {
				wList.add(new Waku(materials.iArray[Materials.WAKU], j));
			}
		}
		guiManager.drawAll();

		guiManager.setScene(GUIManager.Scene.MAIN);
	}
	
	/**
	 * プレイ中
	 */
	private void gameMain() {
		
		if (player.isDead() && player.getLives() < 0) {
			gameEnd();
			guiManager.setScene(GUIManager.Scene.OVER);
		}
		if (quiz.getQuizNumSum() > 10) {
			gameEnd();
			guiManager.setScene(GUIManager.Scene.CLEAR);
		}
		
		dropEnemy();
		
		moveAll();
		
		guiManager.drawAll();
		
		checkHitAll();
		checkDeadAll();
		
		keyControls();
	}
	
	private final int INTERVAL = 15;
	/**
	 * 敵を出現させる.
	 */
	private void dropEnemy() {
		int r = (int)(Math.random() * 500);
		int r2 = (int)(Math.random() * (kanaHints.length-1)); // TODO: バグる
		int tes = kanaHints[r2];
		int r3 = (int)(Math.random() * materials.kana[tes].length);
		
		dropHintsEnemy();
		
		if (r % 50 == 0 && eCount <= 0) {
			addEnemy(0, r2, r3);
			eCount = 20;
		}
		
		if (r % 100 == 0 && eCount <= 3) {
			cList.add(new Heart(materials.iPlayers[choiceIdx]));
		}
		
		switch(quiz.getQuizNumSum()) {
		case 1:
			break;
		case 2:
		case 3:
			if (r % 60 == 0 && eCount <= 3) {
				addEnemy(3, r2, r3);
				eCount = INTERVAL;
			}
			break;
		case 4:
		case 5:
		case 6:
			if (r % 60 == 0 && eCount <= 3) {
				addEnemy(1, r2, r3);
				eCount = INTERVAL;
			}
			if (r % 70 == 0 && eCount <= 5) {
				addEnemy(3, r2, r3);
				eCount = INTERVAL;
			}
			break;
		case 7:
		case 8:
		case 9:
			if (r % 60 == 0 && eCount <= 3) {
				addEnemy(2, r2, r3);
				eCount = INTERVAL;
			}
			if (r % 70 == 0 && eCount <= 5) {
				addEnemy(3, r2, r3);
				eCount = INTERVAL;
			}
			if (r % 80 == 0 && eCount <= 7) {
				addEnemy(4, r2, r3);
				eCount = INTERVAL;
			}
			break;
		case 10:
			if (r % 60 == 0 && eCount <= 3) {
				addEnemy(2, r2, r3);
				eCount = INTERVAL;
			}	
			if (r % 70 == 0 && eCount <= 5) {
				addEnemy(4, r2, r3);
				eCount = INTERVAL;
			}
			if (r % 80 == 0 && eCount <= 7) {
				addEnemy(5, r2, r3);
				eCount = INTERVAL;
			}
			break;
		}
		eCount--;
	}
	
	/**
	 * 敵をcListに追加する.
	 * @param i enemy[]のインデックス
	 * @param j kanaHints[]のインデックス
	 * @param k kanaHints[][]のインデックス
	 */
	private void addEnemy(int i, int j, int k) {
		Image iEnemy = ((Image[][])materials.enemy[i])[kanaHints[j]][k];
		Image iChr = materials.chars[kanaHints[j]][k];
		char chr = materials.kana[kanaHints[j]][k];
		switch(i) {
		case 0 : cList.add(new Zako1(iEnemy, iChr, chr)); break;
		case 1 : cList.add(new Zako2(iEnemy, iChr, chr)); break;
		case 2 : cList.add(new Zako3(iEnemy, iChr, chr)); break;
		case 3 : cList.add(new Zako4(iEnemy, iChr, chr)); break;
		case 4 : cList.add(new Zako5(iEnemy, iChr, chr)); break;
		case 5 : cList.add(new Zako6(iEnemy, iChr, chr)); break;
		}
	}
	
	private Zako1 createHintsEnemy() {
//		int rand = (int)(Math.random() * 5);
		int hint = 0;
		for (int i = 0; i < wList.size(); i++) {
			if (!wList.get(i).isSet()) {
				hint = i;
				break;
			}
		}
		int[] hints = materials.searchIndex(quiz.getAnswer().charAt(hint));
		Image iEnemy = ((Image[][])materials.enemy[0])[hints[0]][hints[1]];
		Image iChr = materials.chars[hints[0]][hints[1]];
		char chr = materials.kana[hints[0]][hints[1]];
		return new Zako1(iEnemy, iChr, chr);
	}
	
	private int hintsCount = 250;
	/**
	 * クイズの答えの埋まってない１番左の文字を持った敵を出現させる.
	 */
	private void dropHintsEnemy() {
		if (hintsCount < 0) {
			cList.add(createHintsEnemy());
			hintsCount = 250;
		}
		hintsCount--;
	}
	
	private void moveAll() {
		Obj c;
		for(int i = 0; i < cList.size(); i++) {
			c = cList.get(i);
			c.move();
		}
	}
	
	private void checkHitAll() {
		Obj c1, c2;
		for(int i = 0; i < cList.size(); i++) {
			c1 = cList.get(i);
			for(int j = 0; j < cList.size(); j++) {
				c2 = cList.get(j);
				c1.checkHit(c2);
			}
		}
	}
	
	private void checkDeadAll() {
		Obj c;
		for(int i = 0; i < cList.size(); i++) {
			c = cList.get(i);
			if (c.isDead()) cList.remove(i);
		}

	}
	
	private boolean removeFlag, skipFlag, hintFlag;
	/**
	 * キー操作を判別する.
	 */
	private void keyControls() {
		if (HiddenFunction.getHigh() && Key.enter) {	// 隠しコマンド
			hidden.the_world(this);
		}
		if (HiddenFunction.getTamagura() && Key.space && Key.c && Key.q) {	// 隠しコマンド
			while(player.getLives() < 999) {
				player.upLives();
			}
		}
		if (Key.c || removeFlag) {
			if (!removeFlag) {
				removeWListChar();
				removeFlag = true;
			}
			if (!Key.c) removeFlag = false;
		}
		if (Key.z || Key.x) {
			player.shoot();
		}
		if (Key.p || skipFlag) {
			if (!skipFlag && quiz.getQuizNumSum() != 10) {
				for (int i = 0; i < 2; i++)	// スキップ時playerの残機を2消費する
					player.dead();
				quiz.nextQuiz();
				guiManager.setScene(GUIManager.Scene.QUIZ);
				skipFlag = true;
			}
			if (!Key.p) skipFlag = false;
		}
		if (Key.h || hintFlag) {
			if (!hintFlag) {	// :TODO 面白くなるやつ
				createHintsEnemy().dropHint();
				player.dead();
				hintFlag = true;
			}
			if (!Key.h) hintFlag = false;
		}
		if (Key.space) {
			gameCheck();
		}
	}
	
	/**
	 * クイズの回答の判定.
	 */
	private void gameCheck() {
		if (wList.get(wList.size() - 1).getChar() != 0) {
			boolean b = false;
			for (int i = 0; i < wList.size(); i++) {
				if (wList.get(i).getChar() != quiz.getAnswer().charAt(i) && !HiddenFunction.getYes()) {
					b = false;
					materials.playSE(materials.snd_Bad);
					player.dead();
					quiz.plusBadCount();
					if (quiz.getBadCount() >= 3 && quiz.getQuizNumSum() != 10) {
						quiz.nextQuiz();
						guiManager.setScene(GUIManager.Scene.QUIZ);
					}
					break;
				}
				if (i == wList.size() - 1) {
					b = true;
					materials.playSE(materials.snd_Good);
					quiz.nextQuiz();
					player.upLives();
					guiManager.setScene(GUIManager.Scene.QUIZ);
					break;
				}
			}
			guiManager.drawCheck(b);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			removeAllWListChar();
		}
	}
	
	/**
	 * 1ゲームを終了する処理.
	 */
	private void gameEnd() {
		materials.stopMusic();
		pTime.stop();
		guiManager.setBackground(GUIManager.SYSTEM_C);
	}
	
	/**
	 * ゲームオーバー時の処理.
	 */
	private void gameOver() {
		guiManager.paintGameOver();
		if (!isProcessed) {
			materials.playSE(materials.music_gameover);
			isProcessed = true;
		}
		if (key.protectPressed(Key.space)) {
			guiManager.setScene(GUIManager.Scene.INIT);
			isProcessed = false;
		}
	}
	
	/**
	 * ゲームクリア時の処理.
	 */
	private void gameClear() {
		guiManager.paintGameClear();
		if (!isProcessed) {
			materials.playMusic(materials.music_clear, 0);
			isProcessed = true;
		}
		if (key.protectPressed(Key.space)) {
			guiManager.setScene(GUIManager.Scene.RANK);
			isProcessed = false;
		}
	}
	
	private void gameRank() {
		if (!isProcessed) {
			A: while(true) {
				playerName = JOptionPane.showInputDialog(null, "プレイヤーネームを入力してください。(10文字以内)", "名無し"+quiz.getPlayerNum());
				if (playerName == null) continue A;
//				System.out.println(playerName);
				for(int i = 0; i < playerName.length(); i++) {
					if (playerName.charAt(i) == ' ') continue A;
				}
				if (playerName != "" && playerName.length() <= 10) {
					break;
				}
			}
			stringScore = playerName + ": " + getScore();
			quiz.insertRank(stringScore);
			quiz.writeRank(playerName, getScore(), getStringTime());
			isProcessed = true;
		}
		
		guiManager.paintRank(playerName, stringScore);
		
		if (key.protectPressed(Key.space)) {
			guiManager.setScene(GUIManager.Scene.INIT);
			isProcessed = false;
		}
	}
	
	/**
	 * ゲームスレッドがnullの時、
	 * インスタンスを生成しゲームスレッドを開始する.
	 */
	private void start() {
		if (gameThread == null) {
			gameThread = new Thread(this);
			gameThread.start();
		}
	}
	
	/**
	 * プレイタイムの文字列を返す.
	 * フォーマットは 分:秒.ミリ秒
	 * 例) 1分23秒45ミリ秒 -> 01:23.450
	 * @return プレイタイムの文字列
	 */
	public String getStringTime() {
		return (String.format("%1$02d", (pTime.getMsTime() / 1000) / 60)) + ":" +
				(String.format("%1$02d", (pTime.getMsTime() / 1000) % 60)) + "." +
				(String.format("%1$03d", (pTime.getMsTime() % 1000)));
	}
	
	/**
	 * スコアを返す.
	 * スコアの計算は プレイヤーの残機数*1000
	 * @return score
	 */
	public int getScore() {
		return player.getLives() * 1000;
	}
	
	/**
	 * wListの最後の要素が持つ文字を１つ消す.
	 */
	private void removeWListChar() {
		Waku waku;
		for (int i = wList.size() - 1; i >= 0; i-- ) {
			waku = wList.get(i);
			if (waku.isSet()) {
				waku.setChar(null);
				break;
			}
		}
	}
	
	/**
	 * wListの要素が持つ文字すべてを消す.
	 */
	private void removeAllWListChar() {
		for(int i = 0; i < wList.size(); i++) {
			wList.get(i).setChar(null);
		}
	}
	
	public GUIManager getGUIManager() {return guiManager;}
	public Player getPlayer() {return player;}
	public ArrayList<Obj> getCList() {return cList;}
	public WList getWList() {return wList;}
	public Materials getMaterials() {return materials;}
	public Quiz getQuiz() {return quiz;}
	
}
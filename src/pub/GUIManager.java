package pub;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import object.Obj;
import object.Waku;

public class GUIManager extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private QuizShooting app;
	private Image offImage;			// 描画用イメージ
	private Graphics imageGraph;	// offImageから取得したグラフィクス
	private Image checkPanelOffImage;
	private Graphics checkPanelGraph;
//	private JPanel panel;
	private Scene scene;			// シーン格納変数
	
	private Image title;
	private Image manual;
	private Image check;
	
	public static final Color SYSTEM_C = Color.getHSBColor(0, 0, 0.9f);	// 薄いグレー
	public static final Color GAME_MAIN_C = Color.getHSBColor(0.51f, 0.30f, 1);	// 薄い水色
	private final Font QUIZ_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);
	private final Font SYSTEM_BOLDFONT = new Font(Font.MONOSPACED, Font.BOLD, 25);
	private final Font SYSTEM_FONT = new Font(Font.MONOSPACED, Font.ITALIC, 20);
	private final Font BIG_FONT = new Font(Font.MONOSPACED, Font.ITALIC, 60);
	enum Scene {
		INIT, TITLE, CHOICE, LOAD, START, MAIN, QUIZ,
		OVER, CLEAR, RANK
	}
	
	GUIManager(QuizShooting app) {
		super("クイズシューティング");
		this.app = app;
		setFrameConfig();
		
		createCheckPanel();
		
		super.setBackground(GAME_MAIN_C);
		
		title = app.getMaterials().title.getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_DEFAULT);
		
		createManual();
		
		// 謎
		requestFocus();
		requestFocusInWindow();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void setFrameConfig() {
		// ハードの画面のサイズを取得する
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle desktopBounds = env.getMaximumWindowBounds();
		
		int w = (int)desktopBounds.getWidth() / 2, h = (int)desktopBounds.getHeight();
		
		setLocation((desktopBounds.width / 3) - (w / 2), 0);
		setSize(w, h);
		setPreferredSize(new Dimension(w, h));
		getRootPane().setDoubleBuffered(true);
		super.setBackground(SYSTEM_C);
		
		pack();
		
		offImage = createImage(getWidth(), getHeight());
		imageGraph = offImage.getGraphics();
	}
	
//	@SuppressWarnings("unused")
	private void createCheckPanel() {
		Dimension d = new Dimension(getWidth() - 80, getHeight() - getInsets().top - 80);
		checkPanelOffImage = createImage(d.width, d.height);
		
		check = app.getMaterials().check.getScaledInstance(d.width, d.height, BufferedImage.SCALE_SMOOTH);
		checkPanelGraph = checkPanelOffImage.getGraphics();
		checkPanelGraph.drawImage(check, 0, 0, this);
	}
	
	public void drawCheck(boolean b) {
		String s;
		Color c;
		if (b) {
			s = "○";
			c =Color.RED;
		} else {
			s = "×";
			c = Color.BLUE;
		}
		imageGraph.setFont(new Font(Font.SERIF, Font.BOLD, 700));
		imageGraph.drawImage(checkPanelOffImage, 40, getInsets().top + 40, this);
		imageGraph.setColor(c);
		imageGraph.drawString(s, 40 + ((getWidth() / 2) - 240), getInsets().top + 40 + ((getHeight() / 2 + 200)) );
		repaint();
		imageGraph.setColor(Color.BLACK);
	}
	
	private Image manualOffImage;
	private void createManual() {
		Graphics g;
		JFrame frame = new JFrame("マニュアル") {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				g.drawImage(manualOffImage, 0, getInsets().top, this);
			}
			
			@Override
			public void update(Graphics g) {
				super.update(g);
				paint(g);
			}
		};
		frame.setSize(new Dimension((int)(getWidth() / 2 * 1.5), getHeight()));
		frame.setLocation(new Point((int)getLocation().getX() + getWidth(),0));
		
		manual = app.getMaterials().manual.getScaledInstance(frame.getWidth(), frame.getHeight() - getInsets().top, BufferedImage.SCALE_SMOOTH);
		
		frame.setVisible(true);
		
		manualOffImage = frame.createImage(frame.getWidth() - getInsets().right - getInsets().left
				, frame.getHeight() - getInsets().top);
		g = manualOffImage.getGraphics();
		
		g.drawImage(manual, frame.getInsets().left, 0, frame);
		frame.repaint();
	}
	
	public void paintGameTitle() {
		imageGraph.drawImage(title, 0, 0, this);
		imageGraph.setFont(SYSTEM_FONT);
		imageGraph.drawString("Press SpaceKey.", getWidth() / 2 - 80, getHeight() / 4 * 3);
//		imageGraph.drawImage(checkPanel.createImage(checkPanel.getWidth(), checkPanel.getHeight()), 30, getInsets().top + 30, this);
	}
	
	public Point[] choiceP = new Point[3];
	public void createChoiceP() {
		for (int i = 0; i < choiceP.length; i++) {
			int x = (app.getGUIManager().getWidth() / 60 * 7) + ((app.getGUIManager().getWidth() / 50 * 6) * (i * 3) );
			int y = app.getGUIManager().getHeight() / 50 * 17;
			choiceP[i] = new Point(x, y);
		}
		cursorP = choiceP[1];
	}
	
	public Point cursorP;
	public void paintPlayerChoice() {
		imageGraph.setFont(QUIZ_FONT);
		imageGraph.drawString("操作するキャラクターを選んでください。", getWidth() / 2 - 145, getHeight() / 50 * 10);
		for (int i = 0; i < choiceP.length; i++) {
			Image img = app.getMaterials().iPlayers[i];
			imageGraph.drawImage(img, choiceP[i].x, choiceP[i].y, this);
		}
		imageGraph.setFont(BIG_FONT);
		BasicStroke superwideStroke = new BasicStroke(5.0f);
		Graphics2D g2 = (Graphics2D)imageGraph;
		g2.setStroke(superwideStroke);
		g2.setColor(Color.RED);
		Image img = app.getMaterials().iPlayers[app.choiceIdx];
		g2.drawRect(cursorP.x, cursorP.y, img.getWidth(this), img.getHeight(this));
		g2.setColor(Color.BLACK);
		}
	
	public void paintGameChoice() {
		imageGraph.setFont(QUIZ_FONT);
		imageGraph.drawString("クイズのジャンルを選んでください。", getWidth() / 2 - 135, getHeight() / 50 * 10);
		imageGraph.drawString("い: アニマル", getWidth() / 2 - 130, getHeight() / 50 * 30);
		imageGraph.drawString("ろ: コミック＆アニメ", getWidth() / 2 - 130, getHeight() / 50 * 32);
		imageGraph.drawString("は: 芸能", getWidth() / 2 - 130, getHeight() / 50 * 34);
		imageGraph.drawString("に: ゲーム", getWidth() / 2 - 130, getHeight() / 50 * 36);
		imageGraph.drawString("ほ: IT", getWidth() / 2 - 130, getHeight() / 50 * 38);
		imageGraph.drawString("へ: 漢字", getWidth() / 2 - 130, getHeight() / 50 * 40);
		imageGraph.drawString("と: スポーツ", getWidth() / 2 - 130, getHeight() / 50 * 42);
		imageGraph.drawString("ち: 雑学", getWidth() / 2 - 130, getHeight() / 50 * 44);
		drawChoice();
	}
	
	private void drawCLists() {
		Obj c;
		for(int i = 0; i < app.getCList().size(); i++) {
			c = app.getCList().get(i);
			Graphics2D g2 = (Graphics2D)imageGraph;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			c.draw(g2);
		}
	}
	
	private void drawWLists() {
		Waku w;
		for(int i = 0; i < app.getWList().size(); i++) {
			w = app.getWList().get(i);
			w.draw(imageGraph);
		}
	}
	
	void drawAll() {
		drawCLists();
		drawWLists();
		
		imageGraph.setFont(QUIZ_FONT);
		String subQuiz = "Q" + app.getQuiz().getQuizNumSum() + ": " + app.getQuiz().getQuiz();
		int x = 0;
		int y = getInsets().top;
		int i;
		for (i = 1; subQuiz.length() > getWidth() / 20; i++){
			imageGraph.drawString(subQuiz.substring(0, getWidth() / 20), 0, y + (i * 25));
			subQuiz = subQuiz.substring(getWidth() / 20, subQuiz.length());
		}
		imageGraph.drawString(subQuiz, 0, y + (i * 25));
		imageGraph.setFont(SYSTEM_FONT);
		Image tmpFPlayer = app.getMaterials().iPlayers[app.choiceIdx];
		x = getWidth() - ( tmpFPlayer.getWidth(this) + 80);
		y = getHeight() - (tmpFPlayer.getHeight(this) + 10);
		imageGraph.drawImage(tmpFPlayer, x, y, this);
		imageGraph.drawString(" x " + app.getPlayer().getLives(), x + tmpFPlayer.getWidth(this), y + (tmpFPlayer.getHeight(this) / 2 + 15));
	}
	
	private void drawChoice() {
		drawCLists();
	}
	
	public void paintGameOver() {
		drawAll();
		imageGraph.setFont(BIG_FONT);
		imageGraph.drawString("GAME OVER...", getWidth() / 2 - 170, getHeight() / 4);
		imageGraph.setFont(SYSTEM_FONT);
		imageGraph.drawString("Press SpaceKey.", getWidth() / 2 - 80, getHeight() / 4 * 3);
	}
	
	public void paintGameClear() {
		imageGraph.setFont(BIG_FONT);
		imageGraph.setColor(Color.YELLOW);
		String s = "Congratulations!!";
		imageGraph.drawString(s, getWidth() / 2 - (s.length() * 18), getHeight() / 4);
		imageGraph.setColor(Color.BLACK);
		imageGraph.setFont(SYSTEM_FONT);
		s = "Clear Time: " + app.getStringTime();
		imageGraph.drawString(s, getWidth() / 2 - 130, getHeight() / 30 * 14);
		s = "Score: " + (app.getScore());
		imageGraph.drawString(s, getWidth() / 2 - 70, getHeight() / 30 * 15);
		s = "Game clear!";
		imageGraph.drawString(s, getWidth() / 2 - (s.length() * 8), getHeight() / 30 * 22);
		s = "Thank you for playing!";
		imageGraph.drawString(s, getWidth() / 2 - (s.length() * 7), getHeight() / 30 * 23);
		s = "By Bteam";
		imageGraph.drawString(s, getWidth() / 2 - (s.length() * 8), getHeight() / 30 * 24);
		s = "Press SpaceKey.";
		imageGraph.drawString(s, getWidth() / 2 - (s.length() * 7), getHeight() / 20 * 18);
	}
	
	public void paintRank(String name, String score) {
		imageGraph.setFont(BIG_FONT);
		String s = "RANKING";
		imageGraph.drawString(s, getWidth() / 2 - (s.length() * 17), (getHeight() / 30) * 5);
		
		imageGraph.setFont(SYSTEM_FONT);
		
		String[] rank = new String[app.getQuiz().getRanking().length];
		for (int i = 0; i < rank.length; i++) {
			rank[i] = app.getQuiz().getRanking()[i];
		}
		
		for (int i = 0; i < rank.length && i < 10;) {
			if (i == 0) {
				rank[i] = i+1 + "st  " + rank[i];
			} else if(i == 1) {
				rank[i] = i+1 + "nd  " + rank[i];
			} else if(i == 2) {
				rank[i] = i+1 + "rd  " + rank[i];
			}else {
				rank[i] = i+1 + "th  " + rank[i];
			}
			imageGraph.drawString(rank[i], getWidth() / 2 - 150, (getHeight() / 30 * 10) + ((getHeight() / 30 * 2) * i));
			i++;
		}
		imageGraph.drawString(score, getWidth() / 2 - (score.length() * 6), (getHeight() / 30 * 7));
	}
	
	public void setScene(Scene scene) {this.scene = scene;}
	public Scene getScene() {return scene;}
	
	public void clearPaint() {
		imageGraph.clearRect(0, 0, getWidth(), getHeight());
	}
	
	@Override
	public void setBackground(Color c) {
		super.setBackground(c);
		createGraphics();
	}
	
	private void createGraphics() {
		offImage = createImage(getWidth(), getHeight());
		if (offImage != null) {
			imageGraph = offImage.getGraphics();
		}
	}
	
	public void drawString(String str, int x , int y) {
		imageGraph.setFont(SYSTEM_BOLDFONT);
		imageGraph.drawString(str, x, y);
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(offImage, 0, 0, this);
	}
	
	@Override
	public void update(Graphics g) {
		super.update(g);
		paint(g);
	}
}

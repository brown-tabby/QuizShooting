package pub;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class Materials {
	public Image[] iArray = new Image[5];
	public Image[] iPlayers = new Image[3];
	Image fPlayer;
	Image barrier;
	BufferedImage title;
	BufferedImage check;
	BufferedImage manual;
	public Clip snd_Shoot;
	public Clip snd_Bang;
	public Clip snd_PBang;
	Clip snd_Good;
	Clip snd_Bad;
	Clip music_title, music_play, music_play2, music_last,
	music_gameover, music_clear;
	public static final int BULLET1 = 1, BULLET2 = 2,
			WAKU = 3;
	
	public char[][] kana = {
			{'あ','い','う','え','お'},
			{'か','き','く','け','こ'},
			{'さ','し','す','せ','そ'},
			{'た','ち','つ','て','と'},
			{'な','に','ぬ','ね','の'},
			{'は','ひ','ふ','へ','ほ'},
			{'ま','み','む','め','も'},
			{'や','ゆ','よ'},
			{'ら','り','る','れ','ろ'},
			{'わ','を','ん'},
			{'ゃ', 'ゅ', 'ょ', 'っ'},
			{'ー','゛','゜'}
	};
	
	public Object[] enemy = new Object[6];

	public Image[][] chars = {
			{ null, null, null, null, null},
			{ null, null, null, null, null},
			{ null, null, null, null, null},
			{ null, null, null, null, null},
			{ null, null, null, null, null},
			{ null, null, null, null, null},
			{ null, null, null, null, null},
			{ null, null, null},
			{ null, null, null, null, null},
			{ null, null, null},
			{ null, null, null, null},
			{ null, null, null}
	};
	
	private volatile Thread sound;
	
	void loadImage() {
		try {
			String address = "/image/enemy/enemy1_あ.png";
			BufferedImage bi = ImageIO.read(getClass().getResource(address));
			iPlayers[0] = bi.getScaledInstance(50, 50, BufferedImage.SCALE_DEFAULT);
			address = "/image/player.png";
			bi = ImageIO.read(getClass().getResource(address));
			iPlayers[1] = bi.getScaledInstance(50, 50, BufferedImage.SCALE_DEFAULT);
			address = "/image/enemy/enemy6_あ.png";
			bi = ImageIO.read(getClass().getResource(address));
			iPlayers[2] = bi.getScaledInstance(50, 50, BufferedImage.SCALE_DEFAULT);
			
			address = "/image/。/。4.png";
			bi = ImageIO.read(getClass().getResource(address));
			iArray[BULLET1] = bi.getScaledInstance(20, 20, BufferedImage.SCALE_DEFAULT);
			address = "/image/。/。2.png";
			bi = ImageIO.read(getClass().getResource(address));
			iArray[BULLET2] = bi.getScaledInstance(20, 20, BufferedImage.SCALE_DEFAULT);
			address = "/image/wakuB.png";
			iArray[WAKU] = ImageIO.read(getClass().getResource(address));
			address = "/image/title.png";
			title = ImageIO.read(getClass().getResource(address));
			address = "/image/check.png";
			check = ImageIO.read(getClass().getResource(address));
			address = "/image/manual.png";
			manual = ImageIO.read(getClass().getResource(address));
			address = "/image/player_front.png";
			bi = ImageIO.read(getClass().getResource(address));
			fPlayer = bi.getScaledInstance(50, 50, BufferedImage.SCALE_DEFAULT);
			address = "/image/。/。3.png";
			bi = ImageIO.read(getClass().getResource(address));
			barrier = bi.getScaledInstance(70, 70, BufferedImage.SCALE_DEFAULT);
			
			for(int i = 1; i <= enemy.length; i++) {
				Image[][] enemys = {
						{ null, null, null, null, null},
						{ null, null, null, null, null},
						{ null, null, null, null, null},
						{ null, null, null, null, null},
						{ null, null, null, null, null},
						{ null, null, null, null, null},
						{ null, null, null, null, null},
						{ null, null, null},
						{ null, null, null, null, null},
						{ null, null, null},
						{ null, null, null, null},
						{ null, null, null}
				};
				for (int j = 0; j < enemys.length; j++) {
					for (int k = 0; k < enemys[j].length; k++) {
						address = "/image/enemy/enemy" + i + "_" + kana[j][k] + ".png";
						bi = ImageIO.read(getClass().getResource(address));
						enemys[j][k] = bi.getScaledInstance(80, 80, BufferedImage.SCALE_DEFAULT);
						if (i == 4) {
							chars[j][k] = bi.getSubimage(13, 9, 13, 13).getScaledInstance(50, 50, Image.SCALE_DEFAULT);
						}
					}
				}
				enemy[i-1] = (Image[][])enemys.clone();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	void loadSound() {
		snd_Shoot = readSoundFile("/sound/shoot3.au");
		snd_Bang = readSoundFile("/sound/bang2.au");
		snd_PBang =readSoundFile("/sound/bang4.au");
		snd_Good = readSoundFile("/sound/good.au");
		controlSoundVolume(snd_Good, 2);
		snd_Bad = readSoundFile("/sound/bad.au");
		controlSoundVolume(snd_Bad, 2);
		music_title = readSoundFile("/sound/music_title.au");
		music_play = readSoundFile("/sound/music.au");
		music_play2 = readSoundFile("/sound/music2.au");
		music_last = readSoundFile("/sound/music_last.au");
		music_gameover = readSoundFile("/sound/music_gameover.au");
		music_clear = readSoundFile("/sound/music_clear.au");
	}
	
	private Clip readSoundFile(String address) {
		Clip clip = null;
		try {
			AudioFormat format = null;
			DataLine.Info info = null;
			URL url = getClass().getResource(address);
			format = AudioSystem.getAudioFileFormat(url).getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(AudioSystem.getAudioInputStream(url));
		} catch(Exception e){
			e.printStackTrace();
		}
		return clip;
	}
	
	public void playSE(Clip clip) {
		sound = new Thread() {
			@Override
			public void run() {
				clip.stop();
				clip.setMicrosecondPosition(0);
				clip.start();
			}
		};
		sound.start();
	}
	
	void playMusic(Clip clip, int waitMs) {
		sound = new Thread() {
			@Override
			public void run() {
				try {
					sleep(waitMs);
					clip.loop(Clip.LOOP_CONTINUOUSLY);
					clip.setMicrosecondPosition(0);
					clip.start();
				} catch(InterruptedException e) {
					
				}
			}
		};
		sound.start();
	}
	
	void fadeoutMusic(Clip clip) {
		sound = new Thread() {
			@Override
			public void run() {
				try {
					for (double i = 1; i > 0.1; i -= 0.01) {
						controlSoundVolume(clip, i);
						sleep(15);
					}
					sleep(750);
					clip.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		sound.start();
	}
	
	void stopMusic() {
		music_title.stop();
		controlSoundVolume(music_play, 1);
		music_play.stop();
		controlSoundVolume(music_play2, 1);
		music_play2.stop();
		music_last.stop();
		music_gameover.stop();
		music_clear.stop();
	}
	
	private void controlSoundVolume(Clip clip, double linearScalar) {
		FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		control.setValue((float)Math.log10(linearScalar) * 20);
	}
	
	public int[] searchIndex(char c) {
		int i, j = 0;
		A: for (i = 0; i < kana.length; i++) {
			for (j = 0; j < kana[i].length; j++) {
				if (c == kana[i][j]) {
					break A;
				}
			}
		}
		
		int[] array = {i, j};
		return array;
	}
	
}

/*
	public void testPlayClip() {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream in = AudioSystem.getAudioInputStream(new File("test.wav"));
			clip.open(in);
			FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			controlByLinearScalar(control, 0.5); // 50%の音量で再生する
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void controlByLinearScalar(FloatControl control, double linearScalar) {
		control.setValue((float)Math.log10(linearScalar) * 20);
	}
*/
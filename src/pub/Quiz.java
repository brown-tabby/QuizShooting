package pub;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

public class Quiz {
	private Properties quizProp;
	private Properties rankProp;
	private ArrayList<String[]> quiz = new ArrayList<String[]>();
	private String[] ranking;
	private String rankAddress;
	private int quizNum;
	private int quizNumSum;
	private int badCount;
	private int playerNum;
	
	public static final String[] CMD = {"jar", "uvf", "QuizShooting_1_2.jar", ""};
	public static final String[] CMD2 = {"rm", "-r", ""};
	
	public enum QUIZ {
		ANIMAL("animal"),
		COMICANIME("comicanime"),
		ENTERTAINMENT("entertainment"),
		GAME("game"),
		IT("it"),
		KANZI("kanzi"),
		SPORTS("sports"),
		TIPS("tips"),
		JOJO1("jojo1"),
		GINTAMA("gintama");
		
		private String address;
		QUIZ(String genre) {
			this.address = "/quiz/" + genre + ".properties";
		}
		
		String getAddress() {
			return address;
		}
	}
	
	Quiz(QUIZ choice) {
		loadQuiz(choice.getAddress());
		
		quizNumSum++;
		setRandomNum();
		
		rankAddress = choice.getAddress();
		rankAddress = rankAddress.replace("quiz", "ranking");
		rankAddress = rankAddress.replace(".properties", "Rank.properties");
//		rankAddress = "/ranking/[ジャンル]Rank.properties";
		
//		loadRanking(rankAddress);
//		writeRank();
		loadRanking(rankAddress.replaceFirst("/", ""));
		
		playerNum = ranking.length + 1;
		
		sortRanking();
	}
	
	private void loadQuiz(String address) {
		quizProp = loadProp(address);
		
		for(int i = 1; i <= quizProp.size() / 2; i++) {
			String[] s = {quizProp.getProperty("Q"+i), quizProp.getProperty("Q"+i+"_A")};
			quiz.add(s);
//			System.out.println("Q"+i+": "+quizProp.getProperty("Q"+i));
			System.out.println("Q"+i+": "+quizProp.getProperty("Q"+i+"_A"));
		}
	}
	
	private void loadRanking(String address) {
		rankProp = loadExternalProp(address);
		
		ranking = new String[rankProp.size() / 3];
		for (int i = 0; i < ranking.length; i++) {
			ranking[i] = rankProp.getProperty("Player" + (i+1) + "_Name") + ": " +
					rankProp.getProperty("Player" + (i+1) + "_Score");
//			System.out.println(ranking[i]);
		}
		
	}
	
	private void sortRanking() {
		for (int i = 0; i < ranking.length; i++) {		
			for (int j = 0; j < ranking.length-1; j++) {
				// ranking[]のフォーマットは [プレイヤー名]: [スコア]
				int score1 = Integer.parseInt(ranking[j].substring(ranking[j].indexOf(": ")+2));
				int score2 = Integer.parseInt(ranking[j+1].substring(ranking[j+1].indexOf(": ")+2));
				if (score1 < score2) {
					String tmp = ranking[j];
					ranking[j] = ranking[j+1];
					ranking[j+1] = tmp;
				}
			}
		}
	}
	
	public void insertRank(String score) {
		int score1 = Integer.parseInt(score.substring(score.indexOf(": ")+2));
		
		for (int i = 0; i < ranking.length; i++) {
			int score2 = Integer.parseInt(ranking[i].substring(ranking[i].indexOf(": ")+2));
			if (score2 < score1) {
				ranking[i] = score;
				for (int j = i + 1; j < ranking.length - 1; j++) {
					String tmp = ranking[j];
					ranking[j] = ranking[j+1];
					ranking[j+1] = tmp;
				}
				break;
			}
		}
	}
	
	private Properties loadProp(String address) {
		URL url = getClass().getResource(address);
		Properties prop = new Properties();
		
		try {
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			prop.load(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	private Properties loadExternalProp(String address) {
		URL url = null;
		try {
			url = new File(address).toURI().toURL();
//			JOptionPane.showMessageDialog(null, url.toString());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		Properties prop = new Properties();
		
		try {
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			prop.load(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	@SuppressWarnings("unused")
	private String[][] loadCSV() {
		String[][] array = null;
		
		return array;
	}
	
	public void writeRank(String name, int score, String time) {
		rankProp.setProperty("Player" + playerNum + "_Score", Integer.toString(score));
		rankProp.setProperty("Player" + playerNum + "_Name", name);
		rankProp.setProperty("Player" + playerNum + "_Time", time);
//		URL url = getClass().getResource(rankAddress);
		try {
//			FileOutputStream fos = new FileOutputStream(url.getFile());
			FileOutputStream fos = new FileOutputStream(rankAddress.replaceFirst("/", ""));
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
//			rankingP.store(bw, null);
			String str  = "";
			for (int i = 0; i < rankProp.size() / 3; i++) {
				str = "Player" + (i+1) + "_Name = " +
						rankProp.getProperty("Player" + (i+1) + "_Name");
				bw.write(str);
				bw.newLine();
				str = "Player" + (i+1) + "_Score = " +
						rankProp.getProperty("Player" + (i+1) + "_Score");
				bw.write(str);
				bw.newLine();
				str = "Player" + (i+1) + "_Time = " +
						rankProp.getProperty("Player" + (i+1) + "_Time");
				bw.write(str);
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private void writeRank() {
		try {
//			FileOutputStream fos = new FileOutputStream(url.getFile());
			FileOutputStream fos = new FileOutputStream(rankAddress.replaceFirst("/", ""));
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
//			rankingP.store(bw, null);
			String str  = "";
			for (int i = 0; i < rankProp.size() / 3; i++) {
				str = "Player" + (i+1) + "_Name = " +
						rankProp.getProperty("Player" + (i+1) + "_Name");
				bw.write(str);
				bw.newLine();
				str = "Player" + (i+1) + "_Score = " +
						rankProp.getProperty("Player" + (i+1) + "_Score");
				bw.write(str);
				bw.newLine();
				str = "Player" + (i+1) + "_Time = " +
						rankProp.getProperty("Player" + (i+1) + "_Time");
				bw.write(str);
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private void replaceRank() {
		TestClass test = new TestClass();
		try {
			String[] cmd = CMD;
			cmd[3] = rankAddress.replaceFirst("/", "");
			test.execCmd(cmd);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	int size() {
		return quiz.size();
	}
	
	void nextQuiz() {
		badCount = 0;
		quizNumSum++;
		quiz.remove(quizNum);
		setRandomNum();
	}
	
	private void setRandomNum() {
		quizNum = (int)(Math.random() * (quiz.size() - 1));
	}
	
	void plusBadCount() {
		badCount++;
	}
	
	String getQuiz() {return quiz.get(quizNum)[0];}
	String getAnswer() {return quizNum != 11 ? quiz.get(quizNum)[1] : "";} // TODO:クイズファイルがきっちり10問の場合エラー
	int getQuizNum() {return quizNum;}
	int getQuizNumSum() {return quizNumSum;}
	int getBadCount() {return badCount;}
	public String[] getRanking() {return ranking;}
	public int getPlayerNum() {return playerNum;}
	
	
	public static void runTest() {
		try {
			TestClass test = new TestClass();
			test.execCmd(CMD);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * コマンドを実行するためのメソッドを持つクラス.
	 * コピペコードを隔離するため内部クラスに
	 * @author http://www.whitemark.co.jp/tec/java/javaExamples9.html
	 */
	static class TestClass {
		
		/**
		 * コマンドを実行する.
		 * コピペコード
		 * @throws IOException
		 * @throws InterruptedException
		 */
		void execCmd(String[] cmd) throws IOException, InterruptedException {
//			String[] cmd = { "java", "-version" };

			Process process = Runtime.getRuntime().exec(cmd);
			InputStream in = process.getInputStream();		// 1 サブプロセスの入力ストリームを取得
			InputStream ein = process.getErrorStream();		// 2 サブプロセスのエラーストリームを取得
			OutputStream out = process.getOutputStream();	// 3 サブプロセスの出力ストリームを取得 ここでは使用しません。
			/* 上記の3つのストリームは finally でクローズします。 */
			
			BufferedReader br = null;
			BufferedReader ebr = null;
			try {
				// 上記 1 のストリームを別スレッドで出力します。
				Runnable inputStreamThread = createRunnable("stdRun", in, br);

				// 上記 2 のストリームを別スレッドで出力
				Runnable errStreamThread = createRunnable("errRun", ein, ebr);

				Thread stdRun = new Thread(inputStreamThread);
				Thread errRun = new Thread(errStreamThread);

				// スレッドを開始します。
				stdRun.start();
				errRun.start();

				// プロセスが終了していなければ終了するまで待機
				int c = process.waitFor();

				// サブスレッドが終了するのを待機
				stdRun.join();
				errRun.join();

				// プロセス終了コード出力
				System.out.println(c);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) br.close();
				if (ebr != null) ebr.close();

				// 子プロセス
				if (in != null) in.close();
				if (ein != null) ein.close();
				if (out != null) out.close();
			}
		}
		
		private Runnable createRunnable(String name, InputStream in, BufferedReader br) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					inputStream(name, in, br);
				}
			};
			return runnable;
		}
		
		private void inputStream(String name, InputStream in, BufferedReader br) {
			System.out.println("Thread " + name + " start");
			try {
				br = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = br.readLine()) != null) {
					System.err.println(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Thread " + name + " end");
		}
	}
	
}

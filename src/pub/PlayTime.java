package pub;

import java.util.Date;

class PlayTime {
	private long startTime;
	private long endTime;
	
	void start() {
		startTime = new Date().getTime();
	}
	
	void stop() {
		endTime = new Date().getTime();
	}
	
	/**
	 * プレイタイムのミリ秒を返す.
	 * 終わった時間から始めた時間を引いたミリ秒
	 * @return プレイタイム(ミリ秒)
	 */
	long getMsTime() {
		return endTime - startTime;
	}
}

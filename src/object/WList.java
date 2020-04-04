package object;

import java.util.ArrayList;

public class WList {
	private ArrayList<Waku> wList;
	
	public void createWList() {
		wList = new ArrayList<Waku>();
	}
	
	public void add(Waku waku) {
		wList.add(waku);
	}
	
	public Waku get(int i) {
		return wList.get(i);
	}
	
	public int size() {
		return wList.size();
	}
	
	public ArrayList<Waku> getWList() {
		return wList;
	}
}

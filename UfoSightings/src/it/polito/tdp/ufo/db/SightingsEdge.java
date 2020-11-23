package it.polito.tdp.ufo.db;

public class SightingsEdge {
	private int cnt;
	private String s1;
	private String s2;
	public SightingsEdge(int cnt, String s1, String s2) {
		super();
		this.cnt = cnt;
		this.s1 = s1;
		this.s2 = s2;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getS1() {
		return s1;
	}
	public void setS1(String s1) {
		this.s1 = s1;
	}
	public String getS2() {
		return s2;
	}
	public void setS2(String s2) {
		this.s2 = s2;
	}

}

package it.polito.tdp.seriea.model;

public class SeasonInt implements Comparable<SeasonInt>{
	private Season season;
	private int i;
	public Season getSeason() {
		return season;
	}
	public void setSeason(Season season) {
		this.season = season;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public SeasonInt(Season season, int i) {
		super();
		this.season = season;
		this.i = i;
	}
	@Override
	public int compareTo(SeasonInt o) {
		return this.getSeason().compareTo(o.getSeason());
	}

}

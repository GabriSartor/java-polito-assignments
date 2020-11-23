package it.polito.tdp.borders.model;

//SEMPRE COMPARABLE
public class Event implements Comparable<Event>{
	private int t; //LocalTime o Date se piu' complesso
	private int n; //Il numero di persone che sono arrivate
	private Country country; //Il paese in cui le persone arrivano
	public Event(int t, int n, Country country) {
		super();
		this.t = t;
		this.n = n;
		this.country = country;
	}
	public int getT() {
		return t;
	}
	public int getN() {
		return n;
	}
	public Country getCountry() {
		return country;
	}
	@Override
	public int compareTo(Event o) {
		return this.t - o.t;
	}
	
	

}

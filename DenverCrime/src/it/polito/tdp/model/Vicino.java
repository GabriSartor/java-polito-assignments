package it.polito.tdp.model;

public class Vicino implements Comparable<Vicino> {
	private Integer V;
	private Double distanza;
	public Vicino(int v, double distanza) {
		super();
		V = v;
		this.distanza = distanza;
	}
	public int getV() {
		return V;
	}
	public void setV(int v) {
		V = v;
	}
	public double getDistanza() {
		return distanza;
	}
	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}
	@Override
	public int compareTo(Vicino o) {
		return (this.distanza.compareTo(o.distanza));
	}

}

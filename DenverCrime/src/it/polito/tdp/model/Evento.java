package it.polito.tdp.model;

import java.time.LocalTime;

public class Evento implements Comparable<Evento> {
	
	public enum TipoEvento {
		CRIMINE,
		ARRIVO_AGENTE,
		CRIMINE_TERMINATO,
	}
	
	private LocalTime h;
	private Event e;
	private TipoEvento type;

	public Evento(LocalTime h, Event e, TipoEvento type) {
		super();
		this.h = h;
		this.e = e;
		this.type = type;
	}

	public LocalTime getH() {
		return h;
	}

	public void setH(LocalTime h) {
		this.h = h;
	}

	public Event getE() {
		return e;
	}

	public void setE(Event e) {
		this.e = e;
	}

	public TipoEvento getType() {
		return type;
	}

	public void setType(TipoEvento type) {
		this.type = type;
	}

	@Override
	public int compareTo(Evento o) {
		return this.h.compareTo(o.h);
	}

}

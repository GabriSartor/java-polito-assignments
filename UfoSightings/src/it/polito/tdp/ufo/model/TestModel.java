package it.polito.tdp.ufo.model;

import java.time.Year;

public class TestModel {
	
	public void run() {
		Model model = new Model();
		Year y = Year.of(2010);
		model.creaGrafo(y);
		
		System.out.println("Archi "+model.getNEdge());
		System.out.println("Vertici "+model.getNVertex());
		
		System.out.println(model.getConnected("dc"));
		System.out.println(model.getStatesBefore("dc"));
		System.out.println(model.getStatesAfter("dc"));
		
	}

	public static void main(String[] args) {
		TestModel main = new TestModel();
		main.run();

	}

}

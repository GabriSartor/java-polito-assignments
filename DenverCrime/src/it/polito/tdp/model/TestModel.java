package it.polito.tdp.model;

import java.time.Month;

public class TestModel {

	public static void main(String[] args) {
		
		TestModel main = new TestModel();
		main.run();
	}

	private void run() {
		Model m = new Model();
		m.creaGrafo(2015);
		m.getVicini(1);
		int mal = m.simulate(22, Month.APRIL, 2015, 100);
		System.out.println("Mal! "+mal);
	}

}

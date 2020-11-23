package it.polito.tdp.seriea.model;

import java.util.Map;

public class TestModel {
	
	public void run() {
		Model model = new Model();
		Team team = new Team("Juventus");
		model.creaGrafo(team);
		System.out.println(model.getGoldYear(team));	
		
    	Map<Season, Integer> pointsPerYear = model.getPointsPerYear(team);
    	for (Season s: pointsPerYear.keySet()) {
    		System.out.println(s.getDescription()+" - "+pointsPerYear.get(s)+" punti\n");
    	}
		
		
	}

	public static void main(String[] args) {
		TestModel main = new TestModel();
		main.run();

	}

}

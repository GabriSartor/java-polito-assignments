package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulator {
	
	// Modello -> Stato del sistema ad ogni passo
	private Graph<Country, DefaultEdge> graph;
	
	// Tipi di evento
	// Un solo
	private PriorityQueue<Event> queue;
	
	// Parametri simulazione
	private int N_MIGRANTS = 1000;
	private Country startingCountry;
	
	// Statistiche misurate
	private int T;
	Map<Country, Integer> sedentaryPerCountry;
	
	public void init(Country startingCountry, Graph<Country,DefaultEdge> graph) {
		this.startingCountry = startingCountry;
		this.graph = graph;
		
		this.T = 1;
		sedentaryPerCountry = new HashMap<Country, Integer>();
		for (Country c : this.graph.vertexSet()) {
			this.sedentaryPerCountry.put(c, 0);
		}
		
		queue = new PriorityQueue<Event>();
		this.queue.add(new Event(T, N_MIGRANTS, startingCountry));
	}
	
	public void run() {
		Event e;
		
		while((e = queue.poll()) != null) {
			this.T= e.getT();
			
			int nPeople = e.getN();
			Country c = e.getCountry();
			List<Country> cConfinants = Graphs.neighborListOf(this.graph, c);
			int nMigr = (nPeople/2) / cConfinants.size();
			
			if(nMigr > 0) {
				for (Country to: cConfinants) {
					this.queue.add(new Event(e.getT()+1, nMigr, to));
				}
			}
			
			int nSed = nPeople - nMigr*cConfinants.size();
			this.sedentaryPerCountry.put(c, this.sedentaryPerCountry.get(c)+nSed);
		}
	}

	public int getT() {
		return T;
	}
	
	public Map<Country, Integer> getSed() {
		return this.sedentaryPerCountry;
	}

}

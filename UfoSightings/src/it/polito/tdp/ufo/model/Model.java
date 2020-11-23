package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.ufo.db.SightingsDAO;
import it.polito.tdp.ufo.db.SightingsEdge;
import it.polito.tdp.ufo.db.YearCount;

public class Model {
	
	//Ricorsione
	// Finale
	private List<String> best; // Lista di stati con stato di partenza e insieme di stati non ripetuti
	
	
	// Condizione di terminazione
	// Se non ci sono successori non considerati
	
	// Nuova soluzione
	// Dato l'ultimo nodo inserito in parizale, considero tutti i successari non visitati
	
	// Filtro
	// Ritorno best.size() maggiore
	
	private List<String> states;
	private Graph<String, DefaultEdge> graph;
	
	private SightingsDAO dao;
	
	public Model() {
		this.dao = new SightingsDAO();
	}
	
	public List<YearCount> getYears () {
		return this.dao.getSightingsPerYear();
	}
	
	public void creaGrafo(Year year) {
		states = new LinkedList<>();
		states.addAll(this.dao.getStates(year));
		this.graph = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(graph, states);
		
		List<SightingsEdge> edges = this.dao.loadEdges(year);
		for (SightingsEdge e: edges) {
			if (!e.getS1().equals(e.getS2()) && e.getCnt() !=0) 
				this.graph.addEdge(e.getS1(), e.getS2());
		}
		System.out.println("Grafo creato");
		System.out.println("VertexSet: "+this.graph.vertexSet().size());
		System.out.println("EdgeSet: "+this.graph.edgeSet().size());
	}

	public int getNVertex() {
		return this.graph.vertexSet().size();
	}

	public int getNEdge() {
		return this.graph.edgeSet().size();
	}

	public List<String> getStates() {
		return this.states;
	}
	
	public List<String> getStatesBefore(String state) {
		return Graphs.predecessorListOf(this.graph, state);
	}
	
	public List<String> getStatesAfter(String state) {
		return Graphs.successorListOf(this.graph, state);
	}
	
	public List<String> getConnected(String state) {
		List<String> connected = new LinkedList<>();		

		GraphIterator<String, DefaultEdge> it = new BreadthFirstIterator<>(this.graph, state);

		//Tolgo il primo
		it.next();
		
		while (it.hasNext()) {
			connected.add(it.next());
		}

		return connected;
	}
	
	public List<String> findMaxWalk(String source) {
		best = new LinkedList<>();
		List<String> partial = new LinkedList<>();
		partial.add(source);
		
		runRecursion(partial);
		
		return this.best;
	}
	
	private void runRecursion(List<String> partial) {
		// Filtro
		if(partial.size() > best.size()) {
			this.best = new LinkedList<>(partial);
		}
		// Ottengo candidati e li provo tutti
		List<String> after = this.getStatesAfter(partial.get(partial.size()-1));
		for (String s: after) {
			if(!partial.contains(s)) {
				partial.add(s);
				this.runRecursion(partial);
				partial.remove(partial.size()-1);
			}
		}
		
	}

}

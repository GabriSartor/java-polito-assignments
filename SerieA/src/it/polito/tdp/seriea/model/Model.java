package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	private Map<Integer, Season> seasonsIdMap;
	private List<Team> teams;
	private SerieADAO dao;
	
	private Graph<Season, DefaultWeightedEdge> graph;
	
	private List<Season> best;
	private List<Season> consecutive;
	
	public Model() {
		dao = new SerieADAO();
		teams = new ArrayList<>(dao.listTeams());
		seasonsIdMap = new HashMap<Integer, Season>();
		dao.listAllSeasons(seasonsIdMap);
	}

	public List<Team> getTeams() {
		return teams;
	}

	public Map<Season, Integer> getPointsPerYear(Team team) {
		Map<Season, Integer> result = new HashMap<Season, Integer>();
		for (SeasonInt s: dao.getWinPerYear(team, seasonsIdMap)) {
			System.out.println(s.getSeason() + " " +s.getI());
			result.put(s.getSeason(), s.getI()*3);
		}
		
		for (SeasonInt s: dao.getDrawPerYear(team, seasonsIdMap)) {
			System.out.println(s.getSeason() + " " +s.getI());
			result.put(s.getSeason(), result.get(s.getSeason())+s.getI()*1);
		}
		return result;
	}

	public void creaGrafo(Team team) {
		this.graph = new DefaultDirectedWeightedGraph<Season, DefaultWeightedEdge>(DefaultWeightedEdge.class);	
		Map<Season, Integer> pointsPerYear = this.getPointsPerYear(team);
		Graphs.addAllVertices(graph, pointsPerYear.keySet());
		for (Season s1: graph.vertexSet()) {
			for (Season s2: graph.vertexSet()) {
				if (s1 != s2) {
					if (pointsPerYear.get(s1) < pointsPerYear.get(s2)) {
						Graphs.addEdge(this.graph, s1, s2, (pointsPerYear.get(s2) - pointsPerYear.get(s1)) );
					}
				}
			}
		}
		
		System.out.println("Grafo creato");
		System.out.println("Vertici creati "+graph.vertexSet().size());
		System.out.println("Archi creati "+graph.edgeSet().size());
		System.out.println("Vertex set");
		System.out.println(graph.vertexSet());
	}
	
	public SeasonInt getGoldYear(Team team) {
		SeasonInt best = new SeasonInt(null, 0);
		for (Season s: graph.vertexSet()) {
			int weight = 0;
			for (DefaultWeightedEdge e: graph.incomingEdgesOf(s)) {
				weight += graph.getEdgeWeight(e);
			}
			for (DefaultWeightedEdge e: graph.outgoingEdgesOf(s)) {
				weight -= graph.getEdgeWeight(e);
			}
			
			if (weight >= best.getI()) {
				best.setSeason(s);
				best.setI(weight);
			}
		}
		return best;
	}
	
	public List<Season> findMaxWalk(Team team) {
		best = new LinkedList<>();
		
		consecutive = new ArrayList<>(graph.vertexSet());
		Collections.sort(consecutive);
		
		List<Season> partial = new LinkedList<>();
		for (Season s: this.graph.vertexSet()) {
			partial.add(s);
			runRecursion(partial);
			partial.remove(0);
		}
		
		return best;
	}
	
	private void runRecursion(List<Season> partial) {
		// Filtro
		if(partial.size() > best.size()) {
			this.best = new LinkedList<>(partial);
		}
		
		// Ottengo candidati e li provo tutti
		Season last = partial.get(partial.size()-1);
		
		for (Season s: Graphs.successorListOf(graph, last)) {
			if (!partial.contains(s)) {
				if (consecutive.indexOf(s) == (consecutive.indexOf(last)+1)) {
					partial.add(s);
					this.runRecursion(partial);
					partial.remove(partial.size()-1);
				}
			}
		}
	}
}

package it.polito.tdp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.db.EventsDao;

public class Model {
	
	private Map<Long, Event> evIdMap;
	private EventsDao dao;
	private Graph<Integer, DefaultWeightedEdge> graph;
	private Map<Integer, LatLng> mapDistrict ;

	public Model() {
		super();
		this.evIdMap = new HashMap<>();
		this.dao = new EventsDao();
		dao.listAllEvents(evIdMap);
	}

	public List<Integer> getYears() {
		List<Integer> result = new ArrayList<Integer>();
		for (Event e: evIdMap.values()) {
			if (!result.contains(e.getReported_date().getYear())) {
				result.add(e.getReported_date().getYear());
			}
		}
		return result;
	}
	
	public List<Month> getMonths() {
		List<Month> result = new ArrayList<Month>();
		for (Event e: evIdMap.values()) {
			if (!result.contains(e.getReported_date().getMonth())) {
				result.add(e.getReported_date().getMonth());
			}
		}
		return result;
	}
	
	public List<Integer> getDays() {
		List<Integer> result = new ArrayList<Integer>();
		for (Event e: evIdMap.values()) {
			if (!result.contains(e.getReported_date().getDayOfMonth())) {
				result.add(e.getReported_date().getDayOfMonth());
			}
		}
		return result;
	}
	
	public void creaGrafo(int year) {
		this.graph = new DefaultUndirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		mapDistrict = dao.listDistrictsAndGeo(year);
		Graphs.addAllVertices(graph, mapDistrict.keySet());
		
		for (Integer v1: graph.vertexSet()) {
			for(Integer v2: graph.vertexSet()) {
				if (v1 != v2) {
					Graphs.addEdgeWithVertices(graph, v1, v2, LatLngTool.distance(mapDistrict.get(v1), mapDistrict.get(v2), LengthUnit.KILOMETER));
				}
			}
		}

	}

	public Set<Integer> getDistretti() {
		return mapDistrict.keySet();
	}

	public List<Vicino> getVicini(Integer d) {
		List<Vicino> result = new ArrayList<>();
		for (Integer i:Graphs.neighborListOf(graph, d)) {
			result.add(new Vicino(i, this.graph.getEdgeWeight(this.graph.getEdge(d, i))));
		}
		Collections.sort(result);
				
		return result;
	}

	public int simulate(Integer day, Month month, Integer year, Integer n) {
		Simulatore s = new Simulatore(this);
		List<Event> eventByDay = new ArrayList<>();
		for (Event e: evIdMap.values()) {
			if (e.getReported_date().toLocalDate().equals(LocalDate.of(year, month, day)))
				eventByDay.add(e);
		}
		s.init(eventByDay, n, LocalDate.of(year, month, day));
		int malGestiti = s.getMalGestiti();
		return malGestiti;
	}

	public Integer getDistrettoSicuro(Integer year) {
		return dao.getDistrettoSicuro(year);
	}
	
}

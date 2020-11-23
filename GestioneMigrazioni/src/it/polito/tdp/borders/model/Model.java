package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> graph ;
	private List<Country> countries ;
	private Map<Integer,Country> countriesMap ;
	
	Simulator sim;
	
	public Model() {
		this.countriesMap = new HashMap<>() ;

	}
	
	public void creaGrafo(int anno) {
		
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;

		BordersDAO dao = new BordersDAO() ;
		
		//vertici
		dao.getCountriesFromYear(anno,this.countriesMap) ;
		Graphs.addAllVertices(graph, this.countriesMap.values()) ;
		
		// archi
		List<Adiacenza> archi = dao.getCoppieAdiacenti(anno) ;
		for( Adiacenza c: archi) {
			graph.addEdge(this.countriesMap.get(c.getState1no()), 
					this.countriesMap.get(c.getState2no())) ;
			
		}
	}
	
	public List<CountryAndNumber> getCountryAndNumber() {
		List<CountryAndNumber> list = new ArrayList<>() ;
		
		for(Country c: graph.vertexSet()) {
			list.add(new CountryAndNumber(c, graph.degreeOf(c))) ;
		}
		Collections.sort(list);
		return list ;
	}

	public List<Country> getCountries() {
		List<Country> res = new ArrayList();
		for (Country c: this.countriesMap.values()) {
			res.add(c);
		}
		Collections.sort(res);
		return res;
	}

	public void simulate(Country start) {
		sim = new Simulator();
		sim.init(start, graph);
		sim.run();
		
	}

	public int getFinalT() {
		return sim.getT();
	}

	public Collection<CountryAndNumber> getSed() {
		Map<Country, Integer> sedentary = this.sim.getSed();
		List<CountryAndNumber> sedList = new ArrayList<CountryAndNumber>();
		for (Country c: sedentary.keySet()) {
			CountryAndNumber cn = new CountryAndNumber(c, sedentary.get(c));
			sedList.add(cn);
		}
		
		Collections.sort(sedList);
		return sedList;
	}

}

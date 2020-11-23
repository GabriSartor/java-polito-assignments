package it.polito.tdp.model;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import it.polito.tdp.model.Evento.TipoEvento;

public class Simulatore {
	private Model model;
	private PriorityQueue<Evento> queue;
	
	//Parametri
	private LocalDate day;
	private int N;
	private double speed = 60/3600; //in km/s
	
	private Duration INTERVAL_2H = Duration.ofHours(2);
	private Duration INTERVAL_1H = Duration.ofHours(2);
	
	//Statistiche
	private int MAL_GESTITI;
	
	//Variabili interne
	private Map<Integer, Integer> agenti;
	private Integer stazione;
	private Random rand = new Random();
	
	public Simulatore(Model model) {
		super();
		this.model = model;
		this.queue = new PriorityQueue<>();
		this.agenti = new HashMap<>();
	}

	
	
	public void init(List<Event> eventByDay, Integer n, LocalDate day) {
		MAL_GESTITI = 0;
		this.N = n;
		this.day = day;
		
		queue.clear();
		System.out.println("Eventi da aggiungere "+eventByDay.size());
		for(Event e:eventByDay) {
			queue.add(new Evento(e.getReported_date().toLocalTime(), e, TipoEvento.CRIMINE));
			System.out.println("Evento aggiunto "+e.toString());
		}
		this.stazione = this.model.getDistrettoSicuro(day.getYear());
		
		for (Integer d: this.model.getDistretti())
			agenti.put(d, 0);
		
		agenti.put(stazione, N);
		System.out.println(agenti);
		
		this.run();
	}
	
	public void run() {
		while (!queue.isEmpty()) {
			Evento ev = queue.poll();
			switch(ev.getType()) {
			case CRIMINE:
				Duration timeToGetThere = Duration.ofMinutes(1);
				//Cerco agente piu' vicino
				System.out.println(agenti);
				System.out.println("Cerco agente per distretto "+ev.getE().getDistrict_id());
				double best_distance = Double.MAX_VALUE;
				int best_distretto = 0;
				if (agenti.get(ev.getE().getDistrict_id()) != 0) {
					timeToGetThere = Duration.ofSeconds(0);
					best_distretto = ev.getE().getDistrict_id();
					System.out.println("Agente in location! ");
				} else {
					for (Vicino v : model.getVicini(ev.getE().getDistrict_id())) {
						if (agenti.get(v.getV()) != 0 && v.getDistanza() < best_distance) {
							System.out.println(v.getV()+" "+v.getDistanza());
							best_distretto = v.getV();
							best_distance = v.getDistanza();
							timeToGetThere = Duration.ofSeconds((long)(best_distance/speed));
						}
					}
				}
				
				//Se ho trovato agente
				if (best_distretto != 0) {
					agenti.put(best_distretto, agenti.get(best_distretto)-1);
					//Schedulo evento arrivo agente
					queue.add(new Evento(ev.getH().plus(timeToGetThere), ev.getE(), TipoEvento.ARRIVO_AGENTE));
				} else {
					MAL_GESTITI++;
				}
				break;
				
			case ARRIVO_AGENTE:
				if (ev.getH().isAfter(ev.getE().getReported_date().toLocalTime().plusMinutes(15))) {
					MAL_GESTITI++;
				}
				Duration timeToManage;
				if (!ev.getE().getOffense_category_id().equals("all_other_crimes")) {
					timeToManage = INTERVAL_2H;
				} else {
					int i = rand.nextInt(1);
					if (i>0) timeToManage = INTERVAL_2H; 
					else timeToManage = INTERVAL_1H;
				}
				queue.add(new Evento(ev.getH().plus(timeToManage), ev.getE(), TipoEvento.CRIMINE_TERMINATO));
				break;
				
			case CRIMINE_TERMINATO:
				this.agenti.put(ev.getE().getDistrict_id(), this.agenti.get(ev.getE().getDistrict_id())+1);
				break;
			}
		}
		
	}



	public int getMalGestiti() {
		return MAL_GESTITI;
		
	}

}

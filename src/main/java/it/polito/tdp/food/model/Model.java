package it.polito.tdp.food.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	
	public Model() {
		dao = new FoodDao();
	}
	
	public void creaGrafo(int numPorzioni) {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new TreeMap<>();
		
		dao.listAllFoods(idMap, numPorzioni);
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(Adiacenza a: dao.getArchi(idMap)) {
			
			Graphs.addEdge(grafo, a.getF1(), a.getF2(), a.getPeso());
		}
		
	}
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return grafo.edgeSet().size();
	}

	public Collection<Food> getVertici() {
		return idMap.values();
	}
	
	public List<Adiacenza> getCalorie(Food partenza){
		
		List<Adiacenza> result = new ArrayList<>();
		
		for(DefaultWeightedEdge de: grafo.edgesOf(partenza)) {
			
			Adiacenza a = new Adiacenza(partenza, Graphs.getOppositeVertex(grafo, de, partenza), grafo.getEdgeWeight(de));
			result.add(a);
		}
		
		Collections.sort(result, new Comparator<Adiacenza>() {

			@Override
			public int compare(Adiacenza o1, Adiacenza o2) {
				return Double.compare(o2.getPeso(), o1.getPeso());
			}
			
		});
		
		return result;
	}

	public Graph<Food, DefaultWeightedEdge> getGrafo() {
		// TODO Auto-generated method stub
		return grafo;
	}
	
	private int k;
	private Food partenza;
	
	private double minuti;
	private int numStazioniOccupate;
	private List<Food> cibiInPreparazione;
	
	private int cibiPreparati;
	private double tempoTotale;
	private PriorityQueue<Event> queue;
	
	public void init(int k, Food start) {
		this.k = k;
		this.partenza = start;
		
		this.cibiPreparati = 0;
		this.tempoTotale = 0.0;
		queue = new PriorityQueue<>();
		numStazioniOccupate = 0;
		
		List<Adiacenza> cibi = this.getCalorie(partenza);
		if(!cibi.isEmpty()) {
			for(int i=0; i< k && i<cibi.size(); i++){
				if(cibi.get(i) != null) {
					queue.add(new Event(cibi.get(i).getF2(), cibi.get(i).getPeso()));
					numStazioniOccupate++;
					cibiInPreparazione.add(cibi.get(i).getF2());
				}
				
			}
		}else {
			return;
		}
	}
	
	public void run() {
		
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			
			tempoTotale += e.getMinuti();
			cibiPreparati++;
			
			List<Adiacenza> successivi = this.getCalorie(e.getFood());
			
			if(!successivi.isEmpty()) {
				int i=0;
				while(i < successivi.size()){
					
					if(!cibiInPreparazione.contains(successivi.get(i))) {
						queue.add(new Event(successivi.get(i).getF2(), successivi.get(i).getPeso()));
						break;
					}
					i++;
				}
				
				if(i == successivi.size()) {
					numStazioniOccupate--;
				}
			}else {
				numStazioniOccupate--;
			}
			
		}
	}
	
	public int getNumeroCibiPreparati() {
		return this.cibiPreparati;
	}
	
	public double getTempoTotale() {
		return this.tempoTotale;
	}
}

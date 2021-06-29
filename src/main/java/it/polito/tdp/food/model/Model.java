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
	
	public String simula(Food cibo, int K) {
		Simulator sim = new Simulator(this.grafo, this) ;
		sim.setK(K);
		sim.init(cibo);
		sim.run();
		String messaggio = String.format("Preparati %d cibi in %f minuti\n", 
				sim.getCibiPreparati(), sim.getTempoPreparazione());
		return messaggio ;
	}
	
	public List<FoodCalories> elencoCibiConnessi(Food f) {
		
		List<FoodCalories> result = new ArrayList<>() ;
		
		List<Food> vicini = Graphs.neighborListOf(this.grafo, f) ;
		
		for(Food v: vicini) {
			Double calorie = this.grafo.getEdgeWeight(this.grafo.getEdge(f, v)) ;
			result.add(new FoodCalories(v, calorie)) ;
		}
		
		Collections.sort(result);
		
		return result ;
	}
}

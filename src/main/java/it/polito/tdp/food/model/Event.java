package it.polito.tdp.food.model;

import java.time.LocalTime;

public class Event {

	private Food food;
	private double minuti;
	
	public Event(Food food, double minuti) {
		super();
		this.food = food;
		this.minuti = minuti;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public double getMinuti() {
		return minuti;
	}

	public void setMinuti(double minuti) {
		this.minuti = minuti;
	}
	
	
		
}

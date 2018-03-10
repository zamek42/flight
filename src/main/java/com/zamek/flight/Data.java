package com.zamek.flight;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.zamek.flight.dijkstra.Graph;
import com.zamek.flight.factory.XMLFactory;

public class Data implements Graph {

	private static Data instance;
	private List<City> cities=Collections.emptyList();
	private Set<Airline> airlines=Collections.emptySet();
	private List<Flight> flights=Collections.emptyList();
	
	private Data() {
		
	}
	
	public synchronized static Data getInstance() {
		if (instance==null)
			instance = new Data();
		
		return instance;
	}
	
	public synchronized void load(String xmlName) {
		XMLFactory factory = new XMLFactory(xmlName);
		factory.loadPopulation();
		factory.loadXml();
		this.cities = factory.getCities();
		this.airlines = factory.getAirlines();
		this.flights = factory.getFlights();
	}
	
	@Override
	public List<City> getVertexes() {
		return Collections.unmodifiableList(this.cities);
	}
	
	public List<City> getCities() {
		return this.cities;
	}

	public Set<Airline> getAirlines() {
		return this.airlines;
	}
	
	@Override
	public List<Flight> getEdges() {
		return Collections.unmodifiableList(this.flights);
	}

	public List<Flight> getFlights() {
		return this.flights;
	}

}

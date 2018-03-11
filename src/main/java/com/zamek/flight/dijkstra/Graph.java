package com.zamek.flight.dijkstra;

import java.util.List;

import com.zamek.flight.City;
import com.zamek.flight.Flight;

/**
 * Helper interface for Dijkstra engine
 * 
 * @author zamek
 *
 */
public interface Graph {

	/**
	 * getter for vertexes
	 * 
	 * @return the vertex list
	 */
	List<City> getCities();
	
	/**
	 * Getter for edges
	 * 
	 * @return edge list
	 */
	List<Flight> getFlights();
	
}

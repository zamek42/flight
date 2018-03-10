package com.zamek.flight.dijkstra;

import java.util.List;

import com.zamek.flight.City;
import com.zamek.flight.Flight;

public interface Graph {

	List<City> getVertexes();
	
	List<Flight> getEdges();
	
}

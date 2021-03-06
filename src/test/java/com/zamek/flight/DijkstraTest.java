package com.zamek.flight;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.zamek.flight.dijkstra.DijkstraEngine;

public class DijkstraTest {

	@BeforeClass
	public static void loadXML() {
		Data.getInstance().load(AppTest.XML_FILE_NAME);
	}
	
	@SuppressWarnings("static-method")
	@Test
	public void minMaxRouteTest() {
		List<City> cities = Data.getInstance().getCities();
		assertNotNull(cities);
		assertTrue(cities.size()>0);
		Optional<City> min = cities.stream().min((a, b)->a.getPopulation() - b.getPopulation());
		assertTrue(min.isPresent());
		System.out.println("City of the smallest population is "+ min.get()); //$NON-NLS-1$
		Optional<City> max = cities.stream().max((a,b)->a.getPopulation()-b.getPopulation());
		assertTrue(max.isPresent());
		System.out.println("City of the largest population is "+ max.get());  //$NON-NLS-1$
		DijkstraEngine de = new DijkstraEngine(Data.getInstance());
		de.execute(min.get());
		Map<Airline, LinkedList<Flight>> paths=new HashMap<>();
		for(Airline al:Data.getInstance().getAirlines()) {
			LinkedList<Flight> path = de.getPath(max.get(), al);
			if (path != null)
				paths.put(al, path);
		}			
		if (paths.isEmpty()) 
			Data.printPath(min.get(), max.get(), de.getPath(max.get()));
		else 
			for(Map.Entry<Airline, LinkedList<Flight>> e:paths.entrySet())
				Data.printPath(min.get(), max.get(), e.getValue());
		
	}
}

package com.zamek.flight;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.zamek.flight.dijkstra.DijkstraEngine;
import com.zamek.flight.dijkstra.Graph;
import com.zamek.flight.factory.XMLFactory;

/**
 * Singleton for data
 * 
 * @author zamek
 *
 */
public class Data implements Graph {

	private final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); //$NON-NLS-1$
	private static Data instance;
	private List<City> cities=Collections.emptyList();
	private Set<Airline> airlines=Collections.emptySet();
	private List<Flight> flights=Collections.emptyList();
	
	private Data() {
		
	}
	
	/**
	 * Get an instance for the data
	 * @return
	 */
	public synchronized static Data getInstance() {
		if (instance==null)
			instance = new Data();
		
		return instance;
	}
	
	/**
	 * Load flight data from an xml file
	 * 
	 * @param xmlName name of the xml file
	 */
	public synchronized void load(String xmlName) {
		XMLFactory factory = new XMLFactory(xmlName);
		factory.loadPopulation();
		factory.loadXml();
		this.cities = factory.getCities();
		this.airlines = factory.getAirlines();
		this.flights = factory.getFlights();
	}
	
	/**
	 * Get a list of cities
	 * 
	 * @return list of cities
	 */
	@Override
	public List<City> getCities() {
		return this.cities;
	}

	/**
	 * Get a list of airlines
	 * 
	 * @return list of airlines
	 */
	public Set<Airline> getAirlines() {
		return this.airlines;
	}
	
	/**
	 * Get a flight list of an airline
	 * 
	 * @param airline airline
	 * @return the list ot the flights of an airline
	 */
	public List<Flight> getFlightOfAirline(Airline airline) {
		return this.flights.stream().filter(f->f.getAirline().equals(airline)).parallel().collect(Collectors.toList());
	}
	
	/**
	 * Get list of all lights
	 * 
	 * @return list of flights
	 */
	@Override
	public List<Flight> getFlights() {
		return this.flights;
	}

	/**
	 * Find a city in the cities list. 
	 * 
	 * Search with ignore cases
	 * 
	 * @param name name of the city
	 * @return Optional with city or an empty optinonal if not found
	 */
	public Optional<City> findCity(String name) {
		return this.cities.stream().filter(c->c.getName().equalsIgnoreCase(name)).findFirst();
	}
	
	/**
	 * Find am airline in the airlines list. 
	 * 
	 * Search with ignore cases
	 * 
	 * @param name name of the airline
	 * @return Optional with airline or an empty optinonal if not found
	 */
	public Optional<Airline> findAirline(String name) {
		return name==null ? Optional.empty() : this.airlines.stream().filter(a->a.getName().equalsIgnoreCase(name)).findFirst();
	}
	
	/**
	 * Find a path between two cities with an optionally airline
	 *  
	 * @param source source city
	 * @param destination destination city
	 * @param airline optional, can be null if airline is not relevant
	 * @return list of path or null if not found
	 */
	public static LinkedList<Flight> findPath(City source, City destination, Airline airline) {
		if (source==null || destination==null)
			return null;
		
		DijkstraEngine de = new DijkstraEngine(Data.getInstance());
		de.execute(source);		
		return de.getPath(destination, airline);		
	}

	/**
	 * Print a path list to the screen 
	 * 
	 * @param source source city
	 * @param destination destination city
	 * @param path the route between source and destiantion
	 */
	public static void printPath(City source, City destination, LinkedList<Flight> path) {
		if (path==null) {
			System.out.println(String.format(Messages.getString("Data.pathNotFound"),   //$NON-NLS-1$
											 source, destination));
			return;
		}
		System.out.println(String.format(Messages.getString("Data.pathBetween"), source, destination)); //$NON-NLS-1$
		int totalDistance = 0;
		Duration duration = Duration.ZERO;
		LocalTime instant = path.get(0).getDeparture();
		for (Flight flight : path) {
			Duration wait = Duration.between(instant, flight.getDeparture());
			duration = duration.plus(wait);
			instant = flight.getDeparture();
			totalDistance += flight.getDistance();
			instant = flight.addDuration(instant);
			duration = duration.plusNanos(flight.getDuring().toNanoOfDay());
			System.out.println(String.format(Messages.getString("Data.pathDetail"), flight.getSource().getName(), flight.getDestination().getName(),   //$NON-NLS-1$ 
					Integer.valueOf(flight.getDistance()), 
					flight.getDeparture(), TIME_FORMATTER.format(instant), flight.getAirline()));
			
		}
		System.out.println(String.format(Messages.getString("Data.pathTotal"),  //$NON-NLS-1$
										 Integer.valueOf(totalDistance), Flight.getFormattedDuring(duration)));
	}
}

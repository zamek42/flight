package com.zamek.flight.factory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.zamek.flight.Airline;
import com.zamek.flight.City;
import com.zamek.flight.Flight;
import com.zamek.flight.util.HasLogger;

public class XMLFactory extends DefaultHandler implements HasLogger {
	private final static String POPULATION_FILE = "data/simplemaps-worldcities-basic.csv"; //$NON-NLS-1$
	
	private String flightsXML;
	protected List<String> populationList;
	private Thread populationLoader;
	private Set<Airline> airlines = new HashSet<>();
	private Airline currentAirline;
	private List<City> cities = new ArrayList<>(); 
	private List<Flight> flights = new ArrayList<>();
	
	public XMLFactory(String flightsXML) {
		this.flightsXML = flightsXML;
	}

	public Set<Airline> getAirlines() {
		return this.airlines;
	}
	
	public List<City> getCities() {
		return this.cities;
	}
	
	public List<Flight> getFlights() {
		return this.flights;
	}
	
	public void loadPopulation() {
		this.populationLoader = new Thread() {
			
			@Override
			public void run() {		
				try {
					XMLFactory.this.populationList = Files.readAllLines(Paths.get(POPULATION_FILE));
				}
				catch (Exception e) {
					getLogger().error("Population reading error:"+e.getMessage()); //$NON-NLS-1$
				}
			}
		};
		
		this.populationLoader.start();
	}
	
	public void loadXml() {
		try {
			this.populationLoader.join();
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			parserFactory.setValidating(true);
			parserFactory.setNamespaceAware(true);
			SAXParser parser = parserFactory.newSAXParser();
			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$ //$NON-NLS-2$
			XMLReader reader = parser.getXMLReader();
			reader.setErrorHandler(new ErrorHandler() {
				
				@Override
				public void warning(SAXParseException exception) throws SAXException {
					getLogger().warn(exception.getMessage());
				}
				
				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					getLogger().error(exception.getMessage());
					
				}
				
				@Override
				public void error(SAXParseException exception) throws SAXException {
					getLogger().error(exception.getMessage());
				}
			});
			
			reader.parse(new InputSource(this.flightsXML));
			parser.parse(this.flightsXML, this);
		}
		catch (Exception e) {
			getLogger().error("XML loading error:"+e.getMessage()); //$NON-NLS-1$
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException, DateTimeParseException {
		switch (qName) {
		case Airline.NODE_NAME : this.currentAirline = new Airline(attributes.getValue(Airline.ATTR_NAME));
								 this.airlines.add(this.currentAirline);
				return;

		case Flight.NODE_NAME : 
			createFlight(attributes);
			return;
			
		default:
			return;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals(Flight.NODE_FLIGHTS))
			this.currentAirline = null;
	}
	
	private void createFlight(Attributes attributes) throws DateTimeParseException {
		City from = createCity(attributes.getValue(Flight.ATTR_FROM));
		if (from==null) 
			return;
		
		City to  = createCity(attributes.getValue(Flight.ATTR_TO));
		if (to==null)
			return;
		
		Optional<Flight> fl = new Flight.Builder()
								.id(attributes.getValue(Flight.ATTR_ID))
								.departure(attributes.getValue(Flight.ATTR_DEPARTURE))
								.during(attributes.getValue(Flight.ATTR_DURING))
								.airLine(this.currentAirline)
								.source(from)
								.destination(to)
								.distance(attributes.getValue(Flight.ATTR_DISTANCE))
								.get();
		if (!fl.isPresent())
			return;
		
		this.flights.add(fl.get());
	}
	
	private City createCity(String name) {
		int population = getPopulation(name);
		if (population<=0) {
			getLogger().error(String.format("Population of %s is not found", name)); //$NON-NLS-1$
			return null;
		}
		
		City city = new City(name, population);
		if (!this.cities.contains(city))
			this.cities.add(city);
		
		return city;
	}
	
	private int getPopulation(String city) {
		String pattern = "^"+city+",.*$"; //$NON-NLS-1$ //$NON-NLS-2$
		Optional<String> line = this.populationList.stream().skip(1).filter(l->l.matches(pattern)).findFirst(); 
		if (line.isPresent()) {
			String[] fields = line.get().split(",");//$NON-NLS-1$
			if (fields != null && fields.length>5)
				if (fields[0].equals(city)) 
					return Math.round(Float.parseFloat(fields[4]));
				
		}
		getLogger().error(String.format("Cannot find city %s in population database", city)); //$NON-NLS-1$
		return -1;
	}
}

package com.zamek.flight;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class City {

	private String name;
	private int population;
	private Set<Flight> flights;
	
	public City(String name, int population) {
		this.name = name;
		this.population = population;
		this.flights = new HashSet<>();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (!StringUtils.isBlank(name))
			this.name = name;
	}

	public int getPopulation() {
		return this.population;
	}

	public void setPopulation(int population) {
		if (population>=0)
			this.population = population;
	}

	public void addFlight(Flight flight) {
		if (!this.flights.contains(flight))
			this.flights.add(flight);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + this.population;
		return result;
	}

	public int getFligtsCounts() {
		return this.flights.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.population != other.population)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%d)", this.name, Integer.valueOf(this.population)); //$NON-NLS-1$
	}

	
}

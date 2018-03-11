package com.zamek.flight;

import org.apache.commons.lang3.StringUtils;
/**
 * City of flights
 * 
 * @author zamek
 *
 */
public class City {

	private String name;
	private int population;
	
	/**
	 * Constructor for city 
	 * @param name name of the city
	 * @param population population of the city
	 */
	public City(String name, int population) {
		this.name = name;
		this.population = population;
	}

	/**
	 * Getter for name of the city
	 * @return name of the city
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter for name of the city
	 * @param name name of the city
	 */
	public void setName(String name) {
		if (!StringUtils.isBlank(name))
			this.name = name;
	}

	/**
	 * Getter for population
	 * @return population
	 */
	public int getPopulation() {
		return this.population;
	}

	/**
	 * Setter for population 
	 * @param population cannot be negative
	 */
	public void setPopulation(int population) {
		if (population>=0)
			this.population = population;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + this.population;
		return result;
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

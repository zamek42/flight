package com.zamek.flight;

import java.util.Optional;

public class Flight {
	public final static String NODE_FLIGHTS = "flights"; //$NON-NLS-1$
	public final static String NODE_NAME = "flight"; //$NON-NLS-1$
	
	public final static String ATTR_ID = "flightId"; //$NON-NLS-1$
	public final static String ATTR_FROM = "from"; //$NON-NLS-1$
	public final static String ATTR_TO = "to"; //$NON-NLS-1$
	public final static String ATTR_DISTANCE = "distance"; //$NON-NLS-1$
	public final static String ATTR_DURING = "during"; //$NON-NLS-1$
	
	private final static float AVERAGE_SPEED_KM_PER_HOUR = 600.0f;
	private final static boolean CALCULATE_DURING = true;
	
	public static class Builder {
		private Flight flight;
		
		public Builder() {
			this.flight = new Flight();
		}
		
		public Builder id(String id) {
			this.flight.setId(id);
			return this;
		}
		
		public Builder source(City from) {
			this.flight.setSource(from);
			return this;
		}
		
		public Builder destination(City to) {
			this.flight.setDestination(to);
			return this;
		}
		
		public Builder airLine(Airline al) {
			this.flight.setAirline(al);
			return this;
		}
		
		public Builder distance(String distance) throws NumberFormatException {
			this.flight.setDistance(Integer.parseInt(distance));
			return this;
		}
		
		public Builder setDuring(String during) throws NumberFormatException {
			this.flight.setDuring(Integer.parseInt(during));
			return this;
		}
		
		public Optional<Flight> get() {
			if (CALCULATE_DURING && this.flight.getDuring()<=1) {
				int d = Math.round(this.flight.getDistance() / AVERAGE_SPEED_KM_PER_HOUR * 60);
				this.flight.setDuring(d);
			}
			return this.flight.getId() != null &&
					this.flight.getAirline()!=null &&
				   this.flight.getDistance()>0 &&
				   this.flight.getSource() != null &&
				   this.flight.getDestination() != null &&
				   this.flight.getDuring()>0
				   ? Optional.of(this.flight)
				   : Optional.empty();
		}
	}
	
	private String id;
	private City source;
	private City destination;
	private Airline airline;
	private int distance;
	private int during;
	
	public Flight() {
	}

	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public City getSource() {
		return this.source;
	}

	public void setSource(City from) {
		if (from != null)
			this.source = from;
	}

	public City getDestination() {
		return this.destination;
	}

	public void setDestination(City to) {
		if (to!=null)
			this.destination = to;
	}

	public Airline getAirline() {
		return this.airline;
	}

	public void setAirline(Airline airline) {
		if (airline!=null)
			this.airline = airline;
	}

	
	public int getDistance() {
		return this.distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDuring() {
		return this.during;
	}

	public void setDuring(int during) {
		this.during = during;
	}

	public int getWeight() {
		return this.distance;
	}
	
	public Flight getReverse() {
		Flight fl = new Flight();
		fl.setId(this.id+"R");//$NON-NLS-1$
		fl.setAirline(this.airline);
		fl.setDistance(this.distance);
		fl.setDuring(this.during);
		fl.setSource(this.destination);
		fl.setDestination(this.source);
		return fl;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.airline == null) ? 0 : this.airline.hashCode());
		result = prime * result + ((this.destination == null) ? 0 : this.destination.hashCode());
		result = prime * result + this.distance;
		result = prime * result + this.during;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.source == null) ? 0 : this.source.hashCode());
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
		Flight other = (Flight) obj;
		if (this.airline == null) {
			if (other.airline != null)
				return false;
		} else if (!this.airline.equals(other.airline))
			return false;
		if (this.destination == null) {
			if (other.destination != null)
				return false;
		} else if (!this.destination.equals(other.destination))
			return false;
		if (this.distance != other.distance)
			return false;
		if (this.during != other.during)
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.source == null) {
			if (other.source != null)
				return false;
		} else if (!this.source.equals(other.source))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s->%s (%dKm %d minutes) by %s", this.id, this.source, this.destination, //$NON-NLS-1$
					Integer.valueOf(this.distance), Integer.valueOf(this.during), this.airline.getName()); 
	}
	
}

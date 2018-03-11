package com.zamek.flight;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
/**
 * A flight of an airline
 * 
 * @author zamek
 *
 */
public class Flight {
	public final static String NODE_FLIGHTS = "flights"; //$NON-NLS-1$
	public final static String NODE_NAME = "flight"; //$NON-NLS-1$
	
	public final static String ATTR_ID = "flightId"; //$NON-NLS-1$
	public final static String ATTR_FROM = "from"; //$NON-NLS-1$
	public final static String ATTR_TO = "to"; //$NON-NLS-1$
	public final static String ATTR_DISTANCE = "distance"; //$NON-NLS-1$
	public final static String ATTR_DURING = "during"; //$NON-NLS-1$
	public final static String ATTR_DEPARTURE = "departure"; //$NON-NLS-1$
	
		
	/**
	 * Builder for flight
	 * 
	 * @author zamek
	 *
	 */
	public static class Builder {
		
		private final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME; 
		private Flight flight;
		
		public Builder() {
			this.flight = new Flight();
		}
		
		/**
		 * Setting flight id
		 * 
		 * @param id of flight
		 * @return the builder
		 */
		public Builder id(String id) {
			this.flight.setId(id);
			return this;
		}
		
		/**
		 * Set departure time for the flight
		 * 
		 * @param time departure time as a string
		 * @return the builder
		 * @throws DateTimeParseException if any error occurs
		 */
		public Builder departure(String time) throws DateTimeParseException {
			this.flight.setDeparture(LocalTime.parse(time, TIME_FORMATTER));
			return this;
		}
		
		/**
		 * Set source city for a flight
		 * 
		 * @param from city
		 * @return the builder
		 */
		public Builder source(City from) {
			this.flight.setSource(from);
			return this;
		}
		
		/**
		 * Set destination city for a flight
		 * 
		 * @param to destionation sity
		 * @return the builder
		 */
		public Builder destination(City to) {
			this.flight.setDestination(to);
			return this;
		}
		
		/**
		 * Set airline for a flight
		 * 
		 * @param al airline
		 * @return the builder
		 */
		public Builder airLine(Airline al) {
			this.flight.setAirline(al);
			return this;
		}
		
		/**
		 * Set distance for a flight
		 * 
		 * @param distance as a string
		 * @return the builder
		 * @throws NumberFormatException if any arror occurs
		 */
		public Builder distance(String distance) throws NumberFormatException {
			this.flight.setDistance(Integer.parseInt(distance));
			return this;
		}
		
		/**
		 * Set duration time for a flight
		 * @param during as a string
		 * @return the builder
		 * @throws DateTimeParseException if any error occurs
		 */
		public Builder during(String during) throws DateTimeParseException {
			this.flight.setDuring(LocalTime.parse(during,TIME_FORMATTER));
			return this;
		}
		
		/**
		 * Get a flight if no error. 
		 * 
		 * @return flight if all checkings are successfully or empty optional if not
		 */
		public Optional<Flight> get() {
			return this.flight.getId() != null &&
					this.flight.getAirline()!=null &&
				   this.flight.getDistance()>0 &&
				   this.flight.getSource() != null &&
				   this.flight.getDestination() != null &&
				   this.flight.getDuring()!=null 
				   ? Optional.of(this.flight)
				   : Optional.empty();
		}
	}
	
	private final static DateTimeFormatter DEPARTURE_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;
	private final static DateTimeFormatter DURATION_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); //$NON-NLS-1$
	private String id;
	private City source;
	private City destination;
	private Airline airline;
	private int distance;
	private LocalTime during;
	private LocalTime departure;
	
	/**
	 * Deafult constructor for Flight
	 */
	public Flight() {
	}

	/**
	 * Get flight id
	 * 
	 * @return id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Set an id for a flight
	 * 
	 * @param id flight id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Getter for source city of a flight
	 * 
	 * @return source city
	 */
	public City getSource() {
		return this.source;
	}

	/**
	 * Setter for source city of a flight
	 * 
	 * @param from source city, does nothing if null
	 */
	public void setSource(City from) {
		if (from != null)
			this.source = from;
	}

	/**
	 * Getter for destination city for a flight
	 *  
	 * @return destination city
	 */
	public City getDestination() {
		return this.destination;
	}

	/**
	 * Setter for destination of a flight
	 * 
	 * @param to destination city, does nothing if null
	 */
	public void setDestination(City to) {
		if (to!=null)
			this.destination = to;
	}

	/**
	 * Getter for airline of a flight
	 * 
	 * @return airline
	 */
	public Airline getAirline() {
		return this.airline;
	}

	/**
	 * Setter for airline of a flight
	 * 
	 * @param airline does nothing if null
	 */
	public void setAirline(Airline airline) {
		if (airline!=null)
			this.airline = airline;
	}

	/**
	 * Getter for distance of a flight in Km
	 * 
	 * @return distance in Km
	 */
	public int getDistance() {
		return this.distance;
	}

	/**
	 * Setter for distance of flight
	 * 
	 * @param distance in Km
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * Getter for during time of a flight
	 * 
	 * @return during time
	 */
	public LocalTime getDuring() {
		return this.during;
	}

	/**
	 * Setter for during time of a flight
	 * 
	 * @param during time
	 */
	public void setDuring(LocalTime during) {
		this.during = during;
	}
	
	/**
	 * Getter for departure time of a flight
	 * 
	 * @return the departure time 
	 */
	public LocalTime getDeparture() {
		return this.departure;
	}

	/**
	 * Setter for departure time of a flight
	 * 
	 * @param departure the departure time
	 */
	public void setDeparture(LocalTime departure) {
		this.departure = departure;
	}
	
	/**
	 * Formatting a duration time for the HH:mm or mm format
	 * 
	 * @param d input duration
	 * @return formatted string
	 */
	public static String getFormattedDuring(Duration d) {
		long hour = d.toHours();
		long min = d.minusHours(hour).toMinutes();
		if (hour==0)
			return String.format("%d %s", Long.valueOf(min), Messages.getString("Flight.minutes")); //$NON-NLS-1$ //$NON-NLS-2$
		
		return String.format("%d %s %d %s", Long.valueOf(hour), Messages.getString("Flight.hours"), //$NON-NLS-1$ //$NON-NLS-2$ 
				Long.valueOf(min), Messages.getString("Flight.minutes")); //$NON-NLS-1$
	}

	/**
	 * Adding the duration time of the flight to a time 
	 * @param t input time
	 * @return the incremented time
	 */
	public LocalTime addDuration(LocalTime t) {
		return t.plus(this.during.toNanoOfDay(), ChronoUnit.NANOS);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.airline == null) ? 0 : this.airline.hashCode());
		result = prime * result + ((this.departure == null) ? 0 : this.departure.hashCode());
		result = prime * result + ((this.destination == null) ? 0 : this.destination.hashCode());
		result = prime * result + this.distance;
		result = prime * result + ((this.during == null) ? 0 : this.during.hashCode());
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
		if (this.departure == null) {
			if (other.departure != null)
				return false;
		} else if (!this.departure.equals(other.departure))
			return false;
		if (this.destination == null) {
			if (other.destination != null)
				return false;
		} else if (!this.destination.equals(other.destination))
			return false;
		if (this.distance != other.distance)
			return false;
		if (this.during == null) {
			if (other.during != null)
				return false;
		} else if (!this.during.equals(other.during))
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
		return String.format(Messages.getString("Flight.tostring"), this.id, this.source.getName(), this.destination.getName(),  //$NON-NLS-1$
					Integer.valueOf(this.distance), DEPARTURE_FORMATTER.format(this.departure), 
					DURATION_FORMATTER.format(this.during), this.airline.getName()); 
	}
	
}

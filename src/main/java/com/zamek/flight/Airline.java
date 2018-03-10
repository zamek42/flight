package com.zamek.flight;

import org.apache.commons.lang3.StringUtils;

public class Airline {
	public final static String NODE_NAME ="airline"; //$NON-NLS-1$
	public final static String ATTR_NAME = "name"; //$NON-NLS-1$
	
	private String name;
	
	public Airline(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (!StringUtils.isBlank(name))
			this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
		Airline other = (Airline) obj;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}

package com.zamek.flight.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper interface for logging
 * 
 * @author zamek
 *
 */
public interface HasLogger {
	
	/**
	 * Getter for the logger
	 * 
	 * @return logger
	 */
	default Logger getLogger() {
		return LoggerFactory.getLogger(this.getClass().getName());
	}

}

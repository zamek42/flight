package com.zamek.flight.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface HasLogger {
	
	default Logger getLogger() {
		return LoggerFactory.getLogger(this.getClass().getName());
	}

}

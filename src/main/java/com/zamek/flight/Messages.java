package com.zamek.flight;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * I18n utility for the application
 * 
 * @author zamek
 *
 */
public class Messages {
	private static final String BUNDLE_NAME = "com.zamek.flight.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	/**
	 * Get an i18n message 
	 * @param key key of the message
	 * @return the message from i18n
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	/**
	 * Get an i18n message formatted with String.format
	 * @param key key of the message
	 * @param args arguments for the message
	 * @return the message from i18n
	 */
	public static String getString(String key, String ...args) {
		try {
			return String.format(RESOURCE_BUNDLE.getString(key), args);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
		
	}
}

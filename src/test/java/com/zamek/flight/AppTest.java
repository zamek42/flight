package com.zamek.flight;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * Unit test for simple App.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses ({
	FactoryTest.class,
	DijkstraTest.class
})

public class AppTest{
	public final static String XML_FILE_NAME = "data/flights.xml"; //$NON-NLS-1$

	//NC.
}
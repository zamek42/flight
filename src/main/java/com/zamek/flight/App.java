package com.zamek.flight;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *  Task:
 * 
 * 	There is an xml database contains flight data of different airlines:
 *  <ul>
 *  <li>Source city</li>
 *  <li>Destination city</li>
 *  <li>Distance</li>
 *  <li>During time</li>
 *  <li>Departure time</li>
 *  <li>Airline</li>
 *  </ul>
 *  
 *  There are cities in csv file which contains population data of a city.
 *  
 *  Application can load XML and population data from xml and csv file and creates a memory database combining this data.
 *  User can lists all airlines, flights, flights of an airline and can find a route between a source and destination cities. 
 *  Optionally can set an airline as an argument of find route.
 *  
 *  Usage of application:
 *  <ul>
 *      <li>--airline &lt;arg&gt;       With airline</li>
        <li>--airlines            Show airlines</li>
        <li>--cities              Show cities</li>
        <li>-d                    Default task: find path between city of lowest population and city of highest population</li>
        <li>--destination &lt;arg&gt;   Set destination</li>
        <li>--flights             Show flights</li>
        <li>--source &lt;arg&gt;        Set source</li>
        <li>--xml &lt;FILENAME&gt;      Name of the XML file</li>
	</ul>
 *
 */
public class App {
	private final static String APP_NAME = "Flight"; //$NON-NLS-1$
	private final static String COMMAND_LINE_SYNTAX = "java -jar " + APP_NAME + ".jar"; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Construct and provide GNU-compatible Options.
	 * 
	 * @return Options expected from command-line of GNU form.
	 */
	public static Options constructOptions() {

		final Options options = new Options();
		Option fileOption = Option.builder().longOpt(Messages.getString("App.optXML")) //$NON-NLS-1$
				.desc(Messages.getString("App.optXMLDetails")) //$NON-NLS-1$
				.hasArg().argName(Messages.getString("App.optXMLName")) //$NON-NLS-1$
				.required(true).build();

		final Option cityOption = Option.builder().required(false).longOpt(Messages.getString("App.optCities")) //$NON-NLS-1$
				.desc(Messages.getString("App.optCitiesDetail")) //$NON-NLS-1$
				.build();

		final Option airlinesOption = Option.builder().required(false).longOpt(Messages.getString("App.optAirlines")) //$NON-NLS-1$
				.desc(Messages.getString("App.optAirlinesDetail")) //$NON-NLS-1$
				.build();

		final Option flightsOption = Option.builder().required(false).longOpt(Messages.getString("App.optFlights")) //$NON-NLS-1$
				.desc(Messages.getString("App.optFlightsDetail")) //$NON-NLS-1$
				.build();

		final Option sourceOption = Option.builder().required(false).longOpt(Messages.getString("App.optSource")) //$NON-NLS-1$
				.desc(Messages.getString("App.optSourceDetail")) //$NON-NLS-1$
				.hasArg()
				.build();

		final Option destOption = Option.builder().required(false).longOpt(Messages.getString("App.optDestination")) //$NON-NLS-1$
				.desc(Messages.getString("App.optDestinationDetail")) //$NON-NLS-1$
				.hasArg()
				.build();

		final Option airlineOptions = Option.builder().longOpt(Messages.getString("App.optAirline")) //$NON-NLS-1$
				.desc(Messages.getString("App.optAirlineDetail")) //$NON-NLS-1$
				.hasArg()
				.required(false).build();

		final Option defaultOption = Option.builder(Messages.getString("App.optDefault")) //$NON-NLS-1$
				.required(false).hasArg(false).desc(Messages.getString("App.optDefaultDetail")) //$NON-NLS-1$
				.build();

		options.addOption(fileOption);
		options.addOption(airlinesOption);
		options.addOption(flightsOption);
		options.addOption(cityOption);
		options.addOption(sourceOption);
		options.addOption(destOption);
		options.addOption(airlineOptions);
		options.addOption(defaultOption);
		return options;
	}

	/**
	 * Find a path between source and dest optionally with airline.
	 * After find the method print the founded route elements
	 * 
	 * @param source Source city name
	 * @param dest Destination city name 
	 * @param airline name of the airline
	 */
	private static void findPath(String source, String dest, String airline) {
		Data d = Data.getInstance();
		Optional<City> os = d.findCity(source);
		if (!os.isPresent()) {
			System.out.println(Messages.getString("App.cityNotFound", source)); //$NON-NLS-1$
			return;
		}

		Optional<City> od = d.findCity(dest);
		if (!od.isPresent()) {
			System.out.println(Messages.getString("App.cityNotFound", dest)); //$NON-NLS-1$
			return;
		}

		Optional<Airline> oa = d.findAirline(airline);
		Data.printPath(os.get(), od.get(), Data.findPath(os.get(), od.get(), oa.isPresent() ? oa.get() : null));
	}

	/**
	 * Default task to find a route between the lowest population city between the highest population city
	 * Try to find route with all airlines, but none of airlines can serve this route, try to find an airline indepenedent route
	 * 
	 */
	private static void doDefault() {
		System.out.println(Messages.getString("App.optDefaultSelected")); //$NON-NLS-1$
		Optional<City> min = Data.getInstance().getCities().stream()
				.min((a, b) -> a.getPopulation() - b.getPopulation());
		Optional<City> max = Data.getInstance().getCities().stream()
				.max((a, b) -> a.getPopulation() - b.getPopulation());
		Map<Airline, LinkedList<Flight>> paths = new HashMap<>();
		for (Airline al : Data.getInstance().getAirlines()) {
			LinkedList<Flight> path = Data.findPath(min.get(), max.get(), al);
			if (path != null)
				paths.put(al, path);
		}
		if (paths.isEmpty())
			Data.printPath(min.get(), max.get(), Data.findPath(min.get(), max.get(), null));
		else
			for (Map.Entry<Airline, LinkedList<Flight>> e : paths.entrySet())
				Data.printPath(min.get(), max.get(), e.getValue());
	}

	/**
	 * Apply Apache Commons CLI GnuParser to command-line arguments.
	 * 
	 * @param commandLineArguments
	 *            Command-line arguments to be processed with Gnu-style parser.
	 */
	public static void useGnuParser(final String[] commandLineArguments) {
		final CommandLineParser cmdLineGnuParser = new DefaultParser();

		final Options gnuOptions = constructOptions();
		CommandLine commandLine;
		try {
			commandLine = cmdLineGnuParser.parse(gnuOptions, commandLineArguments);
			String xmlFile = null;
			if (commandLine.hasOption(Messages.getString("App.optXML"))) { //$NON-NLS-1$
				xmlFile = commandLine.getOptionValue(Messages.getString("App.optXML")); //$NON-NLS-1$
				Data.getInstance().load(xmlFile);
			}

			if (commandLine.hasOption(Messages.getString("App.optCities"))) { //$NON-NLS-1$
				System.out.println(Messages.getString("App.optCitiesSelected")); //$NON-NLS-1$
				Data.getInstance().getCities().stream().forEach(c -> System.out.println(c));
				return;
			}

			if (commandLine.hasOption(Messages.getString("App.optAirlines"))) { //$NON-NLS-1$
				System.out.println(Messages.getString("App.optAirlinesSelected")); //$NON-NLS-1$
				Data.getInstance().getAirlines().stream().forEach(a -> {
					System.out.println(a + ":");//$NON-NLS-1$
					Data.getInstance().getFlightOfAirline(a).stream().forEach(f -> System.out.println("\t" + f)); //$NON-NLS-1$
				});
				return;
			}

			if (commandLine.hasOption(Messages.getString("App.optFlights"))) { //$NON-NLS-1$
				System.out.println(Messages.getString("App.optFlightsSelected")); //$NON-NLS-1$
				Data.getInstance().getFlights().stream().parallel().forEach(f -> System.out.println(f));
				return;
			}

			if (commandLine.hasOption(Messages.getString("App.optSource"))) { //$NON-NLS-1$
				String source = commandLine.getOptionValue(Messages.getString("App.optSource")); //$NON-NLS-1$
				if (commandLine.hasOption(Messages.getString("App.optDestination"))) { //$NON-NLS-1$
					String dest = commandLine.getOptionValue(Messages.getString("App.optDestination"));//$NON-NLS-1$
					String airline = null;
					if (commandLine.hasOption(Messages.getString("App.optAirline"))) //$NON-NLS-1$
						airline = commandLine.getOptionValue(Messages.getString("App.optAirline")); //$NON-NLS-1$

					System.out.print(Messages.getString("App.optFindSelected", source, dest)); //$NON-NLS-1$
					if (airline != null) {
						System.out.println(" "+Messages.getString("App.optAirlineSelected", airline)); //$NON-NLS-1$ //$NON-NLS-2$
					} else
						System.out.println();
					findPath(source, dest, airline);
					return;
				}
			}
			if (commandLine.hasOption(Messages.getString("App.optDefault"))) { //$NON-NLS-1$
				doDefault();
				return;
			}
			usage();
		} catch (ParseException parseException) { // checked exception
			System.err.println(Messages.getString("App.parseException") + parseException.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Display command-line arguments without processing them in any further way.
	 * 
	 * @param commandLineArguments Command-line arguments to be displayed.
	 * @param out output stream
	 */
	public static void displayProvidedCommandLineArguments(final String[] commandLineArguments,
			final OutputStream out) {
		final StringBuffer buffer = new StringBuffer();
		for (final String argument : commandLineArguments) {
			buffer.append(argument).append(" "); //$NON-NLS-1$
		}
		try {
			out.write((buffer.toString() + "\n").getBytes()); //$NON-NLS-1$
		} catch (IOException ioEx) {
			System.err.println(Messages.getString("App.outputException") + ioEx.getMessage()); //$NON-NLS-1$
			System.out.println(buffer.toString());
		}
	}

	/**
	 * Display application header.
	 * 
	 * @param out OutputStream to which header should be written.
	 */
	public static void displayHeader(final OutputStream out) {
		final String header = Messages.getString("App.AppDetail"); //$NON-NLS-1$
		try {
			out.write(header.getBytes());
		} catch (IOException ioEx) {
			System.out.println(header);
		}
	}

	/**
	 * Display n lines of blank line
	 * 
	 * @param cnt number of lines
	 * @param out output stream
	 */
	public static void displayBlankLines(int cnt, final OutputStream out) {
		for (int i = 0; i < cnt; ++i)
			System.out.println();
	}

	/**
	 * Write "help" to the provided OutputStream.
	 * 
	 * @param options options of application
	 * @param printedRowWidth width of screen
	 * @param header header text for help
	 * @param footer footer text for help
	 * @param spacesBeforeOption number of spaces before an option
	 * @param displayUsage usage of display
	 * @param out output stream
	 */
	private static void printHelp(Options options, int printedRowWidth, String header, String footer,
			int spacesBeforeOption, int spacesBeforeOptionDescription, boolean displayUsage, OutputStream out) {
		PrintWriter writer = new PrintWriter(out);
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(writer, printedRowWidth, COMMAND_LINE_SYNTAX, header, options, spacesBeforeOption,
				spacesBeforeOptionDescription, footer, displayUsage);
		writer.flush();
	}

	/**
	 * Print usage information to provided OutputStream.
	 * 
	 * @param options Command-line options to be part of usage.
	 * @param out  OutputStream to which to write the usage information.
	 */
	public static void printUsage(Options options, OutputStream out) {
		final PrintWriter writer = new PrintWriter(out);
		final HelpFormatter usageFormatter = new HelpFormatter();
		usageFormatter.printUsage(writer, 80, APP_NAME, options);
		writer.flush();
	}

	/**
	 * Print a usage message to the screen
	 */
	private static void usage() {
		System.out.println(Messages.getString("App.usage")); //$NON-NLS-1$
		printUsage(constructOptions(), System.out);

		displayBlankLines(4, System.out);

		System.out.println(Messages.getString("App.help")); //$NON-NLS-1$
		printHelp(constructOptions(), 80, Messages.getString("App.helpHeader"), Messages.getString("App.helpFooter"), 5, //$NON-NLS-1$ //$NON-NLS-2$
				3, true, System.out);
	}

	/**
	 * Main entry point of application 
	 * 
	 * @param args  list of arguments
	 */
	public static void main(String[] args) {
		displayBlankLines(1, System.out);
		displayHeader(System.out);
		displayBlankLines(2, System.out);
		if (args.length < 1) {
			usage();
		}
		displayProvidedCommandLineArguments(args, System.out);
		useGnuParser(args);
	}

}

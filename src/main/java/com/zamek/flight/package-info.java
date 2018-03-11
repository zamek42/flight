/**
 * @author zamek
 *
 * Flight finder application. 
 * 
 * Task:
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
package com.zamek.flight;
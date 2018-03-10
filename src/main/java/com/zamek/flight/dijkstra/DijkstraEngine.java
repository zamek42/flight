package com.zamek.flight.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zamek.flight.City;
import com.zamek.flight.Flight;


public class DijkstraEngine {

    private final List<Flight> flights;
    private Set<City> settledNodes;
    private Set<City> unSettledNodes;
    private Map<City, City> predecessors;
    private Map<City, Integer> distance;

    public DijkstraEngine(Graph graph) {
        this.flights = new ArrayList<>(graph.getEdges());
    }

    public void execute(City source) {
        this.settledNodes = new HashSet<>();
        this.unSettledNodes = new HashSet<>();
        this.distance = new HashMap<>();
        this.predecessors = new HashMap<>();
        this.distance.put(source, Integer.valueOf(0));
        this.unSettledNodes.add(source);
        while (this.unSettledNodes.size() > 0) {
            City node = getMinimum(this.unSettledNodes);
            this.settledNodes.add(node);
            this.unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(City node) {
        List<City> adjacentNodes = getNeighbors(node);
        for (City target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                this.distance.put(target, Integer.valueOf(getShortestDistance(node) + getDistance(node, target)));
                this.predecessors.put(target, node);
                this.unSettledNodes.add(target);
            }
        }

    }

    private int getDistance(City node, City target) {
        for (Flight f : this.flights) {
            if (f.getSource().equals(node)
                    && f.getDestination().equals(target)) {
                return f.getWeight();
            }
        }
        throw new RuntimeException("Should not happen"); //$NON-NLS-1$
    }

    private List<City> getNeighbors(City node) {
        List<City> neighbors = new ArrayList<>();
        for (Flight f : this.flights) {
            if (f.getSource().equals(node)
                    && !isSettled(f.getDestination())) {
                neighbors.add(f.getDestination());
            }
        }
        return neighbors;
    }

    private City getMinimum(Set<City> Cities) {
        City minimum = null;
        for (City City : Cities) {
            if (minimum == null) 
                minimum = City;
            else {
                if (getShortestDistance(City) < getShortestDistance(minimum)) 
                    minimum = City;
            }
        }
        return minimum;
    }

    private boolean isSettled(City City) {
        return this.settledNodes.contains(City);
    }

    private int getShortestDistance(City destination) {
        Integer d = this.distance.get(destination);
        return d == null ? Integer.MAX_VALUE : d.intValue();
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<City> getPath(City target) {
        LinkedList<City> path = new LinkedList<>();
        City step = target;
        // check if a path exists
        if (this.predecessors.get(step) == null) 
            return null;
        
        path.add(step);
        while (this.predecessors.get(step) != null) {
            step = this.predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }


}
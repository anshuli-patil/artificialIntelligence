package edu.usc.anshulip.hw;

public class Edge {
	PathNode startLocation; // node where edge starts
	PathNode endLocation; // node where edge ends
	int cost; // cost of traveling from startLocation to endLocation

	Edge(int cost, PathNode startLocation, PathNode endLocation) {
		this.cost = cost;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
	}

	public String toString() {
		return "(" + (startLocation.locationName) + (",") + (endLocation.locationName) + (")");

	}

	public int getKey() {
		return this.toString().hashCode();

	}

}

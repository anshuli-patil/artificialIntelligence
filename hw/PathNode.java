package edu.usc.anshulip.hw;

public class PathNode implements Comparable<PathNode> {
	String locationName;
	int accumulatedCost;
	PathNode parent;

	// To compare which node was discovered first
	int startTime = Integer.MAX_VALUE;

	// used for heuristics based search algorithms
	int estimatedGoalDistance = 0;

	PathNode(String locationName, int accumulatedCost) {
		this.locationName = locationName;
		this.accumulatedCost = accumulatedCost;
	}

	public PathNode(String locationName) {
		this.locationName = locationName;
		// initially the (new) node is marked as unreachable from the source
		// node
		this.accumulatedCost = Integer.MAX_VALUE;
	}

	@Override
	public int compareTo(PathNode o) {

		int compareValue = this.nodeCost() - o.nodeCost();

		// this is for tie-breaking!
		if (compareValue == 0) {
			// new items added will be less than existing ones.
			return (this.startTime - o.startTime);
		}

		return compareValue;
	}

	public int nodeCost() {
		return (this.accumulatedCost + this.estimatedGoalDistance);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PathNode) {
			if (this.locationName.equals(((PathNode) o).locationName)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public String toString() {
		return locationName + " " + accumulatedCost + estimatedGoalDistance;
	}

}

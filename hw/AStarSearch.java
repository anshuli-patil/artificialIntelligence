package edu.usc.anshulip.hw;

import java.util.List;
import java.util.Map;

public class AStarSearch implements ISearchAlgorithm {
	homework problem;

	@Override
	public List<PathNode> computePath(homework problem) {
		addEstimatedCosts(problem.vertices, problem.staticInfo);
		return new UniformCostSearch().computePath(problem);

	}

	private void addEstimatedCosts(Map<String, PathNode> vertices, Map<String, Integer> staticInfo) {
		// System.out.println(staticInfo.size());
		for (PathNode node : vertices.values()) {

			if (staticInfo.containsKey(node.locationName)) {
				node.estimatedGoalDistance = staticInfo.get(node.locationName);
			}
		}

	}

}

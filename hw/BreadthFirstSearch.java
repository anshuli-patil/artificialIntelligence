package edu.usc.anshulip.hw;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearch implements ISearchAlgorithm {

	homework problem;

	// extra information for bookkeeping,etc. that's specific to the algorithm
	// implementation
	Queue<PathNode> nodesQueue = new LinkedList<PathNode>();

	@Override
	public List<PathNode> computePath(homework problem) {
		this.problem = problem;
		breadthFirstSearch();
		return retrieveSrcToDestPath(problem);
	}

	private PathNode breadthFirstSearch() {

		nodesQueue.add(problem.startNode);
		while (!nodesQueue.isEmpty()) {
			PathNode currentNode = nodesQueue.remove();

			if (isGoal(currentNode, problem.destinationNode)) {
				return currentNode;
			}
			List<PathNode> children = tieBreak(expandForChildren(problem, currentNode));
			if (children != null && !children.isEmpty()) {
				for (PathNode child : children) {
					if (child.accumulatedCost == Integer.MAX_VALUE) {
						child.accumulatedCost = currentNode.accumulatedCost + 1;
						child.parent = currentNode;
						nodesQueue.add(child);
					}
				}
			}

		}
		return null;
	}

	private List<PathNode> tieBreak(List<PathNode> expandForChildren) {
		// the adjacency list is ordered already
		return expandForChildren;
	}

}

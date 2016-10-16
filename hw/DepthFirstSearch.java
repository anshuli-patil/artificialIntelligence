package edu.usc.anshulip.hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
1  procedure DFS-iterative(G,v):
2      let S be a stack
3      S.push(v)
4      while S is not empty
5          v = S.pop()
6          for all edges from v to w in G.adjacentEdges(v) do
7              if w is not labeled as discovered:
8                  label w as discovered
9                  S.push(w)
 */
public class DepthFirstSearch implements ISearchAlgorithm {

	homework problem;

	Stack<PathNode> nodes = new Stack<PathNode>();

	@Override
	public List<PathNode> computePath(homework problem) {
		this.problem = problem;
		depthFirstSearch();
		return retrieveSrcToDestPath(problem);
	}

	private PathNode depthFirstSearch() {

		nodes.push(problem.startNode);

		while (!nodes.isEmpty()) {
			PathNode currentNode = nodes.pop();

			if (isGoal(currentNode, problem.destinationNode)) {
				return currentNode;
			}
			List<PathNode> children = tieBreak(expandForChildren(problem, currentNode));

			if (children != null && !children.isEmpty()) {
				// push in reverse order to pop in order of live traffic data
				// for equivalent nodes.

				for (PathNode child : children) {

					int newPathCost = currentNode.accumulatedCost + 1;
					if (child.accumulatedCost == Integer.MAX_VALUE) {
						child.accumulatedCost = newPathCost;
						child.parent = currentNode;

						nodes.push(child);
						// System.out.println("pushing to stack
						// "+child.locationName);

					}
				}

			}
		}
		return null;
	}

	private List<PathNode> tieBreak(List<PathNode> expandForChildren) {
		// Add to stack in reverse order - to check paths in order of adjacency
		// list
		List<PathNode> reverseList = new ArrayList<PathNode>();
		if (expandForChildren == null || expandForChildren.size() == 0) {
			return null;
		}

		for (int i = expandForChildren.size() - 1; i >= 0; i--) {
			reverseList.add(expandForChildren.get(i));
		}

		return reverseList;
	}

}

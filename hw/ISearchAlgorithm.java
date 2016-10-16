package edu.usc.anshulip.hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
Function General-Search(problem, Queuing-Fn) returns a solution, or failure
nodes <- make-queue(make-node(initial-state[problem]))
loop do
	if nodes is empty then return failure
	node <- Remove-Front(nodes)
	if Goal-Test[problem] applied to State(node) succeeds then return node
	nodes <- Queuing-Fn(nodes, Expand(node, Operators[problem]))
end
*/

interface ISearchAlgorithm {
	public abstract List<PathNode> computePath(homework problem);

	public static ISearchAlgorithm getAlgorithmExecutorByType(String algorithmName) {

		if (algorithmName.equals("BFS")) {
			return new BreadthFirstSearch();
		} else if (algorithmName.equals("UCS")) {
			return new UniformCostSearch();
		} else if (algorithmName.equals("A*")) {
			return new AStarSearch();
		} else if (algorithmName.equals("DFS")) {
			return new DepthFirstSearch();
		}
		return null;

	}

	public default List<PathNode> retrieveSrcToDestPath(homework problem) {
		List<PathNode> pathToDestination = new ArrayList<PathNode>();
		pathToDestination.add(problem.startNode);
		Stack<PathNode> s = new Stack<PathNode>();
		PathNode iterator = problem.destinationNode;

		while (!iterator.locationName.equals(problem.startNode.locationName)) {
			s.push(iterator);
			iterator = iterator.parent;
		}

		while (!s.isEmpty()) {
			PathNode p = s.pop();
			pathToDestination.add(p);
		}
		return pathToDestination;
	}

	// can be overridden in implementation if different implementation is needed
	public default List<PathNode> expandForChildren(homework problem, PathNode currentNode) {
		List<PathNode> childrenNodes = problem.outgoingEdges.get(currentNode.locationName);
		return childrenNodes;
	}

	// goal test method - can be used in implementation with more specific goal
	// conditions
	public default boolean isGoal(PathNode currentNode, PathNode destinationNode) {
		return (currentNode.locationName.equals(destinationNode.locationName));
	}

}

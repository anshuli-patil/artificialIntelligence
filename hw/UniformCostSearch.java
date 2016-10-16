package edu.usc.anshulip.hw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/*
Function UniformCost-Search(problem, Queuing-Fn) returns a solution, or failure
	open <- make-queue(make-node(initial-state[problem]))
	closed <- [empty]
	loop do
		currnode <- Remove-Front(open)
		if Goal-Test[problem] applied to State(currnode) then return currnode
		children <- Expand(currnode, Operators[problem])
		
		while children not empty
			child <- Remove-Front(children)
			if no node in open or closed has child’s state
				open <- Queuing-Fn(open, child)
			else if there exists node in open that has child’s state
				if PathCost(child) < PathCost(node)
					open <- Delete-Node(open, node)
					open <- Queuing-Fn(open, child)
			else if there exists node in closed that has child’s state
				if PathCost(child) < PathCost(node)
					closed <- Delete-Node(closed, node)
					open <- Queuing-Fn(open, child)
		end

		closed <- Insert(closed, currnode)
		open <- Sort-By-PathCost(open)
	end
 */
public class UniformCostSearch implements ISearchAlgorithm {
	homework problem;

	// extra information for bookkeeping,etc. that's specific to the algorithm
	// implementation
	// Queues used to keep track of the exploration frontier
	Queue<PathNode> open = new PriorityQueue<PathNode>();
	Queue<PathNode> closed = new PriorityQueue<PathNode>();

	Map<String, Boolean> queueInfo = new HashMap<String, Boolean>();

	private final boolean closedQueue = false;
	private final boolean openQueue = true;

	private int globalTime = 0;

	@Override
	public List<PathNode> computePath(homework problem) {
		this.problem = problem;
		uniformCostSearch();
		return retrieveSrcToDestPath(problem);
	}

	PathNode uniformCostSearch() {
		addToQueue(problem.startNode, openQueue);

		while (true) {
			PathNode currentNode = removeFromQueue(openQueue);

			if (isGoal(currentNode, problem.destinationNode)) {
				System.out.println(currentNode.accumulatedCost);
				return currentNode;
			}
			List<PathNode> children = tieBreak(expandForChildren(problem, currentNode));

			for (int i = 0; i < children.size(); i++) {
				examineChildNode(currentNode, children.get(i));
			}

			addToQueue(currentNode, closedQueue);
			sortByPathCost(open);
		}
	}

	private List<PathNode> tieBreak(List<PathNode> expandForChildren) {
		return expandForChildren;
	}

	private void examineChildNode(PathNode currentNode, PathNode child) {

		Edge matchingKeyEdge = new Edge(Integer.MAX_VALUE, currentNode, child);
		int costOfEdge = problem.edgeMap.get(matchingKeyEdge.getKey()).cost;
		int costFromParent = currentNode.accumulatedCost + costOfEdge;

		// pick out node from queue which has the same ID (locationName) as
		// child
		PathNode node = problem.vertices.get(child.locationName);

		if (!containsObject(openQueue, child.locationName) && !containsObject(closedQueue, child.locationName)) {

			updateNodeDetails(child, costFromParent, currentNode);

			addToQueue(child, openQueue);

		} else if (containsObject(openQueue, child.locationName)) {
			if (costFromParent < node.accumulatedCost) {
				updateNodeDetails(node, costFromParent, currentNode);

				// removing an re-inserting to maintain priority ordering of
				// open Queue
				deleteNode(node, openQueue);
				addToQueue(child, openQueue);
			}
		} else if (containsObject(closedQueue, child.locationName)) {
			if (costFromParent < node.accumulatedCost) {
				updateNodeDetails(child, costFromParent, currentNode);

				deleteNode(node, closedQueue);
				addToQueue(child, openQueue);
			}
		}

	}

	private void updateNodeDetails(PathNode child, int costFromParent, PathNode currentNode) {
		child.accumulatedCost = costFromParent;
		child.parent = currentNode;
	}

	private void deleteNode(PathNode node, boolean fromOpenQueue) {
		if (fromOpenQueue) {
			open.remove(node);
		} else {
			closed.remove(node);
		}
		queueInfo.remove(node.locationName);
	}

	private void addToQueue(PathNode node, boolean fromOpenQueue) {
		if (fromOpenQueue) {
			open.add(node);
			node.startTime = globalTime;
			globalTime++;

			queueInfo.put(node.locationName, true);
		} else {
			closed.add(node);
			queueInfo.put(node.locationName, false);
		}
	}

	// poll
	private PathNode removeFromQueue(boolean fromOpenQueue) {
		PathNode node;
		if (fromOpenQueue) {
			node = open.remove();
		} else {
			node = closed.remove();
		}
		queueInfo.remove(node.locationName);
		return node;
	}

	private boolean containsObject(boolean fromOpenQueue, String child) {
		return (queueInfo.containsKey(child) && queueInfo.get(child) == fromOpenQueue);
	}

	private void sortByPathCost(Queue<PathNode> q) {
		// nothing to do; q is a priority Queue.
	}

}

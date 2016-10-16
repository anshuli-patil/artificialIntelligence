package edu.usc.anshulip.hw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class homework {

	// inputs for the homework problem solvable by search techniques
	ISearchAlgorithm algoType;
	PathNode startNode;
	PathNode destinationNode;

	// stores a edgeID as key to retrieve edge details
	Map<Integer, Edge> edgeMap;
	// Name of node with outgoing edge(s) as key and list of nodes that have an
	// incoming edge from that key-node
	Map<String, List<PathNode>> outgoingEdges;

	// vertex name as key and PathNode description as the value retrievable
	Map<String, PathNode> vertices;
	// The info about Sunday's traffic is stored in this map, key is node name
	// of Start position
	Map<String, Integer> staticInfo;

	public static void main(String[] args) {
		homework hw = new homework();
		hw.readProblemInstance();

		List<PathNode> solutionPath = hw.algoType.computePath(hw);
		hw.printSolutionToFile(solutionPath);
	}

	public void readProblemInstance() {
		BufferedReader bufferedFileReader = null;
		try {

			// initialize all the maps that store graph info
			edgeMap = new HashMap<Integer, Edge>();
			outgoingEdges = new HashMap<String, List<PathNode>>();
			vertices = new HashMap<String, PathNode>();
			staticInfo = new HashMap<String, Integer>();

			// read input from the input file in the prescribed format
			// and populate the data in the data structures created to define
			// the problem
			bufferedFileReader = new BufferedReader(new FileReader("input.txt"));

			algoType = ISearchAlgorithm.getAlgorithmExecutorByType(read(bufferedFileReader));
			String startState = read(bufferedFileReader);
			startNode = new PathNode(startState, 0);
			vertices.put(startState, startNode);

			String endState = read(bufferedFileReader);
			destinationNode = new PathNode(endState);
			vertices.put(endState, destinationNode);

			String lineInput = null;

			// Store the path cost info in the representative graph data
			// structure
			int numOfLiveTrafficInfo = Integer.parseInt(read(bufferedFileReader));
			for (int i = 0; i < numOfLiveTrafficInfo; i++) {
				lineInput = read(bufferedFileReader);
				String[] inputLineParts = lineInput.split(" ");
				int cost = Integer.parseInt(inputLineParts[2]);
				PathNode incomingVertex = addVertex(inputLineParts[1]);
				PathNode outgoingVertex = addVertex(inputLineParts[0]);

				Edge newEdge = new Edge(cost, outgoingVertex, incomingVertex);
				int key = newEdge.getKey();
				edgeMap.put(key, newEdge);

				addInOutgoingEdgesList(newEdge);
			}

			// for use with heuristic function
			int numOfSundayTrafficInfo = Integer.parseInt(read(bufferedFileReader));
			for (int i = 0; i < numOfSundayTrafficInfo; i++) {
				lineInput = read(bufferedFileReader);
				String[] inputLineParts = lineInput.split(" ");

				int cost = Integer.parseInt(inputLineParts[1]);
				staticInfo.put(inputLineParts[0], cost);
			}

		} catch (IOException e) {
			// do nothing
		} finally {
			close(bufferedFileReader);
		}

	}

	private PathNode addVertex(String nodeName) {
		if (!vertices.containsKey(nodeName)) {
			PathNode v = new PathNode(nodeName);
			vertices.put(nodeName, v);
			return v;
		} else {
			return vertices.get(nodeName);
		}
	}

	private void addInOutgoingEdgesList(Edge newEdge) {
		if (outgoingEdges.containsKey(newEdge.startLocation.locationName)) {
			outgoingEdges.get(newEdge.startLocation.locationName).add(newEdge.endLocation);
		} else {
			List<PathNode> newList = new ArrayList<PathNode>();
			newList.add(newEdge.endLocation);
			outgoingEdges.put(newEdge.startLocation.locationName, newList);
		}

	}

	private void close(Closeable ioClass) {
		if (ioClass != null) {
			try {
				ioClass.close();
			} catch (IOException e) {
				// do nothing
			}
		}

	}

	public String read(BufferedReader bufferedFileReader) {
		try {
			return bufferedFileReader.readLine().trim();
		} catch (IOException e) {
			// do nothing
		}
		return null;
	}

	public void printSolutionToFile(List<PathNode> solutionPath) {

		BufferedWriter bufferedFileWriter = null;
		try {
			bufferedFileWriter = new BufferedWriter(new FileWriter("output.txt"));
			for (PathNode node : solutionPath) {
				String nodeInfo = String.format("%s %s\n", node.locationName, node.accumulatedCost);
				bufferedFileWriter.write(nodeInfo);
			}

		} catch (IOException e) {
			// do nothing
		} finally {
			close(bufferedFileWriter);
		}

	}

}

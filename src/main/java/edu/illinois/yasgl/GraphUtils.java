package edu.illinois.yasgl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class GraphUtils<V> {
	
	public static <V> GraphUtils<V> getInstance() {
		return new GraphUtils<V>();
	}
	
	public static <V> Map<V, Set<V>> computeTransitiveClosure(Graph<V> graph) {
		ConcurrentHashMap<V, Set<V>> forwardReach = new ConcurrentHashMap<>();
		new DFSTask<V>(graph, forwardReach).run();
		
		return forwardReach;
	}

	public static <V> Map<V, Set<V>> computeBackwardsTransitiveClosure(Graph<V> graph) {
		ConcurrentHashMap<V, Set<V>> backwardReach = new ConcurrentHashMap<>();
		new DFSTask<V>(graph.inverse(), backwardReach).run();
		
		return backwardReach;
	}
	
	public static <V> Map<V, Integer> longestPaths(Graph<V> graph) {
		Map<V, Integer> lengths = new HashMap<>();
		
		graph.getVertices().stream().forEach(vertex -> longestPaths(vertex, graph, lengths));
		
		return lengths;		
	}

	private static <V> int longestPaths(V vertex, Graph<V> graph, Map<V, Integer> lengths) {

		if(!lengths.containsKey(vertex)) {
			lengths.put(vertex, 0);
			OptionalInt optional = graph.getSuccessors(vertex).stream().mapToInt(v -> longestPaths(v, graph, lengths)).max();
		
			lengths.put(vertex, optional.orElse(0) + 1);
		}
		return lengths.get(vertex);
	}

	public static <V> List<V> computeAnyPath(Graph<V> graph, V source, Set<V> destinations) {
		Stack<V> stack = new Stack<V>();
		stack.push(source);
		Set<V> visited = new HashSet<V>();
		LinkedList<V> path = new LinkedList<V>();
		Map<V, V> parents = new HashMap<V, V>(); 

		V current = source;
		while (!stack.isEmpty()) {
			current = stack.pop();
			
			if (destinations.contains(current))
				break;
			
			for (V vertex : graph.getSuccessors(current)) {
				if (visited.add(vertex)) {
					stack.push(vertex);
					parents.put(vertex, current);
				}
			}
		}
		
		while(current != null){
			path.addFirst(current);
			current = parents.get(current);
		}
		
		return path;
	}

	/**
	 * This method computes one distance from source to destination. This
	 * distance is used as upper limit when executing depth limited search. If
	 * it does not find it, it returns 0.
	 * 
	 */
	public static <V> int computeDFS(Graph<V> graph, V source, Set<V> destinations) {
		Set<V> visited = new HashSet<V>();
		visited.add(source);

		return computeDFSHelper(graph, source, destinations, visited);
	}

	private static <V> int computeDFSHelper(Graph<V> graph, V source, Set<V> destinations, Set<V> visited) {
		if (destinations.contains(source))
			return 1;
		for (V vertex : graph.getSuccessors(source)) {
			if (visited.add(vertex)) {
				int val = computeDFSHelper(graph, vertex, destinations, visited);
				if (val > 0) {
					return val + 1;
				}
			}
		}
		return 0;

	}
	
	/**
	 * Computes the shortest path from source to any of the destinations using
	 * depth limited search. It first needs to obtain limit by running dfs. If
	 * dfs returns 0, it means there is no path to the destination. If there is
	 * no path to destination, computeShortestPath returns empty array.
	 * 
	 */

	public static <V> List<V> computeShortestPath(Graph<V> graph, V source, Set<V> destinations) {

		int maxDepth = computeDFS(graph, source, destinations);
		LinkedList<V> path = new LinkedList<V>();
		if (maxDepth == 0)
			return path;

		for (int i = 0; i < maxDepth; i++) {
			if (computeDepthLimitedDFS(graph, source, destinations, i, path)) {
				break;
			}
		}
		path.add(0, source);
		return path;
	}

	private static <V> boolean computeDepthLimitedDFS(Graph<V> graph, V source, Set<V> destinations, int limit,
			LinkedList<V> path) {

		if (destinations.contains(source)) {
			return true;
		}
		if (limit <= 0) {
			return false;
		}

		for (V vertex : graph.getSuccessors(source)) {
			if (computeDepthLimitedDFS(graph, vertex, destinations, limit - 1, path)) {
				path.addFirst(vertex);
				return true;
			}
		}
		return false;

	}
	
	private final double ERR = 0.001d;
	private final double d = 0.85d;
	private boolean changed = true;
	private Map<V, Double> pr;	

	public Map<V, Double> pageRank(Graph<V> g) {
		pr = new IdentityHashMap<>();
		long N = g.getVertices().size();
		double uniform = 1.d/N;

		g.getVertices().stream().forEach(v -> pr.put(v, uniform));
		
		double ct = (1-d)/N;
		int i=0;
		while (changed) {
			changed = false;
			Map<V, Double> newPr = new IdentityHashMap<>();
			
			for (V v : g.getVertices()) {
				
				double computedPr = 0.;
				for (V pred : g.getPredecessors(v)) {
					computedPr += pr.get(pred) / (1.d * g.getSuccessors(pred).size());
				}
				
				computedPr = ct + d*computedPr;
								
				double currentRank = pr.get(v);
				if (Math.abs(computedPr - currentRank) > ERR) {
					changed = true;
				}
				newPr.put(v, computedPr);
			
			}
			pr = newPr;
		}
		return pr;
	}

}

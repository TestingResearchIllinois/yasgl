package edu.illinois.yasgl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GraphUtils {
	public static <V> Map<V, Set<V>> computeTransitiveClosure(Graph<V> graph) {
		ConcurrentHashMap<V, Set<V>> forwardReach = new ConcurrentHashMap<>();
		new DFSTask<V>(new LinkedList<V>(graph.getVertices()), graph, forwardReach).run();
		
		return forwardReach;
	}

	public static <V> Map<V, Set<V>> computeBackwardsTransitiveClosure(Graph<V> graph) {
		ConcurrentHashMap<V, Set<V>> backwardReach = new ConcurrentHashMap<>();
		new DFSTask<V>(new LinkedList<V>(graph.getVertices()), graph.inverse(), backwardReach).run();
		
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
			OptionalInt m = graph.getSuccessors(vertex).stream().mapToInt(v -> longestPaths(v, graph, lengths)).max();
		
			lengths.put(vertex, m.isPresent() ? m.getAsInt() + 1 : 1);
			
		}
		return lengths.get(vertex);
	}

}

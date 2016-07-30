package edu.illinois.yasgl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GraphUtils {

	private static <V> Collection<V> transitiveClosure(Graph<V> g, V vertex, Map<V, Collection<V>> forwardReach) {
		if (forwardReach.containsKey(vertex)) {
			return forwardReach.get(vertex);
		}
		forwardReach.put(vertex, new HashSet<>());
		forwardReach.get(vertex).add(vertex);
		for (V v : g.getSuccessors(vertex)) {
			forwardReach.get(vertex).addAll(transitiveClosure(g, v, forwardReach));
		}
		return forwardReach.get(vertex);
	}

	private static <V> Collection<V> backwardsTransitiveClosure(Graph<V> g, V vertex, Map<V, Collection<V>> backwardReach) {
		if (backwardReach.containsKey(vertex)) {
			return backwardReach.get(vertex);
		}
		backwardReach.put(vertex, new HashSet<>());
		backwardReach.get(vertex).add(vertex);
		for (V v : g.getPredecessors(vertex)) {
			backwardReach.get(vertex).addAll(backwardsTransitiveClosure(g, v, backwardReach));
		}
		return backwardReach.get(vertex);
	}

	public static <V> Map<V, Collection<V>> computeTransitiveClosure(Graph<V> graph) {
		Map<V, Collection<V>> forwardReach = new HashMap<>();
		for (V vertex : graph.getVertices()) {
			transitiveClosure(graph, vertex, forwardReach);
		}
		return forwardReach;
	}

	public static <V> Map<V, Collection<V>> computeBackwardsTransitiveClosure(Graph<V> graph) {
		Map<V, Collection<V>> backwardReach = new HashMap<>();
		for (V vertex : graph.getVertices()) {
			backwardsTransitiveClosure(graph, vertex, backwardReach);
		}
		return backwardReach;
	}

}

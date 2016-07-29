package edu.illinois.yasgl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GraphUtils {

	private <V> Collection<V> transitiveClosure(Graph<V> g, V vertex, Map<V, Collection<V>> forwardReach) {
		if (forwardReach.containsKey(vertex)) {
			return forwardReach.get(vertex);
		}
		forwardReach.put(vertex, new HashSet<>());
		for (V v : g.getSuccessors(vertex)) {
			forwardReach.get(vertex).add(v);
			forwardReach.get(vertex).addAll(transitiveClosure(g, v, forwardReach));
		}
		return forwardReach.get(vertex);
	}

	private <V> Collection<V> backwardsTransitiveClosure(Graph<V> g, V vertex, Map<V, Collection<V>> backwardReach) {
		if (backwardReach.containsKey(vertex)) {
			return backwardReach.get(vertex);
		}
		backwardReach.put(vertex, new HashSet<>());
		for (V v : g.getPredecessors(vertex)) {
			backwardReach.get(vertex).add(v);
			backwardReach.get(vertex).addAll(backwardsTransitiveClosure(g, v, backwardReach));
		}
		return backwardReach.get(vertex);
	}

	public <V> Map<V, Collection<V>> computeTransitiveClosure(Graph<V> graph) {
		Map<V, Collection<V>> forwardReach = new HashMap<>();
		for (V vertex : graph.getVertices()) {
			this.transitiveClosure(graph, vertex, forwardReach);
		}
		return forwardReach;
	}

	public <V> Map<V, Collection<V>> computeBackwardsTransitiveClosure(Graph<V> graph) {
		Map<V, Collection<V>> backwardReach = new HashMap<>();
		for (V vertex : graph.getVertices()) {
			this.backwardsTransitiveClosure(graph, vertex, backwardReach);
		}
		return backwardReach;
	}

}

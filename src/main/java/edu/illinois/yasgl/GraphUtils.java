package edu.illinois.yasgl;

import java.util.LinkedList;
import java.util.Map;
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

}

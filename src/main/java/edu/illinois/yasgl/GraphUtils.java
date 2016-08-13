package edu.illinois.yasgl;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Queue;
import java.util.Set;
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

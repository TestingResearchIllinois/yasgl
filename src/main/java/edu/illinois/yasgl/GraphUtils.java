/* The MIT License (MIT)

Copyright (c) 2016 Alex Gyori

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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

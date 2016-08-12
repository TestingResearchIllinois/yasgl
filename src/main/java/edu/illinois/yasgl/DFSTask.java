package edu.illinois.yasgl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.IdentityHashMap;

public class DFSTask<V> implements Runnable {

	private final Graph<V> graph;
	private Map<V, Set<V>> resultsMap;

	public DFSTask(Graph<V> graph, Map<V, Set<V>> results) {
		this.graph = graph;
		this.resultsMap = results;
	}

	public void run() {
		
		Queue<V> worklist = new LinkedList<>();
		worklist.addAll(this.graph.getVertices());

		graph.getVertices().stream().forEach(v -> this.resultsMap.put(v, Collections.newSetFromMap(new IdentityHashMap<V, Boolean>())));
        graph.getVertices().stream().forEach(v -> this.resultsMap.get(v).add(v));
        graph.getVertices().stream().forEach(v -> this.resultsMap.get(v).addAll(graph.getSuccessors(v)));

        this.resultsMap = Collections.unmodifiableMap(resultsMap);
        
		while (!worklist.isEmpty()) {
			V current = worklist.remove();
			Set<V> currentTc = this.resultsMap.get(current);
			
            Set<V> nexts = Collections.newSetFromMap(new IdentityHashMap<V, Boolean>());
            currentTc.stream().map(v -> this.resultsMap.get(v)).forEach(vs -> nexts.addAll(vs));

            if (currentTc.addAll(nexts)) {
				worklist.add(current);
			}
		}
	}
}

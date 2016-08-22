package edu.illinois.yasgl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class DFSTask<V> implements Runnable {

	private final Graph<V> graph;
	private Map<V, Set<V>> resultsMap;
	private final Collection<V> vertices;

	public DFSTask(Graph<V> graph, Map<V, Set<V>> results) {
		this(graph, results, graph.getVertices());
	}
	
	public DFSTask(Graph<V> graph, Map<V, Set<V>> results, Collection<V> startingPoints) {
		this.graph = graph;
		this.resultsMap = results;
		this.vertices = startingPoints;
	}

	public void run() {
		
		Queue<V> worklist = new LinkedList<>();
		worklist.addAll(this.graph.getVertices());

		this.vertices.stream().forEach(v -> this.resultsMap.put(v, Collections.newSetFromMap(new HashMap<V, Boolean>())));
        this.vertices.stream().forEach(v -> this.resultsMap.get(v).add(v));
        this.vertices.stream().forEach(v -> this.resultsMap.get(v).addAll(graph.getSuccessors(v)));

        this.resultsMap = Collections.unmodifiableMap(resultsMap);
        
		while (!worklist.isEmpty()) {
			V current = worklist.remove();
			Set<V> currentTc = this.resultsMap.get(current);
			
            Set<V> nexts = Collections.newSetFromMap(new HashMap<V, Boolean>());
            currentTc.stream().map(v -> this.resultsMap.get(v)).forEach(vs -> nexts.addAll(vs));

            if (currentTc.addAll(nexts)) {
				worklist.add(current);
			}
		}
	}
}

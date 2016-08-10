package edu.illinois.yasgl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DFSTask<V> implements Runnable {

	private final List<V> vertices;
	private final Graph<V> graph;
	private final ConcurrentHashMap<V, Set<V>> resultsMap;

	public DFSTask(List<V> vertices, Graph<V> graph, ConcurrentHashMap<V, Set<V>> results) {
		this.vertices = vertices;
		this.graph = graph;
		this.resultsMap = results;
	}

	public void run() {
		vertices.stream().forEach(vertex -> this.dfs(vertex));
	}

	private Set<V> dfs(V vertex) {
		if (this.resultsMap.putIfAbsent(vertex,
				Collections.newSetFromMap(new ConcurrentHashMap<V, Boolean>())) != null) {
			Set<V> result = this.resultsMap.get(vertex);
			return result;
		}

		this.graph.getSuccessors(vertex).stream().forEach(v -> {
			this.resultsMap.get(vertex).addAll(dfs(v));
			this.resultsMap.get(vertex).add(v);
		});

		this.resultsMap.get(vertex).add(vertex);

		Set<V> result = this.resultsMap.get(vertex);
		return this.resultsMap.get(vertex);
	}
}

package edu.illinois.yasgl;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSet;


public class DirectedGraphBuilder<V> {

    private final ImmutableMultimap.Builder<V, V> forward = ImmutableSetMultimap.builder();
    private final Collection<V> vertices = new HashSet<V>();
    
    public DirectedGraphBuilder() {
    }
    
    public DirectedGraphBuilder(DirectedGraph<V> g) {
    	this.vertices.addAll(g.vertices);
    	for (V pre : g.forward.keys()) {
    		for (V suc : g.forward.get(pre)) {
    			this.addEdge(pre, suc);
    		}
    	}
    }
    
    public void addEdge(V vertex1, V vertex2) {
        this.addVertex(vertex1);
        this.addVertex(vertex2);
        this.forward.put(vertex1, vertex2);
    }
    public void addVertex(V vertex) {
        this.vertices.add(vertex);
    }

    public DirectedGraph<V> build() {
        ImmutableMultimap<V, V> multi = this.forward.build();
        
        return new DirectedGraph<V>(multi, ImmutableSet.copyOf(vertices));
    }

}

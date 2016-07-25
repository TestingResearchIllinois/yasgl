package edu.illinois.yasgl;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

public class LabeledDirectedGraphBuilder<V, E> {
	
	private final ImmutableMultimap.Builder<V, VertexEntry<V, E>> forward = ImmutableSetMultimap.builder();
    private final Collection<V> vertices = new HashSet<V>();
    
    public LabeledDirectedGraphBuilder() {
    }
    
    public LabeledDirectedGraphBuilder(LabeledDirectedGraph<V, E> g) {
    	this.vertices.addAll(g.vertices);
    	for (V pre : g.forward.keys()) {
    		for (VertexEntry<V, E> suc : g.forward.get(pre)) {
    			this.addEdge(pre, suc.getVertex(), suc.getEdge());
    		}
    	}
    }
    
    public void addEdge(V vertex1, V vertex2, E edge) {
        this.addVertex(vertex1);
        this.addVertex(vertex2);
        this.forward.put(vertex1, new VertexEntry<V, E>(vertex2, edge));
    }
    
    public void addVertex(V vertex) {
        this.vertices.add(vertex);
    }

    public LabeledDirectedGraph<V, E> build() {
        ImmutableMultimap<V, VertexEntry<V, E>> multi = this.forward.build();
        
        return new LabeledDirectedGraph<V, E>(multi, ImmutableSet.copyOf(vertices));
    }
    
    public static class VertexEntry<V, E> {
    	private final V vertex;
    	private final E edge;
    	
    	public VertexEntry(V vertex, E edge) {
    		this.vertex = vertex;
    		this.edge = edge;
    	}

		public V getVertex() {
			return vertex;
		}

		public E getEdge() {
			return edge;
		}
		
		public String toString() {
			return "(" + this.vertex + ":" + this.edge + ")";
			
		}
    }
}

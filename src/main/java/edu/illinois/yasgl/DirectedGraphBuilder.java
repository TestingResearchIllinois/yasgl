package edu.illinois.yasgl;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSet;


public class DirectedGraphBuilder<V> {

    private ImmutableMultimap.Builder<V, V> forward = ImmutableSetMultimap.builder();
    private Collection<V> vertices = new HashSet<V>();
    
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
        
        return new DirectedGraph(multi, ImmutableSet.copyOf(vertices));
    }

}

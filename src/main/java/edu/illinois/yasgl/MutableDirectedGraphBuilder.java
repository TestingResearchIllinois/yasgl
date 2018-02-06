package edu.illinois.yasgl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.HashMultimap;

public class MutableDirectedGraphBuilder<V> {
    private final Collection<V> vertices = new HashSet<V>();
    private final HashMultimap<V, V> forward = HashMultimap.create();
    private final HashMultimap<V, V> backward = HashMultimap.create();
    
    private boolean built = false;
    
    public MutableDirectedGraphBuilder() {
    }

    public MutableDirectedGraphBuilder(DirectedGraph<V> g) {
        this.vertices.addAll(g.vertices);
        for (V pre : g.forward.keys()) {
            for (V suc : g.forward.get(pre)) {
                this.addEdge(pre, suc);
            }
        }
    }

    private void validate() {
        if (built) {
            throw new UnsupportedOperationException("The builder built the object; no changes are allowed here.");
        }

    }
    
    public void addEdge(V vertex1, V vertex2) {
        this.validate();
        this.addVertex(vertex1);
        this.addVertex(vertex2);
        this.forward.put(vertex1, vertex2);
        this.backward.put(vertex2, vertex1);
    }

    public void addVertex(V vertex) {
        this.validate();
        this.vertices.add(vertex);
    }

    public DirectedGraph<V> build() {
        built = true;
        return new DirectedGraph<V>(forward, backward, vertices);
    }
    
    public static <V> DirectedGraph<V> empty() {
        return new DirectedGraph<V>(HashMultimap.create(), HashMultimap.create(), new HashSet<>());
    }

}

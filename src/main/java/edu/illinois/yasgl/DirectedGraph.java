package edu.illinois.yasgl;

import java.util.Collection;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultimap;


public class DirectedGraph<V> implements Graph<V> {

    private ImmutableMultimap<V, V> forward;
    private ImmutableMultimap<V, V> backward;
    private Collection<V> vertices;

    protected DirectedGraph(ImmutableMultimap<V, V> forward, Collection<V> vertices) {
        this.forward = forward;
        this.backward = this.forward.inverse();
        this.vertices = vertices;
    }
    
    public void traverse(Consumer<V> consumer) {
    }

    public Collection<V> getSuccessors(V vertex) {
        return null;
    }
    
    public Collection<V> getPredecessors(V vertex) {
        return null;
    }

}

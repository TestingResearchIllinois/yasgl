package edu.illinois.yasgl;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.Multimap;

public class MutableDirectedGraph<V> extends DirectedGraph<V> implements MutableGraph<V> {

    private static final long serialVersionUID = 1L;

    protected MutableDirectedGraph(Multimap<V, V> forward, Multimap<V,V> backward, Collection<V> vertices) {
        super(forward, backward, vertices);
    }

    @Override
    public void addEdge(V v1, V v2) {
        this.forward.put(v1, v2);
        this.backward.put(v2, v1);
        this.vertices.add(v1);
        this.vertices.add(v2);
    }

    @Override
    public void addVertex(V v) {
        this.vertices.add(v);
        
    }

    @Override
    public void removeEdge(V v1, V v2) {
        this.forward.remove(v1, v2);
        this.backward.remove(v2, v1);        
    }

    @Override
    public void removeVertex(V v) {
        this.vertices.remove(v);
        Collection<V> successors = new HashSet<V>(this.forward.get(v));
        Collection<V> predecessors = new HashSet<V>(this.backward.get(v));
        
        predecessors.stream().forEach(pre -> forward.remove(pre, v));
        this.forward.removeAll(v);
        
        successors.stream().forEach(suc -> backward.remove(suc, v));
        this.backward.removeAll(v);
    }

}

package edu.illinois.yasgl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface Graph<V> extends Serializable {

    public Collection<V> getSuccessors(V vertex);
    public Collection<V> getPredecessors(V vertex);
    public Collection<V> getVertices();
    public Collection<Edge<V>> getEdges();
    
    public Graph<V> inverse();
    
}

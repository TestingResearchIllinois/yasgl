package edu.illinois.yasgl;

import java.util.Collection;
import java.util.function.Consumer;
import java.io.Serializable;

public interface Graph<V> extends Serializable {

    public Collection<V> getSuccessors(V vertex);
    public Collection<V> getPredecessors(V vertex);
    public Collection<V> getVertices();
    
    public Graph<V> inverse();
    
}

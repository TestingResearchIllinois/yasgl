package edu.illinois.yasgl;

import java.util.Collection;
import java.util.function.Consumer;

public interface Graph<V> {

    public Collection<V> getSuccessors(V vertex);
    public Collection<V> getPredecessors(V vertex);
    
}

package edu.illinois.yasgl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface Graph<V> extends Serializable {

    public Collection<V> getSuccessors(V vertex);
    public Collection<V> getPredecessors(V vertex);
    public Collection<V> getVertices();
    public Collection<Edge<V>> getEdges();
    
    public Graph<V> inverse();
    
    public void acceptForward(V v, GraphVertexVisitor<V> visitor);
    
	public void acceptBackward(V v, GraphVertexVisitor<V> visitor);
    
}

package edu.illinois.yasgl;

import java.util.Collections;
import java.util.Collection;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultimap;


public class DirectedGraph<V> implements Graph<V> {

	private static final long serialVersionUID = -3303603645240328439L;

	final ImmutableMultimap<V, V> forward;
    final ImmutableMultimap<V, V> backward;
    final Collection<V> vertices;

    protected DirectedGraph(ImmutableMultimap<V, V> forward, Collection<V> vertices) {
        this.forward = forward;
        this.backward = this.forward.inverse();
        this.vertices = vertices;
    }
    
    public Collection<V> getSuccessors(V vertex) {
        return this.forward.get(vertex);
    }
    
    public Collection<V> getPredecessors(V vertex) {
        return this.backward.get(vertex);
    }

    public Collection<V> vertexSet() {
        return Collections.unmodifiableCollection(this.vertices);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (V v : vertices) {

            sb.append("<");
            sb.append(v);
            sb.append(" -> ");
            sb.append(this.forward.containsKey(v) ? this.forward.get(v) : "{}");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

	@Override
	public Collection<V> getVertices() {
		return Collections.unmodifiableCollection(this.vertices);
	}
}

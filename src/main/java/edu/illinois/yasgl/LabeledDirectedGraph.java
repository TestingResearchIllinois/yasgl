package edu.illinois.yasgl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

import edu.illinois.yasgl.LabeledDirectedGraphBuilder.VertexEntry;

public class LabeledDirectedGraph <V, E> implements Graph<V>{

	private static final long serialVersionUID = 4772562024828519617L;

	final ImmutableMultimap<V, VertexEntry<V, E>> forward;
    final ImmutableMultimap<V, VertexEntry<V, E>> backward;
    final Collection<V> vertices;
    
	public LabeledDirectedGraph(ImmutableMultimap<V, VertexEntry<V, E>> multi, ImmutableSet<V> vertices) {
		this.forward = multi;
		ImmutableMultimap.Builder<V, VertexEntry<V, E>> backBuilder = ImmutableSetMultimap.builder();
		for (Entry<V, VertexEntry<V, E>> entry : multi.entries()) {
			backBuilder.put(entry.getValue().getVertex(), new VertexEntry<>(entry.getKey(), entry.getValue().getEdge()));
		}
		this.backward = backBuilder.build();
		this.vertices = vertices;
	}

	@Override
	public Collection<V> getSuccessors(V vertex) {
		return this.forward.get(vertex).stream().map(x -> x.getVertex()).collect(Collectors.toSet());
	}

	@Override
	public Collection<V> getPredecessors(V vertex) {
		return this.backward.get(vertex).stream().map(x -> x.getVertex()).collect(Collectors.toSet());
	}

	public Collection<VertexEntry<V, E>> getLabeledSuccessors(V vertex) {
		return this.forward.get(vertex);
	}

	public Collection<VertexEntry<V, E>> getLabeledPredecessors(V vertex) {
		return this.backward.get(vertex);
	}

	@Override
	public Collection<V> getVertices() {
		return Collections.unmodifiableCollection(this.vertices);
	}

	@Override
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
}

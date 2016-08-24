package edu.illinois.yasgl;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

import edu.illinois.yasgl.LabeledDirectedGraphBuilder.VertexEntry;

public class LabeledDirectedGraph <V, E> extends AbstractGraph<V> implements EdgeLabeledGraph<V, E>{

	private static final long serialVersionUID = 4772562024828519617L;

	final ImmutableMultimap<V, VertexEntry<V, E>> forward;
    final ImmutableMultimap<V, VertexEntry<V, E>> backward;
    final Collection<V> vertices;
    
    Collection<E> edges;
    
	public LabeledDirectedGraph(ImmutableMultimap<V, VertexEntry<V, E>> multi, ImmutableSet<V> vertices) {
		this.forward = multi;
		ImmutableMultimap.Builder<V, VertexEntry<V, E>> backBuilder = ImmutableSetMultimap.builder();
		for (Entry<V, VertexEntry<V, E>> entry : multi.entries()) {
			backBuilder.put(entry.getValue().getVertex(), new VertexEntry<>(entry.getKey(), entry.getValue().getEdge()));
		}
		this.backward = backBuilder.build();
		this.vertices = vertices;
	}
	
	private LabeledDirectedGraph(ImmutableMultimap<V, VertexEntry<V, E>> forward,
			ImmutableMultimap<V, VertexEntry<V, E>> backward, Collection<V> vertices) {
		this.forward = forward;
		this.backward = backward;
		this.vertices = vertices;
	}

	@Override
	public LabeledDirectedGraph<V, E> inverse() {
		return new LabeledDirectedGraph<V, E>(this.backward, this.forward, this.vertices);
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

	public void writeToFile(Writer sb) throws IOException {
        for (V key : this.forward.keySet()) {
        	for (VertexEntry<V, E> val : forward.get(key)) {
        		sb.write(key.toString());
        		sb.write("\t");
        		sb.write(val.toString());
        		sb.write("\n");
        	}
        }
        sb.write("Verts: ");
        sb.write(this.getVertices().toString());
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
            sb.append(this.forward.containsKey(v) ? this.forward.get(v) : "[]");
            sb.append(">");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

	@Override
	public int hashCode() {
		return this.vertices.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof LabeledDirectedGraph) {
			LabeledDirectedGraph casted = (LabeledDirectedGraph)o;
			return o == casted || (this.vertices.equals(casted) && this.forward.equals(casted) && this.backward.equals(casted));
		}
		return false;
	}

	@Override
	public Collection<E> getAllLabels() {
		if (edges == null) {
			edges = this.vertices.stream()
					.map(vert -> this.getLabeledSuccessors(vert).stream()
							.map(vertexEntry -> vertexEntry.getEdge())
							.collect(Collectors.toSet()))
					.reduce(new HashSet<>(), (set1, set2) -> {set1.addAll(set2); return set1;});
			
		}
		return edges;
	}
	
	public Collection<Edge<V>> getEdges() {
		return this.forward.entries().stream()
				.map(e -> new Edge<V>(e.getKey(), e.getValue().getVertex()))
				.collect(Collectors.toList());
	}
}

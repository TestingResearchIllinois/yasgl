package edu.illinois.yasgl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import edu.illinois.yasgl.LabeledDirectedGraphBuilder.VertexEntry;

public class LabeledGraphView<V,E> implements EdgeLabeledGraph<V, E>{

	private static final long serialVersionUID = 1695850785518400072L;

	private final EdgeLabeledGraph<V, E> underlyingGraph;
	private final Collection<E> filter;

	private E[] dummy;
	
	public LabeledGraphView(EdgeLabeledGraph<V, E> underlyingGraph, E...filters) {
		this(underlyingGraph, Collections.unmodifiableCollection(new HashSet<>(Arrays.asList(filters))));
	}
	
	public LabeledGraphView(EdgeLabeledGraph<V, E> underlyingGraph, Collection<E> filter) {
		this.underlyingGraph = underlyingGraph;
		this.filter = filter;
	}

	@Override
	public Collection<V> getSuccessors(V vertex) {
		return this.underlyingGraph.getLabeledSuccessors(vertex).stream()
				.filter(vertexEntry -> !this.filter.contains(vertexEntry.getEdge()))
				.map(vertexEntry -> vertexEntry.getVertex())
				.collect(Collectors.toSet());
	}

	@Override
	public Collection<V> getPredecessors(V vertex) {
		return this.underlyingGraph.getLabeledPredecessors(vertex).stream()
				.filter(vertexEntry -> !this.filter.contains(vertexEntry.getEdge()))
				.map(vertexEntry -> vertexEntry.getVertex())
				.collect(Collectors.toSet());
	}

	@Override
	public Collection<V> getVertices() {
		return this.underlyingGraph.getVertices();
	}

	@Override
	public Graph<V> inverse() {
		return new LabeledGraphView<V, E>(this.underlyingGraph, this.filter);
	}

	@Override
	public Collection<VertexEntry<V, E>> getLabeledSuccessors(V vertex) {
		return this.underlyingGraph.getLabeledSuccessors(vertex).stream()
				.filter(vertexEntry -> !this.filter.contains(vertexEntry.getEdge()))
				.collect(Collectors.toSet());
	}

	@Override
	public Collection<VertexEntry<V, E>> getLabeledPredecessors(V vertex) {
		return this.underlyingGraph.getLabeledPredecessors(vertex).stream()
				.filter(vertexEntry -> !this.filter.contains(vertexEntry.getEdge()))
				.collect(Collectors.toSet());
	}

	@Override
	public Collection<E> getAllLabels() {
		return this.underlyingGraph.getAllLabels().stream()
				.filter(e -> !this.filter.contains(e))
				.collect(Collectors.toSet());
	}
	
	public Collection<Edge<V>> getEdges() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void acceptForward(V v, GraphVertexVisitor<V> visitor) {
		this.underlyingGraph.acceptBackward(v, visitor);
	}

	@Override
	public void acceptBackward(V v, GraphVertexVisitor<V> visitor) {
		this.underlyingGraph.acceptForward(v, visitor);		
	}@Override
	
	public void acceptForward(Collection<V> v, GraphVertexVisitor<V> visitor) {
		this.underlyingGraph.acceptBackward(v, visitor);
	}

	@Override
	public void acceptBackward(Collection<V> v, GraphVertexVisitor<V> visitor) {
		this.underlyingGraph.acceptForward(v, visitor);		
	}
}

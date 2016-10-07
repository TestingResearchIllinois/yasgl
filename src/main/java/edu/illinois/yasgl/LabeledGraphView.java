/* The MIT License (MIT)

Copyright (c) 2016 Alex Gyori

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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
	public Set<V> acceptForward(V v, GraphVertexVisitor<V> visitor) {
		return this.underlyingGraph.acceptBackward(v, visitor);
	}

	@Override
	public Set<V> acceptBackward(V v, GraphVertexVisitor<V> visitor) {
		return this.underlyingGraph.acceptForward(v, visitor);		
	}@Override
	
	public Set<V> acceptForward(Collection<V> v, GraphVertexVisitor<V> visitor) {
		return this.underlyingGraph.acceptBackward(v, visitor);
	}

	@Override
	public Set<V> acceptBackward(Collection<V> v, GraphVertexVisitor<V> visitor) {
		return this.underlyingGraph.acceptForward(v, visitor);		
	}
}

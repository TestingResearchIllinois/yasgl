package edu.illinois.yasgl;

import java.util.Collection;

import edu.illinois.yasgl.LabeledDirectedGraphBuilder.VertexEntry;

public interface EdgeLabeledGraph<V, E> extends Graph<V>{
	public Collection<VertexEntry<V, E>> getLabeledSuccessors(V vertex);

	public Collection<VertexEntry<V, E>> getLabeledPredecessors(V vertex);
	
	public Collection<E> getAllLabels();
}

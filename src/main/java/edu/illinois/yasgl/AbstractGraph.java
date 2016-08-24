package edu.illinois.yasgl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractGraph<V> implements Graph<V> {
	@Override
	public Set<V> acceptForward(V v, GraphVertexVisitor<V> visitor) {
		assert this.getVertices().contains(v);
		return acceptForward(v, visitor, new HashSet<>());
	}
	
	@Override
	public Set<V> acceptForward(Collection<V> vs, GraphVertexVisitor<V> visitor) {
		Set<V> visited = new HashSet<>();//Collections.newSetFromMap(new ConcurrentHashMap<V, Boolean>());	
		for (V v : vs) {
			assert this.getVertices().contains(v);
			acceptForward(v, visitor, visited); 
		}
		return visited;
	}
	
	private Set<V> acceptForward(V v, GraphVertexVisitor<V> visitor, Set<V> visited) {
		if (!visited.add(v)) {
			return visited;
		}
		visitor.visit(v);
		for (V vert : this.getSuccessors(v)) {
			this.acceptForward(vert, visitor, visited);
		}
		return visited;
	}
	
	@Override
	public Set<V> acceptBackward(V v, GraphVertexVisitor<V> visitor) {
		assert this.getVertices().contains(v);
		return acceptBackward(v, visitor, new HashSet<>());
	}
	
	@Override
	public Set<V> acceptBackward(Collection<V> vs, GraphVertexVisitor<V> visitor) {
		Set<V> visited = new HashSet<>();//Collections.newSetFromMap(new ConcurrentHashMap<V, Boolean>());
		for (V v : vs) {
			assert this.getVertices().contains(v);
			acceptBackward(v, visitor, visited);
		}
		return visited;
	}
	
	private Set<V> acceptBackward(V v, GraphVertexVisitor<V> visitor, Set<V> visited) {
		if (!visited.add(v)) {
			return visited;
		}
		visitor.visit(v);
		for (V vert : this.getPredecessors(v)) {
			this.acceptBackward(vert, visitor, visited);
		}
		return visited;
	}
}

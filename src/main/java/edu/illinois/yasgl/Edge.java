package edu.illinois.yasgl;

public class Edge<V> {

	private V source;
	private V destination;

	public Edge(V source, V destination) {
		this.source = source;
		this.destination = destination;
	}
	
	public V getSource() {
		return this.source;
	}
	
	public V getDestination() {
		return this.destination;
	}
}

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
	
	public boolean equals(Object o) {
		if (o instanceof Edge) {
			Edge e = (Edge)o;
			return this.source.equals(e.source) && this.destination.equals(e.destination);
		}
		return false;
	}
	
	public int hashCode() {
		return this.source.hashCode() + this.destination.hashCode();
	}
	
	public String toString() {
		return this.source + " --> " + this.destination;
	}
}

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

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultimap;

public class DirectedGraph<V> extends AbstractGraph<V> {

    private static final long serialVersionUID = -3303603645240328439L;

    final ImmutableMultimap<V, V> forward;
    final ImmutableMultimap<V, V> backward;
    final Collection<V>           vertices;

    protected DirectedGraph(ImmutableMultimap<V, V> forward, Collection<V> vertices) {
        this.forward = forward;
        this.backward = this.forward.inverse();
        this.vertices = vertices;
    }

    private DirectedGraph(ImmutableMultimap<V, V> forward, ImmutableMultimap<V, V> backward,
            Collection<V> vertices) {
        this.forward = forward;
        this.backward = backward;
        this.vertices = vertices;
    }

    @Override
    public DirectedGraph<V> inverse() {
        return new DirectedGraph<>(this.backward, this.forward, vertices);
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

    public Collection<Edge<V>> getEdges() {
        return this.forward.entries().stream()
                .map(e -> new Edge<V>((V) e.getKey(), (V) e.getValue()))
                .collect(Collectors.toSet());
    }

}

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
import java.util.HashSet;
import java.util.Map.Entry;
import java.io.Serializable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimaps;

public class LabeledDirectedGraphBuilder<V, E> {

    private final ImmutableMultimap.Builder<V, VertexEntry<V, E>> forward  = ImmutableSetMultimap
            .builder();
    private final Collection<V>                                   vertices = new HashSet<V>();

    public LabeledDirectedGraphBuilder() {
    }

    public LabeledDirectedGraphBuilder(LabeledDirectedGraph<V, E> g) {
        this.vertices.addAll(g.vertices);
        for (V pre : g.forward.keys()) {
            for (VertexEntry<V, E> suc : g.forward.get(pre)) {
                this.addEdge(pre, suc.getVertex(), suc.getEdge());
            }
        }
    }

    public synchronized void addEdge(V vertex1, V vertex2, E edge) {
        this.addVertex(vertex1);
        this.addVertex(vertex2);
        this.forward.put(vertex1, new VertexEntry<V, E>(vertex2, edge));
    }

    public synchronized void addVertex(V vertex) {
        this.vertices.add(vertex);
    }

    public LabeledDirectedGraph<V, E> build() {
        ImmutableMultimap<V, VertexEntry<V, E>> multi = this.forward.build();

        ImmutableMultimap.Builder<V, VertexEntry<V, E>> backBuilder = ImmutableSetMultimap
                .builder();
        for (Entry<V, VertexEntry<V, E>> entry : multi.entries()) {
            backBuilder.put(entry.getValue().getVertex(),
                    new VertexEntry<>(entry.getKey(), entry.getValue().getEdge()));
        }

        return new LabeledDirectedGraph<V, E>(multi, backBuilder.build(), ImmutableSet.copyOf(vertices));
    }

    public static class VertexEntry<V, E> implements Serializable {

        private static final long serialVersionUID = -1772155319994626054L;

        private final V vertex;
        private final E edge;

        public VertexEntry(V vertex, E edge) {
            this.vertex = vertex;
            this.edge = edge;
        }

        public V getVertex() {
            return vertex;
        }

        public E getEdge() {
            return edge;
        }

        public String toString() {
            return this.vertex + "\t" + this.edge;
        }

        public int hashCode() {
            return vertex.hashCode() + edge.hashCode();
        }

        public boolean equals(Object o) {
            if (o instanceof VertexEntry) {
                VertexEntry<V, E> casted = (VertexEntry<V, E>) o;
                return this == casted
                        || (this.vertex.equals(casted.vertex) && this.edge.equals(casted.edge));
            }
            return false;
        }
    }
}

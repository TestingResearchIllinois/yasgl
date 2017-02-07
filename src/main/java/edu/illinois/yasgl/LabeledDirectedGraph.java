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

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

import edu.illinois.yasgl.LabeledDirectedGraphBuilder.VertexEntry;

public class LabeledDirectedGraph<V, E> extends AbstractGraph<V> implements EdgeLabeledGraph<V, E> {

    private static final long serialVersionUID = 4772562024828519617L;

    final Multimap<V, VertexEntry<V, E>> forward;
    final Multimap<V, VertexEntry<V, E>> backward;
    final Collection<V>                           vertices;

    Collection<E> edges;

    protected LabeledDirectedGraph(Multimap<V, VertexEntry<V, E>> forward,
            Multimap<V, VertexEntry<V, E>> backward, Collection<V> vertices) {
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
        return this.forward.get(vertex).stream().map(x -> x.getVertex())
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<V> getPredecessors(V vertex) {
        return this.backward.get(vertex).stream().map(x -> x.getVertex())
                .collect(Collectors.toSet());
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
            LabeledDirectedGraph casted = (LabeledDirectedGraph) o;
            return o == casted || (this.vertices.equals(casted) && this.forward.equals(casted)
                    && this.backward.equals(casted));
        }
        return false;
    }

    @Override
    public Collection<E> getAllLabels() {
        if (edges == null) {
            edges = this.vertices.stream()
                    .map(vert -> this.getLabeledSuccessors(vert).stream()
                            .map(vertexEntry -> vertexEntry.getEdge()).collect(Collectors.toSet()))
                    .reduce(new HashSet<>(), (set1, set2) -> {
                        set1.addAll(set2);
                        return set1;
                    });

        }
        return edges;
    }

    public Collection<Edge<V>> getEdges() {
        return this.forward.entries().stream()
                .map(e -> new Edge<V>(e.getKey(), e.getValue().getVertex()))
                .collect(Collectors.toList());
    }
}

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
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSet;

public class DirectedGraphBuilder<V> {

    private final ImmutableMultimap.Builder<V, V> forward  = ImmutableSetMultimap.builder();
    private final Collection<V>                   vertices = new HashSet<V>();

    public DirectedGraphBuilder() {
    }

    public DirectedGraphBuilder(DirectedGraph<V> g) {
        this.vertices.addAll(g.vertices);
        for (V pre : g.forward.keys()) {
            for (V suc : g.forward.get(pre)) {
                this.addEdge(pre, suc);
            }
        }
    }

    public void addEdge(V vertex1, V vertex2) {
        this.addVertex(vertex1);
        this.addVertex(vertex2);
        this.forward.put(vertex1, vertex2);
    }

    public void addVertex(V vertex) {
        this.vertices.add(vertex);
    }

    public DirectedGraph<V> build() {
        ImmutableMultimap<V, V> multi = this.forward.build();

        return new DirectedGraph<V>(multi, ImmutableSet.copyOf(vertices));
    }

}

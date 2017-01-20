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
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import edu.illinois.yasgl.LabeledDirectedGraphBuilder.VertexEntry;

public abstract class AbstractGraph<V> implements Graph<V> {
    @Override
    public Set<V> acceptForward(V v, GraphVertexVisitor<V> visitor) {
        assert this.getVertices().contains(v);
        return acceptForward(v, visitor, new HashSet<>());
    }

    @Override
    public Set<V> acceptForward(Collection<V> vs, GraphVertexVisitor<V> visitor) {
        Set<V> visited = new HashSet<>();// Collections.newSetFromMap(new ConcurrentHashMap<V,
                                         // Boolean>());
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
        Set<V> visited = new HashSet<>();// Collections.newSetFromMap(new ConcurrentHashMap<V,
                                         // Boolean>());
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

    // creates things for the format of LongLongNullTextInputFormat
    public void toGiraphString(Map<V, Long> outMap, Writer sb) throws IOException {
        // to make sure we don't map to things are already in the map's domain
        Long l = outMap.values().stream().max(Comparator.<Long>naturalOrder()).orElse(0L);

        for (V key : this.getVertices()) {
            if (!outMap.containsKey(key)) {
                outMap.put(key, l++);
            }

            sb.write(outMap.get(key).toString());

            for (V val : this.getSuccessors(key)) {
                if (!outMap.containsKey(val)) {
                    outMap.put(val, l++);
                }

                sb.write("\t");
                sb.write(outMap.get(val).toString());
            }
            sb.write("\n");
        }
    }
}

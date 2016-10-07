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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class DFSTask<V> implements Runnable {

    private final Graph<V>      graph;
    private Map<V, Set<V>>      resultsMap;
    private final Collection<V> vertices;

    public DFSTask(Graph<V> graph, Map<V, Set<V>> results) {
        this(graph, results, graph.getVertices());
    }

    public DFSTask(Graph<V> graph, Map<V, Set<V>> results, Collection<V> startingPoints) {
        this.graph = graph;
        this.resultsMap = results;
        this.vertices = startingPoints;
    }

    public void run() {

        Queue<V> worklist = new LinkedList<>();
        worklist.addAll(this.graph.getVertices());

        this.vertices.stream().forEach(
                v -> this.resultsMap.put(v, Collections.newSetFromMap(new HashMap<V, Boolean>())));
        this.vertices.stream().forEach(v -> this.resultsMap.get(v).add(v));
        this.vertices.stream().forEach(v -> this.resultsMap.get(v).addAll(graph.getSuccessors(v)));

        this.resultsMap = Collections.unmodifiableMap(resultsMap);

        while (!worklist.isEmpty()) {
            V current = worklist.remove();
            Set<V> currentTc = this.resultsMap.get(current);

            Set<V> nexts = Collections.newSetFromMap(new HashMap<V, Boolean>());
            currentTc.stream().map(v -> this.resultsMap.get(v)).forEach(vs -> nexts.addAll(vs));

            if (currentTc.addAll(nexts)) {
                worklist.add(current);
            }
        }
    }
}

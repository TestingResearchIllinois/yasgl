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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class GraphUtilsTest {

    Graph<String> g;
    Graph<String> g2;

    @Before
    public void setup() {
        LabeledDirectedGraphBuilder<String, String> builder = new LabeledDirectedGraphBuilder<>();
        builder.addEdge("a", "b", "X");
        builder.addEdge("b", "c", "X");
        builder.addEdge("c", "d", "X");
        builder.addEdge("a", "e", "X");
        builder.addEdge("e", "b", "X");
        builder.addEdge("b", "f", "X");
        builder.addEdge("f", "g", "X");
        builder.addEdge("g", "f", "X");
        builder.addEdge("f", "d", "X");
        this.g = builder.build();

        builder = new LabeledDirectedGraphBuilder<>();
        builder.addEdge("a", "c", "X");
        builder.addEdge("a", "d", "X");
        builder.addEdge("a", "e", "X");
        builder.addEdge("b", "c", "X");
        builder.addEdge("c", "e", "X");
        builder.addEdge("d", "e", "X");
        builder.addEdge("f", "e", "X");
        builder.addEdge("f", "a", "X");
        builder.addEdge("e", "f", "X");
        this.g2 = builder.build();

    }

    @Test
    public void testDiregtedGraphIsNotMultigraph() {
        DirectedGraphBuilder<String> dgbs = new DirectedGraphBuilder<>();
        dgbs.addEdge("a", "b");
        dgbs.addEdge("a", "c");
        dgbs.addEdge("a", "b");

        Graph<String> g = dgbs.build();
        assertEquals(2, g.getEdges().size());
        assertEquals(3, g.getVertices().size());

    }

    @Test
    public void testFTC() {
        Map<String, Set<String>> tc = GraphUtils.computeTransitiveClosure(this.g);

        assertEquals("a", new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g")),
                tc.get("a"));
        assertEquals("b", new HashSet<>(Arrays.asList("b", "c", "d", "f", "g")), tc.get("b"));
        assertEquals("c", new HashSet<>(Arrays.asList("c", "d")), tc.get("c"));
        assertEquals("d", new HashSet<>(Arrays.asList("d")), tc.get("d"));
        assertEquals("e", new HashSet<>(Arrays.asList("b", "c", "d", "e", "f", "g")), tc.get("e"));
        assertEquals("f", new HashSet<>(Arrays.asList("d", "f", "g")), tc.get("f"));
        assertEquals("g", new HashSet<>(Arrays.asList("d", "f", "g")), tc.get("g"));
    }

    @Test
    public void testBTC() {
        Map<String, Set<String>> tc = GraphUtils.computeBackwardsTransitiveClosure(this.g);

        assertEquals("a", new HashSet<>(Arrays.asList("a")), tc.get("a"));
        assertEquals("b", new HashSet<>(Arrays.asList("b", "a", "e")), tc.get("b"));
        assertEquals("c", new HashSet<>(Arrays.asList("b", "a", "c", "e")), tc.get("c"));
        assertEquals("d", new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g")),
                tc.get("d"));
        assertEquals("e", new HashSet<>(Arrays.asList("a", "e")), tc.get("e"));
        assertEquals("f", new HashSet<>(Arrays.asList("a", "b", "e", "f", "g")), tc.get("f"));
        assertEquals("g", new HashSet<>(Arrays.asList("a", "b", "e", "f", "g")), tc.get("g"));
    }

    @Test
    public void testLongestPaths() {
        LabeledDirectedGraphBuilder<String, String> builder = new LabeledDirectedGraphBuilder<>();
        builder.addEdge("a", "b", "X");
        builder.addEdge("b", "c", "X");
        builder.addEdge("c", "d", "X");
        builder.addEdge("a", "e", "X");
        builder.addEdge("e", "b", "X");
        builder.addEdge("b", "f", "X");
        builder.addEdge("f", "g", "X");
        builder.addEdge("f", "d", "X");
        this.g = builder.build();

        Map<String, Integer> lengths = GraphUtils.longestPaths(this.g);

        assertEquals("a", lengths.get("a").intValue(), 5);
        assertEquals("b", lengths.get("b").intValue(), 3);
        assertEquals("c", lengths.get("c").intValue(), 2);
        assertEquals("d", lengths.get("d").intValue(), 1);
        assertEquals("e", lengths.get("e").intValue(), 4);
        assertEquals("f", lengths.get("f").intValue(), 2);
        assertEquals("g", lengths.get("g").intValue(), 1);
    }

    @Test
    public void testComputeAnyPathSimple() {
        List<String> path = GraphUtils.computeAnyPath(this.g, "a", new HashSet<String>() {
            {
                add("d");
            }
        });
        assertEquals("path length should be 4", 4, path.size());
        assertEquals("path content should be a,b,f,d", "[a, b, f, d]", path.toString());

    }

    @Test
    public void testComputeAnyPathLargeGraph() {
        LabeledDirectedGraphBuilder<String, String> builder = new LabeledDirectedGraphBuilder<>();
        int N = 1000000; // number of nodes
        for (int i = 0; i < N - 1; i++) {
            builder.addEdge("n" + i, "n" + (i + 1), "");
        }
        this.g = builder.build();
        List<String> path = GraphUtils.computeAnyPath(this.g, "n1", new HashSet<String>() {
            {
                add("n999999");
            }
        });
        assertTrue(!path.isEmpty());
    }

    @Test
    public void testComputeShortestPathSimple() {
        List<String> path = GraphUtils.computeShortestPath(this.g, "a", new HashSet<String>() {
            {
                add("d");
            }
        });
        assertEquals("path length should be 4", 4, path.size());
        assertEquals("path content should be a,b,c,d", "[a, b, c, d]", path.toString());

    }

    @Test
    public void testComputeShortestPathNonexistingElement() {
        List<String> path = GraphUtils.computeShortestPath(this.g, "a", new HashSet<String>() {
            {
                add("y");
            }
        });
        assertEquals("path length should be 0", 0, path.size());

    }

    @Test
    public void testComputeShortestPathSimple1() {
        LabeledDirectedGraphBuilder<String, String> builder = new LabeledDirectedGraphBuilder<>();
        builder.addEdge("a", "b", "X");
        builder.addEdge("b", "c", "X");
        builder.addEdge("c", "d", "X");
        builder.addEdge("d", "e", "X");
        builder.addEdge("e", "f", "X");
        builder.addEdge("f", "g", "X");
        builder.addEdge("g", "h", "X");
        builder.addEdge("h", "i", "X");
        builder.addEdge("a", "e", "X");
        builder.addEdge("e", "b", "X");
        builder.addEdge("b", "f", "X");
        builder.addEdge("f", "s", "X");
        builder.addEdge("s", "k", "X");
        builder.addEdge("k", "i", "X");
        this.g = builder.build();

        List<String> path = GraphUtils.computeShortestPath(this.g, "a", new HashSet<String>() {
            {
                add("d");
                add("i");
            }
        });
        assertEquals("path length should be 4", 4, path.size());
        assertEquals("path content should be a,b,c,d", "[a, b, c, d]", path.toString());

    }

    @Test
    public void testPageRank() {

        System.out.println(GraphUtils.<String>getInstance().pageRank(this.g2));
    }

    @Test
    public void testComputeDFS() {
        LabeledDirectedGraphBuilder<String, String> builder = new LabeledDirectedGraphBuilder<>();
        builder.addEdge("a", "b", "X");
        builder.addEdge("b", "c", "X");
        builder.addEdge("c", "a", "X");
        builder.addEdge("b", "d", "X");
        builder.addEdge("d", "e", "X");
        this.g = builder.build();

	Deque<String> result = GraphUtils.computeDFS(this.g, "b", new HashSet<>(), new LinkedList<>());

	LinkedList<String> expected = new LinkedList<>();
	expected.push("a");
	expected.push("c");
	expected.push("e");
	expected.push("d");
	expected.push("b");
	assertEquals("Stack from DFS is wrong", expected, result);
    }

    @Test
    public void testComputeDFSOnDefaultGraph() {
	Deque<String> result = GraphUtils.computeDFS(this.g2, "a", new HashSet<>(), new LinkedList<>());

	LinkedList<String> expected = new LinkedList<>();
	expected.push("f");
	expected.push("e");
	expected.push("c");
	expected.push("d");
	expected.push("a");
	assertEquals("Stack from DFS is wrong", expected, result);
    }

    @Test
    public void testComputeSCCsOnCustomGraph() {
        LabeledDirectedGraphBuilder<String, String> builder = new LabeledDirectedGraphBuilder<>();
        builder.addEdge("a", "b", "X");
        builder.addEdge("b", "c", "X");
        builder.addEdge("c", "a", "X");
        builder.addEdge("b", "d", "X");
        builder.addEdge("d", "e", "X");
        this.g = builder.build();

	List<List<String>> result = GraphUtils.computeSCC(this.g);
	List<List<String>> expected = new LinkedList<>();

	expected.add(new LinkedList<String>(){
		{
		    add("a");
		    add("c");
		    add("b");
		}
	    });
	expected.add(new LinkedList<String>(){
		{
		    add("d");
		}
	    });
	expected.add(new LinkedList<String>(){
		{
		    add("e");
		}
	    });
	assertEquals("Stack from DFS is wrong", expected, result);
    }

    @Test
    public void testComputeSCCsOnDefaultGraph() {
	List<List<String>> result = GraphUtils.computeSCC(this.g2);
	List<List<String>> expected = new LinkedList<>();

	expected.add(new LinkedList<String>(){
		{
		    add("b");
		}
	    });
	expected.add(new LinkedList<String>(){
		{
		    add("a");
		    add("f");
		    add("e");
		    add("d");
		    add("c");
		}
	    });
	assertEquals("Stack from DFS is wrong", expected, result);
    }
}

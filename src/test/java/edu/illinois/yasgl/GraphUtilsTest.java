package edu.illinois.yasgl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class GraphUtilsTest {

	Graph<String> g;

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
	}

	@Test
	public void testFTC() {
		Map<String, Collection<String>> tc = GraphUtils.computeTransitiveClosure(this.g);
		
		assertEquals("a", new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g")), tc.get("a"));
		assertEquals("b", new HashSet<>(Arrays.asList("b", "c", "d", "f", "g")), tc.get("b"));
		assertEquals("c", new HashSet<>(Arrays.asList("c", "d")), tc.get("c"));
		assertEquals("d", new HashSet<>(Arrays.asList("d")), tc.get("d"));
		assertEquals("e", new HashSet<>(Arrays.asList("b", "c", "d", "e", "f", "g")), tc.get("e"));
		assertEquals("f", new HashSet<>(Arrays.asList("d", "f", "g")), tc.get("f"));
		assertEquals("g", new HashSet<>(Arrays.asList("d", "f", "g")), tc.get("g"));
	}


	@Test
	public void testBTC() {
		Map<String, Collection<String>> tc = GraphUtils.computeBackwardsTransitiveClosure(this.g);

		assertEquals("a", new HashSet<>(Arrays.asList("a")), tc.get("a"));
		assertEquals("b", new HashSet<>(Arrays.asList("b", "a", "e")), tc.get("b"));
		assertEquals("c", new HashSet<>(Arrays.asList("b", "a", "c", "e")), tc.get("c"));
		assertEquals("d", new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g")), tc.get("d"));
		assertEquals("e", new HashSet<>(Arrays.asList("a", "e")), tc.get("e"));
		assertEquals("f", new HashSet<>(Arrays.asList("a", "b", "e", "f", "g")), tc.get("f"));
		assertEquals("f", new HashSet<>(Arrays.asList("a", "b", "e", "f", "g")), tc.get("g"));
	}

}
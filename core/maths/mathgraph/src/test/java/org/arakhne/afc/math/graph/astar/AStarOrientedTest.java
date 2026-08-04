/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.graph.astar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.arakhne.afc.math.graph.astar.AStar.CloseComparator;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AStar w/ oriented graph")
@SuppressWarnings("all")
public class AStarOrientedTest extends AbstractTestCase {

	private AStar<AStarPathStub,AStarEdgeStub,AStarNodeStub> astar;
	private AStarNodeStub A, B, C, D, E;
	private AStarEdgeStub AB, BD, DE, DC;
	private AStarHeuristicStub h;
	
	@BeforeEach
	public void setUp() throws Exception {
		h = new AStarHeuristicStub();
		astar = new AStar<>(h, AStarPathStub.class);
		A = new AStarNodeStub("A", 0, 0);  //$NON-NLS-1$
		B = new AStarNodeStub("B", 20, 0);  //$NON-NLS-1$
		C = new AStarNodeStub("C", 0, 20);  //$NON-NLS-1$
		D = new AStarNodeStub("D", 20, 20);  //$NON-NLS-1$
		E = new AStarNodeStub("E", 10, 40);  //$NON-NLS-1$
		AB = new AStarEdgeStub("A-B", A, B, true);  //$NON-NLS-1$
		BD = new AStarEdgeStub("B-D", B, D, true);  //$NON-NLS-1$
		DC = new AStarEdgeStub("D-C", D, C, true);  //$NON-NLS-1$
		DE = new AStarEdgeStub("D-E", D, E, true);  //$NON-NLS-1$
		reset();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		astar = null;
		AB = BD = DE = DC = null;
		A = B = C = D = E = null;
		h = null;
	}
	
	private void reset() {
		A.setArrivalConnection(null);
		A.setCost(Double.NaN);
		A.setEstimatedCost(Double.NaN);
		B.setArrivalConnection(null);
		B.setCost(Double.NaN);
		B.setEstimatedCost(Double.NaN);
		C.setArrivalConnection(null);
		C.setCost(Double.NaN);
		C.setEstimatedCost(Double.NaN);
		D.setArrivalConnection(null);
		D.setCost(Double.NaN);
		D.setEstimatedCost(Double.NaN);
		E.setArrivalConnection(null);
		E.setCost(Double.NaN);
		E.setEstimatedCost(Double.NaN);
	}

	private static double dist(double a, double b) {
		return Math.hypot(a, b);
	}
	
	@DisplayName("estimate")
	@Nested
	public class Estimate {

		private double dDist;
		private double mlDist;
		private double msDist;

		@BeforeEach
		public void setUp() {
			dDist = dist(20f,20f);
			mlDist = dist(10f,40f);
			msDist = dist(10f,20f);
		}

		@DisplayName("(PT,PT) #1")
		@Test
		public void estimatePTPT_1() {
			assertEpsilonEquals(0f, 				astar.estimate(A, A));
		}

		@DisplayName("(PT,PT) #2")
		@Test
		public void estimatePTPT_2() {
			assertEpsilonEquals(20f, 				astar.estimate(A, B));
		}

		@DisplayName("(PT,PT) #3")
		@Test
		public void estimatePTPT_3() {
			assertEpsilonEquals(20f, 				astar.estimate(A, C));
		}

		@DisplayName("(PT,PT) #4")
		@Test
		public void estimatePTPT_4() {
			assertEpsilonEquals(dDist,				astar.estimate(A, D));
		}

		@DisplayName("(PT,PT) #5")
		@Test
		public void estimatePTPT_5() {
			assertEpsilonEquals(mlDist,				astar.estimate(A, E));
		}

		@DisplayName("(PT,PT) #6")
		@Test
		public void estimatePTPT_6() {
			assertEpsilonEquals(20f, 				astar.estimate(B, A));
		}

		@DisplayName("(PT,PT) #7")
		@Test
		public void estimatePTPT_7() {
			assertEpsilonEquals(0f, 				astar.estimate(B, B));
		}

		@DisplayName("(PT,PT) #8")
		@Test
		public void estimatePTPT_8() {
			assertEpsilonEquals(dDist,				astar.estimate(B, C));
		}

		@DisplayName("(PT,PT) #9")
		@Test
		public void estimatePTPT_9() {
			assertEpsilonEquals(20f,				astar.estimate(B, D));
		}

		@DisplayName("(PT,PT) #10")
		@Test
		public void estimatePTPT_10() {
			assertEpsilonEquals(mlDist,				astar.estimate(B, E));
		}

		@DisplayName("(PT,PT) #11")
		@Test
		public void estimatePTPT_11() {
			assertEpsilonEquals(20f, 				astar.estimate(C, A));
		}

		@DisplayName("(PT,PT) #12")
		@Test
		public void estimatePTPT_12() {
			assertEpsilonEquals(dDist, 				astar.estimate(C, B));
		}

		@DisplayName("(PT,PT) #13")
		@Test
		public void estimatePTPT_13() {
			assertEpsilonEquals(0f,					astar.estimate(C, C));
		}

		@DisplayName("(PT,PT) #14")
		@Test
		public void estimatePTPT_14() {
			assertEpsilonEquals(20f,				astar.estimate(C, D));
		}

		@DisplayName("(PT,PT) #15")
		@Test
		public void estimatePTPT_15() {
			assertEpsilonEquals(msDist,				astar.estimate(C, E));
		}

		@DisplayName("(PT,PT) #16")
		@Test
		public void estimatePTPT_16() {
			assertEpsilonEquals(dDist, 				astar.estimate(D, A));
		}

		@DisplayName("(PT,PT) #17")
		@Test
		public void estimatePTPT_17() {
			assertEpsilonEquals(20f, 				astar.estimate(D, B));
		}

		@DisplayName("(PT,PT) #18")
		@Test
		public void estimatePTPT_18() {
			assertEpsilonEquals(20f,				astar.estimate(D, C));
		}

		@DisplayName("(PT,PT) #19")
		@Test
		public void estimatePTPT_19() {
			assertEpsilonEquals(0f,					astar.estimate(D, D));
		}

		@DisplayName("(PT,PT) #20")
		@Test
		public void estimatePTPT_20() {
			assertEpsilonEquals(msDist,				astar.estimate(D, E));
		}

		@DisplayName("(PT,PT) #21")
		@Test
		public void estimatePTPT_21() {
			assertEpsilonEquals(mlDist,				astar.estimate(E, A));
		}

		@DisplayName("(PT,PT) #22")
		@Test
		public void estimatePTPT_22() {
			assertEpsilonEquals(mlDist,				astar.estimate(E, B));
		}

		@DisplayName("(PT,PT) #23")
		@Test
		public void estimatePTPT_23() {
			assertEpsilonEquals(msDist,				astar.estimate(E, C));
		}

		@DisplayName("(PT,PT) #24")
		@Test
		public void estimatePTPT_24() {
			assertEpsilonEquals(msDist,				astar.estimate(E, D));
		}

		@DisplayName("(PT,PT) #25")
		@Test
		public void estimatePTPT_25() {
			assertEpsilonEquals(0f,					astar.estimate(E, E));
		}
	}

	@DisplayName("newPath")
	@Nested
	public class NewPath {

		private AStarPathStub path;

		@BeforeEach
		public void setUp() {
			path = astar.newPath(A, AB);
		}

		@DisplayName("(PT,ST) #1")
		@Test
		public void newPathPTST_1() {
			assertNotNull(path);
		}

		@DisplayName("(PT,ST) #2")
		@Test
		public void newPathPTST_2() {
			assertEquals(1, path.size());
		}

		@DisplayName("(PT,ST) #3")
		@Test
		public void newPathPTST_3() {
			assertEquals(A, path.getFirstPoint());
		}

		@DisplayName("(PT,ST) #4")
		@Test
		public void newPathPTST_4() {
			assertEquals(B, path.getLastPoint());
		}

		@DisplayName("(PT,ST) #5")
		@Test
		public void newPathPTST_5() {
			assertEquals(AB, path.getFirstSegment());
		}

		@DisplayName("(PT,ST) #6")
		@Test
		public void newPathPTST_6() {
			assertEquals(AB, path.getLastSegment());
		}
	}

	@DisplayName("findPath")
	@Nested
	public class FindPath {

		@BeforeEach
		public void setUp() {
			reset();
		}

		@DisplayName("(PT,PT) #1")
		@Test
		public void findPathPTPT_1() {
			A.setCost(0f);
			A.setEstimatedCost(h.evaluate(A, B));
			var list = astar.findPath(A, B);
			
			assertNotNull(list);
			assertEquals(2, list.size());
			assertTrue(list.contains(A));
			assertTrue(list.contains(B));
		}

		@DisplayName("(PT,PT) #2")
		@Test
		public void findPathPTPT_2() {
			A.setCost(0f);
			A.setEstimatedCost(h.evaluate(A, C));
			var list = astar.findPath(A, C);
			
			assertNotNull(list);
			assertEquals(4, list.size());
			assertTrue(list.contains(A));
			assertTrue(list.contains(B));
			assertTrue(list.contains(D));
			assertTrue(list.contains(C));
		}

		@DisplayName("(PT,PT) #3")
		@Test
		public void findPathPTPT_3() {
			A.setCost(0f);
			A.setEstimatedCost(h.evaluate(A, D));
			var list = astar.findPath(A, D);
			
			assertNotNull(list);
			assertEquals(3, list.size());
			assertTrue(list.contains(A));
			assertTrue(list.contains(B));
			assertTrue(list.contains(D));
		}

		@DisplayName("(PT,PT) #4")
		@Test
		public void findPathPTPT_4() {
			A.setCost(0f);
			A.setEstimatedCost(h.evaluate(A, E));
			var list = astar.findPath(A, E);
			
			assertNotNull(list);
			assertEquals(4, list.size());
			assertTrue(list.contains(A));
			assertTrue(list.contains(B));
			assertTrue(list.contains(D));
			assertTrue(list.contains(E));
		}

		@DisplayName("(PT,PT) #5")
		@Test
		public void findPathPTPT_5() {
			B.setCost(0f);
			B.setEstimatedCost(h.evaluate(B, A));
			var list = astar.findPath(B, A);
			
			assertNotNull(list);
			assertEquals(4, list.size());
			assertTrue(list.contains(B));
			assertTrue(list.contains(D));
			assertTrue(list.contains(C));
			assertTrue(list.contains(E));
		}
	}

	@DisplayName("createPath")
	@Nested
	public class CreatePath {

		private CloseComparator<AStarEdgeStub,AStarNodeStub> cComparator;;
		private List<AStarNode<AStarEdgeStub,AStarNodeStub>> list;

		@BeforeEach
		public void setUp() {
			cComparator = new CloseComparator<>();
			list = new ArrayList<>();
			reset();
		}

		@DisplayName("(PT,PT) #1")
		@Test
		public void createPathPTPTList_1() {
			B.setArrivalConnection(AB);
			list.clear();
			list.add(A);
			list.add(B);
			Collections.sort(list, cComparator);
			var path = astar.createPath(A, B, list);
			assertNotNull(path);
			assertEquals(1, path.size());
			assertSame(AB, path.get(0));
		}

		@DisplayName("(PT,PT) #2")
		@Test
		public void createPathPTPTList_2() {
			B.setArrivalConnection(AB);
			D.setArrivalConnection(BD);
			E.setArrivalConnection(DE);
			list.clear();
			list.add(A);
			list.add(B);
			list.add(D);
			list.add(E);
			Collections.sort(list, cComparator);
			var path = astar.createPath(A, E, list);
			assertNotNull(path);
			assertEquals(3, path.size());
			assertSame(AB, path.get(0));
			assertSame(BD, path.get(1));
			assertSame(DE, path.get(2));
		}

		@DisplayName("(PT,PT) #3")
		@Test
		public void createPathPTPTList_3() {
			B.setArrivalConnection(AB);
			D.setArrivalConnection(BD);
			list.clear();
			list.add(A);
			list.add(B);
			list.add(D);
			Collections.sort(list, cComparator);
			var path = astar.createPath(A, D, list);
			assertNotNull(path);
			assertEquals(2, path.size());
			assertSame(AB, path.get(0));
			assertSame(BD, path.get(1));
		}

		@DisplayName("(PT,PT) #4")
		@Test
		public void createPathPTPTList_4() {
			B.setArrivalConnection(AB);
			D.setArrivalConnection(BD);
			C.setArrivalConnection(DC);
			list.clear();
			list.add(A);
			list.add(B);
			list.add(D);
			list.add(C);
			Collections.sort(list, cComparator);
			var path = astar.createPath(A, C, list);
			assertNotNull(path);
			assertEquals(3, path.size());
			assertSame(AB, path.get(0));
			assertSame(BD, path.get(1));
			assertSame(DC, path.get(2));
		}
	}

	@DisplayName("solve")
	@Nested
	public class Solve {

		@BeforeEach
		public void setUp() {
			reset();
		}

		@DisplayName("(PT,PT) #1")
		@Test
		public void solvePTPT_1() {
			var path = astar.solve(A, B);
			assertNotNull(path);
			assertEquals(1, path.size());
			assertSame(AB, path.get(0));
		}

		@DisplayName("(PT,PT) #2")
		@Test
		public void solvePTPT_2() {
			var path = astar.solve(A, E);
			assertNotNull(path);
			assertEquals(3, path.size());
			assertSame(AB, path.get(0));
			assertSame(BD, path.get(1));
			assertSame(DE, path.get(2));
		}

		@DisplayName("(PT,PT) #3")
		@Test
		public void solvePTPT_3() {
			var path = astar.solve(A, D);
			assertNotNull(path);
			assertEquals(2, path.size());
			assertSame(AB, path.get(0));
			assertSame(BD, path.get(1));
		}

		@DisplayName("(PT,PT) #4")
		@Test
		public void solvePTPT_4() {
			var path = astar.solve(A, C);
			assertNotNull(path);
			assertEquals(3, path.size());
			assertSame(AB, path.get(0));
			assertSame(BD, path.get(1));
			assertSame(DC, path.get(2));
		}
	}

}

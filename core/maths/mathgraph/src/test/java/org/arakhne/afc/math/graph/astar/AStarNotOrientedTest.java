/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.graph.astar.AStar.CloseComparator;
import org.arakhne.afc.testtools.AbstractTestCase;

@SuppressWarnings("all")
public class AStarNotOrientedTest extends AbstractTestCase {

	private AStar<AStarPathStub,AStarEdgeStub,AStarNodeStub> astar;
	private AStarNodeStub A, B, C, D, E;
	private AStarEdgeStub AB, CA, DA, EC;
	private AStarHeuristicStub h;
	
	/**
	 * @throws Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.h = new AStarHeuristicStub();
		this.astar = new AStar<>(this.h, AStarPathStub.class);
		this.A = new AStarNodeStub("A", 0, 0);  //$NON-NLS-1$
		this.B = new AStarNodeStub("B", 20, 0);  //$NON-NLS-1$
		this.C = new AStarNodeStub("C", 0, 20);  //$NON-NLS-1$
		this.D = new AStarNodeStub("D", 20, 20);  //$NON-NLS-1$
		this.E = new AStarNodeStub("E", 10, 40);  //$NON-NLS-1$
		this.AB = new AStarEdgeStub("A-B", this.A, this.B, false);  //$NON-NLS-1$
		this.CA = new AStarEdgeStub("C-A", this.C, this.A, false);  //$NON-NLS-1$
		this.DA = new AStarEdgeStub("D-A", this.D, this.A, false);  //$NON-NLS-1$
		this.EC = new AStarEdgeStub("E-C", this.E, this.C, false);  //$NON-NLS-1$
		reset();
	}
	
	/**
	 * @throws Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.astar = null;
		this.AB = this.CA = this.DA = this.EC = null;
		this.A = this.B = this.C = this.D = this.E = null;
		this.h = null;
	}
	
	private void reset() {
		this.A.setArrivalConnection(null);
		this.A.setCost(Double.NaN);
		this.A.setEstimatedCost(Double.NaN);
		this.B.setArrivalConnection(null);
		this.B.setCost(Double.NaN);
		this.B.setEstimatedCost(Double.NaN);
		this.C.setArrivalConnection(null);
		this.C.setCost(Double.NaN);
		this.C.setEstimatedCost(Double.NaN);
		this.D.setArrivalConnection(null);
		this.D.setCost(Double.NaN);
		this.D.setEstimatedCost(Double.NaN);
		this.E.setArrivalConnection(null);
		this.E.setCost(Double.NaN);
		this.E.setEstimatedCost(Double.NaN);
	}
	
	private static double dist(double a, double b) {
		return Math.hypot(a, b);
	}
	
	/**
	 */
	@Test
	public void estimatePTPT() {
		double dDist = dist(20f,20f);
		double mlDist = dist(10f,40f);
		double msDist = dist(10f,20f);
		
		assertEpsilonEquals(0f, 				this.astar.estimate(this.A, this.A));
		assertEpsilonEquals(20f, 				this.astar.estimate(this.A, this.B));
		assertEpsilonEquals(20f, 				this.astar.estimate(this.A, this.C));
		assertEpsilonEquals(dDist,				this.astar.estimate(this.A, this.D));
		assertEpsilonEquals(mlDist,				this.astar.estimate(this.A, this.E));

		assertEpsilonEquals(20f, 				this.astar.estimate(this.B, this.A));
		assertEpsilonEquals(0f, 				this.astar.estimate(this.B, this.B));
		assertEpsilonEquals(dDist,				this.astar.estimate(this.B, this.C));
		assertEpsilonEquals(20f,				this.astar.estimate(this.B, this.D));
		assertEpsilonEquals(mlDist,				this.astar.estimate(this.B, this.E));

		assertEpsilonEquals(20f, 				this.astar.estimate(this.C, this.A));
		assertEpsilonEquals(dDist, 				this.astar.estimate(this.C, this.B));
		assertEpsilonEquals(0f,					this.astar.estimate(this.C, this.C));
		assertEpsilonEquals(20f,				this.astar.estimate(this.C, this.D));
		assertEpsilonEquals(msDist,				this.astar.estimate(this.C, this.E));

		assertEpsilonEquals(dDist, 				this.astar.estimate(this.D, this.A));
		assertEpsilonEquals(20f, 				this.astar.estimate(this.D, this.B));
		assertEpsilonEquals(20f,				this.astar.estimate(this.D, this.C));
		assertEpsilonEquals(0f,					this.astar.estimate(this.D, this.D));
		assertEpsilonEquals(msDist,				this.astar.estimate(this.D, this.E));

		assertEpsilonEquals(mlDist,				this.astar.estimate(this.E, this.A));
		assertEpsilonEquals(mlDist,				this.astar.estimate(this.E, this.B));
		assertEpsilonEquals(msDist,				this.astar.estimate(this.E, this.C));
		assertEpsilonEquals(msDist,				this.astar.estimate(this.E, this.D));
		assertEpsilonEquals(0f,					this.astar.estimate(this.E, this.E));
	}

	/**
	 */
	@Test
	public void newPathPTST() {
		AStarPathStub path = this.astar.newPath(this.A, this.AB);
		assertNotNull(path);
		assertEquals(1, path.size());
		assertEquals(this.A, path.getFirstPoint());
		assertEquals(this.B, path.getLastPoint());
		assertEquals(this.AB, path.getFirstSegment());
		assertEquals(this.AB, path.getLastSegment());
	}

	/**
	 */
	@Test
	public void findPathPTPT() {
		List<AStarNode<AStarEdgeStub,AStarNodeStub>> list;
		
		reset();
		this.A.setCost(0f);
		this.A.setEstimatedCost(this.h.evaluate(this.A, this.B));
		list = this.astar.findPath(this.A, this.B);
		
		assertNotNull(list);
		assertEquals(2, list.size());
		assertTrue(list.contains(this.A));
		assertTrue(list.contains(this.B));

		reset();
		this.A.setCost(0f);
		this.A.setEstimatedCost(this.h.evaluate(this.A, this.C));
		list = this.astar.findPath(this.A, this.C);
		
		assertNotNull(list);
		assertEquals(2, list.size());
		assertTrue(list.contains(this.A));
		assertTrue(list.contains(this.C));

		reset();
		this.A.setCost(0f);
		this.A.setEstimatedCost(this.h.evaluate(this.A, this.D));
		list = this.astar.findPath(this.A, this.D);
		
		assertNotNull(list);
		assertEquals(2, list.size());
		assertTrue(list.contains(this.A));
		assertTrue(list.contains(this.D));

		reset();
		this.A.setCost(0f);
		this.A.setEstimatedCost(this.h.evaluate(this.A, this.E));
		list = this.astar.findPath(this.A, this.E);
		
		assertNotNull(list);
		assertEquals(3, list.size());
		assertTrue(list.contains(this.A));
		assertTrue(list.contains(this.C));
		assertTrue(list.contains(this.E));
	}

	/**
	 */
	@Test
	public void createPathPTPTList() {
		CloseComparator<AStarEdgeStub,AStarNodeStub> cComparator = new CloseComparator<>();
		AStarPathStub path;
		List<AStarNode<AStarEdgeStub,AStarNodeStub>> list = new ArrayList<>();
		
		reset();
		this.B.setArrivalConnection(this.AB);
		list.clear();
		list.add(this.A);
		list.add(this.B);
		Collections.sort(list, cComparator);
		path = this.astar.createPath(this.A, this.B, list);
		assertNotNull(path);
		assertEquals(1, path.size());
		assertSame(this.AB, path.get(0));

		reset();
		this.C.setArrivalConnection(this.CA);
		this.E.setArrivalConnection(this.EC);
		list.clear();
		list.add(this.A);
		list.add(this.C);
		list.add(this.E);
		Collections.sort(list, cComparator);
		path = this.astar.createPath(this.A, this.E, list);
		assertNotNull(path);
		assertEquals(2, path.size());
		assertSame(this.CA, path.get(0));
		assertSame(this.EC, path.get(1));

		reset();
		this.D.setArrivalConnection(this.DA);
		list.clear();
		list.add(this.A);
		list.add(this.D);
		Collections.sort(list, cComparator);
		path = this.astar.createPath(this.A, this.D, list);
		assertNotNull(path);
		assertEquals(1, path.size());
		assertSame(this.DA, path.get(0));

		reset();
		this.C.setArrivalConnection(this.CA);
		list.clear();
		list.add(this.A);
		list.add(this.C);
		Collections.sort(list, cComparator);
		path = this.astar.createPath(this.A, this.C, list);
		assertNotNull(path);
		assertEquals(1, path.size());
		assertSame(this.CA, path.get(0));
	}

	/**
	 */
	@Test
	public void solvePTPT() {
		AStarPathStub path;
		
		reset();
		path = this.astar.solve(this.A, this.B);
		assertNotNull(path);
		assertEquals(1, path.size());
		assertSame(this.AB, path.get(0));

		reset();
		path = this.astar.solve(this.A, this.E);
		assertNotNull(path);
		assertEquals(2, path.size());
		assertSame(this.CA, path.get(0));
		assertSame(this.EC, path.get(1));

		reset();
		path = this.astar.solve(this.A, this.D);
		assertNotNull(path);
		assertEquals(1, path.size());
		assertSame(this.DA, path.get(0));

		reset();
		path = this.astar.solve(this.A, this.C);
		assertNotNull(path);
		assertEquals(1, path.size());
		assertSame(this.CA, path.get(0));
	}
	
}

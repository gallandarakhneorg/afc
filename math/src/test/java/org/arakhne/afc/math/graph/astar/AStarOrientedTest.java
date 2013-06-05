/* 
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.graph.astar;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.graph.astar.AStar.CloseComparator;
import org.arakhne.afc.util.CollectionUtil;

import junit.framework.TestCase;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class AStarOrientedTest extends TestCase {

	private AStar<AStarPathStub,AStarEdgeStub,AStarNodeStub> astar;
	private AStarNodeStub A, B, C, D, E;
	@SuppressWarnings("unused")
	private AStarEdgeStub AB, CA, DA, BD, EC, DE, DC;
	private AStarHeuristicStub h;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.h = new AStarHeuristicStub();
		this.astar = new AStar<AStarPathStub,AStarEdgeStub,AStarNodeStub>(this.h, AStarPathStub.class);
		this.A = new AStarNodeStub("A", 0, 0); //$NON-NLS-1$
		this.B = new AStarNodeStub("B", 20, 0); //$NON-NLS-1$
		this.C = new AStarNodeStub("C", 0, 20); //$NON-NLS-1$
		this.D = new AStarNodeStub("D", 20, 20); //$NON-NLS-1$
		this.E = new AStarNodeStub("E", 10, 40); //$NON-NLS-1$
		this.AB = new AStarEdgeStub("A-B", this.A, this.B, true); //$NON-NLS-1$
		this.BD = new AStarEdgeStub("B-D", this.B, this.D, true); //$NON-NLS-1$
		this.CA = new AStarEdgeStub("C-A", this.C, this.A, true); //$NON-NLS-1$
		this.DA = new AStarEdgeStub("D-A", this.D, this.A, true); //$NON-NLS-1$
		this.DC = new AStarEdgeStub("D-C", this.D, this.C, true); //$NON-NLS-1$
		this.DE = new AStarEdgeStub("D-E", this.D, this.E, true); //$NON-NLS-1$
		this.EC = new AStarEdgeStub("E-C", this.E, this.C, true); //$NON-NLS-1$
		reset();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void tearDown() throws Exception {
		this.astar = null;
		this.AB = this.CA = this.DA = this.BD = this.EC = this.DE = this.DC = null;
		this.A = this.B = this.C = this.D = this.E = null;
		this.h = null;
		super.tearDown();
	}
	
	private void reset() {
		this.A.setArrivalConnection(null);
		this.A.setCost(Float.NaN);
		this.A.setEstimatedCost(Float.NaN);
		this.B.setArrivalConnection(null);
		this.B.setCost(Float.NaN);
		this.B.setEstimatedCost(Float.NaN);
		this.C.setArrivalConnection(null);
		this.C.setCost(Float.NaN);
		this.C.setEstimatedCost(Float.NaN);
		this.D.setArrivalConnection(null);
		this.D.setCost(Float.NaN);
		this.D.setEstimatedCost(Float.NaN);
		this.E.setArrivalConnection(null);
		this.E.setCost(Float.NaN);
		this.E.setEstimatedCost(Float.NaN);
	}

	private void assertEpsilonEquals(float expected, float actual) {
		float d = Math.abs(expected - actual);
		if (d>0.0000001) {
			fail("expected: "+expected+"; actual: "+actual); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	private float dist(float a, float b) {
		return (float)Math.sqrt(a*a+b*b);
	}
	
	/**
	 */
	public void testEstimatePTPT() {
		float dDist = dist(20f,20f);
		float mlDist = dist(10f,40f);
		float msDist = dist(10f,20f);
		
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
	public void testNewPathPTST() {
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
	public void testFindPathPTPT() {
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
		assertEquals(4, list.size());
		assertTrue(list.contains(this.A));
		assertTrue(list.contains(this.B));
		assertTrue(list.contains(this.D));
		assertTrue(list.contains(this.C));

		reset();
		this.A.setCost(0f);
		this.A.setEstimatedCost(this.h.evaluate(this.A, this.D));
		list = this.astar.findPath(this.A, this.D);
		
		assertNotNull(list);
		assertEquals(3, list.size());
		assertTrue(list.contains(this.A));
		assertTrue(list.contains(this.B));
		assertTrue(list.contains(this.D));

		reset();
		this.A.setCost(0f);
		this.A.setEstimatedCost(this.h.evaluate(this.A, this.E));
		list = this.astar.findPath(this.A, this.E);
		
		assertNotNull(list);
		assertEquals(4, list.size());
		assertTrue(list.contains(this.A));
		assertTrue(list.contains(this.B));
		assertTrue(list.contains(this.D));
		assertTrue(list.contains(this.E));

		reset();
		this.B.setCost(0f);
		this.B.setEstimatedCost(this.h.evaluate(this.B, this.A));
		list = this.astar.findPath(this.B, this.A);
		
		assertNotNull(list);
		assertEquals(3, list.size());
		assertTrue(list.contains(this.B));
		assertTrue(list.contains(this.D));
		assertTrue(list.contains(this.A));
	}

	/**
	 */
	public void testCreatePathPTPTList() {
		CloseComparator<AStarEdgeStub,AStarNodeStub> cComparator = new CloseComparator<AStarEdgeStub,AStarNodeStub>();
		AStarPathStub path;
		List<AStarNode<AStarEdgeStub,AStarNodeStub>> list = new ArrayList<AStarNode<AStarEdgeStub,AStarNodeStub>>();
		
		reset();
		this.B.setArrivalConnection(this.AB);
		list.clear();
		CollectionUtil.add(cComparator, this.A, list);
		CollectionUtil.add(cComparator, this.B, list);
		path = this.astar.createPath(this.A, this.B, list);
		assertNotNull(path);
		assertEquals(1, path.size());
		assertSame(this.AB, path.get(0));

		reset();
		this.B.setArrivalConnection(this.AB);
		this.D.setArrivalConnection(this.BD);
		this.E.setArrivalConnection(this.DE);
		list.clear();
		CollectionUtil.add(cComparator, this.A, list);
		CollectionUtil.add(cComparator, this.B, list);
		CollectionUtil.add(cComparator, this.D, list);
		CollectionUtil.add(cComparator, this.E, list);
		path = this.astar.createPath(this.A, this.E, list);
		assertNotNull(path);
		assertEquals(3, path.size());
		assertSame(this.AB, path.get(0));
		assertSame(this.BD, path.get(1));
		assertSame(this.DE, path.get(2));

		reset();
		this.B.setArrivalConnection(this.AB);
		this.D.setArrivalConnection(this.BD);
		list.clear();
		CollectionUtil.add(cComparator, this.A, list);
		CollectionUtil.add(cComparator, this.B, list);
		CollectionUtil.add(cComparator, this.D, list);
		path = this.astar.createPath(this.A, this.D, list);
		assertNotNull(path);
		assertEquals(2, path.size());
		assertSame(this.AB, path.get(0));
		assertSame(this.BD, path.get(1));

		reset();
		this.B.setArrivalConnection(this.AB);
		this.D.setArrivalConnection(this.BD);
		this.C.setArrivalConnection(this.DC);
		list.clear();
		CollectionUtil.add(cComparator, this.A, list);
		CollectionUtil.add(cComparator, this.B, list);
		CollectionUtil.add(cComparator, this.D, list);
		CollectionUtil.add(cComparator, this.C, list);
		path = this.astar.createPath(this.A, this.C, list);
		assertNotNull(path);
		assertEquals(3, path.size());
		assertSame(this.AB, path.get(0));
		assertSame(this.BD, path.get(1));
		assertSame(this.DC, path.get(2));
	}

	/**
	 */
	public void testSolvePTPT() {
		AStarPathStub path;
		
		reset();
		path = this.astar.solve(this.A, this.B);
		assertNotNull(path);
		assertEquals(1, path.size());
		assertSame(this.AB, path.get(0));

		reset();
		path = this.astar.solve(this.A, this.E);
		assertNotNull(path);
		assertEquals(3, path.size());
		assertSame(this.AB, path.get(0));
		assertSame(this.BD, path.get(1));
		assertSame(this.DE, path.get(2));

		reset();
		path = this.astar.solve(this.A, this.D);
		assertNotNull(path);
		assertEquals(2, path.size());
		assertSame(this.AB, path.get(0));
		assertSame(this.BD, path.get(1));

		reset();
		path = this.astar.solve(this.A, this.C);
		assertNotNull(path);
		assertEquals(3, path.size());
		assertSame(this.AB, path.get(0));
		assertSame(this.BD, path.get(1));
		assertSame(this.DC, path.get(2));
	}

}

/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
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
package org.arakhne.afc.math.bounds.bounds2d;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.bounds.bounds2f.BoundingCircle;
import org.arakhne.afc.math.bounds.bounds2f.CombinableBounds2D;
import org.arakhne.afc.math.bounds.bounds2f.MinimumBoundingRectangle;
import org.arakhne.afc.math.bounds.bounds2f.OrientedBoundingRectangle;
import org.arakhne.afc.math.bounds.bounds3f.AlignedBoundingBox;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.intersection.IntersectionType;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.sizediterator.SizedIterator;


/**
 * Unit test for MinimumBoundingRectangle class.
 * 
 * @author $Author: sgalland$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MinimumBoundingRectangleTest extends AbstractOrientedBound2DTest {

	/** Reference instance.
	 */
	private MinimumBoundingRectangle testingBounds;
	
	/** Reference instance.
	 */
	private Point2f[] testingPoints;

	/** Reference lower point.
	 */
	private Point2f lowerPoint;

	/** Reference upper point.
	 */
	private Point2f upperPoint;

	/** Reference center point.
	 */
	private Point2f centerPoint;

	/**
	 * {@inheritDoc}
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.testingPoints = randomPoints2D();
		this.lowerPoint = new Point2f();
		this.upperPoint = new Point2f();
		computeLowerUpper(this.testingPoints,this.lowerPoint,this.upperPoint);
		this.centerPoint = new Point2f(
				(this.lowerPoint.getX()+this.upperPoint.getX())/2.f,
				(this.lowerPoint.getY()+this.upperPoint.getY())/2.f);
		this.testingBounds = createBounds(this.testingPoints);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		this.testingBounds = null;
		this.testingPoints = null;
		this.lowerPoint = null;
		this.upperPoint = null;
		super.tearDown();
	}

	/**
	 * @param points
	 * @return bounds
	 */
	protected MinimumBoundingRectangle createBounds(Tuple2f[] points) {
		MinimumBoundingRectangle b = new MinimumBoundingRectangle();
		b.set(points);
		return b;
	}

	/** Compute the lower, upper and center points.
	 * @param points
	 * @param lower
	 * @param upper
	 */
	private static void computeLowerUpper(Tuple2f[] points, Tuple2f<?> lower, Tuple2f<?> upper) {
		float lx = Float.POSITIVE_INFINITY;
		float ly = Float.POSITIVE_INFINITY;
		float ux = Float.NEGATIVE_INFINITY;
		float uy = Float.NEGATIVE_INFINITY;
		
		for(Tuple2f<?> tuple : points) {
			if (tuple.getX()<lx) lx = tuple.getX();
			if (tuple.getY()<ly) ly = tuple.getY();
			if (tuple.getX()>ux) ux = tuple.getX();
			if (tuple.getY()>uy) uy = tuple.getY();
		}
		
		if (lower!=null) lower.set(lx,ly);
		if (upper!=null) upper.set(ux,uy);
	}

	@Override
	public void testGetMathematicalDimension() {
		assertTrue(this.testingBounds.isInit());
		
		assertEquals(PseudoHamelDimension.DIMENSION_2D, this.testingBounds.getMathematicalDimension());
	}

	@Override
	public void testGetCenterX() {
		assertTrue(this.testingBounds.isInit());

		assertEpsilonEquals((this.lowerPoint.getX()+this.upperPoint.getX())/2.f, this.testingBounds.getCenterX());
	}

	@Override
	public void testGetCenterY() {
		assertTrue(this.testingBounds.isInit());

		assertEpsilonEquals((this.lowerPoint.getY()+this.upperPoint.getY())/2.f, this.testingBounds.getCenterY());
	}

	@Override
	public void testGetMaxX() {
		assertTrue(this.testingBounds.isInit());

		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getMaxX());
	}

	@Override
	public void testGetMaxY() {
		assertTrue(this.testingBounds.isInit());

		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getMaxY());
	}

	@Override
	public void testGetMinX() {
		assertTrue(this.testingBounds.isInit());

		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getMinX());
	}

	@Override
	public void testGetMinY() {
		assertTrue(this.testingBounds.isInit());

		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getMinY());
	}

	@Override
	public void testDistanceMaxP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p;
		float dist;
		
		p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(100,100));
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(p.distance(this.upperPoint), dist);
		
		p = new Point2f(this.upperPoint);
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(p.distance(this.lowerPoint), dist);

		p = new Point2f(this.upperPoint.getX(), this.lowerPoint.getY());
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(
				p.distance(new Point2f(this.lowerPoint.getX(), this.upperPoint.getY())), dist);

		p = new Point2f(this.centerPoint);
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(p.distance(this.upperPoint), dist);

		p = new Point2f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f);
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(
				p.distance(this.upperPoint), dist);
	}

	@Override
	public void testDistanceMaxSquaredP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p;
		float dist;
		
		p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(100,100));
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(p.distanceSquared(this.upperPoint), dist);
		
		p = new Point2f(this.upperPoint);
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(p.distanceSquared(this.lowerPoint), dist);

		p = new Point2f(this.upperPoint.getX(), this.lowerPoint.getY());
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(
				p.distanceSquared(new Point2f(this.lowerPoint.getX(), this.upperPoint.getY())), dist);

		p = new Point2f(this.centerPoint);
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(p.distanceSquared(this.upperPoint), dist);

		p = new Point2f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f);
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(
				p.distanceSquared(this.upperPoint), dist);
	}

	@Override
	public void testDistanceP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p;
		float dist;
		
		p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(100,100));
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(p.distance(this.lowerPoint), dist);
		
		p = new Point2f(this.upperPoint);
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point2f(this.upperPoint.getX(), this.lowerPoint.getY());
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point2f();
		p.add(this.upperPoint, this.lowerPoint);
		p.scale(.5f);
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point2f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f);
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(100, dist);
	}

	@Override
	public void testDistanceSquaredP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p;
		float dist;
		
		p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(100,100));
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(p.distanceSquared(this.lowerPoint), dist);
		
		p = new Point2f(this.upperPoint);
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point2f(this.upperPoint.getX(), this.lowerPoint.getY());
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point2f();
		p.add(this.upperPoint, this.lowerPoint);
		p.scale(.5f);
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point2f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f);
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(100.f*100.f, dist);
	}

	@Override
	public void testGetCenter() {
		assertTrue(this.testingBounds.isInit());

		Point2f c = this.testingBounds.getCenter();
		assertEpsilonEquals((this.lowerPoint.getX()+this.upperPoint.getX())/2.f, c.getX());
		assertEpsilonEquals((this.lowerPoint.getY()+this.upperPoint.getY())/2.f, c.getY());
	}

	@Override
	public void testGetLower() {
		assertTrue(this.testingBounds.isInit());

		Point2f c = this.testingBounds.getLower();
		assertEpsilonEquals(this.lowerPoint.getX(), c.getX());
		assertEpsilonEquals(this.lowerPoint.getY(), c.getY());
	}

	@Override
	public void testGetLowerUpper() {
		assertTrue(this.testingBounds.isInit());

		Point2f l = new Point2f();
		Point2f u = new Point2f();
		
		this.testingBounds.getLowerUpper(l, u);
		assertEpsilonEquals(this.lowerPoint.getX(), l.getX());
		assertEpsilonEquals(this.lowerPoint.getY(), l.getY());
		assertEpsilonEquals(this.upperPoint.getX(), u.getX());
		assertEpsilonEquals(this.upperPoint.getY(), u.getY());
	}

	@Override
	public void testGetPosition() {
		assertTrue(this.testingBounds.isInit());

		Point2f c = this.testingBounds.getPosition();
		assertEpsilonEquals((this.lowerPoint.getX()+this.upperPoint.getX())/2.f, c.getX());
		assertEpsilonEquals((this.lowerPoint.getY()+this.upperPoint.getY())/2.f, c.getY());
	}

	@Override
	public void testGetUpper() {
		assertTrue(this.testingBounds.isInit());

		Point2f c = this.testingBounds.getUpper();
		assertEpsilonEquals(this.upperPoint.getX(), c.getX());
		assertEpsilonEquals(this.upperPoint.getY(), c.getY());
	}

	@Override
	public void testIsEmpty() {
		assertFalse(this.testingBounds.isEmpty());
	}

	@Override
	public void testIsInit() {
		assertTrue(this.testingBounds.isInit());
	}

	@Override
	public void testReset() {
		assertTrue(this.testingBounds.isInit());

		if (this.lowerPoint.distanceLinf(this.upperPoint)>0)
			assertFalse(this.testingBounds.isEmpty());
		else
			assertTrue(this.testingBounds.isEmpty());
		this.testingBounds.reset();
		assertFalse(this.testingBounds.isInit());
		assertTrue(this.testingBounds.isEmpty());
	}

	@Override
	public void testClone() {
		assertTrue(this.testingBounds.isInit());

		MinimumBoundingRectangle clone = this.testingBounds.clone();
		assertNotSame(this.testingBounds, clone);
		assertNotSame(this.testingBounds.getLower(), clone.getLower());
		assertEquals(this.testingBounds.getLower(), clone.getLower());
		assertNotSame(this.testingBounds.getUpper(), clone.getUpper());
		assertEquals(this.testingBounds.getUpper(), clone.getUpper());
	}

	@Override
	public void testTranslateVector2f() {
		assertTrue(this.testingBounds.isInit());

		Vector2f v = randomVector2D();
		this.testingBounds.translate(v);
		assertEpsilonEquals(this.lowerPoint.getX()+v.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY()+v.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getX()+v.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY()+v.getY(), this.testingBounds.getUpper().getY());
	}

	private boolean epsilonRemove(Set<? extends Tuple2f> s, Tuple2f<?> t) {
		Iterator<? extends Tuple2f> iterator = s.iterator();
		Tuple2f<?> candidate;
		while (iterator.hasNext()) {
			candidate = iterator.next();
			if (isEpsilonEquals(candidate.getX(), t.getX()) && isEpsilonEquals(candidate.getY(), t.getY())) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void testGetGlobalOrientedVertices() {
		assertTrue(this.testingBounds.isInit());

		Set<Point2f> expectedPoints = new HashSet<Point2f>();
		Point2f p;
		
		expectedPoints.add(new Point2f(this.lowerPoint.getX(), this.lowerPoint.getY()));
		expectedPoints.add(new Point2f(this.lowerPoint.getX(), this.upperPoint.getY()));
		expectedPoints.add(new Point2f(this.upperPoint.getX(), this.lowerPoint.getY()));
		expectedPoints.add(new Point2f(this.upperPoint.getX(), this.upperPoint.getY()));
		
		SizedIterator<? extends Point2f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
		assertNotNull(iterator);
		assertEquals(4, iterator.totalSize());
		
		for(int i=0; i<4; ++i) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(i, iterator.index());
			assertTrue(i+"; point: "+p+"; inside: "+expectedPoints.toString(), epsilonRemove(expectedPoints,p));  //$NON-NLS-1$//$NON-NLS-2$
		}
		
		assertFalse(iterator.hasNext());
		assertTrue(expectedPoints.isEmpty());
	}

	@Override
	public void testGetGlobalVertexAt() {
		assertTrue(this.testingBounds.isInit());

		List<Point2f> expectedPoints = new ArrayList<Point2f>();
		Point2f p;
		
		expectedPoints.add(new Point2f(this.upperPoint.getX(), this.upperPoint.getY()));
		expectedPoints.add(new Point2f(this.lowerPoint.getX(), this.upperPoint.getY()));
		expectedPoints.add(new Point2f(this.lowerPoint.getX(), this.lowerPoint.getY()));
		expectedPoints.add(new Point2f(this.upperPoint.getX(), this.lowerPoint.getY()));

		try {
			this.testingBounds.getGlobalVertexAt(-1);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
		
		for(int i=0; i<4; ++i) {
			p = this.testingBounds.getGlobalVertexAt(i);
			assertNotNull(p);
			assertEpsilonEquals(expectedPoints.get(i), p);
		}

		try {
			this.testingBounds.getGlobalVertexAt(4);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
	}

	@Override
	public void testGetVertexCount() {
		assertTrue(this.testingBounds.isInit());
		assertEquals(4, this.testingBounds.getVertexCount());
	}

	@Override
	public void testGetLocalOrientedVertices() {
		assertTrue(this.testingBounds.isInit());

		Set<Vector2f> expectedPoints = new HashSet<Vector2f>();
		Vector2f v;
		float sizex = (this.upperPoint.getX() - this.lowerPoint.getX())/2.f;
		float sizey = (this.upperPoint.getY() - this.lowerPoint.getY())/2.f;
		
		expectedPoints.add(new Vector2f(sizex, sizey));
		expectedPoints.add(new Vector2f(sizex, -sizey));
		expectedPoints.add(new Vector2f(-sizex, sizey));
		expectedPoints.add(new Vector2f(-sizex, -sizey));
		
		SizedIterator<Vector2f> iterator = this.testingBounds.getLocalOrientedBoundVertices();
		assertNotNull(iterator);
		assertEquals(4, iterator.totalSize());
		
		for(int i=0; i<4; ++i) {
			assertTrue(iterator.hasNext());
			v = iterator.next();
			assertNotNull(v);
			assertEquals(i, iterator.index());
			assertTrue(i+"; point: "+v+"; inside: "+expectedPoints.toString(), epsilonRemove(expectedPoints,v));  //$NON-NLS-1$//$NON-NLS-2$
		}
		
		assertFalse(iterator.hasNext());
		assertTrue(expectedPoints.isEmpty());
	}

	@Override
	public void testGetLocalVertexAt() {
		assertTrue(this.testingBounds.isInit());

		List<Vector2f> expectedPoints = new ArrayList<Vector2f>();
		Vector2f v;
		float sizex = (this.upperPoint.getX() - this.lowerPoint.getX())/2.f;
		float sizey = (this.upperPoint.getY() - this.lowerPoint.getY())/2.f;
		
		expectedPoints.add(new Vector2f(sizex, sizey));
		expectedPoints.add(new Vector2f(-sizex, sizey));
		expectedPoints.add(new Vector2f(-sizex, -sizey));
		expectedPoints.add(new Vector2f(sizex, -sizey));
		
		try {
			this.testingBounds.getLocalVertexAt(-1);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
		
		for(int i=0; i<4; ++i) {
			v = this.testingBounds.getLocalVertexAt(i);
			assertNotNull(v);
			assertEpsilonEquals(expectedPoints.get(i), v);
		}
		
		try {
			this.testingBounds.getLocalVertexAt(4);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
	}

	/**
	 */
	@Override
	public void testGetOrientedBoundAxis() {
		Vector2f[] axis = this.testingBounds.getOrientedBoundAxis();
		assertEpsilonEquals(new Vector2f(1.f,0.f), axis[0]);
		assertEpsilonEquals(new Vector2f(0.f,1.f), axis[1]);
	}

	/**
	 */
	@Override
	public void testGetOrientedBoundExtents() {
		float sx = (this.upperPoint.getX() - this.lowerPoint.getX()) / 2.f;
		float sy = (this.upperPoint.getY() - this.lowerPoint.getY()) / 2.f;		
		Vector2f extent = this.testingBounds.getOrientedBoundExtentVector();
		assertEpsilonEquals(new Vector2f(sx,sy), extent);
	}

	/**
	 */
	@Override
	public void testGetR() {
		Vector2f axis = this.testingBounds.getR();
		assertEpsilonEquals(new Vector2f(1.f,0.f), axis);
	}
	
	/**
	 */
	@Override
	public void testGetS() {
		Vector2f axis = this.testingBounds.getS();
		assertEpsilonEquals(new Vector2f(0.f,1.f), axis);
	}

	/**
	 */
	@Override
	public void testGetRExtent() {
		float expectedExtent = (this.upperPoint.getX() - this.lowerPoint.getX()) / 2.f;
		float extent = this.testingBounds.getRExtent();
		assertEpsilonEquals(expectedExtent, extent);
	}

	/**
	 */
	@Override
	public void testGetSExtent() {
		float expectedExtent = (this.upperPoint.getY() - this.lowerPoint.getY()) / 2.f;
		float extent = this.testingBounds.getSExtent();
		assertEpsilonEquals(expectedExtent, extent);
	}

	@Override
	public void testSetP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p = randomPoint2D();
		this.testingBounds.set(p);
		assertEpsilonEquals(p.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(p.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getUpper().getY());
	}

	@Override
	public void testSetCB() {
		assertTrue(this.testingBounds.isInit());

		MinimumBoundingRectangle box = new MinimumBoundingRectangle();
		Point2f p = randomPoint2D();
		box.set(p);
		this.testingBounds.set(box);
		assertEpsilonEquals(p.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(p.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getUpper().getY());

		BoundingCircle sphere = new BoundingCircle();
		p = randomPoint2D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);
		this.testingBounds.set(sphere);
		assertEpsilonEquals(p.getX()-r, this.testingBounds.getLower().getX());
		assertEpsilonEquals(p.getY()-r, this.testingBounds.getLower().getY());
		assertEpsilonEquals(p.getX()+r, this.testingBounds.getUpper().getX());
		assertEpsilonEquals(p.getY()+r, this.testingBounds.getUpper().getY());
	}

	@Override
	public void testSetTuple2fArray() {
		assertTrue(this.testingBounds.isInit());

		Tuple2f[] points = randomTuples2D();
		Point2f l = new Point2f();
		Point2f u = new Point2f();
		computeLowerUpper(points, l, u);

		this.testingBounds.set(points);

		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(u.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(u.getY(), this.testingBounds.getUpper().getY());
	}

	@Override
	public void testCombineP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p = randomPoint2D();
		this.testingBounds.combine(p);
		assertEpsilonEquals(Math.min(p.getX(),this.lowerPoint.getX()), this.testingBounds.getLower().getX());
		assertEpsilonEquals(Math.min(p.getY(),this.lowerPoint.getY()), this.testingBounds.getLower().getY());
		assertEpsilonEquals(Math.max(p.getX(),this.upperPoint.getX()), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(Math.max(p.getY(),this.upperPoint.getY()), this.testingBounds.getUpper().getY());
	}

	@Override
	public void testCombineCB() {
		assertTrue(this.testingBounds.isInit());

		Point2f l = new Point2f(this.lowerPoint);
		Point2f u = new Point2f(this.upperPoint);
		
		MinimumBoundingRectangle box = new MinimumBoundingRectangle();
		Point2f p = randomPoint2D();
		box.set(p);
		this.testingBounds.combine(box);
		l.setX(Math.min(p.getX(),l.getX()));
		l.setY(Math.min(p.getY(),l.getY()));
		u.setX(Math.max(p.getX(),u.getX()));
		u.setY(Math.max(p.getY(),u.getY()));
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(u.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(u.getY(), this.testingBounds.getUpper().getY());

		BoundingCircle sphere = new BoundingCircle();
		p = randomPoint2D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);
		this.testingBounds.combine(sphere);
		l.setX(Math.min(p.getX()-r,l.getX()));
		l.setY(Math.min(p.getY()-r,l.getY()));
		u.setX(Math.max(p.getX()+r,u.getX()));
		u.setY(Math.max(p.getY()+r,u.getY()));
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(u.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(u.getY(), this.testingBounds.getUpper().getY());
	}

	@Override
	public void testCombineTuple2fArray() {
		assertTrue(this.testingBounds.isInit());

		Tuple2f[] points = randomTuples2D();
		Point2f l = new Point2f();
		Point2f u = new Point2f();
		computeLowerUpper(points, l, u);

		this.testingBounds.combine(points);

		assertEpsilonEquals(Math.min(l.getX(),this.lowerPoint.getX()), this.testingBounds.getLower().getX());
		assertEpsilonEquals(Math.min(l.getY(),this.lowerPoint.getY()), this.testingBounds.getLower().getY());
		assertEpsilonEquals(Math.max(u.getX(),this.upperPoint.getX()), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(Math.max(u.getY(),this.upperPoint.getY()), this.testingBounds.getUpper().getY());
	}

	@Override
	public void testCombineCollection() {
		assertTrue(this.testingBounds.isInit());

		Point2f l = new Point2f(this.lowerPoint);
		Point2f u = new Point2f(this.upperPoint);

		Collection<CombinableBounds2D> collection = new ArrayList<CombinableBounds2D>();

		
		MinimumBoundingRectangle box = new MinimumBoundingRectangle();
		Point2f p = randomPoint2D();
		box.set(p);
		collection.add(box);
		l.setX(Math.min(p.getX(),l.getX()));
		l.setY(Math.min(p.getY(),l.getY()));
		u.setX(Math.max(p.getX(),u.getX()));
		u.setY(Math.max(p.getY(),u.getY()));

		BoundingCircle sphere = new BoundingCircle();
		p = randomPoint2D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);
		collection.add(sphere);
		l.setX(Math.min(p.getX()-r,l.getX()));
		l.setY(Math.min(p.getY()-r,l.getY()));
		u.setX(Math.max(p.getX()+r,u.getX()));
		u.setY(Math.max(p.getY()+r,u.getY()));
		
		this.testingBounds.combine(collection);
		
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(u.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(u.getY(), this.testingBounds.getUpper().getY());
	}
	
	/**
	 */
	public void testGetSize() {
		assertTrue(this.testingBounds.isInit());

		Vector2f size = this.testingBounds.getSize();
		assertEpsilonEquals(this.upperPoint.getX() - this.lowerPoint.getX(), size.getX());
		assertEpsilonEquals(this.upperPoint.getY() - this.lowerPoint.getY(), size.getY());
	}

	/**
	 */
	public void testGetSizeX() {
		assertTrue(this.testingBounds.isInit());

		float size = this.testingBounds.getSizeX();
		assertEpsilonEquals(this.upperPoint.getX() - this.lowerPoint.getX(), size);
	}

	/**
	 */
	public void testGetSizeY() {
		assertTrue(this.testingBounds.isInit());

		float size = this.testingBounds.getSizeY();
		assertEpsilonEquals(this.upperPoint.getY() - this.lowerPoint.getY(), size);
	}

	//-------------------------------------------
	// MinimumBoundingRectangle dedicated tests
	//-------------------------------------------

	/**
	 */
	public void testSetLowerTuple2f() {
		assertTrue(this.testingBounds.isInit());

		Vector2f v = new Vector2f(
				-this.RANDOM.nextFloat()*100.f,
				-this.RANDOM.nextFloat()*100.f);
		Point2f l = new Point2f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l);
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getUpper().getY());

		v = new Vector2f(
				(this.upperPoint.getX() - this.lowerPoint.getX())/2.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())/2.f);
		l = new Point2f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l);
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getUpper().getY());

		v = new Vector2f(
				(this.upperPoint.getX() - this.lowerPoint.getX())+1.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())+2.f);
		l = new Point2f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l);
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());
	}

	/**
	 */
	public void testSetLowerFloatFloatFloat() {
		assertTrue(this.testingBounds.isInit());
		
		Vector2f v = new Vector2f(
				-this.RANDOM.nextFloat()*100.f,
				-this.RANDOM.nextFloat()*100.f);
		Point2f l = new Point2f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l.getX(), l.getY());
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getUpper().getY());

		v = new Vector2f(
				(this.upperPoint.getX() - this.lowerPoint.getX())/2.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())/2.f);
		l = new Point2f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l.getX(), l.getY());
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getUpper().getY());

		v = new Vector2f(
				(this.upperPoint.getX() - this.lowerPoint.getX())+1.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())+2.f);
		l = new Point2f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l.getX(), l.getY());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());
	}

	/**
	 */
	public void testSetUpperTuple2f() {
		assertTrue(this.testingBounds.isInit());

		Vector2f v = new Vector2f(
				this.RANDOM.nextFloat()*100.f,
				this.RANDOM.nextFloat()*100.f);
		Point2f l = new Point2f(this.upperPoint);
		l.add(v);
		this.testingBounds.setUpper(l);
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());

		v = new Vector2f(
				(this.upperPoint.getX() - this.lowerPoint.getX())/2.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())/2.f);
		l = new Point2f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setUpper(l);
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());

		v = new Vector2f(
				(this.upperPoint.getX() - this.lowerPoint.getX())+1.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())+2.f);
		l = new Point2f(this.lowerPoint);
		l.sub(v);
		this.testingBounds.setUpper(l);
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getUpper().getY());
	}

	/**
	 */
	public void testSetUpperFloatFloatFloat() {
		assertTrue(this.testingBounds.isInit());

		Vector2f v = new Vector2f(
				this.RANDOM.nextFloat()*100.f,
				this.RANDOM.nextFloat()*100.f);
		Point2f l = new Point2f(this.upperPoint);
		l.add(v);
		this.testingBounds.setUpper(l.getX(), l.getY());
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());

		v = new Vector2f(
				(this.upperPoint.getX() - this.lowerPoint.getX())/2.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())/2.f);
		l = new Point2f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setUpper(l.getX(), l.getY());
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());

		v = new Vector2f(
				(this.upperPoint.getX() - this.lowerPoint.getX())+1.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())+2.f);
		l = new Point2f(this.upperPoint);
		l.sub(v);
		this.testingBounds.setUpper(l.getX(), l.getY());
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getUpper().getY());
	}

	/**
	 */
	public void testGetCenterTuple2f() {
		assertTrue(this.testingBounds.isInit());

		Point2f c = new Point2f();
		this.testingBounds.getCenter(c);
		assertEpsilonEquals((this.lowerPoint.getX()+this.upperPoint.getX())/2.f, c.getX());
		assertEpsilonEquals((this.lowerPoint.getY()+this.upperPoint.getY())/2.f, c.getY());
	}

	/**
	 */
	public void testGetLowerTuple2f() {
		assertTrue(this.testingBounds.isInit());

		Point2f l = new Point2f();
		this.testingBounds.getLower(l);
		assertEpsilonEquals(this.lowerPoint.getX(), l.getX());
		assertEpsilonEquals(this.lowerPoint.getY(), l.getY());
	}

	/**
	 */
	public void testGetUpperTuple2f() {
		assertTrue(this.testingBounds.isInit());

		Point2f u = new Point2f();
		this.testingBounds.getUpper(u);
		assertEpsilonEquals(this.upperPoint.getX(), u.getX());
		assertEpsilonEquals(this.upperPoint.getY(), u.getY());
	}

	/** 
	 */
	public void testGetNorthEastRectangle() {
		assertTrue(this.testingBounds.isInit());

		MinimumBoundingRectangle rect = this.testingBounds.getNorthEastRectangle();
		
		Point2f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(center.getX(), rect.getLower().getX());
		assertEpsilonEquals(center.getY(), rect.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getX(), rect.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY(), rect.getUpper().getY());
	}
	
	/** 
	 */
	public void testGetNorthWestRectangle() {
		assertTrue(this.testingBounds.isInit());

		MinimumBoundingRectangle rect = this.testingBounds.getNorthWestRectangle();
		
		Point2f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(this.lowerPoint.getX(), rect.getLower().getX());
		assertEpsilonEquals(center.getY(), rect.getLower().getY());
		assertEpsilonEquals(center.getX(), rect.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY(), rect.getUpper().getY());
	}

	/** 
	 */
	public void testGetSouthEastRectangle() {
		assertTrue(this.testingBounds.isInit());

		MinimumBoundingRectangle rect = this.testingBounds.getSouthEastRectangle();
		
		Point2f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(center.getX(), rect.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), rect.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getX(), rect.getUpper().getX());
		assertEpsilonEquals(center.getY(), rect.getUpper().getY());
	}

	/** 
	 */
	public void testGetSouthWestRectangle() {
		assertTrue(this.testingBounds.isInit());

		MinimumBoundingRectangle rect = this.testingBounds.getSouthWestRectangle();
		
		Point2f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(this.lowerPoint.getX(), rect.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), rect.getLower().getY());
		assertEpsilonEquals(center.getX(), rect.getUpper().getX());
		assertEpsilonEquals(center.getY(), rect.getUpper().getY());
	}

	//-------------------------------------------
	// IntersectionClassifier
	//-------------------------------------------

	@Override
	public void testClassifiesP() {
		assertTrue(this.testingBounds.isInit());
		
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(this.centerPoint));

		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(this.lowerPoint));
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(this.upperPoint));
		
		Point2f p = new Point2f();
		
		p.set(this.lowerPoint.getX() - 100, this.lowerPoint.getY() - 100);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
	}

	@Override
	public void testIntersectsP() {
		assertTrue(this.testingBounds.isInit());
		
		assertTrue(this.testingBounds.intersects(this.centerPoint));

		assertTrue(this.testingBounds.intersects(this.lowerPoint));
		assertTrue(this.testingBounds.intersects(this.upperPoint));
		
		Point2f p = new Point2f();
		
		p.set(this.lowerPoint.getX() - 100, this.lowerPoint.getY() - 100);
		assertFalse(this.testingBounds.intersects(p));
	}

	@Override
	public void testClassifiesPP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p1 = new Point2f();
		Point2f p2 = new Point2f();
		
		// A enclosing B
		p1.set(this.centerPoint);
		p1.add(this.lowerPoint);
		p1.scale(.5f);
		p2.set(this.centerPoint);
		p2.add(this.upperPoint);
		p2.scale(.5f);
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(p1,p2));

		// A inside B
		p1.set(this.lowerPoint);
		p1.setX(p1.getX() - 100.f);
		p1.setY(p1.getY() - 100.f);
		p2.set(this.upperPoint);
		p2.setX(p2.getX() + 100.f);
		p2.setY(p2.getY() + 100.f);
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(p1,p2));

		// A OUTSIDE B
		p1.setX(this.upperPoint.getX() + 100.f);
		p1.setY(this.lowerPoint.getY());
		p2.setX(p1.getX() + 100.f);
		p2.setY(this.upperPoint.getY() + 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p1,p2));

		// A has a corner in B
		p1.setX(this.lowerPoint.getX() - 100.f);
		p1.setY(this.lowerPoint.getY() - 100.f);
		p2.setX(this.centerPoint.getX());
		p2.setY(this.centerPoint.getY());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));

		// A has two corners in B
		p1.setX(this.centerPoint.getX());
		p1.setY(this.lowerPoint.getY() - 100.f);
		p2.setX(this.upperPoint.getX() + 100);
		p2.setY(this.upperPoint.getY() + 100.f);
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));

		// A cross B
		p1.setX((this.lowerPoint.getX()+this.centerPoint.getX())/2.f);
		p1.setY(this.lowerPoint.getY() - 100.f);
		p2.setX((this.upperPoint.getX()+this.centerPoint.getX())/2.f);
		p2.setY(this.upperPoint.getY() + 100.f);
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));

		// A on a side of B
		p1.setX(this.upperPoint.getX());
		p1.setY(this.lowerPoint.getY());
		p2.setX(this.upperPoint.getX() + 100.f);
		p2.setY(this.upperPoint.getY());
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p1,p2));
	}

	@Override
	public void testIntersectsPP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p1 = new Point2f();
		Point2f p2 = new Point2f();
		
		// A enclosing B
		p1.set(this.centerPoint);
		p1.add(this.lowerPoint);
		p1.scale(.5f);
		p2.set(this.centerPoint);
		p2.add(this.upperPoint);
		p2.scale(.5f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		// A inside B
		p1.set(this.lowerPoint);
		p1.setX(p1.getX() - 100.f);
		p1.setY(p1.getY() - 100.f);
		p2.set(this.upperPoint);
		p2.setX(p2.getX() + 100.f);
		p2.setY(p2.getY() + 100.f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		// A OUTSIDE B
		p1.setX(this.upperPoint.getX() + 100.f);
		p1.setY(this.lowerPoint.getY());
		p2.setX(p1.getX() + 100.f);
		p2.setY(this.upperPoint.getY() + 100.f);
		assertFalse(this.testingBounds.intersects(p1,p2));

		// A has a corner in B
		p1.setX(this.lowerPoint.getX() - 100.f);
		p1.setY(this.lowerPoint.getY() - 100.f);
		p2.setX(this.centerPoint.getX());
		p2.setY(this.centerPoint.getY());
		assertTrue(this.testingBounds.intersects(p1,p2));

		// A has two corners in B
		p1.setX(this.centerPoint.getX());
		p1.setY(this.lowerPoint.getY() - 100.f);
		p2.setX(this.upperPoint.getX() + 100);
		p2.setY(this.upperPoint.getY() + 100.f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		// A cross B
		p1.setX((this.lowerPoint.getX()+this.centerPoint.getX())/2.f);
		p1.setY(this.lowerPoint.getY() - 100.f);
		p2.setX((this.upperPoint.getX()+this.centerPoint.getX())/2.f);
		p2.setY(this.upperPoint.getY() + 100.f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		// A on a side of B
		p1.setX(this.upperPoint.getX());
		p1.setY(this.lowerPoint.getY());
		p2.setX(this.upperPoint.getX() + 100.f);
		p2.setY(this.upperPoint.getY());
		assertFalse(this.testingBounds.intersects(p1,p2));
	}

	@Override
	public void testClassifiesPoint2fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point2f c = new Point2f();
		float r;
		
		float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
		float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
		
		// A is enclosing B
		c.setX(this.centerPoint.getX());
		c.setY(this.centerPoint.getY());
		r = minSize / 4.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(c, r));

		// A is inside B
		c.setX(this.centerPoint.getX());
		c.setY(this.centerPoint.getY());
		r = 2.f*(float) Math.sqrt(maxSize * maxSize + maxSize * maxSize + maxSize * maxSize);
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(c, r));

		// A is outside B
		c.setX(this.lowerPoint.getX() - 100.f);
		c.setY(this.lowerPoint.getY() - 100.f);
		r = 50.f;
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(c, r));

		// A has one corner in B
		c.setX(this.lowerPoint.getX() - 100.f);
		c.setY(this.lowerPoint.getY() - 100.f);
		r = 100.f + minSize / 2.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(c, r));

		// A has two corners in B and is intersecting with two opposite sides.
		c.setX(this.centerPoint.getX());
		c.setY(this.lowerPoint.getY() - 100.f);
		r = 100.f + minSize / 2.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(c, r));

		// A has three corners in B.
		c.setX(this.lowerPoint.getX());
		c.setY(this.lowerPoint.getY());
		r = maxSize;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(c, r));

		// A and B touch themelves.
		c.setX(this.centerPoint.getX());
		c.setY(this.lowerPoint.getY() - 100.f);
		r = 100.f;
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(c, r));
	}

	@Override
	public void testIntersectsPoint2fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point2f c = new Point2f();
		float r;
		
		float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
		float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
		
		// A is enclosing B
		c.setX(this.centerPoint.getX());
		c.setY(this.centerPoint.getY());
		r = minSize / 2.f;
		assertTrue(this.testingBounds.intersects(c, r));

		// A is inside B
		c.setX(this.centerPoint.getX());
		c.setY(this.centerPoint.getY());
		r = 2.f*(float) Math.sqrt(maxSize * maxSize + maxSize * maxSize + maxSize * maxSize);
		assertTrue(this.testingBounds.intersects(c, r));

		// A is outside B
		c.setX(this.lowerPoint.getX() - 100.f);
		c.setY(this.lowerPoint.getY() - 100.f);
		r = 50.f;
		assertFalse(this.testingBounds.intersects(c, r));

		// A has one corner in B
		c.setX(this.lowerPoint.getX() - 100.f);
		c.setY(this.lowerPoint.getY() - 100.f);
		r = 100.f + minSize / 2.f;
		assertTrue(this.testingBounds.intersects(c, r));

		// A has two corners in B and is intersecting with two opposite sides.
		c.setX(this.centerPoint.getX());
		c.setY(this.lowerPoint.getY() - 100.f);
		r = 100.f + minSize / 2.f;
		assertTrue(this.testingBounds.intersects(c, r));

		// A has three corners in B.
		c.setX(this.lowerPoint.getX());
		c.setY(this.lowerPoint.getY());
		r = maxSize;
		assertTrue(this.testingBounds.intersects(c, r));
	}

	@Override
	public void testClassifiesPoint2fVector2fArrayFloatArray() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f center = new Point2f();
		Vector2f[] axis = new Vector2f[] {
				new Vector2f(-1.f,1.f),
				new Vector2f(-1.f,-1.f)
		};
		for(int i=0; i<2; ++i) axis[i].normalize();
		float[] extent = new float[2];
		
		float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
		float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
		
		// A inside B
		center.setX(this.centerPoint.getX());
		center.setY(this.centerPoint.getY());
		extent[0] = maxSize * 10.f;
		extent[1] = maxSize * 10.f;
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(center, axis, extent));

		// A is enclosing B
		center.setX(this.centerPoint.getX());
		center.setY(this.centerPoint.getY());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(center, axis, extent));

		// A and B do not intersect
		center.setX(this.lowerPoint.getX() - 1000.f);
		center.setY(this.centerPoint.getY());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(center, axis, extent));

		// A has one corner in B
		center.setX(this.lowerPoint.getX() - 1.f);
		center.setY(this.centerPoint.getY());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, axis, extent));

		// A has three corners in B
		center.setX(this.lowerPoint.getX());
		center.setY(this.lowerPoint.getY());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, axis, extent));
	}

	@Override
	public void testIntersectsPoint2fVector2fArrayFloatArray() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f center = new Point2f();
		Vector2f[] axis = new Vector2f[] {
				new Vector2f(-1.f,1.f),
				new Vector2f(-1.f,-1.f)
		};
		for(int i=0; i<2; ++i) axis[i].normalize();
		float[] extent = new float[2];
		
		float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
		float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
		
		// A inside B
		center.setX(this.centerPoint.getX());
		center.setY(this.centerPoint.getY());
		extent[0] = maxSize * 10.f;
		extent[1] = maxSize * 10.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		// A is enclosing B
		center.setX(this.centerPoint.getX());
		center.setY(this.centerPoint.getY());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		// A and B do not intersect
		center.setX(this.lowerPoint.getX() - 1000.f);
		center.setY(this.centerPoint.getY());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		assertFalse(this.testingBounds.intersects(center, axis, extent));

		// A has one corner in B
		center.setX(this.lowerPoint.getX() - 1.f);
		center.setY(this.centerPoint.getY());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		// A has three corners in B
		center.setX(this.lowerPoint.getX());
		center.setY(this.lowerPoint.getY());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));
	}

	@Override
	public void testClassifiesIC() {
		assertTrue(this.testingBounds.isInit());
		
		// MinimumBoundingRectangle
		{
			Point2f p1 = new Point2f();
			Point2f p2 = new Point2f();
			// A enclosing B
			p1.set(this.centerPoint);
			p1.add(this.lowerPoint);
			p1.scale(.5f);
			p2.set(this.centerPoint);
			p2.add(this.upperPoint);
			p2.scale(.5f);
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new MinimumBoundingRectangle(p1,p2)));
	
			// A OUTSIDE B
			p1.setX(this.upperPoint.getX() + 100.f);
			p1.setY(this.lowerPoint.getY());
			p2.setX(p1.getX() + 100.f);
			p2.setY(this.upperPoint.getY() + 100.f);
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new MinimumBoundingRectangle(p1,p2)));
		}
		
		// BoundingCircle
		{
			Point2f c = new Point2f();
			float r;
			
			float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
			
			// A is enclosing B
			c.setX(this.centerPoint.getX());
			c.setY(this.centerPoint.getY());
			r = minSize / 4.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingCircle(c,r)));

			// A is outside B
			c.setX(this.lowerPoint.getX() - 100.f);
			c.setY(this.lowerPoint.getY() - 100.f);
			r = 50.f;
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new BoundingCircle(c,r)));
		}
		
		// OrientedBoundingRectangle
		{
			assertTrue(this.testingBounds.isInit());
			
			Point2f center = new Point2f();
			Vector2f[] axis = new Vector2f[] {
					new Vector2f(-1.f,1.f),
					new Vector2f(-1.f,-1.f)
			};
			for(int i=0; i<2; ++i) axis[i].normalize();
			float[] extent = new float[2];
			
			float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
			float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
			
			// A inside B
			center.setX(this.centerPoint.getX());
			center.setY(this.centerPoint.getY());
			extent[0] = maxSize * 10.f;
			extent[1] = maxSize * 10.f;
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new OrientedBoundingRectangle(center, axis, extent)));

			// A and B do not intersect
			center.setX(this.lowerPoint.getX() - 1000.f);
			center.setY(this.centerPoint.getY());
			extent[0] = minSize / 10.f;
			extent[1] = minSize / 10.f;
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new OrientedBoundingRectangle(center, axis, extent)));
		}
	}

	@Override
	public void testIntersectsIC() {
		assertTrue(this.testingBounds.isInit());
		
		// MinimumBoundingRectangle
		{
			Point2f p1 = new Point2f();
			Point2f p2 = new Point2f();
			// A enclosing B
			p1.set(this.centerPoint);
			p1.add(this.lowerPoint);
			p1.scale(.5f);
			p2.set(this.centerPoint);
			p2.add(this.upperPoint);
			p2.scale(.5f);
			assertTrue(this.testingBounds.intersects(new MinimumBoundingRectangle(p1,p2)));
	
			// A OUTSIDE B
			p1.setX(this.upperPoint.getX() + 100.f);
			p1.setY(this.lowerPoint.getY());
			p2.setX(p1.getX() + 100.f);
			p2.setY(this.upperPoint.getY() + 100.f);
			assertFalse(this.testingBounds.intersects(new MinimumBoundingRectangle(p1,p2)));
		}
		
		// BoundingCircle
		{
			Point2f c = new Point2f();
			float r;
			
			float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
			
			// A is enclosing B
			c.setX(this.centerPoint.getX());
			c.setY(this.centerPoint.getY());
			r = minSize / 4.f;
			assertTrue(this.testingBounds.intersects(new BoundingCircle(c,r)));

			// A is outside B
			c.setX(this.lowerPoint.getX() - 100.f);
			c.setY(this.lowerPoint.getY() - 100.f);
			r = 50.f;
			assertFalse(this.testingBounds.intersects(new BoundingCircle(c,r)));
		}
		
		// OrientedBoundingRectangle
		{
			assertTrue(this.testingBounds.isInit());
			
			Point2f center = new Point2f();
			Vector2f[] axis = new Vector2f[] {
					new Vector2f(-1.f,1.f),
					new Vector2f(-1.f,-1.f)
			};
			for(int i=0; i<2; ++i) axis[i].normalize();
			float[] extent = new float[2];
			
			float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
			float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY());
			
			// A inside B
			center.setX(this.centerPoint.getX());
			center.setY(this.centerPoint.getY());
			extent[0] = maxSize * 10.f;
			extent[1] = maxSize * 10.f;
			assertTrue(this.testingBounds.intersects(new OrientedBoundingRectangle(center, axis, extent)));

			// A and B do not intersect
			center.setX(this.lowerPoint.getX() - 1000.f);
			center.setY(this.centerPoint.getY());
			extent[0] = minSize / 10.f;
			extent[1] = minSize / 10.f;
			assertFalse(this.testingBounds.intersects(new OrientedBoundingRectangle(center, axis, extent)));
		}
	}

	/**
	 */
	@Override
	public void testToBounds3D() {
		assertTrue(this.testingBounds.isInit());
		
		AlignedBoundingBox aabb = this.testingBounds.toBounds3D();
		
		Point3f p1 = new Point3f(this.lowerPoint.getX(), this.lowerPoint.getY(), 0.f);
		Point3f p2 = new Point3f(this.upperPoint.getX(), this.upperPoint.getY(), 0.f);
		
		assertNotNull(aabb);
		assertEpsilonEquals(p1, aabb.getLower());
		assertEpsilonEquals(p2, aabb.getUpper());
	}
	
	/**
	 */
	@Override
	public void testToBounds3DCoordinateSystem3D() {
		assertTrue(this.testingBounds.isInit());

		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			AlignedBoundingBox aabb = this.testingBounds.toBounds3D(cs);
			
			Point3f p1 = cs.fromCoordinateSystem2D(this.lowerPoint);
			Point3f p2 = cs.fromCoordinateSystem2D(this.upperPoint);
			
			assertNotNull(aabb);
			assertEpsilonEquals(p1, aabb.getLower());
			assertEpsilonEquals(p2, aabb.getUpper());
		}
	}

	/**
	 */
	@Override
	public void testToBounds3DFloatFloat() {
		assertTrue(this.testingBounds.isInit());
		
		AlignedBoundingBox aabb = this.testingBounds.toBounds3D(33445.f, 3323.f);
		
		Point3f p1 = new Point3f(this.lowerPoint.getX(), this.lowerPoint.getY(), 33445.f);
		Point3f p2 = new Point3f(this.upperPoint.getX(), this.upperPoint.getY(), 33445.+3323.f);
		
		assertNotNull(aabb);
		assertEpsilonEquals(p1, aabb.getLower());
		assertEpsilonEquals(p2, aabb.getUpper());

		aabb = this.testingBounds.toBounds3D(33445.f, -3323.f);
		
		p1 = new Point3f(this.lowerPoint.getX(), this.lowerPoint.getY(), 33445.-3323.f);
		p2 = new Point3f(this.upperPoint.getX(), this.upperPoint.getY(), 33445.f);
		
		assertNotNull(aabb);
		assertEpsilonEquals(p1, aabb.getLower());
		assertEpsilonEquals(p2, aabb.getUpper());
	}
	
	/**
	 */
	@Override
	public void testToBounds3DFloatFloatCoordinateSystem3D() {
		assertTrue(this.testingBounds.isInit());

		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			AlignedBoundingBox aabb = this.testingBounds.toBounds3D(33445.f, 3323.f, cs);
			
			Point3f p1 = cs.fromCoordinateSystem2D(this.lowerPoint, 33445.f);
			Point3f p2 = cs.fromCoordinateSystem2D(this.upperPoint, 33445.f+3323.f);
			
			assertNotNull(aabb);
			assertEpsilonEquals(p1, aabb.getLower());
			assertEpsilonEquals(p2, aabb.getUpper());

			aabb = this.testingBounds.toBounds3D(33445.f, -3323.f, cs);
			
			p1 = cs.fromCoordinateSystem2D(this.lowerPoint, 33445.f-3323.f);
			p2 = cs.fromCoordinateSystem2D(this.upperPoint, 33445.f);
			
			assertNotNull(aabb);
			assertEpsilonEquals(p1, aabb.getLower());
			assertEpsilonEquals(p2, aabb.getUpper());
		}
	}

	@Override
	public void testFarestPointP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p, near;
		Point2f expected = new Point2f();
		
		p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(100,100));
		expected.set(this.upperPoint);
		near = this.testingBounds.farestPoint(p);
		assertEpsilonEquals(expected, near);
		
		p = new Point2f(this.upperPoint);
		expected.set(this.lowerPoint);
		near = this.testingBounds.farestPoint(p);
		assertEpsilonEquals(expected, near);

		p = new Point2f(this.upperPoint.getX(), this.lowerPoint.getY());
		expected.set(this.lowerPoint.getX(), this.upperPoint.getY());
		near = this.testingBounds.farestPoint(p);
		assertEpsilonEquals(expected, near);

		p = new Point2f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f);
		expected.set(this.upperPoint.getX(), this.lowerPoint.getY());
		near = this.testingBounds.farestPoint(p);
		assertEpsilonEquals(expected, near);
	}

	@Override
	public void testNearestPointP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p;
		Point2f near;
		Point2f expected = new Point2f();
		
		p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(100,100));
		expected.set(this.lowerPoint);
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(expected, near);
		
		p = new Point2f(this.upperPoint);
		expected.set(this.upperPoint);
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(expected, near);

		p = new Point2f(this.upperPoint.getX(), this.lowerPoint.getY());
		expected.set(p);
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(expected, near);

		p = new Point2f();
		p.add(this.upperPoint, this.lowerPoint);
		p.scale(.5f);
		expected.set(p);
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(expected, near);

		p = new Point2f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f);
		expected.set(this.lowerPoint.getX(), (this.lowerPoint.getY()+this.upperPoint.getY())/2.f);
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(expected, near);
	}

}

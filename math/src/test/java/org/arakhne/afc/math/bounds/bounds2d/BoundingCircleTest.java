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
import java.util.List;
import java.util.Set;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.bounds.bounds2f.BoundingCircle;
import org.arakhne.afc.math.bounds.bounds2f.CombinableBounds2D;
import org.arakhne.afc.math.bounds.bounds2f.MinimumBoundingRectangle;
import org.arakhne.afc.math.bounds.bounds2f.OrientedBoundingRectangle;
import org.arakhne.afc.math.bounds.bounds3f.BoundingSphere;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.intersection.IntersectionType;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.sizediterator.SizedIterator;


/**
 * Unit test for BoundingCircle class.
 * 
 * @author $Author: sgalland$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BoundingCircleTest extends AbstractOrientedBound2DTest {

	/** Reference instance.
	 */
	private BoundingCircle testingBounds;
	
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
	
	/** Radius.
	 */
	private float referenceRadius;

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
		this.centerPoint = new Point2f();
		this.referenceRadius = computeLowerUpperCenter(this.testingPoints,this.lowerPoint,this.upperPoint,this.centerPoint);
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
		this.centerPoint = null;
		super.tearDown();
	}

	/**
	 * @param points
	 * @return a bounds
	 */
	protected BoundingCircle createBounds(Tuple2f[] points) {
		BoundingCircle b = new BoundingCircle();
		b.set(points);
		return b;
	}

	/** Compute the lower, upper and center points.
	 * 
	 * @param points
	 * @param lower
	 * @param upper
	 * @param center
	 * @return a value
	 */
	private static float computeLowerUpperCenter(Tuple2f[] points, Tuple2f lower, Tuple2f upper, Tuple2f center) {
		float minx, miny;
		float maxx, maxy;
		
		minx = miny = Float.POSITIVE_INFINITY;
		maxx = maxy = Float.NEGATIVE_INFINITY;
		
		for(Tuple2f point : points) {
			if (point!=null) {
				if (point.getX()<minx) minx = point.getX();
				if (point.getY()<miny) miny = point.getY();
				if (point.getX()>maxx) maxx = point.getX();
				if (point.getY()>maxy) maxy = point.getY();
			}
		}
		
		Tuple2f c = center==null ? new Point2f() : center;
		c.set(
				(minx+maxx) / 2.f,
				(miny+maxy) / 2.f);
		
		float radius = 0;
		float distance;
		for(Tuple2f point : points) {
			distance = MathUtil.distance(c.getX(),c.getY(),point.getX(),point.getY());
			if(distance>radius)
				radius = distance;
		}
		
		if (lower!=null) lower.set(c.getX()-radius,c.getY()-radius);
		if (upper!=null) upper.set(c.getX()+radius,c.getY()+radius);

		return radius;
	}

	@Override
	public void testGetMathematicalDimension() {
		assertTrue(this.testingBounds.isInit());
		
		assertEquals(PseudoHamelDimension.DIMENSION_2D, this.testingBounds.getMathematicalDimension());
	}

	@Override
	public void testGetCenterX() {
		assertTrue(this.testingBounds.isInit());
		
		assertEpsilonEquals(this.centerPoint.getX(), this.testingBounds.getCenterX());
	}

	@Override
	public void testGetCenterY() {
		assertTrue(this.testingBounds.isInit());
		
		assertEpsilonEquals(this.centerPoint.getY(), this.testingBounds.getCenterY());
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
		
		Point2f p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(10,10));
		assertEpsilonEquals(
				p.distance(this.centerPoint)+this.referenceRadius, 
				this.testingBounds.distanceMax(p));

		p = new Point2f(this.lowerPoint);
		assertEpsilonEquals(
				this.lowerPoint.distance(this.centerPoint)+this.referenceRadius, 
				this.testingBounds.distanceMax(p));

		p = new Point2f(this.centerPoint);
		assertEpsilonEquals(
				0.f, 
				this.testingBounds.distance(p));

		p = new Point2f(this.upperPoint);
		p.sub(new Vector2f(100,0));
		assertEpsilonEquals(
				p.distance(this.centerPoint)+this.referenceRadius, 
				this.testingBounds.distanceMax(p));

		p = new Point2f(this.upperPoint);
		assertEpsilonEquals(
				this.upperPoint.distance(this.centerPoint)+this.referenceRadius, 
				this.testingBounds.distanceMax(p));
	}

	@Override
	public void testDistanceMaxSquaredP() {
		assertTrue(this.testingBounds.isInit());
		
		float sqExpected;
		
		Point2f p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(10,10));
		sqExpected = p.distance(this.centerPoint)+this.referenceRadius;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceMaxSquared(p));

		p = new Point2f(this.lowerPoint);
		sqExpected = this.lowerPoint.distance(this.centerPoint)+this.referenceRadius;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceMaxSquared(p));

		p = new Point2f(this.centerPoint);
		sqExpected = this.referenceRadius;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceMaxSquared(p));

		p = new Point2f(this.upperPoint);
		p.sub(new Vector2f(100,0));
		sqExpected = p.distance(this.centerPoint)+this.referenceRadius;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceMaxSquared(p));

		p = new Point2f(this.upperPoint);
		sqExpected = this.upperPoint.distance(this.centerPoint)+this.referenceRadius;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceMaxSquared(p));
	}

	@Override
	public void testDistanceP() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(10,10));
		assertEpsilonEquals(
				p.distance(this.centerPoint)-this.referenceRadius, 
				this.testingBounds.distance(p));

		p = new Point2f(this.lowerPoint);
		assertEpsilonEquals(
				this.lowerPoint.distance(this.centerPoint)-this.referenceRadius, 
				this.testingBounds.distance(p));

		p = new Point2f(this.centerPoint);
		assertEpsilonEquals(
				0.f, 
				this.testingBounds.distance(p));

		p = new Point2f(this.upperPoint);
		p.sub(new Vector2f(100,0));
		assertEpsilonEquals(
				p.distance(this.centerPoint)-this.referenceRadius, 
				this.testingBounds.distance(p));

		p = new Point2f(this.upperPoint);
		assertEpsilonEquals(
				this.upperPoint.distance(this.centerPoint)-this.referenceRadius, 
				this.testingBounds.distance(p));
	}

	@Override
	public void testDistanceSquaredP() {
		assertTrue(this.testingBounds.isInit());
		
		float sqExpected;
		
		Point2f p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(10,10));
		sqExpected = p.distance(this.centerPoint)-this.referenceRadius;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceSquared(p));

		p = new Point2f(this.lowerPoint);
		sqExpected = this.lowerPoint.distance(this.centerPoint)-this.referenceRadius;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceSquared(p));

		p = new Point2f(this.centerPoint);
		sqExpected = 0.f;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceSquared(p));

		p = new Point2f(this.upperPoint);
		p.sub(new Vector2f(100,0));
		sqExpected = p.distance(this.centerPoint)-this.referenceRadius;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceSquared(p));

		p = new Point2f(this.upperPoint);
		sqExpected = this.upperPoint.distance(this.centerPoint)-this.referenceRadius;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceSquared(p));
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
		
		Point2f l = this.testingBounds.getLower();
		assertEpsilonEquals(this.lowerPoint.getX(), l.getX());
		assertEpsilonEquals(this.lowerPoint.getY(), l.getY());
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
		
		Point2f u = this.testingBounds.getUpper();
		assertEpsilonEquals(this.upperPoint.getX(), u.getX());
		assertEpsilonEquals(this.upperPoint.getY(), u.getY());
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
		
		BoundingCircle clone = this.testingBounds.clone();
		assertNotSame(this.testingBounds, clone);
		assertNotSame(this.testingBounds.getCenter(), clone.getCenter());
		assertEquals(this.testingBounds.getRadius(), clone.getRadius());
	}

	@Override
	public void testTranslateVector2f() {
		assertTrue(this.testingBounds.isInit());
		
		Vector2f v = randomVector2D();
		this.testingBounds.translate(v);
		assertEpsilonEquals(this.centerPoint.getX()+v.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(this.centerPoint.getY()+v.getY(), this.testingBounds.getCenter().getY());
		assertEquals(this.referenceRadius, this.testingBounds.getRadius());
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
			assertTrue(i+"; point: "+p.toString()+"; candidates: "+expectedPoints.toString(), expectedPoints.remove(p)); //$NON-NLS-1$ //$NON-NLS-2$
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
		float size = this.referenceRadius;
		
		expectedPoints.add(new Vector2f(size, size));
		expectedPoints.add(new Vector2f(size, -size));
		expectedPoints.add(new Vector2f(-size, size));
		expectedPoints.add(new Vector2f(-size, -size));
		
		SizedIterator<Vector2f> iterator = this.testingBounds.getLocalOrientedBoundVertices();
		assertNotNull(iterator);
		assertEquals(4, iterator.totalSize());
		
		for(int i=0; i<4; ++i) {
			assertTrue(iterator.hasNext());
			v = iterator.next();
			assertNotNull(v);
			assertEquals(i, iterator.index());
			assertTrue(i+"; point: "+v.toString()+"; candidates: "+expectedPoints.toString(), expectedPoints.remove(v)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		assertFalse(iterator.hasNext());
		assertTrue(expectedPoints.isEmpty());
	}

	@Override
	public void testGetLocalVertexAt() {
		assertTrue(this.testingBounds.isInit());
		
		List<Vector2f> expectedPoints = new ArrayList<Vector2f>();
		Vector2f v;
		float size = this.referenceRadius;
		
		expectedPoints.add(new Vector2f(size, size));
		expectedPoints.add(new Vector2f(-size, size));
		expectedPoints.add(new Vector2f(-size, -size));
		expectedPoints.add(new Vector2f(size, -size));
		
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
		Vector2f extent = this.testingBounds.getOrientedBoundExtentVector();
		assertEpsilonEquals(new Vector2f(this.referenceRadius,this.referenceRadius), extent);
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
		float extent = this.testingBounds.getRExtent();
		assertEpsilonEquals(this.referenceRadius, extent);
	}

	/**
	 */
	@Override
	public void testGetSExtent() {
		float extent = this.testingBounds.getSExtent();
		assertEpsilonEquals(this.referenceRadius, extent);
	}

	@Override
	public void testCombineTuple2fArray() {
		assertTrue(this.testingBounds.isInit());
		
		this.testingBounds.combine(
				new Point2f(this.centerPoint),
				new Point2f(this.testingPoints[0]));

		assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
		assertEpsilonEquals(this.centerPoint, this.testingBounds.getCenter());

		Point2f p = new Point2f(this.centerPoint);
		p.add(new Point2f(this.referenceRadius*4.f, 0));
		this.testingBounds.combine(p);

		assertEpsilonEquals(2.5f*this.referenceRadius, this.testingBounds.getRadius());
		assertEpsilonEquals(new Point2f(
				this.centerPoint.getX() + 1.5*this.referenceRadius,
				this.centerPoint.getY()), this.testingBounds.getCenter());
	}

	@Override
	public void testSetTuple2fArray() {
		assertTrue(this.testingBounds.isInit());
		
		Tuple2f[] points = randomTuples2D();
		Point2f l = new Point2f();
		Point2f u = new Point2f();
		Point2f c = new Point2f();
		computeLowerUpperCenter(points, l, u, c);

		this.testingBounds.set(points);

		assertEpsilonEquals(c.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(c.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(
				MathUtil.max(
						(u.getX()-l.getX())/2.f,
						(u.getY()-l.getY())/2.f),
				this.testingBounds.getRadius());
	}

	@Override
	public void testCombineCB() {
		assertTrue(this.testingBounds.isInit());
		Point2f p;
		Point2f newCenter;
		float newRadius;		
		
		BoundingCircle sphere = new BoundingCircle();
		p = randomPoint2D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);

		{
			Vector2f v = new Vector2f();
			v.sub(this.centerPoint, p);
			newCenter = new Point2f();
			float newDiameter = MathUtil.max(2.f*r, 2.f*this.referenceRadius, v.length()+r+this.referenceRadius);
			newRadius = newDiameter / 2.f;
			if (newDiameter==2.*r) newCenter.set(p);
			else if (newDiameter==2.*this.referenceRadius) newCenter.set(this.centerPoint);
			else {
				v.scale(this.referenceRadius / (this.referenceRadius+r));
				newCenter.add(p,v);
			}
		}
		
		this.testingBounds.combine(sphere);

		assertEpsilonEquals(newCenter.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(newCenter.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(newRadius, this.testingBounds.getRadius());

		MinimumBoundingRectangle box = new MinimumBoundingRectangle();
		box.set(randomPoints2D());
		
		{
			Point2f bCenter = box.getCenter();
			float dmax = box.distanceMax(this.testingBounds.getCenter());
			Vector2f v = new Vector2f();
			v.sub(this.testingBounds.getCenter(), bCenter);
			float bRadius = dmax - v.length();
			newCenter = new Point2f();
			float newDiameter = MathUtil.max(2.f*bRadius, 2.f*this.testingBounds.getRadius(), v.length()+bRadius+this.testingBounds.getRadius());
			newRadius = newDiameter / 2.f;
			if (newDiameter==2.*bRadius) newCenter.set(bCenter);
			else if (newDiameter==2.*this.testingBounds.getRadius()) newCenter.set(this.testingBounds.getCenter());
			else {
				v.scale(this.testingBounds.getRadius() / (this.testingBounds.getRadius()+bRadius));
				newCenter.add(bCenter,v);
			}
		}

		this.testingBounds.combine(box);

		assertEpsilonEquals(newCenter.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(newCenter.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(newRadius, this.testingBounds.getRadius());
	}

	@Override
	public void testCombineCollection() {
		assertTrue(this.testingBounds.isInit());
		
		Collection<CombinableBounds2D> bounds = new ArrayList<CombinableBounds2D>();
		Point2f p;
		Point2f sphereCenter = new Point2f(this.centerPoint);
		float sphereRadius = this.referenceRadius;		
		
		BoundingCircle sphere = new BoundingCircle();
		p = randomPoint2D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);

		MinimumBoundingRectangle box = new MinimumBoundingRectangle();
		box.set(randomPoints2D());
		
		bounds.add(sphere);
		bounds.add(box);

		
		{
			Vector2f v = new Vector2f();
			v.sub(sphereCenter, p);
			float newDiameter = MathUtil.max(2.f*r, 2.f*sphereRadius, v.length()+r+sphereRadius);
			if (newDiameter==2.*r) sphereCenter.set(p);
			else if (newDiameter==2.*sphereRadius) sphereCenter.set(this.centerPoint);
			else {
				v.scale(sphereRadius / (sphereRadius+r));
				sphereCenter.add(p,v);
			}
			sphereRadius = newDiameter / 2.f;
		}
		{
			Point2f bCenter = box.getCenter();
			float dmax = box.distanceMax(sphereCenter);
			Vector2f v = new Vector2f();
			v.sub(sphereCenter, bCenter);
			float bRadius = dmax - v.length();
			float newDiameter = MathUtil.max(2.f*bRadius, 2.f*sphereRadius, v.length()+bRadius+sphereRadius);
			if (newDiameter==2.*bRadius) sphereCenter.set(bCenter);
			else if (newDiameter==2.*sphereRadius) sphereCenter.set(sphereCenter);
			else {
				v.scale(sphereRadius / (sphereRadius+bRadius));
				sphereCenter.add(bCenter,v);
			}
			sphereRadius = newDiameter / 2.f;
		}
		
		this.testingBounds.combine(bounds);

		assertEpsilonEquals(sphereCenter.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(sphereCenter.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(sphereRadius, this.testingBounds.getRadius());
	}

	@Override
	public void testCombineP() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f p = randomPoint2D();

		float newRadius = p.distance(this.centerPoint);
		
		this.testingBounds.combine(p);

		if (newRadius<=this.referenceRadius) {
			assertEpsilonEquals(this.centerPoint.getX(), this.testingBounds.getCenter().getX());
			assertEpsilonEquals(this.centerPoint.getY(), this.testingBounds.getCenter().getY());
			assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
		}
		else {
			newRadius = (newRadius + this.referenceRadius) / 2.f;
			
			Vector2f v = new Vector2f(this.centerPoint);
			v.sub(p);
			v.normalize();
			v.scale(newRadius);
			Point2f newCenter = new Point2f(p);
			newCenter.add(v);
			
			assertEpsilonEquals(newCenter.getX(), this.testingBounds.getCenter().getX());
			assertEpsilonEquals(newCenter.getY(), this.testingBounds.getCenter().getY());
			assertEpsilonEquals(newRadius, this.testingBounds.getRadius());			
		}
	}

	@Override
	public void testSetCB() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f p;
		
		BoundingCircle sphere = new BoundingCircle();
		p = randomPoint2D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);
		this.testingBounds.set(sphere);
		assertEpsilonEquals(p.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(r, this.testingBounds.getRadius());

		MinimumBoundingRectangle box = new MinimumBoundingRectangle();
		p = randomPoint2D();
		box.set(p);
		this.testingBounds.set(box);
		assertEpsilonEquals(box.getCenterX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(box.getCenterY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(box.getSizeX()/2.f, this.testingBounds.getRadius());
	}

	@Override
	public void testSetP() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f p = randomPoint2D();
		this.testingBounds.set(p);
		assertEpsilonEquals(p.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(0.f, this.testingBounds.getRadius());
	}

	/** 
	 */
	public void testGetSize() {
		assertTrue(this.testingBounds.isInit());
		
		Vector2f size = this.testingBounds.getSize();
		assertEpsilonEquals(this.referenceRadius*2.f, size.getX());
		assertEpsilonEquals(this.referenceRadius*2.f, size.getY());
	}

	/** 
	 */
	public void testGetSizeX() {
		assertTrue(this.testingBounds.isInit());
		
		float size = this.testingBounds.getSizeX();
		assertEpsilonEquals(this.referenceRadius*2.f, size);
	}

	/** 
	 */
	public void testGetSizeY() {
		assertTrue(this.testingBounds.isInit());
		
		float size = this.testingBounds.getSizeY();
		assertEpsilonEquals(this.referenceRadius*2.f, size);
	}

	//-------------------------------------------
	// BoundingCircle dedicated tests
	//-------------------------------------------

	/** 
	 */
	public void testGetRadius() {
		assertTrue(this.testingBounds.isInit());
		
		assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
	}

	/**
	 */
	public void testGetCenterPoint2f() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f c = new Point2f();
		this.testingBounds.getCenter(c);
		assertEpsilonEquals((this.lowerPoint.getX()+this.upperPoint.getX())/2.f, c.getX());
		assertEpsilonEquals((this.lowerPoint.getY()+this.upperPoint.getY())/2.f, c.getY());
	}

	/** 
	 */
	public void testSetRadius() {
		assertTrue(this.testingBounds.isInit());
		
		this.testingBounds.setRadius(-100);
		assertEpsilonEquals(this.centerPoint.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(this.centerPoint.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(0, this.testingBounds.getRadius());
		
		float r = this.RANDOM.nextFloat() * 100.f;
		this.testingBounds.setRadius(r);
		assertEpsilonEquals(this.centerPoint.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(this.centerPoint.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(r, this.testingBounds.getRadius());
	}

	/** 
	 */
	public void testSetTuple2fFloat() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f p = randomPoint2D();
		this.testingBounds.set(p, -100);
		assertEpsilonEquals(p.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(0, this.testingBounds.getRadius());
		
		p = randomPoint2D();
		float r = this.RANDOM.nextFloat() * 100.f;
		this.testingBounds.set(p, r);
		assertEpsilonEquals(p.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(r, this.testingBounds.getRadius());
	}

	/**
	 */
	public void testCombineFloatFloatFloat() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f p = randomPoint2D();

		float newRadius = p.distance(this.centerPoint);
		
		this.testingBounds.combine(p.getX(),p.getY());
		

		if (newRadius<=this.testingBounds.getRadius()) {
			assertEpsilonEquals(this.centerPoint.getX(), this.testingBounds.getCenter().getX());
			assertEpsilonEquals(this.centerPoint.getY(), this.testingBounds.getCenter().getY());
			assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
		}
		else {
			newRadius += this.referenceRadius;
			newRadius /= 2;
			
			Vector2f v = new Vector2f(this.centerPoint);
			v.sub(p);
			v.normalize();
			v.scale(newRadius);
			Point2f newCenter = new Point2f(p);
			newCenter.add(v);
			
			assertEpsilonEquals(newCenter.getX(), this.testingBounds.getCenter().getX());
			assertEpsilonEquals(newCenter.getY(), this.testingBounds.getCenter().getY());
			assertEpsilonEquals(newRadius, this.testingBounds.getRadius());
		}
	}
	
	//-------------------------------------------
	// IntersectionClassifier
	//-------------------------------------------

	@Override
	public void testClassifiesP() {
		assertTrue(this.testingBounds.isInit());

		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(this.centerPoint));

		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(this.lowerPoint));
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(this.upperPoint));
		
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
				new Point2f(this.upperPoint.getX() + 10.f, this.centerPoint.getY())));
	}

	@Override
	public void testIntersectsP() {
		assertTrue(this.testingBounds.isInit());

		assertTrue(this.testingBounds.intersects(this.centerPoint));

		assertFalse(this.testingBounds.intersects(this.lowerPoint));
		assertFalse(this.testingBounds.intersects(this.upperPoint));
		
		assertFalse(this.testingBounds.intersects(
				new Point2f(this.upperPoint.getX() + 10.f, this.centerPoint.getY())));
	}

	@Override
	public void testClassifiesPP() {
		assertTrue(this.testingBounds.isInit());

		Point2f lower = new Point2f();
		Point2f upper = new Point2f();
		
		// A is enclosing B
		lower.setX(this.centerPoint.getX()-1.f);
		lower.setY(this.centerPoint.getY()-1.f);
		upper.setX(this.centerPoint.getX()+1.f);
		upper.setY(this.centerPoint.getY()+1.f);
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(lower, upper));

		// A is inside B
		lower.setX(this.lowerPoint.getX()-10.f);
		lower.setY(this.lowerPoint.getY()-10.f);
		upper.setX(this.upperPoint.getX()+10.f);
		upper.setY(this.upperPoint.getY()+10.f);
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(lower, upper));

		// A is outside B
		lower.setX(this.lowerPoint.getX()-100.f);
		lower.setY(this.lowerPoint.getY());
		upper.setX(this.lowerPoint.getX()-10.f);
		upper.setY(this.upperPoint.getY());
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(lower, upper));

		// A has one corner in B
		lower.setX(this.lowerPoint.getX()-100.f);
		lower.setY(this.lowerPoint.getY()-10.f);
		upper.setX(this.centerPoint.getX());
		upper.setY(this.centerPoint.getY());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(lower, upper));
	}

	@Override
	public void testIntersectsPP() {
		assertTrue(this.testingBounds.isInit());

		Point2f lower = new Point2f();
		Point2f upper = new Point2f();
		
		// A is enclosing B
		lower.setX(this.centerPoint.getX()-1.f);
		lower.setY(this.centerPoint.getY()-1.f);
		upper.setX(this.centerPoint.getX()+1.f);
		upper.setY(this.centerPoint.getY()+1.f);
		assertTrue(this.testingBounds.intersects(lower, upper));

		// A is inside B
		lower.setX(this.lowerPoint.getX()-10.f);
		lower.setY(this.lowerPoint.getY()-10.f);
		upper.setX(this.upperPoint.getX()+10.f);
		upper.setY(this.upperPoint.getY()+10.f);
		assertTrue(this.testingBounds.intersects(lower, upper));

		// A is outside B
		lower.setX(this.lowerPoint.getX()-100.f);
		lower.setY(this.lowerPoint.getY());
		upper.setX(this.lowerPoint.getX()-10.f);
		upper.setY(this.upperPoint.getY());
		assertFalse(this.testingBounds.intersects(lower, upper));

		// A has one corner in B
		lower.setX(this.lowerPoint.getX()-100.f);
		lower.setY(this.lowerPoint.getY()-10.f);
		upper.setX(this.centerPoint.getX());
		upper.setY(this.centerPoint.getY());
		assertTrue(this.testingBounds.intersects(lower, upper));
	}

	@Override
	public void testClassifiesPoint2fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point2f center = new Point2f();
		float radius;
		
		// A is inside B
		center.setX(this.testingBounds.getCenter().getX());
		center.setY(this.testingBounds.getCenter().getY());
		radius = this.referenceRadius * 10.f;
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(center, radius));
		
		// A is enclosing B
		center.setX(this.testingBounds.getCenter().getX());
		center.setY(this.testingBounds.getCenter().getY());
		radius = this.referenceRadius / 10.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(center, radius));
		
		// A is enclosing B
		center.setX(this.testingBounds.getCenter().getX() - this.referenceRadius / 20.f);
		center.setY(this.testingBounds.getCenter().getY());
		radius = this.referenceRadius / 10.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(center, radius));

		// A and B do not overlap
		center.setX(this.lowerPoint.getX() - 100.f);
		center.setY(this.testingBounds.getCenter().getY());
		radius = 10.f;
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(center, radius));
		
		// A and B do overlap
		center.setX(this.lowerPoint.getX());
		center.setY(this.centerPoint.getY());
		radius = this.referenceRadius / 2.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, radius));
	}

	@Override
	public void testIntersectsPoint2fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point2f center = new Point2f();
		float radius;
		
		// A is inside B
		center.setX(this.testingBounds.getCenter().getX());
		center.setY(this.testingBounds.getCenter().getY());
		radius = this.referenceRadius * 10.f;
		assertTrue(this.testingBounds.intersects(center, radius));
		
		// A is enclosing B
		center.setX(this.testingBounds.getCenter().getX());
		center.setY(this.testingBounds.getCenter().getY());
		radius = this.referenceRadius / 10.f;
		assertTrue(this.testingBounds.intersects(center, radius));
		
		// A is enclosing B
		center.setX(this.testingBounds.getCenter().getX() - this.referenceRadius / 20.f);
		center.setY(this.testingBounds.getCenter().getY());
		radius = this.referenceRadius / 10.f;
		assertTrue(this.testingBounds.intersects(center, radius));

		// A and B do not overlap
		center.setX(this.lowerPoint.getX() - 100.f);
		center.setY(this.testingBounds.getCenter().getY());
		radius = 10.f;
		assertFalse(this.testingBounds.intersects(center, radius));
		
		// A and B do overlap
		center.setX(this.lowerPoint.getX());
		center.setY(this.centerPoint.getY());
		radius = this.referenceRadius / 2.f;
		assertTrue(this.testingBounds.intersects(center, radius));
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

		// A has at least one corners in B
		center.setX(this.lowerPoint.getX());
		center.setY(this.lowerPoint.getY());
		extent[0] = maxSize;
		extent[1] = maxSize;
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

		// A has at least one corners in B
		center.setX(this.lowerPoint.getX());
		center.setY(this.lowerPoint.getY());
		extent[0] = maxSize;
		extent[1] = maxSize;
		assertTrue(this.testingBounds.intersects(center, axis, extent));
	}

	@Override
	public void testClassifiesIC() {
		assertTrue(this.testingBounds.isInit());

		// MinimumBoundingRectangle
		{
			Point2f lower = new Point2f();
			Point2f upper = new Point2f();
			
			// A is enclosing B
			lower.setX(this.centerPoint.getX()-1.f);
			lower.setY(this.centerPoint.getY()-1.f);
			upper.setX(this.centerPoint.getX()+1.f);
			upper.setY(this.centerPoint.getY()+1.f);
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new MinimumBoundingRectangle(lower, upper)));

			// A is inside B
			lower.setX(this.lowerPoint.getX()-10.f);
			lower.setY(this.lowerPoint.getY()-10.f);
			upper.setX(this.upperPoint.getX()+10.f);
			upper.setY(this.upperPoint.getY()+10.f);
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new MinimumBoundingRectangle(lower, upper)));

			// A is outside B
			lower.setX(this.lowerPoint.getX()-100.f);
			lower.setY(this.lowerPoint.getY());
			upper.setX(this.lowerPoint.getX()-10.f);
			upper.setY(this.upperPoint.getY());
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new MinimumBoundingRectangle(lower, upper)));
		}
		
		// BoundingCircle
		{
			Point2f center = new Point2f();
			float radius;
			
			// A is inside B
			center.setX(this.testingBounds.getCenter().getX());
			center.setY(this.testingBounds.getCenter().getY());
			radius = this.referenceRadius * 10.f;
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new BoundingCircle(center, radius)));
			
			// A is enclosing B
			center.setX(this.testingBounds.getCenter().getX());
			center.setY(this.testingBounds.getCenter().getY());
			radius = this.referenceRadius / 10.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingCircle(center, radius)));
			
			// A is enclosing B
			center.setX(this.testingBounds.getCenter().getX() - this.referenceRadius / 20.f);
			center.setY(this.testingBounds.getCenter().getY());
			radius = this.referenceRadius / 10.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingCircle(center, radius)));

			// A and B do not overlap
			center.setX(this.lowerPoint.getX() - 100.f);
			center.setY(this.testingBounds.getCenter().getY());
			radius = 10.f;
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new BoundingCircle(center, radius)));
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
			Point2f lower = new Point2f();
			Point2f upper = new Point2f();
			
			// A is enclosing B
			lower.setX(this.centerPoint.getX()-1.f);
			lower.setY(this.centerPoint.getY()-1.f);
			upper.setX(this.centerPoint.getX()+1.f);
			upper.setY(this.centerPoint.getY()+1.f);
			assertTrue(this.testingBounds.intersects(new MinimumBoundingRectangle(lower, upper)));

			// A is inside B
			lower.setX(this.lowerPoint.getX()-this.referenceRadius);
			lower.setY(this.lowerPoint.getY()-this.referenceRadius);
			upper.setX(this.upperPoint.getX()+this.referenceRadius);
			upper.setY(this.upperPoint.getY()+this.referenceRadius);
			assertTrue(this.testingBounds.intersects(new MinimumBoundingRectangle(lower, upper)));

			// A is outside B
			lower.setX(this.lowerPoint.getX()-100.f);
			lower.setY(this.lowerPoint.getY());
			upper.setX(this.lowerPoint.getX()-10.f);
			upper.setY(this.upperPoint.getY());
			assertFalse(this.testingBounds.intersects(new MinimumBoundingRectangle(lower, upper)));
		}
		
		// BoundingCircle
		{
			Point2f center = new Point2f();
			float radius;
			
			// A is inside B
			center.setX(this.testingBounds.getCenter().getX());
			center.setY(this.testingBounds.getCenter().getY());
			radius = this.referenceRadius * 10.f;
			assertTrue(this.testingBounds.intersects(new BoundingCircle(center, radius)));
			
			// A is enclosing B
			center.setX(this.testingBounds.getCenter().getX());
			center.setY(this.testingBounds.getCenter().getY());
			radius = this.referenceRadius / 10.f;
			assertTrue(this.testingBounds.intersects(new BoundingCircle(center, radius)));
			
			// A is enclosing B
			center.setX(this.testingBounds.getCenter().getX() - this.referenceRadius / 20.f);
			center.setY(this.testingBounds.getCenter().getY());
			radius = this.referenceRadius / 10.f;
			assertTrue(this.testingBounds.intersects(new BoundingCircle(center, radius)));

			// A and B do not overlap
			center.setX(this.lowerPoint.getX() - 100.f);
			center.setY(this.testingBounds.getCenter().getY());
			radius = 10.f;
			assertFalse(this.testingBounds.intersects(new BoundingCircle(center, radius)));
		}
		
		// OrientedBoundingRectangle
		{
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

			// A is enclosing B
			center.setX(this.centerPoint.getX());
			center.setY(this.centerPoint.getY());
			extent[0] = minSize / 10.f;
			extent[1] = minSize / 10.f;
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
		
		BoundingSphere sphere = this.testingBounds.toBounds3D();
		
		Point3f p = new Point3f(this.centerPoint.getX(), this.centerPoint.getY(), 0.f);
		
		assertNotNull(sphere);
		assertEpsilonEquals(p, sphere.getCenter());
		assertEpsilonEquals(this.referenceRadius, sphere.getRadius());
	}
	
	/**
	 */
	@Override
	public void testToBounds3DCoordinateSystem3D() {
		assertTrue(this.testingBounds.isInit());

		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			BoundingSphere sphere = this.testingBounds.toBounds3D(cs);
			
			Point3f p = cs.fromCoordinateSystem2D(this.centerPoint);
			
			assertNotNull(sphere);
			assertEpsilonEquals(p, sphere.getCenter());
			assertEpsilonEquals(this.referenceRadius, sphere.getRadius());
		}
	}

	/**
	 */
	@Override
	public void testToBounds3DFloatFloat() {
		assertTrue(this.testingBounds.isInit());
		
		BoundingSphere sphere = this.testingBounds.toBounds3D(1234.f, 3456.f);
		
		Point3f p = new Point3f(this.centerPoint.getX(), this.centerPoint.getY(), 1234.f);
		
		assertNotNull(sphere);
		assertEpsilonEquals(p, sphere.getCenter());
		assertEpsilonEquals(this.referenceRadius, sphere.getRadius());

		sphere = this.testingBounds.toBounds3D(1234.f, -3456.f);
		
		p = new Point3f(this.centerPoint.getX(), this.centerPoint.getY(), 1234.f);
		
		assertNotNull(sphere);
		assertEpsilonEquals(p, sphere.getCenter());
		assertEpsilonEquals(this.referenceRadius, sphere.getRadius());
	}
	
	/**
	 */
	@Override
	public void testToBounds3DFloatFloatCoordinateSystem3D() {
		assertTrue(this.testingBounds.isInit());

		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			BoundingSphere sphere = this.testingBounds.toBounds3D(1234.f, 2345.f, cs);
			
			Point3f p = cs.fromCoordinateSystem2D(this.centerPoint, 1234.f);
			
			assertNotNull(sphere);
			assertEpsilonEquals(p, sphere.getCenter());
			assertEpsilonEquals(this.referenceRadius, sphere.getRadius());

			sphere = this.testingBounds.toBounds3D(1234.f, -2345.f, cs);
			
			p = cs.fromCoordinateSystem2D(this.centerPoint, 1234.f);
			
			assertNotNull(sphere);
			assertEpsilonEquals(p, sphere.getCenter());
			assertEpsilonEquals(this.referenceRadius, sphere.getRadius());
		}
	}

	/**
	 */
	@Override
	public void testFarestPointP() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f expected = new Point2f();
		Vector2f v = new Vector2f();

		Point2f p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(10,10));
		v.sub(this.centerPoint, p);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.farestPoint(p));

		p = new Point2f(this.lowerPoint);
		v.sub(this.centerPoint, p);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.farestPoint(p));

		p = new Point2f(this.centerPoint);
		expected.set(this.centerPoint);
		expected.setX(expected.getX() + this.referenceRadius);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.farestPoint(p));

		p = new Point2f(this.upperPoint);
		p.sub(new Vector2f(100,0));
		v.sub(this.centerPoint, p);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.farestPoint(p));

		p = new Point2f(this.upperPoint);
		v.sub(this.centerPoint, p);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.farestPoint(p));
	}

	/**
	 */
	@Override
	public void testNearestPointP() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f expected = new Point2f();
		Vector2f v = new Vector2f();
		
		Point2f p = new Point2f(this.lowerPoint);
		p.sub(new Vector2f(10,10));
		v.sub(p, this.centerPoint);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.nearestPoint(p));

		p = new Point2f(this.lowerPoint);
		v.sub(p, this.centerPoint);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.nearestPoint(p));

		p = new Point2f(this.centerPoint);
		expected.set(this.centerPoint);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.nearestPoint(p));

		p = new Point2f(this.upperPoint);
		p.sub(new Vector2f(100,0));
		v.sub(p, this.centerPoint);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.nearestPoint(p));

		p = new Point2f(this.upperPoint);
		v.sub(p, this.centerPoint);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.nearestPoint(p));
	}

}
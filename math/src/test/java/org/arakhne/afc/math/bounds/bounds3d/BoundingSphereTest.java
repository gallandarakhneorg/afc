/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
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
package org.arakhne.afc.math.bounds.bounds3d;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.bounds.bounds2f.BoundingCircle;
import org.arakhne.afc.math.bounds.bounds3f.AlignedBoundingBox;
import org.arakhne.afc.math.bounds.bounds3f.BoundingSphere;
import org.arakhne.afc.math.bounds.bounds3f.CombinableBounds3D;
import org.arakhne.afc.math.bounds.bounds3f.OrientedBoundingBox;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;
import org.arakhne.afc.math.intersection.IntersectionType;
import org.arakhne.afc.math.plane.PlanarClassificationType;
import org.arakhne.afc.math.plane.Plane;
import org.arakhne.afc.math.plane.Plane4f;
import org.arakhne.afc.math.plane.XYPlane;
import org.arakhne.afc.math.plane.XZPlane;
import org.arakhne.afc.math.plane.YZPlane;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.sizediterator.SizedIterator;


/**
 * Unit test for BoundingSphere class.
 * 
 * @author $Author: sgalland$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BoundingSphereTest extends AbstractOrientedBound3DTest {

	/** Reference instance.
	 */
	private BoundingSphere testingBounds;
	
	/** Reference instance.
	 */
	private Point3f[] testingPoints;

	/** Reference lower point.
	 */
	private Point3f lowerPoint;

	/** Reference upper point.
	 */
	private Point3f upperPoint;

	/** Reference center point.
	 */
	private Point3f centerPoint;
	
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
		this.testingPoints = randomPoints3D();
		this.lowerPoint = new Point3f();
		this.upperPoint = new Point3f();
		this.centerPoint = new Point3f();
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
	protected BoundingSphere createBounds(Tuple3f[] points) {
		BoundingSphere b = new BoundingSphere();
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
	private static float computeLowerUpperCenter(Tuple3f[] points, Tuple3f lower, Tuple3f upper, Tuple3f center) {
		float minx, miny, minz;
		float maxx, maxy, maxz;
		
		minx = miny = minz = Float.POSITIVE_INFINITY;
		maxx = maxy = maxz = Float.NEGATIVE_INFINITY;
		
		for(Tuple3f point : points) {
			if (point!=null) {
				if (point.getX()<minx) minx = point.getX();
				if (point.getY()<miny) miny = point.getY();
				if (point.getZ()<minz) minz = point.getZ();
				if (point.getX()>maxx) maxx = point.getX();
				if (point.getY()>maxy) maxy = point.getY();
				if (point.getZ()>maxz) maxz = point.getZ();
			}
		}
		
		Tuple3f c = center==null ? new Point3f() : center;
		c.set(
				(minx+maxx) / 2.f,
				(miny+maxy) / 2.f,
				(minz+maxz) / 2.f);
		
		float radius = 0;
		float distance;
		for(Tuple3f point : points) {
			distance = MathUtil.distance(c.getX(),c.getY(),c.getZ(),point.getX(),point.getY(),point.getZ());
			if(distance>radius)
				radius = distance;
		}
		
		if (lower!=null) lower.set(c.getX()-radius,c.getY()-radius,c.getZ()-radius);
		if (upper!=null) upper.set(c.getX()+radius,c.getY()+radius,c.getZ()+radius);

		return radius;
	}

	@Override
	public void testGetMathematicalDimension() {
		assertTrue(this.testingBounds.isInit());
		
		assertEquals(PseudoHamelDimension.DIMENSION_3D, this.testingBounds.getMathematicalDimension());
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
	public void testGetCenterZ() {
		assertTrue(this.testingBounds.isInit());
		
		assertEpsilonEquals(this.centerPoint.getZ(), this.testingBounds.getCenterZ());
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
	public void testGetMaxZ() {
		assertTrue(this.testingBounds.isInit());
		
		assertEpsilonEquals(this.upperPoint.getZ(), this.testingBounds.getMaxZ());
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
	public void testGetMinZ() {
		assertTrue(this.testingBounds.isInit());
		
		assertEpsilonEquals(this.lowerPoint.getZ(), this.testingBounds.getMinZ());
	}

	@Override
	public void testDistanceMaxP() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(10,10,10));
		assertEpsilonEquals(
				p.distance(this.centerPoint)+this.referenceRadius, 
				this.testingBounds.distanceMax(p));

		p = new Point3f(this.lowerPoint);
		assertEpsilonEquals(
				this.lowerPoint.distance(this.centerPoint)+this.referenceRadius, 
				this.testingBounds.distanceMax(p));

		p = new Point3f(this.centerPoint);
		assertEpsilonEquals(
				this.referenceRadius, 
				this.testingBounds.distanceMax(p));

		p = new Point3f(this.upperPoint);
		p.sub(new Vector3f(100,0,100));
		assertEpsilonEquals(
				p.distance(this.centerPoint)+this.referenceRadius, 
				this.testingBounds.distanceMax(p));

		p = new Point3f(this.upperPoint);
		assertEpsilonEquals(
				this.upperPoint.distance(this.centerPoint)+this.referenceRadius, 
				this.testingBounds.distanceMax(p));
	}

	@Override
	public void testDistanceMaxSquaredP() {
		assertTrue(this.testingBounds.isInit());
		
		float sqExpected;
		
		Point3f p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(10,10,10));
		sqExpected = p.distance(this.centerPoint)+this.referenceRadius;
		assertEpsilonEquals(
				sqExpected * sqExpected, 
				this.testingBounds.distanceMaxSquared(p));

		p = new Point3f(this.lowerPoint);
		sqExpected = this.lowerPoint.distance(this.centerPoint)+this.referenceRadius;
		assertEpsilonEquals(
				sqExpected * sqExpected, 
				this.testingBounds.distanceMaxSquared(p));

		p = new Point3f(this.centerPoint);
		sqExpected = this.referenceRadius;
		assertEpsilonEquals(
				sqExpected * sqExpected, 
				this.testingBounds.distanceMaxSquared(p));

		p = new Point3f(this.upperPoint);
		p.sub(new Vector3f(100,0,100));
		sqExpected = p.distance(this.centerPoint)+this.referenceRadius;
		assertEpsilonEquals(
				sqExpected * sqExpected, 
				this.testingBounds.distanceMaxSquared(p));

		p = new Point3f(this.upperPoint);
		sqExpected = this.upperPoint.distance(this.centerPoint)+this.referenceRadius;
		assertEpsilonEquals(
				sqExpected * sqExpected, 
				this.testingBounds.distanceMaxSquared(p));
	}

	@Override
	public void testFarestPointP() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f expected = new Point3f();
		Vector3f v = new Vector3f();
		
		Point3f p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(10,10,10));
		v.sub(this.centerPoint, p);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.farestPoint(p));

		p = new Point3f(this.lowerPoint);
		v.sub(this.centerPoint, p);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.farestPoint(p));

		p = new Point3f(this.centerPoint);
		expected.set(this.centerPoint);
		expected.setX(expected.getX() + this.referenceRadius);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.farestPoint(p));

		p = new Point3f(this.upperPoint);
		p.sub(new Vector3f(100,0,100));
		v.sub(this.centerPoint, p);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.farestPoint(p));

		p = new Point3f(this.upperPoint);
		v.sub(this.centerPoint, p);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.farestPoint(p));
	}

	@Override
	public void testDistanceP() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(10,10,10));
		assertEpsilonEquals(
				p.distance(this.centerPoint)-this.referenceRadius, 
				this.testingBounds.distance(p));

		p = new Point3f(this.lowerPoint);
		assertEpsilonEquals(
				this.lowerPoint.distance(this.centerPoint)-this.referenceRadius, 
				this.testingBounds.distance(p));

		p = new Point3f(this.centerPoint);
		assertEpsilonEquals(
				0.f, 
				this.testingBounds.distance(p));

		p = new Point3f(this.upperPoint);
		p.sub(new Vector3f(100,0,100));
		assertEpsilonEquals(
				p.distance(this.centerPoint)-this.referenceRadius, 
				this.testingBounds.distance(p));

		p = new Point3f(this.upperPoint);
		assertEpsilonEquals(
				this.upperPoint.distance(this.centerPoint)-this.referenceRadius, 
				this.testingBounds.distance(p));
	}


	@Override
	public void testDistanceSquaredP() {
		assertTrue(this.testingBounds.isInit());
		
		float sqExpected;
		
		Point3f p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(10,10,10));
		sqExpected = p.distance(this.centerPoint)-this.referenceRadius;
		assertEpsilonEquals(
				sqExpected*sqExpected, 
				this.testingBounds.distanceSquared(p));

		p = new Point3f(this.lowerPoint);
		sqExpected = this.lowerPoint.distance(this.centerPoint)-this.referenceRadius;
		assertEpsilonEquals(
				sqExpected * sqExpected, 
				this.testingBounds.distanceSquared(p));

		p = new Point3f(this.centerPoint);
		assertEpsilonEquals(
				0.f, 
				this.testingBounds.distanceSquared(p));

		p = new Point3f(this.upperPoint);
		p.sub(new Vector3f(100,0,100));
		sqExpected = p.distance(this.centerPoint)-this.referenceRadius;
		assertEpsilonEquals(
				sqExpected * sqExpected, 
				this.testingBounds.distanceSquared(p));

		p = new Point3f(this.upperPoint);
		sqExpected = this.upperPoint.distance(this.centerPoint)-this.referenceRadius;
		assertEpsilonEquals(
				sqExpected * sqExpected, 
				this.testingBounds.distanceSquared(p));
	}

	@Override
	public void testNearestPointP() {
		assertTrue(this.testingBounds.isInit());

		Point3f expected = new Point3f();
		Vector3f v = new Vector3f();
		
		Point3f p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(10,10,10));
		v.sub(p, this.centerPoint);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.nearestPoint(p));

		p = new Point3f(this.lowerPoint);
		v.sub(p, this.centerPoint);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.nearestPoint(p));

		p = new Point3f(this.centerPoint);
		assertEpsilonEquals(
				this.centerPoint, 
				this.testingBounds.nearestPoint(p));

		p = new Point3f(this.upperPoint);
		p.sub(new Vector3f(100,0,100));
		v.sub(p, this.centerPoint);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.nearestPoint(p));

		p = new Point3f(this.upperPoint);
		v.sub(p, this.centerPoint);
		v.normalize();
		v.scale(this.referenceRadius);
		expected.add(this.centerPoint, v);
		assertEpsilonEquals(
				expected, 
				this.testingBounds.nearestPoint(p));
	}

	@Override
	public void testGetCenter() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f c = this.testingBounds.getCenter();
		assertEpsilonEquals((this.lowerPoint.getX()+this.upperPoint.getX())/2.f, c.getX());
		assertEpsilonEquals((this.lowerPoint.getY()+this.upperPoint.getY())/2.f, c.getY());
		assertEpsilonEquals((this.lowerPoint.getZ()+this.upperPoint.getZ())/2.f, c.getZ());
	}

	@Override
	public void testGetLower() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f l = this.testingBounds.getLower();
		assertEpsilonEquals(this.lowerPoint.getX(), l.getX());
		assertEpsilonEquals(this.lowerPoint.getY(), l.getY());
		assertEpsilonEquals(this.lowerPoint.getZ(), l.getZ());
	}

	@Override
	public void testGetLowerUpper() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f l = new Point3f();
		Point3f u = new Point3f();
		
		this.testingBounds.getLowerUpper(l, u);
		assertEpsilonEquals(this.lowerPoint.getX(), l.getX());
		assertEpsilonEquals(this.lowerPoint.getY(), l.getY());
		assertEpsilonEquals(this.lowerPoint.getZ(), l.getZ());
		assertEpsilonEquals(this.upperPoint.getX(), u.getX());
		assertEpsilonEquals(this.upperPoint.getY(), u.getY());
		assertEpsilonEquals(this.upperPoint.getZ(), u.getZ());
	}

	@Override
	public void testGetPosition() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f c = this.testingBounds.getPosition();
		assertEpsilonEquals((this.lowerPoint.getX()+this.upperPoint.getX())/2.f, c.getX());
		assertEpsilonEquals((this.lowerPoint.getY()+this.upperPoint.getY())/2.f, c.getY());
		assertEpsilonEquals((this.lowerPoint.getZ()+this.upperPoint.getZ())/2.f, c.getZ());
	}

	@Override
	public void testGetUpper() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f u = this.testingBounds.getUpper();
		assertEpsilonEquals(this.upperPoint.getX(), u.getX());
		assertEpsilonEquals(this.upperPoint.getY(), u.getY());
		assertEpsilonEquals(this.upperPoint.getZ(), u.getZ());
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
		
		BoundingSphere clone = this.testingBounds.clone();
		assertNotSame(this.testingBounds, clone);
		assertNotSame(this.testingBounds.getCenter(), clone.getCenter());
		assertEquals(this.testingBounds.getRadius(), clone.getRadius());
	}

	@Override
	public void testTranslateVector3f() {
		assertTrue(this.testingBounds.isInit());
		
		Vector3f v = randomVector3D();
		this.testingBounds.translate(v);
		assertEpsilonEquals(this.centerPoint.getX()+v.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(this.centerPoint.getY()+v.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(this.centerPoint.getZ()+v.getZ(), this.testingBounds.getCenter().getZ());
		assertEquals(this.referenceRadius, this.testingBounds.getRadius());
	}

	@Override
	public void testGetGlobalOrientedVertices() {
		assertTrue(this.testingBounds.isInit());
		
		Set<Point3f> expectedPoints = new HashSet<Point3f>();
		Point3f p;
		
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.lowerPoint.getY(), this.lowerPoint.getZ()));
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ()));
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.upperPoint.getY(), this.lowerPoint.getZ()));
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.upperPoint.getY(), this.upperPoint.getZ()));
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.lowerPoint.getZ()));
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ()));
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.upperPoint.getY(), this.lowerPoint.getZ()));
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.upperPoint.getY(), this.upperPoint.getZ()));
		
		SizedIterator<? extends Point3f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
		assertNotNull(iterator);
		assertEquals(8, iterator.totalSize());
		
		for(int i=0; i<8; ++i) {
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
		
		List<Point3f> expectedPoints = new ArrayList<Point3f>();
		Point3f p;
		
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.upperPoint.getY(), this.upperPoint.getZ()));
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.upperPoint.getY(), this.upperPoint.getZ()));
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ()));
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ()));
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.lowerPoint.getZ()));
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.upperPoint.getY(), this.lowerPoint.getZ()));
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.upperPoint.getY(), this.lowerPoint.getZ()));
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.lowerPoint.getY(), this.lowerPoint.getZ()));

		try {
			this.testingBounds.getGlobalVertexAt(-1);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
		
		for(int i=0; i<8; ++i) {
			p = this.testingBounds.getGlobalVertexAt(i);
			assertNotNull(p);
			assertEpsilonEquals(expectedPoints.get(i), p);
		}
		
		try {
			this.testingBounds.getGlobalVertexAt(8);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
	}

	@Override
	public void testGetVertexCount() {
		assertTrue(this.testingBounds.isInit());
		assertEquals(8, this.testingBounds.getVertexCount());
	}

	@Override
	public void testGetLocalOrientedVertices() {
		assertTrue(this.testingBounds.isInit());
		
		Set<Vector3f> expectedPoints = new HashSet<Vector3f>();
		Vector3f v;
		float size = this.referenceRadius;
		
		expectedPoints.add(new Vector3f(size, size, size));
		expectedPoints.add(new Vector3f(size, size, -size));
		expectedPoints.add(new Vector3f(size, -size, size));
		expectedPoints.add(new Vector3f(size, -size, -size));
		expectedPoints.add(new Vector3f(-size, size, size));
		expectedPoints.add(new Vector3f(-size, size, -size));
		expectedPoints.add(new Vector3f(-size, -size, size));
		expectedPoints.add(new Vector3f(-size, -size, -size));
		
		SizedIterator<Vector3f> iterator = this.testingBounds.getLocalOrientedBoundVertices();
		assertNotNull(iterator);
		assertEquals(8, iterator.totalSize());
		
		for(int i=0; i<8; ++i) {
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
		
		List<Vector3f> expectedPoints = new ArrayList<Vector3f>();
		Vector3f v;
		float size = this.referenceRadius;
		
		expectedPoints.add(new Vector3f(size, size, size));
		expectedPoints.add(new Vector3f(-size, size, size));
		expectedPoints.add(new Vector3f(-size, -size, size));
		expectedPoints.add(new Vector3f(size, -size, size));
		expectedPoints.add(new Vector3f(size, -size, -size));
		expectedPoints.add(new Vector3f(size, size, -size));
		expectedPoints.add(new Vector3f(-size, size, -size));
		expectedPoints.add(new Vector3f(-size, -size, -size));

		try {
			this.testingBounds.getLocalVertexAt(-1);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
		
		for(int i=0; i<8; ++i) {
			v = this.testingBounds.getLocalVertexAt(i);
			assertNotNull(v);
			assertEpsilonEquals(expectedPoints.get(i), v);
		}
		
		try {
			this.testingBounds.getLocalVertexAt(8);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
	}

	/**
	 */
	@Override
	public void testGetOrientedBoundAxis() {
		Vector3f[] axis = this.testingBounds.getOrientedBoundAxis();
		assertEpsilonEquals(new Vector3f(1.,0.,0.), axis[0]);
		assertEpsilonEquals(new Vector3f(0.,1.,0.), axis[1]);
		assertEpsilonEquals(new Vector3f(0.,0.,1.), axis[2]);
	}

	/**
	 */
	@Override
	public void testGetOrientedBoundExtents() {
		Vector3f extent = this.testingBounds.getOrientedBoundExtentVector();
		assertEpsilonEquals(new Vector3f(this.referenceRadius,this.referenceRadius,this.referenceRadius), extent);
	}

	/**
	 */
	@Override
	public void testGetR() {
		Vector3f axis = this.testingBounds.getR();
		assertEpsilonEquals(new Vector3f(1.,0.,0.), axis);
	}
	
	/**
	 */
	@Override
	public void testGetS() {
		Vector3f axis = this.testingBounds.getS();
		assertEpsilonEquals(new Vector3f(0.,1.,0.), axis);
	}

	/**
	 */
	@Override
	public void testGetT() {
		Vector3f axis = this.testingBounds.getT();
		assertEpsilonEquals(new Vector3f(0.,0.,1.), axis);
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

	/**
	 */
	@Override
	public void testGetTExtent() {
		float extent = this.testingBounds.getTExtent();
		assertEpsilonEquals(this.referenceRadius, extent);
	}

	@Override
	public void testCombineTuple3fArray() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f[] points = randomPoints3D();

		Point3f newCenter = new Point3f(this.centerPoint);
		float newRadius = this.referenceRadius;
		Vector3f v = new Vector3f();
		
		for(Point3f p : points) {
			float d = p.distance(newCenter);
			if (d>newRadius) {
				newRadius = (d+newRadius) / 2.f;
				v.sub(newCenter, p);
				v.normalize();
				v.scale(newRadius);
				newCenter.add(p, v);
			}
		}
		
		this.testingBounds.combine(points);
		
		assertEpsilonEquals(newRadius, this.testingBounds.getRadius());
		assertEpsilonEquals(newCenter.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(newCenter.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(newCenter.getZ(), this.testingBounds.getCenter().getZ());
	}

	@Override
	public void testSetTuple3fArray() {
		assertTrue(this.testingBounds.isInit());
		
		Tuple3f[] points = randomTuples3D();
		Point3f l = new Point3f();
		Point3f u = new Point3f();
		Point3f c = new Point3f();
		computeLowerUpperCenter(points, l, u, c);

		this.testingBounds.set(points);

		assertEpsilonEquals(c.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(c.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(c.getZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(
				MathUtil.max(
						(u.getX()-l.getX())/2.f,
						(u.getY()-l.getY())/2.f,
						(u.getZ()-l.getZ())/2.f),
				this.testingBounds.getRadius());
	}

	@Override
	public void testCombineCB() {
		assertTrue(this.testingBounds.isInit());
		Point3f p;
		Point3f newCenter;
		float newRadius;		
		
		BoundingSphere sphere = new BoundingSphere();
		p = randomPoint3D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);

		{
			Vector3f v = new Vector3f();
			v.sub(this.centerPoint, p);
			newCenter = new Point3f();
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
		assertEpsilonEquals(newCenter.getZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(newRadius, this.testingBounds.getRadius());

		AlignedBoundingBox box = new AlignedBoundingBox();
		box.set(randomPoints3D());
		
		{
			Point3f bCenter = box.getCenter();
			float dmax = box.distanceMax(this.testingBounds.getCenter());
			Vector3f v = new Vector3f();
			v.sub(this.testingBounds.getCenter(), bCenter);
			float bRadius = dmax - v.length();
			newCenter = new Point3f();
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
		assertEpsilonEquals(newCenter.getZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(newRadius, this.testingBounds.getRadius());
	}

	@Override
	public void testCombineCollection() {
		assertTrue(this.testingBounds.isInit());
		
		Collection<CombinableBounds3D> bounds = new ArrayList<CombinableBounds3D>();
		Point3f p;
		Point3f sphereCenter = new Point3f(this.centerPoint);
		float sphereRadius = this.referenceRadius;		
		
		BoundingSphere sphere = new BoundingSphere();
		p = randomPoint3D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);

		AlignedBoundingBox box = new AlignedBoundingBox();
		box.set(randomPoints3D());
		
		bounds.add(sphere);
		bounds.add(box);

		
		{
			Vector3f v = new Vector3f();
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
			Point3f bCenter = box.getCenter();
			float dmax = box.distanceMax(sphereCenter);
			Vector3f v = new Vector3f();
			v.sub(sphereCenter, bCenter);
			float bRadius = dmax - v.length();
			float newDiameter = MathUtil.max(2.f*bRadius, 2.f*sphereRadius, v.length()+bRadius+sphereRadius);
			if (newDiameter==2.f*bRadius) sphereCenter.set(bCenter);
			else if (newDiameter==2.f*sphereRadius) sphereCenter.set(sphereCenter);
			else {
				v.scale(sphereRadius / (sphereRadius+bRadius));
				sphereCenter.add(bCenter,v);
			}
			sphereRadius = newDiameter / 2.f;
		}
		
		this.testingBounds.combine(bounds);

		assertEpsilonEquals(sphereCenter.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(sphereCenter.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(sphereCenter.getZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(sphereRadius, this.testingBounds.getRadius());
	}

	@Override
	public void testCombineP() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f p = randomPoint3D();

		float newRadius = p.distance(this.centerPoint);
		
		this.testingBounds.combine(p);

		if (newRadius<=this.referenceRadius) {
			assertEpsilonEquals(this.centerPoint.getX(), this.testingBounds.getCenter().getX());
			assertEpsilonEquals(this.centerPoint.getY(), this.testingBounds.getCenter().getY());
			assertEpsilonEquals(this.centerPoint.getZ(), this.testingBounds.getCenter().getZ());
			assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
		}
		else {
			newRadius = (newRadius+this.referenceRadius) /2.f;
			Vector3f v = new Vector3f(this.centerPoint);
			v.sub(p);
			v.normalize();
			v.scale(newRadius);
			Point3f newCenter = new Point3f(p);
			newCenter.add(v);

			assertEpsilonEquals(newCenter.getX(), this.testingBounds.getCenter().getX());
			assertEpsilonEquals(newCenter.getY(), this.testingBounds.getCenter().getY());
			assertEpsilonEquals(newCenter.getZ(), this.testingBounds.getCenter().getZ());
			assertEpsilonEquals(newRadius, this.testingBounds.getRadius());
		}
	}

	@Override
	public void testSetCB() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f p;
		
		BoundingSphere sphere = new BoundingSphere();
		p = randomPoint3D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);
		this.testingBounds.set(sphere);
		assertEpsilonEquals(p.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(p.getZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(r, this.testingBounds.getRadius());

		AlignedBoundingBox box = new AlignedBoundingBox();
		p = randomPoint3D();
		box.set(p);
		this.testingBounds.set(box);
		assertEpsilonEquals(box.getCenterX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(box.getCenterY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(box.getCenterZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(box.getSizeX()/2.f, this.testingBounds.getRadius());
	}

	@Override
	public void testSetP() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f p = randomPoint3D();
		this.testingBounds.set(p);
		assertEpsilonEquals(p.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(p.getZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(0.f, this.testingBounds.getRadius());
	}

	/** 
	 */
	public void testGetSize() {
		assertTrue(this.testingBounds.isInit());
		
		Vector3f size = this.testingBounds.getSize();
		assertEpsilonEquals(this.referenceRadius*2.f, size.getX());
		assertEpsilonEquals(this.referenceRadius*2.f, size.getY());
		assertEpsilonEquals(this.referenceRadius*2.f, size.getZ());
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

	/** 
	 */
	public void testGetSizeZ() {
		assertTrue(this.testingBounds.isInit());
		
		float size = this.testingBounds.getSizeZ();
		assertEpsilonEquals(this.referenceRadius*2.f, size);
	}

	//-------------------------------------------
	// BoundingSphere dedicated tests
	//-------------------------------------------

	/** 
	 */
	public void testGetRadius() {
		assertTrue(this.testingBounds.isInit());
		
		assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
	}

	/**
	 */
	public void testGetCenterPoint3f() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f c = new Point3f();
		this.testingBounds.getCenter(c);
		assertEpsilonEquals((this.lowerPoint.getX()+this.upperPoint.getX())/2.f, c.getX());
		assertEpsilonEquals((this.lowerPoint.getY()+this.upperPoint.getY())/2.f, c.getY());
		assertEpsilonEquals((this.lowerPoint.getZ()+this.upperPoint.getZ())/2.f, c.getZ());
	}

	/** 
	 */
	public void testSetRadius() {
		assertTrue(this.testingBounds.isInit());
		
		this.testingBounds.setRadius(-100);
		assertEpsilonEquals(this.centerPoint.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(this.centerPoint.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(this.centerPoint.getZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(0, this.testingBounds.getRadius());
		
		float r = this.RANDOM.nextFloat() * 100.f;
		this.testingBounds.setRadius(r);
		assertEpsilonEquals(this.centerPoint.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(this.centerPoint.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(this.centerPoint.getZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(r, this.testingBounds.getRadius());
	}

	/** 
	 */
	public void testSetTuple3fFloat() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f p = randomPoint3D();
		this.testingBounds.set(p, -100);
		assertEpsilonEquals(p.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(p.getZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(0, this.testingBounds.getRadius());
		
		p = randomPoint3D();
		float r = this.RANDOM.nextFloat() * 100.f;
		this.testingBounds.set(p, r);
		assertEpsilonEquals(p.getX(), this.testingBounds.getCenter().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getCenter().getY());
		assertEpsilonEquals(p.getZ(), this.testingBounds.getCenter().getZ());
		assertEpsilonEquals(r, this.testingBounds.getRadius());
	}

	/**
	 */
	public void testCombineFloatFloatFloat() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f p = randomPoint3D();

		float newRadius = p.distance(this.centerPoint);
		
		this.testingBounds.combine(p.getX(),p.getY(),p.getZ());

		if (newRadius<=this.referenceRadius) {
			assertEpsilonEquals(this.centerPoint.getX(), this.testingBounds.getCenter().getX());
			assertEpsilonEquals(this.centerPoint.getY(), this.testingBounds.getCenter().getY());
			assertEpsilonEquals(this.centerPoint.getZ(), this.testingBounds.getCenter().getZ());
			assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
		}
		else {
			newRadius = (newRadius+this.referenceRadius) /2.f;
			Vector3f v = new Vector3f(this.centerPoint);
			v.sub(p);
			v.normalize();
			v.scale(newRadius);
			Point3f newCenter = new Point3f(p);
			newCenter.add(v);

			assertEpsilonEquals(newCenter.getX(), this.testingBounds.getCenter().getX());
			assertEpsilonEquals(newCenter.getY(), this.testingBounds.getCenter().getY());
			assertEpsilonEquals(newCenter.getZ(), this.testingBounds.getCenter().getZ());
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
				new Point3f(this.upperPoint.getX() + 10., this.centerPoint.getY(), this.centerPoint.getZ())));
	}

	@Override
	public void testIntersectsP() {
		assertTrue(this.testingBounds.isInit());

		assertTrue(this.testingBounds.intersects(this.centerPoint));

		assertFalse(this.testingBounds.intersects(this.lowerPoint));
		assertFalse(this.testingBounds.intersects(this.upperPoint));
		
		assertFalse(this.testingBounds.intersects(
				new Point3f(this.upperPoint.getX() + 10., this.centerPoint.getY(), this.centerPoint.getZ())));
	}

	@Override
	public void testClassifiesPP() {
		assertTrue(this.testingBounds.isInit());

		Point3f lower = new Point3f();
		Point3f upper = new Point3f();
		
		// A is enclosing B
		lower.setX(this.centerPoint.getX()-1.f);
		lower.setY(this.centerPoint.getY()-1.f);
		lower.setZ(this.centerPoint.getZ()-1.f);
		upper.setX(this.centerPoint.getX()+1.f);
		upper.setY(this.centerPoint.getY()+1.f);
		upper.setZ(this.centerPoint.getZ()+1.f);
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(lower, upper));

		// A is inside B
		lower.setX(this.lowerPoint.getX()-10.f);
		lower.setY(this.lowerPoint.getY()-10.f);
		lower.setZ(this.lowerPoint.getZ()-10.f);
		upper.setX(this.upperPoint.getX()+10.f);
		upper.setY(this.upperPoint.getY()+10.f);
		upper.setZ(this.upperPoint.getZ()+10.f);
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(lower, upper));

		// A is outside B
		lower.setX(this.lowerPoint.getX()-100.f);
		lower.setY(this.lowerPoint.getY());
		lower.setZ(this.lowerPoint.getZ());
		upper.setX(this.lowerPoint.getX()-10.f);
		upper.setY(this.upperPoint.getY());
		upper.setZ(this.upperPoint.getZ());
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(lower, upper));

		// A has one corner in B
		lower.setX(this.lowerPoint.getX()-100.f);
		lower.setY(this.lowerPoint.getY()-10.f);
		lower.setZ(this.lowerPoint.getZ()-10.f);
		upper.setX(this.centerPoint.getX());
		upper.setY(this.centerPoint.getY());
		upper.setZ(this.centerPoint.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(lower, upper));
	}

	@Override
	public void testIntersectsPP() {
		assertTrue(this.testingBounds.isInit());

		Point3f lower = new Point3f();
		Point3f upper = new Point3f();
		
		// A is enclosing B
		lower.setX(this.centerPoint.getX()-1.f);
		lower.setY(this.centerPoint.getY()-1.f);
		lower.setZ(this.centerPoint.getZ()-1.f);
		upper.setX(this.centerPoint.getX()+1.f);
		upper.setY(this.centerPoint.getY()+1.f);
		upper.setZ(this.centerPoint.getZ()+1.f);
		assertTrue(this.testingBounds.intersects(lower, upper));

		// A is inside B
		lower.setX(this.lowerPoint.getX()-10.f);
		lower.setY(this.lowerPoint.getY()-10.f);
		lower.setZ(this.lowerPoint.getZ()-10.f);
		upper.setX(this.upperPoint.getX()+10.f);
		upper.setY(this.upperPoint.getY()+10.f);
		upper.setZ(this.upperPoint.getZ()+10.f);
		assertTrue(this.testingBounds.intersects(lower, upper));

		// A is outside B
		lower.setX(this.lowerPoint.getX()-100.f);
		lower.setY(this.lowerPoint.getY());
		lower.setZ(this.lowerPoint.getZ());
		upper.setX(this.lowerPoint.getX()-10.f);
		upper.setY(this.upperPoint.getY());
		upper.setZ(this.upperPoint.getZ());
		assertFalse(this.testingBounds.intersects(lower, upper));

		// A has one corner in B
		lower.setX(this.lowerPoint.getX()-100.f);
		lower.setY(this.lowerPoint.getY()-10.f);
		lower.setZ(this.lowerPoint.getZ()-10.f);
		upper.setX(this.centerPoint.getX());
		upper.setY(this.centerPoint.getY());
		upper.setZ(this.centerPoint.getZ());
		assertTrue(this.testingBounds.intersects(lower, upper));
	}

	@Override
	public void testClassifiesPoint3fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point3f center = new Point3f();
		float radius;
		
		// A is inside B
		center.setX(this.testingBounds.getCenter().getX());
		center.setY(this.testingBounds.getCenter().getY());
		center.setZ(this.testingBounds.getCenter().getZ());
		radius = this.referenceRadius * 10.f;
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(center, radius));
		
		// A is enclosing B
		center.setX(this.testingBounds.getCenter().getX());
		center.setY(this.testingBounds.getCenter().getY());
		center.setZ(this.testingBounds.getCenter().getZ());
		radius = this.referenceRadius / 10.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(center, radius));
		
		// A is enclosing B
		center.setX(this.testingBounds.getCenter().getX() - this.referenceRadius / 20.f);
		center.setY(this.testingBounds.getCenter().getY());
		center.setZ(this.testingBounds.getCenter().getZ());
		radius = this.referenceRadius / 10.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(center, radius));

		// A and B do not overlap
		center.setX(this.lowerPoint.getX() - 100.f);
		center.setY(this.testingBounds.getCenter().getY());
		center.setZ(this.testingBounds.getCenter().getZ());
		radius = 10.f;
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(center, radius));
		
		// A and B do overlap
		center.setX(this.lowerPoint.getX());
		center.setY(this.centerPoint.getY());
		center.setZ(this.centerPoint.getZ());
		radius = this.referenceRadius / 2.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, radius));
	}

	@Override
	public void testIntersectsPoint3fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point3f center = new Point3f();
		float radius;
		
		// A is inside B
		center.setX(this.testingBounds.getCenter().getX());
		center.setY(this.testingBounds.getCenter().getY());
		center.setZ(this.testingBounds.getCenter().getZ());
		radius = this.referenceRadius * 10.f;
		assertTrue(this.testingBounds.intersects(center, radius));
		
		// A is enclosing B
		center.setX(this.testingBounds.getCenter().getX());
		center.setY(this.testingBounds.getCenter().getY());
		center.setZ(this.testingBounds.getCenter().getZ());
		radius = this.referenceRadius / 10.f;
		assertTrue(this.testingBounds.intersects(center, radius));
		
		// A is enclosing B
		center.setX(this.testingBounds.getCenter().getX() - this.referenceRadius / 20.f);
		center.setY(this.testingBounds.getCenter().getY());
		center.setZ(this.testingBounds.getCenter().getZ());
		radius = this.referenceRadius / 10.f;
		assertTrue(this.testingBounds.intersects(center, radius));

		// A and B do not overlap
		center.setX(this.lowerPoint.getX() - 100.f);
		center.setY(this.testingBounds.getCenter().getY());
		center.setZ(this.testingBounds.getCenter().getZ());
		radius = 10.f;
		assertFalse(this.testingBounds.intersects(center, radius));
		
		// A and B do overlap
		center.setX(this.lowerPoint.getX());
		center.setY(this.centerPoint.getY());
		center.setZ(this.centerPoint.getZ());
		radius = this.referenceRadius / 2.f;
		assertTrue(this.testingBounds.intersects(center, radius));
	}

	@Override
	public void testClassifiesAgainstPlane() {
		assertTrue(this.testingBounds.isInit());

		Plane p;
		
		p = new XYPlane(this.upperPoint.getZ() + 100.f);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));
		p = new XYPlane(this.lowerPoint.getZ() - 100.f);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));
		p = new XYPlane(this.centerPoint.getZ());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new XYPlane(this.lowerPoint.getZ());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new XYPlane(this.upperPoint.getZ());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));

		p = new XZPlane(this.upperPoint.getY() + 100.f);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));
		p = new XZPlane(this.lowerPoint.getY() - 100.f);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));
		p = new XZPlane(this.centerPoint.getY());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new XZPlane(this.lowerPoint.getY());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new XZPlane(this.upperPoint.getY());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));

		p = new YZPlane(this.upperPoint.getX() + 100.f);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));
		p = new YZPlane(this.lowerPoint.getX() - 100.f);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));
		p = new YZPlane(this.centerPoint.getX());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new YZPlane(this.lowerPoint.getX());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new YZPlane(this.upperPoint.getX());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		
		Vector3f v = new Vector3f(1.,1.,1.);
		
		p = new Plane4f(v, this.centerPoint);
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.lowerPoint.getX()+(float) MathConstants.EPSILON,
				this.lowerPoint.getY()+(float) MathConstants.EPSILON,
				this.lowerPoint.getZ()+(float) MathConstants.EPSILON);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.upperPoint.getX()-(float) MathConstants.EPSILON,
				this.upperPoint.getY()-(float) MathConstants.EPSILON,
				this.upperPoint.getZ()-(float) MathConstants.EPSILON);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.lowerPoint.getX()-100.f,
				this.lowerPoint.getY()-100.f,
				this.lowerPoint.getZ()-100.f);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(-v.getX(), -v.getY(), -v.getZ(),
				this.lowerPoint.getX()-100.f,
				this.lowerPoint.getY()-100.f,
				this.lowerPoint.getZ()-100.f);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.upperPoint.getX()+100.f,
				this.upperPoint.getY()+100.f,
				this.upperPoint.getZ()+100.f);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(-v.getX(), -v.getY(), -v.getZ(),
				this.upperPoint.getX()+100.f,
				this.upperPoint.getY()+100.f,
				this.upperPoint.getZ()+100.f);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		
	}

	@Override
	public void testClassifiesPlane() {
		assertTrue(this.testingBounds.isInit());

		Plane p;
		
		p = new XYPlane(this.upperPoint.getZ() + 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new XYPlane(this.lowerPoint.getZ() - 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new XYPlane(this.centerPoint.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new XYPlane(this.lowerPoint.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new XYPlane(this.upperPoint.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));

		p = new XZPlane(this.upperPoint.getY() + 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new XZPlane(this.lowerPoint.getY() - 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new XZPlane(this.centerPoint.getY());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new XZPlane(this.lowerPoint.getY());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new XZPlane(this.upperPoint.getY());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));

		p = new YZPlane(this.upperPoint.getX() + 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new YZPlane(this.lowerPoint.getX() - 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new YZPlane(this.centerPoint.getX());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new YZPlane(this.lowerPoint.getX());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new YZPlane(this.upperPoint.getX());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));

		Vector3f v = new Vector3f(1.,1.,1.);
		
		p = new Plane4f(v, this.centerPoint);
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.lowerPoint.getX()+(float) MathConstants.EPSILON,
				this.lowerPoint.getY()+(float) MathConstants.EPSILON,
				this.lowerPoint.getZ()+(float) MathConstants.EPSILON);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.upperPoint.getX()-(float) MathConstants.EPSILON,
				this.upperPoint.getY()-(float) MathConstants.EPSILON,
				this.upperPoint.getZ()-(float) MathConstants.EPSILON);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.lowerPoint.getX()-100.f,
				this.lowerPoint.getY()-100.f,
				this.lowerPoint.getZ()-100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		

		p = new Plane4f(-v.getX(), -v.getY(), -v.getZ(),
				this.lowerPoint.getX()-100.f,
				this.lowerPoint.getY()-100.f,
				this.lowerPoint.getZ()-100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.upperPoint.getX()+100.f,
				this.upperPoint.getY()+100.f,
				this.upperPoint.getZ()+100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		

		p = new Plane4f(-v.getX(), -v.getY(), -v.getZ(),
				this.upperPoint.getX()+100.f,
				this.upperPoint.getY()+100.f,
				this.upperPoint.getZ()+100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
	}

	@Override
	public void testIntersectsPlane() {
		assertTrue(this.testingBounds.isInit());

		Plane p;
		
		p = new XYPlane(this.upperPoint.getZ() + 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new XYPlane(this.lowerPoint.getZ() - 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new XYPlane(this.centerPoint.getZ());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new XYPlane(this.lowerPoint.getZ());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new XYPlane(this.upperPoint.getZ());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));

		p = new XZPlane(this.upperPoint.getY() + 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new XZPlane(this.lowerPoint.getY() - 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new XZPlane(this.centerPoint.getY());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new XZPlane(this.lowerPoint.getY());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new XZPlane(this.upperPoint.getY());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));

		p = new YZPlane(this.upperPoint.getX() + 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new YZPlane(this.lowerPoint.getX() - 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new YZPlane(this.centerPoint.getX());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new YZPlane(this.lowerPoint.getX());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new YZPlane(this.upperPoint.getX());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));

		Vector3f v = new Vector3f(1.,1.,1.);
		
		p = new Plane4f(v, this.centerPoint);
		assertTrue(this.testingBounds.intersects(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.lowerPoint.getX()+(float) MathConstants.EPSILON,
				this.lowerPoint.getY()+(float) MathConstants.EPSILON,
				this.lowerPoint.getZ()+(float) MathConstants.EPSILON);
		assertFalse(this.testingBounds.intersects(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.upperPoint.getX()-(float) MathConstants.EPSILON,
				this.upperPoint.getY()-(float) MathConstants.EPSILON,
				this.upperPoint.getZ()-(float) MathConstants.EPSILON);
		assertFalse(this.testingBounds.intersects(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.lowerPoint.getX()-100.f,
				this.lowerPoint.getY()-100.f,
				this.lowerPoint.getZ()-100.f);
		assertFalse(this.testingBounds.intersects(p));		

		p = new Plane4f(-v.getX(), -v.getY(), -v.getZ(),
				this.lowerPoint.getX()-100.f,
				this.lowerPoint.getY()-100.f,
				this.lowerPoint.getZ()-100.f);
		assertFalse(this.testingBounds.intersects(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.upperPoint.getX()+100.f,
				this.upperPoint.getY()+100.f,
				this.upperPoint.getZ()+100.f);
		assertFalse(this.testingBounds.intersects(p));		

		p = new Plane4f(-v.getX(), -v.getY(), -v.getZ(),
				this.upperPoint.getX()+100.f,
				this.upperPoint.getY()+100.f,
				this.upperPoint.getZ()+100.f);
		assertFalse(this.testingBounds.intersects(p));		
	}

	@Override
	public void testClassifiesPoint3fVector3fArrayFloatArray() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f center = new Point3f();
		Vector3f[] axis = new Vector3f[] {
				new Vector3f(-1.,1.,0.),
				new Vector3f(-1.,-1.,0.),
				new Vector3f(0.,0.,1.)
		};
		for(int i=0; i<3; ++i) axis[i].normalize();
		float[] extent = new float[3];
		
		float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
		float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
		
		// A inside B
		center.setX(this.centerPoint.getX());
		center.setY(this.centerPoint.getY());
		center.setZ(this.centerPoint.getZ());
		extent[0] = maxSize * 10.f;
		extent[1] = maxSize * 10.f;
		extent[2] = maxSize * 10.f;
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(center, axis, extent));

		// A is enclosing B
		center.setX(this.centerPoint.getX());
		center.setY(this.centerPoint.getY());
		center.setZ(this.centerPoint.getZ());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		extent[2] = minSize / 10.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(center, axis, extent));

		// A and B do not intersect
		center.setX(this.lowerPoint.getX() - 1000.f);
		center.setY(this.centerPoint.getY());
		center.setZ(this.centerPoint.getZ());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		extent[2] = minSize / 10.f;
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(center, axis, extent));

		// A has one corner in B
		center.setX(this.lowerPoint.getX() - 1.f);
		center.setY(this.centerPoint.getY());
		center.setZ(this.centerPoint.getZ());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		extent[2] = minSize / 10.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, axis, extent));

		// A has at least one corners in B
		center.setX(this.lowerPoint.getX());
		center.setY(this.lowerPoint.getY());
		center.setZ(this.lowerPoint.getZ());
		extent[0] = maxSize;
		extent[1] = maxSize;
		extent[2] = maxSize;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, axis, extent));
	}

	@Override
	public void testIntersectsPoint3fVector3fArrayFloatArray() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f center = new Point3f();
		Vector3f[] axis = new Vector3f[] {
				new Vector3f(-1.,1.,0.),
				new Vector3f(-1.,-1.,0.),
				new Vector3f(0.,0.,1.)
		};
		for(int i=0; i<3; ++i) axis[i].normalize();
		float[] extent = new float[3];
		
		float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
		float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
		
		// A inside B
		center.setX(this.centerPoint.getX());
		center.setY(this.centerPoint.getY());
		center.setZ(this.centerPoint.getZ());
		extent[0] = maxSize * 10.f;
		extent[1] = maxSize * 10.f;
		extent[2] = maxSize * 10.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		// A is enclosing B
		center.setX(this.centerPoint.getX());
		center.setY(this.centerPoint.getY());
		center.setZ(this.centerPoint.getZ());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		extent[2] = minSize / 10.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		// A and B do not intersect
		center.setX(this.lowerPoint.getX() - 1000.f);
		center.setY(this.centerPoint.getY());
		center.setZ(this.centerPoint.getZ());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		extent[2] = minSize / 10.f;
		assertFalse(this.testingBounds.intersects(center, axis, extent));

		// A has one corner in B
		center.setX(this.lowerPoint.getX() - 1.f);
		center.setY(this.centerPoint.getY());
		center.setZ(this.centerPoint.getZ());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		extent[2] = minSize / 10.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		// A has at least one corners in B
		center.setX(this.lowerPoint.getX());
		center.setY(this.lowerPoint.getY());
		center.setZ(this.lowerPoint.getZ());
		extent[0] = maxSize;
		extent[1] = maxSize;
		extent[2] = maxSize;
		assertTrue(this.testingBounds.intersects(center, axis, extent));
	}

	@Override
	public void testClassifiesIC() {
		assertTrue(this.testingBounds.isInit());

		// AlignedBoundingBox
		{
			Point3f lower = new Point3f();
			Point3f upper = new Point3f();
			
			// A is enclosing B
			lower.setX(this.centerPoint.getX()-1.f);
			lower.setY(this.centerPoint.getY()-1.f);
			lower.setZ(this.centerPoint.getZ()-1.f);
			upper.setX(this.centerPoint.getX()+1.f);
			upper.setY(this.centerPoint.getY()+1.f);
			upper.setZ(this.centerPoint.getZ()+1.f);
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new AlignedBoundingBox(lower, upper)));

			// A is inside B
			lower.setX(this.lowerPoint.getX()-10.f);
			lower.setY(this.lowerPoint.getY()-10.f);
			lower.setZ(this.lowerPoint.getZ()-10.f);
			upper.setX(this.upperPoint.getX()+10.f);
			upper.setY(this.upperPoint.getY()+10.f);
			upper.setZ(this.upperPoint.getZ()+10.f);
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new AlignedBoundingBox(lower, upper)));

			// A is outside B
			lower.setX(this.lowerPoint.getX()-100.f);
			lower.setY(this.lowerPoint.getY());
			lower.setZ(this.lowerPoint.getZ());
			upper.setX(this.lowerPoint.getX()-10.f);
			upper.setY(this.upperPoint.getY());
			upper.setZ(this.upperPoint.getZ());
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new AlignedBoundingBox(lower, upper)));
		}
		
		// BoundingSphere
		{
			Point3f center = new Point3f();
			float radius;
			
			// A is inside B
			center.setX(this.testingBounds.getCenter().getX());
			center.setY(this.testingBounds.getCenter().getY());
			center.setZ(this.testingBounds.getCenter().getZ());
			radius = this.referenceRadius * 10.f;
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new BoundingSphere(center, radius)));
			
			// A is enclosing B
			center.setX(this.testingBounds.getCenter().getX());
			center.setY(this.testingBounds.getCenter().getY());
			center.setZ(this.testingBounds.getCenter().getZ());
			radius = this.referenceRadius / 10.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingSphere(center, radius)));
			
			// A is enclosing B
			center.setX(this.testingBounds.getCenter().getX() - this.referenceRadius / 20.f);
			center.setY(this.testingBounds.getCenter().getY());
			center.setZ(this.testingBounds.getCenter().getZ());
			radius = this.referenceRadius / 10.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingSphere(center, radius)));

			// A and B do not overlap
			center.setX(this.lowerPoint.getX() - 100.f);
			center.setY(this.testingBounds.getCenter().getY());
			center.setZ(this.testingBounds.getCenter().getZ());
			radius = 10.f;
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new BoundingSphere(center, radius)));
		}
		
		// OrientedBoundigBox
		{
			assertTrue(this.testingBounds.isInit());
			
			Point3f center = new Point3f();
			Vector3f[] axis = new Vector3f[] {
					new Vector3f(-1.,1.,0.),
					new Vector3f(-1.,-1.,0.),
					new Vector3f(0.,0.,1.)
			};
			for(int i=0; i<3; ++i) axis[i].normalize();
			float[] extent = new float[3];
			
			float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
			float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
			
			// A inside B
			center.setX(this.centerPoint.getX());
			center.setY(this.centerPoint.getY());
			center.setZ(this.centerPoint.getZ());
			extent[0] = maxSize * 10.f;
			extent[1] = maxSize * 10.f;
			extent[2] = maxSize * 10.f;
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new OrientedBoundingBox(center, axis, extent)));

			// A and B do not intersect
			center.setX(this.lowerPoint.getX() - 1000.f);
			center.setY(this.centerPoint.getY());
			center.setZ(this.centerPoint.getZ());
			extent[0] = minSize / 10.f;
			extent[1] = minSize / 10.f;
			extent[2] = minSize / 10.f;
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new OrientedBoundingBox(center, axis, extent)));
		}
	}

	@Override
	public void testIntersectsIC() {
		assertTrue(this.testingBounds.isInit());

		// AlignedBoundingBox
		{
			Point3f lower = new Point3f();
			Point3f upper = new Point3f();
			
			// A is enclosing B
			lower.setX(this.centerPoint.getX()-1.f);
			lower.setY(this.centerPoint.getY()-1.f);
			lower.setZ(this.centerPoint.getZ()-1.f);
			upper.setX(this.centerPoint.getX()+1.f);
			upper.setY(this.centerPoint.getY()+1.f);
			upper.setZ(this.centerPoint.getZ()+1.f);
			assertTrue(this.testingBounds.intersects(new AlignedBoundingBox(lower, upper)));

			// A is inside B
			lower.setX(this.lowerPoint.getX()-this.referenceRadius);
			lower.setY(this.lowerPoint.getY()-this.referenceRadius);
			lower.setZ(this.lowerPoint.getZ()-this.referenceRadius);
			upper.setX(this.upperPoint.getX()+this.referenceRadius);
			upper.setY(this.upperPoint.getY()+this.referenceRadius);
			upper.setZ(this.upperPoint.getZ()+this.referenceRadius);
			assertTrue(this.testingBounds.intersects(new AlignedBoundingBox(lower, upper)));

			// A is outside B
			lower.setX(this.lowerPoint.getX()-100.f);
			lower.setY(this.lowerPoint.getY());
			lower.setZ(this.lowerPoint.getZ());
			upper.setX(this.lowerPoint.getX()-10.f);
			upper.setY(this.upperPoint.getY());
			upper.setZ(this.upperPoint.getZ());
			assertFalse(this.testingBounds.intersects(new AlignedBoundingBox(lower, upper)));
		}
		
		// BoundingSphere
		{
			Point3f center = new Point3f();
			float radius;
			
			// A is inside B
			center.setX(this.testingBounds.getCenter().getX());
			center.setY(this.testingBounds.getCenter().getY());
			center.setZ(this.testingBounds.getCenter().getZ());
			radius = this.referenceRadius * 10.f;
			assertTrue(this.testingBounds.intersects(new BoundingSphere(center, radius)));
			
			// A is enclosing B
			center.setX(this.testingBounds.getCenter().getX());
			center.setY(this.testingBounds.getCenter().getY());
			center.setZ(this.testingBounds.getCenter().getZ());
			radius = this.referenceRadius / 10.f;
			assertTrue(this.testingBounds.intersects(new BoundingSphere(center, radius)));
			
			// A is enclosing B
			center.setX(this.testingBounds.getCenter().getX() - this.referenceRadius / 20.f);
			center.setY(this.testingBounds.getCenter().getY());
			center.setZ(this.testingBounds.getCenter().getZ());
			radius = this.referenceRadius / 10.f;
			assertTrue(this.testingBounds.intersects(new BoundingSphere(center, radius)));

			// A and B do not overlap
			center.setX(this.lowerPoint.getX() - 100.f);
			center.setY(this.testingBounds.getCenter().getY());
			center.setZ(this.testingBounds.getCenter().getZ());
			radius = 10.f;
			assertFalse(this.testingBounds.intersects(new BoundingSphere(center, radius)));
		}
		
		// OrientedBoundingBox
		{
			Point3f center = new Point3f();
			Vector3f[] axis = new Vector3f[] {
					new Vector3f(-1.,1.,0.),
					new Vector3f(-1.,-1.,0.),
					new Vector3f(0.,0.,1.)
			};
			for(int i=0; i<3; ++i) axis[i].normalize();
			float[] extent = new float[3];
			
			float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
			float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
			
			// A inside B
			center.setX(this.centerPoint.getX());
			center.setY(this.centerPoint.getY());
			center.setZ(this.centerPoint.getZ());
			extent[0] = maxSize * 10.f;
			extent[1] = maxSize * 10.f;
			extent[2] = maxSize * 10.f;
			assertTrue(this.testingBounds.intersects(new OrientedBoundingBox(center, axis, extent)));

			// A is enclosing B
			center.setX(this.centerPoint.getX());
			center.setY(this.centerPoint.getY());
			center.setZ(this.centerPoint.getZ());
			extent[0] = minSize / 10.f;
			extent[1] = minSize / 10.f;
			extent[2] = minSize / 10.f;
			assertTrue(this.testingBounds.intersects(new OrientedBoundingBox(center, axis, extent)));

			// A and B do not intersect
			center.setX(this.lowerPoint.getX() - 1000.f);
			center.setY(this.centerPoint.getY());
			center.setZ(this.centerPoint.getZ());
			extent[0] = minSize / 10.f;
			extent[1] = minSize / 10.f;
			extent[2] = minSize / 10.f;
			assertFalse(this.testingBounds.intersects(new OrientedBoundingBox(center, axis, extent)));
		}
	}

	/**
	 */
	@Override
	public void testToBounds2D() {
		assertTrue(this.testingBounds.isInit());

		BoundingCircle circle = this.testingBounds.toBounds2D();
		
		assertNotNull(circle);
		assertEpsilonEquals(new Point2f(this.centerPoint.getX(), this.centerPoint.getY()), circle.getCenter());
		assertEpsilonEquals(this.referenceRadius, circle.getRadius());
	}
	
	/**
	 */
	@Override
	public void testToBounds2DCoordinateSystem3D() {
		assertTrue(this.testingBounds.isInit());

		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			BoundingCircle circle = this.testingBounds.toBounds2D(cs);
			
			Point2f p = cs.toCoordinateSystem2D(this.centerPoint);
			
			assertNotNull(circle);
			assertEpsilonEquals(p, circle.getCenter());
			assertEpsilonEquals(this.referenceRadius, circle.getRadius());
		}
	}

	/**
	 */
	@Override
	public void testRotateAxisAngle4f() {
		assertTrue(this.testingBounds.isInit());

		AxisAngle4f a = randomAxisAngle4f();
		
		this.testingBounds.rotate(a);
		
		assertEpsilonEquals(this.centerPoint, this.testingBounds.getCenter());
		assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
	}

	/**
	 */
	@Override
	public void testRotateQuaternion() {
		assertTrue(this.testingBounds.isInit());

		Quaternion a = randomQuaternion();
		
		this.testingBounds.rotate(a);
		
		assertEpsilonEquals(this.centerPoint, this.testingBounds.getCenter());
		assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
	}

	/**
	 */
	@Override
	public void testSetRotationAxisAngle4f() {
		assertTrue(this.testingBounds.isInit());

		AxisAngle4f a = randomAxisAngle4f();
		
		this.testingBounds.setRotation(a);
		
		assertEpsilonEquals(this.centerPoint, this.testingBounds.getCenter());
		assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
	}

	/**
	 */
	@Override
	public void testSetRotationQuaternion() {
		assertTrue(this.testingBounds.isInit());

		Quaternion a = randomQuaternion();
		
		this.testingBounds.setRotation(a);
		
		assertEpsilonEquals(this.centerPoint, this.testingBounds.getCenter());
		assertEpsilonEquals(this.referenceRadius, this.testingBounds.getRadius());
	}

}
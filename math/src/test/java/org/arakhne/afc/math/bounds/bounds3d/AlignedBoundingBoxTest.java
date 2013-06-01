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
package org.arakhne.afc.math.bounds.bounds3d;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.bounds.bounds2f.MinimumBoundingRectangle;
import org.arakhne.afc.math.bounds.bounds3f.AlignedBoundingBox;
import org.arakhne.afc.math.bounds.bounds3f.BoundingSphere;
import org.arakhne.afc.math.bounds.bounds3f.CombinableBounds3D;
import org.arakhne.afc.math.bounds.bounds3f.OrientedBoundingBox;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.intersection.IntersectionType;
import org.arakhne.afc.math.plane.PlanarClassificationType;
import org.arakhne.afc.math.plane.Plane;
import org.arakhne.afc.math.plane.Plane4f;
import org.arakhne.afc.math.plane.XYPlane;
import org.arakhne.afc.math.plane.XZPlane;
import org.arakhne.afc.math.plane.YZPlane;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.vmutil.Resources;


/**
 * Unit test for AlignedBoundingBox class.
 * 
 * @author $Author: sgalland$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AlignedBoundingBoxTest extends AbstractOrientedBound3DTest {

	/** Reference instance.
	 */
	private AlignedBoundingBox testingBounds;
	
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
		computeLowerUpper(this.testingPoints,this.lowerPoint,this.upperPoint);
		this.centerPoint = new Point3f(
				(this.lowerPoint.getX()+this.upperPoint.getX())/2.f,
				(this.lowerPoint.getY()+this.upperPoint.getY())/2.f,
				(this.lowerPoint.getZ()+this.upperPoint.getZ())/2.);
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
	protected AlignedBoundingBox createBounds(Tuple3f[] points) {
		AlignedBoundingBox b = new AlignedBoundingBox();
		b.set(points);
		return b;
	}

	/** Compute the lower, upper and center points.
	 * @param points
	 * @param lower
	 * @param upper
	 */
	private static void computeLowerUpper(Tuple3f[] points, Tuple3f lower, Tuple3f upper) {
		float lx = Float.POSITIVE_INFINITY;
		float ly = Float.POSITIVE_INFINITY;
		float lz = Float.POSITIVE_INFINITY;
		float ux = Float.NEGATIVE_INFINITY;
		float uy = Float.NEGATIVE_INFINITY;
		float uz = Float.NEGATIVE_INFINITY;
		
		for(Tuple3f tuple : points) {
			if (tuple.getX()<lx) lx = tuple.getX();
			if (tuple.getY()<ly) ly = tuple.getY();
			if (tuple.getZ()<lz) lz = tuple.getZ();
			if (tuple.getX()>ux) ux = tuple.getX();
			if (tuple.getY()>uy) uy = tuple.getY();
			if (tuple.getZ()>uz) uz = tuple.getZ();
		}
		
		if (lower!=null) lower.set(lx,ly,lz);
		if (upper!=null) upper.set(ux,uy,uz);
	}

	@Override
	public void testGetMathematicalDimension() {
		assertTrue(this.testingBounds.isInit());
		
		assertEquals(PseudoHamelDimension.DIMENSION_3D, this.testingBounds.getMathematicalDimension());
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
	public void testGetCenterZ() {
		assertTrue(this.testingBounds.isInit());

		assertEpsilonEquals((this.lowerPoint.getZ()+this.upperPoint.getZ())/2.f, this.testingBounds.getCenterZ());
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

		Point3f p;
		float dist;
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(100,100,100));
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(p.distance(this.upperPoint), dist);
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(0,0,100));
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(p.distance(this.upperPoint), dist);

		p = new Point3f(this.upperPoint);
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(p.distance(this.lowerPoint), dist);

		p = new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ());
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(
				p.distance(new Point3f(this.lowerPoint.getX(), this.upperPoint.getY(), this.lowerPoint.getZ())), dist);

		p = new Point3f(this.centerPoint);
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(p.distance(this.upperPoint), dist);

		p = new Point3f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f, this.lowerPoint.getZ());
		dist = this.testingBounds.distanceMax(p);
		assertEpsilonEquals(
				p.distance(this.upperPoint), dist);
	}

	@Override
	public void testDistanceMaxSquaredP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p;
		float dist;
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(100,100,100));
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(p.distanceSquared(this.upperPoint), dist);
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(0,0,100));
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(p.distanceSquared(this.upperPoint), dist);

		p = new Point3f(this.upperPoint);
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(p.distanceSquared(this.lowerPoint), dist);

		p = new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ());
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(
				p.distanceSquared(new Point3f(this.lowerPoint.getX(), this.upperPoint.getY(), this.lowerPoint.getZ())), dist);

		p = new Point3f(this.centerPoint);
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(p.distanceSquared(this.upperPoint), dist);

		p = new Point3f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f, this.lowerPoint.getZ());
		dist = this.testingBounds.distanceMaxSquared(p);
		assertEpsilonEquals(
				p.distanceSquared(this.upperPoint), dist);
	}

	@Override
	public void testFarestPointP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p;
		Point3f far;
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(100,100,100));
		far = this.testingBounds.farestPoint(p);
		assertEpsilonEquals(this.upperPoint, far);
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(0,0,100));
		far = this.testingBounds.farestPoint(p);
		assertEpsilonEquals(this.upperPoint, far);

		p = new Point3f(this.upperPoint);
		far = this.testingBounds.farestPoint(p);
		assertEpsilonEquals(this.lowerPoint, far);

		p = new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ());
		far = this.testingBounds.farestPoint(p);
		assertEpsilonEquals(
				new Point3f(this.lowerPoint.getX(), this.upperPoint.getY(), this.lowerPoint.getZ()), far);
	}

	@Override
	public void testDistanceP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p;
		float dist;
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(100,100,100));
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(p.distance(this.lowerPoint), dist);
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(0,0,100));
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(p.distance(this.lowerPoint), dist);

		p = new Point3f(this.upperPoint);
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ());
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point3f();
		p.add(this.upperPoint, this.lowerPoint);
		p.scale(.5f);
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point3f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f, this.lowerPoint.getZ());
		dist = this.testingBounds.distance(p);
		assertEpsilonEquals(100, dist);
	}

	@Override
	public void testDistanceSquaredP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p;
		float dist;
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(100,100,100));
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(p.distanceSquared(this.lowerPoint), dist);
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(0,0,100));
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(p.distanceSquared(this.lowerPoint), dist);

		p = new Point3f(this.upperPoint);
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ());
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point3f();
		p.add(this.upperPoint, this.lowerPoint);
		p.scale(.5f);
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(0.f, dist);

		p = new Point3f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f, this.lowerPoint.getZ());
		dist = this.testingBounds.distanceSquared(p);
		assertEpsilonEquals(100.f*100.f, dist);
	}

	@Override
	public void testNearestPointP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p;
		Point3f near;
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(100,100,100));
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(this.lowerPoint, near);
		
		p = new Point3f(this.lowerPoint);
		p.sub(new Vector3f(0,0,100));
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(this.lowerPoint, near);

		p = new Point3f(this.upperPoint);
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(p, near);

		p = new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ());
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(p, near);

		p = new Point3f();
		p.add(this.upperPoint, this.lowerPoint);
		p.scale(.5f);
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(p, near);

		p = new Point3f(this.lowerPoint.getX()-100.f, (this.lowerPoint.getY()+this.upperPoint.getY())/2.f, this.lowerPoint.getZ());
		near = this.testingBounds.nearestPoint(p);
		assertEpsilonEquals(new Point3f(this.lowerPoint.getX(), (this.lowerPoint.getY()+this.upperPoint.getY())/2.f, this.lowerPoint.getZ()), near);
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

		Point3f c = this.testingBounds.getLower();
		assertEpsilonEquals(this.lowerPoint.getX(), c.getX());
		assertEpsilonEquals(this.lowerPoint.getY(), c.getY());
		assertEpsilonEquals(this.lowerPoint.getZ(), c.getZ());
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

		Point3f c = this.testingBounds.getUpper();
		assertEpsilonEquals(this.upperPoint.getX(), c.getX());
		assertEpsilonEquals(this.upperPoint.getY(), c.getY());
		assertEpsilonEquals(this.upperPoint.getZ(), c.getZ());
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

		AlignedBoundingBox clone = this.testingBounds.clone();
		assertNotSame(this.testingBounds, clone);
		assertNotSame(this.testingBounds.getLower(), clone.getLower());
		assertEquals(this.testingBounds.getLower(), clone.getLower());
		assertNotSame(this.testingBounds.getUpper(), clone.getUpper());
		assertEquals(this.testingBounds.getUpper(), clone.getUpper());
	}

	@Override
	public void testTranslateVector3f() {
		assertTrue(this.testingBounds.isInit());

		Vector3f v = randomVector3D();
		this.testingBounds.translate(v);
		assertEpsilonEquals(this.lowerPoint.getX()+v.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY()+v.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.lowerPoint.getZ()+v.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(this.upperPoint.getX()+v.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY()+v.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(this.upperPoint.getZ()+v.getZ(), this.testingBounds.getUpper().getZ());
	}

	private boolean epsilonRemove(Set<? extends Tuple3f> s, Tuple3f t) {
		Iterator<? extends Tuple3f> iterator = s.iterator();
		Tuple3f candidate;
		while (iterator.hasNext()) {
			candidate = iterator.next();
			if (isEpsilonEquals(candidate.getX(), t.getX()) && isEpsilonEquals(candidate.getY(), t.getY()) && isEpsilonEquals(candidate.getZ(), t.getZ())) {
				iterator.remove();
				return true;
			}
		}
		return false;
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
			assertTrue(i+"; point: "+p+"; inside: "+expectedPoints.toString(), epsilonRemove(expectedPoints,p));  //$NON-NLS-1$//$NON-NLS-2$
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
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.upperPoint.getY(), this.lowerPoint.getZ()));
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ()));
		expectedPoints.add(new Point3f(this.upperPoint.getX(), this.lowerPoint.getY(), this.lowerPoint.getZ()));
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.upperPoint.getY(), this.upperPoint.getZ()));
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.upperPoint.getY(), this.lowerPoint.getZ()));
		expectedPoints.add(new Point3f(this.lowerPoint.getX(), this.lowerPoint.getY(), this.upperPoint.getZ()));
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
		float sizex = (this.upperPoint.getX() - this.lowerPoint.getX())/2.f;
		float sizey = (this.upperPoint.getY() - this.lowerPoint.getY())/2.f;
		float sizez = (this.upperPoint.getZ() - this.lowerPoint.getZ())/2.f;
		
		expectedPoints.add(new Vector3f(sizex, sizey, sizez));
		expectedPoints.add(new Vector3f(sizex, sizey, -sizez));
		expectedPoints.add(new Vector3f(sizex, -sizey, sizez));
		expectedPoints.add(new Vector3f(sizex, -sizey, -sizez));
		expectedPoints.add(new Vector3f(-sizex, sizey, sizez));
		expectedPoints.add(new Vector3f(-sizex, sizey, -sizez));
		expectedPoints.add(new Vector3f(-sizex, -sizey, sizez));
		expectedPoints.add(new Vector3f(-sizex, -sizey, -sizez));
		
		SizedIterator<Vector3f> iterator = this.testingBounds.getLocalOrientedBoundVertices();
		assertNotNull(iterator);
		assertEquals(8, iterator.totalSize());
		
		for(int i=0; i<8; ++i) {
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

		List<Vector3f> expectedPoints = new ArrayList<Vector3f>();
		Vector3f v;
		float sizex = (this.upperPoint.getX() - this.lowerPoint.getX())/2.f;
		float sizey = (this.upperPoint.getY() - this.lowerPoint.getY())/2.f;
		float sizez = (this.upperPoint.getZ() - this.lowerPoint.getZ())/2.f;
		
		expectedPoints.add(new Vector3f(sizex, sizey, sizez));
		expectedPoints.add(new Vector3f(-sizex, sizey, sizez));
		expectedPoints.add(new Vector3f(-sizex, -sizey, sizez));
		expectedPoints.add(new Vector3f(sizex, -sizey, sizez));
		expectedPoints.add(new Vector3f(sizex, -sizey, -sizez));
		expectedPoints.add(new Vector3f(sizex, sizey, -sizez));
		expectedPoints.add(new Vector3f(-sizex, sizey, -sizez));
		expectedPoints.add(new Vector3f(-sizex, -sizey, -sizez));
		
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
		assertEpsilonEquals(new Vector3f(1.,0.f,0.f), axis[0]);
		assertEpsilonEquals(new Vector3f(0.f,1.,0.f), axis[1]);
		assertEpsilonEquals(new Vector3f(0.f,0.f,1.), axis[2]);
	}

	/**
	 */
	@Override
	public void testGetOrientedBoundExtents() {
		float sx = (this.upperPoint.getX() - this.lowerPoint.getX()) / 2.f;
		float sy = (this.upperPoint.getY() - this.lowerPoint.getY()) / 2.f;		
		float sz = (this.upperPoint.getZ() - this.lowerPoint.getZ()) / 2.f;
		Vector3f extent = this.testingBounds.getOrientedBoundExtentVector();
		assertEpsilonEquals(new Vector3f(sx,sy,sz), extent);
	}

	/**
	 */
	@Override
	public void testGetR() {
		Vector3f axis = this.testingBounds.getR();
		assertEpsilonEquals(new Vector3f(1.,0.f,0.f), axis);
	}
	
	/**
	 */
	@Override
	public void testGetS() {
		Vector3f axis = this.testingBounds.getS();
		assertEpsilonEquals(new Vector3f(0.f,1.,0.f), axis);
	}

	/**
	 */
	@Override
	public void testGetT() {
		Vector3f axis = this.testingBounds.getT();
		assertEpsilonEquals(new Vector3f(0.f,0.f,1.), axis);
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

	/**
	 */
	@Override
	public void testGetTExtent() {
		float expectedExtent = (this.upperPoint.getZ() - this.lowerPoint.getZ()) / 2.f;
		float extent = this.testingBounds.getTExtent();
		assertEpsilonEquals(expectedExtent, extent);
	}

	@Override
	public void testSetP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p = randomPoint3D();
		this.testingBounds.set(p);
		assertEpsilonEquals(p.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(p.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(p.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(p.getZ(), this.testingBounds.getUpper().getZ());
	}

	@Override
	public void testSetCB() {
		assertTrue(this.testingBounds.isInit());

		AlignedBoundingBox box = new AlignedBoundingBox();
		Point3f p = randomPoint3D();
		box.set(p);
		this.testingBounds.set(box);
		assertEpsilonEquals(p.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(p.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(p.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(p.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(p.getZ(), this.testingBounds.getUpper().getZ());

		BoundingSphere sphere = new BoundingSphere();
		p = randomPoint3D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);
		this.testingBounds.set(sphere);
		assertEpsilonEquals(p.getX()-r, this.testingBounds.getLower().getX());
		assertEpsilonEquals(p.getY()-r, this.testingBounds.getLower().getY());
		assertEpsilonEquals(p.getZ()-r, this.testingBounds.getLower().getZ());
		assertEpsilonEquals(p.getX()+r, this.testingBounds.getUpper().getX());
		assertEpsilonEquals(p.getY()+r, this.testingBounds.getUpper().getY());
		assertEpsilonEquals(p.getZ()+r, this.testingBounds.getUpper().getZ());
	}

	@Override
	public void testSetTuple3fArray() {
		assertTrue(this.testingBounds.isInit());

		Tuple3f[] points = randomTuples3D();
		Point3f l = new Point3f();
		Point3f u = new Point3f();
		computeLowerUpper(points, l, u);

		this.testingBounds.set(points);

		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(u.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(u.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(u.getZ(), this.testingBounds.getUpper().getZ());
	}

	@Override
	public void testCombineP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p = randomPoint3D();
		this.testingBounds.combine(p);
		assertEpsilonEquals(Math.min(p.getX(),this.lowerPoint.getX()), this.testingBounds.getLower().getX());
		assertEpsilonEquals(Math.min(p.getY(),this.lowerPoint.getY()), this.testingBounds.getLower().getY());
		assertEpsilonEquals(Math.min(p.getZ(),this.lowerPoint.getZ()), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(Math.max(p.getX(),this.upperPoint.getX()), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(Math.max(p.getY(),this.upperPoint.getY()), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(Math.max(p.getZ(),this.upperPoint.getZ()), this.testingBounds.getUpper().getZ());
	}

	@Override
	public void testCombineCB() {
		assertTrue(this.testingBounds.isInit());

		Point3f l = new Point3f(this.lowerPoint);
		Point3f u = new Point3f(this.upperPoint);
		
		AlignedBoundingBox box = new AlignedBoundingBox();
		Point3f p = randomPoint3D();
		box.set(p);
		this.testingBounds.combine(box);
		l.setX(Math.min(p.getX(),l.getX()));
		l.setY(Math.min(p.getY(),l.getY()));
		l.setZ(Math.min(p.getZ(),l.getZ()));
		u.setX(Math.max(p.getX(),u.getX()));
		u.setY(Math.max(p.getY(),u.getY()));
		u.setZ(Math.max(p.getZ(),u.getZ()));
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(u.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(u.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(u.getZ(), this.testingBounds.getUpper().getZ());

		BoundingSphere sphere = new BoundingSphere();
		p = randomPoint3D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);
		this.testingBounds.combine(sphere);
		l.setX(Math.min(p.getX()-r,l.getX()));
		l.setY(Math.min(p.getY()-r,l.getY()));
		l.setZ(Math.min(p.getZ()-r,l.getZ()));
		u.setX(Math.max(p.getX()+r,u.getX()));
		u.setY(Math.max(p.getY()+r,u.getY()));
		u.setZ(Math.max(p.getZ()+r,u.getZ()));
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(u.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(u.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(u.getZ(), this.testingBounds.getUpper().getZ());
	}

	@Override
	public void testCombineTuple3fArray() {
		assertTrue(this.testingBounds.isInit());

		Tuple3f[] points = randomTuples3D();
		Point3f l = new Point3f();
		Point3f u = new Point3f();
		computeLowerUpper(points, l, u);

		this.testingBounds.combine(points);

		assertEpsilonEquals(Math.min(l.getX(),this.lowerPoint.getX()), this.testingBounds.getLower().getX());
		assertEpsilonEquals(Math.min(l.getY(),this.lowerPoint.getY()), this.testingBounds.getLower().getY());
		assertEpsilonEquals(Math.min(l.getZ(),this.lowerPoint.getZ()), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(Math.max(u.getX(),this.upperPoint.getX()), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(Math.max(u.getY(),this.upperPoint.getY()), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(Math.max(u.getZ(),this.upperPoint.getZ()), this.testingBounds.getUpper().getZ());
	}

	@Override
	public void testCombineCollection() {
		assertTrue(this.testingBounds.isInit());

		Point3f l = new Point3f(this.lowerPoint);
		Point3f u = new Point3f(this.upperPoint);

		Collection<CombinableBounds3D> collection = new ArrayList<CombinableBounds3D>();

		
		AlignedBoundingBox box = new AlignedBoundingBox();
		Point3f p = randomPoint3D();
		box.set(p);
		collection.add(box);
		l.setX(Math.min(p.getX(),l.getX()));
		l.setY(Math.min(p.getY(),l.getY()));
		l.setZ(Math.min(p.getZ(),l.getZ()));
		u.setX(Math.max(p.getX(),u.getX()));
		u.setY(Math.max(p.getY(),u.getY()));
		u.setZ(Math.max(p.getZ(),u.getZ()));

		BoundingSphere sphere = new BoundingSphere();
		p = randomPoint3D();
		float r = this.RANDOM.nextFloat() * 100.f;
		sphere.set(p, r);
		collection.add(sphere);
		l.setX(Math.min(p.getX()-r,l.getX()));
		l.setY(Math.min(p.getY()-r,l.getY()));
		l.setZ(Math.min(p.getZ()-r,l.getZ()));
		u.setX(Math.max(p.getX()+r,u.getX()));
		u.setY(Math.max(p.getY()+r,u.getY()));
		u.setZ(Math.max(p.getZ()+r,u.getZ()));
		
		this.testingBounds.combine(collection);
		
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(u.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(u.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(u.getZ(), this.testingBounds.getUpper().getZ());
	}
	
	/**
	 */
	public void testGetSize() {
		assertTrue(this.testingBounds.isInit());

		Vector3f size = this.testingBounds.getSize();
		assertEpsilonEquals(this.upperPoint.getX() - this.lowerPoint.getX(), size.getX());
		assertEpsilonEquals(this.upperPoint.getY() - this.lowerPoint.getY(), size.getY());
		assertEpsilonEquals(this.upperPoint.getZ() - this.lowerPoint.getZ(), size.getZ());
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

	/**
	 */
	public void testGetSizeZ() {
		assertTrue(this.testingBounds.isInit());

		float size = this.testingBounds.getSizeZ();
		assertEpsilonEquals(this.upperPoint.getZ() - this.lowerPoint.getZ(), size);
	}

	//-------------------------------------------
	// BoundingBox dedicated tests
	//-------------------------------------------

	/**
	 */
	public void testSetLowerTuple3f() {
		assertTrue(this.testingBounds.isInit());

		Vector3f v = new Vector3f(
				-this.RANDOM.nextFloat()*100.f,
				-this.RANDOM.nextFloat()*100.f,
				-this.RANDOM.nextFloat()*100.f);
		Point3f l = new Point3f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l);
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(this.upperPoint.getZ(), this.testingBounds.getUpper().getZ());

		v = new Vector3f(
				(this.upperPoint.getX() - this.lowerPoint.getX())/2.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())/2.f,
				(this.upperPoint.getZ() - this.lowerPoint.getZ())/2.);
		l = new Point3f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l);
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(this.upperPoint.getZ(), this.testingBounds.getUpper().getZ());

		v = new Vector3f(
				(this.upperPoint.getX() - this.lowerPoint.getX())+1.,
				(this.upperPoint.getY() - this.lowerPoint.getY())+2.f,
				(this.upperPoint.getZ() - this.lowerPoint.getZ())+3.);
		l = new Point3f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l);
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getUpper().getZ());
	}

	/**
	 */
	public void testSetLowerFloatFloatFloat() {
		assertTrue(this.testingBounds.isInit());
		
		Vector3f v = new Vector3f(
				-this.RANDOM.nextFloat()*100.f,
				-this.RANDOM.nextFloat()*100.f,
				-this.RANDOM.nextFloat()*100.f);
		Point3f l = new Point3f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l.getX(), l.getY(), l.getZ());
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(this.upperPoint.getZ(), this.testingBounds.getUpper().getZ());

		v = new Vector3f(
				(this.upperPoint.getX() - this.lowerPoint.getX())/2.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())/2.f,
				(this.upperPoint.getZ() - this.lowerPoint.getZ())/2.);
		l = new Point3f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l.getX(), l.getY(), l.getZ());
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(this.upperPoint.getZ(), this.testingBounds.getUpper().getZ());

		v = new Vector3f(
				(this.upperPoint.getX() - this.lowerPoint.getX())+1.,
				(this.upperPoint.getY() - this.lowerPoint.getY())+2.f,
				(this.upperPoint.getZ() - this.lowerPoint.getZ())+3.);
		l = new Point3f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setLower(l.getX(), l.getY(), l.getZ());
		assertEpsilonEquals(this.upperPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.upperPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getUpper().getZ());
	}

	/**
	 */
	public void testSetUpperTuple3f() {
		assertTrue(this.testingBounds.isInit());

		Vector3f v = new Vector3f(
				this.RANDOM.nextFloat()*100.f,
				this.RANDOM.nextFloat()*100.f,
				this.RANDOM.nextFloat()*100.f);
		Point3f l = new Point3f(this.upperPoint);
		l.add(v);
		this.testingBounds.setUpper(l);
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.lowerPoint.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getUpper().getZ());

		v = new Vector3f(
				(this.upperPoint.getX() - this.lowerPoint.getX())/2.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())/2.f,
				(this.upperPoint.getZ() - this.lowerPoint.getZ())/2.);
		l = new Point3f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setUpper(l);
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.lowerPoint.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getUpper().getZ());

		v = new Vector3f(
				(this.upperPoint.getX() - this.lowerPoint.getX())+1.,
				(this.upperPoint.getY() - this.lowerPoint.getY())+2.f,
				(this.upperPoint.getZ() - this.lowerPoint.getZ())+3.);
		l = new Point3f(this.lowerPoint);
		l.sub(v);
		this.testingBounds.setUpper(l);
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(this.lowerPoint.getZ(), this.testingBounds.getUpper().getZ());
	}

	/**
	 */
	public void testSetUpperFloatFloatFloat() {
		assertTrue(this.testingBounds.isInit());

		Vector3f v = new Vector3f(
				this.RANDOM.nextFloat()*100.f,
				this.RANDOM.nextFloat()*100.f,
				this.RANDOM.nextFloat()*100.f);
		Point3f l = new Point3f(this.upperPoint);
		l.add(v);
		this.testingBounds.setUpper(l.getX(), l.getY(), l.getZ());
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.lowerPoint.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getUpper().getZ());

		v = new Vector3f(
				(this.upperPoint.getX() - this.lowerPoint.getX())/2.f,
				(this.upperPoint.getY() - this.lowerPoint.getY())/2.f,
				(this.upperPoint.getZ() - this.lowerPoint.getZ())/2.);
		l = new Point3f(this.lowerPoint);
		l.add(v);
		this.testingBounds.setUpper(l.getX(), l.getY(), l.getZ());
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(this.lowerPoint.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(l.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getUpper().getZ());

		v = new Vector3f(
				(this.upperPoint.getX() - this.lowerPoint.getX())+1.,
				(this.upperPoint.getY() - this.lowerPoint.getY())+2.f,
				(this.upperPoint.getZ() - this.lowerPoint.getZ())+3.);
		l = new Point3f(this.upperPoint);
		l.sub(v);
		this.testingBounds.setUpper(l.getX(), l.getY(), l.getZ());
		assertEpsilonEquals(l.getX(), this.testingBounds.getLower().getX());
		assertEpsilonEquals(l.getY(), this.testingBounds.getLower().getY());
		assertEpsilonEquals(l.getZ(), this.testingBounds.getLower().getZ());
		assertEpsilonEquals(this.lowerPoint.getX(), this.testingBounds.getUpper().getX());
		assertEpsilonEquals(this.lowerPoint.getY(), this.testingBounds.getUpper().getY());
		assertEpsilonEquals(this.lowerPoint.getZ(), this.testingBounds.getUpper().getZ());
	}

	/**
	 */
	public void testGetCenterTuple3f() {
		assertTrue(this.testingBounds.isInit());

		Point3f c = new Point3f();
		this.testingBounds.getCenter(c);
		assertEpsilonEquals((this.lowerPoint.getX()+this.upperPoint.getX())/2.f, c.getX());
		assertEpsilonEquals((this.lowerPoint.getY()+this.upperPoint.getY())/2.f, c.getY());
		assertEpsilonEquals((this.lowerPoint.getZ()+this.upperPoint.getZ())/2.f, c.getZ());
	}

	/**
	 */
	public void testGetLowerTuple3f() {
		assertTrue(this.testingBounds.isInit());

		Point3f l = new Point3f();
		this.testingBounds.getLower(l);
		assertEpsilonEquals(this.lowerPoint.getX(), l.getX());
		assertEpsilonEquals(this.lowerPoint.getY(), l.getY());
		assertEpsilonEquals(this.lowerPoint.getZ(), l.getZ());
	}

	/**
	 */
	public void testGetUpperTuple3f() {
		assertTrue(this.testingBounds.isInit());

		Point3f u = new Point3f();
		this.testingBounds.getUpper(u);
		assertEpsilonEquals(this.upperPoint.getX(), u.getX());
		assertEpsilonEquals(this.upperPoint.getY(), u.getY());
		assertEpsilonEquals(this.upperPoint.getZ(), u.getZ());
	}

	/** 
	 */
	public void testGetNorthWestFrontVoxel() {
		assertTrue(this.testingBounds.isInit());

		AlignedBoundingBox voxel = this.testingBounds.getNorthWestFrontVoxel();
		
		Point3f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(this.lowerPoint.getX(), voxel.getLower().getX());
		assertEpsilonEquals(center.getX(), voxel.getUpper().getX());

		assertEpsilonEquals(center.getY(), voxel.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getY(), voxel.getUpper().getY());
		
		assertEpsilonEquals(center.getZ(), voxel.getLower().getZ());
		assertEpsilonEquals(this.upperPoint.getZ(), voxel.getUpper().getZ());
	}
	
	/** 
	 */
	public void testGetNorthWestBackVoxel() {
		assertTrue(this.testingBounds.isInit());
		
		AlignedBoundingBox voxel = this.testingBounds.getNorthWestBackVoxel();
		
		Point3f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(center.getX(), voxel.getLower().getX());
		assertEpsilonEquals(this.upperPoint.getX(), voxel.getUpper().getX());

		assertEpsilonEquals(center.getY(), voxel.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getY(), voxel.getUpper().getY());
		
		assertEpsilonEquals(center.getZ(), voxel.getLower().getZ());
		assertEpsilonEquals(this.upperPoint.getZ(), voxel.getUpper().getZ());
	}

	/** 
	 */
	public void testGetNorthEastFrontVoxel() {
		assertTrue(this.testingBounds.isInit());

		AlignedBoundingBox voxel = this.testingBounds.getNorthEastFrontVoxel();
		
		Point3f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(this.lowerPoint.getX(), voxel.getLower().getX());
		assertEpsilonEquals(center.getX(), voxel.getUpper().getX());

		assertEpsilonEquals(this.lowerPoint.getY(), voxel.getLower().getY());
		assertEpsilonEquals(center.getY(), voxel.getUpper().getY());
		
		assertEpsilonEquals(center.getZ(), voxel.getLower().getZ());
		assertEpsilonEquals(this.upperPoint.getZ(), voxel.getUpper().getZ());
	}

	/** 
	 */
	public void testGetNorthEastBackVoxel() {
		assertTrue(this.testingBounds.isInit());
		
		AlignedBoundingBox voxel = this.testingBounds.getNorthEastBackVoxel();
		
		Point3f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(center.getX(), voxel.getLower().getX());
		assertEpsilonEquals(this.upperPoint.getX(), voxel.getUpper().getX());

		assertEpsilonEquals(this.lowerPoint.getY(), voxel.getLower().getY());
		assertEpsilonEquals(center.getY(), voxel.getUpper().getY());
		
		assertEpsilonEquals(center.getZ(), voxel.getLower().getZ());
		assertEpsilonEquals(this.upperPoint.getZ(), voxel.getUpper().getZ());
	}

	/** 
	 */
	public void testGetSouthWestFrontVoxel() {
		assertTrue(this.testingBounds.isInit());

		AlignedBoundingBox voxel = this.testingBounds.getSouthWestFrontVoxel();
		
		Point3f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(this.lowerPoint.getX(), voxel.getLower().getX());
		assertEpsilonEquals(center.getX(), voxel.getUpper().getX());

		assertEpsilonEquals(center.getY(), voxel.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getY(), voxel.getUpper().getY());
		
		assertEpsilonEquals(this.lowerPoint.getZ(), voxel.getLower().getZ());
		assertEpsilonEquals(center.getZ(), voxel.getUpper().getZ());
	}
	
	/** 
	 */
	public void testGetSouthWestBackVoxel() {
		assertTrue(this.testingBounds.isInit());

		AlignedBoundingBox voxel = this.testingBounds.getSouthWestBackVoxel();
		
		Point3f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(center.getX(), voxel.getLower().getX());
		assertEpsilonEquals(this.upperPoint.getX(), voxel.getUpper().getX());

		assertEpsilonEquals(center.getY(), voxel.getLower().getY());
		assertEpsilonEquals(this.upperPoint.getY(), voxel.getUpper().getY());
		
		assertEpsilonEquals(this.lowerPoint.getZ(), voxel.getLower().getZ());
		assertEpsilonEquals(center.getZ(), voxel.getUpper().getZ());
	}

	/** 
	 */
	public void testGetSouthEastFrontVoxel() {
		assertTrue(this.testingBounds.isInit());

		AlignedBoundingBox voxel = this.testingBounds.getSouthEastFrontVoxel();
		
		Point3f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(this.lowerPoint.getX(), voxel.getLower().getX());
		assertEpsilonEquals(center.getX(), voxel.getUpper().getX());

		assertEpsilonEquals(this.lowerPoint.getY(), voxel.getLower().getY());
		assertEpsilonEquals(center.getY(), voxel.getUpper().getY());
		
		assertEpsilonEquals(this.lowerPoint.getZ(), voxel.getLower().getZ());
		assertEpsilonEquals(center.getZ(), voxel.getUpper().getZ());
	}

	/** 
	 */
	public void testGetSouthEastBackVoxel() {
		assertTrue(this.testingBounds.isInit());

		AlignedBoundingBox voxel = this.testingBounds.getSouthEastBackVoxel();
		
		Point3f center = this.testingBounds.getCenter();
		
		assertEpsilonEquals(center.getX(), voxel.getLower().getX());
		assertEpsilonEquals(this.upperPoint.getX(), voxel.getUpper().getX());

		assertEpsilonEquals(this.lowerPoint.getY(), voxel.getLower().getY());
		assertEpsilonEquals(center.getY(), voxel.getUpper().getY());
		
		assertEpsilonEquals(this.lowerPoint.getZ(), voxel.getLower().getZ());
		assertEpsilonEquals(center.getZ(), voxel.getUpper().getZ());
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
		
		Point3f p = new Point3f();
		
		p.set(this.lowerPoint.getX() - 100, this.lowerPoint.getY() - 100, this.lowerPoint.getZ() - 100);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
	}

	@Override
	public void testIntersectsP() {
		assertTrue(this.testingBounds.isInit());
		
		assertTrue(this.testingBounds.intersects(this.centerPoint));

		assertTrue(this.testingBounds.intersects(this.lowerPoint));
		assertTrue(this.testingBounds.intersects(this.upperPoint));
		
		Point3f p = new Point3f();
		
		p.set(this.lowerPoint.getX() - 100, this.lowerPoint.getY() - 100, this.lowerPoint.getZ() - 100);
		assertFalse(this.testingBounds.intersects(p));
	}

	@Override
	public void testClassifiesPP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p1 = new Point3f();
		Point3f p2 = new Point3f();
		
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
		p1.setZ(p1.getZ() - 100.f);
		p2.set(this.upperPoint);
		p2.setX(p2.getX() + 100.f);
		p2.setY(p2.getY() + 100.f);
		p2.setZ(p2.getZ() + 100.f);
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(p1,p2));

		// A OUTSIDE B
		p1.setX(this.upperPoint.getX() + 100.f);
		p1.setY(this.lowerPoint.getY());
		p1.setZ(this.lowerPoint.getZ());
		p2.setX(p1.getX() + 100.f);
		p2.setY(this.upperPoint.getY() + 100.f);
		p2.setZ(this.upperPoint.getZ());
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p1,p2));

		// A has a corner in B
		p1.setX(this.lowerPoint.getX() - 100.f);
		p1.setY(this.lowerPoint.getY() - 100.f);
		p1.setZ(this.lowerPoint.getZ() - 100.f);
		p2.setX(this.centerPoint.getX());
		p2.setY(this.centerPoint.getY());
		p2.setZ(this.centerPoint.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));

		// A has two corners in B
		p1.setX(this.centerPoint.getX());
		p1.setY(this.lowerPoint.getY() - 100.f);
		p1.setZ(this.centerPoint.getZ());
		p2.setX(this.upperPoint.getX() + 100);
		p2.setY(this.upperPoint.getY() + 100.f);
		p2.setZ(this.upperPoint.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));

		// A cross B
		p1.setX((this.lowerPoint.getX()+this.centerPoint.getX())/2.f);
		p1.setY(this.lowerPoint.getY() - 100.f);
		p1.setZ(this.lowerPoint.getZ());
		p2.setX((this.upperPoint.getX()+this.centerPoint.getX())/2.f);
		p2.setY(this.upperPoint.getY() + 100.f);
		p2.setZ(this.upperPoint.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));

		// A on a side of B
		p1.setX(this.upperPoint.getX());
		p1.setY(this.lowerPoint.getY());
		p1.setZ(this.lowerPoint.getZ());
		p2.setX(this.upperPoint.getX() + 100.f);
		p2.setY(this.upperPoint.getY());
		p2.setZ(this.upperPoint.getZ());
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p1,p2));
	}

	@Override
	public void testIntersectsPP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p1 = new Point3f();
		Point3f p2 = new Point3f();
		
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
		p1.setZ(p1.getZ() - 100.f);
		p2.set(this.upperPoint);
		p2.setX(p2.getX() + 100.f);
		p2.setY(p2.getY() + 100.f);
		p2.setZ(p2.getZ() + 100.f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		// A OUTSIDE B
		p1.setX(this.upperPoint.getX() + 100.f);
		p1.setY(this.lowerPoint.getY());
		p1.setZ(this.lowerPoint.getZ());
		p2.setX(p1.getX() + 100.f);
		p2.setY(this.upperPoint.getY() + 100.f);
		p2.setZ(this.upperPoint.getZ());
		assertFalse(this.testingBounds.intersects(p1,p2));

		// A has a corner in B
		p1.setX(this.lowerPoint.getX() - 100.f);
		p1.setY(this.lowerPoint.getY() - 100.f);
		p1.setZ(this.lowerPoint.getZ() - 100.f);
		p2.setX(this.centerPoint.getX());
		p2.setY(this.centerPoint.getY());
		p2.setZ(this.centerPoint.getZ());
		assertTrue(this.testingBounds.intersects(p1,p2));

		// A has two corners in B
		p1.setX(this.centerPoint.getX());
		p1.setY(this.lowerPoint.getY() - 100.f);
		p1.setZ(this.centerPoint.getZ());
		p2.setX(this.upperPoint.getX() + 100);
		p2.setY(this.upperPoint.getY() + 100.f);
		p2.setZ(this.upperPoint.getZ());
		assertTrue(this.testingBounds.intersects(p1,p2));

		// A cross B
		p1.setX((this.lowerPoint.getX()+this.centerPoint.getX())/2.f);
		p1.setY(this.lowerPoint.getY() - 100.f);
		p1.setZ(this.lowerPoint.getZ());
		p2.setX((this.upperPoint.getX()+this.centerPoint.getX())/2.f);
		p2.setY(this.upperPoint.getY() + 100.f);
		p2.setZ(this.upperPoint.getZ());
		assertTrue(this.testingBounds.intersects(p1,p2));

		// A on a side of B
		p1.setX(this.upperPoint.getX());
		p1.setY(this.lowerPoint.getY());
		p1.setZ(this.lowerPoint.getZ());
		p2.setX(this.upperPoint.getX() + 100.f);
		p2.setY(this.upperPoint.getY());
		p2.setZ(this.upperPoint.getZ());
		assertFalse(this.testingBounds.intersects(p1,p2));
	}

	@Override
	public void testClassifiesPoint3fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point3f c = new Point3f();
		float r;
		
		float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
		float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
		
		// A is enclosing B
		c.setX(this.centerPoint.getX());
		c.setY(this.centerPoint.getY());
		c.setZ(this.centerPoint.getZ());
		r = minSize / 4.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(c, r));

		// A is inside B
		c.setX(this.centerPoint.getX());
		c.setY(this.centerPoint.getY());
		c.setZ(this.centerPoint.getZ());
		r = 2.f*(float) Math.sqrt(maxSize * maxSize + maxSize * maxSize + maxSize * maxSize);
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(c, r));

		// A is outside B
		c.setX(this.lowerPoint.getX() - 100.f);
		c.setY(this.lowerPoint.getY() - 100.f);
		c.setZ(this.lowerPoint.getZ() - 100.f);
		r = 50.f;
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(c, r));

		// A has one corner in B
		c.setX(this.lowerPoint.getX() - 100.f);
		c.setY(this.lowerPoint.getY() - 100.f);
		c.setZ(this.lowerPoint.getZ() - 100.f);
		r = 100.f + minSize / 2.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(c, r));

		// A has two corners in B and is intersecting with two opposite sides.
		c.setX(this.centerPoint.getX());
		c.setY(this.lowerPoint.getY() - 100.f);
		c.setZ(this.centerPoint.getZ());
		r = 100.f + minSize / 2.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(c, r));

		// A has three corners in B.
		c.setX(this.lowerPoint.getX());
		c.setY(this.lowerPoint.getY());
		c.setZ(this.lowerPoint.getZ());
		r = maxSize;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(c, r));
	}

	@Override
	public void testIntersectsPoint3fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point3f c = new Point3f();
		float r;
		
		float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
		float maxSize = MathUtil.max(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
		
		// A is enclosing B
		c.setX(this.centerPoint.getX());
		c.setY(this.centerPoint.getY());
		c.setZ(this.centerPoint.getZ());
		r = minSize / 2.f;
		assertTrue(this.testingBounds.intersects(c, r));

		// A is inside B
		c.setX(this.centerPoint.getX());
		c.setY(this.centerPoint.getY());
		c.setZ(this.centerPoint.getZ());
		r = 2.f*(float) Math.sqrt(maxSize * maxSize + maxSize * maxSize + maxSize * maxSize);
		assertTrue(this.testingBounds.intersects(c, r));

		// A is outside B
		c.setX(this.lowerPoint.getX() - 100.f);
		c.setY(this.lowerPoint.getY() - 100.f);
		c.setZ(this.lowerPoint.getZ() - 100.f);
		r = 50.f;
		assertFalse(this.testingBounds.intersects(c, r));

		// A has one corner in B
		c.setX(this.lowerPoint.getX() - 100.f);
		c.setY(this.lowerPoint.getY() - 100.f);
		c.setZ(this.lowerPoint.getZ() - 100.f);
		r = 100.f + minSize / 2.f;
		assertTrue(this.testingBounds.intersects(c, r));

		// A has two corners in B and is intersecting with two opposite sides.
		c.setX(this.centerPoint.getX());
		c.setY(this.lowerPoint.getY() - 100.f);
		c.setZ(this.centerPoint.getZ());
		r = 100.f + minSize / 2.f;
		assertTrue(this.testingBounds.intersects(c, r));

		// A has three corners in B.
		c.setX(this.lowerPoint.getX());
		c.setY(this.lowerPoint.getY());
		c.setZ(this.lowerPoint.getZ());
		r = maxSize;
		assertTrue(this.testingBounds.intersects(c, r));

		// A and B touch themelves.
		c.setX(this.centerPoint.getX());
		c.setY(this.lowerPoint.getY() - 100.f);
		c.setZ(this.centerPoint.getZ());
		r = 100.f;
		assertTrue(this.testingBounds.intersects(c, r));
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
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.upperPoint.getX()-(float) MathConstants.EPSILON,
				this.upperPoint.getY()-(float) MathConstants.EPSILON,
				this.upperPoint.getZ()-(float) MathConstants.EPSILON);
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		

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
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.upperPoint.getX()-(float) MathConstants.EPSILON,
				this.upperPoint.getY()-(float) MathConstants.EPSILON,
				this.upperPoint.getZ()-(float) MathConstants.EPSILON);
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		

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
		assertTrue(this.testingBounds.intersects(p));		

		p = new Plane4f(v.getX(), v.getY(), v.getZ(),
				this.upperPoint.getX()-(float) MathConstants.EPSILON,
				this.upperPoint.getY()-(float) MathConstants.EPSILON,
				this.upperPoint.getZ()-(float) MathConstants.EPSILON);
		assertTrue(this.testingBounds.intersects(p));		

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
				new Vector3f(-1.,1.,0.f),
				new Vector3f(-1.,-1.,0.f),
				new Vector3f(0.f,0.f,1.)
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

		// A has three corners in B
		center.setX(this.lowerPoint.getX());
		center.setY(this.lowerPoint.getY());
		center.setZ(this.lowerPoint.getZ());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		extent[2] = minSize / 10.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, axis, extent));
	}

	@Override
	public void testIntersectsPoint3fVector3fArrayFloatArray() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f center = new Point3f();
		Vector3f[] axis = new Vector3f[] {
				new Vector3f(-1.,1.,0.f),
				new Vector3f(-1.,-1.,0.f),
				new Vector3f(0.f,0.f,1.)
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

		// A has three corners in B
		center.setX(this.lowerPoint.getX());
		center.setY(this.lowerPoint.getY());
		center.setZ(this.lowerPoint.getZ());
		extent[0] = minSize / 10.f;
		extent[1] = minSize / 10.f;
		extent[2] = minSize / 10.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));
	}

	@Override
	public void testClassifiesIC() {
		assertTrue(this.testingBounds.isInit());
		
		// AlignedBoundingBox
		{
			Point3f p1 = new Point3f();
			Point3f p2 = new Point3f();
			// A enclosing B
			p1.set(this.centerPoint);
			p1.add(this.lowerPoint);
			p1.scale(.5f);
			p2.set(this.centerPoint);
			p2.add(this.upperPoint);
			p2.scale(.5f);
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new AlignedBoundingBox(p1,p2)));
	
			// A OUTSIDE B
			p1.setX(this.upperPoint.getX() + 100.f);
			p1.setY(this.lowerPoint.getY());
			p1.setZ(this.lowerPoint.getZ());
			p2.setX(p1.getX() + 100.f);
			p2.setY(this.upperPoint.getY() + 100.f);
			p2.setZ(this.upperPoint.getZ());
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new AlignedBoundingBox(p1,p2)));
		}
		
		// BoundingSphere
		{
			Point3f c = new Point3f();
			float r;
			
			float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
			
			// A is enclosing B
			c.setX(this.centerPoint.getX());
			c.setY(this.centerPoint.getY());
			c.setZ(this.centerPoint.getZ());
			r = minSize / 4.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingSphere(c,r)));

			// A is outside B
			c.setX(this.lowerPoint.getX() - 100.f);
			c.setY(this.lowerPoint.getY() - 100.f);
			c.setZ(this.lowerPoint.getZ() - 100.f);
			r = 50.f;
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new BoundingSphere(c,r)));
		}
		
		// OrientedBoundigBox
		{
			assertTrue(this.testingBounds.isInit());
			
			Point3f center = new Point3f();
			Vector3f[] axis = new Vector3f[] {
					new Vector3f(-1.,1.,0.f),
					new Vector3f(-1.,-1.,0.f),
					new Vector3f(0.f,0.f,1.)
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
			Point3f p1 = new Point3f();
			Point3f p2 = new Point3f();
			// A enclosing B
			p1.set(this.centerPoint);
			p1.add(this.lowerPoint);
			p1.scale(.5f);
			p2.set(this.centerPoint);
			p2.add(this.upperPoint);
			p2.scale(.5f);
			assertTrue(this.testingBounds.intersects(new AlignedBoundingBox(p1,p2)));
	
			// A OUTSIDE B
			p1.setX(this.upperPoint.getX() + 100.f);
			p1.setY(this.lowerPoint.getY());
			p1.setZ(this.lowerPoint.getZ());
			p2.setX(p1.getX() + 100.f);
			p2.setY(this.upperPoint.getY() + 100.f);
			p2.setZ(this.upperPoint.getZ());
			assertFalse(this.testingBounds.intersects(new AlignedBoundingBox(p1,p2)));
		}
		
		// BoundingSphere
		{
			Point3f c = new Point3f();
			float r;
			
			float minSize = MathUtil.min(this.testingBounds.getSizeX(),this.testingBounds.getSizeY(),this.testingBounds.getSizeZ());
			
			// A is enclosing B
			c.setX(this.centerPoint.getX());
			c.setY(this.centerPoint.getY());
			c.setZ(this.centerPoint.getZ());
			r = minSize / 4.f;
			assertTrue(this.testingBounds.intersects(new BoundingSphere(c,r)));

			// A is outside B
			c.setX(this.lowerPoint.getX() - 100.f);
			c.setY(this.lowerPoint.getY() - 100.f);
			c.setZ(this.lowerPoint.getZ() - 100.f);
			r = 50.f;
			assertFalse(this.testingBounds.intersects(new BoundingSphere(c,r)));
		}
		
		// OrientedBoundigBox
		{
			assertTrue(this.testingBounds.isInit());
			
			Point3f center = new Point3f();
			Vector3f[] axis = new Vector3f[] {
					new Vector3f(-1.,1.,0.f),
					new Vector3f(-1.,-1.,0.f),
					new Vector3f(0.f,0.f,1.)
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

			// A and B do not intersect
			center.setX(this.lowerPoint.getX() - 1000.f);
			center.setY(this.centerPoint.getY());
			center.setZ(this.centerPoint.getZ());
			extent[0] = minSize / 10.f;
			extent[1] = minSize / 10.f;
			extent[2] = minSize / 10.f;
			assertFalse(this.testingBounds.intersects(new OrientedBoundingBox(center, axis, extent)));
		}
		
		// OrientedBoundingBox
		{
			Point3f center = new Point3f();
			Vector3f[] axis = new Vector3f[] {
					new Vector3f(-1.,1.,0.f),
					new Vector3f(-1.,-1.,0.f),
					new Vector3f(0.f,0.f,1.)
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

		MinimumBoundingRectangle mbr = this.testingBounds.toBounds2D();
		
		assertNotNull(mbr);
		assertEpsilonEquals(new Point2f(this.lowerPoint.getX(), this.lowerPoint.getY()), mbr.getLower());
		assertEpsilonEquals(new Point2f(this.upperPoint.getX(), this.upperPoint.getY()), mbr.getUpper());
	}
	
	/**
	 */
	@Override
	public void testToBounds2DCoordinateSystem3D() {
		assertTrue(this.testingBounds.isInit());

		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			MinimumBoundingRectangle mbr = this.testingBounds.toBounds2D(cs);
			
			Point2f p1 = cs.toCoordinateSystem2D(this.lowerPoint);
			Point2f p2 = cs.toCoordinateSystem2D(this.upperPoint);

			assertNotNull(mbr);
			assertEpsilonEquals(p1, mbr.getLower());
			assertEpsilonEquals(p2, mbr.getUpper());
		}
	}

	/**
	 */
	@Override
	public void testRotateAxisAngle4f() {
		// not applicable
	}

	/**
	 */
	@Override
	public void testRotateQuaternion() {
		// not applicable
	}

	/**
	 */
	@Override
	public void testSetRotationAxisAngle4f() {
		// not applicable
	}

	/**
	 */
	@Override
	public void testSetRotationQuaternion() {
		// not applicable
	}
	
	private Point3f[] readXYZ(String resource) throws Exception {
		URL url = Resources.getResource(resource);
		assertNotNull(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
		
		ArrayList<Point3f> points = new ArrayList<Point3f>();
		
		while (( line = reader.readLine() ) != null) {
			if (!"".equals(line)) { //$NON-NLS-1$
				String[] coords = line.split("[ \t]+"); //$NON-NLS-1$
				Point3f p = new Point3f(
						Float.parseFloat(coords[0]),
						Float.parseFloat(coords[1]),
						Float.parseFloat(coords[2]));
				points.add(p);
			}
		}
		reader.close();
		
		Point3f[] tab = new Point3f[points.size()];
		points.toArray(tab);
		points.clear();
		return tab;
	}
	
	/**
	 * @throws Exception
	 */
	public void testBuildingFromXYZ_before() throws Exception {
		Point3f[] points = readXYZ("fr/utbm/set/geom/bounds/bounds3d/teapotBefore.getX()yz"); //$NON-NLS-1$
		AlignedBoundingBox box = new AlignedBoundingBox();
		box.set(points);
		
		Point3f lower = new Point3f(-46.88664, 11.105659, -8.013674);
		Point3f upper = new Point3f(-22.354062, 23.127457, 7.2521);
		Point3f center = new Point3f(
				(lower.getX()+upper.getX())/2.f,
				(lower.getY()+upper.getY())/2.f,
				(lower.getZ()+upper.getZ())/2.);
		
		assertEpsilonEquals(
				lower,
				box.getLower());
		assertEpsilonEquals(
				upper,
				box.getUpper());
		assertEpsilonEquals(
				center,
				box.getCenter());
	}

	/**
	 * @throws Exception
	 */
	public void testBuildingFromXYZ_after() throws Exception {
		Point3f[] points = readXYZ("fr/utbm/set/geom/bounds/bounds3d/teapotAfter.getX()yz"); //$NON-NLS-1$
		AlignedBoundingBox box = new AlignedBoundingBox();
		box.set(points);
		
		Point3f lower = new Point3f(-45.519805761598626, 10.430658081144884, -8.013674);
		Point3f upper = new Point3f(-20.911044392384888, 22.985570065823836, 7.2521);
		Point3f center = new Point3f(
				(lower.getX()+upper.getX())/2.f,
				(lower.getY()+upper.getY())/2.f,
				(lower.getZ()+upper.getZ())/2.);

		assertEpsilonEquals(
				lower,
				box.getLower());
		assertEpsilonEquals(
				upper,
				box.getUpper());
		assertEpsilonEquals(
				center,
				box.getCenter());
	}

}

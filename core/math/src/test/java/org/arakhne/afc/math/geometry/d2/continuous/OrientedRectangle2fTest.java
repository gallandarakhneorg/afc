/* 
 * $Id$
 * 
 * Copyright (c) 2009-10, Multiagent Team,
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
package org.arakhne.afc.math.geometry.d2.continuous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.continuous.Circle2f;
import org.arakhne.afc.math.geometry.d2.continuous.OrientedRectangle2f;
import org.arakhne.afc.math.geometry.d2.continuous.Path2f;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Rectangle2f;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.arakhne.afc.math.matrix.Matrix2f;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for OrientedRectangle2f class.
 * <p>
 * From "Mathematics for 3D Game Programming and Computer Graphics" pp.220
 * Adapted to 2D by Stephane Galland
 * 
 * P1 = [ -1, -2 ]
 * P2 = [ 1, 0 ]
 * P3 = [ 2, -1 ]
 * P4 = [ 2, -1 ]
 * 
 * average: m = [ 1, -1 ]
 * 
 * Cov = [ 1.5 ,  .5 ]
 *       [  .5 ,  .5 ]
 *
 * Eigenvalues are: 1.70711 and 0.29289
 * 
 * Eigenvectors are:
 * R = [ 0.92388, 0.38268 ]
 * S = [ -0.38268, 0.92388 ]
 * 
 * Point projections are:
 * P1.R = -1.6892			P1.S = -1.4651
 * P2.R = 0.92388			P2.S = -0.38268
 * P3.R = 1.4651			P3.S = -1.6892
 * P4.R = 1.4651			P1.S = -1.6892
 * 
 * OBR planes are:
 * <R,1.6892>				<-R,1.4651>
 * <S,1.6892>				<-S,-0.38268>
 * 
 * Inner parameters are (computed with GNU Octave):
 * a = (min(Pi.R) + max(Pi.R)) / 2 = ( -1.6892 + 1.4651 ) / 2 = -0.11205
 * b = (min(Pi.S) + max(Pi.S)) / 2 = ( -1.6892 - 0.38268 ) / 2 = -1.0359
 * 
 * OBR center is (computed with GNU Octave):
 * Q = aR + bS = [ 0.29291, -0.99996 ]
 * 
 * OBR Extents are (computed with GNU Octave):
 * u = (max(Pi.R) - min(Pi.R)) / 2 = ( 1.4651 + 1.6892 ) / 2 = 1.5772
 * v = (max(Pi.S) - min(Pi.S)) / 2 = ( -0.38268 + 1.6892 ) / 2 = 0.65326
 * 
 * OBR vectors from center to vertices are (computed with GNU Octave):
 * E1 =   uR + vS = [ 1.2071, 1.2071 ]
 * E2 =   uR - vS = [ 1.7071e+00, 9.9132e-06 ]
 * E3 = - uR + vS = [ -1.7071e+00, -9.9132e-06 ]
 * E4 = - uR - vS = [ -1.2071, -1.2071 ]
 * 
 * OBB vertices are (computed with GNU Octave):
 * V1 = Q + E1 = [ 1.50002, 0.20711 ]
 * V2 = Q + E2 = [ 2.00000, -0.99995 ]
 * V3 = Q + E3 = [ -1.41417, -0.99997 ]
 * V4 = Q + E4 = [ -0.91420, -2.20704 ]
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class OrientedRectangle2fTest extends AbstractShape2fTestCase<OrientedRectangle2f> {

	private final Random random = new Random();
	
	private final Point2f P1 = new Point2f(-1., -2.);
	private final Point2f P2 = new Point2f(1., 0.);
	private final Point2f P3 = new Point2f(2., -1.);
	private final Point2f P4 = new Point2f(2., -1.);
	
	private final Vector2f R = new Vector2f(0.92388, 0.38268);
	private final Vector2f S = new Vector2f(-0.38268, 0.92388);
	
	private final Point2f Q = new Point2f(0.29291, -0.99996);

	private final double u = 1.5772;
	private final double v = 0.65326;
	
	private final Vector2f E1 = new Vector2f(1.2071, 1.2071);
	private final Vector2f E2 = new Vector2f(1.7071e+00, 9.9132e-06);
	private final Vector2f E3 = new Vector2f(-1.7071e+00, -9.9132e-06);
	private final Vector2f E4 = new Vector2f(-1.2071, -1.2071);

	private final Point2f V1 = new Point2f(1.50002, 0.20711);
	private final Point2f V2 = new Point2f(2.00000, -0.99995);
	private final Point2f V3 = new Point2f(-1.41417, -0.99997);
	private final Point2f V4 = new Point2f(-0.91420, -2.20704);
	
	/** aR = u * R
	 */
	private Vector2f aR;
	/** bS = v * S
	 */
	private Vector2f bS;
	/** maR = -u * R
	 */
	private Vector2f maR;
	/** mbS = -v * S
	 */
	private Vector2f mbS;
	
	/** Expected vertices
	 */
	private Point2f[] expectedVertices;
	
	/** Expected axis vectors from center to vertices
	 */
	private Vector2f[] expectedVectors;

	@Override
	protected OrientedRectangle2f createShape() {
		return new OrientedRectangle2f(this.Q.clone(), this.R, this.u, this.v);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		this.aR = new Vector2f();
		this.aR.scale(this.u, this.R);
		this.bS = new Vector2f();
		this.bS.scale(this.v, this.S);
		this.maR = new Vector2f(this.aR);
		this.maR.negate();
		this.mbS = new Vector2f(this.bS);
		this.mbS.negate();
		this.expectedVertices = new Point2f[] {
			this.V1, this.V2, this.V3, this.V4
		};
		this.expectedVectors = new Vector2f[] {
				this.E1, this.E3, this.E4, this.E2
		};
		super.setUp();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		this.aR = this.bS = this.maR = this.mbS = null;
		this.expectedVertices = null;
		this.expectedVectors = null;
		super.tearDown();
	}

	private boolean epsilonRemove(Set<? extends Point2D> s, Point2D t) {
		Iterator<? extends Point2D> iterator = s.iterator();
		Point2D candidate;
		while (iterator.hasNext()) {
			candidate = iterator.next();
			if (isEpsilonEquals(candidate.getX(), t.getX()) && isEpsilonEquals(candidate.getY(), t.getY())) {
				iterator.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 */
	@Test
	public void orientedRectangleTuple2dArray() {
		Point2f center = this.r.getCenter();
		double e1 = this.r.getFirstAxisExtent();
		double e2 = this.r.getSecondAxisExtent();
		Vector2f axis1 = this.r.getFirstAxis();
		Vector2f axis2 = this.r.getSecondAxis();
		
		assertEpsilonEquals(this.R, axis1);
		assertEpsilonEquals(this.S, axis2);
		assertEpsilonEquals(this.Q, center);
		assertEpsilonEquals(this.u, e1);
		assertEpsilonEquals(this.v, e2);
	}

	@Test
	@Override
	public void isEmpty() {
		assertFalse(this.r.isEmpty());
	}

	/**
	 */
	@Test
	public void getFirstAxis() {
		assertFalse(this.r.isEmpty());
		assertEpsilonEquals(this.R, this.r.getFirstAxis());
	}

	@Test
	public void getFirstAxisX() {
		assertFalse(this.r.isEmpty());
		assertEpsilonEquals(this.R.x, this.r.getFirstAxisX());
	}

	@Test
	public void getFirstAxisY() {
		assertFalse(this.r.isEmpty());
		assertEpsilonEquals(this.R.y, this.r.getFirstAxisY());
	}

	@Test
	public void getSecondAxis() {
		assertFalse(this.r.isEmpty());
		assertEpsilonEquals(this.S, this.r.getSecondAxis());
	}

	@Test
	public void getSecondAxisX() {
		assertFalse(this.r.isEmpty());
		assertEpsilonEquals(this.S.x, this.r.getSecondAxisX());
	}

	@Test
	public void getSecondAxisY() {
		assertFalse(this.r.isEmpty());
		assertEpsilonEquals(this.S.y, this.r.getSecondAxisY());
	}

	/**
	 */
	@Test
	public void getFirstAxisExtent() {
		assertFalse(this.r.isEmpty());
		assertEpsilonEquals(this.u, this.r.getFirstAxisExtent());
	}

	@Test
	public void getSecondAxisExtent() {
		assertFalse(this.r.isEmpty());
		assertEpsilonEquals(this.v, this.r.getSecondAxisExtent());
	}

	@Test
	public void getCenterX() {
		assertFalse(this.r.isEmpty());
		
		double center = this.r.getCenterX();
		assertEpsilonEquals(this.Q.x, center);
	}

	@Test
	public void getCenterY() {
		assertFalse(this.r.isEmpty());
		
		double center = this.r.getCenterY();
		assertEpsilonEquals(this.Q.y, center);
	}

	@Test
	public void getCenter() {
		assertFalse(this.r.isEmpty());
	
		Point2f center = this.r.getCenter();
		assertEpsilonEquals(this.Q, center);
	}

	@Test
	@Override
	public void clear() {
		assertFalse(this.r.isEmpty());
		
		this.r.clear();
		assertTrue(this.r.isEmpty());
	}

	@Test
	public void testClone() {
		assertFalse(this.r.isEmpty());
		
		OrientedRectangle2f clone = this.r.clone();
		
		assertNotSame(clone, this.r);

		assertNotSame(this.r.getCenter(), clone.getCenter());
		assertEpsilonEquals(this.r.getCenter(), clone.getCenter());

		assertNotSame(this.r.getFirstAxis(), clone.getFirstAxis());
		assertEpsilonEquals(this.r.getFirstAxis(), clone.getFirstAxis());

		assertNotSame(this.r.getSecondAxis(), clone.getSecondAxis());
		assertEpsilonEquals(this.r.getSecondAxis(), clone.getSecondAxis());
		
		assertEpsilonEquals(this.r.getFirstAxisExtent(), clone.getFirstAxisExtent());
		assertEpsilonEquals(this.r.getSecondAxisExtent(), clone.getSecondAxisExtent());
	}
	
	@Test
	public void setFromPointCloudIterable() {
		List<Point2D> list = new ArrayList<>();
		list.add(this.P1);
		list.add(this.P2);
		list.add(this.P3);
		list.add(this.P4);
		OrientedRectangle2f actual = new OrientedRectangle2f();
		actual.setFromPointCloud(list);

		assertNotSame(this.r.getCenter(), actual.getCenter());
		assertEpsilonEquals(this.r.getCenter(), actual.getCenter());

		assertNotSame(this.r.getFirstAxis(), actual.getFirstAxis());
		assertEpsilonEquals(this.r.getFirstAxis(), actual.getFirstAxis());

		assertNotSame(this.r.getSecondAxis(), actual.getSecondAxis());
		assertEpsilonEquals(this.r.getSecondAxis(), actual.getSecondAxis());
		
		assertEpsilonEquals(this.r.getFirstAxisExtent(), actual.getFirstAxisExtent());
		assertEpsilonEquals(this.r.getSecondAxisExtent(), actual.getSecondAxisExtent());
	}

	@Test
	public void setFromPointCloudPoint2DArray() {
		OrientedRectangle2f actual = new OrientedRectangle2f();
		actual.setFromPointCloud(this.P1, this.P2, this.P3, this.P4);

		assertNotSame(this.r.getCenter(), actual.getCenter());
		assertEpsilonEquals(this.r.getCenter(), actual.getCenter());

		assertNotSame(this.r.getFirstAxis(), actual.getFirstAxis());
		assertEpsilonEquals(this.r.getFirstAxis(), actual.getFirstAxis());

		assertNotSame(this.r.getSecondAxis(), actual.getSecondAxis());
		assertEpsilonEquals(this.r.getSecondAxis(), actual.getSecondAxis());
		
		assertEpsilonEquals(this.r.getFirstAxisExtent(), actual.getFirstAxisExtent());
		assertEpsilonEquals(this.r.getSecondAxisExtent(), actual.getSecondAxisExtent());
	}

	@Test
	@Override
	public void translateFloatFloat() {
		assertFalse(this.r.isEmpty());
		
		Vector2f vect = randomVector2f();
		
		this.r.translate(vect.getX(), vect.getY());
		
		Point2f expectedPt = new Point2f();
		
		expectedPt.add(this.Q, vect);
		assertEpsilonEquals(expectedPt, this.r.getCenter());

		assertEpsilonEquals(this.R, this.r.getFirstAxis());
		assertEpsilonEquals(this.S, this.r.getSecondAxis());

		assertEpsilonEquals(this.u, this.r.getFirstAxisExtent());
		assertEpsilonEquals(this.v, this.r.getSecondAxisExtent());
	}

	@Test
	@Override
	public void distancePoint2D() {
		assertFalse(this.r.isEmpty());

		// From box's center
		assertEpsilonEquals(0., this.r.distance(this.Q));
		
		// From the original points
		assertEpsilonEquals(0., this.r.distance(this.P1));
		assertEpsilonEquals(0., this.r.distance(this.P2));
		assertEpsilonEquals(0., this.r.distance(this.P3));
		assertEpsilonEquals(0., this.r.distance(this.P4));

		// From box's vertex
		assertEpsilonEquals(0., this.r.distance(this.V1));
		assertEpsilonEquals(0., this.r.distance(this.V2));
		assertEpsilonEquals(0., this.r.distance(this.V3));
		assertEpsilonEquals(0., this.r.distance(this.V4));
		
		Point2f p = new Point2f();

		// Points near a side and on one of the axis 
		p.scaleAdd(this.u * 4., this.R, this.Q);
		assertEpsilonEquals(this.u*3., this.r.distance(p));

		p.scaleAdd(-this.u * 4., this.R, this.Q);
		assertEpsilonEquals(this.u*3., this.r.distance(p));

		p.scaleAdd(this.v * 4., this.S, this.Q);
		assertEpsilonEquals(this.v*3., this.r.distance(p));

		p.scaleAdd(-this.v * 4., this.S, this.Q);
		assertEpsilonEquals(this.v*3., this.r.distance(p));

		// Points near a side and not on one of the axis 
		p.scaleAdd(this.u * 4., this.R, this.Q);
		p.scaleAdd(.5, this.S, p);
		assertEpsilonEquals(this.u*3., this.r.distance(p));

		p.scaleAdd(-this.u * 4., this.R, this.Q);
		p.scaleAdd(-.5, this.S, p);
		assertEpsilonEquals(this.u*3., this.r.distance(p));

		// Near V1
		p.scaleAdd(this.u * 3., this.R, this.Q);
		p.scaleAdd(this.v * 4., this.S, p);
		assertEpsilonEquals(this.V1.getDistance(p), this.r.distance(p));
	}

	@Test
	@Override
	public void distanceSquaredPoint2D() {
		assertFalse(this.r.isEmpty());

		// From box's center
		assertEpsilonEquals(0., this.r.distanceSquared(this.Q));
		
		// From the original points
		assertEpsilonEquals(0., this.r.distanceSquared(this.P1));
		assertEpsilonEquals(0., this.r.distanceSquared(this.P2));
		assertEpsilonEquals(0., this.r.distanceSquared(this.P3));
		assertEpsilonEquals(0., this.r.distanceSquared(this.P4));

		// From box's vertex
		assertEpsilonEquals(0., this.r.distanceSquared(this.V1));
		assertEpsilonEquals(0., this.r.distanceSquared(this.V2));
		assertEpsilonEquals(0., this.r.distanceSquared(this.V3));
		assertEpsilonEquals(0., this.r.distanceSquared(this.V4));
		
		Point2f p = new Point2f();

		// Points near a side and on one of the axis 
		p.scaleAdd(this.u * 4., this.R, this.Q);
		assertEpsilonEquals(Math.pow(this.u*3.,2.), this.r.distanceSquared(p));

		p.scaleAdd(-this.u * 4., this.R, this.Q);
		assertEpsilonEquals(Math.pow(this.u*3.,2.), this.r.distanceSquared(p));

		p.scaleAdd(this.v * 4., this.S, this.Q);
		assertEpsilonEquals(Math.pow(this.v*3.,2.), this.r.distanceSquared(p));

		p.scaleAdd(-this.v * 4., this.S, this.Q);
		assertEpsilonEquals(Math.pow(this.v*3.,2.), this.r.distanceSquared(p));

		// Points near a side and not on one of the axis 
		p.scaleAdd(this.u * 4., this.R, this.Q);
		p.scaleAdd(.5, this.S, p);
		assertEpsilonEquals(Math.pow(this.u*3.,2.), this.r.distanceSquared(p));

		p.scaleAdd(-this.u * 4., this.R, this.Q);
		p.scaleAdd(-.5, this.S, p);
		assertEpsilonEquals(Math.pow(this.u*3.,2.), this.r.distanceSquared(p));

		// Near V1
		p.scaleAdd(this.u * 3., this.R, this.Q);
		p.scaleAdd(this.v * 4., this.S, p);
		assertEpsilonEquals(this.V1.getDistanceSquared(p), this.r.distanceSquared(p));
	}

	@Override
	public void distanceL1Point2D() {
		assertFalse(this.r.isEmpty());

		// From box's center
		assertEpsilonEquals(0., this.r.distanceL1(this.Q));
		
		// From the original points
		assertEpsilonEquals(0., this.r.distanceL1(this.P1));
		assertEpsilonEquals(0., this.r.distanceL1(this.P2));
		assertEpsilonEquals(0., this.r.distanceL1(this.P3));
		assertEpsilonEquals(0., this.r.distanceL1(this.P4));

		// From box's vertex
		assertEpsilonEquals(0., this.r.distanceL1(this.V1));
		assertEpsilonEquals(0., this.r.distanceL1(this.V2));
		assertEpsilonEquals(0., this.r.distanceL1(this.V3));
		assertEpsilonEquals(0., this.r.distanceL1(this.V4));
		
		Point2f p = new Point2f();

		// Points near a side and on one of the axis 
		p.scaleAdd(this.u * 4., this.R, this.Q);
		assertEpsilonEquals(this.u*3., this.r.distanceL1(p));

		p.scaleAdd(-this.u * 4., this.R, this.Q);
		assertEpsilonEquals(this.u*3., this.r.distanceL1(p));

		p.scaleAdd(this.v * 4., this.S, this.Q);
		assertEpsilonEquals(this.v*3., this.r.distanceL1(p));

		p.scaleAdd(-this.v * 4., this.S, this.Q);
		assertEpsilonEquals(this.v*3., this.r.distanceL1(p));

		// Points near a side and not on one of the axis 
		p.scaleAdd(this.u * 4., this.R, this.Q);
		p.scaleAdd(.5, this.S, p);
		assertEpsilonEquals(this.u*3., this.r.distanceL1(p));

		p.scaleAdd(-this.u * 4., this.R, this.Q);
		p.scaleAdd(-.5, this.S, p);
		assertEpsilonEquals(this.u*3., this.r.distanceL1(p));

		// Near V1
		p.scaleAdd(this.u * 3., this.R, this.Q);
		p.scaleAdd(this.v * 4., this.S, p);
		assertEpsilonEquals(this.V1.getDistanceL1(p), this.r.distanceL1(p));
	}

	@Override
	public void distanceLinfPoint2D() {
		assertFalse(this.r.isEmpty());

		// From box's center
		assertEpsilonEquals(0., this.r.distanceLinf(this.Q));
		
		// From the original points
		assertEpsilonEquals(0., this.r.distanceLinf(this.P1));
		assertEpsilonEquals(0., this.r.distanceLinf(this.P2));
		assertEpsilonEquals(0., this.r.distanceLinf(this.P3));
		assertEpsilonEquals(0., this.r.distanceLinf(this.P4));

		// From box's vertex
		assertEpsilonEquals(0., this.r.distanceLinf(this.V1));
		assertEpsilonEquals(0., this.r.distanceLinf(this.V2));
		assertEpsilonEquals(0., this.r.distanceLinf(this.V3));
		assertEpsilonEquals(0., this.r.distanceLinf(this.V4));
		
		Point2f p = new Point2f();

		// Points near a side and on one of the axis 
		p.scaleAdd(this.u * 4., this.R, this.Q);
		assertEpsilonEquals(this.u*3., this.r.distanceLinf(p));

		p.scaleAdd(-this.u * 4., this.R, this.Q);
		assertEpsilonEquals(this.u*3., this.r.distanceLinf(p));

		p.scaleAdd(this.v * 4., this.S, this.Q);
		assertEpsilonEquals(this.v*3., this.r.distanceLinf(p));

		p.scaleAdd(-this.v * 4., this.S, this.Q);
		assertEpsilonEquals(this.v*3., this.r.distanceLinf(p));

		// Points near a side and not on one of the axis 
		p.scaleAdd(this.u * 4., this.R, this.Q);
		p.scaleAdd(.5, this.S, p);
		assertEpsilonEquals(this.u*3., this.r.distanceLinf(p));

		p.scaleAdd(-this.u * 4., this.R, this.Q);
		p.scaleAdd(-.5, this.S, p);
		assertEpsilonEquals(this.u*3., this.r.distanceLinf(p));

		// Near V1
		p.scaleAdd(this.u * 3., this.R, this.Q);
		p.scaleAdd(this.v * 4., this.S, p);
		assertEpsilonEquals(this.V1.getDistanceLinf(p), this.r.distanceLinf(p));	}

	@Test
	public void setCenterPoint2D() {
		assertFalse(this.r.isEmpty());
		
		Point2f p = randomPoint2f();
		
		this.r.setCenter(p);
		
		assertFalse(this.r.isEmpty());
		assertTrue(this.r.isEmpty());

		assertEquals(p, this.r.getCenter());
		assertEpsilonEquals(0., this.r.getFirstAxisExtent());
		assertEpsilonEquals(0., this.r.getSecondAxisExtent());

		assertEquals(new Vector2f(1.,0.), this.r.getFirstAxis());
		assertEquals(new Vector2f(0.,1.), this.r.getSecondAxis());
	}

	@Test
	public void setCenterDoubleDouble() {
		assertFalse(this.r.isEmpty());
		
		Point2f p = randomPoint2f();
		
		this.r.setCenter(p.getX(), p.getY());
		
		assertFalse(this.r.isEmpty());
		assertTrue(this.r.isEmpty());

		assertEquals(p, this.r.getCenter());
		assertEpsilonEquals(0., this.r.getFirstAxisExtent());
		assertEpsilonEquals(0., this.r.getSecondAxisExtent());

		assertEquals(new Vector2f(1.,0.), this.r.getFirstAxis());
		assertEquals(new Vector2f(0.,1.), this.r.getSecondAxis());
	}

	//-------------------------------------------
	// OrientedRectangle2f dedicated tests
	//-------------------------------------------
	
	/**
	 */
	@Test
	public void getClosestPointTo() {
		assertFalse(this.r.isEmpty());

		assertEpsilonEquals(this.Q, this.r.getClosestPointTo(this.Q));

		assertEpsilonEquals(this.V1, this.r.getClosestPointTo(this.V1));
		assertEpsilonEquals(this.V2, this.r.getClosestPointTo(this.V2));
		assertEpsilonEquals(this.V3, this.r.getClosestPointTo(this.V3));
		assertEpsilonEquals(this.V4, this.r.getClosestPointTo(this.V4));

		assertEpsilonEquals(this.P1, this.r.getClosestPointTo(this.P1));
		assertEpsilonEquals(this.P2, this.r.getClosestPointTo(this.P2));
		assertEpsilonEquals(this.P3, this.r.getClosestPointTo(this.P3));
		assertEpsilonEquals(this.P4, this.r.getClosestPointTo(this.P4));

		Point2f p = new Point2f();
		
		// Near V1
		p.scaleAdd(this.u * 3., this.R, this.Q);
		p.scaleAdd(this.v * 4., this.S, p);
		assertEpsilonEquals(this.V1, this.r.getClosestPointTo(p));
	}

	/**
	 */
	@Test
	public void getFarthestPointTo() {
		assertFalse(this.r.isEmpty());

		assertEpsilonEquals(this.V4, this.r.getFarthestPointTo(this.Q));

		assertEpsilonEquals(this.V4, this.r.getFarthestPointTo(this.V1));
		assertEpsilonEquals(this.V3, this.r.getFarthestPointTo(this.V2));
		assertEpsilonEquals(this.V2, this.r.getFarthestPointTo(this.V3));
		assertEpsilonEquals(this.V1, this.r.getFarthestPointTo(this.V4));

		// Farest points are computed with Octave
		assertEpsilonEquals(this.V1, this.r.getFarthestPointTo(this.P1));
		assertEpsilonEquals(this.V4, this.r.getFarthestPointTo(this.P2));
		assertEpsilonEquals(this.V3, this.r.getFarthestPointTo(this.P3));
		assertEpsilonEquals(this.V3, this.r.getFarthestPointTo(this.P4));

		Point2f p = new Point2f();
		
		// Near V8 -> V1
		p.scaleAdd(-this.u * 4., this.R, this.Q);
		p.scaleAdd(-this.v * 3., this.S, p);
		assertEpsilonEquals(this.V1, this.r.getFarthestPointTo(p));

		// Near V5 -> V2
		p.scaleAdd(-this.u * 5., this.R, this.Q);
		p.scaleAdd(this.v * 8., this.S, p);
		assertEpsilonEquals(this.V2, this.r.getFarthestPointTo(p));
	}

	private Point2f mkPt(Point2f p) {
		Vector2f vv = new Vector2f();
		vv.sub(this.Q, p);
		vv.scale(MathConstants.EPSILON);
		Point2f p2 = new Point2f(p);
		p2.add(vv);
		return p2;
	}
	
	@Test
	public void containsPoint2D() {
		assertFalse(this.r.isEmpty());

		assertTrue(this.r.contains(this.Q));
		
		assertTrue(this.r.contains(mkPt(this.P1)));
		assertTrue(this.r.contains(mkPt(this.P2)));
		assertTrue(this.r.contains(mkPt(this.P3)));
		assertTrue(this.r.contains(mkPt(this.P4)));

		Point2f p = new Point2f();
		
		p.x = -1000.;
		p.y = -1000.;
		assertFalse(this.r.contains(p));

		p.x = 1.7;
		p.y = 0.5;
		assertFalse(this.r.contains(p));
	}

	@Test
	public void intersectsRectangle2f() {
		assertFalse(this.r.isEmpty());

		Point2f p1 = new Point2f();
		Point2f p2 = new Point2f();
		
		double minE = Math.min(this.u, this.v);
		double maxE = Math.max(this.u, this.v);
		
		p1.x = this.Q.x - minE / 10.;
		p1.y = this.Q.y - minE / 10.;
		p2.x = this.Q.x + minE / 10.;
		p2.y = this.Q.y + minE / 10.;
		assertTrue(this.r.intersects(new Rectangle2f(p1,p2)));

		p1.x = this.Q.x - maxE * 10.;
		p1.y = this.Q.y - maxE * 10.;
		p2.x = this.Q.x + maxE * 10.;
		p2.y = this.Q.y + maxE * 10.;
		assertTrue(this.r.intersects(new Rectangle2f(p1,p2)));

		p1.x = this.Q.x - 1000.;
		p1.y = this.Q.y - 10.;
		p2.x = this.Q.x - 900.;
		p2.y = this.Q.y + 10.;
		assertFalse(this.r.intersects(new Rectangle2f(p1,p2)));

		p1.x = -10.;
		p1.y = -10.;
		p2.x = 0.5;
		p2.y = 10.;
		assertTrue(this.r.intersects(new Rectangle2f(p1,p2)));

		p1.x = -10.;
		p1.y = -10.;
		p2.x = this.Q.x;
		p2.y = this.Q.y;
		assertTrue(this.r.intersects(new Rectangle2f(p1,p2)));

		p1.x = this.Q.x;
		p1.y = this.Q.y;
		p2.x = this.Q.x+1;
		p2.y = this.Q.y+1;
		assertTrue(this.r.intersects(new Rectangle2f(p1,p2)));
	}

	@Test
	public void intersectsCircle2f() {
		assertFalse(this.r.isEmpty());

		Point2f c = new Point2f();
		double r;
		
		double minE = Math.min(this.u, this.v);
		double maxE = Math.max(this.u, this.v);

		c.x = this.Q.x;
		c.y = this.Q.y;
		r = minE / 10.;
		assertTrue(this.r.intersects(new Circle2f(c, r)));
		
		c.x = this.Q.x;
		c.y = this.Q.y;
		r = minE;
		assertTrue(this.r.intersects(new Circle2f(c, r)));

		c.x = this.Q.x;
		c.y = this.Q.y;
		r = 0.;
		assertTrue(this.r.intersects(new Circle2f(c, r)));

		c.x = this.Q.x;
		c.y = this.Q.y;
		r = maxE * 10.;
		assertTrue(this.r.intersects(new Circle2f(c, r)));

		c.x = this.P1.x;
		c.y = this.P1.y;
		r = maxE * 50.;
		assertTrue(this.r.intersects(new Circle2f(c, r)));

		c.x = -10.;
		c.y = -10.;
		r = 1.;
		assertFalse(this.r.intersects(new Circle2f(c, r)));

		c.x = this.Q.x;
		c.y = this.Q.y;
		r = maxE;
		assertTrue(this.r.intersects(new Circle2f(c, r)));

		c.x = -2.;
		c.y = this.Q.y;
		r = 1.;
		assertTrue(this.r.intersects(new Circle2f(c, r)));
	}
	
	@Test
	public void intersectSegment2f() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void intersectEllipse2f() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void intersectsOrientedRectangle2f() {
		assertFalse(this.r.isEmpty());
		
		Rectangle2f bounds = this.r.toBoundingBox();

		Point2f center = new Point2f();
		Vector2f axis1 = new Vector2f();
		double extent1 = 0.;
		double extent2 = 0.;
		
		center.x = this.r.getCenterX();
		center.y = this.r.getCenterY();
		axis1 = this.r.getFirstAxis();
		extent1 = this.r.getFirstAxisExtent() / 2.;
		extent2 = this.r.getSecondAxisExtent() / 2.;
		assertTrue(this.r.intersects(new OrientedRectangle2f(center, axis1, extent1, extent2)));

		center.x = this.r.getCenterX();
		center.y = this.r.getCenterY();
		axis1 = this.r.getFirstAxis();
		extent1 = this.r.getFirstAxisExtent() * 2.;
		extent2 = this.r.getSecondAxisExtent() * 2.;
		assertTrue(this.r.intersects(new OrientedRectangle2f(center, axis1, extent1, extent2)));

		center.x = bounds.getMinX() - 20.;
		center.y = this.r.getCenterY();
		axis1 = this.r.getFirstAxis();
		extent1 = this.r.getFirstAxisExtent();
		extent2 = this.r.getSecondAxisExtent();
		assertFalse(this.r.intersects(new OrientedRectangle2f(center, axis1, extent1, extent2)));

		center.x = bounds.getMinX();
		center.y = this.r.getCenterY();
		axis1 = this.r.getFirstAxis();
		extent1 = this.r.getFirstAxisExtent();
		extent2 = this.r.getSecondAxisExtent();
		assertTrue(this.r.intersects(new OrientedRectangle2f(center, axis1, extent1, extent2)));

		center.x = (bounds.getMinX() + this.Q.x) / 2.;
		center.y = this.r.getCenterY();
		axis1 = this.r.getFirstAxis();
		extent1 = this.r.getFirstAxisExtent();
		extent2 = this.r.getSecondAxisExtent();
		assertTrue(this.r.intersects(new OrientedRectangle2f(center, axis1, extent1, extent2)));

		center.x = 0.;
		center.y = 0.;
		axis1.set(1., 0);
		extent1 = 1.;
		extent2 = 1.;
		assertTrue(this.r.intersects(new OrientedRectangle2f(center, axis1, extent1, extent2)));
	}

	@Test
	@Override
	public void toBoundingBox() {
		assertFalse(this.r.isEmpty());

		Rectangle2f box = this.r.toBoundingBox();
		
		assertEpsilonEquals(MathUtil.min(this.P1.x, this.P2.x, this.P3.x, this.P4.x), box.getMinX());
		assertEpsilonEquals(MathUtil.min(this.P1.y, this.P2.y, this.P3.y, this.P4.y), box.getMinY());
		assertEpsilonEquals(MathUtil.max(this.P1.x, this.P2.x, this.P3.x, this.P4.x), box.getMaxX());
		assertEpsilonEquals(MathUtil.max(this.P1.y, this.P2.y, this.P3.y, this.P4.y), box.getMaxY());
	}

	@Test
	public void toBoundingBoxRectangle2f() {
		assertFalse(this.r.isEmpty());

		Rectangle2f box = new Rectangle2f();
		this.r.toBoundingBox(box);
		
		assertEpsilonEquals(MathUtil.min(this.P1.x, this.P2.x, this.P3.x, this.P4.x), box.getMinX());
		assertEpsilonEquals(MathUtil.min(this.P1.y, this.P2.y, this.P3.y, this.P4.y), box.getMinY());
		assertEpsilonEquals(MathUtil.max(this.P1.x, this.P2.x, this.P3.x, this.P4.x), box.getMaxX());
		assertEpsilonEquals(MathUtil.max(this.P1.y, this.P2.y, this.P3.y, this.P4.y), box.getMaxY());
	}

	@Test
	@Override
	public void intersectsPath2f() {
		Path2f p;
		
		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));
		
		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(0f, 0f);
		p.lineTo(-2f, 2f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertFalse(this.r.intersects(p));

		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(2f, 2f);
		p.lineTo(-2f, 2f);
		assertTrue(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));

		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertFalse(this.r.intersects(p));

		p = new Path2f();
		p.moveTo(-2f, 2f);
		p.lineTo(1f, 0f);
		p.lineTo(2f, 1f);
		assertTrue(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));

		p = new Path2f();
		p.moveTo(-2f, 2f);
		p.lineTo(2f, 1f);
		p.lineTo(1f, 0f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));
	}

	@Test
	@Override
	public void intersectsPathIterator2f() {
		Path2f p;

		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIterator()));
		
		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(0f, 0f);
		p.lineTo(-2f, 2f);
		assertFalse(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.r.intersects(p.getPathIterator()));

		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(2f, 2f);
		p.lineTo(-2f, 2f);
		assertTrue(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIterator()));

		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.r.intersects(p.getPathIterator()));

		p = new Path2f();
		p.moveTo(-2f, 2f);
		p.lineTo(1f, 0f);
		p.lineTo(2f, 1f);
		assertTrue(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIterator()));

		p = new Path2f();
		p.moveTo(-2f, 2f);
		p.lineTo(2f, 1f);
		p.lineTo(1f, 0f);
		assertFalse(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIterator()));
	}

	@Test
	public void computeOBRAxis() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void computeOBRCenterExtents() {
		throw new UnsupportedOperationException();
	}

	@Test
	public boolean intersectsOrientedRectangleSegment() {
		throw new UnsupportedOperationException();
	}

	@Test
	public boolean intersectsOrientedRectangleEllipse(){
		throw new UnsupportedOperationException();
	}

	@Test
	public boolean intersectsOrientedRectangleSolidCircle() {
		throw new UnsupportedOperationException();
	}

	@Test
	public boolean intersectsOrientedRectangleOrientedRectangle(){
		throw new UnsupportedOperationException();
	}

	@Test
	public boolean intersectsOrientedRectangleRectangle() {
		throw new UnsupportedOperationException();
	}

	@Test
	public boolean containsOrientedRectangleRectangle() {
		throw new UnsupportedOperationException();
	}

	@Test
	public boolean containsOrientedRectanglePoint() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void computeClosestPoint() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void computeFarthestPoint() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setFirstAxisExtent() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setSecondAxisExtent() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setFirstAxisVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setFirstAxisVector2DDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setFirstAxisDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setFirstAxisDoublDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setSecondAxisVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setSecondAxisVector2DDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setSecondAxisDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setSecondAxisDoublDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void containsDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void containsRectangle2f() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getPathIterator() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getPathIteratorTransform2D_identity() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getPathIteratorTransform2D_noIdentity() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setShape2f() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setPoint2DVector2DDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

}
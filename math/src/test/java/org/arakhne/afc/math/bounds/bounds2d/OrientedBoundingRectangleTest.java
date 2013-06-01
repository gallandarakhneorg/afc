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
package org.arakhne.afc.math.bounds.bounds2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.bounds.bounds2f.BoundingCircle;
import org.arakhne.afc.math.bounds.bounds2f.Bounds2D;
import org.arakhne.afc.math.bounds.bounds2f.CombinableBounds2D;
import org.arakhne.afc.math.bounds.bounds2f.MinimumBoundingRectangle;
import org.arakhne.afc.math.bounds.bounds2f.OrientedBoundingRectangle;
import org.arakhne.afc.math.bounds.bounds3f.OrientedBoundingBox;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.intersection.IntersectionType;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.sizediterator.SizedIterator;


/**
 * Unit test for OrientedBoundingRectangle class.
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
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedBoundingRectangleTest extends AbstractOrientedBound2DTest {

	private final Point2f P1 = new Point2f(-1.f, -2.f);
	private final Point2f P2 = new Point2f(1.f, 0.f);
	private final Point2f P3 = new Point2f(2.f, -1.f);
	private final Point2f P4 = new Point2f(2.f, -1.f);
	
	private final Vector2f R = new Vector2f(0.92388, 0.38268);
	private final Vector2f S = new Vector2f(-0.38268, 0.92388);
	
	private final Point2f Q = new Point2f(0.29291, -0.99996);

	private final float u = 1.5772f;
	private final float v = 0.65326f;
	
	private final Vector2f E1 = new Vector2f(1.2071, 1.2071);
	private final Vector2f E2 = new Vector2f(1.7071e+00, 9.9132e-06);
	private final Vector2f E3 = new Vector2f(-1.7071e+00, -9.9132e-06);
	private final Vector2f E4 = new Vector2f(-1.2071, -1.2071);

	private final Point2f V1 = new Point2f(1.50002, 0.20711);
	private final Point2f V2 = new Point2f(2.00000, -0.99995);
	private final Point2f V3 = new Point2f(-1.41417, -0.99997);
	private final Point2f V4 = new Point2f(-0.91420, -2.20704);
	
	/** Reference instance.
	 */
	private OrientedBoundingRectangle testingBounds;
	
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

	/**
	 * {@inheritDoc}
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
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
		this.testingBounds = createBounds(this.P1, this.P2, this.P3, this.P4);
		setDecimalPrecision(1); // Because the precision of the example is about 2 decimals
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		setDefaultDecimalPrecision();
		this.aR = this.bS = this.maR = this.mbS = null;
		this.expectedVertices = null;
		this.expectedVectors = null;
		this.testingBounds = null;
		super.tearDown();
	}
	
	/**
	 * @param points
	 * @return a bounds
	 */
	private OrientedBoundingRectangle createBounds(Tuple2f... points) {
		OrientedBoundingRectangle obb = new OrientedBoundingRectangle();
		obb.set(points);
		return obb;
	}

	private boolean epsilonRemove(Set<? extends Tuple2f> s, Tuple2f t) {
		Iterator<? extends Tuple2f> iterator = s.iterator();
		Tuple2f candidate;
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
	public void testOrientedBoundingRectangleTuple2fArray() {
		Point2f center = this.testingBounds.getCenter();
		Vector2f extents = this.testingBounds.getOrientedBoundExtentVector();
		Vector2f[] axis = this.testingBounds.getOrientedBoundAxis();
		
		setDecimalPrecision(2);
		assertEpsilonEquals(this.R, axis[0]);
		assertEpsilonEquals(this.S, axis[1]);
		assertEpsilonEquals(this.Q, center);
		assertEpsilonEquals(new Vector2f(this.u, this.v), extents);
		setDefaultDecimalPrecision();
	}

	@Override
	public void testIsInit() {
		assertTrue(this.testingBounds.isInit());
	}

	@Override
	public void testIsEmpty() {
		assertTrue(this.testingBounds.isInit());

		assertFalse(this.testingBounds.isEmpty());
	}

	@Override
	public void testGetMathematicalDimension() {
		assertTrue(this.testingBounds.isInit());
		
		assertEquals(PseudoHamelDimension.DIMENSION_2D, this.testingBounds.getMathematicalDimension());
	}

	/**
	 */
	@Override
	public void testGetOrientedBoundAxis() {
		assertTrue(this.testingBounds.isInit());
		
		Vector2f[] axis = this.testingBounds.getOrientedBoundAxis();
		
		assertEpsilonEquals(this.R, axis[0]);
		assertEpsilonEquals(this.S, axis[1]);
	}

	/**
	 */
	@Override
	public void testGetOrientedBoundExtents() {
		assertTrue(this.testingBounds.isInit());

		Vector2f extents = this.testingBounds.getOrientedBoundExtentVector();
		
		assertEpsilonEquals(this.u, extents.getX());
		assertEpsilonEquals(this.v, extents.getY());
	}

	/**
	 */
	@Override
	public void testGetR() {
		Vector2f axis = this.testingBounds.getR();
		assertEpsilonEquals(this.R, axis);
	}
	
	/**
	 */
	@Override
	public void testGetS() {
		Vector2f axis = this.testingBounds.getS();
		assertEpsilonEquals(this.S, axis);
	}

	/**
	 */
	@Override
	public void testGetRExtent() {
		float extent = this.testingBounds.getRExtent();
		assertEpsilonEquals(this.u, extent);
	}

	/**
	 */
	@Override
	public void testGetSExtent() {
		float extent = this.testingBounds.getSExtent();
		assertEpsilonEquals(this.v, extent);
	}

	@Override
	public void testGetCenterX() {
		assertTrue(this.testingBounds.isInit());
		
		float center = this.testingBounds.getCenterX();
		assertEpsilonEquals(this.Q.getX(), center);
	}

	@Override
	public void testGetCenterY() {
		assertTrue(this.testingBounds.isInit());
		
		float center = this.testingBounds.getCenterY();
		assertEpsilonEquals(this.Q.getY(), center);
	}

	@Override
	public void testGetCenter() {
		assertTrue(this.testingBounds.isInit());
	
		Point2f center = this.testingBounds.getCenter();
		assertEpsilonEquals(this.Q, center);
	}

	@Override
	public void testGetPosition() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f center = this.testingBounds.getPosition();
		assertEpsilonEquals(this.Q, center);
	}

	@Override
	public void testGetMaxX() {
		assertTrue(this.testingBounds.isInit());
		
		float pos = this.testingBounds.getMaxX();
				
		float expectedPos = Float.NEGATIVE_INFINITY;
		for(Point2f p : this.expectedVertices) {
			if (p.getX()>expectedPos) expectedPos = p.getX();
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetMaxY() {
		assertTrue(this.testingBounds.isInit());
		
		float pos = this.testingBounds.getMaxY();
				
		float expectedPos = Float.NEGATIVE_INFINITY;
		for(Point2f p : this.expectedVertices) {
			if (p.getY()>expectedPos) expectedPos = p.getY();
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetMinX() {
		assertTrue(this.testingBounds.isInit());
		
		float pos = this.testingBounds.getMinX();
				
		float expectedPos = Float.POSITIVE_INFINITY;
		for(Point2f p : this.expectedVertices) {
			if (p.getX()<expectedPos) expectedPos = p.getX();
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetMinY() {
		assertTrue(this.testingBounds.isInit());
		
		float pos = this.testingBounds.getMinY();
				
		float expectedPos = Float.POSITIVE_INFINITY;
		for(Point2f p : this.expectedVertices) {
			if (p.getY()<expectedPos) expectedPos = p.getY();
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetLower() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f pos = this.testingBounds.getLower();
				
		Point2f expectedPos = new Point2f(Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
		for(Point2f p : this.expectedVertices) {
			if (p.getX()<expectedPos.getX()) expectedPos.setX(p.getX());
			if (p.getY()<expectedPos.getY()) expectedPos.setY(p.getY());
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetUpper() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f pos = this.testingBounds.getUpper();
				
		Point2f expectedPos = new Point2f(Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
		for(Point2f p : this.expectedVertices) {
			if (p.getX()>expectedPos.getX()) expectedPos.setX(p.getX());
			if (p.getY()>expectedPos.getY()) expectedPos.setY(p.getY());
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetLowerUpper() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f l = new Point2f();
		Point2f u = new Point2f();
		
		this.testingBounds.getLowerUpper(l, u);
				
		Point2f expectedMin = new Point2f(Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
		Point2f expectedMax = new Point2f(Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
		for(Point2f p : this.expectedVertices) {
			if (p.getX()<expectedMin.getX()) expectedMin.setX(p.getX());
			if (p.getY()<expectedMin.getY()) expectedMin.setY(p.getY());
			if (p.getX()>expectedMax.getX()) expectedMax.setX(p.getX());
			if (p.getY()>expectedMax.getY()) expectedMax.setY(p.getY());
		}
		
		assertEpsilonEquals(expectedMin.getX(), l.getX());
		assertEpsilonEquals(expectedMin.getY(), l.getY());
		assertEpsilonEquals(expectedMax.getX(), u.getX());
		assertEpsilonEquals(expectedMax.getY(), u.getY());
	}

	/** 
	 */
	public void testGetSizeX() {
		assertTrue(this.testingBounds.isInit());
		
		float size = this.testingBounds.getSizeX();
				
		float lower = Float.POSITIVE_INFINITY;
		float upper = Float.NEGATIVE_INFINITY;
		for(Point2f p : this.expectedVertices) {
			if (p.getX()<lower) lower = p.getX();
			if (p.getX()>upper) upper = p.getX();
		}
		
		assertEpsilonEquals(upper - lower, size);
	}

	/** 
	 */
	public void testGetSizeY() {
		assertTrue(this.testingBounds.isInit());
		
		float size = this.testingBounds.getSizeY();
				
		float lower = Float.POSITIVE_INFINITY;
		float upper = Float.NEGATIVE_INFINITY;
		for(Point2f p : this.expectedVertices) {
			if (p.getY()<lower) lower = p.getY();
			if (p.getY()>upper) upper = p.getY();
		}
		
		assertEpsilonEquals(upper - lower, size);
	}

	/** 
	 */
	public void testGetSize() {
		assertTrue(this.testingBounds.isInit());
		
		Vector2f size = this.testingBounds.getSize();
				
		Point2f lower = new Point2f(Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
		Point2f upper = new Point2f(Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
		for(Point2f p : this.expectedVertices) {
			if (p.getX()<lower.getX()) lower.setX(p.getX());
			if (p.getY()>upper.getY()) upper.setY(p.getY());
			if (p.getX()>upper.getX()) upper.setX(p.getX());
			if (p.getY()<lower.getY()) lower.setY(p.getY());
		}
		
		assertEpsilonEquals(new Vector2f(
				upper.getX() - lower.getX(),
				upper.getY() - lower.getY()), size);
	}

	@Override
	public void testReset() {
		assertTrue(this.testingBounds.isInit());
		
		this.testingBounds.reset();
		assertFalse(this.testingBounds.isInit());
		assertTrue(this.testingBounds.isEmpty());
	}

	@Override
	public void testClone() {
		assertTrue(this.testingBounds.isInit());
		
		OrientedBoundingRectangle clone = this.testingBounds.clone();
		
		assertNotSame(clone, this.testingBounds);

		assertNotSame(this.testingBounds.getOrientedBoundAxis(), clone.getOrientedBoundAxis());
		assertEpsilonEquals(this.R, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(this.S, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(this.testingBounds.getOrientedBoundAxis(), clone.getOrientedBoundAxis());
		
		assertNotSame(this.testingBounds.getCenter(), clone.getCenter());
		assertEpsilonEquals(this.Q, this.testingBounds.getCenter());
		assertEpsilonEquals(this.testingBounds.getCenter(), clone.getCenter());

		assertNotSame(this.testingBounds.getExtent(), clone.getExtent());
		assertEpsilonEquals(this.u, this.testingBounds.getRExtent());
		assertEpsilonEquals(this.v, this.testingBounds.getSExtent());
		assertEpsilonEquals(this.testingBounds.getExtent(), clone.getExtent());
	}

	@Override
	public void testTranslateVector2f() {
		assertTrue(this.testingBounds.isInit());
		
		Vector2f vect = randomVector2D();
		
		this.testingBounds.translate(vect);
		
		Point2f expectedPt = new Point2f();
		
		expectedPt.add(this.Q, vect);
		assertEpsilonEquals(expectedPt, this.testingBounds.getCenter());

		assertEpsilonEquals(this.R, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(this.S, this.testingBounds.getOrientedBoundAxis()[1]);

		assertEpsilonEquals(this.u, this.testingBounds.getRExtent());
		assertEpsilonEquals(this.v, this.testingBounds.getSExtent());
	}

	@Override
	public void testGetVertexCount() {
		assertTrue(this.testingBounds.isInit());
		assertEquals(4, this.testingBounds.getVertexCount());
	}

	@Override
	public void testGetLocalOrientedVertices() {
		assertTrue(this.testingBounds.isInit());
		
		Set<Vector2f> expected = new HashSet<Vector2f>();
		for(Vector2f vect : this.expectedVectors) {
			expected.add(vect);
		}
		
		SizedIterator<Vector2f> vectors = this.testingBounds.getLocalOrientedBoundVertices();
		
		assertNotNull(vectors);
		assertEquals(4, vectors.totalSize());
		
		Vector2f vect;
		int count=0;
		while (vectors.hasNext()) {
			vect = vectors.next();
			assertNotNull(vect);
			assertTrue(epsilonRemove(expected, vect));
			++count;
		}
		
		assertEquals(4, count);
		assertTrue(expected.isEmpty());
	}

	@Override
	public void testGetLocalVertexAt() {
		assertTrue(this.testingBounds.isInit());
		
		List<Vector2f> expected = new ArrayList<Vector2f>();
		for(Vector2f vect : this.expectedVectors) {
			expected.add(vect);
		}
		
		Vector2f v;
		
		try {
			this.testingBounds.getLocalVertexAt(-1);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
		
		for(int i=0; i<4; ++i) {
			v = this.testingBounds.getLocalVertexAt(i);
			assertNotNull(v);
			assertEpsilonEquals(expected.get(i), v);
		}
		
		try {
			this.testingBounds.getLocalVertexAt(4);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
	}

	@Override
	public void testGetGlobalOrientedVertices() {
		assertTrue(this.testingBounds.isInit());
		
		Set<Point2f> expected = new HashSet<Point2f>();
		for(Point2f p : this.expectedVertices) {
			expected.add(p);
		}
		
		SizedIterator<? extends Point2f> points = this.testingBounds.getGlobalOrientedBoundVertices();
		
		assertNotNull(points);
		assertEquals(4, points.totalSize());
		
		Point2f p;
		int count=0;
		while (points.hasNext()) {
			p = points.next();
			assertNotNull(p);
			assertTrue(epsilonRemove(expected, p));
			++count;
		}
		
		assertEquals(4, count);
		assertTrue(expected.isEmpty());
	}

	@Override
	public void testGetGlobalVertexAt() {
		assertTrue(this.testingBounds.isInit());
		
		List<Point2f> expected = new ArrayList<Point2f>();
		Point2f p;
		for(Vector2f vect : this.expectedVectors) {
			p = new Point2f(this.Q);
			p.add(vect);
			expected.add(p);
		}
		
		try {
			this.testingBounds.getGlobalVertexAt(-1);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
		
		for(int i=0; i<4; ++i) {
			p = this.testingBounds.getGlobalVertexAt(i);
			assertNotNull(p);
			assertEpsilonEquals(expected.get(i), p);
		}
		
		try {
			this.testingBounds.getGlobalVertexAt(4);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
	}

	@Override
	public void testDistanceP() {
		assertTrue(this.testingBounds.isInit());

		// From box's center
		assertEquals(0.f, this.testingBounds.distance(this.Q));
		
		// From the original points
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.P1));
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.P2));
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.P3));
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.P4));

		// From box's vertex
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.V1));
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.V2));
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.V3));
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.V4));
		
		Point2f p = new Point2f();

		// Points near a side and on one of the axis 
		p.scaleAdd(this.u * 4.f, this.R, this.Q);
		assertEpsilonEquals(this.u*3.f, this.testingBounds.distance(p));

		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		assertEpsilonEquals(this.u*3.f, this.testingBounds.distance(p));

		p.scaleAdd(this.v * 4.f, this.S, this.Q);
		assertEpsilonEquals(this.v*3.f, this.testingBounds.distance(p));

		p.scaleAdd(-this.v * 4.f, this.S, this.Q);
		assertEpsilonEquals(this.v*3.f, this.testingBounds.distance(p));

		// Points near a side and not on one of the axis 
		p.scaleAdd(this.u * 4.f, this.R, this.Q);
		p.scaleAdd(.5f, this.S, p);
		assertEpsilonEquals(this.u*3.f, this.testingBounds.distance(p));

		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		p.scaleAdd(-.5f, this.S, p);
		assertEpsilonEquals(this.u*3.f, this.testingBounds.distance(p));

		// Near V1
		p.scaleAdd(this.u * 3.f, this.R, this.Q);
		p.scaleAdd(this.v * 4.f, this.S, p);
		assertEpsilonEquals(this.V1.distance(p), this.testingBounds.distance(p));
	}

	@Override
	public void testDistanceSquaredP() {
		assertTrue(this.testingBounds.isInit());

		// From box's center
		assertEquals(0.f, this.testingBounds.distanceSquared(this.Q));
		
		// From the original points
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.P1));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.P2));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.P3));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.P4));

		// From box's vertex
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V1));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V2));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V3));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V4));
		
		Point2f p = new Point2f();

		// Points near a side and on one of the axis 
		p.scaleAdd(this.u * 4.f, this.R, this.Q);
		assertEpsilonEquals((float) Math.pow(this.u*3.f,2.f), this.testingBounds.distanceSquared(p));

		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		assertEpsilonEquals((float) Math.pow(this.u*3.f,2.f), this.testingBounds.distanceSquared(p));

		p.scaleAdd(this.v * 4.f, this.S, this.Q);
		assertEpsilonEquals((float) Math.pow(this.v*3.f,2.f), this.testingBounds.distanceSquared(p));

		p.scaleAdd(-this.v * 4.f, this.S, this.Q);
		assertEpsilonEquals((float) Math.pow(this.v*3.f,2.f), this.testingBounds.distanceSquared(p));

		// Points near a side and not on one of the axis 
		p.scaleAdd(this.u * 4.f, this.R, this.Q);
		p.scaleAdd(.5f, this.S, p);
		assertEpsilonEquals((float) Math.pow(this.u*3.f,2.f), this.testingBounds.distanceSquared(p));

		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		p.scaleAdd(-.5f, this.S, p);
		assertEpsilonEquals((float) Math.pow(this.u*3.f,2.f), this.testingBounds.distanceSquared(p));

		// Near V1
		p.scaleAdd(this.u * 3.f, this.R, this.Q);
		p.scaleAdd(this.v * 4.f, this.S, p);
		assertEpsilonEquals(this.V1.distanceSquared(p), this.testingBounds.distanceSquared(p));
	}

	@Override
	public void testDistanceMaxP() {
		assertTrue(this.testingBounds.isInit());

		// From box's center
		float diagonalLength = MathUtil.distance(this.u, this.v);
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.Q));
		
		// From the original points (computed with GNU octave)
		assertEpsilonEquals(3.3349f, this.testingBounds.distanceMax(this.P1));
		assertEpsilonEquals(2.9215f, this.testingBounds.distanceMax(this.P2));
		assertEpsilonEquals(3.4142f, this.testingBounds.distanceMax(this.P3));
		assertEpsilonEquals(3.4142f, this.testingBounds.distanceMax(this.P4));

		// From box's vertex
		diagonalLength = MathUtil.distance(this.u*2.f, this.v*2.f);
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V1));
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V2));
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V3));
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V4));
		
		Point2f p = new Point2f();

		// Points near a side and on one of the axis 
		p.scaleAdd(this.u * 4.f, this.R, this.Q);
		assertEpsilonEquals(this.u*5.f, this.testingBounds.distanceMax(p));

		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		assertEpsilonEquals(this.u*5.f, this.testingBounds.distanceMax(p));
	}

	@Override
	public void testDistanceMaxSquaredP() {
		assertTrue(this.testingBounds.isInit());

		// From box's center
		float diagonalLength = MathUtil.distance(this.u, this.v);
		assertEpsilonEquals(diagonalLength*diagonalLength,
				this.testingBounds.distanceMaxSquared(this.Q));
		
		// From the original points (computed with GNU octave)
		assertEpsilonEquals(3.3349f*3.3349f, this.testingBounds.distanceMaxSquared(this.P1));
		assertEpsilonEquals(2.9215f*2.9215f, this.testingBounds.distanceMaxSquared(this.P2));
		assertEpsilonEquals(3.4142f*3.4142f, this.testingBounds.distanceMaxSquared(this.P3));
		assertEpsilonEquals(3.4142f*3.4142f, this.testingBounds.distanceMaxSquared(this.P4));

		// From box's vertex
		diagonalLength = MathUtil.distance(this.u*2.f, this.v*2.f);
		assertEpsilonEquals(diagonalLength*diagonalLength,
				this.testingBounds.distanceMaxSquared(this.V1));
		assertEpsilonEquals(diagonalLength*diagonalLength,
				this.testingBounds.distanceMaxSquared(this.V2));
		assertEpsilonEquals(diagonalLength*diagonalLength,
				this.testingBounds.distanceMaxSquared(this.V3));
		assertEpsilonEquals(diagonalLength*diagonalLength,
				this.testingBounds.distanceMaxSquared(this.V4));
	}

	@Override
	public void testSetP() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f p = randomPoint2D();
		
		this.testingBounds.set(p);
		
		assertTrue(this.testingBounds.isInit());
		assertTrue(this.testingBounds.isEmpty());

		assertEquals(p, this.testingBounds.getCenter());
		assertEquals(0.f, this.testingBounds.getRExtent());
		assertEquals(0.f, this.testingBounds.getSExtent());

		assertEquals(new Vector2f(1.f,0.f), this.testingBounds.getOrientedBoundAxis()[0]);
		assertEquals(new Vector2f(0.f,1.f), this.testingBounds.getOrientedBoundAxis()[1]);
	}

	@Override
	public void testSetTuple2fArray() {
		assertTrue(this.testingBounds.isInit());
		
		Point2f[] points = randomPoints2D();
		Vector2f expectedR = new Vector2f();
		Vector2f expectedS = new Vector2f();
		OrientedBoundingRectangle.computeOBRAxis(points, expectedR, expectedS);
		Point2f expectedCenter = new Point2f();
		float[] expectedExtents = new float[2];
		OrientedBoundingRectangle.computeOBRCenterExtents(points, expectedR, expectedS, expectedCenter, expectedExtents);
		
		this.testingBounds.set(points);
		
		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
		assertEpsilonEquals(expectedExtents, this.testingBounds.getExtent());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
	}

	@Override
	public void testSetCB() {
		assertTrue(this.testingBounds.isInit());
		
		{
			OrientedBoundingRectangle obr = new OrientedBoundingRectangle();
			obr.set(randomPoints2D());

			this.testingBounds.set(obr);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(obr.getCenter(), this.testingBounds.getCenter());
			assertEpsilonEquals(obr.getExtent(), this.testingBounds.getExtent());

			assertEpsilonEquals(obr.getOrientedBoundAxis()[0], this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(obr.getOrientedBoundAxis()[1], this.testingBounds.getOrientedBoundAxis()[1]);
		}

		{
			MinimumBoundingRectangle aabb = new MinimumBoundingRectangle();
			aabb.set(randomPoints2D());
			Vector2f mbrR, mbrS;
			float eR, eS;
			mbrR = new Vector2f(1.f,0.f);
			mbrS = new Vector2f(0.f,1.f);
			eR = aabb.getSizeX() / 2.f;
			eS = aabb.getSizeY() / 2.f;

			this.testingBounds.set(aabb);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(aabb.getCenter(), this.testingBounds.getCenter());

			assertEpsilonEquals(mbrR, this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(mbrS, this.testingBounds.getOrientedBoundAxis()[1]);

			assertEpsilonEquals(new float[] {eR, eS}, this.testingBounds.getExtent());
		}

		{
			BoundingCircle sphere = new BoundingCircle();
			sphere.set(randomPoints2D());
			Vector2f sphereR, sphereS;
			float eR, eS;
			sphereR = new Vector2f(1.f,0.f);
			sphereS = new Vector2f(0.f,1.f);
			eR = sphere.getRadius();
			eS = sphere.getRadius();
			
			this.testingBounds.set(sphere);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(sphere.getCenter(), this.testingBounds.getCenter());

			assertEpsilonEquals(sphereR, this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(sphereS, this.testingBounds.getOrientedBoundAxis()[1]);

			assertEpsilonEquals(new float[] {eR, eS}, this.testingBounds.getExtent());
		}
	}
	
	@Override
	public void testCombineP() {
		assertTrue(this.testingBounds.isInit());
		
		Collection<Point2f> allPoints = new ArrayList<Point2f>();
		Point2f point = randomPoint2D();
		Iterator<? extends Point2f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
		while (iterator.hasNext()) {
			allPoints.add(iterator.next());
		}
		allPoints.add(point);
		Vector2f expectedR = new Vector2f();
		Vector2f expectedS = new Vector2f();
		OrientedBoundingRectangle.computeOBRAxis(allPoints, expectedR, expectedS);
		Point2f expectedCenter = new Point2f();
		float[] expectedExtents = new float[2];
		OrientedBoundingRectangle.computeOBRCenterExtents(allPoints, expectedR, expectedS, expectedCenter, expectedExtents);
		
		this.testingBounds.combine(point);
		
		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
		assertEpsilonEquals(expectedExtents, this.testingBounds.getExtent());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
	}

	@Override
	public void testCombineTuple2fArray() {
		assertTrue(this.testingBounds.isInit());
		
		Collection<Point2f> allPoints = new ArrayList<Point2f>();
		Point2f[] points = randomPoints2D();
		Iterator<? extends Point2f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
		while (iterator.hasNext()) {
			allPoints.add(iterator.next());
		}
		allPoints.addAll(Arrays.asList(points));
		Vector2f expectedR = new Vector2f();
		Vector2f expectedS = new Vector2f();
		OrientedBoundingRectangle.computeOBRAxis(allPoints, expectedR, expectedS);
		Point2f expectedCenter = new Point2f();
		float[] expectedExtents = new float[2];
		OrientedBoundingRectangle.computeOBRCenterExtents(allPoints, expectedR, expectedS, expectedCenter, expectedExtents);
		
		this.testingBounds.combine(points);
		
		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
		assertEpsilonEquals(expectedExtents, this.testingBounds.getExtent());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
	}

	@Override
	public void testCombineCB() {
		assertTrue(this.testingBounds.isInit());
		
		{
			OrientedBoundingRectangle obr = new OrientedBoundingRectangle();
			obr.set(randomPoints2D());
			
			Collection<Point2f> allPoints = new ArrayList<Point2f>();
			Iterator<? extends Point2f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			iterator = obr.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			Vector2f expectedR = new Vector2f();
			Vector2f expectedS = new Vector2f();
			OrientedBoundingRectangle.computeOBRAxis(allPoints, expectedR, expectedS);
			Point2f expectedCenter = new Point2f();
			float[] expectedExtents = new float[2];
			OrientedBoundingRectangle.computeOBRCenterExtents(allPoints, expectedR, expectedS, expectedCenter, expectedExtents);

			this.testingBounds.combine(obr);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
			assertEpsilonEquals(expectedExtents, this.testingBounds.getExtent());

			assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
		}

		{
			MinimumBoundingRectangle aabb = new MinimumBoundingRectangle();
			aabb.set(randomPoints2D());
			
			Collection<Point2f> allPoints = new ArrayList<Point2f>();
			Iterator<? extends Point2f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			iterator = aabb.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			Vector2f expectedR = new Vector2f();
			Vector2f expectedS = new Vector2f();
			OrientedBoundingRectangle.computeOBRAxis(allPoints, expectedR, expectedS);
			Point2f expectedCenter = new Point2f();
			float[] expectedExtents = new float[2];
			OrientedBoundingRectangle.computeOBRCenterExtents(allPoints, expectedR, expectedS, expectedCenter, expectedExtents);

			this.testingBounds.combine(aabb);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
			assertEpsilonEquals(expectedExtents, this.testingBounds.getExtent());

			assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
		}

		{
			BoundingCircle sphere = new BoundingCircle();
			sphere.set(randomPoints2D());
			
			Collection<Point2f> allPoints = new ArrayList<Point2f>();
			Iterator<? extends Point2f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			iterator = sphere.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			Vector2f expectedR = new Vector2f();
			Vector2f expectedS = new Vector2f();
			OrientedBoundingRectangle.computeOBRAxis(allPoints, expectedR, expectedS);
			Point2f expectedCenter = new Point2f();
			float[] expectedExtents = new float[2];
			OrientedBoundingRectangle.computeOBRCenterExtents(allPoints, expectedR, expectedS, expectedCenter, expectedExtents);

			this.testingBounds.combine(sphere);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
			assertEpsilonEquals(expectedExtents, this.testingBounds.getExtent());

			assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
		}
	}

	@Override
	public void testCombineCollection() {
		assertTrue(this.testingBounds.isInit());
		
		Collection<CombinableBounds2D> bounds = new ArrayList<CombinableBounds2D>();
		Collection<Point2f> allPoints = new ArrayList<Point2f>();
		
		Iterator<? extends Point2f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
		while (iterator.hasNext()) {
			allPoints.add(iterator.next());
		}

		{
			OrientedBoundingRectangle obr = new OrientedBoundingRectangle();
			obr.set(randomPoints2D());
			bounds.add(obr);
			
			iterator = obr.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
		}

		{
			MinimumBoundingRectangle mbr = new MinimumBoundingRectangle();
			mbr.set(randomPoints2D());
			bounds.add(mbr);
			
			iterator = mbr.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
		}

		{
			BoundingCircle circle = new BoundingCircle();
			circle.set(randomPoints2D());
			bounds.add(circle);
			
			iterator = circle.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
		}

		Vector2f expectedR = new Vector2f();
		Vector2f expectedS = new Vector2f();
		OrientedBoundingRectangle.computeOBRAxis(allPoints, expectedR, expectedS);
		Point2f expectedCenter = new Point2f();
		float[] expectedExtents = new float[2];
		OrientedBoundingRectangle.computeOBRCenterExtents(allPoints, expectedR, expectedS, expectedCenter, expectedExtents);
		
		this.testingBounds.combine(bounds);

		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
		assertEpsilonEquals(expectedExtents, this.testingBounds.getExtent());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
	}

	//-------------------------------------------
	// OrientedBoundingRectangle dedicated tests
	//-------------------------------------------
	
	/**
	 */
	@Override
	public void testNearestPointP() {
		assertTrue(this.testingBounds.isInit());

		assertEpsilonEquals(this.Q, this.testingBounds.nearestPoint(this.Q));

		assertEpsilonEquals(this.V1, this.testingBounds.nearestPoint(this.V1));
		assertEpsilonEquals(this.V2, this.testingBounds.nearestPoint(this.V2));
		assertEpsilonEquals(this.V3, this.testingBounds.nearestPoint(this.V3));
		assertEpsilonEquals(this.V4, this.testingBounds.nearestPoint(this.V4));

		assertEpsilonEquals(this.P1, this.testingBounds.nearestPoint(this.P1));
		assertEpsilonEquals(this.P2, this.testingBounds.nearestPoint(this.P2));
		assertEpsilonEquals(this.P3, this.testingBounds.nearestPoint(this.P3));
		assertEpsilonEquals(this.P4, this.testingBounds.nearestPoint(this.P4));

		Point2f p = new Point2f();
		
		// Near V1
		p.scaleAdd(this.u * 3.f, this.R, this.Q);
		p.scaleAdd(this.v * 4.f, this.S, p);
		assertEpsilonEquals(this.V1, this.testingBounds.nearestPoint(p));
	}

	/**
	 */
	@Override
	public void testFarestPointP() {
		assertTrue(this.testingBounds.isInit());

		assertEpsilonEquals(this.V4, this.testingBounds.farestPoint(this.Q));

		assertEpsilonEquals(this.V4, this.testingBounds.farestPoint(this.V1));
		assertEpsilonEquals(this.V3, this.testingBounds.farestPoint(this.V2));
		assertEpsilonEquals(this.V2, this.testingBounds.farestPoint(this.V3));
		assertEpsilonEquals(this.V1, this.testingBounds.farestPoint(this.V4));

		// Farest points are computed with Octave
		assertEpsilonEquals(this.V1, this.testingBounds.farestPoint(this.P1));
		assertEpsilonEquals(this.V4, this.testingBounds.farestPoint(this.P2));
		assertEpsilonEquals(this.V3, this.testingBounds.farestPoint(this.P3));
		assertEpsilonEquals(this.V3, this.testingBounds.farestPoint(this.P4));

		Point2f p = new Point2f();
		
		// Near V8 -> V1
		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		p.scaleAdd(-this.v * 3.f, this.S, p);
		assertEpsilonEquals(this.V1, this.testingBounds.farestPoint(p));

		// Near V5 -> V2
		p.scaleAdd(-this.u * 5.f, this.R, this.Q);
		p.scaleAdd(this.v * 8.f, this.S, p);
		assertEpsilonEquals(this.V2, this.testingBounds.farestPoint(p));
	}

	//-------------------------------------------
	// IntersectionClassifier
	//-------------------------------------------

	private Point2f mkPt(Point2f p) {
		Vector2f vv = new Vector2f();
		vv.sub(this.Q, p);
		vv.scale(MathUtil.getEpsilon());
		Point2f p2 = new Point2f(p);
		p2.add(vv);
		return p2;
	}
	
	@Override
	public void testClassifiesP() {
		assertTrue(this.testingBounds.isInit());

		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(this.Q));
		
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(mkPt(this.P1)));
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(mkPt(this.P2)));
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(mkPt(this.P3)));
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(mkPt(this.P3)));

		Point2f p = new Point2f();
		
		p.setX(-1000.f);
		p.setY(-1000.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));

		p.setX(1.7f);
		p.setY(0.5f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
	}

	@Override
	public void testIntersectsP() {
		assertTrue(this.testingBounds.isInit());

		assertTrue(this.testingBounds.intersects(this.Q));
		
		assertTrue(this.testingBounds.intersects(mkPt(this.P1)));
		assertTrue(this.testingBounds.intersects(mkPt(this.P2)));
		assertTrue(this.testingBounds.intersects(mkPt(this.P3)));
		assertTrue(this.testingBounds.intersects(mkPt(this.P4)));

		Point2f p = new Point2f();
		
		p.setX(-1000.f);
		p.setY(-1000.f);
		assertFalse(this.testingBounds.intersects(p));

		p.setX(1.7f);
		p.setY(0.5f);
		assertFalse(this.testingBounds.intersects(p));
	}

	@Override
	public void testClassifiesPP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p1 = new Point2f();
		Point2f p2 = new Point2f();
		
		float minE = MathUtil.min(this.u, this.v);
		float maxE = MathUtil.max(this.u, this.v);
		
		p1.setX(this.Q.getX() - minE / 10.f);
		p1.setY(this.Q.getY() - minE / 10.f);
		p2.setX(this.Q.getX() + minE / 10.f);
		p2.setY(this.Q.getY() + minE / 10.f);
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(p1,p2));

		p1.setX(this.Q.getX() - maxE * 10.f);
		p1.setY(this.Q.getY() - maxE * 10.f);
		p2.setX(this.Q.getX() + maxE * 10.f);
		p2.setY(this.Q.getY() + maxE * 10.f);
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(p1,p2));

		p1.setX(this.Q.getX() - 1000.f);
		p1.setY(this.Q.getY() - 10.f);
		p2.setX(this.Q.getX() - 900.f);
		p2.setY(this.Q.getY() + 10.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p1,p2));

		p1.setX(-10.f);
		p1.setY(-10.f);
		p2.setX(0.5f);
		p2.setY(10.f);
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));

		p1.setX(-10.f);
		p1.setY(-10.f);
		p2.setX(this.Q.getX());
		p2.setY(this.Q.getY());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));

		p1.setX(this.Q.getX());
		p1.setY(this.Q.getY());
		p2.setX(this.Q.getX()+1);
		p2.setY(this.Q.getY()+1);
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));
	}

	@Override
	public void testIntersectsPP() {
		assertTrue(this.testingBounds.isInit());

		Point2f p1 = new Point2f();
		Point2f p2 = new Point2f();
		
		float minE = MathUtil.min(this.u, this.v);
		float maxE = MathUtil.max(this.u, this.v);
		
		p1.setX(this.Q.getX() - minE / 10.f);
		p1.setY(this.Q.getY() - minE / 10.f);
		p2.setX(this.Q.getX() + minE / 10.f);
		p2.setY(this.Q.getY() + minE / 10.f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		p1.setX(this.Q.getX() - maxE * 10.f);
		p1.setY(this.Q.getY() - maxE * 10.f);
		p2.setX(this.Q.getX() + maxE * 10.f);
		p2.setY(this.Q.getY() + maxE * 10.f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		p1.setX(this.Q.getX() - 1000.f);
		p1.setY(this.Q.getY() - 10.f);
		p2.setX(this.Q.getX() - 900.f);
		p2.setY(this.Q.getY() + 10.f);
		assertFalse(this.testingBounds.intersects(p1,p2));

		p1.setX(-10.f);
		p1.setY(-10.f);
		p2.setX(0.5f);
		p2.setY(10.f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		p1.setX(-10.f);
		p1.setY(-10.f);
		p2.setX(this.Q.getX());
		p2.setY(this.Q.getY());
		assertTrue(this.testingBounds.intersects(p1,p2));

		p1.setX(this.Q.getX());
		p1.setY(this.Q.getY());
		p2.setX(this.Q.getX()+1);
		p2.setY(this.Q.getY()+1);
		assertTrue(this.testingBounds.intersects(p1,p2));
	}

	@Override
	public void testClassifiesPoint2fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point2f c = new Point2f();
		float r;
		
		float minE = MathUtil.min(this.u, this.v);
		float maxE = MathUtil.max(this.u, this.v);

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		r = minE / 10.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(c, r));
		
		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		r = minE - 1e-4f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		r = 0.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		r = maxE * 10.f;
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(c, r));

		c.setX(this.P1.getX());
		c.setY(this.P1.getY()+minE/10.f);
		r = maxE * 50.f;
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(c, r));

		c.setX(-10.f);
		c.setY(-10.f);
		r = 1.f;
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		r = maxE + (float) MathConstants.EPSILON;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(c, r));

		c.setX(-2.f);
		c.setY(this.Q.getY());
		r = 1.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(c, r));
	}

	@Override
	public void testIntersectsPoint2fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point2f c = new Point2f();
		float r;
		
		float minE = MathUtil.min(this.u, this.v);
		float maxE = MathUtil.max(this.u, this.v);

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		r = minE / 10.f;
		assertTrue(this.testingBounds.intersects(c, r));
		
		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		r = minE;
		assertTrue(this.testingBounds.intersects(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		r = 0.f;
		assertTrue(this.testingBounds.intersects(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		r = maxE * 10.f;
		assertTrue(this.testingBounds.intersects(c, r));

		c.setX(this.P1.getX());
		c.setY(this.P1.getY());
		r = maxE * 50.f;
		assertTrue(this.testingBounds.intersects(c, r));

		c.setX(-10.f);
		c.setY(-10.f);
		r = 1.f;
		assertFalse(this.testingBounds.intersects(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		r = maxE;
		assertTrue(this.testingBounds.intersects(c, r));

		c.setX(-2.f);
		c.setY(this.Q.getY());
		r = 1.f;
		assertTrue(this.testingBounds.intersects(c, r));
	}

	@Override
	public void testClassifiesPoint2fVector2fArrayFloatArray() {
		assertTrue(this.testingBounds.isInit());

		Point2f center = new Point2f();
		Vector2f[] axis = new Vector2f[] {
				new Vector2f(),
				new Vector2f()
		};
		float[] extent = new float[2];
		
		center.setX(this.testingBounds.getCenterX());
		center.setY(this.testingBounds.getCenterY());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		extent[0] = this.testingBounds.getRExtent() / 2.f;
		extent[1] = this.testingBounds.getSExtent() / 2.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(center, axis, extent));

		center.setX(this.testingBounds.getCenterX());
		center.setY(this.testingBounds.getCenterY());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		extent[0] = this.testingBounds.getRExtent() * 2.f;
		extent[1] = this.testingBounds.getSExtent() * 2.f;
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(center, axis, extent));

		center.setX(this.testingBounds.getLower().getX() - 20.f);
		center.setY(this.testingBounds.getCenterY());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(center, axis, extent));

		center.setX(this.testingBounds.getLower().getX());
		center.setY(this.testingBounds.getCenterY());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, axis, extent));

		center.setX((this.testingBounds.getLower().getX() + this.Q.getX()) / 2.f);
		center.setY(this.testingBounds.getCenterY());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, axis, extent));

		center.setX(0.f);
		center.setY(0.f);
		axis[0].setX(1.f);
		axis[0].setY(0.f);
		axis[1].setX(0.f);
		axis[1].setY(1.f);
		extent[0] = 1.f;
		extent[1] = 1.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, axis, extent));
	}

	@Override
	public void testIntersectsPoint2fVector2fArrayFloatArray() {
		assertTrue(this.testingBounds.isInit());

		Point2f center = new Point2f();
		Vector2f[] axis = new Vector2f[] {
				new Vector2f(),
				new Vector2f()
		};
		float[] extent = new float[2];
		
		center.setX(this.testingBounds.getCenterX());
		center.setY(this.testingBounds.getCenterY());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		extent[0] = this.testingBounds.getRExtent() / 2.f;
		extent[1] = this.testingBounds.getSExtent() / 2.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		center.setX(this.testingBounds.getCenterX());
		center.setY(this.testingBounds.getCenterY());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		extent[0] = this.testingBounds.getRExtent() * 2.f;
		extent[1] = this.testingBounds.getSExtent() * 2.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		center.setX(this.testingBounds.getLower().getX() - 20.f);
		center.setY(this.testingBounds.getCenterY());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		assertFalse(this.testingBounds.intersects(center, axis, extent));

		center.setX(this.testingBounds.getLower().getX());
		center.setY(this.testingBounds.getCenterY());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		center.setX((this.testingBounds.getLower().getX() + this.Q.getX()) / 2.f);
		center.setY(this.testingBounds.getCenterY());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		center.setX(0.f);
		center.setY(0.f);
		axis[0].setX(1.f);
		axis[0].setY(0.f);
		axis[1].setX(0.f);
		axis[1].setY(1.f);
		extent[0] = 1.f;
		extent[1] = 1.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));
	}

	@Override
	public void testClassifiesIC() {
		assertTrue(this.testingBounds.isInit());

		// MinimumBoundingRectangle
		{
			Point2f p1 = new Point2f();
			Point2f p2 = new Point2f();
			
			float minE = MathUtil.min(this.u, this.v);
			float maxE = MathUtil.max(this.u, this.v);
			
			p1.setX(this.Q.getX() - minE / 10.f);
			p1.setY(this.Q.getY() - minE / 10.f);
			p2.setX(this.Q.getX() + minE / 10.f);
			p2.setY(this.Q.getY() + minE / 10.f);
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new MinimumBoundingRectangle(p1,p2)));

			p1.setX(this.Q.getX() - maxE * 10.f);
			p1.setY(this.Q.getY() - maxE * 10.f);
			p2.setX(this.Q.getX() + maxE * 10.f);
			p2.setY(this.Q.getY() + maxE * 10.f);
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new MinimumBoundingRectangle(p1,p2)));

			p1.setX(this.Q.getX() - 1000.f);
			p1.setY(this.Q.getY() - 10.f);
			p2.setX(this.Q.getX() - 900.f);
			p2.setY(this.Q.getY() + 10.f);
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new MinimumBoundingRectangle(p1,p2)));

			p1.setX(-10.f);
			p1.setY(-10.f);
			p2.setX(0.5f);
			p2.setY(10.f);
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new MinimumBoundingRectangle(p1,p2)));

			p1.setX(-10.f);
			p1.setY(-10.f);
			p2.setX(this.Q.getX());
			p2.setY(this.Q.getY());
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new MinimumBoundingRectangle(p1,p2)));

			p1.setX(this.Q.getX());
			p1.setY(this.Q.getY());
			p2.setX(this.Q.getX()+1);
			p2.setY(this.Q.getY()+1);
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new MinimumBoundingRectangle(p1,p2)));
		}
		
		// BoundingCircle
		{
			Point2f c = new Point2f();
			float r;
			
			float minE = MathUtil.min(this.u, this.v);
			float maxE = MathUtil.max(this.u, this.v);

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			r = minE / 10.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingCircle(c,r)));
			
			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			r = minE - 1e-4f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingCircle(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			r = 0.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingCircle(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			r = maxE * 10.f;
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new BoundingCircle(c,r)));

			c.setX(this.P1.getX());
			c.setY(this.P1.getY());
			r = maxE * 50.f;
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new BoundingCircle(c,r)));

			c.setX(-10.f);
			c.setY(-10.f);
			r = 1.f;
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new BoundingCircle(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			r = maxE;
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new BoundingCircle(c,r)));

			c.setX(-2.f);
			c.setY(this.Q.getY());
			r = 1.f;
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new BoundingCircle(c,r)));
		}
		
		// OrientedBoundingRectangle
		{
			Point2f center = new Point2f();
			Vector2f[] axis = new Vector2f[] {
					new Vector2f(),
					new Vector2f()
			};
			float[] extent = new float[2];
			
			center.setX(this.testingBounds.getCenterX());
			center.setY(this.testingBounds.getCenterY());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			extent[0] = this.testingBounds.getRExtent() / 2.f;
			extent[1] = this.testingBounds.getSExtent() / 2.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new OrientedBoundingRectangle(center, axis, extent)));

			center.setX(this.testingBounds.getCenterX());
			center.setY(this.testingBounds.getCenterY());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			extent[0] = this.testingBounds.getRExtent() * 2.f;
			extent[1] = this.testingBounds.getSExtent() * 2.f;
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new OrientedBoundingRectangle(center, axis, extent)));

			center.setX(this.testingBounds.getLower().getX() - 20.f);
			center.setY(this.testingBounds.getCenterY());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new OrientedBoundingRectangle(center, axis, extent)));

			center.setX(this.testingBounds.getLower().getX());
			center.setY(this.testingBounds.getCenterY());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new OrientedBoundingRectangle(center, axis, extent)));

			center.setX((this.testingBounds.getLower().getX() + this.Q.getX()) / 2.f);
			center.setY(this.testingBounds.getCenterY());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new OrientedBoundingRectangle(center, axis, extent)));

			center.setX(0.f);
			center.setY(0.f);
			axis[0].setX(1.f);
			axis[0].setY(0.f);
			axis[1].setX(0.f);
			axis[1].setY(1.f);
			extent[0] = 1.f;
			extent[1] = 1.f;
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
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
			
			float minE = MathUtil.min(this.u, this.v);
			float maxE = MathUtil.max(this.u, this.v);
			
			p1.setX(this.Q.getX() - minE / 10.f);
			p1.setY(this.Q.getY() - minE / 10.f);
			p2.setX(this.Q.getX() + minE / 10.f);
			p2.setY(this.Q.getY() + minE / 10.f);
			assertTrue(this.testingBounds.intersects(
					new MinimumBoundingRectangle(p1,p2)));

			p1.setX(this.Q.getX() - maxE * 10.f);
			p1.setY(this.Q.getY() - maxE * 10.f);
			p2.setX(this.Q.getX() + maxE * 10.f);
			p2.setY(this.Q.getY() + maxE * 10.f);
			assertTrue(this.testingBounds.intersects(
					new MinimumBoundingRectangle(p1,p2)));

			p1.setX(this.Q.getX() - 1000.f);
			p1.setY(this.Q.getY() - 10.f);
			p2.setX(this.Q.getX() - 900.f);
			p2.setY(this.Q.getY() + 10.f);
			assertFalse(this.testingBounds.intersects(
					new MinimumBoundingRectangle(p1,p2)));

			p1.setX(-10.f);
			p1.setY(-10.f);
			p2.setX(0.5f);
			p2.setY(10.f);
			assertTrue(this.testingBounds.intersects(
					new MinimumBoundingRectangle(p1,p2)));

			p1.setX(-10.f);
			p1.setY(-10.f);
			p2.setX(this.Q.getX());
			p2.setY(this.Q.getY());
			assertTrue(this.testingBounds.intersects(
					new MinimumBoundingRectangle(p1,p2)));

			p1.setX(this.Q.getX());
			p1.setY(this.Q.getY());
			p2.setX(this.Q.getX()+1);
			p2.setY(this.Q.getY()+1);
			assertTrue(this.testingBounds.intersects(
					new MinimumBoundingRectangle(p1,p2)));
		}
		
		// BoundingCircle
		{
			Point2f c = new Point2f();
			float r;
			
			float minE = MathUtil.min(this.u, this.v);
			float maxE = MathUtil.max(this.u, this.v);

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			r = minE / 10.f;
			assertTrue(this.testingBounds.intersects(
					new BoundingCircle(c,r)));
			
			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			r = minE;
			assertTrue(this.testingBounds.intersects(
					new BoundingCircle(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			r = 0.f;
			assertTrue(this.testingBounds.intersects(
					new BoundingCircle(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			r = maxE * 10.f;
			assertTrue(this.testingBounds.intersects(
					new BoundingCircle(c,r)));

			c.setX(this.P1.getX());
			c.setY(this.P1.getY());
			r = maxE * 50.f;
			assertTrue(this.testingBounds.intersects(
					new BoundingCircle(c,r)));

			c.setX(-10.f);
			c.setY(-10.f);
			r = 1.f;
			assertFalse(this.testingBounds.intersects(
					new BoundingCircle(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			r = maxE;
			assertTrue(this.testingBounds.intersects(
					new BoundingCircle(c,r)));

			c.setX(-2.f);
			c.setY(this.Q.getY());
			r = 1.f;
			assertTrue(this.testingBounds.intersects(
					new BoundingCircle(c,r)));
		}
		
		// OrientedBoundingRectangle
		{
			Point2f center = new Point2f();
			Vector2f[] axis = new Vector2f[] {
					new Vector2f(),
					new Vector2f()
			};
			float[] extent = new float[2];
			
			center.setX(this.testingBounds.getCenterX());
			center.setY(this.testingBounds.getCenterY());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			extent[0] = this.testingBounds.getRExtent() / 2.f;
			extent[1] = this.testingBounds.getSExtent() / 2.f;
			assertTrue(this.testingBounds.intersects(
					new OrientedBoundingRectangle(center, axis, extent)));

			center.setX(this.testingBounds.getCenterX());
			center.setY(this.testingBounds.getCenterY());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			extent[0] = this.testingBounds.getRExtent() * 2.f;
			extent[1] = this.testingBounds.getSExtent() * 2.f;
			assertTrue(this.testingBounds.intersects(
					new OrientedBoundingRectangle(center, axis, extent)));

			center.setX(this.testingBounds.getLower().getX() - 20.f);
			center.setY(this.testingBounds.getCenterY());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			assertFalse(this.testingBounds.intersects(
					new OrientedBoundingRectangle(center, axis, extent)));

			center.setX(this.testingBounds.getLower().getX());
			center.setY(this.testingBounds.getCenterY());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			assertTrue(this.testingBounds.intersects(
					new OrientedBoundingRectangle(center, axis, extent)));

			center.setX((this.testingBounds.getLower().getX() + this.Q.getX()) / 2.f);
			center.setY(this.testingBounds.getCenterY());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			assertTrue(this.testingBounds.intersects(
					new OrientedBoundingRectangle(center, axis, extent)));

			center.setX(0.f);
			center.setY(0.f);
			axis[0].setX(1.f);
			axis[0].setY(0.f);
			axis[1].setX(0.f);
			axis[1].setY(1.f);
			extent[0] = 1.f;
			extent[1] = 1.f;
			assertTrue(this.testingBounds.intersects(
					new OrientedBoundingRectangle(center, axis, extent)));
		}
	}

	/**
	 */
	@Override
	public void testToBounds3D() {
		assertTrue(this.testingBounds.isInit());
		
		OrientedBoundingBox obb = this.testingBounds.toBounds3D();
		
		Point3f c = new Point3f(this.Q.getX(), this.Q.getY(), 0.f);
		Vector3f r = new Vector3f(this.R.getX(), this.R.getY(), 0.f);
		Vector3f s = new Vector3f(this.S.getX(), this.S.getY(), 0.f);
		Vector3f t = new Vector3f(0.f, 0.f, 1.f);
		Vector3f extents = new Vector3f(this.u, this.v, 0.f);
		
		assertNotNull(obb);
		assertEpsilonEquals(c, obb.getCenter());
		assertEpsilonEquals(r, obb.getR());
		assertEpsilonEquals(s, obb.getS());
		assertEpsilonEquals(t, obb.getT());
		assertEpsilonEquals(extents, obb.getOrientedBoundExtentVector());
	}
	
	/**
	 */
	@Override
	public void testToBounds3DCoordinateSystem3D() {
		assertTrue(this.testingBounds.isInit());

		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			OrientedBoundingBox obb = this.testingBounds.toBounds3D(cs);
			
			Point3f c = cs.fromCoordinateSystem2D(this.Q);
			Vector3f r = cs.fromCoordinateSystem2D(this.R);
			Vector3f s = cs.fromCoordinateSystem2D(this.S);
			Vector3f t;
			if (cs.isLeftHanded())
				t = MathUtil.crossProductLeftHand(r.getX(), r.getY(), r.getZ(), s.getX(), s.getY(), s.getZ());
			else
				t = MathUtil.crossProductRightHand(r.getX(), r.getY(), r.getZ(), s.getX(), s.getY(), s.getZ());
			Vector3f extents = cs.fromCoordinateSystem2D(new Vector2f(this.u, this.v));
			
			assertNotNull(obb);
			assertEpsilonEquals(cs.name(), c, obb.getCenter());
			assertEpsilonEquals(cs.name(), r, obb.getR());
			assertEpsilonEquals(cs.name(), s, obb.getS());
			assertEpsilonEquals(cs.name(), t, obb.getT());
			assertEpsilonEquals(cs.name(), extents, obb.getOrientedBoundExtentVector());
		}
	}

	/**
	 */
	@Override
	public void testToBounds3DFloatFloat() {
		assertTrue(this.testingBounds.isInit());
		
		OrientedBoundingBox obb = this.testingBounds.toBounds3D(3454.456f, 2343.6f);
		
		Point3f c = new Point3f(this.Q.getX(), this.Q.getY(), 3454.456);
		Vector3f r = new Vector3f(this.R.getX(), this.R.getY(), 0.f);
		Vector3f s = new Vector3f(this.S.getX(), this.S.getY(), 0.f);
		Vector3f t = new Vector3f(0.f, 0.f, 1.f);
		Vector3f extents = new Vector3f(this.u, this.v, 2343.6/2.f);
		
		assertNotNull(obb);
		assertEpsilonEquals(c, obb.getCenter());
		assertEpsilonEquals(r, obb.getR());
		assertEpsilonEquals(s, obb.getS());
		assertEpsilonEquals(t, obb.getT());
		assertEpsilonEquals(extents, obb.getOrientedBoundExtentVector());

		obb = this.testingBounds.toBounds3D(3454.456f, -2343.6f);
		
		c = new Point3f(this.Q.getX(), this.Q.getY(), 3454.456);
		r = new Vector3f(this.R.getX(), this.R.getY(), 0.f);
		s = new Vector3f(this.S.getX(), this.S.getY(), 0.f);
		t = new Vector3f(0.f, 0.f, 1.f);
		extents = new Vector3f(this.u, this.v, 2343.6/2.f);
		
		assertNotNull(obb);
		assertEpsilonEquals(c, obb.getCenter());
		assertEpsilonEquals(r, obb.getR());
		assertEpsilonEquals(s, obb.getS());
		assertEpsilonEquals(t, obb.getT());
		assertEpsilonEquals(extents, obb.getOrientedBoundExtentVector());
	}
	
	/**
	 */
	@Override
	public void testToBounds3DFloatFloatCoordinateSystem3D() {
		assertTrue(this.testingBounds.isInit());

		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			OrientedBoundingBox obb = this.testingBounds.toBounds3D(345.45f, 4542.34f, cs);
			
			Point3f c = cs.fromCoordinateSystem2D(this.Q, 345.45f);
			Vector3f r = cs.fromCoordinateSystem2D(this.R);
			Vector3f s = cs.fromCoordinateSystem2D(this.S);
			Vector3f t;
			if (cs.isLeftHanded())
				t = MathUtil.crossProductLeftHand(r.getX(), r.getY(), r.getZ(), s.getX(), s.getY(), s.getZ());
			else
				t = MathUtil.crossProductRightHand(r.getX(), r.getY(), r.getZ(), s.getX(), s.getY(), s.getZ());
			Vector3f extents = cs.fromCoordinateSystem2D(new Vector2f(this.u, this.v), 4542.34f/2.f);
			
			assertNotNull(obb);
			assertEpsilonEquals(cs.name(), c, obb.getCenter());
			assertEpsilonEquals(cs.name(), r, obb.getR());
			assertEpsilonEquals(cs.name(), s, obb.getS());
			assertEpsilonEquals(cs.name(), t, obb.getT());
			assertEpsilonEquals(cs.name(), extents, obb.getOrientedBoundExtentVector());

			obb = this.testingBounds.toBounds3D(345.45f, -4542.34f, cs);
			
			c = cs.fromCoordinateSystem2D(this.Q, 345.45f);
			r = cs.fromCoordinateSystem2D(this.R);
			s = cs.fromCoordinateSystem2D(this.S);
			if (cs.isLeftHanded())
				t = MathUtil.crossProductLeftHand(r.getX(), r.getY(), r.getZ(), s.getX(), s.getY(), s.getZ());
			else
				t = MathUtil.crossProductRightHand(r.getX(), r.getY(), r.getZ(), s.getX(), s.getY(), s.getZ());
			extents = cs.fromCoordinateSystem2D(new Vector2f(this.u, this.v), 4542.34f/2.f);
			
			assertNotNull(obb);
			assertEpsilonEquals(cs.name(), c, obb.getCenter());
			assertEpsilonEquals(cs.name(), r, obb.getR());
			assertEpsilonEquals(cs.name(), s, obb.getS());
			assertEpsilonEquals(cs.name(), t, obb.getT());
			assertEpsilonEquals(cs.name(), extents, obb.getOrientedBoundExtentVector());
		}
	}

	/**
	 */
	public void testSetVector2fTuple2fArray() {
		Point2f[] points = randomPoints2D();

		Vector2f expectedR = randomVector2D();
		expectedR.normalize();
		Vector2f expectedS = MathUtil.perpendicularVector(expectedR);
		expectedS.normalize();

		Point2f expectedCenter = new Point2f();
		float[] expectedExtents = new float[2];
		OrientedBoundingRectangle.computeOBRCenterExtents(points, expectedR, expectedS, expectedCenter, expectedExtents);
		
		this.testingBounds.set(expectedR, points);
		
		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
		assertEpsilonEquals(expectedExtents, this.testingBounds.getExtent());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
	}

	/**
	 */
	public void testSetVector2fTuple2fCollection() {
		Point2f[] points = randomPoints2D();

		Vector2f expectedR = randomVector2D();
		expectedR.normalize();
		Vector2f expectedS = MathUtil.perpendicularVector(expectedR);
		expectedS.normalize();

		Point2f expectedCenter = new Point2f();
		float[] expectedExtents = new float[2];
		OrientedBoundingRectangle.computeOBRCenterExtents(points, expectedR, expectedS, expectedCenter, expectedExtents);
		
		this.testingBounds.set(expectedR, Arrays.asList(points));
		
		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
		assertEpsilonEquals(expectedExtents, this.testingBounds.getExtent());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
	}

	/**
	 */
	public void testSetFromBoundsVector2fBounds2DTuple2fCollection() {
		Collection<Bounds2D> bounds = new ArrayList<Bounds2D>();
		int size = this.RANDOM.nextInt(20)+5;
		for(int i=0; i<size; ++i) {
			Point2f[] points = randomPoints2D();
			OrientedBoundingRectangle obr = new OrientedBoundingRectangle();
			obr.combine(points);
			bounds.add(obr);
		}

		Vector2f expectedR = randomVector2D();
		expectedR.normalize();
		Vector2f expectedS = MathUtil.perpendicularVector(expectedR);
		expectedS.normalize();

		this.testingBounds.setFromBounds(expectedR, bounds);
		
		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
	}

	/**
	 */
	public void testSetFromBoundsVector2fBounds2DArray() {
		int size = this.RANDOM.nextInt(20)+5;
		Bounds2D[] bounds = new Bounds2D[size];
		for(int i=0; i<size; ++i) {
			Point2f[] points = randomPoints2D();
			OrientedBoundingRectangle obr = new OrientedBoundingRectangle();
			obr.combine(points);
			bounds[i] = obr;
		}

		Vector2f expectedR = randomVector2D();
		expectedR.normalize();
		Vector2f expectedS = MathUtil.perpendicularVector(expectedR);
		expectedS.normalize();

		this.testingBounds.setFromBounds(expectedR, bounds);
		
		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
	}

}
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
package org.arakhne.afc.math.bounds.bounds3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.bounds.bounds2f.OrientedBoundingRectangle;
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
import org.arakhne.afc.math.matrix.Matrix4f;
import org.arakhne.afc.math.plane.PlanarClassificationType;
import org.arakhne.afc.math.plane.Plane;
import org.arakhne.afc.math.plane.Plane4f;
import org.arakhne.afc.math.plane.XYPlane;
import org.arakhne.afc.math.plane.XZPlane;
import org.arakhne.afc.math.plane.YZPlane;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.sizediterator.SizedIterator;

import junit.framework.AssertionFailedError;


/**
 * Unit test for OrientedBoundingBox class.
 * <p>
 * From "Mathematics for 3D Game Programming and Computer Graphics" pp.220
 * 
 * P1 = [ -1, -2, 1 ]
 * P2 = [ 1, 0, 2 ]
 * P3 = [ 2, -1, 3 ]
 * P4 = [ 2, -1, 2 ]
 * 
 * average: m = (P1+P2+P3+P4)/4 = [ 1, -1, 2 ]
 *  
 * Cov = [ 1.5 ,  .5 ,  .75 ]
 * 		 [  .5 ,  .5 ,  .25 ]
 * 		 [  .75,  .25,  .5  ]
 * 
 * Eigen components are (computed with GNU Octave):
 * [v,d] = eig(Cov)
 * 
 * Eigenvalues are:
 * diag(d) = [0.09756, 0.3055, 2.096939]
 * 
 * Eigenvectors are:
 * R = v(:,3) = [ 0.833, 0.33, 0.443 ]
 * S = -v(:,2) = [ -0.257, 0.941, -0.218 ]
 * T = -v(:,1) = [ -0.489, 0.0675, 0.870 ]
 * 
 * Point projects are:
 * P1.R = -1.05			P1.S = -1.84		P1.T = 1.22
 * P2.R = 1.72			P2.S = -0.693		P2.T = 1.25
 * P3.R = 2.67			P3.S = -2.11		P3.T = 1.56
 * P4.R = 2.22			P1.S = -1.84		P1.T = 0.695
 * 
 * OBB planes are:
 * <R,1.05>				<-R,2.67>
 * <S,2.11>				<-S,-0693>
 * <T,-0.695>			<-T,1.56>
 * 
 * Inner parameters are (computed with GNU Octave):
 * a = (min(Pi.R) + max(Pi.R)) / 2 = ( -1.05 + 2.67 ) / 2 = 0.81
 * b = (min(Pi.S) + max(Pi.S)) / 2 = ( -2.11 - 0.693 ) / 2 = -1.4015
 * c = (min(Pi.T) + max(Pi.T)) / 2 = ( 0.695 + 1.56 ) / 2 = 1.1275
 * 
 * OBB center is (computed with GNU Octave):
 * Q = aR + bS + cT = [ 0.48357, -0.97541, 1.64528 ]
 * 
 * OBB Extents are (computed with GNU Octave):
 * u = (max(Pi.R) - min(Pi.R)) / 2 = ( 2.67 + 1.05 ) / 2 = 1.8600
 * v = (max(Pi.S) - min(Pi.S)) / 2 = ( -0.693 + 2.11 ) / 2 = 0.70850
 * w = (max(Pi.T) - min(Pi.T)) / 2 = ( 1.56 - .695 ) / 2 = 0.43250
 * 
 * OBB vectors from center to vertices are (computed with GNU Octave):
 * E1 =   uR + vS + wT = [ 1.1558, 1.3097, 1.0458 ]
 * E2 =   uR + vS - wT = [ 1.57879, 1.25130, 0.29325 ]
 * E3 =   uR - vS + wT = [ 1.519972, -0.023705, 1.354708 ]
 * E4 =   uR - vS - wT = [ 1.942957, -0.082092, 0.602158 ]
 * E5 = - uR + vS + wT = [ -1.942957, 0.082092, -0.602158 ]
 * E6 = - uR + vS - wT = [ -1.519972, 0.023705, -1.354708 ]
 * E7 = - uR - vS + wT = [ -1.57879, -1.25130, -0.29325 ]
 * E8 = - uR - vS - wT = [ -1.1558, -1.3097, -1.0458 ]
 * 
 * OBB vertices are (computed with GNU Octave):
 * V1 = Q + E1 = [ 1.63937, 0.33428, 2.69108 ]
 * V2 = Q + E2 = [ 2.06236, 0.27589, 1.93853 ]
 * V3 = Q + E3 = [ 2.00354, -0.99911, 2.99999 ]
 * V4 = Q + E4 = [ 2.4265, -1.0575, 2.2474 ]
 * V5 = Q + E5 = [ -1.45939, -0.89332, 1.04312 ]
 * V6 = Q + E6 = [ -1.03640, -0.95171, 0.29057 ]
 * V7 = Q + E7 = [ -1.0952, -2.2267, 1.3520 ]
 * V8 = Q + E8 = [ -0.67223, -2.28510, 0.59948 ]
 * 
 * @author $Author: sgalland$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedBoundingBoxTest extends AbstractOrientedBound3DTest {

	private final Point3f P1 = new Point3f(-1., -2.f, 1.);
	private final Point3f P2 = new Point3f(1., 0.f, 2.);
	private final Point3f P3 = new Point3f(2.f, -1., 3.);
	private final Point3f P4 = new Point3f(2.f, -1., 2.);
	
	//private final Point3f m = new Point3f(1., -1., 2.);
	//private final Matrix3d cov = new Matrix3d(1.5 ,  .5 ,  .75, .5 ,  .5 ,  .25, .75,  .25,  .5);	
	//private final float eigenvalue1 = 2.097;
	//private final float eigenvalue2 = 0.3055;
	//private final float eigenvalue3 = 0.09756;	
	private final Vector3f R = new Vector3f(-0.257, 0.941, -0.218);
	private final Vector3f S = new Vector3f(-0.489, 0.0675, 0.870);
	private final Vector3f T = new Vector3f(0.833, 0.33, 0.443);
	
	//private final float a = 0.81;
	//private final float b = -1.4015;
	//private final float c = 1.1275;	
	private final Point3f Q = new Point3f(0.48357, -0.97541, 1.64528);

	private final float u = 0.70850f;
	private final float v = 0.43250f;
	private final float w = 1.8600f;
	
	private final Vector3f E1 = new Vector3f(1.1558, 1.3097, 1.0458);
	private final Vector3f E2 = new Vector3f(1.57879, 1.25130, 0.29325);
	private final Vector3f E3 = new Vector3f(1.519972, -0.023705, 1.354708);
	private final Vector3f E4 = new Vector3f(1.942957, -0.082092, 0.602158);
	private final Vector3f E5 = new Vector3f(-1.942957, 0.082092, -0.602158);
	private final Vector3f E6 = new Vector3f(-1.519972, 0.023705, -1.354708);
	private final Vector3f E7 = new Vector3f(-1.57879, -1.25130, -0.29325);
	private final Vector3f E8 = new Vector3f(-1.1558, -1.3097, -1.0458);

	private final Point3f V1 = new Point3f(1.63937, 0.33428, 2.69108);
	private final Point3f V2 = new Point3f(2.06236, 0.27589, 1.93853);
	private final Point3f V3 = new Point3f(2.00354, -0.99911, 2.99999);
	private final Point3f V4 = new Point3f(2.4265, -1.0575, 2.2474);
	private final Point3f V5 = new Point3f(-1.45939, -0.89332, 1.04312);
	private final Point3f V6 = new Point3f(-1.03640, -0.95171, 0.29057);
	private final Point3f V7 = new Point3f(-1.0952, -2.2267, 1.3520);
	private final Point3f V8 = new Point3f(-0.67223, -2.28510, 0.59948);
	
	/** Reference instance.
	 */
	private OrientedBoundingBox testingBounds;
	
	/** aR = u * R
	 */
	private Vector3f aR;
	/** bS = v * S
	 */
	private Vector3f bS;
	/** cT = w * T
	 */
	private Vector3f cT;
	/** maR = -u * R
	 */
	private Vector3f maR;
	/** mbS = -v * S
	 */
	private Vector3f mbS;
	/** mcT = -w * T
	 */
	private Vector3f mcT;
	
	/** Expected vertices
	 */
	private Point3f[] expectedVertices;
	
	/** Expected axis vectors from center to vertices
	 */
	private Vector3f[] expectedVectors;

	/**
	 * {@inheritDoc}
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.aR = new Vector3f();
		this.aR.scale(this.u, this.R);
		this.bS = new Vector3f();
		this.bS.scale(this.v, this.S);
		this.cT = new Vector3f();
		this.cT.scale(this.w, this.T);
		this.maR = new Vector3f(this.aR);
		this.maR.negate();
		this.mbS = new Vector3f(this.bS);
		this.mbS.negate();
		this.mcT = new Vector3f(this.cT);
		this.mcT.negate();
		this.expectedVertices = new Point3f[] {
			this.V1, this.V5, this.V7, this.V3, this.V4, this.V2, this.V6, this.V8
		};
		this.expectedVectors = new Vector3f[] {
				this.E1,
				this.E3,
				this.E4,
				this.E2,
				this.E6,
				this.E5,
				this.E7,
				this.E8
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
		this.aR = this.bS = this.cT = this.maR = this.mbS = this.mcT = null;
		this.expectedVertices = null;
		this.expectedVectors = null;
		this.testingBounds = null;
		super.tearDown();
	}
	
	/**
	 * @param points
	 * @return a bounds
	 */
	private OrientedBoundingBox createBounds(Tuple3f... points) {
		OrientedBoundingBox obb = new OrientedBoundingBox();
		obb.set(points);
		return obb;
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

	/**
	 */
	public void testOrientedBoundingBoxTuple3fArray() {
		Point3f center = this.testingBounds.getCenter();
		Vector3f extents = this.testingBounds.getOrientedBoundExtentVector();
		Vector3f[] axis = this.testingBounds.getOrientedBoundAxis();
		
		setDecimalPrecision(2);
		assertEpsilonEquals(this.R, axis[0]);
		assertEpsilonEquals(this.S, axis[1]);
		assertEpsilonEquals(this.T, axis[2]);
		assertEpsilonEquals(this.Q, center);
		assertEpsilonEquals(new Vector3f(this.u, this.v, this.w), extents);
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
		
		assertEquals(PseudoHamelDimension.DIMENSION_3D, this.testingBounds.getMathematicalDimension());
	}

	/**
	 */
	@Override
	public void testGetOrientedBoundAxis() {
		assertTrue(this.testingBounds.isInit());
		
		Vector3f[] axis = this.testingBounds.getOrientedBoundAxis();
		
		assertEpsilonEquals(this.R, axis[0]);
		assertEpsilonEquals(this.S, axis[1]);
		assertEpsilonEquals(this.T, axis[2]);
	}

	/**
	 */
	@Override
	public void testGetOrientedBoundExtents() {
		assertTrue(this.testingBounds.isInit());

		Vector3f extents = this.testingBounds.getOrientedBoundExtentVector();
		
		assertEpsilonEquals(this.u, extents.getX());
		assertEpsilonEquals(this.v, extents.getY());
		assertEpsilonEquals(this.w, extents.getZ());
	}

	/**
	 */
	@Override
	public void testGetR() {
		Vector3f axis = this.testingBounds.getR();
		assertEpsilonEquals(this.R, axis);
	}
	
	/**
	 */
	@Override
	public void testGetS() {
		Vector3f axis = this.testingBounds.getS();
		assertEpsilonEquals(this.S, axis);
	}

	/**
	 */
	@Override
	public void testGetT() {
		Vector3f axis = this.testingBounds.getT();
		assertEpsilonEquals(this.T, axis);
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

	/**
	 */
	@Override
	public void testGetTExtent() {
		float extent = this.testingBounds.getTExtent();
		assertEpsilonEquals(this.w, extent);
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
	public void testGetCenterZ() {
		assertTrue(this.testingBounds.isInit());
		
		float center = this.testingBounds.getCenterZ();
		assertEpsilonEquals(this.Q.getZ(), center);
	}

	@Override
	public void testGetCenter() {
		assertTrue(this.testingBounds.isInit());
	
		Point3f center = this.testingBounds.getCenter();
		assertEpsilonEquals(this.Q, center);
	}

	@Override
	public void testGetPosition() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f center = this.testingBounds.getPosition();
		assertEpsilonEquals(this.Q, center);
	}

	@Override
	public void testGetMaxX() {
		assertTrue(this.testingBounds.isInit());
		
		float pos = this.testingBounds.getMaxX();
				
		float expectedPos = Float.NEGATIVE_INFINITY;
		for(Point3f p : this.expectedVertices) {
			if (p.getX()>expectedPos) expectedPos = p.getX();
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetMaxY() {
		assertTrue(this.testingBounds.isInit());
		
		float pos = this.testingBounds.getMaxY();
				
		float expectedPos = Float.NEGATIVE_INFINITY;
		for(Point3f p : this.expectedVertices) {
			if (p.getY()>expectedPos) expectedPos = p.getY();
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetMaxZ() {
		assertTrue(this.testingBounds.isInit());
		
		float pos = this.testingBounds.getMaxZ();
				
		float expectedPos = Float.NEGATIVE_INFINITY;
		for(Point3f p : this.expectedVertices) {
			if (p.getZ()>expectedPos) expectedPos = p.getZ();
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetMinX() {
		assertTrue(this.testingBounds.isInit());
		
		float pos = this.testingBounds.getMinX();
				
		float expectedPos = Float.POSITIVE_INFINITY;
		for(Point3f p : this.expectedVertices) {
			if (p.getX()<expectedPos) expectedPos = p.getX();
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetMinY() {
		assertTrue(this.testingBounds.isInit());
		
		float pos = this.testingBounds.getMinY();
				
		float expectedPos = Float.POSITIVE_INFINITY;
		for(Point3f p : this.expectedVertices) {
			if (p.getY()<expectedPos) expectedPos = p.getY();
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetMinZ() {
		assertTrue(this.testingBounds.isInit());
		
		float pos = this.testingBounds.getMinZ();
				
		float expectedPos = Float.POSITIVE_INFINITY;
		for(Point3f p : this.expectedVertices) {
			if (p.getZ()<expectedPos) expectedPos = p.getZ();
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}


	@Override
	public void testGetLower() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f pos = this.testingBounds.getLower();
				
		Point3f expectedPos = new Point3f(Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
		for(Point3f p : this.expectedVertices) {
			if (p.getX()<expectedPos.getX()) expectedPos.setX(p.getX());
			if (p.getY()<expectedPos.getY()) expectedPos.setY(p.getY());
			if (p.getZ()<expectedPos.getZ()) expectedPos.setZ(p.getZ());
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetUpper() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f pos = this.testingBounds.getUpper();
				
		Point3f expectedPos = new Point3f(Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
		for(Point3f p : this.expectedVertices) {
			if (p.getX()>expectedPos.getX()) expectedPos.setX(p.getX());
			if (p.getY()>expectedPos.getY()) expectedPos.setY(p.getY());
			if (p.getZ()>expectedPos.getZ()) expectedPos.setZ(p.getZ());
		}
		
		assertEpsilonEquals(expectedPos, pos);
	}

	@Override
	public void testGetLowerUpper() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f l = new Point3f();
		Point3f u = new Point3f();
		
		this.testingBounds.getLowerUpper(l, u);
				
		Point3f expectedMin = new Point3f(Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
		Point3f expectedMax = new Point3f(Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
		for(Point3f p : this.expectedVertices) {
			if (p.getX()<expectedMin.getX()) expectedMin.setX(p.getX());
			if (p.getY()<expectedMin.getY()) expectedMin.setY(p.getY());
			if (p.getZ()<expectedMin.getZ()) expectedMin.setZ(p.getZ());
			if (p.getX()>expectedMax.getX()) expectedMax.setX(p.getX());
			if (p.getY()>expectedMax.getY()) expectedMax.setY(p.getY());
			if (p.getZ()>expectedMax.getZ()) expectedMax.setZ(p.getZ());
		}
		
		assertEpsilonEquals(expectedMin, l);
		assertEpsilonEquals(expectedMax, u);
	}

	/** 
	 */
	public void testGetSizeX() {
		assertTrue(this.testingBounds.isInit());
		
		float size = this.testingBounds.getSizeX();
				
		float lower = Float.POSITIVE_INFINITY;
		float upper = Float.NEGATIVE_INFINITY;
		for(Point3f p : this.expectedVertices) {
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
		for(Point3f p : this.expectedVertices) {
			if (p.getY()<lower) lower = p.getY();
			if (p.getY()>upper) upper = p.getY();
		}
		
		assertEpsilonEquals(upper - lower, size);
	}

	/** 
	 */
	public void testGetSizeZ() {
		assertTrue(this.testingBounds.isInit());
		
		float size = this.testingBounds.getSizeZ();
				
		float lower = Float.POSITIVE_INFINITY;
		float upper = Float.NEGATIVE_INFINITY;
		for(Point3f p : this.expectedVertices) {
			if (p.getZ()<lower) lower = p.getZ();
			if (p.getZ()>upper) upper = p.getZ();
		}
		
		assertEpsilonEquals(upper - lower, size);
	}

	/** 
	 */
	public void testGetSize() {
		assertTrue(this.testingBounds.isInit());
		
		Vector3f size = this.testingBounds.getSize();
				
		Point3f lower = new Point3f(Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY);
		Point3f upper = new Point3f(Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY);
		for(Point3f p : this.expectedVertices) {
			if (p.getX()<lower.getX()) lower.setX(p.getX());
			if (p.getY()>upper.getY()) upper.setY(p.getY());
			if (p.getZ()<lower.getZ()) lower.setZ(p.getZ());
			if (p.getX()>upper.getX()) upper.setX(p.getX());
			if (p.getY()<lower.getY()) lower.setY(p.getY());
			if (p.getZ()>upper.getZ()) upper.setZ(p.getZ());
		}
		
		assertEpsilonEquals(new Vector3f(
				upper.getX() - lower.getX(),
				upper.getY() - lower.getY(),
				upper.getZ() - lower.getZ()), size);
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
		
		OrientedBoundingBox clone = this.testingBounds.clone();
		
		assertNotSame(clone, this.testingBounds);

		assertNotSame(this.testingBounds.getOrientedBoundAxis(), clone.getOrientedBoundAxis());
		assertEpsilonEquals(this.R, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(this.S, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(this.T, this.testingBounds.getOrientedBoundAxis()[2]);
		assertEpsilonEquals(this.testingBounds.getOrientedBoundAxis(), clone.getOrientedBoundAxis());
		
		assertNotSame(this.testingBounds.getCenter(), clone.getCenter());
		assertEpsilonEquals(this.Q, this.testingBounds.getCenter());
		assertEpsilonEquals(this.testingBounds.getCenter(), clone.getCenter());

		assertNotSame(this.testingBounds.getOrientedBoundExtents(), clone.getOrientedBoundExtents());
		assertEpsilonEquals(this.u, this.testingBounds.getOrientedBoundExtents()[0]);
		assertEpsilonEquals(this.v, this.testingBounds.getOrientedBoundExtents()[1]);
		assertEpsilonEquals(this.w, this.testingBounds.getOrientedBoundExtents()[2]);
		assertEpsilonEquals(this.testingBounds.getOrientedBoundExtents(), clone.getOrientedBoundExtents());
	}

	@Override
	public void testTranslateVector3f() {
		assertTrue(this.testingBounds.isInit());
		
		Vector3f vect = randomVector3D();
		
		this.testingBounds.translate(vect);
		
		Point3f expectedPt = new Point3f();
		
		expectedPt.add(this.Q, vect);
		assertEpsilonEquals(expectedPt, this.testingBounds.getCenter());

		assertEpsilonEquals(this.R, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(this.S, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(this.T, this.testingBounds.getOrientedBoundAxis()[2]);

		assertEpsilonEquals(this.u, this.testingBounds.getOrientedBoundExtents()[0]);
		assertEpsilonEquals(this.v, this.testingBounds.getOrientedBoundExtents()[1]);
		assertEpsilonEquals(this.w, this.testingBounds.getOrientedBoundExtents()[2]);
	}

	@Override
	public void testGetVertexCount() {
		assertTrue(this.testingBounds.isInit());
		assertEquals(8, this.testingBounds.getVertexCount());
	}

	@Override
	public void testGetLocalOrientedVertices() {
		assertTrue(this.testingBounds.isInit());
		
		Set<Vector3f> expected = new HashSet<Vector3f>();
		for(Vector3f vect : this.expectedVectors) {
			expected.add(vect);
		}
		
		SizedIterator<Vector3f> vectors = this.testingBounds.getLocalOrientedBoundVertices();
		
		assertNotNull(vectors);
		assertEquals(8, vectors.totalSize());
		
		Vector3f vect;
		int count=0;
		while (vectors.hasNext()) {
			vect = vectors.next();
			assertNotNull(vect);
			assertTrue(epsilonRemove(expected, vect));
			++count;
		}
		
		assertEquals(8, count);
		assertTrue(expected.isEmpty());
	}

	@Override
	public void testGetLocalVertexAt() {
		assertTrue(this.testingBounds.isInit());
		
		List<Vector3f> expected = new ArrayList<Vector3f>();
		for(Vector3f vect : this.expectedVectors) {
			expected.add(vect);
		}
		
		Vector3f v;
		
		try {
			this.testingBounds.getLocalVertexAt(-1);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
		
		for(int i=0; i<8; ++i) {
			v = this.testingBounds.getLocalVertexAt(i);
			assertNotNull(v);
			try {
				assertEpsilonEquals("#"+Integer.toString(i), expected.get(i), v); //$NON-NLS-1$
			}
			catch(AssertionFailedError e) {
				String m = null;
				for(int j=0; m==null && j<8; ++j) {
					if (j!=i) {
						Vector3f ev = expected.get(j);
						if (isEpsilonEquals(ev.getX(), v.getX())
							&&isEpsilonEquals(ev.getY(), v.getY())
							&&isEpsilonEquals(ev.getZ(), v.getZ())) {
							m = "#"+i+" failed, but #"+j+" is successful";  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
						}
					}
				}
				if (m==null) m = "#"+Integer.toString(i); //$NON-NLS-1$
				fail(m);
			}
		}
		
		try {
			this.testingBounds.getLocalVertexAt(8);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
	}

	@Override
	public void testGetGlobalOrientedVertices() {
		assertTrue(this.testingBounds.isInit());
		
		Set<Point3f> expected = new HashSet<Point3f>();
		for(Point3f p : this.expectedVertices) {
			expected.add(p);
		}
		
		SizedIterator<? extends Point3f> points = this.testingBounds.getGlobalOrientedBoundVertices();
		
		assertNotNull(points);
		assertEquals(8, points.totalSize());
		
		Point3f p;
		int count=0;
		while (points.hasNext()) {
			p = points.next();
			assertNotNull(p);
			assertTrue(epsilonRemove(expected, p));
			++count;
		}
		
		assertEquals(8, count);
		assertTrue(expected.isEmpty());
	}

	@Override
	public void testGetGlobalVertexAt() {
		assertTrue(this.testingBounds.isInit());
		
		List<Point3f> expected = new ArrayList<Point3f>();
		Point3f p;
		for(Vector3f vect : this.expectedVectors) {
			p = new Point3f(this.Q);
			p.add(vect);
			expected.add(p);
		}
		
		try {
			this.testingBounds.getGlobalVertexAt(-1);
		}
		catch(IndexOutOfBoundsException _) {
			//
		}
		
		for(int i=0; i<8; ++i) {
			p = this.testingBounds.getGlobalVertexAt(i);
			assertNotNull(p);
			assertEpsilonEquals("#"+Integer.toString(i), expected.get(i), p); //$NON-NLS-1$
		}
		
		try {
			this.testingBounds.getGlobalVertexAt(8);
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
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.V5));
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.V6));
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.V7));
		assertEpsilonEquals(0.f, this.testingBounds.distance(this.V8));
		
		Point3f p = new Point3f();

		// Points near a side and on one of the axis 
		p.scaleAdd(this.u * 4.f, this.R, this.Q);
		assertEpsilonEquals(this.u*3.f, this.testingBounds.distance(p));

		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		assertEpsilonEquals(this.u*3.f, this.testingBounds.distance(p));

		p.scaleAdd(this.v * 4.f, this.S, this.Q);
		assertEpsilonEquals(this.v*3.f, this.testingBounds.distance(p));

		p.scaleAdd(-this.v * 4.f, this.S, this.Q);
		assertEpsilonEquals(this.v*3.f, this.testingBounds.distance(p));

		p.scaleAdd(this.w * 4.f, this.T, this.Q);
		assertEpsilonEquals(this.w*3.f, this.testingBounds.distance(p));

		p.scaleAdd(-this.w * 4.f, this.T, this.Q);
		assertEpsilonEquals(this.w*3.f, this.testingBounds.distance(p));

		// Points near a side and not on one of the axis 
		p.scaleAdd(this.u * 4.f, this.R, this.Q);
		p.scaleAdd(.5f, this.S, p);
		assertEpsilonEquals(this.u*3.f, this.testingBounds.distance(p));

		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		p.scaleAdd(-.5f, this.S, p);
		assertEpsilonEquals(this.u*3.f, this.testingBounds.distance(p));

		p.scaleAdd(this.v * 4.f, this.S, this.Q);
		p.scaleAdd(.5f, this.T, p);
		assertEpsilonEquals(this.v*3.f, this.testingBounds.distance(p));

		p.scaleAdd(-this.v * 4.f, this.S, this.Q);
		p.scaleAdd(-.5f, this.T, p);
		assertEpsilonEquals(this.v*3.f, this.testingBounds.distance(p));

		p.scaleAdd(this.w * 4.f, this.T, this.Q);
		p.scaleAdd(.5f, this.R, p);
		assertEpsilonEquals(this.w*3.f, this.testingBounds.distance(p));

		p.scaleAdd(-this.w * 4.f, this.T, this.Q);
		p.scaleAdd(-.5f, this.R, p);
		assertEpsilonEquals(this.w*3.f, this.testingBounds.distance(p));

		// Near V1
		p.scaleAdd(this.u * 3.f, this.R, this.Q);
		p.scaleAdd(this.v * 4.f, this.S, p);
		p.scaleAdd(this.w * 5.f, this.T, p);
		assertEpsilonEquals(this.V1.distance(p), this.testingBounds.distance(p));

		// Near V8
		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		p.scaleAdd(-this.v * 3.f, this.S, p);
		p.scaleAdd(-this.w * 6.f, this.T, p);
		assertEpsilonEquals(this.V8.distance(p), this.testingBounds.distance(p));
	}

	@Override
	public void testDistanceSquaredP() {
		assertTrue(this.testingBounds.isInit());

		// From box's center
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.Q));
		
		// From the original points (computed with GNU octave)
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.P1));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.P2));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.P3));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.P4));

		// From box's vertex
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V1));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V2));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V3));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V4));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V5));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V6));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V7));
		assertEpsilonEquals(0.f, this.testingBounds.distanceSquared(this.V8));
	}

	@Override
	public void testDistanceMaxP() {
		assertTrue(this.testingBounds.isInit());

		// From box's center
		float diagonalLength = MathUtil.distance(this.u, this.v, this.w);
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.Q));
		
		// From the original points (computed with GNU octave)
		assertEpsilonEquals(3.9292f, this.testingBounds.distanceMax(this.P1));
		assertEpsilonEquals(3.1590f, this.testingBounds.distanceMax(this.P2));
		assertEpsilonEquals(4.0698f, this.testingBounds.distanceMax(this.P3));
		assertEpsilonEquals(3.5909f, this.testingBounds.distanceMax(this.P4));

		// From box's vertex
		diagonalLength = MathUtil.distance(this.u*2.f, this.v*2.f, this.w*2.f);
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V1));
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V2));
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V3));
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V4));
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V5));
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V6));
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V7));
		assertEpsilonEquals(diagonalLength, this.testingBounds.distanceMax(this.V8));
	}

	@Override
	public void testDistanceMaxSquaredP() {
		assertTrue(this.testingBounds.isInit());

		// From box's center
		float diagonalLength = MathUtil.distance(this.u, this.v, this.w);
		assertEpsilonEquals((float) Math.pow(diagonalLength,2.), this.testingBounds.distanceMaxSquared(this.Q));
		
		// From the original points (computed with GNU octave)
		assertEpsilonEquals((float) Math.pow(3.9292,2.), this.testingBounds.distanceMaxSquared(this.P1));
		assertEpsilonEquals((float) Math.pow(3.1590,2.), this.testingBounds.distanceMaxSquared(this.P2));
		assertEpsilonEquals((float) Math.pow(4.0698,2.), this.testingBounds.distanceMaxSquared(this.P3));
		assertEpsilonEquals((float) Math.pow(3.5909,2.), this.testingBounds.distanceMaxSquared(this.P4));

		// From box's vertex
		diagonalLength = MathUtil.distance(this.u*2.f, this.v*2.f, this.w*2.f);
		assertEpsilonEquals((float) Math.pow(diagonalLength,2.), this.testingBounds.distanceMaxSquared(this.V1));
		assertEpsilonEquals((float) Math.pow(diagonalLength,2.), this.testingBounds.distanceMaxSquared(this.V2));
		assertEpsilonEquals((float) Math.pow(diagonalLength,2.), this.testingBounds.distanceMaxSquared(this.V3));
		assertEpsilonEquals((float) Math.pow(diagonalLength,2.), this.testingBounds.distanceMaxSquared(this.V4));
		assertEpsilonEquals((float) Math.pow(diagonalLength,2.), this.testingBounds.distanceMaxSquared(this.V5));
		assertEpsilonEquals((float) Math.pow(diagonalLength,2.), this.testingBounds.distanceMaxSquared(this.V6));
		assertEpsilonEquals((float) Math.pow(diagonalLength,2.), this.testingBounds.distanceMaxSquared(this.V7));
		assertEpsilonEquals((float) Math.pow(diagonalLength,2.), this.testingBounds.distanceMaxSquared(this.V8));
	}

	@Override
	public void testSetP() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f p = randomPoint3D();
		
		this.testingBounds.set(p);
		
		assertTrue(this.testingBounds.isInit());
		assertTrue(this.testingBounds.isEmpty());

		assertEquals(p, this.testingBounds.getCenter());
		assertEquals(0.f, this.testingBounds.getOrientedBoundExtents()[0]);
		assertEquals(0.f, this.testingBounds.getOrientedBoundExtents()[1]);
		assertEquals(0.f, this.testingBounds.getOrientedBoundExtents()[2]);

		assertEquals(new Vector3f(1.,0.f,0.f), this.testingBounds.getOrientedBoundAxis()[0]);
		assertEquals(new Vector3f(0.f,1.,0.f), this.testingBounds.getOrientedBoundAxis()[1]);
		assertEquals(new Vector3f(0.f,0.f,1.), this.testingBounds.getOrientedBoundAxis()[2]);
	}

	@Override
	public void testSetTuple3fArray() {
		assertTrue(this.testingBounds.isInit());
		
		Point3f[] points = randomPoints3D();
		Vector3f expectedR = new Vector3f();
		Vector3f expectedS = new Vector3f();
		Vector3f expectedT = new Vector3f();
		Point3f expectedCenter = new Point3f();
		float[] expectedExtents = new float[3];
		OrientedBoundingBox.computeOBBCenterAxisExtents(points, expectedR, expectedS, expectedT, expectedCenter, expectedExtents);
		
		this.testingBounds.set(points);
		
		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
		assertEpsilonEquals(expectedExtents, this.testingBounds.getOrientedBoundExtents());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(expectedT, this.testingBounds.getOrientedBoundAxis()[2]);
	}

	@Override
	public void testSetCB() {
		assertTrue(this.testingBounds.isInit());
		
		{
			OrientedBoundingBox obb = new OrientedBoundingBox();
			obb.set(randomPoints3D());

			this.testingBounds.set(obb);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(obb.getCenter(), this.testingBounds.getCenter());
			assertEpsilonEquals(obb.getOrientedBoundExtents(), this.testingBounds.getOrientedBoundExtents());

			assertEpsilonEquals(obb.getOrientedBoundAxis()[0], this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(obb.getOrientedBoundAxis()[1], this.testingBounds.getOrientedBoundAxis()[1]);
			assertEpsilonEquals(obb.getOrientedBoundAxis()[2], this.testingBounds.getOrientedBoundAxis()[2]);
		}

		{
			AlignedBoundingBox aabb = new AlignedBoundingBox();
			aabb.set(randomPoints3D());
			Vector3f aabbR, aabbS, aabbT;
			float eR, eS, eT;
			aabbR = new Vector3f(1.,0.f,0.f);
			aabbS = new Vector3f(0.f,1.,0.f);
			aabbT = new Vector3f(0.f,0.f,1.);
			eR = aabb.getSizeX() / 2.f;
			eS = aabb.getSizeY() / 2.f;
			eT = aabb.getSizeZ() / 2.f;

			this.testingBounds.set(aabb);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(aabb.getCenter(), this.testingBounds.getCenter());

			assertEpsilonEquals(aabbR, this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(aabbS, this.testingBounds.getOrientedBoundAxis()[1]);
			assertEpsilonEquals(aabbT, this.testingBounds.getOrientedBoundAxis()[2]);

			assertEpsilonEquals(new float[] {eR, eS, eT}, this.testingBounds.getOrientedBoundExtents());
		}

		{
			BoundingSphere sphere = new BoundingSphere();
			sphere.set(randomPoints3D());
			Vector3f sphereR, sphereS, sphereT;
			float eR, eS, eT;
			sphereR = new Vector3f(1.,0.f,0.f);
			sphereS = new Vector3f(0.f,1.,0.f);
			sphereT = new Vector3f(0.f,0.f,1.);
			eR = sphere.getRadius();
			eS = sphere.getRadius();
			eT = sphere.getRadius();
			
			this.testingBounds.set(sphere);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(sphere.getCenter(), this.testingBounds.getCenter());

			assertEpsilonEquals(sphereR, this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(sphereS, this.testingBounds.getOrientedBoundAxis()[1]);
			assertEpsilonEquals(sphereT, this.testingBounds.getOrientedBoundAxis()[2]);

			assertEpsilonEquals(new float[] {eR, eS, eT}, this.testingBounds.getOrientedBoundExtents());
		}
	}
	
	@Override
	public void testCombineP() {
		assertTrue(this.testingBounds.isInit());
		
		Collection<Point3f> allPoints = new ArrayList<Point3f>();
		Point3f point = randomPoint3D();
		Iterator<? extends Point3f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
		while (iterator.hasNext()) {
			allPoints.add(iterator.next());
		}
		allPoints.add(point);
		Vector3f expectedR = new Vector3f();
		Vector3f expectedS = new Vector3f();
		Vector3f expectedT = new Vector3f();
		Point3f expectedCenter = new Point3f();
		float[] expectedExtents = new float[3];
		OrientedBoundingBox.computeOBBCenterAxisExtents(allPoints, expectedR, expectedS, expectedT, expectedCenter, expectedExtents);
		
		this.testingBounds.combine(point);
		
		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
		assertEpsilonEquals(expectedExtents, this.testingBounds.getOrientedBoundExtents());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(expectedT, this.testingBounds.getOrientedBoundAxis()[2]);
	}

	@Override
	public void testCombineTuple3fArray() {
		assertTrue(this.testingBounds.isInit());
		
		Collection<Point3f> allPoints = new ArrayList<Point3f>();
		Point3f[] points = randomPoints3D();
		Iterator<? extends Point3f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
		while (iterator.hasNext()) {
			allPoints.add(iterator.next());
		}
		allPoints.addAll(Arrays.asList(points));
		Vector3f expectedR = new Vector3f();
		Vector3f expectedS = new Vector3f();
		Vector3f expectedT = new Vector3f();
		Point3f expectedCenter = new Point3f();
		float[] expectedExtents = new float[3];
		OrientedBoundingBox.computeOBBCenterAxisExtents(allPoints, expectedR, expectedS, expectedT, expectedCenter, expectedExtents);
		
		this.testingBounds.combine(points);
		
		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
		assertEpsilonEquals(expectedExtents, this.testingBounds.getOrientedBoundExtents());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(expectedT, this.testingBounds.getOrientedBoundAxis()[2]);
	}

	@Override
	public void testCombineCB() {
		assertTrue(this.testingBounds.isInit());
		
		{
			OrientedBoundingBox obb = new OrientedBoundingBox();
			obb.set(randomPoints3D());
			
			Collection<Point3f> allPoints = new ArrayList<Point3f>();
			Iterator<? extends Point3f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			iterator = obb.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			Vector3f expectedR = new Vector3f();
			Vector3f expectedS = new Vector3f();
			Vector3f expectedT = new Vector3f();
			
			Point3f expectedCenter = new Point3f();
			float[] expectedExtents = new float[3];
			
			OrientedBoundingBox.computeOBBCenterAxisExtents(allPoints, expectedR, expectedS, expectedT, expectedCenter, expectedExtents);

			this.testingBounds.combine(obb);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
			assertEpsilonEquals(expectedExtents, this.testingBounds.getOrientedBoundExtents());

			assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
			assertEpsilonEquals(expectedT, this.testingBounds.getOrientedBoundAxis()[2]);
		}

		{
			AlignedBoundingBox aabb = new AlignedBoundingBox();
			aabb.set(randomPoints3D());
			
			Collection<Point3f> allPoints = new ArrayList<Point3f>();
			Iterator<? extends Point3f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			iterator = aabb.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			Vector3f expectedR = new Vector3f();
			Vector3f expectedS = new Vector3f();
			Vector3f expectedT = new Vector3f();
			Point3f expectedCenter = new Point3f();
			float[] expectedExtents = new float[3];
			OrientedBoundingBox.computeOBBCenterAxisExtents(allPoints, expectedR, expectedS, expectedT, expectedCenter, expectedExtents);

			this.testingBounds.combine(aabb);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
			assertEpsilonEquals(expectedExtents, this.testingBounds.getOrientedBoundExtents());

			assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
			assertEpsilonEquals(expectedT, this.testingBounds.getOrientedBoundAxis()[2]);
		}

		{
			BoundingSphere sphere = new BoundingSphere();
			sphere.set(randomPoints3D());
			
			Collection<Point3f> allPoints = new ArrayList<Point3f>();
			Iterator<? extends Point3f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			iterator = sphere.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
			Vector3f expectedR = new Vector3f();
			Vector3f expectedS = new Vector3f();
			Vector3f expectedT = new Vector3f();
			Point3f expectedCenter = new Point3f();
			float[] expectedExtents = new float[3];
			OrientedBoundingBox.computeOBBCenterAxisExtents(allPoints, expectedR, expectedS, expectedT, expectedCenter, expectedExtents);

			this.testingBounds.combine(sphere);

			assertTrue(this.testingBounds.isInit());
			assertFalse(this.testingBounds.isEmpty());

			assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
			assertEpsilonEquals(expectedExtents, this.testingBounds.getOrientedBoundExtents());

			assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
			assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
			assertEpsilonEquals(expectedT, this.testingBounds.getOrientedBoundAxis()[2]);
		}
	}

	@Override
	public void testCombineCollection() {
		assertTrue(this.testingBounds.isInit());
		
		Collection<CombinableBounds3D> bounds = new ArrayList<CombinableBounds3D>();
		Collection<Point3f> allPoints = new ArrayList<Point3f>();
		
		Iterator<? extends Point3f> iterator = this.testingBounds.getGlobalOrientedBoundVertices();
		while (iterator.hasNext()) {
			allPoints.add(iterator.next());
		}

		{
			OrientedBoundingBox obb = new OrientedBoundingBox();
			obb.set(randomPoints3D());
			bounds.add(obb);
			
			iterator = obb.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
		}

		{
			AlignedBoundingBox aabb = new AlignedBoundingBox();
			aabb.set(randomPoints3D());
			bounds.add(aabb);
			
			iterator = aabb.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
		}

		{
			BoundingSphere sphere = new BoundingSphere();
			sphere.set(randomPoints3D());
			bounds.add(sphere);
			
			iterator = sphere.getGlobalOrientedBoundVertices();
			while (iterator.hasNext()) {
				allPoints.add(iterator.next());
			}
		}

		Vector3f expectedR = new Vector3f();
		Vector3f expectedS = new Vector3f();
		Vector3f expectedT = new Vector3f();
		Point3f expectedCenter = new Point3f();
		float[] expectedExtents = new float[3];
		OrientedBoundingBox.computeOBBCenterAxisExtents(allPoints, expectedR, expectedS, expectedT, expectedCenter, expectedExtents);
		
		this.testingBounds.combine(bounds);

		assertTrue(this.testingBounds.isInit());
		assertFalse(this.testingBounds.isEmpty());

		assertEpsilonEquals(expectedCenter, this.testingBounds.getCenter());
		assertEpsilonEquals(expectedExtents, this.testingBounds.getOrientedBoundExtents());

		assertEpsilonEquals(expectedR, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(expectedS, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(expectedT, this.testingBounds.getOrientedBoundAxis()[2]);
	}

	//-------------------------------------------
	// OrientedBoundingBox dedicated tests
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
		assertEpsilonEquals(this.V5, this.testingBounds.nearestPoint(this.V5));
		assertEpsilonEquals(this.V6, this.testingBounds.nearestPoint(this.V6));
		assertEpsilonEquals(this.V7, this.testingBounds.nearestPoint(this.V7));
		assertEpsilonEquals(this.V8, this.testingBounds.nearestPoint(this.V8));

		assertEpsilonEquals(this.P1, this.testingBounds.nearestPoint(this.P1));
		assertEpsilonEquals(this.P2, this.testingBounds.nearestPoint(this.P2));
		assertEpsilonEquals(this.P3, this.testingBounds.nearestPoint(this.P3));
		assertEpsilonEquals(this.P4, this.testingBounds.nearestPoint(this.P4));

		Point3f p = new Point3f();
		
		// Near V1
		p.scaleAdd(this.u * 3.f, this.R, this.Q);
		p.scaleAdd(this.v * 4.f, this.S, p);
		p.scaleAdd(this.w * 5.f, this.T, p);
		assertEpsilonEquals(this.V1, this.testingBounds.nearestPoint(p));

		// Near V8
		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		p.scaleAdd(-this.v * 3.f, this.S, p);
		p.scaleAdd(-this.w * 6.f, this.T, p);
		assertEpsilonEquals(this.V8, this.testingBounds.nearestPoint(p));

		// Near V3
		p.scaleAdd(-this.u * 5.f, this.R, this.Q);
		p.scaleAdd(this.v * 8.f, this.S, p);
		p.scaleAdd(this.w * 2.f, this.T, p);
		assertEpsilonEquals(this.V3, this.testingBounds.nearestPoint(p));
	}

	/**
	 */
	@Override
	public void testFarestPointP() {
		assertTrue(this.testingBounds.isInit());

		assertEpsilonEquals(this.V8, this.testingBounds.farestPoint(this.Q));

		assertEpsilonEquals(this.V8, this.testingBounds.farestPoint(this.V1));
		assertEpsilonEquals(this.V7, this.testingBounds.farestPoint(this.V2));
		assertEpsilonEquals(this.V6, this.testingBounds.farestPoint(this.V3));
		assertEpsilonEquals(this.V5, this.testingBounds.farestPoint(this.V4));
		assertEpsilonEquals(this.V4, this.testingBounds.farestPoint(this.V5));
		assertEpsilonEquals(this.V3, this.testingBounds.farestPoint(this.V6));
		assertEpsilonEquals(this.V2, this.testingBounds.farestPoint(this.V7));
		assertEpsilonEquals(this.V1, this.testingBounds.farestPoint(this.V8));

		// Farest points are computed with Octave
		assertEpsilonEquals(this.V2, this.testingBounds.farestPoint(this.P1));
		assertEpsilonEquals(this.V8, this.testingBounds.farestPoint(this.P2));
		assertEpsilonEquals(this.V6, this.testingBounds.farestPoint(this.P3));
		assertEpsilonEquals(this.V5, this.testingBounds.farestPoint(this.P4));

		Point3f p = new Point3f();
		
		// Near V1 -> V8
		p.scaleAdd(this.u * 3.f, this.R, this.Q);
		p.scaleAdd(this.v * 4.f, this.S, p);
		p.scaleAdd(this.w * 5.f, this.T, p);
		assertEpsilonEquals(this.V8, this.testingBounds.farestPoint(p));

		// Near V8 -> V1
		p.scaleAdd(-this.u * 4.f, this.R, this.Q);
		p.scaleAdd(-this.v * 3.f, this.S, p);
		p.scaleAdd(-this.w * 6.f, this.T, p);
		assertEpsilonEquals(this.V1, this.testingBounds.farestPoint(p));

		// Near V5 -> V6
		p.scaleAdd(-this.u * 5.f, this.R, this.Q);
		p.scaleAdd(this.v * 8.f, this.S, p);
		p.scaleAdd(this.w * 2.f, this.T, p);
		assertEpsilonEquals(this.V6, this.testingBounds.farestPoint(p));
	}

	//-------------------------------------------
	// IntersectionClassifier
	//-------------------------------------------

	private Point3f mkPt(Point3f p) {
		Vector3f vv = new Vector3f();
		vv.sub(this.Q, p);
		vv.scale(MathUtil.getEpsilon());
		Point3f p2 = new Point3f(p);
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

		Point3f p = new Point3f();
		
		p.setX(-1000.f);
		p.setY(-1000.f);
		p.setZ(-1000.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));

		p.setX(1.7f);
		p.setY(0.5f);
		p.setZ(2.7f);
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

		Point3f p = new Point3f();
		
		p.setX(-1000.f);
		p.setY(-1000.f);
		p.setZ(-1000.f);
		assertFalse(this.testingBounds.intersects(p));

		p.setX(1.7f);
		p.setY(0.5f);
		p.setZ(2.7f);
		assertFalse(this.testingBounds.intersects(p));
	}

	@Override
	public void testClassifiesPP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p1 = new Point3f();
		Point3f p2 = new Point3f();
		
		float minE = MathUtil.min(this.u, this.v, this.w);
		float maxE = MathUtil.max(this.u, this.v, this.w);
		
		p1.setX(this.Q.getX() - minE / 10.f);
		p1.setY(this.Q.getY() - minE / 10.f);
		p1.setZ(this.Q.getZ() - minE / 10.f);
		p2.setX(this.Q.getX() + minE / 10.f);
		p2.setY(this.Q.getY() + minE / 10.f);
		p2.setZ(this.Q.getZ() + minE / 10.f);
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(p1,p2));

		p1.setX(this.Q.getX() - maxE * 10.f);
		p1.setY(this.Q.getY() - maxE * 10.f);
		p1.setZ(this.Q.getZ() - maxE * 10.f);
		p2.setX(this.Q.getX() + maxE * 10.f);
		p2.setY(this.Q.getY() + maxE * 10.f);
		p2.setZ(this.Q.getZ() + maxE * 10.f);
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(p1,p2));

		p1.setX(this.Q.getX() - 1000.f);
		p1.setY(this.Q.getY() - 10.f);
		p1.setZ(this.Q.getZ() - 10.f);
		p2.setX(this.Q.getX() - 900.f);
		p2.setY(this.Q.getY() + 10.f);
		p2.setZ(this.Q.getZ() + 10.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p1,p2));

		p1.setX(-10.f);
		p1.setY(-10.f);
		p1.setZ(-10.f);
		p2.setX(0.5f);
		p2.setY(10.f);
		p2.setZ(10.f);
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));

		p1.setX(-10.f);
		p1.setY(-10.f);
		p1.setZ(-10.f);
		p2.setX(this.Q.getX());
		p2.setY(this.Q.getY());
		p2.setZ(this.Q.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));

		p1.setX(this.Q.getX());
		p1.setY(this.Q.getY());
		p1.setZ(this.Q.getZ());
		p2.setX(this.Q.getX()+1);
		p2.setY(this.Q.getY()+1);
		p2.setZ(this.Q.getZ()+1);
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p1,p2));
	}

	@Override
	public void testIntersectsPP() {
		assertTrue(this.testingBounds.isInit());

		Point3f p1 = new Point3f();
		Point3f p2 = new Point3f();
		
		float minE = MathUtil.min(this.u, this.v, this.w);
		float maxE = MathUtil.max(this.u, this.v, this.w);
		
		p1.setX(this.Q.getX() - minE / 10.f);
		p1.setY(this.Q.getY() - minE / 10.f);
		p1.setZ(this.Q.getZ() - minE / 10.f);
		p2.setX(this.Q.getX() + minE / 10.f);
		p2.setY(this.Q.getY() + minE / 10.f);
		p2.setZ(this.Q.getZ() + minE / 10.f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		p1.setX(this.Q.getX() - maxE * 10.f);
		p1.setY(this.Q.getY() - maxE * 10.f);
		p1.setZ(this.Q.getZ() - maxE * 10.f);
		p2.setX(this.Q.getX() + maxE * 10.f);
		p2.setY(this.Q.getY() + maxE * 10.f);
		p2.setZ(this.Q.getZ() + maxE * 10.f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		p1.setX(this.Q.getX() - 1000.f);
		p1.setY(this.Q.getY() - 10.f);
		p1.setZ(this.Q.getZ() - 10.f);
		p2.setX(this.Q.getX() - 900.f);
		p2.setY(this.Q.getY() + 10.f);
		p2.setZ(this.Q.getZ() + 10.f);
		assertFalse(this.testingBounds.intersects(p1,p2));

		p1.setX(-10.f);
		p1.setY(-10.f);
		p1.setZ(-10.f);
		p2.setX(0.5f);
		p2.setY(10.f);
		p2.setZ(10.f);
		assertTrue(this.testingBounds.intersects(p1,p2));

		p1.setX(-10.f);
		p1.setY(-10.f);
		p1.setZ(-10.f);
		p2.setX(this.Q.getX());
		p2.setY(this.Q.getY());
		p2.setZ(this.Q.getZ());
		assertTrue(this.testingBounds.intersects(p1,p2));

		p1.setX(this.Q.getX());
		p1.setY(this.Q.getY());
		p1.setZ(this.Q.getZ());
		p2.setX(this.Q.getX()+1);
		p2.setY(this.Q.getY()+1);
		p2.setZ(this.Q.getZ()+1);
		assertTrue(this.testingBounds.intersects(p1,p2));
	}

	@Override
	public void testClassifiesPoint3fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point3f c = new Point3f();
		float r;
		
		float minE = MathUtil.min(this.u, this.v, this.w);
		float maxE = MathUtil.max(this.u, this.v, this.w);

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = minE / 10.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(c, r));
		
		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = minE;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = 0.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = maxE * 10.f;
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(c, r));

		c.setX(this.P1.getX());
		c.setY(this.P1.getY());
		c.setZ(this.P1.getZ());
		r = maxE * 50.f;
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(c, r));

		c.setX(-10.f);
		c.setY(-10.f);
		c.setZ(-10.f);
		r = 1.f;
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = maxE;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(c, r));

		c.setX(-2.f);
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = 1.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(c, r));
	}

	@Override
	public void testIntersectsPoint3fFloat() {
		assertTrue(this.testingBounds.isInit());

		Point3f c = new Point3f();
		float r;
		
		float minE = MathUtil.min(this.u, this.v, this.w);
		float maxE = MathUtil.max(this.u, this.v, this.w);

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = minE / 10.f;
		assertTrue(this.testingBounds.intersects(c, r));
		
		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = minE;
		assertTrue(this.testingBounds.intersects(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = 0.f;
		assertTrue(this.testingBounds.intersects(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = maxE * 10.f;
		assertTrue(this.testingBounds.intersects(c, r));

		c.setX(this.P1.getX());
		c.setY(this.P1.getY());
		c.setZ(this.P1.getZ());
		r = maxE * 50.f;
		assertTrue(this.testingBounds.intersects(c, r));

		c.setX(-10.f);
		c.setY(-10.f);
		c.setZ(-10.f);
		r = 1.f;
		assertFalse(this.testingBounds.intersects(c, r));

		c.setX(this.Q.getX());
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = maxE;
		assertTrue(this.testingBounds.intersects(c, r));

		c.setX(-2.f);
		c.setY(this.Q.getY());
		c.setZ(this.Q.getZ());
		r = 1.f;
		assertTrue(this.testingBounds.intersects(c, r));
	}

	@Override
	public void testClassifiesAgainstPlane() {
		assertTrue(this.testingBounds.isInit());

		Plane p;
		Point3f lowerPoint = new Point3f();
		Point3f upperPoint = new Point3f();
		lowerPoint.set(this.testingBounds.getLower());
		upperPoint.set(this.testingBounds.getUpper());
		
		p = new XYPlane(upperPoint.getZ() + 100.f);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));
		p = new XYPlane(lowerPoint.getZ() - 100.f);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));
		p = new XYPlane(this.Q.getZ());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new XYPlane(lowerPoint.getZ());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new XYPlane(upperPoint.getZ());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));

		p = new XZPlane(upperPoint.getY() + 100.f);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));
		p = new XZPlane(lowerPoint.getY() - 100.f);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));
		p = new XZPlane(this.Q.getY());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new XZPlane(lowerPoint.getY());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new XZPlane(upperPoint.getY());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));

		p = new YZPlane(upperPoint.getX() + 100.f);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));
		p = new YZPlane(lowerPoint.getX() - 100.f);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));
		p = new YZPlane(this.Q.getX());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new YZPlane(lowerPoint.getX());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		p = new YZPlane(upperPoint.getX());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		
		p.negate();
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));
		
		Vector3f vv = new Vector3f(1.,1.,1.);
		
		p = new Plane4f(vv, this.Q);
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(vv.getX(), vv.getY(), vv.getZ(),
				this.P3.getX(),
				this.P3.getY(),
				this.P3.getZ());
		assertEquals(PlanarClassificationType.COINCIDENT, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(vv.getX(), vv.getY(), vv.getZ(),
				lowerPoint.getX()-100.f,
				lowerPoint.getY()-100.f,
				lowerPoint.getZ()-100.f);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(-vv.getX(), -vv.getY(), -vv.getZ(),
				lowerPoint.getX()-100.f,
				lowerPoint.getY()-100.f,
				lowerPoint.getZ()-100.f);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(vv.getX(), vv.getY(), vv.getZ(),
				upperPoint.getX()+100.f,
				upperPoint.getY()+100.f,
				upperPoint.getZ()+100.f);
		assertEquals(PlanarClassificationType.BEHIND, this.testingBounds.classifiesAgainst(p));		

		p = new Plane4f(-vv.getX(), -vv.getY(), -vv.getZ(),
				upperPoint.getX()+100.f,
				upperPoint.getY()+100.f,
				upperPoint.getZ()+100.f);
		assertEquals(PlanarClassificationType.IN_FRONT_OF, this.testingBounds.classifiesAgainst(p));		
	}

	@Override
	public void testClassifiesPlane() {
		assertTrue(this.testingBounds.isInit());

		Plane p;
		Point3f lowerPoint = new Point3f();
		Point3f upperPoint = new Point3f();
		lowerPoint.set(this.testingBounds.getLower());
		upperPoint.set(this.testingBounds.getUpper());
		
		p = new XYPlane(upperPoint.getZ() + 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new XYPlane(lowerPoint.getZ() - 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new XYPlane(this.Q.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new XYPlane(lowerPoint.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new XYPlane(upperPoint.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));

		p = new XZPlane(upperPoint.getY() + 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new XZPlane(lowerPoint.getY() - 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new XZPlane(this.Q.getY());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new XZPlane(lowerPoint.getY());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new XZPlane(upperPoint.getY());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));

		p = new YZPlane(upperPoint.getX() + 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new YZPlane(lowerPoint.getX() - 100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));
		p = new YZPlane(this.Q.getX());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new YZPlane(lowerPoint.getX());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		p = new YZPlane(upperPoint.getX());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		
		p.negate();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));
		
		Vector3f vv = new Vector3f(1.,1.,1.);
		
		p = new Plane4f(vv, this.Q);
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		

		p = new Plane4f(vv.getX(), vv.getY(), vv.getZ(),
				this.P3.getX(),
				this.P3.getY(),
				this.P3.getZ());
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(p));		

		p = new Plane4f(vv.getX(), vv.getY(), vv.getZ(),
				lowerPoint.getX()-100.f,
				lowerPoint.getY()-100.f,
				lowerPoint.getZ()-100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		

		p = new Plane4f(-vv.getX(), -vv.getY(), -vv.getZ(),
				lowerPoint.getX()-100.f,
				lowerPoint.getY()-100.f,
				lowerPoint.getZ()-100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		

		p = new Plane4f(vv.getX(), vv.getY(), vv.getZ(),
				upperPoint.getX()+100.f,
				upperPoint.getY()+100.f,
				upperPoint.getZ()+100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		

		p = new Plane4f(-vv.getX(), -vv.getY(), -vv.getZ(),
				upperPoint.getX()+100.f,
				upperPoint.getY()+100.f,
				upperPoint.getZ()+100.f);
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(p));		
	}

	@Override
	public void testIntersectsPlane() {
		assertTrue(this.testingBounds.isInit());

		Plane p;
		Point3f lowerPoint = new Point3f();
		Point3f upperPoint = new Point3f();
		lowerPoint.set(this.testingBounds.getLower());
		upperPoint.set(this.testingBounds.getUpper());
		
		p = new XYPlane(upperPoint.getZ() + 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new XYPlane(lowerPoint.getZ() - 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new XYPlane(this.Q.getZ());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new XYPlane(lowerPoint.getZ());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new XYPlane(upperPoint.getZ());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));

		p = new XZPlane(upperPoint.getY() + 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new XZPlane(lowerPoint.getY() - 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new XZPlane(this.Q.getY());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new XZPlane(lowerPoint.getY());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new XZPlane(upperPoint.getY());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));

		p = new YZPlane(upperPoint.getX() + 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new YZPlane(lowerPoint.getX() - 100.f);
		assertFalse(this.testingBounds.intersects(p));		
		p.negate();
		assertFalse(this.testingBounds.intersects(p));
		p = new YZPlane(this.Q.getX());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new YZPlane(lowerPoint.getX());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		p = new YZPlane(upperPoint.getX());
		assertTrue(this.testingBounds.intersects(p));		
		p.negate();
		assertTrue(this.testingBounds.intersects(p));
		
		Vector3f vv = new Vector3f(1.,1.,1.);
		
		p = new Plane4f(vv, this.Q);
		assertTrue(this.testingBounds.intersects(p));		

		p = new Plane4f(vv.getX(), vv.getY(), vv.getZ(),
				this.P3.getX(),
				this.P3.getY(),
				this.P3.getZ());
		assertTrue(this.testingBounds.intersects(p));		

		p = new Plane4f(vv.getX(), vv.getY(), vv.getZ(),
				lowerPoint.getX()-100.f,
				lowerPoint.getY()-100.f,
				lowerPoint.getZ()-100.f);
		assertFalse(this.testingBounds.intersects(p));		

		p = new Plane4f(-vv.getX(), -vv.getY(), -vv.getZ(),
				lowerPoint.getX()-100.f,
				lowerPoint.getY()-100.f,
				lowerPoint.getZ()-100.f);
		assertFalse(this.testingBounds.intersects(p));		

		p = new Plane4f(vv.getX(), vv.getY(), vv.getZ(),
				upperPoint.getX()+100.f,
				upperPoint.getY()+100.f,
				upperPoint.getZ()+100.f);
		assertFalse(this.testingBounds.intersects(p));		

		p = new Plane4f(-vv.getX(), -vv.getY(), -vv.getZ(),
				upperPoint.getX()+100.f,
				upperPoint.getY()+100.f,
				upperPoint.getZ()+100.f);
		assertFalse(this.testingBounds.intersects(p));		
	}

	@Override
	public void testClassifiesPoint3fVector3fArrayFloatArray() {
		assertTrue(this.testingBounds.isInit());

		Point3f center = new Point3f();
		Vector3f[] axis = new Vector3f[] {
				new Vector3f(),
				new Vector3f(),
				new Vector3f()
		};
		float[] extent = new float[3];
		
		center.setX(this.testingBounds.getCenterX());
		center.setY(this.testingBounds.getCenterY());
		center.setZ(this.testingBounds.getCenterZ());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[0].setZ(this.testingBounds.getR().getZ());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		axis[1].setZ(this.testingBounds.getS().getZ());
		axis[2].setX(this.testingBounds.getT().getX());
		axis[2].setY(this.testingBounds.getT().getY());
		axis[2].setZ(this.testingBounds.getT().getZ());
		extent[0] = this.testingBounds.getRExtent() / 2.f;
		extent[1] = this.testingBounds.getSExtent() / 2.f;
		extent[2] = this.testingBounds.getTExtent() / 2.f;
		assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(center, axis, extent));

		center.setX(this.testingBounds.getCenterX());
		center.setY(this.testingBounds.getCenterY());
		center.setZ(this.testingBounds.getCenterZ());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[0].setZ(this.testingBounds.getR().getZ());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		axis[1].setZ(this.testingBounds.getS().getZ());
		axis[2].setX(this.testingBounds.getT().getX());
		axis[2].setY(this.testingBounds.getT().getY());
		axis[2].setZ(this.testingBounds.getT().getZ());
		extent[0] = this.testingBounds.getRExtent() * 2.f;
		extent[1] = this.testingBounds.getSExtent() * 2.f;
		extent[2] = this.testingBounds.getTExtent() * 2.f;
		assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(center, axis, extent));

		center.setX(this.testingBounds.getLower().getX() - 20.f);
		center.setY(this.testingBounds.getCenterY());
		center.setZ(this.testingBounds.getCenterZ());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[0].setZ(this.testingBounds.getR().getZ());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		axis[1].setZ(this.testingBounds.getS().getZ());
		axis[2].setX(this.testingBounds.getT().getX());
		axis[2].setY(this.testingBounds.getT().getY());
		axis[2].setZ(this.testingBounds.getT().getZ());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		extent[2] = this.testingBounds.getTExtent();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(center, axis, extent));

		center.setX(this.testingBounds.getLower().getX());
		center.setY(this.testingBounds.getCenterY());
		center.setZ(this.testingBounds.getCenterZ());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[0].setZ(this.testingBounds.getR().getZ());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		axis[1].setZ(this.testingBounds.getS().getZ());
		axis[2].setX(this.testingBounds.getT().getX());
		axis[2].setY(this.testingBounds.getT().getY());
		axis[2].setZ(this.testingBounds.getT().getZ());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		extent[2] = this.testingBounds.getTExtent();
		assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(center, axis, extent));

		center.setX((this.testingBounds.getLower().getX() + this.Q.getX()) / 2.f);
		center.setY(this.testingBounds.getCenterY());
		center.setZ(this.testingBounds.getCenterZ());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[0].setZ(this.testingBounds.getR().getZ());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		axis[1].setZ(this.testingBounds.getS().getZ());
		axis[2].setX(this.testingBounds.getT().getX());
		axis[2].setY(this.testingBounds.getT().getY());
		axis[2].setZ(this.testingBounds.getT().getZ());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		extent[2] = this.testingBounds.getTExtent();
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, axis, extent));

		center.setX(0.f);
		center.setY(0.f);
		center.setZ(0.f);
		axis[0].setX(1.f);
		axis[0].setY(0.f);
		axis[0].setZ(0.f);
		axis[1].setX(0.f);
		axis[1].setY(1.f);
		axis[1].setZ(0.f);
		axis[2].setX(0.f);
		axis[2].setY(0.f);
		axis[2].setZ(1.f);
		extent[0] = 1.f;
		extent[1] = 1.f;
		extent[2] = 1.f;
		assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(center, axis, extent));
	}

	@Override
	public void testIntersectsPoint3fVector3fArrayFloatArray() {
		assertTrue(this.testingBounds.isInit());

		Point3f center = new Point3f();
		Vector3f[] axis = new Vector3f[] {
				new Vector3f(),
				new Vector3f(),
				new Vector3f()
		};
		float[] extent = new float[3];
		
		center.setX(this.testingBounds.getCenterX());
		center.setY(this.testingBounds.getCenterY());
		center.setZ(this.testingBounds.getCenterZ());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[0].setZ(this.testingBounds.getR().getZ());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		axis[1].setZ(this.testingBounds.getS().getZ());
		axis[2].setX(this.testingBounds.getT().getX());
		axis[2].setY(this.testingBounds.getT().getY());
		axis[2].setZ(this.testingBounds.getT().getZ());
		extent[0] = this.testingBounds.getRExtent() / 2.f;
		extent[1] = this.testingBounds.getSExtent() / 2.f;
		extent[2] = this.testingBounds.getTExtent() / 2.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		center.setX(this.testingBounds.getCenterX());
		center.setY(this.testingBounds.getCenterY());
		center.setZ(this.testingBounds.getCenterZ());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[0].setZ(this.testingBounds.getR().getZ());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		axis[1].setZ(this.testingBounds.getS().getZ());
		axis[2].setX(this.testingBounds.getT().getX());
		axis[2].setY(this.testingBounds.getT().getY());
		axis[2].setZ(this.testingBounds.getT().getZ());
		extent[0] = this.testingBounds.getRExtent() * 2.f;
		extent[1] = this.testingBounds.getSExtent() * 2.f;
		extent[2] = this.testingBounds.getTExtent() * 2.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		center.setX(this.testingBounds.getLower().getX() - 20.f);
		center.setY(this.testingBounds.getCenterY());
		center.setZ(this.testingBounds.getCenterZ());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[0].setZ(this.testingBounds.getR().getZ());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		axis[1].setZ(this.testingBounds.getS().getZ());
		axis[2].setX(this.testingBounds.getT().getX());
		axis[2].setY(this.testingBounds.getT().getY());
		axis[2].setZ(this.testingBounds.getT().getZ());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		extent[2] = this.testingBounds.getTExtent();
		assertFalse(this.testingBounds.intersects(center, axis, extent));

		center.setX(this.testingBounds.getLower().getX());
		center.setY(this.testingBounds.getCenterY());
		center.setZ(this.testingBounds.getCenterZ());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[0].setZ(this.testingBounds.getR().getZ());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		axis[1].setZ(this.testingBounds.getS().getZ());
		axis[2].setX(this.testingBounds.getT().getX());
		axis[2].setY(this.testingBounds.getT().getY());
		axis[2].setZ(this.testingBounds.getT().getZ());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		extent[2] = this.testingBounds.getTExtent();
		assertFalse(this.testingBounds.intersects(center, axis, extent));

		center.setX((this.testingBounds.getLower().getX() + this.Q.getX()) / 2.f);
		center.setY(this.testingBounds.getCenterY());
		center.setZ(this.testingBounds.getCenterZ());
		axis[0].setX(this.testingBounds.getR().getX());
		axis[0].setY(this.testingBounds.getR().getY());
		axis[0].setZ(this.testingBounds.getR().getZ());
		axis[1].setX(this.testingBounds.getS().getX());
		axis[1].setY(this.testingBounds.getS().getY());
		axis[1].setZ(this.testingBounds.getS().getZ());
		axis[2].setX(this.testingBounds.getT().getX());
		axis[2].setY(this.testingBounds.getT().getY());
		axis[2].setZ(this.testingBounds.getT().getZ());
		extent[0] = this.testingBounds.getRExtent();
		extent[1] = this.testingBounds.getSExtent();
		extent[2] = this.testingBounds.getTExtent();
		assertTrue(this.testingBounds.intersects(center, axis, extent));

		center.setX(0.f);
		center.setY(0.f);
		center.setZ(0.f);
		axis[0].setX(1.f);
		axis[0].setY(0.f);
		axis[0].setZ(0.f);
		axis[1].setX(0.f);
		axis[1].setY(1.f);
		axis[1].setZ(0.f);
		axis[2].setX(0.f);
		axis[2].setY(0.f);
		axis[2].setZ(1.f);
		extent[0] = 1.f;
		extent[1] = 1.f;
		extent[2] = 1.f;
		assertTrue(this.testingBounds.intersects(center, axis, extent));
	}

	@Override
	public void testClassifiesIC() {
		assertTrue(this.testingBounds.isInit());

		// AlignedBoundingBox
		{
			Point3f p1 = new Point3f();
			Point3f p2 = new Point3f();
			
			float minE = MathUtil.min(this.u, this.v, this.w);
			float maxE = MathUtil.max(this.u, this.v, this.w);
			
			p1.setX(this.Q.getX() - minE / 10.f);
			p1.setY(this.Q.getY() - minE / 10.f);
			p1.setZ(this.Q.getZ() - minE / 10.f);
			p2.setX(this.Q.getX() + minE / 10.f);
			p2.setY(this.Q.getY() + minE / 10.f);
			p2.setZ(this.Q.getZ() + minE / 10.f);
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new AlignedBoundingBox(p1,p2)));

			p1.setX(this.Q.getX() - maxE * 10.f);
			p1.setY(this.Q.getY() - maxE * 10.f);
			p1.setZ(this.Q.getZ() - maxE * 10.f);
			p2.setX(this.Q.getX() + maxE * 10.f);
			p2.setY(this.Q.getY() + maxE * 10.f);
			p2.setZ(this.Q.getZ() + maxE * 10.f);
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new AlignedBoundingBox(p1,p2)));

			p1.setX(this.Q.getX() - 1000.f);
			p1.setY(this.Q.getY() - 10.f);
			p1.setZ(this.Q.getZ() - 10.f);
			p2.setX(this.Q.getX() - 900.f);
			p2.setY(this.Q.getY() + 10.f);
			p2.setZ(this.Q.getZ() + 10.f);
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new AlignedBoundingBox(p1,p2)));

			p1.setX(-10.f);
			p1.setY(-10.f);
			p1.setZ(-10.f);
			p2.setX(0.5f);
			p2.setY(10.f);
			p2.setZ(10.f);
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new AlignedBoundingBox(p1,p2)));

			p1.setX(-10.f);
			p1.setY(-10.f);
			p1.setZ(-10.f);
			p2.setX(this.Q.getX());
			p2.setY(this.Q.getY());
			p2.setZ(this.Q.getZ());
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new AlignedBoundingBox(p1,p2)));

			p1.setX(this.Q.getX());
			p1.setY(this.Q.getY());
			p1.setZ(this.Q.getZ());
			p2.setX(this.Q.getX()+1);
			p2.setY(this.Q.getY()+1);
			p2.setZ(this.Q.getZ()+1);
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new AlignedBoundingBox(p1,p2)));
		}
		
		// BoundingSphere
		{
			Point3f c = new Point3f();
			float r;
			
			float minE = MathUtil.min(this.u, this.v, this.w);
			float maxE = MathUtil.max(this.u, this.v, this.w);

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = minE / 10.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingSphere(c,r)));
			
			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = minE;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingSphere(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = 0.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new BoundingSphere(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = maxE * 10.f;
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new BoundingSphere(c,r)));

			c.setX(this.P1.getX());
			c.setY(this.P1.getY());
			c.setZ(this.P1.getZ());
			r = maxE * 50.f;
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new BoundingSphere(c,r)));

			c.setX(-10.f);
			c.setY(-10.f);
			c.setZ(-10.f);
			r = 1.f;
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new BoundingSphere(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = maxE;
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new BoundingSphere(c,r)));

			c.setX(-2.f);
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = 1.f;
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new BoundingSphere(c,r)));
		}
		
		// OrientedBoundingBox
		{
			Point3f center = new Point3f();
			Vector3f[] axis = new Vector3f[] {
					new Vector3f(),
					new Vector3f(),
					new Vector3f()
			};
			float[] extent = new float[3];
			
			center.setX(this.testingBounds.getCenterX());
			center.setY(this.testingBounds.getCenterY());
			center.setZ(this.testingBounds.getCenterZ());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[0].setZ(this.testingBounds.getR().getZ());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			axis[1].setZ(this.testingBounds.getS().getZ());
			axis[2].setX(this.testingBounds.getT().getX());
			axis[2].setY(this.testingBounds.getT().getY());
			axis[2].setZ(this.testingBounds.getT().getZ());
			extent[0] = this.testingBounds.getRExtent() / 2.f;
			extent[1] = this.testingBounds.getSExtent() / 2.f;
			extent[2] = this.testingBounds.getTExtent() / 2.f;
			assertEquals(IntersectionType.INSIDE, this.testingBounds.classifies(
					new OrientedBoundingBox(center, axis, extent)));

			center.setX(this.testingBounds.getCenterX());
			center.setY(this.testingBounds.getCenterY());
			center.setZ(this.testingBounds.getCenterZ());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[0].setZ(this.testingBounds.getR().getZ());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			axis[1].setZ(this.testingBounds.getS().getZ());
			axis[2].setX(this.testingBounds.getT().getX());
			axis[2].setY(this.testingBounds.getT().getY());
			axis[2].setZ(this.testingBounds.getT().getZ());
			extent[0] = this.testingBounds.getRExtent() * 2.f;
			extent[1] = this.testingBounds.getSExtent() * 2.f;
			extent[2] = this.testingBounds.getTExtent() * 2.f;
			assertEquals(IntersectionType.ENCLOSING, this.testingBounds.classifies(
					new OrientedBoundingBox(center, axis, extent)));

			center.setX(this.testingBounds.getLower().getX() - 20.f);
			center.setY(this.testingBounds.getCenterY());
			center.setZ(this.testingBounds.getCenterZ());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[0].setZ(this.testingBounds.getR().getZ());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			axis[1].setZ(this.testingBounds.getS().getZ());
			axis[2].setX(this.testingBounds.getT().getX());
			axis[2].setY(this.testingBounds.getT().getY());
			axis[2].setZ(this.testingBounds.getT().getZ());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			extent[2] = this.testingBounds.getTExtent();
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new OrientedBoundingBox(center, axis, extent)));

			center.setX(this.testingBounds.getLower().getX());
			center.setY(this.testingBounds.getCenterY());
			center.setZ(this.testingBounds.getCenterZ());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[0].setZ(this.testingBounds.getR().getZ());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			axis[1].setZ(this.testingBounds.getS().getZ());
			axis[2].setX(this.testingBounds.getT().getX());
			axis[2].setY(this.testingBounds.getT().getY());
			axis[2].setZ(this.testingBounds.getT().getZ());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			extent[2] = this.testingBounds.getTExtent();
			assertEquals(IntersectionType.OUTSIDE, this.testingBounds.classifies(
					new OrientedBoundingBox(center, axis, extent)));

			center.setX((this.testingBounds.getLower().getX()) + this.Q.getX() / 2.f);
			center.setY(this.testingBounds.getCenterY());
			center.setZ(this.testingBounds.getCenterZ());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[0].setZ(this.testingBounds.getR().getZ());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			axis[1].setZ(this.testingBounds.getS().getZ());
			axis[2].setX(this.testingBounds.getT().getX());
			axis[2].setY(this.testingBounds.getT().getY());
			axis[2].setZ(this.testingBounds.getT().getZ());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			extent[2] = this.testingBounds.getTExtent();
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
					new OrientedBoundingBox(center, axis, extent)));

			center.setX(0.f);
			center.setY(0.f);
			center.setZ(0.f);
			axis[0].setX(1.f);
			axis[0].setY(0.f);
			axis[0].setZ(0.f);
			axis[1].setX(0.f);
			axis[1].setY(1.f);
			axis[1].setZ(0.f);
			axis[2].setX(0.f);
			axis[2].setY(0.f);
			axis[2].setZ(1.f);
			extent[0] = 1.f;
			extent[1] = 1.f;
			extent[2] = 1.f;
			assertEquals(IntersectionType.SPANNING, this.testingBounds.classifies(
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
			
			float minE = MathUtil.min(this.u, this.v, this.w);
			float maxE = MathUtil.max(this.u, this.v, this.w);
			
			p1.setX(this.Q.getX() - minE / 10.f);
			p1.setY(this.Q.getY() - minE / 10.f);
			p1.setZ(this.Q.getZ() - minE / 10.f);
			p2.setX(this.Q.getX() + minE / 10.f);
			p2.setY(this.Q.getY() + minE / 10.f);
			p2.setZ(this.Q.getZ() + minE / 10.f);
			assertTrue(this.testingBounds.intersects(
					new AlignedBoundingBox(p1,p2)));

			p1.setX(this.Q.getX() - maxE * 10.f);
			p1.setY(this.Q.getY() - maxE * 10.f);
			p1.setZ(this.Q.getZ() - maxE * 10.f);
			p2.setX(this.Q.getX() + maxE * 10.f);
			p2.setY(this.Q.getY() + maxE * 10.f);
			p2.setZ(this.Q.getZ() + maxE * 10.f);
			assertTrue(this.testingBounds.intersects(
					new AlignedBoundingBox(p1,p2)));

			p1.setX(this.Q.getX() - 1000.f);
			p1.setY(this.Q.getY() - 10.f);
			p1.setZ(this.Q.getZ() - 10.f);
			p2.setX(this.Q.getX() - 900.f);
			p2.setY(this.Q.getY() + 10.f);
			p2.setZ(this.Q.getZ() + 10.f);
			assertFalse(this.testingBounds.intersects(
					new AlignedBoundingBox(p1,p2)));

			p1.setX(-10.f);
			p1.setY(-10.f);
			p1.setZ(-10.f);
			p2.setX(0.5f);
			p2.setY(10.f);
			p2.setZ(10.f);
			assertTrue(this.testingBounds.intersects(
					new AlignedBoundingBox(p1,p2)));

			p1.setX(-10.f);
			p1.setY(-10.f);
			p1.setZ(-10.f);
			p2.setX(this.Q.getX());
			p2.setY(this.Q.getY());
			p2.setZ(this.Q.getZ());
			assertTrue(this.testingBounds.intersects(
					new AlignedBoundingBox(p1,p2)));

			p1.setX(this.Q.getX());
			p1.setY(this.Q.getY());
			p1.setZ(this.Q.getZ());
			p2.setX(this.Q.getX()+1);
			p2.setY(this.Q.getY()+1);
			p2.setZ(this.Q.getZ()+1);
			assertTrue(this.testingBounds.intersects(
					new AlignedBoundingBox(p1,p2)));
		}
		
		// BoundingSphere
		{
			Point3f c = new Point3f();
			float r;
			
			float minE = MathUtil.min(this.u, this.v, this.w);
			float maxE = MathUtil.max(this.u, this.v, this.w);

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = minE / 10.f;
			assertTrue(this.testingBounds.intersects(
					new BoundingSphere(c,r)));
			
			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = minE;
			assertTrue(this.testingBounds.intersects(
					new BoundingSphere(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = 0.f;
			assertTrue(this.testingBounds.intersects(
					new BoundingSphere(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = maxE * 10.f;
			assertTrue(this.testingBounds.intersects(
					new BoundingSphere(c,r)));

			c.setX(this.P1.getX());
			c.setY(this.P1.getY());
			c.setZ(this.P1.getZ());
			r = maxE * 50.f;
			assertTrue(this.testingBounds.intersects(
					new BoundingSphere(c,r)));

			c.setX(-10.f);
			c.setY(-10.f);
			c.setZ(-10.f);
			r = 1.f;
			assertFalse(this.testingBounds.intersects(
					new BoundingSphere(c,r)));

			c.setX(this.Q.getX());
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = maxE;
			assertTrue(this.testingBounds.intersects(
					new BoundingSphere(c,r)));

			c.setX(-2.f);
			c.setY(this.Q.getY());
			c.setZ(this.Q.getZ());
			r = 1.f;
			assertTrue(this.testingBounds.intersects(
					new BoundingSphere(c,r)));
		}
		
		// OrientedBoundingBox
		{
			Point3f center = new Point3f();
			Vector3f[] axis = new Vector3f[] {
					new Vector3f(),
					new Vector3f(),
					new Vector3f()
			};
			float[] extent = new float[3];
			
			center.setX(this.testingBounds.getCenterX());
			center.setY(this.testingBounds.getCenterY());
			center.setZ(this.testingBounds.getCenterZ());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[0].setZ(this.testingBounds.getR().getZ());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			axis[1].setZ(this.testingBounds.getS().getZ());
			axis[2].setX(this.testingBounds.getT().getX());
			axis[2].setY(this.testingBounds.getT().getY());
			axis[2].setZ(this.testingBounds.getT().getZ());
			extent[0] = this.testingBounds.getRExtent() / 2.f;
			extent[1] = this.testingBounds.getSExtent() / 2.f;
			extent[2] = this.testingBounds.getTExtent() / 2.f;
			assertTrue(this.testingBounds.intersects(
					new OrientedBoundingBox(center, axis, extent)));

			center.setX(this.testingBounds.getCenterX());
			center.setY(this.testingBounds.getCenterY());
			center.setZ(this.testingBounds.getCenterZ());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[0].setZ(this.testingBounds.getR().getZ());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			axis[1].setZ(this.testingBounds.getS().getZ());
			axis[2].setX(this.testingBounds.getT().getX());
			axis[2].setY(this.testingBounds.getT().getY());
			axis[2].setZ(this.testingBounds.getT().getZ());
			extent[0] = this.testingBounds.getRExtent() * 2.f;
			extent[1] = this.testingBounds.getSExtent() * 2.f;
			extent[2] = this.testingBounds.getTExtent() * 2.f;
			assertTrue(this.testingBounds.intersects(
					new OrientedBoundingBox(center, axis, extent)));

			center.setX(this.testingBounds.getLower().getX() - 20.f);
			center.setY(this.testingBounds.getCenterY());
			center.setZ(this.testingBounds.getCenterZ());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[0].setZ(this.testingBounds.getR().getZ());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			axis[1].setZ(this.testingBounds.getS().getZ());
			axis[2].setX(this.testingBounds.getT().getX());
			axis[2].setY(this.testingBounds.getT().getY());
			axis[2].setZ(this.testingBounds.getT().getZ());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			extent[2] = this.testingBounds.getTExtent();
			assertFalse(this.testingBounds.intersects(
					new OrientedBoundingBox(center, axis, extent)));

			center.setX(this.testingBounds.getLower().getX());
			center.setY(this.testingBounds.getCenterY());
			center.setZ(this.testingBounds.getCenterZ());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[0].setZ(this.testingBounds.getR().getZ());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			axis[1].setZ(this.testingBounds.getS().getZ());
			axis[2].setX(this.testingBounds.getT().getX());
			axis[2].setY(this.testingBounds.getT().getY());
			axis[2].setZ(this.testingBounds.getT().getZ());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			extent[2] = this.testingBounds.getTExtent();
			assertFalse(this.testingBounds.intersects(
					new OrientedBoundingBox(center, axis, extent)));

			center.setX((this.testingBounds.getLower().getX()) + this.Q.getX() / 2.f);
			center.setY(this.testingBounds.getCenterY());
			center.setZ(this.testingBounds.getCenterZ());
			axis[0].setX(this.testingBounds.getR().getX());
			axis[0].setY(this.testingBounds.getR().getY());
			axis[0].setZ(this.testingBounds.getR().getZ());
			axis[1].setX(this.testingBounds.getS().getX());
			axis[1].setY(this.testingBounds.getS().getY());
			axis[1].setZ(this.testingBounds.getS().getZ());
			axis[2].setX(this.testingBounds.getT().getX());
			axis[2].setY(this.testingBounds.getT().getY());
			axis[2].setZ(this.testingBounds.getT().getZ());
			extent[0] = this.testingBounds.getRExtent();
			extent[1] = this.testingBounds.getSExtent();
			extent[2] = this.testingBounds.getTExtent();
			assertTrue(this.testingBounds.intersects(
					new OrientedBoundingBox(center, axis, extent)));

			center.setX(0.f);
			center.setY(0.f);
			center.setZ(0.f);
			axis[0].setX(1.f);
			axis[0].setY(0.f);
			axis[0].setZ(0.f);
			axis[1].setX(0.f);
			axis[1].setY(1.f);
			axis[1].setZ(0.f);
			axis[2].setX(0.f);
			axis[2].setY(0.f);
			axis[2].setZ(1.f);
			extent[0] = 1.f;
			extent[1] = 1.f;
			extent[2] = 1.f;
			assertTrue(this.testingBounds.intersects(
					new OrientedBoundingBox(center, axis, extent)));
		}
	}

	/**
	 */
	@Override
	public void testToBounds2D() {
		assertTrue(this.testingBounds.isInit());

		OrientedBoundingRectangle obr = this.testingBounds.toBounds2D();
		
		assertNotNull(obr);
		assertEpsilonEquals(new Point2f(this.Q.getX(), this.Q.getY()), obr.getCenter());
	}
	
	/**
	 */
	@Override
	public void testToBounds2DCoordinateSystem3D() {
		assertTrue(this.testingBounds.isInit());

		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			OrientedBoundingRectangle obr = this.testingBounds.toBounds2D(cs);
			
			Point2f p = cs.toCoordinateSystem2D(this.Q); 
			
			assertNotNull(obr);
			assertEpsilonEquals(p, obr.getCenter());
		}
	}

	/**
	 */
	@Override
	public void testRotateAxisAngle4f() {
		AxisAngle4f a = randomAxisAngle4f();
		
		Matrix4f m = new Matrix4f();
		m.set(a);
		Point3f Rr = new Point3f(this.R);
		m.transform(Rr);
		Point3f Sr = new Point3f(this.S);
		m.transform(Sr);
		Point3f Tr = new Point3f(this.T);
		m.transform(Tr);
		
		this.testingBounds.rotate(a);
		
		assertEpsilonEquals(this.Q, this.testingBounds.getCenter());
		assertEpsilonEquals(Rr, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(Sr, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(Tr, this.testingBounds.getOrientedBoundAxis()[2]);
	}

	/**
	 */
	@Override
	public void testRotateQuaternion() {
		Quaternion a = randomQuaternion();
		
		Matrix4f m = new Matrix4f();
		m.set(a);
		Point3f Rr = new Point3f(this.R);
		m.transform(Rr);
		Point3f Sr = new Point3f(this.S);
		m.transform(Sr);
		Point3f Tr = new Point3f(this.T);
		m.transform(Tr);
		
		this.testingBounds.rotate(a);
		
		assertEpsilonEquals(this.Q, this.testingBounds.getCenter());
		assertEpsilonEquals(Rr, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(Sr, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(Tr, this.testingBounds.getOrientedBoundAxis()[2]);
	}

	/**
	 */
	@Override
	public void testSetRotationAxisAngle4f() {
		AxisAngle4f a = randomAxisAngle4f();
		
		Matrix4f m = new Matrix4f();
		m.set(a);
		Point3f Rr = new Point3f(1.,0.f,0.f);
		m.transform(Rr);
		Point3f Sr = new Point3f(0.f,1.,0.f);
		m.transform(Sr);
		Point3f Tr = new Point3f(0.f,0.f,1.);
		m.transform(Tr);
		
		this.testingBounds.setRotation(a);
		
		assertEpsilonEquals(this.Q, this.testingBounds.getCenter());
		assertEpsilonEquals(Rr, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(Sr, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(Tr, this.testingBounds.getOrientedBoundAxis()[2]);
	}

	/**
	 */
	@Override
	public void testSetRotationQuaternion() {
		Quaternion a = randomQuaternion();
		
		Matrix4f m = new Matrix4f();
		m.set(a);
		Point3f Rr = new Point3f(1.,0.f,0.f);
		m.transform(Rr);
		Point3f Sr = new Point3f(0.f,1.,0.f);
		m.transform(Sr);
		Point3f Tr = new Point3f(0.f,0.f,1.);
		m.transform(Tr);
		
		this.testingBounds.setRotation(a);
		
		assertEpsilonEquals(this.Q, this.testingBounds.getCenter());
		assertEpsilonEquals(Rr, this.testingBounds.getOrientedBoundAxis()[0]);
		assertEpsilonEquals(Sr, this.testingBounds.getOrientedBoundAxis()[1]);
		assertEpsilonEquals(Tr, this.testingBounds.getOrientedBoundAxis()[2]);
	}
	
	/**
	 */
	public void testSetPlaneConstraintCollection_OXY() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	/**
	 */
	public void testSetPlaneConstraintCollection_OXZ() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

	/**
	 */
	public void testSetPlaneConstraintCollection_OYZ() {
		warning("not yet implemented"); //$NON-NLS-1$
	}

}
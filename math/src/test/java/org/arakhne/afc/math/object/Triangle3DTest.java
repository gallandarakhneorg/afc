/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.object;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;
import org.arakhne.afc.math.plane.Plane;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.math.transform.Transform3D;
import org.arakhne.util.ref.AbstractTestCase;



/**
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Triangle3DTest extends AbstractTestCase {

	private Point3f p1, p2, p3;
	private Vector3f v12, v13, normal;
	private Triangle3D triangle;
	private AxisAngle4f orientation;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.p1 = new Point3f(1., 1., 1.);
		this.p2 = new Point3f(-1., 1., 1.);
		this.p3 = new Point3f(0., 0., 0.);
		this.v12 = new Vector3f();
		this.v12.sub(this.p2, this.p1);
		this.v13 = new Vector3f();
		this.v13.sub(this.p3, this.p1);
		this.triangle = new Triangle3D(this.p1, this.p2, this.p3, true);
		this.normal = new Vector3f();
		if (CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded()) {
			MathUtil.crossProductLeftHand(
					this.p2.getX()-this.p1.getX(),
					this.p2.getY()-this.p1.getY(),
					this.p2.getZ()-this.p1.getZ(),
					this.p3.getX()-this.p1.getX(),
					this.p3.getY()-this.p1.getY(),
					this.p3.getZ()-this.p1.getZ(),
					this.normal);
		}
		else {
			MathUtil.crossProductRightHand(
					this.p2.getX()-this.p1.getX(),
					this.p2.getY()-this.p1.getY(),
					this.p2.getZ()-this.p1.getZ(),
					this.p3.getX()-this.p1.getX(),
					this.p3.getY()-this.p1.getY(),
					this.p3.getZ()-this.p1.getZ(),
					this.normal);
		}
		this.normal.normalize();
		this.orientation = new AxisAngle4f(1.f, 0.f, 0.f, MathConstants.PI/4.f);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void tearDown() throws Exception {
		this.triangle = null;
		this.p1 = this.p2 = this.p3 = null;
		this.v12 = this.v13 = this.normal = null;
		this.orientation = null;
		super.tearDown();
	}

	/**
	 */
	public void testGetPoint1() {
		assertEpsilonEquals(this.p1, this.triangle.getPoint1());
	}

	/**
	 */
	public void testGetPoint2() {
		assertEpsilonEquals(this.p2, this.triangle.getPoint2());
	}

	/**
	 */
	public void testGetPoint3() {
		assertEpsilonEquals(this.p3, this.triangle.getPoint3());
	}

	/**
	 */
	public void testGetNormal() {
		assertEpsilonEquals(this.normal, this.triangle.getNormal());
	}

	/**
	 */
	public void testGetPlane() {
		float d = -(this.normal.getX() * this.p1.getX() + this.normal.getY() * this.p1.getY() + this.normal.getZ() * this.p1.getZ());

		Plane plane = this.triangle.getPlane();
		assertNotNull(plane);
		assertEpsilonEquals(this.normal.getX(), plane.getEquationComponentA());
		assertEpsilonEquals(this.normal.getY(), plane.getEquationComponentB());
		assertEpsilonEquals(this.normal.getZ(), plane.getEquationComponentC());
		assertEpsilonEquals(d, plane.getEquationComponentD());
	}

	/**
	 */
	public void testDistanceToFloatFloatFloat() {
		Point3f barycenter = new Point3f();
		barycenter.add(this.p1, this.p2);
		barycenter.add(this.p3);
		barycenter.scale(1.f/3.f);

		assertEpsilonEquals(0.f, this.triangle.distanceTo(this.p1.getX(), this.p1.getY(), this.p1.getZ()));
		assertEpsilonEquals(0.f, this.triangle.distanceTo(this.p2.getX(), this.p2.getY(), this.p2.getZ()));
		assertEpsilonEquals(0.f, this.triangle.distanceTo(this.p3.getX(), this.p3.getY(), this.p3.getZ()));
		assertEpsilonEquals(0.f, this.triangle.distanceTo(barycenter.getX(), barycenter.getY(), barycenter.getZ()));

		assertEpsilonEquals(
				MathUtil.distance(1.f, 1.f, 1.f, 100.f, 100.f, 100.f),
				this.triangle.distanceTo(100.f, 100.f, 100.f));
	}

	/**
	 */
	public void testDistanceToTuple3d() {
		Point3f barycenter = new Point3f();
		barycenter.add(this.p1, this.p2);
		barycenter.add(this.p3);
		barycenter.scale(1.f/3.f);

		assertEpsilonEquals(0.f, this.triangle.distanceTo(this.p1));
		assertEpsilonEquals(0.f, this.triangle.distanceTo(this.p2));
		assertEpsilonEquals(0.f, this.triangle.distanceTo(this.p3));
		assertEpsilonEquals(0.f, this.triangle.distanceTo(barycenter));

		assertEpsilonEquals(
				MathUtil.distance(1.f, 1.f, 1.f, 100.f, 100.f, 100.f),
				this.triangle.distanceTo(new Point3f(100., 100., 100.)));
	}

	/**
	 */
	public void testContains() {
		Point3f barycenter = new Point3f();
		barycenter.add(this.p1, this.p2);
		barycenter.add(this.p3);
		barycenter.scale(1.f/3.f);

		assertTrue(this.triangle.contains(this.p1));
		assertTrue(this.triangle.contains(this.p2));
		assertTrue(this.triangle.contains(this.p3));
		assertTrue(this.triangle.contains(barycenter));

		assertFalse(this.triangle.contains(new Point3f(100., 100., 100.)));
	}

	/**
	 */
	public void testGetPivot() {
		assertEpsilonEquals(this.p1, this.triangle.getPivot());
	}

	/**
	 */
	public void testSetPivotFloatFloatFloat() {
		assertEpsilonEquals(this.p1, this.triangle.getPivot());
		this.triangle.setPivot(1000.f, 1001.f, 1002.f);
		assertEpsilonEquals(new Point3f(1000., 1001., 1002.), this.triangle.getPivot());
	}

	/**
	 */
	public void testSetPivotPoint3d() {
		assertEpsilonEquals(this.p1, this.triangle.getPivot());
		this.triangle.setPivot(new Point3f(1000., 1001., 1002.));
		assertEpsilonEquals(new Point3f(1000., 1001., 1002.), this.triangle.getPivot());
	}

	/**
	 */
	public void testGetAxisAngle() {
		assertEpsilonEquals(this.orientation, this.triangle.getAxisAngle());
	}

	/**
	 */
	public void testGetTransformMatrix() {
		Transform3D tr = this.triangle.getTransformMatrix();
		
		Vector3f p = new Vector3f();
		tr.get(p);
		Quaternion q = new Quaternion();
		AxisAngle4f aa = new AxisAngle4f();
		tr.get(q);
		aa.set(q);
		
		assertEpsilonEquals(this.p1, p);
		assertEpsilonEquals(this.orientation, aa);
	}

	/**
	 */
	public void testGetTranslation() {
		assertEpsilonEquals(this.p1, this.triangle.getTranslation());
	}

	/**
	 */
	public void testTranslateFloatFloatFloat() {
		Vector3f v = randomVector3D();
		this.triangle.translate(v.getX(), v.getY(), v.getZ());
		Point3f p = new Point3f(this.p1);
		p.add(v);
		assertEpsilonEquals(p, this.triangle.getTranslation());
	}

	/**
	 */
	public void testTranslateVector3d() {
		Vector3f v = randomVector3D();
		this.triangle.translate(v);
		Point3f p = new Point3f(this.p1);
		p.add(v);
		assertEpsilonEquals(p, this.triangle.getTranslation());
	}

	/**
	 */
	public void testSetTranslationFloatFloatFloat() {
		Point3f pos = randomPoint3D();
		this.triangle.setTranslation(pos.getX(), pos.getY(), pos.getZ());
		assertEpsilonEquals(pos, this.triangle.getTranslation());
	}

	/**
	 */
	public void testSetTranslationPoint3d() {
		Point3f pos = randomPoint3D();
		this.triangle.setTranslation(new Point3f(pos.getX(), pos.getY(), pos.getZ()));
		assertEpsilonEquals(pos, this.triangle.getTranslation());
	}

	/**
	 */
	public void testRotateAxisAngle4d() {
		AxisAngle4f aa = new AxisAngle4f(1.f, 0.f, 0.f, -MathConstants.PI);
		
		this.triangle.rotate(aa);
		
		AxisAngle4f expected = new AxisAngle4f(-1.f, 0.f, 0.f, MathConstants.PI/4.f-MathConstants.PI);
		
		assertEpsilonEquals(this.p1, this.triangle.getTranslation());
		assertEpsilonEquals(expected, this.triangle.getAxisAngle());

		assertEpsilonEquals(this.p1, this.triangle.getPoint1());
		assertEpsilonEquals(this.p2, this.triangle.getPoint2());
		assertEpsilonEquals(new Point3f(0., 2., 2.), this.triangle.getPoint3());
	}

	/**
	 */
	public void testRotateQuat4d() {
		AxisAngle4f aa = new AxisAngle4f(1.f, 0.f, 0.f, -MathConstants.PI);
		Quaternion q = new Quaternion();
		q.set(aa);
		
		this.triangle.rotate(q);
		
		AxisAngle4f expected = new AxisAngle4f(-1.f, 0.f, 0.f, MathConstants.PI/4.f-MathConstants.PI);
		
		assertEpsilonEquals(this.p1, this.triangle.getTranslation());
		assertEpsilonEquals(expected, this.triangle.getAxisAngle());

		assertEpsilonEquals(this.p1, this.triangle.getPoint1());
		assertEpsilonEquals(this.p2, this.triangle.getPoint2());
		assertEpsilonEquals(new Point3f(0., 2., 2.), this.triangle.getPoint3());
	}

	/**
	 */
	public void testRotateAxisAngle4dPoint3d() {
		AxisAngle4f aa = new AxisAngle4f(1.f, 0.f, 0.f, -MathConstants.PI);
		
		this.triangle.rotate(aa, new Point3f(0., 0., 1.));

		AxisAngle4f expected = new AxisAngle4f(-1.f, 0.f, 0.f, MathConstants.PI/4.f-MathConstants.PI);
		
		assertEpsilonEquals(new Point3f(1,-1,1), this.triangle.getTranslation());
		assertEpsilonEquals(expected, this.triangle.getAxisAngle());

		assertEpsilonEquals(new Point3f(1,-1,1), this.triangle.getPoint1());
		assertEpsilonEquals(new Point3f(-1,-1,1), this.triangle.getPoint2());
		assertEpsilonEquals(new Point3f(0,0,2), this.triangle.getPoint3());
	}

	/**
	 */
	public void testRotateQuat4dPoint3d() {
		AxisAngle4f aa = new AxisAngle4f(1.f, 0.f, 0.f, -MathConstants.PI);
		Quaternion q = new Quaternion();
		q.set(aa);
		
		this.triangle.rotate(q, new Point3f(0., 0., 1.));

		AxisAngle4f expected = new AxisAngle4f(-1.f, 0.f, 0.f, MathConstants.PI/4.f-MathConstants.PI);
		
		assertEpsilonEquals(new Point3f(1,-1,1), this.triangle.getTranslation());
		assertEpsilonEquals(expected, this.triangle.getAxisAngle());

		assertEpsilonEquals(new Point3f(1,-1,1), this.triangle.getPoint1());
		assertEpsilonEquals(new Point3f(-1,-1,1), this.triangle.getPoint2());
		assertEpsilonEquals(new Point3f(0,0,2), this.triangle.getPoint3());
	}

	/**
	 */
	public void testSetRotationAxisAngle4d() {
		AxisAngle4f aa = new AxisAngle4f(1.f, 0.f, 0.f, -MathConstants.PI);
		
		this.triangle.setRotation(aa);

		AxisAngle4f expected = new AxisAngle4f(-1.f, 0.f, 0.f, MathConstants.PI);
		
		assertEpsilonEquals(this.p1, this.triangle.getTranslation());
		assertEpsilonEquals(expected, this.triangle.getAxisAngle());

		assertEpsilonEquals(this.p1, this.triangle.getPoint1());
		assertEpsilonEquals(new Point3f(-1,1,1), this.triangle.getPoint2());
		assertEpsilonEquals(new Point3f(0,2.414213562373095,1), this.triangle.getPoint3());
	}

	/**
	 */
	public void testSetRotationQuat4d() {
		AxisAngle4f aa = new AxisAngle4f(1.f, 0.f, 0.f, -MathConstants.PI);
		Quaternion q = new Quaternion();
		q.set(aa);
		
		this.triangle.setRotation(q);

		AxisAngle4f expected = new AxisAngle4f(-1.f, 0.f, 0.f, MathConstants.PI);
		
		assertEpsilonEquals(this.p1, this.triangle.getTranslation());
		assertEpsilonEquals(expected, this.triangle.getAxisAngle());

		assertEpsilonEquals(this.p1, this.triangle.getPoint1());
		assertEpsilonEquals(new Point3f(-1,1,1), this.triangle.getPoint2());
		assertEpsilonEquals(new Point3f(0,2.414213562373095,1), this.triangle.getPoint3());
	}


}

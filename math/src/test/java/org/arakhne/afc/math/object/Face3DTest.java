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

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.util.ref.AbstractTestCase;

/**
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Face3DTest extends AbstractTestCase {

	private Point3f p1, p2, p3;
	private Vector3f v12, v13;
	private Facet3D triangle;
	
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
		this.triangle = new Facet3D(this.p1, this.p2, this.p3, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void tearDown() throws Exception {
		this.triangle = null;
		this.p1 = this.p2 = this.p3 = null;
		this.v12 = this.v13 = null;
		super.tearDown();
	}

	/**
	 */
	public void testContainsPoint2d() {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		
		assertFalse(this.triangle.contains(new Point2f(-100, -100)));
		assertFalse(this.triangle.contains(new Point2f(100, -100)));
		assertFalse(this.triangle.contains(new Point2f(100, 100)));
		assertFalse(this.triangle.contains(new Point2f(-100, 100)));

		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p1)));
		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p2)));
		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p3)));

		assertTrue(this.triangle.contains(new Point2f(0., 1.)));
		assertTrue(this.triangle.contains(new Point2f(0., .5)));

		assertFalse(this.triangle.contains(new Point2f(1., 0.)));
		assertFalse(this.triangle.contains(new Point2f(-1., 0.)));
		assertFalse(this.triangle.contains(new Point2f(0., -1.)));
	}
	
	/**
	 */
	public void testContainsPoint2dCoordinateSystem3D_XYZ_RIGHT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XYZ_RIGHT_HAND;
		
		assertFalse(this.triangle.contains(new Point2f(-100, -100), cs));
		assertFalse(this.triangle.contains(new Point2f(100, -100), cs));
		assertFalse(this.triangle.contains(new Point2f(100, 100), cs));
		assertFalse(this.triangle.contains(new Point2f(-100, 100), cs));

		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p1), cs));
		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p2), cs));
		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p3), cs));

		assertTrue(this.triangle.contains(new Point2f(0., 1.), cs));
		assertTrue(this.triangle.contains(new Point2f(0., .5), cs));

		assertFalse(this.triangle.contains(new Point2f(1., 0.), cs));
		assertFalse(this.triangle.contains(new Point2f(-1., 0.), cs));
		assertFalse(this.triangle.contains(new Point2f(0., -1.), cs));
	}

	/**
	 */
	public void testContainsPoint2dCoordinateSystem3D_XYZ_LEFT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XYZ_LEFT_HAND;
		
		assertFalse(this.triangle.contains(new Point2f(-100, -100), cs));
		assertFalse(this.triangle.contains(new Point2f(100, -100), cs));
		assertFalse(this.triangle.contains(new Point2f(100, 100), cs));
		assertFalse(this.triangle.contains(new Point2f(-100, 100), cs));

		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p1), cs));
		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p2), cs));
		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p3), cs));

		assertTrue(this.triangle.contains(new Point2f(0., 1.), cs));
		assertTrue(this.triangle.contains(new Point2f(0., .5), cs));

		assertFalse(this.triangle.contains(new Point2f(1., 0.), cs));
		assertFalse(this.triangle.contains(new Point2f(-1., 0.), cs));
		assertFalse(this.triangle.contains(new Point2f(0., -1.), cs));
	}

	/**
	 */
	public void testContainsPoint2dCoordinateSystem3D_XZY_RIGHT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XZY_RIGHT_HAND;
		
		assertFalse(this.triangle.contains(new Point2f(-100, -100), cs));
		assertFalse(this.triangle.contains(new Point2f(100, -100), cs));
		assertFalse(this.triangle.contains(new Point2f(100, 100), cs));
		assertFalse(this.triangle.contains(new Point2f(-100, 100), cs));

		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p1), cs));
		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p2), cs));
		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p3), cs));

		assertTrue(this.triangle.contains(new Point2f(0., 1.), cs));
		assertTrue(this.triangle.contains(new Point2f(0., .5), cs));

		assertFalse(this.triangle.contains(new Point2f(1., 0.), cs));
		assertFalse(this.triangle.contains(new Point2f(-1., 0.), cs));
		assertFalse(this.triangle.contains(new Point2f(0., -1.), cs));
	}

	/**
	 */
	public void testContainsPoint2dCoordinateSystem3D_XZY_LEFT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XZY_LEFT_HAND;
		
		assertFalse(this.triangle.contains(new Point2f(-100, -100), cs));
		assertFalse(this.triangle.contains(new Point2f(100, -100), cs));
		assertFalse(this.triangle.contains(new Point2f(100, 100), cs));
		assertFalse(this.triangle.contains(new Point2f(-100, 100), cs));

		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p1), cs));
		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p2), cs));
		assertTrue(this.triangle.contains(cs.toCoordinateSystem2D(this.p3), cs));

		assertTrue(this.triangle.contains(new Point2f(0., 1.), cs));
		assertTrue(this.triangle.contains(new Point2f(0., .5), cs));

		assertFalse(this.triangle.contains(new Point2f(1., 0.), cs));
		assertFalse(this.triangle.contains(new Point2f(-1., 0.), cs));
		assertFalse(this.triangle.contains(new Point2f(0., -1.), cs));
	}

	/**
	 */
	public void testInterpolateHeightDoubleDouble() {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(h1, this.triangle.interpolateHeight(p1_2.getX(), p1_2.getY()));
		assertEpsilonEquals(h2, this.triangle.interpolateHeight(p2_2.getX(), p2_2.getY()));
		assertEpsilonEquals(h3, this.triangle.interpolateHeight(p3_2.getX(), p3_2.getY()));
		
		assertEpsilonEquals((h1+h2)/2.f, this.triangle.interpolateHeight(
				(p1_2.getX()+p2_2.getX())/2.f, (p1_2.getY()+p2_2.getY())/2.f));		
		assertEpsilonEquals((h1+h3)/2.f, this.triangle.interpolateHeight(
				(p1_2.getX()+p3_2.getX())/2.f, (p1_2.getY()+p3_2.getY())/2.f));		
		assertEpsilonEquals((h2+h3)/2.f, this.triangle.interpolateHeight(
				(p2_2.getX()+p3_2.getX())/2.f, (p2_2.getY()+p3_2.getY())/2.f));
	}

	/**
	 */
	public void testInterpolateHeightDoubleDoubleCoordinateSystem3D_XYZ_RIGHT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XYZ_RIGHT_HAND;

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(h1, this.triangle.interpolateHeight(p1_2.getX(), p1_2.getY(), cs));
		assertEpsilonEquals(h2, this.triangle.interpolateHeight(p2_2.getX(), p2_2.getY(), cs));
		assertEpsilonEquals(h3, this.triangle.interpolateHeight(p3_2.getX(), p3_2.getY(), cs));
		
		assertEpsilonEquals((h1+h2)/2.f, this.triangle.interpolateHeight(
				(p1_2.getX()+p2_2.getX())/2.f, (p1_2.getY()+p2_2.getY())/2.f, cs));		
		assertEpsilonEquals((h1+h3)/2.f, this.triangle.interpolateHeight(
				(p1_2.getX()+p3_2.getX())/2.f, (p1_2.getY()+p3_2.getY())/2.f, cs));		
		assertEpsilonEquals((h2+h3)/2.f, this.triangle.interpolateHeight(
				(p2_2.getX()+p3_2.getX())/2.f, (p2_2.getY()+p3_2.getY())/2.f, cs));
	}

	/**
	 */
	public void testInterpolateHeightDoubleDoubleCoordinateSystem3D_XYZ_LEFT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XYZ_LEFT_HAND;

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(h1, this.triangle.interpolateHeight(p1_2.getX(), p1_2.getY(), cs));
		assertEpsilonEquals(h2, this.triangle.interpolateHeight(p2_2.getX(), p2_2.getY(), cs));
		assertEpsilonEquals(h3, this.triangle.interpolateHeight(p3_2.getX(), p3_2.getY(), cs));
		
		assertEpsilonEquals((h1+h2)/2.f, this.triangle.interpolateHeight(
				(p1_2.getX()+p2_2.getX())/2.f, (p1_2.getY()+p2_2.getY())/2.f, cs));		
		assertEpsilonEquals((h1+h3)/2.f, this.triangle.interpolateHeight(
				(p1_2.getX()+p3_2.getX())/2.f, (p1_2.getY()+p3_2.getY())/2.f, cs));		
		assertEpsilonEquals((h2+h3)/2.f, this.triangle.interpolateHeight(
				(p2_2.getX()+p3_2.getX())/2.f, (p2_2.getY()+p3_2.getY())/2.f, cs));
	}

	/**
	 */
	public void testInterpolateHeightDoubleDoubleCoordinateSystem3D_XZY_RIGHT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XZY_RIGHT_HAND;

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(h1, this.triangle.interpolateHeight(p1_2.getX(), p1_2.getY(), cs));
		assertEpsilonEquals(h2, this.triangle.interpolateHeight(p2_2.getX(), p2_2.getY(), cs));
		assertEpsilonEquals(h3, this.triangle.interpolateHeight(p3_2.getX(), p3_2.getY(), cs));
		
		assertEpsilonEquals((h1+h2)/2.f, this.triangle.interpolateHeight(
				(p1_2.getX()+p2_2.getX())/2.f, (p1_2.getY()+p2_2.getY())/2.f, cs));		
		assertEpsilonEquals((h1+h3)/2.f, this.triangle.interpolateHeight(
				(p1_2.getX()+p3_2.getX())/2.f, (p1_2.getY()+p3_2.getY())/2.f, cs));		
		assertEpsilonEquals((h2+h3)/2.f, this.triangle.interpolateHeight(
				(p2_2.getX()+p3_2.getX())/2.f, (p2_2.getY()+p3_2.getY())/2.f, cs));
	}

	/**
	 */
	public void testInterpolateHeightDoubleDoubleCoordinateSystem3D_XZY_LEFT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XZY_LEFT_HAND;

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(h1, this.triangle.interpolateHeight(p1_2.getX(), p1_2.getY(), cs));
		assertEpsilonEquals(h2, this.triangle.interpolateHeight(p2_2.getX(), p2_2.getY(), cs));
		assertEpsilonEquals(h3, this.triangle.interpolateHeight(p3_2.getX(), p3_2.getY(), cs));
		
		assertEpsilonEquals((h1+h2)/2.f, this.triangle.interpolateHeight(
				(p1_2.getX()+p2_2.getX())/2.f, (p1_2.getY()+p2_2.getY())/2.f, cs));		
		assertEpsilonEquals((h1+h3)/2.f, this.triangle.interpolateHeight(
				(p1_2.getX()+p3_2.getX())/2.f, (p1_2.getY()+p3_2.getY())/2.f, cs));		
		assertEpsilonEquals((h2+h3)/2.f, this.triangle.interpolateHeight(
				(p2_2.getX()+p3_2.getX())/2.f, (p2_2.getY()+p3_2.getY())/2.f, cs));
	}

	/**
	 */
	public void testInterpolateHeightPoint2d() {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(h1, this.triangle.interpolateHeight(p1_2));
		assertEpsilonEquals(h2, this.triangle.interpolateHeight(p2_2));
		assertEpsilonEquals(h3, this.triangle.interpolateHeight(p3_2));
		
		assertEpsilonEquals((h1+h2)/2.f, this.triangle.interpolateHeight(
				new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.)));		
		assertEpsilonEquals((h1+h3)/2.f, this.triangle.interpolateHeight(
				new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.)));		
		assertEpsilonEquals((h2+h3)/2.f, this.triangle.interpolateHeight(
				new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.)));
	}

	/**
	 */
	public void testInterpolateHeightPoint2dCoordinateSystem3D_XYZ_RIGHT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XYZ_RIGHT_HAND;

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(h1, this.triangle.interpolateHeight(p1_2, cs));
		assertEpsilonEquals(h2, this.triangle.interpolateHeight(p2_2, cs));
		assertEpsilonEquals(h3, this.triangle.interpolateHeight(p3_2, cs));
		
		assertEpsilonEquals((h1+h2)/2.f, this.triangle.interpolateHeight(
				new Point2f((p1_2.getX()+p2_2.getX())/2.f, (p1_2.getY()+p2_2.getY())/2.f), cs));		
		assertEpsilonEquals((h1+h3)/2.f, this.triangle.interpolateHeight(
				new Point2f((p1_2.getX()+p3_2.getX())/2.f, (p1_2.getY()+p3_2.getY())/2.f), cs));		
		assertEpsilonEquals((h2+h3)/2.f, this.triangle.interpolateHeight(
				new Point2f((p2_2.getX()+p3_2.getX())/2.f, (p2_2.getY()+p3_2.getY())/2.f), cs));
	}

	/**
	 */
	public void testInterpolateHeightPoint2dCoordinateSystem3D_XYZ_LEFT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XYZ_LEFT_HAND;

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(h1, this.triangle.interpolateHeight(p1_2, cs));
		assertEpsilonEquals(h2, this.triangle.interpolateHeight(p2_2, cs));
		assertEpsilonEquals(h3, this.triangle.interpolateHeight(p3_2, cs));
		
		assertEpsilonEquals((h1+h2)/2.f, this.triangle.interpolateHeight(
				new Point2f((p1_2.getX()+p2_2.getX())/2.f, (p1_2.getY()+p2_2.getY())/2.f), cs));		
		assertEpsilonEquals((h1+h3)/2.f, this.triangle.interpolateHeight(
				new Point2f((p1_2.getX()+p3_2.getX())/2.f, (p1_2.getY()+p3_2.getY())/2.f), cs));		
		assertEpsilonEquals((h2+h3)/2.f, this.triangle.interpolateHeight(
				new Point2f((p2_2.getX()+p3_2.getX())/2.f, (p2_2.getY()+p3_2.getY())/2.f), cs));
	}

	/**
	 */
	public void testInterpolateHeightPoint2dCoordinateSystem3D_XZY_RIGHT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XZY_RIGHT_HAND;
		
		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(h1, this.triangle.interpolateHeight(p1_2, cs));
		assertEpsilonEquals(h2, this.triangle.interpolateHeight(p2_2, cs));
		assertEpsilonEquals(h3, this.triangle.interpolateHeight(p3_2, cs));
		
		assertEpsilonEquals((h1+h2)/2.f, this.triangle.interpolateHeight(
				new Point2f((p1_2.getX()+p2_2.getX())/2.f, (p1_2.getY()+p2_2.getY())/2.f), cs));		
		assertEpsilonEquals((h1+h3)/2.f, this.triangle.interpolateHeight(
				new Point2f((p1_2.getX()+p3_2.getX())/2.f, (p1_2.getY()+p3_2.getY())/2.f), cs));		
		assertEpsilonEquals((h2+h3)/2.f, this.triangle.interpolateHeight(
				new Point2f((p2_2.getX()+p3_2.getX())/2.f, (p2_2.getY()+p3_2.getY())/2.f), cs));
	}

	/**
	 */
	public void testInterpolateHeightPoint2dCoordinateSystem3D_XZY_LEFT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XZY_LEFT_HAND;

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(h1, this.triangle.interpolateHeight(p1_2, cs));
		assertEpsilonEquals(h2, this.triangle.interpolateHeight(p2_2, cs));
		assertEpsilonEquals(h3, this.triangle.interpolateHeight(p3_2, cs));
		
		assertEpsilonEquals((h1+h2)/2.f, this.triangle.interpolateHeight(
				new Point2f((p1_2.getX()+p2_2.getX())/2.f, (p1_2.getY()+p2_2.getY())/2.f), cs));		
		assertEpsilonEquals((h1+h3)/2.f, this.triangle.interpolateHeight(
				new Point2f((p1_2.getX()+p3_2.getX())/2.f, (p1_2.getY()+p3_2.getY())/2.f), cs));		
		assertEpsilonEquals((h2+h3)/2.f, this.triangle.interpolateHeight(
				new Point2f((p2_2.getX()+p3_2.getX())/2.f, (p2_2.getY()+p3_2.getY())/2.f), cs));
	}

	/**
	 */
	public void testInterpolatePointDoubleDouble() {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		
		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(new Point3f(p1_2.getX(), p1_2.getY(), h1), this.triangle.interpolatePoint(p1_2.getX(), p1_2.getY()));
		assertEpsilonEquals(new Point3f(p2_2.getX(), p2_2.getY(), h2), this.triangle.interpolatePoint(p2_2.getX(), p2_2.getY()));
		assertEpsilonEquals(new Point3f(p3_2.getX(), p3_2.getY(), h3), this.triangle.interpolatePoint(p3_2.getX(), p3_2.getY()));
		
		Point2f p; 
		
		p = new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h2)/2.), this.triangle.interpolatePoint(p.getX(), p.getY()));		

		p = new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h3)/2.), this.triangle.interpolatePoint(p.getX(), p.getY()));
		
		p = new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h2+h3)/2.), this.triangle.interpolatePoint(p.getX(), p.getY()));
	}

	/**
	 */
	public void testInterpolatePointDoubleDoubleCoordinateSystem3D_XYZ_RIGHT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XYZ_RIGHT_HAND;
		
		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(new Point3f(p1_2.getX(), p1_2.getY(), h1), this.triangle.interpolatePoint(p1_2.getX(), p1_2.getY(), cs));
		assertEpsilonEquals(new Point3f(p2_2.getX(), p2_2.getY(), h2), this.triangle.interpolatePoint(p2_2.getX(), p2_2.getY(), cs));
		assertEpsilonEquals(new Point3f(p3_2.getX(), p3_2.getY(), h3), this.triangle.interpolatePoint(p3_2.getX(), p3_2.getY(), cs));
		
		Point2f p; 
		
		p = new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h2)/2.), this.triangle.interpolatePoint(p.getX(), p.getY(), cs));		

		p = new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h3)/2.), this.triangle.interpolatePoint(p.getX(), p.getY(), cs));
		
		p = new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h2+h3)/2.), this.triangle.interpolatePoint(p.getX(), p.getY(), cs));
	}

	/**
	 */
	public void testInterpolatePointDoubleDoubleCoordinateSystem3D_XYZ_LEFT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XYZ_LEFT_HAND;
		
		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(new Point3f(p1_2.getX(), p1_2.getY(), h1), this.triangle.interpolatePoint(p1_2.getX(), p1_2.getY(), cs));
		assertEpsilonEquals(new Point3f(p2_2.getX(), p2_2.getY(), h2), this.triangle.interpolatePoint(p2_2.getX(), p2_2.getY(), cs));
		assertEpsilonEquals(new Point3f(p3_2.getX(), p3_2.getY(), h3), this.triangle.interpolatePoint(p3_2.getX(), p3_2.getY(), cs));
		
		Point2f p; 
		
		p = new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h2)/2.), this.triangle.interpolatePoint(p.getX(), p.getY(), cs));		

		p = new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h3)/2.), this.triangle.interpolatePoint(p.getX(), p.getY(), cs));
		
		p = new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h2+h3)/2.), this.triangle.interpolatePoint(p.getX(), p.getY(), cs));
	}

	/**
	 */
	public void testInterpolatePointDoubleDoubleCoordinateSystem3D_XZY_RIGHT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XZY_RIGHT_HAND;

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(new Point3f(p1_2.getX(), h1, p1_2.getY()), this.triangle.interpolatePoint(p1_2.getX(), p1_2.getY(), cs));
		assertEpsilonEquals(new Point3f(p2_2.getX(), h2, p2_2.getY()), this.triangle.interpolatePoint(p2_2.getX(), p2_2.getY(), cs));
		assertEpsilonEquals(new Point3f(p3_2.getX(), h3, p3_2.getY()), this.triangle.interpolatePoint(p3_2.getX(), p3_2.getY(), cs));
		
		Point2f p; 
		
		p = new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),(h1+h2)/2.,p.getY()), this.triangle.interpolatePoint(p.getX(),p.getY(), cs));		

		p = new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),(h1+h3)/2.,p.getY()), this.triangle.interpolatePoint(p.getX(),p.getY(), cs));
		
		p = new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(), (h2+h3)/2., p.getY()), this.triangle.interpolatePoint(p.getX(),p.getY(), cs));
	}

	/**
	 */
	public void testInterpolatePointDoubleDoubleCoordinateSystem3D_XZY_LEFT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XZY_LEFT_HAND;

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(new Point3f(p1_2.getX(), h1, p1_2.getY()), this.triangle.interpolatePoint(p1_2.getX(), p1_2.getY(), cs));
		assertEpsilonEquals(new Point3f(p2_2.getX(), h2, p2_2.getY()), this.triangle.interpolatePoint(p2_2.getX(), p2_2.getY(), cs));
		assertEpsilonEquals(new Point3f(p3_2.getX(), h3, p3_2.getY()), this.triangle.interpolatePoint(p3_2.getX(), p3_2.getY(), cs));
		
		Point2f p; 
		
		p = new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),(h1+h2)/2.,p.getY()), this.triangle.interpolatePoint(p.getX(),p.getY(), cs));		

		p = new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),(h1+h3)/2.,p.getY()), this.triangle.interpolatePoint(p.getX(),p.getY(), cs));
		
		p = new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(), (h2+h3)/2., p.getY()), this.triangle.interpolatePoint(p.getX(),p.getY(), cs));
	}

	/**
	 */
	public void testInterpolatePointPoint2d() {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		
		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(new Point3f(p1_2.getX(), p1_2.getY(), h1), this.triangle.interpolatePoint(p1_2));
		assertEpsilonEquals(new Point3f(p2_2.getX(), p2_2.getY(), h2), this.triangle.interpolatePoint(p2_2));
		assertEpsilonEquals(new Point3f(p3_2.getX(), p3_2.getY(), h3), this.triangle.interpolatePoint(p3_2));
		
		Point2f p; 
		
		p = new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h2)/2.), this.triangle.interpolatePoint(p));		

		p = new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h3)/2.), this.triangle.interpolatePoint(p));
		
		p = new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h2+h3)/2.), this.triangle.interpolatePoint(p));
	}

	/**
	 */
	public void testInterpolatePointPoint2dCoordinateSystem3D_XYZ_RIGHT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XYZ_RIGHT_HAND;
		
		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(new Point3f(p1_2.getX(), p1_2.getY(), h1), this.triangle.interpolatePoint(p1_2, cs));
		assertEpsilonEquals(new Point3f(p2_2.getX(), p2_2.getY(), h2), this.triangle.interpolatePoint(p2_2, cs));
		assertEpsilonEquals(new Point3f(p3_2.getX(), p3_2.getY(), h3), this.triangle.interpolatePoint(p3_2, cs));
		
		Point2f p; 
		
		p = new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h2)/2.), this.triangle.interpolatePoint(p, cs));		

		p = new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h3)/2.), this.triangle.interpolatePoint(p, cs));
		
		p = new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h2+h3)/2.), this.triangle.interpolatePoint(p, cs));
	}

	/**
	 */
	public void testInterpolatePointPoint2dCoordinateSystem3D_XYZ_LEFT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XYZ_LEFT_HAND;
		
		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(new Point3f(p1_2.getX(), p1_2.getY(), h1), this.triangle.interpolatePoint(p1_2, cs));
		assertEpsilonEquals(new Point3f(p2_2.getX(), p2_2.getY(), h2), this.triangle.interpolatePoint(p2_2, cs));
		assertEpsilonEquals(new Point3f(p3_2.getX(), p3_2.getY(), h3), this.triangle.interpolatePoint(p3_2, cs));
		
		Point2f p; 
		
		p = new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h2)/2.), this.triangle.interpolatePoint(p, cs));		

		p = new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h1+h3)/2.), this.triangle.interpolatePoint(p, cs));
		
		p = new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),p.getY(),(h2+h3)/2.), this.triangle.interpolatePoint(p, cs));
	}

	/**
	 */
	public void testInterpolatePointPoint2dCoordinateSystem3D_XZY_RIGHT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XZY_RIGHT_HAND;
		
		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(new Point3f(p1_2.getX(), h1, p1_2.getY()), this.triangle.interpolatePoint(p1_2, cs));
		assertEpsilonEquals(new Point3f(p2_2.getX(), h2, p2_2.getY()), this.triangle.interpolatePoint(p2_2, cs));
		assertEpsilonEquals(new Point3f(p3_2.getX(), h3, p3_2.getY()), this.triangle.interpolatePoint(p3_2, cs));
		
		Point2f p; 
		
		p = new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),(h1+h2)/2.,p.getY()), this.triangle.interpolatePoint(p, cs));		

		p = new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),(h1+h3)/2.,p.getY()), this.triangle.interpolatePoint(p, cs));
		
		p = new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(), (h2+h3)/2., p.getY()), this.triangle.interpolatePoint(p, cs));
	}

	/**
	 */
	public void testInterpolatePointPoint2dCoordinateSystem3D_XZY_LEFT_HAND() {
		CoordinateSystem3D cs = CoordinateSystem3D.XZY_LEFT_HAND;

		float h1 = cs.height(this.p1);
		float h2 = cs.height(this.p2);
		float h3 = cs.height(this.p3);
		Point2f p1_2 = cs.toCoordinateSystem2D(this.p1);
		Point2f p2_2 = cs.toCoordinateSystem2D(this.p2);
		Point2f p3_2 = cs.toCoordinateSystem2D(this.p3);
		
		assertEpsilonEquals(new Point3f(p1_2.getX(), h1, p1_2.getY()), this.triangle.interpolatePoint(p1_2, cs));
		assertEpsilonEquals(new Point3f(p2_2.getX(), h2, p2_2.getY()), this.triangle.interpolatePoint(p2_2, cs));
		assertEpsilonEquals(new Point3f(p3_2.getX(), h3, p3_2.getY()), this.triangle.interpolatePoint(p3_2, cs));
		
		Point2f p; 
		
		p = new Point2f((p1_2.getX()+p2_2.getX())/2., (p1_2.getY()+p2_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),(h1+h2)/2.,p.getY()), this.triangle.interpolatePoint(p, cs));		

		p = new Point2f((p1_2.getX()+p3_2.getX())/2., (p1_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(),(h1+h3)/2.,p.getY()), this.triangle.interpolatePoint(p, cs));
		
		p = new Point2f((p2_2.getX()+p3_2.getX())/2., (p2_2.getY()+p3_2.getY())/2.);
		assertEpsilonEquals(new Point3f(p.getX(), (h2+h3)/2., p.getY()), this.triangle.interpolatePoint(p, cs));
	}

}

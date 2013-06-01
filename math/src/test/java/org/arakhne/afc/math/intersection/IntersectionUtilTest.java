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
package org.arakhne.afc.math.intersection;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Point3f;

import junit.framework.TestCase;

/**
 * @author $Author: cbohrhauer$
 * @author $Author: jdemange$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class IntersectionUtilTest extends TestCase {
	
	/**
	 * @author $Author: jdemange$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private enum Axis {
		/** 
		 */
		X,
		/** 
		 */
		Y,
		/** 
		 */
		Z;
	}

	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#intersectsSegments(float, float, float, float, float, float, float, float)}.
	 */
	public static void testIntersectsSegments() {
		Point2f p1 = new Point2f(100., 100.);
		Point2f p2 = new Point2f(-100., -100.);
		Point2f p3, p4;

		p3 = new Point2f(p1);
		p4 = new Point2f(p2);
		assertTrue(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertTrue(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(50, -100);
		p4 = new Point2f(-50, 100);
		assertTrue(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertTrue(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(-80, 80);
		p4 = new Point2f(-100, 100);
		assertFalse(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertFalse(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(80, -80);
		p4 = new Point2f(100, -100);
		assertFalse(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertFalse(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(100, 120);
		p4 = new Point2f(-100, -80);
		assertFalse(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertFalse(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(100, 80);
		p4 = new Point2f(-100, -120);
		assertFalse(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertFalse(IntersectionUtil.intersectsSegments(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));
	}
	
	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#intersectsLineSegment(float, float, float, float, float, float, float, float)}.
	 */
	public static void testIntersectsLineSegment() {
		Point2f p1 = new Point2f(100., 100.);
		Point2f p2 = new Point2f(-100., -100.);
		Point2f p3, p4;

		p3 = new Point2f(p1);
		p4 = new Point2f(p2);
		assertTrue(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertTrue(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(50, -100);
		p4 = new Point2f(-50, 100);
		assertTrue(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertTrue(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(-80, 80);
		p4 = new Point2f(-100, 100);
		assertFalse(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertFalse(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(80, -80);
		p4 = new Point2f(100, -100);
		assertFalse(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertFalse(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(100, 120);
		p4 = new Point2f(-100, -80);
		assertFalse(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertFalse(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(100, 80);
		p4 = new Point2f(-100, -120);
		assertFalse(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertFalse(IntersectionUtil.intersectsLineSegment(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));
	}
	
	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#intersectsLines(float, float, float, float, float, float, float, float)}.
	 */
	public static void testIntersectsLines() {
		Point2f p1 = new Point2f(100., 100.);
		Point2f p2 = new Point2f(-100., -100.);
		Point2f p3, p4;

		p3 = new Point2f(p1);
		p4 = new Point2f(p2);
		assertTrue(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertTrue(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(50, -100);
		p4 = new Point2f(-50, 100);
		assertTrue(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertTrue(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(-80, 80);
		p4 = new Point2f(-100, 100);
		assertTrue(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertTrue(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(80, -80);
		p4 = new Point2f(100, -100);
		assertTrue(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertTrue(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(100, 120);
		p4 = new Point2f(-100, -80);
		assertFalse(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertFalse(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));

		p3 = new Point2f(100, 80);
		p4 = new Point2f(-100, -120);
		assertFalse(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY()));
		assertFalse(IntersectionUtil.intersectsLines(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY()));
	}
	
	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#intersectsPointTriangle(float, float, float, float, float, float, float, float)}.
	 */
	public static void testIntersectsPointTriangleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		Point2f p1 = new Point2f(100., 100.);
		Point2f p2 = new Point2f(-100., -100.);
		Point2f p3 = new Point2f(100., -100.);
		Point2f p;
		
		p = new Point2f(p1);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p = new Point2f(p2);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p = new Point2f(p3);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p = new Point2f(-50, 50);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p = new Point2f(0, -150);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p = new Point2f(150, 0);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p = new Point2f(0, 0);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p = new Point2f(10, 10);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p = new Point2f(100, 0);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p1 = new Point2f(16.666664, -50.0);
		p2 = new Point2f(16.666664, 50.0);
		p3 = new Point2f(-16.666668, -50.0);
		p = new Point2f(-11.861863387387388, -35.585585585585584);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p1 = new Point2f(-16.666668, 50.0);
		p2 = new Point2f(-16.666668, -50.0);
		p3 = new Point2f(16.666664, 50.0);
		p = new Point2f(-11.861863387387388, -35.585585585585584);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		// Test from Olivier
		p1 = new Point2f(-3., -3.);
		p2 = new Point2f(6., -3.);
		p3 = new Point2f(5., 5.);
		p = new Point2f(-.5, 0.);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p3.getX(), p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));
		assertFalse(IntersectionUtil.intersectsPointTriangle(-0.5f, 0f, -3f, -3f, 6f, -3f, 5f, 5f));
	}
	
	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#intersectsPointTriangle(float, float, float, float, float, float, float, float, float, float, float, float, boolean)}.
	 */
	public static void testIntersectsPointTriangleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble_false() {
		Point3f p1 = new Point3f(100., 100., 50.);
		Point3f p2 = new Point3f(-100., -100., 50.);
		Point3f p3 = new Point3f(100., -100., 50.);
		Point3f p;
		
		p = new Point3f(p1);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));

		p = new Point3f(p2);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));

		p = new Point3f(p3);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));

		p = new Point3f(-50, 50, 50.);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));

		p = new Point3f(0, -150, 50.);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));

		p = new Point3f(150, 0, 50.);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));

		p = new Point3f(0, 0, 50.);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));

		p = new Point3f(10, 10, 50.);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));

		p = new Point3f(100, 0, 50.);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));

		p = new Point3f(p1.getX(), p1.getY(), 100.);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));

		p = new Point3f(p2.getX(), p2.getY(), -20.);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), false));
	}
	
	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#intersectsPointTriangle(float, float, float, float, float, float, float, float, float, float, float, float, boolean)}.
	 */
	public static void testIntersectsPointTriangleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble_true() {
		Point3f p1 = new Point3f(100., 100., 50.);
		Point3f p2 = new Point3f(-100., -100., 50.);
		Point3f p3 = new Point3f(100., -100., 50.);
		Point3f p;
		
		p = new Point3f(p1);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));

		p = new Point3f(p2);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));

		p = new Point3f(p3);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));

		p = new Point3f(-50, 50, 50.);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));

		p = new Point3f(0, -150, 50.);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));

		p = new Point3f(150, 0, 50.);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));

		p = new Point3f(0, 0, 50.);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));

		p = new Point3f(10, 10, 50.);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));

		p = new Point3f(100, 0, 50.);
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));

		p = new Point3f(p1.getX(), p1.getY(), 100.);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));

		p = new Point3f(p2.getX(), p2.getY(), -20.);
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(), p3.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointTriangle(p.getX(), p.getY(), p.getZ(), p3.getX(), p3.getY(), p3.getZ(), p2.getX(), p2.getY(), p2.getZ(), p1.getX(), p1.getY(), p1.getZ(), true));
	}
	
	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#intersectsCoplanarTriangle(javax.vecmath.Tuple3d, javax.vecmath.Tuple3d, javax.vecmath.Tuple3d, javax.vecmath.Tuple3d, javax.vecmath.Tuple3d, javax.vecmath.Tuple3d)}.
	 */
	public static void testIntersectsCoplanarTriangle() {
		Point3f p1 = new Point3f(-100., -100., 50.);
		Point3f p2 = new Point3f(100., -100., 50.);
		Point3f p3 = new Point3f(100., 100., 50.);
		
		Point3f p4, p5, p6;
		
		assertTrue(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p1,p2,p3));
		assertTrue(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p2,p2,p1));

		p4 = new Point3f(p1);
		p5 = new Point3f(p3);
		p6 = new Point3f(-50., 0., 50.);
		assertTrue(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p4,p5,p6));
		assertTrue(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p6,p5,p4));

		p4 = new Point3f(p2);
		p5 = new Point3f(p3);
		p6 = new Point3f(200., 0., 50.);
		assertTrue(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p4,p5,p6));
		assertTrue(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p6,p5,p4));

		p4 = new Point3f(0., 0., 50.);
		p5 = new Point3f(0., -50, 50.);
		p6 = new Point3f(10., -25., 50.);
		assertTrue(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p4,p5,p6));
		assertTrue(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p6,p5,p4));

		p4 = new Point3f(-1000., 150., 50.);
		p5 = new Point3f(1000., 150., 50.);
		p6 = new Point3f(0., -1000., 50.);
		assertTrue(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p4,p5,p6));
		assertTrue(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p6,p5,p4));

		p4 = new Point3f(-1000., -1000., 50.);
		p5 = new Point3f(-900., -1000., 50.);
		p6 = new Point3f(-900., -900., 50.);
		assertFalse(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p4,p5,p6));
		assertFalse(IntersectionUtil.intersectsCoplanarTriangle(p1,p2,p3, p6,p5,p4));
	}
	
	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#overlapsCoplanarTriangle(javax.vecmath.Tuple3d, javax.vecmath.Tuple3d, javax.vecmath.Tuple3d, javax.vecmath.Tuple3d, javax.vecmath.Tuple3d, javax.vecmath.Tuple3d)}.
	 */
	public static void testOverlapsCoplanarTriangle() {
		Point3f p1 = new Point3f(-100., -100., 50.);
		Point3f p2 = new Point3f(100., -100., 50.);
		Point3f p3 = new Point3f(100., 100., 50.);
		
		Point3f p4, p5, p6;
		
		assertFalse(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p1,p2,p3));
		assertFalse(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p2,p2,p1));

		p4 = new Point3f(p1);
		p5 = new Point3f(p3);
		p6 = new Point3f(-100., 100., 50.);
		assertFalse(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p4,p5,p6));
		assertFalse(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p6,p5,p4));

		p4 = new Point3f(p2);
		p5 = new Point3f(p3);
		p6 = new Point3f(200., 0., 50.);
		assertFalse(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p4,p5,p6));
		assertFalse(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p6,p5,p4));

		p4 = new Point3f(0., 0., 50.);
		p5 = new Point3f(0., -50, 50.);
		p6 = new Point3f(10., -25., 50.);
		assertTrue(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p4,p5,p6));
		assertTrue(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p6,p5,p4));

		p4 = new Point3f(-1000., 150., 50.);
		p5 = new Point3f(1000., 150., 50.);
		p6 = new Point3f(0., -1000., 50.);
		assertTrue(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p4,p5,p6));
		assertTrue(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p6,p5,p4));

		p4 = new Point3f(-1000., -1000., 50.);
		p5 = new Point3f(-900., -1000., 50.);
		p6 = new Point3f(-900., -900., 50.);
		assertFalse(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p4,p5,p6));
		assertFalse(IntersectionUtil.overlapsCoplanarTriangle(p1,p2,p3, p6,p5,p4));
	}
	
	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#intersectsPointSegment(float, float, float, float, float, float)}.
	 */
	public static void testintersectsPointSegmentDoubleDoubleDoublesDoubleDoubleDouble() {
		Point2f s1 = new Point2f(-100, -100);
		Point2f s2 = new Point2f(100, 100);
		Point2f p;

		p = new Point2f(s1);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s1.getX(), s1.getY(), s2.getX(), s2.getY()));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s2.getX(), s2.getY(), s1.getX(), s1.getY()));

		p = new Point2f(s2);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s1.getX(), s1.getY(), s2.getX(), s2.getY()));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s2.getX(), s2.getY(), s1.getX(), s1.getY()));

		p = new Point2f();
		p.add(s1, s2);
		p.scale(.5f);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s1.getX(), s1.getY(), s2.getX(), s2.getY()));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s2.getX(), s2.getY(), s1.getX(), s1.getY()));

		p = new Point2f(0., 0.);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s1.getX(), s1.getY(), s2.getX(), s2.getY()));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s2.getX(), s2.getY(), s1.getX(), s1.getY()));

		p = new Point2f(-10., -10.);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s1.getX(), s1.getY(), s2.getX(), s2.getY()));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s2.getX(), s2.getY(), s1.getX(), s1.getY()));

		p = new Point2f(-200., -200.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s1.getX(), s1.getY(), s2.getX(), s2.getY()));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s2.getX(), s2.getY(), s1.getX(), s1.getY()));

		p = new Point2f(200., 200.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s1.getX(), s1.getY(), s2.getX(), s2.getY()));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), s2.getX(), s2.getY(), s1.getX(), s1.getY()));
	}
	
	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#intersectsPointSegment(float, float, float, float, float, float, float, float, float, boolean)}.
	 */
	public static void testintersectsPointSegmentDoubleDoubleDoublesDoubleDoubleDoubleDoubleDoubleDoubleBoolean_False() {
		Point3f s1 = new Point3f(-100, -100, 50.);
		Point3f s2 = new Point3f(100, 100, 50.);
		Point3f p;

		p = new Point3f(s1);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f(s2);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f();
		p.add(s1, s2);
		p.scale(.5f);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f(0., 0., 50.);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f(-10., -10., 50.);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f(-200., -200., 50.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f(200., 200., 50.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));
		
		p = new Point3f(s1.getX(), s1.getY(), 0.);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f(s2.getX(), s2.getY(), 0.);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f();
		p.add(s1, s2);
		p.scale(.5f);
		p.setZ(0.f);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f(0., 0., 0.);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f(-10., -10., 0.);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f(-200., -200., 0.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));

		p = new Point3f(200., 200., 0.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), false));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), false));
	}
	
	/**
	 * Test method for {@link fr.utbm.set.geom.intersection.IntersectionUtil#intersectsPointSegment(float, float, float, float, float, float, float, float, float, boolean)}.
	 */
	public static void testintersectsPointSegmentDoubleDoubleDoublesDoubleDoubleDoubleDoubleDoubleDoubleBoolean_True() {
		Point3f s1 = new Point3f(-100, -100, 50.);
		Point3f s2 = new Point3f(100, 100, 50.);
		Point3f p;

		p = new Point3f(s1);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f(s2);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f();
		p.add(s1, s2);
		p.scale(.5f);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f(0., 0., 50.);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f(-10., -10., 50.);
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertTrue(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f(-200., -200., 50.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f(200., 200., 50.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));
		
		p = new Point3f(s1.getX(), s1.getY(), 0.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f(s2.getX(), s2.getY(), 0.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f();
		p.add(s1, s2);
		p.scale(.5f);
		p.setZ(0.f);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f(0., 0., 0.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f(-10., -10., 0.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f(-200., -200., 0.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));

		p = new Point3f(200., 200., 0.);
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ(), true));
		assertFalse(IntersectionUtil.intersectsPointSegment(p.getX(), p.getY(), p.getZ(), s2.getX(), s2.getY(), s2.getZ(), s1.getX(), s1.getY(), s1.getZ(), true));
	}
	
	/**
	 */
	public static void testIntersectsSphereCapsule() {
		Point3f center = new Point3f(0,-4,2);
		float capsuleRadius = 1.f;
		//Config 1
		testOneConfigIntersectsSphereCapsule(new Point3f(0,-4,0), new Point3f(0,-4,4), capsuleRadius, center, Axis.Z);
		
		//Config 2
		testOneConfigIntersectsSphereCapsule(new Point3f(0,-6,2), new Point3f(0,-2,2), capsuleRadius, center, Axis.Y);
		
		//Config 3
		testOneConfigIntersectsSphereCapsule(new Point3f(-2,-4,2), new Point3f(2,-4,2), capsuleRadius, center, Axis.X);
		
		/*
		assertTrue(IntersectionUtil.intersectsSphereCapsule(new Point3f(-1,-1,4),1.0,new Point3f(-4,4,4),new Point3f(-1,2,4),2.0));
		assertFalse(IntersectionUtil.intersectsSphereCapsule(new Point3f(1,0,4),0.5,new Point3f(-4,4,4),new Point3f(-1,2,4),2.0));
		assertFalse(IntersectionUtil.intersectsSphereCapsule(new Point3f(-1,-1,4),1.0,new Point3f(-4,4,4),new Point3f(-1,2,4),1.0));
		assertTrue(IntersectionUtil.intersectsSphereCapsule(new Point3f(-3.5,3,4),1.0,new Point3f(-4,4,4),new Point3f(-1,2,4),2.0));
		assertFalse(IntersectionUtil.intersectsSphereCapsule(new Point3f(-3.5,-4,4),4.0,new Point3f(2,2,2.5),new Point3f(4,-1,2.5),2.0));
		assertTrue(IntersectionUtil.intersectsSphereCapsule(new Point3f(1,-1,4),4.0,new Point3f(2,2,2.5),new Point3f(4,-1,2.5),2.0));
		 */
	}
	
	/**
	 */
	private static void testOneConfigIntersectsSphereCapsule(Point3f capsuleA, Point3f capsuleB, float capsuleRadius, Point3f center, Axis axis) {
		float sphereRadius = 0.5f;
		List<Point3f> centers = defineCenterSpheres(sphereRadius, sphereRadius*2, center, 2, axis);
		for (int i = 0; i <= 8; i++) {
			assertTrue(IntersectionUtil.intersectsSphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		for (int i = 9; i <= 18; i++) {
			if (i==13 || i==14)
				assertTrue(IntersectionUtil.intersectsSphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
			else
				assertFalse(IntersectionUtil.intersectsSphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		
		sphereRadius = 1.f;
		centers = defineCenterSpheres(sphereRadius*2, sphereRadius*2, center, 1, axis);
		for (int i = 0; i <= 2; i++) {
			assertTrue(IntersectionUtil.intersectsSphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		for (int i = 3; i <= 12; i++) {
			assertFalse(IntersectionUtil.intersectsSphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		
		sphereRadius = 2.f;
		centers = defineCenterSpheres(sphereRadius, sphereRadius, center, 1, axis);
		for (int i = 0; i <= 6; i++) {
			assertTrue(IntersectionUtil.intersectsSphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		for (int i = 7; i <= 12; i++) {
			if (i==9 || i==10)
				assertTrue(IntersectionUtil.intersectsSphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
			else
				assertFalse(IntersectionUtil.intersectsSphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		
		assertTrue(IntersectionUtil.intersectsSphereCapsule(center,3.0f,capsuleA,capsuleB,capsuleRadius));
	}
	
	/**
	 */
	public static void testClassifySphereCapsule() {
		Point3f center = new Point3f(0,-4,2);
		float capsuleRadius = 1.f;
		//Config 1
		testOneConfigClassifySphereCapsule(new Point3f(0,-4,0), new Point3f(0,-4,4), capsuleRadius, center, Axis.Z);
		
		//Config 2
		testOneConfigClassifySphereCapsule(new Point3f(0,-6,2), new Point3f(0,-2,2), capsuleRadius, center, Axis.Y);
		
		//Config 3
		testOneConfigClassifySphereCapsule(new Point3f(-2,-4,2), new Point3f(2,-4,2), capsuleRadius, center, Axis.X);
		/*
		assertEquals(IntersectionType.SPANNING,IntersectionUtil.classifySphereCapsule(new Point3f(-1,-1,4),1.0,new Point3f(-4,4,4),new Point3f(-1,2,4),2.0));
		assertEquals(IntersectionType.OUTSIDE,IntersectionUtil.classifySphereCapsule(new Point3f(1,0,4),0.5,new Point3f(-4,4,4),new Point3f(-1,2,4),2.0));
		
		assertEquals(IntersectionType.OUTSIDE,IntersectionUtil.classifySphereCapsule(new Point3f(-1,-1,4),1.0,new Point3f(-4,4,4),new Point3f(-1,2,4),1.0));
		
		assertEquals(IntersectionType.ENCLOSING,IntersectionUtil.classifySphereCapsule(new Point3f(-3.5,3,4),0.5,new Point3f(-4,4,4),new Point3f(-1,2,4),2.0));
		
		assertEquals(IntersectionType.OUTSIDE,IntersectionUtil.classifySphereCapsule(new Point3f(-3.5,-4,4),4.0,new Point3f(2,2,2.5),new Point3f(4,-1,2.5),2.0));
		
		assertEquals(IntersectionType.INSIDE,IntersectionUtil.classifySphereCapsule(new Point3f(1,-1,4),4.0,new Point3f(2,2,2.5),new Point3f(4,-1,2.5),2.0));

		assertEquals(IntersectionType.INSIDE,IntersectionUtil.classifySphereCapsule(new Point3f(2,0,4),4.0,new Point3f(2,2,2.5),new Point3f(4,-1,2.5),2.0));
		assertEquals(IntersectionType.INSIDE,IntersectionUtil.classifySphereCapsule(new Point3f(2,0,4),5.0,new Point3f(2,2,2.5),new Point3f(4,-1,2.5),1.0));
		*/
	}
	
	/**
	 * 
	 * @param capsuleA
	 * @param capsuleB
	 * @param capsuleRadius
	 * @param center
	 * @param axis
	 */
	private static void testOneConfigClassifySphereCapsule(Point3f capsuleA, Point3f capsuleB, float capsuleRadius, Point3f center, Axis axis) {
		float sphereRadius = 0.5f;
		List<Point3f> centers = defineCenterSpheres(sphereRadius, sphereRadius*2, center, 2, axis);
		for (int i = 0; i <= 8; i++) {
			assertEquals(IntersectionType.ENCLOSING,IntersectionUtil.classifySphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		for (int i = 9; i <= 18; i++) {
			if (i==13 || i==14)
				assertEquals(IntersectionType.ENCLOSING,IntersectionUtil.classifySphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
			else
				assertEquals(IntersectionType.OUTSIDE,IntersectionUtil.classifySphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		
		sphereRadius = 1.f;
		centers = defineCenterSpheres(sphereRadius*2, sphereRadius*2, center, 1, axis);
		for (int i = 0; i <= 2; i++) {
			assertEquals(IntersectionType.ENCLOSING,IntersectionUtil.classifySphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		for (int i = 3; i <= 12; i++) {
			assertEquals(IntersectionType.OUTSIDE,IntersectionUtil.classifySphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		
		sphereRadius = 2.f;
		centers = defineCenterSpheres(sphereRadius, sphereRadius, center, 1, axis);
		for (int i = 0; i <= 6; i++) {
			assertEquals(IntersectionType.SPANNING,IntersectionUtil.classifySphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		for (int i = 7; i <= 12; i++) {
			if (i==9 || i==10)
				assertEquals(IntersectionType.SPANNING,IntersectionUtil.classifySphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
			else
				assertEquals(IntersectionType.OUTSIDE,IntersectionUtil.classifySphereCapsule(centers.get(i),sphereRadius,capsuleA,capsuleB,capsuleRadius));
		}
		
		assertEquals(IntersectionType.INSIDE,IntersectionUtil.classifySphereCapsule(center,3.0f,capsuleA,capsuleB,capsuleRadius));
	}
	
	/**
	 * 
	 * @param decal
	 * @param space
	 * @param center
	 * @param n the number of pairs of 3D point on x, y and z axis to add in the list
	 * @param axis
	 * 
	 * @return list of center point of sphere
	 */
	private static List<Point3f> defineCenterSpheres(float decal, float space, Point3f center, int n, Axis axis) {
		List<Point3f> centers = new ArrayList<>();
		centers.add(center);
		if (axis.equals(Axis.X)) {
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX()+decal+space*i,center.getY(),center.getZ()));
				centers.add(new Point3f(center.getX()-decal-space*i,center.getY(),center.getZ()));
			}
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX(),center.getY()+decal+space*i,center.getZ()));
				centers.add(new Point3f(center.getX(),center.getY()-decal-space*i,center.getZ()));
			}
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX(),center.getY(),center.getZ()+decal+space*i));
				centers.add(new Point3f(center.getX(),center.getY(),center.getZ()-decal-space*i));
			}
		} else if (axis.equals(Axis.Y)) {
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX(),center.getY()+decal+space*i,center.getZ()));
				centers.add(new Point3f(center.getX(),center.getY()-decal-space*i,center.getZ()));
			}
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX(),center.getY(),center.getZ()+decal+space*i));
				centers.add(new Point3f(center.getX(),center.getY(),center.getZ()-decal-space*i));
			}
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX()+decal+space*i,center.getY(),center.getZ()));
				centers.add(new Point3f(center.getX()-decal-space*i,center.getY(),center.getZ()));
			}
		} else if (axis.equals(Axis.Z))  {
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX(),center.getY(),center.getZ()+decal+space*i));
				centers.add(new Point3f(center.getX(),center.getY(),center.getZ()-decal-space*i));
			}
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX()+decal+space*i,center.getY(),center.getZ()));
				centers.add(new Point3f(center.getX()-decal-space*i,center.getY(),center.getZ()));
			}
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX(),center.getY()+decal+space*i,center.getZ()));
				centers.add(new Point3f(center.getX(),center.getY()-decal-space*i,center.getZ()));
			}
		} else  {
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX()+decal+space*i,center.getY(),center.getZ()));
				centers.add(new Point3f(center.getX()-decal-space*i,center.getY(),center.getZ()));
			}
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX(),center.getY()+decal+space*i,center.getZ()));
				centers.add(new Point3f(center.getX(),center.getY()-decal-space*i,center.getZ()));
			}
			for (int i = 0; i <= n; i++) {
				centers.add(new Point3f(center.getX(),center.getY(),center.getZ()+decal+space*i));
				centers.add(new Point3f(center.getX(),center.getY(),center.getZ()-decal-space*i));
			}
		}
		
		return centers;
	}
	
	/**
	 */
	public static void testIntersectsCapsuleCapsule() {
		assertFalse(IntersectionUtil.intersectsCapsuleCapsule(new Point3f(-3,-3,8), new Point3f(-2,-2,9), 1, new Point3f(1,1,8), new Point3f(3,3,8), 1f));		
		assertFalse(IntersectionUtil.intersectsCapsuleCapsule(new Point3f(-3,3,8), new Point3f(-2,-2,9), 1, new Point3f(1,1,8), new Point3f(3,3,8), 1f));
		
		assertTrue(IntersectionUtil.intersectsCapsuleCapsule(new Point3f(-3,3,8), new Point3f(-2,-2,8), 3, new Point3f(1,1,8), new Point3f(3,3,8), 1f));
		assertTrue(IntersectionUtil.intersectsCapsuleCapsule(new Point3f(-3,3,8), new Point3f(-2,-2,8), 4, new Point3f(1,1,8), new Point3f(3,3,8), 1f));
		assertTrue(IntersectionUtil.intersectsCapsuleCapsule(new Point3f(1,2,8), new Point3f(2,4,8), 1, new Point3f(1,1,8), new Point3f(3,3,8), 1f));
	}
	
	/**
	 */
	public static void testIntersectPointCapsule() {
		assertFalse(IntersectionUtil.intersectPointCapsule(new Point3f(-2.5,2.5,8), new Point3f(1,1,8), new Point3f(3,3,8), 1f));
		assertFalse(IntersectionUtil.intersectPointCapsule(new Point3f(-3.5,4,8), new Point3f(1,1,8), new Point3f(3,3,8), 1f));
		assertTrue(IntersectionUtil.intersectPointCapsule(new Point3f(-2.5,2.5,4), new Point3f(-4,4,4),new Point3f(-1,2,4),1.0f));
		assertTrue(IntersectionUtil.intersectPointCapsule(new Point3f(-3.5,4,4), new Point3f(-4,4,4),new Point3f(-1,2,4),1.0f));
		assertTrue(IntersectionUtil.intersectPointCapsule(new Point3f(-5,4,4), new Point3f(-4,4,4),new Point3f(-1,2,4),1.0f));
		assertTrue(IntersectionUtil.intersectPointCapsule(new Point3f(-1,1,4), new Point3f(-4,4,4),new Point3f(-1,2,4),1.0f));
	}
	
	/**
	 */
	public static void testClassifyPointCapsule() {
		assertEquals(IntersectionType.OUTSIDE,IntersectionUtil.classifyPointCapsule(new Point3f(-2.5,2.5,8), new Point3f(1,1,8), new Point3f(3,3,8), 1f));
		assertEquals(IntersectionType.OUTSIDE,IntersectionUtil.classifyPointCapsule(new Point3f(-3.5,4,8), new Point3f(1,1,8), new Point3f(3,3,8), 1f));
		assertEquals(IntersectionType.INSIDE,IntersectionUtil.classifyPointCapsule(new Point3f(-2.5,2.5,4), new Point3f(-4,4,4),new Point3f(-1,2,4),1.0f));
		assertEquals(IntersectionType.INSIDE,IntersectionUtil.classifyPointCapsule(new Point3f(-3.5,4,4), new Point3f(-4,4,4),new Point3f(-1,2,4),1.0f));
		assertEquals(IntersectionType.SPANNING,IntersectionUtil.classifyPointCapsule(new Point3f(-5,4,4), new Point3f(-4,4,4),new Point3f(-1,2,4),1.0f));
		assertEquals(IntersectionType.SPANNING,IntersectionUtil.classifyPointCapsule(new Point3f(-1,1,4), new Point3f(-4,4,4),new Point3f(-1,2,4),1.0f));
	}
	
	/**
	 */
	@SuppressWarnings({ "null", "boxing" })
	public static void testClassifiesAlignedRectanglesPointPointPointPoint() {
		Point2f l1 = new Point2f(0,-4);
		Point2f u1 = new Point2f(4,8);
		List<Float> pointsX = new ArrayList<Float>();
		pointsX.add(-1.f);
		pointsX.add(0.f);
		pointsX.add(2.f); //Middle
		pointsX.add(4.f);
		pointsX.add(5.f);
		//Border
		pointsX.add(-3.f);
		pointsX.add(-2.f);
		pointsX.add(6.f);
		pointsX.add(7.f);
		//Middle
		pointsX.add(1.f);
		pointsX.add(3.f);
		
		List<Float> pointsY = new ArrayList<Float>();
		pointsY.add(-5.f);
		pointsY.add(-4.f);
		pointsY.add(2.f); //Middle
		pointsY.add(8.f);
		pointsY.add(9.f);
		//Border
		pointsY.add(-7.f);
		pointsY.add(-6.f);
		pointsY.add(10.f);
		pointsY.add(11.f);
		//Middle
		pointsY.add(0.f);
		pointsY.add(4.f);

		IntersectionType interX = null;
		IntersectionType interY = null;
		IntersectionType interFinal;
		boolean toTest;
		
		for (int ix1 = 0 ; ix1 <= 9; ix1++) {
			for (int ix2 = 0 ; ix2 <= 10; ix2++) {
				toTest = true;
				if (pointsX.get(ix1) < pointsX.get(ix2)) {
					if ((ix1==5 && ix2==6) || (ix1==5 && ix2==1) || (ix1==3 && ix2==8) || (ix1==7 && ix2==8)) {
						interX = IntersectionType.OUTSIDE;
					} else if ((ix1==0 && ix2==3) || (ix1==0 && ix2==4) || (ix1==1 && ix2==4)) {
						interX = IntersectionType.INSIDE;
					} else if ((ix1==0 && ix2==2) || (ix1==2 && ix2==4)) {
						interX = IntersectionType.SPANNING;
					} else if ((ix1==9 && ix2==10) || (ix1==1 && ix2==2) || (ix1==2 && ix2==3)) {
						interX = IntersectionType.ENCLOSING;
					} else if (ix1==1 && ix2==3) {
						interX = IntersectionType.SAME;
					} else  {
						toTest = false;
					}
				} else {
					toTest = false;
				}
				if (toTest) {
					for (int iy1 = 0 ; iy1 <= 9; iy1++) {
						for (int iy2 = 0 ; iy2 <= 10; iy2++) {
							toTest = true;
							if (pointsY.get(iy1) < pointsY.get(iy2)) {
								if ((iy1==5 && iy2==6) || (iy1==5 && iy2==1) || (iy1==3 && iy2==8) || (iy1==7 && iy2==8)) {
									interY = IntersectionType.OUTSIDE;
								} else if ((iy1==0 && iy2==3) || (iy1==0 && iy2==4) || (iy1==1 && iy2==4)) {
									interY = IntersectionType.INSIDE;
								} else if ((iy1==0 && iy2==2) || (iy1==2 && iy2==4)) {
									interY = IntersectionType.SPANNING;
								} else if ((iy1==9 && iy2==10) || (iy1==1 && iy2==2) || (iy1==2 && iy2==3)) {
									interY = IntersectionType.ENCLOSING;
								} else if (iy1==1 && iy2==3) {
									interY = IntersectionType.SAME;
								} else  {
									toTest = false;
								}
							} else {
								toTest = false;
							}
							if (toTest) {
								interFinal = interX.and(interY);
								assertEquals(
										interFinal,
										IntersectionUtil.classifiesAlignedRectangles(
												l1, u1,
												new Point2f(pointsX.get(ix1),pointsY.get(iy1)),
												new Point2f(pointsX.get(ix2),pointsY.get(iy2))));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 */
	@SuppressWarnings({ "null", "boxing" })
	public static void testClassifiesAlignedRectanglesDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		Point2f l1 = new Point2f(0,-4);
		Point2f u1 = new Point2f(4,8);
		List<Float> pointsX = new ArrayList<Float>();
		pointsX.add(-1.f);
		pointsX.add(0.f);
		pointsX.add(2.f); //Middle
		pointsX.add(4.f);
		pointsX.add(5.f);
		//Border
		pointsX.add(-3.f);
		pointsX.add(-2.f);
		pointsX.add(6.f);
		pointsX.add(7.f);
		//Middle
		pointsX.add(1.f);
		pointsX.add(3.f);
		
		List<Float> pointsY = new ArrayList<Float>();
		pointsY.add(-5.f);
		pointsY.add(-4.f);
		pointsY.add(2.f); //Middle
		pointsY.add(8.f);
		pointsY.add(9.f);
		//Border
		pointsY.add(-7.f);
		pointsY.add(-6.f);
		pointsY.add(10.f);
		pointsY.add(11.f);
		//Middle
		pointsY.add(0.f);
		pointsY.add(4.f);

		IntersectionType interX = null;
		IntersectionType interY = null;
		IntersectionType interFinal;
		boolean toTest;
		
		for (int ix1 = 0 ; ix1 <= 9; ix1++) {
			for (int ix2 = 0 ; ix2 <= 10; ix2++) {
				toTest = true;
				if (pointsX.get(ix1) < pointsX.get(ix2)) {
					if ((ix1==5 && ix2==6) || (ix1==5 && ix2==1) || (ix1==3 && ix2==8) || (ix1==7 && ix2==8)) {
						interX = IntersectionType.OUTSIDE;
					} else if ((ix1==0 && ix2==3) || (ix1==0 && ix2==4) || (ix1==1 && ix2==4)) {
						interX = IntersectionType.INSIDE;
					} else if ((ix1==0 && ix2==2) || (ix1==2 && ix2==4)) {
						interX = IntersectionType.SPANNING;
					} else if ((ix1==9 && ix2==10) || (ix1==1 && ix2==2) || (ix1==2 && ix2==3)) {
						interX = IntersectionType.ENCLOSING;
					} else if (ix1==1 && ix2==3) {
						interX = IntersectionType.SAME;
					} else  {
						toTest = false;
					}
				} else {
					toTest = false;
				}
				if (toTest) {
					for (int iy1 = 0 ; iy1 <= 9; iy1++) {
						for (int iy2 = 0 ; iy2 <= 10; iy2++) {
							toTest = true;
							if (pointsY.get(iy1) < pointsY.get(iy2)) {
								if ((iy1==5 && iy2==6) || (iy1==5 && iy2==1) || (iy1==3 && iy2==8) || (iy1==7 && iy2==8)) {
									interY = IntersectionType.OUTSIDE;
								} else if ((iy1==0 && iy2==3) || (iy1==0 && iy2==4) || (iy1==1 && iy2==4)) {
									interY = IntersectionType.INSIDE;
								} else if ((iy1==0 && iy2==2) || (iy1==2 && iy2==4)) {
									interY = IntersectionType.SPANNING;
								} else if ((iy1==9 && iy2==10) || (iy1==1 && iy2==2) || (iy1==2 && iy2==3)) {
									interY = IntersectionType.ENCLOSING;
								} else if (iy1==1 && iy2==3) {
									interY = IntersectionType.SAME;
								} else  {
									toTest = false;
								}
							} else {
								toTest = false;
							}
							if (toTest) {
								interFinal = interX.and(interY);
								assertEquals(
										interFinal,
										IntersectionUtil.classifiesAlignedRectangles(
												l1.getX(), l1.getY(), u1.getX(), u1.getY(),
												pointsX.get(ix1),pointsY.get(iy1),
												pointsX.get(ix2),pointsY.get(iy2)));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 */
	@SuppressWarnings({ "null", "boxing" })
	public static void testClassifiesAlignedBoxes() {
		Point3f l1 = new Point3f(0,-4,2);
		Point3f u1 = new Point3f(4,8,12);
		List<Float> pointsX = new ArrayList<Float>();
		pointsX.add(-1.f);
		pointsX.add(0.f);
		pointsX.add(2.f); //Middle
		pointsX.add(4.f);
		pointsX.add(5.f);
		//Border
		pointsX.add(-3.f);
		pointsX.add(-2.f);
		pointsX.add(6.f);
		pointsX.add(7.f);
		//Middle
		pointsX.add(1.f);
		pointsX.add(3.f);
		
		List<Float> pointsY = new ArrayList<Float>();
		pointsY.add(-5.f);
		pointsY.add(-4.f);
		pointsY.add(2.f); //Middle
		pointsY.add(8.f);
		pointsY.add(9.f);
		//Border
		pointsY.add(-7.f);
		pointsY.add(-6.f);
		pointsY.add(10.f);
		pointsY.add(11.f);
		//Middle
		pointsY.add(0.f);
		pointsY.add(4.f);
		
		List<Float> pointsZ = new ArrayList<Float>();
		pointsZ.add(1.f);
		pointsZ.add(2.f);
		pointsZ.add(7.f); //Middle
		pointsZ.add(12.f);
		pointsZ.add(13.f);
		//Border
		pointsZ.add(-1.f);
		pointsZ.add(0.f);
		pointsZ.add(14.f);
		pointsZ.add(15.f);
		//Middle
		pointsZ.add(5.f);
		pointsZ.add(9.f);

		IntersectionType interX = null;
		IntersectionType interY = null;
		IntersectionType interZ = null;
		IntersectionType interFinal;
		boolean toTest;
		
		for (int ix1 = 0 ; ix1 <= 9; ix1++) {
			for (int ix2 = 0 ; ix2 <= 10; ix2++) {
				toTest = true;
				if (pointsX.get(ix1) < pointsX.get(ix2)) {
					if ((ix1==5 && ix2==6) || (ix1==5 && ix2==1) || (ix1==3 && ix2==8) || (ix1==7 && ix2==8)) {
						interX = IntersectionType.OUTSIDE;
					} else if ((ix1==0 && ix2==3) || (ix1==0 && ix2==4) || (ix1==1 && ix2==4)) {
						interX = IntersectionType.INSIDE;
					} else if ((ix1==0 && ix2==2) || (ix1==2 && ix2==4)) {
						interX = IntersectionType.SPANNING;
					} else if ((ix1==9 && ix2==10) || (ix1==1 && ix2==2) || (ix1==2 && ix2==3)) {
						interX = IntersectionType.ENCLOSING;
					} else if (ix1==1 && ix2==3) {
						interX = IntersectionType.SAME;
					} else  {
						toTest = false;
					}
				} else {
					toTest = false;
				}
				if (toTest) {
					for (int iy1 = 0 ; iy1 <= 9; iy1++) {
						for (int iy2 = 0 ; iy2 <= 10; iy2++) {
							toTest = true;
							if (pointsY.get(iy1) < pointsY.get(iy2)) {
								if ((iy1==5 && iy2==6) || (iy1==5 && iy2==1) || (iy1==3 && iy2==8) || (iy1==7 && iy2==8)) {
									interY = IntersectionType.OUTSIDE;
								} else if ((iy1==0 && iy2==3) || (iy1==0 && iy2==4) || (iy1==1 && iy2==4)) {
									interY = IntersectionType.INSIDE;
								} else if ((iy1==0 && iy2==2) || (iy1==2 && iy2==4)) {
									interY = IntersectionType.SPANNING;
								} else if ((iy1==9 && iy2==10) || (iy1==1 && iy2==2) || (iy1==2 && iy2==3)) {
									interY = IntersectionType.ENCLOSING;
								} else if (iy1==1 && iy2==3) {
									interY = IntersectionType.SAME;
								} else  {
									toTest = false;
								}
							} else {
								toTest = false;
							}
							if (toTest) {
								for (int iz1 = 0 ; iz1 <= 9; iz1++) {
									for (int iz2 = 0 ; iz2 <= 10; iz2++) {
										toTest = true;
										if (pointsZ.get(iz1) < pointsZ.get(iz2)) {
											if ((iz1==5 && iz2==6) || (iz1==5 && iz2==1) || (iz1==3 && iz2==8) || (iz1==7 && iz2==8)) {
												interZ = IntersectionType.OUTSIDE;
											} else if ((iz1==0 && iz2==3) || (iz1==0 && iz2==4) || (iz1==1 && iz2==4)) {
												interZ = IntersectionType.INSIDE;
											} else if ((iz1==0 && iz2==2) || (iz1==2 && iz2==4)) {
												interZ = IntersectionType.SPANNING;
											} else if ((iz1==9 && iz2==10) || (iz1==1 && iz2==2) || (iz1==2 && iz2==3)) {
												interZ = IntersectionType.ENCLOSING;
											} else if (iz1==1 && iz2==3) {
												interZ = IntersectionType.SAME;
											} else  {
												toTest = false;
											}
										} else {
											toTest = false;
										}
										if (toTest) {
											interFinal = interX.and(interY.and(interZ));
											assertEquals(
													interFinal,
													IntersectionUtil.classifiesAlignedBoxes(
															l1, u1,
															new Point3f(pointsX.get(ix1),pointsY.get(iy1),pointsZ.get(iz1)),
															new Point3f(pointsX.get(ix2),pointsY.get(iy2),pointsZ.get(iz2))));
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 */
	@SuppressWarnings("boxing")
	public static void testIntersectRectanglesDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		Point2f l1 = new Point2f(0,-4);
		Point2f u1 = new Point2f(4,8);
		List<Float> pointsX = new ArrayList<Float>();
		pointsX.add(-1.f);
		pointsX.add(0.f);
		pointsX.add(2.f); //Middle
		pointsX.add(4.f);
		pointsX.add(5.f);
		//Border
		pointsX.add(-3.f);
		pointsX.add(-2.f);
		pointsX.add(6.f);
		pointsX.add(7.f);
		//Middle
		pointsX.add(1.f);
		pointsX.add(3.f);
		
		List<Float> pointsY = new ArrayList<Float>();
		pointsY.add(-5.f);
		pointsY.add(-4.f);
		pointsY.add(2.f); //Middle
		pointsY.add(8.f);
		pointsY.add(9.f);
		//Border
		pointsY.add(-7.f);
		pointsY.add(-6.f);
		pointsY.add(10.f);
		pointsY.add(11.f);
		//Middle
		pointsY.add(0.f);
		pointsY.add(4.f);

		boolean interX = false;
		boolean interY = false;
		boolean interFinal;
		boolean toTest;
		
		for (int ix1 = 0 ; ix1 <= 9; ix1++) {
			for (int ix2 = 0 ; ix2 <= 10; ix2++) {
				toTest = true;
				if (pointsX.get(ix1) < pointsX.get(ix2)) {
					if ((ix1==5 && ix2==6) || (ix1==5 && ix2==1) || (ix1==3 && ix2==8) || (ix1==7 && ix2==8)) {
						interX = false;
					} else if ((ix1==0 && ix2==3) || (ix1==0 && ix2==4) || (ix1==1 && ix2==4) ||
							(ix1==0 && ix2==2) || (ix1==2 && ix2==4) || (ix1==9 && ix2==10) ||
							(ix1==1 && ix2==2) || (ix1==2 && ix2==3) || (ix1==1 && ix2==3)) {
						interX = true;
					} else {
						toTest = false;
					}
				} else {
					toTest = false;
				}
				if (toTest) {
					for (int iy1 = 0 ; iy1 <= 9; iy1++) {
						for (int iy2 = 0 ; iy2 <= 10; iy2++) {
							toTest = true;
							if (pointsY.get(iy1) < pointsY.get(iy2)) {
								if ((iy1==5 && iy2==6) || (iy1==5 && iy2==1) || (iy1==3 && iy2==8) || (iy1==7 && iy2==8)) {
									interY = false;
								} else if ((iy1==0 && iy2==3) || (iy1==0 && iy2==4) || (iy1==1 && iy2==4) ||
										(iy1==0 && iy2==2) || (iy1==2 && iy2==4) || (iy1==9 && iy2==10) ||
										(iy1==1 && iy2==2) || (iy1==2 && iy2==3) || (iy1==1 && iy2==3)) {
									interY = true;
								} else  {
									toTest = false;
								}
							} else {
								toTest = false;
							}
							if (toTest) {
								interFinal = interX && interY;
								assertEquals(
										interFinal,
										IntersectionUtil.intersectsAlignedRectangles(
												l1.getX(), l1.getY(), u1.getX(), u1.getY(),
												pointsX.get(ix1), pointsY.get(iy1),
												pointsX.get(ix2), pointsY.get(iy2)));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 */
	@SuppressWarnings("boxing")
	public void testIntersectRectanglesPointPointPointPoint() {
		Point2f l1 = new Point2f(0,-4);
		Point2f u1 = new Point2f(4,8);
		List<Float> pointsX = new ArrayList<Float>();
		pointsX.add(-1.f);
		pointsX.add(0.f);
		pointsX.add(2.f); //Middle
		pointsX.add(4.f);
		pointsX.add(5.f);
		//Border
		pointsX.add(-3.f);
		pointsX.add(-2.f);
		pointsX.add(6.f);
		pointsX.add(7.f);
		//Middle
		pointsX.add(1.f);
		pointsX.add(3.f);
		
		List<Float> pointsY = new ArrayList<Float>();
		pointsY.add(-5.f);
		pointsY.add(-4.f);
		pointsY.add(2.f); //Middle
		pointsY.add(8.f);
		pointsY.add(9.f);
		//Border
		pointsY.add(-7.f);
		pointsY.add(-6.f);
		pointsY.add(10.f);
		pointsY.add(11.f);
		//Middle
		pointsY.add(0.f);
		pointsY.add(4.f);

		boolean interX = false;
		boolean interY = false;
		boolean interFinal;
		boolean toTest;
		
		for (int ix1 = 0 ; ix1 <= 9; ix1++) {
			for (int ix2 = 0 ; ix2 <= 10; ix2++) {
				toTest = true;
				if (pointsX.get(ix1) < pointsX.get(ix2)) {
					if ((ix1==5 && ix2==6) || (ix1==5 && ix2==1) || (ix1==3 && ix2==8) || (ix1==7 && ix2==8)) {
						interX = false;
					} else if ((ix1==0 && ix2==3) || (ix1==0 && ix2==4) || (ix1==1 && ix2==4) ||
							(ix1==0 && ix2==2) || (ix1==2 && ix2==4) || (ix1==9 && ix2==10) ||
							(ix1==1 && ix2==2) || (ix1==2 && ix2==3) || (ix1==1 && ix2==3)) {
						interX = true;
					} else {
						toTest = false;
					}
				} else {
					toTest = false;
				}
				if (toTest) {
					for (int iy1 = 0 ; iy1 <= 9; iy1++) {
						for (int iy2 = 0 ; iy2 <= 10; iy2++) {
							toTest = true;
							if (pointsY.get(iy1) < pointsY.get(iy2)) {
								if ((iy1==5 && iy2==6) || (iy1==5 && iy2==1) || (iy1==3 && iy2==8) || (iy1==7 && iy2==8)) {
									interY = false;
								} else if ((iy1==0 && iy2==3) || (iy1==0 && iy2==4) || (iy1==1 && iy2==4) ||
										(iy1==0 && iy2==2) || (iy1==2 && iy2==4) || (iy1==9 && iy2==10) ||
										(iy1==1 && iy2==2) || (iy1==2 && iy2==3) || (iy1==1 && iy2==3)) {
									interY = true;
								} else  {
									toTest = false;
								}
							} else {
								toTest = false;
							}
							if (toTest) {
								interFinal = interX && interY;
								assertEquals(
										interFinal,
										IntersectionUtil.intersectsAlignedRectangles(
												l1, u1,
												new Point2f(pointsX.get(ix1),pointsY.get(iy1)),
												new Point2f(pointsX.get(ix2),pointsY.get(iy2))));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 */
	public void testIntersectOrientedBoudningRectangles() {
		Point2f center1 = new Point2f(939406.8087515527, -2303968.166438711);
		Vector2f[] axis1 = new Vector2f[2];
		axis1[0] = new Vector2f(0.9590890404765877, -0.28310459628500917);
		axis1[1] = new Vector2f(0.28310459628500917, 0.9590890404765877);
		float[] extent1 = new float[2];
		extent1[0] = 0.824999988079071f;
		extent1[1] = 17.30809059389867f;

		Point2f center2 = new Point2f(939400.9988595751, -2303979.353829064);
		Vector2f[] axis2 = new Vector2f[2];
		axis2[0] = new Vector2f(0.9077085892770996, 0.41960114031372403);
		axis2[1] = new Vector2f(-0.41960114031372403, 0.9077085892770996);
		float[] extent2 = new float[2];
		extent2[0] = 0.824999988079071f;
		extent2[1] = 5.838878317037597f;


		assertEquals(true,IntersectionUtil.intersectsOrientedRectangles(center1, axis1, extent1,center2, axis2, extent2));
		assertEquals(true,IntersectionUtil.intersectsOrientedRectangles(center2, axis2, extent2,center1, axis1, extent1));
		
		
		center1 = new Point2f(938664.5767492745, -2302106.9781423765);
		axis1 = new Vector2f[2];
		axis1[0] = new Vector2f(0.817670133531072, 0.5756870267178851);
		axis1[1] = new Vector2f(-0.5756870267178851, 0.817670133531072);
		extent1 = new float[2];
		extent1[0] = 0.8251042517949827f;
		extent1[1] = 19.63264093757607f;

		center2 = new Point2f(938610.1837536765, -2302029.344352074);
		axis2 = new Vector2f[2];
		axis2[0] = new Vector2f(0.8196373270736397, 0.5728827559523669);
		axis2[1] = new Vector2f(-0.5728827559523669, 0.8196373270736397);
		extent2 = new float[2];
		extent2[0] = 0.8378669620724395f;
		extent2[1] = 24.755328231025487f;

		assertEquals(false,IntersectionUtil.intersectsOrientedRectangles(center1, axis1, extent1,center2, axis2, extent2));
		assertEquals(false,IntersectionUtil.intersectsOrientedRectangles(center2, axis2, extent2,center1, axis1, extent1));
	}
	
	/**
	 */
	@SuppressWarnings("boxing")
	public void testIntersectAlignedBoxes() {
		Point3f l1 = new Point3f(0,-4,2);
		Point3f u1 = new Point3f(4,8,12);
		List<Float> pointsX = new ArrayList<Float>();
		pointsX.add(-1.f);
		pointsX.add(0.f);
		pointsX.add(2.f); //Middle
		pointsX.add(4.f);
		pointsX.add(5.f);
		//Border
		pointsX.add(-3.f);
		pointsX.add(-2.f);
		pointsX.add(6.f);
		pointsX.add(7.f);
		//Middle
		pointsX.add(1.f);
		pointsX.add(3.f);
		
		List<Float> pointsY = new ArrayList<Float>();
		pointsY.add(-5.f);
		pointsY.add(-4.f);
		pointsY.add(2.f); //Middle
		pointsY.add(8.f);
		pointsY.add(9.f);
		//Border
		pointsY.add(-7.f);
		pointsY.add(-6.f);
		pointsY.add(10.f);
		pointsY.add(11.f);
		//Middle
		pointsY.add(0.f);
		pointsY.add(4.f);
		
		List<Float> pointsZ = new ArrayList<Float>();
		pointsZ.add(1.f);
		pointsZ.add(2.f);
		pointsZ.add(7.f); //Middle
		pointsZ.add(12.f);
		pointsZ.add(13.f);
		//Border
		pointsZ.add(-1.f);
		pointsZ.add(0.f);
		pointsZ.add(14.f);
		pointsZ.add(15.f);
		//Middle
		pointsZ.add(5.f);
		pointsZ.add(9.f);

		boolean interX = false;
		boolean interY = false;
		boolean interZ = false;
		boolean interFinal;
		boolean toTest;
		
		for (int ix1 = 0 ; ix1 <= 9; ix1++) {
			for (int ix2 = 0 ; ix2 <= 10; ix2++) {
				toTest = true;
				if (pointsX.get(ix1) < pointsX.get(ix2)) {
					if ((ix1==5 && ix2==6) || (ix1==5 && ix2==1) || (ix1==3 && ix2==8) || (ix1==7 && ix2==8)) {
						interX = false;
					} else if ((ix1==0 && ix2==3) || (ix1==0 && ix2==4) || (ix1==1 && ix2==4) ||
							(ix1==0 && ix2==2) || (ix1==2 && ix2==4) || (ix1==9 && ix2==10) ||
							(ix1==1 && ix2==2) || (ix1==2 && ix2==3) || (ix1==1 && ix2==3)) {
						interX = true;
					} else {
						toTest = false;
					}
				} else {
					toTest = false;
				}
				if (toTest) {
					for (int iy1 = 0 ; iy1 <= 9; iy1++) {
						for (int iy2 = 0 ; iy2 <= 10; iy2++) {
							toTest = true;
							if (pointsY.get(iy1) < pointsY.get(iy2)) {
								if ((iy1==5 && iy2==6) || (iy1==5 && iy2==1) || (iy1==3 && iy2==8) || (iy1==7 && iy2==8)) {
									interY = false;
								} else if ((iy1==0 && iy2==3) || (iy1==0 && iy2==4) || (iy1==1 && iy2==4) ||
										(iy1==0 && iy2==2) || (iy1==2 && iy2==4) || (iy1==9 && iy2==10) ||
										(iy1==1 && iy2==2) || (iy1==2 && iy2==3) || (iy1==1 && iy2==3)) {
									interY = true;
								} else  {
									toTest = false;
								}
							} else {
								toTest = false;
							}
							if (toTest) {
								for (int iz1 = 0 ; iz1 <= 9; iz1++) {
									for (int iz2 = 0 ; iz2 <= 10; iz2++) {
										toTest = true;
										if (pointsZ.get(iz1) < pointsZ.get(iz2)) {
											if ((iz1==5 && iz2==6) || (iz1==5 && iz2==1) || (iz1==3 && iz2==8) || (iz1==7 && iz2==8)) {
												interZ = false;
											} else if ((iz1==0 && iz2==3) || (iz1==0 && iz2==4) || (iz1==1 && iz2==4) ||
													(iz1==0 && iz2==2) || (iz1==2 && iz2==4) || (iz1==9 && iz2==10) ||
													(iz1==1 && iz2==2) || (iz1==2 && iz2==3) || (iz1==1 && iz2==3)) {
												interZ = true;
											} else  {
												toTest = false;
											}
										} else {
											toTest = false;
										}
										if (toTest) {
											interFinal = interX && interY && interZ;
											assertEquals(
													interFinal,
													IntersectionUtil.intersectsAlignedBoxes(
															l1, u1,
															new Point3f(pointsX.get(ix1),pointsY.get(iy1),pointsZ.get(iz1)),
															new Point3f(pointsX.get(ix2),pointsY.get(iy2),pointsZ.get(iz2))));
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
}

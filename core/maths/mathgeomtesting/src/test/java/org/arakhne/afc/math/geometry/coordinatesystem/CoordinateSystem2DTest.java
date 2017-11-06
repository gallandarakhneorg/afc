/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.coordinatesystem;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;

import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;

/**
 * Unit test for {@link CoordinateSystem2D}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class CoordinateSystem2DTest extends AbstractMathTestCase {

	@Before
	public void setUp() {
		// Reset the coordinate system to the system default.
		CoordinateSystem2D.setDefaultCoordinateSystem(null);
	}
	
	@Test
	public void getDimensions() {
		assertEquals(2, CoordinateSystem2D.XY_RIGHT_HAND.getDimensions());
		assertEquals(2, CoordinateSystem2D.XY_LEFT_HAND.getDimensions());
	}

	@Test
	public void getViewVector() {
		Vector2D v;
		v = CoordinateSystem2D.getViewVector();
		assertFpVectorEquals(1, 0, v);
		//
		assertInlineParameterUsage(CoordinateSystem2D.class, "getViewVector"); //$NON-NLS-1$
	}
	
	@Test
	public void getViewVectorTuple2D() {
		Vector2D t = new Vector2d();
		Vector2D v;
		v = CoordinateSystem2D.getViewVector(t);
		assertSame(t, v);
		assertFpVectorEquals(1, 0, t);
	}

	@Test
	public void getLeftVector() {
		Vector2D v;
		v = CoordinateSystem2D.XY_RIGHT_HAND.getLeftVector();
		assertFpVectorEquals(0, 1, v);
		v = CoordinateSystem2D.XY_LEFT_HAND.getLeftVector();
		assertFpVectorEquals(0, -1, v);
	}
	
	@Test
	public void getLeftVectorTuple2D() {
		Vector2D t = new Vector2d();
		Vector2D v;
		v = CoordinateSystem2D.XY_RIGHT_HAND.getLeftVector(t);
		assertSame(t, v);
		assertFpVectorEquals(0, 1, t);
		v = CoordinateSystem2D.XY_LEFT_HAND.getLeftVector(t);
		assertSame(t, v);
		assertFpVectorEquals(0, -1, t);
	}

	@Test
	public void getRightVector() {
		Vector2D v;
		v = CoordinateSystem2D.XY_RIGHT_HAND.getRightVector();
		assertFpVectorEquals(0, -1, v);
		v = CoordinateSystem2D.XY_LEFT_HAND.getRightVector();
		assertFpVectorEquals(0, 1, v);
	}
	
	@Test
	public void getRightVectorTuple2D() {
		Vector2D t = new Vector2d();
		Vector2D v;
		v = CoordinateSystem2D.XY_RIGHT_HAND.getRightVector(t);
		assertSame(t, v);
		assertFpVectorEquals(0, -1, t);
		v = CoordinateSystem2D.XY_LEFT_HAND.getRightVector(t);
		assertSame(t, v);
		assertFpVectorEquals(0, 1, t);
	}

	@Test
	public void isLeftHanded() {
		assertFalse(CoordinateSystem2D.XY_RIGHT_HAND.isLeftHanded());
		assertTrue(CoordinateSystem2D.XY_LEFT_HAND.isLeftHanded());
	}
	
	@Test
	public void isRightHanded() {
		assertTrue(CoordinateSystem2D.XY_RIGHT_HAND.isRightHanded());
		assertFalse(CoordinateSystem2D.XY_LEFT_HAND.isRightHanded());
	}

	@Test
	public void toCoordinateSystem3D() {
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem2D.XY_RIGHT_HAND.toCoordinateSystem3D());
		assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem2D.XY_LEFT_HAND.toCoordinateSystem3D());
	}

	@Test(expected = AssertionError.class)
	public void fromVectorsDouble_invalidParameter() {
		CoordinateSystem2D.fromVectors(0.);
	}

	@Test
	public void fromVectorsDouble() {
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.fromVectors(1.));
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem2D.fromVectors(-1.));
	}

	@Test(expected = AssertionError.class)
	public void fromVectorsInt_invalidParameter() {
		CoordinateSystem2D.fromVectors(0);
	}

	@Test
	public void fromVectorsInt() {
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.fromVectors(1));
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem2D.fromVectors(-1));
	}

	@Test
	public void toDefaultPoint2D_systemDefault() {
		Point2d p1 = new Point2d(-45, 78);
		Point2d p2 = new Point2d(45, -78);
		Point2d p3 = new Point2d(-45, -78);
		Point2d p4 = new Point2d(45, 78);
		Point2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(45, 78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(45, -78, p);
	}

	@Test
	public void toDefaultPoint2D_rightHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
		Point2d p1 = new Point2d(-45, 78);
		Point2d p2 = new Point2d(45, -78);
		Point2d p3 = new Point2d(-45, -78);
		Point2d p4 = new Point2d(45, 78);
		Point2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(45, 78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(45, -78, p);
	}

	@Test
	public void toDefaultPoint2D_leftHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
		Point2d p1 = new Point2d(-45, 78);
		Point2d p2 = new Point2d(45, -78);
		Point2d p3 = new Point2d(-45, -78);
		Point2d p4 = new Point2d(45, 78);
		Point2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpPointEquals(45, -78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpPointEquals(45, 78, p);
	}

	@Test
	public void fromDefaultPoint2D_systemDefault() {
		Point2d p1 = new Point2d(-45, 78);
		Point2d p2 = new Point2d(45, -78);
		Point2d p3 = new Point2d(-45, -78);
		Point2d p4 = new Point2d(45, 78);
		Point2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(45, 78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(45, -78, p);
	}

	@Test
	public void fromDefaultPoint2D_rightHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
		Point2d p1 = new Point2d(-45, 78);
		Point2d p2 = new Point2d(45, -78);
		Point2d p3 = new Point2d(-45, -78);
		Point2d p4 = new Point2d(45, 78);
		Point2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(45, 78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(45, -78, p);
	}

	@Test
	public void fromDefaultPoint2D_leftHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
		Point2d p1 = new Point2d(-45, 78);
		Point2d p2 = new Point2d(45, -78);
		Point2d p3 = new Point2d(-45, -78);
		Point2d p4 = new Point2d(45, 78);
		Point2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpPointEquals(45, -78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpPointEquals(45, 78, p);
	}

	@Test
	public void toDefaultVector2D_systemDefault() {
		Vector2d p1 = new Vector2d(-45, 78);
		Vector2d p2 = new Vector2d(45, -78);
		Vector2d p3 = new Vector2d(-45, -78);
		Vector2d p4 = new Vector2d(45, 78);
		Vector2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(45, 78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(45, -78, p);
	}

	@Test
	public void toDefaultVector2D_rightHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
		Vector2d p1 = new Vector2d(-45, 78);
		Vector2d p2 = new Vector2d(45, -78);
		Vector2d p3 = new Vector2d(-45, -78);
		Vector2d p4 = new Vector2d(45, 78);
		Vector2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(45, 78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(45, -78, p);
	}

	@Test
	public void toDefaultVector2D_leftHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
		Vector2d p1 = new Vector2d(-45, 78);
		Vector2d p2 = new Vector2d(45, -78);
		Vector2d p3 = new Vector2d(-45, -78);
		Vector2d p4 = new Vector2d(45, 78);
		Vector2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(p);
		assertFpVectorEquals(45, -78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(p);
		assertFpVectorEquals(45, 78, p);
	}

	@Test
	public void fromDefaultVector2D_systemDefault() {
		Vector2d p1 = new Vector2d(-45, 78);
		Vector2d p2 = new Vector2d(45, -78);
		Vector2d p3 = new Vector2d(-45, -78);
		Vector2d p4 = new Vector2d(45, 78);
		Vector2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(45, 78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(45, -78, p);
	}

	@Test
	public void fromDefaultVector2D_rightHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
		Vector2d p1 = new Vector2d(-45, 78);
		Vector2d p2 = new Vector2d(45, -78);
		Vector2d p3 = new Vector2d(-45, -78);
		Vector2d p4 = new Vector2d(45, 78);
		Vector2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(45, 78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(45, -78, p);
	}

	@Test
	public void fromDefaultVector2D_leftHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
		Vector2d p1 = new Vector2d(-45, 78);
		Vector2d p2 = new Vector2d(45, -78);
		Vector2d p3 = new Vector2d(-45, -78);
		Vector2d p4 = new Vector2d(45, 78);
		Vector2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(p);
		assertFpVectorEquals(45, -78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(p);
		assertFpVectorEquals(45, 78, p);
	}

	@Test
	public void toDefaultDouble_systemDefault() {
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(-45));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(45));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(-45));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(45));
	}

	@Test
	public void toDefaultDouble_rightHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(-45));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(45));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(-45));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(45));
	}

	@Test
	public void toDefaultDouble_leftHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
		assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(-45));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.toDefault(45));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(-45));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.toDefault(45));
	}
	
	@Test
	public void fromDefaultDouble_systemDefault() {
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(-45));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(45));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(-45));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(45));
	}

	@Test
	public void fromDefaultDouble_rightHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(-45));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(45));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(-45));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(45));
	}

	@Test
	public void fromDefaultDouble_leftHanded() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
		assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(-45));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(45));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(-45));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.fromDefault(45));
	}

	@Test
	public void toSystemPoint2D_rightHanded() {
		Point2d p1 = new Point2d(-45, 78);
		Point2d p2 = new Point2d(45, -78);
		Point2d p3 = new Point2d(-45, -78);
		Point2d p4 = new Point2d(45, 78);
		Point2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpPointEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpPointEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpPointEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpPointEquals(45, 78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpPointEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpPointEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpPointEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpPointEquals(45, -78, p);
	}

	@Test
	public void toSystemPoint2D_leftHanded() {
		Point2d p1 = new Point2d(-45, 78);
		Point2d p2 = new Point2d(45, -78);
		Point2d p3 = new Point2d(-45, -78);
		Point2d p4 = new Point2d(45, 78);
		Point2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpPointEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpPointEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpPointEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpPointEquals(45, -78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpPointEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpPointEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpPointEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpPointEquals(45, 78, p);
	}

	@Test
	public void toSystemVector2D_rightHanded() {
		Vector2d p1 = new Vector2d(-45, 78);
		Vector2d p2 = new Vector2d(45, -78);
		Vector2d p3 = new Vector2d(-45, -78);
		Vector2d p4 = new Vector2d(45, 78);
		Vector2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpVectorEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpVectorEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpVectorEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpVectorEquals(45, 78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpVectorEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpVectorEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpVectorEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_RIGHT_HAND);
		assertFpVectorEquals(45, -78, p);
	}

	@Test
	public void toSystemVector2D_leftHanded() {
		Vector2d p1 = new Vector2d(-45, 78);
		Vector2d p2 = new Vector2d(45, -78);
		Vector2d p3 = new Vector2d(-45, -78);
		Vector2d p4 = new Vector2d(45, 78);
		Vector2d p;

		p = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpVectorEquals(-45, -78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpVectorEquals(45, 78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpVectorEquals(-45, 78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpVectorEquals(45, -78, p);

		p = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpVectorEquals(-45, 78, p);
		
		p = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpVectorEquals(45, -78, p);

		p = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpVectorEquals(-45, -78, p);

		p = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(p, CoordinateSystem2D.XY_LEFT_HAND);
		assertFpVectorEquals(45, 78, p);
	}

	@Test
	public void toSystemDouble_rightHanded() {
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.toSystem(-45, CoordinateSystem2D.XY_RIGHT_HAND));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.toSystem(45, CoordinateSystem2D.XY_RIGHT_HAND));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.toSystem(-45, CoordinateSystem2D.XY_RIGHT_HAND));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.toSystem(45, CoordinateSystem2D.XY_RIGHT_HAND));
	}

	@Test
	public void toSystemDouble_leftHanded() {
		assertEpsilonEquals(45, CoordinateSystem2D.XY_RIGHT_HAND.toSystem(-45, CoordinateSystem2D.XY_LEFT_HAND));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_RIGHT_HAND.toSystem(45, CoordinateSystem2D.XY_LEFT_HAND));
		assertEpsilonEquals(-45, CoordinateSystem2D.XY_LEFT_HAND.toSystem(-45, CoordinateSystem2D.XY_LEFT_HAND));
		assertEpsilonEquals(45, CoordinateSystem2D.XY_LEFT_HAND.toSystem(45, CoordinateSystem2D.XY_LEFT_HAND));
	}

	@Test
	public void toSystemTransform2D_rightHanded() {
		Transform2D p1 = new Transform2D();
		Transform2D p2 = new Transform2D();
		Transform2D p3 = new Transform2D();
		Transform2D p4 = new Transform2D();
		Transform2D t;
		
		p1.setIdentity();
		p2.makeTranslationMatrix(-45, 89);
		p3.makeRotationMatrix(-MathConstants.DEMI_PI);
		p4.makeScaleMatrix(.5, 2);

		t = p1.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(new Transform2D(
				1, 0, 0,
				0, 1, 0), t);
		
		t = p2.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(new Transform2D(
				1, 0, -45,
				0, 1, 89), t);

		t = p3.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(new Transform2D(
				0, 1, 0,
				-1, 0, 0), t);

		t = p4.clone();
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(new Transform2D(
				.5, 0, 0,
				0, 2, 0), t);

		t = p1.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(new Transform2D(
				1, 0, 0,
				0, 1, 0), t);
		
		t = p2.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(new Transform2D(
				1, 0, -45,
				0, 1, -89), t);

		t = p3.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(new Transform2D(
				0, -1, 0,
				1, 0, 0), t);

		t = p4.clone();
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(t, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(new Transform2D(
				.5, 0, 0,
				0, 2, 0), t);
	}

	@Test
	public void getBackVector() {
		assertFpVectorEquals(-1, 0, CoordinateSystem2D.getBackVector());
		assertInlineParameterUsage(CoordinateSystem2D.class, "getBackVector"); //$NON-NLS-1$
	}

}

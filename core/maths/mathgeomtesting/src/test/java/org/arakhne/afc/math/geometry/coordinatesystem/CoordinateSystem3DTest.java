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

import org.junit.Test;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;

/**
 * Unit test for {@link CoordinateSystem3D}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class CoordinateSystem3DTest extends AbstractMathTestCase {

	@Test
	public void getDimensions() {
		for (CoordinateSystem3D cst : CoordinateSystem3D.values()) {
			assertEquals(cst.name(), 3, cst.getDimensions());
		}
	}

	@Test
	public void isRightHanded() {
		assertTrue(CoordinateSystem3D.XYZ_RIGHT_HAND.isRightHanded());
		assertTrue(CoordinateSystem3D.XZY_RIGHT_HAND.isRightHanded());
		assertFalse(CoordinateSystem3D.XYZ_LEFT_HAND.isRightHanded());
		assertFalse(CoordinateSystem3D.XZY_LEFT_HAND.isRightHanded());
	}

	@Test
	public void isLeftHanded() {
		assertFalse(CoordinateSystem3D.XYZ_RIGHT_HAND.isLeftHanded());
		assertFalse(CoordinateSystem3D.XZY_RIGHT_HAND.isLeftHanded());
		assertTrue(CoordinateSystem3D.XYZ_LEFT_HAND.isLeftHanded());
		assertTrue(CoordinateSystem3D.XZY_LEFT_HAND.isLeftHanded());
	}

	@Test
	public void getDefaultCoordinateSystem() {
		assertEquals(CoordinateSystemConstants.SIMULATION_3D, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	@Test
	public void setDefaultCoordinateSystem() {
		CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEquals(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEquals(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XZY_LEFT_HAND);
		assertEquals(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEquals(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	@Test
	public void toSystemTuple3DCoordinateSystem3D() {
		Vector3d r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(r, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Vector3d(1, 2, 3), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(r, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Vector3d(1, 3, -2), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(r, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(new Vector3d(1, -2, 3), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(r, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(new Vector3d(1, 3, 2), r);
		//
		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(r, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Vector3d(1, -3, 2), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(r, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Vector3d(1, 2, 3), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(r, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(new Vector3d(1, 3, 2), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(r, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(new Vector3d(1, 2, -3), r);
		//
		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Vector3d(1, -2, 3), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Vector3d(1, 3, 2), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(new Vector3d(1, 2, 3), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(new Vector3d(1, 3, -2), r);
		//
		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Vector3d(1, 3, 2), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Vector3d(1, 2, -3), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(new Vector3d(1, -3, 2), r);

		r = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(new Vector3d(1, 2, 3), r);
	}

	@Test
	public void toSystemQuaternionCoordinateSystem3D() {
		Quaternion4d r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(r, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Quaternion4d(0.18898, 0.37796, 0.56695, 0.70711), r);
		
		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(r, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Quaternion4d(0.16222, 0.48666, 0.48666, 0.70711), r);
		
		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(r, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(new Quaternion4d(-0.18898, 0.37796, -0.56695, 0.70711), r);

		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(r, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(new Quaternion4d(-0.16222, -0.48666, 0.48666, 0.70711), r);
		//
		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(r, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Quaternion4d(0.18898, -0.56695, 0.37796, 0.70711), r);

		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(r, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Quaternion4d(0.18898, 0.37796, 0.56695, 0.70711), r);

		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(r, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(new Quaternion4d(-0.18898, -0.56695, -0.37796, 0.70711), r);

		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XZY_LEFT_HAND.toSystem(r, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(new Quaternion4d(-0.2357, -0.4714, 0.4714, 0.70711), r);
		//
		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Quaternion4d(-0.18898, 0.37796, -0.56695, 0.70711), r);

		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Quaternion4d(-0.16222, -0.48666, -0.48666, 0.70711), r);

		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(new Quaternion4d(0.18898, 0.37796, 0.56695, 0.70711), r);

		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(new Quaternion4d(0.16222, 0.48666, -0.48666, 0.70711), r);
		//
		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Quaternion4d(-0.18898, -0.56695, -0.37796, 0.70711), r);

		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Quaternion4d(-0.2357, -0.4714, -0.4714, 0.70711), r);

		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XYZ_RIGHT_HAND);
		assertEpsilonEquals(new Quaternion4d(0.18898, -0.56695, 0.37796, 0.70711), r);

		r = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		CoordinateSystem3D.XZY_RIGHT_HAND.toSystem(r, CoordinateSystem3D.XZY_RIGHT_HAND);
		assertEpsilonEquals(new Quaternion4d(0.18898, 0.37796, 0.56695, 0.70711), r);
	}

	@Test
	public void toSystemTransform3DCoordinateSystem3D() {
		Transform3D r = new Transform3D(-0.8293098, -0.5587891, 0, 3, 0.5587891, -0.8293098, 0, 6, 0, 0, 1, 0);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(r, CoordinateSystem3D.XYZ_LEFT_HAND);
		assertEpsilonEquals(new Transform3D(-0.8293098, -0.5587891, 0, 3, 0.5587891, -0.8293098, 0, 6, 0, 0, 1, 0), r);

		r = new Transform3D(-0.8293098, -0.5587891, 0, 3, 0.5587891, -0.8293098, 0, 6, 0, 0, 1, 0);
		CoordinateSystem3D.XYZ_LEFT_HAND.toSystem(r, CoordinateSystem3D.XZY_LEFT_HAND);
		assertEpsilonEquals(new Transform3D(-0.8293098, -0.39512, 0.39512, 3, 0.39512, 0.08535, 0.91465, 0, -0.39512, 0.91465, 0.08535, -6), r);
	}

	@Test
	public void fromVectorsIntIntIntInt() {
		assertEquals(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(1, 0, 0, 1));
		try {
			CoordinateSystem3D.fromVectors(1, 0, 0, -1);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		try {
			CoordinateSystem3D.fromVectors(-1, 0, 0, -1);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		assertEquals(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(-1, 0, 0, 1));
		assertEquals(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(0, 1, 1, 0));
		try {
			CoordinateSystem3D.fromVectors(0, 1, -1, 0);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		try {
			CoordinateSystem3D.fromVectors(0, -1, -1, 0);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		assertEquals(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(0, -1, 1, 0));
	}

	@Test
	public void fromVectorsDoubleDoubleDoubleDouble() {
		assertEquals(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(1., 0., 0., 1.));
		try {
			CoordinateSystem3D.fromVectors(1., 0., 0., -1.);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		try {
			CoordinateSystem3D.fromVectors(-1., 0., 0., -1.);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		assertEquals(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(-1., 0., 0., 1.));
		assertEquals(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(0., 1., 1., 0.));
		try {
			CoordinateSystem3D.fromVectors(0., 1., -1., 0.);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		try {
			CoordinateSystem3D.fromVectors(0., -1., -1., 0.);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		assertEquals(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(0., -1., 1., 0.));
	}

	@Test
	public void fromVectorsIntIntIntIntIntIntIntIntInt() {
		assertEquals(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(1, 0, 0, 0, 1, 0, 0, 0, 1));
		try {
			CoordinateSystem3D.fromVectors(1, 0, 0, 0, 1, 0, 0, 0, -1);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		try {
			CoordinateSystem3D.fromVectors(1, 0, 0, 0, -1, 0, 0, 0, -1);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		assertEquals(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(1, 0, 0, 0, -1, 0, 0, 0, 1));
		assertEquals(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(1, 0, 0, 0, 0, 1, 0, 1, 0));
		try {
			CoordinateSystem3D.fromVectors(1, 0, 0, 0, 0, 1, 0, -1, 0);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		try {
			CoordinateSystem3D.fromVectors(1, 0, 0, 0, 0, -1, 0, -1, 0);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		assertEquals(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(1, 0, 0, 0, 0, -1, 0, 1, 0));

		Vector3d v1 = new Vector3d(1, 1, 1);
		v1.normalize();
		Vector3d v2 = new Vector3d(1, -1, -1);
		v2.normalize();
		Vector3d v3l = new Vector3d();
		v3l.crossLeftHand(v1, v2);
		Vector3d v3r = new Vector3d();
		v3r.crossRightHand(v1, v2);
		assertEquals(CoordinateSystem3D.XYZ_RIGHT_HAND,
				CoordinateSystem3D.fromVectors(
						v1.ix(), v1.iy(), v1.iz(),
						v2.ix(), v2.iy(), v2.iz(),
						v3l.ix(), v3l.iy(), v3l.iz()));
	}

	@Test
	public void fromVectorsDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		assertEquals(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(1., 0., 0., 0., 1., 0., 0., 0., 1.));
		try {
			CoordinateSystem3D.fromVectors(1., 0., 0., 0., 1., 0., 0., 0., -1.);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		try {
			CoordinateSystem3D.fromVectors(1., 0., 0., 0., -1., 0., 0., 0., -1.);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		assertEquals(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(1., 0., 0., 0., -1., 0., 0., 0., 1.));
		assertEquals(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(1., 0., 0., 0., 0., 1., 0., 1., 0.));
		try {
			CoordinateSystem3D.fromVectors(1., 0., 0., 0., 0., 1., 0., -1., 0.);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		try {
			CoordinateSystem3D.fromVectors(1., 0., 0., 0., 0., -1., 0., -1., 0.);
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
		assertEquals(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(1., 0., 0., 0., 0., -1., 0., 1., 0.));

		Vector3d v1 = new Vector3d(1, 1, 1);
		v1.normalize();
		Vector3d v2 = new Vector3d(1, -1, -1);
		v2.normalize();
		Vector3d v3l = new Vector3d();
		v3l.crossLeftHand(v1, v2);
		Vector3d v3r = new Vector3d();
		v3r.crossRightHand(v1, v2);
		try {
			assertEquals(CoordinateSystem3D.XYZ_RIGHT_HAND,
					CoordinateSystem3D.fromVectors(
							v1.getX(), v1.getY(), v1.getZ(),
							v2.getX(), v2.getY(), v2.getZ(),
							v3r.getX(), v3r.getY(), v3r.getZ()));
			fail("Expected CoordinateSystemNotFoundException"); //$NON-NLS-1$
		} catch (CoordinateSystemNotFoundException e) {
			//
		}
	}

	@Test
	public void isZOnUp() {
		assertTrue(CoordinateSystem3D.XYZ_RIGHT_HAND.isZOnUp());
		assertFalse(CoordinateSystem3D.XZY_RIGHT_HAND.isZOnUp());
		assertTrue(CoordinateSystem3D.XYZ_LEFT_HAND.isZOnUp());
		assertFalse(CoordinateSystem3D.XZY_LEFT_HAND.isZOnUp());
	}

	@Test
	public void isYOnUp() {
		assertFalse(CoordinateSystem3D.XYZ_RIGHT_HAND.isYOnUp());
		assertTrue(CoordinateSystem3D.XZY_RIGHT_HAND.isYOnUp());
		assertFalse(CoordinateSystem3D.XYZ_LEFT_HAND.isYOnUp());
		assertTrue(CoordinateSystem3D.XZY_LEFT_HAND.isYOnUp());
	}

	@Test
	public void isZOnSide() {
		assertFalse(CoordinateSystem3D.XYZ_RIGHT_HAND.isZOnSide());
		assertTrue(CoordinateSystem3D.XZY_RIGHT_HAND.isZOnSide());
		assertFalse(CoordinateSystem3D.XYZ_LEFT_HAND.isZOnSide());
		assertTrue(CoordinateSystem3D.XZY_LEFT_HAND.isZOnSide());
	}

	@Test
	public void isYOnSide() {
		assertTrue(CoordinateSystem3D.XYZ_RIGHT_HAND.isYOnSide());
		assertFalse(CoordinateSystem3D.XZY_RIGHT_HAND.isYOnSide());
		assertTrue(CoordinateSystem3D.XYZ_LEFT_HAND.isYOnSide());
		assertFalse(CoordinateSystem3D.XZY_LEFT_HAND.isYOnSide());
	}

	@Test
	public void getViewVector() {
		assertEpsilonEquals(new Vector3d(1, 0, 0), CoordinateSystem3D.XYZ_LEFT_HAND.getViewVector());
		assertEpsilonEquals(new Vector3d(1, 0, 0), CoordinateSystem3D.XZY_LEFT_HAND.getViewVector());
		assertEpsilonEquals(new Vector3d(1, 0, 0), CoordinateSystem3D.XYZ_RIGHT_HAND.getViewVector());
		assertEpsilonEquals(new Vector3d(1, 0, 0), CoordinateSystem3D.XZY_RIGHT_HAND.getViewVector());
	}

	@Test
	public void getViewVectorVector3D() {
		Vector3d r = new Vector3d();
		CoordinateSystem3D.XYZ_LEFT_HAND.getViewVector(r);
		assertEpsilonEquals(new Vector3d(1, 0, 0), r);
		CoordinateSystem3D.XZY_LEFT_HAND.getViewVector(r);
		assertEpsilonEquals(new Vector3d(1, 0, 0), r);
		CoordinateSystem3D.XYZ_RIGHT_HAND.getViewVector(r);
		assertEpsilonEquals(new Vector3d(1, 0, 0), r);
		CoordinateSystem3D.XZY_RIGHT_HAND.getViewVector(r);
		assertEpsilonEquals(new Vector3d(1, 0, 0), r);
	}

	@Test
	public void getBackVector() {
		assertEpsilonEquals(new Vector3d(-1, 0, 0), CoordinateSystem3D.XYZ_LEFT_HAND.getBackVector());
		assertEpsilonEquals(new Vector3d(-1, 0, 0), CoordinateSystem3D.XZY_LEFT_HAND.getBackVector());
		assertEpsilonEquals(new Vector3d(-1, 0, 0), CoordinateSystem3D.XYZ_RIGHT_HAND.getBackVector());
		assertEpsilonEquals(new Vector3d(-1, 0, 0), CoordinateSystem3D.XZY_RIGHT_HAND.getBackVector());
	}

	@Test
	public void getBackVectorVector3D() {
		Vector3d r = new Vector3d();
		CoordinateSystem3D.XYZ_LEFT_HAND.getBackVector(r);
		assertEpsilonEquals(new Vector3d(-1, 0, 0), r);
		CoordinateSystem3D.XZY_LEFT_HAND.getBackVector(r);
		assertEpsilonEquals(new Vector3d(-1, 0, 0), r);
		CoordinateSystem3D.XYZ_RIGHT_HAND.getBackVector(r);
		assertEpsilonEquals(new Vector3d(-1, 0, 0), r);
		CoordinateSystem3D.XZY_RIGHT_HAND.getBackVector(r);
		assertEpsilonEquals(new Vector3d(-1, 0, 0), r);
	}

	@Test
	public void getLeftVector() {
		assertEpsilonEquals(new Vector3d(0, -1, 0), CoordinateSystem3D.XYZ_LEFT_HAND.getLeftVector());
		assertEpsilonEquals(new Vector3d(0, 0, 1), CoordinateSystem3D.XZY_LEFT_HAND.getLeftVector());
		assertEpsilonEquals(new Vector3d(0, 1, 0), CoordinateSystem3D.XYZ_RIGHT_HAND.getLeftVector());
		assertEpsilonEquals(new Vector3d(0, 0, -1), CoordinateSystem3D.XZY_RIGHT_HAND.getLeftVector());
	}

	@Test
	public void getLeftVectorVector3D() {
		Vector3d r = new Vector3d();
		CoordinateSystem3D.XYZ_LEFT_HAND.getLeftVector(r);
		assertEpsilonEquals(new Vector3d(0, -1, 0), r);
		CoordinateSystem3D.XZY_LEFT_HAND.getLeftVector(r);
		assertEpsilonEquals(new Vector3d(0, 0, 1), r);
		CoordinateSystem3D.XYZ_RIGHT_HAND.getLeftVector(r);
		assertEpsilonEquals(new Vector3d(0, 1, 0), r);
		CoordinateSystem3D.XZY_RIGHT_HAND.getLeftVector(r);
		assertEpsilonEquals(new Vector3d(0, 0, -1), r);
	}

	@Test
	public void getRightVector() {
		assertEpsilonEquals(new Vector3d(0, 1, 0), CoordinateSystem3D.XYZ_LEFT_HAND.getRightVector());
		assertEpsilonEquals(new Vector3d(0, 0, -1), CoordinateSystem3D.XZY_LEFT_HAND.getRightVector());
		assertEpsilonEquals(new Vector3d(0, -1, 0), CoordinateSystem3D.XYZ_RIGHT_HAND.getRightVector());
		assertEpsilonEquals(new Vector3d(0, 0, 1), CoordinateSystem3D.XZY_RIGHT_HAND.getRightVector());
	}

	@Test
	public void getRightVectorVector3D() {
		Vector3d r = new Vector3d();
		CoordinateSystem3D.XYZ_LEFT_HAND.getRightVector(r);
		assertEpsilonEquals(new Vector3d(0, 1, 0), r);
		CoordinateSystem3D.XZY_LEFT_HAND.getRightVector(r);
		assertEpsilonEquals(new Vector3d(0, 0, -1), r);
		CoordinateSystem3D.XYZ_RIGHT_HAND.getRightVector(r);
		assertEpsilonEquals(new Vector3d(0, -1, 0), r);
		CoordinateSystem3D.XZY_RIGHT_HAND.getRightVector(r);
		assertEpsilonEquals(new Vector3d(0, 0, 1), r);
	}

	@Test
	public void getUpVector() {
		assertEpsilonEquals(new Vector3d(0, 0, 1), CoordinateSystem3D.XYZ_LEFT_HAND.getUpVector());
		assertEpsilonEquals(new Vector3d(0, 1, 0), CoordinateSystem3D.XZY_LEFT_HAND.getUpVector());
		assertEpsilonEquals(new Vector3d(0, 0, 1), CoordinateSystem3D.XYZ_RIGHT_HAND.getUpVector());
		assertEpsilonEquals(new Vector3d(0, 1, 0), CoordinateSystem3D.XZY_RIGHT_HAND.getUpVector());
	}

	@Test
	public void getUpVectorVector3D() {
		Vector3d r = new Vector3d();
		CoordinateSystem3D.XYZ_LEFT_HAND.getUpVector(r);
		assertEpsilonEquals(new Vector3d(0, 0, 1), r);
		CoordinateSystem3D.XZY_LEFT_HAND.getUpVector(r);
		assertEpsilonEquals(new Vector3d(0, 1, 0), r);
		CoordinateSystem3D.XYZ_RIGHT_HAND.getUpVector(r);
		assertEpsilonEquals(new Vector3d(0, 0, 1), r);
		CoordinateSystem3D.XZY_RIGHT_HAND.getUpVector(r);
		assertEpsilonEquals(new Vector3d(0, 1, 0), r);
	}

	@Test
	public void getDownVector() {
		assertEpsilonEquals(new Vector3d(0, 0, -1), CoordinateSystem3D.XYZ_LEFT_HAND.getDownVector());
		assertEpsilonEquals(new Vector3d(0, -1, 0), CoordinateSystem3D.XZY_LEFT_HAND.getDownVector());
		assertEpsilonEquals(new Vector3d(0, 0, -1), CoordinateSystem3D.XYZ_RIGHT_HAND.getDownVector());
		assertEpsilonEquals(new Vector3d(0, -1, 0), CoordinateSystem3D.XZY_RIGHT_HAND.getDownVector());
	}

	@Test
	public void getDownVectorVector3D() {
		Vector3d r = new Vector3d();
		CoordinateSystem3D.XYZ_LEFT_HAND.getDownVector(r);
		assertEpsilonEquals(new Vector3d(0, 0, -1), r);
		CoordinateSystem3D.XZY_LEFT_HAND.getDownVector(r);
		assertEpsilonEquals(new Vector3d(0, -1, 0), r);
		CoordinateSystem3D.XYZ_RIGHT_HAND.getDownVector(r);
		assertEpsilonEquals(new Vector3d(0, 0, -1), r);
		CoordinateSystem3D.XZY_RIGHT_HAND.getDownVector(r);
		assertEpsilonEquals(new Vector3d(0, -1, 0), r);
	}

	@Test
	public void heightTuple3D() {
		Vector3d v = new Vector3d(1, 2, 3);
		assertEpsilonEquals(3, CoordinateSystem3D.XYZ_LEFT_HAND.height(v));

		v = new Vector3d(1, 2, 3);
		assertEpsilonEquals(2, CoordinateSystem3D.XZY_LEFT_HAND.height(v));

		v = new Vector3d(1, 2, 3);
		assertEpsilonEquals(3, CoordinateSystem3D.XYZ_RIGHT_HAND.height(v));

		v = new Vector3d(1, 2, 3);
		assertEpsilonEquals(2, CoordinateSystem3D.XZY_RIGHT_HAND.height(v));
	}

	@Test
	public void heightDoubleDoubleDouble() {
		assertEpsilonEquals(3, CoordinateSystem3D.XYZ_LEFT_HAND.height(1, 2, 3));
		assertEpsilonEquals(2, CoordinateSystem3D.XZY_LEFT_HAND.height(1, 2, 3));
		assertEpsilonEquals(3, CoordinateSystem3D.XYZ_RIGHT_HAND.height(1, 2, 3));
		assertEpsilonEquals(2, CoordinateSystem3D.XZY_RIGHT_HAND.height(1, 2, 3));
	}

	@Test
	public void sideTuple3D() {
		Vector3d v = new Vector3d(1, 2, 3);
		assertEpsilonEquals(2, CoordinateSystem3D.XYZ_LEFT_HAND.side(v));

		v = new Vector3d(1, 2, 3);
		assertEpsilonEquals(3, CoordinateSystem3D.XZY_LEFT_HAND.side(v));

		v = new Vector3d(1, 2, 3);
		assertEpsilonEquals(2, CoordinateSystem3D.XYZ_RIGHT_HAND.side(v));

		v = new Vector3d(1, 2, 3);
		assertEpsilonEquals(3, CoordinateSystem3D.XZY_RIGHT_HAND.side(v));
	}

	@Test
	public void sideDoubleDoubleDouble() {
		assertEpsilonEquals(2, CoordinateSystem3D.XYZ_LEFT_HAND.side(1, 2, 3));
		assertEpsilonEquals(3, CoordinateSystem3D.XZY_LEFT_HAND.side(1, 2, 3));
		assertEpsilonEquals(2, CoordinateSystem3D.XYZ_RIGHT_HAND.side(1, 2, 3));
		assertEpsilonEquals(3, CoordinateSystem3D.XZY_RIGHT_HAND.side(1, 2, 3));
	}

	@Test
	public void viewTuple3D() {
		Vector3d v = new Vector3d(1, 2, 3);
		assertEpsilonEquals(1, CoordinateSystem3D.view(v));
	}

	@Test
	public void viewDoubleDoubleDouble() {
		assertEpsilonEquals(1, CoordinateSystem3D.view(1, 2, 3));
	}

	@Test
	public void getHeightCoordinateIndex() {
		assertEquals(2, CoordinateSystem3D.XYZ_LEFT_HAND.getHeightCoordinateIndex());
		assertEquals(1, CoordinateSystem3D.XZY_LEFT_HAND.getHeightCoordinateIndex());
		assertEquals(2, CoordinateSystem3D.XYZ_RIGHT_HAND.getHeightCoordinateIndex());
		assertEquals(1, CoordinateSystem3D.XZY_RIGHT_HAND.getHeightCoordinateIndex());
	}

	@Test
	public void getSideCoordinateIndex() {
		assertEquals(1, CoordinateSystem3D.XYZ_LEFT_HAND.getSideCoordinateIndex());
		assertEquals(2, CoordinateSystem3D.XZY_LEFT_HAND.getSideCoordinateIndex());
		assertEquals(1, CoordinateSystem3D.XYZ_RIGHT_HAND.getSideCoordinateIndex());
		assertEquals(2, CoordinateSystem3D.XZY_RIGHT_HAND.getSideCoordinateIndex());
	}

	@Test
	public void getViewCoordinateIndex() {
		assertEquals(0, CoordinateSystem3D.getViewCoordinateIndex());
	}

	@Test
	public void setHeightTuple3DDouble() {
		Vector3d v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_LEFT_HAND.setHeight(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 2, 123.456), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_LEFT_HAND.setHeight(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 123.456, 3), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setHeight(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 2, 123.456), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_RIGHT_HAND.setHeight(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 123.456, 3), v);
	}

	@Test
	public void addHeightTuple3DDouble() {
		Vector3d v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_LEFT_HAND.addHeight(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 2, 126.456), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_LEFT_HAND.addHeight(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 125.456, 3), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addHeight(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 2, 126.456), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_RIGHT_HAND.addHeight(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 125.456, 3), v);
	}

	@Test
	public void setSideTuple3DDouble() {
		Vector3d v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_LEFT_HAND.setSide(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 123.456, 3), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_LEFT_HAND.setSide(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 2, 123.456), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setSide(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 123.456, 3), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_RIGHT_HAND.setSide(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 2, 123.456), v);
	}

	@Test
	public void addSideTuple3DDouble() {
		Vector3d v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_LEFT_HAND.addSide(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 125.456, 3), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_LEFT_HAND.addSide(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 2, 126.456), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addSide(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 125.456, 3), v);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_RIGHT_HAND.addSide(v, 123.456);
		assertEpsilonEquals(new Vector3d(1, 2, 126.456), v);
	}

	@Test
	public void setViewTuple3DDouble() {
		Vector3d v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.setView(v, 123.456);
		assertEpsilonEquals(new Vector3d(123.456, 2, 3), v);
	}

	@Test
	public void addViewTuple3DDouble() {
		Vector3d v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.addView(v, 123.456);
		assertEpsilonEquals(new Vector3d(124.456, 2, 3), v);
	}

	@Test
	public void toCoordinateSystem2D() {
		assertEquals(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D());
		assertEquals(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D());
		assertEquals(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D());
		assertEquals(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D());
	}

	@Test
	public void toCoordinateSystem2DTuple3DTuple2D() {
		Vector2d r = new Vector2d();
		Vector3d v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(v, r);
		assertEpsilonEquals(new Vector2d(1, 2), r);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(v, r);
		assertEpsilonEquals(new Vector2d(1, 3), r);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(v, r);
		assertEpsilonEquals(new Vector2d(1, 2), r);

		v = new Vector3d(1, 2, 3);
		CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(v, r);
		assertEpsilonEquals(new Vector2d(1, 3), r);
	}

	@Test
	public void toCoordinateSystem2DTransform3DTransform2D() {
		Transform2D r = new Transform2D();
		Transform3D m = new Transform3D(-0.8293098, -0.5587891, 0, 3, 0.5587891, -0.8293098, 0, 6, 0, 0, 1, 0);
		CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(m, r);
		assertEpsilonEquals(new Transform2D(0.3142, -0.94936, 3, 0.94936, 0.3142, 6), r);

		m = new Transform3D(-0.8293098, -0.5587891, 0, 3, 0.5587891, -0.8293098, 0, 6, 0, 0, 1, 0);
		CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(m, r);
		assertEpsilonEquals(new Transform2D(1, 0, 3, 0, 1, 0), r);

		m = new Transform3D(-0.8293098, -0.5587891, 0, 3, 0.5587891, -0.8293098, 0, 6, 0, 0, 1, 0);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(m, r);
		assertEpsilonEquals(new Transform2D(0.3142, -0.94936, 3, 0.94936, 0.3142, 6), r);

		m = new Transform3D(-0.8293098, -0.5587891, 0, 3, 0.5587891, -0.8293098, 0, 6, 0, 0, 1, 0);
		CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(m, r);
		assertEpsilonEquals(new Transform2D(1, 0, 3, 0, 1, 0), r);
	}

	@Test
	public void toCoordinateSystem2DQuaternion() {
		Quaternion4d q = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		assertEpsilonEquals(1.49533, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(q));

		q = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		assertEpsilonEquals(1.35134, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(q));

		q = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		assertEpsilonEquals(1.49533, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(q));

		q = Quaternion4d.newAxisAngle(1, 2, 3, MathConstants.DEMI_PI);
		assertEpsilonEquals(1.35134, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(q));
	}

}

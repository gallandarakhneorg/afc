/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.test.geometry.coordinatesystem;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.test.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit test for {@link CoordinateSystem3D}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.18
 */
@SuppressWarnings("all")
public class CoordinateSystem3DTest extends AbstractMathTestCase {

	@BeforeEach
	public void setUp() {
		// Reset the coordinate system to the system default.
		CoordinateSystem3D.setDefaultCoordinateSystem(null);
	}

	@Test
	public void getDimensions() {
		assertEquals(3, CoordinateSystem3D.XYZ_RIGHT_HAND.getDimensions());
		assertEquals(3, CoordinateSystem3D.XYZ_LEFT_HAND.getDimensions());
		assertEquals(3, CoordinateSystem3D.XZY_RIGHT_HAND.getDimensions());
		assertEquals(3, CoordinateSystem3D.XZY_RIGHT_HAND.getDimensions());
	}

	@Test
	public void getViewVector() {
		Vector3D<?, ?, ?> v;
		v = CoordinateSystem3D.getViewVector();
		assertEpsilonEquals(new Vector3d(1, 0, 0), v);
		//
		assertInlineParameterUsage(CoordinateSystem3D.class, "getViewVector"); //$NON-NLS-1$
	}

	@DisplayName("getViewVector(Tuple3D)")
	@Test
	public void getViewVectorTuple3D() {
		Vector3D t = new Vector3d();
		Vector3D v;
		v = CoordinateSystem3D.getViewVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(1, 0, 0), t);
	}

	@Test
	public void getBackVector() {
		Vector3D<?, ?, ?> v;
		v = CoordinateSystem3D.getBackVector();
		assertEpsilonEquals(new Vector3d(-1, 0, 0), v);
		//
		assertInlineParameterUsage(CoordinateSystem3D.class, "getBackVector"); //$NON-NLS-1$
	}

	@DisplayName("getBackVector(Tuple3D)")
	@Test
	public void getBackVectorTuple3D() {
		Vector3D t = new Vector3d();
		Vector3D v;
		v = CoordinateSystem3D.getBackVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(-1, 0, 0), t);
	}

	@Test
	public void getLeftVector() {
		Vector3D v;
		v = CoordinateSystem3D.XYZ_RIGHT_HAND.getLeftVector();
		assertEpsilonEquals(new Vector3d(0, 1, 0), v);
		v = CoordinateSystem3D.XYZ_LEFT_HAND.getLeftVector();
		assertEpsilonEquals(new Vector3d(0, -1, 0), v);
		v = CoordinateSystem3D.XZY_RIGHT_HAND.getLeftVector();
		assertEpsilonEquals(new Vector3d(0, 0, -1), v);
		v = CoordinateSystem3D.XZY_LEFT_HAND.getLeftVector();
		assertEpsilonEquals(new Vector3d(0, 0, 1), v);
	}

	@DisplayName("getLeftVector(Tuple3D)")
	@Test
	public void getLeftVectorTuple3D() {
		Vector3D t = new Vector3d();
		Vector3D v;
		v = CoordinateSystem3D.XYZ_RIGHT_HAND.getLeftVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 1, 0), t);
		v = CoordinateSystem3D.XYZ_LEFT_HAND.getLeftVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, -1, 0), t);
		v = CoordinateSystem3D.XZY_RIGHT_HAND.getLeftVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 0, -1), t);
		v = CoordinateSystem3D.XZY_LEFT_HAND.getLeftVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 0, 1), t);
	}

	@Test
	public void getRightVector() {
		Vector3D v;
		v = CoordinateSystem3D.XYZ_RIGHT_HAND.getRightVector();
		assertEpsilonEquals(new Vector3d(0, -1, 0), v);
		v = CoordinateSystem3D.XYZ_LEFT_HAND.getRightVector();
		assertEpsilonEquals(new Vector3d(0, 1, 0), v);
		v = CoordinateSystem3D.XZY_RIGHT_HAND.getRightVector();
		assertEpsilonEquals(new Vector3d(0, 0, 1), v);
		v = CoordinateSystem3D.XZY_LEFT_HAND.getRightVector();
		assertEpsilonEquals(new Vector3d(0, 0, -1), v);
	}

	@DisplayName("getRightVector(Tuple3D)")
	@Test
	public void getRightVectorTuple3D() {
		Vector3D t = new Vector3d();
		Vector3D v;
		v = CoordinateSystem3D.XYZ_RIGHT_HAND.getRightVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, -1, 0), t);
		v = CoordinateSystem3D.XYZ_LEFT_HAND.getRightVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 1, 0), t);
		v = CoordinateSystem3D.XZY_RIGHT_HAND.getRightVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 0, 1), t);
		v = CoordinateSystem3D.XZY_LEFT_HAND.getRightVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 0, -1), t);
	}

	@Test
	public void getUpVector() {
		Vector3D v;
		v = CoordinateSystem3D.XYZ_RIGHT_HAND.getUpVector();
		assertEpsilonEquals(new Vector3d(0, 0, 1), v);
		v = CoordinateSystem3D.XYZ_LEFT_HAND.getUpVector();
		assertEpsilonEquals(new Vector3d(0, 0, 1), v);
		v = CoordinateSystem3D.XZY_RIGHT_HAND.getUpVector();
		assertEpsilonEquals(new Vector3d(0, 1, 0), v);
		v = CoordinateSystem3D.XZY_LEFT_HAND.getUpVector();
		assertEpsilonEquals(new Vector3d(0, 1, 0), v);
	}

	@DisplayName("getUpVector(Tuple3D)")
	@Test
	public void getUpVectorTuple3D() {
		Vector3D t = new Vector3d();
		Vector3D v;
		v = CoordinateSystem3D.XYZ_RIGHT_HAND.getUpVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 0, 1), t);
		v = CoordinateSystem3D.XYZ_LEFT_HAND.getUpVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 0, 1), t);
		v = CoordinateSystem3D.XZY_RIGHT_HAND.getUpVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 1, 0), t);
		v = CoordinateSystem3D.XZY_LEFT_HAND.getUpVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 1, 0), t);
	}

	@Test
	public void getDownVector() {
		Vector3D v;
		v = CoordinateSystem3D.XYZ_RIGHT_HAND.getDownVector();
		assertEpsilonEquals(new Vector3d(0, 0, -1), v);
		v = CoordinateSystem3D.XYZ_LEFT_HAND.getDownVector();
		assertEpsilonEquals(new Vector3d(0, 0, -1), v);
		v = CoordinateSystem3D.XZY_RIGHT_HAND.getDownVector();
		assertEpsilonEquals(new Vector3d(0, -1, 0), v);
		v = CoordinateSystem3D.XZY_LEFT_HAND.getDownVector();
		assertEpsilonEquals(new Vector3d(0, -1, 0), v);
	}

	@DisplayName("getDownVector(Tuple3D)")
	@Test
	public void getDownVectorTuple3D() {
		Vector3D t = new Vector3d();
		Vector3D v;
		v = CoordinateSystem3D.XYZ_RIGHT_HAND.getDownVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 0, -1), t);
		v = CoordinateSystem3D.XYZ_LEFT_HAND.getDownVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, 0, -1), t);
		v = CoordinateSystem3D.XZY_RIGHT_HAND.getDownVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, -1, 0), t);
		v = CoordinateSystem3D.XZY_LEFT_HAND.getDownVector(t);
		assertSame(t, v);
		assertEpsilonEquals(new Vector3d(0, -1, 0), t);
	}

	@Test
	public void isLeftHanded() {
		assertFalse(CoordinateSystem3D.XYZ_RIGHT_HAND.isLeftHanded());
		assertTrue(CoordinateSystem3D.XYZ_LEFT_HAND.isLeftHanded());
		assertFalse(CoordinateSystem3D.XZY_RIGHT_HAND.isLeftHanded());
		assertTrue(CoordinateSystem3D.XZY_LEFT_HAND.isLeftHanded());
	}

	@Test
	public void isRightHanded() {
		assertTrue(CoordinateSystem3D.XYZ_RIGHT_HAND.isRightHanded());
		assertFalse(CoordinateSystem3D.XYZ_LEFT_HAND.isRightHanded());
		assertTrue(CoordinateSystem3D.XZY_RIGHT_HAND.isRightHanded());
		assertFalse(CoordinateSystem3D.XZY_LEFT_HAND.isRightHanded());
	}

	@Test
	public void isZOnUp() {
		assertTrue(CoordinateSystem3D.XYZ_RIGHT_HAND.isZOnUp());
		assertTrue(CoordinateSystem3D.XYZ_LEFT_HAND.isZOnUp());
		assertFalse(CoordinateSystem3D.XZY_RIGHT_HAND.isZOnUp());
		assertFalse(CoordinateSystem3D.XZY_LEFT_HAND.isZOnUp());
	}

	@Test
	public void isYOnUp() {
		assertFalse(CoordinateSystem3D.XYZ_RIGHT_HAND.isYOnUp());
		assertFalse(CoordinateSystem3D.XYZ_LEFT_HAND.isYOnUp());
		assertTrue(CoordinateSystem3D.XZY_RIGHT_HAND.isYOnUp());
		assertTrue(CoordinateSystem3D.XZY_LEFT_HAND.isYOnUp());
	}

	@Test
	public void isZOnSide() {
		assertFalse(CoordinateSystem3D.XYZ_RIGHT_HAND.isZOnSide());
		assertFalse(CoordinateSystem3D.XYZ_LEFT_HAND.isZOnSide());
		assertTrue(CoordinateSystem3D.XZY_RIGHT_HAND.isZOnSide());
		assertTrue(CoordinateSystem3D.XZY_LEFT_HAND.isZOnSide());
	}

	@Test
	public void isYOnSide() {
		assertTrue(CoordinateSystem3D.XYZ_RIGHT_HAND.isYOnSide());
		assertTrue(CoordinateSystem3D.XYZ_LEFT_HAND.isYOnSide());
		assertFalse(CoordinateSystem3D.XZY_RIGHT_HAND.isYOnSide());
		assertFalse(CoordinateSystem3D.XZY_LEFT_HAND.isYOnSide());
	}

	@Test
	public void getDefaultCoordinateSystem() {
		assertTrue(CoordinateSystem3D.getDefaultCoordinateSystem().isRightHanded());
	}

	@Test
	public void toCoordinateSystem2D() {
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D());
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D());
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D());
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D());
	}

	@DisplayName("fromVectors(double,double) with invalid params")
	@Test
	public void fromVectorsDoubleDouble_invalidParameter() {
		assertThrows(AssertionError.class, () -> CoordinateSystem3D.fromVectors(0., 0.));
	}

	@DisplayName("fromVectors(double,double)")
	@Test
	public void fromVectorsDoubleDouble() {
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(1., Double.NaN));
		assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(-1., Double.NaN));
		assertSame(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(0., 1.));
		assertSame(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(0., -1.));
	}

	@DisplayName("fromVectors(int,int) with invalid params")
	@Test
	public void fromVectorsIntInt_invalidParameter() {
		assertThrows(AssertionError.class, () -> CoordinateSystem3D.fromVectors(0, 0));
	}

	@DisplayName("fromVectors(int,int)")
	@Test
	public void fromVectorsIntInt() {
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.fromVectors(1, 0));
		assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.fromVectors(-1, 0));
		assertSame(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.fromVectors(0, 1));
		assertSame(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.fromVectors(0, -1));
	}

	private void doTestToSystem(CoordinateSystem3D input, CoordinateSystem3D output, Point3d[] points) {
		// Even index: points to convert
		// Odd index: expected result of the conversion for the previous even index point
		Point3d p;
		for (int i = 0; i < points.length; i += 2) {
			p = points[i].clone();
			input.toSystem(p, output);
			assertEpsilonEquals(points[i + 1], p, "index=" + i + "; Original point=" + points[i]);
		}		
	}

	@DisplayName("toSystem(Tuple3D): XYZ-LH => XYZ-LH")
	@Test
	public void toSystemTuple3D_XYZL_XYZL() {
		doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 78, 36),
						new Point3d(45, -78, 36), new Point3d(45, -78, 36),
						new Point3d(-45, -78, 36), new Point3d(-45, -78, 36),
						new Point3d(45, 78, 36), new Point3d(45, 78, 36),
						new Point3d(-45, 78, -36), new Point3d(-45, 78, -36),
						new Point3d(45, -78, -36), new Point3d(45, -78, -36),
						new Point3d(-45, -78, -36), new Point3d(-45, -78, -36),
						new Point3d(45, 78, -36), new Point3d(45, 78, -36),
		});
	}

	@DisplayName("toSystem(Tuple3D): XYZ-LH => XYZ-RH")
	@Test
	public void toSystemTuple3D_XYZL_XYZR() {
		doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, -78, 36),
						new Point3d(45, -78, 36), new Point3d(45, 78, 36),
						new Point3d(-45, -78, 36), new Point3d(-45, 78, 36),
						new Point3d(45, 78, 36), new Point3d(45, -78, 36),
						new Point3d(-45, 78, -36), new Point3d(-45, -78, -36),
						new Point3d(45, -78, -36), new Point3d(45, 78, -36),
						new Point3d(-45, -78, -36), new Point3d(-45, 78, -36),
						new Point3d(45, 78, -36), new Point3d(45, -78, -36),
		});
	}

	@DisplayName("toSystem(Tuple3D): XYZ-LH => XZY-LH")
	@Test
	public void toSystemTuple3D_XYZL_XZYL() {
		doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 36, -78),
						new Point3d(45, -78, 36), new Point3d(45, 36, 78),
						new Point3d(-45, -78, 36), new Point3d(-45, 36, 78),
						new Point3d(45, 78, 36), new Point3d(45, 36, -78),
						new Point3d(-45, 78, -36), new Point3d(-45, -36, -78),
						new Point3d(45, -78, -36), new Point3d(45, -36, 78),
						new Point3d(-45, -78, -36), new Point3d(-45, -36, 78),
						new Point3d(45, 78, -36), new Point3d(45, -36, -78),
		});
	}

	@DisplayName("toSystem(Tuple3D): XYZ-LH => XZY-RH")
	@Test
	public void toSystemTuple3D_XYZL_XZYR() {
		doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 36, 78),
						new Point3d(45, -78, 36), new Point3d(45, 36, -78),
						new Point3d(-45, -78, 36), new Point3d(-45, 36, -78),
						new Point3d(45, 78, 36), new Point3d(45, 36, 78),
						new Point3d(-45, 78, -36), new Point3d(-45, -36, 78),
						new Point3d(45, -78, -36), new Point3d(45, -36, -78),
						new Point3d(-45, -78, -36), new Point3d(-45, -36, -78),
						new Point3d(45, 78, -36), new Point3d(45, -36, 78),
		});
	}

	@DisplayName("toSystem(Tuple3D): XYZ-RH => XYZ-RH")
	@Test
	public void toSystemTuple3D_XYZR_XYZR() {
		doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 78, 36),
						new Point3d(45, -78, 36), new Point3d(45, -78, 36),
						new Point3d(-45, -78, 36), new Point3d(-45, -78, 36),
						new Point3d(45, 78, 36), new Point3d(45, 78, 36),
						new Point3d(-45, 78, -36), new Point3d(-45, 78, -36),
						new Point3d(45, -78, -36), new Point3d(45, -78, -36),
						new Point3d(-45, -78, -36), new Point3d(-45, -78, -36),
						new Point3d(45, 78, -36), new Point3d(45, 78, -36),
		});
	}

	@DisplayName("toSystem(Tuple3D): XYZ-RH => XYZ-LH")
	@Test
	public void toSystemTuple3D_XYZR_XYZL() {
		doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, -78, 36),
						new Point3d(45, -78, 36), new Point3d(45, 78, 36),
						new Point3d(-45, -78, 36), new Point3d(-45, 78, 36),
						new Point3d(45, 78, 36), new Point3d(45, -78, 36),
						new Point3d(-45, 78, -36), new Point3d(-45, -78, -36),
						new Point3d(45, -78, -36), new Point3d(45, 78, -36),
						new Point3d(-45, -78, -36), new Point3d(-45, 78, -36),
						new Point3d(45, 78, -36), new Point3d(45, -78, -36),
		});
	}

	@DisplayName("toSystem(Tuple3D): XYZ-RH => XZY-LH")
	@Test
	public void toSystemTuple3D_XYZR_XZYR() {
		doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 36, -78),
						new Point3d(45, -78, 36), new Point3d(45, 36, 78),
						new Point3d(-45, -78, 36), new Point3d(-45, 36, 78),
						new Point3d(45, 78, 36), new Point3d(45, 36, -78),
						new Point3d(-45, 78, -36), new Point3d(-45, -36, -78),
						new Point3d(45, -78, -36), new Point3d(45, -36, 78),
						new Point3d(-45, -78, -36), new Point3d(-45, -36, 78),
						new Point3d(45, 78, -36), new Point3d(45, -36, -78),
		});
	}

	@DisplayName("toSystem(Tuple3D): XYZ-RH => XZY-LH")
	@Test
	public void toSystemTuple3D_XYZR_XZYL() {
		doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 36, 78),
						new Point3d(45, -78, 36), new Point3d(45, 36, -78),
						new Point3d(-45, -78, 36), new Point3d(-45, 36, -78),
						new Point3d(45, 78, 36), new Point3d(45, 36, 78),
						new Point3d(-45, 78, -36), new Point3d(-45, -36, 78),
						new Point3d(45, -78, -36), new Point3d(45, -36, -78),
						new Point3d(-45, -78, -36), new Point3d(-45, -36, -78),
						new Point3d(45, 78, -36), new Point3d(45, -36, 78),
		});
	}

	@DisplayName("toSystem(Tuple3D): XZY-LH => XZY-LH")
	@Test
	public void toSystemTuple3D_XZYL_XZYL() {
		doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 78, 36),
						new Point3d(45, -78, 36), new Point3d(45, -78, 36),
						new Point3d(-45, -78, 36), new Point3d(-45, -78, 36),
						new Point3d(45, 78, 36), new Point3d(45, 78, 36),
						new Point3d(-45, 78, -36), new Point3d(-45, 78, -36),
						new Point3d(45, -78, -36), new Point3d(45, -78, -36),
						new Point3d(-45, -78, -36), new Point3d(-45, -78, -36),
						new Point3d(45, 78, -36), new Point3d(45, 78, -36),
		});
	}

	@DisplayName("toSystem(Tuple3D): XZY-LH => XZY-RH")
	@Test
	public void toSystemTuple3D_XZYL_XZYR() {
		doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, -78, 36),
						new Point3d(45, -78, 36), new Point3d(45, 78, 36),
						new Point3d(-45, -78, 36), new Point3d(-45, 78, 36),
						new Point3d(45, 78, 36), new Point3d(45, -78, 36),
						new Point3d(-45, 78, -36), new Point3d(-45, -78, -36),
						new Point3d(45, -78, -36), new Point3d(45, 78, -36),
						new Point3d(-45, -78, -36), new Point3d(-45, 78, -36),
						new Point3d(45, 78, -36), new Point3d(45, -78, -36),
		});
	}

	@DisplayName("toSystem(Tuple3D): XZY-LH => XYZ-LH")
	@Test
	public void toSystemTuple3D_XZYL_XYZL() {
		doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 36, -78),
						new Point3d(45, -78, 36), new Point3d(45, 36, 78),
						new Point3d(-45, -78, 36), new Point3d(-45, 36, 78),
						new Point3d(45, 78, 36), new Point3d(45, 36, -78),
						new Point3d(-45, 78, -36), new Point3d(-45, -36, -78),
						new Point3d(45, -78, -36), new Point3d(45, -36, 78),
						new Point3d(-45, -78, -36), new Point3d(-45, -36, 78),
						new Point3d(45, 78, -36), new Point3d(45, -36, -78),
		});
	}

	@DisplayName("toSystem(Tuple3D): XZY-LH => XXY-RH")
	@Test
	public void toSystemTuple3D_XZYL_XYZR() {
		doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 36, 78),
						new Point3d(45, -78, 36), new Point3d(45, 36, -78),
						new Point3d(-45, -78, 36), new Point3d(-45, 36, -78),
						new Point3d(45, 78, 36), new Point3d(45, 36, 78),
						new Point3d(-45, 78, -36), new Point3d(-45, -36, 78),
						new Point3d(45, -78, -36), new Point3d(45, -36, -78),
						new Point3d(-45, -78, -36), new Point3d(-45, -36, -78),
						new Point3d(45, 78, -36), new Point3d(45, -36, 78),
		});
	}

	@DisplayName("toSystem(Tuple3D): XZY-RH => XZY-RH")
	@Test
	public void toSystemTuple3D_XZYR_XZYL() {
		doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 78, 36),
						new Point3d(45, -78, 36), new Point3d(45, -78, 36),
						new Point3d(-45, -78, 36), new Point3d(-45, -78, 36),
						new Point3d(45, 78, 36), new Point3d(45, 78, 36),
						new Point3d(-45, 78, -36), new Point3d(-45, 78, -36),
						new Point3d(45, -78, -36), new Point3d(45, -78, -36),
						new Point3d(-45, -78, -36), new Point3d(-45, -78, -36),
						new Point3d(45, 78, -36), new Point3d(45, 78, -36),
		});
	}

	@DisplayName("toSystem(Tuple3D): XZY-RH => XZY-LH")
	@Test
	public void toSystemTuple3D_XZYR_XZYR() {
		doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, -78, 36),
						new Point3d(45, -78, 36), new Point3d(45, 78, 36),
						new Point3d(-45, -78, 36), new Point3d(-45, 78, 36),
						new Point3d(45, 78, 36), new Point3d(45, -78, 36),
						new Point3d(-45, 78, -36), new Point3d(-45, -78, -36),
						new Point3d(45, -78, -36), new Point3d(45, 78, -36),
						new Point3d(-45, -78, -36), new Point3d(-45, 78, -36),
						new Point3d(45, 78, -36), new Point3d(45, -78, -36),
		});
	}

	@DisplayName("toSystem(Tuple3D): XZY-RH => XYZ-RH")
	@Test
	public void toSystemTuple3D_XZYR_XYZL() {
		doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 36, -78),
						new Point3d(45, -78, 36), new Point3d(45, 36, 78),
						new Point3d(-45, -78, 36), new Point3d(-45, 36, 78),
						new Point3d(45, 78, 36), new Point3d(45, 36, -78),
						new Point3d(-45, 78, -36), new Point3d(-45, -36, -78),
						new Point3d(45, -78, -36), new Point3d(45, -36, 78),
						new Point3d(-45, -78, -36), new Point3d(-45, -36, 78),
						new Point3d(45, 78, -36), new Point3d(45, -36, -78),
		});
	}

	@DisplayName("toSystem(Tuple3D): XZY-RH => XZY-LH")
	@Test
	public void toSystemTuple3D_XZYR_XYZR() {
		doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, 36, 78),
						new Point3d(45, -78, 36), new Point3d(45, 36, -78),
						new Point3d(-45, -78, 36), new Point3d(-45, 36, -78),
						new Point3d(45, 78, 36), new Point3d(45, 36, 78),
						new Point3d(-45, 78, -36), new Point3d(-45, -36, 78),
						new Point3d(45, -78, -36), new Point3d(45, -36, -78),
						new Point3d(-45, -78, -36), new Point3d(-45, -36, -78),
						new Point3d(45, 78, -36), new Point3d(45, -36, 78),
		});
	}

	private void doTestToSystem(CoordinateSystem3D input, CoordinateSystem3D output, Quaternion4d[] rotations) {
		// Even index: points to convert
		// Odd index: expected result of the conversion for the previous even index point
		Quaternion4d q;
		for (int i = 0; i < rotations.length; i += 2) {
			q = rotations[i].clone();
			input.toSystem(q, output);
			assertEpsilonEquals(rotations[i + 1], q, "index=" + i + "; Original quaternion=" + rotations[i]);
		}		
	}

	private static Quaternion4d newAxisAngle(double x, double y, double z, double angle) {
		final Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(x, y, z, angle);
		return q;
	}

	@DisplayName("toSystem(Quaternion): XYZ-LH => XYZ-LH")
	@Test
	public void toSystemQuaternion_XYZL_XYZL() {
		doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, 36, 12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, 36, -12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, -36, 12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, -36, -12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, 36, 12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, 36, -12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, -36, 12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, -36, -12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, 36, 12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, 36, -12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, -36, 12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, -36, -12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, 36, 12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, 36, -12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, -36, 12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, -36, -12),
		});
	}

	@DisplayName("toSystem(Quaternion): XYZ-LH => XYZ-RH")
	@Test
	public void toSystemQuaternion_XYZL_XYZR() {
		doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, -78, 36, -12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, -78, 36, 12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -78, -36, -12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -78, -36, 12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 78, 36, -12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 78, 36, 12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, 78, -36, -12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, 78, -36, 12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, -78, 36, -12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, -78, 36, 12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -78, -36, -12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -78, -36, 12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 78, 36, -12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 78, 36, 12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, 78, -36, -12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, 78, -36, 12),
		});
	}

	@DisplayName("toSystem(Quaternion): XYZ-LH => XZY-LH")
	@Test
	public void toSystemQuaternion_XYZL_XZYL() {
		doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, -78, -12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, -78, 12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, -78, -12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, -78, 12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, 78, -12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, 78, 12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, 78, -12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, 78, 12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, -78, -12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, -78, 12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, -78, -12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, -78, 12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, 78, -12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, 78, 12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, 78, -12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, 78, 12),
		});
	}

	@DisplayName("toSystem(Quaternion): XYZ-LH => XZY-RH")
	@Test
	public void toSystemQuaternion_XYZL_XZYR() {
		doTestToSystem(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, 78, 12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, 78, -12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, 78, 12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, 78, -12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, -78, 12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, -78, -12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, -78, 12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, -78, -12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, 78, 12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, 78, -12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, 78, 12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, 78, -12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, -78, 12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, -78, -12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, -78, 12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, -78, -12),
		});
	}

	@DisplayName("toSystem(Quaternion): XYZ-RH => XYZ-RH")
	@Test
	public void toSystemQuaternion_XYZR_XYZR() {
		doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, 36, 12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, 36, -12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, -36, 12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, -36, -12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, 36, 12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, 36, -12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, -36, 12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, -36, -12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, 36, 12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, 36, -12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, -36, 12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, -36, -12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, 36, 12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, 36, -12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, -36, 12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, -36, -12),
		});
	}

	@DisplayName("toSystem(Quaternion): XYZ-RH => XYZ-LH")
	@Test
	public void toSystemQuaternion_XYZR_XYZL() {
		doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, -78, 36, -12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, -78, 36, 12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -78, -36, -12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -78, -36, 12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 78, 36, -12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 78, 36, 12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, 78, -36, -12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, 78, -36, 12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, -78, 36, -12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, -78, 36, 12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -78, -36, -12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -78, -36, 12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 78, 36, -12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 78, 36, 12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, 78, -36, -12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, 78, -36, 12),
		});
	}

	@DisplayName("toSystem(Quaternion): XYZ-RH => XZY-RH")
	@Test
	public void toSystemQuaternion_XYZR_XZYR() {
		doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, -78, -12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, -78, 12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, -78, -12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, -78, 12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, 78, -12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, 78, 12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, 78, -12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, 78, 12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, -78, -12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, -78, 12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, -78, -12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, -78, 12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, 78, -12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, 78, 12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, 78, -12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, 78, 12),
		});
	}

	@DisplayName("toSystem(Quaternion): XYZ-RH => XZY-LH")
	@Test
	public void toSystemQuaternion_XYZR_XZYL() {
		doTestToSystem(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, 78, 12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, 78, -12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, 78, 12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, 78, -12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, -78, 12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, -78, -12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, -78, 12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, -78, -12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, 78, 12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, 78, -12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, 78, 12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, 78, -12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, -78, 12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, -78, -12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, -78, 12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, -78, -12),
		});
	}

	@DisplayName("toSystem(Quaternion): XZY-LH => XZY-LH")
	@Test
	public void toSystemQuaternion_XZYL_XZYL() {
		doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, 36, 12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, 36, -12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, -36, 12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, -36, -12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, 36, 12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, 36, -12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, -36, 12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, -36, -12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, 36, 12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, 36, -12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, -36, 12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, -36, -12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, 36, 12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, 36, -12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, -36, 12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, -36, -12),
		});
	}

	@DisplayName("toSystem(Quaternion): XZY-LH => XZY-RH")
	@Test
	public void toSystemQuaternion_XZYL_XZYR() {
		doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, -36, -12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, -36, 12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, 36, -12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, 36, 12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, -36, -12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, -36, 12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, 36, -12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, 36, 12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, -36, -12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, -36, 12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, 36, -12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, 36, 12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, -36, -12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, -36, 12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, 36, -12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, 36, 12),
		});
	}

	@DisplayName("toSystem(Quaternion): XZY-LH => XYZ-LH")
	@Test
	public void toSystemQuaternion_XZYL_XYZL() {
		doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, -36, 78, -12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, -36, 78, 12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 36, 78, -12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 36, 78, 12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -36, -78, -12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -36, -78, 12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, 36, -78, -12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, 36, -78, 12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, -36, 78, -12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, -36, 78, 12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 36, 78, -12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 36, 78, 12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -36, -78, -12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -36, -78, 12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, 36, -78, -12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, 36, -78, 12),
		});
	}

	@DisplayName("toSystem(Quaternion): XZY-LH => XYZ-RH")
	@Test
	public void toSystemQuaternion_XZYL_XYZR() {
		doTestToSystem(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, 78, 12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, 78, -12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, 78, 12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, 78, -12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, -78, 12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, -78, -12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, -78, 12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, -78, -12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, 78, 12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, 78, -12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, 78, 12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, 78, -12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, -78, 12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, -78, -12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, -78, 12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, -78, -12),
		});
	}

	@DisplayName("toSystem(Quaternion): XZY-RH => XZY-RH")
	@Test
	public void toSystemQuaternion_XZYR_XZYR() {
		doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_RIGHT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, 36, 12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, 36, -12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, -36, 12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, -36, -12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, 36, 12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, 36, -12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, -36, 12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, -36, -12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, 36, 12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, 36, -12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, -36, 12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, -36, -12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, 36, 12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, 36, -12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, -36, 12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, -36, -12),
		});
	}

	@DisplayName("toSystem(Quaternion): XZY-RH => XZY-LH")
	@Test
	public void toSystemQuaternion_XZYR_XZYL() {
		doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XZY_LEFT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 78, -36, -12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 78, -36, 12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 78, 36, -12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 78, 36, 12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -78, -36, -12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -78, -36, 12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -78, 36, -12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -78, 36, 12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 78, -36, -12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 78, -36, 12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 78, 36, -12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 78, 36, 12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -78, -36, -12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -78, -36, 12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -78, 36, -12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -78, 36, 12),
		});
	}

	@DisplayName("toSystem(Quaternion): XZY-RH => XYZ-RH")
	@Test
	public void toSystemQuaternion_XZYR_XYZR() {
		doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_RIGHT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, -36, 78, -12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, -36, 78, 12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, 36, 78, -12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, 36, 78, 12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, -36, -78, -12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, -36, -78, 12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, 36, -78, -12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, 36, -78, 12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, -36, 78, -12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, -36, 78, 12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, 36, 78, -12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, 36, 78, 12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, -36, -78, -12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, -36, -78, 12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, 36, -78, -12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, 36, -78, 12),
		});
	}

	@DisplayName("toSystem(Quaternion): XZY-RH => XYZ-LH")
	@Test
	public void toSystemQuaternion_XZYR_XYZL() {
		doTestToSystem(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.XYZ_LEFT_HAND,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, 78, 12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, 78, -12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, 78, 12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, 78, -12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, -78, 12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, -78, -12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, -78, 12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, -78, -12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, 78, 12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, 78, -12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, 78, 12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, 78, -12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, -78, 12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, -78, -12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, -78, 12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, -78, -12),
		});
	}

	private void doTestToSystem(CoordinateSystem3D input, CoordinateSystem3D output, Quaternion4d[] rotations, Point3d[] translations) {
		// Even index: points to convert
		// Odd index: expected result of the conversion for the previous even index point
		for (int i = 0; i < rotations.length; ++i) {
			final Quaternion4d q = rotations[i].clone();
			final Quaternion4d qt = q.clone();
			input.toSystem(qt, output);

			for (int j = 0; j < translations.length; ++j) {
				final Point3d p = translations[j].clone();
				final Point3d pt = p.clone();
				input.toSystem(pt, output);

				Transform3D transform = new Transform3D();
				transform.setTranslation(p);
				transform.setRotation(q);
				input.toSystem(transform, output);

				Transform3D transformt = new Transform3D();
				transformt.setTranslation(pt);
				transformt.setRotation(qt);

				assertEpsilonEquals(transformt, transform, () -> "Original quaternion=" + q + "; Original vector=" + p);
			}
		}		
	}

	private static Stream<Arguments> providePairsOfCoordinateSystem3D() {
		final List<Arguments> arguments = new ArrayList<>();
		for (final CoordinateSystem3D s1 : CoordinateSystem3D.values()) {
			for (final CoordinateSystem3D s2 : CoordinateSystem3D.values()) {
				arguments.add(Arguments.of(s1, s2));
			}
		}
		return arguments.stream();
	}

	@DisplayName("toSystem(Transform3D)")
	@ParameterizedTest(name = "{index}: {0} => {1}")
	@MethodSource("providePairsOfCoordinateSystem3D")
	public void toSystemTransform3D(CoordinateSystem3D csIn, CoordinateSystem3D csOut) {
		doTestToSystem(csIn, csOut,
				new Quaternion4d[] {
						newAxisAngle(45, 78, 36, 12), newAxisAngle(45, 36, 78, 12),
						newAxisAngle(45, 78, 36, -12), newAxisAngle(45, 36, 78, -12),
						newAxisAngle(45, 78, -36, 12), newAxisAngle(45, -36, 78, 12),
						newAxisAngle(45, 78, -36, -12), newAxisAngle(45, -36, 78, -12),
						newAxisAngle(45, -78, 36, 12), newAxisAngle(45, 36, -78, 12),
						newAxisAngle(45, -78, 36, -12), newAxisAngle(45, 36, -78, -12),
						newAxisAngle(45, -78, -36, 12), newAxisAngle(45, -36, -78, 12),
						newAxisAngle(45, -78, -36, -12), newAxisAngle(45, -36, -78, -12),
						newAxisAngle(-45, 78, 36, 12), newAxisAngle(-45, 36, 78, 12),
						newAxisAngle(-45, 78, 36, -12), newAxisAngle(-45, 36, 78, -12),
						newAxisAngle(-45, 78, -36, 12), newAxisAngle(-45, -36, 78, 12),
						newAxisAngle(-45, 78, -36, -12), newAxisAngle(-45, -36, 78, -12),
						newAxisAngle(-45, -78, 36, 12), newAxisAngle(-45, 36, -78, 12),
						newAxisAngle(-45, -78, 36, -12), newAxisAngle(-45, 36, -78, -12),
						newAxisAngle(-45, -78, -36, 12), newAxisAngle(-45, -36, -78, 12),
						newAxisAngle(-45, -78, -36, -12), newAxisAngle(-45, -36, -78, -12),
		},
				new Point3d[] {
						new Point3d(-45, 78, 36), new Point3d(-45, -78, 36),
						new Point3d(45, -78, 36), new Point3d(45, 78, 36),
						new Point3d(-45, -78, 36), new Point3d(-45, 78, 36),
						new Point3d(45, 78, 36), new Point3d(45, -78, 36),
						new Point3d(-45, 78, -36), new Point3d(-45, -78, -36),
						new Point3d(45, -78, -36), new Point3d(45, 78, -36),
						new Point3d(-45, -78, -36), new Point3d(-45, 78, -36),
						new Point3d(45, 78, -36), new Point3d(45, -78, -36),
		});
	}

	@DisplayName("toCoordinateSystem2D(Tuple3D,Tuple2D)")
	@Test
	public void toCoordinateSystem2DTuple3DTuple2D() {
		Point3d in = new Point3d(459, 658, 145);
		Point2d out = new Point2d();

		CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(in, out);
		assertEpsilonEquals(459, out.getX());
		assertEpsilonEquals(658, out.getY());

		out.set(0, 0);
		CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(in, out);
		assertEpsilonEquals(459, out.getX());
		assertEpsilonEquals(658, out.getY());

		out.set(0, 0);
		CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(in, out);
		assertEpsilonEquals(459, out.getX());
		assertEpsilonEquals(145, out.getY());

		out.set(0, 0);
		CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(in, out);
		assertEpsilonEquals(459, out.getX());
		assertEpsilonEquals(145, out.getY());
	}

	@DisplayName("toCoordinateSystem2D(Quaternion4d)")
	@Test
	public void toCoordinateSystem2DQuaternion4d() {
		Quaternion4d in = new Quaternion4d();
		in.setAxisAngle(1, 1, 1, MathConstants.QUARTER_PI);

		assertEpsilonEquals(.5612027282726586, CoordinateSystem3D.XYZ_LEFT_HAND.toCoordinateSystem2D(in));

		assertEpsilonEquals(.5612027282726586, CoordinateSystem3D.XYZ_RIGHT_HAND.toCoordinateSystem2D(in));

		assertEpsilonEquals(.36836692032349705, CoordinateSystem3D.XZY_LEFT_HAND.toCoordinateSystem2D(in));

		assertEpsilonEquals(.36836692032349705, CoordinateSystem3D.XZY_RIGHT_HAND.toCoordinateSystem2D(in));
	}

	@DisplayName("fromCoordinateSystem2D(Tuple2D,Tuple3D)")
	@Test
	public void toCoordinateSystem2DTuple2DTuple3D() {
		Point2d in = new Point2d(459, 658);
		Point3d out = new Point3d();

		CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(in, 145, out);
		assertEpsilonEquals(459, out.getX());
		assertEpsilonEquals(658, out.getY());
		assertEpsilonEquals(145, out.getZ());

		out.set(0, 0, 0);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(in, 145, out);
		assertEpsilonEquals(459, out.getX());
		assertEpsilonEquals(658, out.getY());
		assertEpsilonEquals(145, out.getZ());

		out.set(0, 0, 0);
		CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(in, 145, out);
		assertEpsilonEquals(459, out.getX());
		assertEpsilonEquals(145, out.getY());
		assertEpsilonEquals(658, out.getZ());

		out.set(0, 0, 0);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(in, 145, out);
		assertEpsilonEquals(459, out.getX());
		assertEpsilonEquals(145, out.getY());
		assertEpsilonEquals(658, out.getZ());
	}

	@DisplayName("fromCoordinateSystem2D(double,Quaternion4d)")
	@Test
	public void toCoordinateSystem2DDoubleQuaternion4d() {
		Point2d in = new Point2d(459, 658);
		Quaternion4d out = new Quaternion4d();

		CoordinateSystem3D.XYZ_LEFT_HAND.fromCoordinateSystem2D(MathConstants.QUARTER_PI, out);
		assertEpsilonEquals(0, out.getX());
		assertEpsilonEquals(0, out.getY());
		assertEpsilonEquals(0.382683432, out.getZ());
		assertEpsilonEquals(0.9238795, out.getW());

		out.set(0, 0, 0, 0);
		CoordinateSystem3D.XYZ_RIGHT_HAND.fromCoordinateSystem2D(MathConstants.QUARTER_PI, out);
		assertEpsilonEquals(0, out.getX());
		assertEpsilonEquals(0, out.getY());
		assertEpsilonEquals(0.382683432, out.getZ());
		assertEpsilonEquals(0.9238795, out.getW());

		out.set(0, 0, 0, 0);
		CoordinateSystem3D.XZY_LEFT_HAND.fromCoordinateSystem2D(MathConstants.QUARTER_PI, out);
		assertEpsilonEquals(0, out.getX());
		assertEpsilonEquals(0.382683432, out.getY());
		assertEpsilonEquals(0, out.getZ());
		assertEpsilonEquals(0.9238795, out.getW());

		out.set(0, 0, 0, 0);
		CoordinateSystem3D.XZY_RIGHT_HAND.fromCoordinateSystem2D(MathConstants.QUARTER_PI, out);
		assertEpsilonEquals(0, out.getX());
		assertEpsilonEquals(0.382683432, out.getY());
		assertEpsilonEquals(0, out.getZ());
		assertEpsilonEquals(0.9238795, out.getW());
	}

	@DisplayName("getSideCoordinate(double,double,double)")
	@Test
	public void getSideCoordinateDoubleDoubleDouble() {
		assertEpsilonEquals(658, CoordinateSystem3D.XYZ_LEFT_HAND.getSideCoordinate(459, 658, 149));
		assertEpsilonEquals(658, CoordinateSystem3D.XYZ_RIGHT_HAND.getSideCoordinate(459, 658, 149));
		assertEpsilonEquals(149, CoordinateSystem3D.XZY_LEFT_HAND.getSideCoordinate(459, 658, 149));
		assertEpsilonEquals(149, CoordinateSystem3D.XZY_RIGHT_HAND.getSideCoordinate(459, 658, 149));
	}

	@DisplayName("getVerticalCoordinate(double,double,double)")
	@Test
	public void getVerticalCoordinateDoubleDoubleDouble() {
		assertEpsilonEquals(149, CoordinateSystem3D.XYZ_LEFT_HAND.getVerticalCoordinate(459, 658, 149));
		assertEpsilonEquals(149, CoordinateSystem3D.XYZ_RIGHT_HAND.getVerticalCoordinate(459, 658, 149));
		assertEpsilonEquals(658, CoordinateSystem3D.XZY_LEFT_HAND.getVerticalCoordinate(459, 658, 149));
		assertEpsilonEquals(658, CoordinateSystem3D.XZY_RIGHT_HAND.getVerticalCoordinate(459, 658, 149));
	}

	@DisplayName("setSideCoordinate(Tuple3D,double)")
	@Test
	public void setSideCoordinateTuple3DDouble() {
		Point3d p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XYZ_LEFT_HAND.setSideCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(459, p.getY());
		assertEpsilonEquals(9875, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setSideCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(459, p.getY());
		assertEpsilonEquals(9875, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XZY_LEFT_HAND.setSideCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1456, p.getY());
		assertEpsilonEquals(459, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XZY_RIGHT_HAND.setSideCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1456, p.getY());
		assertEpsilonEquals(459, p.getZ());
	}

	@DisplayName("setVerticalCoordinate(Tuple3D,double)")
	@Test
	public void setVerticalCoordinateTuple3DDouble() {
		Point3d p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XYZ_LEFT_HAND.setVerticalCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1456, p.getY());
		assertEpsilonEquals(459, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XYZ_RIGHT_HAND.setVerticalCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1456, p.getY());
		assertEpsilonEquals(459, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XZY_LEFT_HAND.setVerticalCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(459, p.getY());
		assertEpsilonEquals(9875, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XZY_RIGHT_HAND.setVerticalCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(459, p.getY());
		assertEpsilonEquals(9875, p.getZ());
	}

	@DisplayName("addSideCoordinate(Tuple3D,double)")
	@Test
	public void addSideCoordinateTuple3DDouble() {
		Point3d p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XYZ_LEFT_HAND.addSideCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1915, p.getY());
		assertEpsilonEquals(9875, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addSideCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1915, p.getY());
		assertEpsilonEquals(9875, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XZY_LEFT_HAND.addSideCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1456, p.getY());
		assertEpsilonEquals(10334, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XZY_RIGHT_HAND.addSideCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1456, p.getY());
		assertEpsilonEquals(10334, p.getZ());
	}

	@DisplayName("addVerticalCoordinate(Tuple3D,double)")
	@Test
	public void addVerticalCoordinateTuple3DDouble() {
		Point3d p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XYZ_LEFT_HAND.addVerticalCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1456, p.getY());
		assertEpsilonEquals(10334, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XYZ_RIGHT_HAND.addVerticalCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1456, p.getY());
		assertEpsilonEquals(10334, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XZY_LEFT_HAND.addVerticalCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1915, p.getY());
		assertEpsilonEquals(9875, p.getZ());

		p = new Point3d(1236, 1456, 9875);
		CoordinateSystem3D.XZY_RIGHT_HAND.addVerticalCoordinate(p, 459);
		assertEpsilonEquals(1236, p.getX());
		assertEpsilonEquals(1915, p.getY());
		assertEpsilonEquals(9875, p.getZ());
	}

}

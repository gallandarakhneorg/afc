/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d3.tests.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Capsule3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractCapsule3dTestCase<T extends Capsule3afp<T, ?, ?, ?, ?, B>,
		B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractBox3dTestCase<T, B> {

	@Override
	protected final T createShape() {
		return (T) createCapsule(5, 8, 9, 1, 2, 3, 5);
	}

	@DisplayName("inflate")
	@Nested
	public class Inflate {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

	}

	@DisplayName("getType")
	@Nested
	public class GetType {

		@DisplayName("(Class) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void type_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(Shape3DType.CAPSULE, getS().getType(Shape3DType.class));
		}
	
		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(Shape3DType.CAPSULE, getS().getType());
		}

	}

	@DisplayName("getCenterX")
	@Nested
	public class GetCenterX {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(5., getS().getCenterX());
		}

	}

	@DisplayName("getCenterY")
	@Nested
	public class GetCenterY {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(8., getS().getCenterY());
		}

	}

	@DisplayName("getCenterZ")
	@Nested
	public class GetCenterZ {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(9., getS().getCenterZ());
		}

	}

	@DisplayName("setWidth")
	@Nested
	public class SetWidth {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setWidth(123.456);
			//			assertEpsilonEquals(61.728, getS().getX());
			//			assertEpsilonEquals(8, getS().getY());
			//			assertEpsilonEquals(9, getS().getZ());
			//			assertEpsilonEquals(61.728, getS().getRadius());
			fail("Todo");
		}

	}

	@DisplayName("setHeight")
	@Nested
	public class SetHeight {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setHeight(123.456);
			//			assertEpsilonEquals(5, getS().getX());
			//			assertEpsilonEquals(64.728, getS().getY());
			//			assertEpsilonEquals(9, getS().getZ());
			//			assertEpsilonEquals(61.728, getS().getRadius());
			fail("Todo");
		}

	}

	@DisplayName("setDepth")
	@Nested
	public class SetDepth {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().setDepth(123.456);
			//			assertEpsilonEquals(5, getS().getX());
			//			assertEpsilonEquals(8, getS().getY());
			//			assertEpsilonEquals(65.728, getS().getZ());
			//			assertEpsilonEquals(61.728, getS().getRadius());
			fail("Todo");
		}

	}

	@DisplayName("getWidth")
	@Nested
	public class GetWidth {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10, getS().getWidth());
		}

	}

	@DisplayName("getHeight")
	@Nested
	public class Geteight {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10, getS().getHeight());
		}

	}

	@DisplayName("getDepth")
	@Nested
	public class GetDepth {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(10, getS().getDepth());
		}

	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			T clone = getS().clone();
			//			assertNotNull(clone);
			//			assertNotSame(getS(), clone);
			//			assertEquals(getS().getClass(), clone.getClass());
			//			assertEpsilonEquals(5, clone.getX());
			//			assertEpsilonEquals(8, clone.getY());
			//			assertEpsilonEquals(9, clone.getZ());
			//			assertEpsilonEquals(5, clone.getRadius());
			fail("Todo");
		}

	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsObject {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(new Object()));
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_3(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equals(createSphere(0, 0, 0, 5)));
			fail("Todo");
		}

	}

	@DisplayName("equalsToShape")
	@Nested
	public class EqualsToShape {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertFalse(getS().equalsToShape(null));
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			// assertFalse(getS().equalsToShape((T) createSphere(0, 0, 0, 5)));
			fail("Todo");
		}

	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			getS().clear();
			//			assertEpsilonEquals(0, getS().getX());
			//			assertEpsilonEquals(0, getS().getY());
			//			assertEpsilonEquals(0, getS().getZ());
			//			assertEpsilonEquals(0, getS().getRadius());
			fail("Todo");
		}

	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

	}

	@DisplayName("getClosestPointTo")
	@Nested
	public class GetClosestPointTo {
		
		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
		
		@DisplayName("(Capsule3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
	}

	@DisplayName("getFarthestPointTo")
	@Nested
	public class GetFarthestPointTo {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

	}

	@DisplayName("getDistance")
	@Nested
	public class GetDistance {

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
		
		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
	}

	@DisplayName("getDistanceSquared")
	@Nested
	public class GetDistanceSquared {

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Shape3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

	}

	@DisplayName("getDistanceL1")
	@Nested
	public class GetDistanceL1 {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

	}

	@DisplayName("getDistanceLinf")
	@Nested
	public class GetDistanceLinf {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

	}

	@DisplayName("set(IT)")
	@Nested
	public class SetIT {

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void test_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

	}

	@DisplayName("translate")
	@Nested
	public class Translate {

		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

	}

	@DisplayName("toBoundingBox")
	@Nested
	public class ToBoundingBox {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
	
		@DisplayName("(box) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void box_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("(Triangle3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void triangle_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(AlignedBox3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void alignedbox_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(MultiShape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
	    public void multishape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Capsule3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void capsule_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Sphere3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void sphere_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Segment3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void segment_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Path3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void path_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(PathIterator3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void pathiterator_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}

		@DisplayName("(Shape3afp) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public final void shape_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			fail("Todo");
		}
	}

	//	@DisplayName("intersectsSphereSphere")
	//	@Nested
	//	public class IntersectsSphereSphere {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereSphere(0, 0, 0, 1, 10, 10, 0, 1));
	//		}
	//
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 1, 0, 0, 0, 1));
	//		}
	//
	//		@DisplayName("#3")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_3(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 1, 0, .5, 0, 1));
	//		}
	//
	//		@DisplayName("#4")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_4(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 1, .5, 0, 0, 1));
	//		}
	//
	//		@DisplayName("#5")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_5(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 1, .5, .5, 0, 1));
	//		}
	//
	//		@DisplayName("#6")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_6(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereSphere(0, 0, 0, 1, 2, 0, 0, 1));
	//		}
	//
	//		@DisplayName("#7")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_7(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 1. Touching externally (distance = sum of radii) -> false
	//			assertFalse(Sphere3afp.intersectsSphereSphere(0, 0, 0, 1, 2, 0, 0, 1));   // distance 2, sum 2
	//		}
	//
	//		@DisplayName("#8")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_8(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 2. Touching internally (one sphere inside the other, tangent from inside)
	//			//		    Small sphere inside large, touching inner surface
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 5, 3, 0, 0, 2));
	//		}
	//
	//		@DisplayName("#9")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_9(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			//		    Another internal tangent: large radius 10, small radius 3, centers distance 7 -> false
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 10, 7, 0, 0, 3));   // 7 = 10-3
	//		}
	//
	//		@DisplayName("#10")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_10(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 3. One sphere completely inside another without touching -> true? 
	//			//		    Actually if one sphere is entirely inside the other but not touching, they intersect (the inner sphere is fully inside). The method should return true because they share all points of the smaller sphere.
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 10, 2, 0, 0, 3));   // distance 2, sum = 13 > 2, and also distance < 10-3? 2 < 7, so inside. Should be true.
	//		}
	//
	//		@DisplayName("#11")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_11(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 5. Spheres identical, same center -> true (already have)
	//			assertTrue(Sphere3afp.intersectsSphereSphere(1, 2, 3, 5, 1, 2, 3, 5));
	//		}
	//
	//		@DisplayName("#12")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_12(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 6. Spheres with zero radius (degenerate points)
	//			//		    Two points at same location -> true (they intersect at the point)
	//			assertFalse(Sphere3afp.intersectsSphereSphere(0, 0, 0, 0, 0, 0, 0, 0));
	//		}
	//
	//		@DisplayName("#13")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_13(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			//		    Two points distinct -> false
	//			assertFalse(Sphere3afp.intersectsSphereSphere(0, 0, 0, 0, 1, 0, 0, 0));
	//		}
	//
	//		@DisplayName("#14")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_14(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			//		    Point inside a sphere -> true (point is inside the sphere)
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 5, 1, 0, 0, 0));   // distance 1 < 5
	//		}
	//
	//		@DisplayName("#15")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_15(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 7. One sphere radius zero touching another sphere from outside? distance = large radius -> false (touching)
	//			assertFalse(Sphere3afp.intersectsSphereSphere(0, 0, 0, 5, 5, 0, 0, 0));   // distance 5 = radius 5 -> touching point
	//		}
	//
	//		@DisplayName("#16")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_16(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 8. Different radii, intersection with positive volume (distance < sum, distance > |R-r|)
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 5, 6, 0, 0, 3));   // distance 6, sum = 8, diff = 2. 2 < 6 < 8 -> overlap.
	//		}
	//
	//		@DisplayName("#17")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_17(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 9. Non‑coplanar centers (z coordinate non‑zero)
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 2, 1, 1, 1, 2));   // distance sqrt(3) ~  1.732, sum = 4 -> true
	//		}
	//
	//		@DisplayName("#18")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_18(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereSphere(0, 0, 0, 2, 4, 0, 4, 1));   // distance sqrt(32) ~  5.657, sum = 3 -> false
	//		}
	//
	//		@DisplayName("#19")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_19(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 10. Borderline just inside / just outside (floating point tolerance)
	//			//		      Just inside: distance = sum - epsilon
	//			assertTrue(Sphere3afp.intersectsSphereSphere(0, 0, 0, 1, 1.9999999, 0, 0, 1));   // distance ~  1.9999999, sum = 2 -> true (since < 2)
	//		}
	//
	//		@DisplayName("#20")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_20(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			//		      Just outside: distance = sum + epsilon
	//			assertFalse(Sphere3afp.intersectsSphereSphere(0, 0, 0, 1, 2.0000001, 0, 0, 1));   // false
	//		}
	//
	//		@DisplayName("#21")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_21(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 11. Large coordinates and large radii (no overflow)
	//			assertTrue(Sphere3afp.intersectsSphereSphere(1e6, 2e6, 3e6, 1e7, 1.5e6, 2.5e6, 3.2e6, 5e6));   // distance ~  sqrt((0.5e6)^2+(0.5e6)^2+(0.2e6)^2) ~  0.734e6, sum = 15e6 -> true
	//		}
	//
	//		@DisplayName("#22")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_22(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereSphere(0, 0, 0, 1e6, 3e6, 0, 0, 1e6));   // distance 3e6, sum = 2e6 -> false
	//		}
	//
	//	}
	//
	//	@DisplayName("intersectsSphereLine")
	//	@Nested
	//	public class IntersectsSphereLine {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, -5, -5, 0, -4, -4, 0));
	//		}
	//
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, -5, -5, 0, 5, 5, 0));
	//		}
	//
	//		@DisplayName("#3")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_3(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, -5, -5, 0, .5, .5, 0));
	//		}
	//
	//		@DisplayName("#4")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_4(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, -5, -5, 0, .5, -4, 0));
	//		}
	//
	//		@DisplayName("#5")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_5(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 20, .5, 0, 21, 1.5, 0));
	//		}
	//
	//		@DisplayName("#6")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_6(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Line tangent to sphere (distance = radius) -> true
	//			// Sphere at origin radius 1, line along y-axis at x=1, z=0
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 1, -2, 0, 1, 2, 0));
	//		}
	//
	//		@DisplayName("#7")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_7(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Line just outside sphere (distance slightly > radius) -> false
	//			assertFalse(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 1.0001, -2, 0, 1.0001, 2, 0));
	//		}
	//
	//		@DisplayName("#8")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_8(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// One point inside sphere, line infinite -> true
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 0.5, 0, 0, 2, 0, 0));
	//		}
	//
	//		@DisplayName("#9")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_9(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Both points outside but line passes through sphere -> true (existing but add another)
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, -3, 0, 0, 3, 0, 0));
	//		}
	//
	//		@DisplayName("#10")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_10(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Both points outside, line completely misses sphere
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 2, 2, 0, 3, 3, 0));
	//		}
	//
	//		@DisplayName("#11")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_11(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 3D line along Z-axis through center
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 0, 0, -2, 0, 0, 2));
	//		}
	//
	//		@DisplayName("#12")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_12(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 3D line missing sphere (distance > radius) -> false
	//			assertFalse(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 2, 0, 0, 2, 0, 2));
	//		}
	//
	//		@DisplayName("#13")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_13(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 3D line tangent to sphere (distance = radius) -> true
	//			// Line parallel to Z-axis, at x=1, y=0
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 1, 0, -2, 1, 0, 2));
	//		}
	//
	//		@DisplayName("#14")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_14(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Degenerate line (both points identical)
	//			// Point inside sphere
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 0.5, 0, 0, 0.5, 0, 0));
	//		}
	//
	//		@DisplayName("#15")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_15(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Point on sphere surface -> true (touches)
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 1, 0, 0, 1, 0, 0));
	//		}
	//
	//		@DisplayName("#16")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_16(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Point outside sphere
	//			assertFalse(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 2, 0, 0, 2, 0, 0));
	//		}
	//
	//		@DisplayName("#17")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_17(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Large coordinates, sphere and line far apart but still intersect
	//			assertTrue(Sphere3afp.intersectsSphereLine(1e6, 2e6, 3e6, 1e7, 1e6 - 2e7, 2e6, 3e6, 1e6 + 2e7, 2e6, 3e6));
	//		}
	//
	//		@DisplayName("#18")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_18(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Floating-point tolerance: line just tangent with epsilon error
	//			// Distance = radius + 1e-12 -> false (assuming exact comparison)
	//			assertFalse(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 1 + 1e-12, -2, 0, 1 + 1e-12, 2, 0));
	//		}
	//
	//		@DisplayName("#19")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_19(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Distance = radius - 1e-12 -> true
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, 1 - 1e-12, -2, 0, 1 - 1e-12, 2, 0));
	//		}
	//
	//		@DisplayName("#20")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_20(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Line passing through sphere center -> true
	//			assertTrue(Sphere3afp.intersectsSphereLine(0, 0, 0, 1, -2, 0, 0, 2, 0, 0));
	//		}
	//
	//	}
	//
	//	@DisplayName("intersectsSphereSegment")
	//	@Nested
	//	public class IntersectsSphereSegment {
	//		
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, -5, -5, 0, -4, -4, 0));
	//		}
	//
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, -5, -5, 0, 5, 5, 0));
	//		}
	//
	//		@DisplayName("#3")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_3(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, -5, -5, 0, .5, .5, 0));
	//		}
	//
	//		@DisplayName("#4")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_4(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, -5, -5, 0, .5, -4, 0));
	//		}
	//
	//		@DisplayName("#5")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_5(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 20, .5, 0, 21, 1.5, 0));
	//		}
	//
	//		@DisplayName("#6")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_6(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereSegment(1, 1, 0, 1, .5, -1, 0, .5, 4, 0));
	//		}
	//
	//		@DisplayName("#7")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_7(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Line passing through sphere center -> true
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, -2, 0, 0, 2, 0, 0));
	//		}
	//
	//		@DisplayName("#8")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_8(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Line tangent to sphere (distance = radius) -> true
	//			// Sphere at origin radius 1, line along y-axis at x=1, z=0
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 1, -2, 0, 1, 2, 0));
	//		}
	//
	//		@DisplayName("#9")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_9(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Line just outside sphere (distance slightly > radius) -> false
	//			assertFalse(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 1.0001, -2, 0, 1.0001, 2, 0));
	//		}
	//
	//		@DisplayName("#10")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_10(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// One point inside sphere, line infinite -> true
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 0.5, 0, 0, 2, 0, 0));
	//		}
	//
	//		@DisplayName("#11")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_11(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Both points outside but line passes through sphere -> true (existing but add another)
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, -3, 0, 0, 3, 0, 0));
	//		}
	//
	//		@DisplayName("#12")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_12(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Both points outside, line completely misses sphere -> false
	//			assertFalse(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 2, 2, 0, 3, 3, 0));
	//		}
	//
	//		@DisplayName("#14")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_14(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 3D line along Z-axis through center -> true
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 0, 0, -2, 0, 0, 2));
	//		}
	//
	//		@DisplayName("#15")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_15(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 3D line missing sphere (distance > radius) -> false
	//			assertFalse(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 2, 0, 0, 2, 0, 2));
	//		}
	//
	//		@DisplayName("#16")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_16(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// 3D line tangent to sphere (distance = radius) -> true
	//			// Line parallel to Z-axis, at x=1, y=0
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 1, 0, -2, 1, 0, 2));
	//		}
	//
	//		@DisplayName("#17")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_17(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Degenerate line (both points identical)
	//			// Point inside sphere -> true
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 0.5, 0, 0, 0.5, 0, 0));
	//		}
	//
	//		@DisplayName("#18")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_18(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Point on sphere surface -> true (touches)
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 1, 0, 0, 1, 0, 0));
	//		}
	//
	//		@DisplayName("#19")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_19(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Point outside sphere
	//			assertFalse(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 2, 0, 0, 2, 0, 0));
	//		}
	//
	//		@DisplayName("#20")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_20(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Large coordinates, sphere and line far apart but still intersect
	//			assertTrue(Sphere3afp.intersectsSphereSegment(1e6, 2e6, 3e6, 1e7, 1e6 - 2e7, 2e6, 3e6, 1e6 + 2e7, 2e6, 3e6));
	//		}
	//
	//		@DisplayName("#21")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_21(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Floating-point tolerance: line just tangent with epsilon error
	//			// Distance = radius + 1e-12 -> false (assuming exact comparison)
	//			assertFalse(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 1 + 1e-12, -2, 0, 1 + 1e-12, 2, 0));
	//		}
	//
	//		@DisplayName("#22")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_22(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			// Distance = radius - 1e-12 -> true
	//			assertTrue(Sphere3afp.intersectsSphereSegment(0, 0, 0, 1, 1 - 1e-12, -2, 0, 1 - 1e-12, 2, 0));
	//		}
	//
	//	}
	//
	//	@DisplayName("intersectsSphereAlignedBox")
	//	@Nested
	//	public class IntersectsSphereAlignedBox {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 1, -5, -5, 0, -4, -4, 10));
	//		}
	//
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 1, -5, -5, 0, 5, 5, 10));
	//		}
	//
	//		@DisplayName("#3")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_3(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 1, -5, -5, 0, .5, .5, 10));
	//		}
	//
	//		@DisplayName("#4")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_4(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 1, -5, -5, 0, .5, -4, 10));
	//		}
	//
	//		@DisplayName("#5")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_5(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 1, 20, .5, 0, 21, 1.5, 10));
	//		}
	//
	//		@DisplayName("#6")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_6(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, -4, -4, 0, -3, -4, 10));
	//		}
	//
	//		@DisplayName("#7")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_7(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, -5, -5, 0, 5, 5, 10));
	//		}
	//
	//		@DisplayName("#8")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_8(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, -5, -5, 0, 5, 5, 1));
	//		}
	//
	//		@DisplayName("#9")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_9(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, -5, -5, 0, .5, .5, 10));
	//		}
	//
	//		@DisplayName("#10")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_10(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, -5, -4, 0, .5, -3, 10));
	//		}
	//
	//		@DisplayName("#11")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_11(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, 20, .5, 0, 21, 1.5, 10));
	//		}
	//
	//		@DisplayName("#12")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_12(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, -5, -5, 0, -4, -4, 10));
	//		}
	//
	//		@DisplayName("#13")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_13(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, -1, -100, 0, 0, 100, 10));
	//		}
	//
	//		@DisplayName("#14")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_14(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, -1, -100, 0, .0001, 100, 10));
	//		}
	//
	//		@DisplayName("#15")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_15(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, -1, 2, 0, .0001, 3.0001, 10));
	//		}
	//
	//		@DisplayName("#16")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_16(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(5, 8, 9, 5, 1, 4, 0, 2.0001, 5.0001, 10));
	//		}
	//
	//		@DisplayName("#17")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_17(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 1, -2, -2, -2, 2, 2, 2));
	//		}
	//
	//		@DisplayName("#18")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_18(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 5, -1, -1, -1, 1, 1, 1));
	//		}
	//
	//		@DisplayName("#19")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_19(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(2, 0, 0, 1, 0, 0, 0, 1, 1, 1));
	//		}
	//
	//		@DisplayName("#20")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_20(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(2, 0, 1, 1, 0, 0, 0, 2, 2, 2));
	//		}
	//
	//		@DisplayName("#21")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_21(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(3, 2, 2, 1, 0, 0, 0, 2, 2, 2));
	//		}
	//
	//		@DisplayName("#22")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_22(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 1, 2, 2, 2, 3, 3, 3));
	//		}
	//
	//		@DisplayName("#23")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_23(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5));
	//		}
	//
	//		@DisplayName("#24")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_24(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 1, 1, 0, 0, 1, 0, 0));
	//		}
	//
	//		@DisplayName("#25")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_25(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 1, 2, 0, 0, 2, 0, 0));
	//		}
	//
	//		@DisplayName("#26")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_26(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(0.5, 0.5, 0.5, 0, 0, 0, 0, 1, 1, 1));
	//		}
	//
	//		@DisplayName("#27")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_27(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(1, 0.5, 0.5, 0, 0, 0, 0, 1, 1, 1));
	//		}
	//
	//		@DisplayName("#28")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_28(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(2, 0.5, 0.5, 0, 0, 0, 0, 1, 1, 1));
	//		}
	//
	//		@DisplayName("#29")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_29(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(100, 0, 0, 10, 0, 0, 0, 1, 1, 1));
	//		}
	//
	//		@DisplayName("#30")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_30(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 10, -2, -2, -2, 2, 2, 2));
	//		}
	//
	//		@DisplayName("#31")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_31(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(0, 0, 0, 2, 1, 1, 1, 3, 3, 3)); // sphere center at 0, radius 2, box from (1,1,1) to (3,3,3) – overlap near (1,1,1) which is distance sqrt(3)~ 1.732 <2
	//		}
	//
	//		@DisplayName("#32")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_32(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(0.5, 0.5, 0.5, 0.2, 0, 0, 0, 1, 1, 1));
	//		}
	//
	//		@DisplayName("#33")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_33(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(1e6, 2e6, 3e6, 1e7, 1e6 - 5e6, 2e6 - 5e6, 3e6 - 5e6, 1e6 + 5e6, 2e6 + 5e6, 3e6 + 5e6));
	//		}
	//
	//		@DisplayName("#34")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_34(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(Sphere3afp.intersectsSphereAlignedBox(1e6, 2e6, 3e6, 1e7, 1e6 + 2e7, 2e6 + 2e7, 3e6 + 2e7, 1e6 + 3e7, 2e6 + 3e7, 3e6 + 3e7));
	//		}
	//
	//		@DisplayName("#35")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_35(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(1.0000001, 0, 0, 1, 0, 0, 0, 2, 2, 2));
	//		}
	//
	//		@DisplayName("#36")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_36(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(Sphere3afp.intersectsSphereAlignedBox(0.9999999, 0, 0, 1, 0, 0, 0, 2, 2, 2));
	//		}
	//
	//	}
	//
	//	@DisplayName("getX")
	//	@Nested
	//	public class GetX {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(5, getS().getX());
	//		}
	//
	//	}
	//
	//	@DisplayName("getY")
	//	@Nested
	//	public class GetY {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(8, getS().getY());
	//		}
	//
	//	}
	//
	//	@DisplayName("getZ")
	//	@Nested
	//	public class GetZ {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(9, getS().getZ());
	//		}
	//
	//	}
	//
	//	@DisplayName("getCenter")
	//	@Nested
	//	public class GetCenter {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			Point3D center = getS().getCenter();
	//			assertEpsilonEquals(5, center.getX());
	//			assertEpsilonEquals(8, center.getY());
	//			assertEpsilonEquals(9, center.getZ());
	//		}
	//	}
	//
	//	@DisplayName("setCenter")
	//	@Nested
	//	public class SetCenter {
	//
	//		@DisplayName("(Point3D) #1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void point_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setCenter(createPoint(123.456, 789.123, 0));
	//			assertEpsilonEquals(123.456, getS().getX());
	//			assertEpsilonEquals(789.123, getS().getY());
	//			assertEpsilonEquals(0, getS().getZ());
	//			assertEpsilonEquals(5, getS().getRadius());
	//		}
	//	
	//		@DisplayName("(double,double,double) #1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void doubledoubledouble_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setCenter(123.456, 789.123, 0);
	//			assertEpsilonEquals(123.456, getS().getX());
	//			assertEpsilonEquals(789.123, getS().getY());
	//			assertEpsilonEquals(0, getS().getZ());
	//			assertEpsilonEquals(5, getS().getRadius());
	//		}
	//
	//	}
	//
	//	@DisplayName("setX")
	//	@Nested
	//	public class SetX {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setX(123.456);
	//			assertEpsilonEquals(123.456, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(5, getS().getRadius());
	//		}
	//
	//	}
	//
	//	@DisplayName("setY")
	//	@Nested
	//	public class SetY {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setY(123.456);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(123.456, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(5, getS().getRadius());
	//		}
	//
	//	}
	//
	//	@DisplayName("setZ")
	//	@Nested
	//	public class SetZ {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setZ(123.456);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(123.456, getS().getZ());
	//			assertEpsilonEquals(5, getS().getRadius());
	//		}
	//
	//	}
	//
	//	@DisplayName("getRadius")
	//	@Nested
	//	public class GetRadius {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(5, getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("setRadius")
	//	@Nested
	//	public class SetRadius {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setRadius(123.456);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(123.456, getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("set")
	//	@Nested
	//	public class Set {
	//
	//		@DisplayName("(double,double,double,double) #1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void doubledoubledoubledouble_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().set(123.456, 789.123, 1, 456.789);
	//			assertEpsilonEquals(123.456, getS().getX());
	//			assertEpsilonEquals(789.123, getS().getY());
	//			assertEpsilonEquals(1, getS().getZ());
	//			assertEpsilonEquals(456.789, getS().getRadius());
	//		}
	//
	//		@DisplayName("(double,double,double,double,double,double) #1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void doubledoubledoubledoubledoubledouble_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().set(-171, -254, -9, 475, 804, 11);
	//			assertEpsilonEquals(66.5, getS().getX()); // 237.5
	//			assertEpsilonEquals(148, getS().getY()); // 402
	//			assertEpsilonEquals(-3.5, getS().getZ()); // 5.5
	//			assertEpsilonEquals(5.5, getS().getRadius());
	//		}
	//
	//		@DisplayName("(Point3D,double) #1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void pointdouble_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().set(createPoint(123.456, 789.123, 1), 456.789);
	//			assertEpsilonEquals(123.456, getS().getX());
	//			assertEpsilonEquals(789.123, getS().getY());
	//			assertEpsilonEquals(1, getS().getZ());
	//			assertEpsilonEquals(456.789, getS().getRadius());
	//		}
	//
	//		@DisplayName("(Point3D,Point3D) #1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void pointpoint_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().set(createPoint(-171, 550, -9), createPoint(475, -254, 11));
	//			assertEpsilonEquals(152, getS().getX()); // 323
	//			assertEpsilonEquals(148, getS().getY()); // 402
	//			assertEpsilonEquals(1, getS().getZ()); // 10
	//			assertEpsilonEquals(10., getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("s += Vector3D")
	//	@Nested
	//	public class OperatorAddVector3D {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().operator_add(createVector(123.456, -789.123, 1));
	//			assertEpsilonEquals(128.456, getS().getX());
	//			assertEpsilonEquals(-781.123, getS().getY());
	//			assertEpsilonEquals(10, getS().getZ());
	//			assertEpsilonEquals(5, getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("s + Vector3D")
	//	@Nested
	//	public class OperatorPlusVector3D {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			T shape = getS().operator_plus(createVector(123.456, -789.123, 1));
	//			assertNotNull(shape);
	//			assertNotSame(getS(), shape);
	//			assertEpsilonEquals(128.456, shape.getX());
	//			assertEpsilonEquals(-781.123, shape.getY());
	//			assertEpsilonEquals(10, shape.getZ());
	//			assertEpsilonEquals(5, shape.getRadius());
	//		}
	//	}
	//
	//	@DisplayName("s -= Vector3D")
	//	@Nested
	//	public class OperatorRemoveVector3D {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().operator_remove(createVector(123.456, -789.123, 1));
	//			assertEpsilonEquals(-118.456, getS().getX());
	//			assertEpsilonEquals(797.123, getS().getY());
	//			assertEpsilonEquals(8, getS().getZ());
	//			assertEpsilonEquals(5, getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("s - Vector3D")
	//	@Nested
	//	public class OperatorMinusVector3D {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			T shape = getS().operator_minus(createVector(123.456, -789.123, 1));
	//			assertNotNull(shape);
	//			assertNotSame(getS(), shape);
	//			assertEpsilonEquals(-118.456, shape.getX());
	//			assertEpsilonEquals(797.123, shape.getY());
	//			assertEpsilonEquals(8, shape.getZ());
	//			assertEpsilonEquals(5, shape.getRadius());
	//		}
	//	}
	//
	//	@DisplayName("s && Point3D")
	//	@Nested
	//	public class OperatorAndPoint3D {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(getS().operator_and(createPoint(0,0, 9)));
	//		}
	//
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(getS().operator_and(createPoint(11,10, 9)));
	//		}
	//
	//		@DisplayName("#3")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_3(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(getS().operator_and(createPoint(11,50, 9)));
	//		}
	//
	//		@DisplayName("#4")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_4(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(getS().operator_and(createPoint(9,12, 9)));
	//		}
	//
	//		@DisplayName("#5")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_5(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(getS().operator_and(createPoint(9,11, 9)));
	//		}
	//
	//		@DisplayName("#6")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_6(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(getS().operator_and(createPoint(8,12, 9)));
	//		}
	//
	//		@DisplayName("#7")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_7(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(getS().operator_and(createPoint(3,7, 9)));
	//		}
	//
	//		@DisplayName("#8")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_8(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertFalse(getS().operator_and(createPoint(10,11, 9)));
	//		}
	//
	//		@DisplayName("#9")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_9(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(getS().operator_and(createPoint(9,10, 9)));
	//		}
	//	}
	//
	//	@DisplayName("s && Shape3D")
	//	@Nested
	//	public class OperatorAndShape3D {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertTrue(getS().operator_and(createSphere(10, 10, 9, 1)));
	//		}
	//	}
	//
	//	@DisplayName("s .. Point3D")
	//	@Nested
	//	public class OperatorUpToPoint3D {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(7.5499003, getS().operator_upTo(createPoint(.5,.5, 0)));
	//		}
	//
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(10.792403, getS().operator_upTo(createPoint(-1.2,-3.4, 0)));
	//		}
	//
	//		@DisplayName("#3")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_3(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(6.1892805, getS().operator_upTo(createPoint(-1.2,5.6, 0)));
	//		}
	//
	//		@DisplayName("#4")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_4(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(4.6705739, getS().operator_upTo(createPoint(7.6,5.6, 0)));
	//		}
	//	}
	//
	//	@DisplayName("setFromCenter")
	//	@Nested
	//	public class SetFromCenter {
	//
	//		@DisplayName("(double,double,double,double,double,double) #1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void doubledoubledoubledoubledoubledouble_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setFromCenter(152, 148, 1, 475, -254, 11);
	//			assertEpsilonEquals(152, getS().getX()); // 323
	//			assertEpsilonEquals(148, getS().getY()); // 402
	//			assertEpsilonEquals(1, getS().getZ()); // 10
	//			assertEpsilonEquals(10., getS().getRadius());
	//		}
	//
	//		@DisplayName("(double,double,double,double,double,double) #2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void doubledoubledoubledoubledoubledouble_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setFromCenter(152, 148, 1, -171, 550, -9);
	//			assertEpsilonEquals(152, getS().getX()); // 323
	//			assertEpsilonEquals(148, getS().getY()); // 402
	//			assertEpsilonEquals(1, getS().getZ()); // 10
	//			assertEpsilonEquals(10., getS().getRadius());
	//		}
	//
	//		@DisplayName("(Point3D, Point3D) #1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void pointpoint_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setFromCenter(createPoint(152, 148, 1), createPoint(475, -254, 11));
	//			assertEpsilonEquals(152, getS().getX()); // 323
	//			assertEpsilonEquals(148, getS().getY()); // 402
	//			assertEpsilonEquals(1, getS().getZ()); // 10
	//			assertEpsilonEquals(10., getS().getRadius());
	//		}
	//
	//		@DisplayName("(Point3D, Point3D) #2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void pointpoint_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setFromCenter(createPoint(152, 148, 1), createPoint(-171, 550, -9));
	//			assertEpsilonEquals(152, getS().getX()); // 323
	//			assertEpsilonEquals(148, getS().getY()); // 402
	//			assertEpsilonEquals(1, getS().getZ()); // 10
	//			assertEpsilonEquals(10., getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("setFromCorners")
	//	@Nested
	//	public class SetFromCorners {
	//
	//		@DisplayName("(double,double,double,double,double,double) #1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void doubledoubledoubledoubledoubledouble_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setFromCorners(-171, 550, -9, 475, -254, 11);
	//			assertEpsilonEquals(152, getS().getX()); // 323
	//			assertEpsilonEquals(148, getS().getY()); // 402
	//			assertEpsilonEquals(1, getS().getZ()); // 10
	//			assertEpsilonEquals(10., getS().getRadius());
	//		}
	//	
	//		@DisplayName("(Point3D, Point3D) #1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void pointpoint_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setFromCorners(createPoint(-171, 550, -9), createPoint(475, -254, 11));
	//			assertEpsilonEquals(152, getS().getX()); // 323
	//			assertEpsilonEquals(148, getS().getY()); // 402
	//			assertEpsilonEquals(1, getS().getZ()); // 10
	//			assertEpsilonEquals(10., getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("getMinX")
	//	@Nested
	//	public class GetMinX {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(0, getS().getMinX());
	//		}
	//	}
	//
	//	@DisplayName("setMinX")
	//	@Nested
	//	public class SetMinX {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMinX(-41);
	//			assertEpsilonEquals(-15.5, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(25.5, getS().getRadius());
	//		}
	//		
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMinX(41);
	//			assertEpsilonEquals(25.5, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(15.5, getS().getRadius());
	//		}
	//
	//	}
	//
	//	@DisplayName("getMaxX")
	//	@Nested
	//	public class GetMaxX {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(10, getS().getMaxX());
	//		}
	//	}
	//
	//	@DisplayName("setMaxX")
	//	@Nested
	//	public class SetMaxX {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMaxX(41);
	//			assertEpsilonEquals(20.5, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(20.5, getS().getRadius());
	//		}
	//	
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMaxX(-41);
	//			assertEpsilonEquals(-20.5, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(20.5, getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("getMinY")
	//	@Nested
	//	public class GetMinY {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(3, getS().getMinY());
	//		}
	//	}
	//
	//	@DisplayName("setMinY")
	//	@Nested
	//	public class SetMinY {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMinY(-41);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(-14, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(27, getS().getRadius());
	//		}
	//
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMinY(41);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(27, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(14, getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("getMaxY")
	//	@Nested
	//	public class GetMaxY {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(13, getS().getMaxY());
	//		}
	//	}
	//
	//	@DisplayName("setMaxY")
	//	@Nested
	//	public class SetMaxY {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMaxY(41);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(22, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(19, getS().getRadius());
	//		}
	//	
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMaxY(-41);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(-19, getS().getY());
	//			assertEpsilonEquals(9, getS().getZ());
	//			assertEpsilonEquals(22, getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("getMinZ")
	//	@Nested
	//	public class GetMinZ {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(4, getS().getMinZ());
	//		}
	//	}
	//
	//	@DisplayName("setMinZ")
	//	@Nested
	//	public class SetMinZ {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMinZ(-41);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(-13.5, getS().getZ());
	//			assertEpsilonEquals(27.5, getS().getRadius());
	//		}
	//	
	//		@DisplayName("#2")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_2(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMinZ(41);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(27.5, getS().getZ());
	//			assertEpsilonEquals(13.5, getS().getRadius());
	//		}
	//	}
	//
	//	@DisplayName("getMaxZ")
	//	@Nested
	//	public class GetMaxZ {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			assertEpsilonEquals(14, getS().getMaxZ());
	//		}
	//	}
	//
	//	@DisplayName("setMaxZ")
	//	@Nested
	//	public class SetMaxZ {
	//
	//		@DisplayName("#1")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void test_1(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMaxZ(41);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(22.5, getS().getZ());
	//			assertEpsilonEquals(18.5, getS().getRadius());
	//		}
	//	
	//		@DisplayName("setMaxZ(double) coord swap")
	//		@ParameterizedTest(name = "{index} => {0}")
	//		@EnumSource(CoordinateSystem3D.class)
	//		public final void setMaxZ_swap(CoordinateSystem3D cs) {
	//			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	//			getS().setMaxZ(-41);
	//			assertEpsilonEquals(5, getS().getX());
	//			assertEpsilonEquals(8, getS().getY());
	//			assertEpsilonEquals(-18.5, getS().getZ());
	//			assertEpsilonEquals(22.5, getS().getRadius());
	//		}
	//	}

}

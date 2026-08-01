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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.GeomFactory3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractGeomFactory3dTestCase extends AbstractMathTestCase {

	private GeomFactory3afp<?, ?, ?, ?, ?> factory;

	protected abstract GeomFactory3afp<?, ?, ?, ?, ?> createFactory();
	
	protected abstract Point3D createPoint(double x, double y, double z);

	protected abstract Vector3D createVector(double x, double y, double z);

	@BeforeEach
	public void setUp() throws Exception {
		factory = createFactory();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		factory = null;
	}

	@DisplayName("convertToPoint")
	@Nested
	public class ConvertToPoint {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Point3D p = createPoint(45, 56, 72);
			Point3D p2 = factory.convertToPoint(p);
			assertSame(p, p2);
		}
		
		@DisplayName("(Point3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void point_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Point3D p = new InnerComputationPoint3D(45, 56, 72);
			Point3D p2 = factory.convertToPoint(p);
			assertNotSame(p, p2);
			assertEquals(p, p2);
		}

		@DisplayName("(Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D v = new InnerComputationVector3D(45, 56, 72);
			Point3D p = factory.convertToPoint(v);
			assertNotSame(v, p);
			assertEquals(v, p);
		}

	}

	@DisplayName("convertToVector")
	@Nested
	public class ConvertToVector {

		@DisplayName("(Point3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void point_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Point3D p = new InnerComputationPoint3D(45, 56, 72);
			Vector3D v = factory.convertToVector(p);
			assertNotSame(p, v);
			assertEquals(p, v);
		}

		@DisplayName("(Vector3D) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vector_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D v = createVector(45, 56, 72);
			Vector3D v2 = factory.convertToVector(v);
			assertSame(v, v2);
		}
		
		@DisplayName("(Vector3D) #2")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void vector_2(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D v = new InnerComputationVector3D(45, 56, 72);
			Vector3D v2 = factory.convertToVector(v);
			assertNotSame(v, v2);
			assertEquals(v, v2);
		}
	}


	@DisplayName("newPoint")
	@Nested
	public class NewPoint {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Point3D p = factory.newPoint();
			assertNotNull(p);
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
			assertEpsilonEquals(0, p.getZ());
			Point3D ref = createPoint(0, 0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}
	
		@DisplayName("(int,int,int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void intintint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Point3D p = factory.newPoint(15, 48, 6);
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(48, p.iy());
			assertEquals(6, p.iz());
			Point3D ref = createPoint(0, 0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}
	
		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Point3D p = factory.newPoint(15.34, 48.56, 6.42);
			assertNotNull(p);
			assertEpsilonEquals(15.34, p.getX());
			assertEpsilonEquals(48.56, p.getY());
			assertEpsilonEquals(6.42, p.getZ());
			Point3D ref = createPoint(0, 0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}
	}

	@DisplayName("newVector")
	@Nested
	public class NewVector {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D p = factory.newVector();
			assertNotNull(p);
			assertEpsilonEquals(0, p.getX());
			assertEpsilonEquals(0, p.getY());
			Vector3D ref = createVector(0, 0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}
	
		@DisplayName("(int,int,int) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void intintint_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D p = factory.newVector(15, 48, 6);
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(48, p.iy());
			assertEquals(6, p.iz());
			Vector3D ref = createVector(0, 0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}
	
		@DisplayName("(double,double,double) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void doubledoubledouble_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Vector3D p = factory.newVector(15.45, 48.67, 6.42);
			assertNotNull(p);
			assertEpsilonEquals(15.45, p.getX());
			assertEpsilonEquals(48.67, p.getY());
			assertEpsilonEquals(6.42, p.getZ());
			Vector3D ref = createVector(0, 0, 0);
			assertEquals(ref.getClass(), p.getClass());
		}
	}

	@DisplayName("newPath")
	@Nested
	public class NewPath {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			Path3afp<?, ?, ?, ?, ?, ?> path = factory.newPath();
			assertNotNull(path);
			assertEquals(0, path.size());
		}
	}

	@DisplayName("newBox")
	@Nested
	public class NewBox {

		@DisplayName("() #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void empty_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp<?, ?, ?, ?, ?, ?> r = factory.newBox();
			assertNotNull(r);
			assertEpsilonEquals(0, r.getMinX());
			assertEpsilonEquals(0, r.getMinY());
			assertEpsilonEquals(0, r.getMinZ());
			assertEpsilonEquals(0, r.getMaxX());
			assertEpsilonEquals(0, r.getMaxY());
			assertEpsilonEquals(0, r.getMaxZ());
		}

		@DisplayName("(x,y,z, x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp<?, ?, ?, ?, ?, ?> r = factory.newBox(1, 2, 3, 4, 5, 6);
			assertNotNull(r);
			assertEpsilonEquals(1, r.getMinX());
			assertEpsilonEquals(2, r.getMinY());
			assertEpsilonEquals(3, r.getMinZ());
			assertEpsilonEquals(5, r.getMaxX());
			assertEpsilonEquals(7, r.getMaxY());
			assertEpsilonEquals(9, r.getMaxZ());
		}
	}

	@DisplayName("newMovePathElement")
	@Nested
	public class NewMovePathElement {

		@DisplayName("(x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			PathElement3afp element = factory.newMovePathElement(1, 2, 3);
			assertNotNull(element);
			assertSame(PathElementType.MOVE_TO, element.getType());
			assertEpsilonEquals(0, element.getFromX());
			assertEpsilonEquals(0, element.getFromY());
			assertEpsilonEquals(0, element.getFromZ());
			assertEpsilonEquals(0, element.getCtrlX1());
			assertEpsilonEquals(0, element.getCtrlY1());
			assertEpsilonEquals(0, element.getCtrlZ1());
			assertEpsilonEquals(0, element.getCtrlX2());
			assertEpsilonEquals(0, element.getCtrlY2());
			assertEpsilonEquals(0, element.getCtrlZ2());
			assertEpsilonEquals(1, element.getToX());
			assertEpsilonEquals(2, element.getToY());
			assertEpsilonEquals(3, element.getToZ());
		}
	}

	@DisplayName("newLinePathElement")
	@Nested
	public class NewLinePathElement {

		@DisplayName("(x,y,z, x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			PathElement3afp element = factory.newLinePathElement(1, 2, 3, 4, 5, 6);
			assertNotNull(element);
			assertSame(PathElementType.LINE_TO, element.getType());
			assertEpsilonEquals(1, element.getFromX());
			assertEpsilonEquals(2, element.getFromY());
			assertEpsilonEquals(3, element.getFromZ());
			assertEpsilonEquals(0, element.getCtrlX1());
			assertEpsilonEquals(0, element.getCtrlY1());
			assertEpsilonEquals(0, element.getCtrlZ1());
			assertEpsilonEquals(0, element.getCtrlX2());
			assertEpsilonEquals(0, element.getCtrlY2());
			assertEpsilonEquals(0, element.getCtrlZ2());
			assertEpsilonEquals(4, element.getToX());
			assertEpsilonEquals(5, element.getToY());
			assertEpsilonEquals(6, element.getToZ());
		}
	}

	@DisplayName("newClosePathElement")
	@Nested
	public class NewClosePathElement {

		@DisplayName("(x,y,z, x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			PathElement3afp element = factory.newClosePathElement(1, 2, 3, 4, 5, 6);
			assertNotNull(element);
			assertSame(PathElementType.CLOSE, element.getType());
			assertEpsilonEquals(1, element.getFromX());
			assertEpsilonEquals(2, element.getFromY());
			assertEpsilonEquals(3, element.getFromZ());
			assertEpsilonEquals(0, element.getCtrlX1());
			assertEpsilonEquals(0, element.getCtrlY1());
			assertEpsilonEquals(0, element.getCtrlZ1());
			assertEpsilonEquals(0, element.getCtrlX2());
			assertEpsilonEquals(0, element.getCtrlY2());
			assertEpsilonEquals(0, element.getCtrlZ2());
			assertEpsilonEquals(4, element.getToX());
			assertEpsilonEquals(5, element.getToY());
			assertEpsilonEquals(6, element.getToZ());
		}
	}

	@DisplayName("newCurvePathElement")
	@Nested
	public class NewCurvePathElement {

		@DisplayName("(x,y,z, x,y,z, x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			PathElement3afp element = factory.newCurvePathElement(1, 2, 3, 4, 5, 6, 7, 8, 9);
			assertNotNull(element);
			assertSame(PathElementType.QUAD_TO, element.getType());
			assertEpsilonEquals(1, element.getFromX());
			assertEpsilonEquals(2, element.getFromY());
			assertEpsilonEquals(3, element.getFromZ());
			assertEpsilonEquals(4, element.getCtrlX1());
			assertEpsilonEquals(5, element.getCtrlY1());
			assertEpsilonEquals(6, element.getCtrlZ1());
			assertEpsilonEquals(0, element.getCtrlX2());
			assertEpsilonEquals(0, element.getCtrlY2());
			assertEpsilonEquals(0, element.getCtrlZ2());
			assertEpsilonEquals(7, element.getToX());
			assertEpsilonEquals(8, element.getToY());
			assertEpsilonEquals(9, element.getToZ());
		}

		@DisplayName("(x,y,z, x,y,z, x,y,z, x,y,z) #1")
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		public void xyzxyzxyzxyz_1(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			PathElement3afp element = factory.newCurvePathElement(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
			assertNotNull(element);
			assertSame(PathElementType.CURVE_TO, element.getType());
			assertEpsilonEquals(1, element.getFromX());
			assertEpsilonEquals(2, element.getFromY());
			assertEpsilonEquals(3, element.getFromZ());
			assertEpsilonEquals(4, element.getCtrlX1());
			assertEpsilonEquals(5, element.getCtrlY1());
			assertEpsilonEquals(6, element.getCtrlZ1());
			assertEpsilonEquals(7, element.getCtrlX2());
			assertEpsilonEquals(8, element.getCtrlY2());
			assertEpsilonEquals(9, element.getCtrlZ2());
			assertEpsilonEquals(10, element.getToX());
			assertEpsilonEquals(11, element.getToY());
			assertEpsilonEquals(12, element.getToZ());
		}
	}

}

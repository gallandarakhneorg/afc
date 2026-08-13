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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d3.afp.GeomFactory3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("all")
public abstract class AbstractPathElement3dTestCase<T extends PathElement3afp, P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>,
			Q extends Quaternion<? super P, ? super V, ? super Q>>
		extends AbstractMathTestCase {

	protected static GeomFactory3afp<?, ?, ?, ?, ?> FACTORY;
	
	protected abstract GeomFactory3afp<?, ?, ?, ?, ?> createFactory();

	@BeforeEach
	public final void setUp() {
		FACTORY = createFactory();
	}
	
	private static Stream<Arguments> proposeArguments() {
		final List<Arguments> args = new ArrayList<>();
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			args.add(Arguments.of(cs, FACTORY.newMovePathElement(3, 2, 1), PathElementType.MOVE_TO));
			args.add(Arguments.of(cs, FACTORY.newLinePathElement(6, 5, 4, 3, 2, 1), PathElementType.LINE_TO));
			args.add(Arguments.of(cs, FACTORY.newCurvePathElement(9, 8, 7, 6, 5, 4, 3, 2, 1), PathElementType.QUAD_TO));
			args.add(Arguments.of(cs, FACTORY.newCurvePathElement(12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1), PathElementType.CURVE_TO));
			args.add(Arguments.of(cs, FACTORY.newClosePathElement(6, 5, 4, 3, 2, 1), PathElementType.CLOSE));
		}
		return args.stream();
	}

	@DisplayName("getFromX")
	@Nested
	public class GetFromX {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			switch (type) {
			case ARC_TO:
				// No yet implemented
				throw new IllegalArgumentException();
			case CLOSE:
				assertEpsilonEquals(6, element.getFromX());
				break;
			case CURVE_TO:
				assertEpsilonEquals(12, element.getFromX());
				break;
			case LINE_TO:
				assertEpsilonEquals(6, element.getFromX());
				break;
			case MOVE_TO:
				// No FROM property
				break;
			case QUAD_TO:
				assertEpsilonEquals(9, element.getFromX());
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	@DisplayName("getCtrlX1")
	@Nested
	public class GetCtrlX1 {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			switch (type) {
			case ARC_TO:
				// No yet implemented
				throw new IllegalArgumentException();
			case CLOSE:
				// No CTRL1 property
				break;
			case CURVE_TO:
				assertEpsilonEquals(9, element.getCtrlX1());
				break;
			case LINE_TO:
				// No CTRL1 property
				break;
			case MOVE_TO:
				// No CTRL1 property
				break;
			case QUAD_TO:
				assertEpsilonEquals(6, element.getCtrlX1());
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	@DisplayName("getCtrlX2")
	@Nested
	public class GetCtrlX2 {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			switch (type) {
			case ARC_TO:
				// No yet implemented
				throw new IllegalArgumentException();
			case CLOSE:
				// No CTRL2 property
				break;
			case CURVE_TO:
				assertEpsilonEquals(6, element.getCtrlX2());
				break;
			case LINE_TO:
				// No CTRL2 property
				break;
			case MOVE_TO:
				// No CTRL2 property
				break;
			case QUAD_TO:
				// No CTRL2 property
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	@DisplayName("getToX")
	@Nested
	public class GetToX {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(3, element.getToX());
		}
	}

	@DisplayName("getFromY")
	@Nested
	public class GetFromY {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			switch (type) {
			case ARC_TO:
				// No yet implemented
				throw new IllegalArgumentException();
			case CLOSE:
				assertEpsilonEquals(5, element.getFromY());
				break;
			case CURVE_TO:
				assertEpsilonEquals(11, element.getFromY());
				break;
			case LINE_TO:
				assertEpsilonEquals(5, element.getFromY());
				break;
			case MOVE_TO:
				// No FROM property
				break;
			case QUAD_TO:
				assertEpsilonEquals(8, element.getFromY());
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	@DisplayName("getCtrlY1")
	@Nested
	public class GetCtrlY1 {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			switch (type) {
			case ARC_TO:
				// No yet implemented
				throw new IllegalArgumentException();
			case CLOSE:
				// No CTRL1 property
				break;
			case CURVE_TO:
				assertEpsilonEquals(8, element.getCtrlY1());
				break;
			case LINE_TO:
				// No CTRL1 property
				break;
			case MOVE_TO:
				// No CTRL1 property
				break;
			case QUAD_TO:
				assertEpsilonEquals(5, element.getCtrlY1());
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	@DisplayName("getCtrlY2")
	@Nested
	public class GetCtrlY2 {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			switch (type) {
			case ARC_TO:
				// No yet implemented
				throw new IllegalArgumentException();
			case CLOSE:
				// No CTRL2 property
				break;
			case CURVE_TO:
				assertEpsilonEquals(5, element.getCtrlY2());
				break;
			case LINE_TO:
				// No CTRL2 property
				break;
			case MOVE_TO:
				// No CTRL2 property
				break;
			case QUAD_TO:
				// No CTRL2 property
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	@DisplayName("getToY")
	@Nested
	public class GetToY {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(2, element.getToY());
		}
	}

	@DisplayName("getFromZ")
	@Nested
	public class GetFromZ {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			switch (type) {
			case ARC_TO:
				// No yet implemented
				throw new IllegalArgumentException();
			case CLOSE:
				assertEpsilonEquals(4, element.getFromZ());
				break;
			case CURVE_TO:
				assertEpsilonEquals(10, element.getFromZ());
				break;
			case LINE_TO:
				assertEpsilonEquals(4, element.getFromZ());
				break;
			case MOVE_TO:
				// No FROM property
				break;
			case QUAD_TO:
				assertEpsilonEquals(7, element.getFromZ());
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	@DisplayName("getCtrlZ1")
	@Nested
	public class GetCtrlZ1 {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			switch (type) {
			case ARC_TO:
				// No yet implemented
				throw new IllegalArgumentException();
			case CLOSE:
				// No CTRL1 property
				break;
			case CURVE_TO:
				assertEpsilonEquals(7, element.getCtrlZ1());
				break;
			case LINE_TO:
				// No CTRL1 property
				break;
			case MOVE_TO:
				// No CTRL1 property
				break;
			case QUAD_TO:
				assertEpsilonEquals(4, element.getCtrlZ1());
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	@DisplayName("getCtrlZ2")
	@Nested
	public class GetCtrlZ2 {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			switch (type) {
			case ARC_TO:
				// No yet implemented
				throw new IllegalArgumentException();
			case CLOSE:
				// No CTRL2 property
				break;
			case CURVE_TO:
				assertEpsilonEquals(4, element.getCtrlZ2());
				break;
			case LINE_TO:
				// No CTRL2 property
				break;
			case MOVE_TO:
				// No CTRL2 property
				break;
			case QUAD_TO:
				// No CTRL2 property
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	@DisplayName("getToZ")
	@Nested
	public class GetToZ {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertEpsilonEquals(1, element.getToZ());
		}
	}

	@DisplayName("getType")
	@Nested
	public class GetType {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			assertSame(type, element.getType());
		}
	}

	@DisplayName("isDrawable")
	@Nested
	public class IsDrawable {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			if (type == PathElementType.MOVE_TO) {
				assertFalse(element.isDrawable());
			} else {
				assertTrue(element.isDrawable());
			}
		}
	}
	
	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			if (type == PathElementType.MOVE_TO) {
				assertTrue(element.isEmpty());
			} else {
				assertFalse(element.isEmpty());
			}
		}
	}
	
	@DisplayName("toArray")
	@Nested
	public class ToArray {
	
		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0} - {2}")
		@MethodSource("org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPathElement3dTestCase#proposeArguments")
		public final void test_1(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			final var arr = new double[12];
			element.toArray(arr);
			switch (type) {
			case ARC_TO:
				// No yet implemented
				throw new IllegalArgumentException();
			case CLOSE:
				break;
			case CURVE_TO:
				break;
			case LINE_TO:
				break;
			case MOVE_TO:
				break;
			case QUAD_TO:
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}
	
}

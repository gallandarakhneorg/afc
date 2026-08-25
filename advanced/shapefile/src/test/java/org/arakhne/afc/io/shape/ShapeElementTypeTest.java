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

package org.arakhne.afc.io.shape;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.arakhne.afc.io.shape.ShapeElementType;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("ShapeElementType")
@SuppressWarnings("all")
public class ShapeElementTypeTest extends AbstractIoShapeTestCase {

	@DisplayName("fromESRIInteger")
	@Nested
	public class FromESRIInteger {

		public static Stream<Arguments> generateValidCases() {
			final var arguments = new ArrayList<Arguments>();
			for (final var expected : ShapeElementType.values()) {
				arguments.add(Arguments.of(expected.shapeType, expected));
			}
			return arguments.stream();
		}
		
		public static Stream<Arguments> generateInvalidCases() {
			final var arguments = new ArrayList<Arguments>();
			for (var i = -5; i < -1; ++i) {
				arguments.add(Arguments.of(i));
			}
			int max = -5;
			for (final var expected : ShapeElementType.values()) {
				if (max < expected.shapeType) {
					max = expected.shapeType;
				}
			}
			for (var i = max + 1; i < 50; ++i) {
				arguments.add(Arguments.of(i));
			}
			return arguments.stream();
		}

		@DisplayName("Valid")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("generateValidCases")
		public void valid(Integer i, ShapeElementType expected) throws Exception {
			assertEquals(expected, ShapeElementType.fromESRIInteger(i));
		}

		@DisplayName("Invalid")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("generateInvalidCases")
		public void invalid(Integer i) throws Exception {
			assertSame(ShapeElementType.UNSUPPORTED, ShapeElementType.fromESRIInteger(i));
		}
	}

	@DisplayName("hasZ")
	@Nested
	public class HasZ {

		@DisplayName("MULTIPATCH")
		@Test
		public void testHasZ_MULTIPATCH() {
			assertTrue(ShapeElementType.MULTIPATCH.hasZ());
		}

		@DisplayName("MULTIPOINT")
		@Test
		public void testHasZ_MULTIPOINT() {
			assertFalse(ShapeElementType.MULTIPOINT.hasZ());
		}

		@DisplayName("MULTIPOINT_M")
		@Test
		public void testHasZ_MULTIPOINT_M() {
			assertFalse(ShapeElementType.MULTIPOINT_M.hasZ());
		}

		@DisplayName("MULTIPOINT_Z")
		@Test
		public void testHasZ_MULTIPOINT_Z() {
			assertTrue(ShapeElementType.MULTIPOINT_Z.hasZ());
		}

		@DisplayName("NULL")
		@Test
		public void testHasZ_NULL() {
			assertFalse(ShapeElementType.NULL.hasZ());
		}

		@DisplayName("POINT")
		@Test
		public void testHasZ_POINT() {
			assertFalse(ShapeElementType.POINT.hasZ());
		}

		@DisplayName("POINT_M")
		@Test
		public void testHasZ_POINT_M() {
			assertFalse(ShapeElementType.POINT_M.hasZ());
		}

		@DisplayName("POINT_Z")
		@Test
		public void testHasZ_POINT_Z() {
			assertTrue(ShapeElementType.POINT_Z.hasZ());
		}

		@DisplayName("POLYGON")
		@Test
		public void testHasZ_POLYGON() {
			assertFalse(ShapeElementType.POLYGON.hasZ());
		}

		@DisplayName("POLYGON_M")
		@Test
		public void testHasZ_POLYGON_M() {
			assertFalse(ShapeElementType.POLYGON_M.hasZ());
		}

		@DisplayName("POLYGON_Z")
		@Test
		public void testHasZ_POLYGON_Z() {
			assertTrue(ShapeElementType.POLYGON_Z.hasZ());
		}

		@DisplayName("POLYLINE")
		@Test
		public void testHasZ_POLYLINE() {
			assertFalse(ShapeElementType.POLYLINE.hasZ());
		}

		@DisplayName("POLYLINE_M")
		@Test
		public void testHasZ_POLYLINE_M() {
			assertFalse(ShapeElementType.POLYLINE_M.hasZ());
		}

		@DisplayName("POLYLINE_Z")
		@Test
		public void testHasZ_POLYLINE_Z() {
			assertTrue(ShapeElementType.POLYLINE_Z.hasZ());
		}

		@DisplayName("UNSUPPORTED")
		@Test
		public void testHasZ_UNSUPPORTED() {
			assertFalse(ShapeElementType.UNSUPPORTED.hasZ());
		}
	}

	@DisplayName("hasM")
	@Nested
	public class HasM {

		@DisplayName("MULTIPATCH")
		@Test
		public void testHasM_MULTIPATCH() {
			assertTrue(ShapeElementType.MULTIPATCH.hasM());
		}

		@DisplayName("MULTIPOINT")
		@Test
		public void testHasM_MULTIPOINT() {
			assertFalse(ShapeElementType.MULTIPOINT.hasM());
		}

		@DisplayName("MULTIPOINT_M")
		@Test
		public void testHasM_MULTIPOINT_M() {
			assertTrue(ShapeElementType.MULTIPOINT_M.hasM());
		}

		@DisplayName("MULTIPOINT_Z")
		@Test
		public void testHasM_MULTIPOINT_Z() {
			assertTrue(ShapeElementType.MULTIPOINT_Z.hasM());
		}

		@DisplayName("NULL")
		@Test
		public void testHasM_NULL() {
			assertFalse(ShapeElementType.NULL.hasM());
		}

		@DisplayName("POINT")
		@Test
		public void testHasM_POINT() {
			assertFalse(ShapeElementType.POINT.hasM());
		}

		@DisplayName("POINT_M")
		@Test
		public void testHasM_POINT_M() {
			assertTrue(ShapeElementType.POINT_M.hasM());
		}

		@DisplayName("POINT_Z")
		@Test
		public void testHasM_POINT_Z() {
			assertTrue(ShapeElementType.POINT_Z.hasM());
		}

		@DisplayName("POLYGON")
		@Test
		public void testHasM_POLYGON() {
			assertFalse(ShapeElementType.POLYGON.hasM());
		}

		@DisplayName("POLYGON_M")
		@Test
		public void testHasM_POLYGON_M() {
			assertTrue(ShapeElementType.POLYGON_M.hasM());
		}

		@DisplayName("POLYGON_Z")
		@Test
		public void testHasM_POLYGON_Z() {
			assertTrue(ShapeElementType.POLYGON_Z.hasM());
		}

		@DisplayName("POLYLINE")
		@Test
		public void testHasM_POLYLINE() {
			assertFalse(ShapeElementType.POLYLINE.hasM());
		}

		@DisplayName("POLYLINE_M")
		@Test
		public void testHasM_POLYLINE_M() {
			assertTrue(ShapeElementType.POLYLINE_M.hasM());
		}

		@DisplayName("POLYLINE_Z")
		@Test
		public void testHasM_POLYLINE_Z() {
			assertTrue(ShapeElementType.POLYLINE_Z.hasM());
		}

		@DisplayName("UNSUPPORTED")
		@Test
		public void testHasM_UNSUPPORTED() {
			assertFalse(ShapeElementType.UNSUPPORTED.hasM());
		}
	}
}

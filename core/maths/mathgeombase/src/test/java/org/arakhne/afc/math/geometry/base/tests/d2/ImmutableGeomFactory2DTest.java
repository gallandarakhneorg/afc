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

package org.arakhne.afc.math.geometry.base.tests.d2;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.d2.ImmutableGeomFactory2D;
import org.arakhne.afc.math.geometry.base.d2.ImmutablePoint2D;
import org.arakhne.afc.math.geometry.base.d2.ImmutableVector2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class ImmutableGeomFactory2DTest extends AbstractMathTestCase {

	private ImmutableGeomFactory2D factory;
	
	@BeforeEach
	public void setUp() {
		this.factory = new ImmutableGeomFactory2D();
	}
	
	@DisplayName("convertToPoint(Point2D)")
	@Nested
	public class ConvertToPointPoint2D {

		@Test
		@DisplayName("(null)")
		public void convertToPoint_nullPoint2D() {
			var expected = new InnerComputationPoint2D(0, 0);
			var actual = factory.convertToPoint((Point2D) null);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToPoint_expectedPoint2D() {
			var expected = new InnerComputationPoint2D(123., 486.);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(immutable)")
		public void convertToPoint_immutablePoint2D() {
			var expected = new ImmutablePoint2D(123., 486.);
			var actual = factory.convertToPoint(expected);
			assertSame(expected, actual);
		}

	}

	@DisplayName("convertToVector(Point2D)")
	@Nested
	public class ConvertToVectorPoint2D {

		@Test
		@DisplayName("(null)")
		public void convertToVector_nullPoint2D() {
			var expected = new InnerComputationPoint2D(0, 0);
			var actual = factory.convertToVector((Point2D) null);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToVector_expectedPoint2D() {
			var expected = new InnerComputationPoint2D(123., 486.);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(immutable)")
		public void convertToVector_immutablePoint2D() {
			var expected = new ImmutablePoint2D(123., 486.);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}

	@DisplayName("convertToPoint(Vector2D)")
	@Nested
	public class ConvertToPointVector2D {

		@Test
		@DisplayName("(null)")
		public void convertToPoint_nullVector2D() {
			var expected = new InnerComputationVector2D(0, 0);
			var actual = factory.convertToPoint((Vector2D) null);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToPoint_expectedVector2D() {
			var expected = new InnerComputationVector2D(123., 486.);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(immutable)")
		public void convertToPoint_immutableVector2D() {
			var expected = new ImmutableVector2D(123., 486.);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}

	@DisplayName("convertToVector(Vector2D)")
	@Nested
	public class ConvertToVectorVector2D {

		@Test
		@DisplayName("(null)")
		public void convertToVector_nullVector2D() {
			var expected = new InnerComputationVector2D(0, 0);
			var actual = factory.convertToVector((Vector2D) null);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToVector_expectedVector() {
			var expected = new InnerComputationVector2D(123., 486.);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(immutable)")
		public void convertToVector_immutableVector2D() {
			var expected = new ImmutableVector2D(123., 486.);
			var actual = factory.convertToVector(expected);
			assertSame(expected, actual);
		}

	}

	@DisplayName("newPoint")
	@Nested
	public class newPoint {

		@Test
		@DisplayName("()")
		public void newPoint() {
			var expected = new ImmutablePoint2D(0., 0.);
			var actual = factory.newPoint();
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(123.568, 457.584)")
		public void newPoint_doubledouble() {
			var expected = new ImmutablePoint2D(123.568, 457.584);
			var actual = factory.newPoint(123.568, 457.584);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(123, 457)")
		public void newPoint_intint() {
			var expected = new ImmutablePoint2D(123, 457);
			var actual = factory.newPoint(123, 457);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}

	@DisplayName("newVector")
	@Nested
	public class newVector {

		@Test
		@DisplayName("()")
		public void newVector() {
			var expected = new ImmutableVector2D(0., 0.);
			var actual = factory.newVector();
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(123.568, 457.584)")
		public void newVector_doubledouble() {
			var expected = new ImmutableVector2D(123.568, 457.584);
			var actual = factory.newVector(123.568, 457.584);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(123, 457)")
		public void newVector_intint() {
			var expected = new ImmutableVector2D(123, 457);
			var actual = factory.newVector(123, 457);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}
}
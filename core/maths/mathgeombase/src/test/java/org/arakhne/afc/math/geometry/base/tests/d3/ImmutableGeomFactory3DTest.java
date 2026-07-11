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

package org.arakhne.afc.math.geometry.base.tests.d3;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.d3.ImmutableGeomFactory3D;
import org.arakhne.afc.math.geometry.base.d3.ImmutablePoint3D;
import org.arakhne.afc.math.geometry.base.d3.ImmutableVector3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class ImmutableGeomFactory3DTest extends AbstractMathTestCase {

	private ImmutableGeomFactory3D factory;
	
	@BeforeEach
	public void setUp() {
		this.factory = new ImmutableGeomFactory3D();
	}
	
	@DisplayName("convertToPoint(Point3D)")
	@Nested
	public class ConvertToPointPoint3D {

		@Test
		@DisplayName("(null)")
		public void convertToPoint_nullPoint3D() {
			var expected = new InnerComputationPoint3D(0, 0, 0);
			var actual = factory.convertToPoint((Point3D) null);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToPoint_expectedPoint3D() {
			var expected = new InnerComputationPoint3D(123., 486., 654.2);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(immutable)")
		public void convertToPoint_immutablePoint3D() {
			var expected = new ImmutablePoint3D(123., 486., 654.2);
			var actual = factory.convertToPoint(expected);
			assertSame(expected, actual);
		}

	}

	@DisplayName("convertToVector(Point3D)")
	@Nested
	public class ConvertToVectorPoint3D {

		@Test
		@DisplayName("(null)")
		public void convertToVector_nullPoint3D() {
			var expected = new InnerComputationPoint3D(0, 0, 0);
			var actual = factory.convertToVector((Point3D) null);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToVector_expectedPoint3D() {
			var expected = new InnerComputationPoint3D(123., 486., 654.2);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(immutable)")
		public void convertToVector_immutablePoint3D() {
			var expected = new ImmutablePoint3D(123., 486., 654.2);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}

	@DisplayName("convertToPoint(Vector3D)")
	@Nested
	public class ConvertToPointVector3D {

		@Test
		@DisplayName("(null)")
		public void convertToPoint_nullVector3D() {
			var expected = new InnerComputationVector3D(0, 0, 0);
			var actual = factory.convertToPoint((Vector3D) null);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToPoint_expectedVector3D() {
			var expected = new InnerComputationVector3D(123., 486., 654.2);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(immutable)")
		public void convertToPoint_immutableVector3D() {
			var expected = new ImmutableVector3D(123., 486., 654.2);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}

	@DisplayName("convertToVector(Vector3D)")
	@Nested
	public class ConvertToVectorVector3D {

		@Test
		@DisplayName("(null)")
		public void convertToVector_nullVector3D() {
			var expected = new InnerComputationVector3D(0, 0, 0);
			var actual = factory.convertToVector((Vector3D) null);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToVector_expectedVector() {
			var expected = new InnerComputationVector3D(123., 486., 654.2);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(immutable)")
		public void convertToVector_immutableVector3D() {
			var expected = new ImmutableVector3D(123., 486., 654.2);
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
			var expected = new ImmutablePoint3D(0., 0., 0.);
			var actual = factory.newPoint();
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(123.568, 457.584, 654.2)")
		public void newPoint_doubledouble() {
			var expected = new ImmutablePoint3D(123.568, 457.584, 654.2);
			var actual = factory.newPoint(123.568, 457.584, 654.2);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(123, 457, 654)")
		public void newPoint_intint() {
			var expected = new ImmutablePoint3D(123, 457, 654);
			var actual = factory.newPoint(123, 457, 654);
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
			var expected = new ImmutableVector3D(0., 0., 0.);
			var actual = factory.newVector();
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(123.568, 457.584, 654.2)")
		public void newVector_doubledouble() {
			var expected = new ImmutableVector3D(123.568, 457.584, 654.2);
			var actual = factory.newVector(123.568, 457.584, 654.2);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(123, 457, 654)")
		public void newVector_intint() {
			var expected = new ImmutableVector3D(123, 457, 654);
			var actual = factory.newVector(123, 457, 654);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}

}
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

package org.arakhne.afc.math.geometry.base.tests.d1;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.d1.ImmutableGeomFactory1D;
import org.arakhne.afc.math.geometry.base.d1.ImmutablePoint1D;
import org.arakhne.afc.math.geometry.base.d1.ImmutableVector1D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ImmutableGeomFactory1D")
@SuppressWarnings("all")
public class ImmutableGeomFactory1DTest extends AbstractMathTestCase {

	private ImmutableGeomFactory1D<Segment1DStub> factory;
	
	private Segment1DStub segment;

	@BeforeEach
	public void setUp() {
		this.factory = new ImmutableGeomFactory1D<>();
		this.segment = new Segment1DStub();
	}

	@DisplayName("convertToPoint(Point1D)")
	@Nested
	public class ConvertToPointPoint1D {

		@Test
		@DisplayName("(immutable)")
		public void convertToPoint_immutablePoint1D() {
			var expected = new ImmutablePoint1D<Segment1DStub>(segment, 1., 2.);
			var actual = factory.convertToPoint(expected);
			assertSame(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToPoint_mutablePoint1D() {
			var expected = new Point1DStub(1., 2., segment);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}

	@DisplayName("convertToPoint(Vector1D)")
	@Nested
	public class ConvertToPointVector1D {

		@Test
		@DisplayName("(immutable)")
		public void convertToPoint_immutableVector1D() {
			var expected = new ImmutableVector1D<Segment1DStub>(segment, 1., 2.);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToPoint_mutableVector1D() {
			var expected = new Vector1DStub(1., 2., segment);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}

	@DisplayName("convertToVector(Point1D)")
	@Nested
	public class ConvertToVectorPoint1D {

		@Test
		@DisplayName("(immutable)")
		public void convertToVector_immutablePoint1D() {
			var expected = new ImmutablePoint1D<Segment1DStub>(segment, 1., 2.);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToVector_mutablePoint1D() {
			var expected = new Point1DStub(1., 2., segment);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}

	@DisplayName("convertToVector(Vector1D)")
	@Nested
	public class ConvertToVectorVector1D {

		@Test
		@DisplayName("(immutable)")
		public void convertToVector_immutableVector1D() {
			var expected = new ImmutableVector1D<Segment1DStub>(segment, 1., 2.);
			var actual = factory.convertToVector(expected);
			assertSame(expected, actual);
		}
	
		@Test
		@DisplayName("(mutable)")
		public void convertToVector_mutableVector1D() {
			var expected = new Vector1DStub(segment);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}

	}

	@DisplayName("newPoint")
	@Nested
	public class NewPoint {

		@Test
		@DisplayName("(Segment1D)")
		public void newPoint() {
			var s = new Segment1DStub();
			var actual = factory.newPoint(s);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(0., actual.getX());
			assertEpsilonEquals(0., actual.getY());
		}
	
		@Test
		@DisplayName("(Segment1D, double, double)")
		public void newPoint_doubledouble() {
			var s = new Segment1DStub();
			var actual = factory.newPoint(s, 154.487, 695.365);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(154.487, actual.getX());
			assertEpsilonEquals(695.365, actual.getY());
		}
	
		@Test
		@DisplayName("(Segment1D, int, int)")
		public void newPoint_intint() {
			var s = new Segment1DStub();
			var actual = factory.newPoint(s, 154, 695);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(154., actual.getX());
			assertEpsilonEquals(695., actual.getY());
		}

	}

	@DisplayName("newVector")
	@Nested
	public class NewVector {

		@Test
		@DisplayName("(Segment1D)")
		public void newVector() {
			var s = new Segment1DStub();
			var actual = factory.newVector(s);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(0., actual.getX());
			assertEpsilonEquals(0., actual.getY());
		}
	
		@Test
		@DisplayName("(Segment1D, double, double)")
		public void newVector_doubledouble() {
			var s = new Segment1DStub();
			var actual = factory.newVector(s, 154.487, 695.365);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(154.487, actual.getX());
			assertEpsilonEquals(695.365, actual.getY());
		}
	
		@Test
		@DisplayName("(Segment1D, int, int)")
		public void newVector_intint() {
			var s = new Segment1DStub();
			var actual = factory.newVector(s, 154, 695);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(154., actual.getX());
			assertEpsilonEquals(695., actual.getY());
		}

	}

}
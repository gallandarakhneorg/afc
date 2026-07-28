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

package org.arakhne.afc.math.geometry.d1.tests.afp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.d1.ImmutablePoint1D;
import org.arakhne.afc.math.geometry.base.d1.ImmutableVector1D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d1.afp.GeomFactory1afp;
import org.arakhne.afc.math.geometry.d1.d.DefaultSegment1d;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d1.d.Vector1d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public abstract class AbstractGeomFactory1afpTestCase<GF extends GeomFactory1afp> extends AbstractMathTestCase {

	private GF factory;

	private DefaultSegment1d segment;

	private Point2d start;

	private Point2d end;

	protected abstract GF createFactory();

	@BeforeEach
	public void setUp() {
		start = new Point2d(12., 65.);
		end = new Point2d(-45., 23.);
		factory = createFactory();
		segment = new DefaultSegment1d(start, end);
	}

	@DisplayName("convertToPoint")
	@Nested
	public class ConverToPoint {

		@DisplayName("(Point1D) immutable")
		@Test
		public void convertToPoint_immutablePoint1D() {
			var expected = new ImmutablePoint1D<DefaultSegment1d>(segment, 1., 2.);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(Point1D) mutable")
		public void convertToPoint_mutablePoint1D() {
			var expected = new Point1d(segment, 1., 2.);
			var actual = factory.convertToPoint(expected);
			assertSame(expected, actual);
		}
	
		@Test
		@DisplayName("(Vector1D) immutable")
		public void convertToPoint_immutableVector1D() {
			var expected = new ImmutableVector1D(segment, 1., 2.);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(Vector1D) mutable")
		public void convertToPoint_mutableVector1D() {
			var expected = new Vector1d(segment, 1., 2.);
			var actual = factory.convertToPoint(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	}

	@DisplayName("convertToVector")
	@Nested
	public class ConvertToVector {

		@Test
		@DisplayName("(Point1D) immutable")
		public void convertToVector_immutablePoint1D() {
			var expected = new ImmutablePoint1D<DefaultSegment1d>(segment, 1., 2.);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(Point1D) mutable")
		public void convertToVector_mutablePoint1D() {
			var expected = new Point1d(segment, 1., 2.);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(Vector1D) immutable")
		public void convertToVector_immutableVector1D() {
			var expected = new ImmutableVector1D<DefaultSegment1d>(segment, 1., 2.);
			var actual = factory.convertToVector(expected);
			assertNotSame(expected, actual);
			assertEpsilonEquals(expected, actual);
		}
	
		@Test
		@DisplayName("(Vector1D) mutable")
		public void convertToVector_mutableVector1D() {
			var expected = new Vector1d(segment, 1., 2.);
			var actual = factory.convertToVector(expected);
			assertSame(expected, actual);
		}
	}

	@DisplayName("newPoint")
	@Nested
	public class NewPoint {

		@Test
		@DisplayName("(Segment1D)")
		public void newPoint() {
			var s = new DefaultSegment1d(start, end);
			var actual = factory.newPoint(s);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(0., actual.getX());
			assertEpsilonEquals(0., actual.getY());
		}
	
		@Test
		@DisplayName("(Segment1D, double, double)")
		public void newPoint_doubledouble() {
			var s = new DefaultSegment1d(start, end);
			var actual = factory.newPoint(s, 154.487, 695.365);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(154.487, actual.getX());
			assertEpsilonEquals(695.365, actual.getY());
		}
	
		@Test
		@DisplayName("(Segment1D, int, int)")
		public void newPoint_intint() {
			var s = new DefaultSegment1d(start, end);
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
			var s = new DefaultSegment1d(start, end);
			var actual = factory.newVector(s);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(0., actual.getX());
			assertEpsilonEquals(0., actual.getY());
		}
	
		@Test
		@DisplayName("(Segment1D, double, double)")
		public void newVector_doubledouble() {
			var s = new DefaultSegment1d(start, end);
			var actual = factory.newVector(s, 154.487, 695.365);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(154.487, actual.getX());
			assertEpsilonEquals(695.365, actual.getY());
		}
	
		@Test
		@DisplayName("(Segment1D, int, int)")
		public void newVector_intint() {
			var s = new DefaultSegment1d(start, end);
			var actual = factory.newVector(s, 154, 695);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(154., actual.getX());
			assertEpsilonEquals(695., actual.getY());
		}
	}

	@DisplayName("newBox")
	@Nested
	public class NewBox {

		@Test
		@DisplayName("(Segment1D)")
		public void newBox() {
			var s = new DefaultSegment1d(start, end);
			var actual = factory.newBox(s);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(0., actual.getMinX());
			assertEpsilonEquals(0., actual.getMinY());
			assertEpsilonEquals(0., actual.getMaxX());
			assertEpsilonEquals(0., actual.getMaxY());
		}
	
		@Test
		@DisplayName("(Segment1D,double,double,double,double)")
		public void newBox_doubledoubledoubledouble() {
			var s = new DefaultSegment1d(start, end);
			var actual = factory.newBox(s, 154.487, 695.365, 7.25, 18.43);
			assertNotNull(actual);
			assertSame(s, actual.getSegment());
			assertEpsilonEquals(154.487, actual.getMinX());
			assertEpsilonEquals(695.365, actual.getMinY());
			assertEpsilonEquals(161.737, actual.getMaxX());
			assertEpsilonEquals(713.795, actual.getMaxY());
		}
	}

}

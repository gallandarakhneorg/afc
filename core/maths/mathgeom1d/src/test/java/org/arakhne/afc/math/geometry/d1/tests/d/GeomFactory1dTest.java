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

package org.arakhne.afc.math.geometry.d1.tests.d;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.d1.ImmutablePoint1D;
import org.arakhne.afc.math.geometry.base.d1.ImmutableVector1D;
import org.arakhne.afc.math.geometry.d1.d.DefaultSegment1d;
import org.arakhne.afc.math.geometry.d1.d.GeomFactory1d;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d1.d.Vector1d;
import org.arakhne.afc.math.geometry.d1.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Test factory of geometrical elements.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@SuppressWarnings("all")
public class GeomFactory1dTest extends AbstractMathTestCase {

	private GeomFactory1d factory;

	private DefaultSegment1d segment;

	private Point2d start;

	private Point2d end;

	@BeforeEach
	public void setUp() {
		this.start = new Point2d(12., 65.);
		this.end = new Point2d(-45., 23.);
		this.factory = new GeomFactory1d();
		this.segment = new DefaultSegment1d(this.start, this.end);
	}

	@Test
	@DisplayName("convertToPoint((Point1D) immutable)")
	public void convertToPoint_immutablePoint1D() {
		var expected = new ImmutablePoint1D<DefaultSegment1d>(this.segment, 1., 2.);
		var actual = this.factory.convertToPoint(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToPoint((Point1D) mutable)")
	public void convertToPoint_mutablePoint1D() {
		var expected = new Point1d(this.segment, 1., 2.);
		var actual = this.factory.convertToPoint(expected);
		assertSame(expected, actual);
	}

	@Test
	@DisplayName("convertToPoint((Vector1D) immutable)")
	public void convertToPoint_immutableVector1D() {
		var expected = new ImmutableVector1D(this.segment, 1., 2.);
		var actual = this.factory.convertToPoint(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToPoint((Vector1D) mutable)")
	public void convertToPoint_mutableVector1D() {
		var expected = new Vector1d(this.segment, 1., 2.);
		var actual = this.factory.convertToPoint(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToVector((Point1D) immutable)")
	public void convertToVector_immutablePoint1D() {
		var expected = new ImmutablePoint1D<DefaultSegment1d>(this.segment, 1., 2.);
		var actual = this.factory.convertToVector(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToVector((Point1D) mutable)")
	public void convertToVector_mutablePoint1D() {
		var expected = new Point1d(this.segment, 1., 2.);
		var actual = this.factory.convertToVector(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToVector((Vector1D) immutable)")
	public void convertToVector_immutableVector1D() {
		var expected = new ImmutableVector1D<DefaultSegment1d>(this.segment, 1., 2.);
		var actual = this.factory.convertToVector(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToVector((Vector1D) mutable)")
	public void convertToVector_mutableVector1D() {
		var expected = new Vector1d(this.segment, 1., 2.);
		var actual = this.factory.convertToVector(expected);
		assertSame(expected, actual);
	}

	@Test
	@DisplayName("newPoint(Segment1D)")
	public void newPoint() {
		var s = new DefaultSegment1d(this.start, this.end);
		var actual = this.factory.newPoint(s);
		assertNotNull(actual);
		assertSame(s, actual.getSegment());
		assertEpsilonEquals(0., actual.getX());
		assertEpsilonEquals(0., actual.getY());
	}

	@Test
	@DisplayName("newPoint(Segment1D, double, double)")
	public void newPoint_doubledouble() {
		var s = new DefaultSegment1d(this.start, this.end);
		var actual = this.factory.newPoint(s, 154.487, 695.365);
		assertNotNull(actual);
		assertSame(s, actual.getSegment());
		assertEpsilonEquals(154.487, actual.getX());
		assertEpsilonEquals(695.365, actual.getY());
	}

	@Test
	@DisplayName("newPoint(Segment1D, int, int)")
	public void newPoint_intint() {
		var s = new DefaultSegment1d(this.start, this.end);
		var actual = this.factory.newPoint(s, 154, 695);
		assertNotNull(actual);
		assertSame(s, actual.getSegment());
		assertEpsilonEquals(154., actual.getX());
		assertEpsilonEquals(695., actual.getY());
	}

	@Test
	@DisplayName("newVector(Segment1D)")
	public void newVector() {
		var s = new DefaultSegment1d(this.start, this.end);
		var actual = this.factory.newVector(s);
		assertNotNull(actual);
		assertSame(s, actual.getSegment());
		assertEpsilonEquals(0., actual.getX());
		assertEpsilonEquals(0., actual.getY());
	}

	@Test
	@DisplayName("newVector(Segment1D, double, double)")
	public void newVector_doubledouble() {
		var s = new DefaultSegment1d(this.start, this.end);
		var actual = this.factory.newVector(s, 154.487, 695.365);
		assertNotNull(actual);
		assertSame(s, actual.getSegment());
		assertEpsilonEquals(154.487, actual.getX());
		assertEpsilonEquals(695.365, actual.getY());
	}

	@Test
	@DisplayName("newVector(Segment1D, int, int)")
	public void newVector_intint() {
		var s = new DefaultSegment1d(this.start, this.end);
		var actual = this.factory.newVector(s, 154, 695);
		assertNotNull(actual);
		assertSame(s, actual.getSegment());
		assertEpsilonEquals(154., actual.getX());
		assertEpsilonEquals(695., actual.getY());
	}

}

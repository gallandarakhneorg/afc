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

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.d1.d.DefaultSegment1d;
import org.arakhne.afc.math.geometry.d1.d.GeomFactory1d;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d1.d.Vector1d;
import org.arakhne.afc.math.geometry.d1.tests.AbstractVector1DTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class Vector1dTest extends AbstractVector1DTestCase<Point1d, Vector1d, DefaultSegment1d> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}

	@Override
	public Vector1d createTuple(double x, double y) {
		return new Vector1d(getS(), x, y);
	}

	@Override
	public DefaultSegment1d createSegment() {
		return new DefaultSegment1d(
				new Point2d(125.3569, 14587.659),
				new Point2d(442.74158, 12473.93215));
	}

	@Override
	public void getGeomFactory() {
		assertSame(GeomFactory1d.SINGLETON, getT().getGeomFactory());
	}

	@Test
	@DisplayName("setSegment")
	public void setSegment() {
		var ns = createSegment();
		getT().setSegment(ns);
		assertSame(ns, getT().getSegment());
	}

	@Test
	@DisplayName("set(Segment1D, double, double)")
	public void set_Segment1Ddoubledouble() {
		var ns = createSegment();
		getT().set(ns, 245.6689, 24587.1224);
		assertSame(ns, getT().getSegment());
		assertEpsilonEquals(245.6689, getT().getX());
		assertEpsilonEquals(24587.1224, getT().getY());
	}

	@Test
	@DisplayName("convert(Tuple1D)")
	public void staticConvert() {
		var t = new Point1d(getS(), 456.4875, 5632.2445);
		var p = Vector1d.convert(t);
		assertNotSame(t, p);
		assertEpsilonEquals(t, p);
	}

	@Test
	@DisplayName("getLength")
	public void getLength() {
		assertEpsilonEquals(2.236067977499790, getT().getLength());
	}

	@Test
	@DisplayName("getLengthSquared")
	public void getLengthSquared() {
		assertEpsilonEquals(5., getT().getLengthSquared());
	}

	@Test
	@DisplayName("add(Vector1D, Vector1D)")
	public void add_Vector1DVector1D() {
		var v1 = new Vector1d(getS(), 123.447661, 68663.5);
		var v2 = new Vector1d(getS(), 458.4452, 7442.5);
		getT().add(v1, v2);
		var expected = new Vector1d(getS(), 123.447661 + 458.4452, 68663.5 + 7442.5);
		assertEpsilonEquals(expected, getT());
	}

	@Test
	@DisplayName("add(Vector1D)")
	public void add_Vector1D() {
		var v1 = new Vector1d(getS(), 123.447661, 68663.5);
		getT().add(v1);
		var expected = new Vector1d(getS(), 123.447661 + 1, 68663.5 - 2);
		assertEpsilonEquals(expected, getT());
	}

	@Test
	@DisplayName("scaleAdd(int, Vector1D, Vector1D)")
	public void scaleAdd_intVector1DVector1D() {
		var v1 = new Vector1d(getS(), 123.447661, 68663.5);
		var v2 = new Vector1d(getS(), 458.4452, 7442.5);
		getT().scaleAdd(2, v1, v2);
		var expected = new Vector1d(getS(), 2 * 123.447661 + 458.4452, 2 * 68663.5 + 7442.5);
		assertEpsilonEquals(expected, getT());
	}

	@Test
	@DisplayName("scaleAdd(double, Vector1D, Vector1D)")
	public void scaleAdd_doubleVector1DVector1D() {
		var v1 = new Vector1d(getS(), 123.447661, 68663.5);
		var v2 = new Vector1d(getS(), 458.4452, 7442.5);
		getT().scaleAdd(2.3, v1, v2);
		var expected = new Vector1d(getS(), 2.3 * 123.447661 + 458.4452, 2.3 * 68663.5 + 7442.5);
		assertEpsilonEquals(expected, getT());
	}

	@Test
	@DisplayName("scaleAdd(int, Vector1D)")
	public void scaleAdd_intVector1D() {
		var v1 = new Vector1d(getS(), 123.447661, 68663.5);
		getT().scaleAdd(2, v1);
		var expected = new Vector1d(getS(), 2 * 1 + 123.447661, 2 * (-2) + 68663.5);
		assertEpsilonEquals(expected, getT());
	}

	@Test
	@DisplayName("scaleAdd(double, Vector1D)")
	public void scaleAdd_doubleVector1D() {
		var v1 = new Vector1d(getS(), 123.447661, 68663.5);
		getT().scaleAdd(2.3, v1);
		var expected = new Vector1d(getS(), 2.3 * 1 + 123.447661, 2.3 * (-2) + 68663.5);
		assertEpsilonEquals(expected, getT());
	}

	@Test
	@DisplayName("sub(Vector1D, Vector1D)")
	public void sub_Vector1DVector1D() {
		var v1 = new Vector1d(getS(), 123.447661, 68663.5);
		var v2 = new Vector1d(getS(), 458.4452, 7442.5);
		getT().sub(v1, v2);
		var expected = new Vector1d(getS(), 123.447661 - 458.4452, 68663.5 - 7442.5);
		assertEpsilonEquals(expected, getT());
	}

	@Test
	@DisplayName("sub(Point1D, Point1D)")
	public void sub_Point1DPoint1D() {
		var v1 = new Point1d(getS(), 123.447661, 68663.5);
		var v2 = new Point1d(getS(), 458.4452, 7442.5);
		getT().sub(v1, v2);
		var expected = new Vector1d(getS(), 123.447661 - 458.4452, 68663.5 - 7442.5);
		assertEpsilonEquals(expected, getT());
	}

	@Test
	@DisplayName("sub(Vector1D)")
	public void sub_Vector1D() {
		var v1 = new Vector1d(getS(), 123.447661, 68663.5);
		getT().sub(v1);
		var expected = new Vector1d(getS(), 1 - 123.447661, -2 - 68663.5);
		assertEpsilonEquals(expected, getT());
	}

	@Test
	@DisplayName("setLength")
	public void setLength() {
		var original = new Vector1d(getT());

		getT().setLength(123.258456);

		assertEpsilonEquals(123.258456, getT().getLength());
		
		var changed = new Vector1d(getT());
		original.normalize();
		changed.normalize();
		assertEpsilonEquals(original, changed);
	}

}

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
import org.arakhne.afc.math.geometry.d1.tests.AbstractPoint1DTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class Point1dTest extends AbstractPoint1DTestCase<Point1d, Vector1d, DefaultSegment1d> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}

	@Override
	public Point1d createTuple(double x, double y) {
		return new Point1d(getS(), x, y);
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
		var t = new Vector1d(getS(), 456.4875, 5632.2445);
		var p = Point1d.convert(t);
		assertNotSame(t, p);
		assertEpsilonEquals(t, p);
	}

}

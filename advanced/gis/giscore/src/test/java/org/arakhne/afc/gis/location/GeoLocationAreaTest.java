/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.gis.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.AbstractGisTest;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/** Unit for for GeoLocation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class GeoLocationAreaTest extends AbstractGisTest {

	private Point2d coords11m;
	private Point2d coords11x;
	private Point2d coords12m;
	private Point2d coords12x;
	private Point2d coords21m;
	private Point2d coords21x;
	private Point2d coords22m;
	private Point2d coords22x;

	private GeoLocationArea location11;
	private GeoLocationArea location12;
	private GeoLocationArea location13;

	private GeoLocationArea location21;
	private GeoLocationArea location22;
	private GeoLocationArea location23;

	@BeforeEach
	public void setUp() {
		// build test data

		Point2d[] pts = new Point2d[8];
		for(int i=0; i<pts.length; ++i) {
			boolean b;
			do {
				pts[i] = randomPoint2D();
				b = false;
				for(int j=0; j<i; ++j) {
					b |= pts[i].equals(pts[j]);
				}
			}
			while(b);
		}
		this.coords11m = pts[0];
		this.coords11x = pts[1];
		this.coords12m = pts[2];
		this.coords12x = pts[3];
		this.coords21m = pts[4];
		this.coords21x = pts[5];
		this.coords22m = pts[6];
		this.coords22x = pts[7];

		// Creation of GeoLocations
		this.location11 = new GeoLocationArea(
				this.coords11m.getX(), this.coords11m.getY(),
				this.coords11x.getX(), this.coords11x.getY());
		this.location12 = new GeoLocationArea(
				this.coords11m.getX(), this.coords11m.getY(),
				this.coords11x.getX(), this.coords11x.getY());
		this.location13 = new GeoLocationArea(
				this.coords12m.getX(), this.coords12m.getY(),
				this.coords12x.getX(), this.coords12x.getY());

		this.location21 = new GeoLocationArea(
				this.coords21m.getX(), this.coords21m.getY(),
				this.coords21x.getX(), this.coords21x.getY());
		this.location22 = new GeoLocationArea(
				this.coords21m.getX(), this.coords21m.getY(),
				this.coords21x.getX(), this.coords21x.getY());
		this.location23 = new GeoLocationArea(
				this.coords22m.getX(), this.coords22m.getY(),
				this.coords22x.getX(), this.coords22x.getY());

		swap(this.coords11m, this.coords11x);
		swap(this.coords12m, this.coords12x);
		swap(this.coords21m, this.coords21x);
		swap(this.coords22m, this.coords22x);
	}

	@AfterEach
	public void tearDown() {
		this.location11 = null;
		this.location12 = null;
		this.location13 = null;
		this.location21 = null;
		this.location22 = null;
		this.location23 = null;
		this.coords11m = this.coords11x = this.coords12m = this.coords12x = null;
		this.coords21m = this.coords21x = this.coords22m = this.coords22x = null;
	}

	private static void swap(Point2d a, Point2d b) {
		double t;
		if (a.getX()>b.getX()) {
			t = a.getX();
			a.setX(b.getX());
			b.setX(t);
		}
		if (a.getY()>b.getY()) {
			t = a.getY();
			a.setY(b.getY());
			b.setY(t);
		}
	}

	@Test
	public void testEqualsStringString() {
		// Get string representations
		String geoid11 = this.location11.toString();
		String geoid12 = this.location12.toString();
		String geoid13 = this.location13.toString();

		String geoid21 = this.location21.toString();
		String geoid22 = this.location22.toString();
		String geoid23 = this.location23.toString();

		assertEquals(geoid11, geoid12);
		assertEquals(geoid12, geoid11);
		assertNotEquals(geoid11, geoid13);
		assertNotEquals(geoid12, geoid13);
		assertNotEquals(geoid13, geoid11);
		assertNotEquals(geoid13, geoid11);

		assertEquals(geoid21, geoid22);
		assertEquals(geoid22, geoid21);
		assertNotEquals(geoid21, geoid23);
		assertNotEquals(geoid22, geoid23);
		assertNotEquals(geoid23, geoid21);
		assertNotEquals(geoid23, geoid22);
	}

	@Test
	public void testEqualsGeoIdGeoId() {
		assertTrue(this.location11.equals(this.location12));
		assertTrue(this.location12.equals(this.location11));
		assertFalse(this.location11.equals(this.location13));
		assertFalse(this.location12.equals(this.location13));
		assertFalse(this.location13.equals(this.location11));
		assertFalse(this.location13.equals(this.location11));

		assertTrue(this.location21.equals(this.location22));
		assertTrue(this.location22.equals(this.location21));
		assertFalse(this.location21.equals(this.location23));
		assertFalse(this.location22.equals(this.location23));
		assertFalse(this.location23.equals(this.location21));
		assertFalse(this.location23.equals(this.location22));
	}

	@Test
	public void testEqualsGeoIdString() {
		// Get string representations
		String geoid11 = this.location11.toString();
		String geoid12 = this.location12.toString();
		String geoid13 = this.location13.toString();

		String geoid21 = this.location21.toString();
		String geoid22 = this.location22.toString();
		String geoid23 = this.location23.toString();

		assertTrue(this.location11.equals(geoid12));
		assertTrue(this.location12.equals(geoid11));
		assertFalse(this.location11.equals(geoid13));
		assertFalse(this.location12.equals(geoid13));
		assertFalse(this.location13.equals(geoid11));
		assertFalse(this.location13.equals(geoid11));

		assertTrue(this.location21.equals(geoid22));
		assertTrue(this.location22.equals(geoid21));
		assertFalse(this.location21.equals(geoid23));
		assertFalse(this.location22.equals(geoid23));
		assertFalse(this.location23.equals(geoid21));
		assertFalse(this.location23.equals(geoid22));
	}

	@Test
	public void testEqualsGeoIdUUID() {
		// Get string representations
		UUID uuid11 = this.location11.toUUID();
		UUID uuid12 = this.location12.toUUID();
		UUID uuid13 = this.location13.toUUID();

		UUID uuid21 = this.location21.toUUID();
		UUID uuid22 = this.location22.toUUID();
		UUID uuid23 = this.location23.toUUID();

		assertTrue(this.location11.equals(uuid12));
		assertTrue(this.location12.equals(uuid11));
		assertFalse(this.location11.equals(uuid13));
		assertFalse(this.location12.equals(uuid13));
		assertFalse(this.location13.equals(uuid11));
		assertFalse(this.location13.equals(uuid11));

		assertTrue(this.location21.equals(uuid22));
		assertTrue(this.location22.equals(uuid21));
		assertFalse(this.location21.equals(uuid23));
		assertFalse(this.location22.equals(uuid23));
		assertFalse(this.location23.equals(uuid21));
		assertFalse(this.location23.equals(uuid22));
	}

	@Test
	public void testEqualsGeoIdGeoLocation() {
		assertEquals(this.location11, this.location11);
		assertEquals(this.location11, this.location12);
		assertNotEquals(this.location11, this.location13);
		assertNotEquals(this.location11, this.location21);
		assertNotEquals(this.location11, this.location22);
		assertNotEquals(this.location11, this.location23);

		assertEquals(this.location12, this.location11);
		assertEquals(this.location12, this.location12);
		assertNotEquals(this.location12, this.location13);
		assertNotEquals(this.location12, this.location21);
		assertNotEquals(this.location12, this.location22);
		assertNotEquals(this.location12, this.location23);

		assertNotEquals(this.location13, this.location11);
		assertNotEquals(this.location13, this.location12);
		assertEquals(this.location13, this.location13);
		assertNotEquals(this.location13, this.location21);
		assertNotEquals(this.location13, this.location22);
		assertNotEquals(this.location13, this.location23);

		assertNotEquals(this.location21, this.location11);
		assertNotEquals(this.location21, this.location12);
		assertNotEquals(this.location21, this.location13);
		assertEquals(this.location21, this.location21);
		assertEquals(this.location21, this.location22);
		assertNotEquals(this.location21, this.location23);

		assertNotEquals(this.location22, this.location11);
		assertNotEquals(this.location22, this.location12);
		assertNotEquals(this.location22, this.location13);
		assertEquals(this.location22, this.location21);
		assertEquals(this.location22, this.location22);
		assertNotEquals(this.location22, this.location23);

		assertNotEquals(this.location23, this.location11);
		assertNotEquals(this.location23, this.location12);
		assertNotEquals(this.location23, this.location13);
		assertNotEquals(this.location23, this.location21);
		assertNotEquals(this.location23, this.location22);
		assertEquals(this.location23, this.location23);
	}

	@Test
	public void testHashCode() {
		// Get string representations
		int geoid11 = this.location11.hashCode();
		int geoid12 = this.location12.hashCode();
		int geoid13 = this.location13.hashCode();

		int geoid21 = this.location21.hashCode();
		int geoid22 = this.location22.hashCode();
		int geoid23 = this.location23.hashCode();

		assertEquals(geoid11, geoid12);
		assertEquals(geoid12, geoid11);
		assertNotEquals(geoid11, geoid13);
		assertNotEquals(geoid12, geoid13);
		assertNotEquals(geoid13, geoid11);
		assertNotEquals(geoid13, geoid11);

		assertEquals(geoid21, geoid22);
		assertEquals(geoid22, geoid21);
		assertNotEquals(geoid21, geoid23);
		assertNotEquals(geoid22, geoid23);
		assertNotEquals(geoid23, geoid21);
		assertNotEquals(geoid23, geoid22);
	}

	@Test
	public void testToString() {
		assertEquals(this.location11.toString(), this.location11.toString());
		assertEquals(this.location12.toString(), this.location11.toString());
		assertNotEquals(this.location13.toString(), this.location11.toString());
		assertNotEquals(this.location21.toString(), this.location11.toString());
		assertNotEquals(this.location22.toString(), this.location11.toString());
		assertNotEquals(this.location23.toString(), this.location11.toString());

		assertEquals(this.location11.toString(), this.location12.toString());
		assertEquals(this.location12.toString(), this.location12.toString());
		assertNotEquals(this.location13.toString(), this.location12.toString());
		assertNotEquals(this.location21.toString(), this.location12.toString());
		assertNotEquals(this.location22.toString(), this.location12.toString());
		assertNotEquals(this.location23.toString(), this.location12.toString());

		assertNotEquals(this.location11.toString(), this.location13.toString());
		assertNotEquals(this.location12.toString(), this.location13.toString());
		assertEquals(this.location13.toString(), this.location13.toString());
		assertNotEquals(this.location21.toString(), this.location13.toString());
		assertNotEquals(this.location22.toString(), this.location13.toString());
		assertNotEquals(this.location23.toString(), this.location13.toString());

		assertNotEquals(this.location11.toString(), this.location21.toString());
		assertNotEquals(this.location12.toString(), this.location21.toString());
		assertNotEquals(this.location13.toString(), this.location21.toString());
		assertEquals(this.location21.toString(), this.location21.toString());
		assertEquals(this.location22.toString(), this.location21.toString());
		assertNotEquals(this.location23.toString(), this.location21.toString());

		assertNotEquals(this.location11.toString(), this.location22.toString());
		assertNotEquals(this.location12.toString(), this.location22.toString());
		assertNotEquals(this.location13.toString(), this.location22.toString());
		assertEquals(this.location21.toString(), this.location22.toString());
		assertEquals(this.location22.toString(), this.location22.toString());
		assertNotEquals(this.location23.toString(), this.location22.toString());

		assertNotEquals(this.location11.toString(), this.location23.toString());
		assertNotEquals(this.location12.toString(), this.location23.toString());
		assertNotEquals(this.location13.toString(), this.location23.toString());
		assertNotEquals(this.location21.toString(), this.location23.toString());
		assertNotEquals(this.location22.toString(), this.location23.toString());
		assertEquals(this.location23.toString(), this.location23.toString());
	}

	@Test
	public void testToUUID() {
		assertEquals(this.location11.toUUID(), this.location11.toUUID());
		assertEquals(this.location12.toUUID(), this.location11.toUUID());
		assertNotEquals(this.location13.toUUID(), this.location11.toUUID());
		assertNotEquals(this.location21.toUUID(), this.location11.toUUID());
		assertNotEquals(this.location22.toUUID(), this.location11.toUUID());
		assertNotEquals(this.location23.toUUID(), this.location11.toUUID());

		assertEquals(this.location11.toUUID(), this.location12.toUUID());
		assertEquals(this.location12.toUUID(), this.location12.toUUID());
		assertNotEquals(this.location13.toUUID(), this.location12.toUUID());
		assertNotEquals(this.location21.toUUID(), this.location12.toUUID());
		assertNotEquals(this.location22.toUUID(), this.location12.toUUID());
		assertNotEquals(this.location23.toUUID(), this.location12.toUUID());

		assertNotEquals(this.location11.toUUID(), this.location13.toUUID());
		assertNotEquals(this.location12.toUUID(), this.location13.toUUID());
		assertEquals(this.location13.toUUID(), this.location13.toUUID());
		assertNotEquals(this.location21.toUUID(), this.location13.toUUID());
		assertNotEquals(this.location22.toUUID(), this.location13.toUUID());
		assertNotEquals(this.location23.toUUID(), this.location13.toUUID());

		assertNotEquals(this.location11.toUUID(), this.location21.toUUID());
		assertNotEquals(this.location12.toUUID(), this.location21.toUUID());
		assertNotEquals(this.location13.toUUID(), this.location21.toUUID());
		assertEquals(this.location21.toUUID(), this.location21.toUUID());
		assertEquals(this.location22.toUUID(), this.location21.toUUID());
		assertNotEquals(this.location23.toUUID(), this.location21.toUUID());

		assertNotEquals(this.location11.toUUID(), this.location22.toUUID());
		assertNotEquals(this.location12.toUUID(), this.location22.toUUID());
		assertNotEquals(this.location13.toUUID(), this.location22.toUUID());
		assertEquals(this.location21.toUUID(), this.location22.toUUID());
		assertEquals(this.location22.toUUID(), this.location22.toUUID());
		assertNotEquals(this.location23.toUUID(), this.location22.toUUID());

		assertNotEquals(this.location11.toUUID(), this.location23.toUUID());
		assertNotEquals(this.location12.toUUID(), this.location23.toUUID());
		assertNotEquals(this.location13.toUUID(), this.location23.toUUID());
		assertNotEquals(this.location21.toUUID(), this.location23.toUUID());
		assertNotEquals(this.location22.toUUID(), this.location23.toUUID());
		assertEquals(this.location23.toUUID(), this.location23.toUUID());
	}

	@Test
	public void testGetX() {
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11m.getX()), this.location11.getX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11m.getX()), this.location12.getX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12m.getX()), this.location13.getX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21m.getX()), this.location21.getX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21m.getX()), this.location22.getX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22m.getX()), this.location23.getX());
	}

	@Test
	public void testGetY() {
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11m.getY()), this.location11.getY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11m.getY()), this.location12.getY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12m.getY()), this.location13.getY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21m.getY()), this.location21.getY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21m.getY()), this.location22.getY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22m.getY()), this.location23.getY());
	}

	@Test
	public void testGetMinX() {
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11m.getX()), this.location11.getMinX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11m.getX()), this.location12.getMinX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12m.getX()), this.location13.getMinX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21m.getX()), this.location21.getMinX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21m.getX()), this.location22.getMinX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22m.getX()), this.location23.getMinX());
	}

	@Test
	public void testGetMinY() {
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11m.getY()), this.location11.getMinY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11m.getY()), this.location12.getMinY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12m.getY()), this.location13.getMinY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21m.getY()), this.location21.getMinY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21m.getY()), this.location22.getMinY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22m.getY()), this.location23.getMinY());
	}

	@Test
	public void testGetMaxX() {
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11x.getX()), this.location11.getMaxX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11x.getX()), this.location12.getMaxX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12x.getX()), this.location13.getMaxX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21x.getX()), this.location21.getMaxX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21x.getX()), this.location22.getMaxX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22x.getX()), this.location23.getMaxX());
	}

	@Test
	public void testGetMaxY() {
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11x.getY()), this.location11.getMaxY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11x.getY()), this.location12.getMaxY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12x.getY()), this.location13.getMaxY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21x.getY()), this.location21.getMaxY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21x.getY()), this.location22.getMaxY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22x.getY()), this.location23.getMaxY());
	}

	@Test
	public void testGetWidth() {
		setDecimalPrecision(0);
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11x.getX()-this.coords11m.getX()), this.location11.getWidth());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11x.getX()-this.coords11m.getX()), this.location12.getWidth());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12x.getX()-this.coords12m.getX()), this.location13.getWidth());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21x.getX()-this.coords21m.getX()), this.location21.getWidth());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21x.getX()-this.coords21m.getX()), this.location22.getWidth());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22x.getX()-this.coords22m.getX()), this.location23.getWidth());
		setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
	}

	@Test
	public void testGetHeight() {
		setDecimalPrecision(0);
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11x.getY()-this.coords11m.getY()), this.location11.getHeight());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11x.getY()-this.coords11m.getY()), this.location12.getHeight());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12x.getY()-this.coords12m.getY()), this.location13.getHeight());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21x.getY()-this.coords21m.getY()), this.location21.getHeight());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21x.getY()-this.coords21m.getY()), this.location22.getHeight());
		setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
	}

	@Test
	public void testToBounds2D() {
		assertEpsilonEquals(makeBounds(this.coords11m, this.coords11x), this.location11.toBounds2D());
		assertEpsilonEquals(makeBounds(this.coords11m, this.coords11x), this.location12.toBounds2D());
		assertEpsilonEquals(makeBounds(this.coords12m, this.coords12x), this.location13.toBounds2D());
		assertEpsilonEquals(makeBounds(this.coords21m, this.coords21x), this.location21.toBounds2D());
		assertEpsilonEquals(makeBounds(this.coords21m, this.coords21x), this.location22.toBounds2D());
		assertEpsilonEquals(makeBounds(this.coords22m, this.coords22x), this.location23.toBounds2D());

		assertEpsilonEquals(this.location11.toBounds2D(), this.location11.toBounds2D());
		assertEpsilonEquals(this.location12.toBounds2D(), this.location11.toBounds2D());
		assertNotEquals(this.location13.toBounds2D(), this.location11.toBounds2D());
		assertNotEquals(this.location21.toBounds2D(), this.location11.toBounds2D());
		assertNotEquals(this.location22.toBounds2D(), this.location11.toBounds2D());
		assertNotEquals(this.location23.toBounds2D(), this.location11.toBounds2D());

		assertEpsilonEquals(this.location11.toBounds2D(), this.location12.toBounds2D());
		assertEpsilonEquals(this.location12.toBounds2D(), this.location12.toBounds2D());
		assertNotEquals(this.location13.toBounds2D(), this.location12.toBounds2D());
		assertNotEquals(this.location21.toBounds2D(), this.location12.toBounds2D());
		assertNotEquals(this.location22.toBounds2D(), this.location12.toBounds2D());
		assertNotEquals(this.location23.toBounds2D(), this.location12.toBounds2D());

		assertNotEquals(this.location11.toBounds2D(), this.location13.toBounds2D());
		assertNotEquals(this.location12.toBounds2D(), this.location13.toBounds2D());
		assertEpsilonEquals(this.location13.toBounds2D(), this.location13.toBounds2D());
		assertNotEquals(this.location21.toBounds2D(), this.location13.toBounds2D());
		assertNotEquals(this.location22.toBounds2D(), this.location13.toBounds2D());
		assertNotEquals(this.location23.toBounds2D(), this.location13.toBounds2D());

		assertNotEquals(this.location11.toBounds2D(), this.location21.toBounds2D());
		assertNotEquals(this.location12.toBounds2D(), this.location21.toBounds2D());
		assertNotEquals(this.location13.toBounds2D(), this.location21.toBounds2D());
		assertEpsilonEquals(this.location21.toBounds2D(), this.location21.toBounds2D());
		assertEpsilonEquals(this.location22.toBounds2D(), this.location21.toBounds2D());
		assertNotEquals(this.location23.toBounds2D(), this.location21.toBounds2D());

		assertNotEquals(this.location11.toBounds2D(), this.location22.toBounds2D());
		assertNotEquals(this.location12.toBounds2D(), this.location22.toBounds2D());
		assertNotEquals(this.location13.toBounds2D(), this.location22.toBounds2D());
		assertEpsilonEquals(this.location21.toBounds2D(), this.location22.toBounds2D());
		assertEpsilonEquals(this.location22.toBounds2D(), this.location22.toBounds2D());
		assertNotEquals(this.location23.toBounds2D(), this.location22.toBounds2D());

		assertNotEquals(this.location11.toBounds2D(), this.location23.toBounds2D());
		assertNotEquals(this.location12.toBounds2D(), this.location23.toBounds2D());
		assertNotEquals(this.location13.toBounds2D(), this.location23.toBounds2D());
		assertNotEquals(this.location21.toBounds2D(), this.location23.toBounds2D());
		assertNotEquals(this.location22.toBounds2D(), this.location23.toBounds2D());
		assertEpsilonEquals(this.location23.toBounds2D(), this.location23.toBounds2D());
	}

}

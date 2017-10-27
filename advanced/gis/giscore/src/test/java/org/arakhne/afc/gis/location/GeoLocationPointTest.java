/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
public class GeoLocationPointTest extends AbstractGisTest {

	private Point2d coords11;
	private Point2d coords12;
	private Point2d coords13;
	private Point2d coords21;
	private Point2d coords22;
	private Point2d coords23;

	private GeoLocationPoint location11;
	private GeoLocationPoint location12;
	private GeoLocationPoint location13;

	private GeoLocationPoint location21;
	private GeoLocationPoint location22;
	private GeoLocationPoint location23;

	@Before
	public void setUp() {
		// build test data

		this.coords11 = randomPoint2D();
		this.coords12 = new Point2d(this.coords11);
		do {
			this.coords13 = randomPoint2D();
		}
		while(this.coords13.equals(this.coords11));

		do {
			this.coords21 = randomPoint2D();
		}
		while(this.coords21.equals(this.coords11) || this.coords21.equals(this.coords13));
		this.coords22 = new Point2d(this.coords21);
		do {
			this.coords23 = randomPoint2D();
		}
		while(this.coords23.equals(this.coords11) || this.coords23.equals(this.coords13)|| this.coords23.equals(this.coords21));

		// Creation of GeoLocations
		this.location11 = new GeoLocationPoint(this.coords11.getX(), this.coords11.getY());
		this.location12 = new GeoLocationPoint(this.coords12.getX(), this.coords12.getY());
		this.location13 = new GeoLocationPoint(this.coords13.getX(), this.coords13.getY());

		this.location21 = new GeoLocationPoint(this.coords21.getX(), this.coords21.getY());
		this.location22 = new GeoLocationPoint(this.coords22.getX(), this.coords22.getY());
		this.location23 = new GeoLocationPoint(this.coords23.getX(), this.coords23.getY());
	}

	@After
	public void tearDown() {
		this.location11 = null;
		this.location12 = null;
		this.location13 = null;
		this.location21 = null;
		this.location22 = null;
		this.location23 = null;
		this.coords11 = this.coords12 = this.coords13 = null;
		this.coords21 = this.coords22 = this.coords23 = null;
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
		Assert.assertEquals(this.location12.toUUID(), this.location12.toUUID());
		assertNotEquals(this.location13.toUUID(), this.location12.toUUID());
		assertNotEquals(this.location21.toUUID(), this.location12.toUUID());
		assertNotEquals(this.location22.toUUID(), this.location12.toUUID());
		assertNotEquals(this.location23.toUUID(), this.location12.toUUID());

		assertNotEquals(this.location11.toUUID(), this.location13.toUUID());
		assertNotEquals(this.location12.toUUID(), this.location13.toUUID());
		Assert.assertEquals(this.location13.toUUID(), this.location13.toUUID());
		assertNotEquals(this.location21.toUUID(), this.location13.toUUID());
		assertNotEquals(this.location22.toUUID(), this.location13.toUUID());
		assertNotEquals(this.location23.toUUID(), this.location13.toUUID());

		assertNotEquals(this.location11.toUUID(), this.location21.toUUID());
		assertNotEquals(this.location12.toUUID(), this.location21.toUUID());
		assertNotEquals(this.location13.toUUID(), this.location21.toUUID());
		Assert.assertEquals(this.location21.toUUID(), this.location21.toUUID());
		Assert.assertEquals(this.location22.toUUID(), this.location21.toUUID());
		assertNotEquals(this.location23.toUUID(), this.location21.toUUID());

		assertNotEquals(this.location11.toUUID(), this.location22.toUUID());
		assertNotEquals(this.location12.toUUID(), this.location22.toUUID());
		assertNotEquals(this.location13.toUUID(), this.location22.toUUID());
		Assert.assertEquals(this.location21.toUUID(), this.location22.toUUID());
		Assert.assertEquals(this.location22.toUUID(), this.location22.toUUID());
		assertNotEquals(this.location23.toUUID(), this.location22.toUUID());

		assertNotEquals(this.location11.toUUID(), this.location23.toUUID());
		assertNotEquals(this.location12.toUUID(), this.location23.toUUID());
		assertNotEquals(this.location13.toUUID(), this.location23.toUUID());
		assertNotEquals(this.location21.toUUID(), this.location23.toUUID());
		assertNotEquals(this.location22.toUUID(), this.location23.toUUID());
		Assert.assertEquals(this.location23.toUUID(), this.location23.toUUID());
	}

	@Test
	public void testGetX() {
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11.getX()), this.location11.getX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12.getX()), this.location12.getX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords13.getX()), this.location13.getX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21.getX()), this.location21.getX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22.getX()), this.location22.getX());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords23.getX()), this.location23.getX());
	}

	@Test
	public void testGetY() {
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11.getY()), this.location11.getY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12.getY()), this.location12.getY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords13.getY()), this.location13.getY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21.getY()), this.location21.getY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22.getY()), this.location22.getY());
		assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords23.getY()), this.location23.getY());
	}

	@Test
	public void testToBounds2D() {
		assertEpsilonEquals(makeBounds(this.coords11), this.location11.toBounds2D());
		assertEpsilonEquals(makeBounds(this.coords12), this.location12.toBounds2D());
		assertEpsilonEquals(makeBounds(this.coords13), this.location13.toBounds2D());
		assertEpsilonEquals(makeBounds(this.coords21), this.location21.toBounds2D());
		assertEpsilonEquals(makeBounds(this.coords22), this.location22.toBounds2D());
		assertEpsilonEquals(makeBounds(this.coords23), this.location23.toBounds2D());

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

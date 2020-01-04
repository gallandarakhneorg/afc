/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

/** Unit for for GeoLocation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class GeoLocationNowhereTest extends AbstractGisTest {

	private UUID uuid11;
	private UUID uuid12;
	private UUID uuid13;
	private UUID uuid21;
	private UUID uuid22;
	private UUID uuid23;

	private GeoLocationNowhere location11;
	private GeoLocationNowhere location12;
	private GeoLocationNowhere location13;

	private GeoLocationNowhere location21;
	private GeoLocationNowhere location22;
	private GeoLocationNowhere location23;

	@BeforeEach
	public void setUp() {
		// build test data

		this.uuid11 = UUID.randomUUID();
		this.uuid12 = new UUID(this.uuid11.getMostSignificantBits(), this.uuid11.getLeastSignificantBits());
		this.uuid13 = UUID.randomUUID();

		this.uuid21 = UUID.randomUUID();
		this.uuid22 = new UUID(this.uuid21.getMostSignificantBits(), this.uuid21.getLeastSignificantBits());
		this.uuid23 = UUID.randomUUID();

		// Creation of GeoLocations
		this.location11 = new GeoLocationNowhere(this.uuid11);
		this.location12 = new GeoLocationNowhere(this.uuid12);
		this.location13 = new GeoLocationNowhere(this.uuid13);

		this.location21 = new GeoLocationNowhere(this.uuid21);
		this.location22 = new GeoLocationNowhere(this.uuid22);
		this.location23 = new GeoLocationNowhere(this.uuid23);
	}

	@AfterEach
	public void tearDown() {
		this.location11 = null;
		this.location12 = null;
		this.location13 = null;
		this.location21 = null;
		this.location22 = null;
		this.location23 = null;
		this.uuid11 = this.uuid12 = this.uuid13 = null;
		this.uuid21 = this.uuid22 = this.uuid23 = null;
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
	public void testToBounds2D() {
		assertTrue(this.location11.toBounds2D().isEmpty());
		assertTrue(this.location12.toBounds2D().isEmpty());
		assertTrue(this.location13.toBounds2D().isEmpty());
		assertTrue(this.location21.toBounds2D().isEmpty());
		assertTrue(this.location22.toBounds2D().isEmpty());
		assertTrue(this.location23.toBounds2D().isEmpty());
	}

}

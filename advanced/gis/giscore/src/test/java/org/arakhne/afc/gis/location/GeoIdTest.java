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
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.gis.AbstractGisTest;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** Unit for for GeoLocation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class GeoIdTest extends AbstractGisTest {

	private GeoLocationPointList loc11;
	private GeoLocationPointList loc12;
	private GeoLocationPointList loc13;

	private GeoLocationPointList loc21;
	private GeoLocationPointList loc22;
	private GeoLocationPointList loc23;

	private GeoId id11;
	private GeoId id12;
	private GeoId id13;

	private GeoId id21;
	private GeoId id22;
	private GeoId id23;

	@Before
	public void setUp() {
		// build test data

		int count1 = 50;
		int count2 = 44;

		double[] coords11 = new double[count1];
		double[] coords12 = new double[count1];
		double[] coords13 = new double[count1];
		double coord;

		for(int j=0; j<count1-1; j+=2) {
			coord = Integer.valueOf(j).doubleValue();
			coords11[j] = coords11[j+1] = coord;
			coords12[count1-j-1] = coords12[count1-j-2] = coord;

			coord = Integer.valueOf(j * 100).doubleValue() + count1;
			coords13[j] = coords13[j+1] = coord;
		}

		double[] coords21 = new double[count2];
		double[] coords22 = new double[count2];
		double[] coords23 = new double[count2];

		for(int j=0; j<count2-1; j+=2) {
			coord = Integer.valueOf(j + count1).doubleValue();
			coords21[j] = coords21[j+1] = coord;
			coords22[count2-j-1] = coords22[count2-j-2] = coord;

			coord = Integer.valueOf(j * 100).doubleValue() + count1 + count2;
			coords23[j] = coords23[j+1] = coord;
		}

		// Creation of GeoLocations
		this.id11 = (this.loc11=new GeoLocationPointList(coords11)).toGeoId();
		this.id12 = (this.loc12=new GeoLocationPointList(coords12)).toGeoId();
		this.id13 = (this.loc13=new GeoLocationPointList(coords13)).toGeoId();

		this.id21 = (this.loc21=new GeoLocationPointList(coords21)).toGeoId();
		this.id22 = (this.loc22=new GeoLocationPointList(coords22)).toGeoId();
		this.id23 = (this.loc23=new GeoLocationPointList(coords23)).toGeoId();
	}

	@After
	public void tearDown() {
		this.id11 = null;
		this.id12 = null;
		this.id13 = null;
		this.id21 = null;
		this.id22 = null;
		this.id23 = null;
		this.loc11 = null;
		this.loc12 = null;
		this.loc13 = null;
		this.loc21 = null;
		this.loc22 = null;
		this.loc23 = null;
	}

	private static String makeInternalId(GeoLocationPointList g) {
		return GeoLocationUtil.makeInternalId(g.toArray(), null);
	}

	private static String makeStringId(GeoLocationPointList g) {
		Rectangle2d b = new Rectangle2d();
		String ii = GeoLocationUtil.makeInternalId(g.toArray(), b);
		StringBuilder geoid = new StringBuilder(ii);
		geoid.append('#');
		geoid.append((long)Math.floor(b.getMinX()));
		geoid.append(';');
		geoid.append((long)Math.floor(b.getMinY()));
		geoid.append(';');
		geoid.append((long)Math.ceil(b.getMaxX()));
		geoid.append(';');
		geoid.append((long)Math.ceil(b.getMaxY()));
		return geoid.toString();
	}

	private static Rectangle2d makeBounds(GeoLocationPointList g) {
		Rectangle2d b = new Rectangle2d();
		GeoLocationUtil.makeInternalId(g.toArray(), b);
		return b;
	}

	@Test
	public void testEqualsStringString() {
		// Get string representations
		String geoid11 = this.id11.toString();
		String geoid12 = this.id12.toString();
		String geoid13 = this.id13.toString();

		String geoid21 = this.id21.toString();
		String geoid22 = this.id22.toString();
		String geoid23 = this.id23.toString();

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
		assertTrue(this.id11.equals(this.id12));
		assertTrue(this.id12.equals(this.id11));
		assertFalse(this.id11.equals(this.id13));
		assertFalse(this.id12.equals(this.id13));
		assertFalse(this.id13.equals(this.id11));
		assertFalse(this.id13.equals(this.id11));

		assertTrue(this.id21.equals(this.id22));
		assertTrue(this.id22.equals(this.id21));
		assertFalse(this.id21.equals(this.id23));
		assertFalse(this.id22.equals(this.id23));
		assertFalse(this.id23.equals(this.id21));
		assertFalse(this.id23.equals(this.id22));
	}

	@Test
	public void testEqualsGeoIdString() {
		// Get string representations
		String geoid11 = this.id11.toString();
		String geoid12 = this.id12.toString();
		String geoid13 = this.id13.toString();

		String geoid21 = this.id21.toString();
		String geoid22 = this.id22.toString();
		String geoid23 = this.id23.toString();

		assertTrue(this.id11.equals(geoid12));
		assertTrue(this.id12.equals(geoid11));
		assertFalse(this.id11.equals(geoid13));
		assertFalse(this.id12.equals(geoid13));
		assertFalse(this.id13.equals(geoid11));
		assertFalse(this.id13.equals(geoid11));

		assertTrue(this.id21.equals(geoid22));
		assertTrue(this.id22.equals(geoid21));
		assertFalse(this.id21.equals(geoid23));
		assertFalse(this.id22.equals(geoid23));
		assertFalse(this.id23.equals(geoid21));
		assertFalse(this.id23.equals(geoid22));
	}

	@Test
	public void testEqualsGeoIdUUID() {
		// Get string representations
		UUID uuid11 = this.id11.toUUID();
		UUID uuid12 = this.id12.toUUID();
		UUID uuid13 = this.id13.toUUID();

		UUID uuid21 = this.id21.toUUID();
		UUID uuid22 = this.id22.toUUID();
		UUID uuid23 = this.id23.toUUID();

		assertTrue(this.id11.equals(uuid12));
		assertTrue(this.id12.equals(uuid11));
		assertFalse(this.id11.equals(uuid13));
		assertFalse(this.id12.equals(uuid13));
		assertFalse(this.id13.equals(uuid11));
		assertFalse(this.id13.equals(uuid11));

		assertTrue(this.id21.equals(uuid22));
		assertTrue(this.id22.equals(uuid21));
		assertFalse(this.id21.equals(uuid23));
		assertFalse(this.id22.equals(uuid23));
		assertFalse(this.id23.equals(uuid21));
		assertFalse(this.id23.equals(uuid22));
	}

	@Test
	public void testEqualsGeoIdGeoLocation() {
		assertEquals(this.id11, this.loc11);
		assertEquals(this.id11, this.loc12);
		assertNotEquals(this.id11, this.loc13);
		assertNotEquals(this.id11, this.loc21);
		assertNotEquals(this.id11, this.loc22);
		assertNotEquals(this.id11, this.loc23);

		assertEquals(this.id12, this.loc11);
		assertEquals(this.id12, this.loc12);
		assertNotEquals(this.id12, this.loc13);
		assertNotEquals(this.id12, this.loc21);
		assertNotEquals(this.id12, this.loc22);
		assertNotEquals(this.id12, this.loc23);

		assertNotEquals(this.id13, this.loc11);
		assertNotEquals(this.id13, this.loc12);
		assertEquals(this.id13, this.loc13);
		assertNotEquals(this.id13, this.loc21);
		assertNotEquals(this.id13, this.loc22);
		assertNotEquals(this.id13, this.loc23);

		assertNotEquals(this.id21, this.loc11);
		assertNotEquals(this.id21, this.loc12);
		assertNotEquals(this.id21, this.loc13);
		assertEquals(this.id21, this.loc21);
		assertEquals(this.id21, this.loc22);
		assertNotEquals(this.id21, this.loc23);

		assertNotEquals(this.id22, this.loc11);
		assertNotEquals(this.id22, this.loc12);
		assertNotEquals(this.id22, this.loc13);
		assertEquals(this.id22, this.loc21);
		assertEquals(this.id22, this.loc22);
		assertNotEquals(this.id22, this.loc23);

		assertNotEquals(this.id23, this.loc11);
		assertNotEquals(this.id23, this.loc12);
		assertNotEquals(this.id23, this.loc13);
		assertNotEquals(this.id23, this.loc21);
		assertNotEquals(this.id23, this.loc22);
		assertEquals(this.id23, this.loc23);
	}

	@Test
	public void testGetInternalId() {
		assertEquals(makeInternalId(this.loc11), this.id11.getInternalId());
		assertEquals(makeInternalId(this.loc12), this.id12.getInternalId());
		assertEquals(makeInternalId(this.loc13), this.id13.getInternalId());
		assertEquals(makeInternalId(this.loc21), this.id21.getInternalId());
		assertEquals(makeInternalId(this.loc22), this.id22.getInternalId());
		assertEquals(makeInternalId(this.loc23), this.id23.getInternalId());
	}

	@Test
	public void testHashCode() {
		// Get string representations
		int geoid11 = this.id11.hashCode();
		int geoid12 = this.id12.hashCode();
		int geoid13 = this.id13.hashCode();

		int geoid21 = this.id21.hashCode();
		int geoid22 = this.id22.hashCode();
		int geoid23 = this.id23.hashCode();

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
		assertEquals(makeStringId(this.loc11), this.id11.toString());
		assertEquals(makeStringId(this.loc12), this.id12.toString());
		assertEquals(makeStringId(this.loc13), this.id13.toString());
		assertEquals(makeStringId(this.loc21), this.id21.toString());
		assertEquals(makeStringId(this.loc22), this.id22.toString());
		assertEquals(makeStringId(this.loc23), this.id23.toString());

		assertEquals(this.id11.toString(), this.id11.toString());
		assertEquals(this.id12.toString(), this.id11.toString());
		assertNotEquals(this.id13.toString(), this.id11.toString());
		assertNotEquals(this.id21.toString(), this.id11.toString());
		assertNotEquals(this.id22.toString(), this.id11.toString());
		assertNotEquals(this.id23.toString(), this.id11.toString());

		assertEquals(this.id11.toString(), this.id12.toString());
		assertEquals(this.id12.toString(), this.id12.toString());
		assertNotEquals(this.id13.toString(), this.id12.toString());
		assertNotEquals(this.id21.toString(), this.id12.toString());
		assertNotEquals(this.id22.toString(), this.id12.toString());
		assertNotEquals(this.id23.toString(), this.id12.toString());

		assertNotEquals(this.id11.toString(), this.id13.toString());
		assertNotEquals(this.id12.toString(), this.id13.toString());
		assertEquals(this.id13.toString(), this.id13.toString());
		assertNotEquals(this.id21.toString(), this.id13.toString());
		assertNotEquals(this.id22.toString(), this.id13.toString());
		assertNotEquals(this.id23.toString(), this.id13.toString());

		assertNotEquals(this.id11.toString(), this.id21.toString());
		assertNotEquals(this.id12.toString(), this.id21.toString());
		assertNotEquals(this.id13.toString(), this.id21.toString());
		assertEquals(this.id21.toString(), this.id21.toString());
		assertEquals(this.id22.toString(), this.id21.toString());
		assertNotEquals(this.id23.toString(), this.id21.toString());

		assertNotEquals(this.id11.toString(), this.id22.toString());
		assertNotEquals(this.id12.toString(), this.id22.toString());
		assertNotEquals(this.id13.toString(), this.id22.toString());
		assertEquals(this.id21.toString(), this.id22.toString());
		assertEquals(this.id22.toString(), this.id22.toString());
		assertNotEquals(this.id23.toString(), this.id22.toString());

		assertNotEquals(this.id11.toString(), this.id23.toString());
		assertNotEquals(this.id12.toString(), this.id23.toString());
		assertNotEquals(this.id13.toString(), this.id23.toString());
		assertNotEquals(this.id21.toString(), this.id23.toString());
		assertNotEquals(this.id22.toString(), this.id23.toString());
		assertEquals(this.id23.toString(), this.id23.toString());
	}

	@Test
	public void testToUUID() {
		assertEquals(UUID.nameUUIDFromBytes(makeStringId(this.loc11).getBytes()), this.id11.toUUID());
		assertEquals(UUID.nameUUIDFromBytes(makeStringId(this.loc12).getBytes()), this.id12.toUUID());
		assertEquals(UUID.nameUUIDFromBytes(makeStringId(this.loc13).getBytes()), this.id13.toUUID());
		assertEquals(UUID.nameUUIDFromBytes(makeStringId(this.loc21).getBytes()), this.id21.toUUID());
		assertEquals(UUID.nameUUIDFromBytes(makeStringId(this.loc22).getBytes()), this.id22.toUUID());
		assertEquals(UUID.nameUUIDFromBytes(makeStringId(this.loc23).getBytes()), this.id23.toUUID());

		assertEquals(this.id11.toUUID(), this.id11.toUUID());
		assertEquals(this.id12.toUUID(), this.id11.toUUID());
		assertNotEquals(this.id13.toUUID(), this.id11.toUUID());
		assertNotEquals(this.id21.toUUID(), this.id11.toUUID());
		assertNotEquals(this.id22.toUUID(), this.id11.toUUID());
		assertNotEquals(this.id23.toUUID(), this.id11.toUUID());

		assertEquals(this.id11.toUUID(), this.id12.toUUID());
		assertEquals(this.id12.toUUID(), this.id12.toUUID());
		assertNotEquals(this.id13.toUUID(), this.id12.toUUID());
		assertNotEquals(this.id21.toUUID(), this.id12.toUUID());
		assertNotEquals(this.id22.toUUID(), this.id12.toUUID());
		assertNotEquals(this.id23.toUUID(), this.id12.toUUID());

		assertNotEquals(this.id11.toUUID(), this.id13.toUUID());
		assertNotEquals(this.id12.toUUID(), this.id13.toUUID());
		assertEquals(this.id13.toUUID(), this.id13.toUUID());
		assertNotEquals(this.id21.toUUID(), this.id13.toUUID());
		assertNotEquals(this.id22.toUUID(), this.id13.toUUID());
		assertNotEquals(this.id23.toUUID(), this.id13.toUUID());

		assertNotEquals(this.id11.toUUID(), this.id21.toUUID());
		assertNotEquals(this.id12.toUUID(), this.id21.toUUID());
		assertNotEquals(this.id13.toUUID(), this.id21.toUUID());
		assertEquals(this.id21.toUUID(), this.id21.toUUID());
		assertEquals(this.id22.toUUID(), this.id21.toUUID());
		assertNotEquals(this.id23.toUUID(), this.id21.toUUID());

		assertNotEquals(this.id11.toUUID(), this.id22.toUUID());
		assertNotEquals(this.id12.toUUID(), this.id22.toUUID());
		assertNotEquals(this.id13.toUUID(), this.id22.toUUID());
		assertEquals(this.id21.toUUID(), this.id22.toUUID());
		assertEquals(this.id22.toUUID(), this.id22.toUUID());
		assertNotEquals(this.id23.toUUID(), this.id22.toUUID());

		assertNotEquals(this.id11.toUUID(), this.id23.toUUID());
		assertNotEquals(this.id12.toUUID(), this.id23.toUUID());
		assertNotEquals(this.id13.toUUID(), this.id23.toUUID());
		assertNotEquals(this.id21.toUUID(), this.id23.toUUID());
		assertNotEquals(this.id22.toUUID(), this.id23.toUUID());
		assertEquals(this.id23.toUUID(), this.id23.toUUID());
	}

	@Test
	public void testToBounds2D() {
		assertEpsilonEquals(makeBounds(this.loc11), this.id11.toBounds2D());
		assertEpsilonEquals(makeBounds(this.loc12), this.id12.toBounds2D());
		assertEpsilonEquals(makeBounds(this.loc13), this.id13.toBounds2D());
		assertEpsilonEquals(makeBounds(this.loc21), this.id21.toBounds2D());
		assertEpsilonEquals(makeBounds(this.loc22), this.id22.toBounds2D());
		assertEpsilonEquals(makeBounds(this.loc23), this.id23.toBounds2D());

		assertEpsilonEquals(this.id11.toBounds2D(), this.id11.toBounds2D());
		assertEpsilonEquals(this.id12.toBounds2D(), this.id11.toBounds2D());
		assertNotEquals(this.id13.toBounds2D(), this.id11.toBounds2D());
		assertNotEquals(this.id21.toBounds2D(), this.id11.toBounds2D());
		assertNotEquals(this.id22.toBounds2D(), this.id11.toBounds2D());
		assertNotEquals(this.id23.toBounds2D(), this.id11.toBounds2D());

		assertEpsilonEquals(this.id11.toBounds2D(), this.id12.toBounds2D());
		assertEpsilonEquals(this.id12.toBounds2D(), this.id12.toBounds2D());
		assertNotEquals(this.id13.toBounds2D(), this.id12.toBounds2D());
		assertNotEquals(this.id21.toBounds2D(), this.id12.toBounds2D());
		assertNotEquals(this.id22.toBounds2D(), this.id12.toBounds2D());
		assertNotEquals(this.id23.toBounds2D(), this.id12.toBounds2D());

		assertNotEquals(this.id11.toBounds2D(), this.id13.toBounds2D());
		assertNotEquals(this.id12.toBounds2D(), this.id13.toBounds2D());
		assertEpsilonEquals(this.id13.toBounds2D(), this.id13.toBounds2D());
		assertNotEquals(this.id21.toBounds2D(), this.id13.toBounds2D());
		assertNotEquals(this.id22.toBounds2D(), this.id13.toBounds2D());
		assertNotEquals(this.id23.toBounds2D(), this.id13.toBounds2D());

		assertNotEquals(this.id11.toBounds2D(), this.id21.toBounds2D());
		assertNotEquals(this.id12.toBounds2D(), this.id21.toBounds2D());
		assertNotEquals(this.id13.toBounds2D(), this.id21.toBounds2D());
		assertEpsilonEquals(this.id21.toBounds2D(), this.id21.toBounds2D());
		assertEpsilonEquals(this.id22.toBounds2D(), this.id21.toBounds2D());
		assertNotEquals(this.id23.toBounds2D(), this.id21.toBounds2D());

		assertNotEquals(this.id11.toBounds2D(), this.id22.toBounds2D());
		assertNotEquals(this.id12.toBounds2D(), this.id22.toBounds2D());
		assertNotEquals(this.id13.toBounds2D(), this.id22.toBounds2D());
		assertEpsilonEquals(this.id21.toBounds2D(), this.id22.toBounds2D());
		assertEpsilonEquals(this.id22.toBounds2D(), this.id22.toBounds2D());
		assertNotEquals(this.id23.toBounds2D(), this.id22.toBounds2D());

		assertNotEquals(this.id11.toBounds2D(), this.id23.toBounds2D());
		assertNotEquals(this.id12.toBounds2D(), this.id23.toBounds2D());
		assertNotEquals(this.id13.toBounds2D(), this.id23.toBounds2D());
		assertNotEquals(this.id21.toBounds2D(), this.id23.toBounds2D());
		assertNotEquals(this.id22.toBounds2D(), this.id23.toBounds2D());
		assertEpsilonEquals(this.id23.toBounds2D(), this.id23.toBounds2D());
	}

}

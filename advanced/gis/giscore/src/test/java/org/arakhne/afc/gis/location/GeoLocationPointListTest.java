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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.AbstractGisTest;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
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
public class GeoLocationPointListTest extends AbstractGisTest {

	private int count1;
	private int count2;

	private double[] coords11;
	private double[] coords12;
	private double[] coords13;
	private double[] coords21;
	private double[] coords22;
	private double[] coords23;

	private Rectangle2d b11;
	private Rectangle2d b12;
	private Rectangle2d b13;
	private Rectangle2d b21;
	private Rectangle2d b22;
	private Rectangle2d b23;

	private GeoLocationPointList location11;
	private GeoLocationPointList location12;
	private GeoLocationPointList location13;

	private GeoLocationPointList location21;
	private GeoLocationPointList location22;
	private GeoLocationPointList location23;

	@BeforeEach
	public void setUp() {
		// build test data

		this.count1 = 50;
		this.count2 = 44;

		this.b11 = new Rectangle2d();
		this.b12 = new Rectangle2d();
		this.b13 = new Rectangle2d();
		this.b21 = new Rectangle2d();
		this.b22 = new Rectangle2d();
		this.b23 = new Rectangle2d();

		this.coords11 = new double[this.count1];
		this.coords12 = new double[this.count1];
		this.coords13 = new double[this.count1];
		double coord;

		for(int j=0; j<this.count1-1; j+=2) {
			coord = Integer.valueOf(j).doubleValue();
			this.coords11[j] = this.coords11[j+1] = coord;
			this.coords12[this.count1-j-1] = this.coords12[this.count1-j-2] = coord;

			coord = Integer.valueOf(j * 100).doubleValue() + this.count1;
			this.coords13[j] = this.coords13[j+1] = coord;

			Point2d pts1 = new Point2d(this.coords11[j], this.coords11[j+1]);
			Point2d pts2 = new Point2d(this.coords12[this.count1-j-1], this.coords12[this.count1-j-2]);
			Point2d pts3 = new Point2d(this.coords13[j], this.coords13[j+1]);

			if (j == 0) {
				this.b11.set(pts1, pts1);
				this.b12.set(pts2, pts2);
				this.b13.set(pts3, pts3);
			} else {
				this.b11.add(pts1);
				this.b12.add(pts2);
				this.b13.add(pts3);
			}
		}

		this.coords21 = new double[this.count2];
		this.coords22 = new double[this.count2];
		this.coords23 = new double[this.count2];

		for(int j=0; j<this.count2-1; j+=2) {
			coord = Integer.valueOf(j + this.count1).doubleValue();
			this.coords21[j] = this.coords21[j+1] = coord;
			this.coords22[this.count2-j-1] = this.coords22[this.count2-j-2] = coord;

			coord = Integer.valueOf(j * 100).doubleValue() + this.count1 + this.count2;
			this.coords23[j] = this.coords23[j+1] = coord;

			Point2d pts1 = new Point2d(this.coords21[j], this.coords21[j+1]);
			Point2d pts2 = new Point2d(this.coords22[this.count2-j-1], this.coords22[this.count2-j-2]);
			Point2d pts3 = new Point2d(this.coords23[j], this.coords23[j+1]);

			if (j == 0) {
				this.b21.set(pts1, pts1);
				this.b22.set(pts2, pts2);
				this.b23.set(pts3, pts3);
			} else {
				this.b21.add(pts1);
				this.b22.add(pts2);
				this.b23.add(pts3);
			}
		}

		// Creation of GeoLocations
		this.location11 = new GeoLocationPointList(this.coords11);
		this.location12 = new GeoLocationPointList(this.coords12);
		this.location13 = new GeoLocationPointList(this.coords13);

		this.location21 = new GeoLocationPointList(this.coords21);
		this.location22 = new GeoLocationPointList(this.coords22);
		this.location23 = new GeoLocationPointList(this.coords23);
	}

	@AfterEach
	public void tearDown() {
		this.location11 = null;
		this.location12 = null;
		this.location13 = null;
		this.location21 = null;
		this.location22 = null;
		this.location23 = null;
		this.coords11 = this.coords12 = this.coords13 = null;
		this.coords21 = this.coords22 = this.coords23 = null;
		this.b11 = this.b12 = this.b13 = null;
		this.b21 = this.b22 = this.b23 = null;
	}

	private static double[] reverse(double[] t) {
		double[] tt = new double[t.length];
		for(int i=0,j=t.length-1; i<t.length; ++i,--j) {
			tt[j] = t[i];
		}
		return tt;
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
	public void testSize() {
		assertEquals(this.count1/2, this.location11.size());
		assertEquals(this.count1/2, this.location12.size());
		assertEquals(this.count1/2, this.location13.size());
		assertEquals(this.count2/2, this.location21.size());
		assertEquals(this.count2/2, this.location22.size());
		assertEquals(this.count2/2, this.location23.size());
	}

	@Test
	public void testGetX() {
		for(int i=0; i<this.location11.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11[i*2]), this.location11.getX(i));
		}
		for(int i=0; i<this.location12.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12[this.coords12.length-(i*2)-1]), this.location12.getX(i));
		}
		for(int i=0; i<this.location13.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords13[i*2]), this.location13.getX(i));
		}
		for(int i=0; i<this.location21.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21[i*2]), this.location21.getX(i));
		}
		for(int i=0; i<this.location22.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22[this.coords22.length-(i*2)-1]), this.location22.getX(i));
		}
		for(int i=0; i<this.location23.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords23[i*2]), this.location23.getX(i));
		}
	}

	@Test
	public void testGetY() {
		for(int i=0; i<this.location11.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords11[i*2+1]), this.location11.getY(i));
		}
		for(int i=0; i<this.location12.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords12[this.coords12.length-(i*2+1)-1]), this.location12.getY(i));
		}
		for(int i=0; i<this.location13.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords13[i*2+1]), this.location13.getY(i));
		}
		for(int i=0; i<this.location21.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords21[i*2+1]), this.location21.getY(i));
		}
		for(int i=0; i<this.location22.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords22[this.coords22.length-(i*2+1)-1]), this.location22.getY(i));
		}
		for(int i=0; i<this.location23.size(); ++i) {
			assertEpsilonEquals(GeoLocationUtil.castDistance(this.coords23[i*2+1]), this.location23.getY(i));
		}
	}

	@Test
	public void testToArray() {
		assertEpsilonArrayEquals(this.coords11, this.location11.toArray());
		assertEpsilonArrayEquals(reverse(this.coords12), this.location12.toArray());
		assertEpsilonArrayEquals(this.coords13, this.location13.toArray());
		assertEpsilonArrayEquals(this.coords21, this.location21.toArray());
		assertEpsilonArrayEquals(reverse(this.coords22), this.location22.toArray());
		assertEpsilonArrayEquals(this.coords23, this.location23.toArray());
	}

	@Test
	public void testToBounds2D() {
		assertEpsilonEquals(this.b11, this.location11.toBounds2D());
		assertEpsilonEquals(this.b12, this.location12.toBounds2D());
		assertEpsilonEquals(this.b13, this.location13.toBounds2D());
		assertEpsilonEquals(this.b21, this.location21.toBounds2D());
		assertEpsilonEquals(this.b22, this.location22.toBounds2D());
		assertEpsilonEquals(this.b23, this.location23.toBounds2D());

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

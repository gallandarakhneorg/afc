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

package org.arakhne.afc.gis.tests.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.arakhne.afc.gis.location.GeoLocationArea;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/** Unit for for GeoLocation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("GeoLocationArea")
@SuppressWarnings("all")
public class GeoLocationAreaTest extends AbstractGisTestCase {

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
		coords11m = pts[0];
		coords11x = pts[1];
		coords12m = pts[2];
		coords12x = pts[3];
		coords21m = pts[4];
		coords21x = pts[5];
		coords22m = pts[6];
		coords22x = pts[7];

		// Creation of GeoLocations
		location11 = new GeoLocationArea(
				coords11m.getX(), coords11m.getY(),
				coords11x.getX(), coords11x.getY());
		location12 = new GeoLocationArea(
				coords11m.getX(), coords11m.getY(),
				coords11x.getX(), coords11x.getY());
		location13 = new GeoLocationArea(
				coords12m.getX(), coords12m.getY(),
				coords12x.getX(), coords12x.getY());

		location21 = new GeoLocationArea(
				coords21m.getX(), coords21m.getY(),
				coords21x.getX(), coords21x.getY());
		location22 = new GeoLocationArea(
				coords21m.getX(), coords21m.getY(),
				coords21x.getX(), coords21x.getY());
		location23 = new GeoLocationArea(
				coords22m.getX(), coords22m.getY(),
				coords22x.getX(), coords22x.getY());

		swap(coords11m, coords11x);
		swap(coords12m, coords12x);
		swap(coords21m, coords21x);
		swap(coords22m, coords22x);
	}

	@AfterEach
	public void tearDown() {
		location11 = null;
		location12 = null;
		location13 = null;
		location21 = null;
		location22 = null;
		location23 = null;
		coords11m = coords11x = coords12m = coords12x = null;
		coords21m = coords21x = coords22m = coords22x = null;
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

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsTest {

		@DisplayName("From toString")
		@Nested
		public class FromToString {
	
			private String geoid11;
			private String geoid12;
			private String geoid13;
			private String geoid21;
			private String geoid22;
			private String geoid23;

			@BeforeEach
			public void setUp() {
				geoid11 = location11.toString();
				geoid12 = location12.toString();
				geoid13 = location13.toString();
				geoid21 = location21.toString();
				geoid22 = location22.toString();
				geoid23 = location23.toString();
			}

			@DisplayName("#1")
			@Test
			public void testEqualsStringString_assertion1() {
				assertEquals(geoid11, geoid12);
			}

			@DisplayName("#2")
			@Test
			public void testEqualsStringString_assertion2() {
				assertEquals(geoid12, geoid11);
			}

			@DisplayName("#3")
			@Test
			public void testEqualsStringString_assertion3() {
				assertNotEquals(geoid11, geoid13);
			}

			@DisplayName("#4")
			@Test
			public void testEqualsStringString_assertion4() {
				assertNotEquals(geoid12, geoid13);
			}

			@DisplayName("#5")
			@Test
			public void testEqualsStringString_assertion5() {
				assertNotEquals(geoid13, geoid11);
			}

			@DisplayName("#6")
			@Test
			public void testEqualsStringString_assertion6() {
				assertNotEquals(geoid13, geoid11);
			}

			@DisplayName("#7")
			@Test
			public void testEqualsStringString_assertion7() {
				assertEquals(geoid21, geoid22);
			}

			@DisplayName("#8")
			@Test
			public void testEqualsStringString_assertion8() {
				assertEquals(geoid22, geoid21);
			}

			@DisplayName("#9")
			@Test
			public void testEqualsStringString_assertion9() {
				assertNotEquals(geoid21, geoid23);
			}

			@DisplayName("#10")
			@Test
			public void testEqualsStringString_assertion10() {
				assertNotEquals(geoid22, geoid23);
			}

			@DisplayName("#11")
			@Test
			public void testEqualsStringString_assertion11() {
				assertNotEquals(geoid23, geoid21);
			}

			@DisplayName("#12")
			@Test
			public void testEqualsStringString_assertion12() {
				assertNotEquals(geoid23, geoid22);
			}
		}		

		@DisplayName("(GeoId)")
		@Nested
		public class WithGeoId {
	
			@DisplayName("#1")
			@Test
			public void testEqualsGeoIdGeoId_assertion1() {
				assertTrue(location11.equals(location12));
			}

			@DisplayName("#2")
			@Test
			public void testEqualsGeoIdGeoId_assertion2() {
				assertTrue(location12.equals(location11));
			}

			@DisplayName("#3")
			@Test
			public void testEqualsGeoIdGeoId_assertion3() {
				assertFalse(location11.equals(location13));
			}

			@DisplayName("#4")
			@Test
			public void testEqualsGeoIdGeoId_assertion4() {
				assertFalse(location12.equals(location13));
			}

			@DisplayName("#5")
			@Test
			public void testEqualsGeoIdGeoId_assertion5() {
				assertFalse(location13.equals(location11));
			}

			@DisplayName("#6")
			@Test
			public void testEqualsGeoIdGeoId_assertion6() {
				assertFalse(location13.equals(location11));
			}

			@DisplayName("#7")
			@Test
			public void testEqualsGeoIdGeoId_assertion7() {
				assertTrue(location21.equals(location22));
			}

			@DisplayName("#8")
			@Test
			public void testEqualsGeoIdGeoId_assertion8() {
				assertTrue(location22.equals(location21));
			}

			@DisplayName("#9")
			@Test
			public void testEqualsGeoIdGeoId_assertion9() {
				assertFalse(location21.equals(location23));
			}

			@DisplayName("#10")
			@Test
			public void testEqualsGeoIdGeoId_assertion10() {
				assertFalse(location22.equals(location23));
			}

			@DisplayName("#11")
			@Test
			public void testEqualsGeoIdGeoId_assertion11() {
				assertFalse(location23.equals(location21));
			}

			@DisplayName("#12")
			@Test
			public void testEqualsGeoIdGeoId_assertion12() {
				assertFalse(location23.equals(location22));
			}
		}		

		@DisplayName("(String)")
		@Nested
		public class WithString {
	
			private String geoid11;
			private String geoid12;
			private String geoid13;
			private String geoid21;
			private String geoid22;
			private String geoid23;

			@BeforeEach
			public void setUp() {
				geoid11 = location11.toString();
				geoid12 = location12.toString();
				geoid13 = location13.toString();
				geoid21 = location21.toString();
				geoid22 = location22.toString();
				geoid23 = location23.toString();
			}

			@DisplayName("#1")
			@Test
			public void testEqualsGeoIdString_assertion1() {
				assertTrue(location11.equals(geoid12));
			}

			@DisplayName("#2")
			@Test
			public void testEqualsGeoIdString_assertion2() {
				assertTrue(location12.equals(geoid11));
			}

			@DisplayName("#3")
			@Test
			public void testEqualsGeoIdString_assertion3() {
				assertFalse(location11.equals(geoid13));
			}

			@DisplayName("#4")
			@Test
			public void testEqualsGeoIdString_assertion4() {
				assertFalse(location12.equals(geoid13));
			}

			@DisplayName("#5")
			@Test
			public void testEqualsGeoIdString_assertion5() {
				assertFalse(location13.equals(geoid11));
			}

			@DisplayName("#6")
			@Test
			public void testEqualsGeoIdString_assertion6() {
				assertFalse(location13.equals(geoid11));
			}

			@DisplayName("#7")
			@Test
			public void testEqualsGeoIdString_assertion7() {
				assertTrue(location21.equals(geoid22));
			}

			@DisplayName("#8")
			@Test
			public void testEqualsGeoIdString_assertion8() {
				assertTrue(location22.equals(geoid21));
			}

			@DisplayName("#9")
			@Test
			public void testEqualsGeoIdString_assertion9() {
				assertFalse(location21.equals(geoid23));
			}

			@DisplayName("#10")
			@Test
			public void testEqualsGeoIdString_assertion10() {
				assertFalse(location22.equals(geoid23));
			}

			@DisplayName("#11")
			@Test
			public void testEqualsGeoIdString_assertion11() {
				assertFalse(location23.equals(geoid21));
			}

			@DisplayName("#12")
			@Test
			public void testEqualsGeoIdString_assertion12() {
				assertFalse(location23.equals(geoid22));
			}
		}		

		@DisplayName("(UUID)")
		@Nested
		public class WithUUID {
	
			private UUID uuid11;
			private UUID uuid12;
			private UUID uuid13;
			private UUID uuid21;
			private UUID uuid22;
			private UUID uuid23;

			@BeforeEach
			public void setUp() {
				uuid11 = location11.toUUID();
				uuid12 = location12.toUUID();
				uuid13 = location13.toUUID();
				uuid21 = location21.toUUID();
				uuid22 = location22.toUUID();
				uuid23 = location23.toUUID();
			}

			@DisplayName("#1")
			@Test
			public void testEqualsGeoIdUUID_assertion1() {
				assertTrue(location11.equals(uuid12));
			}

			@DisplayName("#2")
			@Test
			public void testEqualsGeoIdUUID_assertion2() {
				assertTrue(location12.equals(uuid11));
			}

			@DisplayName("#3")
			@Test
			public void testEqualsGeoIdUUID_assertion3() {
				assertFalse(location11.equals(uuid13));
			}

			@DisplayName("#4")
			@Test
			public void testEqualsGeoIdUUID_assertion4() {
				assertFalse(location12.equals(uuid13));
			}

			@DisplayName("#5")
			@Test
			public void testEqualsGeoIdUUID_assertion5() {
				assertFalse(location13.equals(uuid11));
			}

			@DisplayName("#6")
			@Test
			public void testEqualsGeoIdUUID_assertion6() {
				assertFalse(location13.equals(uuid11));
			}

			@DisplayName("#7")
			@Test
			public void testEqualsGeoIdUUID_assertion7() {
				assertTrue(location21.equals(uuid22));
			}

			@DisplayName("#8")
			@Test
			public void testEqualsGeoIdUUID_assertion8() {
				assertTrue(location22.equals(uuid21));
			}

			@DisplayName("#9")
			@Test
			public void testEqualsGeoIdUUID_assertion9() {
				assertFalse(location21.equals(uuid23));
			}

			@DisplayName("#10")
			@Test
			public void testEqualsGeoIdUUID_assertion10() {
				assertFalse(location22.equals(uuid23));
			}

			@DisplayName("#11")
			@Test
			public void testEqualsGeoIdUUID_assertion11() {
				assertFalse(location23.equals(uuid21));
			}

			@DisplayName("#12")
			@Test
			public void testEqualsGeoIdUUID_assertion12() {
				assertFalse(location23.equals(uuid22));
			}
		}		

		@DisplayName("(GeoLocation)")
		@Nested
		public class WithGeoLocation {
	
			@DisplayName("#1")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion1() {
				assertEquals(location11, location11);
			}

			@DisplayName("#2")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion2() {
				assertEquals(location11, location12);
			}

			@DisplayName("#3")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion3() {
				assertNotEquals(location11, location13);
			}

			@DisplayName("#4")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion4() {
				assertNotEquals(location11, location21);
			}

			@DisplayName("#5")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion5() {
				assertNotEquals(location11, location22);
			}

			@DisplayName("#6")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion6() {
				assertNotEquals(location11, location23);
			}

			@DisplayName("#7")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion7() {
				assertEquals(location12, location11);
			}

			@DisplayName("#8")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion8() {
				assertEquals(location12, location12);
			}

			@DisplayName("#9")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion9() {
				assertNotEquals(location12, location13);
			}

			@DisplayName("#10")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion10() {
				assertNotEquals(location12, location21);
			}

			@DisplayName("#11")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion11() {
				assertNotEquals(location12, location22);
			}

			@DisplayName("#12")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion12() {
				assertNotEquals(location12, location23);
			}

			@DisplayName("#13")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion13() {
				assertNotEquals(location13, location11);
			}

			@DisplayName("#14")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion14() {
				assertNotEquals(location13, location12);
			}

			@DisplayName("#15")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion15() {
				assertEquals(location13, location13);
			}

			@DisplayName("#16")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion16() {
				assertNotEquals(location13, location21);
			}

			@DisplayName("#17")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion17() {
				assertNotEquals(location13, location22);
			}

			@DisplayName("#18")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion18() {
				assertNotEquals(location13, location23);
			}

			@DisplayName("#19")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion19() {
				assertNotEquals(location21, location11);
			}

			@DisplayName("#20")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion20() {
				assertNotEquals(location21, location12);
			}

			@DisplayName("#21")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion21() {
				assertNotEquals(location21, location13);
			}

			@DisplayName("#22")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion22() {
				assertEquals(location21, location21);
			}

			@DisplayName("#23")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion23() {
				assertEquals(location21, location22);
			}

			@DisplayName("#24")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion24() {
				assertNotEquals(location21, location23);
			}

			@DisplayName("#25")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion25() {
				assertNotEquals(location22, location11);
			}

			@DisplayName("#26")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion26() {
				assertNotEquals(location22, location12);
			}

			@DisplayName("#27")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion27() {
				assertNotEquals(location22, location13);
			}

			@DisplayName("#28")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion28() {
				assertEquals(location22, location21);
			}

			@DisplayName("#29")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion29() {
				assertEquals(location22, location22);
			}

			@DisplayName("#30")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion30() {
				assertNotEquals(location22, location23);
			}

			@DisplayName("#31")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion31() {
				assertNotEquals(location23, location11);
			}

			@DisplayName("#32")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion32() {
				assertNotEquals(location23, location12);
			}

			@DisplayName("#33")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion33() {
				assertNotEquals(location23, location13);
			}

			@DisplayName("#34")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion34() {
				assertNotEquals(location23, location21);
			}

			@DisplayName("#35")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion35() {
				assertNotEquals(location23, location22);
			}

			@DisplayName("#36")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion36() {
				assertEquals(location23, location23);
			}
		}
	}

	@DisplayName("hashCode")
	@Nested
	public class HashCode {

		private int geoid11;
		private int geoid12;
		private int geoid13;
		private int geoid21;
		private int geoid22;
		private int geoid23;

		@BeforeEach
		public void setUp() {
			geoid11 = location11.hashCode();
			geoid12 = location12.hashCode();
			geoid13 = location13.hashCode();
			geoid21 = location21.hashCode();
			geoid22 = location22.hashCode();
			geoid23 = location23.hashCode();
		}

		@DisplayName("#1")
		@Test
		public void testHashCode_assertion1() {
			assertEquals(geoid11, geoid12);
		}

		@DisplayName("#2")
		@Test
		public void testHashCode_assertion2() {
			assertEquals(geoid12, geoid11);
		}

		@DisplayName("#3")
		@Test
		public void testHashCode_assertion3() {
			assertNotEquals(geoid11, geoid13);
		}

		@DisplayName("#4")
		@Test
		public void testHashCode_assertion4() {
			assertNotEquals(geoid12, geoid13);
		}

		@DisplayName("#5")
		@Test
		public void testHashCode_assertion5() {
			assertNotEquals(geoid13, geoid11);
		}

		@DisplayName("#6")
		@Test
		public void testHashCode_assertion6() {
			assertNotEquals(geoid13, geoid11);
		}

		@DisplayName("#7")
		@Test
		public void testHashCode_assertion7() {
			assertEquals(geoid21, geoid22);
		}

		@DisplayName("#8")
		@Test
		public void testHashCode_assertion8() {
			assertEquals(geoid22, geoid21);
		}

		@DisplayName("#9")
		@Test
		public void testHashCode_assertion9() {
			assertNotEquals(geoid21, geoid23);
		}

		@DisplayName("#10")
		@Test
		public void testHashCode_assertion10() {
			assertNotEquals(geoid22, geoid23);
		}

		@DisplayName("#11")
		@Test
		public void testHashCode_assertion11() {
			assertNotEquals(geoid23, geoid21);
		}

		@DisplayName("#12")
		@Test
		public void testHashCode_assertion12() {
			assertNotEquals(geoid23, geoid22);
		}
	}

	@DisplayName("toString")
	@Nested
	public class ToString {

		@DisplayName("#1")
		@Test
		public void testToString_assertion1() {
			assertEquals(location11.toString(), location11.toString());
		}

		@DisplayName("#2")
		@Test
		public void testToString_assertion2() {
			assertEquals(location12.toString(), location11.toString());
		}

		@DisplayName("#3")
		@Test
		public void testToString_assertion3() {
			assertNotEquals(location13.toString(), location11.toString());
		}

		@DisplayName("#4")
		@Test
		public void testToString_assertion4() {
			assertNotEquals(location21.toString(), location11.toString());
		}

		@DisplayName("#5")
		@Test
		public void testToString_assertion5() {
			assertNotEquals(location22.toString(), location11.toString());
		}

		@DisplayName("#6")
		@Test
		public void testToString_assertion6() {
			assertNotEquals(location23.toString(), location11.toString());
		}

		@DisplayName("#7")
		@Test
		public void testToString_assertion7() {
			assertEquals(location11.toString(), location12.toString());
		}

		@DisplayName("#8")
		@Test
		public void testToString_assertion8() {
			assertEquals(location12.toString(), location12.toString());
		}

		@DisplayName("#9")
		@Test
		public void testToString_assertion9() {
			assertNotEquals(location13.toString(), location12.toString());
		}

		@DisplayName("#10")
		@Test
		public void testToString_assertion10() {
			assertNotEquals(location21.toString(), location12.toString());
		}

		@DisplayName("#11")
		@Test
		public void testToString_assertion11() {
			assertNotEquals(location22.toString(), location12.toString());
		}

		@DisplayName("#12")
		@Test
		public void testToString_assertion12() {
			assertNotEquals(location23.toString(), location12.toString());
		}

		@DisplayName("#13")
		@Test
		public void testToString_assertion13() {
			assertNotEquals(location11.toString(), location13.toString());
		}

		@DisplayName("#14")
		@Test
		public void testToString_assertion14() {
			assertNotEquals(location12.toString(), location13.toString());
		}

		@DisplayName("#15")
		@Test
		public void testToString_assertion15() {
			assertEquals(location13.toString(), location13.toString());
		}

		@DisplayName("#16")
		@Test
		public void testToString_assertion16() {
			assertNotEquals(location21.toString(), location13.toString());
		}

		@DisplayName("#17")
		@Test
		public void testToString_assertion17() {
			assertNotEquals(location22.toString(), location13.toString());
		}

		@DisplayName("#18")
		@Test
		public void testToString_assertion18() {
			assertNotEquals(location23.toString(), location13.toString());
		}

		@DisplayName("#19")
		@Test
		public void testToString_assertion19() {
			assertNotEquals(location11.toString(), location21.toString());
		}

		@DisplayName("#20")
		@Test
		public void testToString_assertion20() {
			assertNotEquals(location12.toString(), location21.toString());
		}

		@DisplayName("#21")
		@Test
		public void testToString_assertion21() {
			assertNotEquals(location13.toString(), location21.toString());
		}

		@DisplayName("#22")
		@Test
		public void testToString_assertion22() {
			assertEquals(location21.toString(), location21.toString());
		}

		@DisplayName("#23")
		@Test
		public void testToString_assertion23() {
			assertEquals(location22.toString(), location21.toString());
		}

		@DisplayName("#24")
		@Test
		public void testToString_assertion24() {
			assertNotEquals(location23.toString(), location21.toString());
		}

		@DisplayName("#25")
		@Test
		public void testToString_assertion25() {
			assertNotEquals(location11.toString(), location22.toString());
		}

		@DisplayName("#26")
		@Test
		public void testToString_assertion26() {
			assertNotEquals(location12.toString(), location22.toString());
		}

		@DisplayName("#27")
		@Test
		public void testToString_assertion27() {
			assertNotEquals(location13.toString(), location22.toString());
		}

		@DisplayName("#28")
		@Test
		public void testToString_assertion28() {
			assertEquals(location21.toString(), location22.toString());
		}

		@DisplayName("#29")
		@Test
		public void testToString_assertion29() {
			assertEquals(location22.toString(), location22.toString());
		}

		@DisplayName("#30")
		@Test
		public void testToString_assertion30() {
			assertNotEquals(location23.toString(), location22.toString());
		}

		@DisplayName("#31")
		@Test
		public void testToString_assertion31() {
			assertNotEquals(location11.toString(), location23.toString());
		}

		@DisplayName("#32")
		@Test
		public void testToString_assertion32() {
			assertNotEquals(location12.toString(), location23.toString());
		}

		@DisplayName("#33")
		@Test
		public void testToString_assertion33() {
			assertNotEquals(location13.toString(), location23.toString());
		}

		@DisplayName("#34")
		@Test
		public void testToString_assertion34() {
			assertNotEquals(location21.toString(), location23.toString());
		}

		@DisplayName("#35")
		@Test
		public void testToString_assertion35() {
			assertNotEquals(location22.toString(), location23.toString());
		}

		@DisplayName("#36")
		@Test
		public void testToString_assertion36() {
			assertEquals(location23.toString(), location23.toString());
		}
	}

	@DisplayName("toUUID")
	@Nested
	public class ToUUID {

		@DisplayName("#1")
		@Test
		public void testToUUID_assertion1() {
			assertEquals(location11.toUUID(), location11.toUUID());
		}

		@DisplayName("#2")
		@Test
		public void testToUUID_assertion2() {
			assertEquals(location12.toUUID(), location11.toUUID());
		}

		@DisplayName("#3")
		@Test
		public void testToUUID_assertion3() {
			assertNotEquals(location13.toUUID(), location11.toUUID());
		}

		@DisplayName("#4")
		@Test
		public void testToUUID_assertion4() {
			assertNotEquals(location21.toUUID(), location11.toUUID());
		}

		@DisplayName("#5")
		@Test
		public void testToUUID_assertion5() {
			assertNotEquals(location22.toUUID(), location11.toUUID());
		}

		@DisplayName("#6")
		@Test
		public void testToUUID_assertion6() {
			assertNotEquals(location23.toUUID(), location11.toUUID());
		}

		@DisplayName("#7")
		@Test
		public void testToUUID_assertion7() {
			assertEquals(location11.toUUID(), location12.toUUID());
		}

		@DisplayName("#8")
		@Test
		public void testToUUID_assertion8() {
			assertEquals(location12.toUUID(), location12.toUUID());
		}

		@DisplayName("#9")
		@Test
		public void testToUUID_assertion9() {
			assertNotEquals(location13.toUUID(), location12.toUUID());
		}

		@DisplayName("#10")
		@Test
		public void testToUUID_assertion10() {
			assertNotEquals(location21.toUUID(), location12.toUUID());
		}

		@DisplayName("#11")
		@Test
		public void testToUUID_assertion11() {
			assertNotEquals(location22.toUUID(), location12.toUUID());
		}

		@DisplayName("#12")
		@Test
		public void testToUUID_assertion12() {
			assertNotEquals(location23.toUUID(), location12.toUUID());
		}

		@DisplayName("#13")
		@Test
		public void testToUUID_assertion13() {
			assertNotEquals(location11.toUUID(), location13.toUUID());
		}

		@DisplayName("#14")
		@Test
		public void testToUUID_assertion14() {
			assertNotEquals(location12.toUUID(), location13.toUUID());
		}

		@DisplayName("#15")
		@Test
		public void testToUUID_assertion15() {
			assertEquals(location13.toUUID(), location13.toUUID());
		}

		@DisplayName("#16")
		@Test
		public void testToUUID_assertion16() {
			assertNotEquals(location21.toUUID(), location13.toUUID());
		}

		@DisplayName("#17")
		@Test
		public void testToUUID_assertion17() {
			assertNotEquals(location22.toUUID(), location13.toUUID());
		}

		@DisplayName("#18")
		@Test
		public void testToUUID_assertion18() {
			assertNotEquals(location23.toUUID(), location13.toUUID());
		}

		@DisplayName("#19")
		@Test
		public void testToUUID_assertion19() {
			assertNotEquals(location11.toUUID(), location21.toUUID());
		}

		@DisplayName("#20")
		@Test
		public void testToUUID_assertion20() {
			assertNotEquals(location12.toUUID(), location21.toUUID());
		}

		@DisplayName("#21")
		@Test
		public void testToUUID_assertion21() {
			assertNotEquals(location13.toUUID(), location21.toUUID());
		}

		@DisplayName("#22")
		@Test
		public void testToUUID_assertion22() {
			assertEquals(location21.toUUID(), location21.toUUID());
		}

		@DisplayName("#23")
		@Test
		public void testToUUID_assertion23() {
			assertEquals(location22.toUUID(), location21.toUUID());
		}

		@DisplayName("#24")
		@Test
		public void testToUUID_assertion24() {
			assertNotEquals(location23.toUUID(), location21.toUUID());
		}

		@DisplayName("#25")
		@Test
		public void testToUUID_assertion25() {
			assertNotEquals(location11.toUUID(), location22.toUUID());
		}

		@DisplayName("#26")
		@Test
		public void testToUUID_assertion26() {
			assertNotEquals(location12.toUUID(), location22.toUUID());
		}

		@DisplayName("#27")
		@Test
		public void testToUUID_assertion27() {
			assertNotEquals(location13.toUUID(), location22.toUUID());
		}

		@DisplayName("#28")
		@Test
		public void testToUUID_assertion28() {
			assertEquals(location21.toUUID(), location22.toUUID());
		}

		@DisplayName("#29")
		@Test
		public void testToUUID_assertion29() {
			assertEquals(location22.toUUID(), location22.toUUID());
		}

		@DisplayName("#30")
		@Test
		public void testToUUID_assertion30() {
			assertNotEquals(location23.toUUID(), location22.toUUID());
		}

		@DisplayName("#31")
		@Test
		public void testToUUID_assertion31() {
			assertNotEquals(location11.toUUID(), location23.toUUID());
		}

		@DisplayName("#32")
		@Test
		public void testToUUID_assertion32() {
			assertNotEquals(location12.toUUID(), location23.toUUID());
		}

		@DisplayName("#33")
		@Test
		public void testToUUID_assertion33() {
			assertNotEquals(location13.toUUID(), location23.toUUID());
		}

		@DisplayName("#34")
		@Test
		public void testToUUID_assertion34() {
			assertNotEquals(location21.toUUID(), location23.toUUID());
		}

		@DisplayName("#35")
		@Test
		public void testToUUID_assertion35() {
			assertNotEquals(location22.toUUID(), location23.toUUID());
		}

		@DisplayName("#36")
		@Test
		public void testToUUID_assertion36() {
			assertEquals(location23.toUUID(), location23.toUUID());
		}
	}

	@DisplayName("getX")
	@Nested
	public class GetX {

		@DisplayName("#1")
		@Test
		public void testGetX_assertion1() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11m.getX()), location11.getX());
		}

		@DisplayName("#2")
		@Test
		public void testGetX_assertion2() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11m.getX()), location12.getX());
		}

		@DisplayName("#3")
		@Test
		public void testGetX_assertion3() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords12m.getX()), location13.getX());
		}

		@DisplayName("#4")
		@Test
		public void testGetX_assertion4() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21m.getX()), location21.getX());
		}

		@DisplayName("#5")
		@Test
		public void testGetX_assertion5() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21m.getX()), location22.getX());
		}

		@DisplayName("#6")
		@Test
		public void testGetX_assertion6() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords22m.getX()), location23.getX());
		}
	}

	@DisplayName("getY")
	@Nested
	public class GetY {

		@DisplayName("#1")
		@Test
		public void testGetY_assertion1() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11m.getY()), location11.getY());
		}

		@DisplayName("#2")
		@Test
		public void testGetY_assertion2() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11m.getY()), location12.getY());
		}

		@DisplayName("#3")
		@Test
		public void testGetY_assertion3() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords12m.getY()), location13.getY());
		}

		@DisplayName("#4")
		@Test
		public void testGetY_assertion4() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21m.getY()), location21.getY());
		}

		@DisplayName("#5")
		@Test
		public void testGetY_assertion5() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21m.getY()), location22.getY());
		}

		@DisplayName("#6")
		@Test
		public void testGetY_assertion6() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords22m.getY()), location23.getY());
		}
	}

	@DisplayName("getMinX")
	@Nested
	public class GetMinX {

		@DisplayName("#1")
		@Test
		public void testGetMinX_assertion1() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11m.getX()), location11.getMinX());
		}

		@DisplayName("#2")
		@Test
		public void testGetMinX_assertion2() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11m.getX()), location12.getMinX());
		}

		@DisplayName("#3")
		@Test
		public void testGetMinX_assertion3() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords12m.getX()), location13.getMinX());
		}

		@DisplayName("#4")
		@Test
		public void testGetMinX_assertion4() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21m.getX()), location21.getMinX());
		}

		@DisplayName("#5")
		@Test
		public void testGetMinX_assertion5() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21m.getX()), location22.getMinX());
		}

		@DisplayName("#6")
		@Test
		public void testGetMinX_assertion6() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords22m.getX()), location23.getMinX());
		}
	}

	@DisplayName("getMinY")
	@Nested
	public class GetMinY {

		@DisplayName("#1")
		@Test
		public void testGetMinY_assertion1() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11m.getY()), location11.getMinY());
		}

		@DisplayName("#2")
		@Test
		public void testGetMinY_assertion2() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11m.getY()), location12.getMinY());
		}

		@DisplayName("#3")
		@Test
		public void testGetMinY_assertion3() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords12m.getY()), location13.getMinY());
		}

		@DisplayName("#4")
		@Test
		public void testGetMinY_assertion4() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21m.getY()), location21.getMinY());
		}

		@DisplayName("#5")
		@Test
		public void testGetMinY_assertion5() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21m.getY()), location22.getMinY());
		}

		@DisplayName("#6")
		@Test
		public void testGetMinY_assertion6() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords22m.getY()), location23.getMinY());
		}
	}

	@DisplayName("getMaxX")
	@Nested
	public class GetMaxX {

		@DisplayName("#1")
		@Test
		public void testGetMaxX_assertion1() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11x.getX()), location11.getMaxX());
		}

		@DisplayName("#2")
		@Test
		public void testGetMaxX_assertion2() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11x.getX()), location12.getMaxX());
		}

		@DisplayName("#3")
		@Test
		public void testGetMaxX_assertion3() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords12x.getX()), location13.getMaxX());
		}

		@DisplayName("#4")
		@Test
		public void testGetMaxX_assertion4() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21x.getX()), location21.getMaxX());
		}

		@DisplayName("#5")
		@Test
		public void testGetMaxX_assertion5() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21x.getX()), location22.getMaxX());
		}

		@DisplayName("#6")
		@Test
		public void testGetMaxX_assertion6() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords22x.getX()), location23.getMaxX());
		}
	}

	@DisplayName("getMaxY")
	@Nested
	public class GetMaxY {

		@DisplayName("#1")
		@Test
		public void testGetMaxY_assertion1() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11x.getY()), location11.getMaxY());
		}

		@DisplayName("#2")
		@Test
		public void testGetMaxY_assertion2() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11x.getY()), location12.getMaxY());
		}

		@DisplayName("#3")
		@Test
		public void testGetMaxY_assertion3() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords12x.getY()), location13.getMaxY());
		}

		@DisplayName("#4")
		@Test
		public void testGetMaxY_assertion4() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21x.getY()), location21.getMaxY());
		}

		@DisplayName("#5")
		@Test
		public void testGetMaxY_assertion5() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21x.getY()), location22.getMaxY());
		}

		@DisplayName("#6")
		@Test
		public void testGetMaxY_assertion6() {
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords22x.getY()), location23.getMaxY());
		}
	}

	@DisplayName("getWidth")
	@Nested
	public class GetWidth {

		@DisplayName("#1")
		@Test
		public void testGetWidth_assertion1() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11x.getX()-coords11m.getX()), location11.getWidth());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}

		@DisplayName("#2")
		@Test
		public void testGetWidth_assertion2() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11x.getX()-coords11m.getX()), location12.getWidth());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}

		@DisplayName("#3")
		@Test
		public void testGetWidth_assertion3() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords12x.getX()-coords12m.getX()), location13.getWidth());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}

		@DisplayName("#4")
		@Test
		public void testGetWidth_assertion4() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21x.getX()-coords21m.getX()), location21.getWidth());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}

		@DisplayName("#5")
		@Test
		public void testGetWidth_assertion5() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21x.getX()-coords21m.getX()), location22.getWidth());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}

		@DisplayName("#6")
		@Test
		public void testGetWidth_assertion6() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords22x.getX()-coords22m.getX()), location23.getWidth());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}
	}

	@DisplayName("getHeight")
	@Nested
	public class GetHeight {

		@DisplayName("#1")
		@Test
		public void testGetHeight_assertion1() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11x.getY()-coords11m.getY()), location11.getHeight());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}

		@DisplayName("#2")
		@Test
		public void testGetHeight_assertion2() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords11x.getY()-coords11m.getY()), location12.getHeight());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}

		@DisplayName("#3")
		@Test
		public void testGetHeight_assertion3() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords12x.getY()-coords12m.getY()), location13.getHeight());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}

		@DisplayName("#4")
		@Test
		public void testGetHeight_assertion4() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21x.getY()-coords21m.getY()), location21.getHeight());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}

		@DisplayName("#5")
		@Test
		public void testGetHeight_assertion5() {
			setDecimalPrecision(0);
			assertEpsilonEquals(GeoLocationUtil.castDistance(coords21x.getY()-coords21m.getY()), location22.getHeight());
			setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
		}
	}

	@DisplayName("toBounds2D")
	@Nested
	public class ToBounds2D {

		@DisplayName("#1")
		@Test
		public void testToBounds2D_assertion1() {
			assertEpsilonEquals(makeBounds(coords11m, coords11x), location11.toBounds2D());
		}

		@DisplayName("#2")
		@Test
		public void testToBounds2D_assertion2() {
			assertEpsilonEquals(makeBounds(coords11m, coords11x), location12.toBounds2D());
		}

		@DisplayName("#3")
		@Test
		public void testToBounds2D_assertion3() {
			assertEpsilonEquals(makeBounds(coords12m, coords12x), location13.toBounds2D());
		}

		@DisplayName("#4")
		@Test
		public void testToBounds2D_assertion4() {
			assertEpsilonEquals(makeBounds(coords21m, coords21x), location21.toBounds2D());
		}

		@DisplayName("#5")
		@Test
		public void testToBounds2D_assertion5() {
			assertEpsilonEquals(makeBounds(coords21m, coords21x), location22.toBounds2D());
		}

		@DisplayName("#6")
		@Test
		public void testToBounds2D_assertion6() {
			assertEpsilonEquals(makeBounds(coords22m, coords22x), location23.toBounds2D());
		}

		@DisplayName("#7")
		@Test
		public void testToBounds2D_assertion7() {
			assertEpsilonEquals(location11.toBounds2D(), location11.toBounds2D());
		}

		@DisplayName("#8")
		@Test
		public void testToBounds2D_assertion8() {
			assertEpsilonEquals(location12.toBounds2D(), location11.toBounds2D());
		}

		@DisplayName("#9")
		@Test
		public void testToBounds2D_assertion9() {
			assertNotEquals(location13.toBounds2D(), location11.toBounds2D());
		}

		@DisplayName("#10")
		@Test
		public void testToBounds2D_assertion10() {
			assertNotEquals(location21.toBounds2D(), location11.toBounds2D());
		}

		@DisplayName("#11")
		@Test
		public void testToBounds2D_assertion11() {
			assertNotEquals(location22.toBounds2D(), location11.toBounds2D());
		}

		@DisplayName("#12")
		@Test
		public void testToBounds2D_assertion12() {
			assertNotEquals(location23.toBounds2D(), location11.toBounds2D());
		}

		@DisplayName("#13")
		@Test
		public void testToBounds2D_assertion13() {
			assertEpsilonEquals(location11.toBounds2D(), location12.toBounds2D());
		}

		@DisplayName("#14")
		@Test
		public void testToBounds2D_assertion14() {
			assertEpsilonEquals(location12.toBounds2D(), location12.toBounds2D());
		}

		@DisplayName("#15")
		@Test
		public void testToBounds2D_assertion15() {
			assertNotEquals(location13.toBounds2D(), location12.toBounds2D());
		}

		@DisplayName("#16")
		@Test
		public void testToBounds2D_assertion16() {
			assertNotEquals(location21.toBounds2D(), location12.toBounds2D());
		}

		@DisplayName("#17")
		@Test
		public void testToBounds2D_assertion17() {
			assertNotEquals(location22.toBounds2D(), location12.toBounds2D());
		}

		@DisplayName("#18")
		@Test
		public void testToBounds2D_assertion18() {
			assertNotEquals(location23.toBounds2D(), location12.toBounds2D());
		}

		@DisplayName("#19")
		@Test
		public void testToBounds2D_assertion19() {
			assertNotEquals(location11.toBounds2D(), location13.toBounds2D());
		}

		@DisplayName("#20")
		@Test
		public void testToBounds2D_assertion20() {
			assertNotEquals(location12.toBounds2D(), location13.toBounds2D());
		}

		@DisplayName("#21")
		@Test
		public void testToBounds2D_assertion21() {
			assertEpsilonEquals(location13.toBounds2D(), location13.toBounds2D());
		}

		@DisplayName("#22")
		@Test
		public void testToBounds2D_assertion22() {
			assertNotEquals(location21.toBounds2D(), location13.toBounds2D());
		}

		@DisplayName("#23")
		@Test
		public void testToBounds2D_assertion23() {
			assertNotEquals(location22.toBounds2D(), location13.toBounds2D());
		}

		@DisplayName("#24")
		@Test
		public void testToBounds2D_assertion24() {
			assertNotEquals(location23.toBounds2D(), location13.toBounds2D());
		}

		@DisplayName("#25")
		@Test
		public void testToBounds2D_assertion25() {
			assertNotEquals(location11.toBounds2D(), location21.toBounds2D());
		}

		@DisplayName("#26")
		@Test
		public void testToBounds2D_assertion26() {
			assertNotEquals(location12.toBounds2D(), location21.toBounds2D());
		}

		@DisplayName("#27")
		@Test
		public void testToBounds2D_assertion27() {
			assertNotEquals(location13.toBounds2D(), location21.toBounds2D());
		}

		@DisplayName("#28")
		@Test
		public void testToBounds2D_assertion28() {
			assertEpsilonEquals(location21.toBounds2D(), location21.toBounds2D());
		}

		@DisplayName("#29")
		@Test
		public void testToBounds2D_assertion29() {
			assertEpsilonEquals(location22.toBounds2D(), location21.toBounds2D());
		}

		@DisplayName("#30")
		@Test
		public void testToBounds2D_assertion30() {
			assertNotEquals(location23.toBounds2D(), location21.toBounds2D());
		}

		@DisplayName("#31")
		@Test
		public void testToBounds2D_assertion31() {
			assertNotEquals(location11.toBounds2D(), location22.toBounds2D());
		}

		@DisplayName("#32")
		@Test
		public void testToBounds2D_assertion32() {
			assertNotEquals(location12.toBounds2D(), location22.toBounds2D());
		}

		@DisplayName("#33")
		@Test
		public void testToBounds2D_assertion33() {
			assertNotEquals(location13.toBounds2D(), location22.toBounds2D());
		}

		@DisplayName("#34")
		@Test
		public void testToBounds2D_assertion34() {
			assertEpsilonEquals(location21.toBounds2D(), location22.toBounds2D());
		}

		@DisplayName("#35")
		@Test
		public void testToBounds2D_assertion35() {
			assertEpsilonEquals(location22.toBounds2D(), location22.toBounds2D());
		}

		@DisplayName("#36")
		@Test
		public void testToBounds2D_assertion36() {
			assertNotEquals(location23.toBounds2D(), location22.toBounds2D());
		}

		@DisplayName("#37")
		@Test
		public void testToBounds2D_assertion37() {
			assertNotEquals(location11.toBounds2D(), location23.toBounds2D());
		}

		@DisplayName("#38")
		@Test
		public void testToBounds2D_assertion38() {
			assertNotEquals(location12.toBounds2D(), location23.toBounds2D());
		}

		@DisplayName("#39")
		@Test
		public void testToBounds2D_assertion39() {
			assertNotEquals(location13.toBounds2D(), location23.toBounds2D());
		}

		@DisplayName("#40")
		@Test
		public void testToBounds2D_assertion40() {
			assertNotEquals(location21.toBounds2D(), location23.toBounds2D());
		}

		@DisplayName("#41")
		@Test
		public void testToBounds2D_assertion41() {
			assertNotEquals(location22.toBounds2D(), location23.toBounds2D());
		}

		@DisplayName("#42")
		@Test
		public void testToBounds2D_assertion42() {
			assertEpsilonEquals(location23.toBounds2D(), location23.toBounds2D());
		}
	}
}

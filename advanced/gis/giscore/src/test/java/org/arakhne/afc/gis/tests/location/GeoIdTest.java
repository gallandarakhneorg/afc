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
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocationPointList;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** Unit for for GeoLocation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("GeoId")
@SuppressWarnings("all")
public class GeoIdTest extends AbstractGisTestCase {

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

	@BeforeEach
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
		id11 = (loc11=new GeoLocationPointList(coords11)).toGeoId();
		id12 = (loc12=new GeoLocationPointList(coords12)).toGeoId();
		id13 = (loc13=new GeoLocationPointList(coords13)).toGeoId();

		id21 = (loc21=new GeoLocationPointList(coords21)).toGeoId();
		id22 = (loc22=new GeoLocationPointList(coords22)).toGeoId();
		id23 = (loc23=new GeoLocationPointList(coords23)).toGeoId();
	}

	@AfterEach
	public void tearDown() {
		id11 = null;
		id12 = null;
		id13 = null;
		id21 = null;
		id22 = null;
		id23 = null;
		loc11 = null;
		loc12 = null;
		loc13 = null;
		loc21 = null;
		loc22 = null;
		loc23 = null;
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
				geoid11 = id11.toString();
				geoid12 = id12.toString();
				geoid13 = id13.toString();
				geoid21 = id21.toString();
				geoid22 = id22.toString();
				geoid23 = id23.toString();
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
				assertTrue(id11.equals(id12));
			}

			@DisplayName("#2")
			@Test
			public void testEqualsGeoIdGeoId_assertion2() {
				assertTrue(id12.equals(id11));
			}

			@DisplayName("#3")
			@Test
			public void testEqualsGeoIdGeoId_assertion3() {
				assertFalse(id11.equals(id13));
			}

			@DisplayName("#4")
			@Test
			public void testEqualsGeoIdGeoId_assertion4() {
				assertFalse(id12.equals(id13));
			}

			@DisplayName("#5")
			@Test
			public void testEqualsGeoIdGeoId_assertion5() {
				assertFalse(id13.equals(id11));
			}

			@DisplayName("#6")
			@Test
			public void testEqualsGeoIdGeoId_assertion6() {
				assertFalse(id13.equals(id11));
			}

			@DisplayName("#7")
			@Test
			public void testEqualsGeoIdGeoId_assertion7() {
				assertTrue(id21.equals(id22));
			}

			@DisplayName("#8")
			@Test
			public void testEqualsGeoIdGeoId_assertion8() {
				assertTrue(id22.equals(id21));
			}

			@DisplayName("#9")
			@Test
			public void testEqualsGeoIdGeoId_assertion9() {
				assertFalse(id21.equals(id23));
			}

			@DisplayName("#10")
			@Test
			public void testEqualsGeoIdGeoId_assertion10() {
				assertFalse(id22.equals(id23));
			}

			@DisplayName("#11")
			@Test
			public void testEqualsGeoIdGeoId_assertion11() {
				assertFalse(id23.equals(id21));
			}

			@DisplayName("#12")
			@Test
			public void testEqualsGeoIdGeoId_assertion12() {
				assertFalse(id23.equals(id22));
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
				geoid11 = id11.toString();
				geoid12 = id12.toString();
				geoid13 = id13.toString();
				geoid21 = id21.toString();
				geoid22 = id22.toString();
				geoid23 = id23.toString();
			}

			@DisplayName("#1")
			@Test
			public void testEqualsGeoIdString_assertion1() {
				assertTrue(id11.equals(geoid12));
			}

			@DisplayName("#2")
			@Test
			public void testEqualsGeoIdString_assertion2() {
				assertTrue(id12.equals(geoid11));
			}

			@DisplayName("#3")
			@Test
			public void testEqualsGeoIdString_assertion3() {
				assertFalse(id11.equals(geoid13));
			}

			@DisplayName("#4")
			@Test
			public void testEqualsGeoIdString_assertion4() {
				assertFalse(id12.equals(geoid13));
			}

			@DisplayName("#5")
			@Test
			public void testEqualsGeoIdString_assertion5() {
				assertFalse(id13.equals(geoid11));
			}

			@DisplayName("#6")
			@Test
			public void testEqualsGeoIdString_assertion6() {
				assertFalse(id13.equals(geoid11));
			}

			@DisplayName("#7")
			@Test
			public void testEqualsGeoIdString_assertion7() {
				assertTrue(id21.equals(geoid22));
			}

			@DisplayName("#8")
			@Test
			public void testEqualsGeoIdString_assertion8() {
				assertTrue(id22.equals(geoid21));
			}

			@DisplayName("#9")
			@Test
			public void testEqualsGeoIdString_assertion9() {
				assertFalse(id21.equals(geoid23));
			}

			@DisplayName("#10")
			@Test
			public void testEqualsGeoIdString_assertion10() {
				assertFalse(id22.equals(geoid23));
			}

			@DisplayName("#11")
			@Test
			public void testEqualsGeoIdString_assertion11() {
				assertFalse(id23.equals(geoid21));
			}

			@DisplayName("#12")
			@Test
			public void testEqualsGeoIdString_assertion12() {
				assertFalse(id23.equals(geoid22));
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
				uuid11 = id11.toUUID();
				uuid12 = id12.toUUID();
				uuid13 = id13.toUUID();
				uuid21 = id21.toUUID();
				uuid22 = id22.toUUID();
				uuid23 = id23.toUUID();
			}

			@DisplayName("#1")
			@Test
			public void testEqualsGeoIdUUID_assertion1() {
				assertTrue(id11.equals(uuid12));
			}

			@DisplayName("#2")
			@Test
			public void testEqualsGeoIdUUID_assertion2() {
				assertTrue(id12.equals(uuid11));
			}

			@DisplayName("#3")
			@Test
			public void testEqualsGeoIdUUID_assertion3() {
				assertFalse(id11.equals(uuid13));
			}

			@DisplayName("#4")
			@Test
			public void testEqualsGeoIdUUID_assertion4() {
				assertFalse(id12.equals(uuid13));
			}

			@DisplayName("#5")
			@Test
			public void testEqualsGeoIdUUID_assertion5() {
				assertFalse(id13.equals(uuid11));
			}

			@DisplayName("#6")
			@Test
			public void testEqualsGeoIdUUID_assertion6() {
				assertFalse(id13.equals(uuid11));
			}

			@DisplayName("#7")
			@Test
			public void testEqualsGeoIdUUID_assertion7() {
				assertTrue(id21.equals(uuid22));
			}

			@DisplayName("#8")
			@Test
			public void testEqualsGeoIdUUID_assertion8() {
				assertTrue(id22.equals(uuid21));
			}

			@DisplayName("#9")
			@Test
			public void testEqualsGeoIdUUID_assertion9() {
				assertFalse(id21.equals(uuid23));
			}

			@DisplayName("#10")
			@Test
			public void testEqualsGeoIdUUID_assertion10() {
				assertFalse(id22.equals(uuid23));
			}

			@DisplayName("#11")
			@Test
			public void testEqualsGeoIdUUID_assertion11() {
				assertFalse(id23.equals(uuid21));
			}

			@DisplayName("#12")
			@Test
			public void testEqualsGeoIdUUID_assertion12() {
				assertFalse(id23.equals(uuid22));
			}
		}
		
		@DisplayName("(GeoLocation)")
		@Nested
		public class WithGeoLocation {

			@DisplayName("#1")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion1() {
				assertEquals(id11, loc11);
			}

			@DisplayName("#2")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion2() {
				assertEquals(id11, loc12);
			}

			@DisplayName("#3")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion3() {
				assertNotEquals(id11, loc13);
			}

			@DisplayName("#4")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion4() {
				assertNotEquals(id11, loc21);
			}

			@DisplayName("#5")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion5() {
				assertNotEquals(id11, loc22);
			}

			@DisplayName("#6")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion6() {
				assertNotEquals(id11, loc23);
			}

			@DisplayName("#7")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion7() {
				assertEquals(id12, loc11);
			}

			@DisplayName("#8")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion8() {
				assertEquals(id12, loc12);
			}

			@DisplayName("#9")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion9() {
				assertNotEquals(id12, loc13);
			}

			@DisplayName("#10")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion10() {
				assertNotEquals(id12, loc21);
			}

			@DisplayName("#11")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion11() {
				assertNotEquals(id12, loc22);
			}

			@DisplayName("#12")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion12() {
				assertNotEquals(id12, loc23);
			}

			@DisplayName("#13")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion13() {
				assertNotEquals(id13, loc11);
			}

			@DisplayName("#14")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion14() {
				assertNotEquals(id13, loc12);
			}

			@DisplayName("#15")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion15() {
				assertEquals(id13, loc13);
			}

			@DisplayName("#16")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion16() {
				assertNotEquals(id13, loc21);
			}

			@DisplayName("#17")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion17() {
				assertNotEquals(id13, loc22);
			}

			@DisplayName("#18")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion18() {
				assertNotEquals(id13, loc23);
			}

			@DisplayName("#19")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion19() {
				assertNotEquals(id21, loc11);
			}

			@DisplayName("#20")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion20() {
				assertNotEquals(id21, loc12);
			}

			@DisplayName("#21")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion21() {
				assertNotEquals(id21, loc13);
			}

			@DisplayName("#22")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion22() {
				assertEquals(id21, loc21);
			}

			@DisplayName("#23")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion23() {
				assertEquals(id21, loc22);
			}

			@DisplayName("#24")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion24() {
				assertNotEquals(id21, loc23);
			}

			@DisplayName("#25")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion25() {
				assertNotEquals(id22, loc11);
			}

			@DisplayName("#26")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion26() {
				assertNotEquals(id22, loc12);
			}

			@DisplayName("#27")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion27() {
				assertNotEquals(id22, loc13);
			}

			@DisplayName("#28")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion28() {
				assertEquals(id22, loc21);
			}

			@DisplayName("#29")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion29() {
				assertEquals(id22, loc22);
			}

			@DisplayName("#30")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion30() {
				assertNotEquals(id22, loc23);
			}

			@DisplayName("#31")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion31() {
				assertNotEquals(id23, loc11);
			}

			@DisplayName("#32")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion32() {
				assertNotEquals(id23, loc12);
			}

			@DisplayName("#33")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion33() {
				assertNotEquals(id23, loc13);
			}

			@DisplayName("#34")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion34() {
				assertNotEquals(id23, loc21);
			}

			@DisplayName("#35")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion35() {
				assertNotEquals(id23, loc22);
			}

			@DisplayName("#36")
			@Test
			public void testEqualsGeoIdGeoLocation_assertion36() {
				assertEquals(id23, loc23);
			}
		}
	}
	
	@DisplayName("getInternalId")
	@Nested
	public class GetInternalId {

		@DisplayName("#1")
		@Test
		public void testGetInternalId_assertion1() {
			assertEquals(makeInternalId(loc11), id11.getInternalId());
		}

		@DisplayName("#2")
		@Test
		public void testGetInternalId_assertion2() {
			assertEquals(makeInternalId(loc12), id12.getInternalId());
		}

		@DisplayName("#3")
		@Test
		public void testGetInternalId_assertion3() {
			assertEquals(makeInternalId(loc13), id13.getInternalId());
		}

		@DisplayName("#4")
		@Test
		public void testGetInternalId_assertion4() {
			assertEquals(makeInternalId(loc21), id21.getInternalId());
		}

		@DisplayName("#5")
		@Test
		public void testGetInternalId_assertion5() {
			assertEquals(makeInternalId(loc22), id22.getInternalId());
		}

		@DisplayName("#6")
		@Test
		public void testGetInternalId_assertion6() {
			assertEquals(makeInternalId(loc23), id23.getInternalId());
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
			geoid11 = id11.hashCode();
			geoid12 = id12.hashCode();
			geoid13 = id13.hashCode();
			geoid21 = id21.hashCode();
			geoid22 = id22.hashCode();
			geoid23 = id23.hashCode();
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
			assertEquals(makeStringId(loc11), id11.toString());
		}

		@DisplayName("#2")
		@Test
		public void testToString_assertion2() {
			assertEquals(makeStringId(loc12), id12.toString());
		}

		@DisplayName("#3")
		@Test
		public void testToString_assertion3() {
			assertEquals(makeStringId(loc13), id13.toString());
		}

		@DisplayName("#4")
		@Test
		public void testToString_assertion4() {
			assertEquals(makeStringId(loc21), id21.toString());
		}

		@DisplayName("#5")
		@Test
		public void testToString_assertion5() {
			assertEquals(makeStringId(loc22), id22.toString());
		}

		@DisplayName("#6")
		@Test
		public void testToString_assertion6() {
			assertEquals(makeStringId(loc23), id23.toString());
		}

		@DisplayName("#7")
		@Test
		public void testToString_assertion7() {
			assertEquals(id11.toString(), id11.toString());
		}

		@DisplayName("#8")
		@Test
		public void testToString_assertion8() {
			assertEquals(id12.toString(), id11.toString());
		}

		@DisplayName("#9")
		@Test
		public void testToString_assertion9() {
			assertNotEquals(id13.toString(), id11.toString());
		}

		@DisplayName("#10")
		@Test
		public void testToString_assertion10() {
			assertNotEquals(id21.toString(), id11.toString());
		}

		@DisplayName("#11")
		@Test
		public void testToString_assertion11() {
			assertNotEquals(id22.toString(), id11.toString());
		}

		@DisplayName("#12")
		@Test
		public void testToString_assertion12() {
			assertNotEquals(id23.toString(), id11.toString());
		}

		@DisplayName("#13")
		@Test
		public void testToString_assertion13() {
			assertEquals(id11.toString(), id12.toString());
		}

		@DisplayName("#14")
		@Test
		public void testToString_assertion14() {
			assertEquals(id12.toString(), id12.toString());
		}

		@DisplayName("#15")
		@Test
		public void testToString_assertion15() {
			assertNotEquals(id13.toString(), id12.toString());
		}

		@DisplayName("#16")
		@Test
		public void testToString_assertion16() {
			assertNotEquals(id21.toString(), id12.toString());
		}

		@DisplayName("#17")
		@Test
		public void testToString_assertion17() {
			assertNotEquals(id22.toString(), id12.toString());
		}

		@DisplayName("#18")
		@Test
		public void testToString_assertion18() {
			assertNotEquals(id23.toString(), id12.toString());
		}

		@DisplayName("#19")
		@Test
		public void testToString_assertion19() {
			assertNotEquals(id11.toString(), id13.toString());
		}

		@DisplayName("#20")
		@Test
		public void testToString_assertion20() {
			assertNotEquals(id12.toString(), id13.toString());
		}

		@DisplayName("#21")
		@Test
		public void testToString_assertion21() {
			assertEquals(id13.toString(), id13.toString());
		}

		@DisplayName("#22")
		@Test
		public void testToString_assertion22() {
			assertNotEquals(id21.toString(), id13.toString());
		}

		@DisplayName("#23")
		@Test
		public void testToString_assertion23() {
			assertNotEquals(id22.toString(), id13.toString());
		}

		@DisplayName("#24")
		@Test
		public void testToString_assertion24() {
			assertNotEquals(id23.toString(), id13.toString());
		}

		@DisplayName("#25")
		@Test
		public void testToString_assertion25() {
			assertNotEquals(id11.toString(), id21.toString());
		}

		@DisplayName("#26")
		@Test
		public void testToString_assertion26() {
			assertNotEquals(id12.toString(), id21.toString());
		}

		@DisplayName("#27")
		@Test
		public void testToString_assertion27() {
			assertNotEquals(id13.toString(), id21.toString());
		}

		@DisplayName("#28")
		@Test
		public void testToString_assertion28() {
			assertEquals(id21.toString(), id21.toString());
		}

		@DisplayName("#29")
		@Test
		public void testToString_assertion29() {
			assertEquals(id22.toString(), id21.toString());
		}

		@DisplayName("#30")
		@Test
		public void testToString_assertion30() {
			assertNotEquals(id23.toString(), id21.toString());
		}

		@DisplayName("#31")
		@Test
		public void testToString_assertion31() {
			assertNotEquals(id11.toString(), id22.toString());
		}

		@DisplayName("#32")
		@Test
		public void testToString_assertion32() {
			assertNotEquals(id12.toString(), id22.toString());
		}

		@DisplayName("#33")
		@Test
		public void testToString_assertion33() {
			assertNotEquals(id13.toString(), id22.toString());
		}

		@DisplayName("#34")
		@Test
		public void testToString_assertion34() {
			assertEquals(id21.toString(), id22.toString());
		}

		@DisplayName("#35")
		@Test
		public void testToString_assertion35() {
			assertEquals(id22.toString(), id22.toString());
		}

		@DisplayName("#36")
		@Test
		public void testToString_assertion36() {
			assertNotEquals(id23.toString(), id22.toString());
		}

		@DisplayName("#37")
		@Test
		public void testToString_assertion37() {
			assertNotEquals(id11.toString(), id23.toString());
		}

		@DisplayName("#38")
		@Test
		public void testToString_assertion38() {
			assertNotEquals(id12.toString(), id23.toString());
		}

		@DisplayName("#39")
		@Test
		public void testToString_assertion39() {
			assertNotEquals(id13.toString(), id23.toString());
		}

		@DisplayName("#40")
		@Test
		public void testToString_assertion40() {
			assertNotEquals(id21.toString(), id23.toString());
		}

		@DisplayName("#41")
		@Test
		public void testToString_assertion41() {
			assertNotEquals(id22.toString(), id23.toString());
		}

		@DisplayName("#42")
		@Test
		public void testToString_assertion42() {
			assertEquals(id23.toString(), id23.toString());
		}
	}
	
	@DisplayName("toUUID")
	@Nested
	public class ToUUID {

		@DisplayName("#1")
		@Test
		public void testToUUID_assertion1() {
			assertEquals(UUID.nameUUIDFromBytes(makeStringId(loc11).getBytes()), id11.toUUID());
		}

		@DisplayName("#2")
		@Test
		public void testToUUID_assertion2() {
			assertEquals(UUID.nameUUIDFromBytes(makeStringId(loc12).getBytes()), id12.toUUID());
		}

		@DisplayName("#3")
		@Test
		public void testToUUID_assertion3() {
			assertEquals(UUID.nameUUIDFromBytes(makeStringId(loc13).getBytes()), id13.toUUID());
		}

		@DisplayName("#4")
		@Test
		public void testToUUID_assertion4() {
			assertEquals(UUID.nameUUIDFromBytes(makeStringId(loc21).getBytes()), id21.toUUID());
		}

		@DisplayName("#5")
		@Test
		public void testToUUID_assertion5() {
			assertEquals(UUID.nameUUIDFromBytes(makeStringId(loc22).getBytes()), id22.toUUID());
		}

		@DisplayName("#6")
		@Test
		public void testToUUID_assertion6() {
			assertEquals(UUID.nameUUIDFromBytes(makeStringId(loc23).getBytes()), id23.toUUID());
		}

		@DisplayName("#7")
		@Test
		public void testToUUID_assertion7() {
			assertEquals(id11.toUUID(), id11.toUUID());
		}

		@DisplayName("#8")
		@Test
		public void testToUUID_assertion8() {
			assertEquals(id12.toUUID(), id11.toUUID());
		}

		@DisplayName("#9")
		@Test
		public void testToUUID_assertion9() {
			assertNotEquals(id13.toUUID(), id11.toUUID());
		}

		@DisplayName("#10")
		@Test
		public void testToUUID_assertion10() {
			assertNotEquals(id21.toUUID(), id11.toUUID());
		}

		@DisplayName("#11")
		@Test
		public void testToUUID_assertion11() {
			assertNotEquals(id22.toUUID(), id11.toUUID());
		}

		@DisplayName("#12")
		@Test
		public void testToUUID_assertion12() {
			assertNotEquals(id23.toUUID(), id11.toUUID());
		}

		@DisplayName("#13")
		@Test
		public void testToUUID_assertion13() {
			assertEquals(id11.toUUID(), id12.toUUID());
		}

		@DisplayName("#14")
		@Test
		public void testToUUID_assertion14() {
			assertEquals(id12.toUUID(), id12.toUUID());
		}

		@DisplayName("#15")
		@Test
		public void testToUUID_assertion15() {
			assertNotEquals(id13.toUUID(), id12.toUUID());
		}

		@DisplayName("#16")
		@Test
		public void testToUUID_assertion16() {
			assertNotEquals(id21.toUUID(), id12.toUUID());
		}

		@DisplayName("#17")
		@Test
		public void testToUUID_assertion17() {
			assertNotEquals(id22.toUUID(), id12.toUUID());
		}

		@DisplayName("#18")
		@Test
		public void testToUUID_assertion18() {
			assertNotEquals(id23.toUUID(), id12.toUUID());
		}

		@DisplayName("#19")
		@Test
		public void testToUUID_assertion19() {
			assertNotEquals(id11.toUUID(), id13.toUUID());
		}

		@DisplayName("#20")
		@Test
		public void testToUUID_assertion20() {
			assertNotEquals(id12.toUUID(), id13.toUUID());
		}

		@DisplayName("#21")
		@Test
		public void testToUUID_assertion21() {
			assertEquals(id13.toUUID(), id13.toUUID());
		}

		@DisplayName("#22")
		@Test
		public void testToUUID_assertion22() {
			assertNotEquals(id21.toUUID(), id13.toUUID());
		}

		@DisplayName("#23")
		@Test
		public void testToUUID_assertion23() {
			assertNotEquals(id22.toUUID(), id13.toUUID());
		}

		@DisplayName("#24")
		@Test
		public void testToUUID_assertion24() {
			assertNotEquals(id23.toUUID(), id13.toUUID());
		}

		@DisplayName("#25")
		@Test
		public void testToUUID_assertion25() {
			assertNotEquals(id11.toUUID(), id21.toUUID());
		}

		@DisplayName("#26")
		@Test
		public void testToUUID_assertion26() {
			assertNotEquals(id12.toUUID(), id21.toUUID());
		}

		@DisplayName("#27")
		@Test
		public void testToUUID_assertion27() {
			assertNotEquals(id13.toUUID(), id21.toUUID());
		}

		@DisplayName("#28")
		@Test
		public void testToUUID_assertion28() {
			assertEquals(id21.toUUID(), id21.toUUID());
		}

		@DisplayName("#29")
		@Test
		public void testToUUID_assertion29() {
			assertEquals(id22.toUUID(), id21.toUUID());
		}

		@DisplayName("#30")
		@Test
		public void testToUUID_assertion30() {
			assertNotEquals(id23.toUUID(), id21.toUUID());
		}

		@DisplayName("#31")
		@Test
		public void testToUUID_assertion31() {
			assertNotEquals(id11.toUUID(), id22.toUUID());
		}

		@DisplayName("#32")
		@Test
		public void testToUUID_assertion32() {
			assertNotEquals(id12.toUUID(), id22.toUUID());
		}

		@DisplayName("#33")
		@Test
		public void testToUUID_assertion33() {
			assertNotEquals(id13.toUUID(), id22.toUUID());
		}

		@DisplayName("#34")
		@Test
		public void testToUUID_assertion34() {
			assertEquals(id21.toUUID(), id22.toUUID());
		}

		@DisplayName("#35")
		@Test
		public void testToUUID_assertion35() {
			assertEquals(id22.toUUID(), id22.toUUID());
		}

		@DisplayName("#36")
		@Test
		public void testToUUID_assertion36() {
			assertNotEquals(id23.toUUID(), id22.toUUID());
		}

		@DisplayName("#37")
		@Test
		public void testToUUID_assertion37() {
			assertNotEquals(id11.toUUID(), id23.toUUID());
		}

		@DisplayName("#38")
		@Test
		public void testToUUID_assertion38() {
			assertNotEquals(id12.toUUID(), id23.toUUID());
		}

		@DisplayName("#39")
		@Test
		public void testToUUID_assertion39() {
			assertNotEquals(id13.toUUID(), id23.toUUID());
		}

		@DisplayName("#40")
		@Test
		public void testToUUID_assertion40() {
			assertNotEquals(id21.toUUID(), id23.toUUID());
		}

		@DisplayName("#41")
		@Test
		public void testToUUID_assertion41() {
			assertNotEquals(id22.toUUID(), id23.toUUID());
		}

		@DisplayName("#42")
		@Test
		public void testToUUID_assertion42() {
			assertEquals(id23.toUUID(), id23.toUUID());
		}
	}
	
	@DisplayName("toBounds2D")
	@Nested
	public class ToBounds2D {

		@DisplayName("#1")
		@Test
		public void testToBounds2D_assertion1() {
			assertEpsilonEquals(makeBounds(loc11), id11.toBounds2D());
		}

		@DisplayName("#2")
		@Test
		public void testToBounds2D_assertion2() {
			assertEpsilonEquals(makeBounds(loc12), id12.toBounds2D());
		}

		@DisplayName("#3")
		@Test
		public void testToBounds2D_assertion3() {
			assertEpsilonEquals(makeBounds(loc13), id13.toBounds2D());
		}

		@DisplayName("#4")
		@Test
		public void testToBounds2D_assertion4() {
			assertEpsilonEquals(makeBounds(loc21), id21.toBounds2D());
		}

		@DisplayName("#5")
		@Test
		public void testToBounds2D_assertion5() {
			assertEpsilonEquals(makeBounds(loc22), id22.toBounds2D());
		}

		@DisplayName("#6")
		@Test
		public void testToBounds2D_assertion6() {
			assertEpsilonEquals(makeBounds(loc23), id23.toBounds2D());
		}

		@DisplayName("#7")
		@Test
		public void testToBounds2D_assertion7() {
			assertEpsilonEquals(id11.toBounds2D(), id11.toBounds2D());
		}

		@DisplayName("#8")
		@Test
		public void testToBounds2D_assertion8() {
			assertEpsilonEquals(id12.toBounds2D(), id11.toBounds2D());
		}

		@DisplayName("#9")
		@Test
		public void testToBounds2D_assertion9() {
			assertNotEquals(id13.toBounds2D(), id11.toBounds2D());
		}

		@DisplayName("#10")
		@Test
		public void testToBounds2D_assertion10() {
			assertNotEquals(id21.toBounds2D(), id11.toBounds2D());
		}

		@DisplayName("#11")
		@Test
		public void testToBounds2D_assertion11() {
			assertNotEquals(id22.toBounds2D(), id11.toBounds2D());
		}

		@DisplayName("#12")
		@Test
		public void testToBounds2D_assertion12() {
			assertNotEquals(id23.toBounds2D(), id11.toBounds2D());
		}

		@DisplayName("#13")
		@Test
		public void testToBounds2D_assertion13() {
			assertEpsilonEquals(id11.toBounds2D(), id12.toBounds2D());
		}

		@DisplayName("#14")
		@Test
		public void testToBounds2D_assertion14() {
			assertEpsilonEquals(id12.toBounds2D(), id12.toBounds2D());
		}

		@DisplayName("#15")
		@Test
		public void testToBounds2D_assertion15() {
			assertNotEquals(id13.toBounds2D(), id12.toBounds2D());
		}

		@DisplayName("#16")
		@Test
		public void testToBounds2D_assertion16() {
			assertNotEquals(id21.toBounds2D(), id12.toBounds2D());
		}

		@DisplayName("#17")
		@Test
		public void testToBounds2D_assertion17() {
			assertNotEquals(id22.toBounds2D(), id12.toBounds2D());
		}

		@DisplayName("#18")
		@Test
		public void testToBounds2D_assertion18() {
			assertNotEquals(id23.toBounds2D(), id12.toBounds2D());
		}

		@DisplayName("#19")
		@Test
		public void testToBounds2D_assertion19() {
			assertNotEquals(id11.toBounds2D(), id13.toBounds2D());
		}

		@DisplayName("#20")
		@Test
		public void testToBounds2D_assertion20() {
			assertNotEquals(id12.toBounds2D(), id13.toBounds2D());
		}

		@DisplayName("#21")
		@Test
		public void testToBounds2D_assertion21() {
			assertEpsilonEquals(id13.toBounds2D(), id13.toBounds2D());
		}

		@DisplayName("#22")
		@Test
		public void testToBounds2D_assertion22() {
			assertNotEquals(id21.toBounds2D(), id13.toBounds2D());
		}

		@DisplayName("#23")
		@Test
		public void testToBounds2D_assertion23() {
			assertNotEquals(id22.toBounds2D(), id13.toBounds2D());
		}

		@DisplayName("#24")
		@Test
		public void testToBounds2D_assertion24() {
			assertNotEquals(id23.toBounds2D(), id13.toBounds2D());
		}

		@DisplayName("#25")
		@Test
		public void testToBounds2D_assertion25() {
			assertNotEquals(id11.toBounds2D(), id21.toBounds2D());
		}

		@DisplayName("#26")
		@Test
		public void testToBounds2D_assertion26() {
			assertNotEquals(id12.toBounds2D(), id21.toBounds2D());
		}

		@DisplayName("#27")
		@Test
		public void testToBounds2D_assertion27() {
			assertNotEquals(id13.toBounds2D(), id21.toBounds2D());
		}

		@DisplayName("#28")
		@Test
		public void testToBounds2D_assertion28() {
			assertEpsilonEquals(id21.toBounds2D(), id21.toBounds2D());
		}

		@DisplayName("#29")
		@Test
		public void testToBounds2D_assertion29() {
			assertEpsilonEquals(id22.toBounds2D(), id21.toBounds2D());
		}

		@DisplayName("#30")
		@Test
		public void testToBounds2D_assertion30() {
			assertNotEquals(id23.toBounds2D(), id21.toBounds2D());
		}

		@DisplayName("#31")
		@Test
		public void testToBounds2D_assertion31() {
			assertNotEquals(id11.toBounds2D(), id22.toBounds2D());
		}

		@DisplayName("#32")
		@Test
		public void testToBounds2D_assertion32() {
			assertNotEquals(id12.toBounds2D(), id22.toBounds2D());
		}

		@DisplayName("#33")
		@Test
		public void testToBounds2D_assertion33() {
			assertNotEquals(id13.toBounds2D(), id22.toBounds2D());
		}

		@DisplayName("#34")
		@Test
		public void testToBounds2D_assertion34() {
			assertEpsilonEquals(id21.toBounds2D(), id22.toBounds2D());
		}

		@DisplayName("#35")
		@Test
		public void testToBounds2D_assertion35() {
			assertEpsilonEquals(id22.toBounds2D(), id22.toBounds2D());
		}

		@DisplayName("#36")
		@Test
		public void testToBounds2D_assertion36() {
			assertNotEquals(id23.toBounds2D(), id22.toBounds2D());
		}

		@DisplayName("#37")
		@Test
		public void testToBounds2D_assertion37() {
			assertNotEquals(id11.toBounds2D(), id23.toBounds2D());
		}

		@DisplayName("#38")
		@Test
		public void testToBounds2D_assertion38() {
			assertNotEquals(id12.toBounds2D(), id23.toBounds2D());
		}

		@DisplayName("#39")
		@Test
		public void testToBounds2D_assertion39() {
			assertNotEquals(id13.toBounds2D(), id23.toBounds2D());
		}

		@DisplayName("#40")
		@Test
		public void testToBounds2D_assertion40() {
			assertNotEquals(id21.toBounds2D(), id23.toBounds2D());
		}

		@DisplayName("#41")
		@Test
		public void testToBounds2D_assertion41() {
			assertNotEquals(id22.toBounds2D(), id23.toBounds2D());
		}

		@DisplayName("#42")
		@Test
		public void testToBounds2D_assertion42() {
			assertEpsilonEquals(id23.toBounds2D(), id23.toBounds2D());
		}
	}
}

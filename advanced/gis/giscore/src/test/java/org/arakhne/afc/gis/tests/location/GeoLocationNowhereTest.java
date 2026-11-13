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
import org.arakhne.afc.gis.location.GeoLocationNowhere;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;

/** Unit for for GeoLocation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("GeoLocationNowhere")
@SuppressWarnings("all")
public class GeoLocationNowhereTest extends AbstractGisTestCase {

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

		uuid11 = UUID.randomUUID();
		uuid12 = new UUID(uuid11.getMostSignificantBits(), uuid11.getLeastSignificantBits());
		uuid13 = UUID.randomUUID();

		uuid21 = UUID.randomUUID();
		uuid22 = new UUID(uuid21.getMostSignificantBits(), uuid21.getLeastSignificantBits());
		uuid23 = UUID.randomUUID();

		// Creation of GeoLocations
		location11 = new GeoLocationNowhere(uuid11);
		location12 = new GeoLocationNowhere(uuid12);
		location13 = new GeoLocationNowhere(uuid13);

		location21 = new GeoLocationNowhere(uuid21);
		location22 = new GeoLocationNowhere(uuid22);
		location23 = new GeoLocationNowhere(uuid23);
	}

	@AfterEach
	public void tearDown() {
		location11 = null;
		location12 = null;
		location13 = null;
		location21 = null;
		location22 = null;
		location23 = null;
		uuid11 = uuid12 = uuid13 = null;
		uuid21 = uuid22 = uuid23 = null;
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

	@DisplayName("toBounds2D")
	@Nested
	public class ToBounds2D {
		
		@DisplayName("#1")
		@Test
		public void testToBounds2D_assertion1() {
			assertTrue(location11.toBounds2D().isEmpty());
		}

		@DisplayName("#2")
		@Test
		public void testToBounds2D_assertion2() {
			assertTrue(location12.toBounds2D().isEmpty());
		}

		@DisplayName("#3")
		@Test
		public void testToBounds2D_assertion3() {
			assertTrue(location13.toBounds2D().isEmpty());
		}

		@DisplayName("#4")
		@Test
		public void testToBounds2D_assertion4() {
			assertTrue(location21.toBounds2D().isEmpty());
		}

		@DisplayName("#5")
		@Test
		public void testToBounds2D_assertion5() {
			assertTrue(location22.toBounds2D().isEmpty());
		}

		@DisplayName("#6")
		@Test
		public void testToBounds2D_assertion6() {
			assertTrue(location23.toBounds2D().isEmpty());
		}
	}
}

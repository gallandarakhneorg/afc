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

package org.arakhne.afc.gis.tests.coordinate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.arakhne.afc.gis.coordinate.GISCoordinates;
import org.arakhne.afc.gis.coordinate.GeodesicPosition;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/**
 * Web sites where converters are available:<ul>
 * <li><a href="http://geodesie.ign.fr/index.php?p=53&page=circe">IGN Circe</a></li>
 * <li><a href="http://vtopo.free.fr/convers.htm">http://vtopo.free.fr/convers.htm</a></li>
 * <li><a href="http://www.excellance.fr/fr/lambert_wgs84_php">http://www.excellance.fr/fr/lambert_wgs84_php</a></li>
 * <li><a href="http://www.archaero.com/elinik.htm">http://www.archaero.com/elinik.htm</a></li>
 * </ul>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("GISCoordinates")
@SuppressWarnings("all")
public class GISCoordinatesTest extends AbstractGisTestCase {

	private GeodesicPosition wgs1, wgs2, wgs3, wgs4, wgs5;
	private Point2d lambertIIe_1, lambertIIe_2, lambertIIe_3, lambertIIe_4;
	private Point2d lambertIIe_5;

	@BeforeEach
	public void setUp() throws Exception {
		setDecimalPrecision(3);
		// -------------------
		// GIVEN BY IGN CIRCE:
		// -------------------
		// Lambda  = 3.666666667 (long)
		// Phi     = 50.633055556 (lat)
		// Origin    = Greenwich
		// X = E(m)  = 694310.948
		// Y = N(m)  = 2627328.610
		// epsilon   = 2..5 meters
		wgs1 = new GeodesicPosition(3.666666667, 50.633055556);
		lambertIIe_1 = new Point2d(694310.948, 2627328.610);

		// -------------------
		// GIVEN BY IGN CIRCE:
		// -------------------
		// Lambda  = 3.533055556
		// Phi     = 50.349722222
		// Origin    = Greenwich
		// X = E(m)  = 685319.079
		// Y = N(m)  = 2595599.220
		// epsilon = 2..5 meters
		wgs2 = new GeodesicPosition(3.533055556, 50.349722222);
		lambertIIe_2 = new Point2d(685319.079, 2595599.220);

		// -------------------
		// GIVEN BY IGN CIRCE:
		// -------------------
		// Lambda  = 6.84076953
		// Phi     = 47.64425505
		// Origin    = Greenwich
		// X = E(m)  = 938247.8125
		// Y = N(m)  = 2303550.0
		// epsilon = 2..5 meters
		wgs3 = new GeodesicPosition(6.84076953, 47.64425505);
		lambertIIe_3 = new Point2d(938247.8125, 2303550.0);

		// -------------------
		// GIVEN BY IGN CIRCE:
		// -------------------
		// Lambda  = 6.84674456
		// Phi     = 47.64556157
		// Origin    = Greenwich
		// X = E(m)  = 938687.6875
		// Y = N(m)  = 2303720.75
		// epsilon = 2..5 meters
		wgs4 = new GeodesicPosition(6.84674456, 47.64556157);
		lambertIIe_4 = new Point2d(938687.6875, 2303720.75);

		// -------------------
		// GIVEN BY IGN CIRCE:
		// -------------------
		// Lambda  = 6.8533028
		// Phi     = 47.627376
		// Origin    = Greenwich
		// X = E(m)  = 939295.736
		// Y = N(m)  = 2301730.451
		// epsilon = 2..5 meters
		wgs5 = new GeodesicPosition(6.8533028, 47.627376);
		lambertIIe_5 = new Point2d(939295.736, 2301730.451);
	}

	@AfterEach
	public void tearDown() throws Exception {
		wgs1 = wgs2 = wgs3 = wgs4 = wgs5 = null;
		lambertIIe_1 = lambertIIe_2 = lambertIIe_3 = lambertIIe_4 = lambertIIe_5 = null;
	}

	@DisplayName("WGS84_EL2")
	@Nested
	public class WGS84_EL2 {

		private Point2d p;

		@DisplayName("#1")
		@Test
		public void testWGS84_EL2_assertion1() {
			p = GISCoordinates.WGS84_EL2(wgs1.lambda, wgs1.phi);
			assertNotNull(p);
		}

		@DisplayName("#2")
		@Test
		public void testWGS84_EL2_assertion2() {
			p = GISCoordinates.WGS84_EL2(wgs1.lambda, wgs1.phi);
			assertEpsilonEquals(lambertIIe_1.getX(), p.getX());
		}

		@DisplayName("#3")
		@Test
		public void testWGS84_EL2_assertion3() {
			p = GISCoordinates.WGS84_EL2(wgs1.lambda, wgs1.phi);
			assertEpsilonEquals(lambertIIe_1.getY(), p.getY());
		}

		@DisplayName("#4")
		@Test
		public void testWGS84_EL2_assertion4() {
			p = GISCoordinates.WGS84_EL2(wgs2.lambda, wgs2.phi);
			assertNotNull(p);
		}

		@DisplayName("#5")
		@Test
		public void testWGS84_EL2_assertion5() {
			p = GISCoordinates.WGS84_EL2(wgs2.lambda, wgs2.phi);
			assertEpsilonEquals(lambertIIe_2.getX(), p.getX());
		}

		@DisplayName("#6")
		@Test
		public void testWGS84_EL2_assertion6() {
			p = GISCoordinates.WGS84_EL2(wgs2.lambda, wgs2.phi);
			assertEpsilonEquals(lambertIIe_2.getY(), p.getY());
		}

		@DisplayName("#7")
		@Test
		public void testWGS84_EL2_assertion7() {
			p = GISCoordinates.WGS84_EL2(wgs3.lambda, wgs3.phi);
			assertNotNull(p);
		}

		@DisplayName("#8")
		@Test
		public void testWGS84_EL2_assertion8() {
			p = GISCoordinates.WGS84_EL2(wgs3.lambda, wgs3.phi);
			assertEpsilonEquals(lambertIIe_3.getX(), p.getX());
		}

		@DisplayName("#9")
		@Test
		public void testWGS84_EL2_assertion9() {
			p = GISCoordinates.WGS84_EL2(wgs3.lambda, wgs3.phi);
			assertEpsilonEquals(lambertIIe_3.getY(), p.getY());
		}

		@DisplayName("#10")
		@Test
		public void testWGS84_EL2_assertion10() {
			p = GISCoordinates.WGS84_EL2(wgs4.lambda, wgs4.phi);
			assertNotNull(p);
		}

		@DisplayName("#11")
		@Test
		public void testWGS84_EL2_assertion11() {
			p = GISCoordinates.WGS84_EL2(wgs4.lambda, wgs4.phi);
			assertEpsilonEquals(lambertIIe_4.getX(), p.getX());
		}

		@DisplayName("#12")
		@Test
		public void testWGS84_EL2_assertion12() {
			p = GISCoordinates.WGS84_EL2(wgs4.lambda, wgs4.phi);
			assertEpsilonEquals(lambertIIe_4.getY(), p.getY());
		}

		@DisplayName("#13")
		@Test
		public void testWGS84_EL2_assertion13() {
			p = GISCoordinates.WGS84_EL2(wgs5.lambda, wgs5.phi);
			assertNotNull(p);
		}

		@DisplayName("#14")
		@Test
		public void testWGS84_EL2_assertion14() {
			p = GISCoordinates.WGS84_EL2(wgs5.lambda, wgs5.phi);
			assertEpsilonEquals(lambertIIe_5.getX(), p.getX());
		}

		@DisplayName("#15")
		@Test
		public void testWGS84_EL2_assertion15() {
			p = GISCoordinates.WGS84_EL2(wgs5.lambda, wgs5.phi);
			assertEpsilonEquals(lambertIIe_5.getY(), p.getY());
		}
	}

	@DisplayName("EL2_WGS84")
	@Nested
	public class EL2_WGS84 {

		private GeodesicPosition p;

		@DisplayName("#1")
		@Test
		public void testEL2_WGS84_assertion1() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_1.getX(), lambertIIe_1.getY());
			assertNotNull(p);
		}

		@DisplayName("#2")
		@Test
		public void testEL2_WGS84_assertion2() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_1.getX(), lambertIIe_1.getY());
			assertEpsilonEquals(wgs1.lambda, p.lambda);
		}

		@DisplayName("#3")
		@Test
		public void testEL2_WGS84_assertion3() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_1.getX(), lambertIIe_1.getY());
			assertEpsilonEquals(wgs1.phi, p.phi);
		}

		@DisplayName("#4")
		@Test
		public void testEL2_WGS84_assertion4() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_2.getX(), lambertIIe_2.getY());
			assertNotNull(p);
		}

		@DisplayName("#5")
		@Test
		public void testEL2_WGS84_assertion5() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_2.getX(), lambertIIe_2.getY());
			assertEpsilonEquals(wgs2.lambda, p.lambda);
		}

		@DisplayName("#6")
		@Test
		public void testEL2_WGS84_assertion6() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_2.getX(), lambertIIe_2.getY());
			assertEpsilonEquals(wgs2.phi, p.phi);
		}

		@DisplayName("#7")
		@Test
		public void testEL2_WGS84_assertion7() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_3.getX(), lambertIIe_3.getY());
			assertNotNull(p);
		}

		@DisplayName("#8")
		@Test
		public void testEL2_WGS84_assertion8() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_3.getX(), lambertIIe_3.getY());
			assertEpsilonEquals(wgs3.lambda, p.lambda);
		}

		@DisplayName("#9")
		@Test
		public void testEL2_WGS84_assertion9() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_3.getX(), lambertIIe_3.getY());
			assertEpsilonEquals(wgs3.phi, p.phi);
		}

		@DisplayName("#10")
		@Test
		public void testEL2_WGS84_assertion10() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_4.getX(), lambertIIe_4.getY());
			assertNotNull(p);
		}

		@DisplayName("#11")
		@Test
		public void testEL2_WGS84_assertion11() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_4.getX(), lambertIIe_4.getY());
			assertEpsilonEquals(wgs4.lambda, p.lambda);
		}

		@DisplayName("#12")
		@Test
		public void testEL2_WGS84_assertion12() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_4.getX(), lambertIIe_4.getY());
			assertEpsilonEquals(wgs4.phi, p.phi);
		}

		@DisplayName("#13")
		@Test
		public void testEL2_WGS84_assertion13() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_5.getX(), lambertIIe_5.getY());
			assertNotNull(p);
		}

		@DisplayName("#14")
		@Test
		public void testEL2_WGS84_assertion14() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_5.getX(), lambertIIe_5.getY());
			assertEpsilonEquals(wgs5.lambda, p.lambda);
		}

		@DisplayName("#15")
		@Test
		public void testEL2_WGS84_assertion15() {
			p = GISCoordinates.EL2_WGS84(lambertIIe_5.getX(), lambertIIe_5.getY());
			assertEpsilonEquals(wgs5.phi, p.phi);
		}
	}
}

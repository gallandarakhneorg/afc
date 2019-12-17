/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.gis.coordinate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.gis.AbstractGisTest;
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
@SuppressWarnings("all")
public class GISCoordinatesTest extends AbstractGisTest {

	private GeodesicPosition wsg1, wsg2, wsg3, wsg4, wsg5;
	private Point2d lambertIIe_1, lambertIIe_2, lambertIIe_3, lambertIIe_4;
	private Point2d lambertIIe_5;

	@Before
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
		this.wsg1 = new GeodesicPosition(3.666666667, 50.633055556);
		this.lambertIIe_1 = new Point2d(694310.948, 2627328.610);

		// -------------------
		// GIVEN BY IGN CIRCE:
		// -------------------
		// Lambda  = 3.533055556
		// Phi     = 50.349722222
		// Origin    = Greenwich
		// X = E(m)  = 685319.079
		// Y = N(m)  = 2595599.220
		// epsilon = 2..5 meters
		this.wsg2 = new GeodesicPosition(3.533055556, 50.349722222);
		this.lambertIIe_2 = new Point2d(685319.079, 2595599.220);

		// -------------------
		// GIVEN BY IGN CIRCE:
		// -------------------
		// Lambda  = 6.84076953
		// Phi     = 47.64425505
		// Origin    = Greenwich
		// X = E(m)  = 938247.8125
		// Y = N(m)  = 2303550.0
		// epsilon = 2..5 meters
		this.wsg3 = new GeodesicPosition(6.84076953, 47.64425505);
		this.lambertIIe_3 = new Point2d(938247.8125, 2303550.0);

		// -------------------
		// GIVEN BY IGN CIRCE:
		// -------------------
		// Lambda  = 6.84674456
		// Phi     = 47.64556157
		// Origin    = Greenwich
		// X = E(m)  = 938687.6875
		// Y = N(m)  = 2303720.75
		// epsilon = 2..5 meters
		this.wsg4 = new GeodesicPosition(6.84674456, 47.64556157);
		this.lambertIIe_4 = new Point2d(938687.6875, 2303720.75);

		// -------------------
		// GIVEN BY IGN CIRCE:
		// -------------------
		// Lambda  = 6.8533028
		// Phi     = 47.627376
		// Origin    = Greenwich
		// X = E(m)  = 939295.736
		// Y = N(m)  = 2301730.451
		// epsilon = 2..5 meters
		this.wsg5 = new GeodesicPosition(6.8533028, 47.627376);
		this.lambertIIe_5 = new Point2d(939295.736, 2301730.451);
	}

	@After
	public void tearDown() throws Exception {
		this.wsg1 = this.wsg2 = this.wsg3 = this.wsg4 = this.wsg5 = null;
		this.lambertIIe_1 = this.lambertIIe_2 = this.lambertIIe_3 = this.lambertIIe_4 = this.lambertIIe_5 = null;
	}

	@Test
	public void testWSG84_EL2DoubleDouble() {
		Point2d p;

		p = GISCoordinates.WSG84_EL2(this.wsg1.lambda, this.wsg1.phi);
		assertNotNull(p);
		assertEpsilonEquals(this.lambertIIe_1.getX(), p.getX());
		assertEpsilonEquals(this.lambertIIe_1.getY(), p.getY());

		p = GISCoordinates.WSG84_EL2(this.wsg2.lambda, this.wsg2.phi);
		assertNotNull(p);
		assertEpsilonEquals(this.lambertIIe_2.getX(), p.getX());
		assertEpsilonEquals(this.lambertIIe_2.getY(), p.getY());

		p = GISCoordinates.WSG84_EL2(this.wsg3.lambda, this.wsg3.phi);
		assertNotNull(p);
		assertEpsilonEquals(this.lambertIIe_3.getX(), p.getX());
		assertEpsilonEquals(this.lambertIIe_3.getY(), p.getY());

		p = GISCoordinates.WSG84_EL2(this.wsg4.lambda, this.wsg4.phi);
		assertNotNull(p);
		assertEpsilonEquals(this.lambertIIe_4.getX(), p.getX());
		assertEpsilonEquals(this.lambertIIe_4.getY(), p.getY());

		p = GISCoordinates.WSG84_EL2(this.wsg5.lambda, this.wsg5.phi);
		assertNotNull(p);
		assertEpsilonEquals(this.lambertIIe_5.getX(), p.getX());
		assertEpsilonEquals(this.lambertIIe_5.getY(), p.getY());
	}

	@Test
	public void testEL2_WSG84DoubleDouble() {
		GeodesicPosition p;

		p = GISCoordinates.EL2_WSG84(this.lambertIIe_1.getX(), this.lambertIIe_1.getY());
		assertNotNull(p);
		assertEpsilonEquals(this.wsg1.lambda, p.lambda);
		assertEpsilonEquals(this.wsg1.phi, p.phi);

		p = GISCoordinates.EL2_WSG84(this.lambertIIe_2.getX(), this.lambertIIe_2.getY());
		assertNotNull(p);
		assertEpsilonEquals(this.wsg2.lambda, p.lambda);
		assertEpsilonEquals(this.wsg2.phi, p.phi);

		p = GISCoordinates.EL2_WSG84(this.lambertIIe_3.getX(), this.lambertIIe_3.getY());
		assertNotNull(p);
		assertEpsilonEquals(this.wsg3.lambda, p.lambda);
		assertEpsilonEquals(this.wsg3.phi, p.phi);

		p = GISCoordinates.EL2_WSG84(this.lambertIIe_4.getX(), this.lambertIIe_4.getY());
		assertNotNull(p);
		assertEpsilonEquals(this.wsg4.lambda, p.lambda);
		assertEpsilonEquals(this.wsg4.phi, p.phi);

		p = GISCoordinates.EL2_WSG84(this.lambertIIe_5.getX(), this.lambertIIe_5.getY());
		assertNotNull(p);
		assertEpsilonEquals(this.wsg5.lambda, p.lambda);
		assertEpsilonEquals(this.wsg5.phi, p.phi);
	}

}

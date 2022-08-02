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

package org.arakhne.afc.gis.coordinate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.AbstractGisTest;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class GeodesicPositionTest extends AbstractGisTest {

	@Test
	public void testSexagesimalToAngular() {
		GeodesicPosition pos = new GeodesicPosition(
				121, 8, 6, SexagesimalLatitudeAxis.NORTH,
				45, 53, 36, SexagesimalLongitudeAxis.WEST);
		assertEpsilonEquals(121.135, pos.getLatitude());
		assertEpsilonEquals(-45.893333, pos.getLongitude());
	}

	@Test
	public void testAngularToSexagesimal() {
		GeodesicPosition pos = new GeodesicPosition(121.135,-34.5678989);
		assertEquals(121, pos.getLongitudeDegree());
		assertEquals(8, pos.getLongitudeMinute());
		assertEpsilonEquals(6., pos.getLongitudeSecond());
		assertEquals(SexagesimalLongitudeAxis.EAST, pos.getLongitudeAxis());
		assertEquals(34, pos.getLatitudeDegree());
		assertEquals(34, pos.getLatitudeMinute());
		assertEpsilonEquals(4.43604, pos.getLatitudeSecond());
		assertEquals(SexagesimalLatitudeAxis.SOUTH, pos.getLatitudeAxis());

		pos = new GeodesicPosition(-121.135,34.5678989);
		assertEquals(121, pos.getLongitudeDegree());
		assertEquals(8, pos.getLongitudeMinute());
		assertEpsilonEquals(6, pos.getLongitudeSecond());
		assertEquals(SexagesimalLongitudeAxis.WEST, pos.getLongitudeAxis());
		assertEquals(34, pos.getLatitudeDegree());
		assertEquals(34, pos.getLatitudeMinute());
		assertEpsilonEquals(4.43604, pos.getLatitudeSecond());
		assertEquals(SexagesimalLatitudeAxis.NORTH, pos.getLatitudeAxis());
	}

}

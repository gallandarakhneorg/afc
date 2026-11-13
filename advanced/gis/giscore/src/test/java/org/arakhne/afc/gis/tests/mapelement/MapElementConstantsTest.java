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

package org.arakhne.afc.gis.tests.mapelement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.prefs.Preferences;

import org.arakhne.afc.gis.mapelement.MapElementConstants;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MapElementConstants")
@SuppressWarnings("all")
public class MapElementConstantsTest extends AbstractGisTestCase {

	private static final String KEY_RADIUS = "RADIUS";
	private static final String KEY_COLOR = "COLOR";

	@AfterEach
	void cleanupPreferences() throws Exception {
		Preferences prefs = Preferences.userNodeForPackage(MapElementConstants.class);
		prefs.remove(KEY_RADIUS);
		prefs.remove(KEY_COLOR);
		prefs.flush();
	}

	@DisplayName("constants")
	@Nested
	public class Constants {

		@DisplayName("POINT_FUSION_SQUARED_DISTANCE = POINT_FUSION_DISTANCE²")
		@Test
		void pointFusionSquaredDistance_matchesSquare() {
			assertEquals(
					MapElementConstants.POINT_FUSION_DISTANCE * MapElementConstants.POINT_FUSION_DISTANCE,
					MapElementConstants.POINT_FUSION_SQUARED_DISTANCE,
					EPSILON);
		}

		@DisplayName("DEFAULT_RADIUS is 5.0")
		@Test
		void defaultRadius_value() {
			assertEquals(5.0, MapElementConstants.DEFAULT_RADIUS, EPSILON);
		}
	}

	@DisplayName("preferred radius")
	@Nested
	public class PreferredRadius {

		@DisplayName("getPreferredRadius returns default when preference not set")
		@Test
		void getPreferredRadius_defaultValue() throws Exception {
			Preferences prefs = Preferences.userNodeForPackage(MapElementConstants.class);
			prefs.remove(KEY_RADIUS);
			prefs.flush();

			assertEquals(MapElementConstants.DEFAULT_RADIUS, MapElementConstants.getPreferredRadius(), EPSILON);
		}

		@DisplayName("setPreferredRadius persists value")
		@Test
		void setPreferredRadius_persistsValue() {
			MapElementConstants.setPreferredRadius(12.5);

			assertEquals(12.5, MapElementConstants.getPreferredRadius(), EPSILON);
		}

		@DisplayName("setPreferredRadius can persist negative value")
		@Test
		void setPreferredRadius_negativeValue() {
			MapElementConstants.setPreferredRadius(-3.0);

			assertEquals(-3.0, MapElementConstants.getPreferredRadius(), EPSILON);
		}
	}

	@DisplayName("preferred color")
	@Nested
	public class PreferredColor {

		@DisplayName("getPreferredColor returns default when preference not set")
		@Test
		void getPreferredColor_defaultValue() throws Exception {
			Preferences prefs = Preferences.userNodeForPackage(MapElementConstants.class);
			prefs.remove(KEY_COLOR);
			prefs.flush();

			assertEquals(MapElementConstants.DEFAULT_COLOR, MapElementConstants.getPreferredColor());
		}

		@DisplayName("setPreferredColor persists value")
		@Test
		void setPreferredColor_persistsValue() {
			int color = 0xFF336699;
			MapElementConstants.setPreferredColor(color);

			assertEquals(color, MapElementConstants.getPreferredColor());
		}
	}
}

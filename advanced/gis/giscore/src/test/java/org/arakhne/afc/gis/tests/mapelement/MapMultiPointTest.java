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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.mapelement.MapMultiPoint;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/** Unit tests for MapMultiPoint.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("MapMultiPoint")
@SuppressWarnings("all")
public class MapMultiPointTest extends AbstractMapComposedElementTestCase {

	@DisplayName("Creation")
	@Nested
	public class Creation {

		private MapMultiPoint element;
		private List<Double> referenceCoordinates;
		private Map<Point2d,String> creationPoints;

		@BeforeEach
		public void setUp() {
			element = new MapMultiPoint(new HeapAttributeCollection());
			referenceCoordinates = new ArrayList<>();
			creationPoints = new HashMap<>();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.gis.tests.mapelement.AbstractMapComposedElementTestCase#providesIntegers()")
		public void creation(Integer i) throws Exception {
			mapComposedElement_creation(i, element, referenceCoordinates, creationPoints, null);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.gis.tests.mapelement.AbstractMapComposedElementTestCase#providesIntegers()")
		public void check(Integer i) throws Exception {
			mapComposedElement_creation(i, element, referenceCoordinates, creationPoints, null);
			mapComposedElement_checkPoints(element, referenceCoordinates, creationPoints);
		}
	}

	@DisplayName("Deletion")
	@Nested
	public class Deletion {

		private MapMultiPoint element;
		private List<Double> referenceCoordinates;
		private Map<Point2d,String> creationPoints;

		@BeforeEach
		public void setUp() {
			element = new MapMultiPoint(new HeapAttributeCollection());
			referenceCoordinates = new ArrayList<>();
			creationPoints = new HashMap<>();
		}

		@DisplayName("Without creation")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.gis.tests.mapelement.AbstractMapComposedElementTestCase#providesIntegers()")
		public void withoutCreation(Integer i) throws Exception {
			mapComposedElement_deletion(element,referenceCoordinates);
		}

		@DisplayName("With creation")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.gis.tests.mapelement.AbstractMapComposedElementTestCase#providesIntegers()")
		public void withCreation(Integer i) throws Exception {
			mapComposedElement_creation(i, element, referenceCoordinates, creationPoints, null);
			mapComposedElement_deletion(element,referenceCoordinates);
		}

		@DisplayName("With check")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.gis.tests.mapelement.AbstractMapComposedElementTestCase#providesIntegers()")
		public void withCheck(Integer i) throws Exception {
			mapComposedElement_creation(i, element, referenceCoordinates, creationPoints, null);
			mapComposedElement_deletion(element,referenceCoordinates);
			mapComposedElement_checkPoints(element,referenceCoordinates,creationPoints);
		}
	}
}

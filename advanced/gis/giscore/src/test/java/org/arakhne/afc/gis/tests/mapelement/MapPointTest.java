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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MapPoint")
@SuppressWarnings("all")
public class MapPointTest extends AbstractGisTestCase {

	@DisplayName("constructors")
	@Nested
	public class Constructors {

		@DisplayName("(x,y) stores coordinates")
		@Test
		void constructor_xy_storesValues() {
			MapPoint point = new MapPoint(10.0, 20.0);

			assertEquals(10.0, point.getX(), EPSILON);
			assertEquals(20.0, point.getY(), EPSILON);
		}

		@DisplayName("(point2d) stores coordinates")
		@Test
		void constructor_point_storesValues() {
			MapPoint point = new MapPoint(new Point2d(1.5, -2.5));

			assertEquals(1.5, point.getX(), EPSILON);
			assertEquals(-2.5, point.getY(), EPSILON);
		}

		@DisplayName("(id,x,y) keeps explicit id")
		@Test
		void constructor_idxy_storesId() {
			UUID id = UUID.randomUUID();
			MapPoint point = new MapPoint(id, 0.0, 0.0);

			assertEquals(id, point.getUUID());
		}
	}

	@DisplayName("point size")
	@Nested
	public class PointSize {

		@DisplayName("setPointSize updates value")
		@Test
		void setPointSize_updatesValue() {
			MapPoint point = new MapPoint(0.0, 0.0);

			point.setPointSize(8.0);

			assertEquals(8.0, point.getPointSize(), EPSILON);
		}

		@DisplayName("setPointSize uses absolute value")
		@Test
		void setPointSize_negative_becomesPositive() {
			MapPoint point = new MapPoint(0.0, 0.0);

			point.setPointSize(-6.0);

			assertEquals(0.0, point.getPointSize(), EPSILON);
		}

		@DisplayName("doubleFramed flag is toggled")
		@Test
		void doubleFramed_toggle() {
			MapPoint point = new MapPoint(0.0, 0.0);

			point.setDoubleFramed(true);
			assertTrue(point.isDoubleFramed());

			point.setDoubleFramed(false);
			assertEquals(false, point.isDoubleFramed());
		}
	}

	@DisplayName("distance")
	@Nested
	public class Distance {

		@DisplayName("distance to Point2d is euclidean")
		@Test
		void getDistance_point2d() {
			MapPoint point = new MapPoint(0.0, 0.0);

			assertEquals(5.0, point.getDistance(new Point2d(3.0, 4.0)), EPSILON);
		}

		@DisplayName("distance to MapPoint is euclidean")
		@Test
		void distance_mapPoint() {
			MapPoint a = new MapPoint(0.0, 0.0);
			MapPoint b = new MapPoint(6.0, 8.0);

			assertEquals(10.0, a.distance(b), EPSILON);
		}
	}

	@DisplayName("bounds and shape")
	@Nested
	public class BoundsAndShape {

		@DisplayName("bounding box is centered on point")
		@Test
		void boundingBox_centeredOnPoint() {
			MapPoint point = new MapPoint(10.0, 20.0);
			point.setPointSize(4.0);

			Rectangle2d box = point.getBoundingBox();

			assertEquals(8.0, box.getMinX(), EPSILON);
			assertEquals(18.0, box.getMinY(), EPSILON);
			assertEquals(4.0, box.getWidth(), EPSILON);
			assertEquals(4.0, box.getHeight(), EPSILON);
		}

		@DisplayName("shape is non-null and bounded around point")
		@Test
		void getShape_nonNull() {
			MapPoint point = new MapPoint(1.0, 2.0);
			point.setPointSize(2.0);

			Shape2d<?> shape = point.getShape();
			Rectangle2d bounds = shape.toBoundingBox();

			assertEquals(0.0, bounds.getMinX(), EPSILON);
			assertEquals(1.0, bounds.getMinY(), EPSILON);
			assertEquals(2.0, bounds.getWidth(), EPSILON);
			assertEquals(2.0, bounds.getHeight(), EPSILON);
		}
	}

	@DisplayName("equals/hashCode")
	@Nested
	public class EqualsHashCode {

		@DisplayName("same geometry and id -> equals")
		@Test
		void equals_sameValues_true() {
			UUID id = UUID.randomUUID();
			MapPoint a = new MapPoint(id, 5.0, 6.0);
			MapPoint b = new MapPoint(id, 5.0, 6.0);

			assertTrue(a.equals(b));
			assertEquals(a.hashCode(), b.hashCode());
		}
	}

	@DisplayName("toJson")
	@Nested
	public class ToJson {

		@DisplayName("json contains coordinates")
		@Test
		void toJson_containsCoordinates() {
			MapPoint point = new MapPoint(3.5, 4.5);
			JsonBuffer buffer = new JsonBuffer();

			point.toJson(buffer);

			String json = buffer.toString();
			assertTrue(json.contains("3.5") || json.contains("3,5"));
			assertTrue(json.contains("4.5") || json.contains("4,5"));
		}
	}
}
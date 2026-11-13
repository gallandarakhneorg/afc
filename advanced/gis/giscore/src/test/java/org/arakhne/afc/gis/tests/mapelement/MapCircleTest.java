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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.arakhne.afc.gis.mapelement.MapCircle;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MapCircle")
@SuppressWarnings("all")
public class MapCircleTest extends AbstractGisTestCase {

	@DisplayName("constructors")
	@Nested
	public class Constructors {

		@DisplayName("(x,y,radius) stores center and radius")
		@Test
		void constructor_xyz_storesValues() {
			MapCircle circle = new MapCircle(10.0, 20.0, 5.0);

			assertEquals(10.0, circle.getX(), EPSILON);
			assertEquals(20.0, circle.getY(), EPSILON);
			assertEquals(5.0, circle.getRadius(), EPSILON);
		}

		@DisplayName("(point,radius) stores center and radius")
		@Test
		void constructor_pointRadius_storesValues() {
			MapCircle circle = new MapCircle(new Point2d(1.5, -2.5), 3.25);

			assertEquals(1.5, circle.getX(), EPSILON);
			assertEquals(-2.5, circle.getY(), EPSILON);
			assertEquals(3.25, circle.getRadius(), EPSILON);
		}

		@DisplayName("(id,x,y,radius) keeps explicit id")
		@Test
		void constructor_idxyz_storesId() {
			UUID id = UUID.randomUUID();
			MapCircle circle = new MapCircle(id, 0.0, 0.0, 2.0);

			assertEquals(id, circle.getUUID());
		}
	}

	@DisplayName("radius and diameter")
	@Nested
	public class RadiusDiameter {

		@DisplayName("getDiameter = 2 * radius")
		@Test
		void getDiameter_fromRadius() {
			MapCircle circle = new MapCircle(0.0, 0.0, 4.5);
			assertEquals(9.0, circle.getDiameter(), EPSILON);
		}

		@DisplayName("setRadius keeps absolute value")
		@Test
		void setRadius_negative_becomesPositive() {
			MapCircle circle = new MapCircle(0.0, 0.0, 1.0);

			circle.setRadius(-7.0);

			assertEquals(7.0, circle.getRadius(), EPSILON);
			assertEquals(14.0, circle.getDiameter(), EPSILON);
		}

		@DisplayName("setDiameter keeps absolute value and updates radius")
		@Test
		void setDiameter_negative_becomesPositiveAndHalved() {
			MapCircle circle = new MapCircle(0.0, 0.0, 1.0);

			circle.setDiameter(-8.0);

			assertEquals(4.0, circle.getRadius(), EPSILON);
			assertEquals(8.0, circle.getDiameter(), EPSILON);
		}
	}

	@DisplayName("distance")
	@Nested
	public class Distance {

		@DisplayName("distance(map point) subtracts radius")
		@Test
		void distanceToMapPoint_subtractsRadius() {
			MapCircle circle = new MapCircle(0.0, 0.0, 2.0);
			MapPoint point = new MapPoint(10.0, 0.0);

			// center distance = 10, then - radius => 8
			assertEquals(8.0, circle.distance(point), EPSILON);
		}

		@DisplayName("getDistance(point2d) subtracts radius")
		@Test
		void getDistance_point2d_subtractsRadius() {
			MapCircle circle = new MapCircle(0.0, 0.0, 2.0);

			// center distance = 10, then - radius => 8
			assertEquals(8.0, circle.getDistance(new Point2d(10.0, 0.0)), EPSILON);
		}
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		@DisplayName("rectangle crossing circle boundary -> true")
		@Test
		void intersects_crossingRectangle_true() {
			MapCircle circle = new MapCircle(0.0, 0.0, 5.0);
			Rectangle2d rectangle = new Rectangle2d(4.0, -1.0, 4.0, 2.0);

			assertTrue(circle.intersects(rectangle));
		}

		@DisplayName("far rectangle -> false")
		@Test
		void intersects_farRectangle_false() {
			MapCircle circle = new MapCircle(0.0, 0.0, 2.0);
			Rectangle2d rectangle = new Rectangle2d(100.0, 100.0, 10.0, 10.0);

			assertFalse(circle.intersects(rectangle));
		}
	}

	@DisplayName("bounds and shape")
	@Nested
	public class BoundsAndShape {

		@DisplayName("bounding box is centered and sized with diameter")
		@Test
		void boundingBox_centeredOnCircle() {
			MapCircle circle = new MapCircle(10.0, 20.0, 3.0);

			Rectangle2d box = circle.getBoundingBox();

			assertEquals(7.0, box.getMinX(), EPSILON);
			assertEquals(17.0, box.getMinY(), EPSILON);
			assertEquals(6.0, box.getWidth(), EPSILON);
			assertEquals(6.0, box.getHeight(), EPSILON);
		}

		@DisplayName("shape is a circle with same center and radius")
		@Test
		void getShape_matchesGeometry() {
			MapCircle circle = new MapCircle(1.0, 2.0, 3.0);

			Shape2d<?> shape = circle.getShape();
			Rectangle2d bounds = shape.toBoundingBox();

			assertEquals(-2.0, bounds.getMinX(), EPSILON);
			assertEquals(-1.0, bounds.getMinY(), EPSILON);
			assertEquals(6.0, bounds.getWidth(), EPSILON);
			assertEquals(6.0, bounds.getHeight(), EPSILON);
		}
	}

	@DisplayName("equals/hashCode")
	@Nested
	public class EqualsHashCode {

		@DisplayName("same geometry and id -> equals")
		@Test
		void equals_sameValues_true() {
			UUID id = UUID.randomUUID();
			MapCircle a = new MapCircle(id, 5.0, 6.0, 7.0);
			MapCircle b = new MapCircle(id, 5.0, 6.0, 7.0);

			assertTrue(a.equals(b));
			assertEquals(a.hashCode(), b.hashCode());
		}

		@DisplayName("different radius -> not equals")
		@Test
		void equals_differentRadius_false() {
			UUID id = UUID.randomUUID();
			MapCircle a = new MapCircle(id, 5.0, 6.0, 7.0);
			MapCircle b = new MapCircle(id, 5.0, 6.0, 8.0);

			assertFalse(a.equals(b));
		}
	}

	@DisplayName("toJson")
	@Nested
	public class ToJson {

		@DisplayName("json contains radius")
		@Test
		void toJson_containsRadius() {
			MapCircle circle = new MapCircle(0.0, 0.0, 12.5);
			JsonBuffer buffer = new JsonBuffer();

			circle.toJson(buffer);

			String json = buffer.toString();
			assertTrue(json.contains("radius"));
			assertTrue(json.contains("12.5"));
		}
	}
}

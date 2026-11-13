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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationNowhere;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MapElement")
@SuppressWarnings("all")
public class MapElementTest extends AbstractGisTestCase {

	/** Minimal concrete implementation for testing abstract MapElement behavior. */
	private static class TestMapElement extends MapElement {

		private static final long serialVersionUID = 1L;

		private final double x;
		private final double y;

		TestMapElement(UUID id, double x, double y) {
			super(id, new HeapAttributeCollection());
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(MapElement element) {
			if (element instanceof TestMapElement other) {
				return getUUID().equals(other.getUUID())
						&& this.x == other.x
						&& this.y == other.y;
			}
			return false;
		}

		@Override
		public int hashCode() {
			int h = getUUID().hashCode();
			h = 31 * h + Double.hashCode(this.x);
			h = 31 * h + Double.hashCode(this.y);
			return h;
		}

		@Override
		public double getDistance(Point2D<?, ?> point) {
			final double dx = point.getX() - this.x;
			final double dy = point.getY() - this.y;
			return Math.hypot(dx, dy);
		}

		@Override
		public boolean contains(Point2D<?, ?> point, double delta) {
			return getDistance(point) <= Math.abs(delta);
		}

		@Override
		public boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> rectangle) {
			return getBoundingBox().intersects(rectangle);
		}

		@Override
		protected Rectangle2d calcBounds() {
			return new Rectangle2d(this.x - 1., this.y - 1., 2., 2.);
		}

		@Override
		public Shape2d<?> getShape() {
			return getBoundingBox();
		}

		@Override
		public GeoLocation getGeoLocation() {
			return new GeoLocationNowhere(getUUID());
		}
	}

	@DisplayName("flags")
	@Nested
	public class Flags {

		@DisplayName("set/unset/switch selected flag")
		@Test
		void selectedFlag_lifecycle() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 0., 0.);

			assertFalse(element.hasFlag(MapElement.FLAG_SELECTED));

			element.setFlag(MapElement.FLAG_SELECTED);
			assertTrue(element.hasFlag(MapElement.FLAG_SELECTED));

			element.switchFlag(MapElement.FLAG_SELECTED);
			assertFalse(element.hasFlag(MapElement.FLAG_SELECTED));

			element.unsetFlag(MapElement.FLAG_SELECTED);
			assertFalse(element.hasFlag(MapElement.FLAG_SELECTED));
		}

		@DisplayName("read-only flag through API")
		@Test
		void readOnlyFlag_api() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 0., 0.);

			assertFalse(element.isReadOnlyObject());

			element.setReadOnlyObject(true);
			assertTrue(element.isReadOnlyObject());

			element.setReadOnlyObject(false);
			assertFalse(element.isReadOnlyObject());
		}
	}

	@DisplayName("color")
	@Nested
	public class Color {

		@DisplayName("raw color is null by default")
		@Test
		void rawColor_defaultNull() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 0., 0.);
			assertEquals(null, element.getRawColor());
		}

		@DisplayName("setColor updates raw color and effective color")
		@Test
		void setColor_updatesColor() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 0., 0.);

			element.setColor(0x11223344);

			assertEquals(Integer.valueOf(0x11223344), element.getRawColor());
			assertEquals(0x11223344, element.getColor());
		}

		@DisplayName("container-color usage flag")
		@Test
		void containerColorFlag() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 0., 0.);

			assertFalse(element.isContainerColorUsed());

			element.setContainerColorUse(true);
			assertTrue(element.isContainerColorUsed());

			element.setContainerColorUse(false);
			assertFalse(element.isContainerColorUsed());
		}
	}

	@DisplayName("visualization type")
	@Nested
	public class VisualizationTypeTest {

		@DisplayName("default visualization type")
		@Test
		void defaultVisualizationType() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 0., 0.);
			assertEquals(MapElement.VisualizationType.SHAPE_ONLY, element.getVisualizationType());
		}

		@DisplayName("set/get visualization type")
		@Test
		void setGetVisualizationType() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 0., 0.);

			element.setVisualizationType(MapElement.VisualizationType.ICON_ONLY);

			assertEquals(MapElement.VisualizationType.ICON_ONLY, element.getVisualizationType());
		}

		@DisplayName("enum helpers isShape/isIcon")
		@Test
		void enumHelpers() {
			assertTrue(MapElement.VisualizationType.SHAPE_ONLY.isShape());
			assertFalse(MapElement.VisualizationType.SHAPE_ONLY.isIcon());

			assertFalse(MapElement.VisualizationType.ICON_ONLY.isShape());
			assertTrue(MapElement.VisualizationType.ICON_ONLY.isIcon());

			assertTrue(MapElement.VisualizationType.SHAPE_AND_ICON.isShape());
			assertTrue(MapElement.VisualizationType.SHAPE_AND_ICON.isIcon());
		}
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		@DisplayName("contains(point) delegates to contains(point,0)")
		@Test
		void contains_point_withoutDelta() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 10., 10.);

			assertTrue(element.contains(new Point2d(10., 10.)));
			assertFalse(element.contains(new Point2d(10.1, 10.)));
		}

		@DisplayName("contains(point,delta)")
		@Test
		void contains_point_withDelta() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 10., 10.);

			assertTrue(element.contains(new Point2d(10.5, 10.0), 1.0));
			assertFalse(element.contains(new Point2d(12.0, 10.0), 1.0));
		}
	}

	@DisplayName("distance")
	@Nested
	public class Distance {

		@DisplayName("getDistance computes euclidean distance")
		@Test
		void getDistance_euclidean() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 0., 0.);
			assertEquals(5.0, element.getDistance(new Point2d(3., 4.)), EPSILON);
		}
	}

	@DisplayName("bounds/intersection")
	@Nested
	public class BoundsIntersection {

		@DisplayName("bounding box from calcBounds")
		@Test
		void boundingBox_matchesCalcBounds() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 5., 6.);
			Rectangle2d box = element.getBoundingBox();

			assertEquals(4., box.getMinX(), EPSILON);
			assertEquals(5., box.getMinY(), EPSILON);
			assertEquals(2., box.getWidth(), EPSILON);
			assertEquals(2., box.getHeight(), EPSILON);
		}

		@DisplayName("intersects uses bounding boxes")
		@Test
		void intersects_withRectangle() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 0., 0.);
			Rectangle2d near = new Rectangle2d(-0.5, -0.5, 1., 1.);
			Rectangle2d far = new Rectangle2d(100., 100., 2., 2.);

			assertTrue(element.intersects(near));
			assertFalse(element.intersects(far));
		}
	}

	@DisplayName("equals/hashCode")
	@Nested
	public class EqualsHashCode {

		@DisplayName("equals(Object) with same values")
		@Test
		void equals_sameValues() {
			UUID id = UUID.randomUUID();
			TestMapElement a = new TestMapElement(id, 1., 2.);
			TestMapElement b = new TestMapElement(id, 1., 2.);

			assertTrue(a.equals((Object) b));
			assertTrue(a.equals((MapElement) b));
			assertEquals(a.hashCode(), b.hashCode());
		}

		@DisplayName("equals(Object) with different values")
		@Test
		void equals_differentValues() {
			TestMapElement a = new TestMapElement(UUID.randomUUID(), 1., 2.);
			TestMapElement b = new TestMapElement(UUID.randomUUID(), 1., 2.);

			assertFalse(a.equals((Object) b));
			assertNotEquals(a.hashCode(), b.hashCode());
		}
	}

	@DisplayName("toJson")
	@Nested
	public class ToJson {

		@DisplayName("json contains expected editable fields")
		@Test
		void toJson_containsFields() {
			TestMapElement element = new TestMapElement(UUID.randomUUID(), 0., 0.);
			element.setColor(0xFF0000FF);
			element.setReadOnlyObject(true);
			element.setFlag(MapElement.FLAG_SELECTED);
			element.setVisualizationType(MapElement.VisualizationType.SHAPE_AND_ICON);

			JsonBuffer buffer = new JsonBuffer();
			element.toJson(buffer);

			String json = buffer.toString();
			assertTrue(json.contains("color"));
			assertTrue(json.contains("selected"));
			assertTrue(json.contains("readOnly"));
			assertTrue(json.contains("visualizationType"));
		}
	}
}

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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.mapelement.MapComposedElement;
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

@DisplayName("MapComposedElement")
@SuppressWarnings("all")
public class MapComposedElementTest extends AbstractGisTestCase {

	private static final double EPSILON = 1e-10;

	/** Minimal concrete implementation for testing abstract MapComposedElement behavior. */
	private static class TestMapComposedElement extends MapComposedElement {

		private static final long serialVersionUID = 1L;

		TestMapComposedElement(UUID id) {
			super(id, new HeapAttributeCollection());
		}

		@Override
		public double getDistance(Point2D<?, ?> point) {
			if (getPointCount() == 0) {
				return Double.POSITIVE_INFINITY;
			}
			double min = Double.POSITIVE_INFINITY;
			for (Point2d p : points()) {
				double d = p.getDistance(point);
				if (d < min) {
					min = d;
				}
			}
			return min;
		}

		@Override
		public boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> rectangle) {
			Rectangle2d b = getBoundingBox();
			return b != null && b.intersects(rectangle);
		}

		@Override
		public Shape2d<?> getShape() {
			Rectangle2d b = getBoundingBox();
			return b == null ? new Rectangle2d() : b;
		}
	}

	@DisplayName("add/clear basics")
	@Nested
	public class AddClear {

		@DisplayName("initial counts are zero")
		@Test
		void initialCounts() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			assertEquals(0, elt.getPointCount());
			assertEquals(0, elt.getGroupCount());
		}

		@DisplayName("addPoint creates first group")
		@Test
		void addPoint_createsFirstGroup() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			int idx = elt.addPoint(1.0, 2.0);

			assertEquals(0, idx);
			assertEquals(1, elt.getPointCount());
			assertEquals(1, elt.getGroupCount());
			assertEquals(1.0, elt.getPointAt(0).getX(), EPSILON);
			assertEquals(2.0, elt.getPointAt(0).getY(), EPSILON);
		}

		@DisplayName("addGroup creates new group boundary")
		@Test
		void addGroup_createsSecondGroup() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0);
			elt.addPoint(1.0, 1.0);
			int idx = elt.addGroup(10.0, 10.0);

			assertEquals(2, idx);
			assertEquals(3, elt.getPointCount());
			assertEquals(2, elt.getGroupCount());
			assertEquals(2, elt.getFirstPointIndexInGroup(1));
			assertEquals(2, elt.getLastPointIndexInGroup(1));
		}

		@DisplayName("clear removes all points and groups")
		@Test
		void clear_removesAll() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0);
			elt.addGroup(5.0, 5.0);

			elt.clear();

			assertEquals(0, elt.getPointCount());
			assertEquals(0, elt.getGroupCount());
		}
	}

	@DisplayName("group indexing")
	@Nested
	public class GroupIndexing {

		@DisplayName("point count in groups")
		@Test
		void pointCountInGroup() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0); // group 0
			elt.addPoint(1.0, 1.0); // group 0
			elt.addGroup(10.0, 10.0); // group 1
			elt.addPoint(11.0, 11.0, 1); // group 1

			assertEquals(2, elt.getPointCountInGroup(0));
			assertEquals(2, elt.getPointCountInGroup(1));
		}

		@DisplayName("getPointIndex clamps local position")
		@Test
		void getPointIndex_clampsPosition() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0);
			elt.addPoint(1.0, 1.0);

			assertEquals(0, elt.getPointIndex(0, -100));
			assertEquals(1, elt.getPointIndex(0, 100));
		}

		@DisplayName("getPointAt with negative global index")
		@Test
		void getPointAt_negativeIndexFromEnd() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(4.0, 5.0);
			elt.addPoint(6.0, 7.0);

			Point2d p = elt.getPointAt(-1);
			assertEquals(6.0, p.getX(), EPSILON);
			assertEquals(7.0, p.getY(), EPSILON);
		}
	}

	@DisplayName("contains / containsPoint")
	@Nested
	public class ContainsApi {

		@DisplayName("containsPoint(global)")
		@Test
		void containsPoint_global() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(1.0, 1.0);

			assertTrue(elt.containsPoint(new Point2d(1.0, 1.0)));
			assertFalse(elt.containsPoint(new Point2d(2.0, 2.0)));
		}

		@DisplayName("containsPoint(group)")
		@Test
		void containsPoint_group() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0); // g0
			elt.addGroup(10.0, 10.0); // g1

			assertTrue(elt.containsPoint(new Point2d(10.0, 10.0), 1));
			assertFalse(elt.containsPoint(new Point2d(10.0, 10.0), 0));
		}

		@DisplayName("contains(point,delta) uses distance")
		@Test
		void contains_point_delta() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0);

			assertTrue(elt.contains(new Point2d(0.5, 0.0), 1.0));
			assertFalse(elt.contains(new Point2d(2.0, 0.0), 1.0));
		}
	}

	@DisplayName("set/insert/remove points")
	@Nested
	public class PointMutations {

		@DisplayName("insertPointAt in group")
		@Test
		void insertPointAt_group() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0);
			elt.addPoint(2.0, 2.0);

			int global = elt.insertPointAt(1.0, 1.0, 0, 1);

			assertEquals(1, global);
			assertEquals(3, elt.getPointCount());
			assertEquals(1.0, elt.getPointAt(1).getX(), EPSILON);
			assertEquals(1.0, elt.getPointAt(1).getY(), EPSILON);
		}

		@DisplayName("setPointAt returns true when modified")
		@Test
		void setPointAt_modified_true() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0);

			assertTrue(elt.setPointAt(0, 3.0, 4.0));
			assertEquals(3.0, elt.getPointAt(0).getX(), EPSILON);
			assertEquals(4.0, elt.getPointAt(0).getY(), EPSILON);
		}

		@DisplayName("setPointAt returns false when same value")
		@Test
		void setPointAt_same_false() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0);

			assertFalse(elt.setPointAt(0, 0.0, 0.0));
		}

		@DisplayName("removePointAt removes targeted point")
		@Test
		void removePointAt_removesPoint() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0);
			elt.addPoint(1.0, 1.0);

			Point2d removed = elt.removePointAt(0, 0);

			assertEquals(0.0, removed.getX(), EPSILON);
			assertEquals(0.0, removed.getY(), EPSILON);
			assertEquals(1, elt.getPointCount());
			assertEquals(1.0, elt.getPointAt(0).getX(), EPSILON);
		}

		@DisplayName("removeGroupAt removes full group")
		@Test
		void removeGroupAt_removesGroup() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0); // g0
			elt.addGroup(10.0, 10.0); // g1

			assertTrue(elt.removeGroupAt(1));
			assertEquals(1, elt.getGroupCount());
			assertEquals(1, elt.getPointCount());
			assertEquals(0.0, elt.getPointAt(0).getX(), EPSILON);
		}
	}

	@DisplayName("invert")
	@Nested
	public class InvertApi {

		@DisplayName("invertPointsIn reverses only one group")
		@Test
		void invertPointsIn_singleGroup() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(1.0, 1.0);
			elt.addPoint(2.0, 2.0);
			elt.addPoint(3.0, 3.0);

			elt.invertPointsIn(0);

			assertEquals(3.0, elt.getPointAt(0).getX(), EPSILON);
			assertEquals(2.0, elt.getPointAt(1).getX(), EPSILON);
			assertEquals(1.0, elt.getPointAt(2).getX(), EPSILON);
		}

		@DisplayName("invert reverses global order")
		@Test
		void invert_globalOrder() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(1.0, 1.0);
			elt.addPoint(2.0, 2.0);
			elt.addPoint(3.0, 3.0);

			elt.invert();

			assertEquals(3.0, elt.getPointAt(0).getX(), EPSILON);
			assertEquals(2.0, elt.getPointAt(1).getX(), EPSILON);
			assertEquals(1.0, elt.getPointAt(2).getX(), EPSILON);
		}
	}

	@DisplayName("iterators")
	@Nested
	public class Iterators {

		@DisplayName("point iterator traverses all points")
		@Test
		void pointIterator_traversal() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(1.0, 1.0);
			elt.addPoint(2.0, 2.0);

			Iterator<Point2d> it = elt.pointIterator();
			assertTrue(it.hasNext());
			assertEquals(1.0, it.next().getX(), EPSILON);
			assertTrue(it.hasNext());
			assertEquals(2.0, it.next().getX(), EPSILON);
			assertFalse(it.hasNext());
			assertThrows(NoSuchElementException.class, it::next);
		}

		@DisplayName("group iterator traverses all groups")
		@Test
		void groupIterator_traversal() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0);
			elt.addGroup(10.0, 10.0);

			Iterator<MapComposedElement.PointGroup> it = elt.groupIterator();
			assertTrue(it.hasNext());
			assertEquals(0, it.next().getGroupIndex());
			assertTrue(it.hasNext());
			assertEquals(1, it.next().getGroupIndex());
			assertFalse(it.hasNext());
		}
	}

	@DisplayName("point groups")
	@Nested
	public class PointGroupApi {

		@DisplayName("group add/get/set/remove")
		@Test
		void pointGroup_mutations() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(0.0, 0.0);

			MapComposedElement.PointGroup g0 = elt.getGroupAt(0);
			g0.add(1.0, 1.0);
			assertEquals(2, g0.size());

			g0.set(1, 2.0, 2.0);
			assertEquals(2.0, g0.get(1).getX(), EPSILON);

			Point2d removed = g0.removeAt(1);
			assertEquals(2.0, removed.getX(), EPSILON);
			assertEquals(1, g0.size());
		}
	}

	@DisplayName("json")
	@Nested
	public class JsonApi {

		@DisplayName("toJson contains groups field")
		@Test
		void toJson_containsGroups() {
			TestMapComposedElement elt = new TestMapComposedElement(UUID.randomUUID());
			elt.addPoint(1.0, 2.0);

			JsonBuffer buffer = new JsonBuffer();
			elt.toJson(buffer);

			String json = buffer.toString();
			assertTrue(json.contains("groups"));
		}
	}
}

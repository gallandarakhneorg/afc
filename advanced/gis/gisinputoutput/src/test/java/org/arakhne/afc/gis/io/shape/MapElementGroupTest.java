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

package org.arakhne.afc.gis.io.shape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.mapelement.MapCircle;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapMultiPoint;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.mapelement.MapPolygon;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.testtools.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("MapElementGroup")
@SuppressWarnings("all")
public class MapElementGroupTest extends AbstractTestCase {

	private MapPoint point;
	private MapMultiPoint multiPoint;
	private MapPolyline polyline;
	private MapPolygon polygon;
	private MapCircle circle;

	private MapElementGroup group;

	@BeforeEach
	public void setUp() throws Exception {
		point = new MapPoint(0, 0);
		multiPoint = new MapMultiPoint();
		multiPoint.addPoint(0,  0);
		multiPoint.addPoint(1,  1);
		polyline = new MapPolyline();
		polyline.addPoint(0,  0);
		polyline.addPoint(1,  1);
		polygon = new MapPolygon();
		polygon.addPoint(0,  0);
		polygon.addPoint(1,  1);
		circle = new MapCircle(0, 0, 1);
		group = new MapElementGroup();
	}

	@AfterEach
	public void tearDown() {
		group = null;
	}

	@DisplayName("classifiesElement")
	@Nested
	public class ClassifiesElementTest {

		@DisplayName("(MapPoint)")
		@Test
		public void classifiesElementMapPoint() {
			assertEquals(ShapeElementType.POINT, MapElementGroup.classifiesElement(new MapPoint(0, 0)));
		}

		@DisplayName("(MapMultiPoint)")
		@Test
		public void classifiesElementMapMultiPoint() {
			assertEquals(ShapeElementType.MULTIPOINT, MapElementGroup.classifiesElement(new MapMultiPoint()));
		}

		@DisplayName("(MapPolyline)")
		@Test
		public void classifiesElementMapPolyline() {
			assertEquals(ShapeElementType.POLYLINE, MapElementGroup.classifiesElement(new MapPolyline()));
		}

		@DisplayName("(MapPolygon)")
		@Test
		public void classifiesElementMapPolygon() {
			assertEquals(ShapeElementType.POLYGON, MapElementGroup.classifiesElement(new MapPolygon()));
		}

		@DisplayName("(MapCircle)")
		@Test
		public void classifiesElementMapCircle() {
			assertEquals(ShapeElementType.UNSUPPORTED, MapElementGroup.classifiesElement(new MapCircle(0, 0)));
		}

	}

	@DisplayName("classifiesElements")
	@Nested
	public class ClassifiesElementsTest {

		@DisplayName("(Map, Iterator)")
		@Nested
		public class ClassifiesElementMapIteratorTest {

			@DisplayName("#1")
			@Test
			public void classifiesElementsMapIterator() {
				Map<ShapeElementType, MapElementGroup> map = new TreeMap<>();
		
				MapElementGroup.classifiesElements(map,
						Arrays.asList(point, multiPoint, polyline, polygon, circle).iterator());
		
				assertSame(point, map.get(ShapeElementType.POINT).elements.iterator().next());
				assertSame(multiPoint, map.get(ShapeElementType.MULTIPOINT).elements.iterator().next());
				assertSame(polyline, map.get(ShapeElementType.POLYLINE).elements.iterator().next());
				assertSame(polygon, map.get(ShapeElementType.POLYGON).elements.iterator().next());
			}
		}

		@DisplayName("(Map, Collection)")
		@Nested
		public class ClassifiesElementMapCollectionTest {

			@DisplayName("#1")
			@Test
			public void classifiesElementsCollection() {
				Map<ShapeElementType, MapElementGroup> map = new TreeMap<>();
	
				MapElementGroup.classifiesElements(map,
						Arrays.asList(point, multiPoint, polyline, polygon, circle));
	
				assertSame(point, map.get(ShapeElementType.POINT).elements.iterator().next());
				assertSame(multiPoint, map.get(ShapeElementType.MULTIPOINT).elements.iterator().next());
				assertSame(polyline, map.get(ShapeElementType.POLYLINE).elements.iterator().next());
				assertSame(polygon, map.get(ShapeElementType.POLYGON).elements.iterator().next());
			}

		}

		@DisplayName("(Iterator)")
		@Nested
		public class ClassifiesElementIteratorTest {

			@DisplayName("#1")
			@Test
			public void classifiesElementsIterator() {
				Map<ShapeElementType, MapElementGroup> map = MapElementGroup.classifiesElements(
						Arrays.asList(point, multiPoint, polyline, polygon, circle).iterator());
	
				assertSame(point, map.get(ShapeElementType.POINT).elements.iterator().next());
				assertSame(multiPoint, map.get(ShapeElementType.MULTIPOINT).elements.iterator().next());
				assertSame(polyline, map.get(ShapeElementType.POLYLINE).elements.iterator().next());
				assertSame(polygon, map.get(ShapeElementType.POLYGON).elements.iterator().next());
			}

		}

		@DisplayName("(Collection)")
		@Nested
		public class ClassifiesElementCollectionTest {

			@DisplayName("#1")
			@Test
			public void classifiesElementsMapCollection() {
				Map<ShapeElementType, MapElementGroup> map = MapElementGroup.classifiesElements(
						Arrays.asList(point, multiPoint, polyline, polygon, circle));
	
				assertSame(point, map.get(ShapeElementType.POINT).elements.iterator().next());
				assertSame(multiPoint, map.get(ShapeElementType.MULTIPOINT).elements.iterator().next());
				assertSame(polyline, map.get(ShapeElementType.POLYLINE).elements.iterator().next());
				assertSame(polygon, map.get(ShapeElementType.POLYGON).elements.iterator().next());
			}

		}

	}

	@DisplayName("add(MapElement)")
	@Nested
	public class AddMapElementTest {
		
		@DisplayName("#1")
		@Test
		public void addMapElement() {
			group.add(point);
			group.add(multiPoint);
			group.add(polyline);
			group.add(polygon);
			group.add(circle);
			Iterator<? extends MapElement> iterator = group.elements.iterator();
			assertTrue(iterator.hasNext());
			assertSame(point, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(multiPoint, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(polyline, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(polygon, iterator.next());
			assertTrue(iterator.hasNext());
			assertSame(circle, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#2")
		@Test
		public void addNoElements() {
		    Iterator<? extends MapElement> iterator = group.elements.iterator();
		    assertFalse(iterator.hasNext());
		}

		@DisplayName("#3")
		@Test
		public void addSingleElement() {
		    group.add(point);

		    Iterator<? extends MapElement> iterator = group.elements.iterator();
		    assertTrue(iterator.hasNext());
		    assertSame(point, iterator.next());
		    assertFalse(iterator.hasNext());
		}

	}

	@DisplayName("getBounds")
	@Nested
	public class GetBoundsTest {
		
		@DisplayName("#1")
		@Test
		public void getBounds() {
			group.add(point);
			group.add(multiPoint);
			group.add(polyline);
			group.add(polygon);
			group.add(circle);
			ESRIBounds bounds = group.getBounds();
			assertEpsilonEquals(-1, bounds.getMinX());
			assertEpsilonEquals(-1, bounds.getMinY());
			assertEpsilonEquals(1, bounds.getMaxX());
			assertEpsilonEquals(1, bounds.getMaxY());
		}

		@DisplayName("#2")
		@Test
		public void getBoundsWhenEmpty() {
		    ESRIBounds bounds = group.getBounds();

		    assertNaN(bounds.getMinX());
		    assertNaN(bounds.getMinY());
		    assertNaN(bounds.getMaxX());
		    assertNaN(bounds.getMaxY());
		}

		@DisplayName("#3")
		@Test
		public void getBoundsIndependentOfInsertionOrder() {
		    group.add(circle);
		    group.add(polygon);
		    group.add(polyline);
		    group.add(multiPoint);
		    group.add(point);

		    ESRIBounds bounds = group.getBounds();
		    assertEpsilonEquals(-1, bounds.getMinX());
		    assertEpsilonEquals(-1, bounds.getMinY());
		    assertEpsilonEquals(1, bounds.getMaxX());
		    assertEpsilonEquals(1, bounds.getMaxY());
		}
		
	}

}

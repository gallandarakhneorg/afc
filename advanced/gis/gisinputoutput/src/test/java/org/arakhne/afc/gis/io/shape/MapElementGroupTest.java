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
		this.point = new MapPoint(0, 0);
		this.multiPoint = new MapMultiPoint();
		this.multiPoint.addPoint(0,  0);
		this.multiPoint.addPoint(1,  1);
		this.polyline = new MapPolyline();
		this.polyline.addPoint(0,  0);
		this.polyline.addPoint(1,  1);
		this.polygon = new MapPolygon();
		this.polygon.addPoint(0,  0);
		this.polygon.addPoint(1,  1);
		this.circle = new MapCircle(0, 0, 1);
		this.group = new MapElementGroup();
	}

	@AfterEach
	public void tearDown() {
		this.group = null;
	}

	@Test
	public void classifiesElementMapElement() {
		assertEquals(ShapeElementType.POINT, MapElementGroup.classifiesElement(new MapPoint(0, 0)));
		assertEquals(ShapeElementType.MULTIPOINT, MapElementGroup.classifiesElement(new MapMultiPoint()));
		assertEquals(ShapeElementType.POLYLINE, MapElementGroup.classifiesElement(new MapPolyline()));
		assertEquals(ShapeElementType.POLYGON, MapElementGroup.classifiesElement(new MapPolygon()));
		assertEquals(ShapeElementType.UNSUPPORTED, MapElementGroup.classifiesElement(new MapCircle(0, 0)));
	}

	@Test
	public void classifiesElementsMapIterator() {
		Map<ShapeElementType, MapElementGroup> map = new TreeMap<>();

		MapElementGroup.classifiesElements(map,
				Arrays.asList(this.point, this.multiPoint, this.polyline, this.polygon, this.circle).iterator());

		assertSame(this.point, map.get(ShapeElementType.POINT).elements.iterator().next());
		assertSame(this.multiPoint, map.get(ShapeElementType.MULTIPOINT).elements.iterator().next());
		assertSame(this.polyline, map.get(ShapeElementType.POLYLINE).elements.iterator().next());
		assertSame(this.polygon, map.get(ShapeElementType.POLYGON).elements.iterator().next());
	}

	@Test
	public void classifiesElementsCollection() {
		Map<ShapeElementType, MapElementGroup> map = new TreeMap<>();

		MapElementGroup.classifiesElements(map,
				Arrays.asList(this.point, this.multiPoint, this.polyline, this.polygon, this.circle));

		assertSame(this.point, map.get(ShapeElementType.POINT).elements.iterator().next());
		assertSame(this.multiPoint, map.get(ShapeElementType.MULTIPOINT).elements.iterator().next());
		assertSame(this.polyline, map.get(ShapeElementType.POLYLINE).elements.iterator().next());
		assertSame(this.polygon, map.get(ShapeElementType.POLYGON).elements.iterator().next());
	}

	@Test
	public void classifiesElementsIterator() {
		Map<ShapeElementType, MapElementGroup> map = MapElementGroup.classifiesElements(
				Arrays.asList(this.point, this.multiPoint, this.polyline, this.polygon, this.circle).iterator());

		assertSame(this.point, map.get(ShapeElementType.POINT).elements.iterator().next());
		assertSame(this.multiPoint, map.get(ShapeElementType.MULTIPOINT).elements.iterator().next());
		assertSame(this.polyline, map.get(ShapeElementType.POLYLINE).elements.iterator().next());
		assertSame(this.polygon, map.get(ShapeElementType.POLYGON).elements.iterator().next());
	}

	@Test
	public void classifiesElementsMapCollection() {
		Map<ShapeElementType, MapElementGroup> map = MapElementGroup.classifiesElements(
				Arrays.asList(this.point, this.multiPoint, this.polyline, this.polygon, this.circle));

		assertSame(this.point, map.get(ShapeElementType.POINT).elements.iterator().next());
		assertSame(this.multiPoint, map.get(ShapeElementType.MULTIPOINT).elements.iterator().next());
		assertSame(this.polyline, map.get(ShapeElementType.POLYLINE).elements.iterator().next());
		assertSame(this.polygon, map.get(ShapeElementType.POLYGON).elements.iterator().next());
	}

	@Test
	public void addMapElement() {
		this.group.add(this.point);
		this.group.add(this.multiPoint);
		this.group.add(this.polyline);
		this.group.add(this.polygon);
		this.group.add(this.circle);

		Iterator<? extends MapElement> iterator = this.group.elements.iterator();
		assertTrue(iterator.hasNext());
		assertSame(this.point, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.multiPoint, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.polyline, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.polygon, iterator.next());
		assertTrue(iterator.hasNext());
		assertSame(this.circle, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void getBounds() {
		this.group.add(this.point);
		this.group.add(this.multiPoint);
		this.group.add(this.polyline);
		this.group.add(this.polygon);
		this.group.add(this.circle);

		ESRIBounds bounds = this.group.getBounds();
		assertEpsilonEquals(-1, bounds.getMinX());
		assertEpsilonEquals(-1, bounds.getMinY());
		assertEpsilonEquals(1, bounds.getMaxX());
		assertEpsilonEquals(1, bounds.getMaxY());
	}

}

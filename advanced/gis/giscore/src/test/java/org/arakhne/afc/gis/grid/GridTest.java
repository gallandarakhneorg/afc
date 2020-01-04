/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.gis.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.AbstractGisTest;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** Unit test for Grid.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class GridTest extends AbstractGisTest {

	private ArrayList<MapPoint> reference = null;
	private Rectangle2d bounds = null;
	private Grid<MapPoint> grid = null;

	@BeforeEach
	public void setUp() throws Exception {
		this.reference = new ArrayList<>();
        this.reference.add(new MapPoint(1000,1000));
        this.reference.add(new MapPoint(0,0));
        this.reference.add(new MapPoint(501,801));
        this.reference.add(new MapPoint(500,800));
        this.reference.add(new MapPoint(502,802));
        this.reference.add(new MapPoint(100,800));
        this.reference.add(new MapPoint(800,0));

        List<Point2d> transformed = new ArrayList<>();
        for (final MapPoint it : this.reference) {
        	transformed.add(it.getPoint());
        }
        
        this.bounds = new Rectangle2d();
        this.bounds.setFromPointCloud(transformed);

        this.grid = new Grid<>(10, 10, this.bounds);
        for(MapPoint p : this.reference) {
        	this.grid.addElement(p);
        }
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.reference.clear();
		this.reference = null;
		this.bounds = null;
		this.grid = null;
	}

	@Test
	public void testGetGridCellsAround() {
		GridCell<MapPoint> cell;

		AroundCellIterable<MapPoint> iterable = this.grid.getGridCellsAround(new Point2d(480, 230), Double.POSITIVE_INFINITY);
		AroundCellIterator<MapPoint> iterator = iterable.aroundIterator();

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(7, cell.column());
		assertEquals(0, cell.row());
		assertEquals(3, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(8, cell.column());
		assertEquals(0, cell.row());
		assertEquals(4, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(0, cell.column());
		assertEquals(0, cell.row());
		assertEquals(4, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(0, cell.column());
		assertEquals(7, cell.row());
		assertEquals(5, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(1, cell.column());
		assertEquals(7, cell.row());
		assertEquals(5, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(4, cell.column());
		assertEquals(7, cell.row());
		assertEquals(5, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(5, cell.column());
		assertEquals(7, cell.row());
		assertEquals(5, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(0, cell.column());
		assertEquals(8, cell.row());
		assertEquals(6, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(1, cell.column());
		assertEquals(8, cell.row());
		assertEquals(6, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(4, cell.column());
		assertEquals(8, cell.row());
		assertEquals(6, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(5, cell.column());
		assertEquals(8, cell.row());
		assertEquals(6, iterator.getLevel());

		assertTrue(iterator.hasNext());
		cell = iterator.next();
		assertNotNull(cell);
		assertEquals(9, cell.column());
		assertEquals(9, cell.row());
		assertEquals(7, iterator.getLevel());

		assertFalse(iterator.hasNext());
	}

}

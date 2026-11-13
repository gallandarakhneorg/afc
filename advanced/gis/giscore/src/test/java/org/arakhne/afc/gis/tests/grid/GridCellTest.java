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

package org.arakhne.afc.gis.tests.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.arakhne.afc.gis.grid.GridCell;
import org.arakhne.afc.gis.grid.GridCellElement;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Unit test for GridCell.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@DisplayName("GridCell")
@SuppressWarnings("all")
public class GridCellTest {

    private GridCell<MapPoint> cell;
    private Rectangle2d cellBounds;
    private MapPoint point1;
    private MapPoint point2;
    private MapPoint point3;

    @BeforeEach
    public void setUp() {
        cellBounds = new Rectangle2d(0, 0, 10, 10);
        cell = new GridCell<>(5, 6, cellBounds);
        point1 = new MapPoint(2, 3);
        point2 = new MapPoint(5, 6);
        point3 = new MapPoint(8, 9);
    }

    @DisplayName("row")
    @Nested
    public class Row {

        @Test
        @DisplayName("Returns correct row")
        public void testRow() {
            assertEquals(5, cell.row());
        }
    }

    @DisplayName("column")
    @Nested
    public class Column {

        @Test
        @DisplayName("Returns correct column")
        public void testColumn() {
            assertEquals(6, cell.column());
        }
    }

    @DisplayName("getBounds")
    @Nested
    public class GetBounds {

        @Test
        @DisplayName("Returns the bounds passed to constructor")
        public void testGetBounds() {
            assertEquals(new Rectangle2d(0, 0, 10, 10), cell.getBounds());
        }
    }

    @DisplayName("isEmpty")
    @Nested
    public class IsEmpty {

        @Test
        @DisplayName("Returns true initially")
        public void testIsEmptyInitially() {
            assertTrue(cell.isEmpty());
        }
    }

    @DisplayName("getReferenceElementCount")
    @Nested
    public class GetReferenceElementCount {

        @Test
        @DisplayName("Returns 0 initially")
        public void testGetReferenceElementCountInitially() {
            assertEquals(0, cell.getReferenceElementCount());
        }
    }

    @DisplayName("addElement")
    @Nested
    public class AddElement {

        @Test
        @DisplayName("Adds element and updates reference count")
        public void testAddElement() {
            GridCellElement<MapPoint> gce = new GridCellElement<>(point1);
            assertTrue(cell.addElement(gce));
            assertFalse(cell.isEmpty());
            assertEquals(1, cell.getReferenceElementCount());

            Iterator<MapPoint> it = cell.iterator();
            assertTrue(it.hasNext());
            assertEquals(point1, it.next());
            assertFalse(it.hasNext());
        }

        @Test
        @DisplayName("With null returns false")
        public void testAddElementNull() {
            assertFalse(cell.addElement(null));
            assertTrue(cell.isEmpty());
            assertEquals(0, cell.getReferenceElementCount());
        }

        @Test
        @DisplayName("Duplicate returns false")
        public void testAddElementDuplicate() {
            GridCellElement<MapPoint> gce1 = new GridCellElement<>(point1);
            GridCellElement<MapPoint> gce2 = new GridCellElement<>(point1);
            assertTrue(cell.addElement(gce1));
            assertFalse(cell.addElement(gce2));
            assertEquals(1, cell.getReferenceElementCount());
            assertEquals(1, countElements(cell.iterator()));
        }

        @Test
        @DisplayName("Removes existing element and decrements count")
        public void testRemoveElement() {
            GridCellElement<MapPoint> gce = new GridCellElement<>(point1);
            cell.addElement(gce);
            GridCellElement<MapPoint> removed = cell.removeElement(point1);
            assertNotNull(removed);
            assertEquals(gce, removed);
            assertTrue(cell.isEmpty());
            assertEquals(0, cell.getReferenceElementCount());
            assertFalse(cell.iterator().hasNext());
        }

        @Test
        @DisplayName("With multiple elements updates count correctly")
        public void testAddMultipleElements() {
            cell.addElement(new GridCellElement<>(point1));
            cell.addElement(new GridCellElement<>(point2));
            cell.addElement(new GridCellElement<>(point3));
            assertEquals(3, cell.getReferenceElementCount());
            assertEquals(3, countElements(cell.iterator()));
            assertTrue(cell.iterator().hasNext());
        }

        private int countElements(Iterator<?> it) {
            int count = 0;
            while (it.hasNext()) {
                it.next();
                count++;
            }
            return count;
        }
    }

    @DisplayName("removeElement")
    @Nested
    public class RemoveElement {

        @Test
        @DisplayName("For non-existing returns null and count unchanged")
        public void testRemoveElementNonExisting() {
            GridCellElement<MapPoint> gce = new GridCellElement<>(point1);
            cell.addElement(gce);
            GridCellElement<MapPoint> removed = cell.removeElement(point2);
            assertNull(removed);
            assertEquals(1, cell.getReferenceElementCount());
        }
    }

    @DisplayName("indexOf")
    @Nested
    public class IndexOf {

        @BeforeEach
        public void addElements() {
            cell.addElement(new GridCellElement<>(point1));
            cell.addElement(new GridCellElement<>(point2));
            cell.addElement(new GridCellElement<>(point3));
        }

        @Test
        @DisplayName("indexOf returns correct index for existing element")
        public void testIndexOfExisting() {
            int idx = cell.indexOf(point2);
            assertTrue(idx >= 0);
            assertEquals(point2, cell.getElementAt(idx));
        }

        @Test
        @DisplayName("indexOf returns -1 for non-existing element")
        public void testIndexOfNonExisting() {
            MapPoint other = new MapPoint(99, 99);
            assertEquals(-1, cell.indexOf(other));
        }
    }

    @DisplayName("getElementAt")
    @Nested
    public class GetElementAt {

        @BeforeEach
        public void setUp() {
            cell.addElement(new GridCellElement<>(point1));
            cell.addElement(new GridCellElement<>(point2));
            cell.addElement(new GridCellElement<>(point3));
        }

        @Test
        @DisplayName("With valid index returns correct element")
        public void testGetElementAtValid() {
            // The order is determined by TreeSet ordering (natural order of MapPoint?)
            // We'll just check that we can retrieve all three.
            List<MapPoint> found = new ArrayList<>();
            for (int i = 0; i < cell.getReferenceElementCount(); i++) {
                found.add(cell.getElementAt(i));
            }
            assertTrue(found.contains(point1));
            assertTrue(found.contains(point2));
            assertTrue(found.contains(point3));
            assertEquals(3, found.size());
        }

        @Test
        @DisplayName("With negative index throws IndexOutOfBoundsException")
        public void testGetElementAtNegative() {
            assertThrows(IndexOutOfBoundsException.class, () -> cell.getElementAt(-1));
        }

        @Test
        @DisplayName("With index >= count")
        public void testGetElementAtTooHigh() {
            assertThrows(IndexOutOfBoundsException.class, () -> cell.getElementAt(3));
        }
    }

    @DisplayName("iterator")
    @Nested
    public class IteratorTest {

        @BeforeEach
        public void setUp() {
            // (3,4)-(8,9)
            // x : 3->8
            // y : 4->9
            cell.addElement(new GridCellElement<>(point1));
            cell.addElement(new GridCellElement<>(point2));
            cell.addElement(new GridCellElement<>(point3));
        }

        @DisplayName("()")
        @Nested
        public class WithoutArgument {

	        @Test
	        @DisplayName("Iterates over all elements")
	        public void testIterator() {
	            List<MapPoint> elements = new ArrayList<>();
	            for (MapPoint p : cell) {
	                elements.add(p);
	            }
	            assertEquals(3, elements.size());
	            assertTrue(elements.contains(point1));
	            assertTrue(elements.contains(point2));
	            assertTrue(elements.contains(point3));
	        }
        }

        @DisplayName("(Rectangle2afp)")
        @Nested
        public class WithRectangle {

	        @Test
	        @DisplayName("Returns elements intersecting bounds")
	        public void testIteratorWithBounds() {
	            Rectangle2d bounds = new Rectangle2d(3, 4, 5, 5);
	            // (3,4)-(8,9)
	            // x : 3->8
	            // y : 4->9
	            Iterator<MapPoint> it = cell.iterator(bounds);
	            List<MapPoint> result = new ArrayList<>();
	            while (it.hasNext()) {
	                result.add(it.next());
	            }
	            assertEquals(2, result.size());
	            assertTrue(result.contains(point2));
	            assertTrue(result.contains(point3));
	        }
	
	        @Test
	        @DisplayName("With no intersection returns empty")
	        public void testIteratorWithBoundsNoIntersection() {
	            Rectangle2d bounds = new Rectangle2d(100, 100, 10, 10);
	            Iterator<MapPoint> it = cell.iterator(bounds);
	            assertFalse(it.hasNext());
	            assertThrows(NoSuchElementException.class, it::next);
	        }
        }
    }

    @DisplayName("getGridCellElements")
    @Nested
    public class GetGridCellElements {

        @BeforeEach
        public void setUp() {
            cell.addElement(new GridCellElement<>(point1));
            cell.addElement(new GridCellElement<>(point2));
            cell.addElement(new GridCellElement<>(point3));
        }

        @Test
        @DisplayName("Returns iterator over GridCellElement")
        public void testGetGridCellElements() {
            Iterator<GridCellElement<MapPoint>> it = cell.getGridCellElements();
            List<GridCellElement<MapPoint>> elements = new ArrayList<>();
            while (it.hasNext()) {
                elements.add(it.next());
            }
            assertEquals(3, elements.size());
            // Check that each contains the corresponding point
            List<MapPoint> contained = new ArrayList<>();
            for (GridCellElement<MapPoint> gce : elements) {
                contained.add(gce.get());
            }
            assertTrue(contained.contains(point1));
            assertTrue(contained.contains(point2));
            assertTrue(contained.contains(point3));
        }
    }
}

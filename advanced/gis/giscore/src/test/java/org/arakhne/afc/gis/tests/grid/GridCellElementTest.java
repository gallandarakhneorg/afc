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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.arakhne.afc.gis.grid.GridCell;
import org.arakhne.afc.gis.grid.GridCellElement;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Unit test for GridCellElement.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@DisplayName("GridCellElement")
@SuppressWarnings("all")
public class GridCellElementTest {

    private GISPrimitive primitive1;
    private GISPrimitive primitive2;
    private GridCellElement<GISPrimitive> element;
    private GridCell cell1;
    private GridCell cell2;

    @BeforeEach
    public void setUp() {
        primitive1 = mock(GISPrimitive.class);
        primitive2 = mock(GISPrimitive.class);
        element = new GridCellElement<>(primitive1);
        cell1 = mock(GridCell.class);
        cell2 = mock(GridCell.class);
    }

    @DisplayName("get")
    @Nested
    public class Get {

        @Test
        @DisplayName("Returns the element passed in constructor")
        void testGet() {
            assertSame(primitive1, element.get());
        }
    }

    @DisplayName("addCellLink")
    @Nested
    public class AddCellLink {

        @Test
        @DisplayName("Returns true for first cell (reference cell)")
        void testAddCellLinkFirstReference() {
            assertTrue(element.addCellLink(cell1));
            assertTrue(element.isReferenceCell(cell1));
        }

        @Test
        @DisplayName("Returns false for non-reference cells")
        void testAddCellLinkNonReference() {
            element.addCellLink(cell1); // reference cell
            assertFalse(element.addCellLink(cell2));
            assertTrue(element.isReferenceCell(cell1));
            assertFalse(element.isReferenceCell(cell2));
        }
    }

    @DisplayName("removeCellLink")
    @Nested
    public class RemoveCellLink {

        @Test
        @DisplayName("Removes cell link and returns true if it was the reference")
        void testRemoveCellLinkReference() {
            element.addCellLink(cell1);
            element.addCellLink(cell2);
            assertTrue(element.removeCellLink(cell1)); // returns true because cell1 is reference (index 0)
            assertFalse(element.isReferenceCell(cell1));
            // Now cell2 becomes reference (since it's the only remaining)
            assertTrue(element.isReferenceCell(cell2));
        }

        @Test
        @DisplayName("Returns false if removed cell is not reference")
        void testRemoveCellLinkNonReference() {
            element.addCellLink(cell1);
            element.addCellLink(cell2);
            assertFalse(element.removeCellLink(cell2)); // cell2 is not reference (index 1)
            assertTrue(element.isReferenceCell(cell1));
        }

        @Test
        @DisplayName("Returns false if cell not present")
        void testRemoveCellLinkNotFound() {
            element.addCellLink(cell1);
            assertFalse(element.removeCellLink(cell2)); // not present
            // reference cell unchanged
            assertTrue(element.isReferenceCell(cell1));
        }
    }

    @DisplayName("consumeCells")
    @Nested
    public class ConsumeCells {

        @Test
        @DisplayName("Returns a copy of cells and clears internal list")
        void testConsumeCells() {
            element.addCellLink(cell1);
            element.addCellLink(cell2);
            assertTrue(element.isReferenceCell(cell1));
            assertFalse(element.isReferenceCell(cell2));

            List<GridCell<GISPrimitive>> cells = element.consumeCells();
            assertEquals(2, cells.size());
            assertTrue(cells.contains(cell1));
            assertTrue(cells.contains(cell2));
            
            assertTrue(element.consumeCells().isEmpty());
            assertFalse(element.isReferenceCell(cell1));
            assertFalse(element.isReferenceCell(cell2));
        }

        @Test
        @DisplayName("Returns empty list if no cells")
        void testConsumeCellsEmpty() {
            List<GridCell<GISPrimitive>> cells = element.consumeCells();
            assertTrue(cells.isEmpty());
        }
    }

    @DisplayName("isReferenceCell")
    @Nested
    public class IsReferenceCell {

        @Test
        @DisplayName("returns true if cell is at index 0")
        void testIsReferenceCellTrue() {
            element.addCellLink(cell1);
            element.addCellLink(cell2);
            assertTrue(element.isReferenceCell(cell1));
            assertFalse(element.isReferenceCell(cell2));
        }

        @Test
        @DisplayName("returns false if no cells")
        void testIsReferenceCellFalseNoCells() {
            assertFalse(element.isReferenceCell(cell1));
        }

        @Test
        @DisplayName("returns false if cell not in list")
        void testIsReferenceCellFalseNotPresent() {
            element.addCellLink(cell1);
            assertFalse(element.isReferenceCell(cell2));
        }
    }

    @DisplayName("equals and hashCode")
    @Nested
    public class EqualsAndHashCode {

        @Test
        @DisplayName("equals returns true for same object")
        void testEqualsSame() {
            assertEquals(element, element);
        }

        @Test
        @DisplayName("equals returns true for different GridCellElement with same primitive")
        void testEqualsDifferentWrapperSamePrimitive() {
            GridCellElement<GISPrimitive> another = new GridCellElement<>(primitive1);
            assertEquals(element, another);
            assertEquals(element.hashCode(), another.hashCode());
        }

        @Test
        @DisplayName("equals returns true when comparing to the primitive itself")
        void testEqualsPrimitiveDirect() {
            assertEquals(element, primitive1);
        }

        @Test
        @DisplayName("equals returns false for different primitive")
        void testEqualsDifferentPrimitive() {
            GridCellElement<GISPrimitive> other = new GridCellElement<>(primitive2);
            assertNotEquals(element, other);
            assertNotEquals(element, primitive2);
        }

        @Test
        @DisplayName("equals returns false for null")
        void testEqualsNull() {
            assertNotEquals(element, null);
        }

        @Test
        @DisplayName("hashCode consistent with primitive's hashCode")
        void testHashCode() {
            assertEquals(primitive1.hashCode(), element.hashCode());
        }
    }

    @DisplayName("compareTo")
    @Nested
    public class CompareToTest {

        @Test
        @DisplayName("compareTo compares geoIds")
        void testCompareTo() {
        	when(primitive1.getGeoId()).thenReturn(new GeoId("id1"));
        	when(primitive2.getGeoId()).thenReturn(new GeoId("id2"));
            // "A" < "B"
        	var otherAPrimitive = mock(GISPrimitive.class);
            when(otherAPrimitive.getGeoId()).thenReturn(new GeoId("id1"));
        	var otherBPrimitive = mock(GISPrimitive.class);
            when(otherBPrimitive.getGeoId()).thenReturn(new GeoId("id3"));
            GridCellElement<GISPrimitive> otherA = new GridCellElement<>(otherAPrimitive);
            GridCellElement<GISPrimitive> otherB = new GridCellElement<>(otherBPrimitive);

            assertTrue(element.compareTo(otherB) < 0);
            assertTrue(element.compareTo(otherA) == 0);
            assertTrue(otherB.compareTo(element) > 0);
        }

        @Test
        @DisplayName("compareTo handles null by returning Integer.MAX_VALUE")
        void testCompareToNull() {
            assertEquals(Integer.MAX_VALUE, element.compareTo(null));
        }
    }
}

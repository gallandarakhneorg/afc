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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.arakhne.afc.gis.grid.AroundCellIterable;
import org.arakhne.afc.gis.grid.AroundCellIterator;
import org.arakhne.afc.gis.grid.Grid;
import org.arakhne.afc.gis.grid.GridCell;
import org.arakhne.afc.gis.grid.GridCellElement;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Unit test for Grid.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("Grid")
@SuppressWarnings("all")
public class GridTest extends AbstractGisTestCase {

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

		this.grid = new Grid<MapPoint>(10, 10, this.bounds);
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

	@DisplayName("getGridCellsAround")
	@Nested
	public class GetGridCellsAround {

		private Point2d point;
		private List<CellInfo> cellInfos;
		private List<Boolean> hasNextBefore;
		private boolean finalHasNext;

		private static class CellInfo {
			final int column;
			final int row;
			final int level;
			CellInfo(int column, int row, int level) {
				this.column = column;
				this.row = row;
				this.level = level;
			}
		}

		@BeforeEach
		public void setUp() {
			point = new Point2d(480, 230);
			cellInfos = new ArrayList<>();
			hasNextBefore = new ArrayList<>();

			AroundCellIterable<MapPoint> iterable = grid.getGridCellsAround(point, Double.POSITIVE_INFINITY);
			AroundCellIterator<MapPoint> iterator = iterable.aroundIterator();

			while (iterator.hasNext()) {
				hasNextBefore.add(true);
				GridCell<MapPoint> cell = iterator.next();
				cellInfos.add(new CellInfo(cell.column(), cell.row(), iterator.getLevel()));
			}
			finalHasNext = iterator.hasNext();
		}

		@DisplayName("#1")
		@Test
		public void testCell1() {
			assertTrue(hasNextBefore.get(0));
			CellInfo info = cellInfos.get(0);
			assertEquals(7, info.column);
			assertEquals(0, info.row);
			assertEquals(3, info.level);
		}

		@DisplayName("#2")
		@Test
		public void testCell2() {
			assertTrue(hasNextBefore.get(1));
			CellInfo info = cellInfos.get(1);
			assertEquals(8, info.column);
			assertEquals(0, info.row);
			assertEquals(4, info.level);
		}

		@DisplayName("#3")
		@Test
		public void testCell3() {
			assertTrue(hasNextBefore.get(2));
			CellInfo info = cellInfos.get(2);
			assertEquals(0, info.column);
			assertEquals(0, info.row);
			assertEquals(4, info.level);
		}

		@DisplayName("#4")
		@Test
		public void testCell4() {
			assertTrue(hasNextBefore.get(3));
			CellInfo info = cellInfos.get(3);
			assertEquals(0, info.column);
			assertEquals(7, info.row);
			assertEquals(5, info.level);
		}

		@DisplayName("#5")
		@Test
		public void testCell5() {
			assertTrue(hasNextBefore.get(4));
			CellInfo info = cellInfos.get(4);
			assertEquals(1, info.column);
			assertEquals(7, info.row);
			assertEquals(5, info.level);
		}

		@DisplayName("#6")
		@Test
		public void testCell6() {
			assertTrue(hasNextBefore.get(5));
			CellInfo info = cellInfos.get(5);
			assertEquals(4, info.column);
			assertEquals(7, info.row);
			assertEquals(5, info.level);
		}

		@DisplayName("#7")
		@Test
		public void testCell7() {
			assertTrue(hasNextBefore.get(6));
			CellInfo info = cellInfos.get(6);
			assertEquals(5, info.column);
			assertEquals(7, info.row);
			assertEquals(5, info.level);
		}

		@DisplayName("#8")
		@Test
		public void testCell8() {
			assertTrue(hasNextBefore.get(7));
			CellInfo info = cellInfos.get(7);
			assertEquals(0, info.column);
			assertEquals(8, info.row);
			assertEquals(6, info.level);
		}

		@DisplayName("#9")
		@Test
		public void testCell9() {
			assertTrue(hasNextBefore.get(8));
			CellInfo info = cellInfos.get(8);
			assertEquals(1, info.column);
			assertEquals(8, info.row);
			assertEquals(6, info.level);
		}

		@DisplayName("#10")
		@Test
		public void testCell10() {
			assertTrue(hasNextBefore.get(9));
			CellInfo info = cellInfos.get(9);
			assertEquals(4, info.column);
			assertEquals(8, info.row);
			assertEquals(6, info.level);
		}

		@DisplayName("#11")
		@Test
		public void testCell11() {
			assertTrue(hasNextBefore.get(10));
			CellInfo info = cellInfos.get(10);
			assertEquals(5, info.column);
			assertEquals(8, info.row);
			assertEquals(6, info.level);
		}

		@DisplayName("#12")
		@Test
		public void testCell12() {
			assertTrue(hasNextBefore.get(11));
			CellInfo info = cellInfos.get(11);
			assertEquals(9, info.column);
			assertEquals(9, info.row);
			assertEquals(7, info.level);
		}

		@DisplayName("#13")
		@Test
		public void testNoMoreCells() {
			assertFalse(finalHasNext);
		}
	}

	@DisplayName("getRowCount")
	@Nested
	public class GetRowCount {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(10, grid.getRowCount());
		}
	}

	@DisplayName("getColumnCount")
	@Nested
	public class GetColumnCount {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(10, grid.getColumnCount());
		}
	}

	@DisplayName("getBounds")
	@Nested
	public class GetBounds {

		@DisplayName("#1")
		@Test
		public void test_1() {
			Rectangle2d actual = grid.getBounds();
			assertEquals(bounds.getMinX(), actual.getMinX());
			assertEquals(bounds.getMinY(), actual.getMinY());
			assertEquals(bounds.getMaxX(), actual.getMaxX());
			assertEquals(bounds.getMaxY(), actual.getMaxY());
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@Test
		public void test_1() {
			grid.clear();
			assertTrue(grid.isEmpty());
			assertEquals(0, grid.getElementCount());
		}
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertFalse(grid.isEmpty());
			grid.clear();
			assertTrue(grid.isEmpty());
		}
	}

	@DisplayName("getElementCount")
	@Nested
	public class GetElementCount {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(7, grid.getElementCount());
		}
	}

	@DisplayName("getCellAt")
	@Nested
	public class GetCellAt {

	    @DisplayName("Negative row index throws IndexOutOfBoundsException")
	    @Test
	    public void negativeRowThrowsException() {
	        assertThrows(IndexOutOfBoundsException.class, () -> grid.getCellAt(-1, 0));
	    }

	    @DisplayName("Negative column index throws IndexOutOfBoundsException")
	    @Test
	    public void negativeColumnThrowsException() {
	        assertThrows(IndexOutOfBoundsException.class, () -> grid.getCellAt(0, -1));
	    }

	    @DisplayName("Both negative indices throw IndexOutOfBoundsException")
	    @Test
	    public void bothNegativeIndicesThrowException() {
	        assertThrows(IndexOutOfBoundsException.class, () -> grid.getCellAt(-1, -1));
	    }

	    @DisplayName("Row index equal to row count throws IndexOutOfBoundsException")
	    @Test
	    public void rowEqualToRowCountThrowsException() {
	        assertThrows(IndexOutOfBoundsException.class, () -> grid.getCellAt(10, 0));
	    }

	    @DisplayName("Column index equal to column count throws IndexOutOfBoundsException")
	    @Test
	    public void columnEqualToColumnCountThrowsException() {
	        assertThrows(IndexOutOfBoundsException.class, () -> grid.getCellAt(0, 10));
	    }

	    @DisplayName("Both indices equal to counts throw IndexOutOfBoundsException")
	    @Test
	    public void bothIndicesEqualToCountsThrowException() {
	        assertThrows(IndexOutOfBoundsException.class, () -> grid.getCellAt(10, 10));
	    }

	    @DisplayName("Cell at (0,0) is not null")
	    @Test
	    public void cellAt00NotNull() {
	        assertNotNull(grid.getCellAt(0, 0));
	        assertFalse(grid.getCellAt(0, 0).isEmpty());
	    }

	    @DisplayName("Cell at (0,1) is null")
	    @Test
	    public void cellAt01Null() {
	        assertNull(grid.getCellAt(0, 1));
	    }

	    @DisplayName("Cell at (0,2) is null")
	    @Test
	    public void cellAt02Null() {
	        assertNull(grid.getCellAt(0, 2));
	    }

	    @DisplayName("Cell at (0,3) is null")
	    @Test
	    public void cellAt03Null() {
	        assertNull(grid.getCellAt(0, 3));
	    }

	    @DisplayName("Cell at (0,4) is null")
	    @Test
	    public void cellAt04Null() {
	        assertNull(grid.getCellAt(0, 4));
	    }

	    @DisplayName("Cell at (0,5) is null")
	    @Test
	    public void cellAt05Null() {
	        assertNull(grid.getCellAt(0, 5));
	    }

	    @DisplayName("Cell at (0,6) is null")
	    @Test
	    public void cellAt06Null() {
	        assertNull(grid.getCellAt(0, 6));
	    }

	    @DisplayName("Cell at (0,7) is not null")
	    @Test
	    public void cellAt07NotNull() {
	        assertNotNull(grid.getCellAt(0, 7));
	        assertFalse(grid.getCellAt(0, 7).isEmpty());
	    }

	    @DisplayName("Cell at (0,8) is not null")
	    @Test
	    public void cellAt08NotNull() {
	        assertNotNull(grid.getCellAt(0, 8));
	        assertFalse(grid.getCellAt(0, 8).isEmpty());
	    }

	    @DisplayName("Cell at (0,9) is null")
	    @Test
	    public void cellAt09Null() {
	        assertNull(grid.getCellAt(0, 9));
	    }

	    @DisplayName("Cell at (1,0) is null")
	    @Test
	    public void cellAt10Null() {
	        assertNull(grid.getCellAt(1, 0));
	    }

	    @DisplayName("Cell at (1,1) is null")
	    @Test
	    public void cellAt11Null() {
	        assertNull(grid.getCellAt(1, 1));
	    }

	    @DisplayName("Cell at (1,2) is null")
	    @Test
	    public void cellAt12Null() {
	        assertNull(grid.getCellAt(1, 2));
	    }

	    @DisplayName("Cell at (1,3) is null")
	    @Test
	    public void cellAt13Null() {
	        assertNull(grid.getCellAt(1, 3));
	    }

	    @DisplayName("Cell at (1,4) is null")
	    @Test
	    public void cellAt14Null() {
	        assertNull(grid.getCellAt(1, 4));
	    }

	    @DisplayName("Cell at (1,5) is null")
	    @Test
	    public void cellAt15Null() {
	        assertNull(grid.getCellAt(1, 5));
	    }

	    @DisplayName("Cell at (1,6) is null")
	    @Test
	    public void cellAt16Null() {
	        assertNull(grid.getCellAt(1, 6));
	    }

	    @DisplayName("Cell at (1,7) is null")
	    @Test
	    public void cellAt17Null() {
	        assertNull(grid.getCellAt(1, 7));
	    }

	    @DisplayName("Cell at (1,8) is null")
	    @Test
	    public void cellAt18Null() {
	        assertNull(grid.getCellAt(1, 8));
	    }

	    @DisplayName("Cell at (1,9) is null")
	    @Test
	    public void cellAt19Null() {
	        assertNull(grid.getCellAt(1, 9));
	    }

	    @DisplayName("Cell at (2,0) is null")
	    @Test
	    public void cellAt20Null() {
	        assertNull(grid.getCellAt(2, 0));
	    }

	    @DisplayName("Cell at (2,1) is null")
	    @Test
	    public void cellAt21Null() {
	        assertNull(grid.getCellAt(2, 1));
	    }

	    @DisplayName("Cell at (2,2) is null")
	    @Test
	    public void cellAt22Null() {
	        assertNull(grid.getCellAt(2, 2));
	    }

	    @DisplayName("Cell at (2,3) is null")
	    @Test
	    public void cellAt23Null() {
	        assertNull(grid.getCellAt(2, 3));
	    }

	    @DisplayName("Cell at (2,4) is null")
	    @Test
	    public void cellAt24Null() {
	        assertNull(grid.getCellAt(2, 4));
	    }

	    @DisplayName("Cell at (2,5) is null")
	    @Test
	    public void cellAt25Null() {
	        assertNull(grid.getCellAt(2, 5));
	    }

	    @DisplayName("Cell at (2,6) is null")
	    @Test
	    public void cellAt26Null() {
	        assertNull(grid.getCellAt(2, 6));
	    }

	    @DisplayName("Cell at (2,7) is null")
	    @Test
	    public void cellAt27Null() {
	        assertNull(grid.getCellAt(2, 7));
	    }

	    @DisplayName("Cell at (2,8) is null")
	    @Test
	    public void cellAt28Null() {
	        assertNull(grid.getCellAt(2, 8));
	    }

	    @DisplayName("Cell at (2,9) is null")
	    @Test
	    public void cellAt29Null() {
	        assertNull(grid.getCellAt(2, 9));
	    }

	    @DisplayName("Cell at (3,0) is null")
	    @Test
	    public void cellAt30Null() {
	        assertNull(grid.getCellAt(3, 0));
	    }

	    @DisplayName("Cell at (3,1) is null")
	    @Test
	    public void cellAt31Null() {
	        assertNull(grid.getCellAt(3, 1));
	    }

	    @DisplayName("Cell at (3,2) is null")
	    @Test
	    public void cellAt32Null() {
	        assertNull(grid.getCellAt(3, 2));
	    }

	    @DisplayName("Cell at (3,3) is null")
	    @Test
	    public void cellAt33Null() {
	        assertNull(grid.getCellAt(3, 3));
	    }

	    @DisplayName("Cell at (3,4) is null")
	    @Test
	    public void cellAt34Null() {
	        assertNull(grid.getCellAt(3, 4));
	    }

	    @DisplayName("Cell at (3,5) is null")
	    @Test
	    public void cellAt35Null() {
	        assertNull(grid.getCellAt(3, 5));
	    }

	    @DisplayName("Cell at (3,6) is null")
	    @Test
	    public void cellAt36Null() {
	        assertNull(grid.getCellAt(3, 6));
	    }

	    @DisplayName("Cell at (3,7) is null")
	    @Test
	    public void cellAt37Null() {
	        assertNull(grid.getCellAt(3, 7));
	    }

	    @DisplayName("Cell at (3,8) is null")
	    @Test
	    public void cellAt38Null() {
	        assertNull(grid.getCellAt(3, 8));
	    }

	    @DisplayName("Cell at (3,9) is null")
	    @Test
	    public void cellAt39Null() {
	        assertNull(grid.getCellAt(3, 9));
	    }

	    @DisplayName("Cell at (4,0) is null")
	    @Test
	    public void cellAt40Null() {
	        assertNull(grid.getCellAt(4, 0));
	    }

	    @DisplayName("Cell at (4,1) is null")
	    @Test
	    public void cellAt41Null() {
	        assertNull(grid.getCellAt(4, 1));
	    }

	    @DisplayName("Cell at (4,2) is null")
	    @Test
	    public void cellAt42Null() {
	        assertNull(grid.getCellAt(4, 2));
	    }

	    @DisplayName("Cell at (4,3) is null")
	    @Test
	    public void cellAt43Null() {
	        assertNull(grid.getCellAt(4, 3));
	    }

	    @DisplayName("Cell at (4,4) is null")
	    @Test
	    public void cellAt44Null() {
	        assertNull(grid.getCellAt(4, 4));
	    }

	    @DisplayName("Cell at (4,5) is null")
	    @Test
	    public void cellAt45Null() {
	        assertNull(grid.getCellAt(4, 5));
	    }

	    @DisplayName("Cell at (4,6) is null")
	    @Test
	    public void cellAt46Null() {
	        assertNull(grid.getCellAt(4, 6));
	    }

	    @DisplayName("Cell at (4,7) is null")
	    @Test
	    public void cellAt47Null() {
	        assertNull(grid.getCellAt(4, 7));
	    }

	    @DisplayName("Cell at (4,8) is null")
	    @Test
	    public void cellAt48Null() {
	        assertNull(grid.getCellAt(4, 8));
	    }

	    @DisplayName("Cell at (4,9) is null")
	    @Test
	    public void cellAt49Null() {
	        assertNull(grid.getCellAt(4, 9));
	    }

	    @DisplayName("Cell at (5,0) is null")
	    @Test
	    public void cellAt50Null() {
	        assertNull(grid.getCellAt(5, 0));
	    }

	    @DisplayName("Cell at (5,1) is null")
	    @Test
	    public void cellAt51Null() {
	        assertNull(grid.getCellAt(5, 1));
	    }

	    @DisplayName("Cell at (5,2) is null")
	    @Test
	    public void cellAt52Null() {
	        assertNull(grid.getCellAt(5, 2));
	    }

	    @DisplayName("Cell at (5,3) is null")
	    @Test
	    public void cellAt53Null() {
	        assertNull(grid.getCellAt(5, 3));
	    }

	    @DisplayName("Cell at (5,4) is null")
	    @Test
	    public void cellAt54Null() {
	        assertNull(grid.getCellAt(5, 4));
	    }

	    @DisplayName("Cell at (5,5) is null")
	    @Test
	    public void cellAt55Null() {
	        assertNull(grid.getCellAt(5, 5));
	    }

	    @DisplayName("Cell at (5,6) is null")
	    @Test
	    public void cellAt56Null() {
	        assertNull(grid.getCellAt(5, 6));
	    }

	    @DisplayName("Cell at (5,7) is null")
	    @Test
	    public void cellAt57Null() {
	        assertNull(grid.getCellAt(5, 7));
	    }

	    @DisplayName("Cell at (5,8) is null")
	    @Test
	    public void cellAt58Null() {
	        assertNull(grid.getCellAt(5, 8));
	    }

	    @DisplayName("Cell at (5,9) is null")
	    @Test
	    public void cellAt59Null() {
	        assertNull(grid.getCellAt(5, 9));
	    }

	    @DisplayName("Cell at (6,0) is null")
	    @Test
	    public void cellAt60Null() {
	        assertNull(grid.getCellAt(6, 0));
	    }

	    @DisplayName("Cell at (6,1) is null")
	    @Test
	    public void cellAt61Null() {
	        assertNull(grid.getCellAt(6, 1));
	    }

	    @DisplayName("Cell at (6,2) is null")
	    @Test
	    public void cellAt62Null() {
	        assertNull(grid.getCellAt(6, 2));
	    }

	    @DisplayName("Cell at (6,3) is null")
	    @Test
	    public void cellAt63Null() {
	        assertNull(grid.getCellAt(6, 3));
	    }

	    @DisplayName("Cell at (6,4) is null")
	    @Test
	    public void cellAt64Null() {
	        assertNull(grid.getCellAt(6, 4));
	    }

	    @DisplayName("Cell at (6,5) is null")
	    @Test
	    public void cellAt65Null() {
	        assertNull(grid.getCellAt(6, 5));
	    }

	    @DisplayName("Cell at (6,6) is null")
	    @Test
	    public void cellAt66Null() {
	        assertNull(grid.getCellAt(6, 6));
	    }

	    @DisplayName("Cell at (6,7) is null")
	    @Test
	    public void cellAt67Null() {
	        assertNull(grid.getCellAt(6, 7));
	    }

	    @DisplayName("Cell at (6,8) is null")
	    @Test
	    public void cellAt68Null() {
	        assertNull(grid.getCellAt(6, 8));
	    }

	    @DisplayName("Cell at (6,9) is null")
	    @Test
	    public void cellAt69Null() {
	        assertNull(grid.getCellAt(6, 9));
	    }

	    @DisplayName("Cell at (7,0) is not null")
	    @Test
	    public void cellAt70NotNull() {
	        assertNotNull(grid.getCellAt(7, 0));
	        assertFalse(grid.getCellAt(7, 0).isEmpty());
	    }

	    @DisplayName("Cell at (7,1) is not null")
	    @Test
	    public void cellAt71NotNull() {
	        assertNotNull(grid.getCellAt(7, 1));
	        assertFalse(grid.getCellAt(7, 1).isEmpty());
	    }

	    @DisplayName("Cell at (7,2) is null")
	    @Test
	    public void cellAt72Null() {
	        assertNull(grid.getCellAt(7, 2));
	    }

	    @DisplayName("Cell at (7,3) is null")
	    @Test
	    public void cellAt73Null() {
	        assertNull(grid.getCellAt(7, 3));
	    }

	    @DisplayName("Cell at (7,4) is not null")
	    @Test
	    public void cellAt74NotNull() {
	        assertNotNull(grid.getCellAt(7, 4));
	        assertFalse(grid.getCellAt(7, 4).isEmpty());
	    }

	    @DisplayName("Cell at (7,5) is not null")
	    @Test
	    public void cellAt75NotNull() {
	        assertNotNull(grid.getCellAt(7, 5));
	        assertFalse(grid.getCellAt(7, 5).isEmpty());
	    }

	    @DisplayName("Cell at (7,6) is null")
	    @Test
	    public void cellAt76Null() {
	        assertNull(grid.getCellAt(7, 6));
	    }

	    @DisplayName("Cell at (7,7) is null")
	    @Test
	    public void cellAt77Null() {
	        assertNull(grid.getCellAt(7, 7));
	    }

	    @DisplayName("Cell at (7,8) is null")
	    @Test
	    public void cellAt78Null() {
	        assertNull(grid.getCellAt(7, 8));
	    }

	    @DisplayName("Cell at (7,9) is null")
	    @Test
	    public void cellAt79Null() {
	        assertNull(grid.getCellAt(7, 9));
	    }

	    @DisplayName("Cell at (8,0) is not null")
	    @Test
	    public void cellAt80NotNull() {
	        assertNotNull(grid.getCellAt(8, 0));
	        assertFalse(grid.getCellAt(8, 0).isEmpty());
	    }

	    @DisplayName("Cell at (8,1) is not null")
	    @Test
	    public void cellAt81NotNull() {
	        assertNotNull(grid.getCellAt(8, 1));
	        assertFalse(grid.getCellAt(8, 1).isEmpty());
	    }

	    @DisplayName("Cell at (8,2) is null")
	    @Test
	    public void cellAt82Null() {
	        assertNull(grid.getCellAt(8, 2));
	    }

	    @DisplayName("Cell at (8,3) is null")
	    @Test
	    public void cellAt83Null() {
	        assertNull(grid.getCellAt(8, 3));
	    }

	    @DisplayName("Cell at (8,4) is not null")
	    @Test
	    public void cellAt84NotNull() {
	        assertNotNull(grid.getCellAt(8, 4));
	        assertFalse(grid.getCellAt(8, 4).isEmpty());
	    }

	    @DisplayName("Cell at (8,5) is not null")
	    @Test
	    public void cellAt85NotNull() {
	        assertNotNull(grid.getCellAt(8, 5));
	        assertFalse(grid.getCellAt(8, 5).isEmpty());
	    }

	    @DisplayName("Cell at (8,6) is null")
	    @Test
	    public void cellAt86Null() {
	        assertNull(grid.getCellAt(8, 6));
	    }

	    @DisplayName("Cell at (8,7) is null")
	    @Test
	    public void cellAt87Null() {
	        assertNull(grid.getCellAt(8, 7));
	    }

	    @DisplayName("Cell at (8,8) is null")
	    @Test
	    public void cellAt88Null() {
	        assertNull(grid.getCellAt(8, 8));
	    }

	    @DisplayName("Cell at (8,9) is null")
	    @Test
	    public void cellAt89Null() {
	        assertNull(grid.getCellAt(8, 9));
	    }

	    @DisplayName("Cell at (9,0) is null")
	    @Test
	    public void cellAt90Null() {
	        assertNull(grid.getCellAt(9, 0));
	    }

	    @DisplayName("Cell at (9,1) is null")
	    @Test
	    public void cellAt91Null() {
	        assertNull(grid.getCellAt(9, 1));
	    }

	    @DisplayName("Cell at (9,2) is null")
	    @Test
	    public void cellAt92Null() {
	        assertNull(grid.getCellAt(9, 2));
	    }

	    @DisplayName("Cell at (9,3) is null")
	    @Test
	    public void cellAt93Null() {
	        assertNull(grid.getCellAt(9, 3));
	    }

	    @DisplayName("Cell at (9,4) is null")
	    @Test
	    public void cellAt94Null() {
	        assertNull(grid.getCellAt(9, 4));
	    }

	    @DisplayName("Cell at (9,5) is null")
	    @Test
	    public void cellAt95Null() {
	        assertNull(grid.getCellAt(9, 5));
	    }

	    @DisplayName("Cell at (9,6) is null")
	    @Test
	    public void cellAt96Null() {
	        assertNull(grid.getCellAt(9, 6));
	    }

	    @DisplayName("Cell at (9,7) is null")
	    @Test
	    public void cellAt97Null() {
	        assertNull(grid.getCellAt(9, 7));
	    }

	    @DisplayName("Cell at (9,8) is null")
	    @Test
	    public void cellAt98Null() {
	        assertNull(grid.getCellAt(9, 8));
	    }

	    @DisplayName("Cell at (9,9) is not null")
	    @Test
	    public void cellAt99NotNull() {
	        assertNotNull(grid.getCellAt(9, 9));
	        assertFalse(grid.getCellAt(9, 9).isEmpty());
	    }
	}

	@DisplayName("getCellBounds")
	@Nested
	public class GetCellBounds {

		@DisplayName("#1")
		@Test
		public void test_1() {
			Rectangle2d cellBounds = grid.getCellBounds(0, 0);
			assertEquals(0.0, cellBounds.getMinX());
			assertEquals(0.0, cellBounds.getMinY());
			assertEquals(100.0, cellBounds.getMaxX());
			assertEquals(100.0, cellBounds.getMaxY());
		}
	}

	@DisplayName("createCellAt")
	@Nested
	public class CreateCellAt {

		@DisplayName("#1")
		@Test
		public void test_1() {
			grid.createCellAt(5, 5);
			assertNotNull(grid.getCellAt(5, 5));
		}
	}

	@DisplayName("removeCellAt")
	@Nested
	public class RemoveCellAt {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var cell = grid.removeCellAt(0, 0);
			assertNotNull(cell);
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			grid.removeCellAt(0, 0);
			assertNull(grid.getCellAt(0, 0));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			grid.removeCellAt(0, 0);
			assertEquals(6, grid.getElementCount());
		}
	}

	@DisplayName("addElement")
	@Nested
	public class AddElement {

		private MapPoint newPoint;

		@BeforeEach
		public void setUp() {
			newPoint = new MapPoint(48, 49);
			assertTrue(grid.addElement(newPoint));
		}
		
		@DisplayName("Count of elements")
		@Test
		public void test_1() {
			assertEquals(8, grid.getElementCount());
		}

	    @DisplayName("Cell at (0,0) is not null")
	    @Test
	    public void cellAt00NotNull() {
	        assertNotNull(grid.getCellAt(0, 0));
	        assertFalse(grid.getCellAt(0, 0).isEmpty());
	    }

	    @DisplayName("Cell at (0,1) is null")
	    @Test
	    public void cellAt01Null() {
	        assertNull(grid.getCellAt(0, 1));
	    }

	    @DisplayName("Cell at (0,2) is null")
	    @Test
	    public void cellAt02Null() {
	        assertNull(grid.getCellAt(0, 2));
	    }

	    @DisplayName("Cell at (0,3) is null")
	    @Test
	    public void cellAt03Null() {
	        assertNull(grid.getCellAt(0, 3));
	    }

	    @DisplayName("Cell at (0,4) is null")
	    @Test
	    public void cellAt04Null() {
	        assertNull(grid.getCellAt(0, 4));
	    }

	    @DisplayName("Cell at (0,5) is null")
	    @Test
	    public void cellAt05Null() {
	        assertNull(grid.getCellAt(0, 5));
	    }

	    @DisplayName("Cell at (0,6) is null")
	    @Test
	    public void cellAt06Null() {
	        assertNull(grid.getCellAt(0, 6));
	    }

	    @DisplayName("Cell at (0,7) is not null")
	    @Test
	    public void cellAt07NotNull() {
	        assertNotNull(grid.getCellAt(0, 7));
	        assertFalse(grid.getCellAt(0, 7).isEmpty());
	    }

	    @DisplayName("Cell at (0,8) is not null")
	    @Test
	    public void cellAt08NotNull() {
	        assertNotNull(grid.getCellAt(0, 8));
	        assertFalse(grid.getCellAt(0, 8).isEmpty());
	    }

	    @DisplayName("Cell at (0,9) is null")
	    @Test
	    public void cellAt09Null() {
	        assertNull(grid.getCellAt(0, 9));
	    }

	    @DisplayName("Cell at (1,0) is null")
	    @Test
	    public void cellAt10Null() {
	        assertNull(grid.getCellAt(1, 0));
	    }

	    @DisplayName("Cell at (1,1) is null")
	    @Test
	    public void cellAt11Null() {
	        assertNull(grid.getCellAt(1, 1));
	    }

	    @DisplayName("Cell at (1,2) is null")
	    @Test
	    public void cellAt12Null() {
	        assertNull(grid.getCellAt(1, 2));
	    }

	    @DisplayName("Cell at (1,3) is null")
	    @Test
	    public void cellAt13Null() {
	        assertNull(grid.getCellAt(1, 3));
	    }

	    @DisplayName("Cell at (1,4) is null")
	    @Test
	    public void cellAt14Null() {
	        assertNull(grid.getCellAt(1, 4));
	    }

	    @DisplayName("Cell at (1,5) is null")
	    @Test
	    public void cellAt15Null() {
	        assertNull(grid.getCellAt(1, 5));
	    }

	    @DisplayName("Cell at (1,6) is null")
	    @Test
	    public void cellAt16Null() {
	        assertNull(grid.getCellAt(1, 6));
	    }

	    @DisplayName("Cell at (1,7) is null")
	    @Test
	    public void cellAt17Null() {
	        assertNull(grid.getCellAt(1, 7));
	    }

	    @DisplayName("Cell at (1,8) is null")
	    @Test
	    public void cellAt18Null() {
	        assertNull(grid.getCellAt(1, 8));
	    }

	    @DisplayName("Cell at (1,9) is null")
	    @Test
	    public void cellAt19Null() {
	        assertNull(grid.getCellAt(1, 9));
	    }

	    @DisplayName("Cell at (2,0) is null")
	    @Test
	    public void cellAt20Null() {
	        assertNull(grid.getCellAt(2, 0));
	    }

	    @DisplayName("Cell at (2,1) is null")
	    @Test
	    public void cellAt21Null() {
	        assertNull(grid.getCellAt(2, 1));
	    }

	    @DisplayName("Cell at (2,2) is null")
	    @Test
	    public void cellAt22Null() {
	        assertNull(grid.getCellAt(2, 2));
	    }

	    @DisplayName("Cell at (2,3) is null")
	    @Test
	    public void cellAt23Null() {
	        assertNull(grid.getCellAt(2, 3));
	    }

	    @DisplayName("Cell at (2,4) is null")
	    @Test
	    public void cellAt24Null() {
	        assertNull(grid.getCellAt(2, 4));
	    }

	    @DisplayName("Cell at (2,5) is null")
	    @Test
	    public void cellAt25Null() {
	        assertNull(grid.getCellAt(2, 5));
	    }

	    @DisplayName("Cell at (2,6) is null")
	    @Test
	    public void cellAt26Null() {
	        assertNull(grid.getCellAt(2, 6));
	    }

	    @DisplayName("Cell at (2,7) is null")
	    @Test
	    public void cellAt27Null() {
	        assertNull(grid.getCellAt(2, 7));
	    }

	    @DisplayName("Cell at (2,8) is null")
	    @Test
	    public void cellAt28Null() {
	        assertNull(grid.getCellAt(2, 8));
	    }

	    @DisplayName("Cell at (2,9) is null")
	    @Test
	    public void cellAt29Null() {
	        assertNull(grid.getCellAt(2, 9));
	    }

	    @DisplayName("Cell at (3,0) is null")
	    @Test
	    public void cellAt30Null() {
	        assertNull(grid.getCellAt(3, 0));
	    }

	    @DisplayName("Cell at (3,1) is null")
	    @Test
	    public void cellAt31Null() {
	        assertNull(grid.getCellAt(3, 1));
	    }

	    @DisplayName("Cell at (3,2) is null")
	    @Test
	    public void cellAt32Null() {
	        assertNull(grid.getCellAt(3, 2));
	    }

	    @DisplayName("Cell at (3,3) is null")
	    @Test
	    public void cellAt33Null() {
	        assertNull(grid.getCellAt(3, 3));
	    }

	    @DisplayName("Cell at (3,4) is null")
	    @Test
	    public void cellAt34Null() {
	        assertNull(grid.getCellAt(3, 4));
	    }

	    @DisplayName("Cell at (3,5) is null")
	    @Test
	    public void cellAt35Null() {
	        assertNull(grid.getCellAt(3, 5));
	    }

	    @DisplayName("Cell at (3,6) is null")
	    @Test
	    public void cellAt36Null() {
	        assertNull(grid.getCellAt(3, 6));
	    }

	    @DisplayName("Cell at (3,7) is null")
	    @Test
	    public void cellAt37Null() {
	        assertNull(grid.getCellAt(3, 7));
	    }

	    @DisplayName("Cell at (3,8) is null")
	    @Test
	    public void cellAt38Null() {
	        assertNull(grid.getCellAt(3, 8));
	    }

	    @DisplayName("Cell at (3,9) is null")
	    @Test
	    public void cellAt39Null() {
	        assertNull(grid.getCellAt(3, 9));
	    }

	    @DisplayName("Cell at (4,0) is null")
	    @Test
	    public void cellAt40Null() {
	        assertNull(grid.getCellAt(4, 0));
	    }

	    @DisplayName("Cell at (4,1) is null")
	    @Test
	    public void cellAt41Null() {
	        assertNull(grid.getCellAt(4, 1));
	    }

	    @DisplayName("Cell at (4,2) is null")
	    @Test
	    public void cellAt42Null() {
	        assertNull(grid.getCellAt(4, 2));
	    }

	    @DisplayName("Cell at (4,3) is null")
	    @Test
	    public void cellAt43Null() {
	        assertNull(grid.getCellAt(4, 3));
	    }

	    @DisplayName("Cell at (4,4) is null")
	    @Test
	    public void cellAt44Null() {
	        assertNull(grid.getCellAt(4, 4));
	    }

	    @DisplayName("Cell at (4,5) is null")
	    @Test
	    public void cellAt45Null() {
	        assertNull(grid.getCellAt(4, 5));
	    }

	    @DisplayName("Cell at (4,6) is null")
	    @Test
	    public void cellAt46Null() {
	        assertNull(grid.getCellAt(4, 6));
	    }

	    @DisplayName("Cell at (4,7) is null")
	    @Test
	    public void cellAt47Null() {
	        assertNull(grid.getCellAt(4, 7));
	    }

	    @DisplayName("Cell at (4,8) is null")
	    @Test
	    public void cellAt48Null() {
	        assertNull(grid.getCellAt(4, 8));
	    }

	    @DisplayName("Cell at (4,9) is null")
	    @Test
	    public void cellAt49Null() {
	        assertNull(grid.getCellAt(4, 9));
	    }

	    @DisplayName("Cell at (5,0) is null")
	    @Test
	    public void cellAt50Null() {
	        assertNull(grid.getCellAt(5, 0));
	    }

	    @DisplayName("Cell at (5,1) is null")
	    @Test
	    public void cellAt51Null() {
	        assertNull(grid.getCellAt(5, 1));
	    }

	    @DisplayName("Cell at (5,2) is null")
	    @Test
	    public void cellAt52Null() {
	        assertNull(grid.getCellAt(5, 2));
	    }

	    @DisplayName("Cell at (5,3) is null")
	    @Test
	    public void cellAt53Null() {
	        assertNull(grid.getCellAt(5, 3));
	    }

	    @DisplayName("Cell at (5,4) is null")
	    @Test
	    public void cellAt54Null() {
	        assertNull(grid.getCellAt(5, 4));
	    }

	    @DisplayName("Cell at (5,5) is null")
	    @Test
	    public void cellAt55Null() {
	        assertNull(grid.getCellAt(5, 5));
	    }

	    @DisplayName("Cell at (5,6) is null")
	    @Test
	    public void cellAt56Null() {
	        assertNull(grid.getCellAt(5, 6));
	    }

	    @DisplayName("Cell at (5,7) is null")
	    @Test
	    public void cellAt57Null() {
	        assertNull(grid.getCellAt(5, 7));
	    }

	    @DisplayName("Cell at (5,8) is null")
	    @Test
	    public void cellAt58Null() {
	        assertNull(grid.getCellAt(5, 8));
	    }

	    @DisplayName("Cell at (5,9) is null")
	    @Test
	    public void cellAt59Null() {
	        assertNull(grid.getCellAt(5, 9));
	    }

	    @DisplayName("Cell at (6,0) is null")
	    @Test
	    public void cellAt60Null() {
	        assertNull(grid.getCellAt(6, 0));
	    }

	    @DisplayName("Cell at (6,1) is null")
	    @Test
	    public void cellAt61Null() {
	        assertNull(grid.getCellAt(6, 1));
	    }

	    @DisplayName("Cell at (6,2) is null")
	    @Test
	    public void cellAt62Null() {
	        assertNull(grid.getCellAt(6, 2));
	    }

	    @DisplayName("Cell at (6,3) is null")
	    @Test
	    public void cellAt63Null() {
	        assertNull(grid.getCellAt(6, 3));
	    }

	    @DisplayName("Cell at (6,4) is null")
	    @Test
	    public void cellAt64Null() {
	        assertNull(grid.getCellAt(6, 4));
	    }

	    @DisplayName("Cell at (6,5) is null")
	    @Test
	    public void cellAt65Null() {
	        assertNull(grid.getCellAt(6, 5));
	    }

	    @DisplayName("Cell at (6,6) is null")
	    @Test
	    public void cellAt66Null() {
	        assertNull(grid.getCellAt(6, 6));
	    }

	    @DisplayName("Cell at (6,7) is null")
	    @Test
	    public void cellAt67Null() {
	        assertNull(grid.getCellAt(6, 7));
	    }

	    @DisplayName("Cell at (6,8) is null")
	    @Test
	    public void cellAt68Null() {
	        assertNull(grid.getCellAt(6, 8));
	    }

	    @DisplayName("Cell at (6,9) is null")
	    @Test
	    public void cellAt69Null() {
	        assertNull(grid.getCellAt(6, 9));
	    }

	    @DisplayName("Cell at (7,0) is not null")
	    @Test
	    public void cellAt70NotNull() {
	        assertNotNull(grid.getCellAt(7, 0));
	        assertFalse(grid.getCellAt(7, 0).isEmpty());
	    }

	    @DisplayName("Cell at (7,1) is not null")
	    @Test
	    public void cellAt71NotNull() {
	        assertNotNull(grid.getCellAt(7, 1));
	        assertFalse(grid.getCellAt(7, 1).isEmpty());
	    }

	    @DisplayName("Cell at (7,2) is null")
	    @Test
	    public void cellAt72Null() {
	        assertNull(grid.getCellAt(7, 2));
	    }

	    @DisplayName("Cell at (7,3) is null")
	    @Test
	    public void cellAt73Null() {
	        assertNull(grid.getCellAt(7, 3));
	    }

	    @DisplayName("Cell at (7,4) is not null")
	    @Test
	    public void cellAt74NotNull() {
	        assertNotNull(grid.getCellAt(7, 4));
	        assertFalse(grid.getCellAt(7, 4).isEmpty());
	    }

	    @DisplayName("Cell at (7,5) is not null")
	    @Test
	    public void cellAt75NotNull() {
	        assertNotNull(grid.getCellAt(7, 5));
	        assertFalse(grid.getCellAt(7, 5).isEmpty());
	    }

	    @DisplayName("Cell at (7,6) is null")
	    @Test
	    public void cellAt76Null() {
	        assertNull(grid.getCellAt(7, 6));
	    }

	    @DisplayName("Cell at (7,7) is null")
	    @Test
	    public void cellAt77Null() {
	        assertNull(grid.getCellAt(7, 7));
	    }

	    @DisplayName("Cell at (7,8) is null")
	    @Test
	    public void cellAt78Null() {
	        assertNull(grid.getCellAt(7, 8));
	    }

	    @DisplayName("Cell at (7,9) is null")
	    @Test
	    public void cellAt79Null() {
	        assertNull(grid.getCellAt(7, 9));
	    }

	    @DisplayName("Cell at (8,0) is not null")
	    @Test
	    public void cellAt80NotNull() {
	        assertNotNull(grid.getCellAt(8, 0));
	        assertFalse(grid.getCellAt(8, 0).isEmpty());
	    }

	    @DisplayName("Cell at (8,1) is not null")
	    @Test
	    public void cellAt81NotNull() {
	        assertNotNull(grid.getCellAt(8, 1));
	        assertFalse(grid.getCellAt(8, 1).isEmpty());
	    }

	    @DisplayName("Cell at (8,2) is null")
	    @Test
	    public void cellAt82Null() {
	        assertNull(grid.getCellAt(8, 2));
	    }

	    @DisplayName("Cell at (8,3) is null")
	    @Test
	    public void cellAt83Null() {
	        assertNull(grid.getCellAt(8, 3));
	    }

	    @DisplayName("Cell at (8,4) is not null")
	    @Test
	    public void cellAt84NotNull() {
	        assertNotNull(grid.getCellAt(8, 4));
	        assertFalse(grid.getCellAt(8, 4).isEmpty());
	    }

	    @DisplayName("Cell at (8,5) is not null")
	    @Test
	    public void cellAt85NotNull() {
	        assertNotNull(grid.getCellAt(8, 5));
	        assertFalse(grid.getCellAt(8, 5).isEmpty());
	    }

	    @DisplayName("Cell at (8,6) is null")
	    @Test
	    public void cellAt86Null() {
	        assertNull(grid.getCellAt(8, 6));
	    }

	    @DisplayName("Cell at (8,7) is null")
	    @Test
	    public void cellAt87Null() {
	        assertNull(grid.getCellAt(8, 7));
	    }

	    @DisplayName("Cell at (8,8) is null")
	    @Test
	    public void cellAt88Null() {
	        assertNull(grid.getCellAt(8, 8));
	    }

	    @DisplayName("Cell at (8,9) is null")
	    @Test
	    public void cellAt89Null() {
	        assertNull(grid.getCellAt(8, 9));
	    }

	    @DisplayName("Cell at (9,0) is null")
	    @Test
	    public void cellAt90Null() {
	        assertNull(grid.getCellAt(9, 0));
	    }

	    @DisplayName("Cell at (9,1) is null")
	    @Test
	    public void cellAt91Null() {
	        assertNull(grid.getCellAt(9, 1));
	    }

	    @DisplayName("Cell at (9,2) is null")
	    @Test
	    public void cellAt92Null() {
	        assertNull(grid.getCellAt(9, 2));
	    }

	    @DisplayName("Cell at (9,3) is null")
	    @Test
	    public void cellAt93Null() {
	        assertNull(grid.getCellAt(9, 3));
	    }

	    @DisplayName("Cell at (9,4) is null")
	    @Test
	    public void cellAt94Null() {
	        assertNull(grid.getCellAt(9, 4));
	    }

	    @DisplayName("Cell at (9,5) is null")
	    @Test
	    public void cellAt95Null() {
	        assertNull(grid.getCellAt(9, 5));
	    }

	    @DisplayName("Cell at (9,6) is null")
	    @Test
	    public void cellAt96Null() {
	        assertNull(grid.getCellAt(9, 6));
	    }

	    @DisplayName("Cell at (9,7) is null")
	    @Test
	    public void cellAt97Null() {
	        assertNull(grid.getCellAt(9, 7));
	    }

	    @DisplayName("Cell at (9,8) is null")
	    @Test
	    public void cellAt98Null() {
	        assertNull(grid.getCellAt(9, 8));
	    }

	    @DisplayName("Cell at (9,9) is not null")
	    @Test
	    public void cellAt99NotNull() {
	        assertNotNull(grid.getCellAt(9, 9));
	        assertFalse(grid.getCellAt(9, 9).isEmpty());
	    }
	}

	@DisplayName("removeElement")
	@Nested
	public class RemoveElement {

		@BeforeEach
		public void setUp() {
			assertTrue(grid.removeElement(new MapPoint(0, 0)));
		}
		
		@DisplayName("Count of elements")
		@Test
		public void test_1() {
			assertEquals(6, grid.getElementCount());
		}

	    @DisplayName("Cell at (0,0) is not null")
	    @Test
	    public void cellAt00NotNull() {
	        assertNull(grid.getCellAt(0, 0));
	    }

	    @DisplayName("Cell at (0,1) is null")
	    @Test
	    public void cellAt01Null() {
	        assertNull(grid.getCellAt(0, 1));
	    }

	    @DisplayName("Cell at (0,2) is null")
	    @Test
	    public void cellAt02Null() {
	        assertNull(grid.getCellAt(0, 2));
	    }

	    @DisplayName("Cell at (0,3) is null")
	    @Test
	    public void cellAt03Null() {
	        assertNull(grid.getCellAt(0, 3));
	    }

	    @DisplayName("Cell at (0,4) is null")
	    @Test
	    public void cellAt04Null() {
	        assertNull(grid.getCellAt(0, 4));
	    }

	    @DisplayName("Cell at (0,5) is null")
	    @Test
	    public void cellAt05Null() {
	        assertNull(grid.getCellAt(0, 5));
	    }

	    @DisplayName("Cell at (0,6) is null")
	    @Test
	    public void cellAt06Null() {
	        assertNull(grid.getCellAt(0, 6));
	    }

	    @DisplayName("Cell at (0,7) is not null")
	    @Test
	    public void cellAt07NotNull() {
	        assertNotNull(grid.getCellAt(0, 7));
	        assertFalse(grid.getCellAt(0, 7).isEmpty());
	    }

	    @DisplayName("Cell at (0,8) is not null")
	    @Test
	    public void cellAt08NotNull() {
	        assertNotNull(grid.getCellAt(0, 8));
	        assertFalse(grid.getCellAt(0, 8).isEmpty());
	    }

	    @DisplayName("Cell at (0,9) is null")
	    @Test
	    public void cellAt09Null() {
	        assertNull(grid.getCellAt(0, 9));
	    }

	    @DisplayName("Cell at (1,0) is null")
	    @Test
	    public void cellAt10Null() {
	        assertNull(grid.getCellAt(1, 0));
	    }

	    @DisplayName("Cell at (1,1) is null")
	    @Test
	    public void cellAt11Null() {
	        assertNull(grid.getCellAt(1, 1));
	    }

	    @DisplayName("Cell at (1,2) is null")
	    @Test
	    public void cellAt12Null() {
	        assertNull(grid.getCellAt(1, 2));
	    }

	    @DisplayName("Cell at (1,3) is null")
	    @Test
	    public void cellAt13Null() {
	        assertNull(grid.getCellAt(1, 3));
	    }

	    @DisplayName("Cell at (1,4) is null")
	    @Test
	    public void cellAt14Null() {
	        assertNull(grid.getCellAt(1, 4));
	    }

	    @DisplayName("Cell at (1,5) is null")
	    @Test
	    public void cellAt15Null() {
	        assertNull(grid.getCellAt(1, 5));
	    }

	    @DisplayName("Cell at (1,6) is null")
	    @Test
	    public void cellAt16Null() {
	        assertNull(grid.getCellAt(1, 6));
	    }

	    @DisplayName("Cell at (1,7) is null")
	    @Test
	    public void cellAt17Null() {
	        assertNull(grid.getCellAt(1, 7));
	    }

	    @DisplayName("Cell at (1,8) is null")
	    @Test
	    public void cellAt18Null() {
	        assertNull(grid.getCellAt(1, 8));
	    }

	    @DisplayName("Cell at (1,9) is null")
	    @Test
	    public void cellAt19Null() {
	        assertNull(grid.getCellAt(1, 9));
	    }

	    @DisplayName("Cell at (2,0) is null")
	    @Test
	    public void cellAt20Null() {
	        assertNull(grid.getCellAt(2, 0));
	    }

	    @DisplayName("Cell at (2,1) is null")
	    @Test
	    public void cellAt21Null() {
	        assertNull(grid.getCellAt(2, 1));
	    }

	    @DisplayName("Cell at (2,2) is null")
	    @Test
	    public void cellAt22Null() {
	        assertNull(grid.getCellAt(2, 2));
	    }

	    @DisplayName("Cell at (2,3) is null")
	    @Test
	    public void cellAt23Null() {
	        assertNull(grid.getCellAt(2, 3));
	    }

	    @DisplayName("Cell at (2,4) is null")
	    @Test
	    public void cellAt24Null() {
	        assertNull(grid.getCellAt(2, 4));
	    }

	    @DisplayName("Cell at (2,5) is null")
	    @Test
	    public void cellAt25Null() {
	        assertNull(grid.getCellAt(2, 5));
	    }

	    @DisplayName("Cell at (2,6) is null")
	    @Test
	    public void cellAt26Null() {
	        assertNull(grid.getCellAt(2, 6));
	    }

	    @DisplayName("Cell at (2,7) is null")
	    @Test
	    public void cellAt27Null() {
	        assertNull(grid.getCellAt(2, 7));
	    }

	    @DisplayName("Cell at (2,8) is null")
	    @Test
	    public void cellAt28Null() {
	        assertNull(grid.getCellAt(2, 8));
	    }

	    @DisplayName("Cell at (2,9) is null")
	    @Test
	    public void cellAt29Null() {
	        assertNull(grid.getCellAt(2, 9));
	    }

	    @DisplayName("Cell at (3,0) is null")
	    @Test
	    public void cellAt30Null() {
	        assertNull(grid.getCellAt(3, 0));
	    }

	    @DisplayName("Cell at (3,1) is null")
	    @Test
	    public void cellAt31Null() {
	        assertNull(grid.getCellAt(3, 1));
	    }

	    @DisplayName("Cell at (3,2) is null")
	    @Test
	    public void cellAt32Null() {
	        assertNull(grid.getCellAt(3, 2));
	    }

	    @DisplayName("Cell at (3,3) is null")
	    @Test
	    public void cellAt33Null() {
	        assertNull(grid.getCellAt(3, 3));
	    }

	    @DisplayName("Cell at (3,4) is null")
	    @Test
	    public void cellAt34Null() {
	        assertNull(grid.getCellAt(3, 4));
	    }

	    @DisplayName("Cell at (3,5) is null")
	    @Test
	    public void cellAt35Null() {
	        assertNull(grid.getCellAt(3, 5));
	    }

	    @DisplayName("Cell at (3,6) is null")
	    @Test
	    public void cellAt36Null() {
	        assertNull(grid.getCellAt(3, 6));
	    }

	    @DisplayName("Cell at (3,7) is null")
	    @Test
	    public void cellAt37Null() {
	        assertNull(grid.getCellAt(3, 7));
	    }

	    @DisplayName("Cell at (3,8) is null")
	    @Test
	    public void cellAt38Null() {
	        assertNull(grid.getCellAt(3, 8));
	    }

	    @DisplayName("Cell at (3,9) is null")
	    @Test
	    public void cellAt39Null() {
	        assertNull(grid.getCellAt(3, 9));
	    }

	    @DisplayName("Cell at (4,0) is null")
	    @Test
	    public void cellAt40Null() {
	        assertNull(grid.getCellAt(4, 0));
	    }

	    @DisplayName("Cell at (4,1) is null")
	    @Test
	    public void cellAt41Null() {
	        assertNull(grid.getCellAt(4, 1));
	    }

	    @DisplayName("Cell at (4,2) is null")
	    @Test
	    public void cellAt42Null() {
	        assertNull(grid.getCellAt(4, 2));
	    }

	    @DisplayName("Cell at (4,3) is null")
	    @Test
	    public void cellAt43Null() {
	        assertNull(grid.getCellAt(4, 3));
	    }

	    @DisplayName("Cell at (4,4) is null")
	    @Test
	    public void cellAt44Null() {
	        assertNull(grid.getCellAt(4, 4));
	    }

	    @DisplayName("Cell at (4,5) is null")
	    @Test
	    public void cellAt45Null() {
	        assertNull(grid.getCellAt(4, 5));
	    }

	    @DisplayName("Cell at (4,6) is null")
	    @Test
	    public void cellAt46Null() {
	        assertNull(grid.getCellAt(4, 6));
	    }

	    @DisplayName("Cell at (4,7) is null")
	    @Test
	    public void cellAt47Null() {
	        assertNull(grid.getCellAt(4, 7));
	    }

	    @DisplayName("Cell at (4,8) is null")
	    @Test
	    public void cellAt48Null() {
	        assertNull(grid.getCellAt(4, 8));
	    }

	    @DisplayName("Cell at (4,9) is null")
	    @Test
	    public void cellAt49Null() {
	        assertNull(grid.getCellAt(4, 9));
	    }

	    @DisplayName("Cell at (5,0) is null")
	    @Test
	    public void cellAt50Null() {
	        assertNull(grid.getCellAt(5, 0));
	    }

	    @DisplayName("Cell at (5,1) is null")
	    @Test
	    public void cellAt51Null() {
	        assertNull(grid.getCellAt(5, 1));
	    }

	    @DisplayName("Cell at (5,2) is null")
	    @Test
	    public void cellAt52Null() {
	        assertNull(grid.getCellAt(5, 2));
	    }

	    @DisplayName("Cell at (5,3) is null")
	    @Test
	    public void cellAt53Null() {
	        assertNull(grid.getCellAt(5, 3));
	    }

	    @DisplayName("Cell at (5,4) is null")
	    @Test
	    public void cellAt54Null() {
	        assertNull(grid.getCellAt(5, 4));
	    }

	    @DisplayName("Cell at (5,5) is null")
	    @Test
	    public void cellAt55Null() {
	        assertNull(grid.getCellAt(5, 5));
	    }

	    @DisplayName("Cell at (5,6) is null")
	    @Test
	    public void cellAt56Null() {
	        assertNull(grid.getCellAt(5, 6));
	    }

	    @DisplayName("Cell at (5,7) is null")
	    @Test
	    public void cellAt57Null() {
	        assertNull(grid.getCellAt(5, 7));
	    }

	    @DisplayName("Cell at (5,8) is null")
	    @Test
	    public void cellAt58Null() {
	        assertNull(grid.getCellAt(5, 8));
	    }

	    @DisplayName("Cell at (5,9) is null")
	    @Test
	    public void cellAt59Null() {
	        assertNull(grid.getCellAt(5, 9));
	    }

	    @DisplayName("Cell at (6,0) is null")
	    @Test
	    public void cellAt60Null() {
	        assertNull(grid.getCellAt(6, 0));
	    }

	    @DisplayName("Cell at (6,1) is null")
	    @Test
	    public void cellAt61Null() {
	        assertNull(grid.getCellAt(6, 1));
	    }

	    @DisplayName("Cell at (6,2) is null")
	    @Test
	    public void cellAt62Null() {
	        assertNull(grid.getCellAt(6, 2));
	    }

	    @DisplayName("Cell at (6,3) is null")
	    @Test
	    public void cellAt63Null() {
	        assertNull(grid.getCellAt(6, 3));
	    }

	    @DisplayName("Cell at (6,4) is null")
	    @Test
	    public void cellAt64Null() {
	        assertNull(grid.getCellAt(6, 4));
	    }

	    @DisplayName("Cell at (6,5) is null")
	    @Test
	    public void cellAt65Null() {
	        assertNull(grid.getCellAt(6, 5));
	    }

	    @DisplayName("Cell at (6,6) is null")
	    @Test
	    public void cellAt66Null() {
	        assertNull(grid.getCellAt(6, 6));
	    }

	    @DisplayName("Cell at (6,7) is null")
	    @Test
	    public void cellAt67Null() {
	        assertNull(grid.getCellAt(6, 7));
	    }

	    @DisplayName("Cell at (6,8) is null")
	    @Test
	    public void cellAt68Null() {
	        assertNull(grid.getCellAt(6, 8));
	    }

	    @DisplayName("Cell at (6,9) is null")
	    @Test
	    public void cellAt69Null() {
	        assertNull(grid.getCellAt(6, 9));
	    }

	    @DisplayName("Cell at (7,0) is not null")
	    @Test
	    public void cellAt70NotNull() {
	        assertNotNull(grid.getCellAt(7, 0));
	        assertFalse(grid.getCellAt(7, 0).isEmpty());
	    }

	    @DisplayName("Cell at (7,1) is not null")
	    @Test
	    public void cellAt71NotNull() {
	        assertNotNull(grid.getCellAt(7, 1));
	        assertFalse(grid.getCellAt(7, 1).isEmpty());
	    }

	    @DisplayName("Cell at (7,2) is null")
	    @Test
	    public void cellAt72Null() {
	        assertNull(grid.getCellAt(7, 2));
	    }

	    @DisplayName("Cell at (7,3) is null")
	    @Test
	    public void cellAt73Null() {
	        assertNull(grid.getCellAt(7, 3));
	    }

	    @DisplayName("Cell at (7,4) is not null")
	    @Test
	    public void cellAt74NotNull() {
	        assertNotNull(grid.getCellAt(7, 4));
	        assertFalse(grid.getCellAt(7, 4).isEmpty());
	    }

	    @DisplayName("Cell at (7,5) is not null")
	    @Test
	    public void cellAt75NotNull() {
	        assertNotNull(grid.getCellAt(7, 5));
	        assertFalse(grid.getCellAt(7, 5).isEmpty());
	    }

	    @DisplayName("Cell at (7,6) is null")
	    @Test
	    public void cellAt76Null() {
	        assertNull(grid.getCellAt(7, 6));
	    }

	    @DisplayName("Cell at (7,7) is null")
	    @Test
	    public void cellAt77Null() {
	        assertNull(grid.getCellAt(7, 7));
	    }

	    @DisplayName("Cell at (7,8) is null")
	    @Test
	    public void cellAt78Null() {
	        assertNull(grid.getCellAt(7, 8));
	    }

	    @DisplayName("Cell at (7,9) is null")
	    @Test
	    public void cellAt79Null() {
	        assertNull(grid.getCellAt(7, 9));
	    }

	    @DisplayName("Cell at (8,0) is not null")
	    @Test
	    public void cellAt80NotNull() {
	        assertNotNull(grid.getCellAt(8, 0));
	        assertFalse(grid.getCellAt(8, 0).isEmpty());
	    }

	    @DisplayName("Cell at (8,1) is not null")
	    @Test
	    public void cellAt81NotNull() {
	        assertNotNull(grid.getCellAt(8, 1));
	        assertFalse(grid.getCellAt(8, 1).isEmpty());
	    }

	    @DisplayName("Cell at (8,2) is null")
	    @Test
	    public void cellAt82Null() {
	        assertNull(grid.getCellAt(8, 2));
	    }

	    @DisplayName("Cell at (8,3) is null")
	    @Test
	    public void cellAt83Null() {
	        assertNull(grid.getCellAt(8, 3));
	    }

	    @DisplayName("Cell at (8,4) is not null")
	    @Test
	    public void cellAt84NotNull() {
	        assertNotNull(grid.getCellAt(8, 4));
	        assertFalse(grid.getCellAt(8, 4).isEmpty());
	    }

	    @DisplayName("Cell at (8,5) is not null")
	    @Test
	    public void cellAt85NotNull() {
	        assertNotNull(grid.getCellAt(8, 5));
	        assertFalse(grid.getCellAt(8, 5).isEmpty());
	    }

	    @DisplayName("Cell at (8,6) is null")
	    @Test
	    public void cellAt86Null() {
	        assertNull(grid.getCellAt(8, 6));
	    }

	    @DisplayName("Cell at (8,7) is null")
	    @Test
	    public void cellAt87Null() {
	        assertNull(grid.getCellAt(8, 7));
	    }

	    @DisplayName("Cell at (8,8) is null")
	    @Test
	    public void cellAt88Null() {
	        assertNull(grid.getCellAt(8, 8));
	    }

	    @DisplayName("Cell at (8,9) is null")
	    @Test
	    public void cellAt89Null() {
	        assertNull(grid.getCellAt(8, 9));
	    }

	    @DisplayName("Cell at (9,0) is null")
	    @Test
	    public void cellAt90Null() {
	        assertNull(grid.getCellAt(9, 0));
	    }

	    @DisplayName("Cell at (9,1) is null")
	    @Test
	    public void cellAt91Null() {
	        assertNull(grid.getCellAt(9, 1));
	    }

	    @DisplayName("Cell at (9,2) is null")
	    @Test
	    public void cellAt92Null() {
	        assertNull(grid.getCellAt(9, 2));
	    }

	    @DisplayName("Cell at (9,3) is null")
	    @Test
	    public void cellAt93Null() {
	        assertNull(grid.getCellAt(9, 3));
	    }

	    @DisplayName("Cell at (9,4) is null")
	    @Test
	    public void cellAt94Null() {
	        assertNull(grid.getCellAt(9, 4));
	    }

	    @DisplayName("Cell at (9,5) is null")
	    @Test
	    public void cellAt95Null() {
	        assertNull(grid.getCellAt(9, 5));
	    }

	    @DisplayName("Cell at (9,6) is null")
	    @Test
	    public void cellAt96Null() {
	        assertNull(grid.getCellAt(9, 6));
	    }

	    @DisplayName("Cell at (9,7) is null")
	    @Test
	    public void cellAt97Null() {
	        assertNull(grid.getCellAt(9, 7));
	    }

	    @DisplayName("Cell at (9,8) is null")
	    @Test
	    public void cellAt98Null() {
	        assertNull(grid.getCellAt(9, 8));
	    }

	    @DisplayName("Cell at (9,9) is not null")
	    @Test
	    public void cellAt99NotNull() {
	        assertNotNull(grid.getCellAt(9, 9));
	        assertFalse(grid.getCellAt(9, 9).isEmpty());
	    }
	}


	@DisplayName("getCellCount")
	@Nested
	public class GetCellCount {

		@DisplayName("#1")
		@Test
		public void test_1() {
			// Only a subset of the 100 possible cells are really created dependeing on the added elements
			assertEquals(12, grid.getCellCount());
		}
	}

	@DisplayName("getGridCells")
	@Nested
	public class GetGridCells {

		@DisplayName("#1")
		@Test
		public void test_1() {
			var cells = grid.getGridCells();
			var count = 0;
			for (var c : cells) {
				assertFalse(c.isEmpty());
				++count;
			}
			assertEquals(12, count);
		}
	}

	@DisplayName("getGridCellsOn")
	@Nested
	public class GetGridCellsOn {

		@DisplayName("(Rectangle2afp)")
		@Nested
		public class WithRectangle {

			@DisplayName("(0,0)-(200,200)")
			@Test
			public void test_1() {
				Rectangle2d rect = new Rectangle2d(0, 0, 200, 200);
				Iterable<GridCell<MapPoint>> cells = grid.getGridCellsOn(rect);
				var count = new AtomicInteger(0);
				cells.forEach((it) -> count.incrementAndGet());
				assertEquals(1, count.get());
			}

			@DisplayName("(200, 150)-(600,750)")
			@Test
			public void test_2() {
				Rectangle2d rect = new Rectangle2d(200, 150, 400, 600);
				Iterable<GridCell<MapPoint>> cells = grid.getGridCellsOn(rect);
				var count = new AtomicInteger(0);
				cells.forEach((it) -> count.incrementAndGet());
				assertEquals(2, count.get());
			}

			@DisplayName("(200, 0)-(300,50)")
			@Test
			public void test_3() {
				Rectangle2d rect = new Rectangle2d(200, 0, 400, 50);
				Iterable<GridCell<MapPoint>> cells = grid.getGridCellsOn(rect);
				var count = new AtomicInteger(0);
				cells.forEach((it) -> count.incrementAndGet());
				assertEquals(0, count.get());
			}
		}

		@DisplayName("(Rectangle2afp,false)")
		@Nested
		public class WithRectangleFalse {

			@DisplayName("(0,0)-(200,200)")
			@Test
			public void test_1() {
				Rectangle2d rect = new Rectangle2d(0, 0, 200, 200);
				Iterable<GridCell<MapPoint>> cells = grid.getGridCellsOn(rect, false);
				var count = new AtomicInteger(0);
				cells.forEach((it) -> count.incrementAndGet());
				assertEquals(1, count.get());
			}

			@DisplayName("(200, 150)-(600,750)")
			@Test
			public void test_2() {
				Rectangle2d rect = new Rectangle2d(200, 150, 400, 600);
				Iterable<GridCell<MapPoint>> cells = grid.getGridCellsOn(rect, false);
				var count = new AtomicInteger(0);
				cells.forEach((it) -> count.incrementAndGet());
				assertEquals(2, count.get());
			}

			@DisplayName("(200, 0)-(300,50)")
			@Test
			public void test_3() {
				Rectangle2d rect = new Rectangle2d(200, 0, 400, 50);
				Iterable<GridCell<MapPoint>> cells = grid.getGridCellsOn(rect, false);
				var count = new AtomicInteger(0);
				cells.forEach((it) -> count.incrementAndGet());
				assertEquals(0, count.get());
			}
		}

		@DisplayName("(Rectangle2afp,true)")
		@Nested
		public class WithRectangleTrue {

			@DisplayName("(0,0)-(200,200)")
			@Test
			public void test_1() {
				Rectangle2d rect = new Rectangle2d(0, 0, 200, 200);
				Iterable<GridCell<MapPoint>> cells = grid.getGridCellsOn(rect, true);
				var count = new AtomicInteger(0);
				cells.forEach((it) -> count.incrementAndGet());
				assertEquals(9, count.get());
			}

			@DisplayName("(200, 150)-(600,750)")
			@Test
			public void test_2() {
				Rectangle2d rect = new Rectangle2d(200, 150, 400, 600);
				Iterable<GridCell<MapPoint>> cells = grid.getGridCellsOn(rect, true);
				var count = new AtomicInteger(0);
				cells.forEach((it) -> count.incrementAndGet());
				assertEquals(35, count.get());
			}

			@DisplayName("(200, 0)-(300,50)")
			@Test
			public void test_3() {
				Rectangle2d rect = new Rectangle2d(200, 0, 400, 50);
				Iterable<GridCell<MapPoint>> cells = grid.getGridCellsOn(rect, true);
				var count = new AtomicInteger(0);
				cells.forEach((it) -> count.incrementAndGet());
				assertEquals(5, count.get());
			}
		}
	}

	@DisplayName("getCellWidth")
	@Nested
	public class GetCellWidth {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(100.0, grid.getCellWidth());
		}
	}

	@DisplayName("getCellHeight")
	@Nested
	public class GetCellHeight {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(100.0, grid.getCellHeight());
		}
	}

	@DisplayName("getColumnFor")
	@Nested
	public class GetColumnFor {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(5, grid.getColumnFor(500));
		}
	}

	@DisplayName("getRowFor")
	@Nested
	public class GetRowFor {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(8, grid.getRowFor(800));
		}
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

		@DisplayName("()")
		@Nested
		public class WithNoArgument {

			@DisplayName("#1")
			@Test
			public void test_1() {
				Iterator<MapPoint> it = grid.iterator();
				List<MapPoint> elements = new ArrayList<>();
				while (it.hasNext()) {
					elements.add(it.next());
				}
				assertEquals(7, elements.size());
				assertTrue(elements.containsAll(reference));
			}
		}

		@DisplayName("(Rectangle2afp)")
		@Nested
		public class WithRectangle {

			@DisplayName("#1")
			@Test
			public void test_1() {
				Rectangle2d rect = new Rectangle2d(0, 0, 100, 100);
				Iterator<MapPoint> it = grid.iterator(rect);
				List<MapPoint> elements = new ArrayList<>();
				while (it.hasNext()) {
					elements.add(it.next());
				}
				assertEquals(1, elements.size());
				assertTrue(elements.contains(new MapPoint(0,0)));
			}
		}

		@DisplayName("(Rectangle2afp,int)")
		@Nested
		public class WithRectangleInt {

			@DisplayName("#1")
			@Test
			public void test_1() {
				Rectangle2d rect = new Rectangle2d(0, 0, 1000, 1000);
				Iterator<MapPoint> it = grid.iterator(rect, 3);
				int count = 0;
				while (it.hasNext()) {
					it.next();
					count++;
				}
				assertEquals(3, count);
			}
		}
	}

	@DisplayName("indexOf")
	@Nested
	public class IndexOf {

		@DisplayName("#1")
		@Test
		public void test_1() {
			MapPoint p = new MapPoint(0, 0);
			int idx = grid.indexOf(p);
			assertTrue(idx >= 0);
			assertEquals(p, grid.getElementAt(idx));
		}
	}


	@DisplayName("getElementAt")
	@Nested
	public class GetElementAt {

		@DisplayName("#1")
		@Test
		public void test_1() {
			MapPoint p = new MapPoint(0, 0);
			int idx = grid.indexOf(p);
			assertEquals(p, grid.getElementAt(idx));
		}
	}
}

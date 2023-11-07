/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.coordinate.MapMetricProjection;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPolygon;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.io.dbase.DBaseFileReader;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ESRIFileUtil;
import org.arakhne.afc.io.shape.SeekOperationDisabledException;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeFileIndexReader;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class GISShapeFileReaderTest {

	static final URL shpUrl = Resources.getResource(GISShapeFileReaderTest.class, "test.shp"); //$NON-NLS-1$

	private static final URL dbfUrl = Resources.getResource(GISShapeFileReaderTest.class, "test.dbf"); //$NON-NLS-1$

	private static final URL shxUrl = Resources.getResource(GISShapeFileReaderTest.class, "test.shx"); //$NON-NLS-1$

	static final double[][] ALL_XS = new double[][] {
		new double[] { 939468.7, 939470.4, 939492.1, 939491.7, 939468.7,  },
		new double[] { 938446.2, 938453.9, 938458.5, 938464.1, 938454.5, 938450.9, 938447.5, 938446.2,  },
		new double[] { 939489.9, 939519.0, 939520.4, 939492.5, 939489.9,  },
		new double[] { 938552.0, 938558.9, 938562.3, 938570.8, 938564.4, 938552.0,  },
		new double[] { 939880.9, 939886.0, 939883.2, 939924.2, 939930.2, 939932.3, 939928.6, 939890.5, 939889.4, 939883.0, 939880.9,  },
		new double[] { 940411.4, 940427.0, 940438.5, 940422.1, 940411.4,  },
		new double[] { 938920.7, 938929.8, 938932.6, 938923.3, 938920.7,  },
		new double[] { 938626.1, 938643.5, 938679.2, 938650.5, 938626.1,  },
		new double[] { 938220.4, 938262.9, 938269.5, 938227.9, 938220.4,  },
		new double[] { 940422.4, 940439.0, 940440.4, 940424.0, 940422.4,  },
		new double[] { 937971.8, 937972.1, 937979.3, 937996.2, 938005.6, 938013.8, 938015.3, 938014.8, 938012.0, 938006.0, 937999.3, 937983.9, 937974.6, 937971.8,  },
		new double[] { 941060.9, 941065.4, 941061.6, 941083.5, 941085.9, 941088.4, 941091.3, 941092.2, 941091.6, 941093.9, 941073.2, 941067.9, 941065.8, 941060.9,  },
		new double[] { 939513.6, 939542.4, 939543.5, 939545.2, 939545.5, 939545.1, 939544.1, 939516.2, 939513.6,  },
		new double[] { 938838.9, 938840.8, 938844.9, 938843.8, 938845.7, 938852.9, 938852.0, 938912.5, 938918.4, 938858.8, 938858.1, 938851.0, 938847.9, 938846.6, 938841.8, 938838.9,  },
		new double[] { 939229.2, 939237.7, 939240.3, 939241.1, 939239.2, 939235.7, 939232.3, 939231.1, 939229.2,  },
		new double[] { 939470.4, 939473.9, 939477.9, 939476.9, 939491.5, 939492.5, 939520.7, 939523.9, 939494.5, 939494.9, 939478.6, 939477.5, 939472.2, 939470.4,  },
		new double[] { 940018.3, 940040.8, 940050.4, 940057.5, 940060.8, 940063.0, 940062.1, 940054.1, 940054.7, 940037.9, 940036.3, 940031.8, 940029.5, 940034.9, 940025.2, 940021.2, 940018.3,  },
		new double[] { 939986.0, 939989.2, 939996.2, 940017.5, 940021.6, 940021.6, 940033.9, 940034.0, 940036.6, 940037.0, 940039.8, 940039.6, 940035.7, 940036.0, 940019.9, 939989.8, 939989.8, 939986.5, 939986.0,  },
		new double[] { 938961.6, 938963.9, 938966.8, 938976.1, 938974.9, 939012.3, 939010.3, 939003.6, 939002.7, 938997.9, 938973.2, 938971.7, 938961.6,  },
		new double[] { 936456.7, 936460.4, 936460.7, 936484.5, 936491.5, 936491.8, 936486.8, 936486.7, 936460.7, 936460.1, 936456.9, 936456.7,  },
		new double[] { 937621.6, 937622.2, 937623.3, 937622.9, 937632.7, 937632.9, 937633.6, 937637.9, 937642.2, 937644.6, 937658.2, 937667.6, 937650.5, 937650.2, 937648.3, 937644.5, 937638.7, 937630.0, 937627.5, 937624.3, 937621.6,  },
	};

	static final double[][] ALL_YS = new double[][] {
		new double[] { 2302926.2, 2302941.6, 2302939.3, 2302924.4, 2302926.2,  },
		new double[] { 2302913.0, 2302914.5, 2302915.4, 2302893.7, 2302891.9, 2302906.7, 2302906.1, 2302913.0,  },
		new double[] { 2303088.7, 2303092.1, 2303078.8, 2303074.0, 2303088.7,  },
		new double[] { 2300659.3, 2300673.2, 2300671.7, 2300667.6, 2300653.7, 2300659.3,  },
		new double[] { 2304413.6, 2304415.7, 2304419.4, 2304435.6, 2304433.3, 2304426.5, 2304421.3, 2304405.6, 2304410.1, 2304407.8, 2304413.6,  },
		new double[] { 2302485.0, 2302493.0, 2302472.3, 2302463.6, 2302485.0,  },
		new double[] { 2304289.6, 2304292.1, 2304281.2, 2304278.9, 2304289.6,  },
		new double[] { 2302212.8, 2302234.1, 2302220.4, 2302196.2, 2302212.8,  },
		new double[] { 2306120.7, 2306138.6, 2306121.4, 2306103.1, 2306120.7,  },
		new double[] { 2308843.3, 2308847.4, 2308839.7, 2308836.1, 2308843.3,  },
		new double[] { 2303059.4, 2303063.6, 2303072.9, 2303085.0, 2303079.6, 2303072.8, 2303069.9, 2303067.0, 2303061.3, 2303050.6, 2303050.1, 2303051.8, 2303054.4, 2303059.4,  },
		new double[] { 2305895.7, 2305900.5, 2305903.7, 2305923.1, 2305919.4, 2305919.5, 2305917.8, 2305915.9, 2305913.8, 2305910.3, 2305890.6, 2305893.1, 2305891.6, 2305895.7,  },
		new double[] { 2303763.9, 2303771.9, 2303771.1, 2303769.1, 2303765.8, 2303763.2, 2303761.8, 2303753.6, 2303763.9,  },
		new double[] { 2303772.1, 2303775.8, 2303776.6, 2303783.1, 2303786.1, 2303787.2, 2303791.0, 2303801.6, 2303773.4, 2303760.2, 2303763.9, 2303762.1, 2303763.2, 2303769.1, 2303768.2, 2303772.1,  },
		new double[] { 2303624.0, 2303625.6, 2303615.9, 2303610.9, 2303608.4, 2303607.8, 2303609.5, 2303615.2, 2303624.0,  },
		new double[] { 2302400.4, 2302404.8, 2302405.9, 2302411.2, 2302413.0, 2302408.8, 2302412.4, 2302391.0, 2302388.5, 2302384.0, 2302382.1, 2302388.1, 2302390.1, 2302400.4,  },
		new double[] { 2300763.5, 2300771.4, 2300744.6, 2300747.4, 2300746.5, 2300741.5, 2300737.8, 2300735.1, 2300732.7, 2300725.9, 2300730.3, 2300728.9, 2300734.6, 2300735.7, 2300758.5, 2300756.4, 2300763.5,  },
		new double[] { 2303029.0, 2303037.3, 2303037.0, 2303037.2, 2303037.3, 2303039.1, 2303039.3, 2303037.9, 2303037.8, 2303028.7, 2303026.5, 2303017.9, 2303015.2, 2303005.9, 2303006.0, 2303006.6, 2303016.3, 2303017.0, 2303029.0,  },
		new double[] { 2301383.6, 2301395.1, 2301408.9, 2301406.4, 2301403.8, 2301395.9, 2301382.9, 2301384.5, 2301379.7, 2301380.9, 2301386.2, 2301381.9, 2301383.6,  },
		new double[] { 2302067.6, 2302067.9, 2302070.8, 2302072.1, 2302071.7, 2302065.2, 2302064.8, 2302063.3, 2302058.5, 2302063.3, 2302063.2, 2302067.6,  },
		new double[] { 2300706.3, 2300712.2, 2300714.0, 2300717.6, 2300722.5, 2300724.8, 2300728.2, 2300729.8, 2300729.6, 2300727.8, 2300735.0, 2300718.5, 2300710.6, 2300708.4, 2300706.4, 2300705.4, 2300705.8, 2300700.2, 2300706.6, 2300705.7, 2300706.3,  },

	};

	@Nested
	@DisplayName("-DBF-SHX")
	public class WithoutDbfWithoutShx extends AbstractTestCase {

		private GISShapeFileReader reader;

		@BeforeEach
		public void setUp() throws Exception {
			this.reader = new GISShapeFileReader(shpUrl);
		}

		@AfterEach
		public void tearDown() throws Exception {
			this.reader.close();
			this.reader = null;
		}

		@Test
		public void getMapElementType() {
			assertEquals(MapPolygon.class, this.reader.getMapElementType());
		}

		@Test
		public void getMapMetricProjection() {
			assertEquals(MapMetricProjection.FRANCE_LAMBERT_2_EXTENDED, this.reader.getMapMetricProjection());
		}

		@Test
		public void setMapMetricProjection() {
			this.reader.setMapMetricProjection(MapMetricProjection.FRANCE_LAMBERT_93);
			assertEquals(MapMetricProjection.FRANCE_LAMBERT_93, this.reader.getMapMetricProjection());
		}

		@Test
		public void getFileSize() throws Exception {
			assertEquals(4764, this.reader.getFileSize());
		}

		@Test
		public void getShapeElementType() {
			assertEquals(ShapeElementType.POLYGON, this.reader.getShapeElementType());
		}

		@Test
		public void getFileReadingPosition() throws Exception {
			assertEquals(ESRIFileUtil.HEADER_BYTES, this.reader.getFileReadingPosition());
			this.reader.read();
			assertEquals(236, this.reader.getFileReadingPosition());
		}

		@Test
		public void getBoundsFromHeader() {
			ESRIBounds bounds = this.reader.getBoundsFromHeader();
			assertEpsilonEquals(936456.7, bounds.getMinX());
			assertEpsilonEquals(2300653.7, bounds.getMinY());
			assertEpsilonEquals(941093.9, bounds.getMaxX());
			assertEpsilonEquals(2308847.4, bounds.getMaxY());
		}

		@Test
		public void read() throws Exception {
			MapElement element = this.reader.read();
			assertTrue(element instanceof MapPolygon);
			MapPolygon poly = (MapPolygon) element;
			Iterator<Point2d> iterator = poly.pointIterator();
			Point2d p;
			for (int i = 0; i < ALL_XS[0].length; ++i) {
				p = iterator.next();
				assertEpsilonEquals(ALL_XS[0][i], p.getX());
				assertEpsilonEquals(ALL_YS[0][i], p.getY());
			}
			assertFalse(iterator.hasNext());
		}

		@Test
		public void iterator() {
			Point2d p;
			Iterator<MapElement> iterator = this.reader.iterator();
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = (MapPolygon) iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorBoolean() {
			Point2d p;
			Iterator<MapElement> iterator = this.reader.iterator(false);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = (MapPolygon) iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClass_polygon() {
			Point2d p;
			Iterator<MapPolygon> iterator = this.reader.iterator(MapPolygon.class);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClass_polyline() {
			Iterator<MapPolyline> iterator = this.reader.iterator(MapPolyline.class);
			assertFalse(iterator.hasNext());
		}

		@Test
		public void iteratorClassBoolean_false() {
			Point2d p;
			Iterator<MapPolygon> iterator = this.reader.iterator(MapPolygon.class, false);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClassBoolean_polyline() {
			Iterator<MapPolyline> iterator = this.reader.iterator(MapPolyline.class, false);
			assertFalse(iterator.hasNext());
			
		}

		@Test
		public void seek() throws Exception {
			assertThrows(SeekOperationDisabledException.class, () -> this.reader.seek(4));
		}
		
	}

	@Nested
	@DisplayName("+DBF-SHX")
	public class WithDbfWithoutShx extends AbstractTestCase {

		private DBaseFileReader dbaseReader;

		private GISShapeFileReader reader;

		@BeforeEach
		public void setUp() throws Exception {
			this.dbaseReader = new DBaseFileReader(dbfUrl);
			this.reader = new GISShapeFileReader(shpUrl, this.dbaseReader);
		}

		@AfterEach
		public void tearDown() throws Exception {
			this.dbaseReader.close();
			this.dbaseReader = null;
			this.reader.close();
			this.reader = null;
		}

		@Test
		public void getMapElementType() {
			assertEquals(MapPolygon.class, this.reader.getMapElementType());
		}

		@Test
		public void getMapMetricProjection() {
			assertEquals(MapMetricProjection.FRANCE_LAMBERT_2_EXTENDED, this.reader.getMapMetricProjection());
		}

		@Test
		public void setMapMetricProjection() {
			this.reader.setMapMetricProjection(MapMetricProjection.FRANCE_LAMBERT_93);
			assertEquals(MapMetricProjection.FRANCE_LAMBERT_93, this.reader.getMapMetricProjection());
		}

		@Test
		public void getFileSize() throws Exception {
			assertEquals(4764, this.reader.getFileSize());
		}

		@Test
		public void getShapeElementType() {
			assertEquals(ShapeElementType.POLYGON, this.reader.getShapeElementType());
		}

		@Test
		public void getFileReadingPosition() throws Exception {
			assertEquals(ESRIFileUtil.HEADER_BYTES, this.reader.getFileReadingPosition());
			this.reader.read();
			assertEquals(236, this.reader.getFileReadingPosition());
		}

		@Test
		public void getBoundsFromHeader() {
			ESRIBounds bounds = this.reader.getBoundsFromHeader();
			assertEpsilonEquals(936456.7, bounds.getMinX());
			assertEpsilonEquals(2300653.7, bounds.getMinY());
			assertEpsilonEquals(941093.9, bounds.getMaxX());
			assertEpsilonEquals(2308847.4, bounds.getMaxY());
		}

		@Test
		public void read() throws Exception {
			MapElement element = this.reader.read();
			assertTrue(element instanceof MapPolygon);
			MapPolygon poly = (MapPolygon) element;
			Iterator<Point2d> iterator = poly.pointIterator();
			Point2d p;
			for (int i = 0; i < ALL_XS[0].length; ++i) {
				p = iterator.next();
				assertEpsilonEquals(ALL_XS[0][i], p.getX());
				assertEpsilonEquals(ALL_YS[0][i], p.getY());
			}
			assertFalse(iterator.hasNext());
		}

		@Test
		public void iterator() {
			Point2d p;
			Iterator<MapElement> iterator = this.reader.iterator();
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = (MapPolygon) iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorBoolean() {
			Point2d p;
			Iterator<MapElement> iterator = this.reader.iterator(false);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = (MapPolygon) iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClass_polygon() {
			Point2d p;
			Iterator<MapPolygon> iterator = this.reader.iterator(MapPolygon.class);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClass_polyline() {
			Iterator<MapPolyline> iterator = this.reader.iterator(MapPolyline.class);
			assertFalse(iterator.hasNext());
		}

		@Test
		public void iteratorClassBoolean_false() {
			Point2d p;
			Iterator<MapPolygon> iterator = this.reader.iterator(MapPolygon.class, false);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClassBoolean_polyline() {
			Iterator<MapPolyline> iterator = this.reader.iterator(MapPolyline.class, false);
			assertFalse(iterator.hasNext());
			
		}

		@Test
		public void seek() throws Exception {
			assertThrows(SeekOperationDisabledException.class, () -> this.reader.seek(4));
		}

	}

	@Nested
	@DisplayName("-DBF+SHX")
	public class WithoutDbfWithShx extends AbstractTestCase {

		private ShapeFileIndexReader shxReader;

		private GISShapeFileReader reader;

		@BeforeEach
		public void setUp() throws Exception {
			this.shxReader = new ShapeFileIndexReader(shxUrl);
			this.reader = new GISShapeFileReader(shpUrl, this.shxReader);
		}

		@AfterEach
		public void tearDown() throws Exception {
			this.shxReader.close();
			this.shxReader = null;
			this.reader.close();
			this.reader = null;
		}

		@Test
		public void getMapElementType() {
			assertEquals(MapPolygon.class, this.reader.getMapElementType());
		}

		@Test
		public void getMapMetricProjection() {
			assertEquals(MapMetricProjection.FRANCE_LAMBERT_2_EXTENDED, this.reader.getMapMetricProjection());
		}

		@Test
		public void setMapMetricProjection() {
			this.reader.setMapMetricProjection(MapMetricProjection.FRANCE_LAMBERT_93);
			assertEquals(MapMetricProjection.FRANCE_LAMBERT_93, this.reader.getMapMetricProjection());
		}

		@Test
		public void getFileSize() throws Exception {
			assertEquals(4764, this.reader.getFileSize());
		}

		@Test
		public void getShapeElementType() {
			assertEquals(ShapeElementType.POLYGON, this.reader.getShapeElementType());
		}

		@Test
		public void getFileReadingPosition() throws Exception {
			assertEquals(ESRIFileUtil.HEADER_BYTES, this.reader.getFileReadingPosition());
			this.reader.read();
			assertEquals(236, this.reader.getFileReadingPosition());
		}

		@Test
		public void getBoundsFromHeader() {
			ESRIBounds bounds = this.reader.getBoundsFromHeader();
			assertEpsilonEquals(936456.7, bounds.getMinX());
			assertEpsilonEquals(2300653.7, bounds.getMinY());
			assertEpsilonEquals(941093.9, bounds.getMaxX());
			assertEpsilonEquals(2308847.4, bounds.getMaxY());
		}

		@Test
		public void read() throws Exception {
			MapElement element = this.reader.read();
			assertTrue(element instanceof MapPolygon);
			MapPolygon poly = (MapPolygon) element;
			Iterator<Point2d> iterator = poly.pointIterator();
			Point2d p;
			for (int i = 0; i < ALL_XS[0].length; ++i) {
				p = iterator.next();
				assertEpsilonEquals(ALL_XS[0][i], p.getX());
				assertEpsilonEquals(ALL_YS[0][i], p.getY());
			}
			assertFalse(iterator.hasNext());
		}

		@Test
		public void iterator() {
			Point2d p;
			Iterator<MapElement> iterator = this.reader.iterator();
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = (MapPolygon) iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorBoolean() {
			Point2d p;
			Iterator<MapElement> iterator = this.reader.iterator(false);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = (MapPolygon) iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClass_polygon() {
			Point2d p;
			Iterator<MapPolygon> iterator = this.reader.iterator(MapPolygon.class);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClass_polyline() {
			Iterator<MapPolyline> iterator = this.reader.iterator(MapPolyline.class);
			assertFalse(iterator.hasNext());
		}

		@Test
		public void iteratorClassBoolean_false() {
			Point2d p;
			Iterator<MapPolygon> iterator = this.reader.iterator(MapPolygon.class, false);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClassBoolean_polyline() {
			Iterator<MapPolyline> iterator = this.reader.iterator(MapPolyline.class, false);
			assertFalse(iterator.hasNext());
			
		}

		@Test
		public void seek() throws Exception {
			this.reader.seek(4);

			MapElement element = this.reader.read();
			assertTrue(element instanceof MapPolygon);
			MapPolygon poly = (MapPolygon) element;
			Iterator<Point2d> iterator = poly.pointIterator();
			Point2d p;
			for (int i = 0; i < ALL_XS[4].length; ++i) {
				p = iterator.next();
				assertEpsilonEquals(ALL_XS[4][i], p.getX());
				assertEpsilonEquals(ALL_YS[4][i], p.getY());
			}
			assertFalse(iterator.hasNext());
		}
		
	}

	@Nested
	@DisplayName("+DBF+SHX")
	public class WithDbfWithShx extends AbstractTestCase {

		private DBaseFileReader dbaseReader;

		private ShapeFileIndexReader shxReader;

		private GISShapeFileReader reader;

		@BeforeEach
		public void setUp() throws Exception {
			this.dbaseReader = new DBaseFileReader(dbfUrl);
			this.shxReader = new ShapeFileIndexReader(shxUrl);
			this.reader = new GISShapeFileReader(shpUrl, this.dbaseReader, this.shxReader);
		}

		@AfterEach
		public void tearDown() throws Exception {
			this.dbaseReader.close();
			this.dbaseReader = null;
			this.shxReader.close();
			this.shxReader = null;
			this.reader.close();
			this.reader = null;
		}

		@Test
		public void getMapElementType() {
			assertEquals(MapPolygon.class, this.reader.getMapElementType());
		}

		@Test
		public void getMapMetricProjection() {
			assertEquals(MapMetricProjection.FRANCE_LAMBERT_2_EXTENDED, this.reader.getMapMetricProjection());
		}

		@Test
		public void setMapMetricProjection() {
			this.reader.setMapMetricProjection(MapMetricProjection.FRANCE_LAMBERT_93);
			assertEquals(MapMetricProjection.FRANCE_LAMBERT_93, this.reader.getMapMetricProjection());
		}

		@Test
		public void getFileSize() throws Exception {
			assertEquals(4764, this.reader.getFileSize());
		}

		@Test
		public void getShapeElementType() {
			assertEquals(ShapeElementType.POLYGON, this.reader.getShapeElementType());
		}

		@Test
		public void getFileReadingPosition() throws Exception {
			assertEquals(ESRIFileUtil.HEADER_BYTES, this.reader.getFileReadingPosition());
			this.reader.read();
			assertEquals(236, this.reader.getFileReadingPosition());
		}

		@Test
		public void getBoundsFromHeader() {
			ESRIBounds bounds = this.reader.getBoundsFromHeader();
			assertEpsilonEquals(936456.7, bounds.getMinX());
			assertEpsilonEquals(2300653.7, bounds.getMinY());
			assertEpsilonEquals(941093.9, bounds.getMaxX());
			assertEpsilonEquals(2308847.4, bounds.getMaxY());
		}

		@Test
		public void read() throws Exception {
			MapElement element = this.reader.read();
			assertTrue(element instanceof MapPolygon);
			MapPolygon poly = (MapPolygon) element;
			Iterator<Point2d> iterator = poly.pointIterator();
			Point2d p;
			for (int i = 0; i < ALL_XS[0].length; ++i) {
				p = iterator.next();
				assertEpsilonEquals(ALL_XS[0][i], p.getX());
				assertEpsilonEquals(ALL_YS[0][i], p.getY());
			}
			assertFalse(iterator.hasNext());
		}

		@Test
		public void iterator() {
			Point2d p;
			Iterator<MapElement> iterator = this.reader.iterator();
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = (MapPolygon) iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorBoolean() {
			Point2d p;
			Iterator<MapElement> iterator = this.reader.iterator(false);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = (MapPolygon) iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClass_polygon() {
			Point2d p;
			Iterator<MapPolygon> iterator = this.reader.iterator(MapPolygon.class);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClass_polyline() {
			Iterator<MapPolyline> iterator = this.reader.iterator(MapPolyline.class);
			assertFalse(iterator.hasNext());
		}

		@Test
		public void iteratorClassBoolean_false() {
			Point2d p;
			Iterator<MapPolygon> iterator = this.reader.iterator(MapPolygon.class, false);
			int j = 0;
			for (; iterator.hasNext(); ++j) {
				MapPolygon poly = iterator.next();
				Iterator<Point2d> iterator2 = poly.pointIterator();
				for (int i = 0; i < ALL_XS[j].length; ++i) {
					p = iterator2.next();
					assertEpsilonEquals(ALL_XS[j][i], p.getX(), "Polygon #" + j + "; Point #" + i + "; X"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					assertEpsilonEquals(ALL_YS[j][i], p.getY(), "Polygon #" + j + "; Point #" + i + "; Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				assertFalse(iterator2.hasNext());
			}
			assertEquals(ALL_XS.length, j);
		}

		@Test
		public void iteratorClassBoolean_polyline() {
			Iterator<MapPolyline> iterator = this.reader.iterator(MapPolyline.class, false);
			assertFalse(iterator.hasNext());
			
		}

		@Test
		public void seek() throws Exception {
			this.reader.seek(4);

			MapElement element = this.reader.read();
			assertTrue(element instanceof MapPolygon);
			MapPolygon poly = (MapPolygon) element;
			Iterator<Point2d> iterator = poly.pointIterator();
			Point2d p;
			for (int i = 0; i < ALL_XS[4].length; ++i) {
				p = iterator.next();
				assertEpsilonEquals(ALL_XS[4][i], p.getX());
				assertEpsilonEquals(ALL_YS[4][i], p.getY());
			}
			assertFalse(iterator.hasNext());
		}

	}

}

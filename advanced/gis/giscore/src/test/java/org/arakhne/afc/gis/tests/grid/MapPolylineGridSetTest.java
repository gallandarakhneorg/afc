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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.arakhne.afc.gis.grid.MapPolylineGridSet;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.gis.tests.TestGISReader;
import org.arakhne.afc.io.shape.ShapeFileFormatException;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.util.OutputParameter;
import org.arakhne.afc.vmutil.Resources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Unit test for MapPolylineTreeSet.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("MapPolylineGridSet")
@SuppressWarnings("all")
public class MapPolylineGridSetTest extends AbstractGisTestCase {

	private static final String SHP_RESOURCE = MapPolylineGridSetTest.class.getPackage().getName().replaceAll("\\.", "/") //$NON-NLS-1$ //$NON-NLS-2$
			+ "/test.shp"; //$NON-NLS-1$

	private ArrayList<MapPolyline> reference = null;
	private Rectangle2d bounds = null;

	@BeforeEach
	public void setUp() throws Exception {
		reference = new ArrayList<>();

		InputStream is = Resources.getResourceAsStream(SHP_RESOURCE);
		assertNotNull(is);

		try {
			TestGISReader reader = new TestGISReader(is);
	
			MapElement element;
	
			Rectangle2d abounds = new Rectangle2d();
			boolean first = true;
	
			while ((element=reader.read())!=null) {
				if (element instanceof MapPolyline) {
					MapPolyline p = (MapPolyline)element;
					reference.add(p);
					if (first) {
						first = false;
						abounds.set(p.getBoundingBox());
					} else {
						abounds.setUnion(p.getBoundingBox());
					}
				}
			}
	
			reader.close();

			if (!first) bounds = abounds;
			else bounds = null;
		} catch (ShapeFileFormatException ex) {
			assumeFalse(true, "The test Shape file cannot be read" + ex); //$NON-NLS-1$
		}
	}

	@AfterEach
	public void tearDown() throws Exception {
		reference.clear();
		reference = null;
		bounds = null;
	}
	
	private boolean isEq(Point2d p, MapPolyline polyline) {
		Point2d p1 = polyline.getPointAt(0);
		Point2d p2 = polyline.getPointAt(polyline.getPointCount()-1);
		boolean a = isEpsilonEquals(p.getX(), p1.getX())
				&&  isEpsilonEquals(p.getY(), p1.getY());
		boolean b = isEpsilonEquals(p.getX(), p2.getX())
				&&  isEpsilonEquals(p.getY(), p2.getY());
		return a || b;
	}
	
	private MapPolyline extractFromReference(Point2d p) {
		for(MapPolyline pp : reference) {
			if (isEq(p, pp)) {
				return pp;
			}
		}
		return null;
	}
	
	private void assertConnected(MapPolyline base, MapPolyline neightbour, boolean isFirst) {
		assertNotSame(base, neightbour);
		Point2d p;
		if (isFirst) {
			p = base.getPointAt(0);
		}
		else {
			p = base.getPointAt(base.getPointCount()-1);
		}

		if (neightbour==null) {
			if (extractFromReference(p)==null) return;
		}
		else if (isEq(p, neightbour)) {
			return;
		}

		if (neightbour==null)
			fail("invalid connection: null"); //$NON-NLS-1$
		else
			fail("invalid connection: "+neightbour.getDistance(p)); //$NON-NLS-1$
	}

	@DisplayName("getNearestEnd")
	@Nested
	public class GetNearestEnd {

	    private MapPolylineGridSet<MapPolyline> testGrid;
	    private List<Point2d> testPoints;
	    private List<List<MapPolyline>> expectedNearests;

	    @BeforeEach
	    public void setUp() {
	        testGrid = new MapPolylineGridSet<>(100, 100, bounds);
	        testGrid.addAll(reference);

	        Random rnd = new Random(0);
	        testPoints = new ArrayList<>();
	        expectedNearests = new ArrayList<>();

	        for (int i = 0; i < 5; i++) {
	            double x = rnd.nextDouble() * bounds.getWidth() + bounds.getMinX();
	            double y = rnd.nextDouble() * bounds.getHeight() + bounds.getMinY();
	            Point2d p = new Point2d(x, y);

	            double minDistance = Double.MAX_VALUE;
	            List<MapPolyline> nearest = new ArrayList<>();
	            for (MapPolyline line : reference) {
	                double d = line.distanceToEnd(p);
	                if (d < minDistance) {
	                    minDistance = d;
	                    nearest.clear();
	                    nearest.add(line);
	                } else if (d == minDistance) {
	                    nearest.add(line);
	                }
	            }

	            testPoints.add(p);
	            expectedNearests.add(nearest);
	        }
	    }

	    @DisplayName("#1")
	    @Test
	    public void testBoundsNotNull() {
	        assertNotNull(bounds);
	    }

	    @DisplayName("#2")
	    @Test
	    public void testGridSize() {
	        assertEquals(reference.size(), testGrid.size());
	    }

	    @DisplayName("#3")
	    @Test
	    public void testNearestEndPoint1() {
	        verifyNearestEndForIndex(0);
	    }

	    @DisplayName("#4")
	    @Test
	    public void testNearestEndPoint2() {
	        verifyNearestEndForIndex(1);
	    }

	    @DisplayName("#5")
	    @Test
	    public void testNearestEndPoint3() {
	        verifyNearestEndForIndex(2);
	    }

	    @DisplayName("#6")
	    @Test
	    public void testNearestEndPoint4() {
	        verifyNearestEndForIndex(3);
	    }

	    @DisplayName("#7")
	    @Test
	    public void testNearestEndPoint5() {
	        verifyNearestEndForIndex(4);
	    }

	    private void verifyNearestEndForIndex(int index) {
	        Point2d p = testPoints.get(index);
	        MapPolyline nearestData = testGrid.getNearestEnd(p.getX(), p.getY());
	        assertNotNull(nearestData);
	        List<MapPolyline> expected = expectedNearests.get(index);
	        assertTrue(expected.contains(nearestData));
	    }
	}

	@DisplayName("add")
	@Nested
	public class Add {

	    private MapPolylineGridSet<MapPolyline> testGrid;
	    private List<Boolean> addResults;
	    private List<MapPolyline> addedPolylines;
	    private List<MapPolyline> firstOutputs;
	    private List<MapPolyline> secondOutputs;

	    @BeforeEach
	    public void setUp() {
	        testGrid = new MapPolylineGridSet<>(100, 100, bounds);
	        testGrid.addAll(reference);

	        Random rnd = new Random(0);

	        addResults = new ArrayList<>();
	        addedPolylines = new ArrayList<>();
	        firstOutputs = new ArrayList<>();
	        secondOutputs = new ArrayList<>();

	        OutputParameter<MapPolyline> first = new OutputParameter<>();
	        OutputParameter<MapPolyline> second = new OutputParameter<>();
	        MapPolyline polyline;
	        Point2d p1, p2;

	        first.clear();
	        second.clear();
	        polyline = new MapPolyline();
	        p1 = reference.get(0).getPointAt(reference.get(0).getPointCount() - 1);
	        p2 = new Point2d(
	                bounds.getMinX() + rnd.nextDouble() * bounds.getWidth(),
	                bounds.getMinY() + rnd.nextDouble() * bounds.getHeight());
	        polyline.addPoint(p1.getX(), p1.getY());
	        polyline.addPoint(p2.getX(), p2.getY());
	        addResults.add(testGrid.add(polyline, 0.5, first, second));
	        addedPolylines.add(polyline);
	        firstOutputs.add(first.get());
	        secondOutputs.add(second.get());

	        first.clear();
	        second.clear();
	        polyline = new MapPolyline();
	        p1 = new Point2d(
	                bounds.getMinX() + rnd.nextDouble() * bounds.getWidth(),
	                bounds.getMinY() + rnd.nextDouble() * bounds.getHeight());
	        p2 = reference.get(1).getPointAt(reference.get(1).getPointCount() - 1);
	        polyline.addPoint(p1.getX(), p1.getY());
	        polyline.addPoint(p2.getX(), p2.getY());
	        addResults.add(testGrid.add(polyline, 0.5, first, second));
	        addedPolylines.add(polyline);
	        firstOutputs.add(first.get());
	        secondOutputs.add(second.get());

	        first.clear();
	        second.clear();
	        polyline = new MapPolyline();
	        p1 = reference.get(2).getPointAt(reference.get(2).getPointCount() - 1);
	        p2 = reference.get(3).getPointAt(reference.get(3).getPointCount() - 1);
	        polyline.addPoint(p1.getX(), p1.getY());
	        polyline.addPoint(p2.getX(), p2.getY());
	        addResults.add(testGrid.add(polyline, 0.5, first, second));
	        addedPolylines.add(polyline);
	        firstOutputs.add(first.get());
	        secondOutputs.add(second.get());

	        first.clear();
	        second.clear();
	        polyline = new MapPolyline();
	        p1 = new Point2d(
	                bounds.getMinX() + rnd.nextDouble() * bounds.getWidth(),
	                bounds.getMinY() + rnd.nextDouble() * bounds.getHeight());
	        p2 = new Point2d(
	                bounds.getMinX() + rnd.nextDouble() * bounds.getWidth(),
	                bounds.getMinY() + rnd.nextDouble() * bounds.getHeight());
	        polyline.addPoint(p1.getX(), p1.getY());
	        polyline.addPoint(p2.getX(), p2.getY());
	        addResults.add(testGrid.add(polyline, 0.5, first, second));
	        addedPolylines.add(polyline);
	        firstOutputs.add(first.get());
	        secondOutputs.add(second.get());
	    }

	    @DisplayName("#1")
	    @Test
	    public void testBoundsNotNull() {
	        assertNotNull(bounds);
	    }

	    @DisplayName("#2")
	    @Test
	    public void testGridSize() {
	        assertEquals(reference.size(), testGrid.size());
	    }

	    @DisplayName("#3")
	    @Test
	    public void testAddResult1() {
	        assertTrue(addResults.get(0));
	    }

	    @DisplayName("#4")
	    @Test
	    public void testFirstConnected1() {
	        assertConnected(addedPolylines.get(0), firstOutputs.get(0), true);
	    }

	    @DisplayName("#5")
	    @Test
	    public void testSecondConnected1() {
	        assertConnected(addedPolylines.get(0), secondOutputs.get(0), false);
	    }

	    @DisplayName("#6")
	    @Test
	    public void testAddResult2() {
	        assertTrue(addResults.get(1));
	    }

	    @DisplayName("#7")
	    @Test
	    public void testFirstConnected2() {
	        assertConnected(addedPolylines.get(1), firstOutputs.get(1), true);
	    }

	    @DisplayName("#8")
	    @Test
	    public void testSecondConnected2() {
	        assertConnected(addedPolylines.get(1), secondOutputs.get(1), false);
	    }

	    @DisplayName("#9")
	    @Test
	    public void testAddResult3() {
	        assertTrue(addResults.get(2));
	    }

	    @DisplayName("#10")
	    @Test
	    public void testFirstConnected3() {
	        assertConnected(addedPolylines.get(2), firstOutputs.get(2), true);
	    }

	    @DisplayName("#11")
	    @Test
	    public void testSecondConnected3() {
	        assertConnected(addedPolylines.get(2), secondOutputs.get(2), false);
	    }

	    @DisplayName("#12")
	    @Test
	    public void testAddResult4() {
	        assertTrue(addResults.get(3));
	    }

	    @DisplayName("#13")
	    @Test
	    public void testFirstConnected4() {
	        assertConnected(addedPolylines.get(3), firstOutputs.get(3), true);
	    }

	    @DisplayName("#14")
	    @Test
	    public void testSecondConnected4() {
	        assertConnected(addedPolylines.get(3), secondOutputs.get(3), false);
	    }
	}
}

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

package org.arakhne.afc.gis.tests.tree;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.gis.tests.TestGISReader;
import org.arakhne.afc.gis.tree.MapPolylineTreeSet;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/** Unit test for MapPolylineTreeSet.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("MapPolylineTreeSet")
@SuppressWarnings("all")
public class MapPolylineTreeSetTest extends AbstractGisTestCase {

	private static final String SHP_RESOURCE = MapElementTreeSetTest.class.getPackage().getName().replaceAll("\\.", "/") //$NON-NLS-1$ //$NON-NLS-2$
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
			assumeFalse(true, "Cannot read the Shape file" + ex); //$NON-NLS-1$
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

	@DisplayName("getNearest")
	@Nested
	public class GetNearest {

	    private MapPolylineTreeSet<MapPolyline> testGrid;
	    private List<Point2d> testPoints;
	    private List<List<MapPolyline>> expectedNearests;

	    @BeforeEach
	    public void setUp() {
	        if (bounds != null) {
	            testGrid = new MapPolylineTreeSet<>(bounds);
	        } else {
	            testGrid = new MapPolylineTreeSet<>();
	        }
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
	        assertEpsilonEquals(reference.size(), testGrid.size());
	    }

	    private static Stream<Integer> provideIndices() {
	        return IntStream.range(0, 5).boxed();
	    }

	    @DisplayName("#3")
	    @ParameterizedTest(name = "point {0}")
	    @MethodSource("provideIndices")
	    public void testNearestNotNull(Integer idx) {
	        Point2d p = testPoints.get(idx);
	        MapPolyline nearestData = testGrid.getNearestEnd(p.getX(), p.getY());
	        assertNotNull(nearestData);
	    }

	    @DisplayName("#4")
	    @ParameterizedTest(name = "point {0}")
	    @MethodSource("provideIndices")
	    public void testNearestContainsExpected(Integer idx) {
	        Point2d p = testPoints.get(idx);
	        MapPolyline nearestData = testGrid.getNearestEnd(p.getX(), p.getY());
	        List<MapPolyline> expected = expectedNearests.get(idx);
	        assertTrue(expected.contains(nearestData));
	    }
	}

	@DisplayName("add")
	@Nested
	public class Add {

	    private List<Boolean> addResults;
	    private List<MapPolyline> addedPolylines;
	    private List<MapPolyline> firstOutputs;
	    private List<MapPolyline> secondOutputs;

	    @BeforeEach
	    public void setUp() {
	        Random rnd = new Random(0);

	        addResults = new ArrayList<>();
	        addedPolylines = new ArrayList<>();
	        firstOutputs = new ArrayList<>();
	        secondOutputs = new ArrayList<>();

	        OutputParameter<MapPolyline> first = new OutputParameter<>();
	        OutputParameter<MapPolyline> second = new OutputParameter<>();
	        MapPolyline polyline;
	        Point2d p1, p2;

	        // Scenario 1
	        first.clear();
	        second.clear();
	        polyline = new MapPolyline();
	        p1 = reference.get(0).getPointAt(reference.get(0).getPointCount() - 1);
	        p2 = new Point2d(
	                bounds.getMinX() + rnd.nextDouble() * bounds.getWidth(),
	                bounds.getMinY() + rnd.nextDouble() * bounds.getHeight());
	        polyline.addPoint(p1.getX(), p1.getY());
	        polyline.addPoint(p2.getX(), p2.getY());
	        MapPolylineTreeSet<MapPolyline> test1 = new MapPolylineTreeSet<>(bounds);
	        test1.addAll(reference);
	        addResults.add(test1.add(polyline, 0.5, first, second));
	        addedPolylines.add(polyline);
	        firstOutputs.add(first.get());
	        secondOutputs.add(second.get());

	        // Scenario 2
	        first.clear();
	        second.clear();
	        polyline = new MapPolyline();
	        p1 = new Point2d(
	                bounds.getMinX() + rnd.nextDouble() * bounds.getWidth(),
	                bounds.getMinY() + rnd.nextDouble() * bounds.getHeight());
	        p2 = reference.get(1).getPointAt(reference.get(1).getPointCount() - 1);
	        polyline.addPoint(p1.getX(), p1.getY());
	        polyline.addPoint(p2.getX(), p2.getY());
	        MapPolylineTreeSet<MapPolyline> test2 = new MapPolylineTreeSet<>(bounds);
	        test2.addAll(reference);
	        addResults.add(test2.add(polyline, 0.5, first, second));
	        addedPolylines.add(polyline);
	        firstOutputs.add(first.get());
	        secondOutputs.add(second.get());

	        // Scenario 3
	        first.clear();
	        second.clear();
	        polyline = new MapPolyline();
	        p1 = reference.get(2).getPointAt(reference.get(2).getPointCount() - 1);
	        p2 = reference.get(3).getPointAt(reference.get(3).getPointCount() - 1);
	        polyline.addPoint(p1.getX(), p1.getY());
	        polyline.addPoint(p2.getX(), p2.getY());
	        MapPolylineTreeSet<MapPolyline> test3 = new MapPolylineTreeSet<>(bounds);
	        test3.addAll(reference);
	        addResults.add(test3.add(polyline, 0.5, first, second));
	        addedPolylines.add(polyline);
	        firstOutputs.add(first.get());
	        secondOutputs.add(second.get());

	        // Scenario 4
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
	        MapPolylineTreeSet<MapPolyline> test4 = new MapPolylineTreeSet<>(bounds);
	        test4.addAll(reference);
	        addResults.add(test4.add(polyline, 0.5, first, second));
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
	    public void testInitialAddAllSize() {
	        MapPolylineTreeSet<MapPolyline> test = new MapPolylineTreeSet<>(bounds);
	        test.addAll(reference);
	        assertEpsilonEquals(reference.size(), test.size());
	    }

	    private static Stream<Integer> provideScenarios() {
	        return IntStream.range(0, 4).boxed();
	    }

	    @DisplayName("#3")
	    @ParameterizedTest(name = "scenario {0}")
	    @MethodSource("provideScenarios")
	    public void testAddReturnsTrue(Integer idx) {
	        assertTrue(addResults.get(idx));
	    }

	    @DisplayName("#4")
	    @ParameterizedTest(name = "scenario {0}")
	    @MethodSource("provideScenarios")
	    public void testFirstConnectedTrue(Integer idx) {
	        assertConnected(addedPolylines.get(idx), firstOutputs.get(idx), true);
	    }

	    @DisplayName("#5")
	    @ParameterizedTest(name = "scenario {0}")
	    @MethodSource("provideScenarios")
	    public void testSecondConnectedFalse(Integer idx) {
	        assertConnected(addedPolylines.get(idx), secondOutputs.get(idx), false);
	    }
	}
}

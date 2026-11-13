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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.gis.tests.TestGISReader;
import org.arakhne.afc.gis.tree.MapElementTreeSet;
import org.arakhne.afc.gis.tree.StandardGISTreeSet;
import org.arakhne.afc.io.shape.ShapeFileFormatException;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.vmutil.Resources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/** Unit test for MapElementTreeSet.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("MapElementTreeSet")
@SuppressWarnings("all")
public class MapElementTreeSetTest extends AbstractGisTestCase {

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

	@DisplayName("getNearest")
	@Nested
	public class GetNearest {

	    private MapElementTreeSet<MapPolyline> testGrid;
	    private List<Point2d> testPoints;
	    private List<List<MapPolyline>> expectedNearests;

	    @BeforeEach
	    public void setUp() {
	        if (bounds != null) {
	            testGrid = new MapElementTreeSet<>(bounds);
	        } else {
	            testGrid = new MapElementTreeSet<>();
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
	                double d = line.getDistance(p);
	                if (isEpsilonEquals(minDistance, d)) {
	                    nearest.add(line);
	                } else if (d < minDistance) {
	                    minDistance = d;
	                    nearest.clear();
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
	        MapPolyline nearestData = testGrid.getNearest(p.getX(), p.getY());
	        assertNotNull(nearestData);
	    }

	    @DisplayName("#4")
	    @ParameterizedTest(name = "point {0}")
	    @MethodSource("provideIndices")
	    public void testNearestContainsExpected(Integer idx) {
	        Point2d p = testPoints.get(idx);
	        MapPolyline nearestData = testGrid.getNearest(p.getX(), p.getY());
	        List<MapPolyline> expected = expectedNearests.get(idx);
	        assertTrue(expected.contains(nearestData));
	    }
	}

	@DisplayName("add")
	@Nested
	public class Add {

	    private StandardGISTreeSet<GISPrimitive> testGrid;
	    private List<MapPolyline> newElements;
	    private List<Boolean> addResults;
	    private List<Integer> sizesAfterAdd;
	    private List<Boolean> containsAfterAdd;
	    private List<Object[]> expectedArrays;
	    private List<Object[]> actualArrays;

	    @BeforeEach
	    public void setUp() {
	        testGrid = new StandardGISTreeSet<>(bounds);
	        testGrid.addAll(reference);

	        Random rnd = new Random(0);
	        newElements = new ArrayList<>();
	        addResults = new ArrayList<>();
	        sizesAfterAdd = new ArrayList<>();
	        containsAfterAdd = new ArrayList<>();
	        expectedArrays = new ArrayList<>();
	        actualArrays = new ArrayList<>();

	        for (int i = 0; i < 5; i++) {
	            MapPolyline newElement = new MapPolyline();
	            double x = bounds.getMinX() + rnd.nextDouble() * bounds.getWidth();
	            double y = bounds.getMinY() + rnd.nextDouble() * bounds.getHeight();
	            newElement.addPoint(x, y);
	            x = bounds.getMinX() + rnd.nextDouble() * bounds.getWidth();
	            y = bounds.getMinY() + rnd.nextDouble() * bounds.getHeight();
	            newElement.addPoint(x, y);

	            // Create a fresh grid for each scenario to avoid side effects
	            StandardGISTreeSet<GISPrimitive> freshGrid = new StandardGISTreeSet<>(bounds);
	            freshGrid.addAll(reference);
	            addResults.add(freshGrid.add(newElement));
	            sizesAfterAdd.add(freshGrid.size());
	            containsAfterAdd.add(freshGrid.slowContains(newElement));

	            List<MapPolyline> expected = new ArrayList<>(reference);
	            expected.add(newElement);
	            expectedArrays.add(expected.toArray());
	            actualArrays.add(freshGrid.toArray());

	            newElements.add(newElement);
	        }
	    }

	    private static Stream<Integer> provideIndices() {
	        return IntStream.range(0, 5).boxed();
	    }

	    @DisplayName("#1")
	    @Test
	    public void testAddAll() {
	        StandardGISTreeSet<GISPrimitive> grid = new StandardGISTreeSet<>(bounds);
	        assertTrue(grid.addAll(reference));
	    }

	    @DisplayName("#2")
	    @Test
	    public void testSizeAfterAddAll() {
	        StandardGISTreeSet<GISPrimitive> grid = new StandardGISTreeSet<>(bounds);
	        grid.addAll(reference);
	        assertEpsilonEquals(reference.size(), grid.size());
	    }

	    @DisplayName("#3")
	    @ParameterizedTest(name = "scenario {0}")
	    @MethodSource("provideIndices")
	    public void testAddReturnsTrue(Integer idx) {
	        assertTrue(addResults.get(idx));
	    }

	    @DisplayName("#4")
	    @ParameterizedTest(name = "scenario {0}")
	    @MethodSource("provideIndices")
	    public void testSizeAfterAdd(Integer idx) {
	        assertEquals(reference.size() + 1, (int) sizesAfterAdd.get(idx));
	    }

	    @DisplayName("#5")
	    @ParameterizedTest(name = "scenario {0}")
	    @MethodSource("provideIndices")
	    public void testSlowContainsAfterAdd(Integer idx) {
	        assertTrue(containsAfterAdd.get(idx));
	    }

	    @DisplayName("#6")
	    @ParameterizedTest(name = "scenario {0}")
	    @MethodSource("provideIndices")
	    public void testEpsilonEqualsAfterAdd(Integer idx) {
	        assertEpsilonEquals(expectedArrays.get(idx), actualArrays.get(idx));
	    }
	}
}

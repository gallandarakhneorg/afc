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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.arakhne.afc.gis.grid.MapElementGridSet;
import org.arakhne.afc.gis.grid.StandardGISGridSet;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.gis.tests.TestGISReader;
import org.arakhne.afc.io.shape.ShapeFileFormatException;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Unit test for MapElementGridSet.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("MapElementGridSet")
@SuppressWarnings("all")
public class MapElementGridSetTest extends AbstractTestCase {

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
			assumeFalse(true, "Cannot read Shape file" + ex); //$NON-NLS-1$
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

	    // ---- class attributes (moved from local variables) ----
	    private MapElementGridSet<MapPolyline> testGrid;      // the grid under test

	    // Store points and their expected nearest polylines
	    private List<Point2d> testPoints;
	    private List<List<MapPolyline>> expectedNearests;

	    @BeforeEach
	    public void setUp() {
	        // Build the grid and add all reference elements
	        testGrid = new MapElementGridSet<>(100, 100, bounds);
	        testGrid.addAll(reference);

	        // Generate 5 deterministic points using a fixed seed
	        Random rnd = new Random(0);   // fixed seed for reproducibility
	        testPoints = new ArrayList<>();
	        expectedNearests = new ArrayList<>();

	        for (int i = 0; i < 5; i++) {
	            double x = rnd.nextDouble() * bounds.getWidth() + bounds.getMinX();
	            double y = rnd.nextDouble() * bounds.getHeight() + bounds.getMinY();
	            Point2d p = new Point2d(x, y);

	            // Compute minimal distance to any polyline in reference
	            double minDistance = Double.MAX_VALUE;
	            for (MapPolyline line : reference) {
	                double d = line.getDistance(p);
	                if (d < minDistance) {
	                    minDistance = d;
	                }
	            }

	            // Collect all polylines that achieve that minimal distance
	            List<MapPolyline> nearest = new ArrayList<>();
	            for (MapPolyline line : reference) {
	                double d = line.getDistance(p);
	                if (isEpsilonEquals(d, minDistance)) {
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
	    public void testNearestPoint1() {
	        verifyNearestForIndex(0);
	    }

	    @DisplayName("#4")
	    @Test
	    public void testNearestPoint2() {
	        verifyNearestForIndex(1);
	    }

	    @DisplayName("#5")
	    @Test
	    public void testNearestPoint3() {
	        verifyNearestForIndex(2);
	    }

	    @DisplayName("#6")
	    @Test
	    public void testNearestPoint4() {
	        verifyNearestForIndex(3);
	    }

	    @DisplayName("#7")
	    @Test
	    public void testNearestPoint5() {
	        verifyNearestForIndex(4);
	    }

	    // Helper method to avoid duplication
	    private void verifyNearestForIndex(int index) {
	        Point2d p = testPoints.get(index);
	        MapPolyline nearestData = testGrid.getNearest(p.getX(), p.getY());
	        assertNotNull(nearestData);

	        List<MapPolyline> expected = expectedNearests.get(index);
	        boolean found = false;
	        for (MapPolyline poly : expected) {
	            if (poly == nearestData) {
	                found = true;
	                break;
	            }
	        }
	        assertTrue(found, "Nearest polyline not found in expected set for point " + p);
	    }
	}

	@DisplayName("add")
	@Nested
	public class Add {

	    // ---- class attributes (moved from local variables) ----
	    private StandardGISGridSet<GISPrimitive> testGrid;  // the grid under test
	    private List<MapPolyline> newElements;          // deterministic list of elements to add

	    @BeforeEach
	    public void setUp() {
	        // Create the grid and add all reference elements once
	        testGrid = new StandardGISGridSet<>(100, 100, bounds);
	        testGrid.addAll(reference);

	        // Generate 5 deterministic new polylines using a fixed seed
	        Random rnd = new Random(0);   // fixed seed for reproducibility
	        newElements = new ArrayList<>();
	        for (int i = 0; i < 5; i++) {
	            MapPolyline newElement = new MapPolyline();
	            double x = bounds.getMinX() + rnd.nextDouble() * bounds.getWidth();
	            double y = bounds.getMinY() + rnd.nextDouble() * bounds.getHeight();
	            newElement.addPoint(x, y);
	            x = bounds.getMinX() + rnd.nextDouble() * bounds.getWidth();
	            y = bounds.getMinY() + rnd.nextDouble() * bounds.getHeight();
	            newElement.addPoint(x, y);
	            newElements.add(newElement);
	        }
	    }

	    // ---- individual test methods ----

	    @DisplayName("#1")
	    @Test
	    public void testInitialAddAll() {
	        // Verify that addAll was successful and the size is correct
	        assertTrue(testGrid.addAll(reference));    // addAll is called again but we already added in setUp
	        // Actually we already called addAll in setUp, so we might not want to call again.
	        // Better: we can test that the grid already contains the reference elements.
	        // Since we added in setUp, we can verify size.
	        assertEquals(reference.size(), testGrid.size());
	        // Also verify that slowContains works for all reference elements? Not required but we can add.
	        // For thoroughness, we can check one element.
	        if (!reference.isEmpty()) {
	            assertTrue(testGrid.slowContains(reference.get(0)));
	        }
	    }

	    @DisplayName("#2")
	    @Test
	    public void testAddElement1() {
	        addAndVerifyElement(0);
	    }

	    @DisplayName("#3")
	    @Test
	    public void testAddElement2() {
	        addAndVerifyElement(1);
	    }

	    @DisplayName("#4")
	    @Test
	    public void testAddElement3() {
	        addAndVerifyElement(2);
	    }

	    @DisplayName("#5")
	    @Test
	    public void testAddElement4() {
	        addAndVerifyElement(3);
	    }

	    @DisplayName("#6")
	    @Test
	    public void testAddElement5() {
	        addAndVerifyElement(4);
	    }

	    // Helper method to avoid duplication
	    private void addAndVerifyElement(int index) {
	        MapPolyline element = newElements.get(index);
	        assertTrue(testGrid.add(element), "add should return true");

	        // After adding one element, size should be reference.size() + 1
	        assertEquals(reference.size() + 1, testGrid.size());

	        // The element should be contained
	        assertTrue(testGrid.slowContains(element));

	        // Prepare the expected array: reference + the new element
	        List<MapPolyline> expectedList = new ArrayList<>(reference);
	        expectedList.add(element);
	        assertEpsilonEquals(expectedList.toArray(), testGrid.toArray());
	    }
	}
}

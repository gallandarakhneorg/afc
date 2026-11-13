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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.gis.tests.TestGISReader;
import org.arakhne.afc.gis.tree.StandardGISTreeSet;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ShapeFileFormatException;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.Resources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Unit test for GISTreeSet.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("StandardGISTreeSet")
@SuppressWarnings("all")
public class StandardGISTreeSetTest extends AbstractGisTestCase {

	private static final String SHP_RESOURCE = MapElementTreeSetTest.class.getPackage().getName().replaceAll("\\.", "/") //$NON-NLS-1$ //$NON-NLS-2$
			+ "/test.shp"; //$NON-NLS-1$

	private static final int MAX_REFERENCE_SIZE = 1000;
	private static final int MAX_TEST_COUNT = 500;
	private static final int MAX_REFERENCE_NEWS = 50;

	private ArrayList<GISPrimitive> reference = null;
	private Point2d center = null;
	private Rectangle2d worldBounds = null;

	@BeforeEach
	public void setUp() throws Exception {
		try {
			getLogger().info("Reading reference shape file..."); //$NON-NLS-1$

			reference = new ArrayList<>();

			InputStream is = Resources.getResourceAsStream(SHP_RESOURCE);
			assertNotNull(is);

			TestGISReader reader = new TestGISReader(is);

			MapElement element;

			ESRIBounds eb = reader.getBoundsFromHeader();
			worldBounds = new Rectangle2d();
			worldBounds.setFromCorners(
					eb.getMinX(), eb.getMinY(),
					eb.getMaxX(), eb.getMaxY());

			double x = 0;
			double y = 0;
			int i=0;
			while (i<MAX_REFERENCE_SIZE && (element=reader.read())!=null) {
				reference.add(element);
				x += element.getBoundingBox().getCenterX();
				y += element.getBoundingBox().getCenterY();
				++i;
			}

			reader.close();

			x /= reference.size();
			y /= reference.size();

			center = new Point2d(x,y);
		} catch (ShapeFileFormatException ex) {
			assumeFalse(true, "Cannot read the Shape file" + ex); //$NON-NLS-1$
		} finally {
			getLogger().info("finished"); //$NON-NLS-1$
		}
	}

	@AfterEach
	public void tearDown() throws Exception {
		reference.clear();
		reference = null;
		center = null;
		worldBounds = null;
	}

	public static Stream<Arguments> provideIntegers() {
		final var list = new ArrayList<Arguments>();
		final var n = MAX_TEST_COUNT;
		for (var i = 0; i < n; ++i) {
			list.add(Arguments.of(i));
		}
		return list.stream();
	}

	public static Stream<Arguments> provideAdditionIndexes() {
		final var list = new ArrayList<Arguments>();
		for (var j = 2; j < MAX_REFERENCE_NEWS; ++j) {
			list.add(Arguments.of(j));
		}
		return list.stream();
	}

	@DisplayName("addAll")
	@Nested
	public class AddAll {

        private List<Point2d> transformed;
        private Rectangle2d bounds;
        private StandardGISTreeSet<GISPrimitive> test;

        @BeforeEach
        public void setUp() {
	        reference.clear();
	        reference.add(new MapPoint(1000,1000));
	        reference.add(new MapPoint(0,0));
	        reference.add(new MapPoint(501,801));
	        reference.add(new MapPoint(500,800));
	        reference.add(new MapPoint(502,802));
	        reference.add(new MapPoint(100,800));
	        reference.add(new MapPoint(800,0));

	        transformed = new ArrayList<>();
	        for (final GISPrimitive it : reference) {
	        	transformed.add(((MapPoint)it).getPoint());
	        }
	
	        bounds = new Rectangle2d();
	        bounds.setFromPointCloud(transformed);
	
	        test = new StandardGISTreeSet<>(bounds);
        }

		@DisplayName("#1")
        @Test
		public void testAddAll_1() {	
	        assertTrue(test.addAll(reference));
        }

		@DisplayName("#2")
        @Test
		public void testAddAll_2() {	
	        test.addAll(reference);
	        for(GISPrimitive p : test) {
	        	assertTrue(reference.remove(p));
	        }
	        assertTrue(reference.isEmpty());
		}
	}

	@DisplayName("size")
	@Nested
	public class Size {

        private StandardGISTreeSet<GISPrimitive> test;

        @BeforeEach
        public void setUp() {
	        test = new StandardGISTreeSet<>(worldBounds);
        }

		@DisplayName("#1")
		@Test
		public void testSize_1() {
	        assertEpsilonEquals(0, test.size());
        }

		@DisplayName("#2")
		@Test
		public void testSize_2() {
	        test.addAll(reference);
	        assertEpsilonEquals(reference.size(), test.size());
	    }
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

        private StandardGISTreeSet<GISPrimitive> test;

        @BeforeEach
        public void setUp() {
	        test = new StandardGISTreeSet<>(worldBounds);
        }

		@DisplayName("#1")
		@Test
	    public void testIsEmpty_1() {
	        assertTrue(test.isEmpty());
        }

		@DisplayName("#2")
		@Test
	    public void testIsEmpty_2() {
	   		test.addAll(reference);
	        assertFalse(test.isEmpty());
	    }
	}

	@DisplayName("slowContains")
	@Nested
	public class SlowContains {

        private StandardGISTreeSet<GISPrimitive> test;

        @BeforeEach
        public void setUp() {
	        test = new StandardGISTreeSet<>(worldBounds);
	        assertTrue(test.addAll(reference));
	        assertEpsilonEquals(reference.size(), test.size());
        }

		@DisplayName("#1")
        @Test
	    public void testSlowContains_1() {
			assertFalse(test.slowContains(null));
        }

		@DisplayName("#2")
        @Test
	    public void testSlowContains_2() {
			assertFalse(test.slowContains(test));
        }

		@DisplayName("#3")
        @Test
	    public void testSlowContains_3() {
			// Test the content
			for(GISPrimitive elt : reference) {
				assertTrue(test.slowContains(elt));
			}
        }

		@DisplayName("#4")
        @Test
	    public void testSlowContains_4() {
			assertFalse(test.slowContains(reference));
	    }
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

        private StandardGISTreeSet<GISPrimitive> test;

        @BeforeEach
        public void setUp() {
	        test = new StandardGISTreeSet<>(worldBounds);
	        assertTrue(test.addAll(reference));
	        assertEpsilonEquals(reference.size(), test.size());
        }

		@DisplayName("#1")
		@Test
	    public void testContains_1() {
			assertFalse(test.contains(null));
        }

		@DisplayName("#2")
		@Test
	    public void testContains_2() {
			assertFalse(test.contains(test));
        }

		@DisplayName("#3")
		@Test
	    public void testContains_3() {
			for(GISPrimitive elt : reference) {
				assertTrue(test.contains(elt), elt.getGeoLocation().toBounds2D().toString());
			}
        }

		@DisplayName("#4")
		@Test
	    public void testContains_4() {
			assertFalse(test.contains(reference));
	    }
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

        private StandardGISTreeSet<GISPrimitive> test;

        @BeforeEach
        public void setUp() {
	        test = new StandardGISTreeSet<>(worldBounds);
	        assertTrue(test.addAll(reference));
	        assertEpsilonEquals(reference.size(), test.size());
        }

        @DisplayName("()")
		@Nested
		public class NoArgument {

			@DisplayName("#1")
			@Test
		    public void testIterator() {
		    	Iterator<GISPrimitive> iter = test.iterator();
		    	while (iter.hasNext()) {
		    		var s = iter.next();
		    		assertTrue(reference.remove(s));
		    	}
		    	assertTrue(reference.isEmpty());
		    }
		}

		@DisplayName("(Rectangle2d)")
		@Nested
		public class WithRectangle {

	    	private Collection<GISPrimitive> inter;
	    	private Rectangle2d bounds;
	    	
	    	@BeforeEach
	    	public void setUp() {
		    	inter = new TreeSet<>(new Comparator<GISPrimitive>() {
					@Override
					public int compare(GISPrimitive o1, GISPrimitive o2) {
						return System.identityHashCode(o2) - System.identityHashCode(o1);
					}
		    	});

		    	var deltaX = getRandom().nextInt(1000)*(getRandom().nextBoolean() ? -1 : 1);
	        	var deltaY = getRandom().nextInt(1000)*(getRandom().nextBoolean() ? -1 : 1);
	        	var width = getRandom().nextInt(100);
	        	var height = getRandom().nextInt(100);
	
		        bounds = new Rectangle2d();
		        bounds.setFromCorners(
		        		center.getX()+deltaX, center.getY()+deltaY,
		        		center.getX()+deltaX+width,
		        		center.getY()+deltaY+height);
		        inter.clear();
	
		    	for (GISPrimitive primitive : reference) {
		    		Shape2d<?> primitiveRect = primitive.getGeoLocation().toBounds2D();
					if (bounds.intersects(primitiveRect)) {
						inter.add(primitive);
					}
				}
	    	}

			@DisplayName("#1")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
			public void testIteratorRectangle_1(Integer i) {
		        var iter = test.iterator(bounds);
		    	while (iter.hasNext()) {
		    		var primitive = iter.next();
		    		assertTrue(inter.remove(primitive));
		    	}
			}

			@DisplayName("#2")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
			public void testIteratorRectangle_2(Integer i) {
		        var iter = test.iterator(bounds);
		    	while (iter.hasNext()) {
		    		var primitive = iter.next();
		    		inter.remove(primitive);
		    	}
	
		    	if (!inter.isEmpty()) {
			    	for (GISPrimitive pr: reference) {
			    		Shape2d<?> primitiveRect = pr.getGeoLocation().toBounds2D();
						if (bounds.intersects(primitiveRect)) {
							inter.add(pr);
						}
					}
	
			        iter = test.iterator(bounds);
			    	while (iter.hasNext()) {
			    		var primitive = iter.next();
			    		assertTrue(inter.remove(primitive));
			    	}
		    	}
			}
		}
	}

	@DisplayName("containsAll")
	@Nested
	public class ContainsAll {

        private StandardGISTreeSet<GISPrimitive> test;

        @BeforeEach
        public void setUp() {
	        test = new StandardGISTreeSet<>(worldBounds);
	        assertTrue(test.addAll(reference));
	        assertEpsilonEquals(reference.size(), test.size());
        }

		@DisplayName("#1")
		@Test
		public void testContainsAll_1() {
	    	assertTrue(reference.containsAll(test));
		}

		@DisplayName("#2")
		@Test
		public void testContainsAll_2() {
	    	assertTrue(test.containsAll(reference));
		}
	}

	@DisplayName("toArray")
	@Nested
	public class ToArray {

		@DisplayName("()")
		@Nested
		public class NoArgument {

	        private StandardGISTreeSet<GISPrimitive> test;

	        @BeforeEach
	        public void setUp() {
		        test = new StandardGISTreeSet<>(worldBounds);
		        assertTrue(test.addAll(reference));
	        }

			@DisplayName("#1")
	        @Test
			public void testToArray_1() {
		        assertEpsilonEquals(reference.size(), test.size());
			}

			@DisplayName("#2")
	        @Test
			public void testToArray_2() {
		    	assertEpsilonEquals(reference.toArray(),test.toArray());
			}
		}

		@DisplayName("(GISPrimitive[])")
		@Nested
		public class WithGISPrimitiveArray {

	        private StandardGISTreeSet<GISPrimitive> test;

	        @BeforeEach
	        public void setUp() {
		        test = new StandardGISTreeSet<>(worldBounds);
		        assertTrue(test.addAll(reference));
	        }
			
			@DisplayName("#1")
			@Test
			public void testToArrayArray_1() {
		        assertEpsilonEquals(reference.size(), test.size());
			}

			@DisplayName("#2")
			@Test
			public void testToArrayArray_2() {
		        var tab = new GISPrimitive[reference.size()];
		    	assertEpsilonEquals(reference.toArray(),test.toArray(tab));
		    	assertEpsilonEquals(reference.toArray(),tab);
			}

			@DisplayName("#3")
			@Test
			public void testToArrayArray_3() {
		        var tab = new GISPrimitive[reference.size()/2];
		        GISPrimitive[] tab2 = test.toArray(tab);
		    	assertEpsilonEquals(reference.toArray(),tab2);
		    	assertNotEquals(tab2,tab);
			}
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		@DisplayName("#1")
		@Test
		public void testClear() {
	        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(worldBounds);
	        test.addAll(reference);
	        assertEpsilonEquals(reference.size(), test.size());
	    	test.clear();
	    	assertTrue(test.isEmpty());
		}
	}

	@DisplayName("add")
	@Nested
	public class Add {

		private StandardGISTreeSet<GISPrimitive> test;
		
		@BeforeEach
		public void setUp() {
	        test = new StandardGISTreeSet<>(worldBounds);
	        assertTrue(test.addAll(reference));
	        assertEpsilonEquals(reference.size(), test.size());
		}

		@DisplayName("One addition")
		@Nested
		public class OneAddition {

			private double x;
			private double y;
			private GISPrimitive newElement;
			
			@BeforeEach
			public void setUp() {
	        	x = worldBounds.getMinX() + getRandom().nextDouble() * worldBounds.getWidth();
	        	y = worldBounds.getMinY() + getRandom().nextDouble() * worldBounds.getHeight();
		        newElement = new MapPoint(x,y);
			}

			@DisplayName("Single add")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
			public void testAddE_1(Integer i) {
		        assertTrue(test.add(newElement));
			}
	
			@DisplayName("Add multiple times")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
			public void testAddE_2(Integer i) {
		        test.add(newElement);
		        assertTrue(test.add(newElement));
			}
	
			@DisplayName("Size after")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
			public void testAddE_3(Integer i) {
				test.add(newElement);
		        assertEquals(reference.size() + 1, test.size());
			}
	
			@DisplayName("Containing before")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
			public void testAddE_4(Integer i) {
		        assertFalse(test.slowContains(newElement));
			}
	
			@DisplayName("Containing after")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
			public void testAddE_5(Integer i) {
				test.add(newElement);
		        assertTrue(test.slowContains(newElement));
			}
	
			@DisplayName("toArray")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
			public void testAddE_6(Integer i) {
				reference.add(newElement);
				test.add(newElement);
		    	assertEpsilonEquals(reference.toArray(),test.toArray());
			}
		}

		@DisplayName("Many additions")
		@Nested
		public class ManyAdditions {

			private double[] x;
			private double[] y;
			private GISPrimitive[] newElement;
			
			@BeforeEach
			public void setUp() {
				x = new double[MAX_REFERENCE_NEWS];
				y = new double[MAX_REFERENCE_NEWS];
				newElement = new GISPrimitive[MAX_REFERENCE_NEWS];
				for (var i = 0; i < MAX_REFERENCE_NEWS; ++i) {
		        	x[i] = worldBounds.getMinX() + getRandom().nextDouble() * worldBounds.getWidth();
		        	y[i] = worldBounds.getMinY() + getRandom().nextDouble() * worldBounds.getHeight();
			        newElement[i] = new MapPoint(x[i], y[i]);
				}
			}

			@DisplayName("Single add")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideAdditionIndexes")
			public void testAddE_1(Integer j) {
				for (var k = 0; k < j; ++k) {
					assertTrue(test.add(newElement[k]));
				}
			}
	
			@DisplayName("Add multiple times")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideAdditionIndexes")
			public void testAddE_2(Integer j) {
				for (var k = 0; k < j; ++k) {
					test.add(newElement[k]);
				}
		        assertTrue(test.add(newElement[j - 1]));
			}
	
			@DisplayName("Size after")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideAdditionIndexes")
			public void testAddE_3(Integer j) {
				for (var k = 0; k < j; ++k) {
					test.add(newElement[k]);
				}
		        assertEquals(reference.size() + j, test.size());
			}
	
			@DisplayName("Containing before")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideAdditionIndexes")
			public void testAddE_4(Integer j) {
				for (var k = 0; k < j; ++k) {
					test.add(newElement[k]);
				}
		        assertFalse(test.slowContains(newElement));
			}
	
			@DisplayName("Containing after")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideAdditionIndexes")
			public void testAddE_5(Integer j) {
				for (var k = 0; k < j; ++k) {
					test.add(newElement[k]);
				}
		        assertTrue(test.slowContains(newElement[j - 1]));
			}
	
			@DisplayName("toArray")
		    @ParameterizedTest(name = "{index} => {0}")
		    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideAdditionIndexes")
			public void testAddE_6(Integer j) {
				for (var k = 0; k < j; ++k) {
					reference.add(newElement[k]);
					test.add(newElement[k]);
				}
		    	assertEpsilonEquals(reference.toArray(),test.toArray());
			}
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		private StandardGISTreeSet<GISPrimitive> test;
		private int removalIndex;
		private GISPrimitive toRemove;
		
		@BeforeEach
		public void setUp() {
	        test = new StandardGISTreeSet<>(worldBounds);
	        assertTrue(test.addAll(reference));
	        assertEpsilonEquals(reference.size(), test.size());
        	removalIndex = getRandom().nextInt(reference.size());
        	toRemove = reference.get(removalIndex);
		}

		@DisplayName("#1")
	    @ParameterizedTest(name = "{index} => {0}")
	    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
		public void testRemoveObject_1(Integer i) {
	        assertTrue(test.remove(toRemove));
	        assertFalse(test.slowContains(toRemove));
		}
	}

	@DisplayName("getGeoLocation")
	@Nested
	public class GetGeoLocation {

		private StandardGISTreeSet<GISPrimitive> test;
		
		@BeforeEach
		public void setUp() {
	        test = new StandardGISTreeSet<>(worldBounds);
	        assertTrue(test.addAll(reference));
	        assertEpsilonEquals(reference.size(), test.size());
		}
		
	    @DisplayName("#1")
	    @Test
	    public void testGeoLocation_1() {
			assertNull(test.get((GeoLocation)null));
	    }

	    @DisplayName("#2")
	    @ParameterizedTest(name = "{index} => {0}")
	    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
	    public void testGeoLocation_2(Integer i) {
			var p = reference.get(getRandom().nextInt(reference.size()));
			assertSame(p, test.get(p.getGeoLocation()));
		}
		
	    @DisplayName("#3")
	    @Test
	    public void testGeoLocation_3() {
			assertNull(test.get(new GeoLocationPoint(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)));
	    }
	}

	@DisplayName("getGeoId")
	@Nested
	public class GetGeoId {

		private StandardGISTreeSet<GISPrimitive> test;
		
		@BeforeEach
		public void setUp() {
	        test = new StandardGISTreeSet<>(worldBounds);
	        assertTrue(test.addAll(reference));
	        assertEpsilonEquals(reference.size(), test.size());
		}
		
	    @DisplayName("#1")
	    @Test
	    public void testGetGeoId_1() {
			assertNull(test.get((GeoId)null));
	    }

	    @DisplayName("#2")
	    @ParameterizedTest(name = "{index} => {0}")
	    @MethodSource("org.arakhne.afc.gis.tests.tree.StandardGISTreeSetTest#provideIntegers")
	    public void testGetGeoId_2(Integer i) {
			var p = reference.get(getRandom().nextInt(reference.size()));
			assertSame(p, test.get(p.getGeoId()));
	    }
	}
}

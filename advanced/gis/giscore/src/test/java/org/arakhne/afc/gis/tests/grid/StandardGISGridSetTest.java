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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.arakhne.afc.gis.grid.GridCell;
import org.arakhne.afc.gis.grid.StandardGISGridSet;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.gis.tests.TestGISReader;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ShapeFileFormatException;
import org.arakhne.afc.math.geometry.d2.afp.BoundedElement2afp;
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
@DisplayName("StandardGISGridSet")
@SuppressWarnings("all")
public class StandardGISGridSetTest extends AbstractGisTestCase {

	private static final String SHP_RESOURCE = MapPolylineGridSetTest.class.getPackage().getName().replaceAll("\\.", "/") //$NON-NLS-1$ //$NON-NLS-2$
			+ "/test.shp"; //$NON-NLS-1$

	private static final int MAX_REFERENCE_SIZE = 1000;
	private static final int MAX_TEST_COUNT = 1000;

	private ArrayList<GISPrimitive> reference = null;
	private Point2d center = null;
	private Rectangle2d worldBounds = null;

	void doSetUp() throws Exception {
		try {
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
			assumeTrue(false, "Cannot read Shape file: " + ex); //$NON-NLS-1$
		}
	}

	@AfterEach
	public void tearDown() throws Exception {
		if (reference != null) {
			reference.clear();
			reference = null;
		}
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

	public static Stream<Arguments> provideIntegerReferenceIndex() {
		final var list = new ArrayList<Arguments>();
		final var n = MAX_TEST_COUNT;
		for (var i = 0; i < n; ++i) {
			var index = getRandom().nextInt(MAX_REFERENCE_SIZE);
			list.add(Arguments.of(i, index));
		}
		return list.stream();
	}

	@DisplayName("addAll")
	@Nested
	public class AddAll {

		private Rectangle2d bounds;
		private StandardGISGridSet<GISPrimitive> testGrid;
		private boolean addAllResult;
		private int sizeAfterAdd;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			reference = new ArrayList<>();
			reference.add(new MapPoint(1000, 1000));
			reference.add(new MapPoint(0, 0));
			reference.add(new MapPoint(501, 801));
			reference.add(new MapPoint(500, 800));
			reference.add(new MapPoint(502, 802));
			reference.add(new MapPoint(100, 800));
			reference.add(new MapPoint(800, 0));

			List<Point2d> transformed = new ArrayList<>();
			for (GISPrimitive it : reference) {
				transformed.add(((MapPoint) it).getPoint());
			}

			bounds = new Rectangle2d();
			bounds.setFromPointCloud(transformed);

			testGrid = new StandardGISGridSet<>(100, 100, bounds);
			addAllResult = testGrid.addAll(reference);
			sizeAfterAdd = testGrid.size();
		}

		@DisplayName("#1")
		@Test
		public void testAddAll() {
			assertTrue(addAllResult);
		}

		@DisplayName("#2")
		@Test
		public void testSize() {
			assertEquals(reference.size(), sizeAfterAdd);
		}

		private static Stream<Integer> provideIndices() {
			return IntStream.range(0, 7).boxed();
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => element {0}")
		@MethodSource("provideIndices")
		public void testContainsElement(Integer idx) {
			GISPrimitive element = reference.get(idx);
			assertTrue(testGrid.contains(element));
		}
	}

	@DisplayName("size")
	@Nested
	public class Size {

		private StandardGISGridSet<GISPrimitive> emptyGrid;
		private StandardGISGridSet<GISPrimitive> filledGrid;
		private int emptySize;
		private int filledSize;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			emptyGrid = new StandardGISGridSet<>(100, 100, worldBounds);
			emptySize = emptyGrid.size();

			filledGrid = new StandardGISGridSet<>(100, 100, worldBounds);
			filledGrid.addAll(reference);
			filledSize = filledGrid.size();
		}

		@DisplayName("#1")
		@Test
		public void testSizeEmpty() {
			assertEquals(0, emptySize);
		}

		@DisplayName("#2")
		@Test
		public void testSizeAfterAdd() {
			assertEquals(reference.size(), filledSize);
		}
	}

	@DisplayName("isEmpty")
	@Nested
	public class IsEmpty {

		private StandardGISGridSet<GISPrimitive> emptyGrid;
		private StandardGISGridSet<GISPrimitive> filledGrid;
		private boolean emptyIsEmpty;
		private boolean filledIsEmpty;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			emptyGrid = new StandardGISGridSet<>(100, 100, worldBounds);
			emptyIsEmpty = emptyGrid.isEmpty();

			filledGrid = new StandardGISGridSet<>(100, 100, worldBounds);
			filledGrid.addAll(reference);
			filledIsEmpty = filledGrid.isEmpty();
		}

		@DisplayName("#1")
		@Test
		public void testIsEmptyTrue() {
			assertTrue(emptyIsEmpty);
		}

		@DisplayName("#2")
		@Test
		public void testIsEmptyFalse() {
			assertFalse(filledIsEmpty);
		}
	}

	@DisplayName("slowContains")
	@Nested
	public class SlowContains {

		private StandardGISGridSet<GISPrimitive> testGrid;
		private boolean addAllResult;
		private int sizeAfterAdd;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			testGrid = new StandardGISGridSet<>(100, 100, worldBounds);
			addAllResult = testGrid.addAll(reference);
			sizeAfterAdd = testGrid.size();
		}

		@DisplayName("#1")
		@Test
		public void testAddAll() {
			assertTrue(addAllResult);
		}

		@DisplayName("#2")
		@Test
		public void testSize() {
			assertEquals(reference.size(), sizeAfterAdd);
		}

		@DisplayName("#3")
		@Test
		public void testSlowContainsNull() {
			assertFalse(testGrid.slowContains(null));
		}

		@DisplayName("#4")
		@Test
		public void testSlowContainsTestObject() {
			assertFalse(testGrid.slowContains(testGrid));
		}

		private static Stream<Integer> provideIndices() {
			return IntStream.range(0, Math.min(5, MAX_REFERENCE_SIZE)).boxed();
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => element {0}")
		@MethodSource("provideIndices")
		public void testSlowContainsReferenceElement(Integer idx) {
			GISPrimitive element = reference.get(idx);
			assertTrue(testGrid.slowContains(element));
		}

		@DisplayName("#6")
		@Test
		public void testSlowContainsReferenceCollection() {
			assertFalse(testGrid.slowContains(reference));
		}
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		private StandardGISGridSet<GISPrimitive> testGrid;
		private boolean addAllResult;
		private int sizeAfterAdd;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			testGrid = new StandardGISGridSet<>(100, 100, worldBounds);
			addAllResult = testGrid.addAll(reference);
			sizeAfterAdd = testGrid.size();
		}

		@DisplayName("#1")
		@Test
		public void testAddAll() {
			assertTrue(addAllResult);
		}

		@DisplayName("#2")
		@Test
		public void testSize() {
			assertEquals(reference.size(), sizeAfterAdd);
		}

		@DisplayName("#3")
		@Test
		public void testContainsNull() {
			assertFalse(testGrid.contains(null));
		}

		@DisplayName("#4")
		@Test
		public void testContainsTestObject() {
			assertFalse(testGrid.contains(testGrid));
		}

		private static Stream<Integer> provideIndices() {
			return IntStream.range(0, Math.min(5, MAX_REFERENCE_SIZE)).boxed();
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => element {0}")
		@MethodSource("provideIndices")
		public void testContainsReferenceElement(Integer idx) {
			GISPrimitive element = reference.get(idx);
			assertTrue(testGrid.contains(element));
		}

		@DisplayName("#6")
		@Test
		public void testContainsReferenceCollection() {
			assertFalse(testGrid.contains(reference));
		}
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

		@DisplayName("()")
		@Nested
		public class WithoutArgument {

			private boolean addAllResult;
			private int sizeAfterAdd;
			private List<GISPrimitive> iteratorElements;

			@BeforeEach
			public void setUp() throws Exception {
				doSetUp();
				StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
				addAllResult = test.addAll(reference);
				sizeAfterAdd = test.size();
				iteratorElements = new ArrayList<>();
				for (GISPrimitive p : test) {
					iteratorElements.add(p);
				}
			}

			@DisplayName("#1")
			@Test
			public void testAddAll() {
				assertTrue(addAllResult);
			}

			@DisplayName("#2")
			@Test
			public void testSize() {
				assertEquals(reference.size(), sizeAfterAdd);
			}

			@DisplayName("#3")
			@Test
			public void testIteratorSize() {
				assertEquals(reference.size(), iteratorElements.size());
			}

			private static Stream<Integer> provideIndices() {
				return IntStream.range(0, Math.min(5, MAX_REFERENCE_SIZE)).boxed();
			}

			@DisplayName("#4")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("provideIndices")
			public void testIteratorContainsElement(Integer idx) {
				GISPrimitive element = reference.get(idx);
				assertTrue(iteratorElements.contains(element));
			}
		}

		@DisplayName("(Rectangle2d)")
		@Nested
		public class WithRectangle {

			private static final Collection<GISPrimitive> INTER = new TreeSet<>(new Comparator<GISPrimitive>() {
				@Override
				public int compare(GISPrimitive o1, GISPrimitive o2) {
					return System.identityHashCode(o2) - System.identityHashCode(o1);
				}
			});

			private StandardGISGridSet<GISPrimitive> test;
			private int deltaX;
			private int deltaY;
			private int width;
			private int height;
			private Rectangle2d bounds;

			@BeforeEach
			public void setUp() throws Exception {
				doSetUp();
				test = new StandardGISGridSet<>(100, 100, worldBounds);
				test.addAll(reference);
				deltaX = getRandom().nextInt(1000)*(getRandom().nextBoolean() ? -1 : 1);
				deltaY = getRandom().nextInt(1000)*(getRandom().nextBoolean() ? -1 : 1);
				width = getRandom().nextInt(100);
				height = getRandom().nextInt(100);

				bounds = new Rectangle2d();
				bounds.setFromCorners(
						center.getX()+deltaX, center.getY()+deltaY,
						center.getX()+deltaX+width,
						center.getY()+deltaY+height);
				INTER.clear();
				for (GISPrimitive primitive : reference) {
					Shape2d<?> primitiveRect = primitive.getGeoLocation().toBounds2D();
					if (bounds.intersects(primitiveRect)) {
						INTER.add(primitive);
					}
				}
			}

			@DisplayName("iterator")
			@ParameterizedTest(name = "{0}")
			@MethodSource("org.arakhne.afc.gis.tests.grid.StandardGISGridSetTest#provideIntegers")
			public void testIteratorRectangle(Integer idxTest) {
				var iter = test.iterator(bounds);
				while (iter.hasNext()) {
					var primitive = iter.next();
					assertTrue(INTER.remove(primitive));
				}
				if (!INTER.isEmpty()) {
					for (GISPrimitive pr: reference) {
						Shape2d<?> primitiveRect = pr.getGeoLocation().toBounds2D();
						if (bounds.intersects(primitiveRect)) {
							INTER.add(pr);
						}
					}

					iter = test.iterator(bounds);
					while (iter.hasNext()) {
						var primitive = iter.next();
						assertTrue(INTER.remove(primitive));
					}
				}
			}
		}

		@DisplayName("(GeoLocation)")
		@Nested
		public class WithGeoLocation {

			private StandardGISGridSet<GISPrimitive> test;

			private List<GISPrimitive> iteratorElements;

			@BeforeEach
			public void setUp() throws Exception {
				doSetUp();
				test = new StandardGISGridSet<>(100, 100, worldBounds);
				test.addAll(reference);
				iteratorElements = new ArrayList<>();
				for (GISPrimitive p : test) {
					iteratorElements.add(p);
				}
			}

			private static Stream<Integer> provideIndices() {
				return IntStream.range(0, Math.min(5, MAX_REFERENCE_SIZE)).boxed();
			}

			@DisplayName("#1")
			@ParameterizedTest(name = "{index} => {0}")
			@MethodSource("provideIndices")
			public void testIteratorContainsElement(Integer idx) {
				var element = reference.get(idx);
				var location = element.getGeoLocation();
				var iter = test.iterator(location);
				assertTrue(iter.hasNext());
				while (iter.hasNext()) {
					var currentElement = iter.next();
					assertEquals(location, currentElement.getGeoLocation());
				}
			}
		}
	}

	@DisplayName("containsAll")
	@Nested
	public class ContainsAll {

		private StandardGISGridSet<GISPrimitive> testGrid;
		private boolean addAllResult;
		private int sizeAfterAdd;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			testGrid = new StandardGISGridSet<>(100, 100, worldBounds);
			addAllResult = testGrid.addAll(reference);
			sizeAfterAdd = testGrid.size();
		}

		@DisplayName("#1")
		@Test
		public void testAddAll() {
			assertTrue(addAllResult);
		}

		@DisplayName("#2")
		@Test
		public void testSize() {
			assertEquals(reference.size(), sizeAfterAdd);
		}

		@DisplayName("#3")
		@Test
		public void testReferenceContainsAllTest() {
			assertTrue(reference.containsAll(testGrid));
		}

		@DisplayName("#4")
		@Test
		public void testTestContainsAllReference() {
			assertTrue(testGrid.containsAll(reference));
		}
	}

	@DisplayName("toArray")
	@Nested
	public class ToArray {

		@DisplayName("()")
		@Nested
		public class NoArgument {

			private boolean addAllResult;
			private int sizeResult;
			private Object[] expectedArray;
			private Object[] actualArray;

			@BeforeEach
			public void setUp() throws Exception {
				doSetUp();
				StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
				addAllResult = test.addAll(reference);
				sizeResult = test.size();
				expectedArray = reference.toArray();
				actualArray = test.toArray();
			}

			@DisplayName("#1")
			@Test
			public void testAddAll() {
				assertTrue(addAllResult);
			}

			@DisplayName("#2")
			@Test
			public void testSize() {
				assertEquals(reference.size(), sizeResult);
			}

			@DisplayName("#3")
			@Test
			public void testContent() {
				assertEpsilonEquals(expectedArray, actualArray);
			}
		}

		@DisplayName("(GISPrimitive[])")
		@Nested
		public class WithArrayArgument {

			private boolean addAllResult;
			private int sizeResult;
			private Object[] expectedArray;
			private Object[] actualArrayRightSize;
			private Object[] tabRightSize;
			private Object[] actualArraySmallSize;
			private Object[] tabSmallSize;
			private Object[] tab2SmallSize;

			@BeforeEach
			public void setUp() throws Exception {
				doSetUp();
				StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
				addAllResult = test.addAll(reference);
				sizeResult = test.size();
				expectedArray = reference.toArray();

				tabRightSize = new GISPrimitive[reference.size()];
				actualArrayRightSize = test.toArray(tabRightSize);

				tabSmallSize = new GISPrimitive[reference.size() / 2];
				tab2SmallSize = test.toArray(tabSmallSize);
				actualArraySmallSize = tab2SmallSize;
			}

			@DisplayName("#1")
			@Test
			public void testAddAll() {
				assertTrue(addAllResult);
			}

			@DisplayName("#2")
			@Test
			public void testSize() {
				assertEquals(reference.size(), sizeResult);
			}

			@DisplayName("#3")
			@Test
			public void testToArrayWithRightSizeReturnValue() {
				assertEpsilonEquals(expectedArray, actualArrayRightSize);
			}

			@DisplayName("#4")
			@Test
			public void testToArrayWithRightSizeFillsArray() {
				assertEpsilonEquals(expectedArray, tabRightSize);
			}

			@DisplayName("#5")
			@Test
			public void testToArrayWithSmallSizeReturnValue() {
				assertEpsilonEquals(expectedArray, tab2SmallSize);
			}

			@DisplayName("#6")
			@Test
			public void testToArrayWithSmallSizeReturnsNewArray() {
				assertNotEquals(tab2SmallSize, tabSmallSize);
			}
		}
	}

	@DisplayName("clear")
	@Nested
	public class Clear {

		private int sizeAfterAdd;
		private boolean isEmptyAfterClear;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
			test.addAll(reference);
			sizeAfterAdd = test.size();
			test.clear();
			isEmptyAfterClear = test.isEmpty();
		}

		@DisplayName("#1")
		@Test
		public void testAddAllSize() {
			assertEquals(reference.size(), sizeAfterAdd);
		}

		@DisplayName("#2")
		@Test
		public void testClearIsEmpty() {
			assertTrue(isEmptyAfterClear);
		}
	}

	@DisplayName("add")
	@Nested
	public class Add {

		private List<GISPrimitive> newElements;
		private List<Boolean> addResults;
		private List<Integer> sizesAfterAdd;
		private List<Boolean> containsAfterAdd;
		private List<Object[]> expectedArrays;
		private List<Object[]> actualArrays;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			newElements = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				double x = worldBounds.getMinX() + getRandom().nextDouble() * worldBounds.getWidth();
				double y = worldBounds.getMinY() + getRandom().nextDouble() * worldBounds.getHeight();
				newElements.add(new MapPoint(x, y));
			}

			addResults = new ArrayList<>();
			sizesAfterAdd = new ArrayList<>();
			containsAfterAdd = new ArrayList<>();
			expectedArrays = new ArrayList<>();
			actualArrays = new ArrayList<>();

			for (GISPrimitive newElement : newElements) {
				StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
				test.addAll(reference);
				addResults.add(test.add(newElement));
				sizesAfterAdd.add(test.size());
				containsAfterAdd.add(test.slowContains(newElement));
				List<GISPrimitive> expectedList = new ArrayList<>(reference);
				expectedList.add(newElement);
				expectedArrays.add(expectedList.toArray());
				actualArrays.add(test.toArray());
			}
		}

		@DisplayName("#1")
		@Test
		public void testAddAllReturnsTrue() {
			StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
			assertTrue(test.addAll(reference));
		}

		@DisplayName("#2")
		@Test
		public void testSizeAfterAddAll() {
			StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
			test.addAll(reference);
			assertEquals(reference.size(), test.size());
		}

		private static Stream<Integer> provideIndices() {
			return IntStream.range(0, 5).boxed();
		}

		@DisplayName("#3")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("provideIndices")
		public void testAddReturnsTrue(Integer idx) {
			assertTrue(addResults.get(idx));
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("provideIndices")
		public void testSizeAfterAdd(Integer idx) {
			assertEquals((Integer) (reference.size() + 1), sizesAfterAdd.get(idx));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("provideIndices")
		public void testSlowContainsAfterAdd(Integer idx) {
			assertTrue(containsAfterAdd.get(idx));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("provideIndices")
		public void testEpsilonEqualsAfterAdd(Integer idx) {
			assertEpsilonEquals(expectedArrays.get(idx), actualArrays.get(idx));
		}
	}

	@DisplayName("remove")
	@Nested
	public class Remove {

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
		}

		@DisplayName("#1")
		@Test
		public void testAddAllReturnsTrue() {
			StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
			assertTrue(test.addAll(reference));
		}

		@DisplayName("#2")
		@Test
		public void testSizeMatches() {
			StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
			test.addAll(reference);
			assertEquals(reference.size(), test.size());
		}

		@DisplayName("#3")
		@Test
		public void testGetNullGeoLocationReturnsNull() {
			StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
			test.addAll(reference);
			assertNull(test.get((GeoLocation) null));
		}

		private static Stream<Integer> provideIndices() {
			return IntStream.range(0, 5).boxed();
		}

		@DisplayName("#4")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("provideIndices")
		public void testSlowContainsBeforeRemoval(Integer idx) {
			StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
			test.addAll(reference);
			GISPrimitive toRemove = reference.get(idx);
			assertTrue(test.slowContains(toRemove));
		}

		@DisplayName("#5")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("provideIndices")
		public void testRemoveReturnsTrue(Integer idx) {
			StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
			test.addAll(reference);
			GISPrimitive toRemove = reference.get(idx);
			assertTrue(test.remove(toRemove));
		}

		@DisplayName("#6")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("provideIndices")
		public void testSlowContainsAfterRemoval(Integer idx) {
			StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
			test.addAll(reference);
			GISPrimitive toRemove = reference.get(idx);
			test.remove(toRemove);
			assertFalse(test.slowContains(toRemove));
		}

		@DisplayName("#7")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("provideIndices")
		public void testEpsilonEqualsAfterRemoval(Integer idx) {
			StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100, 100, worldBounds);
			test.addAll(reference);
			GISPrimitive toRemove = reference.get(idx);
			test.remove(toRemove);

			List<GISPrimitive> expected = new ArrayList<>(reference);
			expected.remove(toRemove);
			assertEpsilonEquals(expected.toArray(), test.toArray());
		}
	}

	@DisplayName("getGeoLocation")
	@Nested
	public class GetGeoLocation {

		private StandardGISGridSet<GISPrimitive> test;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			test = new StandardGISGridSet<>(100,100,worldBounds);
			assertTrue(test.addAll(reference));
			assertEquals(reference.size(), test.size());
		}

		@DisplayName("Null location")
		@Test
		public void testGetGeoLocation_1() {
			assertNull(test.get((GeoLocation) null));
		}

		@DisplayName("Infinite coordinates")
		@Test
		public void testGetGeoLocation_3() {
			assertNull(test.get(new GeoLocationPoint(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)));
		}

		@DisplayName("Location from inner element")
		@ParameterizedTest(name = "{0} => {1}")
		@MethodSource("org.arakhne.afc.gis.tests.grid.StandardGISGridSetTest#provideIntegerReferenceIndex")
		public void testGetGeoLocation_2(Integer ntry, Integer index) {
			var p = reference.get(index);
			assertNotNull(p);
			var location = p.getGeoLocation();
			assertNotNull(location);
			var actual = test.get(location.clone());
			assertNotNull(actual);
			assertSame(p, actual);
		}

		@DisplayName("Location for id 891")
		@Test
		public void id891() {
			var p = reference.get(891);
			assertNotNull(p);
			var location = p.getGeoLocation();
			assertNotNull(location);
			var actual = test.get(location);
			assertNotNull(actual);
			assertSame(p, actual);
		}

		@DisplayName("Location for id 645")
		@Test
		public void id645() {
			var p = reference.get(645);
			assertNotNull(p);
			var location = p.getGeoLocation();
			assertNotNull(location);
			var actual = test.get(location);
			assertNotNull(actual);
			assertSame(p, actual);
		}

		@DisplayName("Location for id 643")
		@Test
		public void id643() {
			var p = reference.get(643);
			assertNotNull(p);
			var location = p.getGeoLocation();
			assertNotNull(location);
			var actual = test.get(location);
			assertNotNull(actual);
			assertSame(p, actual);
		}

		@DisplayName("Location for id 620")
		@Test
		public void id620() {
			// 173b2d0883bae3e474235de95c36bb49#940108;2297012;940109;2297022
			var p = reference.get(620);
			assertNotNull(p);
			var location = p.getGeoLocation();
			assertNotNull(location);
			var actual = test.get(location);
			if (actual == null) {
				actual = test.get(location);
			}
			assertNotNull(actual);
			assertSame(p, actual);
		}

		@DisplayName("Mutiple objects for location with id 620")
		@Test
		public void mid620() {
			var p = reference.get(620);
			assertNotNull(p);
			var location = p.getGeoLocation();
			assertNotNull(location);
			var iterator = test.iterator(location);
			assertTrue(iterator.hasNext());
			var foundReference = false;
			while (iterator.hasNext()) {
				var element = iterator.next();
				assertEquals(location, element.getGeoLocation());
				if (element == p) {
					foundReference = true;
				}
			}
			assertTrue(foundReference);
		}

		@DisplayName("Location for id 646")
		@Test
		public void id646() {
			var p = reference.get(646);
			assertNotNull(p);
			var location = p.getGeoLocation();
			assertNotNull(location);
			var actual = test.get(location);
			assertNotNull(actual);
			assertSame(p, actual);
		}
	}

	@DisplayName("getGrid")
	@Nested
	public class GetGrid {

		private StandardGISGridSet<GISPrimitive> test;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			test = new StandardGISGridSet<>(100, 100, worldBounds);
			assertTrue(test.addAll(reference));
		}

		@DisplayName("Get grid object")
		@Test
		public void getGrid_1() {
			assertNotNull(test.getGrid());
		}

		@DisplayName("Get grid bounds")
		@Test
		public void getGrid_2() {
			var bounds = test.getGrid().getBounds();
			assertNotNull(bounds);
			assertNotSame(worldBounds, bounds);
			assertEpsilonEquals(worldBounds.getMinX(), bounds.getMinX());
			assertEpsilonEquals(worldBounds.getMinY(), bounds.getMinY());
			assertEpsilonEquals(worldBounds.getMaxX(), bounds.getMaxX());
			assertEpsilonEquals(worldBounds.getMaxY(), bounds.getMaxY());
		}

		@DisplayName("Contains all")
		@Test
		public void containsAll() {
			// Check if all the reference elements are inside the set
			for (var elt : reference) {
				assertPositive(test.getGrid().indexOf(elt));
			}
		}

		@DisplayName("getGridCells")
		@Nested
		public class GetGridCells {

			private List<GridCell<GISPrimitive>> cells;

			@BeforeEach
			public void setUp() {
				var iterable = test.getGrid().getGridCells();
				cells = new ArrayList<>();
				iterable.forEach(cells::add);
			}

			@DisplayName("size")
			@Test
			public void size() {
				// 760 cells contain data over the total of theoritical 10000 cells.
				assertEquals(760, cells.size());
			}

			@DisplayName("Cell bounds inside world bounds")
			@Test
			public void boxBoundsInWorldBounds() {
				for (var cell : cells) {
					var cellBounds = cell.getBounds();
					assertTrue(worldBounds.contains(cellBounds));
				}
			}

			@DisplayName("Contains all from at least one cell")
			@Test
			public void containsAllInCells() {
				// Check if all the reference elements are inside a cell
				var copy = new HashSet<>(reference);
				for (var cell : cells) {
					for (var elt : cell) {
						copy.remove(elt);
					}
				}
				assertEquals(0, copy.size());
			}

			@DisplayName("Elements bounds intersect cell bounds")
			@Test
			public void boundCompatibility() {
				for (var cell : cells) {
					final var cellBounds = new Rectangle2d(cell.getBounds());
					for (var elt : cell) {
						assertInstanceOf(elt, BoundedElement2afp.class);
						var boundedElement = (BoundedElement2afp) elt;
						var elementBounds = boundedElement.getBoundingBox();
						assertNotNull(elementBounds);
						assertTrue(cellBounds.intersects(elementBounds));
					}
				}
			}


			@DisplayName("Cell bounds inside world bounds")
			@Test
			public void cellBoundsInsideWorldBounds() {
				for (var cell : cells) {
					var cellBounds = cell.getBounds();
					assertTrue(worldBounds.contains(cellBounds));
				}
			}

			@DisplayName("Unique cell")
			@ParameterizedTest(name = "{0}")
			@MethodSource("org.arakhne.afc.gis.tests.grid.StandardGISGridSetTest#provideIntegers")
			public void uniqueCell(Integer ntry) {
				var x = worldBounds.getMinX() + getRandom().nextDouble(worldBounds.getWidth());
				var y = worldBounds.getMinY() + getRandom().nextDouble(worldBounds.getHeight());
				assertTrue(worldBounds.contains(x, y));

				var allCells = new ArrayList<>();
				for (var cell : test.getGrid().getGridCells()) {
					var box = cell.getBounds();
					if (box.contains(x, y)) {
						allCells.add(cell);
					}
				}
				// Sometimes the coordinates are in a zone/cell without element;
				// There is no cell instance created. Therefore, there is no
				// cell that contains the coordinates.
				assertTrue(allCells.size() == 0 || allCells.size() == 1);
			}
		}

		@DisplayName("getGridCellsOn")
		@Nested
		public class GetGridCellsOn {

			@DisplayName("World Bounds")
			@Test
			public void worldBounds() {
				var allCells = new ArrayList<>();
				test.getGrid().getGridCells().forEach(allCells::add);

				var iterator = test.getGrid().getGridCellsOn(worldBounds);
				for (var cell : iterator) {
					assertTrue(allCells.remove(cell));
				}
				assertEquals(0, allCells.size());
			}

			@DisplayName("Inner Bounds")
			@ParameterizedTest(name = "{0}")
			@MethodSource("org.arakhne.afc.gis.tests.grid.StandardGISGridSetTest#provideIntegers")
			public void innerBounds(Integer ntry) {
				var x1 = worldBounds.getMinX() + getRandom().nextDouble(worldBounds.getWidth() / 2.);
				var y1 = worldBounds.getMinY() + getRandom().nextDouble(worldBounds.getHeight() / 2.);
				var x2 = worldBounds.getMaxX() - getRandom().nextDouble(worldBounds.getWidth() / 2.);
				var y2 = worldBounds.getMaxX() - getRandom().nextDouble(worldBounds.getHeight() / 2.);
				var innerBounds = new Rectangle2d(x1, y1, x2 - x1, y2 - y1);
				var allCells = new ArrayList<>();
				for (var cell : test.getGrid().getGridCells()) {
					var box = cell.getBounds();
					if (box.intersects(innerBounds)) {
						allCells.add(cell);
					}
				}

				var iterator = test.getGrid().getGridCellsOn(innerBounds);
				for (var cell : iterator) {
					assertTrue(allCells.remove(cell));
				}
				assertEquals(0, allCells.size());
			}
		}

		@DisplayName("getGridCellsAround")
		@Nested
		public class GetGridCellsAround {

			private double x;
			private double y;
			private GridCell expectedCell;
			
			public boolean prepareTest() {
				x = worldBounds.getMinX() + getRandom().nextDouble(worldBounds.getWidth());
				y = worldBounds.getMinY() + getRandom().nextDouble(worldBounds.getHeight());
				assertTrue(worldBounds.contains(x, y));

				var allCells = new ArrayList<GridCell>();
				for (var cell : test.getGrid().getGridCells()) {
					var box = cell.getBounds();
					if (box.contains(x, y)) {
						allCells.add(cell);
					}
				}
				// Sometimes the coordinates are in a zone/cell without element;
				// There is no cell instance created. Therefore, there is no
				// cell that contains the coordinates.
				assertTrue(allCells.size() == 0 || allCells.size() == 1);
				expectedCell = allCells.size() == 1 ? allCells.get(0) : null;
				
				return expectedCell != null;
			}
			
			@DisplayName("Distance 0 with cell")
			@ParameterizedTest(name = "{0}")
			@MethodSource("org.arakhne.afc.gis.tests.grid.StandardGISGridSetTest#provideIntegers")
			public void distance0WithCell(Integer ntry) {
				assumeTrue(prepareTest());
				var iterable = test.getGrid().getGridCellsAround(new Point2d(x, y), 0.);
				var iterator = iterable.aroundIterator();

				assertTrue(iterator.hasNext());
				var cell = iterator.next();
				assertSame(expectedCell, cell);

				assertFalse(iterator.hasNext());
			}

			@DisplayName("Distance 0 without cell")
			@ParameterizedTest(name = "{0}")
			@MethodSource("org.arakhne.afc.gis.tests.grid.StandardGISGridSetTest#provideIntegers")
			public void distance0WithoutCell(Integer ntry) {
				assumeFalse(prepareTest());
				var iterable = test.getGrid().getGridCellsAround(new Point2d(x, y), 0.);
				var iterator = iterable.aroundIterator();
				assertFalse(iterator.hasNext());
			}
		}
	}

	@DisplayName("getGeoId")
	@Nested
	public class GetGeoId {

		private StandardGISGridSet<GISPrimitive> test;

		@BeforeEach
		public void setUp() throws Exception {
			doSetUp();
			test = new StandardGISGridSet<>(100,100,worldBounds);
			assertTrue(test.addAll(reference));
			assertEquals(reference.size(), test.size());
			assertNull(test.get((GeoId)null));
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{0}")
		@MethodSource("org.arakhne.afc.gis.tests.grid.StandardGISGridSetTest#provideIntegers")
		public void testGetGeoId(Integer i) {
			var p = reference.get(getRandom().nextInt(reference.size()));
			assertSame(p, test.get(p.getGeoId()));
		}
	}
}

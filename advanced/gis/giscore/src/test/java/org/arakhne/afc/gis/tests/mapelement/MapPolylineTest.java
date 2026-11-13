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

package org.arakhne.afc.gis.tests.mapelement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/** Unit tests for MapPolyline.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("MapPolyline")
@SuppressWarnings("all")
public class MapPolylineTest extends AbstractMapComposedElementTestCase {

	@DisplayName("Creation")
	@Nested
	public class Creation {

		private MapPolyline element;
		private List<Double> referenceCoordinates;
		private Map<Point2d,String> creationPoints;

		@BeforeEach
		public void setUp() {
			element = new MapPolyline(new HeapAttributeCollection());
			referenceCoordinates = new ArrayList<>();
			creationPoints = new HashMap<>();
		}

		@DisplayName("#1")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.gis.tests.mapelement.AbstractMapComposedElementTestCase#providesIntegers()")
		public void creation(Integer i) throws Exception {
			mapComposedElement_creation(i, element, referenceCoordinates, creationPoints, null);
		}

		@DisplayName("#2")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.gis.tests.mapelement.AbstractMapComposedElementTestCase#providesIntegers()")
		public void check(Integer i) throws Exception {
			mapComposedElement_creation(i, element, referenceCoordinates, creationPoints, null);
			mapComposedElement_checkPoints(element, referenceCoordinates, creationPoints);
		}
	}

	@DisplayName("Deletion")
	@Nested
	public class Deletion {

		private MapPolyline element;
		private List<Double> referenceCoordinates;
		private Map<Point2d,String> creationPoints;

		@BeforeEach
		public void setUp() {
			element = new MapPolyline(new HeapAttributeCollection());
			referenceCoordinates = new ArrayList<>();
			creationPoints = new HashMap<>();
		}

		@DisplayName("Without creation")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.gis.tests.mapelement.AbstractMapComposedElementTestCase#providesIntegers()")
		public void withoutCreation(Integer i) throws Exception {
			mapComposedElement_deletion(element,referenceCoordinates);
		}

		@DisplayName("With creation")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.gis.tests.mapelement.AbstractMapComposedElementTestCase#providesIntegers()")
		public void withCreation(Integer i) throws Exception {
			mapComposedElement_creation(i, element, referenceCoordinates, creationPoints, null);
			mapComposedElement_deletion(element,referenceCoordinates);
		}

		@DisplayName("With check")
		@ParameterizedTest(name = "{index} => {0}")
		@MethodSource("org.arakhne.afc.gis.tests.mapelement.AbstractMapComposedElementTestCase#providesIntegers()")
		public void withCheck(Integer i) throws Exception {
			mapComposedElement_creation(i, element, referenceCoordinates, creationPoints, null);
			mapComposedElement_deletion(element,referenceCoordinates);
			mapComposedElement_checkPoints(element,referenceCoordinates,creationPoints);
		}
	}

	@DisplayName("invert")
	@Nested
	public class Invert {

		@DisplayName("testPolylineManualInverse2")
		@Nested
		public class TestPolylineManualInverse2 {

			private Point2d[] pts;
			private MapPolyline line;

			@BeforeEach
			public void setUp() {
				pts = new Point2d[] {
						new Point2d(0,0),
						new Point2d(1,1),
						new Point2d(2,2),
						new Point2d(3,3),
						new Point2d(4,4),
						new Point2d(5,5),
						new Point2d(6,6),
						new Point2d(7,7),
						new Point2d(8,8),
						new Point2d(9,9),
						new Point2d(10,10)
				};

				line = new MapPolyline();
				line.addPoint(pts[0]);
				line.addPoint(pts[1]);
				line.addPoint(pts[2]);
				line.addPoint(pts[3]);
				line.addPoint(pts[4]);
				line.addPoint(pts[5]);
				line.addPoint(pts[6]);
				line.addPoint(pts[7]);
				line.addPoint(pts[8]);
				line.addPoint(pts[9]);
				line.addPoint(pts[10]);
			}

			@DisplayName("#1")
			@Test
			public void testPolylineManualInverse2_assertion1() {
				assertEquals(line.getFirstPointIndexInGroup(0), 0);
			}

			@DisplayName("#2")
			@Test
			public void testPolylineManualInverse2_assertion2() {
				assertTrue(line.getGroupCount() == 1 );
			}

			@DisplayName("#3")
			@Test
			public void testPolylineManualInverse2_assertion3() {
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(0), 0);
			}

			@DisplayName("#4")
			@Test
			public void testPolylineManualInverse2_assertion4() {
				line.invert();
				assertEquals(line.getPointIndex(0, pts[10]), 0);
			}

			@DisplayName("#5")
			@Test
			public void testPolylineManualInverse2_assertion5() {
				line.invert();
				assertEquals(line.getPointIndex(0, pts[0]), 10);
			}
		}

		@DisplayName("testPolylineManualInverse")
		@Nested
		public class TestPolylineManualInverse {

			private Point2d[] pts;
			private MapPolyline line;

			@BeforeEach
			public void setUp() {
				pts = new Point2d[] {
						new Point2d(0,0),
						new Point2d(1,1),
						new Point2d(2,2),
						new Point2d(3,3),
						new Point2d(4,4),
						new Point2d(5,5),
						new Point2d(6,6),
						new Point2d(7,7),
						new Point2d(8,8),
						new Point2d(9,9),
						new Point2d(10,10)
				};

				line = new MapPolyline();
				line.addPoint(pts[0]);
				line.addPoint(pts[1]);
				line.addGroup(pts[2]);
				line.addPoint(pts[3]);
				line.addPoint(pts[4]);
				line.addPoint(pts[5]);
				line.addGroup(pts[6]);
				line.addGroup(pts[7]);
				line.addPoint(pts[8]);
				line.addPoint(pts[9]);
				line.addGroup(pts[10]);
			}

			@DisplayName("#1")
			@Test
			public void testPolylineManualInverse_assertion1() {
				assertEquals(line.getFirstPointIndexInGroup(0), 0);
			}

			@DisplayName("#2")
			@Test
			public void testPolylineManualInverse_assertion2() {
				assertEquals(line.getFirstPointIndexInGroup(1), 2);
			}

			@DisplayName("#3")
			@Test
			public void testPolylineManualInverse_assertion3() {
				assertEquals(line.getFirstPointIndexInGroup(2), 6);
			}

			@DisplayName("#4")
			@Test
			public void testPolylineManualInverse_assertion4() {
				assertEquals(line.getFirstPointIndexInGroup(3), 7);
			}

			@DisplayName("#5")
			@Test
			public void testPolylineManualInverse_assertion5() {
				assertEquals(line.getFirstPointIndexInGroup(4), 10);
			}

			@DisplayName("#6")
			@Test
			public void testPolylineManualInverse_assertion6() {
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(0), 0);
			}

			@DisplayName("#7")
			@Test
			public void testPolylineManualInverse_assertion7() {
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(1), 1);
			}

			@DisplayName("#8")
			@Test
			public void testPolylineManualInverse_assertion8() {
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(2), 4);
			}

			@DisplayName("#9")
			@Test
			public void testPolylineManualInverse_assertion9() {
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(3), 5);
			}

			@DisplayName("#10")
			@Test
			public void testPolylineManualInverse_assertion10() {
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(4), 9);
			}

			@DisplayName("#11")
			@Test
			public void testPolylineManualInverse_assertion11() {
				line.invert();
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(0), 0);
			}

			@DisplayName("#12")
			@Test
			public void testPolylineManualInverse_assertion12() {
				line.invert();
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(1), 2);
			}

			@DisplayName("#13")
			@Test
			public void testPolylineManualInverse_assertion13() {
				line.invert();
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(2), 6);
			}

			@DisplayName("#14")
			@Test
			public void testPolylineManualInverse_assertion14() {
				line.invert();
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(3), 7);
			}

			@DisplayName("#15")
			@Test
			public void testPolylineManualInverse_assertion15() {
				line.invert();
				line.invert();
				assertEquals(line.getFirstPointIndexInGroup(4), 10);
			}

			@DisplayName("#16")
			@Test
			public void testPolylineManualInverse_assertion16() {
				line.invert();
				line.invert();
				assertEquals(line.getPointIndex(0, pts[0]), 0);
			}

			@DisplayName("#17")
			@Test
			public void testPolylineManualInverse_assertion17() {
				line.invert();
				line.invert();
				assertEquals(line.getPointIndex(3, pts[7]), 7);
			}

			@DisplayName("#18")
			@Test
			public void testPolylineManualInverse_assertion18() {
				line.invert();
				line.invert();
				assertEquals(line.getPointIndex(3, pts[8]), 8);
			}

			@DisplayName("#19")
			@Test
			public void testPolylineManualInverse_assertion19() {
				line.invert();
				line.invert();
				assertEquals(pts[0], line.getPointAt(0));
			}

			@DisplayName("#20")
			@Test
			public void testPolylineManualInverse_assertion20() {
				line.invert();
				line.invert();
				assertEquals(pts[1], line.getPointAt(1));
			}

			@DisplayName("#21")
			@Test
			public void testPolylineManualInverse_assertion21() {
				line.invert();
				line.invert();
				assertEquals(pts[2], line.getPointAt(2));
			}

			@DisplayName("#22")
			@Test
			public void testPolylineManualInverse_assertion22() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				assertEquals(line.getPointIndex(0, pts[0]), 1);
			}

			@DisplayName("#23")
			@Test
			public void testPolylineManualInverse_assertion23() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				assertEquals(pts[0], line.getPointAt(1));
			}

			@DisplayName("#24")
			@Test
			public void testPolylineManualInverse_assertion24() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				assertEquals(pts[1], line.getPointAt(0));
			}

			@DisplayName("#25")
			@Test
			public void testPolylineManualInverse_assertion25() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				assertEquals(pts[2], line.getPointAt(2));
			}

			@DisplayName("#26")
			@Test
			public void testPolylineManualInverse_assertion26() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				assertEquals(pts[2], line.getPointAt(5));
			}

			@DisplayName("#27")
			@Test
			public void testPolylineManualInverse_assertion27() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				assertEquals(pts[3], line.getPointAt(4));
			}

			@DisplayName("#28")
			@Test
			public void testPolylineManualInverse_assertion28() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				assertEquals(pts[4], line.getPointAt(3));
			}

			@DisplayName("#29")
			@Test
			public void testPolylineManualInverse_assertion29() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				assertEquals(pts[5], line.getPointAt(2));
			}

			@DisplayName("#30")
			@Test
			public void testPolylineManualInverse_assertion30() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				assertEquals(line.getPointIndex(1, pts[2]), 5);
			}

			@DisplayName("#31")
			@Test
			public void testPolylineManualInverse_assertion31() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				line.invertPointsIn(1);
				assertEquals(line.getPointIndex(1, pts[2]), 2);
			}

			@DisplayName("#32")
			@Test
			public void testPolylineManualInverse_assertion32() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				line.invertPointsIn(1);
				assertEquals(pts[2], line.getPointAt(2));
			}

			@DisplayName("#33")
			@Test
			public void testPolylineManualInverse_assertion33() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				line.invertPointsIn(1);
				assertEquals(pts[3], line.getPointAt(3));
			}

			@DisplayName("#34")
			@Test
			public void testPolylineManualInverse_assertion34() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				line.invertPointsIn(1);
				assertEquals(pts[4], line.getPointAt(4));
			}

			@DisplayName("#35")
			@Test
			public void testPolylineManualInverse_assertion35() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				line.invertPointsIn(1);
				assertEquals(pts[5], line.getPointAt(5));
			}

			@DisplayName("#36")
			@Test
			public void testPolylineManualInverse_assertion36() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				line.invertPointsIn(1);
				assertTrue(line.containsPoint(pts[2], 1));
			}

			@DisplayName("#37")
			@Test
			public void testPolylineManualInverse_assertion37() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				line.invertPointsIn(1);
				assertTrue(line.containsPoint(pts[3], 1));
			}

			@DisplayName("#38")
			@Test
			public void testPolylineManualInverse_assertion38() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				line.invertPointsIn(1);
				assertFalse(line.containsPoint(pts[2], 2));
			}

			@DisplayName("#39")
			@Test
			public void testPolylineManualInverse_assertion39() {
				line.invert();
				line.invert();
				line.invertPointsIn(0);
				line.invertPointsIn(1);
				line.invertPointsIn(1);
				assertFalse(line.containsPoint(pts[1], 1));
			}
		}
	}

	@DisplayName("getLength")
	@Nested
	public class GetLength {

		private List<Double> points;
		private List<Integer> firstPoints;
		private MapPolyline testElement;
		private double expectedLength;

		@BeforeEach
		public void setUp() throws Exception {
			points = new ArrayList<>();
			firstPoints = new ArrayList<>();
			testElement = new MapPolyline(new HeapAttributeCollection());
			mapComposedElement_creation(0, testElement, points, null, firstPoints);

			expectedLength = 0.;

			for(int groupIndex=0; groupIndex<firstPoints.size(); ++groupIndex) {
				Point2d previousPts = null;
				int firstInGroup = firstPoints.get(groupIndex);
				int lastInGroup = (groupIndex<firstPoints.size()-1)
						? firstPoints.get(groupIndex+1) - 2
								: points.size() - 2;
						for(int ptsIndex=firstInGroup; ptsIndex<=lastInGroup; ptsIndex+=2) {
							Point2d pts = new Point2d(points.get(ptsIndex), points.get(ptsIndex+1));
							if (previousPts!=null) {
								expectedLength += previousPts.getDistance(pts);
							}
							previousPts = pts;
						}
			}
		}

		@DisplayName("#1")
		@Test
		public void testGetLength_assertion1() {
			assertEpsilonEquals(expectedLength, testElement.getLength());
		}
	}

	@DisplayName("getSubSegmentForDistance")
	@Nested
	public class GetSubSegmentForDistance {

		private List<Double> points;
		private List<Integer> firstPoints;
		private MapPolyline testElement;
		private double desiredDistance;
		private Point2d firstPt;
		private Point2d secondPt;

		@BeforeEach
		public void setUp() throws Exception {
			points = new ArrayList<>();
			firstPoints = new ArrayList<>();
			testElement = new MapPolyline(new HeapAttributeCollection());
			mapComposedElement_creation(0, testElement, points, null, firstPoints);

			desiredDistance = Math.random() * testElement.getLength();
			firstPt = null;
			secondPt = null;
			double expectedLength = 0.;

			for(int groupIndex=0; (groupIndex<firstPoints.size())&&(firstPt==null)&&(secondPt==null); ++groupIndex) {
				Point2d previousPts = null;
				int firstInGroup = firstPoints.get(groupIndex);
				int lastInGroup = (groupIndex<firstPoints.size()-1)
						? firstPoints.get(groupIndex+1) - 2
								: points.size() - 2;
						for(int ptsIndex=firstInGroup; (ptsIndex<=lastInGroup)&&(firstPt==null)&&(secondPt==null); ptsIndex+=2) {
							Point2d pts = new Point2d(points.get(ptsIndex), points.get(ptsIndex+1));
							if (previousPts!=null) {
								expectedLength += previousPts.getDistance(pts);
								if (desiredDistance<expectedLength) {
									firstPt = previousPts;
									secondPt = pts;
								}
							}
							previousPts = pts;
						}
			}
		}

		@DisplayName("#1")
		@Test
		public void testGetSubSegmentForDistance_validDistance_assertion1() {
			assert(firstPt!=null) : "the distance must lies on the segment"; //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void testGetSubSegmentForDistance_validDistance_assertion2() {
			assert(secondPt!=null) : "the distance must lies on the segment"; //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void testGetSubSegmentForDistance_validDistance_assertion3() {
			Segment1D sgmt = testElement.getSubSegmentForDistance(desiredDistance);
			assertEpsilonEquals(firstPt, sgmt.getFirstPoint());
		}

		@DisplayName("#4")
		@Test
		public void testGetSubSegmentForDistance_validDistance_assertion4() {
			Segment1D sgmt = testElement.getSubSegmentForDistance(desiredDistance);
			assertEpsilonEquals(secondPt, sgmt.getLastPoint());
		}
	}
}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.gis.road;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.io.shape.GISShapeFileReader;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadConnection.ClockwiseBoundType;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.io.dbase.DBaseFileReader;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** Unit test dedicated to
 * [counter] clockwise iterators on road segments..
 *
 * <p>This unit test is related to bug {@jira GIS-4}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class ClockwiseTest extends AbstractGisTest {

	private Rectangle2d bounds;
	private StandardRoadNetwork network;

	@BeforeEach
	public void setUp() throws Exception {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();
		URL shapeFile = ClockwiseTest.class.getResource("/org/arakhne/afc/gis/road/Export_Output_Bug26.shp"); //$NON-NLS-1$
		assertNotNull(shapeFile);
		URL dbfFile = ClockwiseTest.class.getResource("/org/arakhne/afc/gis/road/Export_Output_Bug26.dbf"); //$NON-NLS-1$
		assertNotNull(dbfFile);

		try (DBaseFileReader dbaseReader = new DBaseFileReader(dbfFile)) {
			try (GISShapeFileReader reader = new GISShapeFileReader(shapeFile, RoadPolyline.class,
										dbaseReader, dbfFile)) {
				ESRIBounds eBounds = reader.getBoundsFromHeader();
				eBounds.ensureMinMax();
				this.bounds = new Rectangle2d();
				this.bounds.setFromCorners(eBounds.getMinX(), eBounds.getMinY(), eBounds.getMaxX(), eBounds.getMaxY());
				Iterator<RoadPolyline> iterator = reader.iterator(RoadPolyline.class);
				this.network = new StandardRoadNetwork(this.bounds);
				while (iterator.hasNext()) {
					this.network.addRoadPolyline(iterator.next());
				}
			}
		}
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.bounds = null;
		this.network = null;
	}

	private static <T> void setCollection(Collection<T> col, Iterable<T> it) {
		col.clear();
		for(T t : it) {
			col.add(t);
		}
	}

	private static void setIdCollection(Collection<String> col, Iterable<RoadSegment> it) {
		col.clear();
		for(RoadSegment t : it) {
			col.add(t.getGeoId().toString());
		}
	}

	private static RoadSegment getLastClockwiseSegment(RoadConnection con, RoadSegment s) {
		RoadSegment sss = s;
		RoadSegment previous = null;
		for(RoadSegment ss : con.getConnectedSegments()) {
			if (sss!=null && sss.equals(ss)) {
				if (previous!=null) return previous;
				sss = null;
			}
			previous = ss;
		}
		return previous;
	}

	private void assertConnectionClockwise(int segmentIdx, RoadConnection con, RoadSegment source) {
		Collection<RoadSegment> originalSegments = new ArrayList<>();
		Collection<RoadSegment> actualSegments = new ArrayList<>();
		Collection<RoadSegment> expectedSegments;
		Iterator<RoadSegment> it;
		int count;
		int expectedCount;

		setCollection(originalSegments, con.getConnectedSegments());

		it = con.toClockwiseIterator(source);
		count = 0;
		expectedSegments = new ArrayList<>(originalSegments);
		actualSegments.clear();
		while (it.hasNext()) {
			actualSegments.add(it.next());
			++count;
		}
		assertEquals(con.getConnectedSegmentCount(), count, "count|DEFAULT|"+segmentIdx); //$NON-NLS-1$
		assertEpsilonEquals(expectedSegments, actualSegments, "segments|DEFAULT|"+segmentIdx); //$NON-NLS-1$

		it = con.toClockwiseIterator(source, ClockwiseBoundType.INCLUDE_BOTH);
		count = 0;
		expectedSegments = new ArrayList<>(originalSegments);
		actualSegments.clear();
		while (it.hasNext()) {
			actualSegments.add(it.next());
			++count;
		}
		assertEquals(con.getConnectedSegmentCount(), count, "count|INCLUDE_BOTH|"+segmentIdx); //$NON-NLS-1$
		assertEpsilonEquals(expectedSegments, actualSegments, "segments|INCLUDE_BOTH|"+segmentIdx); //$NON-NLS-1$

		it = con.toClockwiseIterator(source, ClockwiseBoundType.EXCLUDE_START_SEGMENT);
		count = 0;
		expectedSegments = new ArrayList<>(originalSegments);
		actualSegments.clear();
		while (it.hasNext()) {
			actualSegments.add(it.next());
			++count;
		}
		if (originalSegments.size()==1) {
			expectedCount = 1;
		}
		else {
			expectedSegments.remove(source);
			expectedCount = con.getConnectedSegmentCount()-1;
		}
		assertEquals(expectedCount, count, "count|EXCLUDE_START_SEGMENT|"+segmentIdx); //$NON-NLS-1$
		assertEpsilonEquals(expectedSegments, actualSegments, "segments|EXCLUDE_START_SEGMENT|"+segmentIdx); //$NON-NLS-1$

		it = con.toClockwiseIterator(source, ClockwiseBoundType.EXCLUDE_END_SEGMENT);
		count = 0;
		expectedSegments = new ArrayList<>(originalSegments);
		actualSegments.clear();
		while (it.hasNext()) {
			actualSegments.add(it.next());
			++count;
		}
		if (originalSegments.size()==1) {
			expectedCount = 1;
		}
		else {
			RoadSegment rr = getLastClockwiseSegment(con, source);
			assertNotNull(rr);
			expectedSegments.remove(rr);
			expectedCount = con.getConnectedSegmentCount()-1;
		}
		assertEquals(expectedCount, count, "count|EXCLUDE_END_SEGMENT|"+segmentIdx); //$NON-NLS-1$
		assertEpsilonEquals(expectedSegments, actualSegments, "segments|EXCLUDE_END_SEGMENT|"+segmentIdx); //$NON-NLS-1$

		it = con.toClockwiseIterator(source, ClockwiseBoundType.EXCLUDE_BOTH);
		count = 0;
		expectedSegments = new ArrayList<>(originalSegments);
		actualSegments.clear();
		while (it.hasNext()) {
			actualSegments.add(it.next());
			++count;
		}
		if (originalSegments.size()==1) {
			expectedSegments.remove(source);
			expectedCount = 0;
		}
		else {
			expectedSegments.remove(source);
			RoadSegment rr = getLastClockwiseSegment(con, source);
			assertNotNull(rr);
			expectedSegments.remove(rr);
			expectedCount = con.getConnectedSegmentCount()-2;
		}
		assertEquals(expectedCount, count, "count|EXCLUDE_BOTH|"+segmentIdx); //$NON-NLS-1$
		assertEpsilonEquals(expectedSegments, actualSegments, "segments|EXCLUDE_BOTH|"+segmentIdx); //$NON-NLS-1$
	}

	@Test
	public void testToClockwiseIterator() {
		int segmentIdx = 0;
		for(RoadSegment s : this.network.getRoadSegments()) {
			assertConnectionClockwise(segmentIdx, s.getBeginPoint(), s);
			assertConnectionClockwise(segmentIdx, s.getEndPoint(), s);
			++segmentIdx;
		}
	}

	private static RoadSegment getLastCounterclockwiseSegment(RoadConnection con, RoadSegment s) {
		RoadSegment first = null;
		RoadSegment sss = null;
		for(RoadSegment ss : con.getConnectedSegments()) {
			if (sss==null && s.equals(ss)) {
				sss = ss;
			}
			else if (sss!=null) {
				return ss;
			}
			else if (first==null) {
				first = ss;
			}
		}
		return first;
	}

	private void assertConnectionCounterclockwise(int segmentIdx, RoadConnection con, RoadSegment source) {
		Collection<RoadSegment> originalSegments = new ArrayList<>();
		Collection<RoadSegment> actualSegments = new ArrayList<>();
		Collection<RoadSegment> expectedSegments;
		Iterator<RoadSegment> it;
		int count;
		int expectedCount;

		setCollection(originalSegments, con.getConnectedSegments());

		it = con.toCounterclockwiseIterator(source);
		count = 0;
		expectedSegments = new ArrayList<>(originalSegments);
		actualSegments.clear();
		while (it.hasNext()) {
			actualSegments.add(it.next());
			++count;
		}
		assertEquals(con.getConnectedSegmentCount(), count, "count|DEFAULT|"+segmentIdx); //$NON-NLS-1$
		assertEpsilonEquals(expectedSegments, actualSegments, "segments|DEFAULT|"+segmentIdx); //$NON-NLS-1$

		it = con.toCounterclockwiseIterator(source, ClockwiseBoundType.INCLUDE_BOTH);
		count = 0;
		expectedSegments = new ArrayList<>(originalSegments);
		actualSegments.clear();
		while (it.hasNext()) {
			actualSegments.add(it.next());
			++count;
		}
		assertEquals(con.getConnectedSegmentCount(), count, "count|INCLUDE_BOTH|"+segmentIdx); //$NON-NLS-1$
		assertEpsilonEquals(expectedSegments, actualSegments, "segments|INCLUDE_BOTH|"+segmentIdx); //$NON-NLS-1$

		it = con.toCounterclockwiseIterator(source, ClockwiseBoundType.EXCLUDE_START_SEGMENT);
		count = 0;
		expectedSegments = new ArrayList<>(originalSegments);
		actualSegments.clear();
		while (it.hasNext()) {
			actualSegments.add(it.next());
			++count;
		}
		if (originalSegments.size()==1) {
			expectedCount = 1;
		}
		else {
			expectedSegments.remove(source);
			expectedCount = con.getConnectedSegmentCount()-1;
		}
		assertEquals(expectedCount, count, "count|EXCLUDE_START_SEGMENT|"+segmentIdx); //$NON-NLS-1$
		assertEpsilonEquals(expectedSegments, actualSegments, "segments|EXCLUDE_START_SEGMENT|"+segmentIdx); //$NON-NLS-1$

		it = con.toCounterclockwiseIterator(source, ClockwiseBoundType.EXCLUDE_END_SEGMENT);
		count = 0;
		expectedSegments = new ArrayList<>(originalSegments);
		actualSegments.clear();
		while (it.hasNext()) {
			actualSegments.add(it.next());
			++count;
		}
		if (originalSegments.size()==1) {
			expectedCount = 1;
		}
		else {
			RoadSegment rr = getLastCounterclockwiseSegment(con, source);
			assertNotNull(rr);
			expectedSegments.remove(rr);
			expectedCount = con.getConnectedSegmentCount()-1;
		}
		assertEquals(expectedCount, count, "count|EXCLUDE_END_SEGMENT|"+segmentIdx); //$NON-NLS-1$
		assertEpsilonEquals(expectedSegments, actualSegments, "segments|EXCLUDE_END_SEGMENT|"+segmentIdx); //$NON-NLS-1$

		it = con.toCounterclockwiseIterator(source, ClockwiseBoundType.EXCLUDE_BOTH);
		count = 0;
		expectedSegments = new ArrayList<>(originalSegments);
		actualSegments.clear();
		while (it.hasNext()) {
			actualSegments.add(it.next());
			++count;
		}
		if (originalSegments.size()==1) {
			expectedSegments.remove(source);
			expectedCount = 0;
		}
		else {
			expectedSegments.remove(source);
			RoadSegment rr = getLastCounterclockwiseSegment(con, source);
			assertNotNull(rr);
			expectedSegments.remove(rr);
			expectedCount = con.getConnectedSegmentCount()-2;
		}
		assertEquals(expectedCount, count, "count|EXCLUDE_BOTH|"+segmentIdx); //$NON-NLS-1$
		assertEpsilonEquals(expectedSegments, actualSegments, "segments|EXCLUDE_BOTH|"+segmentIdx); //$NON-NLS-1$
	}

	@Test
	public void testToCounterclockwiseIterator() {
		int segmentIdx = 0;
		for(RoadSegment s : this.network.getRoadSegments()) {
			assertConnectionCounterclockwise(segmentIdx, s.getBeginPoint(), s);
			assertConnectionCounterclockwise(segmentIdx, s.getEndPoint(), s);
			++segmentIdx;
		}
	}

	@Test
	public void testSpecificalCoordinates() {
		// The following cases are feeded back by Olivier

		Point2d problem1 = new Point2d(935579.5, 2311395.0);
		Point2d problem2 = new Point2d(935646.3, 2311319.3);

		String expectedSegment1 = "e74b1ed940bfbffc59a26786994ec0a8#935579;2311319;935647;2311395"; //$NON-NLS-1$
		String expectedSegment2 = "67b453c1f5f2a55c3e91aa0468fc861a#935579;2311319;935647;2311395"; //$NON-NLS-1$
		String expectedSegment3 = "42bc6f398c0552f66138fe953a9a52d8#935561;2311395;935580;2311432"; //$NON-NLS-1$
		String expectedSegment4 = "ccf6f6d64c911eec7e16dd51c3385a28#935646;2311307;935665;2311320"; //$NON-NLS-1$

		RoadConnection con = this.network.getNearestConnection(problem1);
		assertNotNull(con);
		assertEpsilonEquals(problem1, con.getPoint());

		RoadSegment segment = null;

		for(RoadSegment sgmt : con.getConnectedSegments()) {
			if (segment==null) {
				Point2d p1 = sgmt.getFirstPoint();
				Point2d p2 = sgmt.getLastPoint();
				if (problem1.epsilonEquals(p1, EPSILON) && problem2.epsilonEquals(p2, EPSILON)) {
					segment = sgmt;
				}
				else if (problem1.epsilonEquals(p2, EPSILON) && problem2.epsilonEquals(p1, EPSILON)) {
					segment = sgmt;
				}
			}
		}

		assertNotNull(segment);
		assert(segment!=null);
		assertEquals(expectedSegment1, segment.getGeoId().toString());

		// CONNECTIONS
		Collection<String> segmentList = new ArrayList<>();
		setIdCollection(segmentList, segment.getBeginPoint().getConnectedSegments());
		assertEpsilonEquals(
				Arrays.asList(expectedSegment1, expectedSegment2, expectedSegment3),
				segmentList);
		setIdCollection(segmentList, segment.getEndPoint().getConnectedSegments());
		assertEpsilonEquals(
				Arrays.asList(expectedSegment1, expectedSegment2, expectedSegment4),
				segmentList);

		RoadSegment s;
		Iterator<RoadSegment> it;

		// START CONNECTION
		// Clockwise
		// Include first and last segments
		it = segment.getBeginPoint().toClockwiseIterator(segment, ClockwiseBoundType.INCLUDE_BOTH);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment1, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment3, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// START CONNECTION
		// Clockwise
		// Exclude first segment only
		it = segment.getBeginPoint().toClockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_START_SEGMENT);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment3, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// START CONNECTION
		// Clockwise
		// Exclude last segment only
		it = segment.getBeginPoint().toClockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_END_SEGMENT);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment1, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// START CONNECTION
		// Clockwise
		// Exclude first and last segments
		it = segment.getBeginPoint().toClockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_BOTH);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// START CONNECTION
		// Counterclockwise
		// Include first and last segments
		it = segment.getBeginPoint().toCounterclockwiseIterator(segment, ClockwiseBoundType.INCLUDE_BOTH);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment1, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment3, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// START CONNECTION
		// Counterclockwise
		// Exclude first segment only
		it = segment.getBeginPoint().toCounterclockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_START_SEGMENT);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment3, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// START CONNECTION
		// Counterclockwise
		// Exclude last segment only
		it = segment.getBeginPoint().toCounterclockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_END_SEGMENT);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment1, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment3, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// START CONNECTION
		// Counterclockwise
		// Exclude first and last segments
		it = segment.getBeginPoint().toCounterclockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_BOTH);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment3, s.getGeoId().toString());
		assertFalse(it.hasNext());

		//
		//-------------------------------------------
		//

		// END CONNECTION
		// Clockwise
		// Include first and last segments
		it = segment.getEndPoint().toClockwiseIterator(segment, ClockwiseBoundType.INCLUDE_BOTH);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment1, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment4, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// END CONNECTION
		// Clockwise
		// Exclude first segment only
		it = segment.getEndPoint().toClockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_START_SEGMENT);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment4, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// END CONNECTION
		// Clockwise
		// Exclude last segment only
		it = segment.getEndPoint().toClockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_END_SEGMENT);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment1, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment4, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// END CONNECTION
		// Clockwise
		// Exclude first and last segments
		it = segment.getEndPoint().toClockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_BOTH);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment4, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// END CONNECTION
		// Counterclockwise
		// Include first and last segments
		it = segment.getEndPoint().toCounterclockwiseIterator(segment, ClockwiseBoundType.INCLUDE_BOTH);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment1, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment4, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// END CONNECTION
		// Counterclockwise
		// Exclude first segment only
		it = segment.getEndPoint().toCounterclockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_START_SEGMENT);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment4, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// END CONNECTION
		// Counterclockwise
		// Exclude last segment only
		it = segment.getEndPoint().toCounterclockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_END_SEGMENT);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment1, s.getGeoId().toString());
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertFalse(it.hasNext());

		// END CONNECTION
		// Counterclockwise
		// Exclude first and last segments
		it = segment.getEndPoint().toCounterclockwiseIterator(segment, ClockwiseBoundType.EXCLUDE_BOTH);
		assertTrue(it.hasNext());
		assertNotNull(s = it.next());
		assertEquals(expectedSegment2, s.getGeoId().toString());
		assertFalse(it.hasNext());
	}

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.gis.mapelement;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/** Unit tests for MapPolyline.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class MapPolylineTest extends AbstractMapComposedElementTest {

	/**
	 * @throws Exception
	 */
	public static void testMapPolyline() throws Exception {
		for(int i=0; i<TEST_COUNT; ++i) mapComposedElement(i,new MapPolyline(new HeapAttributeCollection()));
	}


	/**
	 * @throws Exception
	 */
	public static void testPolylineManualInverse2() throws Exception {
		Point2d[] pts = new Point2d[] {
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

		MapPolyline line = new MapPolyline();
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

		assertEquals(line.getFirstPointIndexInGroup(0), 0);
		assertTrue(line.getGroupCount() == 1 );

		line.invert();

		assertEquals(line.getFirstPointIndexInGroup(0), 0);
		assertEquals(line.getPointIndex(0, pts[10]), 0);
		assertEquals(line.getPointIndex(0, pts[0]), 10);


	}

	/**
	 * @throws Exception
	 */
	public static void testPolylineManualInverse() throws Exception {
		Point2d[] pts = new Point2d[] {
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

		MapPolyline line = new MapPolyline();
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

		assertEquals(line.getFirstPointIndexInGroup(0), 0);
		assertEquals(line.getFirstPointIndexInGroup(1), 2);
		assertEquals(line.getFirstPointIndexInGroup(2), 6);
		assertEquals(line.getFirstPointIndexInGroup(3), 7);
		assertEquals(line.getFirstPointIndexInGroup(4), 10);

		line.invert();

		assertEquals(line.getFirstPointIndexInGroup(0), 0);
		assertEquals(line.getFirstPointIndexInGroup(1), 1);
		assertEquals(line.getFirstPointIndexInGroup(2), 4);
		assertEquals(line.getFirstPointIndexInGroup(3), 5);
		assertEquals(line.getFirstPointIndexInGroup(4), 9);

		line.invert();

		assertEquals(line.getFirstPointIndexInGroup(0), 0);
		assertEquals(line.getFirstPointIndexInGroup(1), 2);
		assertEquals(line.getFirstPointIndexInGroup(2), 6);
		assertEquals(line.getFirstPointIndexInGroup(3), 7);
		assertEquals(line.getFirstPointIndexInGroup(4), 10);

		assertEquals(line.getPointIndex(0, pts[0]), 0);
		assertEquals(line.getPointIndex(3, pts[7]), 7);
		assertEquals(line.getPointIndex(3, pts[8]), 8);

		//index 0
		assertEquals(pts[0], line.getPointAt(0));
		assertEquals(pts[1], line.getPointAt(1));
		//index 1
		assertEquals(pts[2], line.getPointAt(2));

		line.invertPointsIn(0);

		assertEquals(line.getPointIndex(0, pts[0]), 1);

		//index 0
		assertEquals(pts[0], line.getPointAt(1));
		assertEquals(pts[1], line.getPointAt(0));
		//index 1
		assertEquals(pts[2], line.getPointAt(2));

		line.invertPointsIn(1);

		//index 1
		assertEquals(pts[2], line.getPointAt(5));
		assertEquals(pts[3], line.getPointAt(4));
		assertEquals(pts[4], line.getPointAt(3));
		assertEquals(pts[5], line.getPointAt(2));

		assertEquals(line.getPointIndex(1, pts[2]), 5);

		line.invertPointsIn(1);

		assertEquals(line.getPointIndex(1, pts[2]), 2);

		//index 1
		assertEquals(pts[2], line.getPointAt(2));
		assertEquals(pts[3], line.getPointAt(3));
		assertEquals(pts[4], line.getPointAt(4));
		assertEquals(pts[5], line.getPointAt(5));

		assertTrue(line.containsPoint(pts[2], 1));
		assertTrue(line.containsPoint(pts[3], 1));

		assertFalse(line.containsPoint(pts[2], 2));
		assertFalse(line.containsPoint(pts[1], 1));
	}

    /**
     * @throws Exception
     */
    public void testGetLength() throws Exception {
    	List<Double> points = new ArrayList<>();
    	List<Integer> firstPoints = new ArrayList<>();
    	MapPolyline testElement = new MapPolyline(new HeapAttributeCollection());
    	mapComposedElement_creation(0, testElement, points, null, firstPoints);

    	double expectedLength = 0.;

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

    	assertEpsilonEquals(expectedLength, testElement.getLength());
    }

    /**
     * @throws Exception
     */
    public void testGetSubSegmentForDistance_validDistance() throws Exception {
    	List<Double> points = new ArrayList<>();
    	List<Integer> firstPoints = new ArrayList<>();
    	MapPolyline testElement = new MapPolyline(new HeapAttributeCollection());
    	mapComposedElement_creation(0, testElement, points, null, firstPoints);

    	double desiredDistance = Math.random() * testElement.getLength();
    	Point2d firstPt = null;
    	Point2d secondPt = null;
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

    	assert(firstPt!=null) : "the distance must lies on the segment"; //$NON-NLS-1$
    	assert(secondPt!=null) : "the distance must lies on the segment"; //$NON-NLS-1$

    	Segment1D sgmt = testElement.getSubSegmentForDistance(desiredDistance);
    	assertEpsilonEquals(firstPt, sgmt.getFirstPoint());
    	assertEpsilonEquals(secondPt, sgmt.getLastPoint());
    }

}

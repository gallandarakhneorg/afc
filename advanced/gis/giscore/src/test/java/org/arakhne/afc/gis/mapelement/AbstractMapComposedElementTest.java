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

package org.arakhne.afc.gis.mapelement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arakhne.afc.gis.AbstractGisTest;
import org.arakhne.afc.gis.mapelement.MapComposedElement;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/** Abstract unit test for all composed map elements.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public abstract class AbstractMapComposedElementTest extends AbstractGisTest {

	/**
	 * Count of tests
	 */
	protected static final int TEST_COUNT = 50;

	/**
	 * Max count of parts in a map element.
	 */
	protected static final int MAX_PART_COUNT = 50;

	/**
	 * Max count of points in a part.
	 */
	protected static final int MAX_PART_POINT_COUNT = 5000;

	/**
	 * Insertion probability
	 */
	protected static final double INSERTION_PROBABILITY = .5;

	private static double pp=0;

	/**
	 */
	public AbstractMapComposedElementTest() {
		//
	}

	private static Point2d getPoint() {
		return new Point2d(pp,pp++);
	}

	private static void checkInternalStructures(MapComposedElement element) {
		int ptsCount = element.getPointCount();
		int groupCount = element.getGroupCount();
		for (int idxGrp=0; idxGrp<groupCount; ++idxGrp) {
			int idx = element.getFirstPointIndexInGroup(idxGrp);
			assertTrue(idx>=0);
			assertTrue(idx<ptsCount);
		}
		if (groupCount>0) {
			for(int i=0; i<groupCount; ++i) {
				int elementCount = element.getPointCountInGroup(i);
				assertTrue(((elementCount==0)&&(i==0))||(elementCount>0));
				if (elementCount>0) {
					int firstIdx = element.getFirstPointIndexInGroup(i);
					int lastIdx = element.getLastPointIndexInGroup(i);
					assertTrue(firstIdx>=0);
					assertTrue(firstIdx<ptsCount);
					assertTrue(lastIdx>=0);
					assertTrue(lastIdx<ptsCount);
					assertTrue(firstIdx<=lastIdx);
				}
			}
		}
	}

	/** Created and check the content of a MapComposedElement.
	 *
	 * @param testStep
	 * @param element
	 * @param referenceCoordinates
	 * @param creationPoints
	 * @param firstGroupPoints
	 * @throws Exception
	 */
	protected static void mapComposedElement_creation(int testStep, MapComposedElement element, List<Double> referenceCoordinates, Map<Point2d,String> creationPoints, List<Integer> firstGroupPoints) throws Exception {
		element.clear();
		if (referenceCoordinates!=null) referenceCoordinates.clear();
		if (creationPoints!=null) creationPoints.clear();
		pp = 0;

		// Test creation
		final int maxPartCount = Math.max(1,(int)(Math.random() * MAX_PART_COUNT * 2.) % (MAX_PART_COUNT+1));

		for(int i=0; i<maxPartCount; ++i) {
			Point2d p = getPoint();
			element.addGroup(p);
			checkInternalStructures(element);

			if (referenceCoordinates!=null) {
				referenceCoordinates.add(p.getX());
				referenceCoordinates.add(p.getY());
				if (firstGroupPoints!=null) firstGroupPoints.add(referenceCoordinates.size()-2);
			}
			if (creationPoints!=null) creationPoints.put(p,"addGroup"); //$NON-NLS-1$
		}

		final int maxPointCount = Math.max(1,(int)(Math.random() * MAX_PART_POINT_COUNT * 2) % (MAX_PART_POINT_COUNT+1));
		//final int totalPointCount = maxPointCount+maxPartCount;

		final int maxInsertionPoint = (int)(Math.random() * maxPointCount * 2) % (maxPointCount+1);

		int pointIndex;
		for(int idxPts=0; idxPts<maxInsertionPoint; ++idxPts) {
			Point2d p = getPoint();
			int insertionPart = (int)(Math.random() * (maxPartCount+1)) % maxPartCount;

			if (Math.random() < INSERTION_PROBABILITY) {
				int limit = element.getPointCountInGroup(insertionPart) + 1;
				int insertionPoint = (int)(Math.random() * limit) % limit;

				int pointIndex2 = element.insertPointAt(p, insertionPart, insertionPoint);
				checkInternalStructures(element);

				pointIndex = element.getPointIndex(insertionPart, insertionPoint);

				assertEquals(pointIndex,pointIndex2);

				if (referenceCoordinates!=null) {
						referenceCoordinates.add(pointIndex*2,p.getX());
						referenceCoordinates.add(pointIndex*2+1,p.getY());
						if (firstGroupPoints!=null) {
							int first = firstGroupPoints.get(insertionPart);
							if (first>pointIndex*2) firstGroupPoints.set(insertionPart, pointIndex*2);
							for(int idx=insertionPart+1; idx<firstGroupPoints.size(); ++idx) {
								first = firstGroupPoints.get(idx);
								firstGroupPoints.set(idx, first+2);
							}
						}
				}
				if (creationPoints!=null) creationPoints.put(p,"insertPointAt(p,"+insertionPart+","+insertionPoint+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			else {
				pointIndex = element.addPoint(p, insertionPart);
				checkInternalStructures(element);

				int pointIndex2 = element.getLastPointIndexInGroup(insertionPart);

				assertEquals(pointIndex,pointIndex2);

				if (referenceCoordinates!=null) {
					referenceCoordinates.add(pointIndex*2,p.getX());
					referenceCoordinates.add(pointIndex*2+1,p.getY());
					if (firstGroupPoints!=null) {
						int first = firstGroupPoints.get(insertionPart);
						if (first>pointIndex*2) firstGroupPoints.set(insertionPart, pointIndex*2);
						for(int idx=insertionPart+1; idx<firstGroupPoints.size(); ++idx) {
							first = firstGroupPoints.get(idx);
							firstGroupPoints.set(idx, first+2);
						}
					}
				}
				if (creationPoints!=null) creationPoints.put(p,"addPoint(p,"+insertionPart+")"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		for(int idxPts=0; idxPts<(maxPointCount-maxInsertionPoint); ++idxPts) {
			Point2d p = getPoint();

			int pointIndex2 = element.addPoint(p);
			checkInternalStructures(element);

			if (referenceCoordinates!=null) {
				assertEquals(referenceCoordinates.size()/2,pointIndex2);

				referenceCoordinates.add(p.getX());
				referenceCoordinates.add(p.getY());
			}
			if (creationPoints!=null) creationPoints.put(p, "addPoint(p)"); //$NON-NLS-1$
		}
	}

	private static void mapComposedElement_checkPoints(MapComposedElement element, List<Double> referenceCoordinates, Map<Point2d,String> creationPoints) throws Exception {
		checkInternalStructures(element);
		// Test if the list of the point is the same
		int idxPts = 0;
		Point2d refPt;
		for(Point2d pt: element.points()) {
			refPt = new Point2d(
					referenceCoordinates.get(idxPts*2),
					referenceCoordinates.get(idxPts*2+1));
			if (!pt.equals(refPt)) {
				int count = element.getPointCount();
				for(int i=0; i<count; ++i) {
					/*Point2d sp = */element.getPointAt(i);
				}
				/*String creationLocation = */creationPoints.get(pt);
				/*creationLocation = */creationPoints.get(refPt);
				fail("Reference points are not the same as the element's points"); //$NON-NLS-1$
			}
			idxPts ++;
		}
	}

	private static void mapComposedElement_deletion(MapComposedElement element, List<Double> referenceCoordinates) throws Exception {
		// Remove several points
		int ptsToRemove = Math.max(1,(int)(Math.random() * element.getPointCount() * 2) % (element.getPointCount()+1));
		for(int i=0; i<ptsToRemove; ++i) {
			int groupCount = element.getGroupCount();
			assertTrue(groupCount>=0);

			if (groupCount>0) {
				int idxGroup = (int)(Math.random() * groupCount * 2) % groupCount;
				assertTrue(idxGroup>=0);
				assertTrue(idxGroup<groupCount);

				int elementCount = element.getPointCountInGroup(idxGroup);
				assertTrue(elementCount>=0);

				if (elementCount>0) {
					int idxPts = (int)(Math.random() * elementCount * 2) % elementCount;

					assertTrue(idxPts>=0);
					assertTrue(idxPts<elementCount);

					int idxToRemove = element.getPointIndex(idxGroup, idxPts) * 2;

					element.removePointAt(idxGroup, idxPts);
					checkInternalStructures(element);

					referenceCoordinates.remove(idxToRemove+1);
					referenceCoordinates.remove(idxToRemove);
				}
			}
		}
		//Remove several groups
		int grpToRemove = Math.max(1,(int)(Math.random() * element.getGroupCount() * 2) % (element.getGroupCount()+1));
		for(int i=0; i<grpToRemove; ++i) {
			int groupCount = element.getGroupCount();
			assertTrue(groupCount>=0);

			if (groupCount>0) {
				int idxGroup = (int)(Math.random() * groupCount * 2) % groupCount;

				int firstIndex = element.getFirstPointIndexInGroup(idxGroup) * 2;
				int lastIndex = element.getLastPointIndexInGroup(idxGroup) * 2 + 1;

				element.removeGroupAt(idxGroup);
				checkInternalStructures(element);

				for(int idxToRemove=lastIndex; idxToRemove>=firstIndex; --idxToRemove) {
					referenceCoordinates.remove(idxToRemove);
				}
			}
		}
	}

	/**
	 * @param testStep
	 * @param element
	 * @throws Exception
	 */
	protected static void mapComposedElement(int testStep, MapComposedElement element) throws Exception {
		List<Double> referenceCoordinates = new ArrayList<>();
		Map<Point2d,String> creationPoints = new HashMap<>();

		// Creation Test
		mapComposedElement_creation(testStep, element,referenceCoordinates,creationPoints,null);
		mapComposedElement_checkPoints(element,referenceCoordinates,creationPoints);

		// Deletion Test
		mapComposedElement_deletion(element,referenceCoordinates);
		mapComposedElement_checkPoints(element,referenceCoordinates,creationPoints);
	}

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.gis.road.path;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;

/**
 * This class describes a cross road or a cul-de-sac in
 * a road path.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:visibilitymodifier")
public class CrossRoad implements Comparable<CrossRoad> {

	/** This is the cross-road point.
	 */
	public final RoadConnection connectionPoint;

	/** This is the distance along the path to the cross-road.
	 */
	public final double distance;

	/** This is the distance along the path to the cross-road from the previous
	 * croos road in the path.
	 */
	public final double distanceFromPreviousCrossRoad;

	/** This is the segment that permits to reach the cross-road.
	 */
	public final RoadSegment enteringSegment;

	/** This is the segment that permits to quit the cross-road.
	 */
	public final RoadSegment exitingSegment;

	/** This is the position in the path of the segment that permits to reach the cross-road.
	 */
	public final int enteringSegmentIndex;

	/** This is the position in the path of the segment that permits to quit the cross-road.
	 */
	public final int exitingSegmentIndex;

	/** Constructor.
	 * @param connectionPoint is the connection of the cross road
	 * @param enteringSegment is the enter segment on the cross road
	 * @param enteringIndex is the position of the entering segment in the path.
	 * @param exitingSegment is the exit segment from the cross road
	 * @param exitingIndex is the position of the exiting segment in the path.
	 * @param distance is the distance to the cross road.
	 * @param distancePrev is the distance to the cross road from the previous one.
	 */
	CrossRoad(RoadConnection connectionPoint, RoadSegment enteringSegment, int enteringIndex,
			RoadSegment exitingSegment, int exitingIndex, double distance, double distancePrev) {
		this.connectionPoint = connectionPoint;
		this.distance = distance;
		this.enteringSegment = enteringSegment;
		this.exitingSegment = exitingSegment;
		this.distanceFromPreviousCrossRoad = distancePrev;
		this.enteringSegmentIndex = enteringSegment == null ? -1 : (enteringIndex < 0 ? exitingIndex - 1 : enteringIndex);
		this.exitingSegmentIndex = exitingSegment == null ? -1 : (exitingIndex < 0 ? enteringIndex + 1 : exitingIndex);
	}

	@Override
	@Pure
	public int compareTo(CrossRoad other) {
		if (other == null) {
			return -hashCode();
		}
		return other.hashCode() - hashCode();
	}

}

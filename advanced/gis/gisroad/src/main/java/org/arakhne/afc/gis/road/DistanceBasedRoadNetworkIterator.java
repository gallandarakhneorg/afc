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

package org.arakhne.afc.gis.road;

import java.util.Comparator;

import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.gis.road.primitive.RoadSegmentContainer;
import org.arakhne.afc.math.graph.DepthGraphIterator;
import org.arakhne.afc.math.graph.DynamicDepthUpdater;
import org.arakhne.afc.math.graph.GraphIterationElement;


/**
 * This class is an iterator on the segments of a road network
 * limited to a specified depth.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
final class DistanceBasedRoadNetworkIterator extends DepthGraphIterator<RoadSegment, RoadConnection> {

	/** Constructor.
	 * @param network is the road network to iterator on.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param position_from_starting_point is the starting position from
	 *     the <var>starting_point</var> (in meters).
	 * @param segment is the segment from which to start.
	 * @param starting_point is the segment's point indicating the direction.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times
	 *     the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments indicates if the iterator is taking into account
	 *     the orientation of the road segments. If <code>true</code> it assumes that
	 *     a segment could be reached by both its end points. If <code>false</code> it
	 *     assumes that a segment could be reach only one time.
	 * @deprecated since 16.0,
	 *     see {@link #DistanceBasedRoadNetworkIterator(RoadSegmentContainer, double, double, RoadSegment,
	 *     RoadConnection, boolean, boolean, DynamicDepthUpdater)}.
	 */
	@Deprecated
	DistanceBasedRoadNetworkIterator(
			RoadSegmentContainer network,
			double depth,
			double position_from_starting_point,
			RoadSegment segment,
			RoadConnection starting_point,
			boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		super(network, depth, position_from_starting_point, segment, starting_point,
				allowManyReplies, assumeOrientedSegments);

	}

	/** Constructor.
	 * @param network is the road network to iterator on.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param position_from_starting_point is the starting position from
	 *     the <var>starting_point</var> (in meters).
	 * @param segment is the segment from which to start.
	 * @param starting_point is the segment's point indicating the direction.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times
	 *     the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments indicates if the iterator is taking into account
	 *     the orientation of the road segments. If <code>true</code> it assumes that
	 *     a segment could be reached by both its end points. If <code>false</code> it
	 *     assumes that a segment could be reach only one time.
	 * @param dynamicDepthUpdate if not {@code null}, it is used to dynamically update the {@code depth}.
	 * @since 16.0
	 */
	DistanceBasedRoadNetworkIterator(
			RoadSegmentContainer network,
			double depth,
			double position_from_starting_point,
			RoadSegment segment,
			RoadConnection starting_point,
			boolean allowManyReplies,
			boolean assumeOrientedSegments,
			DynamicDepthUpdater<RoadSegment, RoadConnection> dynamicDepthUpdate) {
		super(network, depth, position_from_starting_point, segment, starting_point,
				allowManyReplies, assumeOrientedSegments, dynamicDepthUpdate);

	}

	@Override
	protected Comparator<GraphIterationElement<RoadSegment, RoadConnection>> createVisitedSegmentComparator(
			boolean assumeOrientedSegments) {
		return assumeOrientedSegments ? RoadSegmentIterationComparator.ORIENTED_SEGMENT_SINGLETON
				: RoadSegmentIterationComparator.NOT_ORIENTED_SEGMENT_SINGLETON;
	}

}

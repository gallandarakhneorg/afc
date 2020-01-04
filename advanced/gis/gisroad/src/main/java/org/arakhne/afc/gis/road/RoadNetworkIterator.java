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

package org.arakhne.afc.gis.road;

import java.util.Comparator;

import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.gis.road.primitive.RoadSegmentContainer;
import org.arakhne.afc.math.graph.BreadthFirstGraphCourseModel;
import org.arakhne.afc.math.graph.GraphIterationElement;
import org.arakhne.afc.math.graph.GraphIterator;

/**
 * This class is an iterator on the segments of a road network.
 *
 * <p>This iterator uses a {@link BreadthFirstGraphCourseModel shortest path course model}.
 * It means that the order of the replies graph elements depending of the
 * length of the path to pass through them. The nearest segment leaving point (from the starting point)
 * will cause the corresponding segments to be replied before.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
final class RoadNetworkIterator extends GraphIterator<RoadSegment, RoadConnection> {

	/** Constructor.
	 * @param network is the road network to iterator on.
	 * @param segment is the segment from which to start.
	 * @param starting_point is the segment's point indicating the direction.
	 * @param allowManyReplies must be <code>true</code> to allow cycles, otherwise <code>false</code>.
	 * @param assumeOrientedSegments indicates if the iterator is taking into account
	 *     the orientation of the road segments. If <code>true</code> it assumes that
	 *     a segment could be reached by both its end points. If <code>false</code> it
	 *     assumes that a segment could be reach only one time. This parameter is used only
	 *     when <var>allowManyReplies</var> was set to <code>true</code>.
	 * @param distanceToReachStartingPoint is the distance to reach the starting point.
	 *     It must be negative or nul.
	 */
	RoadNetworkIterator(RoadSegmentContainer network, RoadSegment segment, RoadConnection starting_point,
			boolean allowManyReplies, boolean assumeOrientedSegments,
			double distanceToReachStartingPoint) {
		super(network,
				new BreadthFirstGraphCourseModel<RoadSegment, RoadConnection>(),
				segment, starting_point,
				allowManyReplies, assumeOrientedSegments,
				distanceToReachStartingPoint);
	}

	@Override
	protected Comparator<GraphIterationElement<RoadSegment, RoadConnection>> createVisitedSegmentComparator(
			boolean assumeOrientedSegments) {
		return assumeOrientedSegments ? RoadSegmentIterationComparator.ORIENTED_SEGMENT_SINGLETON
			: RoadSegmentIterationComparator.NOT_ORIENTED_SEGMENT_SINGLETON;
	}

}

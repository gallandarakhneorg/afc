/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.gis.road.primitive;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.graph.DynamicDepthUpdater;
import org.arakhne.afc.math.graph.Graph;
import org.arakhne.afc.math.graph.GraphIterator;

/**
 * This interface describes a road segment container.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface RoadSegmentContainer extends Graph<RoadSegment, RoadConnection> {

	/** Replies if this road network uses a left-side circulation direction rule.
	 *
	 * <p>When left-side circulation direction rule is used, it is supposed that all
	 * vehicles are going on the left side of the roads. For example,
	 * this rule is used in UK.
	 *
	 * @return <code>true</code> if the left-side rule is used on this network,
	 *     otherwise <code>false</code>.
	 */
	@Pure
	boolean isLeftSidedTrafficDirection();

	/** Replies if this road network uses a right-side circulation direction rule.
	 *
	 * <p>When right-side circulation direction rule is used, it is supposed that all
	 * vehicles are going on the right side of the roads. For example,
	 * this rule is used in France.
	 *
	 * @return <code>true</code> if the right-side rule is used on this network,
	 *     otherwise <code>false</code>.
	 */
	@Pure
	boolean isRightSidedTrafficDirection();

	/**
	 * Replies if the given road segment is inside this road network.
	 *
	 * @param segment a segment.
	 * @return <code>true</code> if the segment is inside the road network,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean contains(RoadSegment segment);

	/**
	 * Replies the road segment with the given identifier.
	 *
	 * <p>This function is time consuming because the location
	 * of the road segment could not be retrieved from
	 * the geoId.
	 *
	 * @param geoId an identifier.
	 * @return the road segment or <code>null</code> if not found.
	 */
	@Pure
	RoadSegment getRoadSegment(GeoId geoId);

	/**
	 * Replies the road segment with the given location.
	 *
	 * @param location a location
	 * @return the road segment or <code>null</code> if not found.
	 */
	@Pure
	RoadSegment getRoadSegment(GeoLocation location);

	//-------------------------------------------------
	// Iterator functions
	//-------------------------------------------------

	@Override
	@Pure
	default GraphIterator<RoadSegment, RoadConnection> depthIterator(
			RoadSegment startingSegment, double depth,
			double positionFromStartingPoint, RoadConnection startingPoint,
			boolean allowManyReplies, boolean assumeOrientedSegments) {
		return depthIterator(startingSegment, depth, positionFromStartingPoint, startingPoint,
				allowManyReplies, assumeOrientedSegments, null);
	}

	@Override
	@Pure
	GraphIterator<RoadSegment, RoadConnection> depthIterator(
			RoadSegment startingSegment, double depth,
			double positionFromStartingPoint, RoadConnection startingPoint,
			boolean allowManyReplies, boolean assumeOrientedSegments,
			DynamicDepthUpdater<RoadSegment, RoadConnection> dynamicDepthUpdater);

	@Override
	@Pure
	GraphIterator<RoadSegment, RoadConnection> iterator(
			RoadSegment starting_segment, RoadConnection starting_point,
			boolean allowManyReplies, boolean assumeOrientedSegments);

	/** Iterates on the segments that intersect the specified bounds.
	 *
	 * @param bounds is the rectangle inside which the replied elements must be located
	 * @return an iterator.
	 */
	@Pure
	Iterator<? extends RoadSegment> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds);

	/** Iterates on the segments that intersect the specified bounds.
	 *
	 * @param bounds is the rectangle inside which the replied elements must be located
	 * @param budget is the maximal count of elements which will be replied by the iterator.
	 * @return an iterator.
	 */
	@Pure
	Iterator<? extends RoadSegment> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, int budget);

	/** Iterates on the segments that intersect the specified bounds.
	 *
	 * @param bounds is the rectangle inside which the replied elements must be located
	 * @return an iterator.
	 */
	@Pure
	Iterable<? extends RoadSegment> toIterable(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds);

	/** Iterates on the segments that intersect the specified bounds.
	 *
	 * @param bounds is the rectangle inside which the replied elements must be located
	 * @param budget is the maximal count of elements which will be replied by the iterator.
	 * @return an iterator.
	 */
	@Pure
	Iterable<? extends RoadSegment> toIterable(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, int budget);

}

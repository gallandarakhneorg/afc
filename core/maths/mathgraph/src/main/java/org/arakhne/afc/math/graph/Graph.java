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

package org.arakhne.afc.math.graph;

import org.eclipse.xtext.xbase.lib.Pure;

/** This interface representes a graph.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Graph<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>>
		extends Iterable<ST> {

	/** Replies the count of segments in this graph.
	 *
	 * @return the count of segments in this graph.
	 */
	@Pure
	int getSegmentCount();

	/** Replies the count of points in this graph.
	 *
	 * @return the count of points in this graph.
	 */
	@Pure
	int getPointCount();

	/** Replies if this graph is empty or not.
	 *
	 * @return <code>true</code> if the graph is empty,
	 *     otherwise <code>false</code>.
	 */
	@Pure
	boolean isEmpty();

	/** Replies is this graph contains the given segment.
	 * The test is based on {@link Object#equals(Object)}.
	 *
	 * @param obj the object to search for.
	 * @return <code>true</code> if the graph contains the segment,
	 *     otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(Object obj);

	/** Replies an iterator that permits to move along the road segment's graph
	 * starting from this road segment and from the specified starting point.
	 *
	 * @param startingSegment is the first segment to iterate.
	 * @param startingPoint is the starting point of the iterations.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same
	 *     segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 * @return the iterator.
	 */
	@Pure
	GraphIterator<ST, PT> iterator(ST startingSegment, PT startingPoint, boolean allowManyReplies,
			boolean assumeOrientedSegments);

	/** Replies an iterator that permits to move along the segment's graph
	 * starting from the specified segment and from the specified starting point.
	 * If the specified starting point is not one of the ends of the segment,
	 * this function assumes to start from the point replied by
	 * {@link GraphSegment#getBeginPoint()}.
	 *
	 * <p>This function does not allow the cycles during the iterations.
	 *
	 * @param startingSegment is the first segment to iterate.
	 * @param depth is the maximal depth to reach.
	 * @param positionFromStartingPoint is the starting position from
	 *     the {@code startingPoint}.
	 * @param startingPoint is the starting point of the iterations.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same
	 *     segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 * @return the iterator.
	 * @see #depthIterator(GraphSegment, double, double, GraphPoint, boolean, boolean, DynamicDepthUpdater)
	 */
	@Pure
	default GraphIterator<ST, PT> depthIterator(ST startingSegment, double depth, double positionFromStartingPoint,
			PT startingPoint, boolean allowManyReplies, boolean assumeOrientedSegments) {
		return depthIterator(startingSegment, depth, positionFromStartingPoint, startingPoint,
				allowManyReplies, assumeOrientedSegments, null);
	}

	/** Replies an iterator that permits to move along the segment's graph
	 * starting from the specified segment and from the specified starting point.
	 * If the specified starting point is not one of the ends of the segment,
	 * this function assumes to start from the point replied by
	 * {@link GraphSegment#getBeginPoint()}.
	 *
	 * <p>This function does not allow the cycles during the iterations.
	 *
	 * @param startingSegment is the first segment to iterate.
	 * @param depth is the maximal depth to reach.
	 * @param positionFromStartingPoint is the starting position from
	 *     the {@code startingPoint}.
	 * @param startingPoint is the starting point of the iterations.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same
	 *     segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 * @param dynamicDepthUpdater if not {@code null}, it is a lambda that is used for dynamically updating the maximal depth.
	 * @return the iterator.
	 * @see #depthIterator(GraphSegment, double, double, GraphPoint, boolean, boolean)
	 * @since 16.0
	 */
	@Pure
	GraphIterator<ST, PT> depthIterator(ST startingSegment, double depth, double positionFromStartingPoint,
			PT startingPoint, boolean allowManyReplies, boolean assumeOrientedSegments,
			DynamicDepthUpdater<ST, PT> dynamicDepthUpdater);

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

/**
 * This class is an iterator on the segments
 * limited to a specified depth.
 *
 * <p>The behaviour of the iterator is strongly influenced by the constructor's parameters.
 * One of the most important parameter is {@code assumeOrientedSegments}.
 * The {@code assumeOrientedSegments} parameter indicates how the segments are considered
 * by the iterator. If {@code assumeOrientedSegments} is {@code true} it means
 * that a segment reached by one of its end point is different than the same segment reached
 * by the other end point. If {@code assumeOrientedSegments} is {@code false} it means
 * that the end points of the segments are not take into account.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class DepthGraphIterator<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>>
		extends GraphIterator<ST, PT> {

	/** Constructor.
	 * @param graph is the graph associated to this iterator.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param positionFromStartingPoint is the starting position from
	 *     the {@code starting_point} (in meters).
	 * @param segment is the segment from which to start.
	 * @param startingPoint is the segment's point indicating the direction.
	 * @param allowManyReplies may be {@code true} to allow to reply many times the same
	 *     segment, otherwhise {@code false}.
	 * @param assumeOrientedSegments may be {@code true} to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is {@code false} to assume that
	 *     the end points of a segment are not distinguished.
	 * @deprecated since 16.0
	 */
	@Deprecated(since = "16.0", forRemoval = true)
	public DepthGraphIterator(
			Graph<ST, PT> graph,
			double depth,
			double positionFromStartingPoint,
			ST segment,
			PT startingPoint,
			boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		this(graph, depth, segment, startingPoint, allowManyReplies,
				assumeOrientedSegments,
				-getStartingDistance(positionFromStartingPoint, segment), null);
	}

	/** Constructor.
	 * @param graph is the graph associated to this iterator.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param positionFromStartingPoint is the starting position from
	 *     the {@code starting_point} (in meters).
	 * @param segment is the segment from which to start.
	 * @param startingPoint is the segment's point indicating the direction.
	 * @param allowManyReplies may be {@code true} to allow to reply many times the same
	 *     segment, otherwhise {@code false}.
	 * @param assumeOrientedSegments may be {@code true} to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is {@code false} to assume that
	 *     the end points of a segment are not distinguished.
	 * @param dynamicDepthUpdater if not {@code null}, it is used to dynamically update the depth.
	 */
	public DepthGraphIterator(
			Graph<ST, PT> graph,
			double depth,
			double positionFromStartingPoint,
			ST segment,
			PT startingPoint,
			boolean allowManyReplies,
			boolean assumeOrientedSegments,
			DynamicDepthUpdater<ST, PT> dynamicDepthUpdater) {
		this(graph, depth, segment, startingPoint, allowManyReplies,
				assumeOrientedSegments,
				-getStartingDistance(positionFromStartingPoint, segment),
				dynamicDepthUpdater);
	}

	/** Constructor.
	 * @param graph is the graph associated to this iterator.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param positionFromStartingPoint is the starting position from
	 *     the {@code starting_point} (in meters).
	 * @param segment is the segment from which to start.
	 * @param startingPoint is the segment's point indicating the direction.
	 * @param allowManyReplies may be {@code true} to allow to reply many times the same
	 *     segment, otherwhise {@code false}.
	 * @param assumeOrientedSegments may be {@code true} to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is {@code false} to assume that
	 *     the end points of a segment are not distinguished.
	 * @param distanceToReachStartingPoint is the distance to reach the starting point.
	 *     It must be negative or nul.
	 * @param dynamicDepthUpdater if not {@code null}, it is used to dynamically update the depth.
	 */
	private DepthGraphIterator(
			Graph<ST, PT> graph,
			double depth,
			ST segment,
			PT startingPoint,
			boolean allowManyReplies,
			boolean assumeOrientedSegments,
			double distanceToReachStartingPoint,
			DynamicDepthUpdater<ST, PT> dynamicDepthUpdater) {
		super(graph,
				new BreadthFirstGraphCourseModel<ST, PT>(),
				segment, startingPoint,
				allowManyReplies,
				assumeOrientedSegments,
				distanceToReachStartingPoint,
				depth - distanceToReachStartingPoint,
				dynamicDepthUpdater);
	}

	/** Constructor.
	 * @param graph is the graph associated to this iterator.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param segment is the segment from which to start.
	 * @param startingPoint is the segment's point indicating the direction.
	 * @param allowManyReplies may be {@code true} to allow to reply many times the same
	 *     segment, otherwhise {@code false}.
	 * @param assumeOrientedSegments may be {@code true} to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is {@code false} to assume that
	 *     the end points of a segment are not distinguished.
	 * @deprecated since 16.0
	 */
	@Deprecated(since = "16.0", forRemoval = true)
	public DepthGraphIterator(
			Graph<ST, PT> graph,
			double depth,
			ST segment,
			PT startingPoint,
			boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		this(graph, depth, 0.f, segment,
				startingPoint, allowManyReplies,
				assumeOrientedSegments, null);
	}

	/** Constructor.
	 * @param graph is the graph associated to this iterator.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param segment is the segment from which to start.
	 * @param startingPoint is the segment's point indicating the direction.
	 * @param allowManyReplies may be {@code true} to allow to reply many times the same
	 *     segment, otherwhise {@code false}.
	 * @param assumeOrientedSegments may be {@code true} to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is {@code false} to assume that
	 *     the end points of a segment are not distinguished.
	 * @param dynamicDepthUpdater if not {@code null}, it is used to dynamically update the depth.
	 */
	public DepthGraphIterator(
			Graph<ST, PT> graph,
			double depth,
			ST segment,
			PT startingPoint,
			boolean allowManyReplies,
			boolean assumeOrientedSegments,
			DynamicDepthUpdater<ST, PT> dynamicDepthUpdater) {
		this(graph, depth, 0.f, segment,
				startingPoint, allowManyReplies,
				assumeOrientedSegments, dynamicDepthUpdater);
	}

	private static double getStartingDistance(double positionFromStartingPoint, GraphSegment<?, ?> segment) {
		final double totalLength = segment.getLength();
		if (positionFromStartingPoint > totalLength) {
			return totalLength;
		}
		if (positionFromStartingPoint < 0.) {
			return 0;
		}
		return positionFromStartingPoint;
	}

	/** Replies if the specified element could be added into the list of futher elements.
	 */
	@Override
	@Pure
	protected boolean canGotoIntoElement(GraphIterationElement<ST, PT> element) {
		return element.getDistanceToConsume() > 0.;
	}

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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
 * by the iterator. If {@code assumeOrientedSegments} is <code>true</code> it means
 * that a segment reached by one of its end point is different than the same segment reached
 * by the other end point. If {@code assumeOrientedSegments} is <code>false</code> it means
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

	/**
	 * @param graph is the graph associated to this iterator.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param position_from_starting_point is the starting position from
	 *     the {@code starting_point} (in meters).
	 * @param segment is the segment from which to start.
	 * @param starting_point is the segment's point indicating the direction.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same
	 *     segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 */
	public DepthGraphIterator(
			Graph<ST, PT> graph,
			double depth,
			double position_from_starting_point,
			ST segment,
			PT starting_point,
			boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		this(graph, depth, segment, starting_point, allowManyReplies,
				assumeOrientedSegments,
				-getStartingDistance(position_from_starting_point, segment));
	}

	/**
	 * @param graph is the graph associated to this iterator.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param position_from_starting_point is the starting position from
	 *     the {@code starting_point} (in meters).
	 * @param segment is the segment from which to start.
	 * @param starting_point is the segment's point indicating the direction.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same
	 *     segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 * @param distanceToReachStartingPoint is the distance to reach the starting point.
	 *     It must be negative or nul.
	 */
	private DepthGraphIterator(
			Graph<ST, PT> graph,
			double depth,
			ST segment,
			PT starting_point,
			boolean allowManyReplies,
			boolean assumeOrientedSegments,
			double distanceToReachStartingPoint) {
		super(graph,
				new BreadthFirstGraphCourseModel<ST, PT>(),
				segment, starting_point,
				allowManyReplies,
				assumeOrientedSegments,
				distanceToReachStartingPoint,
				depth - distanceToReachStartingPoint);
	}

	/**
	 * @param graph is the graph associated to this iterator.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param segment is the segment from which to start.
	 * @param starting_point is the segment's point indicating the direction.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same
	 *     segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 */
	public DepthGraphIterator(
			Graph<ST, PT> graph,
			double depth,
			ST segment,
			PT starting_point,
			boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		this(graph, depth, 0.f, segment,
				starting_point, allowManyReplies,
				assumeOrientedSegments);
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
		return element.distanceToConsume > 0.;
	}

}

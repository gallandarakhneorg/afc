/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.graph;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class is an iterator on the segments
 * limited to a specified depth.
 * <p>
 * The behaviour of the iterator is strongly influenced by the constructor's parameters.
 * One of the most important parameter is <var>assumeOrientedSegments</var>.
 * The <var>assumeOrientedSegments</var> parameter indicates how the segments are considered
 * by the iterator. If <var>assumeOrientedSegments</var> is <code>true</code> it means
 * that a segment reached by one of its end point is different than the same segment reached
 * by the other end point. If <var>assumeOrientedSegments</var> is <code>false</code> it means
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
public class DepthGraphIterator<ST extends GraphSegment<ST,PT>,PT extends GraphPoint<PT,ST>>
extends GraphIterator<ST,PT> {
	
	/**
	 * @param graph is the graph associated to this iterator.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param position_from_starting_point is the starting position from
	 * the <var>starting_point</var> (in meters).
	 * @param segment is the segment from which to start.
	 * @param starting_point is the segment's point indicating the direction.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 * instances for graph iteration: the first instance is associated the first point of the segment and the second
	 * instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 * the end points of a segment are not distinguished.
	 */
	public DepthGraphIterator(
			Graph<ST,PT> graph,
			double depth,
			double position_from_starting_point,
			ST segment, 
			PT starting_point,
			boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		this(graph, depth, segment, starting_point, allowManyReplies,
				assumeOrientedSegments, 
				-getStartingDistance(position_from_starting_point,segment));
	}
	
	/**
	 * @param graph is the graph associated to this iterator.
	 * @param depth is the maximal depth to reach (in the metric coordiante system).
	 * @param position_from_starting_point is the starting position from
	 * the <var>starting_point</var> (in meters).
	 * @param segment is the segment from which to start.
	 * @param starting_point is the segment's point indicating the direction.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 * instances for graph iteration: the first instance is associated the first point of the segment and the second
	 * instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 * the end points of a segment are not distinguished.
	 * @param distanceToReachStartingPoint is the distance to reach the starting point.
	 * It must be negative or nul.
	 */
	private DepthGraphIterator(
			Graph<ST,PT> graph,
			double depth,
			ST segment, 
			PT starting_point,
			boolean allowManyReplies,
			boolean assumeOrientedSegments,
			double distanceToReachStartingPoint) {
		super(graph,
				new BreadthFirstGraphCourseModel<ST,PT>(),
				segment,starting_point,
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
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 * instances for graph iteration: the first instance is associated the first point of the segment and the second
	 * instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 * the end points of a segment are not distinguished.
	 */
	public DepthGraphIterator(
			Graph<ST,PT> graph,
			double depth,
			ST segment, 
			PT starting_point,
			boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		this(graph, depth, 0.f, segment, 
				starting_point, allowManyReplies, 
				assumeOrientedSegments);
	}

	private static double getStartingDistance(double position_from_starting_point, GraphSegment<?,?> segment) {
		double total_length = segment.getLength();
		if (position_from_starting_point>total_length) return total_length;
		if (position_from_starting_point<0.) return 0;
		return position_from_starting_point;
	}
	
	/** Replies if the specified element could be added into the list of futher elements.
	 */
	@Override
	@Pure
	protected boolean canGotoIntoElement(GraphIterationElement<ST,PT> element) {
		return (element.distanceToConsume>0.);
	}

}

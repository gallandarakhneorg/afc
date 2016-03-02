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

/** This interface representes a graph.
 * 
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Graph<ST extends GraphSegment<ST,PT>, 
                       PT extends GraphPoint<PT,ST>>
extends Iterable<ST> {

    /** Replies the count of segments in this graph.
     * 
     * @return the count of segments in this graph.
     * @since 4.1
     */
	public int getSegmentCount();
	
    /** Replies the count of points in this graph.
     * 
     * @return the count of points in this graph.
     * @since 4.1
     */
	public int getPointCount();

	/** Replies if this graph is empty or not.
     * 
     * @return <code>true</code> if the graph is empty,
     * otherwise <code>false</code>.
     */
	public boolean isEmpty();

    /** Replies is this graph contains the given segment.
     * The test is based on {@link Object#equals(Object)}.
     * 
     * @param obj
     * @return <code>true</code> if the graph contains the segment,
     * otherwise <code>false</code>.
     */
	public boolean contains(Object obj);

	/** Replies an iterator that permits to move along the road segment's graph
     * starting from this road segment and from the specified starting point.
     * 
     * @param starting_segment is the first segment to iterate.
     * @param starting_point is the starting point of the iterations.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 * instances for graph iteration: the first instance is associated the first point of the segment and the second
	 * instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 * the end points of a segment are not distinguished.
     * @return the iterator.
     */
    public GraphIterator<ST,PT> iterator(ST starting_segment, PT starting_point, boolean allowManyReplies, boolean assumeOrientedSegments);
    
    /** Replies an iterator that permits to move along the segment's graph
	 * starting from the specified segment and from the specified starting point.
	 * If the specified starting point is not one of the ends of the segment,
	 * this function assumes to start from the point replied by
	 * {@link GraphSegment#getBeginPoint()}.
     * <p>
     * This function does not allow the cycles during the iterations.
	 * 
     * @param startingSegment is the first segment to iterate.
	 * @param depth is the maximal depth to reach.
	 * @param position_from_starting_point is the starting position from
	 * the <var>startingPoint</var>.
     * @param startingPoint is the starting point of the iterations.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 * instances for graph iteration: the first instance is associated the first point of the segment and the second
	 * instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 * the end points of a segment are not distinguished.
     * @return the iterator.
	 */
	public GraphIterator<ST,PT> depthIterator(ST startingSegment, double depth, double position_from_starting_point, PT startingPoint, boolean allowManyReplies, boolean assumeOrientedSegments);

}

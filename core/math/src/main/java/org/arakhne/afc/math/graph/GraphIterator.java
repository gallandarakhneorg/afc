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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * This class is an iterator on a graph.
 * <p>
 * The behaviour of the iterator is strongly influenced by the constructor's parameters.
 * The two most important parameters are <var>allowManyReplies</var> and
 * <var>assumeOrientedSegments</var>.
 * <p>
 * The <var>allowManyReplies</var> parameter indicates if a segment could be replied many
 * types, or not, by the iterator. This parameter permits to control the behaviour of
 * the iterator against graph cycles.
 * <p>
 * The <var>assumeOrientedSegments</var> parameter indicates how the segments are considered
 * by the iterator. If <var>assumeOrientedSegments</var> is <code>true</code> it means
 * that a segment reached by one of its end point is different than the same segment reached
 * by the other end point. If <var>assumeOrientedSegments</var> is <code>false</code> it means
 * that the end points of the segments are not take into account. This parameter is usefull
 * only if <var>allowManyReplies</var> is set to <code>false</code>.
 * 
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GraphIterator<ST extends GraphSegment<ST,PT>,
                           PT extends GraphPoint<PT,ST>>
implements Iterator<ST> {

	private final boolean allowManyReplies;
	private final boolean assumeOrientedSegments;
	private final GraphCourseModel<ST,PT> courseModel;
	private final TreeSet<GraphIterationElement<ST,PT>> visited;
	private GraphIterationElement<ST,PT> current = null;
	private final WeakReference<Graph<ST,PT>> graph;
	
	/**
	 * @param graph is the graph associated to this iterator.
	 * @param segment is the segment from which to start.
	 * @param point is the segment's point indicating the direction.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 * instances for graph iteration: the first instance is associated the first point of the segment and the second
	 * instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 * the end points of a segment are not distinguished.
	 * @param distanceToReachStartingPoint is the distance to reach the starting point.
	 * It must be negative or nul.
	 */
	public GraphIterator(Graph<ST,PT> graph, ST segment, PT point, boolean allowManyReplies, boolean assumeOrientedSegments, double distanceToReachStartingPoint) {
		this(graph,null,segment,point,allowManyReplies, assumeOrientedSegments,distanceToReachStartingPoint);
	}

	/**
	 * @param graph is the graph associated to this iterator.
	 * @param courseModel is the course model to use.
	 * @param segment is the segment from which to start.
	 * @param point is the segment's point indicating the direction.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 * instances for graph iteration: the first instance is associated the first point of the segment and the second
	 * instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 * the end points of a segment are not distinguished.
	 * @param distanceToReachStartingPoint is the distance to reach the starting point.
	 * It must be negative or nul.
	 */
	public GraphIterator(Graph<ST,PT> graph, GraphCourseModel<ST,PT> courseModel, ST segment, PT point, boolean allowManyReplies, boolean assumeOrientedSegments, double distanceToReachStartingPoint) {
		this(graph, courseModel, segment, point, allowManyReplies, assumeOrientedSegments, distanceToReachStartingPoint, Double.POSITIVE_INFINITY);
	}
	
	/**
	 * @param graph is the graph associated to this iterator.
	 * @param courseModel is the course model to use.
	 * @param segment is the segment from which to start.
	 * @param point is the segment's point indicating the direction.
	 * @param allowManyReplies may be <code>true</code> to allow to reply many times the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 * instances for graph iteration: the first instance is associated the first point of the segment and the second
	 * instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 * the end points of a segment are not distinguished.
	 * @param distanceToReachStartingPoint is the distance to reach the starting point.
	 * @param distanceToConsumeAfter is the distance to consume after traversing the segment.
	 * It must be negative or nul.
	 */
	protected GraphIterator(
			Graph<ST,PT> graph, GraphCourseModel<ST,PT> courseModel, 
			ST segment, PT point, 
			boolean allowManyReplies, boolean assumeOrientedSegments, 
			double distanceToReachStartingPoint,
			double distanceToConsumeAfter) {
		this.graph = new WeakReference<>(graph);
		GraphCourseModel<ST,PT> courseM = courseModel;
		if (courseM==null) {
			courseM = new BreadthFirstGraphCourseModel<>();
		}

		this.courseModel = courseM;
		this.allowManyReplies = allowManyReplies;
		this.assumeOrientedSegments = assumeOrientedSegments;
		
		GraphIterationElement<ST,PT> firstElement = newIterationElement(
				null,segment,point,
				(distanceToReachStartingPoint>0) ? 0 : distanceToReachStartingPoint,
				distanceToConsumeAfter);
		
		this.courseModel.addIterationElement(firstElement);
		if (!this.allowManyReplies) {
			GraphIterationElementComparator<ST,PT> comparator = createVisitedSegmentComparator(this.assumeOrientedSegments);
			assert(comparator!=null);
			this.visited = new TreeSet<>(comparator);
			this.visited.add(firstElement);
		}
		else {
			this.visited = null;
		}
	}

	/** Invoked when a comparator on visited segments is required.
	 * 
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 * instances for graph iteration: the first instance is associated the first point of the segment and the second
	 * instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 * the end points of a segment are not distinguished.
	 * @return the graph element iterator, or <code>null</code> to use the
	 * default comparation behaviour of the <code>GraphIterationElement</code>. 
	 */
	protected GraphIterationElementComparator<ST,PT> createVisitedSegmentComparator(boolean assumeOrientedSegments) {
		return new GraphIterationElementComparator<>(assumeOrientedSegments);
	}
	
	/** Replies the graph on which this iterator is iterating.
	 * 
	 * @return the graph.
	 */
	Graph<ST,PT> getGraph() {
		return this.graph.get();
	}
	
	/** Clear the temporary buffers of this graph iterator.
	 */
	void clear() {
		if (this.visited!=null) this.visited.clear();
		this.current = null;
	}

	/** Replies the next segments.
	 * 
	 * @param avoid_visited_segments is <code>true</code> to avoid to reply already visited segments, otherwise <code>false</code>
	 * @param element is the element from which the next segments must be replied.
	 * @return the list of the following segments
	 * @see #next()
	 * @throws NoSuchElementException
	 */
	protected final List<GraphIterationElement<ST,PT>> getNextSegments(boolean avoid_visited_segments, GraphIterationElement<ST,PT> element) {
		assert(this.allowManyReplies || this.visited!=null);
		if (element!=null) {
			ST segment = element.getSegment();
			PT point = element.getPoint();
			if ((segment!=null)&&(point!=null)) {
				PT pts = segment.getOtherSidePoint(point);
				if (pts!=null) {
					double distanceToReach = element.getDistanceToReachSegment()+segment.getLength();
					GraphIterationElement<ST,PT> candidate;
					double restToConsume = element.distanceToConsume - segment.getLength();
					ArrayList<GraphIterationElement<ST,PT>> list = new ArrayList<>();
					for (ST theSegment : pts.getConnectedSegmentsStartingFrom(segment)) {
						if (!theSegment.equals(segment)) {
							candidate = newIterationElement(
									segment,theSegment,
									pts,
									distanceToReach,
									restToConsume);
							if ((this.allowManyReplies)
								||(!avoid_visited_segments)
								||(!this.visited.contains(candidate))) {
								list.add(candidate);
							}
						}
					}
					
					return list;
				}
			}
		}
		throw new NoSuchElementException();
	}
	
	/** Replies the next element without removing it from the iterator list.
	 * 
	 * @return the next element without removing it from the iterator list.
	 */
	protected GraphIterationElement<ST,PT> getNextElement() {
		return this.courseModel.getNextIterationElement();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if (this.courseModel.isEmpty()) {
			clear();
			return false;
		}
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final ST next() {
		GraphIterationElement<ST,PT> theElement = nextElement();
		ST sgmt = theElement.getSegment();
		if (sgmt!=null) return sgmt;
		clear();
		throw new NoSuchElementException();
	}

	/** Replies the next segment.
	 *
	 * @return the next segment
	 * @throws NoSuchElementException
	 */
	public final GraphIterationElement<ST,PT> nextElement() {
		if (!this.courseModel.isEmpty()) {
			GraphIterationElement<ST,PT> theElement = this.courseModel.removeNextIterationElement();
			if (theElement!=null) {
				List<GraphIterationElement<ST,PT>> list = getNextSegments(true,theElement);
				Iterator<GraphIterationElement<ST,PT>> iterator;
				boolean hasFollowingSegments = false;
				GraphIterationElement<ST,PT> elt;

				if (this.courseModel.isReversedRestitution()) {
					iterator = new ReverseIterator<>(list);
				}
				else {
					iterator = list.iterator();
				}
				
				while (iterator.hasNext()) {
					elt = iterator.next();
					if (canGotoIntoElement(elt)) {
						hasFollowingSegments = true;
						this.courseModel.addIterationElement(elt);
						if (!this.allowManyReplies) {
							this.visited.add(elt);
						}
					}
				}
				
				theElement.setTerminalSegment(!hasFollowingSegments);
				theElement.replied = true;
				this.current = theElement;
				return this.current;
			}
		}
		clear();
		throw new NoSuchElementException();
	}
	
	/** Create an instance of GraphIterationElement.
	 * 
	 * @param previous_segment is the previous element that permits to reach this object during an iteration
	 * @param segment is the current segment
	 * @param point is the point on which the iteration arrived on the current segment.
	 * @param distanceToReach is the distance that is already consumed to reach the segment.
	 * @param distanceToConsume is the rest of distance to consume including the segment.
	 * @return a graph iteration element.
	 */
	protected GraphIterationElement<ST,PT> newIterationElement(
			ST previous_segment, ST segment, 
			PT point, 
			double distanceToReach,
			double distanceToConsume) {
		return new GraphIterationElement<>(
				previous_segment, segment, 
				point, 
				distanceToReach,
				distanceToConsume);
	}
	
	/** Replies if the specified element could be added into the list of futher elements.
	 * 
	 * @param element
	 * @return <code>true</code> if the given element is addable into the associated list.
	 */
	protected boolean canGotoIntoElement(GraphIterationElement<ST,PT> element) {
		return true;
	}

	/** Ignore the elements after the specified element.
	 * 
	 * @param element
	 */
	public void ignoreElementsAfter(GraphIterationElement<ST,PT> element) {
		List<GraphIterationElement<ST,PT>> nexts = getNextSegments(false,element);
		this.courseModel.removeIterationElements(nexts);
	}

	/** Ignore the elements after the specified element.
	 */
	public void ignoreElementsAfter() {
		if (this.current!=null) {
			ignoreElementsAfter(this.current);
		}
		else {
			clear();
			throw new NoSuchElementException();
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void remove() {
		if (this.current!=null) {
			ignoreElementsAfter(this.current);
			if (this.visited!=null) this.visited.remove(this.current);
		}
		else {
			clear();
			throw new NoSuchElementException();
		}
	}

	/** Replies if this iterator is assumed that a segment may be replied many times.
	 * 
	 * @return <code>true</code> if this iterator allows cycles, otherwise <code>false</code>
	 */
	public final boolean isManySegmentReplyEnabled() {
		return this.allowManyReplies;
	}
		
	/** Replies if this iterator is assumed oriented segments or not.
	 * 
	 * @return <code>true</code> if this iterator assumes oriented segments, otherwise <code>false</code>
	 */
	public final boolean isOrientedSegmentSupportEnabled() {
		return this.assumeOrientedSegments;
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
    private static class ReverseIterator<E> implements Iterator<E> {

    	private int savedSize;
    	private int index;
    	private final List<E> list;
    	
    	public ReverseIterator(List<E> list) {
    		this.savedSize = (list==null) ? 0 : list.size();
    		this.index = this.savedSize - 1;
    		this.list = list;
    	}

		@Override
		public boolean hasNext() {
			return this.list!=null && this.index>=0 && this.index<this.list.size();
		}

		@Override
		public E next() {
			if (this.list==null) throw new NoSuchElementException();
			if (this.savedSize!=this.list.size()) throw new ConcurrentModificationException();
			if (this.index>=this.list.size()) throw new NoSuchElementException();
			E elt = this.list.get(this.index);
			--this.index;
			return elt;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
    	
    }

}

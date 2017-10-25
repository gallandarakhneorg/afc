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

package org.arakhne.afc.math.graph;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class is an iterator on a graph.
 *
 * <p>The behaviour of the iterator is strongly influenced by the constructor's parameters.
 * The two most important parameters are {@code allowManyReplies} and
 * {@code assumeOrientedSegments}.
 *
 * <p>The {@code allowManyReplies} parameter indicates if a segment could be replied many
 * types, or not, by the iterator. This parameter permits to control the behaviour of
 * the iterator against graph cycles.
 *
 * <p>The {@code assumeOrientedSegments} parameter indicates how the segments are considered
 * by the iterator. If {@code assumeOrientedSegments} is <code>true</code> it means
 * that a segment reached by one of its end point is different than the same segment reached
 * by the other end point. If {@code assumeOrientedSegments} is <code>false</code> it means
 * that the end points of the segments are not take into account. This parameter is usefull
 * only if {@code allowManyReplies} is set to <code>false</code>.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GraphIterator<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>>
		implements Iterator<ST> {

	private final boolean allowManyReplies;

	private final boolean assumeOrientedSegments;

	private final GraphCourseModel<ST, PT> courseModel;

	private final Set<GraphIterationElement<ST, PT>> visited;

	private GraphIterationElement<ST, PT> current;

	private final WeakReference<Graph<ST, PT>> graph;

	/**
	 * @param graph1 is the graph associated to this iterator.
	 * @param segment is the segment from which to start.
	 * @param point is the segment's point indicating the direction.
	 * @param allowManyReplies1 may be <code>true</code> to allow to reply many times
	 *     the same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments1 may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 * @param distanceToReachStartingPoint is the distance to reach the starting point.
	 *     It must be negative or nul.
	 */
	public GraphIterator(Graph<ST, PT> graph1, ST segment, PT point, boolean allowManyReplies1,
			boolean assumeOrientedSegments1, double distanceToReachStartingPoint) {
		this(graph1, null, segment, point, allowManyReplies1, assumeOrientedSegments1, distanceToReachStartingPoint);
	}

	/**
	 * @param graph1 is the graph associated to this iterator.
	 * @param courseModel1 is the course model to use.
	 * @param segment is the segment from which to start.
	 * @param point is the segment's point indicating the direction.
	 * @param allowManyReplies1 may be <code>true</code> to allow to reply many times the
	 *     same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments1 may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 * @param distanceToReachStartingPoint is the distance to reach the starting point.
	 *     It must be negative or nul.
	 */
	public GraphIterator(Graph<ST, PT> graph1, GraphCourseModel<ST, PT> courseModel1, ST segment,
			PT point, boolean allowManyReplies1, boolean assumeOrientedSegments1, double distanceToReachStartingPoint) {
		this(graph1, courseModel1, segment, point, allowManyReplies1, assumeOrientedSegments1,
				distanceToReachStartingPoint, Double.POSITIVE_INFINITY);
	}

	/**
	 * @param graph1 is the graph associated to this iterator.
	 * @param courseModel1 is the course model to use.
	 * @param segment is the segment from which to start.
	 * @param point is the segment's point indicating the direction.
	 * @param allowManyReplies1 may be <code>true</code> to allow to reply many times the
	 *     same segment, otherwhise <code>false</code>.
	 * @param assumeOrientedSegments1 may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 * @param distanceToReachStartingPoint is the distance to reach the starting point.
	 * @param distanceToConsumeAfter is the distance to consume after traversing the segment.
	 *     It must be negative or nul.
	 */
	protected GraphIterator(
			Graph<ST, PT> graph1, GraphCourseModel<ST, PT> courseModel1,
			ST segment, PT point,
			boolean allowManyReplies1, boolean assumeOrientedSegments1,
			double distanceToReachStartingPoint,
			double distanceToConsumeAfter) {
		this.graph = new WeakReference<>(graph1);
		GraphCourseModel<ST, PT> courseM = courseModel1;
		if (courseM == null) {
			courseM = new BreadthFirstGraphCourseModel<>();
		}

		this.courseModel = courseM;
		this.allowManyReplies = allowManyReplies1;
		this.assumeOrientedSegments = assumeOrientedSegments1;

		final GraphIterationElement<ST, PT> firstElement = newIterationElement(
				null, segment, point,
				(distanceToReachStartingPoint > 0) ? 0 : distanceToReachStartingPoint,
				distanceToConsumeAfter);

		this.courseModel.addIterationElement(firstElement);
		if (!this.allowManyReplies) {
			final GraphIterationElementComparator<ST, PT> comparator = createVisitedSegmentComparator(
					this.assumeOrientedSegments);
			assert comparator != null;
			this.visited = new TreeSet<>(comparator);
			this.visited.add(firstElement);
		} else {
			this.visited = null;
		}
	}

	/** Invoked when a comparator on visited segments is required.
	 *
	 * @param assumeOrientedSegments1 may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 * @return the graph element iterator, or <code>null</code> to use the
	 *     default comparation behaviour of the <code>GraphIterationElement</code>.
	 */
	@Pure
	protected GraphIterationElementComparator<ST, PT> createVisitedSegmentComparator(boolean assumeOrientedSegments1) {
		return new GraphIterationElementComparator<>(assumeOrientedSegments1);
	}

	/** Replies the graph on which this iterator is iterating.
	 *
	 * @return the graph.
	 */
	@Pure
	Graph<ST, PT> getGraph() {
		return this.graph.get();
	}

	/** Clear the temporary buffers of this graph iterator.
	 */
	void clear() {
		if (this.visited != null) {
			this.visited.clear();
		}
		this.current = null;
	}

	/** Replies the next segments.
	 *
	 * @param avoid_visited_segments is <code>true</code> to avoid to reply already visited segments, otherwise <code>false</code>
	 * @param element is the element from which the next segments must be replied.
	 * @return the list of the following segments
	 * @see #next()
	 */
	@SuppressWarnings("checkstyle:nestedifdepth")
	protected final List<GraphIterationElement<ST, PT>> getNextSegments(boolean avoid_visited_segments,
			GraphIterationElement<ST, PT> element) {
		assert this.allowManyReplies || this.visited != null;
		if (element != null) {
			final ST segment = element.getSegment();
			final PT point = element.getPoint();
			if ((segment != null) && (point != null)) {
				final PT pts = segment.getOtherSidePoint(point);
				if (pts != null) {
					final double distanceToReach = element.getDistanceToReachSegment() + segment.getLength();
					GraphIterationElement<ST, PT> candidate;
					final double restToConsume = element.distanceToConsume - segment.getLength();
					final List<GraphIterationElement<ST, PT>> list = new ArrayList<>();
					for (final ST theSegment : pts.getConnectedSegmentsStartingFrom(segment)) {
						if (!theSegment.equals(segment)) {
							candidate = newIterationElement(
									segment, theSegment,
									pts,
									distanceToReach,
									restToConsume);
							if ((this.allowManyReplies)
									|| (!avoid_visited_segments)
									|| (!this.visited.contains(candidate))) {
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
	protected GraphIterationElement<ST, PT> getNextElement() {
		return this.courseModel.getNextIterationElement();
	}

	@Pure
	@Override
	public boolean hasNext() {
		if (this.courseModel.isEmpty()) {
			clear();
			return false;
		}
		return true;
	}

	@Override
	public final ST next() {
		final GraphIterationElement<ST, PT> theElement = nextElement();
		final ST sgmt = theElement.getSegment();
		if (sgmt != null) {
			return sgmt;
		}
		clear();
		throw new NoSuchElementException();
	}

	/** Replies the next segment.
	 *
	 * @return the next segment
	 */
	public final GraphIterationElement<ST, PT> nextElement() {
		if (!this.courseModel.isEmpty()) {
			final GraphIterationElement<ST, PT> theElement = this.courseModel.removeNextIterationElement();
			if (theElement != null) {
				final List<GraphIterationElement<ST, PT>> list = getNextSegments(true, theElement);
				final Iterator<GraphIterationElement<ST, PT>> iterator;
				boolean hasFollowingSegments = false;
				GraphIterationElement<ST, PT> elt;

				if (this.courseModel.isReversedRestitution()) {
					iterator = new ReverseIterator<>(list);
				} else {
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
	protected GraphIterationElement<ST, PT> newIterationElement(
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
	 * @param element the element to test.
	 * @return <code>true</code> if the given element is addable into the associated list.
	 */
	@Pure
	protected boolean canGotoIntoElement(GraphIterationElement<ST, PT> element) {
		return true;
	}

	/** Ignore the elements after the specified element.
	 *
	 * @param element the reference element.
	 */
	public void ignoreElementsAfter(GraphIterationElement<ST, PT> element) {
		final List<GraphIterationElement<ST, PT>> nexts = getNextSegments(false, element);
		this.courseModel.removeIterationElements(nexts);
	}

	/** Ignore the elements after the specified element.
	 */
	public void ignoreElementsAfter() {
		if (this.current != null) {
			ignoreElementsAfter(this.current);
		} else {
			clear();
			throw new NoSuchElementException();
		}
	}

	@Override
	public void remove() {
		if (this.current != null) {
			ignoreElementsAfter(this.current);
			if (this.visited != null) {
				this.visited.remove(this.current);
			}
		} else {
			clear();
			throw new NoSuchElementException();
		}
	}

	/** Replies if this iterator is assumed that a segment may be replied many times.
	 *
	 * @return <code>true</code> if this iterator allows cycles, otherwise <code>false</code>
	 */
	@Pure
	public final boolean isManySegmentReplyEnabled() {
		return this.allowManyReplies;
	}

	/** Replies if this iterator is assumed oriented segments or not.
	 *
	 * @return <code>true</code> if this iterator assumes oriented segments, otherwise <code>false</code>
	 */
	@Pure
	public final boolean isOrientedSegmentSupportEnabled() {
		return this.assumeOrientedSegments;
	}

	/** Reverse iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private static class ReverseIterator<E> implements Iterator<E> {

		private int savedSize;

		private int index;

		private final List<E> list;

		ReverseIterator(List<E> list1) {
			this.savedSize = (list1 == null) ? 0 : list1.size();
			this.index = this.savedSize - 1;
			this.list = list1;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.list != null && this.index >= 0 && this.index < this.list.size();
		}

		@Override
		public E next() {
			if (this.list == null) {
				throw new NoSuchElementException();
			}
			if (this.savedSize != this.list.size()) {
				throw new ConcurrentModificationException();
			}
			if (this.index >= this.list.size()) {
				throw new NoSuchElementException();
			}
			final E elt = this.list.get(this.index);
			--this.index;
			return elt;
		}

	}

}

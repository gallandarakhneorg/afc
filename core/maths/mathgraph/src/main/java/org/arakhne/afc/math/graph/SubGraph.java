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
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.references.ComparableWeakReference;
import org.arakhne.afc.references.WeakArrayList;

/**
 * A subgraph.
 *
 * <p>The segments is the subgraph are weak references to the segments
 * to the complete graph.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @param <GP> is the type of graph path
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class SubGraph<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>, GP extends GraphPath<GP, ST, PT>>
		implements Graph<ST, PT> {

	/** Comparator of graph iteration element on oriented segments.
	 */
	protected final GraphIterationElementComparator<ST, PT> iterationOrientedElementComparator;

	/** Comparator of graph iteration element on not-oriented segments.
	 */
	protected final GraphIterationElementComparator<ST, PT> iterationNotOrientedElementComparator;

	private final Set<ComparableWeakReference<PT>> terminalPoints = new TreeSet<>();

	private final Collection<ST> segments;

	private int pointNumber;

	private WeakReference<Graph<ST, PT>> parentGraph;

	/** Constructor.
	 * @param segments1 is the collection to use to store the segments.
	 * @param pointNumber1 is the number of connection points in the subgraph described by the {@code segments}.
	 * @param orientedIterator is a comparator which may be used by this subgraph.
	 * @param notOrientedIterator is a comparator which may be used by this subgraph.
	 */
	protected SubGraph(
			Collection<ST> segments1,
			int pointNumber1,
			GraphIterationElementComparator<ST, PT> orientedIterator,
			GraphIterationElementComparator<ST, PT> notOrientedIterator) {
		this.segments = segments1;
		this.pointNumber = pointNumber1;
		assert orientedIterator != null;
		this.iterationOrientedElementComparator = orientedIterator;
		assert notOrientedIterator != null;
		this.iterationNotOrientedElementComparator = notOrientedIterator;
	}

	/**
	 * Build a subgraph in which graph segments are stored inside
	 * a {@link WeakArrayList} instance.
	 *
	 * @param orientedIterator is a comparator which may be used by this subgraph.
	 * @param notOrientedIterator is a comparator which may be used by this subgraph.
	 */
	public SubGraph(
			GraphIterationElementComparator<ST, PT> orientedIterator,
			GraphIterationElementComparator<ST, PT> notOrientedIterator) {
		this(
				new WeakArrayList<ST>(),
				0,
				orientedIterator,
				notOrientedIterator);
	}

	//-----------------------------------------------------------
	// SubGraph interface
	//-----------------------------------------------------------

	/** Replies the parent graph is this subgraph was built.
	 *
	 * @return the parent graph or <code>null</code>
	 */
	@Pure
	protected final Graph<ST, PT> getParentGraph() {
		return this.parentGraph == null ? null : this.parentGraph.get();
	}

	/** Replies the segments in this subgraph.
	 *
	 * <p>Caution: the replied collection is the real reference
	 * to the internal collection. Any change in this collection
	 * outside this {@link SubGraph} class may causes invalid
	 * behaviour (no event...).
	 *
	 * @return the internal data structure that contains all the
	 *     segments in this subgraph.
	 */
	@Pure
	protected final Collection<ST> getGraphSegments() {
		return this.segments;
	}

	/** Build a subgraph from the specified graph.
	 *
	 * @param iterator is the iterator on the graph.
	 */
	public final void build(GraphIterator<ST, PT> iterator) {
		build(iterator, null);
	}

	/** Build a subgraph from the specified graph.
	 *
	 * @param iterator is the iterator on the graph.
	 * @param listener is the listener invoked each time a segment was added to the subgraph.
	 */
	public final void build(GraphIterator<ST, PT> iterator, SubGraphBuildListener<ST, PT> listener) {
		assert iterator != null;
		final Set<ComparableWeakReference<PT>> reachedPoints = new TreeSet<>();
		GraphIterationElement<ST, PT> element;
		ST segment;
		PT point;
		PT firstPoint = null;

		this.parentGraph = new WeakReference<>(iterator.getGraph());
		this.segments.clear();
		this.pointNumber = 0;
		this.terminalPoints.clear();

		while (iterator.hasNext()) {
			element = iterator.nextElement();
			point = element.getPoint();
			segment = element.getSegment();

			// First reached segment
			if (this.segments.isEmpty()) {
				firstPoint = point;
			}

			this.segments.add(segment);
			if (listener != null) {
				listener.segmentAdded(this, element);
			}

			// Register terminal points
			point = segment.getOtherSidePoint(point);
			final ComparableWeakReference<PT> ref = new ComparableWeakReference<>(point);
			if (element.isTerminalSegment()) {
				if (!reachedPoints.contains(ref)) {
					this.terminalPoints.add(ref);
					if (listener != null) {
						listener.terminalPointReached(this, point, segment);
					}
				}
			} else {
				this.terminalPoints.remove(ref);
				reachedPoints.add(ref);
				if (listener != null) {
					listener.nonTerminalPointReached(this, point, segment);
				}
			}
		}

		if (firstPoint != null) {
			final ComparableWeakReference<PT> ref = new ComparableWeakReference<>(firstPoint);
			if (!reachedPoints.contains(ref)) {
				this.terminalPoints.add(ref);
			}
		}

		this.pointNumber = this.terminalPoints.size() + reachedPoints.size();

		reachedPoints.clear();
	}

	/** Replies if the given point is a terminal point.
	 *
	 * @param point the point to test.
	 * @return <code>true</code> if the point is terminal otherwise <code>false</code>
	 */
	@Pure
	protected final boolean isTerminalPoint(PT point) {
		return this.terminalPoints.contains(new ComparableWeakReference<>(point));
	}

	/** Create a wrapping segment for the given graph segment.
	 *
	 * @param segment the segment to wrap.
	 * @return a wrapping segment or the {@code segment} itself.
	 */
	@Pure
	protected ST wrapSegment(ST segment) {
		return segment;
	}

	/** Create a wrapping point for the given graph point.
	 *
	 * @param point the point to wrap.
	 * @param segment is the segment connected to the point.
	 * @param isTerminal indicates if this point should be a terminal point.
	 * @return a wrapping point or the {@code point} itself.
	 */
	@Pure
	protected PT wrapPoint(PT point, ST segment, boolean isTerminal) {
		return point;
	}

	private ST filterSegment(ST segment) {
		if (this.segments.contains(segment)) {
			return segment;
		}
		return null;
	}

	//-----------------------------------------------------------
	// Graph interface
	//-----------------------------------------------------------

	@Override
	@Pure
	public boolean isEmpty() {
		return this.segments.isEmpty();
	}

	@Pure
	@Override
	public boolean contains(Object obj) {
		return this.segments.contains(obj);
	}

	@Pure
	@Override
	public final int getSegmentCount() {
		return this.segments.size();
	}

	@Pure
	@Override
	public final int getPointCount() {
		return this.pointNumber;
	}

	@Pure
	@Override
	public GraphIterator<ST, PT> depthIterator(
			ST startingSegment,
			double depth, double positionFromStartingPoint,
			PT startingPoint, boolean allowManyReplies,
			boolean assumeOrientedSegments,
			DynamicDepthUpdater<ST, PT> dynamicDepthUpdater) {
		return new DepthSubGraphIterator(
				filterSegment(startingSegment),
				depth,
				positionFromStartingPoint,
				startingPoint,
				allowManyReplies, assumeOrientedSegments,
				dynamicDepthUpdater);
	}

	@Pure
	@Override
	public final Iterator<ST> iterator() {
		return new SubGraphSegmentIterator(this.segments.iterator());
	}

	@Pure
	@Override
	public GraphIterator<ST, PT> iterator(ST startingSegment,
			PT startingPoint, boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		return new SubGraphIterator(
				filterSegment(startingSegment),
				startingPoint,
				allowManyReplies, assumeOrientedSegments);
	}

	/** Iterator on subgraph segments, with exploration depth.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class DepthSubGraphIterator extends DepthGraphIterator<ST, PT> {

		/** Construct the iterator.
		 *
		 * @param startingSegment the starting segment.
		 * @param depth the exploration depth.
		 * @param positionFromStartingPoint the position from the starting point.
		 * @param startingPoint the starting point, attached to the starting segment.
		 * @param allowManyReplies indicates if a segment could be reply many times.
		 * @param assumedOrientedSegments indicates if the segments are assumed to be oriented,
		 *     i.e. that are from their starting point to their ending points.
		 * @param dynamicDepthUpdater if not {@code null}, it is used for updating dynamically the depth.
		 */
		DepthSubGraphIterator(ST startingSegment,
				double depth, double positionFromStartingPoint, PT startingPoint,
				boolean allowManyReplies,
				boolean assumedOrientedSegments,
				DynamicDepthUpdater<ST, PT> dynamicDepthUpdater) {
			super(SubGraph.this, depth, positionFromStartingPoint,
					startingSegment, startingPoint,
					allowManyReplies, assumedOrientedSegments,
					dynamicDepthUpdater);
		}

		@Override
		protected GraphIterationElement<ST, PT> newIterationElement(
				ST previousSegment, ST segment,
				PT point,
				double distanceToReach,
				double distanceToConsume) {
			final double newDistanceToConsume;
			if (this.dynamicDepthUpdater != null) {
				newDistanceToConsume = this.dynamicDepthUpdater.updateDepth(
						previousSegment, segment, point, distanceToReach, distanceToConsume);
			} else {
				newDistanceToConsume = distanceToConsume;
			}
			return new SubGraphIterationElement(
					previousSegment, segment,
					point,
					distanceToReach,
					newDistanceToConsume);
		}

	}

	/** Iterator on segments of a subgraph, without exploration depth.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class SubGraphIterator extends GraphIterator<ST, PT> {

		/** Construct the iterator.
		 *
		 * @param startingSegment the starting segment.
		 * @param startingPoint the starting point, attached to the starting segment.
		 * @param allowCycles indicates if cycles in iteration are allowed.
		 * @param assumeOrientedSegments indicates if the segments are assumed to be oriented,
		 *     i.e. that are from their starting point to their ending points.
		 */
		SubGraphIterator(ST startingSegment, PT startingPoint, boolean allowCycles, boolean assumeOrientedSegments) {
			super(SubGraph.this, startingSegment, startingPoint, allowCycles, assumeOrientedSegments, 0.f);
		}

		/** Construct the iterator.
		 *
		 * @param startingSegment the starting segment.
		 * @param startingPoint the starting point, attached to the starting segment.
		 * @param assumeOrientedSegments indicates if the segments are assumed to be oriented,
		 *     i.e. that are from their starting point to their ending points.
		 */
		SubGraphIterator(ST startingSegment, PT startingPoint, boolean assumeOrientedSegments) {
			super(SubGraph.this, startingSegment, startingPoint, false, assumeOrientedSegments, 0.f);
		}

		@Override
		protected GraphIterationElementComparator<ST, PT> createVisitedSegmentComparator(boolean assumeOrientedSegments) {
			if (assumeOrientedSegments) {
				if (SubGraph.this.iterationOrientedElementComparator != null) {
					return SubGraph.this.iterationOrientedElementComparator;
				}
			} else {
				if (SubGraph.this.iterationNotOrientedElementComparator != null) {
					return SubGraph.this.iterationNotOrientedElementComparator;
				}
			}
			return super.createVisitedSegmentComparator(assumeOrientedSegments);
		}

		@Override
		protected GraphIterationElement<ST, PT> newIterationElement(
				ST previousSegment, ST segment,
				PT point,
				double distanceToReach,
				double distanceToConsume) {
			return new SubGraphIterationElement(
					previousSegment, segment,
					point,
					distanceToReach,
					distanceToConsume);
		}

	}

	/** Element replied during the iration on a graph.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private final class SubGraphIterationElement extends GraphIterationElement<ST, PT> {

		private ST wrappedPreviousSegment;

		private ST wrappedSegment;

		private PT wrappedPoint;

		/** Constructor.
		 * @param previousSegment is the previous element that permits to reach this object during an iteration
		 * @param segment is the current segment
		 * @param point is the point on which the iteration arrived on the current segment.
		 * @param distanceToReach1 is the distance that is already consumed.
		 * @param distanceToConsume1 is the distance to consume including the segment.
		 */
		SubGraphIterationElement(ST previousSegment, ST segment, PT point, double distanceToReach1, double distanceToConsume1) {
			super(previousSegment, segment, point, distanceToReach1, distanceToConsume1);
		}

		@Override
		public ST getPreviousSegment() {
			if (this.wrappedPreviousSegment == null) {
				this.wrappedPreviousSegment = wrapSegment(this.previousSegment);
			}
			return this.wrappedPreviousSegment;
		}

		@Override
		public ST getSegment() {
			if (this.wrappedSegment == null) {
				this.wrappedSegment = wrapSegment(this.currentSegment);
			}
			return this.wrappedSegment;
		}

		@Override
		public PT getPoint() {
			if (this.wrappedPoint == null) {
				boolean isTerminal = false;
				ST sgmt = this.previousSegment;
				if (sgmt == null) {
					sgmt = this.currentSegment;
					isTerminal = true;
				}
				assert this.connectionPoint != null;
				assert sgmt != null;
				this.wrappedPoint = wrapPoint(
						this.connectionPoint, sgmt, isTerminal || this.lastReachableSegment);
			}
			return this.wrappedPoint;
		}

	}

	/** Iterator on segments of a subgraph.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class SubGraphSegmentIterator implements Iterator<ST> {

		private final Iterator<ST> iterator;

		/** Constructor.
		 * @param iter the original iterator.
		 */
		SubGraphSegmentIterator(Iterator<ST> iter) {
			this.iterator = iter;
		}

		@Override
		public final boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public final ST next() {
			return wrapSegment(this.iterator.next());
		}

		@Override
		public final void remove() {
			//
		}

	}

}

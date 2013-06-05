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
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.arakhne.util.ref.ComparableWeakReference;
import org.arakhne.util.ref.WeakArrayList;

/**
 * A subgraph.
 * <p>
 * The segments is the subgraph are weak references to the segments
 * to the complete graph.
 * 
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @param <GP> is the type of graph path
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SubGraph<ST extends GraphSegment<ST,PT>,
PT extends GraphPoint<PT,ST>,
GP extends GraphPath<GP,ST,PT>>
implements Graph<ST,PT> {

	private final Set<ComparableWeakReference<PT>> terminalPoints = new TreeSet<ComparableWeakReference<PT>>();
	private final Collection<ST> segments;
	private int pointNumber;
	private WeakReference<Graph<ST,PT>> parentGraph = null;
	
	/** Comparator of graph iteration element on oriented segments.
	 */
	protected final GraphIterationElementComparator<ST,PT> iterationOrientedElementComparator;

	/** Comparator of graph iteration element on not-oriented segments.
	 */
	protected final GraphIterationElementComparator<ST,PT> iterationNotOrientedElementComparator;

	/**
	 * @param segments is the collection to use to store the segments.
	 * @param pointNumber is the number of connection points in the subgraph described by the <var>segments</var>.
	 * @param orientedIterator is a comparator which may be used by this subgraph.
	 * @param notOrientedIterator is a comparator which may be used by this subgraph.
	 */
	protected SubGraph(
			Collection<ST> segments,
			int pointNumber,
			GraphIterationElementComparator<ST,PT> orientedIterator,
			GraphIterationElementComparator<ST,PT> notOrientedIterator) {
		this.segments = segments;
		this.pointNumber = pointNumber;
		assert(orientedIterator!=null);
		this.iterationOrientedElementComparator = orientedIterator;
		assert(notOrientedIterator!=null);
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
			GraphIterationElementComparator<ST,PT> orientedIterator,
			GraphIterationElementComparator<ST,PT> notOrientedIterator) {
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
	protected final Graph<ST,PT> getParentGraph() {
		return this.parentGraph==null ? null : this.parentGraph.get();
	}

	/** Replies the segments in this subgraph.
	 * <p>
	 * Caution: the replied collection is the real reference
	 * to the internal collection. Any change in this collection
	 * outside this {@link SubGraph} class may causes invalid
	 * behaviour (no event...).
	 * 
	 * @return the internal data structure that contains all the
	 * segments in this subgraph.
	 */
	protected final Collection<ST> getGraphSegments() {
		return this.segments;
	}

	/** Build a subgraph from the specified graph.
	 * 
	 * @param iterator is the iterator on the graph.
	 */
	public final void build(GraphIterator<ST,PT> iterator) {
		build(iterator, null);
	}

	/** Build a subgraph from the specified graph.
	 * 
	 * @param iterator is the iterator on the graph.
	 * @param listener is the listener invoked each time a segment was added to the subgraph.
	 */
	public final void build(GraphIterator<ST,PT> iterator, SubGraphBuildListener<ST,PT> listener) {
		assert(iterator!=null);
		Set<ComparableWeakReference<PT>> reachedPoints = new TreeSet<ComparableWeakReference<PT>>();		
		GraphIterationElement<ST,PT> element;
		ST segment;
		PT point, firstPoint=null;

		this.parentGraph = new WeakReference<Graph<ST,PT>>(iterator.getGraph());
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
			if (listener!=null) {
				listener.segmentAdded(this, element);
			}

			// Register terminal points
			point = segment.getOtherSidePoint(point);
			ComparableWeakReference<PT> ref = new ComparableWeakReference<PT>(point);
			if (element.isTerminalSegment()) {
				if (!reachedPoints.contains(ref)) {
					this.terminalPoints.add(ref);
					if (listener!=null) {
						listener.terminalPointReached(this, point, segment);
					}
				}
			}
			else {
				this.terminalPoints.remove(ref);
				reachedPoints.add(ref);
				if (listener!=null) {
					listener.nonTerminalPointReached(this, point, segment);
				}
			}
		}

		if (firstPoint!=null) {
			ComparableWeakReference<PT> ref = new ComparableWeakReference<PT>(firstPoint);
			if (!reachedPoints.contains(ref)) this.terminalPoints.add(ref);
		}
		
		this.pointNumber = this.terminalPoints.size() + reachedPoints.size();

		reachedPoints.clear();		
	}

	/** Replies if the given point is a terminal point.
	 *
	 * @param point
	 * @return <code>true</code> if the point is terminal otherwise <code>false</code>
	 */
	protected final boolean isTerminalPoint(PT point) {
		return this.terminalPoints.contains(new ComparableWeakReference<PT>(point));
	}

	/** Create a wrapping segment for the given graph segment.
	 * 
	 * @param segment
	 * @return a wrapping segment or the <var>segment</var> itself.
	 */
	protected ST wrapSegment(ST segment) {
		return segment;
	}

	/** Create a wrapping point for the given graph point.
	 * 
	 * @param point
	 * @param segment is the segment connected to the point.
	 * @param isTerminal indicates if this point should be a terminal point.
	 * @return a wrapping point or the <var>point</var> itself.
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.segments.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object obj) {
		return this.segments.contains(obj);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated see {@link #getSegmentCount()}
	 */
	@Deprecated
	@Override
	public final int size() {
		return getSegmentCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getSegmentCount() {
		return this.segments.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getPointCount() {
		return this.pointNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GraphIterator<ST, PT> depthIterator(
			ST startingSegment,
			float depth, float position_from_starting_point,
			PT startingPoint, boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		return new DepthSubGraphIterator(
				filterSegment(startingSegment),
				depth,
				position_from_starting_point,
				startingPoint,
				allowManyReplies, assumeOrientedSegments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Iterator<ST> iterator() {
		return new SubGraphSegmentIterator(this.segments.iterator());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GraphIterator<ST, PT> iterator(ST starting_segment,
			PT starting_point, boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		return new SubGraphIterator(
				filterSegment(starting_segment),
				starting_point,
				allowManyReplies, assumeOrientedSegments);
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class DepthSubGraphIterator extends DepthGraphIterator<ST,PT> {

		/**
		 * @param startingSegment
		 * @param depth
		 * @param position_from_starting_point
		 * @param startingPoint
		 * @param allowManyReplies
		 * @param assumedOrientedSegments
		 */
		public DepthSubGraphIterator(ST startingSegment,
				float depth, float position_from_starting_point, PT startingPoint,
				boolean allowManyReplies,
				boolean assumedOrientedSegments) {
			super(SubGraph.this, depth, position_from_starting_point,
					startingSegment, startingPoint, 
					allowManyReplies, assumedOrientedSegments);
		}

		@Override
		protected GraphIterationElement<ST,PT> newIterationElement(
				ST previous_segment, ST segment, 
				PT point, 
				float distanceToReach,
				float distanceToConsume) {
			return new SubGraphIterationElement(
					previous_segment, segment, 
					point, 
					distanceToReach,
					distanceToConsume);
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class SubGraphIterator extends GraphIterator<ST,PT> {

		/**
		 * @param startingSegment
		 * @param startingPoint
		 * @param allowCycles
		 * @param assumeOrientedSegments
		 */
		public SubGraphIterator(ST startingSegment, PT startingPoint, boolean allowCycles, boolean assumeOrientedSegments) {
			super(SubGraph.this, startingSegment, startingPoint, allowCycles, assumeOrientedSegments, 0.f);
		}

		/**
		 * @param startingSegment
		 * @param startingPoint
		 * @param assumeOrientedSegments
		 */
		public SubGraphIterator(ST startingSegment, PT startingPoint, boolean assumeOrientedSegments) {
			super(SubGraph.this, startingSegment, startingPoint, false, assumeOrientedSegments, 0.f);
		}

		@Override
		protected GraphIterationElementComparator<ST,PT> createVisitedSegmentComparator(boolean assumeOrientedSegments) {
			if (assumeOrientedSegments) {
				if (SubGraph.this.iterationOrientedElementComparator!=null)
					return SubGraph.this.iterationOrientedElementComparator;
			}
			else {
				if (SubGraph.this.iterationNotOrientedElementComparator!=null)
					return SubGraph.this.iterationNotOrientedElementComparator;
			}
			return super.createVisitedSegmentComparator(assumeOrientedSegments);
		}

		@Override
		protected GraphIterationElement<ST,PT> newIterationElement(
				ST previous_segment, ST segment, 
				PT point, 
				float distanceToReach,
				float distanceToConsume) {
			return new SubGraphIterationElement(
					previous_segment, segment, 
					point, 
					distanceToReach, 
					distanceToConsume);
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private final class SubGraphIterationElement extends GraphIterationElement<ST,PT> {

		private ST wrappedPreviousSegment = null;
		private ST wrappedSegment = null;
		private PT wrappedPoint = null;

		/**
		 * @param previous_segment is the previous element that permits to reach this object during an iteration
		 * @param segment is the current segment
		 * @param point is the point on which the iteration arrived on the current segment.
		 * @param distanceToReach is the distance that is already consumed.
		 * @param distanceToConsume is the distance to consume including the segment.
		 */
		public SubGraphIterationElement(ST previous_segment, ST segment, PT point, float distanceToReach, float distanceToConsume) {
			super(previous_segment, segment, point, distanceToReach, distanceToConsume);
		}

		/** {@inheritDoc}
		 */
		@Override
		public ST getPreviousSegment() {
			if (this.wrappedPreviousSegment==null) {
				this.wrappedPreviousSegment = wrapSegment(this.previousSegment);
			}
			return this.wrappedPreviousSegment;
		}

		/** {@inheritDoc}
		 */
		@Override
		public ST getSegment() {
			if (this.wrappedSegment==null) {
				this.wrappedSegment = wrapSegment(this.currentSegment);
			}
			return this.wrappedSegment;
		}

		/** {@inheritDoc}
		 */
		@Override
		public PT getPoint() {
			if (this.wrappedPoint==null) {
				boolean isTerminal = false;
				ST sgmt = this.previousSegment;
				if (sgmt==null) {
					sgmt = this.currentSegment;
					isTerminal = true;
				}
				assert(this.connectionPoint!=null);
				assert(sgmt!=null);
				this.wrappedPoint = wrapPoint(
						this.connectionPoint, sgmt, isTerminal || this.lastReachableSegment);
			}
			return this.wrappedPoint;
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class SubGraphSegmentIterator implements Iterator<ST> {

		private final Iterator<ST> iterator;

		public SubGraphSegmentIterator(Iterator<ST> iter) {
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
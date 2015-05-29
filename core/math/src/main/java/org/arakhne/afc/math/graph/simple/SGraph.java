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
package org.arakhne.afc.math.graph.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.arakhne.afc.math.graph.DepthGraphIterator;
import org.arakhne.afc.math.graph.Graph;
import org.arakhne.afc.math.graph.GraphIterator;


/** This class provides a simple implementation of a graph.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.1
 */
public class SGraph
implements Graph<SGraphSegment,SGraphPoint> {

	private final Collection<SGraphSegment> segments = new ArrayList<SGraphSegment>();
	private int pointCount = 0;
	
	/**
	 */
	public SGraph() {
		//
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<SGraphSegment> iterator() {
		return Collections.unmodifiableCollection(this.segments).iterator();
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
	public int getSegmentCount() {
		return this.segments.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPointCount() {
		return this.pointCount;
	}

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
	 */
	@Override
	public GraphIterator<SGraphSegment,SGraphPoint> iterator(
			SGraphSegment starting_segment, SGraphPoint starting_point,
			boolean allowManyReplies, boolean assumeOrientedSegments) {
		if (starting_segment.getGraph()!=this
			||starting_point.getGraph()!=this) {
			throw new IllegalArgumentException();
		}
		return new GraphIterator<SGraphSegment,SGraphPoint>(
				this,
				starting_segment,
				starting_point,
				allowManyReplies,
				assumeOrientedSegments,
				0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GraphIterator<SGraphSegment, SGraphPoint> depthIterator(
			SGraphSegment startingSegment, float depth,
			float position_from_starting_point, SGraphPoint startingPoint,
			boolean allowManyReplies, boolean assumeOrientedSegments) {
		if (startingSegment.getGraph()!=this
				||startingPoint.getGraph()!=this) {
				throw new IllegalArgumentException();
			}
			return new DepthGraphIterator<SGraphSegment, SGraphPoint>(
					this,
					depth,
					startingSegment,
					startingPoint,
					allowManyReplies,
					assumeOrientedSegments);
	}
	
	/** Add the given segment. The number of points
	 * is incremented by 2.
	 * 
	 * @param segment
	 * @throws IllegalStateException if the graph is not empty.
	 */
	void add(SGraphSegment segment) {
		this.segments.add(segment);
		this.pointCount += 2;
	}
	
	/** Update the number of points in this graph with the given
	 * amount.
	 * 
	 * @param amount
	 */
	void updatePointCount(int amount) {
		this.pointCount += amount;
	}

}

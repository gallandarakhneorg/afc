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

package org.arakhne.afc.math.graph.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.graph.DepthGraphIterator;
import org.arakhne.afc.math.graph.Graph;
import org.arakhne.afc.math.graph.GraphIterator;


/** This class provides a simple implementation of a graph.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class SGraph implements Graph<SGraphSegment, SGraphPoint> {

	private final Collection<SGraphSegment> segments = new ArrayList<>();

	private int pointCount;

	/** Construct a simpole empty graph.
	 */
	public SGraph() {
		//
	}

	@Pure
	@Override
	public Iterator<SGraphSegment> iterator() {
		return Collections.unmodifiableCollection(this.segments).iterator();
	}

	@Pure
	@Override
	public GraphIterator<SGraphSegment, SGraphPoint> iterator(
			SGraphSegment starting_segment, SGraphPoint starting_point,
			boolean allowManyReplies, boolean assumeOrientedSegments) {
		if (starting_segment.getGraph() != this
				|| starting_point.getGraph() != this) {
			throw new IllegalArgumentException();
		}
		return new GraphIterator<>(
				this,
				starting_segment,
				starting_point,
				allowManyReplies,
				assumeOrientedSegments,
				0);
	}

	@Pure
	@Override
	public int getSegmentCount() {
		return this.segments.size();
	}

	@Pure
	@Override
	public int getPointCount() {
		return this.pointCount;
	}

	@Pure
	@Override
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
	public GraphIterator<SGraphSegment, SGraphPoint> depthIterator(
			SGraphSegment startingSegment, double depth,
			double position_from_starting_point, SGraphPoint startingPoint,
			boolean allowManyReplies, boolean assumeOrientedSegments) {
		if (startingSegment.getGraph() != this
				|| startingPoint.getGraph() != this) {
			throw new IllegalArgumentException();
		}
		return new DepthGraphIterator<>(
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
	 * @param segment the segment to add.
	 * @throws IllegalStateException if the graph is not empty.
	 */
	void add(SGraphSegment segment) {
		this.segments.add(segment);
		this.pointCount += 2;
	}

	/** Update the number of points in this graph with the given
	 * amount.
	 *
	 * @param amount the nbumber of points to add.
	 */
	void updatePointCount(int amount) {
		this.pointCount += amount;
	}

}

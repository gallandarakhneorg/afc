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

package org.arakhne.afc.gis.road;

import java.util.Comparator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.graph.GraphIterationElement;

/**
 * This class permits to compare two road segments.
 *
 * <p>It is used during road network iterations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
final class RoadSegmentIterationComparator implements Comparator<GraphIterationElement<RoadSegment, RoadConnection>> {

	/** Singleton of a comparator which assumes oriented segments.
	 *
	 * @see #NOT_ORIENTED_SEGMENT_SINGLETON
	 */
	public static final RoadSegmentIterationComparator ORIENTED_SEGMENT_SINGLETON = new RoadSegmentIterationComparator(true);

	/** Singleton of a comparator which assumes not-oriented segments.
	 *
	 * @see #ORIENTED_SEGMENT_SINGLETON
	 */
	public static final RoadSegmentIterationComparator NOT_ORIENTED_SEGMENT_SINGLETON = new RoadSegmentIterationComparator(false);

	private final boolean isOrientedSegments;

	/** Constructor.
	 * @param assumeOrientedSegments may be {@code true} to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is {@code false} to assume that
	 *      the end points of a segment are not distinguished.
	 */
	private RoadSegmentIterationComparator(boolean assumeOrientedSegments) {
		this.isOrientedSegments = assumeOrientedSegments;
	}

	@Pure
	@Override
	public int compare(
			GraphIterationElement<RoadSegment, RoadConnection> o1,
			GraphIterationElement<RoadSegment, RoadConnection> o2) {
		assert o1 != null && o2 != null;
		if (o1 == o2) {
			return 0;
		}
		final int cmp = compareSegments(o1.getSegment(), o2.getSegment());
		if (!this.isOrientedSegments || cmp != 0) {
			return cmp;
		}
		return compareConnections(o1.getPoint(), o2.getPoint());
	}

	/** Compare the two given segments.
	 *
	 * @param s1 the first segment.
	 * @param s2 the second segment.
	 * @return {@code -1} if {@code s1} is lower than {@code s2},
	 *     {@code 1} if {@code s1} is greater than {@code s2},
	 *     otherwise {@code 0}.
	 */
	@SuppressWarnings("static-method")
	@Pure
	protected int compareSegments(RoadSegment s1, RoadSegment s2) {
		assert s1 != null && s2 != null;
		return s1.getUUID().compareTo(s2.getUUID());
	}

	/** Compare the two given entry points.
	 *
	 * @param p1 the first connection.
	 * @param p2 the second connection.
	 * @return {@code -1} if {@code p1} is lower than {@code p2},
	 *     {@code 1} if {@code p1} is greater than {@code p2},
	 *     otherwise {@code 0}.
	 */
	@SuppressWarnings("static-method")
	@Pure
	protected int compareConnections(RoadConnection p1, RoadConnection p2) {
		assert p1 != null && p2 != null;
		return p1.getUUID().compareTo(p2.getUUID());
	}

	/** Replies if this comparator is assuming that
	 * segments are oriented.
	 *
	 * @return {@code true} if segments are oriented,
	 *     otherwise {@code false}
	 */
	@Pure
	public boolean isOrientedSegments() {
		return this.isOrientedSegments;
	}

}

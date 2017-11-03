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

package org.arakhne.afc.gis.road;

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.graph.GraphIterationElementComparator;

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
final class RoadSegmentIterationComparator extends GraphIterationElementComparator<RoadSegment, RoadConnection> {

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

	/** Constructor.
	 * @param assumeOrientedSegments may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *      the end points of a segment are not distinguished.
	 */
	private RoadSegmentIterationComparator(boolean assumeOrientedSegments) {
		super(assumeOrientedSegments);
	}

	@Override
	protected int compareSegments(RoadSegment s1, RoadSegment s2) {
		assert s1 != null && s2 != null;
		return GISPrimitive.COMPARATOR.compare(s1, s2);
	}

	@Override
	protected int compareConnections(RoadConnection p1, RoadConnection p2) {
		assert p1 != null && p2 != null;
		return p1.compareTo(p2);
	}

}

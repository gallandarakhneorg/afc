/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import java.util.Comparator;

import org.eclipse.xtext.xbase.lib.Pure;

/** Compare two iteration elements.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GraphIterationElementComparator<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>>
		implements Comparator<GraphIterationElement<ST, PT>> {

	private final boolean assumeOrientedSegments;

	/** Constructor.
	 * @param assumeOrientedSegments1 may be <code>true</code> to assume that the same segment has two different
	 *     instances for graph iteration: the first instance is associated the first point of the segment and the second
	 *     instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 *     the end points of a segment are not distinguished.
	 */
	public GraphIterationElementComparator(boolean assumeOrientedSegments1) {
		this.assumeOrientedSegments = assumeOrientedSegments1;
	}

	@Pure
	@Override
	public int compare(
			GraphIterationElement<ST, PT> o1,
			GraphIterationElement<ST, PT> o2) {
		assert o1 != null && o2 != null;
		if (o1 == o2) {
			return 0;
		}
		final int cmp = compareSegments(o1.getSegment(), o2.getSegment());
		if (!this.assumeOrientedSegments || cmp != 0) {
			return cmp;
		}
		return compareConnections(o1.getPoint(), o2.getPoint());
	}

	/** Compare the two given segments.
	 *
	 * @param s1 the first segment.
	 * @param s2 the second segment.
	 * @return <code>-1</code> if {@code s1} is lower than {@code s2},
	 *     <code>1</code> if {@code s1} is greater than {@code s2},
	 *     otherwise <code>0</code>.
	 */
	@Pure
	protected int compareSegments(ST s1, ST s2) {
		assert s1 != null && s2 != null;
		return s1.hashCode() - s2.hashCode();
	}

	/** Compare the two given entry points.
	 *
	 * @param p1 the first connection.
	 * @param p2 the second connection.
	 * @return <code>-1</code> if {@code p1} is lower than {@code p2},
	 *     <code>1</code> if {@code p1} is greater than {@code p2},
	 *     otherwise <code>0</code>.
	 */
	@Pure
	protected int compareConnections(PT p1, PT p2) {
		assert p1 != null && p2 != null;
		return p1.hashCode() - p2.hashCode();
	}

	/** Replies if this comparator is assuming that
	 * segments are oriented.
	 *
	 * @return <code>true</code> if segments are oriented,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isOrientedSegments() {
		return this.assumeOrientedSegments;
	}

}

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

/** This interface represents an dynamic updater of the iterator depth that is used when iterating on graph elements.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 16.0
 */
@FunctionalInterface
public interface DynamicDepthUpdater<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> {

	/** Invoked when a segment was added to a subgraph.
	 *
	 * @param previousSegment is the previous element that permits to reach this object during an iteration
	 * @param segment is the current segment
	 * @param point is the point on which the iteration arrived on the current segment.
	 * @param distanceToReach is the distance that is already consumed to reach the segment.
	 * @param distanceToConsume is the rest of distance to consume including the segment.
	 * @return the new depth.
	 */
	double updateDepth(ST previousSegment, ST segment, PT point,
			double distanceToReach, double distanceToConsume);

}

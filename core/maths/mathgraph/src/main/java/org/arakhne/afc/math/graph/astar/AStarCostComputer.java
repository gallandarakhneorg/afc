/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.graph.astar;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.math.graph.GraphSegment;

/** This interface provides a way to the {@link AStar A* algorithm}
 * to compute the costs of the graph points and the segments.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see AStar
 */
public interface AStarCostComputer<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> {

	/** Compute and replies the cost to traverse the given graph point.
	 *
	 * @param pt the point.
	 * @return the cost to traverse the point.
	 */
	@Pure
	default double computeCostFor(PT pt) {
		return 0;
	}

	/** Compute and replies the cost to traverse the given graph segment.
	 *
	 * @param segment the segment.
	 * @return the cost to traverse the segment.
	 */
	@Pure
	default double computeCostFor(ST segment) {
		return segment.getLength();
	}

}

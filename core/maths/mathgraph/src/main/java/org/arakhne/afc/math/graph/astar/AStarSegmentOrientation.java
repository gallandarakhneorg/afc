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

package org.arakhne.afc.math.graph.astar;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.math.graph.GraphPoint.GraphPointConnection;
import org.arakhne.afc.math.graph.GraphSegment;

/** This interface provides a way to the {@link AStar A* algorithm}
 * to retreive the orientation of a segment.
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
public interface AStarSegmentOrientation<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> {

	/** Replies if the given segment {@code s} is traversable
	 * from the given connection point {@code p}.
	 *
	 * <p>By default, this function uses the segment orientation tool passed as parameter
	 * of the constructor, or replies <code>true</code>.
	 *
	 * @param entrySegment is the last traversed segment.
	 * @param connection is the graph connection to test.
	 * @return <code>true</code> if the segment {@code s} is traversable,
	 *     otherwise <code>false</code>.
	 */
	@Pure
	default boolean isTraversable(ST entrySegment, GraphPointConnection<PT, ST> connection) {
		return connection != null && connection.isSegmentStartConnected();
	}

}

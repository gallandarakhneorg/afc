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

package org.arakhne.afc.math.graph.astar;

import org.arakhne.afc.math.graph.GraphSegment;

/** This interface provides a way to replace the segments of the
 * paths found by the {@link AStar A* algorithm}.
 *
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see AStar
 */
public interface AStarSegmentReplacer<ST extends GraphSegment<ST, ?>> {

	/** Replies a instance of segment to add inside the shortest path
	 * in place of the given segment. If this function replies, {@code null}
	 * the given segment is added.
	 *
	 * @param index is the position in the shortest path of the segment to replace
	 * @param segment is the segment to replace.
	 * @return the replacement, or {@code null}.
	 */
	default ST replaceSegment(int index, ST segment) {
		return null;
	}

}

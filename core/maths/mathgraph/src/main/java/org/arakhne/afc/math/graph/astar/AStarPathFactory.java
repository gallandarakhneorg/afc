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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.graph.GraphPath;
import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.math.graph.GraphSegment;

/** This interface provides a mean to create a path factory
 * for the {@link AStar A* algorithm}.
 *
 * @param <GP> is the type of the path to create.
 * @param <ST> is the type of the segments in the path.
 * @param <PT> is the type of the nodes in the path.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see AStar
 */
public interface AStarPathFactory<GP extends GraphPath<GP, ST, PT>, ST extends GraphSegment<ST, PT>,
		PT extends GraphPoint<PT, ST>> {

	/** Create an empty path.
	 *
	 * @param startPoint is the first point in the path.
	 * @param segment is the first connection to follow.
	 * @return the path instance.
	 */
	@Pure
	GP newPath(PT startPoint, ST segment);

	/** Add the given segment into the given path.
	 *
	 * @param path is the path to build.
	 * @param segment is the segment to add.
	 * @return {@code true} if the segment was added;
	 *     otherwise {@code false}.
	 */
	default boolean addToPath(GP path, ST segment) {
		assert path != null;
		assert segment != null;
		return path.add(segment);
	}

}

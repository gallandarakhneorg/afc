/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

/** This interface provides an heuristic evaluation
 * of a path for the {@link AStar A* algorithm}.
 *
 * @param <PT> is the type of node in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see AStar
 */
@FunctionalInterface
public interface AStarHeuristic<PT extends GraphPoint<PT, ?>> {

	/** Evaluate the distance between two points in the graph.
	 *
	 * @param p1 the first point.
	 * @param p2 the second point.
	 * @return the evaluated distance between {@code p1} and {@code p2}.
	 */
	@Pure
	double evaluate(PT p1, PT p2);

}

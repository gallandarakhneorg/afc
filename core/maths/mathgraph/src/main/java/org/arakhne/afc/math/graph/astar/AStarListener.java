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

import java.util.EventListener;
import java.util.List;

import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.math.graph.GraphSegment;

/** This interface is implemented by
 * the listeners on the progression of
 * the {@link AStar A* algorithm}.
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
public interface AStarListener<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> extends EventListener {

	/** Invoked when the A* algorithm has started.
	 *
	 * @param startPoint is the starting node.
	 * @param endPoint is the target node.
	 */
	default void algorithmStarted(AStarNode<ST, PT> startPoint, PT endPoint) {
		//
	}

	/** Invoked when a node is removed from the open list.
	 *
	 * @param node is the node just removed from the open list.
	 * @param openList is the current state of the open list.
	 */
	default void nodeConsumed(AStarNode<ST, PT> node, List<AStarNode<ST, PT>> openList) {
		//
	}

	/** Invoked when a node is put in the open list.
	 *
	 * @param node is the node just added into the open list.
	 * @param openList is the current state of the open list.
	 */
	default void nodeOpened(AStarNode<ST, PT> node, List<AStarNode<ST, PT>> openList) {
		//
	}

	/** Invoked when the position of a node in the open list has changed.
	 *
	 * @param node is the node moved into the open list.
	 * @param openList is the current state of the open list.
	 */
	default void nodeReopened(AStarNode<ST, PT> node, List<AStarNode<ST, PT>> openList) {
		//
	}

	/** Invoked when a node is put in the close list.
	 *
	 * @param node is the node just added into the close list.
	 * @param closeList is the current state of the close list.
	 */
	default void nodeClosed(AStarNode<ST, PT> node, List<AStarNode<ST, PT>> closeList) {
		//
	}

	/** Invoked when the A* algorithm has finished.
	 *
	 * @param closeList is the close list.
	 */
	default void algorithmEnded(List<AStarNode<ST, PT>> closeList) {
		//
	}

}

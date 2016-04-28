/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
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
 * @see AStar
 * @since 13.0
 */
public interface AStarListener<ST extends GraphSegment<ST,PT>, PT extends GraphPoint<PT,ST>> extends EventListener {

	/** Invoked when the A* algorithm has started.
	 * 
	 * @param startPoint is the starting node.
	 * @param endPoint is the target node.
	 */
	default void algorithmStarted(AStarNode<ST,PT> startPoint, PT endPoint) {
		//
	}
	
	/** Invoked when a node is removed from the open list.
	 * 
	 * @param node is the node just removed from the open list.
	 * @param openList is the current state of the open list.
	 */
	default void nodeConsumed(AStarNode<ST,PT> node, List<AStarNode<ST,PT>> openList) {
		//
	}

	/** Invoked when a node is put in the open list.
	 * 
	 * @param node is the node just added into the open list.
	 * @param openList is the current state of the open list.
	 */
	default void nodeOpened(AStarNode<ST,PT> node, List<AStarNode<ST,PT>> openList) {
		//
	}

	/** Invoked when the position of a node in the open list has changed.
	 * 
	 * @param node is the node moved into the open list.
	 * @param openList is the current state of the open list.
	 */
	default void nodeReopened(AStarNode<ST,PT> node, List<AStarNode<ST,PT>> openList) {
		//
	}

	/** Invoked when a node is put in the close list.
	 * 
	 * @param node is the node just added into the close list.
	 * @param closeList is the current state of the close list.
	 */
	default void nodeClosed(AStarNode<ST,PT> node, List<AStarNode<ST,PT>> closeList) {
		//
	}

	/** Invoked when the A* algorithm has finished.
	 * 
	 * @param closeList is the close list.
	 */
	default void algorithmEnded(List<AStarNode<ST,PT>> closeList) {
		//
	}

}

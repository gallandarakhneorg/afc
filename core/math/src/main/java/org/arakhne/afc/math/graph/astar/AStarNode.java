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

package org.arakhne.afc.math.graph.astar;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.math.graph.GraphSegment;

/** This interface provides services for an A* node.
 * This interface may be implemented by the
 * {@link GraphPoint} to have better performances
 * for the A* algorithm.
 *
 * @param <ST> is the type of the graph segment associated to this A* node.
 * @param <PT> is the type of the graph point associated to this A* node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface AStarNode<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> {

	/** Replies the graph point associated to this AStarNode.
	 *
	 * @return the graph point associated to this AStarNode.
	 */
	@Pure
	PT getGraphPoint();

	/** Replies the segments which are traversable from this node.
	 *
	 * @return the traversable segments.
	 */
	@Pure
	Iterable<ST> getGraphSegments();

	/** Replies the connection to reach the node.
	 *
	 * @return the connection to reach the node, or <code>null</code> if none.
	 */
	@Pure
	ST getArrivalConnection();

	/** Set the connection to reach the node.
	 *
	 * @param connection is the new arrival connection.
	 * @return the connection to reach the node, or <code>null</code> if none.
	 */
	ST setArrivalConnection(ST connection);

	/** Replies the cost to reach the node.
	 *
	 * @return the cost to reach the node.
	 */
	@Pure
	double getCost();

	/** Set the cost to reach the node.
	 *
	 * @param cost is the new cost.
	 * @return the cost to reach the node.
	 */
	double setCost(double cost);

	/** Replies the cost from the node to the target point.
	 *
	 * @return the cost from the node to the target point.
	 */
	@Pure
	double getEstimatedCost();

	/** Set the cost from the node to the target point.
	 *
	 * @param cost is the new estimated cost.
	 * @return the cost from the node to the target point.
	 */
	double setEstimatedCost(double cost);

	/** Replies the cost of the overall path.
	 * It is the sum of {@link #getCost()} and
	 * {@link #getEstimatedCost()}.
	 *
	 * @return the cost of the path.
	 */
	@Pure
	double getPathCost();

}

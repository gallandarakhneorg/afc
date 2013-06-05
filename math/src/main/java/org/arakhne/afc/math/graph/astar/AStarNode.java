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
 * @since 4.0
 */
public interface AStarNode<ST extends GraphSegment<ST,PT>, PT extends GraphPoint<PT,ST>> {

	/** Replies the graph point associated to this AStarNode.
	 * 
	 * @return the graph point associated to this AStarNode.
	 */
	public PT getGraphPoint();
	
	/** Replies the segments which are traversable from this node.
	 * 
	 * @return the traversable segments.
	 */
	public Iterable<ST> getGraphSegments();

	/** Replies the connection to reach the node.
	 * 
	 * @return the connection to reach the node, or <code>null</code> if none.
	 */
	public ST getArrivalConnection();
	
	/** Set the connection to reach the node.
	 * 
	 * @param connection is the new arrival connection.
	 * @return the connection to reach the node, or <code>null</code> if none.
	 */
	public ST setArrivalConnection(ST connection);

	/** Replies the cost to reach the node.
	 * 
	 * @return the cost to reach the node.
	 */
	public float cost();
	
	/** Set the cost to reach the node.
	 * 
	 * @param cost is the new cost.
	 * @return the cost to reach the node.
	 */
	public float setCost(float cost);

	/** Replies the cost from the node to the target point.
	 * 
	 * @return the cost from the node to the target point.
	 */
	public float estimatedCost();
	
	/** Set the cost from the node to the target point.
	 * 
	 * @param cost is the new estimated cost.
	 * @return the cost from the node to the target point.
	 */
	public float setEstimatedCost(float cost);

	/** Replies the cost of the overall path.
	 * It is the sum of {@link #cost()} and
	 * {@link #estimatedCost()}.
	 * 
	 * @return the cost of the path.
	 */
	public float pathCost();

}

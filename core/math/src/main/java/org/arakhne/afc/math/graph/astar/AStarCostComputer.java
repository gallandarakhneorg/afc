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
import org.eclipse.xtext.xbase.lib.Pure;


/** This interface provides a way to the {@link AStar A* algorithm}
 * to compute the costs of the graph points and the segments.
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
public interface AStarCostComputer<ST extends GraphSegment<ST,PT>, PT extends GraphPoint<PT,ST>> {

	/** Compute and replies the cost to traverse the given graph point.
	 * 
	 * @param p
	 * @return the cost to traverse the point.
	 */
	@Pure
	public double computeCostFor(PT p);

	/** Compute and replies the cost to traverse the given graph segment.
	 * 
	 * @param s
	 * @return the cost to traverse the segment.
	 */
	@Pure
	public double computeCostFor(ST s);

}

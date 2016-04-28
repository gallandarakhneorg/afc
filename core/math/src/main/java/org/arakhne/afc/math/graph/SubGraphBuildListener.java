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
package org.arakhne.afc.math.graph;

/** This interface is a listener invoked by the subgraph builder
 * each time a new subgraph segment was reached.
 * 
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface SubGraphBuildListener<ST extends GraphSegment<ST,PT>,PT extends GraphPoint<PT,ST>> {

	/** Invoked when a segment was added to a subgraph.
	 * 
	 * @param graph is the subgraph under building.
	 * @param elementDescription is the added segment.
	 */
	default void segmentAdded(SubGraph<ST,PT,?> graph, GraphIterationElement<ST,PT> elementDescription) {
		//
	}
	
	/** Invoked when a terminal point was added to a subgraph.
	 * <p>
	 * A terminal point is a point which corresponds to the bounds
	 * of the subgraph.
	 * 
	 * @param graph is the subgraph under building.
	 * @param point is the added terminal point.
	 * @param arrivingSegment is the segment from which this point is reached.
	 */
	default void terminalPointReached(SubGraph<ST,PT,?> graph, PT point, ST arrivingSegment) {
		//
	}

	/** Invoked when a non terminal point was added to a subgraph.
	 * <p>
	 * A terminal point is a point which corresponds to the bounds
	 * of the subgraph.
	 * 
	 * @param graph is the subgraph under building.
	 * @param point is the added terminal point.
	 * @param arrivingSegment is the segment from which this point is reached.
	 */
	default void nonTerminalPointReached(SubGraph<ST,PT,?> graph, PT point, ST arrivingSegment) {
		//
	}

}

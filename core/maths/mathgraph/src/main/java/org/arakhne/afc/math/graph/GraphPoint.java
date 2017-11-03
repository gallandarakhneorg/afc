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

package org.arakhne.afc.math.graph;

import org.eclipse.xtext.xbase.lib.Pure;

/** This interface representes a graph's point.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface GraphPoint<PT extends GraphPoint<PT, ST>, ST extends GraphSegment<ST, PT>>
		extends Comparable<GraphPoint<PT, ST>> {

	/** Replies the count of segments connected to this point.
	 *
	 * @return the count of segments connected to this point.
	 */
	@Pure
	int getConnectedSegmentCount();

	/** Replies the list of segments connected to this point.
	 *
	 * @return the list of segments connected to this point.
	 */
	@Pure
	Iterable<ST> getConnectedSegments();

	/** Replies the list of segments connected to this point.
	 *
	 * <p>If the graph point implementation is supporting an ordered
	 * list of segment, it will reply the segments starting from
	 * the given segment. If the implementation does not
	 * support any order, it will reply the same as
	 * {@link #getConnectedSegments()}.
	 *
	 * @param startingPoint the startint point.
	 * @return the list of segments connected to this point.
	 */
	@Pure
	Iterable<ST> getConnectedSegmentsStartingFrom(ST startingPoint);

	/** Replies the list of segment connections for this point.
	 *
	 * @return the list of segments connected to this point.
	 */
	@Pure
	Iterable<? extends GraphPointConnection<PT, ST>> getConnections();

	/** Replies the list of segment connections for this point.
	 *
	 * <p>If the graph point implementation is supporting an ordered
	 * list of segment, it will reply the segments starting from
	 * the given segment. If the implementation does not
	 * support any order, it will reply the same as
	 * {@link #getConnections()}.
	 *
	 * @param startingPoint the starting point.
	 * @return the list of segments connected to this point.
	 */
	@Pure
	Iterable<? extends GraphPointConnection<PT, ST>> getConnectionsStartingFrom(ST startingPoint);

	/** Replies if the specified segment was connected to
	 * this point.
	 *
	 * @param segment the starting point.
	 * @return <code>true</code> if the given segment is connected to this node,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean isConnectedSegment(ST segment);

	/** Replies if this point is a final connection point ie,
	 * a point connected to only one segment.
	 *
	 * @return <code>true</code> if zero or one segment was connected
	 *     to this point, otherwhise <code>false</code>
	 */
	@Pure
	boolean isFinalConnectionPoint();

	/** Describes a connection pair composed of the graph point
	 * and one graph segment. The GraphPointConnection is
	 * describes how a segment is connected to the graph point.
	 * Because it is an inner-class, the graph point replied
	 * should be the enclosing GraphPoint instance.
	 * Basically, any instance of the GraphPointConnection
	 * is instanced when mandatory, and they are not stored
	 * in any data structure.
	 *
	 * @param <PT> is the type of node in the graph
	 * @param <ST> is the type of edge in the graph
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public interface GraphPointConnection<PT extends GraphPoint<PT, ST>, ST extends GraphSegment<ST, PT>> {

		/** Replies the connected segment.
		 *
		 * @return the connected segment.
		 */
		@Pure
		ST getGraphSegment();

		/** Replies the connection point.
		 *
		 * @return the connection point.
		 */
		@Pure
		PT getGraphPoint();

		/** Replies if the connected segment is connected
		 * by its start point or not.
		 *
		 * @return <code>true</code> if the segment replied
		 *     by {@link #getGraphSegment()} is connected
		 *     to the point replied by {@link #getGraphPoint()}
		 *     by its start end, otherwise <code>false</code>
		 */
		@Pure
		boolean isSegmentStartConnected();

	}

}

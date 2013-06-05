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

/** This interface representes a graph's point.
 * 
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface GraphPoint<PT extends GraphPoint<PT,ST>, ST extends GraphSegment<ST,PT>> extends Comparable<GraphPoint<PT,ST>> {

    /** Replies the count of segments connected to this point.
     * 
     * @return the count of segments connected to this point.
     */
	public int getConnectedSegmentCount();

	/** Replies the list of segments connected to this point.
     * 
     * @return the list of segments connected to this point.
     */
	public Iterable<ST> getConnectedSegments();
	
    /** Replies the list of segments connected to this point.
     * <p>
     * If the graph point implementation is supporting an ordered
     * list of segment, it will reply the segments starting from
     * the given segment. If the implementation does not
     * support any order, it will reply the same as
     * {@link #getConnectedSegments()}.
     * 
     * @param startingPoint
     * @return the list of segments connected to this point.
     */
	public Iterable<ST> getConnectedSegmentsStartingFrom(ST startingPoint);

    /** Replies the list of segment connections for this point.
     * 
     * @return the list of segments connected to this point.
     */
	public Iterable<? extends GraphPointConnection<PT,ST>> getConnections();

	/** Replies the list of segment connections for this point.
     * <p>
     * If the graph point implementation is supporting an ordered
     * list of segment, it will reply the segments starting from
     * the given segment. If the implementation does not
     * support any order, it will reply the same as
     * {@link #getConnections()}.
     * 
     * @param startingPoint
     * @return the list of segments connected to this point.
     */
	public Iterable<? extends GraphPointConnection<PT,ST>> getConnectionsStartingFrom(ST startingPoint);

	/** Replies if the specified segment was connected to
     * this point.
     * 
     * @param segment
     * @return <code>true</code> if the given segment is connected to this node,
     * otherwise <code>false</code>
     */
	public boolean isConnectedSegment(ST segment);

    /** Replies if this point is a final connection point ie,
     * a point connected to only one segment.
     * 
     * @return <code>true</code> if zero or one segment was connected
     * to this point, otherwhise <code>false</code>
     */
	public boolean isFinalConnectionPoint();

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
     * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
     * @version $FullVersion$
     * @mavengroupid fr.utbm.set.sfc
     * @mavenartifactid setgraph
     */
	public static interface GraphPointConnection<PT extends GraphPoint<PT,ST>, ST extends GraphSegment<ST,PT>> {

		/** Replies the connected segment.
		 * 
		 * @return the connected segment.
		 */
		public ST getGraphSegment();
		
		/** Replies the connection point.
		 * 
		 * @return the connection point.
		 */
		public PT getGraphPoint();
		
		/** Replies if the connected segment is connected
		 * by its start point or not.
		 * 
		 * @return <code>true</code> if the segment replied
		 * by {@link #getGraphSegment()} is connected
		 * to the point replied by {@link #getGraphPoint()}
		 * by its start end, otherwise <code>false</code>
		 */
		public boolean isSegmentStartConnected();

	}

}

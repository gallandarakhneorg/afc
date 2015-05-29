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

import java.util.Iterator;
import java.util.List;

/**
 * This class describes a path inside a graph.
 * 
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface GraphSegmentList<ST extends GraphSegment<ST,PT>,PT extends GraphPoint<PT,ST>> extends List<ST> {
	
	/**
	 * Replies an iterator on the path's points.
	 * 
	 * @return an iterator on the path's points.
	 */
	public Iterator<PT> pointIterator();

	/**
	 * Replies an iterator on the path's points.
	 * 
	 * @return an iterator on the path's points.
	 */
	public Iterable<PT> points();

	/**
	 * Add the segment starting from the given point.
	 * 
	 * @param segment is the segment to add
	 * @param point is the point at which the path is starting to go through the given segment.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean add(ST segment, PT point);

	/** Replies the last point.
	 * 
	 * @return the last point.
	 */
	public PT getLastPoint();

	/** Replies the first point.
	 * 
	 * @return the first point.
	 */
	public PT getFirstPoint();

	/** Replies the last segment.
	 * 
	 * @return the last segment or <code>null</code>.
	 */
	public ST getLastSegment();

	/** Replies the antepenulvian segment.
	 * 
	 * @return the antepenulvian segment or <code>null</code>.
	 */
	public ST getAntepenulvianSegment();

	/** Replies the second element of the path if it exists.
	 * 
	 * @return the second element of the path if it exists.
	 */
	public ST getSecondSegment();
	
	/** Replies the first segment.
	 * 
	 * @return the first segment or <code>null</code>.
	 */
	public ST getFirstSegment();

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * not be removed.
	 *
	 * @param o
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeBefore(ST o);

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * not be removed.
	 * 
	 * @param o
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeBeforeLast(ST o);

	/** Remove the path's elements before the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * not be removed.
	 * 
	 * @param o is the segment to remove
	 * @param p is the point on which the segment was connected
	 * as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeBeforeLast(ST o, PT p);

	/** Remove the path's elements before the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * not be removed.
	 * 
	 * @param o is the segment to remove
	 * @param p is the point on which the segment was connected
	 * as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeBefore(ST o, PT p);

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * also be removed.
	 * 
	 * @param o
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeUntil(ST o);

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * also be removed.
	 * 
	 * @param o
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeUntilLast(ST o);

}

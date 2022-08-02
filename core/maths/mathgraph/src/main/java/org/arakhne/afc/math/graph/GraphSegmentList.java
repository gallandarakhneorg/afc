/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import java.util.Iterator;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class describes a path inside a graph.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface GraphSegmentList<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> extends List<ST> {

	/**
	 * Replies an iterator on the path's points.
	 *
	 * @return an iterator on the path's points.
	 */
	@Pure
	Iterator<PT> pointIterator();

	/**
	 * Replies an iterator on the path's points.
	 *
	 * @return an iterator on the path's points.
	 */
	@Pure
	Iterable<PT> points();

	/**
	 * Add the segment starting from the given point.
	 *
	 * @param segment is the segment to add
	 * @param point is the point at which the path is starting to go through the given segment.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	boolean add(ST segment, PT point);

	/** Replies the last point.
	 *
	 * @return the last point.
	 */
	@Pure
	PT getLastPoint();

	/** Replies the first point.
	 *
	 * @return the first point.
	 */
	@Pure
	PT getFirstPoint();

	/** Replies the last segment.
	 *
	 * @return the last segment or <code>null</code>.
	 */
	@Pure
	ST getLastSegment();

	/** Replies the antepenulvian segment.
	 *
	 * @return the antepenulvian segment or <code>null</code>.
	 */
	@Pure
	ST getAntepenulvianSegment();

	/** Replies the second element of the path if it exists.
	 *
	 * @return the second element of the path if it exists.
	 */
	@Pure
	ST getSecondSegment();

	/** Replies the first segment.
	 *
	 * @return the first segment or <code>null</code>.
	 */
	@Pure
	ST getFirstSegment();

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * not be removed.
	 *
	 * @param obj the reference object.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	boolean removeBefore(ST obj);

	/** Remove the path's elements before the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * not be removed.
	 *
	 * @param obj the refrence segment.
	 * @param pt is the point on which the segment was connected
	 *     as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	boolean removeBefore(ST obj, PT pt);

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * not be removed.
	 *
	 * @param obj the reference object
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	boolean removeBeforeLast(ST obj);

	/** Remove the path's elements before the
	 * specified one which is starting
	 * at the specified point. The specified element will
	 * not be removed.
	 *
	 * @param obj the refrence segment.
	 * @param pt is the point on which the segment was connected
	 *     as its first point.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	boolean removeBeforeLast(ST obj, PT pt);

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * also be removed.
	 *
	 * @param obj the refrence segment.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	boolean removeUntil(ST obj);

	/** Remove the path's elements before the
	 * specified one. The specified element will
	 * also be removed.
	 *
	 * @param obj the refrence segment.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	boolean removeUntilLast(ST obj);

}

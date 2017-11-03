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

import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.ReflectionUtil;

/** Describe an element of the graph during an iteration.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GraphIterationElement<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> implements Comparable<Object> {

	/** Previous element in the iteration.
	 */
	ST previousSegment;

	/** Current segment in the iteration.
	 */
	final ST currentSegment;

	/** Connection point between the previous segment and the current segment.
	 */
	final PT connectionPoint;

	/** Distance that was consumed for reaching the connection point.
	 */
	double distanceToReach;

	/** Indicates if the current segment is a cul-de-sac.
	 */
	boolean culDeSac;

	/** Indicates if the segment is the last segment to be reachable in the current exploration branch.
	 * This may indicates that the iteration algorithm is not allowed to go deeper in exploration from
	 * the current segment.
	 */
	boolean lastReachableSegment;

	/** Distance amount which was not consumed.
	 */
	final double distanceToConsume;

	/** Indicates if this element was replied by the iterator.
	 */
	boolean replied;

	/** Constructor.
	 * @param previousSegment1 is the previous element that permits to reach this object during an iteration
	 * @param segment is the current segment
	 * @param point is the point on which the iteration arrived on the current segment.
	 * @param distanceToReach1 is the distance that is already consumed to reach the segment.
	 * @param distanceToConsume1 is the distance to consume including this segment length.
	 */
	GraphIterationElement(ST previousSegment1, ST segment, PT point, double distanceToReach1, double distanceToConsume1) {
		assert segment != null;
		assert point != null;
		this.previousSegment = previousSegment1;
		this.currentSegment = segment;
		this.connectionPoint = point;
		this.distanceToReach = distanceToReach1;

		final PT otherPoint = segment.getOtherSidePoint(point);
		this.culDeSac = (otherPoint != null) && (otherPoint != point) && otherPoint.isFinalConnectionPoint();

		this.lastReachableSegment = this.culDeSac;

		this.distanceToConsume = distanceToConsume1;
	}

	/** Replies the segment from which the iterator arrived on the current segment.
	 *
	 * @return the segment from which the iterator arrived on the current segment.
	 */
	@Pure
	public ST getPreviousSegment() {
		return this.previousSegment;
	}

	/** Replies the segment.
	 *
	 * @return the segment.
	 */
	@Pure
	public ST getSegment() {
		return this.currentSegment;
	}

	/** Replies the entry point on the segment.
	 *
	 * @return the entry point on the segment.
	 */
	@Pure
	public PT getPoint() {
		return this.connectionPoint;
	}

	/** Replies the distance needed to reach the segment.
	 *
	 * @return the distance to reach the starting point of the segment.
	 *     The value could be negative in case the iterations start from
	 *     inside the segment.
	 */
	@Pure
	public final double getDistanceToReachSegment() {
		return this.distanceToReach;
	}

	@Pure
	@Override
	public String toString() {
		return ReflectionUtil.toString(this);
	}

	@Pure
	@Override
	public final boolean equals(Object obj) {
		return compareTo(obj) == 0;
	}

	@Pure
	@Override
	public int hashCode() {
		return Objects.hashCode(this.currentSegment);
	}

	@Pure
	@Override
	public final int compareTo(Object obj) {
		if (obj instanceof GraphIterationElement<?, ?>) {
			final GraphSegment<?, ?> sgmt = ((GraphIterationElement<?, ?>) obj).getSegment();
			return compareSegments(this.currentSegment, sgmt);
		}
		if (obj instanceof GraphSegment<?, ?>) {
			return compareSegments(this.currentSegment, (GraphSegment<?, ?>) obj);
		}
		final int h1 = (obj == null) ? 0 : obj.hashCode();
		final int h2 = (this.currentSegment == null) ? 0 : this.currentSegment.hashCode();
		return h2 - h1;
	}

	/** Compare the two given segments.
	 *
	 * @param firstSegment the first segment to compare to the second segment.
	 * @param secondSegment the second segment to compare to the first segment.
	 * @return <code>-1</code> if {@code firstSegment} is lower than {@code secondSegment},
	 *     <code>1</code> if {@code firstSegment} is greater than {@code secondSegment},
	 *     or <code>0</code> if {@code firstSegment} is equal to {@code secondSegment}.
	 */
	@Pure
	@SuppressWarnings({ "unchecked", "rawtypes", "static-method" })
	protected int compareSegments(GraphSegment<?, ?> firstSegment, GraphSegment<?, ?> secondSegment) {
		if (firstSegment instanceof Comparable) {
			try {
				return ((Comparable) firstSegment).compareTo(secondSegment);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable e) {
				//
			}
		}
		return firstSegment.hashCode() - secondSegment.hashCode();
	}

	/** Replies if this segment is a cul-de-sac ie if the next
	 * connection point is only connected to this segment.
	 *
	 * @return <code>true</code> if this segment is a cul-de-sac, otherwise <code>false</code>
	 */
	@Pure
	public final boolean isCulDeSac() {
		return this.culDeSac;
	}

	/** Replies if this segment is the last which will be reached
	 * by the iterator. It means that the iterator will not pass
	 * this segment and continue the iterations on the following graph branches.
	 *
	 * <p>Of course a segment with a cul-de-sac end is always assumed as a terminal segment.
	 *
	 * @return <code>true</code> if this segment is terminal, otherwise <code>false</code>
	 */
	@Pure
	public final boolean isTerminalSegment() {
		return this.culDeSac || this.lastReachableSegment;
	}

	/** Set the flag that indicating if this segment is the last which will be reached
	 * by the iterator. It means that the iterator will not pass
	 * this segment and continue the iterations on the following graph branches.
	 *
	 * @param isTerminal indicates if the current segment is terminal.
	 */
	final void setTerminalSegment(boolean isTerminal) {
		this.lastReachableSegment = isTerminal;
	}

}

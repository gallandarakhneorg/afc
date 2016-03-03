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
public class GraphIterationElement<ST extends GraphSegment<ST,PT>,PT extends GraphPoint<PT,ST>> implements Comparable<Object> {
	
	/**
	 */
	ST previousSegment;

	/**
	 */
	final ST currentSegment;
	
	/**
	 */
	final PT connectionPoint;
	
	/**
	 */
	double distanceToReach;
	
	/**
	 */
	boolean culDeSac;
	
	/**
	 */
	boolean lastReachableSegment;

	/**
	 * Distance amount which was not consumed.
	 */
	final double distanceToConsume;
	
	/** Indicates if this element was replied by the iterator.
	 */
	boolean replied = false;

	/**
	 * @param previousSegment1 is the previous element that permits to reach this object during an iteration
	 * @param segment is the current segment
	 * @param point is the point on which the iteration arrived on the current segment.
	 * @param distanceToReach1 is the distance that is already consumed to reach the segment.
	 * @param distanceToConsume1 is the distance to consume including this segment length.
	 */
	GraphIterationElement(ST previousSegment1, ST segment, PT point, double distanceToReach1, double distanceToConsume1) {
		assert(segment!=null);
		assert(point!=null);
		this.previousSegment = previousSegment1;
		this.currentSegment = segment;
		this.connectionPoint = point;
		this.distanceToReach = distanceToReach1;
		
		PT otherPoint = segment.getOtherSidePoint(point);
		this.culDeSac = ((otherPoint!=null)&&(otherPoint!=point)&&(otherPoint.isFinalConnectionPoint()));

		this.lastReachableSegment = this.culDeSac;
		
		this.distanceToConsume = distanceToConsume1;
	}
	
	/** Replies the segment from which the iterator arrived on the current segment.
	 * 
	 * @return the segment from which the iterator arrived on the current segment.
	 */
	public ST getPreviousSegment() {
		return this.previousSegment;
	}

	/** Replies the segment.
	 * 
	 * @return the segment.
	 */
	public ST getSegment() {
		return this.currentSegment;
	}
	
	/** Replies the entry point on the segment.
	 * 
	 * @return the entry point on the segment.
	 */
	public PT getPoint() {
		return this.connectionPoint;
	}
	
	/** Replies the distance needed to reach the segment.
	 * 
	 * @return the distance to reach the starting point of the segment.
	 * The value could be negative in case the iterations start from
	 * inside the segment.
	 */
	public final double getDistanceToReachSegment() {
		return this.distanceToReach;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(this.currentSegment.toString());
		buffer.append(">"); //$NON-NLS-1$
		buffer.append(getDistanceToReachSegment());
		if (this.previousSegment!=null) {
			buffer.append("<"); //$NON-NLS-1$
			buffer.append(this.previousSegment.toString());
		}
		if (isCulDeSac()) {
			buffer.append("]"); //$NON-NLS-1$
		}
		else if (this.isTerminalSegment()) {
			buffer.append("|"); //$NON-NLS-1$
		}
		return buffer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object obj) {
		return compareTo(obj) == 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.currentSegment==null ? 0 : this.currentSegment.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int compareTo(Object o) {
		if (o instanceof GraphIterationElement<?,?>) {
			GraphSegment<?,?> sgmt = ((GraphIterationElement<?,?>)o).getSegment();
			return compareSegments(this.currentSegment, sgmt);
		}
		if (o instanceof GraphSegment<?,?>) {
			return compareSegments(this.currentSegment, (GraphSegment<?,?>)o);
		}
		int h1 = (o==null) ? 0 : o.hashCode();
		int h2 = (this.currentSegment==null) ? 0 : this.currentSegment.hashCode();
		return h2 - h1;
	}
	
	/** Compare the two given segments.
	 *
	 * @param firstSegment
	 * @param secondSegment
	 * @return <code>-1</code> if <var>firstSegment</var> is lower than <var>secondSegment</var>,
	 * <code>1</code> if <var>firstSegment</var> is greater than <var>secondSegment</var>,
	 * or <code>0</code> if <var>firstSegment</var> is equal to <var>secondSegment</var>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "static-method" })
	protected int compareSegments(GraphSegment<?,?> firstSegment, GraphSegment<?,?> secondSegment) {
		if (firstSegment instanceof Comparable) {
			try {
				return ((Comparable)firstSegment).compareTo(secondSegment);
			}
			catch(AssertionError e) {
				throw e;
			}
			catch(Throwable e) {
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
	public final boolean isCulDeSac() {
		return this.culDeSac;
	}
	
	/** Replies if this segment is the last which will be reached
	 * by the iterator. It means that the iterator will not pass
	 * this segment and continue the iterations on the following graph branches.
	 * <p>
	 * Of course a segment with a cul-de-sac end is always assumed as a terminal segment.
	 * 
	 * @return <code>true</code> if this segment is terminal, otherwise <code>false</code>
	 */
	public final boolean isTerminalSegment() {
		return this.culDeSac || this.lastReachableSegment;
	}

	/** Set the flag that indicating if this segment is the last which will be reached
	 * by the iterator. It means that the iterator will not pass
	 * this segment and continue the iterations on the following graph branches.
	 * 
	 * @param isTerminal
	 */
	final void setTerminalSegment(boolean isTerminal) {
		this.lastReachableSegment = isTerminal;
	}

}

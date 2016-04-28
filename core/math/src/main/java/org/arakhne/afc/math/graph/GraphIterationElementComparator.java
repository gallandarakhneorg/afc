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

import java.util.Comparator;

import org.eclipse.xtext.xbase.lib.Pure;

/** Compare two iteration elements.
 * 
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GraphIterationElementComparator<ST extends GraphSegment<ST,PT>,PT extends GraphPoint<PT,ST>>
implements Comparator<GraphIterationElement<ST,PT>> {
	
	private final boolean assumeOrientedSegments;
	
	/**
	 * @param assumeOrientedSegments1 may be <code>true</code> to assume that the same segment has two different
	 * instances for graph iteration: the first instance is associated the first point of the segment and the second
	 * instance is associated to the last point of the segment. If this parameter is <code>false</code> to assume that
	 * the end points of a segment are not distinguished.
	 */
	public GraphIterationElementComparator(boolean assumeOrientedSegments1) {
		this.assumeOrientedSegments = assumeOrientedSegments1;
	}

	@Pure
	@Override
	public int compare(
			GraphIterationElement<ST, PT> o1,
			GraphIterationElement<ST, PT> o2) {
		assert(o1!=null && o2!=null);
		if (o1==o2) return 0;
		int cmp = compareSegments(o1.getSegment(), o2.getSegment());
		if (!this.assumeOrientedSegments || cmp!=0) return cmp;
		return compareConnections(o1.getPoint(), o2.getPoint());
	}
	
	/** Compare the two given segments.
	 * 
	 * @param s1
	 * @param s2
	 * @return <code>-1</code> if <var>s1</var> is lower than <var>s2</var>,
	 * <code>1</code> if <var>s1</var> is greater than <var>s2</var>,
	 * otherwise <code>0</code>.
	 */
	@Pure
	protected int compareSegments(ST s1, ST s2) {
		assert(s1!=null && s2!=null);
		return s1.hashCode() - s2.hashCode();
	}
	
	/** Compare the two given entry points.
	 * 
	 * @param p1
	 * @param p2
	 * @return <code>-1</code> if <var>p1</var> is lower than <var>p2</var>,
	 * <code>1</code> if <var>p1</var> is greater than <var>p2</var>,
	 * otherwise <code>0</code>.
	 */
	@Pure
	protected int compareConnections(PT p1, PT p2) {
		assert(p1!=null && p2!=null);
		return p1.hashCode() - p2.hashCode();
	}
	
	/** Replies if this comparator is assuming that
	 * segments are oriented.
	 * 
	 * @return <code>true</code> if segments are oriented,
	 * otherwise <code>false</code>
	 */
	@Pure
	public boolean isOrientedSegments() {
		return this.assumeOrientedSegments;
	}

}

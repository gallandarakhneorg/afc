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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class permits to implement a shortest path
 * algorithm to make a course inside a graph.
 * 
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ShortestSegmentFirstGraphCourseModel<ST extends GraphSegment<ST,PT>,PT extends GraphPoint<PT,ST>> implements GraphCourseModel<ST,PT>{

	private final LinkedList<GraphIterationElement<ST,PT>> list = new LinkedList<GraphIterationElement<ST,PT>>();
	
	/** Replies if this model restitutes the elements in a reverse order.
	 * <p>
	 * If <code>true</code> this model is assumed to replies the <code>GraphIterationElement</code>
	 * in the reverse order than the sequence of calls to <code>addIterationElement()</code>.
	 * If <code>false</code> this model is assumed to replies the <code>GraphIterationElement</code>
	 * in the same order as the sequence of calls to <code>addIterationElement()</code>.
	 * 
	 * @return <code>true</code> if reversed, otherwise <code>false</code>
	 */
	@Override
	public final boolean isReversedRestitution() {
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addIterationElement(GraphIterationElement<ST,PT> element) {
		// Search for the insertion index
		Iterator<GraphIterationElement<ST,PT>> iterator = this.list.iterator();
		float d1 = element.getDistanceToReachSegment() + element.getSegment().getLength();
		int index = 0;
		
		while (iterator.hasNext()) {
			GraphIterationElement<ST,PT> e = iterator.next();
			if (e!=null) {
				float d2 = e.getDistanceToReachSegment() + e.getSegment().getLength();
				if (d1<=d2) break; // Stop looping
			}
			++index;
		}
		
		// Bounds the index
		if (index<0) index = 0;
		if (index>this.list.size())
			index = this.list.size();
		
		// Insert the element at the computed index
		this.list.add(index,element);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}
		
	/** {@inheritDoc}
	 */
	@Override
	public GraphIterationElement<ST,PT> getNextIterationElement() {
		if (this.list.isEmpty()) return null;
		return this.list.getFirst();
	}

	/** {@inheritDoc}
	 */
	@Override
	public GraphIterationElement<ST,PT> removeNextIterationElement() {
		if (this.list.isEmpty()) return null;
		return this.list.removeFirst();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeIterationElements(Collection<GraphIterationElement<ST,PT>> elements) {
		this.list.removeAll(elements);
	}

}

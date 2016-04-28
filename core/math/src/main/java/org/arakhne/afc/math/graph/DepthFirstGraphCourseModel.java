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
import java.util.Stack;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class permits to implement a depth-first
 * algorithm to make a course inside a graph.
 * 
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class DepthFirstGraphCourseModel<ST extends GraphSegment<ST,PT>,PT extends GraphPoint<PT,ST>> implements GraphCourseModel<ST,PT>{

	private final Stack<GraphIterationElement<ST,PT>> stack = new Stack<>();
	
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
	@Pure
	public final boolean isReversedRestitution() {
		return true;
	}

	@Override
	public void addIterationElement(GraphIterationElement<ST,PT> element) {
		this.stack.push(element);
	}
	
	@Pure
	@Override
	public boolean isEmpty() {
		return this.stack.isEmpty();
	}
		
	@Override
	public GraphIterationElement<ST,PT> getNextIterationElement() {
		if (this.stack.isEmpty()) return null;
		return this.stack.peek();
	}

	@Override
	public GraphIterationElement<ST,PT> removeNextIterationElement() {
		if (this.stack.isEmpty()) return null;
		return this.stack.pop();
	}

	@Override
	public void removeIterationElements(Collection<GraphIterationElement<ST,PT>> elements) {
		this.stack.removeAll(elements);
	}

}

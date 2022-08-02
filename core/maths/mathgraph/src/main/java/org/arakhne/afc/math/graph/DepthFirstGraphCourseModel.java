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

import java.util.ArrayDeque;
import java.util.Deque;

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
public class DepthFirstGraphCourseModel<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>>
		implements GraphCourseModel<ST, PT> {

	private final Deque<GraphIterationElement<ST, PT>> stack = new ArrayDeque<>();

	/** Replies if this model restitutes the elements in a reverse order.
	 *
	 * <p>If <code>true</code> this model is assumed to replies the <code>GraphIterationElement</code>
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
	public void addIterationElement(GraphIterationElement<ST, PT> element) {
		this.stack.push(element);
	}

	@Pure
	@Override
	public boolean isEmpty() {
		return this.stack.isEmpty();
	}

	@Override
	public GraphIterationElement<ST, PT> getNextIterationElement() {
		if (this.stack.isEmpty()) {
			return null;
		}
		return this.stack.peek();
	}

	@Override
	public GraphIterationElement<ST, PT> removeNextIterationElement() {
		if (this.stack.isEmpty()) {
			return null;
		}
		return this.stack.pop();
	}

	@Override
	public void removeIterationElements(Iterable<GraphIterationElement<ST, PT>> elements) {
		for (final GraphIterationElement<ST, PT> element : elements) {
			this.stack.remove(element);
		}
	}

	@Override
	public String toString() {
		return this.stack.toString();
	}

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

/**
 * This class permits to implement specifical
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
public interface GraphCourseModel<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> {

	/** Replies if this model restitutes the elements in a reverse order.
	 *
	 * <p>If {@code true} this model is assumed to replies the {@code GraphIterationElement}
	 * in the reverse order than the sequence of calls to {@code addIterationElement()}.
	 * If {@code false} this model is assumed to replies the {@code GraphIterationElement}
	 * in the same order as the sequence of calls to {@code addIterationElement()}.
	 *
	 * @return {@code true} if reversed, otherwise {@code false}
	 */
	@Pure
	boolean isReversedRestitution();

	/** Add an element to iterate on.
	 *
	 * @param element is the element which could be the result of a
	 *     future iteration.
	 */
	void addIterationElement(GraphIterationElement<ST, PT> element);

	/** Replies if the model contains an element to iterate one.
	 *
	 * @return {@code true} if an element exists, otherwhise {@code false}.
	 */
	@Pure
	boolean isEmpty();

	/** Replies the next element without removing it from the iterator list.
	 *
	 * @return the next element
	 */
	GraphIterationElement<ST, PT> getNextIterationElement();

	/** Replies the next element and removes it from the iterator list.
	 *
	 * @return the next element
	 */
	GraphIterationElement<ST, PT> removeNextIterationElement();

	/** Remove the specified iteration elements.
	 *
	 * @param elements the elements to remove.
	 * @since 16.0
	 */
	void removeIterationElements(Iterable<GraphIterationElement<ST, PT>> elements);

}

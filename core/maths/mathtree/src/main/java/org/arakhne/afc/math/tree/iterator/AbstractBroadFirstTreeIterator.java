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

package org.arakhne.afc.math.tree.iterator;

import org.arakhne.afc.math.tree.IterableNode;

/** This class is an iterator on a tree.
 *
 * <p>The node A is treated <i>before</i> its children.
 *
 * @param <P> is the type of the parent nodes.
 * @param <C> is the type of the child nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @deprecated since 18.0, see {@link AbstractBreadthFirstTreeIterator}
 */
@Deprecated(since = "18.0", forRemoval = true)
public abstract class AbstractBroadFirstTreeIterator<P extends IterableNode<? extends C>, C extends IterableNode<?>>
		extends AbstractBreadthFirstTreeIterator<P, C> {

	/** Create an iterator on the given node.
	 *
	 * @param node is the node to iterate.
	 */
	public AbstractBroadFirstTreeIterator(P node) {
		super(node);
	}

	/** Set the listener invoked when a level of the tree has been treated.
	 *
	 * @param listener the listener.
	 */
	public void setBroadFirstIterationListener(BreadthFirstIterationListener listener) {
		setBreadthFirstIterationListener(listener);
	}

	/** Invoked when a row of tree nodes was completely replied by the iterator.
	 */
	protected void onBoardFirstIterationLevelFinished() {
		onBreadthFirstIterationLevelFinished();
	}

}

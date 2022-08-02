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

package org.arakhne.afc.math.tree;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interface is used to specify the base functions
 * of a tree node which is usable by an tree iterator.
 *
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface IterableNode<N extends IterableNode<?>> {

	/** Replies count of children in this node.
	 *
	 * <p>The number of children is greater or equal
	 * to the value replied by {@link #getNotNullChildCount()}.
	 *
	 * @return the count of children.
	 * @see #getNotNullChildCount()
	 */
	@Pure
	int getChildCount();

	/** Replies count of not-null children in this node.
	 *
	 * <p>The number of not-null children is lower or equal
	 * to the value replied by {@link #getChildCount()}.
	 *
	 * @return the count of not-null children.
	 * @see #getChildCount()
	 */
	@Pure
	int getNotNullChildCount();

	/** Replies the n-th child in this node.
	 *
	 * @param index is the index of the child to reply
	 * @return the child node.
	 * @throws IndexOutOfBoundsException if the given index was invalid
	 */
	@Pure
	N getChildAt(int index) throws IndexOutOfBoundsException;

	/** Replies if this node is a leaf.
	 *
	 * @return <code>true</code> is this node is a leaf,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean isLeaf();

	/** Remove this node from its parent.
	 *
	 * @return the parent node from which the node was removed.
	 */
	N removeFromParent();

}

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

package org.arakhne.afc.math.tree;

import java.util.EventObject;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Called each time an removal event occurs on a tree node.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class TreeNodeRemovedEvent extends EventObject {

	private static final long serialVersionUID = 6341954540213247389L;

	private final int childIndex;

	private final TreeNode<?, ?> child;

	/** Constructor.
	 * @param source1 is the node on which the event occured
	 * @param childIndex1 is the index of the removed child
	 * @param child1 is the removed child.
	 */
	public TreeNodeRemovedEvent(TreeNode<?, ?> source1, int childIndex1, TreeNode<?, ?> child1) {
		super(source1);
		this.childIndex = childIndex1;
		this.child = child1;
	}

	/** Replies the node that fire the event.
	 *
	 * @return the parent nodeof the removed child
	 */
	@Pure
	public TreeNode<?, ?> getParentNode() {
		return (TreeNode<?, ?>) getSource();
	}

	/** Replies the index of the child node that changed.
	 *
	 * @return the index of the removed child
	 */
	@Pure
	public int getChildIndex() {
		return this.childIndex;
	}

	/** Replies the child node that changed.
	 *
	 * @return the removed child itself
	 */
	@Pure
	public TreeNode<?, ?> getChild() {
		return this.child;
	}

}

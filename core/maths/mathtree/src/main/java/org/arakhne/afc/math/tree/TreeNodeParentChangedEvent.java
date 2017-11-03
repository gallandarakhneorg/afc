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

package org.arakhne.afc.math.tree;

import java.util.EventObject;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Called each time an hierarchy update event occurs on a tree node.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class TreeNodeParentChangedEvent extends EventObject {

	private static final long serialVersionUID = 4548135101298979693L;

	private final TreeNode<?, ?> oldParent;

	private final TreeNode<?, ?> newParent;

	/** Constructor.
	 * @param child is the node that changed of parent node
	 * @param oldParent1 is the old parent node.
	 * @param newParent1 is the new parent node.
	 */
	public TreeNodeParentChangedEvent(TreeNode<?, ?> child, TreeNode<?, ?> oldParent1, TreeNode<?, ?> newParent1) {
		super(child);
		this.oldParent = oldParent1;
		this.newParent = newParent1;
	}

	/** Replies the node that fire the event.
	 *
	 * @return the node that fire the event.
	 */
	@Pure
	public TreeNode<?, ?> getChildNode() {
		return (TreeNode<?, ?>) getSource();
	}

	/** Replies the old parent node.
	 *
	 * @return the old parent node of the event firing node.
	 */
	@Pure
	public TreeNode<?, ?> getOldParent() {
		return this.oldParent;
	}

	/** Replies the new parent node.
	 *
	 * @return the new parent node of the event firing node.
	 */
	@Pure
	public TreeNode<?, ?> getNewParent() {
		return this.newParent;
	}

}

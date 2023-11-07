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

package org.arakhne.afc.math.tree.node;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.tree.TreeDataEvent;
import org.arakhne.afc.math.tree.TreeNode;
import org.arakhne.afc.math.tree.TreeNodeAddedEvent;
import org.arakhne.afc.math.tree.TreeNodeListener;
import org.arakhne.afc.math.tree.TreeNodeParentChangedEvent;
import org.arakhne.afc.math.tree.TreeNodeRemovedEvent;

/**
 * @param <N>
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class TreeNodeListenerStub<N extends TreeNode<Object,N>> implements TreeNodeListener {

	/**
	 */
	public final List<TreeNodeAddedEvent> additionEvent = new ArrayList<>();
	
	/**
	 */
	public final List<TreeNodeRemovedEvent> removalEvent = new ArrayList<>();

	/**
	 */
	public final List<TreeDataEvent> dataEvent = new ArrayList<>();

	/**
	 */
	public final List<TreeNodeParentChangedEvent> parentEvent = new ArrayList<>();

	/**
	 */
	public TreeNodeListenerStub() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treeNodeChildAdded(TreeNodeAddedEvent event) {
		this.additionEvent.add(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treeNodeChildRemoved(TreeNodeRemovedEvent event) {
		this.removalEvent.add(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treeNodeDataChanged(TreeDataEvent event) {
		this.dataEvent.add(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treeNodeParentChanged(TreeNodeParentChangedEvent event) {
		this.parentEvent.add(event);
	}
	
	/** Clear all lists.
	 */
	public void reset() {
		this.additionEvent.clear();
		this.dataEvent.clear();
		this.parentEvent.clear();
		this.removalEvent.clear();
	}

}
/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
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
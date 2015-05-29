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
package org.arakhne.afc.math.tree;

import java.util.EventObject;

/**
 * Called each time an hierarchy update event occurs on a tree node.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TreeNodeParentChangedEvent extends EventObject {

	private static final long serialVersionUID = 4548135101298979693L;
	
	private final TreeNode<?,?> oldParent;
	private final TreeNode<?,?> newParent;

	/**
	 * @param child is the node that changed of parent node
	 * @param oldParent is the old parent node.
	 * @param newParent is the new parent node.
	 */
	public TreeNodeParentChangedEvent(TreeNode<?,?> child, TreeNode<?,?> oldParent, TreeNode<?,?> newParent) {
		super(child);
		this.oldParent = oldParent;
		this.newParent = newParent;
	}
	
	/** Replies the node that fire the event.
	 * 
	 * @return the node that fire the event. 
	 */
	public TreeNode<?,?> getChildNode() {
		return (TreeNode<?,?>)getSource();
	}
	
	/** Replies the old parent node.
	 * 
	 * @return the old parent node of the event firing node.
	 */
	public TreeNode<?,?> getOldParent() {
		return this.oldParent;
	}

	/** Replies the new parent node.
	 * 
	 * @return the new parent node of the event firing node.
	 */
	public TreeNode<?,?> getNewParent() {
		return this.newParent;
	}

}
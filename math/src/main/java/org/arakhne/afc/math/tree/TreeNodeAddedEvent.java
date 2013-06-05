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
 * Called each time an addition event occurs on a tree node.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TreeNodeAddedEvent extends EventObject {

	private static final long serialVersionUID = -4262273320530301198L;
	
	private final int childIndex;
	private final TreeNode<?,?> child;

	/**
	 * @param source is the node on which the event occured
	 * @param childIndex is the index of the new child
	 * @param child is the new child
	 */
	public TreeNodeAddedEvent(TreeNode<?,?> source, int childIndex, TreeNode<?,?> child) {
		super(source);
		this.childIndex = childIndex;
		this.child = child;
	}
	
	/** Replies the node that fire the event.
	 * 
	 * @return the node inside which a node was added.
	 */
	public TreeNode<?,?> getParentNode() {
		return (TreeNode<?,?>)getSource();
	}
	
	/** Replies the index of the child node that changed.
	 * 
	 * @return the index of the new node
	 */
	public int getChildIndex() {
		return this.childIndex;
	}
	
	/** Replies the child node that changed.
	 * 
	 * @return the new node
	 */
	public TreeNode<?,?> getChild() {
		return this.child;
	}

}
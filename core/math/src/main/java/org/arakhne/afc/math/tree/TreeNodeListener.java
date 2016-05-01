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

/**
 * Called each time an event occurs on a tree node.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface TreeNodeListener {
	
	/** A child of the specified node was added.
	 * 
	 * @param event is the description of the addition event
	 */
	public void treeNodeChildAdded(TreeNodeAddedEvent event);
	
	/** A child of the specified node was removed.
	 * 
	 * @param event is the description of the removal event
	 */
	public void treeNodeChildRemoved(TreeNodeRemovedEvent event);

	/** The parent of the specified node has changed.
	 * 
	 * @param event is the description of the event
	 */
	public void treeNodeParentChanged(TreeNodeParentChangedEvent event);

	/** The data associated to a node was changed.
	 * 
	 * @param event is the description of the event
	 */
	public void treeNodeDataChanged(TreeDataEvent event);

}
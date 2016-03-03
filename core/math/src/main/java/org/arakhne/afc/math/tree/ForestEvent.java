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
 * Event on tree forest.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class ForestEvent extends EventObject {
	
	private static final long serialVersionUID = 6460614269720932825L;
	
	private final Tree<?,?> oldValue; 
	private final Tree<?,?> newValue; 
	
	/**
	 * @param forest is the source of the event
	 * @param oldTree is the old tree
	 * @param newTree is the new tree
	 */
	public ForestEvent(Forest<?> forest, Tree<?,?> oldTree, Tree<?,?> newTree) {
		super(forest);
		this.oldValue = oldTree;
		this.newValue = newTree;
	}
	
	/** Replies the forest, source of the event.
	 * 
	 * @return the forest
	 */
	public Forest<?> getForest() {
		return (Forest<?>)getSource();
	}
	
	/** Replies the removed tree.
	 * 
	 * @return the removed tree.
	 */
	public Tree<?,?> getRemovedTree() {
		return this.oldValue;
	}
	
	/** Replies the added tree.
	 * 
	 * @return the added tree.
	 */
	public Tree<?,?> getAddedTree() {
		return this.newValue;
	}

}
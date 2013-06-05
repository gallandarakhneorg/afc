/* 
 * $Id$
 * 
 * Copyright (c) 2005-11, Multiagent Team,
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

import java.util.Collection;
import java.util.LinkedList;

/**
 * This is a linked-list implementation
 * of a forest of trees.
 * 
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
public class LinkedForest<D>
extends AbstractForest<D> { 
	
	/**
	 */
	public LinkedForest() {
		super(new LinkedList<Tree<D,?>>());
	}
	
	/**
	 * @param trees is the trees to put inside the forest.
	 */
	public LinkedForest(Collection<? extends Tree<D,?>> trees) {
		super(new LinkedList<Tree<D,?>>(), trees);
	}
	
}
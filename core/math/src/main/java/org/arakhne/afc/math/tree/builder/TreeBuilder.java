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
package org.arakhne.afc.math.tree.builder;

import java.util.List;

import org.arakhne.afc.math.tree.Tree;


/**
 * A tree builder is a class that create a tree
 * according to a set of objects and a set
 * of contraints and heuristics.
 * 
 * @param <D> is the type of the data inside the tree
 * @param <T> is the type of the tree.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface TreeBuilder<D,T extends Tree<D,?>> {

	/** Builds a tree.
	 * 
	 * @param worldEntities is the set of world's entities from which the tree must be built.
	 * @return the built tree.
	 * @throws TreeBuilderException in case of error during the building.
	 */
	public T buildTree(List<? extends D> worldEntities) throws TreeBuilderException;

	/** Set the maximal count of elements inside a tree's node
	 * over each the node must be splitted.
	 * 
	 * @param count is the maximal count of elements per node.
	 */
	public void setSplittingCount(int count);

	/** Replies the maximal count of elements inside a tree's node
	 * over each the node must be splitted.
	 * 
	 * @return the maximal count of elements per node.
	 */
	public int getSplittingCount();

}
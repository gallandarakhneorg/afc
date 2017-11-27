/* 
 * $Id$
 * 
 * Copyright (c) 2006-07, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.model.perception.tree.builders;

import java.util.List;

import org.arakhne.afc.progress.Progression;

import fr.utbm.set.jasim.environment.model.perception.tree.PerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.BoundCenterPartitionPolicy;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionPolicy;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.tree.builder.TreeBuilder;
import fr.utbm.set.tree.builder.TreeBuilderException;

/**
 * A tree builder is a class that create a tree
 * according to a set of objects and a set
 * of contraints and heuristics.
 * 
 * @param <D> is the type of entity in the built tree.
 * @param <T> is the type of the built tree.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PerceptionTreeBuilder<D extends WorldEntity<?>, T extends PerceptionTree<?,?,D,?,?>>
extends TreeBuilder<D,T> {

	/** Default heuristic partition.
	 */
	public static final PartitionPolicy DEFAULT_PARTITION_POLICY = new BoundCenterPartitionPolicy();
	
	/** Default split count.
	 */
	public static final int DEFAULT_SPLIT_COUNT = 10;
	
	/** Builds a tree.
	 * 
	 * @param worldEntities is the set of world's entities from which the tree must be built.
	 * @param pm is a task progression indicator.
	 * @return the built tree.
	 * @throws TreeBuilderException in case of error during the building.
	 * @see #buildTree(List)
	 */
	public T buildTree(List<? extends D> worldEntities, Progression pm) throws TreeBuilderException;

}
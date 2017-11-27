/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.environment.model.perception.tree.manipulators;

import java.util.Collection;

import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;

/**
 * A tree manipulator is the abstract definition
 * of a modification operation on a tree or on
 * a tree node.
 * 
 * @param <D> is the type of the data in the tree.
 * @param <T> is the type of the manipulated tree.
 * @param <N> is the type of the manipulated nodes.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface TreeManipulator<
						D extends MobileEntity<?>,
						N extends DynamicPerceptionTreeNode<?,D,?,N>,
						T extends DynamicPerceptionTree<?,?,D,?,N,T,?>> {

	/** Replies the tree on which this manipulator is working.
	 * 
	 * @return the associated tree.
	 */
	public T getTree();

	/** Move the specified entities if their current bounding boxes
	 * are not enclosed by the node's bounding boxes in which the objects
	 * are located.
	 * This function simply checks for the coherence of the tree content
	 * and does not require transformations.
	 * <p>
	 * Caution: you must call {@link #commit()} to finalize the reordering action
	 * 
	 * @param entities to check.
	 */
	public void reorderEntities(Collection<? extends D> entities);

	/** Finalize the manipulation of the tree.
	 * <p>
	 * This function does the things that should be done once time
	 * to not consume too much time.
	 */
	public void commit();
		
	/**
	 * Insert a data in a tree using a deep insertion algorithm.
	 * <p>
	 * This function is assumed to create child nodes when they are required.
	 * <p>
	 * Caution: you must call {@link #commit()} to finalize the reordering action
	 * 
	 * @param data is the new data to insert.
	 */
	public void insert(D data);

	/**
	 * Remove a data from a tree using a deep search algorithm.
	 * <p>
	 * This function is assumed to remov child nodes when they are no more required.
	 * <p>
	 * Caution: you must call {@link #commit()} to finalize the reordering action
	 * 
	 * @param data is the new data to insert.
	 */
	public void remove(D data);

}
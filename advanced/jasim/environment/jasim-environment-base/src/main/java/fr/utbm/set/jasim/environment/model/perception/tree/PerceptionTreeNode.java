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
package fr.utbm.set.jasim.environment.model.perception.tree;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.tree.TreeNode;

/**
 * Tree node for perception trees.
 * 
 * @param <D> is the type of data stored inside the tree.
 * @param <NB> is the bounds type supported by the tree node itself (not necessary the
 * same type as the data bounds).
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PerceptionTreeNode<D extends WorldEntity<?>, NB extends Bounds<?,?,?>, N extends PerceptionTreeNode<D,NB,N>> extends TreeNode<D,N> {

	/** Replies the bounds of this node.
	 * 
	 * @return the bounds of this node.
	 */
	public NB getBounds();

	/** Invalidate the current bounds in this perception tree. This function goes deeply through tree nodes.
	 * <p>
	 * A future call to {@link #getBounds()} will recompute the bounds.
	 */
	public void invalidateBounds();

	/** Update the bounds of this node and of the parent nodes.
	 * 
	 * @param updateParents msut be <code>true</code> to allows to update also
	 * the parent nodes.
	 */
	public void updateBounds(boolean updateParents);
	
	/** Add a listener of perception node events.
	 * 
	 * @param listener
	 */
	public void addPerceptionNodeListener(PerceptionNodeListener<N> listener);
	
	/** Remove a listener of perception node events.
	 * 
	 * @param listener
	 */
	public void removePerceptionNodeListener(PerceptionNodeListener<N> listener);

}
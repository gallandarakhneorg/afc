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
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.tree.TreeNode;

/**
 * Node for dynamic perception tree.
 * 
 * @param <DB> is the bounds type supported by the data inside the tree.
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
public interface DynamicPerceptionTreeNode<DB extends Bounds<?,?,?>, D extends MobileEntity<DB>, NB extends Bounds<?,?,?>, N extends PerceptionTreeNode<D,NB,N>> extends PerceptionTreeNode<D,NB,N> {

	/** Replies the index of the child node which
	 * corresponds to the specified box.
	 * <p>
	 * The returned values are:
	 * <table>
	 * <tr><td><code>0..(getChildCount()-1)</code></td><td>the
	 * index of the child node which completely encloses the
	 * specified bounds.</td></tr>
	 * <tr><td><code>i&lt;0</code></td><td>a negative value that
	 * corresponds to the first plan intersecting the given
	 * bounds. The plan could be taken with the call
	 * <code>getCutPlanes()[-i-1]</code>.</td></tr>
	 * </table>
	 * 
	 * @param box is the box to test for.
	 * @return a value between <code>0</code> and
	 * the value returned by {@link TreeNode#getChildCount()},
	 * or a negative value that corresponds to the first
	 * plan intersecting the bounding box.
	 */
	public int classifies(DB box);
	
	/** Invalidate the bounding box to force its recomputation.
	 */
	public void invalidate();

}
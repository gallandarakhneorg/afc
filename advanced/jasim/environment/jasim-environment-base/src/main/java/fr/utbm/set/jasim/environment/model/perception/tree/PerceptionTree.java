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
import fr.utbm.set.jasim.environment.interfaces.body.frustums.Frustum;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.InterestFilter;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator;
import fr.utbm.set.jasim.environment.model.perception.tree.cullers.FrustumCuller;
import fr.utbm.set.jasim.environment.model.world.EntityContainer;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.tree.Tree;

/**
 * This is the generic implementation of a perception tree.
 * 
 * @param <CB> is the bounds type used by the cullers or iterators.
 * @param <DB> is the bounds type supported by the data.
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
public interface PerceptionTree<CB extends Bounds<?,?,?>,
								DB extends CB,
								D extends WorldEntity<DB>,
								NB extends CB,
								N extends PerceptionTreeNode<D,NB,N>>
extends Tree<D,N>,
        EntityContainer<D> {

	/** Inform this tree to temporaly cache, or not, the bounding box
	 * refreshes.
	 * 
	 * @param refreshBounds is <code>true</code> to immediately refresh the bounds
	 * of the tree, otherwise <code>false</code>
	 */
	public void setBoundRefreshing(boolean refreshBounds);
	
	/** Replies if the bounds are automatically refreshed.
	 * 
	 * @return <code>true</code> if the bounds are updated
	 * at each updating function call. <code>false</code>
	 * if the bounds are not refreshed.
	 */
	public boolean isBoundRefreshing();

	/** Replies the bounds of the tree's content.
	 * <p>
	 * An invocation to this function is equivalent to
	 * <code>getRoot().getBounds()</code>.
	 * 
	 * @return the bounds of the tree or <code>null</code>
	 */
	public NB getBounds();
	
	/** Invalidate the current bounds in this perception tree. This function goes deeply through tree nodes.
	 * <p>
	 * A future call to {@link #getBounds()} will recompute the bounds.
	 */
	public void invalidateBounds();

	/** Replies a frustum culler on this tree based on the given frustum.
	 * 
	 * @param frustum is the culler to use.
	 * @param physicFilter is the perception filter based on physical critera.
	 * @param interestFilter is the perception filter based on interest critera.
	 * @return the culler, never <code>null</code>.
	 */
	public FrustumCuller<CB,DB,D,NB,N> getFrustumCuller(Frustum<? super CB,?,?> frustum, PhysicalPerceptionAlterator physicFilter, InterestFilter interestFilter);

}
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.utbm.set.geom.bounds.CombinableBounds;
import fr.utbm.set.jasim.environment.interfaces.body.frustums.Frustum;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.InterestFilter;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator;
import fr.utbm.set.jasim.environment.model.perception.tree.cullers.FrustumCuller;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.tree.LinkedTree;


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
public abstract class AbstractPerceptionTree<
						CB extends CombinableBounds<? super CB,?,?,?>, 
						DB extends CB, 
						D extends WorldEntity<DB>, 
						NB extends CB, 
						N extends PerceptionTreeNode<D,NB,N>>
extends LinkedTree<D,N>
implements PerceptionTree<CB,DB,D,NB,N> {

	private static final long serialVersionUID = 7593064166489415961L;
	
	private transient List<PerceptionTreeNode<D,NB,N>> refreshingList = null;
	private transient boolean boundRefreshing = true;
	
	/**
	 */
	public AbstractPerceptionTree() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<? extends D> entityIterator() {
		return dataDepthFirstIterator();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setBoundRefreshing(boolean refreshBounds) {
		if (this.boundRefreshing==refreshBounds) return;
		
		this.boundRefreshing = refreshBounds;
				
		if (this.boundRefreshing) {
			boolean refresh = this.refreshingList!=null;
			N root = getRoot();
			if (!refresh && root!=null) refresh = root.getBounds()==null;
			if (refresh) {
				refreshBounds();
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isBoundRefreshing() {
		return this.boundRefreshing;
	}	

	/** Save the specified node for a future refreshing.
	 * The refreshing will be proceeded at the next call
	 * to {@link #setBoundRefreshing(boolean)} with
	 * the parameter value equals to <code>false</code>.
	 * 
	 * @param nodeToRefresh is the node that must have its bounds refreshed
	 * as soon as possible.
	 */
	public void saveNodeForRefreshing(PerceptionTreeNode<D,NB,N> nodeToRefresh) {
		assert(nodeToRefresh!=null);
		if (this.refreshingList==null) {
			this.refreshingList = new ArrayList<PerceptionTreeNode<D,NB,N>>();
		}
		this.refreshingList.add(nodeToRefresh);
	}
	
	/** Refresh the cached bounds.
	 */
	protected void refreshBounds() {
		if (this.refreshingList!=null) {
			for (PerceptionTreeNode<D,NB,N> node : this.refreshingList) {
				if (node!=null) node.updateBounds(true);
			}
			this.refreshingList.clear();
			this.refreshingList = null;
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final NB getBounds() {
		N root = getRoot();
		return (root==null) ? null : root.getBounds();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final void invalidateBounds() {
		N root = getRoot();
		if (root!=null) root.invalidateBounds();
	}

	/** {@inheritDoc}
	 */
	@Override
	public FrustumCuller<CB,DB,D,NB,N> getFrustumCuller(Frustum<? super CB,?,?> frustum, PhysicalPerceptionAlterator physicFilter, InterestFilter interestFilter) {
		return new FrustumCuller<CB,DB,D,NB,N>(this, frustum, physicFilter, interestFilter);
	}

}
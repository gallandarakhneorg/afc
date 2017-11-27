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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.octree;

import java.util.List;

import fr.utbm.set.geom.bounds.CombinableBounds;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.perception.tree.manipulators.TreeManipulator;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;

/**
 * This is the generic implementation of a
 * tree for which each node has eight children plus
 * one icosep child.
 * 
 * @param <CB> is the type of the bounds common to <var>&lt;DB&gt;</var> and <var>&lt;NB&gt;</var>.
 * @param <DB> is the type of the bounds of the data.
 * @param <D> is the type of the user data inside this tree.
 * @param <NB> is the type of the bounds of the nodes.
 * @param <N> is the type of the nodes in the tree.
 * @param <T> is the type of the tree.
 * @param <M> is the type of the manipulator on the tree.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class DynamicIcosepOctTreeNode<
					CB extends CombinableBounds<? super CB,?,?,?>,
					DB extends CB,
					D extends MobileEntity<DB>,
					NB extends CB,
					N extends DynamicIcosepOctTreeNode<CB,DB,D,NB,N,T,M>,
					T extends DynamicIcosepOctTree<CB,DB,D,NB,N,T,M>,
					M extends TreeManipulator<D,N,T>>
extends AbstractIcosepOctTreeNode<CB,DB,D,NB,N,T>
implements DynamicPerceptionTreeNode<DB,D,NB,N> {

	private static final long serialVersionUID = -7386742276500015811L;

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutX is the coordinate of the plane's intersection point. 
	 * @param cutY is the coordinate of the plane's intersection point. 
	 * @param cutZ is the coordinate of the plane's intersection point. 
	 */
	public DynamicIcosepOctTreeNode(T tree, double cutX, double cutY, double cutZ) {
		super(tree, cutX, cutY, cutZ);
	}

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutX is the coordinate of the plane's intersection point. 
	 * @param cutY is the coordinate of the plane's intersection point. 
	 * @param cutZ is the coordinate of the plane's intersection point. 
	 * @param data is the user data associated to this node. The list is not copied.
	 */
	public DynamicIcosepOctTreeNode(T tree, double cutX, double cutY, double cutZ, List<D> data) {
		super(tree, cutX, cutY, cutZ, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int classifies(DB box) {
		return OctTreeUtil.classifiesBoxWithIcosep(
				getCutLineX(), getCutLineY(), getCutLineZ(), 
				box.getLower(), box.getUpper());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invalidate() {
		updateBoundsAndNodeOwners(true, false);
	}

}
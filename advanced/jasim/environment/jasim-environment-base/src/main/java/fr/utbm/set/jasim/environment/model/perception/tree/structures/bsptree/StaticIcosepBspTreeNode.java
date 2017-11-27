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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree;

import java.util.List;

import fr.utbm.set.geom.bounds.CombinableBounds;
import fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/**
 * This is the generic implementation of a
 * tree for which each node has two children plus
 * one icosep child.
 * 
 * @param <CB> is the type of the bounds common to <var>&lt;DB&gt;</var> and <var>&lt;NB&gt;</var>.
 * @param <DB> is the type of the bounds of the data.
 * @param <D> is the type of the user data inside this tree.
 * @param <NB> is the type of the bounds of the nodes.
 * @param <N> is the type of the nodes in the tree.
 * @param <T> is the type of the tree.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class StaticIcosepBspTreeNode<
					CB extends CombinableBounds<? super CB,?,?,?>,
					DB extends CB,
					D extends WorldEntity<DB>,
					NB extends CB,
					N extends StaticIcosepBspTreeNode<CB,DB,D,NB,N,T>,
					T extends StaticIcosepBspTree<CB,DB,D,NB,N,T>>
extends AbstractIcosepBspTreeNode<CB,DB,D,NB,N,T>
implements StaticPerceptionTreeNode<D,NB,N> {

	private static final long serialVersionUID = 1013393894468784270L;

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutValue is the coordinate of the line that cut the plane. 
	 * @param cutCoordinateIndex is the index of the <var>cutValue</var> in an euclidian point. 
	 */
	public StaticIcosepBspTreeNode(T tree, double cutValue, int cutCoordinateIndex) {
		super(tree, cutValue, cutCoordinateIndex);
	}

	/**
	 * @param tree is the tree in which this node is located.
	 * @param cutValue is the coordinate of the line that cut the plane. 
	 * @param cutCoordinateIndex is the index of the <var>cutValue</var> in an euclidian point. 
	 * @param data is the user data associated to this node. 
	 */
	public StaticIcosepBspTreeNode(T tree, double cutValue, int cutCoordinateIndex, List<D> data) {
		super(tree, cutValue, cutCoordinateIndex, data);
	}

}
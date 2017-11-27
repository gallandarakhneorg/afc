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
import fr.utbm.set.jasim.environment.model.perception.tree.manipulators.TreeManipulator;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;

/**
 * This is the generic implementation of a perception tree.
 * 
 * @param <CB> is the type of the bounds common to <var>&lt;DB&gt;</var> and <var>&lt;NB&gt;</var>.
 * @param <DB> is the type of the bounds of the data.
 * @param <D> is the type of the user data inside this tree.
 * @param <NB> is the type of the bounds of the nodes.
 * @param <N> is the type of the nodes in the tree.
 * @param <TT> is the type of the tree.
 * @param <TM> is the type of tree manipulator supported by this dynamic perception tree.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface DynamicPerceptionTree<
						CB extends Bounds<?,?,?>, 
						DB extends CB, 
						D extends MobileEntity<DB>, 
						NB extends CB, 
						N extends DynamicPerceptionTreeNode<DB,D,NB,N>,
						TT extends DynamicPerceptionTree<CB,DB,D,NB,N,TT,TM>,
						TM extends TreeManipulator<D,N,TT>>
extends PerceptionTree<CB,DB,D,NB,N> {

	/** Replies a tree manipulator on this tree.
	 * 
	 * @return a tree manipulator on this tree, never <code>null</code>
	 */
	public TM getManipulator();

}
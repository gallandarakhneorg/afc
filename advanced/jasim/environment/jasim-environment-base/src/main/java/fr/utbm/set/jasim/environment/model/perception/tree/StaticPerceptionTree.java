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

/**
 * This is the generic implementation of a perception tree.
 * 
 * @param <CB> is the bounds type used by the cullers.
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
public interface StaticPerceptionTree<CB extends Bounds<?,?,?>, 
									  DB extends CB, 
									  D extends WorldEntity<DB>, 
									  NB extends CB, 
									  N extends StaticPerceptionTreeNode<D,NB,N>>
extends PerceptionTree<CB,DB,D,NB,N> {
	//
}
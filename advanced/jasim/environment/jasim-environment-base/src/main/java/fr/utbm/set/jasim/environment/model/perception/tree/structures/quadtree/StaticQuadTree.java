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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree;

import fr.utbm.set.geom.bounds.CombinableBounds;
import fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/**
 * This is the generic implementation of a quad tree
 * containing bounds.
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
public abstract class StaticQuadTree<
					CB extends CombinableBounds<? super CB,?,?,?>,
					DB extends CB,
					D extends WorldEntity<DB>,
					NB extends CB,
					N extends StaticQuadTreeNode<CB,DB,D,NB,N,T>,
					T extends StaticQuadTree<CB,DB,D,NB,N,T>>
extends AbstractQuadTree<CB,DB,D,NB,N,T>
implements StaticPerceptionTree<CB,DB,D,NB,N> {

	private static final long serialVersionUID = 1017992836968709936L;

	/** Empty tree.
	 */
	public StaticQuadTree() {
		//
	}

}
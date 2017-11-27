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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.shagam;

import fr.utbm.set.geom.bounds.CombinableBounds;
import fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/**
 * This is the generic implementation of a shagam's tree
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
public abstract class StaticShagamTree<
					CB extends CombinableBounds<? super CB,?,?,?>,
					DB extends CB,
					D extends WorldEntity<DB>,
					NB extends CB,
					N extends StaticShagamTreeNode<CB,DB,D,NB,N,T>,
					T extends StaticShagamTree<CB,DB,D,NB,N,T>>
extends AbstractShagamTree<CB,DB,D,NB,N,T>
implements StaticPerceptionTree<CB,DB,D,NB,N> {

	private static final long serialVersionUID = -3639402653094183612L;

	/** Empty tree.
	 */
	public StaticShagamTree() {
		//
	}

}
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

import java.io.IOException;
import java.io.ObjectInputStream;

import fr.utbm.set.geom.bounds.CombinableBounds;
import fr.utbm.set.jasim.environment.model.perception.tree.AbstractPerceptionTree;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/**
 * This is the generic implementation of a binary 
 * space (or plane) partition tree containing bounds.
 * 
 * @param <CB> is the type of the bounds common to <code>&lt;DB&gt;</code> and <code>&lt;NB&gt;</code>.
 * @param <DB> is the type of the bounds for the user data inside the tree.
 * @param <D> is the type of the user data inside this tree.
 * @param <NB> is the type of the bounds for the tree nodes.
 * @param <N> is the type of the tree nodes.
 * @param <T> is the type of the tree.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
abstract class AbstractBspTree<
				CB extends CombinableBounds<? super CB,?,?,?>, 
				DB extends CB, 
				D extends WorldEntity<DB>, 
				NB extends CB, 
				N extends AbstractBspTreeNode<CB,DB,D,NB,N,T>,
				T extends AbstractBspTree<CB,DB,D,NB,N,T>>
extends AbstractPerceptionTree<CB,DB,D,NB,N>  {

	private static final long serialVersionUID = 8283473983091631859L;

	/** Empty tree.
	 */
	public AbstractBspTree() {
		//
	}
	
	/** Invoked when this object must be deserialized.
	 * 
	 * @param in is the input stream.
	 * @throws IOException in case of input stream access error.
	 * @throws ClassNotFoundException if some class was not found.
	 */
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		N root = getRoot();
		if (root!=null) {
			root.setTreeRecursively((T)this);
		}
		setBoundRefreshing(true);
	}

}
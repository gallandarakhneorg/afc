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

import java.io.IOException;
import java.io.ObjectInputStream;

import fr.utbm.set.geom.bounds.CombinableBounds;
import fr.utbm.set.jasim.environment.model.perception.tree.AbstractPerceptionTree;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/**
 * This is the generic implementation of a oct-tree
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
abstract class AbstractOctTree<
				CB extends CombinableBounds<? super CB,?,?,?>, 
				DB extends CB, 
				D extends WorldEntity<DB>, 
				NB extends CB, 
				N extends AbstractOctTreeNode<CB,DB,D,NB,N,T>,
				T extends AbstractOctTree<CB,DB,D,NB,N,T>>
extends AbstractPerceptionTree<CB,DB,D,NB,N>  {

	private static final long serialVersionUID = 6080478779462046626L;

	/** Empty tree.
	 */
	public AbstractOctTree() {
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
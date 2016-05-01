/* 
 * $Id$
 * 
 * Copyright (c) 2005-11, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.tree.iterator;

import java.util.Iterator;

import org.arakhne.afc.math.tree.TreeNode;

/**
 * This interface is used to represent an iterator on the tree's data with
 * selection.
 * 
 * @see NodeSelector
 * @see DataSelector
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface DataSelectionTreeIterator<D, N extends TreeNode<D,?>>
extends Iterator<D> {

	/** Set the data selector used by this iterator.
	 * 
	 * @param selector permits to filter the user data that will be replied by this iterator.
	 */
	public void setDataSelector(DataSelector<D> selector);
	
	/** Set the node selector used by this iterator.
	 * 
	 * @param selector permits to filter the node that will be replied or traversed by this iterator.
	 */
	public void setNodeSelector(NodeSelector<N> selector);
	
}

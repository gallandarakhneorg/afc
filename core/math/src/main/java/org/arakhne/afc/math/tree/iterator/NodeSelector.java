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

import org.arakhne.afc.math.tree.TreeNode;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interface is used to select the nodes to treat
 * by a Data*TreeIterator.
 * 
 * @see PostfixDataDepthFirstTreeIterator
 * @see DataBroadFirstTreeIterator
 * 
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface NodeSelector<N extends TreeNode<?,?>> {

	/** Replies if the specified node could be treated by the iterator.
	 * 
	 * @param node is the node to test.
	 * @return <code>true</code> if the node could be treated by the iterator,
	 * otherwhise <code>false</code>
	 */
	@Pure
	boolean nodeCouldBeTreatedByIterator(N node);
	
}

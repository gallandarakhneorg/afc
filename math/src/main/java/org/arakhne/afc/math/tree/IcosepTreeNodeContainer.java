/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team,
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
package org.arakhne.afc.math.tree;

/**
 * This interfaces permits to define a tree node container
 * which is containing an icosep tree node.
 * 
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface IcosepTreeNodeContainer<N extends TreeNode<?,N>> {

	/** Set the icosep child of this node.
	 * 
	 * @param newChild is the new child used as the icosep branch
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean setIcosepChild(N newChild);

	/** Get the icosep node of this node.
	 * 
	 * @return the node that is used as the icosep branch.
	 */
	public N getIcosepChild();

}
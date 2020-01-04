/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.tree;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interfaces permits to define a tree node container
 * which is containing an icosep tree node.
 *
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface IcosepTreeNodeContainer<N extends TreeNode<?, N>> {

	/** Set the icosep child of this node.
	 *
	 * @param newChild is the new child used as the icosep branch
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	boolean setIcosepChild(N newChild);

	/** Get the icosep node of this node.
	 *
	 * @return the node that is used as the icosep branch.
	 */
	@Pure
	N getIcosepChild();

}

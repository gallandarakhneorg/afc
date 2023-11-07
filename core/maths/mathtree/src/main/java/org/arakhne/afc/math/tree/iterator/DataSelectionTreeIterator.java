/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.tree.iterator;

import java.util.Iterator;

import org.arakhne.afc.math.tree.TreeNode;

/**
 * This interface is used to represent an iterator on the tree's data with
 * selection.
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see NodeSelector
 * @see DataSelector
 */
public interface DataSelectionTreeIterator<D, N extends TreeNode<D, ?>>
		extends Iterator<D> {

	/** Set the data selector used by this iterator.
	 *
	 * @param selector permits to filter the user data that will be replied by this iterator.
	 */
	void setDataSelector(DataSelector<D> selector);

	/** Set the node selector used by this iterator.
	 *
	 * @param selector permits to filter the node that will be replied or traversed by this iterator.
	 */
	void setNodeSelector(NodeSelector<N> selector);

}

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

package org.arakhne.afc.math.tree.iterator;

import java.util.Iterator;

import org.arakhne.afc.math.tree.Tree;

/**
 * This class is an iterator on a forest that replies the user data.
 *
 * <p>This iterator goes thru the trees in a broad-first order.
 *
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @deprecated since 18.0, see {@link DataBreadthFirstForestIterator}
 */
@Deprecated(since = "18.0", forRemoval = true)
public class DataBroadFirstForestIterator<D> extends DataBreadthFirstForestIterator<D> {

	/** Constructor.
	 * @param iterator is the iterator on the trees.
	 */
	public DataBroadFirstForestIterator(Iterator<Tree<D, ?>> iterator) {
		super(iterator);
	}

}

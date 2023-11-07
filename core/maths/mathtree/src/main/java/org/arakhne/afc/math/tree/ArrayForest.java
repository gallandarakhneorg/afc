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

package org.arakhne.afc.math.tree;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a array implementation
 * of a forest of trees.
 *
 * @param <D> is the type of the data inside the forest
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class ArrayForest<D> extends AbstractForest<D> {

	/** Construct a forest.
	 */
	protected ArrayForest() {
		super(new ArrayList<Tree<D, ?>>());
	}

	/** Constructor.
	 * @param trees is the trees to put inside the forest.
	 */
	protected ArrayForest(Collection<? extends Tree<D, ?>> trees) {
		super(new ArrayList<Tree<D, ?>>(), trees);
	}

}

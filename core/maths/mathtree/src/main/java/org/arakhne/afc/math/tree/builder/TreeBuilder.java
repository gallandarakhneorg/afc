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

package org.arakhne.afc.math.tree.builder;

import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.Tree;

/** A tree builder is a class that create a tree
 * according to a set of objects and a set
 * of constraints and heuristics.
 *
 * @param <D> is the type of the data inside the tree
 * @param <T> is the type of the tree.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface TreeBuilder<D, T extends Tree<D, ?>> {

	/** Builds a tree.
	 *
	 * @param worldEntities is the set of world's entities from which the tree must be built.
	 * @return the built tree.
	 * @throws TreeBuilderException in case of error during the building.
	 */
	T buildTree(List<? extends D> worldEntities) throws TreeBuilderException;

	/** Set the maximal count of elements inside a tree's node
	 * over each the node must be splitted.
	 *
	 * @param count is the maximal count of elements per node.
	 */
	void setSplittingCount(int count);

	/** Replies the maximal count of elements inside a tree's node
	 * over each the node must be splitted.
	 *
	 * @return the maximal count of elements per node.
	 */
	@Pure
	int getSplittingCount();

}

/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import java.util.EventObject;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Event on tree forest.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class ForestEvent extends EventObject {

	private static final long serialVersionUID = 6460614269720932825L;

	private final Tree<?, ?> oldValue;

	private final Tree<?, ?> newValue;

	/** Cinstruct an event.
	 *
	 * @param forest is the source of the event
	 * @param oldTree is the old tree
	 * @param newTree is the new tree
	 */
	public ForestEvent(Forest<?> forest, Tree<?, ?> oldTree, Tree<?, ?> newTree) {
		super(forest);
		this.oldValue = oldTree;
		this.newValue = newTree;
	}

	/** Replies the forest, source of the event.
	 *
	 * @return the forest
	 */
	@Pure
	public Forest<?> getForest() {
		return (Forest<?>) getSource();
	}

	/** Replies the removed tree.
	 *
	 * @return the removed tree.
	 */
	@Pure
	public Tree<?, ?> getRemovedTree() {
		return this.oldValue;
	}

	/** Replies the added tree.
	 *
	 * @return the added tree.
	 */
	@Pure
	public Tree<?, ?> getAddedTree() {
		return this.newValue;
	}

}

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

/**
 * Called each time an event occurs on a tree node.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface TreeNodeListener {

	/** A child of the specified node was added.
	 *
	 * @param event is the description of the addition event
	 */
	void treeNodeChildAdded(TreeNodeAddedEvent event);

	/** A child of the specified node was removed.
	 *
	 * @param event is the description of the removal event
	 */
	void treeNodeChildRemoved(TreeNodeRemovedEvent event);

	/** The parent of the specified node has changed.
	 *
	 * @param event is the description of the event
	 */
	void treeNodeParentChanged(TreeNodeParentChangedEvent event);

	/** The data associated to a node was changed.
	 *
	 * @param event is the description of the event
	 */
	void treeNodeDataChanged(TreeDataEvent event);

}

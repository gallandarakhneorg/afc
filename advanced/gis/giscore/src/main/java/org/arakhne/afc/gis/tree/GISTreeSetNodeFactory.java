/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.gis.tree;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.tree.TreeNode;
import org.arakhne.afc.math.tree.node.IcosepQuadTreeNode.IcosepQuadTreeZone;

/**
 * Builder for GIS tree nodes.
 *
 * @param <P> is the type of the user data inside the node.
 * @param <N> is the type of the nodes to create.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
interface GISTreeSetNodeFactory<P extends GISPrimitive, N extends TreeNode<P, N>> {

	/**
	 * Create a root node with the given element inside.
	 *
	 * @param element is the element to initially put inside the node.
	 * @return the node
	 */
	@Pure
	N newRootNode(P element);

	/**
	 * Create a root node with the given element inside.
	 *
	 * @param element is the element to initially put inside the node.
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 * @return the node
	 */
	@Pure
	N newRootNode(P element, double boundsX, double boundsY, double boundsWidth, double boundsHeight);

	/**
	 * Create a root node with the given element inside.
	 *
	 * @param zone is the zone enclosed by this node.
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 * @return the node
	 */
	@Pure
	N newNode(IcosepQuadTreeZone zone, double boundsX, double boundsY, double boundsWidth, double boundsHeight);

}

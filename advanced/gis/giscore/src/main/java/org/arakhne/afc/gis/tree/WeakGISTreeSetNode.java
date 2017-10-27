/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.tree.TreeDataEvent;
import org.arakhne.afc.math.tree.TreeNodeAddedEvent;
import org.arakhne.afc.math.tree.TreeNodeListener;
import org.arakhne.afc.math.tree.TreeNodeParentChangedEvent;
import org.arakhne.afc.math.tree.TreeNodeRemovedEvent;
import org.arakhne.afc.references.ReferenceListener;
import org.arakhne.afc.references.WeakArrayList;

/**
 * A node inside a {@link WeakGISTreeSet}.
 *
 * @param <P> is the type of the user data inside the node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
class WeakGISTreeSetNode<P extends GISPrimitive> extends AbstractGISTreeSetNode<P, WeakGISTreeSetNode<P>>
		implements ReferenceListener, TreeNodeListener {

	private static final long serialVersionUID = 6752729239913440515L;

	/**
	 * @param zone is the zone enclosed by this node.
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	WeakGISTreeSetNode(IcosepQuadTreeZone zone, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		super(zone, new WeakArrayList<P>(), boundsX, boundsY, boundsWidth, boundsHeight);
		addTreeNodeListener(this);
	}

	/**
	 * @param zone is the zone enclosed by this node.
	 * @param elements is the list used to store the elements in this node.
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	private WeakGISTreeSetNode(IcosepQuadTreeZone zone, WeakArrayList<P> elements, double boundsX,
			double boundsY, double boundsWidth, double boundsHeight) {
		super(zone, new WeakArrayList<P>(), boundsX, boundsY, boundsWidth, boundsHeight);
		elements.addReferenceListener(this);
		addTreeNodeListener(this);
	}

	@Override
	public void referenceReleased(int released) {
		clearBuffers();
		firePropertyDataChanged(-released);
	}

	@Override
	public void treeNodeChildAdded(TreeNodeAddedEvent event) {
		//
	}

	@Override
	public void treeNodeChildRemoved(TreeNodeRemovedEvent event) {
		//
	}

	@Override
	public void treeNodeDataChanged(TreeDataEvent event) {
		//
	}

	@Override
	public void treeNodeParentChanged(TreeNodeParentChangedEvent event) {
		final WeakArrayList<P> userData = (WeakArrayList<P>) getInternalDataStructureForUserData();
		if (userData != null) {
			if (event.getNewParent() == null) {
				userData.removeReferenceListener(this);
			} else {
				userData.addReferenceListener(this);
			}
		}
	}

}

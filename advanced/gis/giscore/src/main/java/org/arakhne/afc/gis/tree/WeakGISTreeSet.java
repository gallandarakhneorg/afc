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

package org.arakhne.afc.gis.tree;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.tree.node.IcosepQuadTreeNode.IcosepQuadTreeZone;

/**
 * This class describes a quad tree that contains weak references on
 * GIS primitives.
 *
 * @param <P> is the type of the user data inside the node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see GISPrimitive
 */
public class WeakGISTreeSet<P extends GISPrimitive>
		extends AbstractGISTreeSet<P, WeakGISTreeSetNode<P>>
		implements GISTreeSetNodeFactory<P, WeakGISTreeSetNode<P>> {

	private GISTreeSetNodeFactory<P, WeakGISTreeSetNode<P>> factory;

	/**
	 * Create an empty tree.
	 */
	public WeakGISTreeSet() {
		super();
	}

	/** Constructor.
	 * @param bounds are the bounds of the scene stored inside this tree.
	 */
	public WeakGISTreeSet(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		super(bounds);
	}

	/** Constructor.
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	public WeakGISTreeSet(double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		super(boundsX, boundsY, boundsWidth, boundsHeight);
	}

	@Override
	public final boolean add(P elt) {
		return GISTreeSetUtil.addInside(this, getTree().getRoot(), elt, this);
	}

	@Override
	@Pure
	public GISTreeSetNodeFactory<P, WeakGISTreeSetNode<P>> getNodeFactory() {
		return this.factory;
	}

	@Override
	public void setNodeFactory(GISTreeSetNodeFactory<P, WeakGISTreeSetNode<P>> factory) {
		this.factory = factory;
	}

	@Override
	@Pure
	public WeakGISTreeSetNode<P> newNode(
			IcosepQuadTreeZone zone,
			double boundsX, double boundsY, double boundsWidth,
			double boundsHeight) {
		return new WeakGISTreeSetNode<>(
				zone,
				boundsX, boundsY, boundsWidth, boundsHeight);
	}

	@Override
	@Pure
	public WeakGISTreeSetNode<P> newRootNode(P element) {
		if (this.factory != null) {
			return this.factory.newRootNode(element);
		}
		final Rectangle2d b = element.getGeoLocation().toBounds2D();
		assert b != null;
		final WeakGISTreeSetNode<P> rootNode = new WeakGISTreeSetNode<>(
				null,
				b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
		rootNode.addUserData(element);
		return rootNode;
	}

	@Override
	public WeakGISTreeSetNode<P> newRootNode(
			P element,
			double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		if (this.factory != null) {
			return this.factory.newRootNode(element);
		}
		final WeakGISTreeSetNode<P> rootNode = new WeakGISTreeSetNode<>(
				null,
				boundsX, boundsY, boundsWidth, boundsHeight);
		rootNode.addUserData(element);
		return rootNode;
	}

}

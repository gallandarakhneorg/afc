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

package org.arakhne.afc.gis.tree;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.tree.node.IcosepQuadTreeNode.IcosepQuadTreeZone;

/**
 * This class describes a quad tree that contains GIS primitives
 * and thatp permits to find them according to there geo-location.
 *
 * @param <P> is the type of the user data inside the node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see GISPrimitive
 */
public class StandardGISTreeSet<P extends GISPrimitive> extends AbstractGISTreeSet<P, GISTreeSetNode<P>>
		implements GISTreeSetNodeFactory<P, GISTreeSetNode<P>> {

	private GISTreeSetNodeFactory<P, GISTreeSetNode<P>> factory;

	/**
	 * Create an empty tree.
	 */
	public StandardGISTreeSet() {
		super();
	}

	/** Constructor.
	 * @param bounds are the bounds of the scene stored inside this tree.
	 */
	public StandardGISTreeSet(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		super(bounds);
	}

	/** Constructor.
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	public StandardGISTreeSet(double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		super(boundsX, boundsY, boundsWidth, boundsHeight);
	}

	@Override
	public final boolean add(P elt) {
		return GISTreeSetUtil.addInside(this, getTree().getRoot(), elt, this);
	}

	/** Set the node factory used by this tree.
	 *
	 * @param factory the node factory.
	 */
	@Override
	public void setNodeFactory(GISTreeSetNodeFactory<P, GISTreeSetNode<P>> factory) {
		this.factory = factory;
	}

	/** Replies the node factory used by this tree.
	 *
	 * @return the factory
	 */
	@Override
	@Pure
	public GISTreeSetNodeFactory<P, GISTreeSetNode<P>> getNodeFactory() {
		return this.factory;
	}

	@Override
	@Pure
	public GISTreeSetNode<P> newRootNode(P element) {
		if (this.factory != null) {
			return this.factory.newRootNode(element);
		}
		final Rectangle2d b = element.getGeoLocation().toBounds2D();
		assert b != null;
		final GISTreeSetNode<P> rootNode = new GISTreeSetNode<>(
				null,
				b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
		rootNode.addUserData(element);
		return rootNode;
	}

	@Override
	@Pure
	public GISTreeSetNode<P> newRootNode(P element, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		if (this.factory != null) {
			return this.factory.newRootNode(element, boundsX, boundsY, boundsWidth, boundsHeight);
		}
		final GISTreeSetNode<P> rootNode = new GISTreeSetNode<>(
				null,
				boundsX, boundsY, boundsWidth, boundsHeight);
		rootNode.addUserData(element);
		return rootNode;
	}

	@Override
	@Pure
	public GISTreeSetNode<P> newNode(IcosepQuadTreeZone zone, double boundsX, double boundsY,
			double boundsWidth, double boundsHeight) {
		if (this.factory != null) {
			return this.factory.newNode(zone, boundsX, boundsY, boundsWidth, boundsHeight);
		}
		return new GISTreeSetNode<>(
				zone,
				boundsX, boundsY, boundsWidth, boundsHeight);
	}

}

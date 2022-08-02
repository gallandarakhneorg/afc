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
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.tree.iterator.DataSelector;
import org.arakhne.afc.math.tree.iterator.NodeSelector;

/**
 * This class describes an iterator node selector based on bounds.
 *
 * @param <P> is the type of the user data inside the node.
 * @param <N> is the type of the nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
class FrustumSelector<P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
		implements NodeSelector<N>, DataSelector<P> {

	private final Rectangle2afp<?, ?, ?, ?, ?, ?> bounds;

	/** Constructor.
	 * @param bounds the selection bounds.
	 */
	FrustumSelector(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		this.bounds = bounds;
	}

	@Override
	@Pure
	public boolean nodeCouldBeTreatedByIterator(N node) {
		return node.intersects(this.bounds);
	}

	@Override
	@Pure
	public boolean dataCouldBeRepliedByIterator(P data) {
		if (data != null) {
			return this.bounds.intersects(data.getGeoLocation().toBounds2D());
		}
		return false;
	}

}

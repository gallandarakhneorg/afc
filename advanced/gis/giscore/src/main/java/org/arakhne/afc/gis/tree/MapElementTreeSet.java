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

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.GISElementSet;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapElementConstants;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.iterator.BroadFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.NodeSelector;

/**
 * This class describes a quad tree that contains map elements
 * and that permits to find them according to there geo-location.
 *
 * @param <P> is the type of the user data inside the node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see GISPrimitive
 */
public class MapElementTreeSet<P extends MapElement> extends StandardGISTreeSet<P> implements GISElementSet<P> {

	/**
	 * Create an empty tree.
	 */
	public MapElementTreeSet() {
		super();
	}

	/**
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	public MapElementTreeSet(double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		super(boundsX, boundsY, boundsWidth, boundsHeight);
	}

	/**
	 * @param bounds are the bounds of the scene stored inside this tree.
	 */
	public MapElementTreeSet(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		super(bounds);
	}

	//-----------------------------------------------------------------
	// Dedicated API
	//----------------------------------------------------------------

	@Override
	@Pure
	public final P getNearest(Point2D<?, ?> position) {
		final Pair<P, Double> data = getNearestData(position.getX(), position.getY());
		if (data != null) {
			return data.getKey();
		}
		return null;
	}

	@Override
	@Pure
	public final P getNearest(double x, double y) {
		final Pair<P, Double> data = getNearestData(x, y);
		if (data != null) {
			return data.getKey();
		}
		return null;
	}

	@Override
	@Pure
	public final Pair<P, Double> getNearestData(Point2D<?, ?> position) {
		return getNearestData(position.getX(), position.getY());
	}

	@Override
	@Pure
	public Pair<P, Double> getNearestData(double x, double y) {
		final NearNodeSelector<P> selector = new NearNodeSelector<>(x, y);
		return selector.select(getTree());
	}

	/**
	 * This class describes an iterator node selector based on visiblity.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 * @see GISPrimitive
	 */
	private static class NearNodeSelector<P extends MapElement> implements NodeSelector<GISTreeSetNode<P>> {

		private final Point2d point = new Point2d();

		private double minDistance = Double.POSITIVE_INFINITY;

		private P bestResult;

		/**
		 * @param x x coordinate.
		 * @param y y coordinate.
		 */
		NearNodeSelector(double x, double y) {
			this.point.set(x, y);
		}

		@Override
		@Pure
		public boolean nodeCouldBeTreatedByIterator(GISTreeSetNode<P> node) {
			if (node.isRoot()) {
				return true;
			}
			final double distance = node.distance(this.point.getX(), this.point.getY());
			return distance <= (this.minDistance + MapElementConstants.POINT_FUSION_DISTANCE);
		}

		/** Do the selection.
		 *
		 * @param tree is the tree to traverse.
		 * @return the next pair or {@code null}.
		 */
		public Pair<P, Double> select(Tree<P, GISTreeSetNode<P>> tree) {
			final Iterator<GISTreeSetNode<P>> iter = new BroadFirstTreeIterator<>(tree, this);
			GISTreeSetNode<P> node;
			double distance;
			while (iter.hasNext()) {
				node = iter.next();
				for (final P element : node.getAllUserData()) {
					distance = element.getDistance(this.point);
					if (distance < this.minDistance) {
						this.minDistance = distance;
						this.bestResult = element;
					}
				}
			}
			if (this.bestResult != null && !Double.isInfinite(this.minDistance) && !Double.isNaN(this.minDistance)) {
				return new Pair<>(this.bestResult, Double.valueOf(this.minDistance));
			}
			return null;
		}

	} /* class NearNodeSelector */

}

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

package org.arakhne.afc.gis;

import org.eclipse.xtext.xbase.lib.Pair;

import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.Point2D;

/**
 * This interface describes a set that contains GIS primitives
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
public interface GISElementSet<P extends MapElement> extends GISSet<P> {

	/** Replies the nearest object from the specified point.
	 *
	 * @param position is the position from which the nearest primitive must be replied.
	 * @return the nearest element or <code>null</code> if none.
	 * @see #getNearestData(Point2D)
	 */
	P getNearest(Point2D<?, ?> position);

	/** Replies the nearest object from the specified point.
	 *
	 * @param x is the position from which the nearest primitive must be replied.
	 * @param y is the position from which the nearest primitive must be replied.
	 * @return the nearest element or <code>null</code> if none.
	 * @see #getNearestData(double, double)
	 */
	P getNearest(double x, double y);

	/** Replies the nearest object from the specified point, and
	 * its distance to the point.
	 *
	 * @param position is the position from which the nearest primitive must be replied.
	 * @return the nearest element and its distance to the given coordinates;
	 *     or <code>null</code> if none.
	 * @see #getNearest(Point2D)
	 */
	Pair<P, Double> getNearestData(Point2D<?, ?> position);

	/** Replies the nearest object from the specified point, and
	 * its distance to the point.
	 *
	 * <p>The nearest neighbor (NN) algorithm, to find the NN to a given target
	 * point not in the tree, relies on the ability to discard large portions
	 * of the tree by performing a simple test. To perform the NN calculation,
	 * the tree is searched in a depth-first fashion, refining the nearest
	 * distance. First the root node is examined with an initial assumption
	 * that the smallest distance to the next point is infinite. The subdomain
	 * (right or left), which is a hyperrectangle, containing the target point
	 * is searched. This is done recursively until a final minimum region
	 * containing the node is found. The algorithm then (through recursion)
	 * examines each parent node, seeing if it is possible for the other
	 * domain to contain a point that is closer. This is performed by
	 * testing for the possibility of intersection between the hyperrectangle
	 * and the hypersphere (formed by target node and current minimum radius).
	 * If the rectangle that has not been recursively examined yet does not
	 * intersect this sphere, then there is no way that the rectangle can
	 * contain a point that is a better nearest neighbour. This is repeated
	 * until all domains are either searched or discarded, thus leaving the
	 * nearest neighbour as the final result. In addition to this one also
	 * has the distance to the nearest neighbour on hand as well. Finding the
	 * nearest point is an O(logN) operation.
	 *
	 * @param x is the position from which the nearest primitive must be replied.
	 * @param y is the position from which the nearest primitive must be replied.
	 * @return the nearest element and its distance to the given coordinates;
	 *     or <code>null</code> if none.
	 * @see #getNearest(double, double)
	 */
	Pair<P, Double> getNearestData(double x, double y);

}

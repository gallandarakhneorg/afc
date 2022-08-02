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

import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.tree.TreeNode;

/**
 * This interfaces defines a tree node for a {@link GISTreeSet}.
 *
 * @param <P> is the type of the user data inside the node.
 * @param <N> is the type of node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GISTreeNode<P extends GISPrimitive, N extends GISTreeNode<P, N>>
		extends TreeNode<P, N>, GISTreeBoundedArea {

	/** Replies the region covered by this node.
	 *
	 * @return the region covered by this node
	 */
	@Pure
	int getRegion();

	/** Replies if the specified rectangle intersects the
	 * bounds of the area covered by this node.
	 *
	 * @param rect the shape
	 * @return <code>true</code> if this node intersects the given rectangle, otherwise <code>false</code>
	 */
	@Pure
	boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rect);

	/** Replies if the specified rectangle intersects the
	 * bounds of the area covered by this node.
	 *
	 * @param location the position
	 * @return <code>true</code> if this node intersects the given rectangle, otherwise <code>false</code>
	 */
	@Pure
	boolean intersects(GeoLocation location);

	/** Replies if the specified points is inside the
	 * bounds of the area covered by this node.
	 *
	 * @param point the point.
	 * @return <code>true</code> if this node encloses the given point, otherwise <code>false</code>
	 */
	@Pure
	boolean contains(Point2D<?, ?> point);

	/** Replies the distance between the specified point and the area covered by
	 * this node.
	 * If the point is inside the area, the distance is zero.
	 *
	 * <p>This this node has no parent, then this function is assuming that the
	 * node encloses the entire system area. It means that the distance to
	 * the point is always zero in this case.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the distance between the given point and the bounds of this node.
	 */
	@Pure
	double distance(double x, double y);

}

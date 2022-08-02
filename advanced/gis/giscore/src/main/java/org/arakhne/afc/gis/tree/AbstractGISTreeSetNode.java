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

import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.tree.node.IcosepQuadTreeNode;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * A node inside a {@link StandardGISTreeSet}.
 *
 * @param <P> is the type of the user data inside the node.
 * @param <N> is the type of node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
class AbstractGISTreeSetNode<P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
		extends IcosepQuadTreeNode<P, N> implements GISTreeNode<P, N> {

	private static final long serialVersionUID = 6922627868187994214L;

	/** Split line.
	 */
	final double verticalSplit;

	/** Split line.
	 */
	final double horizontalSplit;

	/** Size of the child's boxes.
	 */
	final double nodeWidth;

	/** Size of the child's boxes.
	 */
	final double nodeHeight;

	/** Name of the area covered by this node in its parent.
	 */
	private final IcosepQuadTreeZone zone;

	/** Bounds of the data in the subtree.
	 */
	private transient Rectangle2afp<?, ?, ?, ?, ?, ?> dataBounds;

	/** Constructor.
	 * @param zone is the zone enclosed by this node.
	 * @param boundsX is the bounds of the node.
	 * @param boundsY is the bounds of the node.
	 * @param boundsWidth is the bounds of the node.
	 * @param boundsHeight is the bounds of the node.
	 */
	AbstractGISTreeSetNode(IcosepQuadTreeZone zone, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		super(true);
		this.zone = zone;
		this.nodeWidth = boundsWidth;
		this.nodeHeight = boundsHeight;
		this.verticalSplit = boundsX + this.nodeWidth / 2.;
		this.horizontalSplit = boundsY + this.nodeHeight / 2.;
	}

	/** Constructor.
	 * @param zone is the zone enclosed by this node.
	 * @param elements is the list of elements inside this node. The given list instance is directly used by the node.
	 * @param boundsX is the bounds of the node.
	 * @param boundsY is the bounds of the node.
	 * @param boundsWidth is the bounds of the node.
	 * @param boundsHeight is the bounds of the node.
	 */
	protected AbstractGISTreeSetNode(IcosepQuadTreeZone zone, List<P> elements, double boundsX,
			double boundsY, double boundsWidth, double boundsHeight) {
		super(true, false, elements);
		this.zone = zone;
		this.nodeWidth = boundsWidth;
		this.nodeHeight = boundsHeight;
		this.verticalSplit = boundsX + this.nodeWidth / 2.;
		this.horizontalSplit = boundsY + this.nodeHeight / 2.;
	}

	@Override
	public boolean setIcosepChild(N newChild) {
		if (super.setIcosepChild(newChild)) {
			clearBuffers();
			return true;
		}
		return false;
	}

	@Override
	public boolean setFirstChild(N newChild) {
		if (super.setFirstChild(newChild)) {
			clearBuffers();
			return true;
		}
		return false;
	}

	@Override
	public boolean setFourthChild(N newChild) {
		if (super.setFourthChild(newChild)) {
			clearBuffers();
			return true;
		}
		return false;
	}

	@Override
	public boolean setSecondChild(N newChild) {
		if (super.setSecondChild(newChild)) {
			clearBuffers();
			return true;
		}
		return false;
	}

	@Override
	public boolean setThirdChild(N newChild) {
		if (super.setThirdChild(newChild)) {
			clearBuffers();
			return true;
		}
		return false;
	}

	@Override
	public boolean addUserData(Collection<? extends P> data) {
		if (super.addUserData(data)) {
			clearBuffers();
			return true;
		}
		return false;
	}

	@Override
	public void removeAllUserData() {
		clearBuffers();
		super.removeAllUserData();
	}

	@Override
	public boolean removeUserData(Collection<P> data) {
		if (super.removeUserData(data)) {
			clearBuffers();
			return true;
		}
		return false;
	}

	@Override
	public boolean setUserData(Collection<P> data) {
		if (super.setUserData(data)) {
			clearBuffers();
			return true;
		}
		return false;
	}

	@Override
	public boolean setUserDataAt(int index, P data) throws IndexOutOfBoundsException {
		if (super.setUserDataAt(index, data)) {
			clearBuffers();
			return true;
		}
		return false;
	}

	@Override
	@Pure
	public int getRegion() {
		return this.zone.ordinal();
	}

	/** Replies the region covered by this node.
	 *
	 * @return the region covered by this node
	 * @see #getRegion()
	 */
	@Pure
	public IcosepQuadTreeZone getZone() {
		return this.zone;
	}

	/** Clear buffered variables.
	 */
	protected void clearBuffers() {
		clearBounds();
	}

	/** Clear buffered variables.
	 */
	@Pure
	void clearBounds() {
		this.dataBounds = null;
		final N parent = getParentNode();
		if (parent != null) {
			parent.clearBounds();
		}
	}

	/** Replies the bounds of this node.
	 *
	 * <p>Caution: This function does not replies a copy
	 * of the bounds, but the bounding object itself.
	 * See {@link #getObjectBounds()} to obtain a copy.
	 *
	 * @return the bounds, never <code>null</code>
	 * @see #getObjectBounds()
	 */
	@Pure
	Rectangle2afp<?, ?, ?, ?, ?, ?> getBounds() {
		if (this.dataBounds == null) {
			this.dataBounds = calcBounds();
		}
		return this.dataBounds;
	}

	/** Compute and replies the bounds for this node.
	 *
	 * @return bounds
	 */
	@Pure
	protected Rectangle2d calcBounds() {
		final Rectangle2d bb = new Rectangle2d();
		boolean first = true;
		Rectangle2afp<?, ?, ?, ?, ?, ?> b;

		// Child bounds
		N child;
		for (int i = 0; i < getChildCount(); ++i) {
			child = getChildAt(i);
			if (child != null) {
				b = child.getBounds();
				if (b != null) {
					if (first) {
						first = false;
						bb.setFromCorners(
								b.getMinX(),
								b.getMinY(),
								b.getMaxX(),
								b.getMaxY());
					} else {
						bb.setUnion(b);
					}
				}
			}
		}

		// Data bounds
		P primitive;
		GeoLocation location;
		for (int i = 0; i < getUserDataCount(); ++i) {
			primitive = getUserDataAt(i);
			if (primitive != null) {
				location = primitive.getGeoLocation();
				if (location != null) {
					b = location.toBounds2D();
					if (b != null) {
						if (first) {
							first = false;
							bb.setFromCorners(
									b.getMinX(),
									b.getMinY(),
									b.getMaxX(),
									b.getMaxY());
						} else {
							bb.setUnion(b);
						}
					}
				}
			}
		}

		return first ? null : bb;
	}

	@Override
	@Pure
	public boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rect) {
		final Rectangle2afp<?, ?, ?, ?, ?, ?> b = getBounds();
		return b != null && b.intersects(rect);
	}

	@Override
	@Pure
	public final boolean intersects(GeoLocation location) {
		return intersects(location.toBounds2D());
	}

	@Override
	@Pure
	public boolean contains(Point2D<?, ?> point) {
		final Rectangle2afp<?, ?, ?, ?, ?, ?> bounds = getBounds();
		return bounds != null && bounds.contains(point);
	}

	@Override
	@SuppressWarnings({"unchecked", "checkstyle:magicnumber", "checkstyle:npathcomplexity"})
	@Pure
	public double distance(double x, double y) {
		// All points of the scene could be covered by the icosep node.
		// Replying 0 means that the distance must be significantly computed
		// by the child nodes.
		if (this.zone == IcosepQuadTreeZone.ICOSEP) {
			return 0.;
		}

		final N parent = getParentNode();
		if (parent == null) {
			// Assuming that root nodes are enclosing the whole system area
			return 0;
		}

		final int region = parent.indexOf((N) this);

		// Compute the positions of the other regions of the parent area
		if (region >= IcosepQuadTreeZone.ICOSEP.ordinal()) {
			return Math.min(Math.abs(x - parent.verticalSplit), Math.abs(y - parent.horizontalSplit));
		}

		assert region >= 0 && region < 4;

		// The point is inside the current region. Do this test now
		// to prevent other region to be tested on a point located
		// on the bounds of the region (consequently inside two regions).
		if (GISTreeSetUtil.contains(region, parent.verticalSplit, parent.horizontalSplit, x, y)) {
			return 0.;
		}

		int vertRegion = region + 2;
		if (vertRegion > 3) {
			vertRegion -= 4;
		}

		if (GISTreeSetUtil.contains(vertRegion, parent.verticalSplit, parent.horizontalSplit, x, y)) {
			// The point is inside the top/bottom area on the area of this node
			return Math.abs(parent.horizontalSplit - y);
		}

		int horizRegion = -1;
		int diagRegion = -1;
		if (region == 0 || region == 2) {
			horizRegion = region + 1;
			diagRegion = region + 3;
		}
		if (region == 1 || region == 3) {
			horizRegion = region - 1;
			diagRegion = region - 3;
		}
		if (diagRegion > 3) {
			diagRegion -= 4;
		}
		if (diagRegion < 0) {
			diagRegion += 4;
		}

		if (GISTreeSetUtil.contains(horizRegion, parent.verticalSplit, parent.horizontalSplit, x, y)) {
			// The point is inside the left/right area on the area of this node
			return Math.abs(parent.verticalSplit - x);
		}

		// The point is inside the diagonaly opposed area on the area of this node
		return Point2D.getDistancePointPoint(x, y, parent.verticalSplit, parent.horizontalSplit);
	}

	@Override
	@Pure
	public Rectangle2d getAreaBounds() {
		final Rectangle2d b = new Rectangle2d();
		final double dw = this.nodeWidth / 2.;
		final double dh = this.nodeHeight / 2.;
		b.setFromCorners(this.verticalSplit - dw, this.horizontalSplit - dh,
				this.verticalSplit + dw, this.horizontalSplit + dh);
		return b;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Caution: This function does not replies the bounding
	 * object itself, but a copy of it.
	 * See {@link #getBounds()} to obtain the object itself.
	 *
	 * @see #getBounds()
	 */
	@Override
	@Pure
	public Rectangle2d getObjectBounds() {
		final Rectangle2d b = new Rectangle2d();
		final Rectangle2afp<?, ?, ?, ?, ?, ?> bb = getBounds();
		if (bb != null && !bb.isEmpty()) {
			b.set(bb.getMinX(), bb.getMinY(), bb.getWidth(), bb.getHeight());
		}
		return b;
	}

	@Override
	@Pure
	public boolean isIcosepHeuristicArea() {
		if (getZone() == IcosepQuadTreeZone.ICOSEP) {
			return true;
		}
		final N parent = getParentNode();
		if (parent != null) {
			return parent.isIcosepHeuristicArea();
		}
		return false;
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("depth", getDepth()); //$NON-NLS-1$
		buffer.add("zone", getZone()); //$NON-NLS-1$
		buffer.add("areaBounds", getAreaBounds()); //$NON-NLS-1$
		buffer.add("bounds", getBounds()); //$NON-NLS-1$
	}

}

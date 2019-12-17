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

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.mapelement.MapElementConstants;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.IntersectionType;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.tree.iterator.PrefixDataDepthFirstTreeIterator;
import org.arakhne.afc.math.tree.node.IcosepQuadTreeNode.IcosepQuadTreeZone;

/**
 * Utilities for GISTreeSet.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class GISTreeSetUtil {

	private GISTreeSetUtil() {
		//
	}

	/** Replies the classificiation of the specified location.
	 *
	 * @param node the classification base.
	 * @param location the point to classify.
	 * @return the classificiation of the specified location inside to the node.
	 */
	@Pure
	public static int classifies(AbstractGISTreeSetNode<?, ?> node, GeoLocation location) {
		if (node.getZone() == IcosepQuadTreeZone.ICOSEP) {
			return classifiesIcocep(node.verticalSplit, node.horizontalSplit, location.toBounds2D());
		}
		return classifiesNonIcocep(node.verticalSplit, node.horizontalSplit, location.toBounds2D());
	}

	private static int classifiesNonIcocep(double cutX, double cutY, Rectangle2afp<?, ?, ?, ?, ?, ?> location) {
		if (location.getMaxX() <= cutX) {
			// Left of the vertical line
			if (location.getMaxY() <= cutY) {
				// Bottom of the horizontal line
				return IcosepQuadTreeZone.SOUTH_WEST.ordinal();
			}
			if (location.getMinY() >= cutY) {
				// Top of the horizontal line
				return IcosepQuadTreeZone.NORTH_WEST.ordinal();
			}
			// One the horizontal line
			return IcosepQuadTreeZone.ICOSEP.ordinal();
		}
		if (location.getMinX() >= cutX) {
			// Right of the vertical line
			if (location.getMaxY() <= cutY) {
				// Bottom of the horizontal line
				return IcosepQuadTreeZone.SOUTH_EAST.ordinal();
			}
			if (location.getMinY() >= cutY) {
				// Top of the horizontal line
				return IcosepQuadTreeZone.NORTH_EAST.ordinal();
			}
			// One the horizontal line
			return IcosepQuadTreeZone.ICOSEP.ordinal();
		}

		// One the vertical line
		return IcosepQuadTreeZone.ICOSEP.ordinal();
	}

	private static int classifiesIcocep(double cutX, double cutY, Rectangle2afp<?, ?, ?, ?, ?, ?> location) {
		if (location.getMaxX() <= cutX) {
			return IcosepQuadTreeZone.SOUTH_WEST.ordinal();
		}
		if (location.getMinX() >= cutX) {
			return IcosepQuadTreeZone.NORTH_EAST.ordinal();
		}
		if (location.getMaxY() <= cutY) {
			return IcosepQuadTreeZone.SOUTH_EAST.ordinal();
		}
		if (location.getMinY() >= cutY) {
			return IcosepQuadTreeZone.NORTH_WEST.ordinal();
		}
		return IcosepQuadTreeZone.ICOSEP.ordinal();
	}

	/** Replies if the given region contains the given point.
	 *
	 * @param region is the index of the region.
	 * @param cutX is the cut line of the region parent.
	 * @param cutY is the cut line of the region parent.
	 * @param pointX is the coordinate of the point to classify.
	 * @param pointY is the coordinate of the point to classify.
	 * @return <code>true</code> if the point is inside the given region,
	 *     otherwise <code>false</code>
	 */
	@Pure
	static boolean contains(int region, double cutX, double cutY, double pointX, double pointY) {
		switch (IcosepQuadTreeZone.values()[region]) {
		case SOUTH_WEST:
			return pointX <= cutX && pointY <= cutY;
		case SOUTH_EAST:
			return pointX >= cutX && pointY <= cutY;
		case NORTH_WEST:
			return pointX <= cutX && pointY >= cutY;
		case NORTH_EAST:
			return pointX >= cutX && pointY >= cutY;
		case ICOSEP:
			return true;
		default:
		}
		return false;
	}

	/**
	 * Add the given element inside the given node (or one of its children).
	 *
	 * @param <P> is the type of the primitives.
	 * @param <N> is the type of the nodes.
	 * @param tree is the tree to update.
	 * @param insertionNode the receiving node.
	 * @param element the element to insert.
	 * @param builder is the node factory.
	 * @return success state.
	 */
	static <P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
		boolean addInside(AbstractGISTreeSet<P, N> tree,
			N insertionNode,
			P element,
			GISTreeSetNodeFactory<P, N> builder) {
		return addInside(tree, insertionNode, element, builder, true);
	}

	/**
	 * Add the given element inside the given node (or one of its children).
	 *
	 * @param <P> is the type of the primitives.
	 * @param <N> is the type of the nodes.
	 * @param tree is the tree to update.
	 * @param insertionNode the receiving node.
	 * @param element the element to insert.
	 * @param builder is the node factory.
	 * @param enableRearrange indicates if the subtree rearrangement is enabled.
	 * @return success state.
	 */
	@SuppressWarnings({"unchecked", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity",
		"checkstyle:nestedifdepth"})
	private static <P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
		boolean addInside(AbstractGISTreeSet<P, N> tree,
			N insertionNode,
			P element,
			GISTreeSetNodeFactory<P, N> builder,
			boolean enableRearrange) {
		if (element == null) {
			return false;
		}
		final GeoLocation location = element.getGeoLocation();
		if (location == null) {
			return false;
		}

		N insNode = insertionNode;

		if (insNode == null) {
			insNode = tree.getTree().getRoot();
			// The insertion node is the root.
			if (insNode == null) {
				if (tree.worldBounds == null) {
					insNode = builder.newRootNode(element);
				} else {
					insNode = builder.newRootNode(
							element,
							tree.worldBounds.getMinX(),
							tree.worldBounds.getMinY(),
							tree.worldBounds.getWidth(),
							tree.worldBounds.getHeight());
				}
				tree.getTree().setRoot(insNode);
				tree.updateComponentType(element);
				return true;
			}
		}

		// Go through the tree to retreive the leaf where to put the element

		final boolean isRearrangable = enableRearrange && tree.worldBounds == null;

		while (insNode != null) {
			if (insNode.isLeaf()) {
				// The node is a leaf. Add the element inside the node only if
				// the splitting count is not reached. Otherwise split this node
				if (insNode.getUserDataCount() >= GISTreeSetConstants.SPLIT_COUNT) {
					if (isRearrangable && isOutsideNodeBuildingBounds(insNode, location.toBounds2D())) {
						if (!insNode.addUserData(element)) {
							return false;
						}
						// Make the building bounds of the node larger
						rearrangeTree(tree, insNode, union(insNode, location.toBounds2D()), builder);
					} else {
						// Split the node
						int classification;
						final int count = insNode.getChildCount();

						// Prepare the list of elements for each child
						GeoLocation dataLocation;
						final List<P>[] collections = (List<P>[]) Array.newInstance(List.class, count);

						int subBranchCount = 0;

						// compute the positions of the data
						for (final P data : insNode.getAllUserData()) {
							dataLocation = data.getGeoLocation();
							classification = GISTreeSetUtil.classifies(insNode, dataLocation);
							if (collections[classification] == null) {
								collections[classification] = new LinkedList<>();
								++subBranchCount;
							}
							collections[classification].add(data);
						}

						classification = GISTreeSetUtil.classifies(insNode, location);
						if (collections[classification] == null) {
							collections[classification] = new LinkedList<>();
							++subBranchCount;
						}
						collections[classification].add(element);

						// save the data into the children
						if (subBranchCount > 1) {
							for (int region = 0; region < count; ++region) {
								if (collections[region] != null) {
									final N newNode = createNode(insNode,
											IcosepQuadTreeZone.values()[region], builder);
									assert newNode != null;
									if (!newNode.addUserData(collections[region])) {
										return false;
									}
									if (!insNode.setChildAt(region, newNode)) {
										return false;
									}
								}
							}
							insNode.removeAllUserData();
						} else if (subBranchCount == 1) {
							insNode.addUserData(element);
						}

					}
					insNode = null;
				} else {
					// Insert inside this node, and stop the loop on the tree nodes
					if (!insNode.addUserData(element)) {
						return false;
					}
					if (isRearrangable && isOutsideNodeBuildingBounds(insNode, location.toBounds2D())) {
						// Make the building bounds of the node larger
						rearrangeTree(tree, insNode, union(insNode, location.toBounds2D()), builder);
					}
					insNode = null;
				}
			} else {
				// The node is not a leaf. Insert the element inside one of the subtrees
				final int region = classifies(insNode, location);
				final N child = insNode.getChildAt(region);
				if (child == null) {
					final N newNode = createNode(insNode, IcosepQuadTreeZone.values()[region], builder);
					insNode.setChildAt(region, newNode);
					insNode = newNode;
				} else {
					insNode = child;
				}
			}
		}

		tree.updateComponentType(element);
		return true;
	}

	/** Replies if the given geolocation is outside the building bounds of the given node.
	 */
	@Pure
	private static boolean isOutsideNodeBuildingBounds(AbstractGISTreeSetNode<?, ?> node,
			Rectangle2afp<?, ?, ?, ?, ?, ?> location) {
		final Rectangle2d b2 = getNormalizedNodeBuildingBounds(node, location);
		if (b2 == null) {
			return false;
		}
		return Rectangle2afp.classifiesRectangleRectangle(
				b2.getMinX(), b2.getMinY(), b2.getMaxX(), b2.getMaxY(),
				location.getMinX(), location.getMinY(), location.getMaxX(), location.getMaxY()) != IntersectionType.INSIDE;
	}

	/** Normalize the given rectangle <var>n</var> to be sure that
	 * it has the same coordinates as <var>b</var> when they are closed.
	 * This function permits to obtain a bounding rectangle which may
	 * be properly used for classification against the given bounds.
	 *
	 * @param rect rectangle to be normalized.
	 * @param reference the reference shape.
	 */
	public static void normalize(Rectangle2d rect, Rectangle2afp<?, ?, ?, ?, ?, ?> reference) {
		double x1 = rect.getMinX();
		if (reference.getMinX() < x1
				&& MathUtil.isEpsilonEqual(reference.getMinX(), x1, MapElementConstants.POINT_FUSION_DISTANCE)) {
			x1 = reference.getMinX();
		}
		double y1 = rect.getMinY();
		if (reference.getMinY() < y1
				&& MathUtil.isEpsilonEqual(reference.getMinY(), y1, MapElementConstants.POINT_FUSION_DISTANCE)) {
			y1 = reference.getMinY();
		}
		double x2 = rect.getMaxX();
		if (reference.getMaxX() > x2
				&& MathUtil.isEpsilonEqual(reference.getMaxX(), x2, MapElementConstants.POINT_FUSION_DISTANCE)) {
			x2 = reference.getMaxX();
		}
		double y2 = rect.getMaxY();
		if (reference.getMaxY() > y2
				&& MathUtil.isEpsilonEqual(reference.getMaxY(), y2, MapElementConstants.POINT_FUSION_DISTANCE)) {
			y2 = reference.getMaxY();
		}
		rect.setFromCorners(x1, y1, x2, y2);
	}

	/** Compute the union of the building bounds of the given node and the given geolocation.
	 */
	private static Rectangle2d union(AbstractGISTreeSetNode<?, ?> node, Rectangle2afp<?, ?, ?, ?, ?, ?> shape) {
		final Rectangle2d b = getNodeBuildingBounds(node);
		b.setUnion(shape);
		normalize(b, shape);
		return b;
	}

	/** Replies the bounds of the area covered by the node and normalize it to
	 * fit as well as possible the coordinates of the given reference.
	 * The normalization is done by {@link #normalize(Rectangle2d, Rectangle2afp)}.
	 *
	 * @param node is the node for which the bounds must be extracted and reploed.
	 * @param reference is the bounding object which is the reference for the normalization.
	 * @return the normalized rectangle.
	 */
	@Pure
	public static Rectangle2d getNormalizedNodeBuildingBounds(AbstractGISTreeSetNode<?, ?> node,
			Rectangle2afp<?, ?, ?, ?, ?, ?> reference) {
		final Rectangle2d b = getNodeBuildingBounds(node);
		assert b != null;
		normalize(b, reference);
		return b;
	}

	/** Replies the bounds of the area covered by the node.
	 * The replied rectangle is not normalized.
	 *
	 * @param node is the node for which the bounds must be extracted and reploed.
	 * @return the not-normalized rectangle.
	 */
	@Pure
	public static Rectangle2d getNodeBuildingBounds(AbstractGISTreeSetNode<?, ?> node) {
		assert node != null;
		final double w = node.nodeWidth / 2.;
		final double h = node.nodeHeight / 2.;
		final IcosepQuadTreeZone zone = node.getZone();
		final double lx;
		final double ly;
		if (zone == null) {
			// Is root node
			lx = node.verticalSplit - w;
			ly = node.horizontalSplit - h;
		} else {
			switch (zone) {
			case SOUTH_EAST:
				lx = node.verticalSplit;
				ly = node.horizontalSplit - h;
				break;
			case SOUTH_WEST:
				lx = node.verticalSplit - w;
				ly = node.horizontalSplit - h;
				break;
			case NORTH_EAST:
				lx = node.verticalSplit;
				ly = node.horizontalSplit;
				break;
			case NORTH_WEST:
				lx = node.verticalSplit - w;
				ly = node.horizontalSplit;
				break;
			case ICOSEP:
				return getNodeBuildingBounds(node.getParentNode());
			default:
				throw new IllegalStateException();
			}
		}
		return new Rectangle2d(lx, ly, node.nodeWidth, node.nodeHeight);
	}

	/** Create a child node that supports the specified region.
	 *
	 * @param <P> is the type of the primitives.
	 * @param parent is the node in which the new node will be inserted.
	 * @param region is the region that must be covered by the new node.
	 * @param element is the element to initially put inside the node.
	 * @param builder permits to create nodes.
	 * @return a new node that is covering the given region.
	 */
	private static <P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
		N createNode(N parent, IcosepQuadTreeZone region, GISTreeSetNodeFactory<P, N> builder) {

		Rectangle2d area = parent.getAreaBounds();

		if (region == IcosepQuadTreeZone.ICOSEP) {
			return builder.newNode(
					IcosepQuadTreeZone.ICOSEP,
					area.getMinX(),
					area.getMinY(),
					area.getWidth(),
					area.getHeight());
		}

		if (parent.getZone() == IcosepQuadTreeZone.ICOSEP) {
			area = computeIcosepSubarea(region, area);
			return builder.newNode(
					region,
					area.getMinX(),
					area.getMinY(),
					area.getWidth(),
					area.getHeight());
		}

		final Point2d childCutPlane = computeCutPoint(region, parent);
		assert childCutPlane != null;
		final double w = area.getWidth() / 4.;
		final double h = area.getHeight() / 4.;
		return builder.newNode(region,
				childCutPlane.getX() - w, childCutPlane.getY() - h, 2. * w, 2. * h);
	}

	/** Computes the area covered by a child node of an icosep-node.
	 *
	 * @param region is the id of the region for which the area must be computed
	 * @param area is the parent icosep area.
	 * @return the area covered by the child node.
	 */
	private static Rectangle2d
		computeIcosepSubarea(IcosepQuadTreeZone region, Rectangle2d area) {
		if (area == null || area.isEmpty()) {
			return area;
		}
		final double demiWidth = area.getWidth() / 2.;
		final double demiHeight = area.getHeight() / 2.;
		switch (region) {
		case ICOSEP:
			return area;
		case SOUTH_WEST:
			return new Rectangle2d(
					area.getMinX(), area.getMinY(),
					demiWidth, demiHeight);
		case NORTH_WEST:
			return new Rectangle2d(
					area.getMinX(), area.getCenterY(),
					demiWidth, demiHeight);
		case NORTH_EAST:
			return new Rectangle2d(
					area.getCenterX(), area.getMinY(),
					demiWidth, demiHeight);
		case SOUTH_EAST:
			return new Rectangle2d(
					area.getMinX(), area.getMinY(),
					demiWidth, demiHeight);
		default:
		}
		throw new IllegalStateException();
	}

	/** Computes the cut planes' position of the given region.
	 *
	 * @param region is the id of the region for which the cut plane position must be computed
	 * @param parent is the parent node.
	 * @return the cut planes' position of the region, or <code>null</code> if
	 *     the cut planes' position could not be computed (because the region is the icosep region
	 *     for instance).
	 */
	private static <P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
		Point2d computeCutPoint(IcosepQuadTreeZone region, N parent) {
		final double w = parent.nodeWidth / 4.;
		final double h = parent.nodeHeight / 4.;
		final double x;
		final double y;
		switch (region) {
		case SOUTH_WEST:
			x = parent.verticalSplit - w;
			y = parent.horizontalSplit - h;
			break;
		case SOUTH_EAST:
			x = parent.verticalSplit + w;
			y = parent.horizontalSplit - h;
			break;
		case NORTH_WEST:
			x = parent.verticalSplit - w;
			y = parent.horizontalSplit + h;
			break;
		case NORTH_EAST:
			x = parent.verticalSplit + w;
			y = parent.horizontalSplit + h;
			break;
		case ICOSEP:
		default:
			return null;
		}
		return new Point2d(x, y);
	}

	/** Try to rearrange the cut planes of the given node.
	 */
	private static <P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
		N rearrangeTree(AbstractGISTreeSet<P, N> tree, N node, Rectangle2afp<?, ?, ?, ?, ?, ?> desiredBounds,
				GISTreeSetNodeFactory<P, N> builder) {
		// Search for the node that completely contains the desired area
		N topNode = node.getParentNode();
		while (topNode != null && isOutsideNodeBuildingBounds(topNode, desiredBounds)) {
			topNode = topNode.getParentNode();
		}

		final Rectangle2afp<?, ?, ?, ?, ?, ?> dr;
		if (topNode == null) {
			// Node node found, the entire tree should be rebuilt
			topNode = tree.getTree().getRoot();
			if (topNode == null) {
				throw new IllegalStateException();
			}
			dr = union(topNode, desiredBounds);
		} else {
			dr = getNormalizedNodeBuildingBounds(topNode, desiredBounds);
		}

		// Build a new subtree
		final N parent = topNode.getParentNode();
		final Iterator<P> dataIterator = new PrefixDataDepthFirstTreeIterator<>(topNode);
		final N newTopNode = builder.newNode(
				topNode.getZone(),
				dr.getMinX(), dr.getMinY(), dr.getWidth(), dr.getHeight());
		while (dataIterator.hasNext()) {
			if (!addInside(tree, newTopNode, dataIterator.next(), builder, false)) {
				throw new IllegalStateException();
			}
		}

		// Replace rearranged subtree by the new one
		if (parent != null) {
			parent.setChildAt(topNode.getZone().ordinal(), newTopNode);
			return parent;
		}
		tree.getTree().setRoot(newTopNode);
		return newTopNode;
	}

}

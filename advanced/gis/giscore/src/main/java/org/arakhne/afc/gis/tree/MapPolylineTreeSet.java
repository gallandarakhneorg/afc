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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.GISPolylineSet;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.iterator.BroadFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.NodeSelector;
import org.arakhne.afc.math.tree.iterator.PostfixDepthFirstTreeIterator;
import org.arakhne.afc.util.OutputParameter;

/**
 * This class describes a quad tree that contains map polylines
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
public class MapPolylineTreeSet<P extends MapPolyline> extends MapElementTreeSet<P> implements GISPolylineSet<P> {

	/**
	 * Create an empty tree.
	 */
	public MapPolylineTreeSet() {
		super();
	}

	/** Constructor.
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	public MapPolylineTreeSet(double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		super(boundsX, boundsY, boundsWidth, boundsHeight);
	}

	/** Constructor.
	 * @param bounds are the bounds of the scene stored inside this tree.
	 */
	public MapPolylineTreeSet(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		super(bounds);
	}

	//-----------------------------------------------------------------
	// Dedicated API
	//----------------------------------------------------------------

	@Override
	public boolean add(P polyline, double precision, OutputParameter<P> firstNeighbour, OutputParameter<P> secondNeighbour) {
		assert firstNeighbour != null;
		assert secondNeighbour != null;
		final OutputParameter<GISTreeSetNode<P>> insertionNode = new OutputParameter<>();
		GISTreeSetNode<P> node = null;
		if (ComplexInsertionNodeSelector.computeConnectableInsertion(
				getTree(),
				polyline,
				precision,
				insertionNode, firstNeighbour, secondNeighbour)) {
			node = insertionNode.get();
		} else {
			return false;
		}

		return GISTreeSetUtil.addInside(this, node, polyline, this);
	}

	/** {@inheritDoc}
	 * The nearest neighbor (NN) algorithm, to find the NN to a given target
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
	 */
	@Override
	@Pure
	public final P getNearestEnd(Point2D<?, ?> position) {
		return getNearestEnd(position.getX(), position.getY());
	}

	/** {@inheritDoc}
	 * The nearest neighbor (NN) algorithm, to find the NN to a given target
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
	 */
	@Override
	@Pure
	public P getNearestEnd(double x, double y) {
		return NearNodeSelector.getNearest(getTree(), x, y);
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
	private static class NearNodeSelector<P extends MapPolyline> implements NodeSelector<GISTreeSetNode<P>> {

		private final Point2d point = new Point2d();

		private double minDistance = Double.MAX_VALUE;

		private P bestResult;

		/** Constructor.
		 * @param x x coordinate.
		 * @param y y coordinate.
		 */
		NearNodeSelector(double x, double y) {
			this.point.set(x, y);
		}

		/** Replies the nearest polyline that has one of its ends near to the specified point.
		 *
		 * @param <P> is the type of user data.
		 * @param tree is the tree to explore
		 * @param x is one coordinate of the point
		 * @param y is one coordinate of the point
		 * @return the nearest polyline to the specified point
		 */
		@Pure
		public static <P extends MapPolyline> P getNearest(Tree<P, GISTreeSetNode<P>> tree, double x, double y) {
			final NearNodeSelector<P> selector = new NearNodeSelector<>(x, y);
			return selector.select(tree);
		}

		@Override
		@Pure
		public boolean nodeCouldBeTreatedByIterator(GISTreeSetNode<P> node) {
			if (node.isRoot()) {
				return true;
			}
			return node.distance(this.point.getX(), this.point.getY()) <= this.minDistance;
		}

		private P select(Tree<P, GISTreeSetNode<P>> tree) {
			final Iterator<GISTreeSetNode<P>> iter = new PostfixDepthFirstTreeIterator<>(tree, this);
			GISTreeSetNode<P> node;
			double distance;
			while (iter.hasNext()) {
				node = iter.next();
				for (final P data : node.getAllUserData()) {
					distance = data.distanceToEnd(this.point);
					if (distance < this.minDistance) {
						this.minDistance = distance;
						this.bestResult = data;
					}
				}
			}
			return this.bestResult;
		}

	} /* class NearNodeSelector */

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
	private static class ComplexInsertionNodeSelector<P extends MapPolyline> implements NodeSelector<GISTreeSetNode<P>> {

		private final GeoLocation location;

		private final Point2d startPt = new Point2d();

		private final Point2d endPt = new Point2d();

		private final Rectangle2d insertionCover;

		private final double precision;

		private GISTreeSetNode<P> insertionNode;

		private P nearestToStart;

		private P nearestToEnd;

		/** Constructor.
		 * @param location the location.
		 * @param startX x coordinate of the starting point.
		 * @param startY y coordinate of the starting point.
		 * @param endX x coordinate of the end point.
		 * @param endY y coordinate of the end point.
		 * @param precision floating-point number precision.
		 */
		ComplexInsertionNodeSelector(GeoLocation location, double startX, double startY, double endX,
				double endY, double precision) {
			this.location = location;
			this.startPt.set(startX, startY);
			this.endPt.set(endX, endY);
			this.precision = precision;
			this.insertionCover = new Rectangle2d(this.startPt, this.endPt);
			final double ix = this.insertionCover.getMinX() - this.precision;
			final double iy = this.insertionCover.getMinY() - this.precision;
			final double ax = this.insertionCover.getMaxX() + this.precision;
			final double ay = this.insertionCover.getMaxY() + this.precision;
			this.insertionCover.set(ix, iy, ax, ay);
		}

		/** Compute the possible insertion of the specified polyline inside the tree and replies the two nearest
		 * polylines that are already inside the tree.
		 *
		 * @param <P> is the type of the user data.
		 * @param tree is the tree inside which the polyline must be inserted
		 * @param polyline is the polyline to insert
		 * @param precision is the precision (in meters) of the connection.
		 * @param insertionNode will be filled by the node that could receive the specified polyline
		 * @param firstNeighbour is one of the two nearest polylines that could be connected to the new segment.
		 * @param secondNeighbour is one of the two nearest polylines that could be connected to the new segment.
		 * @return <code>true</code> the element could be added, <code>false</code> otherwise
		 */
		@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
		public static <P extends MapPolyline> boolean computeConnectableInsertion(
				Tree<P, GISTreeSetNode<P>> tree,
				P polyline,
				double precision,
				OutputParameter<GISTreeSetNode<P>> insertionNode,
				OutputParameter<P> firstNeighbour,
				OutputParameter<P> secondNeighbour) {

			insertionNode.clear();
			firstNeighbour.clear();
			secondNeighbour.clear();

			final Point2d startPoint = polyline.getPointAt(0);
			final Point2d endPoint = polyline.getPointAt(-1);
			double minDistance1 = Double.POSITIVE_INFINITY;
			double minDistance2 = Double.POSITIVE_INFINITY;

			final ComplexInsertionNodeSelector<P> selector = new ComplexInsertionNodeSelector<>(
						polyline.getGeoLocation(),
						startPoint.getX(), startPoint.getY(),
						endPoint.getX(), endPoint.getY(),
						precision);

			final Iterator<GISTreeSetNode<P>> iter = new BroadFirstTreeIterator<>(tree, selector);

			GISTreeSetNode<P> node;

			while (iter.hasNext()) {
				node = iter.next();

				if ((!node.isLeaf())
					&&
					(selector.insertionNode == null || selector.insertionNode == node)) {
					selector.insertionNode = node.getChildAt(GISTreeSetUtil.classifies(node, selector.location));
				}

				if (node.getUserDataCount() > 0) {
					double dist = node.getBounds().getDistance(selector.startPt);
					final boolean bstartTest = dist <= precision;
					dist = node.getBounds().getDistance(selector.endPt);
					final boolean bendTest = dist <= precision;

					if (bstartTest || bendTest) {
						for (final P data : node.getAllUserData()) {

							// Is the polyline already existing inside the tree?
							if (polyline.equals(data)) {
								return false;
							}

							if (bstartTest) {
								dist = data.distanceToEnd(selector.startPt);
								if (dist <= precision && dist < minDistance1) {
									selector.nearestToStart = data;
									minDistance1 = dist;
								}
							}

							if (bendTest) {
								dist = data.distanceToEnd(selector.endPt);
								if (dist <= precision && dist < minDistance2) {
									selector.nearestToEnd = data;
									minDistance2 = dist;
								}
							}

						}
					}
				}
			}

			// Prepare the reply of values
			if (selector.insertionNode != null) {
				insertionNode.set(selector.insertionNode);
			}
			if (selector.nearestToStart != null) {
				firstNeighbour.set(selector.nearestToStart);
			}
			if (selector.nearestToEnd != null) {
				secondNeighbour.set(selector.nearestToEnd);
			}

			return true;
		}

		@Override
		@Pure
		public boolean nodeCouldBeTreatedByIterator(GISTreeSetNode<P> node) {
			return node.intersects(this.insertionCover);
		}

	} /* class ComplexInsertionNodeSelector */

}

/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree;

import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.tree.TreeNode;
import fr.utbm.set.tree.node.IcosepQuadTreeNode.IcosepQuadTreeZone;
import fr.utbm.set.tree.node.QuadTreeNode.QuadTreeZone;

/**
 * Utility functions for quad trees.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class QuadTreeUtil {

	/** Replies the index of the child node which
	 * corresponds to the specified box.
	 * <p>
	 * The returned values are:
	 * <table>
	 * <tr><td><code>0..(getChildCount()-1)</code></td><td>the
	 * index of the child node which completely encloses the
	 * specified bounds.</td></tr>
	 * <tr><td><code>i&lt;0</code></td><td>a negative value that
	 * corresponds to the first plan intersecting the given
	 * bounds. The plan could be taken with the call
	 * <code>getCutLine()[-i-1]</code>.</td></tr>
	 * </table>
	 * 
	 * @param cutX is the coordinate of the vertical partition line
	 * @param cutY is the coordinate of the horizontal partition line
	 * @param lower is the lower point of the box.
	 * @param upper is the lower point of the box.
	 * @return a value between <code>0</code> and
	 * the value returned by {@link TreeNode#getChildCount()},
	 * or a negative value that corresponds to the first
	 * plan intersecting the bounding box.
	 * @see QuadTreeZone
	 */
	public static int classifiesBox(double cutX, double cutY, EuclidianPoint lower, EuclidianPoint upper) {
		if (upper.compareCoordinateTo(0,cutX)<=0) {
			// Left of the vertical line
			if (upper.compareCoordinateTo(1,cutY)<=0) {
				// Bottom of the horizontal line
				return QuadTreeZone.SOUTH_WEST.ordinal();
			}
			if (lower.compareCoordinateTo(1,cutY)>=0) {
				// Top of the horizontal line
				return QuadTreeZone.NORTH_WEST.ordinal();
			}
			// One the horizontal line
			return -2;
		}
		if (lower.compareCoordinateTo(0,cutX)>=0) {
			// Right of the vertical line
			if (upper.compareCoordinateTo(1,cutY)<=0) {
				// Bottom of the horizontal line
				return QuadTreeZone.SOUTH_EAST.ordinal();
			}
			if (lower.compareCoordinateTo(1,cutY)>=0) {
				// Top of the horizontal line
				return QuadTreeZone.NORTH_EAST.ordinal();
			}
			// One the horizontal line
			return -2;
		}

		// One the vertical line
		return -1;
	}

	/** Replies the index of the child node which
	 * corresponds to the specified box.
	 * <p>
	 * This function supports the icosep children given
	 * by {@link IcosepQuadTreeZone}.
	 * <p>
	 * The returned values are:
	 * <table>
	 * <tr><td><code>0..(getChildCount()-1)</code></td><td>the
	 * index of the child node which completely encloses the
	 * specified bounds.</td></tr>
	 * </table>
	 * 
	 * @param cutX is the coordinate of the vertical partition line
	 * @param cutY is the coordinate of the horizontal partition line
	 * @param lower is the lower point of the box.
	 * @param upper is the lower point of the box.
	 * @return a value between <code>0</code> and
	 * the value returned by {@link TreeNode#getChildCount()},
	 * never negative.
	 * @see IcosepQuadTreeZone
	 */
	public static int classifiesBoxWithIcosep(double cutX, double cutY, EuclidianPoint lower, EuclidianPoint upper) {
		if (upper.compareCoordinateTo(0,cutX)<=0) {
			// Left of the vertical line
			if (upper.compareCoordinateTo(1,cutY)<=0) {
				// Bottom of the horizontal line
				return IcosepQuadTreeZone.SOUTH_WEST.ordinal();
			}
			if (lower.compareCoordinateTo(1,cutY)>=0) {
				// Top of the horizontal line
				return IcosepQuadTreeZone.NORTH_WEST.ordinal();
			}
			// One the horizontal line
			return IcosepQuadTreeZone.ICOSEP.ordinal();
		}
		if (lower.compareCoordinateTo(0,cutX)>=0) {
			// Right of the vertical line
			if (upper.compareCoordinateTo(1,cutY)<=0) {
				// Bottom of the horizontal line
				return IcosepQuadTreeZone.SOUTH_EAST.ordinal();
			}
			if (lower.compareCoordinateTo(1,cutY)>=0) {
				// Top of the horizontal line
				return IcosepQuadTreeZone.NORTH_EAST.ordinal();
			}
			// One the horizontal line
			return IcosepQuadTreeZone.ICOSEP.ordinal();
		}

		// One the vertical line
		return IcosepQuadTreeZone.ICOSEP.ordinal();
	}

}

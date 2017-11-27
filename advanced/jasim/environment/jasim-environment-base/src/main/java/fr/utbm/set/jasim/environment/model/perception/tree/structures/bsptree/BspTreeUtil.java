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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree;

import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.tree.TreeNode;
import fr.utbm.set.tree.node.BinaryTreeNode.BinaryTreeZone;
import fr.utbm.set.tree.node.IcosepBinaryTreeNode.IcosepBinaryTreeZone;

/**
 * Utility functions for BSP trees.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BspTreeUtil {

	/** Replies the index of the child node which
	 * corresponds to the specified coordinates.
	 * Only the coordinates at the given index are
	 * taken into account. 
	 * <p>
	 * This function uses the ordinal indexes of
	 * {@link BinaryTreeZone} as child index.
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
	 * @param coordinateIndex is the index of the coordinates to 
	 * classify in the euclidian points
	 * @param cut is the coordinate of the partition line
	 * @param lower is the lowest point of the box to classify.
	 * @param upper is the uppest point of the box to classify.
	 * @return a value between <code>0</code> and
	 * the value returned by {@link TreeNode#getChildCount()},
	 * or a negative value that corresponds to the first
	 * plan intersecting the bounding box.
	 * @see BinaryTreeZone
	 */
	public static int classifiesOnCoordinates(int coordinateIndex, double cut, EuclidianPoint lower, EuclidianPoint upper) {
		if (upper.compareCoordinateTo(coordinateIndex, cut)<=0) {
			// On the left side of the cut line
			return BinaryTreeZone.LEFT.ordinal();
		}
		if (lower.compareCoordinateTo(coordinateIndex, cut)>=0) {
			// On the right side of the cut line
			return BinaryTreeZone.RIGHT.ordinal();
		}
		// On the cut line.
		return -1;
	}

	/** Replies the index of the child node which
	 * corresponds to the specified coordinates.
	 * <p>
	 * This function uses the ordinal indexes of
	 * {@link IcosepBinaryTreeZone} as child index.
	 * <p>
	 * The returned values are:
	 * <table>
	 * <tr><td><code>0..(getChildCount()-1)</code></td><td>the
	 * index of the child node which completely encloses the
	 * specified bounds.</td></tr>
	 * </table>
	 * 
	 * @param coordinateIndex is the index of the coordinates to 
	 * classify in the euclidian points
	 * @param cut is the coordinate of the partition line
	 * @param lower is the lowest point of the box to classify.
	 * @param upper is the uppest point of the box to classify.
	 * @return a value between <code>0</code> and
	 * the value returned by {@link TreeNode#getChildCount()}.
	 * @see IcosepBinaryTreeZone
	 */
	public static int classifiesOnCoordinatesWithIcosep(int coordinateIndex, double cut, EuclidianPoint lower, EuclidianPoint upper) {
		if (upper.compareCoordinateTo(coordinateIndex, cut)<=0) {
			// On the left side of the cut line
			return IcosepBinaryTreeZone.LEFT.ordinal();
		}
		if (lower.compareCoordinateTo(coordinateIndex, cut)>=0) {
			// On the right side of the cut line
			return IcosepBinaryTreeZone.RIGHT.ordinal();
		}
		// On the cut line.
		return IcosepBinaryTreeZone.ICOSEP.ordinal();
	}

}

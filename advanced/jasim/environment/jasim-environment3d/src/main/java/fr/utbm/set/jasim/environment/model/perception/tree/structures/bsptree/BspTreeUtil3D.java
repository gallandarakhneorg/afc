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

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.BinaryPartitionField;
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
public class BspTreeUtil3D extends BspTreeUtil {

	/**
	 * Replies the sub-area of the given box.
	 * 
	 * @param box is the box to split
	 * @param zone is the index of the zone for which the sub-area must be computed
	 * @param cutPlane is the description of the cut plane.
	 * @return the sub-area, never <code>null</code>
	 */
	public static AlignedBoundingBox getSubArea(
			AlignedBoundingBox box,
			BinaryTreeZone zone,
			BinaryPartitionField cutPlane) {
		return getSubArea(box, zone,
				cutPlane.getCutCoordinate(),
				cutPlane.getCutCoordinateIndex());
	}

	/**
	 * Replies the sub-area of the given box.
	 * 
	 * @param box is the box to split
	 * @param zone is the index of the zone for which the sub-area must be computed
	 * @param cutPlaneCoordinate is the coordinate of the cut plane/line.
	 * @param cutPlaneIndex is the index of the euclidian coordinate,
	 * <code>0</code> for x, <code>1</code> for y, and
	 * <code>2</code> for z.
	 * @return the sub-area, never <code>null</code>
	 */
	public static AlignedBoundingBox getSubArea(
			AlignedBoundingBox box,
			BinaryTreeZone zone,
			double cutPlaneCoordinate,
			int cutPlaneIndex) {
		AlignedBoundingBox sub;
		
		switch(cutPlaneIndex) {
		case 0:
			if (zone==BinaryTreeZone.LEFT)
				sub = new AlignedBoundingBox(
						box.getMinX(), box.getMinY(), box.getMinZ(),
						cutPlaneCoordinate, box.getMaxY(), box.getMaxZ());
			else
				sub = new AlignedBoundingBox(
						cutPlaneCoordinate, box.getMinY(), box.getMinZ(),
						box.getMaxX(), box.getMaxY(), box.getMaxZ());
			break;
		case 1:
			if (zone==BinaryTreeZone.LEFT)
				sub = new AlignedBoundingBox(
						box.getMinX(), box.getMinY(), box.getMinZ(),
						box.getMaxX(), cutPlaneCoordinate, box.getMaxZ());
			else
				sub = new AlignedBoundingBox(
						box.getMinX(), cutPlaneCoordinate, box.getMinZ(),
						box.getMaxX(), box.getMaxY(), box.getMaxZ());
			break;
		case 2:
			if (zone==BinaryTreeZone.LEFT)
				sub = new AlignedBoundingBox(
						box.getMinX(), box.getMinY(), box.getMinZ(),
						box.getMaxX(), box.getMaxY(), cutPlaneCoordinate);
			else
				sub = new AlignedBoundingBox(
						box.getMinX(), box.getMinY(), cutPlaneCoordinate,
						box.getMaxX(), box.getMaxY(), box.getMaxZ());
			break;
		default:
			sub = new AlignedBoundingBox();
			sub.set(box.getCenter());
		}
		
		return sub;
	}

	/**
	 * Replies the sub-area of the given box.
	 * 
	 * @param box is the box to split
	 * @param zone is the index of the zone for which the sub-area must be computed
	 * @param cutPlane is the description of the cut plane.
	 * @return the sub-area or <code>null</code> if the subarea could not be
	 * computed (in icosep child node case for example).
	 */
	public static AlignedBoundingBox getSubArea(
			AlignedBoundingBox box,
			IcosepBinaryTreeZone zone,
			BinaryPartitionField cutPlane) {
		return getSubArea(box, zone, 
				cutPlane.getCutCoordinate(),
				cutPlane.getCutCoordinateIndex());
	}
	
	/**
	 * Replies the sub-area of the given box.
	 * 
	 * @param box is the box to split
	 * @param zone is the index of the zone for which the sub-area must be computed
	 * @param cutPlaneCoordinate is the coordinate of the cut plane/line.
	 * @param cutPlaneIndex is the index of the euclidian coordinate,
	 * <code>0</code> for x, <code>1</code> for y, and
	 * <code>2</code> for z.
	 * @return the sub-area, never <code>null</code>
	 */
	public static AlignedBoundingBox getSubArea(
			AlignedBoundingBox box,
			IcosepBinaryTreeZone zone,
			double cutPlaneCoordinate,
			int cutPlaneIndex) {
		AlignedBoundingBox sub = null;
		
		switch(cutPlaneIndex) {
		case 0:
			switch(zone) {
			case LEFT:
				sub = new AlignedBoundingBox(
						box.getMinX(), box.getMinY(), box.getMinZ(),
						cutPlaneCoordinate, box.getMaxY(), box.getMaxZ());
				break;
			case RIGHT:
				sub = new AlignedBoundingBox(
						cutPlaneCoordinate, box.getMinY(), box.getMinZ(),
						box.getMaxX(), box.getMaxY(), box.getMaxZ());
				break;
			case ICOSEP:
				break;
			default:
			}
			break;
		case 1:
			switch(zone) {
			case LEFT:
				sub = new AlignedBoundingBox(
						box.getMinX(), box.getMinY(), box.getMinZ(),
						box.getMaxX(), cutPlaneCoordinate, box.getMaxZ());
				break;
			case RIGHT:
				sub = new AlignedBoundingBox(
						box.getMinX(), cutPlaneCoordinate, box.getMinZ(),
						box.getMaxX(), box.getMaxY(), box.getMaxZ());
				break;
			case ICOSEP:
				break;
			default:
			}
			break;
		case 2:
			switch(zone) {
			case LEFT:
				sub = new AlignedBoundingBox(
						box.getMinX(), box.getMinY(), box.getMinZ(),
						box.getMaxX(), box.getMaxY(), cutPlaneCoordinate);
				break;
			case RIGHT:
				sub = new AlignedBoundingBox(
						box.getMinX(), box.getMinY(), cutPlaneCoordinate,
						box.getMaxX(), box.getMaxY(), box.getMaxZ());
				break;
			case ICOSEP:
				break;
			default:
			}
			break;
		default:
		}
		
		return sub;
	}

}
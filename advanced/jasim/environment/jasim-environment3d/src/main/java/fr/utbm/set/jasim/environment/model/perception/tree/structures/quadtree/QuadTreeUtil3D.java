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

import javax.vecmath.Point2d;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.QuadryPartitionField;
import fr.utbm.set.tree.node.IcosepQuadTreeNode.IcosepQuadTreeZone;
import fr.utbm.set.tree.node.QuadTreeNode.QuadTreeZone;

/**
 * Utility functions for Quad trees.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class QuadTreeUtil3D extends QuadTreeUtil {

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
			QuadTreeZone zone,
			QuadryPartitionField cutPlane) {
		return getSubArea(box, zone, cutPlane.getCutPlaneIntersection());
	}

	/**
	 * Replies the sub-area of the given box.
	 * 
	 * @param box is the box to split
	 * @param zone is the index of the zone for which the sub-area must be computed
	 * @param cutPlaneCenter is the center point where all cut planes are intersecting
	 * @return the sub-area, never <code>null</code>
	 */
	public static AlignedBoundingBox getSubArea(
			AlignedBoundingBox box,
			QuadTreeZone zone,
			Point2d cutPlaneCenter) {
		AlignedBoundingBox sub = null;

		switch(zone) {
		case NORTH_EAST:
			sub = new AlignedBoundingBox(
					cutPlaneCenter.x, cutPlaneCenter.y, box.getMinZ(),
					box.getMaxX(), box.getMaxY(), box.getMaxZ());
			break;
		case NORTH_WEST:
			sub = new AlignedBoundingBox(
					box.getMinX(), cutPlaneCenter.y, box.getMinZ(),
					cutPlaneCenter.x, box.getMaxY(), box.getMaxZ());
			break;
		case SOUTH_EAST:
			sub = new AlignedBoundingBox(
					cutPlaneCenter.x, box.getMinY(), box.getMinZ(),
					box.getMaxX(), cutPlaneCenter.y, box.getMaxZ());
			break;
		case SOUTH_WEST:
			sub = new AlignedBoundingBox(
					box.getMinX(), box.getMinY(), box.getMinZ(),
					cutPlaneCenter.x, cutPlaneCenter.y, box.getMaxZ());
			break;
		default:
		}
		
		return sub;
	}

	/**
	 * Replies the sub-area of the given box.
	 * 
	 * @param box is the box to split
	 * @param zone is the index of the zone for which the sub-area must be computed
	 * @param cutPlane is the description of the cut plane.
	 * @return the sub-area, or <code>null</code> if the bounds could not
	 * be computed (in case of icosep child node for example).
	 */
	public static AlignedBoundingBox getSubArea(
			AlignedBoundingBox box,
			IcosepQuadTreeZone zone,
			QuadryPartitionField cutPlane) {
		return getSubArea(box, zone, cutPlane.getCutPlaneIntersection());
	}

	/**
	 * Replies the sub-area of the given box.
	 * 
	 * @param box is the box to split
	 * @param zone is the index of the zone for which the sub-area must be computed
	 * @param cutPlaneCenter is the center point where all cut planes are intersecting
	 * @return the sub-area, or <code>null</code> if the bounds could not
	 * be computed (in case of icosep child node for example).
	 */
	public static AlignedBoundingBox getSubArea(
			AlignedBoundingBox box,
			IcosepQuadTreeZone zone,
			Point2d cutPlaneCenter) {
		AlignedBoundingBox sub = null;

		switch(zone) {
		case NORTH_EAST:
			sub = new AlignedBoundingBox(
					cutPlaneCenter.x, cutPlaneCenter.y, box.getMinZ(),
					box.getMaxX(), box.getMaxY(), box.getMaxZ());
			break;
		case NORTH_WEST:
			sub = new AlignedBoundingBox(
					box.getMinX(), cutPlaneCenter.y, box.getMinZ(),
					cutPlaneCenter.x, box.getMaxY(), box.getMaxZ());
			break;
		case SOUTH_EAST:
			sub = new AlignedBoundingBox(
					cutPlaneCenter.x, box.getMinY(), box.getMinZ(),
					box.getMaxX(), cutPlaneCenter.y, box.getMaxZ());
			break;
		case SOUTH_WEST:
			sub = new AlignedBoundingBox(
					box.getMinX(), box.getMinY(), box.getMinZ(),
					cutPlaneCenter.x, cutPlaneCenter.y, box.getMaxZ());
			break;
		case ICOSEP:
			break;
		default:
		}
		
		return sub;
	}

}
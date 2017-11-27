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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.shagam;

import javax.vecmath.Point3d;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.OctyPartitionField;

/**
 * Utility functions for Quad trees.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ShagamTreeUtil3D extends ShagamTreeUtil {

	/**
	 * Replies the sub-area of the given box.
	 * 
	 * @param box is the box to split
	 * @param zone is the index of the zone for which the sub-area must be computed
	 * @param cutPlane is the description of the cut plane.
	 * @return the sub-area
	 */
	public static AlignedBoundingBox getSubArea(
			AlignedBoundingBox box,
			ShagamTreeZone zone,
			OctyPartitionField cutPlane) {
		return getSubArea(box, zone, cutPlane.getCutPlaneIntersection());
	}

	/**
	 * Replies the sub-area of the given box.
	 * 
	 * @param box is the box to split
	 * @param zone is the index of the zone for which the sub-area must be computed
	 * @param cutPlaneCenter is point where all cut planes are intersecting.
	 * @return the sub-area
	 */
	public static AlignedBoundingBox getSubArea(
			AlignedBoundingBox box,
			ShagamTreeZone zone,
			Point3d cutPlaneCenter) {
		AlignedBoundingBox sub = null;

		switch(zone) {
		case NORTH_EAST_BACK_VOXEL:
			sub = new AlignedBoundingBox(
					cutPlaneCenter.x, cutPlaneCenter.y, cutPlaneCenter.z,
					box.getMaxX(), box.getMaxY(), box.getMaxZ());
			break;
		case NORTH_EAST_FRONT_VOXEL:
			sub = new AlignedBoundingBox(
					cutPlaneCenter.x, box.getMinY(), cutPlaneCenter.z,
					box.getMaxX(), cutPlaneCenter.y, box.getMaxZ());
			break;
		case NORTH_WEST_BACK_VOXEL:
			sub = new AlignedBoundingBox(
					box.getMinX(), cutPlaneCenter.y, cutPlaneCenter.z,
					cutPlaneCenter.x, box.getMaxY(), box.getMaxZ());
			break;
		case NORTH_WEST_FRONT_VOXEL:
			sub = new AlignedBoundingBox(
					box.getMinX(), box.getMinY(), cutPlaneCenter.z,
					cutPlaneCenter.x, cutPlaneCenter.y, box.getMaxZ());
			break;
		case SOUTH_EAST_BACK_VOXEL:
			sub = new AlignedBoundingBox(
					cutPlaneCenter.x, cutPlaneCenter.y, box.getMinZ(),
					box.getMaxX(), box.getMaxY(), cutPlaneCenter.z);
			break;
		case SOUTH_EAST_FRONT_VOXEL:
			sub = new AlignedBoundingBox(
					cutPlaneCenter.x, box.getMinY(), box.getMinZ(),
					box.getMaxX(), cutPlaneCenter.y, cutPlaneCenter.z);
			break;
		case SOUTH_WEST_BACK_VOXEL:
			sub = new AlignedBoundingBox(
					box.getMinX(), cutPlaneCenter.y, box.getMinZ(),
					cutPlaneCenter.x, box.getMaxY(), cutPlaneCenter.z);
			break;
		case SOUTH_WEST_FRONT_VOXEL:
			sub = new AlignedBoundingBox(
					box.getMinX(), box.getMinY(), box.getMinZ(),
					cutPlaneCenter.x, cutPlaneCenter.y, cutPlaneCenter.z);
			break;
		default:
			sub = new AlignedBoundingBox(box);
		}
		
		return sub;
	}

}
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

import fr.utbm.set.tree.node.OctTreeNode.OctTreeZone;

/**
 * This is the generic implementation of a
 * tree for which each node has eight children.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum ShagamTreeZone {
	
	/** This is the index of the child that correspond to
	 * the voxel at north-west and front position.
	 */
	NORTH_WEST_FRONT_VOXEL,
	
	/** This is the index of the child that correspond to
	 * the voxel at north-west and back position.
	 */
	NORTH_WEST_BACK_VOXEL,

	/** This is the index of the child that correspond to
	 * the voxel at north-east and front position.
	 */
	NORTH_EAST_FRONT_VOXEL,
	
	/** This is the index of the child that correspond to
	 * the voxel at north-east and back position.
	 */
	NORTH_EAST_BACK_VOXEL,

	/** This is the index of the child that correspond to
	 * the voxel at south-west and front position.
	 */
	SOUTH_WEST_FRONT_VOXEL,
	
	/** This is the index of the child that correspond to
	 * the voxel at south-west and back position.
	 */
	SOUTH_WEST_BACK_VOXEL,

	/** This is the index of the child that correspond to
	 * the voxel at south-east and front position.
	 */
	SOUTH_EAST_FRONT_VOXEL,
	
	/** This is the index of the child that correspond to
	 * the voxel at south-east and back position.
	 */
	SOUTH_EAST_BACK_VOXEL,
	
	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at south-west-back
	 * and south-east-back position.
	 */
	ICOSEP_YZ_SOUTH_BACK_PLANE,

	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at south-west-front
	 * and south-east-front position.
	 */
	ICOSEP_YZ_SOUTH_FRONT_PLANE,

	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at north-west-back
	 * and north-east-back position.
	 */
	ICOSEP_YZ_NORTH_BACK_PLANE,

	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at north-west-front
	 * and north-east-front position.
	 */
	ICOSEP_YZ_NORTH_FRONT_PLANE,
	
	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at south-west-back
	 * and south-west-front position.
	 */
	ICOSEP_XZ_SOUTH_WEST_PLANE,

	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at south-east-back
	 * and south-east-front position.
	 */
	ICOSEP_XZ_SOUTH_EAST_PLANE,

	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at north-west-back
	 * and north-west-front position.
	 */
	ICOSEP_XZ_NORTH_WEST_PLANE,

	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at north-east-back
	 * and north-east-front position.
	 */
	ICOSEP_XZ_NORTH_EAST_PLANE,
	
	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at south-west-back
	 * and north-west-back position.
	 */
	ICOSEP_XY_WEST_BACK_PLANE,
	
	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at south-west-front
	 * and north-west-front position.
	 */
	ICOSEP_XY_WEST_FRONT_PLANE,
	
	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at south-east-back
	 * and north-east-back position.
	 */
	ICOSEP_XY_EAST_BACK_PLANE,
	
	/** This is the index of the child that correspond to
	 * the plane of intersection between voxels at south-east-front
	 * and north-east-front position.
	 */
	ICOSEP_XY_EAST_FRONT_PLANE,
	
	/** This is the index of the child that correspond to
	 * the axe of intersection between voxels at west position.
	 */
	ICOSEP_X_WEST_AXIS,
	
	/** This is the index of the child that correspond to
	 * the axe of intersection between voxels at east position.
	 */
	ICOSEP_X_EAST_AXIS,
	
	/** This is the index of the child that correspond to
	 * the axe of intersection between voxels at back position.
	 */
	ICOSEP_Y_BACK_AXIS,

	/** This is the index of the child that correspond to
	 * the axe of intersection between voxels at front position.
	 */
	ICOSEP_Y_FRONT_AXIS,
	
	/** This is the index of the child that correspond to
	 * the axe of intersection between voxels at south position.
	 */
	ICOSEP_Z_SOUTH_AXIS,
	
	/** This is the index of the child that correspond to
	 * the axe of intersection between voxels at north position.
	 */
	ICOSEP_Z_NORTH_AXIS,
	
	/** This is the index of the child that correspond to
	 * the point of intersection between all voxels.
	 */
	ICOSEP_CENTER;
	
	/** Replies the octtree zone that is corresponding to this zone.
	 * 
	 * @return a quadtree zone
	 */
	public OctTreeZone toOctTreeZone() {
		return OctTreeZone.values()[ordinal()];
	}

	/** Replies the zone corresponding to the given index.
	 * The index is the same as the ordinal value of the
	 * enumeration. If the given index does not correspond
	 * to an ordinal value, <code>null</code> is replied.
	 * 
	 * @param index
	 * @return the zone or <code>null</code>
	 */
	public static ShagamTreeZone fromInteger(int index) {
		if (index<0) return null;
		ShagamTreeZone[] nodes = values();
		if (index>=nodes.length) return null;
		return nodes[index];
	}

}

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
package fr.utbm.set.jasim.environment.model.ground;

import java.util.UUID;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.semantics.GroundType;

/** This class representes the ground in a environment.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractGround implements Ground {
	
	/** Identifier of this ground.
	 */
	private final UUID id;
	
	/** Default semantic.
	 */
	private GroundType defaultSemantic;
	
	/** Lower point of the map.
	 */
	private final Point3d lower;

	/** Upper point of the map.
	 */
	private final Point3d upper;

	/** Size of the world.
	 */
	private final Vector3d worldSize;
		
	/**
	 * @param id is the identifier of this ground.
	 * @param minX is the lowest X-coordinate of the map. 
	 * @param minY is the lowest Y-coordinate of the map. 
	 * @param minZ is the lowest height of the map (it corresponds to <code>-128</code> in the height matrix). 
	 * @param maxX is the highest X-coordinate of the map. 
	 * @param maxY is the highest Y-coordinate of the map. 
	 * @param maxZ is the highest height of the map (it corresponds to <code>128</code> in the height matrix). 
	 */
	public AbstractGround(UUID id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this(id, minX, minY, minZ, maxX, maxY, maxZ, null);
	}

	/**
	 * @param id is the identifier of this ground.
	 * @param minX is the lowest X-coordinate of the map. 
	 * @param minY is the lowest Y-coordinate of the map. 
	 * @param minZ is the lowest height of the map (it corresponds to <code>-128</code> in the height matrix). 
	 * @param maxX is the highest X-coordinate of the map. 
	 * @param maxY is the highest Y-coordinate of the map. 
	 * @param maxZ is the highest height of the map (it corresponds to <code>128</code> in the height matrix). 
	 * @param defaultSemantic is the default semantic associated to the ground.
	 */
	public AbstractGround(UUID id, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, GroundType defaultSemantic) {
		this.id = id;
		this.lower = new Point3d(minX, minY, minZ);
		this.upper = new Point3d(maxX, maxY, maxZ);
		this.defaultSemantic = defaultSemantic;
		this.worldSize = new Vector3d(
				this.upper.x - this.lower.x,
				this.upper.y - this.lower.y,
				this.upper.z - this.lower.z);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public UUID getIdentifier() {
		return this.id;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMinX() {
		return this.lower.x;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMaxX() {
		return this.upper.x;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMinY() {
		return this.lower.y;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMaxY() {
		return this.upper.y;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMinZ() {
		return this.lower.z;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getMaxZ() {
		return this.upper.z;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSizeX() {
		return this.worldSize.x;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double getSizeY() {
		return this.worldSize.y;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSizeZ() {
		return this.worldSize.z;
	}

	/** {@inheritDoc}
	 */
	@Override
	public GroundType getGroundType(double x, double y) {
		if (!isTraversable(x, y))
			return null;
		return this.defaultSemantic;
	}
	
	/** Set the type of the ground.
	 * 
	 * @param type is the default type of the ground.
	 */
	public void setDefaultGroundType(GroundType type) {
		this.defaultSemantic = type;
	}

}
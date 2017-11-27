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
package fr.utbm.set.jasim.spawn.spawnlocation3d;

import java.lang.ref.WeakReference;
import java.util.UUID;

import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.spawn.AbstractSpawnLocation;
import fr.utbm.set.jasim.spawn.GroundBasedSpawnLocation;

/**
 * Spawn location with ground reference.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSpawnLocation3D
extends AbstractSpawnLocation<EuclidianPoint3D,Direction2D,DirectionConstraint3D>
implements GroundBasedSpawnLocation {

	private WeakReference<Ground> ground = null;
	
	/**
	 * @param identifier is the identifier of the spawning location.
	 * @param name is the name of the spawning location.
	 * @param spawnerPosition is the position of this spawner.
	 * @param direction describes the constraints on the direction.
	 * @param startDate is the date at which the spawn point is starting to spawn entities.
	 * @param endDate is the date at which the spawn point is finishing to spawn entities.
	 */
	public AbstractSpawnLocation3D(UUID identifier, String name, EuclidianPoint3D spawnerPosition, DirectionConstraint3D direction, double startDate, double endDate) {
		super(identifier, name, spawnerPosition, direction, startDate, endDate);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setGround(Ground ground) {
		this.ground = new WeakReference<Ground>(ground);
	}

	/**
	 * Replies the ground.
	 * 
	 * @return the ground.
	 */
	protected Ground getGround() {
		return this.ground==null ? null : this.ground.get();
	}

}

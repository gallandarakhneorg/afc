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
package fr.utbm.set.jasim.spawn.spawnlocation2d;

import java.util.UUID;

import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.jasim.environment.model.ground.Ground;

/**
 * Spawn entities at a point of the environment.
 * <p>
 * The agents will be spawn at the reference position.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SpawnPoint2D
extends AbstractSpawnLocation2D {
	
	/**
	 * @param identifier is the identifier of the spawning location.
	 * @param name is the name of the spawning location.
	 * @param spawnerPosition is the position of this spawner
	 * @param startAngle is the first angle that describes the start of the spawning quadrant. The angle is expressed in radian from the X axis.
	 * @param endAngle is the last angle that describes the start of the spawning quadrant. The angle is expressed in radian from the X axis.
	 * @param startDate is the date at which the spawn point is starting to spawn entities.
	 * @param endDate is the date at which the spawn point is finishing to spawn entities.
	 */
	public SpawnPoint2D(
			UUID identifier,
			String name,
			EuclidianPoint2D spawnerPosition, 
			double startAngle, 
			double endAngle, 
			double startDate, 
			double endDate) {
		super(identifier, name, spawnerPosition, new DirectionConstraint2D(startAngle, endAngle), startDate, endDate);
	}
	
	/** Set the ground.
	 * 
	 * @param ground
	 */
	@Override
	public void setGround(Ground ground) {
		if (ground==null)
			throw new IllegalArgumentException("No ground given"); //$NON-NLS-1$
		EuclidianPoint2D p = getReferencePoint();
		if (!ground.isTraversable(p.x, p.y)) {
			throw new IllegalArgumentException(
					"The position of the spawner does not correspond to a traversable point: "+p); //$NON-NLS-1$
		}
		super.setGround(ground);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EuclidianPoint2D getPosition(long currentIdx, long spawnedEntities, long spawnableEntities) {
		return getReferencePoint();
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	protected final Direction2D randomDirection() {
		DirectionConstraint2D ref = getReferenceDirection();
		double a = this.rnd.nextDouble() * ref.delta + ref.startAngle;
		return new Direction2D(Math.cos(a), Math.sin(a));
	}

}

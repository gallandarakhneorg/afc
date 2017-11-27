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
 * Spawn entities inside an area of the environment.
 * <p>
 * The agents will be spawn at position {@code (rx+dx,ry+dy)}
 * where {@code (rx,ry)} is the reference position, {@code dx} is a 
 * random number in {@code [0, width]} interval, and {@code dy}
 * is a random number in {@code [0, height]}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SpawnArea2D
extends AbstractSpawnLocation2D {
	
	/**
	 * Is the width of the spawner.
	 */
	private final double width;

	/**
	 * Is the height of the spawner.
	 */
	private final double height;

	/**
	 * @param identifier is the identifier of the spawning location.
	 * @param name is the name of the spawning location.
	 * @param spawnerPosition is the position of this spawner.
	 * @param spawnerWidth is the width of this spawner.
	 * @param spawnerHeight is the width of this spawner.
	 * @param startAngle is the first angle that describes the start of the spawning quadrant. The angle is expressed in radian from the X axis.
	 * @param endAngle is the last angle that describes the start of the spawning quadrant. The angle is expressed in radian from the X axis.
	 * @param startDate is the date at which the spawn point is starting to spawn entities.
	 * @param endDate is the date at which the spawn point is finishing to spawn entities.
	 */
	public SpawnArea2D(
			UUID identifier,
			String name,
			EuclidianPoint2D spawnerPosition, 
			double spawnerWidth, 
			double spawnerHeight, 
			double startAngle, 
			double endAngle, 
			double startDate, 
			double endDate) {
		super(identifier, name, spawnerPosition, new DirectionConstraint2D(startAngle, endAngle), startDate, endDate);
		this.width = spawnerWidth;
		this.height = spawnerHeight;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EuclidianPoint2D getPosition(long currentIdx, long spawnedEntities, long spawnableEntities) {
		double dx, dy, z;
		int t = 0;
		Ground g = getGround();

		EuclidianPoint2D reference = getReferencePoint();

		do {
			dx = this.rnd.nextDouble() * this.width;
			dy = this.rnd.nextDouble() * this.height;
			z = (g==null) ? 0 : g.getHeightAt(reference.x+dx,reference.y+dy);
			++t;
		}
		while (Double.isNaN(z) && t<SPACE_TIMEOUT);
		if (Double.isNaN(z)) return null;
		
		return new EuclidianPoint2D(reference.x+dx,reference.y+dy);
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

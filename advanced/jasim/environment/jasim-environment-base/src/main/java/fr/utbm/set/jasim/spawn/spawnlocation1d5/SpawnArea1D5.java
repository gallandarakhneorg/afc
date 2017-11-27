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
package fr.utbm.set.jasim.spawn.spawnlocation1d5;

import java.util.UUID;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.jasim.spawn.AbstractSpawnLocation;

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
public class SpawnArea1D5 extends AbstractSpawnLocation<Point1D5,Direction1D,Direction1D> {
	
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
	 * @param position is the position on the road.
	 * @param width is the width of the area.
	 * @param height is the width of the area.
	 * @param direction is the allowed spawning direction(s).
	 * @param startDate is the date at which the spawn point is starting to spawn entities.
	 * @param endDate is the date at which the spawn point is finishing to spawn entities.
	 */
	public SpawnArea1D5(UUID identifier, String name, Point1D5 position, double width, double height, Direction1D direction, double startDate, double endDate) {
		super(identifier, name, position, direction, startDate, endDate);
		this.width = width;
		this.height = height;
	}
	
	/** Replies the width of the spawner.
	 * 
	 * @return the width of the spawner.
	 */
	public double getSpawningAreaWidth() {
		return this.width;
	}

	/** Replies the height of the spawner.
	 * 
	 * @return the height of the spawner.
	 */
	public double getSpawningAreaHeight() {
		return this.height;
	}

	/** Replies the spawning position of an agent.
	 * 
	 * @param currentIdx is the current index of the spawned agent.
	 * @param spawnedEntities is the count of already spawned entities from the current spawning list.
	 * @param spawnableEntities is the count of entities from the current spawning list.
	 * @return a random direction
	 */
	@Override
	protected Point1D5 getPosition(long currentIdx, long spawnedEntities, long spawnableEntities) {
		double dx = this.rnd.nextDouble() * this.width;
		double dy = this.rnd.nextDouble() * this.height;
		Point1D5 reference = getReferencePoint();
		return new Point1D5(
				reference.getSegment(),
				reference.getCurvilineCoordinate()+dx,
				reference.getJuttingDistance()+dy);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	protected final Direction1D randomDirection() {
		Direction1D ref = getReferenceDirection();
		switch(ref) {
		case SEGMENT_DIRECTION:
		case REVERTED_DIRECTION:
			return ref;
		case BOTH_DIRECTIONS:
			return this.rnd.nextBoolean() ? Direction1D.SEGMENT_DIRECTION : Direction1D.REVERTED_DIRECTION;
		default:
		}
		throw new IllegalArgumentException("invalid direction constraint"); //$NON-NLS-1$
	}

}

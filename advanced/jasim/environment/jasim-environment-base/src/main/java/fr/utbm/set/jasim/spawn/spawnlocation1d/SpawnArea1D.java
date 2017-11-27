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
package fr.utbm.set.jasim.spawn.spawnlocation1d;

import java.util.UUID;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.jasim.spawn.AbstractSpawnLocation;

/**
 * Spawn entities inside an area of the environment.
 * <p>
 * The agents will be spawn at position {@code r+dx}
 * where {@code r} is the reference position and {@code dx} is a 
 * random number in {@code [0, width]} interval.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SpawnArea1D extends AbstractSpawnLocation<Point1D,Direction1D,Direction1D> {
	
	/**
	 * Is the width of the spawner.
	 */
	private final double width;

	/**
	 * @param identifier is the identifier of the spawning location.
	 * @param name is the name of the spawning location.
	 * @param position is the position on the road.
	 * @param width is the width of the area.
	 * @param direction is the allowed spawning direction(s).
	 * @param startDate is the date at which the spawn point is starting to spawn entities.
	 * @param endDate is the date at which the spawn point is finishing to spawn entities.
	 */
	public SpawnArea1D(UUID identifier, String name, Point1D position, double width, Direction1D direction, double startDate, double endDate) {
		super(identifier, name, position, direction, startDate, endDate);
		this.width = width;
	}
	
	/** Replies the width of the spawner.
	 * 
	 * @return the width of the spawner.
	 */
	public double getSpawningAreaWidth() {
		return this.width;
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	protected Point1D getPosition(long currentIdx, long spawnedEntities, long spawnableEntities) {
		double dx = this.rnd.nextDouble() * this.width;
		Point1D reference = getReferencePoint();
		return new Point1D(
				reference.getSegment(),
				reference.getCurvilineCoordinate()+dx);
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

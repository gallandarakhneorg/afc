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

import java.util.UUID;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.system.CoordinateSystem3D;
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
public class SpawnArea3D
extends AbstractSpawnLocation3D {
	
	private static EuclidianPoint3D makePosition(EuclidianPoint2D p) {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		if (cs.isZOnUp())
			return new EuclidianPoint3D(p.x,p.y,0.);
		return new EuclidianPoint3D(p.x,0.,p.y);
	}
	
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
	public SpawnArea3D(
			UUID identifier,
			String name,
			EuclidianPoint2D spawnerPosition, 
			double spawnerWidth, 
			double spawnerHeight, 
			double startAngle, 
			double endAngle, 
			double startDate, 
			double endDate) {
		super(identifier, name, makePosition(spawnerPosition), new DirectionConstraint3D(startAngle, endAngle), startDate, endDate);
		this.width = spawnerWidth;
		this.height = spawnerHeight;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGround(Ground ground) {
		if (ground==null)
			throw new IllegalArgumentException("No ground given"); //$NON-NLS-1$
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		EuclidianPoint3D p = getReferencePoint();
		Point2d p2 = cs.toCoordinateSystem2D(p);
		double z = ground.getHeightAt(p2.x, p2.y);
		if (Double.isNaN(z)) {
			throw new IllegalArgumentException(
					"The position of the spawner does not correspond to a traversable point: "+p); //$NON-NLS-1$
		}
		cs.setHeight(p, z);
		super.setGround(ground);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EuclidianPoint3D getPosition(long currentIdx, long spawnedEntities, long spawnableEntities) {
		double dx, dy, z;
		int t = 0;
		Ground g = getGround();
		if (g==null) return null;
		CoordinateSystem3D cs =	CoordinateSystem3D.getDefaultCoordinateSystem();

		EuclidianPoint3D reference = getReferencePoint();

		do {
			dx = this.rnd.nextDouble() * this.width;
			dy = this.rnd.nextDouble() * this.height;
			z = g.getHeightAt(reference.x+dx,reference.y+dy);
			++t;
		}
		while (Double.isNaN(z) && t<SPACE_TIMEOUT);
		if (Double.isNaN(z)) return null;
		if (cs.isZOnUp())
			return new EuclidianPoint3D(reference.x+dx,reference.y+dy,z);
		return new EuclidianPoint3D(reference.x+dx,z,reference.y+dy);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	protected final Direction2D randomDirection() {
		DirectionConstraint3D ref = getReferenceDirection();
		double a = this.rnd.nextDouble() * ref.delta + ref.startAngle;
		return new Direction2D(Math.cos(a), Math.sin(a));
	}

}

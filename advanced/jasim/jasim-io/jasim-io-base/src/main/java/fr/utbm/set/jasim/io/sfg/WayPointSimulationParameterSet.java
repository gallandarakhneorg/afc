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
package fr.utbm.set.jasim.io.sfg;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.object.Direction1D;

/**
 * Set of parameters to initialize a simulation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WayPointSimulationParameterSet extends AbstractParameter {

	private final String name;
	private final PositionParameter position;
	private final Vector3d tangent;
	private final Direction1D direction;
	private final double velocity;
	private final double time;
	
	/**
	 * @param name
	 * @param position
	 * @param tangentX
	 * @param tangentY
	 * @param tangentZ
	 * @param velocity
	 * @param time
	 */
	WayPointSimulationParameterSet(String name, PositionParameter position, double tangentX, double tangentY, double tangentZ, double velocity, double time) {
		super(position.getEnvironmentDimension());
		this.name = name;
		this.position = position;
		this.velocity = velocity;
		this.time = time;
		this.direction = null;
		
		if (Double.isNaN(tangentX) || Double.isNaN(tangentY) || Double.isNaN(tangentZ))
			this.tangent = null;
		else
			this.tangent = new Vector3d(tangentX, tangentY, tangentZ);
	}
	
	/**
	 * @param name
	 * @param position
	 * @param direction
	 * @param velocity
	 * @param time
	 */
	WayPointSimulationParameterSet(String name, PositionParameter position, Direction1D direction, double velocity, double time) {
		super(position.getEnvironmentDimension());
		this.name = name;
		this.position = position;
		this.velocity = velocity;
		this.time = time;
		this.tangent = null;
		this.direction = direction;
	}

	/** Replies the name of this waypoint.
	 * 
	 * @return the name of this waypoint.
	 */
	public String getName() {
		return this.name;
	}

	/** Replies the position of this waypoint.
	 * 
	 * @return the position of this waypoint.
	 */
	public PositionParameter getPosition() {
		return this.position;
	}
	
	/** Replies the tangent to this waypoint.
	 * 
	 * @return the tangent to this waypoint, or <code>null</code> if none.
	 */
	public Vector3d getTangent3D() {
		return this.tangent==null ? null : new Vector3d(this.tangent);
	}

	/** Replies the tangent to this waypoint.
	 * 
	 * @return the tangent to this waypoint, or <code>null</code> if none.
	 */
	public Vector2d getTangent2D() {
		return this.tangent==null ? null : new Vector2d(this.tangent.x, this.tangent.y);
	}

	/** Replies the tangent to this goal.
	 * 
	 * @return the direction according to the segment.
	 */
	public Direction1D getTangent1D() {
		return this.direction;
	}

	/** Replies the desired velocity at this waypoint.
	 * 
	 * @return the velocity or {@link Double#NaN} if no desired velocity. 
	 */
	public double getVelocity() {
		return this.velocity;
	}

	/** Replies the desired time at which the waypoint must be reach.
	 * 
	 * @return the time or {@link Double#NaN} if no desired time. 
	 */
	public double getTime() {
		return this.time;
	}

}

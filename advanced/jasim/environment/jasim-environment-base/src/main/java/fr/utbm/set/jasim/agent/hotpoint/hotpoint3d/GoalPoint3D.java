/* 
 * $Id$
 * 
 * Copyright (c) 2004-08, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.agent.hotpoint.hotpoint3d;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.jasim.agent.hotpoint.GoalPoint;

/**
 * A goal point is a situated point on which an agent is supposed to reach and stay.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GoalPoint3D extends GoalPoint {

	/**
	 */
	private static final long serialVersionUID = -3504591710703531360L;
	
	private final double x,y,z;
	private final double tx,ty,tz;
	
	/**
	 * @param x is the position of the hot point.
	 * @param y is the position of the hot point.
	 * @param z is the position of the hot point.
	 * @param tx is the tangent of the hot point.
	 * @param ty is the tangent of the hot point.
	 * @param tz is the tangent of the hot point.
	 * @param desiredTime is the desired time at which this point must be used by the agent.
	 */
	public GoalPoint3D(double x, double y, double z, double tx, double ty, double tz, double desiredTime) {
		super(desiredTime);
		this.x = x;
		this.y = y;
		this.z = z;
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
	}
	
	/**
	 * @param x is the position of the hot point.
	 * @param y is the position of the hot point.
	 * @param z is the position of the hot point.
	 * @param tx is the tangent of the hot point.
	 * @param ty is the tangent of the hot point.
	 * @param tz is the tangent of the hot point.
	 */
	public GoalPoint3D(double x, double y, double z, double tx, double ty, double tz) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
	}
	
	/** Replies the tangent of the agent movement when it reach
	 * this waypoint.
	 * 
	 * @return the position in 3D.
	 */
	@Override
	public Vector3d getTangent3D() {
		return new Vector3d(this.tx,this.ty,this.tz);
	}

	/** Replies the tangent of the agent movement when it reach
	 * this waypoint.
	 * 
	 * @return the position in 2D.
	 */
	@Override
	public Vector2d getTangent2D() {
		CoordinateSystem3D system = CoordinateSystem3D.getDefaultCoordinateSystem();
		return system.toCoordinateSystem2D(getTangent3D());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Direction1D getTangent1D() {
		throw new UnsupportedOperationException("1D is not supported from 3D"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point1D getPosition1D() {
		throw new UnsupportedOperationException("1D is not supported from 3D"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point1D5 getPosition1D5() {
		throw new UnsupportedOperationException("1.5D is not supported from 3D"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point2d getPosition2D() {
		CoordinateSystem3D system = CoordinateSystem3D.getDefaultCoordinateSystem();
		return system.toCoordinateSystem2D(getPosition3D());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point3d getPosition3D() {
		return new Point3d(this.x,this.y,this.z);
	}
	
}

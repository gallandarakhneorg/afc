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
package fr.utbm.set.jasim.agent.hotpoint.hotpoint2d;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.jasim.agent.hotpoint.WayPoint;

/**
 * A way point is a situated point on which an agent is supposed to pass.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WayPoint2D extends WayPoint {

	private static final long serialVersionUID = -7710002382633008020L;
	
	private final double x,y;
	private final double tx,ty;

	/**
	 * @param x is the position of the hot point.
	 * @param y is the position of the hot point.
	 * @param tx is the tangent of the hot point.
	 * @param ty is the tangent of the hot point.
	 * @param desiredTime is the desired time at which this point must be used by the agent.
	 * @param velocity is the desired velocity that the agent should have
	 * when it reach this waypoint.
	 */
	public WayPoint2D(double x, double y, double tx, double ty, double desiredTime, double velocity) {
		super(velocity,desiredTime);
		this.x = x;
		this.y = y;
		this.tx = tx;
		this.ty = ty;
	}
	
	/**
	 * @param x is the position of the hot point.
	 * @param y is the position of the hot point.
	 * @param tx is the tangent of the hot point.
	 * @param ty is the tangent of the hot point.
	 * @param velocity is the desired velocity that the agent should have
	 * when it reach this waypoint.
	 */
	public WayPoint2D(double x, double y, double tx, double ty, double velocity) {
		super(velocity);
		this.x = x;
		this.y = y;
		this.tx = tx;
		this.ty = ty;
	}
	
	/**
	 * @param x is the position of the hot point.
	 * @param y is the position of the hot point.
	 * @param desiredTime is the desired time at which this point must be used by the agent.
	 * @param velocity is the desired velocity that the agent should have
	 * when it reach this waypoint.
	 */
	public WayPoint2D(double x, double y, double desiredTime, double velocity) {
		this(x,y,Double.NaN,Double.NaN,velocity,desiredTime);
	}
	
	/**
	 * @param x is the position of the hot point.
	 * @param y is the position of the hot point.
	 * @param velocity is the desired velocity that the agent should have
	 * when it reach this waypoint.
	 */
	public WayPoint2D(double x, double y, double velocity) {
		this(x,y,Double.NaN,Double.NaN,velocity);
	}

	/** Replies the tangent of the agent movement when it reach
	 * this waypoint.
	 * 
	 * @return the position in 3D.
	 */
	@Override
	public Vector3d getTangent3D() {
		CoordinateSystem3D system = CoordinateSystem3D.getDefaultCoordinateSystem();
		return system.fromCoordinateSystem2D(getTangent2D());
	}

	/** Replies the tangent of the agent movement when it reach
	 * this waypoint.
	 * 
	 * @return the position in 2D.
	 */
	@Override
	public Vector2d getTangent2D() {
		return new Vector2d(this.tx,this.ty);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Direction1D getTangent1D() {
		throw new UnsupportedOperationException("1D is not supported from 2D"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point1D getPosition1D() {
		throw new UnsupportedOperationException("1D is not supported from 2D"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point1D5 getPosition1D5() {
		throw new UnsupportedOperationException("1.5D is not supported from 2D"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point2d getPosition2D() {
		return new Point2d(this.x,this.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point3d getPosition3D() {
		CoordinateSystem3D system = CoordinateSystem3D.getDefaultCoordinateSystem();
		return system.fromCoordinateSystem2D(getPosition2D());
	}
	
}

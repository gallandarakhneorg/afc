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
package fr.utbm.set.jasim.agent.hotpoint.hotpoint1d5;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.object.Segment1D;
import fr.utbm.set.jasim.agent.hotpoint.GoalPoint;

/**
 * A goal point is a situated point on which an agent is supposed to reach and stay.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GoalPoint1D5 extends GoalPoint {

	private static final long serialVersionUID = 1840369274734694840L;
	
	private final Point1D5 position;
	private final Direction1D segmentDirection;
	
	/**
	 * @param position is the position of the hot point.
	 * @param segmentDirection is the direction allowed to reach the goal.
	 * @param desiredTime is the desired time at which this point must be used by the agent.
	 */
	public GoalPoint1D5(Point1D5 position, Direction1D segmentDirection, double desiredTime) {
		super(desiredTime);
		this.position = position;
		this.segmentDirection = segmentDirection;
	}
	
	/**
	 * @param position is the position of the hot point.
	 * @param segmentDirection is the direction allowed to reach the goal.
	 */
	public GoalPoint1D5(Point1D5 position, Direction1D segmentDirection) {
		this.position = position;
		this.segmentDirection = segmentDirection;
	}
	
	/** Replies the tangent of the agent movement when it reach
	 * this goal.
	 * 
	 * @return the tangent in 1D or 1.5D.
	 */
	@Override
	public Direction1D getTangent1D() {
		return this.segmentDirection;
	}

	/** Replies the tangent of the agent movement when it reach
	 * this goal.
	 * 
	 * @return the tangent in 3D.
	 */
	@Override
	public Vector3d getTangent3D() {
		Vector2d t = getTangent2D();
		return new Vector3d(t.x,t.y,0.);
	}

	/** Replies the tangent of the agent movement when it reach
	 * this goal.
	 * 
	 * @return the tangent in 2D.
	 */
	@Override
	public Vector2d getTangent2D() {
		Segment1D segment = this.position.getSegment();
		Vector2d segmentVec = new Vector2d();
		segmentVec.sub(segment.getLastPoint(), segment.getFirstPoint());
		segmentVec.normalize();
		if (!this.segmentDirection.isSegmentDirection()) segmentVec.negate();
		return segmentVec;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point1D getPosition1D() {
		return this.position.toPoint1D();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point1D5 getPosition1D5() {
		return this.position;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point2d getPosition2D() {
		return this.position.toPoint2D();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point3d getPosition3D() {
		Point2d p = this.position.toPoint2D();
		return new Point3d(p.x,p.y,0.);
	}
	
}

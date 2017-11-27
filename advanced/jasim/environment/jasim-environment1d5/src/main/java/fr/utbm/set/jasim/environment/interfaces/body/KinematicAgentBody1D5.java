/*
 * $Id$
 * 
 * Copyright (c) 2006-10, 2012, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.environment.interfaces.body;

import java.util.UUID;

import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;
import fr.utbm.set.gis.road.primitive.RoadConnection;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.KinematicPerceivable;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.semantics.BodyType;
import fr.utbm.set.jasim.environment.semantics.ObjectType;
import fr.utbm.set.math.AngularUnit;
import fr.utbm.set.math.MeasureUnitUtil;
import fr.utbm.set.math.SpeedUnit;

/**
 * This class describes the body of an situated agent inside a 1D5 environment.
 * The body is the only available interaction mean between an agent and the
 * environment.
 * 
 * @param <INF>
 *            The type of influence this body may receive and forward to the
 *            collector
 * @param <PT>
 *            The type of perception this body may receive
 * @param <B>
 *            The type of Bound associated to the 3D representation of this body
 *            in its environment
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class KinematicAgentBody1D5<INF extends Influence,
								   PT extends Perception,
								   B extends CombinableBounds1D5<RoadSegment>>
extends AgentBody1D5<INF,PT,B>
implements KinematicPerceivable {

	private static final long serialVersionUID = 7307113881847429065L;
	
	private final double maxForwardSpeed;
	private final double maxAngularSpeed;

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param type
	 *            is the type of the body.
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			BodyType type, RoadSegment segment, RoadConnection roadEntry,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			Tuple2d... convexHull) {
		super(ownerIdentifier,
			  perceptionType,
			  boundClass,
			  type,
			  segment,
			  roadEntry,
			  convexHull);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param type
	 *            is the type of the body.
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			BodyType type, RoadSegment segment, RoadConnection roadEntry,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double... convexHull) {
		super(ownerIdentifier,
			  perceptionType,
			  boundClass,
			  type,
			  segment,
			  roadEntry,
			  convexHull);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param types
	 *            are the types of the body.
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			ObjectType[] types, RoadSegment segment, RoadConnection roadEntry,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			Tuple2d... convexHull) {
		super(ownerIdentifier,
			  perceptionType,
			  boundClass,
			  types,
			  segment,
			  roadEntry,
			  convexHull);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param types
	 *            are the types of the body.
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			ObjectType[] types, RoadSegment segment, RoadConnection roadEntry,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double... convexHull) {
		super(ownerIdentifier,
			  perceptionType,
			  boundClass,
			  types,
			  segment,
			  roadEntry,
			  convexHull);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param type
	 *            is the type of the body.
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, BodyType type,
			RoadConnection roadEntry,
			double forwardSpeed, double backwardSpeed, double angularSpeed) {
		super(ownerIdentifier,
			  perceptionType,
			  bounds,
			  type,
			  roadEntry);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param type
	 *            is the type of the body.
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, BodyType type,
			double forwardSpeed, double backwardSpeed, double angularSpeed) {
		super(ownerIdentifier,
			  perceptionType,
			  bounds,
			  type);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param types
	 *            are the types of the body.
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, ObjectType[] types,
			RoadConnection roadEntry,
			double forwardSpeed, double backwardSpeed, double angularSpeed) {
		super(ownerIdentifier,
			  perceptionType,
			  bounds,
			  types,
			  roadEntry);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param types
	 *            are the types of the body.
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, ObjectType[] types,
			double forwardSpeed, double backwardSpeed, double angularSpeed) {
		super(ownerIdentifier,
			  perceptionType,
			  bounds,
			  types);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			RoadSegment segment, RoadConnection roadEntry,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			Tuple2d... convexHull) {
		super(ownerIdentifier,
			  perceptionType,
			  boundClass,
			  segment,
			  roadEntry,
			  convexHull);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			RoadSegment segment, RoadConnection roadEntry,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double... convexHull) {
		super(ownerIdentifier,
				  perceptionType,
				  boundClass,
				  segment,
				  roadEntry,
				  convexHull);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, RoadConnection roadEntry,
			double forwardSpeed, double backwardSpeed, double angularSpeed) {
		super(ownerIdentifier,
			  perceptionType,
			  bounds,
			  roadEntry);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param forwardSpeed
	 *            is the maximal forward speed for this body in m/s.
	 * @param backwardSpeed
	 *            is the maximal backward speed for this body in m/s.
	 * @param angularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 */
	public KinematicAgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds,
			double forwardSpeed, double backwardSpeed, double angularSpeed) {
		super(ownerIdentifier,
			  perceptionType,
			  bounds);
		this.maxForwardSpeed = forwardSpeed;
		this.maxAngularSpeed = angularSpeed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getMaxAngularSpeed() {
		return this.maxAngularSpeed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getMaxAngularSpeed(AngularUnit unit) {
		return MeasureUnitUtil.fromRadiansPerSecond(this.maxAngularSpeed, unit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getMaxLinearSpeed() {
		return this.maxForwardSpeed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getMaxLinearSpeed(SpeedUnit unit) {
		return MeasureUnitUtil.fromMetersPerSecond(this.maxForwardSpeed, unit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3d getLinearUnitVelocity3D() {
		Vector3d v = getLinearVelocity3D();
		if (v.lengthSquared()!=0) v.normalize();
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2d getLinearUnitVelocity2D() {
		Vector2d v = getLinearVelocity2D();
		if (v.lengthSquared()!=0) v.normalize();
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2d getLinearUnitVelocity1D5() {
		Vector2d v = getLinearVelocity1D5();
		if (v.lengthSquared()!=0) v.normalize();
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getLinearUnitVelocity1D() {
		return Math.signum(getLinearVelocity1D());
	}
    
}

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
package fr.utbm.set.jasim.environment.interfaces.body;

import java.util.UUID;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.SteeringPerceivable;
import fr.utbm.set.jasim.environment.model.world.Mesh3D;
import fr.utbm.set.jasim.environment.semantics.BodyType;
import fr.utbm.set.jasim.environment.semantics.ObjectType;
import fr.utbm.set.math.AngularUnit;
import fr.utbm.set.math.MeasureUnitUtil;
import fr.utbm.set.math.SpeedUnit;

/**
 * This class describes the body of an situated agent inside a 3D environment.
 * The body is the only available interaction mean between an agent and the
 * environment.
 * 
 * @param <INF>
 *            The type of influence this body may receive and forward to the
 *            collector
 * @param <PER>
 *            The type of perception this body may receive
 * @param <B>
 *            The type of Bound associated to the 3D representation of this body
 *            in its environment
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SteeringAgentBody3D<INF extends Influence, PER extends Perception, B extends CombinableBounds3D>
extends AgentBody3D<INF,PER,B>
implements SteeringPerceivable {

	private static final long serialVersionUID = 183825582308259878L;
	
	private final double maxLinearSpeed;
	private final double maxAngularSpeed;
	private final double maxLinearAcceleration;
	private final double maxLinearDeceleration;
	private final double maxAngularAcceleration;
	private final double maxAngularDeceleration;

	/**
	 * Builds a new SteeringAgentBody3D
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param type
	 *            is the type of the body.
	 * @param pivot
	 *            is the pivot expressed in local coordinates from the entity
	 *            position.
	 * @param convexHull
	 *            is the convex hull of the entity. All the points are relative
	 *            to the specified pivot
	 * @param maxLinearSpeed is the maximal forward speed for this body in m/s.
	 * @param maxAngularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param maxLinearAcceleration is the maximal linear acceleration for this body in m/s^2.
	 * @param maxLinearDeceleration is the maximal linear deceleration for this body in m/s^2.
	 * @param maxAngularAcceleration
	 *            is the maximal angular acceleration for this body in r/s^2.
	 * @param maxAngularDeceleration
	 *            is the maximal angular deceleration for this body in r/s^2.
	 */
	public SteeringAgentBody3D(UUID ownerIdentifier,
			Class<PER> perceptionType, B bounds, BodyType type,
			Vector3d pivot, Mesh3D convexHull,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		super(ownerIdentifier, perceptionType, bounds, new ObjectType[] { type },
				pivot, convexHull);
		this.maxLinearSpeed = Math.abs(maxLinearSpeed);
		this.maxAngularSpeed = Math.abs(maxAngularSpeed);
		this.maxLinearAcceleration = Math.abs(maxLinearAcceleration);
		this.maxLinearDeceleration = Math.abs(maxLinearDeceleration);
		this.maxAngularAcceleration = Math.abs(maxAngularAcceleration);
		this.maxAngularDeceleration = Math.abs(maxAngularDeceleration);
	}

	/**
	 * Builds a new SteeringAgentBody3D
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param types
	 *            are the types of the body.
	 * @param pivot
	 *            is the pivot expressed in local coordinates from the entity
	 *            position.
	 * @param convexHull
	 *            is the convex hull of the entity. All the points are relative
	 *            to the specified pivot
	 * @param maxLinearSpeed is the maximal forward speed for this body in m/s.
	 * @param maxAngularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param maxLinearAcceleration is the maximal linear acceleration for this body in m/s^2.
	 * @param maxLinearDeceleration is the maximal linear deceleration for this body in m/s^2.
	 * @param maxAngularAcceleration
	 *            is the maximal angular acceleration for this body in r/s^2.
	 * @param maxAngularDeceleration
	 *            is the maximal angular deceleration for this body in r/s^2.
	 */
	public SteeringAgentBody3D(UUID ownerIdentifier,
			Class<PER> perceptionType, B bounds, ObjectType[] types,
			Vector3d pivot, Mesh3D convexHull,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		super(ownerIdentifier, perceptionType, bounds, types, pivot, convexHull);
		this.maxLinearSpeed = Math.abs(maxLinearSpeed);
		this.maxAngularSpeed = Math.abs(maxAngularSpeed);
		this.maxLinearAcceleration = Math.abs(maxLinearAcceleration);
		this.maxLinearDeceleration = Math.abs(maxLinearDeceleration);
		this.maxAngularAcceleration = Math.abs(maxAngularAcceleration);
		this.maxAngularDeceleration = Math.abs(maxAngularDeceleration);
	}

	/**
	 * Builds a new SteeringAgentBody3D
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param type
	 *            is the type of the body.
	 * @param convexHull
	 *            is the convex hull of the entity. If global, mesh
	 *            will be localized according to the body position.
	 * @param maxLinearSpeed is the maximal forward speed for this body in m/s.
	 * @param maxAngularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param maxLinearAcceleration is the maximal linear acceleration for this body in m/s^2.
	 * @param maxLinearDeceleration is the maximal linear deceleration for this body in m/s^2.
	 * @param maxAngularAcceleration
	 *            is the maximal angular acceleration for this body in r/s^2.
	 * @param maxAngularDeceleration
	 *            is the maximal angular deceleration for this body in r/s^2.
	 */
	public SteeringAgentBody3D(UUID ownerIdentifier,
			Class<PER> perceptionType, B bounds, BodyType type,
			Mesh3D convexHull,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		super(ownerIdentifier, perceptionType, bounds, type, convexHull);
		this.maxLinearSpeed = Math.abs(maxLinearSpeed);
		this.maxAngularSpeed = Math.abs(maxAngularSpeed);
		this.maxLinearAcceleration = Math.abs(maxLinearAcceleration);
		this.maxLinearDeceleration = Math.abs(maxLinearDeceleration);
		this.maxAngularAcceleration = Math.abs(maxAngularAcceleration);
		this.maxAngularDeceleration = Math.abs(maxAngularDeceleration);
	}

	/**
	 * Builds a new SteeringAgentBody3D
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param types
	 *            are the types of the body.
	 * @param convexHull
	 *            is the convex hull of the entity. If global, mesh
	 *            will be localized according to the body position.
	 * @param maxLinearSpeed is the maximal forward speed for this body in m/s.
	 * @param maxAngularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param maxLinearAcceleration is the maximal linear acceleration for this body in m/s^2.
	 * @param maxLinearDeceleration is the maximal linear deceleration for this body in m/s^2.
	 * @param maxAngularAcceleration
	 *            is the maximal angular acceleration for this body in r/s^2.
	 * @param maxAngularDeceleration
	 *            is the maximal angular deceleration for this body in r/s^2.
	 */
	public SteeringAgentBody3D(UUID ownerIdentifier,
			Class<PER> perceptionType, B bounds, ObjectType[] types,
			Mesh3D convexHull,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		super(ownerIdentifier, perceptionType, bounds, types, convexHull);
		this.maxLinearSpeed = Math.abs(maxLinearSpeed);
		this.maxAngularSpeed = Math.abs(maxAngularSpeed);
		this.maxLinearAcceleration = Math.abs(maxLinearAcceleration);
		this.maxLinearDeceleration = Math.abs(maxLinearDeceleration);
		this.maxAngularAcceleration = Math.abs(maxAngularAcceleration);
		this.maxAngularDeceleration = Math.abs(maxAngularDeceleration);
	}

	/**
	 * Builds a new SteeringAgentBody3D
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param pivot
	 *            is the pivot expressed in local coordinates from the entity
	 *            position.
	 * @param convexHull
	 *            is the convex hull of the entity. All the points are relative
	 *            to the specified pivot
	 * @param maxLinearSpeed is the maximal forward speed for this body in m/s.
	 * @param maxAngularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param maxLinearAcceleration is the maximal linear acceleration for this body in m/s^2.
	 * @param maxLinearDeceleration is the maximal linear deceleration for this body in m/s^2.
	 * @param maxAngularAcceleration
	 *            is the maximal angular acceleration for this body in r/s^2.
	 * @param maxAngularDeceleration
	 *            is the maximal angular deceleration for this body in r/s^2.
	 */
	public SteeringAgentBody3D(UUID ownerIdentifier,
			Class<PER> perceptionType, B bounds, Vector3d pivot,
			Mesh3D convexHull,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		super(ownerIdentifier, perceptionType, bounds, pivot, convexHull);
		this.maxLinearSpeed = Math.abs(maxLinearSpeed);
		this.maxAngularSpeed = Math.abs(maxAngularSpeed);
		this.maxLinearAcceleration = Math.abs(maxLinearAcceleration);
		this.maxLinearDeceleration = Math.abs(maxLinearDeceleration);
		this.maxAngularAcceleration = Math.abs(maxAngularAcceleration);
		this.maxAngularDeceleration = Math.abs(maxAngularDeceleration);
	}

	/**
	 * Builds a new SteeringAgentBody3D
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param convexHull
	 *            is the convex hull of the entity. If global, mesh
	 *            will be localized according to the body position.
	 * @param maxLinearSpeed is the maximal forward speed for this body in m/s.
	 * @param maxAngularSpeed
	 *            is the maximal angular speed for this body in r/s.
	 * @param maxLinearAcceleration is the maximal linear acceleration for this body in m/s^2.
	 * @param maxLinearDeceleration is the maximal linear deceleration for this body in m/s^2.
	 * @param maxAngularAcceleration
	 *            is the maximal angular acceleration for this body in r/s^2.
	 * @param maxAngularDeceleration
	 *            is the maximal angular deceleration for this body in r/s^2.
	 */
	public SteeringAgentBody3D(UUID ownerIdentifier,
			Class<PER> perceptionType, B bounds, Mesh3D convexHull,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		super(ownerIdentifier, perceptionType, bounds, convexHull);
		this.maxLinearSpeed = Math.abs(maxLinearSpeed);
		this.maxAngularSpeed = Math.abs(maxAngularSpeed);
		this.maxLinearAcceleration = Math.abs(maxLinearAcceleration);
		this.maxLinearDeceleration = Math.abs(maxLinearDeceleration);
		this.maxAngularAcceleration = Math.abs(maxAngularAcceleration);
		this.maxAngularDeceleration = Math.abs(maxAngularDeceleration);
	}

	@Override
	public final double getMaxAngularAcceleration() {
		return this.maxAngularAcceleration;
	}

	@Override
	public final double getMaxAngularAcceleration(AngularUnit unit) {
		return MeasureUnitUtil.fromRadiansPerSecond(this.maxAngularAcceleration, unit);
	}

	@Override
	public final double getMaxAngularDeceleration() {
		return this.maxAngularDeceleration;
	}

	@Override
	public final double getMaxAngularDeceleration(AngularUnit unit) {
		return MeasureUnitUtil.fromRadiansPerSecond(this.maxAngularDeceleration, unit);
	}

	@Override
	public final double getMaxAngularSpeed() {
		return this.maxAngularSpeed;
	}

	@Override
	public final double getMaxAngularSpeed(AngularUnit unit) {
		return MeasureUnitUtil.fromRadiansPerSecond(this.maxAngularSpeed, unit);
	}

	@Override
	public final double getMaxLinearAcceleration() {
		return this.maxLinearAcceleration;
	}

	@Override
	public final double getMaxLinearAcceleration(SpeedUnit unit) {
		return MeasureUnitUtil.fromMetersPerSecond(this.maxLinearAcceleration, unit);
	}

	@Override
	public final double getMaxLinearSpeed() {
		return this.maxLinearSpeed;
	}

	@Override
	public final double getMaxLinearSpeed(SpeedUnit unit) {
		return MeasureUnitUtil.fromMetersPerSecond(this.maxLinearSpeed, unit);
	}

	@Override
	public final double getMaxLinearDeceleration() {
		return this.maxLinearDeceleration;
	}

	@Override
	public final double getMaxLinearDeceleration(SpeedUnit unit) {
		return MeasureUnitUtil.fromMetersPerSecond(this.maxLinearDeceleration, unit);
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

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
package fr.utbm.set.jasim.environment.model.influencereaction;

import java.util.concurrent.TimeUnit;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.transform.Transform2D;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencable;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence2D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencer;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.KinematicEntity;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.SteeringEntity;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.jasim.environment.time.TimeManager;
import fr.utbm.set.physics.PhysicsUtil;

/**
 * This solver is filtering the influences into environmental actions
 * without any check according to the other influences but using 
 * a &laquo;keep-on-floor&raquo; constraint.
 *
 * @param <ME> is the type of the supported entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class KeepOnFloorImmediateInfluenceApplier3D<ME extends MobileEntity3D<?>>
extends ImmediateInfluenceApplier<EnvironmentalAction3D,ME> {

	/**
	 * Create a solver with link to a dispatcher nor a collector.
	 */
	public KeepOnFloorImmediateInfluenceApplier3D() {
		//
	}
	
	/** Invoked to filter an influence.
	 * <p>
	 * Don't forget to invoke {@link Influencer#setLastInfluenceStatus(fr.utbm.set.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus)}
	 * in the overridden functions.
	 * 
	 * @param influence is the influence to filter.
	 * @return the corresponding action
	 */
	@Override
	protected EnvironmentalAction3D filterStandardInfluence(
			Influencer defaultInfluencer,
			Influencable defaultInfluencedObject,
			Influence influence) {
		TimeManager timeManager = getTimeManager();
		assert(timeManager!=null);
		Clock clock = timeManager.getClock();
		assert(clock!=null);

		Influencer influencer = influence.getInfluencer();
		if (influencer==null) influencer = defaultInfluencer;
		Influencable object = influence.getInfluencedObject();
		if (object==null) object = defaultInfluencedObject;
		Point3d position = object.getPosition3D();
		Transform3D tr;
		
		if (Double.isNaN(position.x) || Double.isNaN(position.y) || Double.isNaN(position.z)) {
			tr = new Transform3D();
			tr.setIdentity();
			influencer.setLastInfluenceStatus(InfluenceApplicationStatus.INVALID_POSITION);
		}
		else {
			Ground ground = getGround();
			
			influencer.setLastInfluenceStatus(InfluenceApplicationStatus.SUCCESS);
			if (influence instanceof Influence2D) {
				if (object instanceof SteeringEntity) {
					tr = computeSteeringTransformation(
							((Influence2D)influence).getTransformation(),
							clock, 
							(SteeringEntity)object);
				}
				else if (object instanceof KinematicEntity) {
					tr = computeKinematicTransformation(
							((Influence2D)influence).getTransformation(),
							clock, 
							(KinematicEntity)object);
				}
				else {
					throw new IllegalArgumentException("not kinematic nor steering entity"); //$NON-NLS-1$
				}
			}
			else  if (influence instanceof Influence3D) {
				if (object instanceof SteeringEntity) {
					tr = computeSteeringTransformation(
							((Influence3D)influence).getTransformation(),
							clock, 
							(SteeringEntity)object);
				}
				else if (object instanceof KinematicEntity) {
					tr = computeKinematicTransformation(
							((Influence3D)influence).getTransformation(),
							clock, 
							(KinematicEntity)object);
				}
				else {
					throw new IllegalArgumentException("not kinematic nor steering entity"); //$NON-NLS-1$
				}
			}
			else 
				throw new IllegalArgumentException("solver does not support influences of type: "+influence.getClass().getCanonicalName()); //$NON-NLS-1$

			keepOnFloor(ground, position.x, position.y, position.z, tr, influencer);
		}
		return new DefaultEnvironmentalAction3D(object,clock,tr);
	}
	
	private static Transform3D computeSteeringTransformation(
			Transform3D original,
			Clock clock, 
			SteeringEntity entity) {
		Vector3d v = new Vector3d();
		Quat4d q = new Quat4d();
		AxisAngle4d aa = new AxisAngle4d();
		
		original.get(v);
		original.get(q);
		aa.set(q);
		
		double dt = clock.getSimulationStepDuration(TimeUnit.SECONDS);
		
		v = PhysicsUtil.motionNewtonLaw3D(
				entity.getLinearVelocity3D(), 
				0,
				entity.getMaxLinearSpeed(),
				v,
				-entity.getMaxLinearDeceleration(),
				entity.getMaxLinearAcceleration(),
				dt);
		
		aa.angle = PhysicsUtil.motionNewtonLaw1D(
				entity.getAngularSpeed(),
				0,
				entity.getMaxAngularSpeed(),
				aa.angle,
				-entity.getMaxAngularDeceleration(),
				entity.getMaxAngularAcceleration(),
				dt);
		
		Transform3D tr = new Transform3D();
		tr.setTranslation(v);
		if (aa.angle!=0.) {
			tr.setRotation(aa);
		}
		return tr;
	}

	private static Transform3D computeSteeringTransformation(
			Transform2D original,
			Clock clock, 
			SteeringEntity entity) {
		Vector2d v = original.getTranslationVector();
		double angle = original.getRotation();
		
		double dt = clock.getSimulationStepDuration(TimeUnit.SECONDS);
		
		v = PhysicsUtil.motionNewtonLaw2D(
				entity.getLinearVelocity2D(), 
				0,
				entity.getMaxLinearSpeed(),
				v,
				-entity.getMaxLinearDeceleration(),
				entity.getMaxLinearAcceleration(),
				dt);
		
		angle = PhysicsUtil.motionNewtonLaw1D(
				entity.getAngularSpeed(),
				0,
				entity.getMaxAngularSpeed(),
				angle,
				-entity.getMaxAngularDeceleration(),
				entity.getMaxAngularAcceleration(),
				dt);
		
		Transform3D tr = new Transform3D();
		tr.setTranslation(v.x,v.y,0.);
		if (angle!=0.) {
			tr.setRotation(new AxisAngle4d(
					CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
					angle));
		}
		return tr;
	}

	private static Transform3D computeKinematicTransformation(
			Transform3D original,
			Clock clock, 
			KinematicEntity entity) {
		Vector3d v = new Vector3d();
		Quat4d q = new Quat4d();
		AxisAngle4d aa = new AxisAngle4d();
		original.get(v);
		original.get(q);
		aa.set(q);
		
		double dt = clock.getSimulationStepDuration(TimeUnit.SECONDS);
		
		v = PhysicsUtil.motionNewtonEuler1Law3D(
				v, 
				0,
				entity.getMaxLinearSpeed(),
				dt);
		
		aa.angle = PhysicsUtil.motionNewtonEuler1Law1D(
				aa.angle,
				0,
				entity.getMaxAngularSpeed(),
				dt);
		
		Transform3D tr = new Transform3D();
		tr.setTranslation(v);
		if (aa.angle!=0.) {
			tr.setRotation(aa);
		}
		return tr;
	}

	private static Transform3D computeKinematicTransformation(
			Transform2D original,
			Clock clock, 
			KinematicEntity entity) {
		Vector2d v = original.getTranslationVector();
		double angle = original.getRotation();
		
		double dt = clock.getSimulationStepDuration(TimeUnit.SECONDS);
		
		v = PhysicsUtil.motionNewtonEuler1Law2D(
				v, 
				0,
				entity.getMaxLinearSpeed(),
				dt);
		
		angle = PhysicsUtil.motionNewtonEuler1Law1D(
				angle,
				0,
				entity.getMaxAngularSpeed(),
				dt);
		
		Transform3D tr = new Transform3D();
		tr.setTranslation(v.x,v.y,0.);
		if (angle!=0.) {
			tr.setRotation(new AxisAngle4d(
					CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(),
					angle));
		}
		return tr;
	}

	/** Compute the position of the object to keep it on the floor.
	 * 
	 * @param ground is the ground object. Could be <code>null</code>
	 * @param x is the position of the object
	 * @param y is the position of the object
	 * @param z is the position of the object
	 * @param tr is the transformation to translate with a keep on floor.
	 * @param influencer is the object that sent the influence.
	 */
	@SuppressWarnings("static-method")
	protected void keepOnFloor(Ground ground, double x, double y, double z, Transform3D tr, Influencer influencer) {
		assert(tr!=null);
		if (ground!=null) {
			double nx = x+tr.getTranslationX();
			double ny = y+tr.getTranslationY();
			
			if (Double.isNaN(nx) || Double.isNaN(ny)) {
				influencer.setLastInfluenceStatus(InfluenceApplicationStatus.INVALID_MOVE_VALUE);
				tr.setTranslation(0,0,0);
			}
			else {
				
				double height = ground.getHeightAt(nx,ny);
				if (!Double.isNaN(height)) {
					influencer.setLastInfluenceStatus(InfluenceApplicationStatus.SUCCESS);
					tr.m23 = height - z;
				}
				else {
					influencer.setLastInfluenceStatus(InfluenceApplicationStatus.MOVE_DISCARTED);
					tr.setTranslation(0,0,0);
				}
			}
		}
		else {
			tr.setIdentity();
			influencer.setLastInfluenceStatus(InfluenceApplicationStatus.MOVE_DISCARTED);
		}
	}

}

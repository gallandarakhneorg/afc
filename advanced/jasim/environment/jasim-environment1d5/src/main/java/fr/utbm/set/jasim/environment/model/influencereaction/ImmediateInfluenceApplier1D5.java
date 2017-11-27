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

import javax.vecmath.Vector2d;

import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.transform.Transform1D5;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencable;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence1D5;
import fr.utbm.set.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencer;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.SteeringEntity;
import fr.utbm.set.jasim.environment.model.world.MobileEntity1D5;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.jasim.environment.time.TimeManager;
import fr.utbm.set.physics.PhysicsUtil;

/**
 * This solver is filtering the influences into environmental actions
 * without any check according to the other influences.
 * The action is immediately put in the action collector.
 * 
 * @param <ME> is the type of the supported mobile entities.
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class ImmediateInfluenceApplier1D5<ME extends MobileEntity1D5<?>>
extends ImmediateInfluenceApplier<EnvironmentalAction1D5,ME> {

	/**
	 * Create a solver with link to a dispatcher nor a collector.
	 */
	public ImmediateInfluenceApplier1D5() {
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
	@SuppressWarnings("unchecked")
	@Override
	public EnvironmentalAction1D5 filterStandardInfluence(
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
		
		Influence1D5<RoadSegment> inf = (Influence1D5<RoadSegment>)influence;
		
		Transform1D5<RoadSegment> tr;
		
		if (object instanceof SteeringEntity) {
			tr = computeSteeringTransformation(inf, clock, (SteeringEntity)object);
		}
		else {
			tr = inf.getTransformation(clock);
		}
		
		influencer.setLastInfluenceStatus(InfluenceApplicationStatus.SUCCESS);
		
		Point1D5 previousPosition = object.getPosition1D5();
		
		return new DefaultEnvironmentalAction1D5(object,clock,tr, previousPosition);
	}
	
	private static Transform1D5<RoadSegment> computeSteeringTransformation(
			Influence1D5<RoadSegment> influence, 
			Clock clock, 
			SteeringEntity entity) {
		Transform1D5<RoadSegment> requestedTransformation = influence.getTransformation();
		
		Vector2d move = PhysicsUtil.motionNewtonLaw1D5(
				entity.getLinearVelocity1D5(), 
				0,
				entity.getMaxLinearSpeed(), 
				new Vector2d(requestedTransformation.getCurvilineTransformation(), requestedTransformation.getJuttingTransformation()),
				-entity.getMaxLinearDeceleration(),
				entity.getMaxLinearAcceleration(), 
				clock.getSimulationStepDuration(TimeUnit.SECONDS));
		
		return new Transform1D5<RoadSegment>(
				requestedTransformation.getPath(),
				requestedTransformation.getFirstSegmentPathDirection(),
				move.x, move.y);
	}
	
}

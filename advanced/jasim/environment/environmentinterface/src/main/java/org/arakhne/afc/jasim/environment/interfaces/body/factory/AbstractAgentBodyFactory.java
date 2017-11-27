/*
 * 
 * $Id$
 * 
 * Copyright (c) 2006-09, 2012, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package org.arakhne.afc.jasim.environment.interfaces.body.factory;

import java.util.Collection;

import org.arakhne.afc.jasim.agent.SituatedAgent;
import org.arakhne.afc.jasim.environment.interfaces.body.AgentBody;
import org.arakhne.afc.jasim.environment.interfaces.body.Mesh;
import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influence;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.Perception;
import org.arakhne.afc.jasim.environment.semantics.BodyType;
import org.arakhne.afc.jasim.environment.semantics.Semantic;

/**
 * Default implementation of a factory that permits to create an instance of an agent body.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractAgentBodyFactory implements AgentBodyFactory {

	private final BodyDescription body;
	private final Iterable<FrustumDescription> frustums;

	/**
	 * @param body describes the new bodies
	 * @param frustums describes the new frustums.
	 */
	public AbstractAgentBodyFactory(BodyDescription body, Iterable<FrustumDescription> frustums) {
		this.body = body;
		this.frustums = frustums;
	}

	/** Replies the body description embedded in this factory.
	 * 
	 * @return the body description embedded in this factory.
	 */
	public BodyDescription getDefaultBodyDescription() {
		return this.body;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void attachDefaultFrustums(AgentBody<?,?> bodyToChange) {
		for(FrustumDescription frustum : this.frustums) {
			switch(frustum.getFrustumType()) {
			case RECTANGLE:
				{
					RectangularFrustumDescription f = (RectangularFrustumDescription)frustum;
					attachRectangularFrustum(bodyToChange,
							f.getEyePosition(), 
							f.getFarDistance(), 
							f.getLeftRightDistance(),
							f.getBottomUpDistance());
				}
				break;
			case SPHERE:
				{
					CircularFrustumDescription f = (CircularFrustumDescription)frustum;
					attachCircularFrustum(bodyToChange,
							f.getEyePosition(), 
							f.getRadius());
				}
				break;
			case PYRAMID:
				{
					TriangularFrustumDescription f = (TriangularFrustumDescription)frustum;
					attachTriangularFrustum(bodyToChange,
							f.getEyePosition(),
							f.getNearDistance(),
							f.getFarDistance(), 
							f.getHorizontalAngle(),
							f.getVerticalAngle());
				}
				break;
			case PEDESTRIAN:
				{
					PedestrianFrustumDescription f = (PedestrianFrustumDescription)frustum;
					attachPedestrianFrustum(bodyToChange,
							f.getEyePosition(),
							f.getNearDistance(),
							f.getFarDistance(), 
							f.getHorizontalAngle(),
							f.getVerticalAngle());
				}
				break;
			default:
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createAgentBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			BodyType bodyType, 
			double forwardSpeed, double backwardSpeed,
			double angularSpeed, double maxLinearAcceleration,
			double maxLinearDeceleration, double maxAngularAcceleration,
			double maxAngularDeceleration) {
		return createDefaultBody(
				agent, 
				perceptionType, 
				bodyType,
				forwardSpeed, backwardSpeed, angularSpeed,
				maxLinearAcceleration, maxLinearDeceleration,
				maxAngularAcceleration, maxAngularDeceleration);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated
	 */
	@Deprecated
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			BodyType bodyType, 
			double forwardSpeed, double backwardSpeed,
			double angularSpeed, double maxLinearAcceleration,
			double maxLinearDeceleration, double maxAngularAcceleration,
			double maxAngularDeceleration) {
		return createAgentBody(
				agent, 
				perceptionType, 
				bodyType, this.body.getSemantics(),
				forwardSpeed, backwardSpeed, angularSpeed,
				maxLinearAcceleration, maxLinearDeceleration,
				maxAngularAcceleration, maxAngularDeceleration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			BodyType bodyType, 
			double maxLinearSpeed, double maxAngularSpeed, double maxLinearAcceleration,
			double maxLinearDeceleration, double maxAngularAcceleration,
			double maxAngularDeceleration) {
		return createDefaultBody(
				agent, 
				perceptionType, 
				bodyType, this.body.getSemantics(),
				maxLinearSpeed, maxAngularSpeed,
				maxLinearAcceleration, maxLinearDeceleration,
				maxAngularAcceleration, maxAngularDeceleration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createAgentBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			BodyType bodyType, Collection<? extends Semantic> semantics, 
			double forwardSpeed, double backwardSpeed,
			double angularSpeed, double maxLinearAcceleration,
			double maxLinearDeceleration, double maxAngularAcceleration,
			double maxAngularDeceleration) {
		return createDefaultBody(
				agent, perceptionType, bodyType, semantics,
				forwardSpeed, backwardSpeed,
				angularSpeed, maxLinearAcceleration,
				maxLinearDeceleration, maxAngularAcceleration,
				maxAngularDeceleration);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated
	 */
	@Deprecated
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			BodyType bodyType, Collection<? extends Semantic> semantics, 
			double forwardSpeed, double backwardSpeed,
			double angularSpeed, double maxLinearAcceleration,
			double maxLinearDeceleration, double maxAngularAcceleration,
			double maxAngularDeceleration) {
		if (this.body.isPedestrian()) {
			return createPedestrianBody(
					agent, perceptionType, 
					this.body.getPedestrianHeight(), this.body.getPedestrianRadius(), 
					bodyType, semantics,
					forwardSpeed, backwardSpeed, angularSpeed, 
					maxLinearAcceleration, maxLinearDeceleration, 
					maxAngularAcceleration, maxAngularDeceleration);
		}
		
		if (this.body.isVehicle()) {
			return createVehicleBody(
					agent, perceptionType, 
					this.body.getVehicleLength(), this.body.getVehicleLateralSize(), 
					bodyType, semantics,
					forwardSpeed, backwardSpeed, angularSpeed, 
					maxLinearAcceleration, maxLinearDeceleration, 
					maxAngularAcceleration, maxAngularDeceleration);
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			BodyType bodyType, Collection<? extends Semantic> semantics, 
			double maxLinearSpeed, double maxAngularSpeed, double maxLinearAcceleration,
			double maxLinearDeceleration, double maxAngularAcceleration,
			double maxAngularDeceleration) {
		if (this.body.isPedestrian()) {
			return createPedestrianBody(
					agent, perceptionType, 
					this.body.getPedestrianHeight(), this.body.getPedestrianRadius(), 
					bodyType, semantics,
					maxLinearSpeed, maxAngularSpeed, 
					maxLinearAcceleration, maxLinearDeceleration, 
					maxAngularAcceleration, maxAngularDeceleration);
		}
		
		if (this.body.isVehicle()) {
			return createVehicleBody(
					agent, perceptionType, 
					this.body.getVehicleLength(), this.body.getVehicleLateralSize(), 
					bodyType, semantics,
					maxLinearSpeed, maxLinearSpeed, maxAngularSpeed, 
					maxLinearAcceleration, maxLinearDeceleration, 
					maxAngularAcceleration, maxAngularDeceleration);
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createAgentBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed) {
		return createDefaultBody(
				agent, perceptionType, bodyType, forwardSpeed, backwardSpeed, angularSpeed);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated
	 */
	@Deprecated
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed) {
		if (this.body.isPedestrian()) {
			return createPedestrianBody(
					agent, perceptionType, 
					this.body.getPedestrianHeight(), this.body.getPedestrianRadius(), 
					bodyType,
					forwardSpeed, backwardSpeed, angularSpeed);
		}
		
		if (this.body.isVehicle()) {
			return createVehicleBody(
					agent, perceptionType, 
					this.body.getVehicleLength(), this.body.getVehicleLateralSize(), 
					bodyType,
					forwardSpeed, backwardSpeed, angularSpeed);
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			BodyType bodyType,
			double maxLinearSpeed, double maxAngularSpeed) {
		if (this.body.isPedestrian()) {
			return createPedestrianBody(
					agent, perceptionType, 
					this.body.getPedestrianHeight(), this.body.getPedestrianRadius(), 
					bodyType,
					maxLinearSpeed, maxAngularSpeed);
		}
		
		if (this.body.isVehicle()) {
			return createVehicleBody(
					agent, perceptionType, 
					this.body.getVehicleLength(), this.body.getVehicleLateralSize(), 
					bodyType,
					maxLinearSpeed, maxLinearSpeed, maxAngularSpeed);
		}

		return null;
	}

	/** {@inheritDoc}
	 * @deprecated
	 */
	@Deprecated
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I,P>
	createPedestrianBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyHeight, double bodyRadius,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		return createPedestrianBody(
				agent, 
				perceptionType,
				bodyHeight, bodyRadius,
				bodyType, this.body.getSemantics(),
				forwardSpeed, backwardSpeed, angularSpeed,
				maxLinearAcceleration, maxLinearDeceleration,
				maxAngularAcceleration, maxAngularDeceleration);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I,P>
	createPedestrianBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyHeight, double bodyRadius,
			BodyType bodyType,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		return createPedestrianBody(
				agent, 
				perceptionType,
				bodyHeight, bodyRadius,
				bodyType, this.body.getSemantics(),
				maxLinearSpeed, maxAngularSpeed,
				maxLinearAcceleration, maxLinearDeceleration,
				maxAngularAcceleration, maxAngularDeceleration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createVehicleBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			double bodyLength, double bodyLateralSize,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		return createVehicleBody(
				agent, perceptionType,
				bodyLength, bodyLateralSize,
				bodyType, this.body.getSemantics(),
				forwardSpeed, backwardSpeed, angularSpeed,
				maxLinearAcceleration, maxLinearDeceleration,
				maxAngularAcceleration, maxAngularDeceleration);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated
	 */
	@Deprecated
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			Mesh<?> mesh, BodyType bodyType,
			Collection<? extends Semantic> semantics, double forwardSpeed,
			double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		return createMeshedBody(
				agent, perceptionType,
				mesh,
				bodyType, this.body.getSemantics(),
				forwardSpeed, backwardSpeed, angularSpeed,
				maxLinearAcceleration, maxLinearDeceleration,
				maxAngularAcceleration, maxAngularDeceleration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
			SituatedAgent<?, I, P> agent, Class<P> perceptionType,
			Mesh<?> mesh, BodyType bodyType,
			Collection<? extends Semantic> semantics, 
			double maxLinearSpeed,
			double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration) {
		return createMeshedBody(
				agent, perceptionType,
				mesh,
				bodyType, this.body.getSemantics(),
				maxLinearSpeed, maxAngularSpeed,
				maxLinearAcceleration, maxLinearDeceleration,
				maxAngularAcceleration, maxAngularDeceleration);
	}

}

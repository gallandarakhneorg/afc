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
import java.util.UUID;

import org.arakhne.afc.jasim.agent.SituatedAgent;
import org.arakhne.afc.jasim.environment.interfaces.body.AgentBody;
import org.arakhne.afc.jasim.environment.interfaces.body.Mesh;
import org.arakhne.afc.jasim.environment.interfaces.body.frustums.Frustum;
import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influence;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.KinematicEntity;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.Perception;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.SteeringEntity;
import org.arakhne.afc.jasim.environment.semantics.BodyType;
import org.arakhne.afc.jasim.environment.semantics.Semantic;

/**
 * Factory that permits to create an instance of an agent body.
 * <p>
 * The factory implementation is given by the situated environment itself. 
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface AgentBodyFactory {

	/** Create a instance of a steering pedestrian body based on the distance from feet
	 * to head and the radius.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyHeight is the height of the body.
	 * @param bodyRadius is the radius of the body.
	 * @param bodyType is the type of the body.
	 * @param semantics is the list of additional semantics.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 * @deprecated
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createPedestrianBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyHeight, double bodyRadius,
			BodyType bodyType, Collection<? extends Semantic> semantics,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering pedestrian body based on the distance from feet
	 * to head and the radius.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyHeight is the height of the body.
	 * @param bodyRadius is the radius of the body.
	 * @param bodyType is the type of the body.
	 * @param semantics is the list of additional semantics.
	 * @param maxLinearSpeed is the maximal translation speed.
	 * @param maxAngularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createPedestrianBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyHeight, double bodyRadius,
			BodyType bodyType, Collection<? extends Semantic> semantics,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering pedestrian body based on the distance from feet
	 * to head and the radius.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyHeight is the height of the body.
	 * @param bodyRadius is the radius of the body.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 * @deprecated
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createPedestrianBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyHeight, double bodyRadius,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering pedestrian body based on the distance from feet
	 * to head and the radius.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyHeight is the height of the body.
	 * @param bodyRadius is the radius of the body.
	 * @param bodyType is the type of the body.
	 * @param maxLinearSpeed is the maximal translation speed.
	 * @param maxAngularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createPedestrianBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyHeight, double bodyRadius,
			BodyType bodyType,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering vehicle body based on the length of
	 * the vehicle and its lateral size.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyLength is the length of the body.
	 * @param bodyLateralSize is the lateral size of the body.
	 * @param bodyType is the type of the body.
	 * @param semantics is the list of additional semantics.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createVehicleBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyLength, double bodyLateralSize,
			BodyType bodyType, Collection<? extends Semantic> semantics,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering vehicle body based on the length of
	 * the vehicle and its lateral size.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyLength is the length of the body.
	 * @param bodyLateralSize is the lateral size of the body.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createVehicleBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyLength, double bodyLateralSize,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering body according to the factory's
	 * defaults (size...).
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyType is the type of the body.
	 * @param semantics is the list of additional semantics.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 * @deprecated see {@link #createDefaultBody(SituatedAgent, Class, BodyType, Collection, double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createAgentBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			BodyType bodyType, Collection<? extends Semantic> semantics,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering body according to the factory's
	 * defaults (size...).
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyType is the type of the body.
	 * @param semantics is the list of additional semantics.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 * @deprecated see {@link #createDefaultBody(SituatedAgent, Class, BodyType, Collection, double, double, double, double, double, double)}
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createDefaultBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			BodyType bodyType, Collection<? extends Semantic> semantics,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering body according to the factory's
	 * defaults (size...).
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyType is the type of the body.
	 * @param semantics is the list of additional semantics.
	 * @param maxLinearSpeed is the maximal translation speed.
	 * @param maxAngularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createDefaultBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			BodyType bodyType, Collection<? extends Semantic> semantics,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering body according to the factory's
	 * defaults (size...).
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 * @deprecated see {@link #createDefaultBody(SituatedAgent, Class, BodyType, double, double, double, double, double, double, double)}
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createAgentBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering body according to the factory's
	 * defaults (size...).
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 * @deprecated
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createDefaultBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a steering body according to the factory's
	 * defaults (size...).
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyType is the type of the body.
	 * @param maxLinearSpeed is the maximal translation speed.
	 * @param maxAngularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createDefaultBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			BodyType bodyType,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a standard (non steering) 
	 * pedestrian body based on the distance from feet
	 * to head and the radius.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyHeight is the height of the body.
	 * @param bodyRadius is the radius of the body.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see KinematicEntity
	 * @deprecated
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createPedestrianBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyHeight, double bodyRadius,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed);
	
	/** Create a instance of a standard (non steering) 
	 * pedestrian body based on the distance from feet
	 * to head and the radius.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyHeight is the height of the body.
	 * @param bodyRadius is the radius of the body.
	 * @param bodyType is the type of the body.
	 * @param maxLinearSpeed is the maximal translation speed.
	 * @param maxAngularSpeed is the maximal rotation speed.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see KinematicEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createPedestrianBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyHeight, double bodyRadius,
			BodyType bodyType,
			double maxLinearSpeed, double maxAngularSpeed);

	/** Create a instance of a standard (non steering) 
	 * pedestrian body based on the distance from feet
	 * to head and the radius.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param bodyID is the wished identifier for the created body.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyHeight is the height of the body.
	 * @param bodyRadius is the radius of the body.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see KinematicEntity
	 * @deprecated
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createPedestrianBody(UUID bodyID,
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyHeight, double bodyRadius,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed);
	
	/** Create a instance of a standard (non steering) 
	 * pedestrian body based on the distance from feet
	 * to head and the radius.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param bodyID is the wished identifier for the created body.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyHeight is the height of the body.
	 * @param bodyRadius is the radius of the body.
	 * @param bodyType is the type of the body.
	 * @param maxLinearSpeed is the maximal translation speed.
	 * @param maxAngularSpeed is the maximal rotation speed.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see KinematicEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createPedestrianBody(UUID bodyID,
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyHeight, double bodyRadius,
			BodyType bodyType,
			double maxLinearSpeed, double maxAngularSpeed);

	/** Create a instance of a standard (non steering) 
	 * vehicle body based on the length of
	 * the vehicle and its lateral size.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyLength is the length of the body.
	 * @param bodyLateralSize is the lateral size of the body.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see KinematicEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createVehicleBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			double bodyLength, double bodyLateralSize,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed);

	/** Create a instance of a standard (non steering) body according to the factory's
	 * defaults (size...).
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see KinematicEntity
	 * @deprecated see {@link #createDefaultBody(SituatedAgent, Class, BodyType, double, double, double)}
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createAgentBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed);

	/** Create a instance of a standard (non steering) body according to the factory's
	 * defaults (size...).
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see KinematicEntity
	 * @deprecated see {@link #createDefaultBody(SituatedAgent, Class, BodyType, double, double)}
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createDefaultBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed);

	/** Create a instance of a standard (non steering) body according to the factory's
	 * defaults (size...).
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param bodyType is the type of the body.
	 * @param maxLinearSpeed is the maximal translation speed.
	 * @param maxAngularSpeed is the maximal rotation speed.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see KinematicEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createDefaultBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			BodyType bodyType,
			double maxLinearSpeed, double maxAngularSpeed);

	/** Attach a circular or spherical frustum to the given body.
	 *
	 * @param body is the agent body on which a frustum must be attached.
	 * @param eyeVerticalPositionFromGround is the distance from the ground to the eye.
	 * @param radius is the radius of the frustum.
	 * @return the attached frustum.
	 */
	public Frustum<?,?,?> attachCircularFrustum(AgentBody<?,?> body,
			double eyeVerticalPositionFromGround,
			double radius);

	/** Attach the default frustums if they exist.
	 *
	 * @param body is the agent body on which a frustum must be attached.
	 */
	public void attachDefaultFrustums(AgentBody<?,?> body);

	/** Remove all the already attached frustum from the given body.
	 *
	 * @param body is the agent body on which frustums must be removed.
	 */
	public void detachAllFrustums(AgentBody<?,?> body);

	/** Attach a pedestrian frustum to the given body.
	 *
	 * @param body is the agent body on which a frustum must be attached.
	 * @param eyeVerticalPositionFromGround is the distance from the ground to the eye.
	 * @param nearDistance is the distance to the near plane.
	 * @param farDistance is the distance to the far plane.
	 * @param horizontalOpennessAngle is the angle of the horizontal view area.
	 * @param verticalOpennessAngle is the angle of the vertical view area.
	 * @return the attached frustum.
	 */
	public Frustum<?,?,?> attachPedestrianFrustum(AgentBody<?,?> body,
			double eyeVerticalPositionFromGround,
			double nearDistance, double farDistance,
			double horizontalOpennessAngle, double verticalOpennessAngle);

	/** Attach a triangular or pyramidal frustum to the given body.
	 *
	 * @param body is the agent body on which a frustum must be attached.
	 * @param eyeVerticalPositionFromGround is the distance from the ground to the eye.
	 * @param nearDistance is the distance to the near plane.
	 * @param farDistance is the distance to the far plane.
	 * @param horizontalOpennessAngle is the angle of the horizontal view area.
	 * @param verticalOpennessAngle is the angle of the vertical view area.
	 * @return the attached frustum.
	 */
	public Frustum<?,?,?> attachTriangularFrustum(AgentBody<?,?> body,
			double eyeVerticalPositionFromGround,
			double nearDistance, double farDistance,
			double horizontalOpennessAngle, double verticalOpennessAngle);

	/** Attach a circular or spherical frustum to the given body.
	 *
	 * @param body is the agent body on which a frustum must be attached.
	 * @param eyeVerticalPositionFromGround is the distance from the ground to the eye.
	 * @param farDistance is the distance from the eye to the far plane.
	 * @param leftRightDistance is the distance between the left and the right planes.
	 * @param upDownDistance is the distance between the top and the bottom planes.
	 * @return the attached frustum.
	 */
	public Frustum<?,?,?> attachRectangularFrustum(AgentBody<?,?> body, 
			double eyeVerticalPositionFromGround,
			double farDistance, double leftRightDistance, double upDownDistance);

	/** Create a instance of a spherical body for a steering agent.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param mesh is the mesh that is representing the shape of the body.
	 * @param bodyType is the type of the body.
	 * @param semantics is the list of additional semantics.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 * @deprecated
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createMeshedBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			Mesh<?> mesh,
			BodyType bodyType, Collection<? extends Semantic> semantics,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a spherical body for a steering agent.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param mesh is the mesh that is representing the shape of the body.
	 * @param bodyType is the type of the body.
	 * @param semantics is the list of additional semantics.
	 * @param maxLinearSpeed is the maximal translation speed.
	 * @param maxAngularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createMeshedBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			Mesh<?> mesh,
			BodyType bodyType, Collection<? extends Semantic> semantics,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a spherical body for a not-steering agent.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param mesh is the mesh that is representing the shape of the body.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see KinematicEntity
	 * @deprecated
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createMeshedBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			Mesh<?> mesh,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed);

	/** Create a instance of a spherical body for a not-steering agent.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param mesh is the mesh that is representing the shape of the body.
	 * @param bodyType is the type of the body.
	 * @param maxLinearSpeed is the maximal translation speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see KinematicEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createMeshedBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			Mesh<?> mesh,
			BodyType bodyType,
			double maxLinearSpeed, double angularSpeed);

	/** Create a instance of a spherical body for a steering agent.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param mesh is the mesh that is representing the shape of the body.
	 * @param bodyType is the type of the body.
	 * @param forwardSpeed is the maximal forward speed.
	 * @param backwardSpeed is the maximal backward speed.
	 * @param angularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 * @deprecated
	 */
	@Deprecated
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createMeshedBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			Mesh<?> mesh,
			BodyType bodyType,
			double forwardSpeed, double backwardSpeed, double angularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

	/** Create a instance of a spherical body for a steering agent.
	 *
	 * @param <I> is the type of the supported influence.
	 * @param <P> is the type of the supported perception.
	 * @param agent is the agent for which the body must be spawned.
	 * @param perceptionType is the type of the supported perceptions.
	 * @param mesh is the mesh that is representing the shape of the body.
	 * @param bodyType is the type of the body.
	 * @param maxLinearSpeed is the maximal translation speed.
	 * @param maxAngularSpeed is the maximal rotation speed.
	 * @param maxLinearAcceleration is the maximal linear acceleration.
	 * @param maxLinearDeceleration is the maximal linear deceleration.
	 * @param maxAngularAcceleration is the maximal angular acceleration.
	 * @param maxAngularDeceleration is the maximal angular deceleration.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @see SteeringEntity
	 */
	public <I extends Influence, P extends Perception> AgentBody<I,P>
	createMeshedBody(
			SituatedAgent<?,I,P> agent, 
			Class<P> perceptionType,
			Mesh<?> mesh,
			BodyType bodyType,
			double maxLinearSpeed, double maxAngularSpeed,
			double maxLinearAcceleration, double maxLinearDeceleration,
			double maxAngularAcceleration, double maxAngularDeceleration);

}

/* 
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
package fr.utbm.set.jasim.environment.model.place;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;
import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.EuclidianDirection;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.gis.road.primitive.RoadConnection;
import fr.utbm.set.gis.road.primitive.RoadNetwork;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody1D5;
import fr.utbm.set.jasim.environment.interfaces.body.BodyContainerEnvironment;
import fr.utbm.set.jasim.environment.interfaces.body.KinematicAgentBody1D5;
import fr.utbm.set.jasim.environment.interfaces.body.Mesh;
import fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody1D5;
import fr.utbm.set.jasim.environment.interfaces.body.factory.AbstractAgentBodyFactory;
import fr.utbm.set.jasim.environment.interfaces.body.factory.AgentBodyFactory;
import fr.utbm.set.jasim.environment.interfaces.body.factory.BodyDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription;
import fr.utbm.set.jasim.environment.interfaces.body.frustums.Frustum;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment1D5;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction1D5;
import fr.utbm.set.jasim.environment.model.perception.frustum.FrontFrustum1D5;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGeneratorType;
import fr.utbm.set.jasim.environment.model.world.Entity1D5;
import fr.utbm.set.jasim.environment.model.world.MobileEntity1D5;
import fr.utbm.set.jasim.environment.model.world.WorldModelActuator;
import fr.utbm.set.jasim.environment.model.world.WorldModelContainer;
import fr.utbm.set.jasim.environment.model.world.WorldModelManager1D5;
import fr.utbm.set.jasim.environment.semantics.BodyType;
import fr.utbm.set.jasim.environment.semantics.Semantic;

/** This class representes the place in the world.
 * A place contains several objects (in the corresponding
 * perception tree), its own dynamics, its own ground, and
 * its own probe manager.
 * Each place could contains a set of portals to be connected to
 * other places.
 *
 * @param <SB> is the type of the bounds of the static entities.
 * @param <MB> is the type of the bounds of the mobile entities.
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class Place1D5<SB extends CombinableBounds1D5<RoadSegment>, MB extends CombinableBounds1D5<RoadSegment>>
extends AbstractPlace<EnvironmentalAction1D5,
                      Entity1D5<SB>, MobileEntity1D5<MB>>
implements BodyContainerEnvironment {

	private final WorldModelManager1D5<SB,MB> worldModel = new WorldModelManager1D5<SB,MB>();
	
	/** Real type of the mobile entity bounds.
	 */
	final Class<MB> mobileEntityBounds;

	/**
	 * @param environment is the environment in which this place is located.
	 * @param placeId is the identifier of the place.
	 * @param mobileEntityBounds is the type of the bounds for the mobile entities.
	 */
	public Place1D5(SituatedEnvironment1D5<SB,MB> environment, UUID placeId, Class<MB> mobileEntityBounds) {
		super(environment, placeId);
		this.mobileEntityBounds = mobileEntityBounds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PerceptionGeneratorType getPerceptionGeneratorType() {
		return PerceptionGeneratorType.LOCAL_SEQUENTIAL_BOTTOMUP;
	}

	/** Initialize this place.
	 * 
	 * @param roads are all the road network.
	 * @param staticEntitiesIterator is the structure containing the static entities
	 * @param mobileEntitiesIterator is the structure containing the mobile entities
	 */
	public void init(RoadNetwork roads,
					Iterator<? extends Entity1D5<SB>> staticEntitiesIterator, 
					Iterator<? extends MobileEntity1D5<MB>> mobileEntitiesIterator) {
		this.worldModel.init(
				roads,
				staticEntitiesIterator,
				mobileEntitiesIterator,
				this);
	}

	/** Destroy this place.
	 */
	public void destroy() {
		this.worldModel.destroy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WorldModelContainer<Entity1D5<SB>,MobileEntity1D5<MB>> getWorldModel() {
		return this.worldModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WorldModelActuator<EnvironmentalAction1D5,MobileEntity1D5<MB>> getWorldModelUpdater() {
		return this.worldModel;
	}

	/**
	 * Replies the world model manager used by this place.
	 * 
	 * @return the world model manager used by this place.
	 */
	public WorldModelManager1D5<SB,MB> getWorldModelManager1D5() {
		return this.worldModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgentBodyFactory getAgentBodyFactory(
			EuclidianPoint position, 
			EuclidianDirection orientation,
			BodyDescription body,
			Iterable<FrustumDescription> frustums) {
		if (orientation instanceof Direction1D) {
			if (position instanceof Point1D5) {
				return new BodyFactory((Point1D5)position, (Direction1D)orientation, body, frustums);
			}
			throw new IllegalArgumentException("unsupported type of point"); //$NON-NLS-1$
		}
		throw new IllegalArgumentException("unsupported type of direction"); //$NON-NLS-1$
	}

	/** Body factory implementation.
	 *
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid fr.utbm.set.sfc.jasim
	 * @mavenartifactid jasim-environment3d
	 */
	private class BodyFactory extends AbstractAgentBodyFactory {
		
		private final Point1D5 position;
		private final Direction1D orientation;
		
		/**
		 * @param position
		 * @param orientation
		 * @param body
		 * @param frustums
		 */
		public BodyFactory(
				Point1D5 position, 
				Direction1D orientation,
				BodyDescription body,
				Iterable<FrustumDescription> frustums) {
			super(body, frustums);
			this.position = position;
			this.orientation = orientation;
		}

		/**
		 * Replies a frustum identifier for the given body.
		 * @param body
		 * @return a frustum identifier for the given body.
		 */
		private UUID id(AgentBody1D5<?,?,?> body) {
			return UUID.randomUUID();
		}

		private FrontFrustum1D5 attachFrontFrustum(
				AgentBody1D5<?,?,?> body,
				double forwardDistance,
				double sideDistance) {
			FrontFrustum1D5 frustum = new FrontFrustum1D5(
					id(body),
					body.getPosition1D5(),
					body.getRoadEntry(),
					forwardDistance,
					sideDistance);
			body.addFrustums(frustum);
			return frustum;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Frustum<?, ?, ?> attachCircularFrustum(AgentBody<?, ?> body,
				double eyeVerticalPositionFromGround, double radius) {
			if (body instanceof AgentBody1D5<?,?,?>) {
				return attachFrontFrustum(
						(AgentBody1D5<?,?,?>)body,
						radius,
						radius);
			}
			throw new IllegalArgumentException("not an 3D agent body"); //$NON-NLS-1$
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Frustum<?, ?, ?> attachPedestrianFrustum(AgentBody<?, ?> body,
				double eyeVerticalPositionFromGround, double nearDistance,
				double farDistance, double horizontalOpennessAngle,
				double verticalOpennessAngle) {
			if (body instanceof AgentBody1D5<?,?,?>) {
				return attachFrontFrustum(
						(AgentBody1D5<?,?,?>)body,
						farDistance,
						Math.tan(horizontalOpennessAngle) * farDistance);
			}
			throw new IllegalArgumentException("not an 3D agent body"); //$NON-NLS-1$
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Frustum<?, ?, ?> attachRectangularFrustum(AgentBody<?, ?> body,
				double eyeVerticalPositionFromGround, double farDistance,
				double leftRightDistance, double upDownDistance) {
			if (body instanceof AgentBody1D5<?,?,?>) {
				return attachFrontFrustum(
						(AgentBody1D5<?,?,?>)body,
						farDistance,
						leftRightDistance);
			}
			throw new IllegalArgumentException("not an 3D agent body"); //$NON-NLS-1$
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Frustum<?, ?, ?> attachTriangularFrustum(AgentBody<?, ?> body,
				double eyeVerticalPositionFromGround, double nearDistance,
				double farDistance, double horizontalOpennessAngle,
				double verticalOpennessAngle) {
			if (body instanceof AgentBody1D5<?,?,?>) {
				return attachFrontFrustum(
						(AgentBody1D5<?,?,?>)body,
						farDistance,
						Math.tan(horizontalOpennessAngle) * farDistance);
			}
			throw new IllegalArgumentException("not an 3D agent body"); //$NON-NLS-1$
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void detachAllFrustums(AgentBody<?, ?> body) {
			if (body instanceof AgentBody1D5<?,?,?>) {
				AgentBody1D5<?,?,?> agBody1d5 = (AgentBody1D5<?,?,?>)body;
				agBody1d5.setFrustums();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createVehicleBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyLength, double bodyLateralSize, BodyType bodyType,
				double forwardSpeed, double backwardSpeed, double angularSpeed) {			
			return this.createVehicleBody(agent.getUUID(), perceptionType, bodyLength, bodyLateralSize, bodyType, forwardSpeed, backwardSpeed, angularSpeed);
		}
		
		private <I extends Influence, P extends Perception> AgentBody<I, P> createVehicleBody(UUID bodyID,
				Class<P> perceptionType,
				double bodyLength, double bodyLateralSize, BodyType bodyType,
				double forwardSpeed, double backwardSpeed, double angularSpeed) {
			RoadSegment road = (RoadSegment)this.position.getSegment();
			RoadConnection roadEntry = 
				(this.orientation.isSegmentDirection())
				? road.getBeginPoint()
				: road.getEndPoint();

			double demiLength = bodyLength/2.;
			double demiLat = bodyLateralSize/2.;
			Point2d lower = new Point2d(
					this.position.getCurvilineCoordinate() - demiLength,
					this.position.getJuttingDistance() - demiLat);
			Point2d upper = new Point2d(
					this.position.getCurvilineCoordinate() + demiLength,
					this.position.getJuttingDistance() + demiLat);
				
			AgentBody1D5<I,P,MB> body = new KinematicAgentBody1D5<I,P,MB>(
					bodyID,
					perceptionType,
					Place1D5.this.mobileEntityBounds,
					bodyType,
					road, roadEntry,
					forwardSpeed, backwardSpeed, angularSpeed,
					lower,
					upper);
			
			attachDefaultFrustums(body);
			
			return body;
		}

		/**
		 * {@inheritDoc}
		 * @deprecated
		 */
		@Deprecated
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				Collection<? extends Semantic> semantics, double forwardSpeed,
				double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			double s = bodyRadius * 2.;
			return createVehicleBody(
					agent, perceptionType, 
					s, s, 
					bodyType, 
					forwardSpeed, backwardSpeed, angularSpeed, 
					maxLinearAcceleration, maxLinearDeceleration, 
					maxAngularAcceleration, maxAngularDeceleration);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				Collection<? extends Semantic> semantics, double forwardSpeed,
				double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			double s = bodyRadius * 2.;
			return createVehicleBody(
					agent, perceptionType, 
					s, s, 
					bodyType, 
					forwardSpeed, forwardSpeed, angularSpeed, 
					maxLinearAcceleration, maxLinearDeceleration, 
					maxAngularAcceleration, maxAngularDeceleration);
		}

		/**
		 * {@inheritDoc}
		 * @deprecated
		 */
		@Deprecated
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double backwardSpeed, double angularSpeed) {
			double s = bodyRadius * 2.;
			return createVehicleBody(
					agent, perceptionType, 
					s, s, 
					bodyType,
					forwardSpeed, backwardSpeed, angularSpeed);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double angularSpeed) {
			double s = bodyRadius * 2.;
			return createVehicleBody(
					agent, perceptionType, 
					s, s, 
					bodyType,
					forwardSpeed, forwardSpeed, angularSpeed);
		}

		/**
		 * {@inheritDoc}
		 * @deprecated
		 */
		@Deprecated
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				UUID bodyID,
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double backwardSpeed, double angularSpeed) {
			double s = bodyRadius * 2.;
			return createVehicleBody(bodyID,
					perceptionType, 
					s, s, 
					bodyType,
					forwardSpeed, backwardSpeed, angularSpeed);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				UUID bodyID,
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double angularSpeed) {
			double s = bodyRadius * 2.;
			return createVehicleBody(bodyID,
					perceptionType, 
					s, s, 
					bodyType,
					forwardSpeed, forwardSpeed, angularSpeed);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createVehicleBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyLength, double bodyLateralSize, BodyType bodyType,
				Collection<? extends Semantic> semantics, double forwardSpeed,
				double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			RoadSegment road = (RoadSegment)this.position.getSegment();
			RoadConnection roadEntry = 
				(this.orientation.isSegmentDirection())
				? road.getBeginPoint()
				: road.getEndPoint();

			double demiLength = bodyLength/2.;
			double demiLat = bodyLateralSize/2.;
			Point2d lower = new Point2d(
					this.position.getCurvilineCoordinate() - demiLength,
					this.position.getJuttingDistance() - demiLat);
			Point2d upper = new Point2d(
					this.position.getCurvilineCoordinate() + demiLength,
					this.position.getJuttingDistance() + demiLat);
				
			SteeringAgentBody1D5<I,P,MB> body = new SteeringAgentBody1D5<I,P,MB>(
					agent.getUUID(),
					perceptionType,
					Place1D5.this.mobileEntityBounds,
					bodyType,
					road, roadEntry,
					forwardSpeed, backwardSpeed, angularSpeed,
					maxLinearAcceleration, maxLinearDeceleration,
					maxAngularAcceleration, maxAngularDeceleration,
					lower,
					upper);
			
			attachDefaultFrustums(body);
			
			return body;
		}

		/**
		 * {@inheritDoc}
		 * @deprecated
		 */
		@Deprecated
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType, double forwardSpeed,
				double backwardSpeed, double angularSpeed) {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType, double forwardSpeed,
				double angularSpeed) {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 * @deprecated
		 */
		@Deprecated
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType, double forwardSpeed,
				double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType, double forwardSpeed,
				double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			throw new UnsupportedOperationException();
		}

	} // class BodyFactory

}
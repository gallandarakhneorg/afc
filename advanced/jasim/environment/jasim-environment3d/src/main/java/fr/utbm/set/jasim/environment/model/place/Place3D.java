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
package fr.utbm.set.jasim.environment.model.place;

import java.util.Collection;
import java.util.UUID;

import javax.vecmath.Point3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.object.EuclidianDirection;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.jasim.JasimConstants;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.BodyContainerEnvironment;
import fr.utbm.set.jasim.environment.interfaces.body.KinematicAgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.Mesh;
import fr.utbm.set.jasim.environment.interfaces.body.SteeringAgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.factory.AbstractAgentBodyFactory;
import fr.utbm.set.jasim.environment.interfaces.body.factory.AgentBodyFactory;
import fr.utbm.set.jasim.environment.interfaces.body.factory.BodyDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription;
import fr.utbm.set.jasim.environment.interfaces.body.frustums.Frustum;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment3D;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction3D;
import fr.utbm.set.jasim.environment.model.perception.algorithm.PerceptionAlgorithm;
import fr.utbm.set.jasim.environment.model.perception.frustum.PedestrianFrustum3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.PyramidalFrustum;
import fr.utbm.set.jasim.environment.model.perception.frustum.RectangularFrustum3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.manipulators.TreeManipulator3D;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGeneratorType;
import fr.utbm.set.jasim.environment.model.world.BoxMesh3D;
import fr.utbm.set.jasim.environment.model.world.CylinderMesh3D;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.Mesh3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.jasim.environment.model.world.WorldModelActuator;
import fr.utbm.set.jasim.environment.model.world.WorldModelContainer;
import fr.utbm.set.jasim.environment.model.world.WorldModelManager3D;
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
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Place3D<SB extends CombinableBounds3D, MB extends CombinableBounds3D>
extends AbstractPlace<EnvironmentalAction3D,Entity3D<SB>,MobileEntity3D<MB>>
implements BodyContainerEnvironment {

	private final WorldModelManager3D<SB,MB> worldModel;
	
	/** Real type of the mobile entity bounds.
	 */
	final Class<MB> mobileEntityBounds;
	
	/**
	 * @param environment is the environment in which this place is located.
	 * @param placeId is the identifier of the place.
	 * @param mobileEntityBounds is the type of the bounds for the mobile entities.
	 * @param perceptionAlgorithm is the algorithm to use to compute agents perceptions.
	 */
	public Place3D(
			SituatedEnvironment3D<SB,MB> environment, 
			UUID placeId,
			Class<MB> mobileEntityBounds,
			PerceptionAlgorithm<SB,MB> perceptionAlgorithm) {
		super(environment, placeId);
		this.worldModel = new WorldModelManager3D<SB,MB>(perceptionAlgorithm);
		this.mobileEntityBounds = mobileEntityBounds;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PerceptionGeneratorType getPerceptionGeneratorType() {
		return this.worldModel.getPerceptionAlgorithm().getPerceptionGeneratorType();
	}

	/** Initialize this place.
	 * 
	 * @param dynamicTree is the tree which must contains the mobile entities.
	 * @param staticTree is the tree which must contains the static entities.
	 */
	public void init(
			DynamicPerceptionTree<CombinableBounds3D,MB,MobileEntity3D<MB>,?,?,?,? extends TreeManipulator3D<MB,?,?>> dynamicTree,
			StaticPerceptionTree<CombinableBounds3D,SB,Entity3D<SB>,?,?> staticTree) {
		this.worldModel.init(dynamicTree, staticTree, this);
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
	public WorldModelContainer<Entity3D<SB>,MobileEntity3D<MB>> getWorldModel() {
		return this.worldModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WorldModelActuator<EnvironmentalAction3D,MobileEntity3D<MB>> getWorldModelUpdater() {
		return this.worldModel;
	}

	/**
	 * Replies the world model manager used by this place.
	 * 
	 * @return the world model manager used by this place.
	 */
	public WorldModelManager3D<SB,MB> getWorldModelManager3D() {
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
		if (position instanceof EuclidianPoint3D) {
			if (orientation instanceof Direction2D)
				return new BodyFactory((EuclidianPoint3D)position, (Direction2D)orientation, body, frustums);
			throw new IllegalArgumentException("unsupported type of direction for spawner: "+orientation); //$NON-NLS-1$
		}
		throw new IllegalArgumentException("unsupported type of position for spawner: "+position); //$NON-NLS-1$
	}
	
	
	/** Body factory implementation.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class BodyFactory extends AbstractAgentBodyFactory {
		
		private final EuclidianPoint3D position;
		private final Direction2D orientation;
		
		/**
		 * @param position
		 * @param orientation
		 * @param body
		 * @param frustums
		 */
		public BodyFactory(
				EuclidianPoint3D position, 
				Direction2D orientation,
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
		private UUID id(AgentBody3D<?,?,?> body) {
			return UUID.randomUUID();
		}

		/**
		 * Replies the eye position for the given body.
		 * @param body
		 * @param eyeDistance
		 * @return the eye position for the given body.
		 */
		private Point3d eye(AgentBody3D<?,?,?> body, double eyeDistance) {
			CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
			Point3d ref = new Point3d(body.getPosition3D());
			double z;

			if (!body.isOnGround()) {
				Point3d p = body.getBounds().getLower();
				z = cs.height(p);
			}
			else {
				z = cs.height(ref);
			}

			cs.setHeight(ref, z+eyeDistance);
			
			return ref;
		}

		/** {@inheritDoc}
		 */
		@Override
		public void detachAllFrustums(AgentBody<?,?> body) {
			if (body instanceof AgentBody3D<?,?,?>) {
				AgentBody3D<?,?,?> agBody3d = (AgentBody3D<?,?,?>)body;
				agBody3d.setFrustums();
			}
		}

		/** {@inheritDoc}
		 */
		@Override
		public Frustum<?,?,?> attachCircularFrustum(AgentBody<?,?> body,
				double eyeVerticalPositionFromGround,
				double radius) {
			if (body instanceof AgentBody3D<?,?,?>) {
				AgentBody3D<?,?,?> agBody3d = (AgentBody3D<?,?,?>)body;
				SphericalFrustum frustum = new SphericalFrustum(
						id(agBody3d),
						eye(agBody3d, eyeVerticalPositionFromGround),
						radius);
				agBody3d.addFrustums(frustum);
				return frustum;
			}
			throw new IllegalArgumentException("not an 3D agent body"); //$NON-NLS-1$
		}

		/** {@inheritDoc}
		 */
		@Override
		public Frustum<?,?,?> attachPedestrianFrustum(AgentBody<?,?> body,
				double eyeVerticalPositionFromGround,
				double nearDistance, double farDistance,
				double horizontalOpennessAngle, double verticalOpennessAngle) {
			if (body instanceof AgentBody3D<?,?,?>) {
				AgentBody3D<?,?,?> agBody3d = (AgentBody3D<?,?,?>)body;
				PedestrianFrustum3D frustum = new PedestrianFrustum3D(
						id(agBody3d),
						eye(agBody3d, eyeVerticalPositionFromGround),
						nearDistance, agBody3d.getViewDirection3D(),
						farDistance, horizontalOpennessAngle,
						verticalOpennessAngle);
				agBody3d.addFrustums(frustum);
				return frustum;
			}
			throw new IllegalArgumentException("not an 3D agent body"); //$NON-NLS-1$
		}

		/** {@inheritDoc}
		 */
		@Override
		public Frustum<?,?,?> attachTriangularFrustum(AgentBody<?,?> body,
				double eyeVerticalPositionFromGround,
				double nearDistance, double farDistance,
				double horizontalOpennessAngle, double verticalOpennessAngle) {
			if (body instanceof AgentBody3D<?,?,?>) {
				AgentBody3D<?,?,?> agBody3d = (AgentBody3D<?,?,?>)body;
				PyramidalFrustum frustum = new PyramidalFrustum(
						id(agBody3d),
						eye(agBody3d, eyeVerticalPositionFromGround),
						agBody3d.getViewDirection3D(),
						nearDistance, farDistance,
						horizontalOpennessAngle, verticalOpennessAngle);
				agBody3d.addFrustums(frustum);
				return frustum;
			}
			throw new IllegalArgumentException("not an 3D agent body"); //$NON-NLS-1$
		}

		/** {@inheritDoc}
		 */
		@Override
		public Frustum<?,?,?> attachRectangularFrustum(AgentBody<?,?> body, 
				double eyeVerticalPositionFromGround,
				double farDistance, double leftRightDistance, double upDownDistance) {
			if (body instanceof AgentBody3D<?,?,?>) {
				AgentBody3D<?,?,?> agBody3d = (AgentBody3D<?,?,?>)body;
				RectangularFrustum3D frustum = new RectangularFrustum3D(
						id(agBody3d),
						eye(agBody3d, eyeVerticalPositionFromGround),
						agBody3d.getViewDirection3D(),
						leftRightDistance, upDownDistance, farDistance);
				agBody3d.addFrustums(frustum);
				return frustum;
			}
			throw new IllegalArgumentException("not an 3D agent body"); //$NON-NLS-1$
		}

		/**
		 * {@inheritDoc}
		 * @deprecated
		 */
		@Deprecated
		@Override
		public final <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius,
				BodyType bodyType, Collection<? extends Semantic> semantics,
				double forwardSpeed, double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			return createSteeringBody(
						agent, 
						perceptionType, 
						new CylinderMesh3D(bodyRadius, bodyHeight, true), 
						bodyType, 
						forwardSpeed, angularSpeed, 
						maxLinearAcceleration, maxLinearDeceleration, 
						maxAngularAcceleration, maxAngularDeceleration);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius,
				BodyType bodyType, Collection<? extends Semantic> semantics,
				double forwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			return createSteeringBody(
						agent, 
						perceptionType, 
						new CylinderMesh3D(bodyRadius, bodyHeight, true), 
						bodyType, 
						forwardSpeed, angularSpeed, 
						maxLinearAcceleration, maxLinearDeceleration, 
						maxAngularAcceleration, maxAngularDeceleration);
		}

		/**
		 * {@inheritDoc}
		 * @deprecated
		 */
		@Deprecated
		@Override
		public final <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double backwardSpeed, double angularSpeed) {
			return createNonSteeringBody(
					agent, 
					perceptionType, 
					new CylinderMesh3D(bodyRadius, bodyHeight, true), 
					bodyType,
					forwardSpeed, angularSpeed);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public final <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double angularSpeed) {
			return createNonSteeringBody(
					agent, 
					perceptionType, 
					new CylinderMesh3D(bodyRadius, bodyHeight, true), 
					bodyType,
					forwardSpeed, angularSpeed);
		}

		/**
		 * {@inheritDoc}
		 * @deprecated
		 */
		@Deprecated
		@Override
		public final <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				UUID bodyID,
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double backwardSpeed, double angularSpeed) {
			return createNonSteeringBody(
					bodyID,
					agent, 
					perceptionType, 
					new CylinderMesh3D(bodyRadius, bodyHeight, true), 
					bodyType,
					forwardSpeed, angularSpeed);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				UUID bodyID,
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double angularSpeed) {
			return createNonSteeringBody(
					bodyID,
					agent, 
					perceptionType, 
					new CylinderMesh3D(bodyRadius, bodyHeight, true), 
					bodyType,
					forwardSpeed, angularSpeed);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final <I extends Influence, P extends Perception> AgentBody<I, P> createVehicleBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyLength, double bodyLateralSize,
				BodyType bodyType, Collection<? extends Semantic> semantics,
				double forwardSpeed, double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			return createSteeringBody(
					agent, 
					perceptionType, 
					new BoxMesh3D(bodyLength, bodyLateralSize, JasimConstants.DEFAULT_VEHICLE_HEIGHT, true), 
					bodyType, 
					forwardSpeed, angularSpeed, 
					maxLinearAcceleration, maxLinearDeceleration, 
					maxAngularAcceleration, maxAngularDeceleration);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final <I extends Influence, P extends Perception> AgentBody<I,P> createVehicleBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyLength, double bodyLateralSize, BodyType bodyType,
				double forwardSpeed, double backwardSpeed, double angularSpeed) {
			return createNonSteeringBody(
					agent, 
					perceptionType, 
					new BoxMesh3D(bodyLength, bodyLateralSize, 
							JasimConstants.DEFAULT_VEHICLE_HEIGHT, true), 
					bodyType,
					forwardSpeed, angularSpeed);
		}
		
		/** Create a steering body.
		 * 
		 * @param <I> is the type of the supported influence.
		 * @param <P> is the type of the supported perception.
		 * @param agent is the agent for which the body must be spawned.
		 * @param perceptionType is the type of the supported perceptions.
		 * @param mesh is the local mesh of the body.
		 * @param bodyType is the type of the body.
		 * @param forwardSpeed is the maximal forward speed.
		 * @param angularSpeed is the maximal rotation speed.
		 * @param maxLinearAcceleration is the maximal linear acceleration.
		 * @param maxLinearDeceleration is the maximal linear deceleration.
		 * @param maxAngularAcceleration is the maximal angular acceleration.
		 * @param maxAngularDeceleration is the maximal angular deceleration.
		 * @return a new instance of body to insert into the simulation, or <code>null</code>
		 */
		protected <I extends Influence, P extends Perception> AgentBody3D<I,P,MB>
		createSteeringBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh3D mesh, BodyType bodyType,
				double forwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			assert(mesh.isLocalMesh());
			MB bounds = mesh.toBounds(Place3D.this.mobileEntityBounds);
			AgentBody3D<I,P,MB> body = new SteeringAgentBody3D<I,P,MB>(
				agent.getUUID(),
				perceptionType,
				bounds,
				bodyType,
				mesh,
				forwardSpeed, angularSpeed,
				maxLinearAcceleration, maxLinearDeceleration,
				maxAngularAcceleration, maxAngularDeceleration);
			finalizeCreation(body);
			return body;
		}

		/** Create a non steering body.
		 * 
		 * @param <I> is the type of the supported influence.
		 * @param <P> is the type of the supported perception.
		 * @param agent is the agent for which the body must be spawned.
		 * @param perceptionType is the type of the supported perceptions.
		 * @param mesh is the local mesh of the body.
		 * @param bodyType is the type of the body.
		 * @param forwardSpeed is the maximal forward speed.
		 * @param angularSpeed is the maximal rotation speed.
		 * @return a new instance of body to insert into the simulation, or <code>null</code>
		 */
		protected <I extends Influence, P extends Perception> AgentBody3D<I,P,MB>
		createNonSteeringBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh3D mesh, BodyType bodyType,
				double forwardSpeed, double angularSpeed) {
			
			return this.createNonSteeringBody(agent.getUUID(), agent, perceptionType, mesh, bodyType, forwardSpeed, angularSpeed);
		}
		
		/** Create a non steering body.
		 * 
		 * @param <I> is the type of the supported influence.
		 * @param <P> is the type of the supported perception.
		 * @param bodyID is the identifier of the body.
		 * @param agent is the agent for which the body must be spawned.
		 * @param perceptionType is the type of the supported perceptions.
		 * @param mesh is the local mesh of the body.
		 * @param bodyType is the type of the body.
		 * @param forwardSpeed is the maximal forward speed.
		 * @param angularSpeed is the maximal rotation speed.
		 * @return a new instance of body to insert into the simulation, or <code>null</code>
		 */
		protected <I extends Influence, P extends Perception> AgentBody3D<I,P,MB>
		createNonSteeringBody(UUID bodyID,
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh3D mesh, BodyType bodyType,
				double forwardSpeed, double angularSpeed) {
			assert(mesh.isLocalMesh());
			MB bounds = mesh.toBounds(Place3D.this.mobileEntityBounds);
			AgentBody3D<I,P,MB> body = new KinematicAgentBody3D<I,P,MB>(
					bodyID,
				perceptionType,
				bounds,
				bodyType,
				mesh,
				forwardSpeed, angularSpeed);
			finalizeCreation(body);
			return body;
		}

		private <I extends Influence, P extends Perception> void finalizeCreation(AgentBody3D<I,P,MB> body) {
			Direction3D aa = GeometryUtil.viewVector2rotation2D(
					new Direction2D(this.orientation.x,this.orientation.y),
					CoordinateSystem3D.getDefaultCoordinateSystem());
			body.setRotation(aa);
			body.translate(this.position.getX(), this.position.getY(), this.position.getZ());
			attachDefaultFrustums(body);
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
			if (mesh instanceof Mesh3D) {
				return createNonSteeringBody(
						agent, 
						perceptionType, 
						(Mesh3D)mesh, 
						bodyType,
						forwardSpeed, angularSpeed);
			}
			throw new IllegalArgumentException("mesh is not a Mesh3D"); //$NON-NLS-1$
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType, double forwardSpeed,
				double angularSpeed) {
			if (mesh instanceof Mesh3D) {
				return createNonSteeringBody(
						agent, 
						perceptionType, 
						(Mesh3D)mesh, 
						bodyType,
						forwardSpeed, angularSpeed);
			}
			throw new IllegalArgumentException("mesh is not a Mesh3D"); //$NON-NLS-1$
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
			if (mesh instanceof Mesh3D) {
				return createSteeringBody(
						agent, 
						perceptionType, 
						(Mesh3D)mesh, 
						bodyType, 
						forwardSpeed, angularSpeed, 
						maxLinearAcceleration, maxLinearDeceleration, 
						maxAngularAcceleration, maxAngularDeceleration);
			}
			throw new IllegalArgumentException("mesh is not a Mesh3D"); //$NON-NLS-1$
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
			if (mesh instanceof Mesh3D) {
				return createSteeringBody(
						agent, 
						perceptionType, 
						(Mesh3D)mesh, 
						bodyType, 
						forwardSpeed, angularSpeed, 
						maxLinearAcceleration, maxLinearDeceleration, 
						maxAngularAcceleration, maxAngularDeceleration);
			}
			throw new IllegalArgumentException("mesh is not a Mesh3D"); //$NON-NLS-1$
		}

	} // class BodyFactory
	
}
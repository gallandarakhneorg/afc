/*
 * 
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
package fr.utbm.set.jasim.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;

import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;

import fr.utbm.set.jasim.JasimConstants;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.agent.hotpoint.GoalPoint;
import fr.utbm.set.jasim.agent.hotpoint.WayPoint;
import fr.utbm.set.jasim.agent.hotpoint.hotpoint2d.GoalPoint2D;
import fr.utbm.set.jasim.agent.hotpoint.hotpoint2d.WayPoint2D;
import fr.utbm.set.jasim.io.sfg.EnvironmentSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.GoalSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.PositionParameter;
import fr.utbm.set.jasim.io.sfg.SpawnerSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.WayPointSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.XMLSimulationConfigParser3D;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment3D;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction3D;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.BoundCenterPartitionPolicy;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree.StaticIcosepBspTreeBuilder3D;
import fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree.DynamicIcosepQuadTreeBuilder3D;
import fr.utbm.set.jasim.environment.model.place.DefaultPlaceDescription;
import fr.utbm.set.jasim.environment.model.place.PlaceDescription;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.environment.time.TimeManager;
import fr.utbm.set.jasim.io.sfg.XMLSimulationConfigParser;
import fr.utbm.set.jasim.spawn.SpawnLocation;
import fr.utbm.set.jasim.spawn.spawnlocation3d.SpawnArea3D;
import fr.utbm.set.jasim.spawn.spawnlocation3d.SpawnPoint3D;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;

/**
 * A simulation controller is an object which permits to launch, stop a simulation.
 * It is also able to dynamically add and remove entities from the simulation.
 * The SimulationController is the entry point and the bottleneck in which
 * all the actions of the simulation's user must pass. 
 *
 * @param <SB> is the type of the bounds for static entities.
 * @param <MB> is the type of the bounds for mobile entities.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SimulationController3D<SB extends CombinableBounds3D, MB extends CombinableBounds3D>
extends AbstractSimulationController<EnvironmentalAction3D,
									 Bounds3D,
									 SB,
									 Entity3D<SB>,
									 MB,
									 MobileEntity3D<MB>,
									 SituatedEnvironment3D<SB,MB>,
									 Point3d> {

	/**
	 * Creates a new simulation controller
	 * 
	 * @param binder is the object which is permit to bind the simulation controller to an agent platform.
	 * @param boundType is the type of the bounds for mobile entities.
	 */
	public SimulationController3D(ControllableAgentPlatform binder, Class<MB> boundType) {
		this(binder, null, boundType);
	}

	/**
	 * Creates a new simulation controller
	 * 
	 * @param binder is the object which is permit to bind the simulation controller to an agent platform.
	 * @param timeManager is the time manager
	 * @param boundType is the type of the bounds for mobile entities.
	 */
	public SimulationController3D(ControllableAgentPlatform binder, TimeManager timeManager, Class<MB> boundType) {
		this(binder, new SituatedEnvironment3D<SB,MB>(boundType), timeManager);
	}

	/**
	 * Creates a new simulation controller
	 * 
	 * @param binder is the object which is permit to bind the simulation controller to an agent platform.
	 * @param environment is the environment
	 * @param timeManager is the time manager
	 */
	public SimulationController3D(ControllableAgentPlatform binder, SituatedEnvironment3D<SB,MB> environment, TimeManager timeManager) {
		super(binder, environment, timeManager);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected GoalPoint createGoalPoint(GoalSimulationParameterSet description) {
		PositionParameter position = description.getPosition();
		Vector2d tangent = description.getTangent2D();
		double time = description.getTime();
		if (Double.isNaN(time)) {
			if (tangent!=null) {
				return new GoalPoint2D(
						position.getX(), position.getY(),
						tangent.x,tangent.y);
			}
			return new GoalPoint2D(position.getX(), position.getY());
		}

		if (tangent!=null) {
			return new GoalPoint2D(
					position.getX(), position.getY(),
					tangent.x,tangent.y, time);
		}
		return new GoalPoint2D(position.getX(), position.getY(), time);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected SpawnLocation createSpawner(SpawnerSimulationParameterSet description) throws IOException {
		PositionParameter position = description.getPosition();
		switch(description.getType()) {
		case AREA:
			Vector2d dimension = description.getDimension2D();
			return new SpawnArea3D(
					description.getIdentifier(),
					description.getName(),
					position.getPosition2D(),
					dimension.x, dimension.y,
					description.getStartAngle(),
					description.getEndAngle(),
					description.getStartDate(),
					description.getEndDate());
		case POINT:
			return new SpawnPoint3D(
					description.getIdentifier(),
					description.getName(),
					position.getPosition2D(),
					description.getStartAngle(),
					description.getEndAngle(),
					description.getStartDate(),
					description.getEndDate());
		default:
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected WayPoint createWayPoint(WayPointSimulationParameterSet description) {
		PositionParameter position = description.getPosition();
		Vector2d tangent = description.getTangent2D();
		double time = description.getTime();
		
		if (Double.isNaN(time)) {
			if (tangent!=null) {
				return new WayPoint2D(
						position.getX(), position.getY(),
						tangent.x,tangent.y,
						description.getVelocity());
			}
			return new WayPoint2D(
					position.getX(), position.getY(),
					description.getVelocity());
		}

		if (tangent!=null) {
			return new WayPoint2D(
					position.getX(), position.getY(),
					tangent.x,tangent.y,
					description.getVelocity(), time);
		}
		return new WayPoint2D(
				position.getX(), position.getY(),
				description.getVelocity(), time);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initializeAgentBinding(
			Class<? extends SituatedAgent<?, ?, ?>> defaultType,
			Map<Class<?>, Class<? extends SituatedAgent<?, ?, ?>>> agentMap,
			Map<Class<? extends SituatedAgent<?, ?, ?>>, Collection<Class<? extends Semantic>>> semanticMap,
			PlaceDescription<Entity3D<SB>, MobileEntity3D<MB>> placeDescription,
			SituatedEnvironment3D<SB, MB> environmentInstance)
			throws IOException {
		bindAgentsNow(
				defaultType,
				agentMap,
				semanticMap,
				placeDescription.getMobileEntities());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initializePlaceDescription(URL prebuiltStatic,
			URL prebuiltDynamic,
			EnvironmentSimulationParameterSet environmentDescrption,
			PlaceDescription<Entity3D<SB>, MobileEntity3D<MB>> placeDescription) {

		if (placeDescription instanceof DefaultPlaceDescription) {
			DefaultPlaceDescription<Entity3D<SB>,MobileEntity3D<MB>> placeDesc = (DefaultPlaceDescription<Entity3D<SB>,MobileEntity3D<MB>>)placeDescription;
			
			//
			// Dynamic tree initialization
			//
			DynamicPerceptionTree<CombinableBounds3D,MB,MobileEntity3D<MB>,?,?,?,?> dynamicTree = null;
			
			if (prebuiltDynamic!=null) {
				dynamicTree = deserialize(prebuiltDynamic, DynamicPerceptionTree.class);
			}
			
			if (dynamicTree==null) {
				DynamicIcosepQuadTreeBuilder3D<MB> builder = new DynamicIcosepQuadTreeBuilder3D<MB>(
						JasimConstants.DEFAULT_TREE_SPLIT_COUNT,
						BoundCenterPartitionPolicy.SINGLETON);
				
				Ground ground = placeDesc.getGround();
				AlignedBoundingBox worldBounds = null;
				if (ground!=null) {
					worldBounds = new AlignedBoundingBox();
					worldBounds.setLower(ground.getMinX(), ground.getMinY(), ground.getMinZ());
					worldBounds.setUpper(ground.getMaxX(), ground.getMaxY(), ground.getMaxZ());
				}
				
				try {
					dynamicTree = builder.buildTree(placeDesc.getMobileEntities(), worldBounds);
				}
				catch (Exception e) {
					getLogger().log(Level.SEVERE,e.getMessage(), e);
				}
			}
			
			if (dynamicTree!=null) {
				placeDesc.setWorldModel(DynamicPerceptionTree.class, dynamicTree);
				placeDesc.setMobileEntities(DynamicPerceptionTree.class);
			}

			//
			// Static tree initialization
			//
			StaticPerceptionTree<CombinableBounds3D,SB,Entity3D<SB>,?,?> staticTree = null;
			
			if (prebuiltStatic!=null) {
				staticTree = deserialize(prebuiltStatic, StaticPerceptionTree.class);
			}
			
			if (staticTree==null) {
				StaticIcosepBspTreeBuilder3D<SB> builder = new StaticIcosepBspTreeBuilder3D<SB>(
						JasimConstants.DEFAULT_TREE_SPLIT_COUNT,
						BoundCenterPartitionPolicy.SINGLETON);
	
				Ground ground = placeDesc.getGround();
				AlignedBoundingBox worldBounds = null;
				if (ground!=null) {
					worldBounds = new AlignedBoundingBox();
					worldBounds.setLower(ground.getMinX(), ground.getMinY(), ground.getMinZ());
					worldBounds.setUpper(ground.getMaxX(), ground.getMaxY(), ground.getMaxZ());
				}

				try {
					staticTree = builder.buildTree(placeDesc.getStaticEntities(), worldBounds);
				}
				catch (Exception e) {
					getLogger().log(Level.SEVERE,e.getMessage(), e);
				}
			}
			
			if (staticTree!=null) {
				placeDesc.setWorldModel(StaticPerceptionTree.class, staticTree);
				placeDesc.setStaticEntities(StaticPerceptionTree.class);
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected XMLSimulationConfigParser createSFGParser(InputStream xmlFileContent) {
		return new XMLSimulationConfigParser3D(xmlFileContent);
	}

}

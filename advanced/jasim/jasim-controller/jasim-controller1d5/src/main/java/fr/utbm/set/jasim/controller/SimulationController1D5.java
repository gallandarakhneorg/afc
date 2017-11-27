/*
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.vecmath.Vector2d;

import fr.utbm.set.geom.bounds.bounds1d5.Bounds1D5;
import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;
import fr.utbm.set.geom.bounds.bounds2d.MinimumBoundingRectangle;
import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.gis.io.shape.GISShapeFileReader;
import fr.utbm.set.gis.location.GeoId;
import fr.utbm.set.gis.mapelement.MapElement;
import fr.utbm.set.gis.road.RoadPolyline;
import fr.utbm.set.gis.road.StandardRoadNetwork;
import fr.utbm.set.gis.road.primitive.RoadNetwork;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.io.dbase.DBaseFileReader;
import fr.utbm.set.io.shape.ESRIBounds;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.agent.hotpoint.GoalPoint;
import fr.utbm.set.jasim.agent.hotpoint.WayPoint;
import fr.utbm.set.jasim.agent.hotpoint.hotpoint1d5.GoalPoint1D5;
import fr.utbm.set.jasim.agent.hotpoint.hotpoint1d5.WayPoint1D5;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment1D5;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction1D5;
import fr.utbm.set.jasim.environment.model.place.DefaultPlaceDescription;
import fr.utbm.set.jasim.environment.model.place.Place1D5;
import fr.utbm.set.jasim.environment.model.place.PlaceDescription;
import fr.utbm.set.jasim.environment.model.world.Entity1D5;
import fr.utbm.set.jasim.environment.model.world.MobileEntity1D5;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.environment.time.TimeManager;
import fr.utbm.set.jasim.io.sfg.EnvironmentSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.GoalSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.PositionParameter;
import fr.utbm.set.jasim.io.sfg.SpawnerSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.WayPointSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.XMLSimulationConfigParser;
import fr.utbm.set.jasim.io.sfg.XMLSimulationConfigParser1D5;
import fr.utbm.set.jasim.spawn.SpawnLocation;
import fr.utbm.set.jasim.spawn.spawnlocation1d5.SpawnArea1D5;
import fr.utbm.set.jasim.spawn.spawnlocation1d5.SpawnPoint1D5;
import fr.utbm.set.util.error.ErrorLogger;

/**
 * A simulation controller is an object which permits to launch, stop a simulation.
 * It is also able to dynamically add and remove entities from the simulation.
 * The SimulationController is the entry point and the bottleneck in which
 * all the actions of the simulation's user must pass. 
 *
 * @param <SB> is the type of the bounds for static entities.
 * @param <MB> is the type of the bounds for mobile entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SimulationController1D5<SB extends CombinableBounds1D5<RoadSegment>,
									 MB extends CombinableBounds1D5<RoadSegment>>
extends AbstractSimulationController<EnvironmentalAction1D5,
									 Bounds1D5<RoadSegment>,
									 SB,
									 Entity1D5<SB>,
									 MB,
									 MobileEntity1D5<MB>,
									 SituatedEnvironment1D5<SB,MB>,
									 Point1D5> {

	private RoadNetwork initialRoadNetwork = null;
	
	/**
	 * Creates a new simulation controller
	 * 
	 * @param binder is the object which is permit to bind the simulation controller to an agent platform.
	 * @param boundType is the type of the bounds for mobile entities.
	 */
	public SimulationController1D5(ControllableAgentPlatform binder, Class<MB> boundType) {
		this(binder, null, boundType);
	}

	/**
	 * Creates a new simulation controller
	 * 
	 * @param binder is the object which is permit to bind the simulation controller to an agent platform.
	 * @param timeManager is the time manager
	 * @param boundType is the type of the bounds for mobile entities.
	 */
	public SimulationController1D5(ControllableAgentPlatform binder, TimeManager timeManager, Class<MB> boundType) {
		this(binder, new SituatedEnvironment1D5<SB,MB>(boundType), timeManager);
	}

	/**
	 * Creates a new simulation controller
	 * 
	 * @param binder is the object which is permit to bind the simulation controller to an agent platform.
	 * @param environment is the environment
	 * @param timeManager is the time manager
	 */
	public SimulationController1D5(ControllableAgentPlatform binder, SituatedEnvironment1D5<SB,MB> environment, TimeManager timeManager) {
		super(binder, environment, timeManager);
	}
	
	/** Force this controller to use the given road network insteed of
	 * loading a new road network given by the XML configuration file.
	 * The given road network will be passed to all the places.
	 * 
	 * @param network
	 */
	public void forceInitialRoadNetwork(RoadNetwork network) {
		this.initialRoadNetwork = network;
	}
	
	/** Replies the 1.5D position from the given position parameter.
	 * 
	 * @param position
	 * @return the 1.5D position, never <code>null</code>
	 */
	private Point1D5 toPoint1D5(PositionParameter position) {
		String roadId = position.getRoad();
		assert(roadId!=null);
		GeoId geoId = GeoId.valueOf(roadId);
		assert(geoId!=null);
		UUID placeId = position.getPlace();
		assert(placeId!=null);
		Place1D5<?,?> place = getEnvironment().getPlaceObject(placeId);
		assert(place!=null);
		RoadNetwork network = place.getWorldModel().getInnerWorldModel(RoadNetwork.class);
		assert(network!=null);
		RoadSegment segment = network.getRoadSegment(geoId);
		assert(segment!=null);
		return position.getPosition1D5(segment, 0.);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected GoalPoint createGoalPoint(GoalSimulationParameterSet description) {
		Direction1D tangent = description.getTangent1D();
		Point1D5 position = toPoint1D5(description.getPosition());
		double time = description.getTime();
		
		if (Double.isNaN(time)) {
			if (tangent!=null) {
				return new GoalPoint1D5(
						position,
						tangent);
			}
			return new GoalPoint1D5(
					position,
					Direction1D.BOTH_DIRECTIONS);
		}

		if (tangent!=null) {
			return new GoalPoint1D5(
					position,
					tangent,
					time);
		}
		return new GoalPoint1D5(
				position,
				Direction1D.BOTH_DIRECTIONS,
				time);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected SpawnLocation createSpawner(SpawnerSimulationParameterSet description) throws IOException {
		Direction1D tangent = description.getDirection1D();
		Point1D5 position = toPoint1D5(description.getPosition());
		switch(description.getType()) {
		case AREA:
			Vector2d dimension = description.getDimension1D5();
			return new SpawnArea1D5(
					description.getIdentifier(),
					description.getName(),
					position,
					dimension.x,
					dimension.y,
					tangent,
					description.getStartDate(),
					description.getEndDate());
		case POINT:
			return new SpawnPoint1D5(
					description.getIdentifier(),
					description.getName(),
					position,
					tangent,
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
		Direction1D tangent = description.getTangent1D();
		Point1D5 position = toPoint1D5(description.getPosition());
		double time = description.getTime();
		
		if (Double.isNaN(time)) {
			if (tangent!=null) {
				return new WayPoint1D5(
						position,
						tangent,
						description.getVelocity());
			}
			return new WayPoint1D5(
					position,
					tangent,
					description.getVelocity());
		}

		if (tangent!=null) {
			return new WayPoint1D5(
					position,
					tangent,
					description.getVelocity(),
					time);
		}
		return new WayPoint1D5(
				position,
				tangent,
				description.getVelocity(),
				time);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initializeAgentBinding(
			Class<? extends SituatedAgent<?, ?, ?>> defaultType,
			Map<Class<?>, Class<? extends SituatedAgent<?, ?, ?>>> agentMap,
			Map<Class<? extends SituatedAgent<?, ?, ?>>, Collection<Class<? extends Semantic>>> semanticMap,
			PlaceDescription<Entity1D5<SB>, MobileEntity1D5<MB>> placeDescription,
			SituatedEnvironment1D5<SB, MB> environmentInstance)
			throws IOException {
		bindAgentsNow(
				defaultType,
				agentMap,
				semanticMap,
				placeDescription.getMobileEntities());
	}

	@Override
	protected void initializePlaceDescription(URL prebuiltStatic,
			URL prebuiltDynamic,
			EnvironmentSimulationParameterSet environmentDescription,
			PlaceDescription<Entity1D5<SB>, MobileEntity1D5<MB>> placeDescription) {

		if (placeDescription instanceof DefaultPlaceDescription<?,?>) {
			DefaultPlaceDescription<Entity1D5<SB>,MobileEntity1D5<MB>> placeDesc = (DefaultPlaceDescription<Entity1D5<SB>,MobileEntity1D5<MB>>)placeDescription;
			
			RoadNetwork roadNetwork = this.initialRoadNetwork;

			if (roadNetwork==null) {
				//
				// Road network initialization
				//
				URL roadNetworkShapeUrl = environmentDescription.getGroundRoadShapeResource();
				URL roadNetworkAttrUrl = environmentDescription.getGroundRoadAttributeResource();
				if (roadNetworkShapeUrl==null)
					throw new RuntimeException("no given road network URL"); //$NON-NLS-1$
				
				try {
					roadNetwork = loadRoadNetwork(roadNetworkShapeUrl, roadNetworkAttrUrl);
				}
				catch(IOException e) {
					throw new RuntimeException(e);
				}
			}

			if (roadNetwork!=null) {
				Iterator<? extends Entity1D5<SB>> sIterator = placeDesc.getStaticEntities();
				if (sIterator.hasNext()) {
					getLogger().warning("unable to put the static entities in the road network model"); //$NON-NLS-1$
				}
				
				Iterator<? extends MobileEntity1D5<MB>> mIterator = placeDesc.getMobileEntities();
				if (mIterator.hasNext()) {
					getLogger().warning("unable to put the mobile entities in the road network model"); //$NON-NLS-1$
				}
				
				placeDesc.setWorldModel(RoadNetwork.class, roadNetwork);			
			}
			else {
				throw new RuntimeException("no road network loaded"); //$NON-NLS-1$
			}
		}

	}
	
	private static RoadNetwork loadRoadNetwork(URL shape, URL dbase) throws IOException {
		GISShapeFileReader shapeReader;
		if (dbase!=null) {
			DBaseFileReader dbfReader = new DBaseFileReader(dbase.openStream());
			shapeReader = new GISShapeFileReader(
					shape.openStream(),
					RoadPolyline.class,
					dbfReader,
					dbase);
		}
		else {
			shapeReader = new GISShapeFileReader(
					shape.openStream(),
					RoadPolyline.class);
		}
		
		ESRIBounds r = shapeReader.getBoundsFromHeader();
		RoadNetwork network = new StandardRoadNetwork(
				new MinimumBoundingRectangle(
						r.getMinX(), r.getMinY(),
						r.getMaxX(), r.getMaxY()));

		Iterator<MapElement> iterator = shapeReader.iterator();
		MapElement elt;
		while (iterator.hasNext()) {
			elt = iterator.next();
			if (elt instanceof RoadPolyline) {
				try {
					network.addRoadSegment((RoadPolyline)elt);
				}
				catch(Throwable e) {
					ErrorLogger.logWarning(network, e);
				}
			}
		}

		return network;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected XMLSimulationConfigParser createSFGParser(InputStream xmlFileContent) {
		return new XMLSimulationConfigParser1D5(xmlFileContent);
	}

}

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
package fr.utbm.set.jasim.environment.model;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.system.CoordinateSystem2D;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.system.CoordinateSystemConstants;
import fr.utbm.set.gis.road.primitive.RoadNetwork;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.model.actions.EnvironmentalActionCollector;
import fr.utbm.set.jasim.environment.model.dynamics.DynamicsEngine;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.influencereaction.DefaultEnvironmentalActionCollector;
import fr.utbm.set.jasim.environment.model.influencereaction.DefaultInfluenceCollector;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction1D5;
import fr.utbm.set.jasim.environment.model.influencereaction.ImmediateInfluenceApplier1D5;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.influences.InfluenceSolver;
import fr.utbm.set.jasim.environment.model.place.DefaultPortal;
import fr.utbm.set.jasim.environment.model.place.Place1D5;
import fr.utbm.set.jasim.environment.model.place.PlaceDescription;
import fr.utbm.set.jasim.environment.model.place.PortalDescription;
import fr.utbm.set.jasim.environment.model.world.Entity1D5;
import fr.utbm.set.jasim.environment.model.world.MobileEntity1D5;

/**
 * The default implementation of a situated environment for a MABS using
 * the Influence-Reaction model to manage environmental dynamic and agent's actions/perceptions.
 * 
 * @param <SB> is the type of the bounds of the static entities.
 * @param <MB> is the type of the bounds of the mobile entities.
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class SituatedEnvironment1D5<SB extends CombinableBounds1D5<RoadSegment>, MB extends CombinableBounds1D5<RoadSegment>>
extends AbstractSituatedEnvironment<EnvironmentalAction1D5,
									Entity1D5<SB>,
									MobileEntity1D5<MB>,
									Place1D5<SB,MB>,
									Point1D5> {

	private final Class<MB> mobileEntityBoundType;
	
	static {
		// Ensure that the default coordinate systems are
		// mapped to the GIS coorinate system.
		CoordinateSystem2D.setDefaultCoordinateSystem(
				CoordinateSystemConstants.GIS_2D);
		CoordinateSystem3D.setDefaultCoordinateSystem(
				CoordinateSystemConstants.GIS_3D);
	}
	
	/**
	 * @param boundType is the type of the bounds for mobile entities.
	 */
	public SituatedEnvironment1D5(Class<MB> boundType) {
		this.mobileEntityBoundType = boundType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Place1D5<SB,MB> createPlace(
			PlaceDescription<Entity1D5<SB>, MobileEntity1D5<MB>> description) {
		Place1D5<SB,MB> place = new Place1D5<SB,MB>(this,description.getIdentifier(), this.mobileEntityBoundType);
		place.setName(description.getName());
		place.setGround(description.getGround());
		place.setPerceptionGenerator(place.getWorldModelManager1D5());

		try {

			Class<? extends DynamicsEngine> deType = description.getDynamicsEngineType();
			if (deType!=null) place.setDynamicsEngine(deType.newInstance());

			InfluenceSolver<EnvironmentalAction1D5,MobileEntity1D5<MB>> influenceSolver = null;
			InfluenceCollector influenceCollector = null;
			EnvironmentalActionCollector<EnvironmentalAction1D5> actionCollector = null;
			
			Class<? extends InfluenceSolver<?,MobileEntity1D5<MB>>> isType = description.getInfluenceSolverType();
			if (isType==null) {
				influenceSolver = new ImmediateInfluenceApplier1D5<MobileEntity1D5<MB>>();
			}
			else {
				influenceSolver = (InfluenceSolver<EnvironmentalAction1D5,MobileEntity1D5<MB>>)isType.newInstance();
			}
			
			if (influenceSolver instanceof InfluenceCollector) {
				influenceCollector = (InfluenceCollector)influenceSolver;
			}
			else {
				Class<? extends InfluenceCollector> icType = description.getInfluenceCollectorType();
				if (icType!=null) influenceCollector = icType.newInstance();
				else influenceCollector = new DefaultInfluenceCollector();
			}

			if (influenceSolver instanceof EnvironmentalActionCollector) {
				actionCollector = (EnvironmentalActionCollector<EnvironmentalAction1D5>)influenceSolver;
			}
			else {
				Class<? extends EnvironmentalActionCollector<?>> eacType = description.getEnvironmentalActionCollectorType();
				if (eacType!=null) actionCollector = (EnvironmentalActionCollector<EnvironmentalAction1D5>)eacType.newInstance();
				else actionCollector = new DefaultEnvironmentalActionCollector<EnvironmentalAction1D5>();
			}
			
			// Bind the influence-reaction pipeline components
			place.setInfluenceSolver(influenceSolver);
			place.setInfluenceCollector(influenceCollector);
			place.setEnvironmentalActionCollector(actionCollector);
			
			influenceSolver.setInfluenceCollector(influenceCollector);
			influenceSolver.setGround(place.getGround());
			//binded by the abstract implementation: influenceSolver.setDynamicEntityManager(entityManager);
			//binded by the abstract implementation: influenceSolver.setTimeManager(timeManager);
			influenceSolver.setActionCollector(actionCollector);
			
		}
		catch (Throwable e) {
			throw new Error(e);
		}
		
		RoadNetwork network = description.getWorldModel(RoadNetwork.class);
		assert(network!=null);
		
		place.init(
				network,
				description.getStaticEntities(),
				description.getMobileEntities());
		
		return place;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void destroyPlace(Place1D5<SB,MB> place) {
		place.destroy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createPortal(PortalDescription description, Place1D5<SB,MB> sourcePlace, Place1D5<SB,MB> targetPlace) {
		DefaultPortal<EnvironmentalAction1D5,Entity1D5<SB>,MobileEntity1D5<MB>> portal
		= new DefaultPortal<EnvironmentalAction1D5,Entity1D5<SB>,MobileEntity1D5<MB>>();
		
		sourcePlace.addPortal(portal, description.getPositionOnFirstPlace());
		targetPlace.addPortal(portal, description.getPositionOnSecondPlace());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Place1D5<SB,MB> getPlaceObject(MobileEntity1D5<MB> entity) {
		return getPlaceObject(entity.getPosition1D5());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Place1D5<SB,MB> getPlaceObject(Point1D5 position) {
		double currentHeight = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		Point2d p2d = position.toPoint2D();
		Place1D5<SB,MB> firstSelectedPlace = null;
		Place1D5<SB,MB> bestSelectedPlace = null;
		Ground ground;
		double h;
		
		for(Place1D5<SB,MB> place : getPlaceObjects()) {
			ground = place.getGround();
			if (ground==null) {
				bestSelectedPlace = place;
			}
			else {
				h = ground.getHeightAt(p2d.x, p2d.y);
				if (!Double.isNaN(h)) {
					if (firstSelectedPlace==null) firstSelectedPlace = place;
					if (h<=currentHeight && h>max) {
						max = h;
						bestSelectedPlace = place;
					}
				}
			}
		}
		
		return bestSelectedPlace==null ? firstSelectedPlace : bestSelectedPlace;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Place1D5<SB,MB> getPlaceObject(EuclidianPoint position) {
		Point1D5 p;
		if (position instanceof Point1D5) {
			p = (Point1D5)position;
		}
		else if (position instanceof Point1D) {
			p = ((Point1D)position).toPoint1D5();
		}
		else {
			return null;
		}
		return getPlaceObject(p);
	}

	@Override
	public PseudoHamelDimension getMathematicalDimension() {
		return PseudoHamelDimension.DIMENSION_1D5;
	}

}

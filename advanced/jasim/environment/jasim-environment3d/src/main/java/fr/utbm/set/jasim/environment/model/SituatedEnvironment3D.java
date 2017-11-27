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
import javax.vecmath.Point3d;

import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.jasim.environment.model.actions.EnvironmentalActionCollector;
import fr.utbm.set.jasim.environment.model.dynamics.DynamicsEngine;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.influencereaction.DefaultEnvironmentalActionCollector;
import fr.utbm.set.jasim.environment.model.influencereaction.DefaultInfluenceCollector;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction3D;
import fr.utbm.set.jasim.environment.model.influencereaction.KeepOnFloorImmediateInfluenceApplier3D;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.influences.InfluenceSolver;
import fr.utbm.set.jasim.environment.model.perception.algorithm.ParallelHybridPerceptionAlgorithm;
import fr.utbm.set.jasim.environment.model.perception.algorithm.SequentialHybridPerceptionAlgorithm;
import fr.utbm.set.jasim.environment.model.perception.algorithm.ParallelTopDownPerceptionAlgorithm;
import fr.utbm.set.jasim.environment.model.perception.algorithm.PerceptionAlgorithm;
import fr.utbm.set.jasim.environment.model.perception.algorithm.SequencialTopDownPerceptionAlgorithm;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree;
import fr.utbm.set.jasim.environment.model.place.DefaultPortal;
import fr.utbm.set.jasim.environment.model.place.Place3D;
import fr.utbm.set.jasim.environment.model.place.PlaceDescription;
import fr.utbm.set.jasim.environment.model.place.PortalDescription;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

/**
 * The default implementation of a situated environment for a MABS using
 * the Influence-Reaction model to manage environmental dynamic and agent's actions/perceptions.
 *
 * @param <SB> is the type of the bounds of the static entities.
 * @param <MB> is the type of the bounds of the mobile entities.
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @author Nicolas GAUD &lt;nicolas.gaud@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment3d
 */
public class SituatedEnvironment3D<SB extends CombinableBounds3D, MB extends CombinableBounds3D>
extends AbstractSituatedEnvironment<
				EnvironmentalAction3D,
				Entity3D<SB>,
				MobileEntity3D<MB>,
				Place3D<SB,MB>,
				Point3d> {

	/** Number of threads allowed to compute perceptions with a parallel generator.
	 */
	public static final int PARALLEL_PERCEPTION_GENERATOR_THREADS = 30;
	
	private final Class<MB> mobileEntityBoundType;
	
	/**
	 * @param boundType is the type of the bounds for mobile entities.
	 */
	public SituatedEnvironment3D(Class<MB> boundType) {
		this.mobileEntityBoundType = boundType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Place3D<SB,MB> createPlace(
			PlaceDescription<Entity3D<SB>, MobileEntity3D<MB>> description) {
		
		PerceptionAlgorithm<SB,MB> perceptionAlgorithm;
		switch(description.getPerceptionGeneratorType()) {
		case LOCAL_SEQUENTIAL_TOPDOWN:
			perceptionAlgorithm = new SequencialTopDownPerceptionAlgorithm<SB,MB>();
			break;
		case LOCAL_THREADED_TOPDOWN:
			perceptionAlgorithm = new ParallelTopDownPerceptionAlgorithm<SB, MB>(PARALLEL_PERCEPTION_GENERATOR_THREADS);
			break;
		case LOCAL_SEQUENTIAL_BOTTOMUP:
			perceptionAlgorithm = new SequentialHybridPerceptionAlgorithm<SB,MB>();
			break;
		case LOCAL_THREADED_BOTTOMUP:
			perceptionAlgorithm = new ParallelHybridPerceptionAlgorithm<SB, MB>(PARALLEL_PERCEPTION_GENERATOR_THREADS);
			break;
		default:
			throw new IllegalStateException();
		}
		
		Place3D<SB,MB> place = new Place3D<SB,MB>(
				this,
				description.getIdentifier(),
				this.mobileEntityBoundType,
				perceptionAlgorithm);
		place.setName(description.getName());
		place.setGround(description.getGround());
		place.setPerceptionGenerator(place.getWorldModelManager3D());

		try {

			Class<? extends DynamicsEngine> deType = description.getDynamicsEngineType();
			if (deType!=null) place.setDynamicsEngine(deType.newInstance());

			InfluenceSolver<EnvironmentalAction3D,MobileEntity3D<MB>> influenceSolver = null;
			InfluenceCollector influenceCollector = null;
			EnvironmentalActionCollector<EnvironmentalAction3D> actionCollector = null;
			
			Class<? extends InfluenceSolver<?,MobileEntity3D<MB>>> isType = description.getInfluenceSolverType();
			if (isType==null) {
				influenceSolver = new KeepOnFloorImmediateInfluenceApplier3D<MobileEntity3D<MB>>();
			}
			else {
				influenceSolver = (InfluenceSolver<EnvironmentalAction3D,MobileEntity3D<MB>>)isType.newInstance();
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
				actionCollector = (EnvironmentalActionCollector<EnvironmentalAction3D>)influenceSolver;
			}
			else {
				Class<? extends EnvironmentalActionCollector<?>> eacType = description.getEnvironmentalActionCollectorType();
				if (eacType!=null) actionCollector = (EnvironmentalActionCollector<EnvironmentalAction3D>)eacType.newInstance();
				else actionCollector = new DefaultEnvironmentalActionCollector<EnvironmentalAction3D>(); 
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
		
		place.init(
				description.getWorldModel(DynamicPerceptionTree.class),
				description.getWorldModel(StaticPerceptionTree.class));
		
		return place;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void destroyPlace(Place3D<SB,MB> place) {
		place.destroy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createPortal(PortalDescription description, Place3D<SB,MB> sourcePlace, Place3D<SB,MB> targetPlace) {
		DefaultPortal<EnvironmentalAction3D,Entity3D<SB>,MobileEntity3D<MB>> portal
		= new DefaultPortal<EnvironmentalAction3D,Entity3D<SB>,MobileEntity3D<MB>>();
		
		sourcePlace.addPortal(portal, description.getPositionOnFirstPlace());
		targetPlace.addPortal(portal, description.getPositionOnSecondPlace());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Place3D<SB, MB> getPlaceObject(MobileEntity3D<MB> entity) {
		return getPlaceObject((Point3d)entity.getPosition3D());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Place3D<SB,MB> getPlaceObject(Point3d position) {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		double currentHeight = cs.height(position);
		double max = Double.NEGATIVE_INFINITY;
		Point2d p2d = cs.toCoordinateSystem2D(position);
		Place3D<SB,MB> firstSelectedPlace = null;
		Place3D<SB,MB> bestSelectedPlace = null;
		Ground ground;
		double h;
		
		for(Place3D<SB,MB> place : getPlaceObjects()) {
			ground = place.getGround();
			if (ground!=null) {
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
	public Place3D<SB,MB> getPlaceObject(EuclidianPoint position) {
		Point3d p;
		switch(position.getMathematicalDimension().mathematicalDimension) {
		case 2:
			p = new Point3d(
					position.getCoordinate(0),
					position.getCoordinate(1),
					Double.POSITIVE_INFINITY);
			break;
		case 3:
			p = new Point3d(
					position.getCoordinate(0),
					position.getCoordinate(1),
					position.getCoordinate(2));
			break;
		default:
			return null;
		}
		return getPlaceObject(p);
	}

	@Override
	public PseudoHamelDimension getMathematicalDimension() {
		return PseudoHamelDimension.DIMENSION_3D;
	}

}

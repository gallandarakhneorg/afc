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
package fr.utbm.set.jasim.environment.model.perception.algorithm;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.InterestFilter;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator;
import fr.utbm.set.jasim.environment.model.ground.PerceivableGround;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum3D;
import fr.utbm.set.jasim.environment.model.perception.percepts.DefaultGroundPerception;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D1D;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D1D5;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D2D;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D3D;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.cullers.FrustumCuller;
import fr.utbm.set.jasim.environment.model.perception.tree.manipulators.TreeManipulator3D;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

/**
 * Abstract implementation of a perception algorithm which
 * is treating the agents and their frustums sequentially.
 * 
 * @param <SB> is the bounds of the static entities.
 * @param <MB> is the bounds of the mobile entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSequentialPerceptionAlgorithm<SB extends CombinableBounds3D, MB extends CombinableBounds3D>
implements PerceptionAlgorithm<SB,MB> {

	/** {@inheritDoc}
	 */
	@Override
	public void computePerceptions(
			Map<AgentBody3D<?,?,MB>, PerceptionList3D<SB,MB,?>> population, 
			PerceivableGround ground,
			StaticPerceptionTree<CombinableBounds3D, SB, Entity3D<SB>, ?,?> staticPerceptionTree,
			DynamicPerceptionTree<CombinableBounds3D, MB, MobileEntity3D<MB>,
			  ?,?,?, ? extends TreeManipulator3D<MB,?,?>> dynamicPerceptionTree) {
		
		AgentBody3D<?,?,MB> body;
		PseudoHamelDimension dimension;
		PerceptionList3D<SB,MB,?> list;
		Collection<? extends Frustum3D> frustums;
		PhysicalPerceptionAlterator physicFilter;
		InterestFilter interestFilter;
		CullingResult<? extends Entity3D<? extends CombinableBounds3D>> current;
		Entity3D<? extends CombinableBounds3D> perceivedEntity;

		for(Entry<AgentBody3D<?,?,MB>,PerceptionList3D<SB,MB,?>>  entry : population.entrySet()) {
			
			body = entry.getKey();
			dimension = body.getPreferredMathematicalDimension();

			list = null;
			switch(dimension) {
			case DIMENSION_1D:
				list = new PerceptionList3D1D<SB,MB>();
				break;
			case DIMENSION_1D5:
				list = new PerceptionList3D1D5<SB,MB>();
				break;
			case DIMENSION_2D:
				list = new PerceptionList3D2D<SB,MB>();
				break;
			case DIMENSION_2D5:
			case DIMENSION_3D:
				list = new PerceptionList3D3D<SB,MB>();
				break;
			default:
			}
			
			if (list!=null) {
				frustums = body.getFrustums();			
				physicFilter = body.getPerceptionFilter();
				interestFilter = body.getInterestFilter();
				
				//Iterate of the various frustums associated to the current body
				Iterator<? extends Frustum3D> frustumIterator = frustums.iterator();
				Frustum3D frustum;
				while (frustumIterator.hasNext()) {			
					frustum = frustumIterator.next();
					//Perception of dynamic entities
					if (dynamicPerceptionTree!=null) {
						FrustumCuller<CombinableBounds3D,MB,MobileEntity3D<MB>,?,?> culler = 
							createDynamicCuller(dynamicPerceptionTree, body, frustum, physicFilter, interestFilter);
						while (culler.hasNext()) {
							current = culler.next();
							if (current.isVisible()) {
								perceivedEntity = current.getCulledObject();
								// ensure that the perceived object is not the perceiver
								if (!perceivedEntity.getIdentifier().equals(body.getIdentifier())) {
									list.addDynamicPerception(current);
								}
							}
						}
					}
					
					//Perception of static entities
					if (staticPerceptionTree!=null) {
						FrustumCuller<CombinableBounds3D,SB,Entity3D<SB>,?,?> culler = 
							createStaticCuller(staticPerceptionTree, body, frustum, physicFilter, interestFilter);
						while (culler.hasNext()) {
							current = culler.next();
							if (current.isVisible()) {
								list.addStaticPerception(current);
							}
						}
					}
					
				} // End of frustum iteration
				

				// Save ground perception
				if (ground!=null) {
					Point2d position = body.getPosition2D();
					list.setGroundPerception(new DefaultGroundPerception(ground,position.x,position.y));
				}
				
			}

			entry.setValue(list);
		}
	}
	
	/**
	 * Create a frustum culler for the given static tree according to the given frustum and the filters.
	 * 
	 * @param tree is the tree to traverse.
	 * @param entity is the perceiver.
	 * @param frustum is the frustum to use.
	 * @param physicFilter is the physical attribute filter.
	 * @param interestFilter is the interest filter.
	 * @return the frustum culler.
	 */
	protected abstract FrustumCuller<CombinableBounds3D,SB,Entity3D<SB>,?,?> createStaticCuller(
			StaticPerceptionTree<CombinableBounds3D, SB, Entity3D<SB>,?,?> tree,
			AgentBody3D<?,?,MB> entity,
			Frustum3D frustum,
			PhysicalPerceptionAlterator physicFilter,
			InterestFilter interestFilter);

	/**
	 * Create a frustum culler for the given static tree according to the given frustum and the filters.
	 *
	 * @param tree is the tree to traverse.
	 * @param entity is the perceiver.
	 * @param frustum is the frustum to use.
	 * @param physicFilter is the physical attribute filter.
	 * @param interestFilter is the interest filter.
	 * @return the frustum culler.
	 */
	protected abstract FrustumCuller<CombinableBounds3D,MB,MobileEntity3D<MB>,?,?> createDynamicCuller(
			DynamicPerceptionTree<CombinableBounds3D, MB, MobileEntity3D<MB>,
			  ?,?,?, ? extends TreeManipulator3D<MB,?,?>> tree,
			AgentBody3D<?,?,MB> entity,
			Frustum3D frustum,
			PhysicalPerceptionAlterator physicFilter,
			InterestFilter interestFilter);

}


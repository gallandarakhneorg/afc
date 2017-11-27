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

import java.util.Map;

import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D;
import fr.utbm.set.jasim.environment.model.ground.PerceivableGround;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.manipulators.TreeManipulator3D;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGeneratorType;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

/**
 * This interface is implemented by the perception algorithms
 * which may be used to compute the perceptions of agents.
 * 
 * @param <SB> is the bounds of the static entities.
 * @param <MB> is the bounds of the mobile entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PerceptionAlgorithm<SB extends CombinableBounds3D, MB extends CombinableBounds3D> {

	/** Compute the perceptions for the given agent bodies and fill
	 * the given perception lists with the perceived objects.
	 * 
	 * @param population is mapping the perceiver to their perception lists.
	 * @param ground is the ground which may be perceived by the agents.
	 * @param staticPerceptionTree is the tree data-structure which is containing the static objects.
	 * @param dynamicPerceptionTree is the tree data-structure which is containing the mobile objects.
	 */
	public void computePerceptions(
			Map<AgentBody3D<?,?,MB>, PerceptionList3D<SB,MB,?>> population, 
			PerceivableGround ground,
			StaticPerceptionTree<CombinableBounds3D, SB, Entity3D<SB>, ?,?> staticPerceptionTree,
			DynamicPerceptionTree<CombinableBounds3D, MB, MobileEntity3D<MB>,
			  ?,?,?, ? extends TreeManipulator3D<MB,?,?>> dynamicPerceptionTree);

	/**
	 * Replies the type of perception generator which is corresponding to
	 * this perception algorithm.
	 * 
	 * @return type of perception generator which is corresponding to this algorithm.
	 */
	public PerceptionGeneratorType getPerceptionGeneratorType();

}


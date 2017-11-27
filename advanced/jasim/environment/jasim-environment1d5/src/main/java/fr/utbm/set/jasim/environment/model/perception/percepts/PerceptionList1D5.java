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
package fr.utbm.set.jasim.environment.model.perception.percepts;

import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult1D5;
import fr.utbm.set.jasim.environment.model.world.Entity1D5;
import fr.utbm.set.jasim.environment.model.world.MobileEntity1D5;
import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;

/**
 * This class describes a set of perceptions and their respective classifications
 * for a specific frustum.
 * 
 * @param <SB> is the bounds of the static entities.
 * @param <MB> is the bounds of the mobile entities.
 * @param <P> is the type of the replied perceptions.
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public abstract class PerceptionList1D5<SB extends CombinableBounds1D5<RoadSegment>,
									    MB extends CombinableBounds1D5<RoadSegment>,
									    P extends Perception>
extends PerceptionList<Entity1D5<? extends CombinableBounds1D5<RoadSegment>>,
					   Entity1D5<SB>,
					   MobileEntity1D5<MB>,
					   CullingResult1D5<Entity1D5<? extends CombinableBounds1D5<RoadSegment>>>,
					   P> {
	
	/**
	 */
	public PerceptionList1D5() {
		//
	}

}

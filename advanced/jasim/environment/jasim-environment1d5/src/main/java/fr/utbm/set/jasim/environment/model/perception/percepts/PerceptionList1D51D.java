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
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception1D;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult1D5;
import fr.utbm.set.jasim.environment.model.world.Entity1D5;
import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;

/**
 * This class describes a set of perceptions and their respective classifications
 * for a specific frustum.
 * 
 * @param <SB> is the bounds of the static entities.
 * @param <MB> is the bounds of the mobile entities.
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class PerceptionList1D51D<SB extends CombinableBounds1D5<RoadSegment>, MB extends CombinableBounds1D5<RoadSegment>>
extends PerceptionList1D5<SB,MB,Perception1D> {
	
	/**
	 */
	public PerceptionList1D51D() {
		//
	}

	@Override
	public boolean isSupportedPerceptionType(Class<? extends Perception> type) {
		return type.isAssignableFrom(Perception1D.class);
	}

	@Override
	protected Perception1D createPerception(
			CullingResult1D5<Entity1D5<? extends CombinableBounds1D5<RoadSegment>>> result) {
		return new Perception1D51D(
				result.getFrustum(),
				result.getIntersectionType(),
				result.getCulledObject(),
				result.getPerceivedObjectCurvilineDistance(),
				result.isInFront(),
				result.isSameDirection(),
				result.getPathToPerceivedObject());
	}

}

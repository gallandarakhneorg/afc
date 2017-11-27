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

import java.util.UUID;

import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceivable;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/**
 * This class describes a set of perceptions and their respective classifications
 * for a specific frustum.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PerceptionListStub extends PerceptionList<WorldEntity<?>,
													   WorldEntity<?>, 
													   MobileEntity<?>,
													   CullingResult<WorldEntity<?>>,
													   Perception> {

	/**
	 */
	public PerceptionListStub() {
		//
	}

	@Override
	public boolean isSupportedPerceptionType(Class<? extends Perception> type) {
		return true;
	}

	@Override
	protected Perception createPerception(CullingResult<WorldEntity<?>> result) {
		return new PerceptionStub(result);
	}
	
	private class PerceptionStub implements Perception {
		
		private final CullingResult<?> result;
		
		/**
		 * @param result
		 */
		public PerceptionStub(CullingResult<?> result) {
			this.result = result;
		}

		@Override
		public IntersectionType getClassification() {
			return this.result.getIntersectionType();
		}

		@Override
		public UUID getFrustum() {
			return UUID.randomUUID();
		}

		@Override
		public Perceivable getPerceivedObject() {
			return this.result.getCulledObject();
		}
		
	}
	
}
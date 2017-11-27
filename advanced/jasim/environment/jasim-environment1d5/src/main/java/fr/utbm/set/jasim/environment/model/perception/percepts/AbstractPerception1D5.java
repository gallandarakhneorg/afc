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
import fr.utbm.set.jasim.environment.model.world.Entity1D5;

/** This class describes a perception inside a situated environment.
 *
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
abstract class AbstractPerception1D5 implements Perception {

	/** Frustum.
	 */
	final UUID frustum;
	
	/** Classification.
	 */
	final IntersectionType classification;
	
	/**
	 * Perceived object.
	 */
	final Entity1D5<?> perceivedObject;
	
	/**
	 * @param frustum
	 * @param type
	 * @param perceivedObject
	 */
	public AbstractPerception1D5(UUID frustum, IntersectionType type, Entity1D5<?> perceivedObject) {
		this.frustum = frustum;
		this.classification = type;
		this.perceivedObject = perceivedObject;
	}
	
	@Override
	public IntersectionType getClassification() {
		return this.classification;
	}

	@Override
	public UUID getFrustum() {
		return this.frustum;
	}
	
	@Override
	public Perceivable getPerceivedObject() {
		return this.perceivedObject;
	}

}
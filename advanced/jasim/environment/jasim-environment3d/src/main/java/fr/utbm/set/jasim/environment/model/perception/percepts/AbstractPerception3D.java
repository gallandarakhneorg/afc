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
import fr.utbm.set.jasim.environment.model.world.Entity3D;

/** This class describes a perception inside a situated environment.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
abstract class AbstractPerception3D implements Perception {

	/** Frustum.
	 */
	final UUID frustum;
	
	/** Classification.
	 */
	final IntersectionType classification;
	
	/**
	 * Perceived object.
	 */
	final Entity3D<?> perceivedObject;
	
	/**
	 * @param frustum
	 * @param type
	 * @param perceivedObject
	 */
	public AbstractPerception3D(UUID frustum, IntersectionType type, Entity3D<?> perceivedObject) {
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
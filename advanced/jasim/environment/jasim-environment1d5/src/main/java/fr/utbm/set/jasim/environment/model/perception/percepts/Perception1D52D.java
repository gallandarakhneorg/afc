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

import javax.vecmath.Point2d;

import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceivable;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception2D;
import fr.utbm.set.jasim.environment.model.world.Entity1D5;
import fr.utbm.set.geom.intersection.IntersectionType;

/** This class describes a 2D mapping of a 1.5D perception inside a situated environment.
 *
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
class Perception1D52D extends AbstractPerception1D5 implements Perception2D {

	/**
	 * @param frustum
	 * @param type
	 * @param perceivedObject
	 */
	public Perception1D52D(UUID frustum, IntersectionType type, Entity1D5<?> perceivedObject) {
		super(frustum, type, perceivedObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point2d getPerceivedObjectPosition() {
		Perceivable p = getPerceivedObject();
		return p.getPosition2D();
	}
	
}
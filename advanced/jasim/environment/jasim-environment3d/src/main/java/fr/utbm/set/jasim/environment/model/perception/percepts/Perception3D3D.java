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

import javax.vecmath.Point3d;

import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception3D;
import fr.utbm.set.jasim.environment.model.world.Entity3D;

/** This class describes a 3D mapping of a 3D perception inside a situated environment.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class Perception3D3D extends AbstractPerception3D implements Perception3D {

	/**
	 * @param frustum
	 * @param type
	 * @param perceivedObject
	 */
	public Perception3D3D(UUID frustum, IntersectionType type, Entity3D<?> perceivedObject) {
		super(frustum, type, perceivedObject);
	}
	
	@Override
	public Point3d getPerceivedObjectPosition() {
		return this.perceivedObject.getPosition3D();
	}
	
}
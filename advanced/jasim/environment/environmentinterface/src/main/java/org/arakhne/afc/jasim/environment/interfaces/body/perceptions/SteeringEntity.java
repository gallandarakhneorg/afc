/*
 * 
 * $Id$
 * 
 * Copyright (c) 2006-09, 2012, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package org.arakhne.afc.jasim.environment.interfaces.body.perceptions;

import fr.utbm.set.physics.kinematic.angular.AngularAccelerationKinematic;
import fr.utbm.set.physics.kinematic.linear.LinearAccelerationKinematic;

/**
 * This interface describes an object which has a
 * steering behaviour.
 * <p>
 * Steering behaviours extend simple kinematic movemenent algorithms
 * by adding velocity and rotation to position and orientation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface SteeringEntity extends KinematicEntity, LinearAccelerationKinematic, AngularAccelerationKinematic {
	//
}

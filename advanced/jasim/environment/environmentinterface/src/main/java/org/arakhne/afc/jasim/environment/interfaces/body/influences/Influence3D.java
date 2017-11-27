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
package org.arakhne.afc.jasim.environment.interfaces.body.influences;

import fr.utbm.set.geom.transform.Transform3D;

import org.arakhne.afc.jasim.environment.time.Clock;


/**
 * This class describes an influence inside a 3D situated environment.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Influence3D extends Influence {

	/**
	 * @return the 3D transformation contained in this 3D influence and expressed according to the default
	 * velocity and rotation units.
	 */
	public Transform3D getTransformation();

	/**
	 * @param time is the current time simulation.
	 * @return the 3D transformation contained in this 3D influence and expressed according to the given clock.
	 */
	public Transform3D getTransformation(Clock time);

}

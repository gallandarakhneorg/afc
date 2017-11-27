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

import fr.utbm.set.geom.transform.Transform2D;

import org.arakhne.afc.jasim.environment.time.Clock;


/**
 * This class describes an influence inside a 2D situated environment.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Influence2D extends Influence {

	/**
	 * @return the 2D transformation contained in this 2D influence
	 */
	public Transform2D getTransformation();

	/**
	 * @param time is the current time simulation.
	 * @return the 2D transformation contained in this 2D influence and expressed according to the given clock.
	 */
	public Transform2D getTransformation(Clock time);

}

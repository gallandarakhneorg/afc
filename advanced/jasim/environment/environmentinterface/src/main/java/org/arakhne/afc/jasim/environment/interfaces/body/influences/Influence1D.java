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

import fr.utbm.set.geom.object.Segment1D;
import fr.utbm.set.geom.transform.Transform1D;

import org.arakhne.afc.jasim.environment.time.Clock;


/**
 *  This class describes an influence inside a 1D situated environment.
 *  
 * @param <S> is the type of segment which is supported by this influence.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Influence1D<S extends Segment1D> extends Influence {

	/**
	 * @return the 1D transformation contained in this 1D influence
	 */
	public Transform1D<S> getTransformation();

	/**
	 * @param time is the current time simulation.
	 * @return the 1D transformation contained in this 1D influence and expressed according to the given clock.
	 */
	public Transform1D<S> getTransformation(Clock time);

}

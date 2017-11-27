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
package fr.utbm.set.jasim.environment.model.influencereaction;

import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;

/** This class describes an action inside a situated environment.
 * <p>
 * <em>The embedded transformation is local.</em>
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface EnvironmentalAction3D extends EnvironmentalAction {
	
	/**
	 * @return the 3D transformation contained in this 3D action
	 */
	public Transform3D getTransformation();

}
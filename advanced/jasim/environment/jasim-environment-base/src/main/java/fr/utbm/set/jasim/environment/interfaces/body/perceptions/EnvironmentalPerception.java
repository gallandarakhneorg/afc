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
package fr.utbm.set.jasim.environment.interfaces.body.perceptions;

import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/** This class describes a perception inside a situated environment.
 *
 * @param <SE> is the type of the perceived entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface EnvironmentalPerception<SE extends WorldEntity<?>> extends Perception {
	
	/**
	 * Replies the elements inside the perception frustum of an agent.
	 * 
	 * @return the perceived object.
	 */
	public SE getEnvironmentalObject();	

}
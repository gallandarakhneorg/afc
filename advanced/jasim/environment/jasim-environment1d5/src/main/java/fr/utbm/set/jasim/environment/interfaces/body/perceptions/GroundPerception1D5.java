/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
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

import fr.utbm.set.gis.road.SubRoadNetwork;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.GroundPerception;

/** This class describes a perception of the gorund inside a situated 1.5D environment.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface GroundPerception1D5 extends GroundPerception {
	
	/** Replies the perceived road network.
	 * 
	 * @return the perceived road network.
	 */
	public SubRoadNetwork getRoads();

}
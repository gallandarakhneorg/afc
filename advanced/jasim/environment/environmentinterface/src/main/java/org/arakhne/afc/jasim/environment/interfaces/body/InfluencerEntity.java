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
package org.arakhne.afc.jasim.environment.interfaces.body;

import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influence;
import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencer;

/** This interface representes an object in a 1D, 2D or 3D space
 * that has the property to be mobile and that could be directly influenced
 * by an action.
 *
 * @param <INF> is the type of the influence associated to this entity.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface InfluencerEntity<INF extends Influence> extends Influencer {
	
	/** Send an influence to the environment.
	 * 
	 * @param influence is the influence to send.
	 */
	public void influence(INF... influence);	
	
}
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


/** This interface describes an influencer object.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Influencer {

	/** Store the status of the last influence which was tried to be applied in the environment.
	 * 
	 * @param status is the applicaton status.
	 */
	public void setLastInfluenceStatus(InfluenceApplicationStatus status);
	
	/** Store the status of the last influence which was tried to be applied in the environment.
	 * 
	 * @param status is the applicaton status.
	 * @param e is the cause of the status.
	 */
	public void setLastInfluenceStatus(InfluenceApplicationStatus status, Throwable e);

}
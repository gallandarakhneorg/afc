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
package org.arakhne.afc.jasim.environment.interfaces.internalevents;

import java.io.Serializable;

import org.arakhne.afc.jasim.environment.time.Clock;

/** This class describes an action inside a situated environment.
 * <p>
 * <em>The embedded transformation is local.</em>
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface EnvironmentalAction extends Serializable {
	
	/**
	 * Replies the elementon which the action must occur.
	 * 
	 * @param <ET> is the type of the entity to reply.
	 * @param type is the desired type for the replied entity. 
	 * @return the acted object or <code>null</code> if it has not the given type.
	 */
	public <ET> ET getEnvironmentalObject(Class<ET> type);
	
	/** Replies the clock at which the action occured.
	 * 
	 * @return the clock, never <code>null</code>
	 */
	public Clock getClock();

}